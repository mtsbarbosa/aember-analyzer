# KeyForge Core Rules - Game Overview

Source of truth: the keyteki engine implementation (`C:\Users\Mateus\work\keyteki`), cross-checked
against the official KeyForge Master Rulebook where noted. All paths below are relative to the
keyteki repository root.

This file is written for an implementer building a KeyForge engine. It defines WHAT the game is.
Phase-by-phase order of play lives in `01-turn-structure.md`; the exhaustive decision/window spec
lives in `02-phases-and-decisions.md`; setup detail lives in `04-mulligan-and-setup.md`.

---

## 1. What KeyForge is

- KeyForge is a two-player, unique-deck card game. There is NO deck building: every deck is a
  fixed, pre-constructed 36-card deck produced by the game's algorithmic deck generator.
- A deck contains exactly 12 cards from each of exactly 3 houses (server/game/deck.js:56,
  `result.houses = this.data.houses`).
- Players bring one deck each. Win condition: be the first to forge 3 keys (see section 8).
- Each deck also carries an "Archon identity card" which is a card back/identity, not a play card,
  plus optionally a "38th card" reference card. Neither is ever shuffled into the deck (official
  rulebook; keyteki models this by only rendering `deckData`).
- Because decks are unique and fixed, out-of-play zones and the deck itself are the "identity" of
  the game; keyteki identifies a deck by `deckData.uuid` and `deckData.name`
  (server/game/gamesteps/setup/setupphase.js:34-38).

Official rulebook note (not in code): decks are shuffled at setup and offered to the opponent for
additional shuffling / a final cut. keyteki only shuffles its own deck
(server/game/gamesteps/setup/setupphase.js:80).

## 2. Deck anatomy as modeled by keyteki

`Deck.prepare(player)` (server/game/deck.js:49-90) builds:

- `houses` - the deck's 3 houses exactly as stored in deck data.
- `cards` - every deck-listed card instantiated once per copy (`count` expanded), all located in
  `'deck'`. Non-deck cards are excluded here (`isNonDeck` filter).
- `tokenCard` - the deck's back-side "token creature" card (type `'token creature'`,
  `isNonDeck`), used to generate token creatures in play (server/game/deck.js:70-77). Supported
  from the WoE set onward (tokenRequired flag, server/constants.js:43).
- `prophecyCards` - up to 4 `prophecy` cards per deck, each initialized with
  `activeProphecy = false` (server/game/deck.js:79-87). Prophecies are supported in GR and PV sets
  (prophecySupported flags, server/constants.js:44,56).
- Tide decks (Dark Tidings, expansion 496) carry a single synthetic non-deck card with id
  `the-tide` (`isNonDeck: true`), added at deck import for any `tideRequired` expansion
  (server/services/DeckService.js:720,884-890; the card definition lives in DT.json). Like the
  token card, it is never shuffled into the deck and never enters play - it represents the Tide
  marker shown in the UI. The actual game state is a `Game`-level pointer `game.highTide`
  (`null` = neutral, player = high tide for that side), not a card.
- Deck data also can include other non-deck back cards such as archon powers (`isNonDeck`).

Per-card deck data variants handled at instantiation (server/game/deck.js:21-44):

- `maverick` - a maverick: the card's house is overridden by the maverick house.
- `anomaly` - same override mechanism for anomaly cards.
- `house` - deck-list house override (used for maverick/anomaly and alliance-built decks).
- `enhancements` - enhanced bonus-icon pips (sets 11+ / "enhanced" cards).
- `isNonDeck` - back-of-deck cards (token creature, prophecy) that never enter the shuffled deck.

## 3. Card types

Modeled card types in play (`Card` and `DrawCard` classes, server/game/Card.js):

- `creature` - enters a battleline row; has power and armor; ready/exhausted state; may reap,
  fight, or use Action abilities when ready.
- `artifact` - enters the play area (not the battleline); persistent or Action abilities; like
  creatures, artifacts have ready/exhausted states - using an Action ability exhausts the
  artifact, and it readies in the ready phase (server/game/Card.js; artifact reuse/counters
  tracked the same way as creature exhaustion).
- `upgrade` - attaches to a creature; stored on the creature (`card.upgrades`), never in
  `cardsInPlay` itself (comment at server/game/player.js:20).
- `action` - one-shot effect from hand, goes to discard after playing.
- Engine-only types: `token creature` (created in play), `prophecy` (facedown reference card,
  activated during the main phase by its owner, server/game/gamesteps/main/actionwindow.js:87-107),
  and `the tide` (a marker card only; see section 2 - the Tide mechanic itself is board state, not
  a card). None of these are ever shuffled into a deck.

