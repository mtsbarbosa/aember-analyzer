# Artifact, object, and location traits

Traits found on artifacts, upgrades, and action/place abstracts. Verified against pack JSON extraction. Object traits usually appear on artifact-type cards and are the second-most hooked group after mutant.

- **arm** (first DT; houses: unfathomable). Disembodied arm (Deep); part-of-creature tag; no hooks.
- **buggy** (first GR; houses: ekwidon). Dune-buggy vehicle species of Ekwidon; hook: `cards/07-GR/ConductorJarroya.js` checks `hasTrait('buggy')`.
- **clock** (first GR; houses: geistoid). Gear-clock part objects; strong machine-synergy hooks: `cards/10-MoMu/AncestralTimekeeper.js`, `cards/10-MoMu/Chronometer.js`, `cards/10-MoMu/Synchronizer.js`, `cards/10-MoMu/TickTock.js`.
- **cloud** (see 03-creature-types.md).
- **curse** (first CC; houses: skyborn, brobnar, ekwidon, geistoid, mars, staralliance, unfathomable, untamed). Curse artifact tag; CC curse engine is the main user (curses are artifacts with hex-themed effects). No direct `hasTrait('curse')` hooks; card texts count curse cards instead.
- **equation** (first AoA; houses: logos). Abstract equations; flavor only.
- **item** (first AoA; houses: almost all). Generic object tag; broadest object trait. Refers in texts; no direct hooks.
- **law** (first AoA; houses: sanctum, saurian, staralliance, redemption). Legal abstract cards (decree flavor); no direct hooks.
- **location** (first AoA; houses: almost all). Places as card concepts; no direct hooks.
- **plan** (first GR; houses: mars, shadows). Heist/scheme abstracts (GR); no direct hooks.
- **power** (first AoA; houses: almost all). Abstract "power" wording tag; no direct hooks.
- **quest** (first AoA; houses: sanctum). Adventure abstracts (Sanctum quests); hook: `cards/02-AoA/EpicQuest.js` era cards reference quests in text.
- **shard** (first AoA; houses: brobnar, dis, logos, mars, sanctum, shadows, untamed, ekwidon, geistoid, skyborn). Shard artifact tag; the crystals that AoA cards care about. Hooks: 10 shard cards themselves (`cards/02-AoA/ShardOf*.js`, `cards/04-MM/ShardOfResurgence.js`) count friendly shards in play for their "gain 1 amber for each friendly Shard" style effects.
- **ship** (first AS; houses: many). Starships/boats; no direct hooks.
- **sin** (first CC; houses: dis). The Seven Sins as artifact creatures (Dis); hooks: all 7 sin cards themselves (`cards/13-CC/Desire.js`, `Envy.js`, `Gluttony.js`, `Greed.js`, `Pride.js`, `Sloth.js`, `Wrath.js`).
- **treasure** (first AS; houses: skyborn, ouboros, logos, saurian, shadows, untamed). Loot artifacts; no direct hooks.
- **variant** (first PV; houses: brobnar). Variant tag (PV); one hook file.
- **vehicle** (first AoA; houses: brobnar, dis, shadows, geistoid, unfathomable, ekwidon, staralliance). Ground transport tags; no direct hooks.
- **weapon** (first AoA; houses: brobnar, dis, logos, mars, shadows, sanctum, redemption). Armaments (upgrade/artifact weapons); no direct `hasTrait('weapon')` hooks; weapon cards count themselves in text.
