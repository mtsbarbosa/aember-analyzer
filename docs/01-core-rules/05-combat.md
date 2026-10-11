# 05 - Combat: Fighting, Damage, Armor, Destruction

Ground truth for how fights and destruction actually execute in keyteki. Cited paths are
keyteki source files.

## 1. Initiating a fight

- A fight is a card **use**: during the main action window only cards of the active house
  can be used, unless a card effect grants a house exception (e.g. `canFightNonHouse`,
  registered in `server/game/effects.js:137`; `ignoreHouse` contexts, e.g.
  `server/game/GameActions/FightGameAction.js:21-23`).
- Framework fight actions come from `Card.getFightAction()`
  (`server/game/Card.js:1323-1342`): costs (exhaust + must be unstunned/ready) are added by
  `CardAction` (`server/game/cardaction.js:48`: `this.cost = this.cost.concat(Costs.exhaust(), Costs.use())`),
  use conditions require `checkRestrictions('fight')` and `type === 'creature'`.
- The player chooses the attack target from the opponent's battleline
  (`getFightAction` target spec: `cardType: 'creature'`, `controller: 'opponent'`,
  `Card.js:1330-1340`).
- **Taunt restrictions apply at target selection and again at resolution**:
  `ResolveFightAction.canAffect` rejects targets that fail
  `checkRestrictions('attackDueToTaunt')`, unless `ignoreTaunt` is set or the attacker
  `ignores('taunt')` (`server/game/GameActions/ResolveFightAction.js:29-36`). The taunt
  keyword's persistent effect makes each NEIGHBOR of a taunt-ing creature
  `attackDueToTaunt` restricted (i.e. "cannot be attacked"), not the taunt creature
  itself (`server/game/Card.js:330-337`).
- A stunned creature cannot fight: when used it instead removes the stun token
  (framework `getRemoveStunAction`, `server/game/Card.js:1355-1360`; `FightGameAction`
  routes a stun-through-fight into the remove-stun action,
  `server/game/GameActions/FightGameAction.js:47-65`).
- `mustFightIfAble` effects (e.g. enrage) filter the legal action list down to fight actions
  only (`server/game/Card.js:1316-1319`; enraged keyword effect at `Card.js:340-349`).
- The initiated use raises `onInitiateFight` (`FightGameAction.js:48`), then
  `ResolveFightAction` executes the actual `onFight` event.

## 2. Damage calculation

All of this lives in `ResolveFightAction.getEvent`
(`server/game/GameActions/ResolveFightAction.js:41-180`):

1. The fight event carries snapshots: `attackerClone` and `attackerTargetClone`
   (`createSnapshot()`), plus `destroyed: []` and `cancelFight: false` flags
   (`ResolveFightAction.js:41-56`). The event aborts inside the handler if either
   participant left play or the fight was cancelled (`condition` at lines 45-48).
2. **Defender damage**: `defenderAmount = event.card.power`, clamped by
   `limitFightDamage` effects. Armor is ignored only if the attacker
   `ignores('armor')` (lines 63-76).
3. **Attacker damage**: `attackerAmount = event.attacker.power + bonusFightDamage effects`
   (each effect evaluated against the actual target), also clamped by
   `limitFightDamage` (lines 77-87).
4. **Simultaneous, two-way damage**: both damage events are constructed and the attacker
   damage event is added as a CHILD of the defender damage event (or vice versa), so
   they execute in one event window - both creatures deal damage even if one is
   destroyed by the other (`ResolveFightAction.js:106-121`).
5. **Poison etc.** are handled later by the apply-damage step (section 4.3).

### 2.1 Elusive

- `elusive` is a keyword counted via `getKeywordValue('elusive')` (printed keywords register
  with value 1, `server/game/Card.js:57-69; gains via addKeyword`).
- Mechanically: if the defender has elusive AND has not yet been attacked this turn AND the
  attacker does not `ignores('elusive')`, the defender deals no fight damage and the
  attacker deals its damage to the DEFENDER TARGET (which may be a redirected target),
  not to the elusive creature (`ResolveFightAction.js:95-129`).
