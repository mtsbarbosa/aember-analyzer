# Key Cost Modifiers

The default key cost is 6 amber. Every cost change flows through one function, so cost modification is highly stylizable in an engine.

## How the cost is computed

`server/game/player.js`:

```javascript
getCurrentKeyCost() {
    return this.sumEffects('modifyKeyCost') + 6;
}
```

- Every `modifyKeyCost` effect (static and dynamic/flexible values) **adds up**. There is no override, multiplication, or "last one wins": a +3 and a -2 active together give cost 7.
- Dynamic effects are functions evaluated when the cost is read: `(server/game/cards/01-Core/IronObelisk.js)` "Your opponent's keys cost +1A for each friendly damaged Brobnar creature" is `modifyKeyCost(() => countOfMatchingCreatures)`. The count changes live.
- The cost floor during payment is 0: `forgeKey` pays `Math.max(0, getCurrentKeyCost() + modifier)` (player.js line ~944). A card cannot be forced to overpay, and `forgeKey({ modifier: -X })` reducing below 0 costs 0.
- Single cards with fixed totals are still implemented as modifiers: `(server/game/cards/03-WC/TheQuietAnvil.js)` "Keys cost 2A" is `modifyKeyCost(-2)`, `any` target-controller so both players feel it. Compare `(server/game/cards/06-WoE/Aembitrage.js)` (+1) and `(server/game/cards/01-Core/GrabberJammer.js)` (opponent +1) for one-point equivalents and The Quiet Anvil's self-cleanup reaction (`onForgeKey` -> destroy) for "once" semantics.

Stacking order to replicate in an engine: gather all active `modifyKeyCost` values from both players' cards, sum, add 6. Check conditions (including card-exhausted state) at read time so temporary effects expire on schedule.

## Forging at reduced or zero cost

Card-triggered forging outside the key phase carries its own modifier:

- `forgeKey({ modifier })` (`server/game/GameActions/ForgeAction.js`) - `canAffect` calls `player.canForgeKey(modifier)`, so affordability is checked **after** all modifiers, both card effects and this action's.
- "Forge at no cost" is expressed as `modifier: -player.getCurrentKeyCost()` (fully neutralizing the current cost, including other modifiers):
  - `(server/game/cards/06-WoE/BlorbHive.js)` - after 10 Blorbs: `forgeKey((context) => ({ modifier: -context.player.getCurrentKeyCost() }))`.
  - `(server/game/cards/01-Core/EpicQuest.js)` - same "forge a key at current cost" pattern from a quest-complete reaction.
- The engine also has a first-class `atNoCost` property on ForgeAction (`getModifier` returns `-player.getCurrentKeyCost()`), used by cards like `(server/game/cards/12-PV/Sample42C.js)`.
- Fate/prophecy forging adjusts only against the default: `forgeKey((context) => ({ modifier: getCurrentKeyCost() - defaultKeyCost }))` keeps the exact live cost.

## Cannot forge and skip-the-key-phase

"Cannot forge" is implemented two ways:

- **Restriction keyword** `forge`: `canForgeKey` (`player.js` line ~884) first checks `checkRestrictions('forge', frameworkContext)`, so effects like "your opponent cannot forge keys" gate at the eligibility level and show nothing in prompts.
- **Phase skipping**: `ability.effects.skipStep('key')` removes the forge-key phase step entirely. `(server/game/cards/01-Core/Miasma.js)`: `duringOpponentNextTurn({ targetController: 'opponent', effect: skipStep('key') })` on their next turn. `(server/game/cards/01-Core/TheSting.js)` skips its **own** key step as the price of redirecting all opponent forge-amber to itself.
- **Event cancellation**: a third "not forging" shape is an interrupt that cancels `onForgeKey`. `(server/game/cards/13-CC/CurseOfForgery.js)` purges itself inside an `onForgeKey` interrupt via `changeEvent({ cancel: true })` so no amber is spent.
- Phase-skip and low-cost interplay: a player skipping the key phase still forges normally on a later turn; skipping does not stack cost.

## Spending other pools (amber "bridges")

A player's shortfall can be bridged by alternative amber sources, all implemented inside `forgeKey`/`canForgeKey`:

- `getAmberSources()` (`player.js` line ~818) collects amber tokens on cards the player may spend (own spendable creatures, opponent's creatures when allowed) plus cards that *count as* amber themselves; the payer picks sources via prompts (`chooseAmberSource`) when pool amber alone cannot cover cost.
- `forgeWithOpponentsAmber(amount)` effect lets the player spend up to N amber from the opponent's pool. `(server/game/cards/14-DM/HonorableAbagnale.js)`: "You may spend up to 3A from your opponent's pool when forging keys" -> `forgeWithOpponentsAmber(3)`. `getMaxAmberFromOpponentPool` caps at both the effect value and the opponent's actual amber.

## Unforging and key colors

- `unforgeKey` (`server/game/GameActions/UnforgeAction.js`) returns a forged key to unforged state, refunding nothing (amber is consumed). `(server/game/cards/01-Core/KeyHammer.js)` - "If your opponent forged a key on their previous turn, unforge it. Your opponent gains 6A": the `then` picks from `opponent.keysForgedThisRound` and unforges, while the amber gain compensates the opponent for the full key price.
- Keys are colored; `forgeKey({ keyColor })` forges a specific color if it is still unforged (checked in `canForgeKey`), used by cards like `(server/game/cards/08-AS/BlueAemberdrake.js)`.
- Cap effects: `cannotForgeMoreThan2KeysInATurn` (`server/game/cards/12-PV/RelativityImp.js`) limits multi-forge turns; `canForgeSecondKeyDuringKeyPhase` (checked in `server/game/gamesteps/key/KeyPhase.js`) is the grant that allows a second forge in the phase.
