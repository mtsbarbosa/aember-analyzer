# Traits, Keywords, and Enhancements

Reference for the mechanical keywords, flavor traits, and enhancement/bonus systems in KeyForge, as implemented in the keyteki engine. Sources: `docs/keywords.md`, `server/game/Card.js`, `master-vault-data/packs/*.json` (15 set files, CotA through DM).

## Keywords

Keywords are mechanical terms printed in the "keywords" array of each card's JSON. The engine parses them in `Card.js` (`setupKeywordAbilities`, `Card.js:293` onward): each `keyword:value` string is stored in `printedKeywords` and wired into a persistent effect that calls `ability.effects.addKeyword(...)`. Card code should query them with `card.hasKeyword(name)` / `card.getKeywordValue(name)` (`Card.js:1054-1064`) and must NOT reimplement them.

Distinct keywords found in the pack JSONs (13 total, verified by extracting all `.keywords[]` entries):

- alpha
- assault (values 2, 3, 4, x)
- deploy
- elusive
- entrench
- hazardous (values 1-6, x)
- omega
- poison
- skirmish
- splash-attack (values 1-5, x)
- taunt
- treachery
- versatile

### How each keyword works

- **Alpha** - You can only play this card as the first thing you do this step. Enforced as a play cost in `server/game/costs.js:99`: the play fails unless `game.firstThingThisPhase()` (`server/game/game.js:1612`) reports nothing has been played, used, or discarded this phase. Example: Daat, eureka (AS.json).
- **Assault X** - Before this creature attacks, deal X damage to the attacked creature, before normal fight damage is exchanged. Implemented as an `interrupt` on `onFight` in `Card.js` (title "Assault"); if the defender dies from assault damage the fight does not proceed and "after fight" abilities do not trigger, but the creature still counts as having fought.
- **Deploy** - Creature may be placed anywhere in the battleline, not just a flank. Checked in `server/game/GameActions/PutIntoPlayAction.js:128` when building the "Deploy Left / Deploy Right / Left / Right" prompt.
- **Elusive** - The first time this creature is attacked each turn, neither creature deals combat damage. Tracked per turn via the `elusiveUsed` property and checked in `server/game/GameActions/ResolveFightAction.js:96`; the flag resets with turn structure.
- **Entrench** - During your ready cards step, you may choose for this card to remain exhausted instead of readying. Player-choice prompt in `server/game/gamesteps/ReadyPhase.js:18` (`promptForEntrenched`). Introduced with set 14 DM (Waspscream, DM.json); the keyword id in code/JSON is `entrench`.
- **Hazardous X** - Before this creature is attacked, deal X damage to the attacker, before normal fight damage is exchanged. Interrupt on `onFight` where `event.card === context.source` (`Card.js:311`). If the attacker dies from hazardous damage the fight does not proceed.
- **Omega** - After you play this card, end this step (you cannot play, use, or discard more cards). Handled in `server/game/BaseActions/BasePlayAction.js:52` and in copy-play helper `server/game/helpers/playAsCopy.js:134`.
- **Poison** - Any damage this creature deals to another creature in a fight is enough to destroy it. Checked in `server/game/GameActions/ApplyDamageAction.js:52` via `damageSource.getKeywordValue('poison')`.
- **Skirmish** - When this creature fights, it does not take damage from the creature it fights (its own damage still lands). Checked in `ResolveFightAction.js:101`.
- **Splash-Attack X** - When this creature attacks, deal X damage to each of the attacked creature's neighbors. Handled after main fight damage in `ResolveFightAction.js:132` via `splash-attack` keyword value; creatures killed by splash damage count as destroyed fighting the attacker.
- **Taunt** - Enemies can only attack this creature's neighbors if those neighbors also have taunt. Implemented as a persistent effect in `Card.js:332` that marks non-taunt neighbors as untargetable by attacks (`GameActions/ResolveFightAction` and fight targeting honor the block; see also `cards/04-MM/LadyLoreena.js` and `cards/07-GR/LoyalPackmate.js` for cards that refer to it). Multiple taunt creatures extend the "wall" that must be broken first.
- **Treachery** - This card enters play under your opponent's control. Persistent effect calling `ability.effects.entersPlayUnderOpponentsControl()` in `Card.js:412`.
- **Versatile** - This card can be played or used regardless of the house you chose this turn. Persistent effect `ability.effects.canUse(...)` in `Card.js:425` exempts the card from house-restriction checks.

### omni abilities (not a keyword)

"Omni:" is not collected in the `keywords` array; it is an ability prefix. In card code, `this.omni({...})` (`Card.js:539`) marks the action ability so the prompt reads "Use this card's Omni ability" (`server/game/CardAbility.js:43`). Omni abilities may be triggered while the card is stunned (costing the standard stun-removal route) and come from artifacts/creatures in any house; the house restriction is waived because an Omni ability belongs to the card, not to your chosen house. There are ~174 "Omni:" abilities across the pack JSONs. Engine manually granted keywords (via `addKeyword`) also exist for: elusive, skirmish, taunt, hazardous, assault, poison, invulnerable, omega, versatile.