- `elusiveUsed` is set on the defender inside the `onUseCard` child event of the fight
  (`ResolveFightAction.js:163-177`) and cleared by `Card.endRound()` at round end
  (`server/game/Card.js:783-786`).

### 2.2 Assault and Hazardous

Both are implemented as keyword interrupts on `onFight` (`server/game/Card.js:293-327`):

- **Assault N**: interrupts when `event.attacker === context.source`; deals N (its keyword
  value) damage to the defender, tagged `damageType: 'assault'`.
- **Hazardous N**: interrupts when the source is the defender
  (`event.card === context.source`), unless the attacker `ignores('hazardous')`; deals N
  damage to the attacker, tagged `damageType: 'hazardous'`.

### 2.3 Splash-attack

- Keyword `splash-attack: X` deals X damage to each of the defender's NEIGHBORS regardless
  of elusive (`ResolveFightAction.js:131-149`).
- Damage anywhere in a fight marks both participants `isFighting = true` while the damage
  event window is open, then resets it (`ResolveFightAction.js:151-159`).

### 2.4 Text-based fight abilities: Before Fight and After Fight

Card-text fight skills hook the same `onFight` event the keywords use, but split across the two
phases of the event window:

**"Before Fight:" = an interrupt on the card's own `onFight` event.**

- `Card.beforeFight(properties)` (`server/game/Card.js:553-560`) is just a shorthand for
  `this.interrupt({ when: { onFight: (event, context) => event.attacker === context.source } })`
  - an interrupt ability that triggers only when the card is the ATTACKER. They have no
  pre-built engine behavior; the game effect lives entirely in the ability body.
- Because they are interrupts, they resolve in the **interrupt phase of the `onFight` event
  window, BEFORE the damage handler in `ResolveFightAction.getEvent` runs**
  (`server/game/GameActions/ResolveFightAction.js:57` handler executes after the window's
  interrupt phase; see `01-core-rules/06-abilities-and-timing.md` for window mechanics).
- They can:
  - **Deal pre-fight damage**: `(server/game/cards/01-Core/Firespitter.js)` - "Before Fight:
  Deal 1D to each enemy creature"; `(server/game/cards/01-Core/LordGolgotha.js)` - "Before
  Fight: Deal 3D to each neighbor of the creature Lord Golgotha fights" (which reroutes
  pre-fight damage BEFORE the two-way damage step).
  - **Redirect or alter the fight**: `(server/game/cards/01-Core/GabosLongarms.js)` -
  "Before Fight: Choose a creature. Gabos Longarms deals damage to that creature rather than
  the one it is fighting" - implemented by a `changeEvent` action that edits the `attackerTarget`
  of the in-flight `onFight` event. Note the event snapshots (`attackerTargetClone`) are taken
  BEFORE interrupts, so post-redirect damage math uses the live event, not the snapshot.
  - **Prevent the fight entirely**: handlers can set `cancelFight = true` on the event
  (`server/game/Events/types.js:549-551`, e.g. Evasion Sigil). When that happens, or when a
  before-fight effect (assault, hazardous, or any "Before Fight" damage) destroys one of the
  creatures before fight damage is applied, the `onFight` event is CANCELLED - the damage
  handler never runs AND `onFight` reaction ("After Fight") listeners never fire
  (`server/game/Events/types.js:526-531`).
- Ordering among multiple "Before Fight" interrupts follows the normal forced-interrupt window
  order (the attacking player's cards before the defending player's, then by trigger index -
  see `01-core-rules/06-abilities-and-timing.md`).

**"After Fight:" = a reaction on the same `onFight` event.**

- A creature's own "After Fight" text is registered through `this.fight(...)`, which the
  reaction helper turns into `when: { onFight: (event, context) => event.attacker === context.source }`
  (`server/game/Card.js:579-589`). Example: `(server/game/cards/06-WoE/Berserker.js)` -
  "After Fight: Destroy Berserker" is simply `this.fight({ gameAction: ability.actions.destroy() })`.
