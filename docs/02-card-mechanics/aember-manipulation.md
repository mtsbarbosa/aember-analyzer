# Amber Manipulation

Amber exists in three places: a player's **pool**, **amber tokens on creatures/artifacts**, and the **common supply**. Cards move amber between these in five main categories: steal, capture, transfer, exalt, and pool modification (gain/lose), plus removal helpers (place/return).

## Steal

`steal` (`server/game/GameActions/StealAction.js`) moves amber from the opponent's pool directly to the acting player's pool.

- Default amount is 1; `amount` is capped at the opponent's current amber at event creation (`Math.min(this.amount, player.amber)`).
- The action cannot affect a player with 0 amber: `canAffect` requires `player.amber > 0` and `this.amount > 0`. If there is no legal target the whole action is skipped silently.
- Steal respects the `steal` ability-restriction keyword: `checkEventCondition` calls `player.checkRestrictions('steal', context)` and prints a prevention message when a card blocks the steal (e.g. "steal cannot" effects).
- The stolen amber is removed via `event.player.modifyAmber(-event.amount)` unless the opponent has a `stealFromPool` effect (a rare "amber counts as pool" case), then the acting player gains the amount with `gainAmber`.
- Raises `onStealAmber`, which other cards can react to.

Examples:

- `(server/game/cards/02-AoA/BrendTheFanatic.js)` - `Destroyed: Steal 3A` implemented as `this.destroyed({ gameAction: ability.actions.steal({ amount: 3 }) })`. The card's `Play:` side gives the opponent 1 amber, showing how steal cards can combine relocation and push.
- `(server/game/cards/01-Core/BaitAndSwitch.js)` - "If your opponent has more amber than you, steal 1A. Repeat if they still have more." Implemented with a `condition` on the outer ability and an `alwaysTriggers: true` `then` block with the same condition; each steal naturally stops when the condition fails or the opponent hits 0.
- `(server/game/cards/01-Core/Duskrunner.js)` - A upgrade-style grant: `whileAttached` grants the host `Reap: Steal 1A` via `ability.effects.gainAbility('reap', { gameAction: ability.actions.steal() })`.

## Capture

`capture` (`server/game/GameActions/CaptureAction.js`) moves amber from a player's pool onto a **creature** as amber tokens.

- Default source of the amber is `context.player.opponent` (capturing from the opposing player). Passing a `player` property captures from that player instead (cards like "capture from your own pool" set this).
- The amber lands as an `amber` token: `event.card.addToken('amber', amount)`. Amount is capped by the source player's pool (`Math.min(this.amount, player.amber)`).
- `canAffect` requires the source player to have amber (`player.amber > 0`) unless `allowNoAmber` is set, and respects the `capture` restriction keyword.
- **Multi-creature capture shortfalls**: in `preEventHandler`, if capturing onto multiple creatures (e.g. "each friendly creature captures 1A") and the pool cannot cover `target.length * amount`, the engine prompts a select to choose which creatures actually capture (`numCards: Math.ceil(player.amber / this.amount)`). See also `server/game/GameActions/AllocateCaptureAction.js`, which does stepped allocation (e.g. "have your creatures capture 2A each, distributing as you choose") and logs per-creature captured amounts.

Examples:

- `(server/game/cards/01-Core/Charette.js)` - `Play: Capture 3A` -> `this.play({ gameAction: ability.actions.capture({ amount: 3 }) })`.
- `(server/game/cards/01-Core/Gatekeeper.js)` - `Play: If your opponent has 7 or more A, capture all but 5 of it` -> a `condition` on opponent amber plus `capture((context) => ({ amount: context.player.opponent.amber - 5 }))`.
- `(server/game/cards/04-MM/Deusillus.js)` - Gigantic creature play that captures the opponent's entire pool (`amount: context.player.opponent.amber`) as part of a sequential with a damage action.

## Captured amber when a creature leaves play

`Card.onLeavesPlay()` (`server/game/Card.js`, lines ~750-755) returns all amber tokens on a departing creature to that creature's **controller's opponent**:

```javascript
if (this.type === 'creature' && this.hasToken('amber') && this.controller.opponent) {
    this.game.actions.gainAmber({ amount: this.amber })
        .resolve(this.controller.opponent, this.game.getFrameworkContext());
}
```

Consequences for an engine:

- Captured amber always goes to the opposing player of whoever controls the creature at the moment it leaves play. If control has been stolen, the amber follows control, not ownership.
- This refund is unconditional; take control of an enemy creature holding your amber and use/destroy it to un-capture it.
- `onLeavesPlay` also clears all tokens, so the return happens exactly once, before the card's new zone copy exists.
- Captured amber is **not** part of the player's pool: it does not count toward forging cost until returned or spent.

## Exalt

`exalt` (`server/game/GameActions/ExaltAction.js`) places amber tokens onto a creature, with the amber **taken from the common supply** - no player's pool changes. It is the Saurian house's signature mechanic (Worlds Collide onward).

Mechanics exactly:

- Extends `AddTokenAction`; default `amount: 1`, `targetType: ['creature']` - it only targets creatures in the `'play area'` (for placing amber on any card type, use `placeAmber`). Effect message: `exalt {0}`.
- Mints the amber through `card.addToken('amber', amount)` inside an `onExalt` event (`server/game/Events/types.js:506`), so other cards can react to a creature being exalted via the standard `onExalt` trigger.
- `canAffect` requires `amount > 0` and the target creature to be in play; there is NO restriction keyword check of its own beyond the generic paths - "exalt cannot"-style cards are rare and modeled as restrictions on the generic token-add path.
- The amber it adds is indistinguishable from captured amber: it is a plain `amber` token (`card.amber`, `server/game/Card.js:1136`). That has three big consequences:
  - **Leaves-play refund applies.** `Card.onLeavesPlay()` returns ALL amber tokens to the creature's **controller's opponent** (`server/game/Card.js:751-755`). Exalted amber on your OWN creature therefore goes to your opponent the moment that creature leaves play - exalt is a deliberate risk/reward investment, not a safe saving account.
  - **It is not pool amber.** It does not count toward forging; it only affects cards that read `card.amber`.
  - **It is only spendable if a card says so** (e.g. `Medicus Lacus`, 05-DT: "While the tide is high, you may spend A on friendly creatures as if it were in your pool", or forge-key alternative sources via `getAmberSources()`).

