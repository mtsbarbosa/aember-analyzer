#!/usr/bin/env python3
"""Card fidelity inventory for a set (default: Core / CotA).

One row per card: how it is implemented today (generated DSL, DSL override, manual
script or nothing), its declared fidelity level, blocking text patterns, the keyteki
features its reference implementation uses, and its impact on the deck pool.

Usage:
  python3 tools/fidelity-inventory.py [--pack PATH] [--keyteki-cards DIR]
      [--server URL --pool KEY] [--out docs/fidelity/core-inventory.csv]
      [--decks-dir DIR]  # alternatively: directory of deck JSON files (cards[].cardId/count)
      [--check-drift]    # Phase 4 spec drift check (CI): exit 1 if keyteki card files
                         # changed / appeared / disappeared relative to the committed CSV

Pool impact = number of pool decks that contain the card (needs the running server, or
--decks-dir). Priority = pool_impact * (1 + cluster_size / 100), where cluster_size is the
size of the largest blocking-pattern cluster the card belongs to (work on big clusters first).

Registration precedence mirrors infrastructure/config/CardLibraryConfig.kt:
generated DSL < DSL overrides (CoreCards.kt lists) < manual CardScript objects.
"""
import argparse
import collections
import csv
import hashlib
import json
import os
import re
import sys
import urllib.request

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
CARDS_DIR = os.path.join(ROOT, "src/main/kotlin/com/aember/analyzer/domain/engine/cards")
CONFIG = os.path.join(ROOT, "src/main/kotlin/com/aember/analyzer/infrastructure/config/CardLibraryConfig.kt")

# Blocking text patterns (a card may match several).
PATTERNS = [
    ("static-aura", r"^(each|your|enemy|friendly|creatures|non-|while|this creature (cannot|gets|deals|is)|cards)|\bwhile\b"),
    ("conditional-if", r"\bif\b"),
    ("for-each", r"for each"),
    ("may", r"\bmay\b"),
    ("until-next-turn", r"until|next turn|opponent.s next"),
    ("name-house", r"choose a house|name a house"),
    ("upgrade", r"upgrade"),
    ("capture", r"capture"),
    ("armor-ward", r"armor|ward"),
    ("destroyed-trigger", r"destroyed:"),
    ("play-trigger", r"play:"),
    ("fight-reap-trigger", r"fight:|reap:"),
    ("after-before", r"\bafter\b|\bbefore\b"),
    ("instead", r"instead"),
    ("discard", r"discard"),
    ("power-counters", r"power counter|\+\d power"),
    ("omni-action", r"omni:|action:"),
]
KEYWORD_ONLY = re.compile(r"^((alpha|omega|elusive|skirmish|taunt|hazardous\s*\d*|assault\s*\d*|deploy|poison|splash-attack\s*\d*|"
                          r"\(.*?\))[\.\s\x0b,]*)*$", re.I | re.S)


def catalog_id(pack_id):
    """Mirror of JsonCardCatalogLoader.sanitizeId: the engine/catalog card id."""
    return re.sub(r"[^a-z0-9\-]", "", pack_id.lower())


def load_cards(pack):
    data = json.load(open(pack))
    return data["cards"] if isinstance(data, dict) else data


def parse_dsl_levels(path):
    """card id -> (level, notes, no_effect) for `card("id") { ... fidelity(FidelityLevel.X, "...") }` blocks."""
    src = open(path).read()
    out = {}
    for chunk in re.split(r'\n\s*card\("', src)[1:]:
        cid = chunk.split('"', 1)[0]
        m = re.search(r'fidelity\(FidelityLevel\.(\w+),\s*"([^"]*)"', chunk)
        if m:
            out[cid] = (m.group(1), m.group(2), "NoEffect" in chunk)
    return out


def parse_manual(core_cards_kt, config_kt):
    """card id -> (level, notes) for manual CardScript objects registered in CardLibraryConfig."""
    objs = {}
    src = open(core_cards_kt).read()
    for name, body in re.findall(r"object (\w+) : CardScript \{(.*?)(?=\nobject |\nval |\Z)", src, re.S):
        m = re.search(r"level = FidelityLevel\.(\w+)(?:,\s*notes = \"([^\"]*)\")?", body)
        objs[name] = (m.group(1), m.group(2) or "") if m else ("UNKNOWN", "")
    return {cid: objs.get(obj, ("UNKNOWN", "")) for cid, obj in re.findall(r'library\.register\("([^"]+)", (\w+)\)', open(config_kt).read())}


