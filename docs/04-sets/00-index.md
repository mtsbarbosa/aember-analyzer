# KeyForge Sets Index

Data source: `master-vault-data/packs/*.json` (one JSON per set; `ids` field holds the Master Vault expansion id). Engine implementations live in `server/game/cards/<NN-Set>/`.

Note: `jq` is not installed in this environment; all numbers below were extracted with equivalent `node` one-liners against the pack JSON and re-checked with a final verification pass.

Important naming correction: pack `MG24` is **Menagerie 2024** (`"name": "Menagerie"`, code `MG2024`), not "Mid Gambit". The string "Mid Gambit" does not appear anywhere in the repo or pack data.

For the full card-by-card listing of any set, see `../07-card-database/` (generated from the same pack JSONs with per-card engine-verb extraction).

## Set Table

| File | Engine prefix | Codename | Full name | Expansion id(s) | Release (JSON) | Cards (JSON array) | Houses |
| --- | --- | --- | --- | --- | --- | --- | --- |
| [01-core.md](01-core.md) | `01-Core` | CotA | Call of the Archons | 341 | 2018-11-15 | 370 | Brobnar, Dis, Logos, Mars, Sanctum, Shadows, Untamed |
| [02-aoa.md](02-aoa.md) | `02-AoA` | AoA | Age of Ascension | 435 | 2019-05-30 | 370 | Brobnar, Dis, Logos, Mars, Sanctum, Shadows, Untamed |
| [03-wc.md](03-wc.md) | `03-WC` | WC | Worlds Collide | 452, 453 | 2019-11-30 | 415 | Brobnar, Dis, Logos, Saurian, Shadows, Star Alliance, Untamed |
| [04-mm.md](04-mm.md) | `04-MM` | MM | Mass Mutation | 479, 480 | 2020-05-31 (retail July 2020) | 438 | Dis, Logos, Sanctum, Saurian, Shadows, Star Alliance, Untamed |
| [05-dt.md](05-dt.md) | `05-DT` | DT | Dark Tidings | 496 | 2020-05-31 (retail March 2021) | 429 | Logos, Sanctum, Saurian, Shadows, Star Alliance, Unfathomable, Untamed, The Tide |
| [06-woe.md](06-woe.md) | `06-WoE` | WoE | Winds of Exchange | 600, 453 | 2023-06-05 | 414 | Brobnar, Ekwidon, Mars, Sanctum, Saurian, Star Alliance, Unfathomable |
| [07-gr.md](07-gr.md) | `07-GR` | GR | Grim Reminders | 700 | not in JSON (retail March 1, 2024) | 398 | Brobnar, Ekwidon, Geistoid, Mars, Star Alliance, Unfathomable, Untamed |
| [08-as.md](08-as.md) | `08-AS` | AS | Aember Skies | 800 | not in JSON (retail October 18, 2024) | 329 | Brobnar, Dis, Ekwidon, Geistoid, Logos, Mars, Skyborn |
| [09-toc.md](09-toc.md) | `09-ToC` | ToC | Tokens of Change | 855 | not in JSON (retail December 2024) | 321 | Dis, Geistoid, Logos, Redemption, Shadows, Skyborn, Untamed |
| [10-momu.md](10-momu.md) | `10-MoMu` | MoMu | MORE Mutation | 874 | not in JSON (retailer special, 2025) | 484 (441 unique) | Dis, Logos, Sanctum, Saurian, Shadows, Star Alliance, Untamed |
| [11-vm25.md](11-vm25.md) | `11-VM25` | VM25 | Vault Masters 2025 | 939, 453 | not in JSON (2025) | 305 | Brobnar, Ekwidon, Geistoid, Logos, Mars, Saurian, Shadows, Unfathomable |
| [12-pv.md](12-pv.md) | `12-PV` | PV | Prophetic Visions | 886, 453 | not in JSON (retail late Spring 2025) | 369 | Brobnar (reprints), Dis, Logos, Redemption, Sanctum, Saurian, Shadows, Star Alliance, Untamed, Prophecy |
| [13-cc.md](13-cc.md) | `13-CC` | CC | Crucible Clash | 918 | not in JSON (retail September 2025) | 350 (JSON declares 476) | Brobnar, Dis, Mars, Sanctum, Saurian, Skyborn, Untamed, Archon Power |
| [14-dm.md](14-dm.md) | `14-DM` | DM | Draconian Measures | 928 | 2026-04-28 | 303 | Ekwidon, Geistoid, Mars, Ouboros, Shadows, Skyborn, Unfathomable |
| [mg24-menagerie.md](mg24-menagerie.md) | (no engine dir) | MG2024 | Menagerie 2024 | 722 | 2024-01-01 (placeholder) | 791 (783 unique) | Sets 1-7 houses plus Keyraken |

## House Distribution Summary (cards per house in pack JSON)

| Set | Brobnar | Dis | Ekwidon | Geistoid | Logos | Mars | Redemption | Sanctum | Saurian | Shadows | Skyborn | Star Alliance | Unfathomable | Untamed | Other |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| CotA | 52 | 54 | - | - | 53 | 52 | - | 55 | - | 52 | - | - | - | 52 | - |
| AoA | 52 | 55 | - | - | 52 | 51 | - | 56 | - | 52 | - | - | - | 52 | - |
| WC | 78 | 60 | - | - | 53 | 53 | - | - | 53 | 57 | - | 61 | - | 53 | - |
| MM | - | 66 | - | - | 63 | - | - | 59 | 66 | 59 | - | 60 | - | 65 | - |
| DT | - | - | - | - | 63 | - | - | 60 | 60 | 59 | - | 63 | 62 | 61 | The Tide 1 |
| WoE | 67 | - | 57 | - | - | 61 | - | 56 | 59 | - | - | 57 | 57 | - | - |
| GR | 55 | - | 54 | 60 | - | 54 | - | - | - | - | - | 56 | 58 | 57 | 4 reprint strays |
| AS | 65 | 43 | 42 | 45 | 45 | 42 | - | - | - | - | 47 | - | - | - | - |
| ToC | - | 43 | - | 45 | 47 | - | 51 | - | - | 43 | 49 | - | - | 43 | - |
| MoMu | - | 74 | - | - | 68 | - | - | 66 | 71 | 67 | - | 68 | - | 70 | - |
| VM25 | 14 | - | 39 | 43 | 44 | 44 | - | - | 39 | 39 | - | - | 43 | - | - |
| PV | 14 | 47 | - | - | 42 | - | 41 | 40 | 42 | 40 | - | 40 | - | 41 | Prophecy 22 |
| CC | 62 | 45 | - | - | - | 43 | - | 42 | 41 | - | 45 | - | - | 44 | Archon Power 28 |
| DM | - | - | 43 | 44 | - | 42 | - | - | - | 44 | 43 | - | 44 | - | Ouboros 43 |
| MG24 | 56 | 69 | 59 | 59 | 64 | 60 | - | 72 | 69 | 57 | - | 75 | 63 | 61 | Keyraken 27 |

## Keyword History (first standard-set appearance per pack JSON)

| Keyword | First appears | Notes |
| --- | --- | --- |
| assault, elusive, poison, skirmish, taunt | CotA | Core keywords |
| hazardous | CotA (single card: Briar Grubbling, hazardous:5) | expanded use in AoA |
| alpha, deploy, omega | AoA | not in CotA pack data |
| splash-attack, treachery | WoE | |
| versatile, assault:x, hazardous:x, splash-attack:x | GR | |
| entrench | DM | 17 cards, e.g. Waspscream |