Typical design patterns (all generic engine, no exalt-specific card code):

- **Cost/trade-off action**: "You may exalt this creature. If you do, [bonus]" - implemented as an `optional: true` action with `ability.actions.exalt()` and a `then:` block that fires on the exalt succeeding. `(server/game/cards/03-WC/QuestorJarta.js)` (exalt -> gain 1A), `(server/game/cards/03-WC/CincinnatusRex.js)` (exalt -> ready each other friendly card), `(server/game/cards/03-WC/LegatusRaptor.js)` (exalt -> ready and use another friendly creature).
- **Power scaling**: persistent effects read the token count. `(server/game/cards/03-WC/CenturionStenopius.js)` gets `+3 power for each A on it` via `ability.effects.modifyPower((card) => 3 * card.amber)`.
- **Token-count payoffs**: cards that reward or consume the accumulated tokens, e.g. `(server/game/cards/06-WoE/InspiringOration.js)` exalts a friendly creature then makes one token creature per amber token on it (reads the same `card.amber` counter afterwards).

## Transfer

`transferAmber` (`server/game/GameActions/TransferAmberAction.js`) moves amber from the opponent's pool to the acting player's pool with the same math as steal, but mechanically distinct:

- Same `canAffect` gate (opponent must have amber) and same cap, so amounts are identical at equal inputs.
- Different event (`onTransferAmber`) and, critically, **no card text checks the `transfer` name** for restrictions - cards that say "cannot steal" do not block transfer effects. (Transfer's `checkEventCondition` is not overridden; only the generic `checkRestrictions` path applies.)
- Use transfer when card text says "give you their amber" but "steal" interplay would be wrong.

Example:

- `(server/game/cards/01-Core/InterdimensionalGraft.js)` - Sets up a `duringOpponentNextTurn` delayed lasting effect that, on the opponent's `onForgeKey`, does `transferAmber({ amount: context.event.player.amber })` - "they must give you their remaining amber after forging".

## Removing captured amber from a card

`returnAmber` (`server/game/GameActions/ReturnAmberAction.js`) moves amber tokens off a card into a player's pool.

- `all: true` moves every amber token; otherwise `amount` (capped by the card's current amber).
- Recipient resolution order: explicit `recipient` > `controllerRecipient: true` (the card's controller) > default (the controller's **opponent**), which is the un-capture case.
- Commonly used both to steal captured value ("move 1A from this creature to your pool") and to un-capture enemy creatures.

Example:

- `(server/game/cards/07-GR/Azuretooth.js)` - `Fight/Reap: Move all A from a friendly creature to your pool. Give control of that creature to your opponent.` - `returnAmber({ all: true, recipient: context.player })` plus a `takeControl` lasting effect on the same target.

## Gain / Lose (pool modification)

- `gainAmber` - adds to a player's pool. `amount` defaults to 1; the `target` property can hand the amber to another player (used by StealAction/ReturnAmberAction internally).
- `loseAmber` (`server/game/GameActions/LoseAmberAction.js`) - removes from a player's pool to the common supply. `checkEventCondition` requires `event.player.amber > 0`, and the amount is capped (`Math.min(player.amber, this.amount)`), so losing 2 from a 1-amber pool removes 1 and does not fizzle.

  Implemented in `(server/game/cards/01-Core/EffervescentPrinciple.js)`: "Each player loses half their amber (rounding down the loss). Gain 1 chain." Two independent `loseAmber` actions computed from `Math.floor(pool / 2)` in the action factory, with matching `effectArgs` so the log shows actual numbers.
- `placeAmber` - places amber tokens from the common supply onto any card type (creatures, artifacts); same family as exalt but with a wider `targetType` scope.

Restrictions to model:

- Gain/lose actions check restriction keywords like `gainAmber`/`loseAmber` through the generic restriction path.
- Amber leaves the pool only via: steal (to opponent), capture (to a creature keys cannot spend), transfer (to opponent), spreading to cards, or **forging** (to the common supply). Pool amber cannot be negative; all reducers clamp.

## Interaction with keys and forging

- Forging happens in the key phase (`server/game/gamesteps/key/KeyPhase.js`) by resolving `forgeKey()`. Cost is `player.getCurrentKeyCost()` (default 6, see key-cost-modifiers.md).
- `player.forgeKey(modifier)` (`server/game/player.js`, line ~943) computes `cost = Math.max(0, getCurrentKeyCost() + modifier)` and pays from multiple sources in order: the player's own pool, **alternative amber sources** (`getAmberSources()`: amber tokens on the player's own cards that are marked spendable, plus cards that count as amber), and optionally **opponent pool amber** via `forgeWithOpponentsAmber` effects (`getMaxAmberFromOpponentPool`, see Honorable Abagnale).
- `canForgeKey` (`player.js`, line ~884) checks eligibility before prompting: unforged keys remaining, `forge` restriction NOT blocking, not-capped `cannotForgeMoreThan2KeysInATurn`, and total availability >= cost.
- Captured amber on creatures is never paid to forge; it only contributes if a card effect lets the owner spend card-stored amber at key time (see key-cost-modifiers.md).