def parse_override_ids(core_cards_kt):
    src = open(core_cards_kt).read()
    out = {}
    for chunk in re.split(r'\n\s*card\("', src)[1:]:
        cid = chunk.split('"', 1)[0]
        m = re.search(r'fidelity\(FidelityLevel\.(\w+),\s*"([^"]*)"', chunk)
        out[cid] = (m.group(1), m.group(2)) if m else ("UNKNOWN", "")
    return out


def keyteki_features(cards_dir):
    """card id -> dict of reference-implementation features + file hash (spec drift check)."""
    out = {}
    if not cards_dir or not os.path.isdir(cards_dir):
        return out
    for fn in sorted(os.listdir(cards_dir)):
        if not fn.endswith(".js"):
            continue
        src = open(os.path.join(cards_dir, fn), encoding="utf8").read()
        m = re.search(r"\.id\s*=\s*'([^']+)'", src)
        if not m:
            continue
        feats = [name for name, pat in [
            ("persistentEffect", r"persistentEffect"), ("interrupt", r"interrupt\("), ("reaction", r"reaction\("),
            ("play", r"\.play\("), ("reap", r"\.reap\("), ("fight", r"\.fight\("), ("destroyed", r"destroyed\("),
            ("action", r"\.action\("), ("omni", r"omni\("), ("target", r"target:"), ("gainAbility", r"gainAbility"),
            ("optional", r"optional:\s*true"), ("condition", r"condition:"),
        ] if re.search(pat, src)]
        out[m.group(1)] = {"features": feats, "sha": hashlib.sha1(src.encode()).hexdigest()[:10]}
    return out


def pool_impact(server, pool, decks_dir):
    """card id -> number of pool decks containing it."""
    counts = collections.Counter()
    decks = []
    if decks_dir:
        for fn in os.listdir(decks_dir):
            if fn.endswith(".json"):
                decks.append(json.load(open(os.path.join(decks_dir, fn))))
    elif server and pool:
        pool_data = json.load(urllib.request.urlopen(f"{server}/api/pools/{pool}"))
        for uuid in pool_data["deckUuids"]:
            decks.append(json.load(urllib.request.urlopen(f"{server}/api/decks/{uuid}")))
    for deck in decks:
        for cid in {c["cardId"] for c in deck.get("cards", [])}:
            counts[cid] += 1
    return counts, len(decks)


def text_is_vanilla(text):
    t = (text or "").strip().lower()
    return not t or bool(KEYWORD_ONLY.match(t))


def verified_ids():
    """Card ids with a passing parity test (source of truth: main resources)."""
    path = os.path.join(ROOT, "src/main/resources/fidelity/verified-cards.txt")
    if not os.path.exists(path):
        return set()
    out = set()
    for line in open(path, encoding="utf8"):
        line = line.split("#")[0].strip()
        if line:
            out.add(line)
    return out


