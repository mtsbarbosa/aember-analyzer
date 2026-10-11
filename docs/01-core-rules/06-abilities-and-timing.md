# 06 - Abilities and Timing

How abilities are declared, costed, targeted, and how triggers/resolutions are ordered in
keyteki. All paths cite keyteki sources.

## 1. Ability classes and the definition DSL

- `Card.setupCardAbilities(ability)` is invoked after construction; every helper lives on
  the card or on the ability DSL imported from `server/game/CardAbility.js` family
  (`server/game/Card.js:281-291`).
- Class chain: `BaseAbility` -> `ThenAbility` -> `CardAbility` ->
  - `CardAction` (action abilities, `server/game/cardaction.js`),
  - `TriggeredAbility` (reactions/interrupts, `server/game/triggeredability.js`).

### 1.1 Definition helpers on `Card`

- `action(properties)`: registers a `CardAction` in `abilities.actions`
  (`server/game/Card.js:544-551`).
- `omni(properties)`: sets `properties.omni = true` then `action(...)`
  (`server/game/Card.js:539-542`). Omni abilities can be used even when the card is not of
  the active house; they consume the card's exhaust as a framework cost (see 1.4).
- `reaction(properties)`: creates a `TriggeredAbility('reaction', ...)`. Shorthand works:
  if `play`/`fight`/`reap` properties are true, the `when` clause is auto-wired to
  `onCardPlayed`/`onFight`/`onReap` events matching `context.source`
  (`server/game/Card.js:579-589`).
- `interrupt(properties)`: same shape, ability type `'interrupt'` (fires before the event
  resolves; can cancel or replace events) (`server/game/Card.js:591-593`).
- `beforeFight(properties)`: sugar for an interrupt on `onFight` where the source is the
  attacker (`server/game/Card.js:553-560`).
- `persistentEffect(properties)`: a `while` effect (static/ongoing). Allowed locations:
  `any`, `play area`, `discard`; default duration `persistentEffect`; registered into
  `abilities.persistentEffects` (`server/game/Card.js:639-660`). "While" semantics come from
  `condition` (source-side) and `match` (target-side); the engine re-applies/unapplies them
  as state changes (section 5).
- `triggeredAbility(type, properties)`: raw registration for reactions/interrupts with
  full `when` clauses (`server/game/Card.js:562-569`).

### 1.2 CardAction details

- Costs: EVERY action appends `Costs.exhaust()` and `Costs.use()`; i.e. using an action
  exhausts the card (for creatures this also consumes their fight/reap/act for the turn)
  (`server/game/cardaction.js:48`).
- `meetsRequirements` blocks on: `useAction` restrictions, `use` restrictions, location,
  `useCondition`, `stunned`, `exhausted` (`server/game/cardaction.js:51-73`).
- If no legal targets exist when resolving, the ability still fires its `onUseCard` event
  (unless it is a reap/fight) and messages "uses X without effect"
  (`server/game/cardaction.js:78-104`).
- `omni` abilities are those with `omni: true`; their menu title reads "Use this card's
  Omni ability" (`server/game/cardaction.js:41-43`).

### 1.3 TriggeredAbility details

- `when`: map of event name -> predicate. `properties.condition`: extra state guard
  evaluated in `isTriggeredByEvent` (`server/game/triggeredability.js:130-149`).
- `optional` abilities present a Done button in the trigger window.
- `location: 'any'` abilities register listeners at construction and stay active anywhere
  (discard, hand...) (`server/game/triggeredability.js:54-57, 170-194`).
- `play`/`fight`/`reap` shorthand combined with effects like
  `playAbilitiesAddReap`/`reapAbilitiesAddFight` expand which events a given ability
  responds to (`server/game/triggeredability.js:151-168`).
- Multi-subject triggers (`multiTriggerEvent`) let one bulk event produce one windowed
  choice per subject card (`server/game/triggeredability.js:78-106`).

### 1.4 Play / fight / reap as abilities

