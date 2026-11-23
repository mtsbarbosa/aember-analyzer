#!/usr/bin/env bash
# One-command ladder rerun: single curl against POST /api/ladder/rerun.
# The call blocks until the batch completes (~4-5 min for the full cota pool).
#
# Usage:
#   tools/rerun-ladder.sh                     # cota pool, documented defaults
#   POOL=cota PAIRINGS=30 SEED=12345 AI=tactical tools/rerun-ladder.sh
#   tools/rerun-ladder.sh | jq                # pretty summary
set -euo pipefail

BASE_URL="${BASE_URL:-http://localhost:8080}"
POOL="${POOL:-cota}"
PAIRINGS="${PAIRINGS:-30}"
SEED="${SEED:-12345}"
AI="${AI:-tactical}"
TIMEOUT="${TIMEOUT:-1800}"

BODY=$(printf '{"pool":"%s","matchesPerPairing":%s,"baseSeed":%s,"ai":"%s","timeoutSeconds":%s}' \
  "$POOL" "$PAIRINGS" "$SEED" "$AI" "$TIMEOUT")

echo "Rerunning ladder: pool=$POOL pairings=$PAIRINGS seed=$SEED ai=$AI (blocking)..." >&2
curl -s -X POST "$BASE_URL/api/ladder/rerun" \
  -H "Content-Type: application/json" \
  -d "$BODY"
