# Hand and Turn Manipulation

## Turn structure and skipping

A turn is a fixed sequence of phases; the engine exposes them by name to card effects (`server/game/gamesteps/phase.js` line ~22 checks `getEffects('skipStep').includes(this.name)`):

- `main`, `ready`, `key`, `draw` (and setup-only phases). Skipping is implemented purely as `ability.effects.skipStep(stepName)`, scoped by the usual lasting-effect durations.
- **There are no extra turns in KeyForge.** Turn manipulation is limited to skipping a player's steps on upcoming turns, typically via `duringOpponentNextTurn`.
- Numbered-turn sensitivity is implemented as card conditions on turn counters/forge counts, not as phase hacks.

Examples:

- `(server/game/cards/01-Core/Miasma.js)` - "Your opponent skips the forge a key step on their next turn": `duringOpponentNextTurn({ targetController: 'opponent', effect: skipStep('key') })`.
- `(server/game/cards/03-WC/BookOfLeQ.js)` - "end your turn" is implemented with the same primitives: on the bad branch the card self-applies `untilPlayerTurnEnd({ targetController: 'current', effect: [skipStep('ready'), skipStep('draw')] })` and sets `game.endPhaseRightNow`, skipping its own remaining steps.
- `(server/game/cards/11-VM25/TheConstant.js)` - "Each player skips their draw cards step": symmetric targeting.

## Hand size

- Base hand size is 6, computed as `6 + sumEffects('modifyHandSize')` (`server/game/player.js` line ~775).
- `modifyHandSize` effects apply to **refills only** (the draw-cards step), not to mid-turn draws, and can be dynamic: `(server/game/cards/06-WoE/Aembitrage.js)` uses the function form `modifyHandSize(() => 1)` (a constant delivered through the flexible API); true state-driven functions appear on cards like `(server/game/cards/03-WC/Greed.js)`.
- Positive: `(server/game/cards/01-Core/Mother.js)` "During your draw cards step, refill your hand to 1 additional card" -> `modifyHandSize(1)`. `(server/game/cards/03-WC/Zenzizenzizenzic.js)` is +2 with a position condition (`isInCenter()`).
- Negative: `(server/game/cards/02-AoA/Streke.js)` "While Streke is not on a flank, your opponent refills their hand to 1 less card" -> `persistentEffect({ targetController: 'opponent', condition: not on flank, effect: modifyHandSize(-1) })`.
- Hand size effects stack additively like key cost modifiers; capacity enforcement is at refill time (`DrawAction.getAmountAndShedChains` uses `player.maxHandSize`).

## Drawing extra cards

- Mid-turn draws are ordinary `draw` actions; they do not interact with refill math or hand size caps.
- Refill-time draws also **shed one chain per turn** when applicable (see Chains below), implemented in the same `DrawAction`.
- Effects can draw for the opponent - lethal-mill style engines should note the auto-recycle (empty deck shuffles discard back in, `player.drawWithEmptyDeck`), so drawing can never kill, only recycle and reveal information.

## Chains

Chains are a persistent draw-tax charged to a player, tracked in `player.chains`:

- `gainChains({ amount })` (`server/game/GameActions/ModifyChainsAction.js`) adds; chains **persist across turns** and are only shed through refill draws: after a full refill with chains apt, one chain is shed (`DrawAction` lines ~42-83).
- The per-turn draw reduction is `Math.floor((chains + 5) / 6)`: with chains 1-5 you draw one fewer card; chains 6-10 draw two fewer, and so on. Refill still counts as "up to max hand size - held cards - reduction" and one chain is shed only when the player had chains and the refill effect applied (`shedChains`).
- Large chain payloads also show up pushed with other effects (e.g. `(server/game/cards/01-Core/EffervescentPrinciple.js)` pairs a global amber wash with `gainChains()` as the cost).

## Playing cards outside the active house

House legality per card is `card.getHouses()` intersecting the active house; both sides are modifiable:

- `changeHouse(house)` effect adds a house to a card permanently while the effect lives: `(server/game/cards/03-WC/AcademyTraining.js)` "If you control this creature, it belongs to house Logos" -> `whileAttached({ match: owner is controller, effect: changeHouse('logos') })`; `(server/game/cards/04-MM/Borrow.js)` turns a taken enemy creature into a Shadow so you can actually use your theft.
- `canUse`/special play grants: effects that make specific cards usable regardless of house, like `(server/game/cards/01-Core/TirelessCrocag.js)` "You may use Tireless Crocag as if it belonged to the active house" -> `ability.effects.canUse(...)`.
- House **enhancements** (bonus icons in card data) make a card belong to two houses simultaneously for play and use purposes; see docs/card-properties.md. Implement engines to union printed house + enhancement house + `changeHouse` list.
- Alpha keyword ("play only during your first turn each round") is a play-window restriction, not a house restriction - it is enforced by abilities that check turn state, even for copied cards (see docs/card-abilities.md `buildPlayAsCopyEffects` note).

## Playing from other zones

- `putIntoPlay` resolves a card straight into a battleline/artifact slot from **any named zone** (hand, discard, archives, purged) - the card never passes through "hand" mechanics, so hand-size, entropy scrapping, and hand-reaction triggers don't fire.
- Strictly "play" (pays play costs, triggers play abilities, counts as cards played) is `playCard`-family actions with `location` sources for discard/archive/purged origins. Engines must keep the two distinct.

## Card text touching turns on other players' turns

Prophecy cards (`prophecyInterrupt` / `prophecyReaction` in docs/card-abilities.md) are the only card slot that triggers **on the opponent's turn** at fixed windows ("At end of X's turn..."), and their fulfillment moves the prophecy card into play. Engine-wise this is a separate trigger queue keyed on the opponent's turn rather than a lasting effect, which matters for prevention effects.