- Play actions (`PlayCreatureAction`, `PlayArtifactAction`, `PlayAction` under
  `server/game/BaseActions/`) are also abilities: their cost is the amber cost of the card
  and their handler puts the card into play, raising `onCardPlayed` (whose child handles
  bonus icons via `game.actions.resolveBonusIcons`).
- Fight: `FightGameAction` -> `ResolveFightAction` (see 05-combat.md).
- Reap: `ReapGameAction` -> `ResolveReapAction` raising `onReap`, which gains 1 amber
  (`server/game/GameActions/ReapGameAction.js`, `ResolveReapAction.js:20-22`).
- Framework `use` costs live in `server/game/costs.js` (`Costs.exhaust()`, `Costs.use()`,
  `Costs.amber(...)` for card costs, etc.).

## 2. The ability resolution pipeline (AbilityResolver)

When any player uses an ability, `game.resolveAbility(context)` raises `onResolveAbility`
and queues an `AbilityResolver` step (`server/game/game.js:1090-1098`). The resolver runs a
strict pipeline (`server/game/gamesteps/abilityresolver.js:15-25`):

1. **createSnapshot** - for creature/artifact sources, capture
   `context.cardStateWhenInitiated` so later triggers see pre-resolution state
   (`abilityresolver.js:27-31`).
2. **payCostsThatCanFail** - costs that can cancel the ability resolve FIRST in their own
   event window; if any is cancelled (e.g. a "choose X or cancel" cost), the ability aborts
   (`abilityresolver.js:49-58`; cost flagging in `server/game/baseability.js:156-161`).
3. **payCosts** - remaining costs resolve in one simultaneous event window
   (`abilityresolver.js:60-78`; `baseability.js:166-171`; additional per-player costs come
   from `getAdditionalCosts`, `server/game/player.js:1392-1396`).
4. **resolveTargets** - each declared target prompts the player in order
   (`baseability.js:185-200`; early/resolved snapshot handling at
   `abilityresolver.js:33-47`).
5. **initiateAbility** - raises `onAbilityInitiated`
   (`abilityresolver.js:88-100`).
6. **executeHandler** - sets `context.stage = 'effect'`, prints the effect message, then
   runs the handler (`abilityresolver.js:102-110`).
7. **raiseResolvedEvent** - final `onAbilityResolved` event
   (`abilityresolver.js:112-118`).

A cancelled ability simply skips all remaining steps (`this.cancelled` guards).

### 2.1 Executing the handler: game actions become events

- `ThenAbility.executeHandler` optionally prompts "Do you wish to X?" for `may`
  properties, then schedules `executeGameActions`
  (`server/game/ThenAbility.js:65-78`).
- `executeGameActions` converts all actions into events via `action.getEventArray(context)`
  and opens ONE event window for them; then-chains are queued after; post handlers run last
  (`ThenAbility.js:89-103`).
- `then:` properties create a nested `ThenAbility` at resolution time; by default the
  `then` block only resolves if every preceding event resolved (not cancelled);
  `alwaysTriggers` bypasses that gate but NOT the legal-target check
  (`ThenAbility.js:105-148`).
- `effectStyle: 'all'` runs actions sequentially instead of one simultaneous window, so
  each action's chat message precedes its events (`server/game/CardAbility.js:11-59`).

## 3. Events and windows

- `game.openEventWindow(events)` merges concurrent events into a parent event (child of the
  first) and queues an `EventWindow` (`server/game/game.js:1142-1162`).
- `EventWindow` pipeline (`server/game/Events/EventWindow.js:14-24`):
  1. `checkEventCondition` - event-level cancel/replacement checks.
  2. `openAbilityWindow('interrupt')` - interrupt window for simultaneous events
     (`EventWindow.js:30-57`).
  3. `preResolutionEffects` - emits `<event>:preResolution` for each event (line 59-63).
  4. `executeHandler` - for each simultaneous event: re-check condition, execute unless
     cancelled, then `game.emit(event.name, event)` broadcasts the plain event to
     persistent listeners (65-76).
  5. `checkForSubEvent` - sub-events spawned by handlers get their own nested EventWindow
     immediately after (85-119).
  6. `checkGameState` - refresh transient state (destroyed creatures, effect re-checks).
  7. `openAbilityWindow('reaction')` - AFTER the event handler and game-state check, the
     reaction window opens. Reactions only open when the event
     `openReactionWindow` flag is set (line 32; default `true` on every Event,
     `server/game/Events/Event.js:18`; some internal sub-events suppress it).

