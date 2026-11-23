# CotA Reference Deck Pool — Import Tracker

Per-set deck pools live in this directory: one file per set (`<set>.md`). This
file tracks the **Core set (CotA, expansion 341)** reference pool used for
multi-deck round-robin batches and ratings. Import via
`POST /api/decks/import/{uuid}` or the CLI `import-deck` command. Status values:
`imported`, `pending`, `failed`.

Ratings in this document are from the deck-tracking tool (SAS / base AERC / SAS
synergy shift `s`, win percentage `wp`, key cost `kc`) — they are informational
context, not produced by the simulator.

Simulator ladder standings live in exactly one place, `cota-ladder.md`, which is
**generated** from a completed batch with `python3 tools/ladder-report.py <batchUuid>`
(never edit it by hand; no scores are copied into this file). Start a ladder with
`POST /api/mass-simulations/multi` or
`./gradlew bootRun --args "mass-simulate-decks cota-pool.txt 30 tactical 12345"`. Results
from older engine versions are not comparable; the run table below says which batch is current.

Pool last updated: 2026-11-21 — **all 23 pool decks imported**.
Note: Master Vault imports are rate-limited (~429 after ~15 rapid requests);
space imports with several seconds of delay when adding new decks.

## Import summary

| Tier | Decks | Imported | Pending |
|------|-------|----------|---------|
| S+   | 5     | 5        | 0       |
| S    | 5     | 5        | 0       |
| A    | 5     | 5        | 0       |
| B    | 5     | 5        | 0       |
| Z    | 3     | 3        | 0       |
| **Total** | **23** | **23** | **0** |

Note: "Galilee el Obtuso" (S+) and "Galilee, o Engenhoso" (S) are different decks.

## S+ (SAS 97+)

| Deck | SAS | base AERC | notes | UUID | Status |
|------|-----|-----------|-------|------|--------|
| Seraphina, a Opulenta, Filósofa do Museu | 99 | 72 | -1.5s / 1wp / 0kc | `fda97a96-2453-441e-9ae9-15e335d4e2d5` | imported |
| P. Crowe, Vicedirettrice di Cigliafugace | 98 | 73 | 2.8s / 0wp / 0kc | `fed5e902-5b3b-4ce5-9bdd-42ae1c0f55c9` | imported |
| Galilee el Obtuso, Canciller de la Estación | 101 | 77 | 15s / 0wp / 0kc | `895ac75a-c839-4c5a-bc5d-fc991c59437f` | imported |
| Giulia, Xamã do Monumento Vítreo | 97 | 69 | -18s / 1wp / 0kc | `20731362-5cf7-4717-9c18-ddce10407841` | imported |
| Everlasting “Linguini” Lewry | 106 | 61 | 17s / 0wp / 4kc | `d00d4b93-8eae-4ac8-b376-0bfb1b085290` | imported |

## S (SAS ~81–90)

| Deck | SAS | base AERC | notes | UUID | Status |
|------|-----|-----------|-------|------|--------|
| Galilee, o Engenhoso, Traidor da Loja | 81 | 67 | 3.8s / 1wp / 0kc | `8118a3a3-285b-42e8-b13d-6b32c5d3d574` | imported |
| Mehitabel, a Raivosa, Protetora do Retiro | 82 | 70 | 13s / 0wp / 0kc | `a0f71671-fe30-40bd-9130-aa5f75da4a9c` | imported |
| Torsen, Duca Tossico | 90 | 67 | 4.5s / 1wp / 1kc | `c11e29c6-3c9c-4ab0-91e1-60a98dc7c19c` | imported |
| Fast Benedora of the Acropolis | 85 | 60 | 15s / 1wp / 5kc | `dfba7439-5cfd-495c-9260-c99316c006ef` | imported |
| Time-stopper Kadence Lockenpsen | 82 | 71 | 0s / 1wp / 0kc | `f194a2c2-8bd1-47ca-a130-a4d9dc6d9538` | imported |

## A (SAS ~67–76)