- Reactions resolve in the reaction phase AFTER the interrupt phase and the fight damage
  handler complete; they see the post-combat board (including any creatures destroyed by the
  fight already processed through destroyed windows).
- Cards referencing OTHER creatures' fights ("After a creature fights, ...") use the same
  `onFight` event with custom `when` conditions, e.g.
  `(server/game/cards/06-WoE/BattlefieldEvangelist.js)` and
  `(server/game/cards/06-WoE/AncientBattleground.js)`; they need their own window since they
  are not registered through `this.fight` on the fighter itself.

### 2.5 Full fight order of operations

Numbered master list combining everything above:

1. **Selection**: player picks Fight on a ready, unstunned active-house creature (or via
   card-text exceptions); choose target from opponent battleline with taunt rules enforced
   (section 1). This is a full player prompt - fight declaration is interactive.
2. **`onInitiateFight`**: `FightGameAction.getEvent`
   (`server/game/GameActions/FightGameAction.js:47-65`) raises the initiation event and its
   handler resolves the fighter's fight `CardAction` through `resolveAbility` with
   `canCancel = false`.
3. **Ability costs**: exhaust and `use` costs are paid (the creature becomes exhausted;
   `onUseCard` child event registers the use).
4. **`onFight` event created** by `ResolveFightAction` with snapshots, `destroyed: []`,
   `cancelFight: false` (`ResolveFightAction.js:41-56`).