"Interrupt" = before the resolution; "Reaction" = after the resolution and the game-state
check.

## 4. The forced triggered ability window (ordering choices)

`ForcedTriggeredAbilityWindow` (`server/game/gamesteps/forcedtriggeredabilitywindow.js`)
is the player-facing prompt used for every interrupt/reaction window:

1. **Emit**: `emitEvents()` clears choices and, for each simultaneous event in the parent
   window, emits `<event.name>:<abilityType>`; every registered triggered ability of that
   type receives the event and calls `window.addChoice(context)` if its `when` clause
   passes (`forcedtriggeredabilitywindow.js:27-44, 467-475`;
   `server/game/triggeredability.js:59-117`).
2. **Deferred choices**: abilities whose requirements fail NOW but might pass after another
   trigger changes state (only `destroyed`/`play` abilities) are queued separately and
   merged when other valid choices exist (`forcedtriggeredabilitywindow.js:59-79, 81-88`).
3. **Auto-resolve rules**:
   - If ALL choices are `autoResolve` abilities, the first resolves without prompting
     (line 94-101).
   - If none are optional and (window autoResolve, exactly 1 choice, or the player disabled
     `orderForcedAbilities` option), the first choice resolves automatically
     (103-114).
   - Otherwise prompt the player.
4. **Prompting cascade** (lines 121-453):
   - `promptBetweenSources`: pick a triggering CARD (any location incl. discard, prophecy).
   - `promptBetweenAbilities`: pick which ability of that card.
   - `promptBetweenEventCards`: pick which affected card (for multi-subject triggers).
   - `promptBetweenEvents`: pick which event to respond to.
   - Back buttons navigate between stages; "Done" ends the window when all choices are
     optional; "Autoresolve" lets the engine take over.
5. **resolveAbility** delegates to `game.resolveAbility`, records the resolution so the
   same `(ability, event, subject)` triple cannot trigger twice in one window, and
   unregisters lasting single-shot triggers (455-465).

The ACTIVE player is always the one prompted to order their triggers first
(`this.currentPlayer = this.game.activePlayer`, line 15).

- Prompt titles come from `TriggeredAbilityWindowTitles`
  (`server/game/gamesteps/triggeredabilitywindowtitles.js`): e.g. "Choose forced reaction
  order for X being destroyed or Y being played".
- Because the window re-emits after every resolution ("continue" loop), new triggers
  spawned by a resolved trigger join the same window until nothing is left.

## 5. Persistent effects, effect engine, and state changes

- The `EffectEngine` (`server/game/effectengine.js`) owns lists of
  `effects` (static), `delayedEffects`, `duringOpponentNextTurnEffects`,
  `terminalConditions`, and `customDurationEvents` (`effectengine.js:8-13`).
- `checkEffects(prevStateChanged)` re-evaluates every effect's condition and target match,
  looping until the state stabilises; if the loop count hits 10, an oscillating pair of
  effects is detected by target-set snapshots and the sourced cards are REMOVED FROM PLAY
  to their owner's discard (not destroyed) to settle infinite loops
  (`effectengine.js:72-162`).
- Effect durations (`albeit on turn end`): `untilPlayerTurnEnd` effects are removed on turn
  end; `untilPlayerNextTurnEnd`/`Start` effects survive via an initiated-marker state
  machine; `duringOpponentNextTurn` effects are promoted at the turn switch;
  'anotherTurn' effects special-case consecutive turns (`effectengine.js:164-237`).
