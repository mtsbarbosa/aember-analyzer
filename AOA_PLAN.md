# Plan — Add Age of Ascension (AoA) + per-set and all-sets ladders

## Context

Only Core (CotA, 341) is wired. Goal: import AoA (435), simulate AoA decks, and when the
user imports a new deck, rate it against **(a) its own set's pool** and **(b) an all-sets
pool**, giving two Aember Scores. AGENTS.md already has a generic "Importing Cards from a New
Set" checklist; this plan applies it and adds the cross-set layer.

Findings from inspection:
- `packs/AoA.json`: 370 cards, 7 Core houses only (no new houses), types creature/action/artifact/upgrade only
  (no token/prophecy), keywords all already in the engine (elusive, skirmish, alpha, omega, taunt,
  deploy, hazardous, assault, poison). **No house/type/keyword code changes needed.**
- **166 of 370 are reprints of Core ids** (same slug) → already scripted/verified; only **204 new cards** need scripts.
- Hard-coded Core spots: `JpaCardCatalogAdapter.seedHousesAndExpansion` (expansion 341 only),
  `CatalogSeedConfig` (seeds only when catalog empty), `JsonCardCatalogLoader.loadCoreSet`,
  `MasterVaultDeckParser` ("Expansion N is not supported" via `catalog.loadByExpansion`),
  `CardLibraryConfig.buildCoreCardLibrary`, `tools/generate-core-cards.py` (CotA path/out file),
  `tools/port-keyteki-specs.mjs` (`SPEC_DIR` = 01-Core), `tools/fidelity-inventory.py`,
  test `CoreCardLibrary`.
- Pools are already set-agnostic (`DeckPool`, `DeckPoolService`, `LadderService` take any pool key,
  tiers from `docs/deck-pool/{pool}-tiers.json`). Matches between decks of different sets work as
  long as the card library holds both sets (scripts keyed by id). So the "all sets" ladder is
  just a pool named `all` containing the union — no new rating math.

## Prerequisite — AoA tier decks (owner, before execution starts)

The owner collects the AoA pool **before** this plan is executed: ~40 AoA deck UUIDs spanning S+ … Z, with tiers
(S+=6 … Z=0). Expected hand-off: `aoa-pool.txt` (one UUID per line) and `docs/deck-pool/aoa-tiers.json`
(`{deckUuid: tier}`). Consequences for the plan:
- Phase C no longer waits on deck selection; it only imports the decks (with delays to avoid Master Vault 429s) and
  seeds the pool/tier files.
- The decks' card lists feed Phase B directly: the inventory's pool-impact priority can be computed from day one.
- Phase A's AoA import test can use one of these decks as its fixture.

## Phase A — Catalog + import (small, unblocks everything)

1. `JsonCardCatalogLoader`: add `loadPackFor(expansionId)` using `expansions.json` (id→pack code); keep
   `loadCoreSet()`. De-duplicate ids inside a pack; for ids already in the catalog keep the existing row
   (reprint keeps first-seen expansion; decks resolve cards by id so imports still work).
2. `JpaCardCatalogAdapter`/`CatalogSeedConfig`: make seeding **incremental** (insert missing ids, insert
   expansion rows 341 + 435 from `expansions.json`) instead of "only when empty". Flyway migration only if
   an expansion table constraint requires it.
3. `MasterVaultDeckParser`: the "supported expansion" check passes for 435 once expansion rows/cards exist
   (verify reprint-heavy AoA decks resolve every card id).
4. Test: extend catalog seeding integration test (AoA count, reprint not duplicated); import one AoA deck from a
   saved JSON fixture (`src/test/resources/decks/`).

## Phase B — Card scripts for the 204 new AoA cards