| Deck | SAS | base AERC | notes | UUID | Status |
|------|-----|-----------|-------|------|--------|
| Violet, a Grosseira, Inventora do Cemitério | 76 | 68 | 0.5s / 0wp / 0kc | `31f25022-9fd2-4bb0-8510-b2a4f6ca3b7a` | imported |
| Parthenia “Garra”, a Precavida | 67 | 55 | -2s / 1wp / 0kc | `c8956c78-fa6c-4b9d-ab3a-50ea245d75d4` | imported |
| Mustabos, Spawn of Bestispike | 76 | 63 | 12s / 0wp / 0kc | `df2dc522-2888-4d9a-9c43-43af3e983790` | imported |
| The Public Butcher Brewer of Quakehell | 73 | 66 | -4.5s / 3wp / 0kc | `dc2f4c0b-f6be-4b09-856b-1f8fa4aef3a7` | imported |
| Chang, Tower Baroness | 73 | 73 | 13s / 3wp / 0kc | `fb58ab11-d920-4514-9da0-ee86c3859087` | imported |

## B (SAS ~66–69)

| Deck | SAS | base AERC | notes | UUID | Status |
|------|-----|-----------|-------|------|--------|
| Rutherford a Dourada | 69 | 63 | 0s / 2wp / 0kc | `9b5d307c-b2b3-49c6-abfe-977d18fae9ac` | imported |
| Ingrid, Quanland Borrower | 68 | 62 | 0.3s / 2wp / 0kc | `e72a7f7f-7afd-421d-94aa-08dae00f9f96` | imported |
| Chaos, Baltiholt's Deuce | 68 | 60 | 7.9s / 1wp / 0kc | `1d380430-91c0-4162-8f66-9f8a31217ae0` | imported |
| Martínez, Marshcot's Painter | 66 | 58 | -3s / 2wp / 0kc | `be3ecd0c-354e-4e4e-8fec-4bf33b186156` | imported |
| González the Swiftly Secret | 66 | 54 | 9.8s / 0wp / 2kc | `b66e669d-7e08-48d4-a8b2-22c6e641416f` | imported |

## Z (SAS not tracked — weak pool / baseline)

| Deck | SAS | base AERC | notes | UUID | Status |
|------|-----|-----------|-------|------|--------|
| Patricia, Hacker da Vila Primordial | — | — | | `d09ffcf1-a49f-42d9-a3d5-29f6c58495c4` | imported |
| Eraz-Toth, the Solid City Doctor | — | — | | `9959466e-b4ff-4b35-871f-76d8a4023474` | imported |
| Xipty J. Buwaaoki, The Fifteenth | — | — | | `12b6cad6-57d4-45fa-ad4d-cf53218eddc2` | imported |

## Out-of-pool decks (already in DB, not part of the reference ladder)

| Deck | UUID | Reason |
|------|------|--------|
| I. Stone, Diabrete do Arquipélago Laranja | `c90797b4-3436-41d3-ad68-afdefd800084` | not tiered |
| Madame Eliene Wolf | `e37672cd-9b1e-4664-861a-e151aa547c28` | not tiered |
| Shadowmaster Desmares | `eb5d4c4a-5957-4276-ab9a-0d1b19f42e81` | not tiered |
| Test Archon Two | `aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee` | test fixture |

## Reference ladder runs

