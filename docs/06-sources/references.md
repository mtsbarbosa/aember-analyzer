# Sources

Every aember-analyzer document was produced by reading the open-source **keyteki** implementation (KeyForge engine, https://github.com/keyteki/keyteki) so claims trace back to actual engine behavior, not memory of the rulebook.

## Primary source locations in the keyteki repo

| Area | Path | Used by |
|------|------|---------|
| Turn/phase engine | `server/game/game.js`, `server/game/gamesteps/*` (phase.js, setup/, draw/, house/, main/, key/, ReadyPhase.js, FinalTurn.js, TimeLimit.js) | 01-core-rules |
| Player state (amber, keys, chains, hand) | `server/game/player.js` | 01, 02 |
| Card/ability classes | `server/game/Card.js`, `baseability.js`, `cardaction.js`, `triggeredability.js`, `ThenAbility.js`, `DelayedEffect.js` | 01, 02 |
| Atomic game actions | `server/game/GameActions/*` (StealAction, CaptureAction, TransferAmberAction, DealDamageAction, DrawAction, PurgeAction, RaiseTideAction, ResolveBonusIconsAction, ResolveFightAction, ForgeAction, ...) | 02, 03, 06 |
| Ability windows/resolution | `server/game/gamesteps/abilityresolver.js`, `forcedtriggeredabilitywindow.js`, `DestroyedAbilityWindow.js`, `SimultaneousEffectWindow.js`, `AllocateDamagePrompt.js`, `AllocateAmberPrompt.js` | 01, 02 |
| Card implementations by set | `server/game/cards/01-Core` ... `14-DM` | 02, 03, 04 |
| Card metadata JSON | `master-vault-data/packs/*.json` (CotA, AoA, WC, MM, DT, WoE, GR, AS, ToC, MoMu, MG24, VM25, PV, CC, DM), `master-vault-data/standalone-decks.json` | 03, 04, 05, 07 |
| Card database generation | `tools/generate-card-db.mjs` (builds `07-card-database/*` from packs + implementations) | 07 |
| Deck import | `server/scripts/fetchdata.js` + `server/scripts/fetchdata/*`, `server/services/DeckService.js`, `server/services/CardService.js`, `server/api/*decks*` | 05 |
| Pre-existing authored docs | `docs/card-abilities.md`, `docs/game-actions.md`, `docs/card-properties.md`, `docs/card-messages.md`, `docs/keywords.md`, `docs/new-sets.md` | 02, 03 |

## Method notes

- Engine behavior was treated as ground truth; official rulebook concepts were layered on top where the code was silent (flagged inline in the docs, e.g. deviations around chains on opening hands and the exact first-player choice flow).
- Trait and keyword statistics were machine-extracted from all 15 pack JSONs (13 keywords, 98 distinct traits; `"elf "` in DT.json is a known trailing-space duplicate).
- Where a mechanic exists in only some formats (tide costs, adaptive bidding, enhancements in DM), the document says which sets/formats it applies to.

## Out-of-band references (not read during authoring, consult if needed)

- Archon Arcana community wiki: https://archonarcana.com (rules glossary, per-card rulings).
- Official KeyForge rulebook PDFs per set (dark-horse/Fantasy Flight official rulebooks).
