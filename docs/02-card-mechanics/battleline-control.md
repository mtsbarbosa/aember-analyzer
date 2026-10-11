# Battleline Control

Creatures live on a player's battleline, an ordered list accessible as `player.creaturesInPlay`. Position matters: creatures have neighbors, flanks (list ends), and a center (only for battlelines of odd length). All of these are mechanical targets for cards.

## Playing creatures into the battleline

- `putIntoPlay` / `playCard` actions add creatures to their controller's battleline. Default placement is a **flank choice prompt** (`server/game/GameActions/PutIntoPlayAction.js` shows flank/deploy handling).
- **Deploy** keyword creatures (or actions like `makeTokenCreature({ deploy: true })`) can be placed anywhere in the battleline (`deployIndex`).
- Creatures are played exhausted unless an effect reads them; `new` and `exhausted` flags are managed by the engine.

## Gigantic creatures

`server/game/GiganticCard.js` implements the "play only with the other half" mechanic:

- Each half is a separate card; a `GiganticCard` is either `giganticBottom` (bottom half) or not (top half).
- While **not composed**, the top half owns play restrictions and the engine pulls the bottom half's printed power via `powerForPlayRestriction`.
- Base `setupCardAbilities` installs a persistent `cardCannot('play')` effect that blocks playing either half **from hand** unless both halves are in the hand (`this.compositeId`). Playing from other zones (discard via Exhume-style effects) stays selectable, and the play fizzles if allowances cannot cover both halves.
- Composition is enforced by play/`PutIntoPlay` logic; `composedPart` links the halves. Purging a composed gigantic from discard prompts "Choose which half to purge" (`server/game/GameActions/PurgeAction.js`).

Example: `(server/game/cards/04-MM/Deusillus.js)` (plus `Deusillus2.js`) - top half implements `Play: Capture all of your opponent's amber...` and shared Fight/Reap text; the class only extends GiganticCard and calls `super.setupCardAbilities` for the composition guard.

## Destroying and cascades

- `destroy` (`server/game/GameActions/DestroyAction.js`) tags a card `moribund` and keeps it in play while the destroyed-ability window resolves; it leaves play via `onCardLeavesPlay` only after the window closes. See docs/card-abilities.md "Destruction timing" for the batching rules.
- `sacrifice` bypasses destruction-prevention effects ("cannot destroy" restrictions) - a player-initiated exit.
- When destroying every creature (`target: context.game.creaturesInPlay`), treat destruction as simultaneous: all `Destroyed:` triggers assemble into one ordering prompt.

## Bouncing (returning to hand)

`returnToHand` moves a card to its **owner's** hand regardless of current controller. Cards entangled with it (upgrades, child cards) resolve their own `onLeavesPlay` first.

- `(server/game/cards/01-Core/NaturesCall.js)` - "Return up to 3 creatures to their owners' hands": `target: { mode: 'upTo', numCards: 3, cardType: 'creature', gameAction: ability.actions.returnToHand() }`.
- `(server/game/cards/01-Core/BadPenny.js)` - `Destroyed: Return this card to your hand`, the canonical "bounce myself instead of dying" pattern.
- Distinct variants: `returnToHand({ location: 'discard' })` calls (e.g. `(server/game/cards/01-Core/Regrowth.js)`) return cards from discard to hand; `returnToDeck({ bottom: true, shuffle })` sends to deck (`server/game/GameActions/ReturnToDeckAction.js` also auto-shuffles the discard in when returning a whole discard pile).

## Swap and repositioning

- `swap` (`server/game/GameActions/SwapAction.js`) exchanges two cards' battleline positions. `(server/game/cards/04-MM/TeleporterChiefTink.js)` swaps itself with a friendly creature and grants `canUse` on the swapped creature.
- `moveToFlank({ left })` forces a creature to a flank; `moveOnBattleline` / `rearrangeBattleline` let players re-order (e.g. `(server/game/cards/05-DT/TheMysticeti.js)` "move it anywhere in your battleline").
- Position-only effects leave upgrades attached: moving a creature carries its engine state (exhausted, tokens).

## Taunt (neighbor protection)