| Date | Batch UUID | Matches | AI | Notes |
|------|-----------|---------|-----|-------|
| 2026-11-21 | `77b6f0fe-649e-4091-9f47-aa7e08055d9b` | 2,530 (23 decks × 10 per pairing) | tactical | Full pool; avg fidelity 85.5%, avg rounds 7.9 |
| 2026-11-21 | `0de5f424-0f5b-45b3-ab96-ced1f4c6409c` | 2,530 (23 decks × 10 per pairing) | tactical | After Mars-engine + control-card pass & enter-ready fix; avg fidelity 85.8%, avg rounds 8.1 |
| 2026-11-21 | `2e34c9c4-8ea0-4976-b8a5-24906e737a39` | 2,530 (23 decks × 10 per pairing) | tactical | After key-cheat pass (Key Charge, Chota Hazri, Key of Darkness, Epic Quest); avg fidelity 85.9% |
| 2026-11-21 | `33272b10-6b66-45c4-9f4b-92f0ee6d2687` | 2,530 (23 decks × 10 per pairing) | tactical | After rules-review fixes (Speed Sigil global/creature-only, seeded in-game RNG, fully deterministic replays); avg fidelity 85.8%, avg rounds 12.3. |
| 2026-11-22 | `083dc1b0-1e96-49ce-8433-1c630b3e2ea8` | 25,830 (42 decks × 30 per pairing, seed 12345) | tactical | Phase-5 reference baseline; avg fidelity ~85%, 0 failures / 0 draws |
| 2026-11-22 | `fad7c062-1c9b-40df-8c59-325f460ee1fc` | 25,830 (42 decks × 30 per pairing, seed 12345) | tactical | After Phase 0+1 (splash, poison, moribund fight triggers); avg fidelity 85.1%, verified 66.2%, avg rounds 11.6, 0 failures / 0 draws. Diff against the baseline with `tools/ladder-compare.py` (`docs/fidelity/ladder-phi1-vs-baseline.txt`) |
| 2026-11-22 | `e72fcf30-7c69-47f2-88ae-7a729c7c022b` | 25,830 (42 decks × 30 per pairing, seed 12345) | tactical | After Phase 2.1 (EffectRegistry persistent auras: 16 cards + Jammer Pack migration); avg fidelity 86.0%, verified 65.9%, avg rounds 11.6, 0 failures / 0 draws. Stable vs previous run (rank corr 0.99, agreement 96.7%); tier validation 0.50 → 0.51. Diff in `docs/fidelity/ladder-phi21-vs-phi1.txt` |
| 2026-11-22 | `21137849-248f-49f9-b79a-cbca460c29a5` | 25,830 (42 decks × 30 per pairing, seed 12345) | tactical | After Phase 2.2 (event bus + 10 reaction cards: Soul Snatcher, Tolas, Pile of Skulls, Krump, Grommid, Carlo Phantom, Looter Goblin, Magda the Rat, Brain Stem Antenna, Loot the Bodies + Annihilation Ritual); avg fidelity 86.4%, verified 65.7%, avg rounds 11.6, 0 failures / 0 draws. Krump 23k / carlo-phantom 11k / magda 11k plays. More movement than previous runs (rank corr 0.97, agreement 94.7% — the amber-drain reactions shift matchups); tier validation 0.51 → 0.45 (see `docs/fidelity/ladder-phi22-vs-phi21.txt`) |
| 2026-11-23 | `928c859e-987d-495b-9487-3e9f95deecb2` | 25,830 (42 decks × 30 per pairing, seed 12345) | tactical | After Phase 2.2 fixes (Biomatrix archive path no longer emits CreatureDestroyed; event delivery on self-exit/bounce/subscription paths; ForgeKey emits KeyForged; Brain Stem house reset at end of turn). Near-identical: rank corr **1.00**, agreement **100%** vs the previous run — exactly one match flipped (Ingrid vs Quick Sycophant); avg fidelity 86.4%, 0 failures / 0 draws (`docs/fidelity/ladder-fix1-vs-phi22.txt`) |

Stored ratings from before Aember Score scale v2 used the legacy scale `50 + 10·log10(r/median)`; the tools convert
them to the current `50 + 12.5·ln(r/median)` when reading stored `ratings.json`. Runs before the 42-deck ladder predate the rules-review fixes and are not comparable with later runs.

## How to import missing decks

```bash
# App running on :8080
curl -X POST http://localhost:8080/api/decks/import/<uuid>

# Or via CLI (one deck per process start)
./gradlew bootRun --args "import-deck <uuid>"

# List currently imported decks
curl http://localhost:8080/api/decks
```