1. Parametrise `tools/generate-core-cards.py` → `--pack AoA --out AoaSetDslCards.kt --skip-existing` (skip ids already in
   Core). Output a **separate file** so Core hand-patches (Known Gap #12) are never clobbered; register in
   `buildCoreCardLibrary` after Core and before manual scripts.
2. Anything the generator can't express → `PARTIAL`/`APPROXIMATED`, never silent `FULL`.
3. Extend `tools/fidelity-inventory.py` with `--pack AoA --keyteki-cards .../02-AoA` → `docs/fidelity/aoa-inventory.csv`.
   Priority = pool impact × cluster size, once the AoA pool exists (Phase C).
4. Engine mechanics AoA needs that Core lacks are discovered by the inventory (expected: capture / "for each" /
   power counters / "may", already Known Gaps) — schedule them as Phase-2-style engine work per cluster, not per card.
5. Port specs: parametrise `SPEC_DIR` in `port-keyteki-specs.mjs` (`--set 02-AoA`); parity tests under
   `engine/cards/aoa/`; add ids to `verified-cards.txt` only when all portable specs pass. Make
   `CardScriptRegistrationTest`/`VerifiedCardRegistryTest` load Core+AoA (`CoreCardLibrary` → multi-pack).

## Phase C — Pools and the three ladders

- `cota` pool (existing), `aoa` pool (new: ~40 AoA decks with tier labels in `docs/deck-pool/aoa-tiers.json`,
  seed file `aoa-pool.txt`, tracker `docs/deck-pool/aoa.md`) — decks and tiers are supplied by the owner beforehand (see *Prerequisite*).
- `all` pool = `cota ∪ aoa`, implemented as a **derived pool**: `DeckPoolService.getPool("all")` computes the union
  of member set pools instead of storing a separate list (adding a deck to `aoa` updates `all` automatically).
  Config: `aember.pools.composite.all: [cota, aoa]`. `LadderService` already matches baselines by deck-uuid set.
- Ladder cost: ~84 decks → ~3,500 pairings × 30 = ~105k matches (vs 25.8k now, ~4–5 min). Default
  `matchesPerPairing` for `all` = 10 (≈35k matches), overridable.
  Tiers for `all`: `all-tiers.json` merge of the set files (labels are set-relative; cross-set tier validation is
  indicative only, per Known Gap #7).
- `tools/rerun-ladder.sh POOL=aoa|all`; `ladder-report.py` already takes tiers/out paths.

## Phase D — "Rate my new deck" flow (main deliverable)

New endpoint `POST /api/decks/{uuid}/rate` (or extend `DeckPoolController`): runs
`DeckPoolService.rateDeckAgainstPool` twice — against the deck's **own-set pool** (resolved from `deck.expansion` →
pool key via config map `341→cota, 435→aoa`) and against `all` — and returns both anchored Aember Scores
(+ bootstrap bounds, percentile) side by side. Optional `matchesPerPairing`, `ai`, `seed`, `addToPool`.
Reuse `rateDeckAgainstPool` / `findAnchorBatch` (anchors on a completed ladder over the same pool; `all` needs its own
completed ladder first) and `PoolDeckRating` snapshots. CLI: `rate-deck <uuid>` in `AemberCliRunner`.
Edge: if a pool has no ladder yet, return raw stats plus a note instead of failing.

## Verification

1. `./gradlew test` (new seeding, import, registration, AoA parity tests).
2. Generator diff review; inventory CSV shows no ability-text card left as unlisted `NoEffect`.
3. Import an AoA and a CotA deck; extend `tools/smoke-test.py` with an AoA-deck check and a composite-pool check.
4. `POST /api/ladder/rerun {"pool":"aoa"}`, then `{"pool":"all","matchesPerPairing":10}`; check health
   (0 failures, avg rounds ≈ 11–14) and tier validation per set.
5. `POST /api/decks/{uuid}/rate` on one deck of each set → two scores; CotA decks' `all` ordering must be consistent
   with their `cota` ordering.

## Notes
- AoA fidelity will start low (many new cards `PARTIAL`); the first ladder is a baseline, not a quality claim. Raise
  via ~40-card batches by priority, re-running the `aoa` ladder per batch.
- Do not commit unless asked (AGENTS.md).