Taunt is a stat-like **keyword** whose protection is an engine-installed persistent effect, not per-card text (`server/game/Card.js`, lines ~329-337):

```javascript
match: (card) => this.neighbors.includes(card) && !card.getKeywordValue('taunt'),
effect: ability.effects.cardCannot('attackDueToTaunt')
```

- A creature taunts its neighbors unless those neighbors also taunt; the attacker's fight target selection filters `attackDueToTaunt` failures (`server/game/GameActions/ResolveFightAction.js` checks `attacker.ignores('taunt')` first).
- Taunt can be granted/removed dynamically: `(server/game/cards/02-AoA/Gub.js)` `gains taunt while not on a flank`; `(server/game/cards/01-Core/NiffleGrounds.js)` removes taunt+elusive for the turn; `(server/game/cards/01-Core/NiffleApe.js)` `ignores('taunt')` while attacking.

## Taking control of creatures

Control is an effect, not a flag: `ability.effects.takeControl(player)` (`server/game/effects.js`), resolved through `game.takeControl` (`server/game/game.js`, line ~1199), which also honors `takeControlOn`/`takeControlOnLeft`/`takeControlOnRight` for placement.

- A controlled creature departs with the effect. When a `lastingEffect` control effect expires, control returns to the default controller (`Card.controller` falls back to `defaultController`, `server/game/Card.js` line ~1399).
  - `takeControl` can be blocked per card by `allowGameAction('takeControl')` restrictions.
- `(server/game/cards/02-AoA/Hypnobeam.js)` - `Play: Gain control of an enemy creature` -> `cardLastingEffect({ duration: 'lastingEffect', effect: takeControl(context.player) })`, i.e. control lasts while that specific copy is in play.
- `(server/game/cards/01-Core/CollarOfSubordination.js)` - an enemy upgrade whose whole text is `whileAttached({ effect: takeControl(() => this.controller) })`; control holds as long as the upgrade stays attached.
- `(server/game/cards/14-DM/EvenSwap.js)` - mutual control exchange (one lasting effect each way).

Engine consequences to replicate:

- Amber captured on a taken creature reads from its controller, not owner (see aember-manipulation.md).
- A controlled creature can be played/used only when its controller's house checks pass; house checks see the original deck's houses.
- Take-control is commonly paired with `changeHouse` so the taker can actually activate the stolen card (e.g. `(server/game/cards/04-MM/Borrow.js)` takes an enemy **artifact** with double lasting effects: `takeControl` plus a `changeHouse('shadows')` effect that self-cancels when control reverts).

## Token creatures

Tokens are not summoned from nothing: `makeTokenCreature` (`server/game/GameActions/MakeTokenCreatureAction.js`) takes the **top cards of the player's deck** and converts them into token creatures.

- The deck's registration includes a `tokenCard` - a deck-independent card of type `token creature` found by `server/game/deck.js` (`c.card.isNonDeck && c.card.type === 'token creature'`) and stored on `player.tokenCard`.
- Conversion is a `cardLastingEffect` with `flipToken`, `changeType('creature')`, and `copyCard(event.player.tokenCard, false)` effects, then `putIntoPlay` renders it (with deploy prompt when requested). The underlying deck card is consumed; the token inherits name/houses/abilities of the token identity.
- Card text often gates on the token identity: `(server/game/cards/09-ToC/Suzerain.js)` checks `context.player.tokenCard.name === 'Minion'` before making a Minion; `(server/game/cards/06-WoE/BlorbHive.js)` requires the 'Blorb' token and counts `card.name === 'Blorb'` creatures.
- "Universal" token support means any deck can run token-identity cards regardless of houses set - the token creature's houses come from the token card, and cards that generate tokens never check the token's house for playability of the generator itself.
- `(server/game/cards/06-WoE/InspiringOration.js)` shows amount scaling: `makeTokenCreature(() => ({ amount: preThenContext.target.amber }))` after an `exalt`.

## Multiple-house and anomaly notes

- `Card.getHouses()` unions printed house, enhancements, and `changeHouse` effects (`server/game/Card.js` line ~707).
- Anomaly cards (no printed house) adopt whatever house set the deck supports; token creatures' houses come from the token card. Use `card.hasHouse(house)` rather than string equality on `card.house`.
