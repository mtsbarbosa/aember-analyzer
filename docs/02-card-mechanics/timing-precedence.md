# Timing and Precedence

## Event windows

All card resolution runs through the event system (`server/game/game.js` `getEvent`/`openEventWindow`). Each game action creates events (`onCapture`, `onCardDestroyed`, `onForgeKey`, ...) and abilities react to them:

- `reaction()` abilities resolve **after** the event (with the event already applied).
- `interrupt()` abilities resolve **before** the event resolves and may modify or cancel it via `changeEvent({ cancel: true })` - the canonical example is the engine-built ward reaction in `server/game/Card.js` (lines ~350-382), which cancels the destruction event and removes the ward.

## Simultaneous triggers and player ordering

When several triggered abilities fire off the same event window, resolution order is chosen by a player:

- The default engine protocol is `ForcedTriggeredAbilityWindow` (`server/game/gamesteps/forcedtriggeredabilitywindow.js`): the **active player** picks the order of their own triggers with opponent triggers interleaved at forced-choice points; the prompt is suppressed when the option `orderForcedAbilities` is off (then the engine picks a deterministic order) or when only one choice exists (line ~110).
- `SimultaneousEffectWindow` (`server/game/gamesteps/SimultaneousEffectWindow.js`) is the same idea for **delayed effects**: it collects choices with condition checks, auto-resolves a single one, and otherwise prompts "Order Simultaneous effects - Choose an effect to be resolved". A shared helper `targetsCanChangeViaSimultaneousAction` (e.g. on `MakeTokenCreatureAction.js`) returns true precisely when the outcome depends on ordering so that the player gets the choice.
- `autoResolve: true` on a reaction (`docs/card-abilities.md`) collapses repeated instances of the *same* ability so the prompt does not nag; it never overrides real inter-ability ordering.
- Cards like `(server/game/cards/14-DM/TrashHeap.js)` and `(server/game/cards/14-DM/BlankCheck.js)` check `optionSettings.orderForcedAbilities` in card code to decide whether to open a shared prompt or resolve in fixed player order - two valid card-side patterns for the same rule.

## Delayed effects ("delayedAbility" text)

`server/game/DelayedEffect.js` models "when X happens, do Y later":

- Properties: `target`, `context`, `when` (event names -> condition), `gameAction`, `message`, optional custom `handler`, `multipleTrigger`.
- `checkEffect(events)` runs for each event commit through `EffectEngine.checkDelayedEffects` (`server/game/effectengine.js` line ~47); without `multipleTrigger` the effect is single-use and removes itself after firing.
- Delayed effects whose target left play are pruned (`checkDelayedEffects` filters on `target.location === 'play area'` or player targets), which is how "if that card leaves play, the pending effect fizzles" is enforced.
- `ability.effects.delayedEffect({ ... })` (`server/game/effects.js` line ~145) is the card-facing wrapper (attaches the delayed effect to a source card so it dies with the card).

## Terminal conditions

`terminalCondition` (`server/game/effects.js` line ~99) is a standing trigger checked continuously, not tied to one future event:

- `EffectEngine.checkTerminalConditions()` (`server/game/effectengine.js` lines ~59-64) collects satisfied conditions and opens an event window for them.
- Examples: `(server/game/cards/01-Core/TirelessCrocag.js)` "If your opponent has no creatures in play, destroy Tireless Crocag (immediately)" and `(server/game/cards/03-WC/CincinnatusRex.js)` with the same condition - both implemented purely as `ability.effects.terminalCondition({ condition, message, gameAction: destroy() })`.

## "Then" chains

Sequential dependency is the `then` block (documented in detail in docs/card-abilities.md):

- Default gating: the `then` resolves only if the main ability's `gameAction` produced uncanceled events; `alwaysTriggers: true` overrides that, at which point the `then` must self-gate via `condition` (usually on `preThenContext.target` or `preThenEvents`).
- "If you do, ..." wording maps to checking `preThenEvents` for the actual event (`onCapture` with `amount === 2`, `onCardPurged` with the right card), not final card state.
- Fizzle rules fell out of two engine checks: (a) an ability with no legal target resolves as a no-op, and (b) a `then` block with no legal target for its own `gameAction` is skipped entirely - a function-form `then` returning an object with no `gameAction` is the escape hatch when the chain must keep running on an empty hand (docs/card-abilities.md "Pitfall" section).

## When an ability fizzles

An action's resolution is skipped when:

1. `canAffect` fails for all targets - the resource gate (e.g. `StealAction` refusing a 0-amber opponent, `LoseAmberAction` needing amber > 0).
2. A restriction keyword blocks it (`player.checkRestrictions(name)` / `card.checkRestrictions`), as with steal/capture/forge in the GameActions read earlier.
3. The event is cancelled by an interrupt (ward popping on destruction, change-event effects).
4. The chosen target became invalid between selection and resolution - persistent effects re-evaluate (`EffectEngine.checkEffects` re-snapshots target sets each pass), and the cancellation is silent except when actions print a prevention message.

## Order-of-payment chains

- `sequential([actions])` runs actions one after another (`server/game/GameActions/SequentialAction.js`), preserving ability resolution windows between steps; a plain array of actions resolves **simultaneously** (all events in one window).
- Multi-target actions with shortfall (capture on many creatures, damage with wounds) open their own sub-prompts in `preEventHandler` before events fire - replicate this by resolving target *choosing* before the event, not during.

## Infinite loops

`EffectEngine.checkEffects` iterates until state stops changing, with a hard cap of 10 passes (`server/game/effectengine.js` lines ~72-101). If loops persist (two cards feeding each other), `resolveInfiniteLoop()` removes the oscillating sources to their owners' discard - explicitly **not** considered destroyed - matching the KeyForge infinite-loop rule, then throws if nothing could be removed.