- Custom durations listen for specific events via `until` clauses and cancel themselves
  when matched (`effectengine.js:239-279`).

### 5.1 "on" vs "when" vs "while"

- **"on" / "when"**: `TriggeredAbility` with `when` clauses. `interrupt` runs inside the
  EventWindow before the handler (may cancel or replace events);
  `reaction` runs after. In card text keyteki treats "when:" keys (e.g. `onFight`) as
  reaction/interrupt triggers; there is no separate "on" type.
- **"while"**: `persistentEffect` with `condition`/`match`; applied and unapplied by the
  EffectEngine as targets enter/leave matching state; no window prompts.
- **Delayed effects**: `DelayedEffect` objects register `when` clauses checked against
  events AFTER the resolution phase (`game.checkDelayedEffects` inside
  `EventWindow.openAbilityWindow('reaction')` path,
  `server/game/Events/EventWindow.js:51-53`); they execute their `gameAction` on the first
  matching event unless `multipleTrigger` (`server/game/DelayedEffect.js:28-65`).

### 5.2 SimultaneousEffectWindow

`SimultaneousEffectWindow` (`server/game/gamesteps/SimultaneousEffectWindow.js`) is a
special window (abilityType `'delayedeffects'`) used by
`game.openSimultaneousEffectWindow(choices)` to let the active player order arbitrary
simultaneous pending effects (choices with `title`/`handler`); resolution order follows the
prompt (`server/game/game.js:1100-1106`).

### 5.3 TerminalCondition

`TerminalCondition` (`server/game/TerminalCondition.js`) models "when X, then Y" state
watchers (e.g. "at the end of the turn, if this creature is still in play, destroy it").
`checkCondition()` is polled by `EffectEngine.checkTerminalConditions()` which opens an
event window and resolves the condition's `gameAction` when it first passes
(`effectengine.js:59-64`; `TerminalCondition.js:15-51`).

## 6. Destroyed windows (cascade specifics)

- When a card with `Destroyed:` abilities is destroyed, `EventWindow` swaps the generic
  interrupt window for a `DestroyedAbilityWindow`
  (`server/game/Events/EventWindow.js:36-50`).
- Cascading destroys by destroyed abilities are batched into the SAME window
  (`server/game/gamesteps/DestroyedAbilityWindow.js:19-35, 66-72`).
- The window opens `reaction` triggers on leaves-play AFTER all destroyed abilities; the
  critical move-to-discard (`discardAllTaggedCards`) happens only when the window closes
  (lines 74-86, 131-164).

## 7. Per-phase pipelining

- Phases (house selection, main action window, ready, draw, key forge) are chained as
  steps in `server/game/gamesteps/*` classes (`phase.js`, `MainPhase.js`, `HousePhase.js`,
  `KeyPhase.js`, etc.), each queuing `SimpleStep`s like AbilityResolver does.
- The main action window (`server/game/gamesteps/main/actionwindow.js`) prompts the active
  player repeatedly: play a card, use a card (fight/type/reap/board), or done; each chosen
  action calls `game.resolveAbility(...)`.
- House legality is enforced at action creation time (context `ignoreHouse`) rather than a
  global filter; exceptions come from `canUse`/`canPlay`/`canFightNonHouse`-style effects,
  `server/game/effects.js:123-141`.

## 8. Analyzer pitfalls

- Reactions are windowed per EVENT WINDOW, not per event: multiple simultaneous events in
  one window produce ONE combined prompt ordering both.
- The `onUseCard` event is raised for play, fight, reap, and action uses (different flags),
  which is what "after you use a card" triggers latch onto.
- Ability cancel semantics: interrupts CAN cancel events; a cancelled event's handler is
  skipped (`Events/EventWindow.js:65-76`) and the destroy/damage sub-event chain is
  skipped with it.
- The `moribund` flag means destroyed-but-still-visible; selectors must exclude or include
  it carefully (e.g. `MostStatCardSelector` filters `moribund` in
  `server/game/CardSelectors/MostStatCardSelector.js:28`).
