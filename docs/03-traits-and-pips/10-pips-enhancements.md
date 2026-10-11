# Bonus Pips, Enhancements, and Deck House Rules

How aember pips and DM-style enhancements are declared and resolved, plus house composition rules. Sources: `master-vault-data/packs/*.json`, `server/game/Card.js`, `server/game/GameActions/ResolveBonusIconsAction.js`, `server/services/DeckService.js`, `server/game/deck.js`, `server/constants.js`.

## Bonus amber pips

- Declared per-card as the numeric `amber` field in the pack JSON (values 0, 1, or 2; AoA introduced most of them, CotA had the first; DM's `reap-the-wild-wind` has `amber: 1`).
- `Card.bonusIcons` (`server/game/Card.js:265-279`) converts `amber` points into an equal number of `amber` bonus icons, then appends any `enhancements` from deck data.
- Resolution is centralized in `server/game/GameActions/ResolveBonusIconsAction.js`; icons resolve in order before "Play:" abilities fire (`server/game/BaseActions/BasePlayAction.js:49-72` fires the bonus event before the play event effects).

## Six bonus icon flavors (all resolved by ResolveBonusIconsAction.js)

- **amber** - gain 1 amber from the common supply (line 188).
- **capture** - capture 1 amber from your opponent onto a chosen friendly creature (lines 203-215).
- **damage** - deal 1 damage to a chosen creature; can destroy it (lines 228-239).
- **discard** - discard 1 card from any deck (lines 252-264).
- **draw** - draw 1 card (line 276).
- **power** - add a +1 power counter to a chosen creature (lines 290-312).

Replacements exist: check `ResolveBonusIconsAction.js:105` for the icon list, `cards/04-MM/AmphoraCaptura.js` (resolve any icon as capture), `cards/09-ToC/WhimsicalConjuror.js` (amber icon may make a token creature).

## Enhanced cards (set 14 DM "Enhance")

- DM cards print "Enhance [pips]" in their text (pack JSON `text` shows "Enhance ." plus invisible icon glyphs); the actual icons assigned to a specific physical card are recorded in per-deck data, not the pack JSON. DM.json's card entries have no `enhancements` field.
- Deck import pipeline: `server/services/DeckService.js:1316-1324` builds `enhancementsByCardId` from bonus icon data; `server/services/DeckService.js:1114-1121` persists them; `server/game/deck.js:36-38` copies `card.enhancements` into the in-game card data.
- Enhancement icon values can be one of the six flavors above, or a house name; house enhancements make the card count for an extra house via `Card.getHouseEnhancements()` (`server/game/Card.js:688-695`, "star alliance" -> "staralliance") and feed `player.getAvailableHouses()` (`server/game/player.js:778`).
- Engine effects can modify icons (`modifyBonusIcons`, used by copy-play in `server/game/helpers/playAsCopy.js:133`).

## Amber pips and analyzer math

- A card's total "bonus amber" on play = count of `amber` entries in `bonusIcons` (printed amber + amber enhancements). `cards/02-AoA/Rustgnawer.js:16` reads exactly this count when destroying an artifact with its Fight ability.
- DM's "Enhance" cards themselves: `game-changer` (Enhance), `reap-the-wild-wind` (Enhance, `amber: 1`), `conrad fisique`, `empathic malice`, `holographic-banshee` (DM.json).

## Houses, mavericks, anomalies, deck composition

- Houses are per-card via the `house` field in the pack JSON; `Constants.Houses` in `server/constants.js` lists: brobnar, dis, ekwidon, logos, mars, redemption, saurian, sanctum, shadows, skyborn, staralliance, unfathomable, untamed.
- A deck has exactly 3 houses (`deck.js:56` `result.houses = this.data.houses`). Turn house choice is any house represented by hand/battleline cards (`player.js:778-814`, `gamesteps/house/HousePhase.js`).
- `Card.getHouses()` (`Card.js:704-721`) computes a card's houses from printed house + house enhancements + `changeHouse` + `addHouse` effects; tokens use their token card's house (`player.js:787-788`).
- Mavericks/anomalies come from deck entries (`deck.js:21-25`), overriding the printed house at game build (`deck.js:108-111`). An anomalies are cards issued outside the normal house system and assigned one at deck level; mavericks are in-house prints from other sets.
- Deck composition: 36 cards, exactly 12 per house, no duplicate card names (except special standalone/expansion decks); prophecy cards (up to 4) and a token creature are deck-level extra entries (`deck.js:69-87`).
- "Flex" behavior for house lock bypass comes from card mechanics: versatile keyword, omni abilities, and `changeHouse`/`addHouse` effects - never from deck-level house counts.
