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
                         # (or pack/overrides/spec sources) changed relative to the
                         # committed CSV / keyteki-sources.sha

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
import pathlib
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
KEYWORD_ONLY_PATTERN = os.path.join(ROOT, "src/test/resources/fidelity/keyword-only.pattern")

# Sidecar with hashes of keyteki sources the CSV cannot carry per-card:
# the pack JSON, master-vault overrides.json and the reference .spec.js files.
SOURCES_SHA = os.path.join(ROOT, "docs/fidelity/keyteki-sources.sha")


def keyword_only_regex():
    """Canonical pattern shared with CoverageRatchetTest (fidelity/keyword-only.pattern)."""
    lines = [l.strip() for l in open(KEYWORD_ONLY_PATTERN, encoding="utf8")
             if l.strip() and not l.strip().startswith("#")]
    return re.compile(lines[0], re.I)


def sha256_of(path):
    return hashlib.sha256(open(path, "rb").read()).hexdigest()[:16]


def specs_digest(spec_dir):
    """Aggregate sha256 over the sorted spec file names + contents."""
    if not spec_dir or not os.path.isdir(spec_dir):
        return ""
    h = hashlib.sha256()
    for root, _, files in os.walk(spec_dir):
        for fn in sorted(files):
            if not fn.endswith(".spec.js"):
                continue
            full = os.path.join(root, fn)
            h.update(os.path.relpath(full, spec_dir).encode())
            h.update(open(full, "rb").read())
    return h.hexdigest()[:16]


def read_source_hashes():
    if not os.path.exists(SOURCES_SHA):
        return {}
    out = {}
    for line in open(SOURCES_SHA, encoding="utf8"):
        key, _, value = line.partition("=")
        if value:
            out[key.strip()] = value.strip()
    return out


def write_source_hashes(pack, overrides, spec_dir):
    lines = [
        f"pack={sha256_of(pack) if os.path.exists(pack) else ''}",
        f"overrides={sha256_of(overrides) if overrides and os.path.exists(overrides) else ''}",
        f"specs={specs_digest(spec_dir)}",
    ]
    with open(SOURCES_SHA, "w", encoding="utf8") as f:
        f.write("\n".join(lines) + "\n")


def check_source_drift(pack, overrides, spec_dir):
    """Compare the keyteki source hashes (pack / overrides / specs) with the sidecar."""
    stored = read_source_hashes()
    if not stored:
        print("SOURCE HASHES: no docs/fidelity/keyteki-sources.sha yet (run the inventory once to generate it)")
        return False
    current = {
        "pack": sha256_of(pack) if os.path.exists(pack) else "",
        "overrides": sha256_of(overrides) if overrides and os.path.exists(overrides) else "",
        "specs": specs_digest(spec_dir),
    }
    drifted = [k for k, v in current.items() if v and stored.get(k) and v != stored[k]]
    appeared = [k for k, v in current.items() if v and not stored.get(k)]
    vanished = [k for k, v in stored.items() if v and not current.get(k)]
    if drifted:
        print(f"DRIFT: keyteki source(s) changed since the inventory was generated: {', '.join(drifted)}")
    if appeared:
        print(f"DRIFT: new hashable keyteki source(s): {', '.join(appeared)}")
    if vanished:
        print(f"DRIFT: stored keyteki source(s) no longer present: {', '.join(vanished)}")
    if drifted or appeared or vanished:
        print("Re-run the inventory and re-review the affected cards/specs, then commit the new CSV + sources.sha.")
        return True
    print(f"Spec source hashes OK: {', '.join(f'{k}={v}' for k, v in current.items() if v)}.")
    return False


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


def parse_manual(core_cards_kt, config_kt, cards_dir=None):
    """card id -> (level, notes) for manual CardScript objects registered in CardLibraryConfig."""
    objs = {}
    files = [core_cards_kt]
    if cards_dir:
        files += sorted(str(f) for f in pathlib.Path(cards_dir).glob("*.kt")
                        if pathlib.Path(f).name != "CoreSetDslCards.kt" and
                        not pathlib.Path(f).name.startswith("DslOverrides"))
    for path in files:
        src = open(path).read()
        for name, body in re.findall(r"object (\w+) : CardScript \{(.*?)(?=\nobject |\nval |\Z)", src, re.S):
            m = re.search(r"level = FidelityLevel\.(\w+)(?:,\s*notes = \"([^\"]*)\")?", body)
            objs[name] = (m.group(1), m.group(2) or "") if m else ("UNKNOWN", "")
    return {cid: objs.get(obj, ("UNKNOWN", "")) for cid, obj in re.findall(r'library\.register\("([^"]+)", (\w+)\)', open(config_kt).read())}


def parse_override_ids(core_cards_kt, cards_dir=None):
    """card id -> (level, notes) for the hand-written DSL override lists.

    CoreCards.kt aggregates the per-mechanic DslOverrides*.kt files, so every
    file carrying `card("id") { ... fidelity(...) }` blocks is scanned.
    """
    files = [core_cards_kt]
    if cards_dir:
        files += sorted(str(f) for f in pathlib.Path(cards_dir).glob("DslOverrides*.kt"))
    out = {}
    for path in files:
        src = open(path).read()
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
    return not t or bool(keyword_only_regex().match(t))


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


def check_drift(csv_path, cards_dir, pack_path, overrides_path, spec_dir):
    """Compare keyteki card-file hashes in the committed CSV with the current sources.

    Fails (returns non-zero) when a keyteki card file changed, appeared or disappeared
    since the CSV was generated, or when the pack / overrides / spec sources drifted
    against the stored sidecar hashes — those must be re-reviewed before the next batch.
    """
    if not cards_dir or not os.path.isdir(cards_dir):
        print(f"DRIFT CHECK FAILED: keyteki card directory not found: {cards_dir!r}")
        print("Point --keyteki-cards at a keyteki checkout (server/game/cards/<NN>-<Set>) or skip the check.")
        return 1
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
    card_drift = bool(changed or appeared or disappeared)
    source_drift = check_source_drift(pack_path, overrides_path, spec_dir)
    if card_drift or source_drift:
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
    ap.add_argument("--keyteki-specs", default=os.path.expanduser("~/work/keyteki/test/server/cards/01-Core"),
                    help="keyteki .spec.js directory (hashed into keyteki-sources.sha)")
    ap.add_argument("--overrides", default=os.path.expanduser("~/work/keyteki/master-vault-data/overrides.json"),
                    help="master-vault overrides.json (hashed into keyteki-sources.sha)")
    ap.add_argument("--check-drift", action="store_true",
                    help="compare keyteki card-file hashes against the committed CSV; exit 1 on drift")
    args = ap.parse_args()

    if args.check_drift:
        return check_drift(args.out, args.keyteki_cards, args.pack, args.overrides, args.keyteki_specs)

    cards = load_cards(args.pack)
    dsl = parse_dsl_levels(os.path.join(CARDS_DIR, "CoreSetDslCards.kt"))
    core_kt = os.path.join(CARDS_DIR, "CoreCards.kt")
    overrides = parse_override_ids(core_kt, os.path.dirname(core_kt))
    manual = parse_manual(core_kt, CONFIG, os.path.dirname(core_kt))
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

    # Store the hashes of the other keyteki sources (pack, overrides, specs) so the
    # drift check can also detect changes the per-card CSV cannot express.
    write_source_hashes(args.pack, args.overrides, args.keyteki_specs)

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