Hand-card action menus are enumerated by `Card.getActions(location, ...)`:
- From `'hand'`: PlayCreatureAction / PlayArtifactAction / PlayAction, plus
  PlayUpgradeAction when the card can play as an upgrade, plus an always-available
  DiscardAction (server/game/Card.js:1362-1389).
- From `'play area'` for creatures: Fight action, Reap action, Remove-stun action, plus the
  card's own printed Action/Omni abilities (server/game/Card.js:1378-1389).

## 4. Board layout (per player)

Per-player zones tracked in `Player` (server/game/player.js:19-35):

- `hand` - in-hand cards (private to the owner's view; visible only through explicit effects).
- `deck` - facedown draw pile; players may never look at it unless an effect says so.
- `discard` - faceup ordered discard pile.
- `purged` - cards removed from the game; order preserved.
- `archives` - facedown storage pile; cards are picked up into hand during the house phase
  (server/game/gamesteps/house/HousePhase.js:45-141).
- `cardsInPlay` - all creatures and artifacts the player controls. The order of creatures in
  cardsInPlay forms the player's battleline (one row). Upgrades live attached to their creature,
  not here.
- `keys` - 3 key flags named `red`, `blue`, `yellow` (server/game/player.js:276). Keys are forged
  one at a time and once forged stay forged (unless a card effect unforges).
- `amber` - the player's amber pool (resource to spend when forging).
- `chains` - handicap count reducing card-draw (server/game/player.js:34).
- `activeHouse` - house chosen this turn (set in the house phase, cleared at end of turn,
  server/game/game.js:1543).

Shared board state in `Game`:

- `highTide` - which player currently has "High Tide" (server/constants.js:60-65). Strictly a
  `Game`-level pointer, not a card. Levels per player: HIGH (`highTide === player`,
  player.js:1438-1440), LOW (`highTide` set and !== player, player.js:1442-1444), NEUTRAL
  (`highTide === null`). A player raises the tide by clicking the tide marker during their main
  phase, costing `3 + sumEffects('modifyTideCost')` chains and confirmed by a Yes/No handler
  menu, server/game/gamesteps/main/actionwindow.js:64-85; paying it applies chains (see
  `08-chains.md`). The tide resets to neutral ONLY via card effects (`ResetTideAction`, e.g.
  Trial by Water, 05-DT) or a manual host command - it never resets automatically between rounds.
- Prophecy cards of each player (`player.prophecyCards`, `game.activeProphecies`,
  server/game/game.js:1580-1585).

Board tokens rendered on cards:

- `power` - printed creature power plus counters/effects; a creature is destroyed by game-state
  check when `power <= 0` or `damage >= power` (server/game/game.js:1436-1444).
- `armor` - printed armor; leftover armor shows as temporary armor tokens each state check
  (server/game/game.js:1446-1451).
- `damage`, `amber` (captured), `stun`, `ward`, `enrage`, power counters, doom tokens, etc.
- Game state checks recompute controller changes from `takeControl` effects and terminal
  conditions after every action (server/game/game.js:1414-1455).

## 5. Houses

- A legal KeyForge deck uses 3 of the game's houses. keyteki currently knows 15 houses
  (server/constants.js:3-19): Brobnar, Dis, Ekwidon, Geistoid, Logos, Mars, Ouboros, Redemption,
  Sanctum, Saurian, Shadows, Skyborn, Star Alliance, Unfathomable, Untamed.
- Each turn the active player chooses exactly ONE active house from the houses they could use; a
  card of the active house may be played, used, or discarded. Which houses are selectable is
  computed by `Player.getAvailableHouses()` (server/game/player.js:778-815): the deck houses plus
  houses of cards in hand/cards in play (respecting `changeHouse` effects), filtered by
  `stopHouseChoice` / `restrictHouseChoice` effects.
- If no legal choice exists, the turn continues with NO active house
  (server/game/gamesteps/house/HousePhase.js:37-42).

## 6. Deck identities and game formats

keyteki does not model a "Legacy" identity card. Deck identity is the Archon identity card +
deck list stored in `deckData`; there is no second identity type in the code. Formats supported
(client game-creation list, client/Components/Games/GameFormats.jsx:13-20):

- `normal` - standard Archon format: each player brings one fixed deck (keyteki also accepts the
  synonym `archon` historical name in older files).
- `unchained` - play with chainsrehandicap handling unlocked (deck verification relaxed).
- `sealed` - decks are randomly generated at game start; `hideDeckLists`-style presentation.
- `reversal` - reversed board presentation variant.
- `adaptive-bo1` - Adaptive Best-of-1: both players may select a deck, chains are bid for the
  selected deck, decks can swap (server/game/gamesteps/setup/AdaptiveDeckSelectionPrompt.js,
  server/game/gamesteps/setup/ChainBiddingPrompt.js). See `04-mulligan-and-setup.md`.
- `alliance` - Unbound Alliance: a deck constructed from multiple decks' cards
  (server/lobby.js:861-863). It is treated specially during setup (no deck-list link message,
  server/game/gamesteps/setup/setupphase.js:40).

If your analyzer was told about a "Legacy" deck identity: treat it as an official-rules concept /
manufacturer identity scheme; keyteki engine code does not implement it. Flag it for a rules
lookup rather than engine code.

## 7. Amber, keys, and forging

- Amber is the only resource. It is gained by reaping, bonus icons, card effects, etc.; it can be
  captured onto creatures, stolen, transferred, and lost.
- Key cost: default 6 amber; modified by effects: `getCurrentKeyCost() =
  6 + sumEffects('modifyKeyCost')` (server/game/player.js:935-937).
- Forging: during the key phase, if `canForgeKey()` the key phase forges a key automatically -
  there is no "do you wish to forge?" prompt (server/game/gamesteps/key/KeyPhase.js:10-30).
- `canForgeKey` also accounts for:
  - restriction checks (a `'forge'` restriction and unforged keys remaining,
    server/game/player.js:884-901),
  - second-key permission `canForgeSecondKeyDuringKeyPhase` (KeyPhase.js:22),
  - alternative amber sources - amber tokens on cards and "card counts as amber" effects
    (`forgeAmberSource`, server/game/player.js:817-882),
  - forging with the opponent's pool, capped by effect values (`forgeWithOpponentsAmber`,
    server/game/player.js:920-933).