def check_drift(csv_path, cards_dir):
    """Compare keyteki card-file hashes in the committed CSV with the current sources.

    Fails (returns non-zero) when a keyteki card file changed, appeared or disappeared
    since the CSV was generated — those cards must be re-reviewed before the next batch.
    """
    kt = keyteki_features(cards_dir)
    stored = {}
    with open(csv_path, newline="", encoding="utf8") as f:
        for row in csv.DictReader(f):
            if row["keyteki_sha"]:
                stored[row["id"]] = row["keyteki_sha"]
    current = {catalog_id(kid): v["sha"] for kid, v in kt.items()}
    changed = sorted(cid for cid in stored if cid in current and current[cid] != stored[cid])
    appeared = sorted(set(current) - set(stored))
    disappeared = sorted(set(stored) - set(current))
    if changed:
        print(f"DRIFT: {len(changed)} keyteki card file(s) changed since the inventory was generated:")
        for cid in changed:
            print(f"  {cid}: {stored[cid]} -> {current[cid]}")
    if appeared:
        print(f"DRIFT: {len(appeared)} new keyteki card file(s) not in the inventory:")
        for cid in appeared:
            print(f"  {cid}")
    if disappeared:
        print(f"DRIFT: {len(disappeared)} inventoried card file(s) missing from keyteki now:")
        for cid in disappeared:
            print(f"  {cid}")
    if changed or appeared or disappeared:
        print("Re-run the inventory and re-review the affected cards, then commit the new CSV.")
        return 1
    print(f"Spec drift check OK: {len(stored)} keyteki card files unchanged.")
    return 0


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("--pack", default=os.path.expanduser("~/work/keyteki/master-vault-data/packs/CotA.json"))
    ap.add_argument("--keyteki-cards", default=os.path.expanduser("~/work/keyteki/server/game/cards/01-Core"))
    ap.add_argument("--server", default=None, help="e.g. http://localhost:8080 (pool impact)")
    ap.add_argument("--pool", default="cota")
    ap.add_argument("--decks-dir", default=None)
    ap.add_argument("--out", default=os.path.join(ROOT, "docs/fidelity/core-inventory.csv"))
    ap.add_argument("--check-drift", action="store_true",
                    help="compare keyteki card-file hashes against the committed CSV; exit 1 on drift")
    args = ap.parse_args()

    if args.check_drift:
        return check_drift(args.out, args.keyteki_cards)

    cards = load_cards(args.pack)
    dsl = parse_dsl_levels(os.path.join(CARDS_DIR, "CoreSetDslCards.kt"))
    core_kt = os.path.join(CARDS_DIR, "CoreCards.kt")
    overrides = parse_override_ids(core_kt)
    manual = parse_manual(core_kt, CONFIG)
    kt = keyteki_features(args.keyteki_cards)
    verified = verified_ids()
    impact, n_decks = pool_impact(args.server, args.pool, args.decks_dir)

    rows = []
    for c in cards:
        kid = c["id"]  # keyteki id
        cid = catalog_id(kid)  # engine id
        text = (c.get("text") or "").replace("\x0b", " ").strip()
        low = text.lower()
        if cid in manual:
            source, (level, notes) = "manual", manual[cid]
            no_effect = False
        elif cid in overrides:
            source, (level, notes) = "dsl-override", overrides[cid]
            no_effect = False
        elif cid in dsl:
            source = "dsl"
            level, notes, no_effect = dsl[cid]
        else:
            source, level, notes, no_effect = "none", ("VANILLA" if text_is_vanilla(text) else "MISSING"), "", False
        tags = [name for name, pat in PATTERNS if re.search(pat, low)] if level != "FULL" else []
        k = kt.get(kid, {})
        rows.append({
            "id": cid, "name": c.get("name", cid), "house": c.get("house", ""), "type": c.get("type", ""),
            "source": source, "level": level, "verified": int(cid in verified), "no_effect": int(no_effect), "tags": ";".join(tags),
            "keyteki_features": ";".join(k.get("features", [])), "keyteki_sha": k.get("sha", ""),
            "pool_decks": impact.get(cid, 0), "text": text.replace("\n", " "),
        })

    cluster = collections.Counter(t for r in rows for t in r["tags"].split(";") if t)
    for r in rows:
        size = max([cluster[t] for t in r["tags"].split(";") if t] or [0])
        r["cluster_size"] = size
        r["priority"] = round(r["pool_decks"] * (1 + size / 100), 2) if r["level"] not in ("FULL", "VANILLA") else 0

    rows.sort(key=lambda r: (-r["priority"], r["id"]))
    os.makedirs(os.path.dirname(args.out), exist_ok=True)
    fields = ["id", "name", "house", "type", "source", "level", "verified", "no_effect", "tags", "cluster_size",
              "pool_decks", "priority", "keyteki_features", "keyteki_sha", "text"]
    with open(args.out, "w", newline="", encoding="utf8") as f:
        w = csv.DictWriter(f, fieldnames=fields)
        w.writeheader()
        w.writerows(rows)

    levels = collections.Counter(r["level"] for r in rows)
    print(f"{len(rows)} cards -> {args.out}")
    print("by level:", dict(levels))
    verified_full = sum(1 for r in rows if r["level"] == "FULL" and r["verified"])
    declared_full = sum(1 for r in rows if r["level"] == "FULL")
    print(f"verified: {verified_full}/{declared_full} declared-FULL cards have parity tests")
    print("by source:", dict(collections.Counter(r["source"] for r in rows)))
    print("no-effect cards with ability text:", sum(r["no_effect"] for r in rows))
    print("blocking clusters:", cluster.most_common())
    if n_decks:
        print(f"pool impact computed over {n_decks} decks; top unfinished cards:")
        for r in rows[:15]:
            print(f"  {r['priority']:6} {r['pool_decks']:3} decks  {r['level']:12} {r['name']}  [{r['tags']}]")
    else:
        print("(no pool impact: pass --server/--pool or --decks-dir)")
    missing = [r["id"] for r in rows if r["level"] == "MISSING"]
    if missing:
        print("MISSING (ability text, no implementation):", missing)


if __name__ == "__main__":
    sys.exit(main())
