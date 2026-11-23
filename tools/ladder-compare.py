#!/usr/bin/env python3
"""Diff the ratings of two multi-deck ladder batches (Phase 5 of the fidelity plan).

Usage:
  python3 tools/ladder-compare.py <oldBatchUuid> <newBatchUuid> [--tiers TIERS_JSON] [--out PATH]

Reads `.simulations/<uuid>/ratings.json` and `.simulations/<uuid>/batch.json` for both
batches and reports:
  - per-deck Aember Score movement (old rank -> new rank, score delta)
  - rank correlation (Spearman) and pairwise agreement between the two ladders
  - optionally: Spearman / pairwise agreement of each ladder against hand-assigned
    tiers (TIERS_JSON maps deckUuid -> tier number, higher = better, e.g. S+=6 ... Z=0),
    so the new run can be compared with the baseline 0.44 / 67.5%.

Exit status is 0 even when agreement does not improve — the numbers are the deliverable,
not a pass/fail gate.
"""
import argparse
import json
import os
import sys

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
from ratings_io import load_names, load_ratings  # noqa: E402


def ranks_from_ratings(ratings):
    ordered = sorted(ratings, key=lambda r: -r["aemberScore"])
    return {r["deckUuid"]: i + 1 for i, r in enumerate(ordered)}


def spearman(a, b):
    common = sorted(set(a) & set(b))
    n = len(common)
    if n < 2:
        return float("nan")
    da = [a[k] for k in common]
    db = [b[k] for k in common]

    def rank(xs):
        order = sorted(range(len(xs)), key=lambda i: xs[i])
        ranks = [0.0] * len(xs)
        i = 0
        while i < len(order):
            j = i
            while j + 1 < len(order) and xs[order[j + 1]] == xs[order[i]]:
                j += 1
            avg = (i + j) / 2 + 1
            for k in range(i, j + 1):
                ranks[order[k]] = avg
            i = j + 1
        return ranks

    ra, rb = rank(da), rank(db)
    ma, mb = sum(ra) / n, sum(rb) / n
    num = sum((x - ma) * (y - mb) for x, y in zip(ra, rb))
    den = (sum((x - ma) ** 2 for x in ra) * sum((y - mb) ** 2 for y in rb)) ** 0.5
    return num / den if den else float("nan")


def pairwise_agreement(a, b):
    """Fraction of pairs ordered the same way (TierValidator convention: pairs equal on the
    reference side are skipped, ties on the compared side count 0.5)."""
    common = sorted(set(a) & set(b))
    total, agree = 0, 0.0
    for i, x in enumerate(common):
        for y in common[i + 1:]:
            if b[x] == b[y]:
                continue
            total += 1
            product = (a[x] - a[y]) * (b[x] - b[y])
            agree += 1.0 if product > 0 else (0.5 if product == 0 else 0.0)
    return agree / total if total else float("nan")


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("old_batch")
    ap.add_argument("new_batch")
    ap.add_argument("--tiers", default=None, help="JSON file: {deckUuid: tier number}")
    ap.add_argument("--out", default=None, help="also write the table to this path")
    args = ap.parse_args()

    old, new = load_ratings(args.old_batch), load_ratings(args.new_batch)
    names = {**load_names(args.old_batch), **load_names(args.new_batch)}
    old_r, new_r = ranks_from_ratings(old), ranks_from_ratings(new)
    old_by_uuid = {r["deckUuid"]: r for r in old}
    new_by_uuid = {r["deckUuid"]: r for r in new}

    rows = []
    for uuid in sorted(new_r, key=lambda u: new_r[u]):
        o = old_by_uuid.get(uuid)
        n = new_by_uuid[uuid]
        rows.append({
            "uuid": uuid,
            "name": names.get(uuid, uuid),
            "oldRank": old_r.get(uuid),
            "newRank": new_r[uuid],
            "oldScore": o["aemberScore"] if o else None,
            "newScore": n["aemberScore"],
            "games": n["matches"],
            "winRate": n["winRate"],
        })

    corr = spearman(old_r, new_r)
    agreement = pairwise_agreement(old_r, new_r)
    lines = []
    lines.append(f"ladder comparison: {args.old_batch} -> {args.new_batch}")
    lines.append(f"rank correlation between ladders: {corr:.3f} | pairwise agreement: {agreement * 100:.1f}%")
    lines.append("")
    lines.append(f"{'rank':>4} {'deck':44} {'old':>4} {'new':>4} {'drk':>4} {'old':>6} {'new':>6} {'dA':>6} {'win%':>6}")
    for r in rows:
        delta_rank = "" if r["oldRank"] is None else r["oldRank"] - r["newRank"]
        delta_score = "" if r["oldScore"] is None else f"{r['newScore'] - r['oldScore']:+.1f}"
        old_score = "-" if r["oldScore"] is None else f"{r['oldScore']:.1f}"
        lines.append(
            f"{r['newRank']:>4} {r['name'][:44]:44} "
            f"{r['oldRank'] if r['oldRank'] else '-':>4} {r['newRank']:>4} {delta_rank if delta_rank != '' else '-':>4} "
            f"{old_score:>6} {r['newScore']:>6.1f} {delta_score:>6} {r['winRate'] * 100:>6.1f}"
        )

    tiers = json.load(open(args.tiers)) if args.tiers else None
    if tiers:
        old_scores = {r["deckUuid"]: r["aemberScore"] for r in old}
        new_scores = {r["deckUuid"]: r["aemberScore"] for r in new}
        for tag, values in (("old", old_scores), ("new", new_scores)):
            common = [u for u in tiers if u in values]
            # Compare aember scores directly (higher = better), not ranks, so the
            # Spearman sign is not inverted by the rank direction.
            s = spearman({u: values[u] for u in common}, {u: tiers[u] for u in common})
            ag = pairwise_agreement({u: values[u] for u in common}, {u: tiers[u] for u in common})
            lines.append("")
            lines.append(f"tier validation ({tag}, {len(common)} labelled decks): Spearman {s:.2f}, pairwise agreement {ag * 100:.1f}%")

    report = "\n".join(lines)
    print(report)
    if args.out:
        os.makedirs(os.path.dirname(args.out) or ".", exist_ok=True)
        with open(args.out, "w") as f:
            f.write(report + "\n")
    return 0


if __name__ == "__main__":
    sys.exit(main())