### invulnerable (grantable keyword)

`invulnerable` is not printed on any card in the current pack data, but it is a real engine keyword: cards can grant it with `ability.effects.addKeyword({ invulnerable: 1 })` (see `cards/14-DM/Zomok.js` (ignores it), `cards/14-DM/EmperorMemrox.js`, `cards/03-WC/Ghostform.js`, `cards/11-VM25/TreokTheWise.js`). An invulnerable creature cannot be dealt damage, destroyed, or sacrificed while the keyword persists (`Card.js:399`, effects `cardCannot('damage' | 'destroy' | 'sacrifice')`); it can still be returned to hand, archived, moved, etc. Other card abilities can "ignore" it via `ability.effects.ignores('invulnerable')` (e.g., Zomok ignoring taunt, elusive, poison, hazardous, invulnerable while attacking).

Not present in keyteki card data or engine: armor keywords, "stealth" (that is a card name, `cards/03-WC/StealthMode.js`; there is no stealth keyword), ward (ward lives in card text and `cardCanPlaceAmber`/ward actions, not keywords), "complex/vivid/gambit" (those are DM card-set designators, not keywords), and "maverick/anomaly" (deck-build labels, see below).

`docs/keywords.md` also documents granted-only keywords such as exalt/graft/haunted/overwhelmed behavior; those appear in card text but are not `keywords`-array entries.

## Traits

Traits (for example Martian, Thief, Mutant, Cyborg, Scientist) are flavor/tribal tags in the `traits` array of each card's JSON. They have no inherent rules by themselves; they only matter when other card texts reference them.

Engine handling: `Card.hasTrait(trait)` / `Card.getTraits()` (`server/game/Card.js:662-686`). Traits are compared case-insensitively and include dynamically added traits from `getEffects('addTrait')`; `removeAllTraits` effects can blank them, and copy effects (`copyCard`) can replace the printed set. Card implementations hook traits through `card.hasTrait('mutant')` style conditions, `trait:` matchers in ability targets, and `chooseTrait` selectors. The most-referenced traits in card code: mutant (55 files), shard (11), dinosaur (9), robot (8), giant (8), sin (7), niffle (6), knight (6), specter (5), clock (5), beast (5).

Distinct trait count: 99 raw strings were extracted from all pack JSONs, but one is a data defect: DT.json contains the literal string `"elf "` with a trailing space, which is a duplicate of `elf`. The real distinct trait count is 98 (97 named traits plus `[redacted]`, a Logos redaction-joke trait - literally the string "[REDACTED]" - on REDACTED cards in AoA/MM/MoMu/PV).

The full traits list with families, houses, and intro sets is in `../03-traits-and-pips/00-index.md` and the per-family files in that directory.

Set abbreviations (expansion ids in `server/constants.js:37`): CotA 341, AoA 435, WC 452, MM 479, DT 496, WoE 600, GR 700, AS 800, ToC 855, MoMu 874, PV 886, CC 918, DM 928, VM2025 939, DISC 907 (unused in pack data).

## Enhancements and bonus pips

### Bonus amber (bonus icon)

- Declared per-card as the numeric `amber` field in the card JSON (0, 1, or 2; CotA and AoA introduced them).
- At deck import, `amber` becomes "bonus amber" pips on the deck viewer. In game, `Card.bonusIcons` (`Card.js:265-279`) turns each point of printed amber into an `amber` bonus icon.
- When a card is played, its bonus icons resolve first (amber gain, capture, damage, discard, draw, power), then "Play:" effects happen. All icon resolution lives in `server/game/GameActions/ResolveBonusIconsAction.js`: amber icons gain 1 amber (to your pool), capture icons capture 1 amber from the opponent onto a chosen friendly creature, damage icons deal 1 damage to a chosen creature, draw icons draw 1 card, discard icons discard 1 card from any deck, power icons add a +1 power counter to a creature. Replacements are supported (e.g., `cards/04-MM/AmphoraCaptura.js` resolves any bonus icon as capture instead).
- In the deck analyzer sense: the `amber` field in the pack JSON is the per-card bonus amber count (e.g. `reap-the-wild-wind` in DM.json has `amber: 1`).

### Enhanced cards (set 14 DM "enhance")

