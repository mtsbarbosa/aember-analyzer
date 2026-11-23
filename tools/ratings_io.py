"""Shared loader for stored ladder ratings (used by ladder-compare.py and ladder-report.py).

Stored `ratings.json` files written before Aember Score scale v2 carry no `scaleVersion` and
use `50 + 10*log10(r/median)`. They are converted exactly (not approximated) to the current
scale `50 + 12.5*ln(r/median)`: new = 50 + (old - 50) * 12.5*ln(10)/10, intervals included.
Keep this in sync with domain/rating/AemberScale.kt (VERSION = 2).
"""
import json
import math
import os
import sys

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
CURRENT_SCALE = 2
LEGACY_TO_CURRENT = 12.5 * math.log(10) / 10  # ≈ 2.878


def load_ratings(uuid):
    path = os.path.join(ROOT, ".simulations", uuid, "ratings.json")
    if not os.path.exists(path):
        sys.exit(f"No ratings.json for batch {uuid} (has it completed?)")
    ratings = json.load(open(path))
    for r in ratings:
        if r.get("scaleVersion", 1) == CURRENT_SCALE:
            continue
        for key in ("aemberScore", "aemberScoreLow", "aemberScoreHigh"):
            if r.get(key) is not None:
                r[key] = 50.0 + (r[key] - 50.0) * LEGACY_TO_CURRENT
        r["scaleVersion"] = CURRENT_SCALE
    return ratings


def load_batch(uuid):
    path = os.path.join(ROOT, ".simulations", uuid, "batch.json")
    return json.load(open(path)) if os.path.exists(path) else {}


def load_names(uuid):
    return {d["uuid"]: d["name"] for d in load_batch(uuid).get("deckPool", [])}
