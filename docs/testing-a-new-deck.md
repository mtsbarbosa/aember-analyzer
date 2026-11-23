# Testing a New Deck's Rating

How to measure how a single deck performs in this simulator. There are two
paths: a **quick check** (head-to-head vs. reference decks, minutes) and a
**full ladder rating** (pool-relative Aember Score, recommended for anything
you want to record).

## Prerequisites

- App running: `./gradlew bootRun` (or use the CLI commands below, which boot
  their own context). Postgres up: `podman-compose up -d`.
- You have the Master Vault UUID of the deck.

## Step 1 — Import the deck

```bash
./gradlew bootRun --args "import-deck <master-vault-uuid>"
```

or via REST (mind rate limits: space imports by a few seconds if importing
several):

```bash
curl -X POST http://localhost:8080/api/decks/import/<master-vault-uuid>
```

If Master Vault rate-limits you (HTTP 429 → 500), wait ~30s and retry.

## Step 2 — Sanity check the deck's cards

Before trusting any number, verify the engine can actually execute the deck.
A deck full of `PARTIAL`/no-op cards gets a **meaningless rating** (that is
the #1 source of divergences from SAS/AERC in this project).

```bash
# one match with verbose state + fidelity report
curl -s -H "X-Debug: true" \
  "http://localhost:8080/api/simulate/<new-deck-uuid>/<any-pool-deck-uuid>?seed=42" \
  | python3 -c "import json,sys; d=json.load(sys.stdin); print(d['fidelity'])"
```

Check:

- `fidelity.score` is well above the threshold (70%).
- `fidelity.lowestCards` contains no signature cards of the deck (if a deck's
  key combo card is `NoEffect`/`PARTIAL`, the rating will not reflect real
  strength — see Torsen: SAS 90, sim rank 23).
- The deck wins some matches across a few seeds (`seed=1..5`).

## Step 3 — Quick check (optional): head-to-head

Win rates against specific reference decks are **not** pool-relative and are
directly comparable to historical runs.

```bash
# 100 matches vs one reference deck (async batch)
curl -X POST http://localhost:8080/api/mass-simulations \
  -H "Content-Type: application/json" \
  -d '{
    "deckUuidA": "<new-deck-uuid>",
    "deckUuidB": "<reference-deck-uuid>",
    "matchCount": 100,
    "baseSeed": 12345
  }'
```

Poll `GET /api/mass-simulations/{batch-uuid}` until `COMPLETED`; the deck
aggregates include win rate and margins. Reference decks used in past
autopsies: Violet (`31f25022-…`), Galilee el Obtuso (`895ac75a-…`), Torsen
(`c11e29c6-…`).

## Step 4 — Full ladder rating

Ratings (Aember Score, SoS, Bradley-Terry strength) are **relative to the
pool** — they only mean something when every pool member runs in the same
batch. Adding a deck therefore means re-running the full round-robin with the
new deck appended.

1. Append the UUID to the pool file (one UUID per line):

   ```bash
   echo "<master-vault-uuid>" >> cota-pool.txt
   ```

2. Run the ladder (pairs grow linearly: 23 decks → 253 pairs, 24 → 276):

   ```bash
   ./gradlew bootRun --args "mass-simulate-decks cota-pool.txt 10 tactical 0"
   ```

   (10 matches per pairing with seat alternation; default seed 0 — pass a different
   seed for a different deterministic run. ~2s per 1,000 matches.)
   Alternatively via REST:

   ```bash
   curl -X POST http://localhost:8080/api/mass-simulations/multi \
     -H "Content-Type: application/json" \
     -d '{ "deckUuids": [<all pool uuids>], "matchesPerPairing": 10, "ai": "tactical", "seatAlternation": true }'
   ```

3. Read the ratings (computed at batch completion and persisted as
   `ratings.json` in `.simulations/{batch-uuid}/`):

   ```bash
   curl http://localhost:8080/api/mass-simulations/{batch-uuid}/ratings
   ```

## Step 4b — REST shortcut: rate a deck against a named pool

For the common "add a deck and check it" flow there is a synchronous endpoint
that does **not** require re-running the whole ladder. It replays the most
recent stored pool ladder, plays the new deck against every pool member, and
refits all decks together (ladder methodology), so the Aember Score is directly
comparable with the ladder.

```bash
# 1. Import the deck (Step 1), then add it to the pool (optional but recommended):
curl -X POST http://localhost:8080/api/pools/cota/decks \
  -H "Content-Type: application/json" \
  -d '{ "deckUuid": "<master-vault-uuid>" }'

# 2. Rate it synchronously (~1s for 220 matches):
curl -X POST "http://localhost:8080/api/pools/cota/ratings/<master-vault-uuid>?matchesPerPairing=10&baseSeed=0"
```

The response contains the raw record (wins/losses/draws, win rate ± margin),
per-opponent win rates sorted best-to-worst, and — when a stored ladder covers
the pool — the anchored `bradleyTerry`, `aemberScore` (50 = pool median) and
`percentile`, plus the `anchorBatchUuid` it was computed against. Every call is
snapshotted to `.simulations/pools/cota/ratings/` and is retrievable via
`GET /api/pools/cota/ratings/<deckUuid>`.

Query params: `matchesPerPairing` (default 10), `baseSeed` (default 0),
`ai` (default `tactical`), `concurrency` (default = available processors).
Rating a deck that is already a pool member is allowed (it plays against the
other members only).

## Step 5 — Interpret

| Field | Meaning |
|-------|---------|
| `winRate` ± `winRateMargin` | Raw win rate over all matches (±95% CI); below 50% margin with `lowConfidence=true` means treat with suspicion |
| `sos` | Strength of schedule — check it before celebrating: a high win rate vs. weak opponents is worth less |
| `bradleyTerry` | Maximum-likelihood strength (draws = half win, smoothing 0.5) — comparable *within the run only* |
| `aemberScore` | `50 + 12.5·ln(r/r_median)` — 50 = pool median; ≈ 1 point per 2pp win-rate edge vs an equal deck (2 pts subtle, 5 pts clear). Scale v2; pre-v2 stored snapshots used `50 + 10·log10(r/r_median)` |
| `percentile` | Rank within the pool |

Caveats:

- **Aember Scores from different batches are not comparable** unless the pool
  is identical: the median is the anchor, and adding/removing decks shifts it.
  To track a new deck against the historical ladder, re-run the whole pool
  (new deck included) in one batch and compare with the recorded previous run.
- Record the run: batch UUID, date, pool file version, and the ratings JSON
  snapshot (see `.simulations/pools/{key}/ratings/` for the stored format; current pool standings are in `docs/deck-pool/cota-ladder.md`).
- If the new deck lands far from its SAS/AERC tier, audit its fidelity first
  (Step 2) — divergence usually means engine coverage, not deck strength.