- DM's "Enhance" mechanic prints generic pips on the card text; each physical card in a specific deck had icons assigned. Declared in deck data as an `enhancements` array per card (not in the static pack JSON - DM.json contains no `enhancements` field; enhancement assignment is per-deck).
- Deck import: `server/services/DeckService.js:1316` (`enhancementsByCardId`) reads bonus icon data, and `server/game/deck.js:36` copies `card.enhancements` into the game's card data.
- In game, `Card.bonusIcons` (`Card.js:275-278`) appends `enhancements` to the printed amber pips: enhancement icons include amber, capture, damage, draw, discard, power, and also house enhancements (`getHouseEnhancements`, `Card.js:688`, normalizes "star alliance" -> "staralliance" etc.; a house enhancement lets the in-play card count for that house per `player.getAvailableHouses` in `server/game/player.js:778`).
- The card text shows "Enhance ." with the icons; engine runtime behaves exactly like other bonus icons per `ResolveBonusIconsAction.js`. Enhance-cost examples: `game-changer`, `reap-the-wild-wind` (amber 1), `holographic-banshee` (DM.json).

### Amber pips and the damage/draw/capture bonus icons

Bonus pips come in six flavors, all resolved at play time by `server/game/GameActions/ResolveBonusIconsAction.js`:

- **Bonus amber** (+1 amber / "+" icon) - you gain 1 amber from the common supply. Declared as the numeric `amber` field in the pack JSON (0, 1, or 2) or as `enhancements` entries from per-deck data. Both contribute to `Card.bonusIcons` (`Card.js:265-279`).
- **Bonus draw** ("D" icon) - draw 1 card (`DrawAction` with `bonus: true` in `ResolveBonusIconsAction.js:276`).
- **Bonus capture** ("C" icon) - capture 1 amber from your opponent onto a chosen friendly creature (`ResolveBonusIconsAction.js:203-215`).
- **Bonus damage** ("damage"/"crab" icon) - deal 1 damage to a chosen creature (`ResolveBonusIconsAction.js:228-239`); this is real damage and can destroy creatures, dealt on behalf of the played card.
- **Bonus discard** - discard 1 card from any deck (`ResolveBonusIconsAction.js:252-264`).
- **Bonus power** - add a +1 power counter to a creature (`ResolveBonusIconsAction.js:290-312`).

The "amber pips" on action/artifact cards and the "+" icons are the same mechanic; there is no separate object. Cards with bonus amber are worth +N amber when played - notable for analyzer math like `Rustgnawer` destroying an artifact and gaining its bonus amber count (`cards/02-AoA/Rustgnawer.js:16`).

## Houses, deck composition, mavericks, and flex

- Houses are per-card, declared as the `house` field in the card JSON. `Constants.Houses` (`server/constants.js`) lists the playable houses: brobnar, dis, ekwidon, logos, mars, redemption, saurian, sanctum, shadows, skyborn, staralliance, unfathomable, untamed (plus post-set-14 labels like "staralliance" appearing in enhancements).
- A deck has exactly 3 houses. Deck composition in `server/game/deck.js` reads `data.houses` and builds one deck per deck-data entry; house choice for a turn is any house represented by cards in hand or in play (`server/game/player.js:778`, `gamesteps/house/HousePhase.js`).
- Cards count for a house based on `Card.getHouses()` (`Card.js:704`), which combines the printed house, house enhancements, `changeHouse` effects, and `addHouse` effects. Tokens use their token card's house. Runtime "anomaly/flex" additions work through `addHouse`/`changeHouse` card effects.
- Mavericks and anomalies: declared on the deck-entry (`deck.js:21-25`, `card.maverick` / `card.anomaly`), and at game-build time they override the card's house (`deck.js:108-111`). A maverick card is a card from another set/house printed into this deck; an anomaly is a card that printed outside houses and is assigned one.
- Flex: "flex" cards (deploy-eligible or cross-house cards like versatile) work by the keyword effects above, not by deck house counts. Omni abilities also bypass the house lock; house lock is enforced on play/use actions, not on house choice itself.
- Deckbuilding: exactly 3 houses, 12 cards each = 36 cards, no duplicates (copies exist only in special standalone decks); props/decree/variants are per-deck, not per-set.

## Data-model cheat sheet (pack JSON per card)

- `id`, `name`, `number`, `image`, `locale` - identity.
- `expansion` - set id, maps to `Constants.Expansions` in `server/constants.js`.
- `house` - printed house (lowercase key of `Constants.Houses`).
- `keywords` - array, optional `:value` suffix for numeric keywords.
- `traits` - array of flavor tags (no leading house names; not the same as houses).
- `type` - action, artifact, creature, upgrade, event (plus deck-only types like token creature and prophecy in newer sets).
- `amber` - bonus amber pips.
- `armor`, `power` - creature stats (null for other types).
- `rarity` - Common/Uncommon/Rare/Special/Variant/Prophecy.
- `text` - card text; includes "Enhance \[pips\]." and "Omni:" lines.
- `enhancements` - NOT present in pack JSON; comes from per-deck data via DeckService/deck.js.

Per-set house keys used inside the JSON: brobnar, dis, ekwidon, logos, mars, redemption, saurian, sanctum, shadows, skyborn, staralliance, unfathomable, untamed. Geistoid/ouboros/keyraken appear only as trait-family strings in certain episode decks, not as playable houses.