5. **Interrupt phase** of the `onFight` window: keyword interrupts first per normal window
   ordering - assault (attacker's keyword, deals N to defender, `Card.js:295-309`), hazardous
   (defender's keyword, deals N to attacker unless the attacker ignores it, `Card.js:312-327`),
   then card-text "Before Fight" interrupts (section 2.4). Any of these may kill a participant,
   redirect `attackerTarget`/`defenderTarget`, or set `cancelFight = true`.
6. **Fight survives check**: if `cancelFight` or either creature has left play
   (`condition`, `ResolveFightAction.js:45-48`), the event is cancelled here - skip to the
   destroyed windows if a creature died; NO fight damage exchanged and NO "After Fight"
   reactions fire.
7. **Damage handler** (`ResolveFightAction.js:57-160`): computes defender damage
   (power, plus `limitFightDamage` clamp, armor honored unless ignored) and attacker damage
   (power + `bonusFightDamage`, clamped), applies elusive gating (section 2.1), builds the
   two-way simultaneous damage event (section 2, step 4), appends splash damage to neighbors
   (section 2.3), marks both participants `isFighting` while the window is open, and opens the
   single combined damage event window.
8. **Damage resolution**: ward (section 3.1) and armor (section 3.2) absorb; `ApplyDamageAction`
   (section 3.3) adds damage tokens, flags poison/`destroyedFighting` destroys; destruction
   cascades run through `DestroyedAbilityWindow` (section 4).
9. **Reaction phase**: "After Fight" reactions and other `onFight`/damage reactions resolve
   (triggered ability window); note if heavy damage already deleted both creatures, these
   reactions still fire if their conditions were met at trigger time.
10. **Post-fight**: `onUseCard` side effects clean up (the fighter's `elusiveUsed` reset path
    runs via the child event, `ResolveFightAction.js:163-177`); fight bookkeeping
    (`cardNamesPlayedOrUsed`, +1 draw counts if relevant) is recorded by the use, not the fight.

## 3. Dealing damage in general (card effects)

`DealDamageAction` (`server/game/GameActions/DealDamageAction.js`) is the general damage
action used by card effects, bonus icons, and fight damage alike:

- Per-card amount via `amountForCard` or flat `amount`; `splash` deals damage to neighbors;
  `damageType` defaults to `'card effect'`; `ignoreArmor` skips armor
  (`DealDamageAction.js:4-24`).
- Damage can be wholly replaced by a `replaceDamage` effect on the source, which resolves a
  different action instead of dealing damage (`DealDamageAction.js:50-64, 66-71, 121-126`).

### 3.1 Ward checked first

- In the `onDamageDealt` handler, if the target has any ward token FIRST: all simultaneous
  `onDamageDealt` events for that card are cancelled, a chat message notes the ward
  prevented the damage, and the ward is removed
  (`unwardAndCancel`, `DealDamageAction.js:91-119, 140-143`). A redirected apply-damage
  bypasses ward, since ward was already consumed on the original target
  (`server/game/GameActions/ApplyDamageAction.js:24-26`).

### 3.2 Armor

- Two armor values live on a card: `armorTotal` = printed armor (`armorPrinted`) + summed
  `modifyArmor` effects (+ armor tokens handling block), and `armor` = armor TOKENS on the
  card (`server/game/Card.js:1121-1133`).
- `armorUsed` counts how much printed/derived armor has been consumed this turn; it is reset
  with round state (`server/game/Card.js:90, 784`).
- In `DealDamageAction.getEvent`, if armor is not ignored and `armorTotal > armorUsed`, the
  armor absorbs up to the remaining armor allowance: `armorUsed = min(amount,
  armorTotal - armorUsed)`. The residual `amountApplied = amount - armorUsed` becomes an
  `onDamageApplied` sub-event; the armor consumption itself is an `onDamagePreventedByArmor`
  sub-event that increments `armorUsed` (`DealDamageAction.js:145-204`).
- Because armor is consumed per damage event, multiple small damage events in the same turn
  each get absorbed separately until the creature's armor allowance is exhausted.
- `onDamagePreventedByArmor` sub-events set `openReactionWindow = true` so reactions can
  respond to armor-prevented damage (`DealDamageAction.js:178-204`).

### 3.3 Applying damage and destroying

`ApplyDamageAction` (`server/game/GameActions/ApplyDamageAction.js`):

- Adds damage tokens: `event.card.addToken('damage', event.amount)` (line 44).
- If the creature is NOT already `moribund` and either
  `damage >= power` (fatal) or the damage came from a fight opponent with `poison`
  (respecting `ignores('poison')`), a `DestroyAction` event is created and attached as a
  sub-event (lines 41-77).
- If this happens inside a fight, the destroy event gets `destroyedFighting = true` and is
  pushed onto `fightEvent.destroyed` (lines 67-74) - any creature destroyed during a fight,
  even by splash or damage movement, counts as "destroyed fighting".

## 4. Destruction

### 4.1 The destroy event and the destroyed window

`DestroyAction.getEvent` (`server/game/GameActions/DestroyAction.js:30-82`):

- Sets `event.card.moribund = true` (the card is "tagged" for destruction; it remains in
  play - visible to selectors - until the destroyed-ability window closes; comment at
  `DestroyAction.js:19-28`).
- Creates an `onCardLeavesPlay` child event which will move the card to its owner's discard.
- Special batching: if destruction happens while a destroyed-ability window is already open
  (`game.currentDestructionWindow`), the new destroy event is batched into that window
  (`openReactionWindow = false`, `addBatchedDestroyEvent`) instead of opening a nested one
  (lines 44-45, 73-81). A destroy for an already-moribund card is a no-op via its
  `condition` (line 41).

`DestroyedTriggeredAbilityWindow` (`server/game/gamesteps/DestroyedAbilityWindow.js`):

- Enforces cascade batching: cards destroyed by `Destroyed:` abilities mid-window join the
  SAME window; all tagged cards stay on the board until `discardAllTaggedCards()` runs at
  window close (class comment, lines 5-17; `discardAllTaggedCards`, lines 131-164).
- Enforces ability lock-in: a card only resolves the `Destroyed:` abilities it had when
  tagged; abilities gained after tagging are rejected (`lockedInChoices`, lines 32-50, 66-72).
- At close, each surviving destroy event is attached as a child of the parent event so the
  outer reaction phase sees the full cascade as one grouped trigger (lines 140-163).

### 4.2 "Destroyed" vs "dies"

- keyteki has exactly one destroyed mechanic: an `onCardDestroyed` event
  (with `Destroyed:` abilities and `LeavesPlay` as sub-windows), NOT a separate "dies"
  trigger. Any card that "dies" in play is destroyed: combat damage, `DestroyAction` from
  card text, sacrifice (name `'sacrifice'`), or Battleline-clearing (`Swipe`, etc. all reuse
  `DestroyAction`). The distinction in card text (destroyed vs sacrificed vs leaves play)
  is expressed via `damageEvent`, `destroyedByDamageDealt`, `destroyedFighting` flags and
  action naming (`ApplyDamageAction.js:60-74`; `DestroyAction.js:5-9`).
- `Destroyed:` reactions register normally via `properties.destroyed` on triggered
  abilities and are windowed by `DestroyedAbilityWindow`.

### 4.3 Ward vs destroy

- Ward interrupts destruction the same way it interrupts damage: a warded creature's
  keyword reaction triggers on its own `onCardDestroyed`, `onCardPurged`, or
  `onCardLeavesPlay` and cancels the event while removing the ward token; the card's
  `moribund` flag is reset in the postHandler (`server/game/Card.js:352-382`).
- Invulnerable creatures additionally cannot be damaged, destroyed, or sacrificed when the
  damage source does not `ignores('invulnerable')`; in a fight only the ATTACKER's ignores
  flag bypasses invulnerability, not the defender's counter-damage
  (`server/game/Card.js:384-410`).

### 4.4 Captured amber on destruction

- When a creature leaves play (destruction moves it to discard), the amber captured on it
  goes to the creature controller's OPPONENT via `Card.onLeavesPlay()` resolving
  `gainAmber({ amount: this.amber })` for `this.controller.opponent`
  (`server/game/Card.js:750-755`). This means the person who captured your amber loses it
  to you (if you are the opponent of the creature's controller) at destruction time.
- The token map is cleared immediately after (`this.tokens = {}; Card.js:760`), so the
  destroyed card enters discard with no amber on it.

## 5. Damage and amber allocation prompts

- `AllocateDamagePrompt` (`server/game/gamesteps/AllocateDamagePrompt.js`): used by actions
  with a fixed per-step damage size (`damageStep * numSteps`); the player clicks creatures
  to allocate damage until the total is reached, optionally propagating `splash` values to
  neighbors in the same pass (`AllocateDamagePrompt.js:60-106`).
- `AllocateAmberPrompt` (`server/game/gamesteps/AllocateAmberPrompt.js`): same click-driven
  allocation for capture ("Choose a creature to capture 1 amber on"); all choices are
  gathered up front and captures are queued as ONE batch so mid-allocation state changes
  (e.g. a creature losing power as amber is captured) only resolve after every choice
  (comment, lines 3-8; allocation loop lines 64-84).

## 6. Priority keyword summary (targeting and damage flow)

| Keyword      | Mechanical effect in keyteki (source)                                       |
| ------------ | ---------------------------------------------------------------------------- |
| elusive      | First attack each turn deals no counter-damage from the defender; attacker still damages its declared target (`ResolveFightAction.js:95-129`) |
| assault X    | Attacker deals X damage to the defender before the fight resolves (`Card.js:295-309`) |
| hazardous X  | Defender deals X damage to the attacker before the fight resolves (`Card.js:312-327`) |
| taunt        | Neighbors of the taunt creature cannot be attacked (`Card.js:330-337`), enforced again in `ResolveFightAction.canAffect` |
| ward         | Consumes first damage/destroy attempt instead (`DealDamageAction.js:91-119`; `Card.js:352-382`) |
| armor        | Absorbs damage up to `armorTotal - armorUsed` per event (`DealDamageAction.js:145-204`) |
| poison       | Destroy-on-fatal-damage shortcut for fight damage (`ApplyDamageAction.js:45-57`) |
| splash X     | Neighbors of the target take X damage regardless of elusive (`ResolveFightAction.js:131-149`) |
| invulnerable | Cannot be damaged/destroyed/sacrificed unless the source ignores it (`Card.js:384-410`) |