- A forged key is announced with `onForgeKey` (server/game/Events/types.js:612); card effects can
  also unforge keys (`onUnforgeKey`, server/game/Events/types.js:1159).

## 8. Win conditions

- Primary: forge all 3 keys. Engine check: after any state change, if
  `Object.values(player.keys).every(key => key)` that player wins
  (server/game/game.js:607-630).
- Special-card win schemes (e.g. cards that unforge keys, force loss of keys, or grant instant
  wins) hook the same events; they are covered per-card elsewhere. The engine hook points to
  extend are `onForgeKey` / `onUnforgeKey` and `checkWinCondition`.
- Concede: instant loss for the conceding player (game command `concede`,
  server/game/GameCommands.js:59-61).
- Disconnect/leave: a player who disconnects without finishing concedes by default
  (server/game/game.js:1386-1397).
- Time-limit matches (optional `useGameTimeLimit`) end with a FinalTurn and a tiebreaker ladder:
  each player with 6+ amber forges a key, then most keys -> most amber -> fewest chains ->
  most creatures -> first player (server/game/gamesteps/TimeLimit.js:40, game.js:543-601).
  Full flow in `01-turn-structure.md`.

## 9. "Check!"

At end of turn, if the finishing player could afford a key, the engine announces "Check!"
(one forge away notice) (server/game/game.js:1525-1528). This mirrors the official rule that a
player announces "Check!" when ending their turn able to afford a key.

## 10. Engine-facing facts worth memorizing

- Two distinct "turn" numbering systems: `game.round` increments only when both players have
  taken equal turns (server/game/game.js:1555-1560), while `player.turn` increments per player
  turn (server/game/player.js:298-304).
- Round-scoped tracking reset in `Game.endRound` (server/game/game.js:1525-1541):
  `cardsPlayedOrUsed`-style lists (`cardNamesPlayedOrUsed`, `cardsUsed`, `cardsPlayed`,
  `cardsDiscarded`, `omegaCard`) are per-round; `*ThisPhase` lists are per-phase via
  `resetThingsThisPhase` (server/game/game.js:1621-1628). `firstThingThisPhase`
  (game.js:1612-1619) powers Alpha-tracker style rules.
- `beginRound` on a Player only resets `keysForgedThisRound` (server/game/player.js:294-296);
  `endRound` clears each card's "new" flag and bumps the player's turn count.
- Amber pool is clamped at >= 0 (server/game/player.js:760-761), as is chains
  (player.js:763-765).
- Hand size is soft: `maxHandSize = 6 + sumEffects('modifyHandSize')` (player.js:774-776); you are
  never forced to discard down to 6 in the draw phase (official rule; refill draw only adds).
- Key colors matter: forging picks an unforged color; `keysForgedThisRound` limits multi-forging
  with `cannotForgeMoreThan2KeysInATurn` (server/game/player.js:902-908).
