# KeyForge Core Rules - Phases, Decisions, and Windows (interaction spec)

Audience: an agent implementing the PLAYER-INTERACTION layer (prompts, buttons, menus, windows)
of a KeyForge engine. This is the exhaustive list of what the human-or-bot sees and can click per
phase, derived from keyteki (`C:\Users\Mateus\work\keyteki`). Turn order lives in
`01-turn-structure.md`; this file assumes that order and expands every decision point.

Prompt plumbing conventions used throughout:

- Every interactive step is a `UiPrompt` (server/game/gamesteps/uiprompt.js). It owns a uuid,
  exposes `activePrompt()` for the choosing player(s) and `waitingPrompt()` for everyone else.
  Buttons are stamped `command: 'menuButton'` + prompt uuid so the server can route clicks
  (uiprompt.js:37-53). Input entry points on Game: `menuButton` (game.js:867-883),
  `cardClicked` (game.js:407-451), `drop` (game.js:493-532), `menuItemClick` (game.js:471-491).
- While a prompt is live, `player.setPrompt(...)` also starts the player's decision clock
  (uiprompt.js:19-28); prompts are the ONLY place where inputs are accepted.

---

## 1. Prompt types (the menu vocabulary)

### 1.1 Base UiPrompt (server/game/gamesteps/uiprompt.js)

- States per player: `activeCondition(player)` (default: `player === game.activePlayer`)
  decides who gets the active prompt; everyone else sees `waitingPrompt()`
  (default `{ menuTitle: 'Waiting for opponent' }`).
- All prompts do not complete until `complete()`; an explicit make-complete comes from players'
  inputs or auto-resolution logic.

### 1.2 SelectCardPrompt - "choose card(s)" (server/game/gamesteps/selectcardprompt.js)

Properties an implementer must support (selectcardprompt.js:7-47):

- `numCards` (0 = unlimited), `multiSelect` (always return an array), `buttons` (extra menu
  buttons alongside the card grid), `activePromptTitle`, `waitingPromptTitle`, `cardCondition`,
  `cardType`, `maxStat`/`cardStat`, `gameAction` (cards must be legal targets of this action),
  `onSelect` / `onMenuCommand` / `onCancel` (return true to complete),
  `source` (what the prompt belongs to - card, string, or EffectSource; used for promptTitle and
  the "targeting" control overlay), `ordered` (display selection order), `optional`
  (evaluated against the context; when true a Done button appears even with 0 selected),
  `revealTargets` (temporarily reveal selectable facedown cards), `controls` (UI overlay
  definition, default a `targeting` control listing context targets).
- Behavior: click toggles selection; `automaticFireOnSelect` prompts fire onSelect as soon as the
  selector's limit is reached (single-select prompts resolve immediately on click,
  selectcardprompt.js:222-241); a `Done` button is auto-injected when selection is sufficient or
  the prompt is optional (selectcardprompt.js:181-207); manual mode always adds `Cancel Prompt`.
- Selection state is exposed to the client via `selectedCards` / `selectableCards` and
  `promptedPiles` (which piles - hand/discard/archives/purged/deck - should be open for browsing,
  selectcardprompt.js:146-175).
- `onCancel` also serves any custom button that returns early; custom buttons go through
  `onMenuCommand(player, arg)` and complete the prompt if they return truthy.

### 1.3 HandlerMenuPrompt - "choose one of N buttons" (server/game/gamesteps/handlermenuprompt.js)

- `choices` (array of strings or `{text, ...}`), `handlers` (parallel array of callbacks), OR
  `cards` + `cardHandler` (buttons are card names; clicking a listed card also works,
  handlermenuprompt.js:122-142). `promptTitle`, `activePromptTitle`, `waitingPromptTitle`,
  `source`, `controls`, and a `choiceHandler` (receives the raw choice object) are supported.
- Manual mode appends a `Cancel Prompt` button (handlermenuprompt.js:73-75). Used everywhere:
  house choice, Yes/No confirmations, "Choose an ability", play-allowance selection.

### 1.4 MenuPrompt - context-method menu (server/game/gamesteps/menuprompt.js)

- A `promptWithMenu(player, contextObj, { activePrompt: { buttons: [{text, method, ...}] } })`
  where each button carries a `method` name; clicking calls `contextObj.method(context, player,
  arg, method)` (menuprompt.js:42-52). Buttons are validated against the prompt definition
  (hasMethodButtonOrControl, menuprompt.js:54-63). Rarely used by core phases; used by some cards.

### 1.5 AllPlayerPrompt - simultaneous player decisions (server/game/gamesteps/allplayerprompt.js)

- `activeCondition(player)` = NOT yet completed for that player; the prompt stays open until
  `completionCondition(player)` is true for ALL players (allplayerprompt.js:3-17). Each player
  answers independently; nobody sees the other's choice until completion (unless the prompt logs
  it, as mulligan does for the first player only).
- Users: MulliganPrompt, FirstPlayerSelection, GameStartPrompt, AdaptiveDeckSelectionPrompt,
  ChainBiddingPrompt, ManualModePrompt.

### 1.6 AllocateDamagePrompt (server/game/gamesteps/AllocateDamagePrompt.js)

- Used by `AllocateDamageAction` when damage must be divided. Active player repeatedly clicks
  creatures; each click adds `damageStep` damage to that creature (+ `splash` to neighbors when
  configured, AllocateDamagePrompt.js:83-106). Selectable set driven by a `CardSelector`
  (`selector.canTarget`, AllocateDamagePrompt.js:60-67). Completes automatically when the total
  allocation reaches `damageStep * numSteps` (AllocateDamagePrompt.js:73-80). Hangs a running
  `cardDamage` map on the prompt payload so the client can show partial allocation.
  Manual mode adds Cancel (AllocateDamagePrompt.js:30-34).

### 1.7 AllocateAmberPrompt (server/game/gamesteps/AllocateAmberPrompt.js)

- Amber-capture distribution ("Choose a creature to capture 1 amber on" or a custom menuTitle).
  Clicks add `amberStep` per click; completes at `numSteps` (AllocateAmberPrompt.js:64-84).
  Used by AllocateCaptureAction. All clicks are a single batch - state changes resolve after the
  whole allocation (AllocateAmberPrompt.js:1-9). Manual mode adds Cancel.

### 1.8 ForcedTriggeredAbilityWindow (server/game/gamesteps/forcedtriggeredabilitywindow.js)

The triggered-ability chooser; spawns for every event with listeners (see section 3). Its own
prompts:

- `promptBetweenSources` (forcedtriggeredabilitywindow.js:121-196): if multiple different source
  cards have triggers, a select prompt over the dynamic `cardType` list
  `['action','artifact','creature','upgrade']` (+ `'prophecy'` when relevant). Buttons are built
  for "lasting trigger" cards (e.g. triggers from cards being played). Special buttons:
  - `Done` - allowed ONLY when every choice is optional (stops resolving remaining optional
    triggers) (getPromptForSelectProperties, forcedtriggeredabilitywindow.js:198-232).
  - `Autoresolve` - offered when NO choice is optional and the player option
    `orderForcedAbilities` is on; pressing it lets the engine resolve the rest automatically.
- `promptBetweenAbilities` (same file, below line 240): after a source is picked, if several
  abilities of that source could resolve, a HandlerMenuPrompt "Choose an ability" style prompt.
- Optional-ability prompts honor the player option settings; `Optional.EvalOptional` gates
  whether an optional trigger even prompts (triggeredabilitywindowtitles / filterChoices).

### 1.9 DestroyedTriggeredAbilityWindow (server/game/gamesteps/DestroyedAbilityWindow.js)

- Specialized ForcedTriggeredAbilityWindow for destruction. Core semantics an engine must copy
  (DestroyedAbilityWindow.js:10-35):
  - Cascade batching: cards destroyed BY Destroyed: abilities during the window stay on the board
    (tagged) until the window closes; their triggers are merged into the same window
    (`addBatchedDestroyEvent`, `discardAllTaggedCards` at DestroyedAbilityWindow.js:107-164).
  - Ability lock-in: a card's Destroyed: triggers are locked to the abilities it had when tagged;
    abilities gained later are rejected unless the event was "just tagged"
    (addChoice, DestroyedAbilityWindow.js:37-64).
  - On close: surviving players' order decides nothing - the engine moves every still-present
    tagged card to its owner's discard (discardAllTaggedCards).

### 1.10 SimultaneousEffectWindow (server/game/gamesteps/SimultaneousEffectWindow.js)

- Orderer for simultaneously created delayed ("until X happens") effects. If more than one and
  the current player's option `orderForcedAbilities` is on, a HandlerMenuPrompt
  "Choose an effect to be resolved" lets the player order them; otherwise the first is resolved
  with no prompt (SimultaneousEffectWindow.js:16-45).

### 1.11 OptionsMenuPrompt

- Dropdown-style option chooser (`promptWithOptionsMenu`, server/game/game.js:848-855): arbitrary
  option lists needed by some card prompts (e.g. named-card selection). Used sparingly.

---

## 2. Decisions and windows PER PHASE

Below, "active player" = the player whose turn it is. "Open window" = both players may respond.

### 2.1 Setup phase (see doc 04 for the full flow)

Active decisions (details in 04-mulligan-and-setup.md):

1. AdaptiveDeckSelectionPrompt (adaptive-bo1): both players, "Choose the deck to bid on" -
   buttons are both deck names (AdaptiveDeckSelectionPrompt.js:25-54).
2. ChainBiddingPrompt (opened mid-setup if both picked the same deck): bid buttons
   `currentChains+1 .. currentChains+10` plus `Pass` (ChainBiddingPrompt.js:17-28). Open window:
   any player not owning the bid deck can bid or pass; bidding resets the button list; a pass
   ends it (ChainBiddingPrompt.js:30-62).
3. FirstPlayerSelection: open window; both players see BOTH player-name buttons plus `random`
   (FirstPlayerSelection.js:21-29). First click wins the message ("chooses to go first/second").
4. GameStartPrompt: open window; both players click "Start the Game" (GameStartPrompt.js).
5. MulliganPrompt: open window; both players choose `Keep Hand` / `Mulligan`; first player's
   choice is logged immediately, the other player's is deferred so neither reveals early
   (mulliganprompt.js:31-61). Mulligan = reshuffle hand, draw one FEWER than you had
   (server/game/player.js:246-256).

### 2.2 Key phase (server/game/gamesteps/key/KeyPhase.js)

- NO interactive choice. Engine-only:
  - Auto-forge when `canForgeKey()`; auto forge a SECOND key while
    `canForgeSecondKeyDuringKeyPhase` is in effect (KeyPhase.js:10-30).
  - Log-only branch otherwise.
- If you need "choose which color key": the engine picks the next unforged color internally
  (player.js:943+; `getUnforgedKeys` used for optional color-specific forge effects).
- Interactive hooks that CAN appear here are only triggered-ability windows raised by the forge
  event (`onForgeKey` reactions) and any simultaneous delayed effects.

### 2.3 House phase (server/game/gamesteps/house/HousePhase.js)

Active player decisions:

1. "Choose Active House" HandlerMenuPrompt. Buttons = available houses (one per house, icon
   attached). Source label 'House Choice'. No Cancel - a choice is mandatory when choices exist
   (HousePhase.js:14-43).
2. Archive pickup, only if the player has cards in `archives` and no `takeArchives` restriction:
   - With a `chooseCardsFromArchives` effect: SelectCardPrompt, optional, multiSelect, unlimited
     (numCards 0), starting button "All Cards"; cardCondition restricts to pickable archives
     cards (owned ones only under `opponentCardsCannotLeaveArchives`). Choosing Done with
     nothing = pick up nothing; onSelect with cards moves exactly those (plus the event if the
     archives are emptied).
   - Without it: HandlerMenuPrompt Yes/No "Do you wish to take all the cards in archives into
     your hand?" (HousePhase.js:113-139). Yes = move every legal archive card to hand.
3. Triggered windows as usual. `onChooseActiveHouse` and `onArchivesAddedToHand` are the events
  to open.

Not a decision: the house choice is final; `activeHouse` survives until the end of turn except
where cards change it.

### 2.4 Main phase (server/game/gamesteps/main/actionwindow.js)

The dominant interaction surface. The active player repeats free actions until they press End
Turn (or Omega/effects end the phase):

- Buttons visible to the active player while idle in this window: exactly one - "End Turn"
  (`arg: 'done'`) (actionwindow.js:126-134). Everything else is driven by clicking cards/dragging.
- Card actions enumerable via `Card.getActions(player, ignoreHouse)` (server/game/Card.js:1309-1321).
  For a hand card: Play creature/artifact/action, optionally Play-as-upgrade, plus
  Discard (Card.js:1362-1377). For a creature in play: Fight, Reap, Remove stun, card Action/ Omni
  abilities (Card.js:1378-1389). "Ignore house" actions (Omni) are enabled regardless of house
  match.
- Clicking a hand card opens the per-card menu "Play {card}:" with every legal action as a button
  + Cancel (Card.js:1269-1305). A single legal action may be auto-resolved (unless the user
  enables confirm-one-click, Card.js:1259-1268).
- Clicking a creature/artifact in play: UseAction (creature: pick Fight / Reap / remove stun /
  action ability) or directly the use menu (actionwindow.js:8-25).
- Drag-to-play prompts: "How would you like to play this card?" when a card has >1 play action
  (actionwindow.js:30-46).
- Play-cost intricacies (server/game/costs.js:100-148):
  - "First thing this phase" gate (firstThingThisPhase, game.js:1612-1619) supports Alpha;
  - 6-name-per-round cap (unusual duplicate-name effects);
  - play-allowance effects (house-permit effects) - if more than one could authorize the play,
    a HandlerMenuPrompt asks WHICH allowance to spend (costs.js:183-231).
- Creature placement prompts at play time (server/game/GameActions/PutIntoPlayAction.js):
  - deploy=0 creatures: "Which flank do you want to place this creature on?" (left/right)
    (PutIntoPlayAction.js:147).
  - DEPLOY creatures: "Select a card to deploy to the {flank} of" - pick any card in any
    battleline as an anchor (PutIntoPlayAction.js:199).
- Gigantic creatures with two top-part copies in hand: "Choose a top part to play" select
  prompt (server/game/BaseActions/PlayCreatureAction.js:23-48).
- Fighting: target select "Choose a creature to attack" over enemy creatures (filtered by Taunt /
  taunt-ignoring (server/game/Card.js:1323-1342)). The fight resolution itself opens windows but
  almost no decisions (see section 3.3).
- Tide: click the tide marker -> "Raise the Tide?" Yes/No HandlerMenuPrompt; cost
  `3 + sumEffects('modifyTideCost')` chains (actionwindow.js:64-85).
- Prophecy: click one of your faceup prophecy cards -> "Activate prophecy?" Yes/No prompt
  (actionwindow.js:87-107).
- End Turn confirmation: "Are you sure you want to end your turn?" Yes/No only if playable cards
  or activatable prophecies remain (actionwindow.js:162-179). YES ends the window and therefore
  the phase.

Open (opponent-visible) windows DURING the main phase - the non-active player never gets the
free-action menu but DOES get:

- Every ForcedTriggeredAbilityWindow spawned by an event: interrupts first (they may cancel or
  mutate the pending action), reactions after resolution (EventWindow pipeline, below).
- If the active player is prompted by a nested ability prompt whose target includes the
  opponent's cards, the opponent still only watches ("Waiting for opponent") - interactive
  targeting always belongs to `promptForSelect`'s choosing player.
- Discard/order prompts that belong to the active player exclusively.

Phase-ending mechanics the engine must observe here: `endPhaseRightNow` (effect-forced end),
`omegaCard` (Omega keyword ends the whole main phase the moment any Omega card is played)
(actionwindow.js:109-124).

### 2.5 Ready phase (server/game/gamesteps/ReadyPhase.js)

- Player decision ONLY when the player has exhausted friendly creatures with the `entrench`
  keyword and readying is not blocked: SelectCardPrompt, unlimited, with Done,
  "Select entrenched creatures to keep exhausted" (ReadyPhase.js:36-52). Untouched = ready
  normally.
- Everything else is automatic; an `onCardsReadied` reaction window can theoretically appear.

### 2.6 Draw phase (server/game/gamesteps/draw/drawphase.js)

- No player decision. The refill is one resolved action; it opens the usual `onDrawCards`
  interrupt/reaction windows (chains-prevention effects live here), and empty-deck reshuffles
  produce `onDeckShuffled` events with their windows.

### 2.7 End of round

- `onTurnEnd` reaction window (raised by `raiseEndRoundEvent`, game.js:1475-1479) - this is the
  classic "At the end of your turn" trigger window. Multiple simultaneous reactions are ordered
  by the active player (or auto-resolved per option settings).
- Then `Game.endRound` automation (no prompts).

---

## 3. Cross-phase shared windows (the effect engine's window spec)

### 3.1 EventWindow - the standard event lifecycle

Every game Event goes through this pipeline (server/game/Events/EventWindow.js:14-23), executed
in EXACT order:

1. `checkEventCondition` - re-evaluate whether the event still fires (targets may have vanished).
2. `openAbilityWindow('interrupt')` - ForcedTriggeredAbilityWindow over all `interrupt`
   listeners of the event AND its simultaneous sibling events. Destruction events instead open a
   DestroyedAbilityWindow (EventWindow.js:36-56), and nested destruction windows are suppressed
   while one is already open (EventWindow.js:46-48).
3. `preResolutionEffects` - listeners on `{event}:preResolution` may still bend the event.
4. `executeHandler` - run the event handler per simultaneous event (each individually
   re-checked and cancelable).
5. `checkForSubEvent` - sub-events (e.g. a leavesPlay under a destroy) are replayed as nested
   EventWindows; their leavesPlayEvent reference bubbles up to the parent.
6. `checkGameState` - recalculates effects, control transfers, terminal conditions, and tags
   lethal creatures for destruction (server/game/game.js:1414-1455).
7. `openAbilityWindow('reaction')` - ForcedTriggeredAbilityWindow over `reaction` listeners
   (`After:` / `While:` wording), plus `checkDelayedEffects` for pending "until X" effects
   (EventWindow.js:51-56).

### 3.2 ForcedTriggeredAbilityWindow - ordering semantics (non-active players matter!)

- `currentPlayer` is the game's ACTIVE player at window creation; they get priority to resolve
  first. Choices auto-resolve (no prompt) when: there is exactly 1 choice, the player disabled
  `orderForcedAbilities`, or all choices are non-optional with auto-resolve granted
  (forcedtriggeredabilitywindow.js:81-119).
- Deferred choices: abilities whose requirements failed at emit time are remembered and get a
  second chance when another trigger changes state (forcedtriggeredabilitywindow.js:59-94).
- Player decisions inside the window: which source, which ability, Done (decline optional),
  Autoresolve (let engine finish the window). Cancellation is only possible in manual mode.

### 3.3 Fight resolution windows (server/game/GameActions/ResolveFightAction.js)

Little player-facing choice, MANY windows:

- Attack target selection happens BEFORE the fight event (Card.js:1323-1342).
- `onInitiateFight` interrupt/reaction windows (e.g. effects that replace or cancel the fight).
- `onFight` event: both damage events (defender damage to attacker vs attacker damage to
  defender) are built simultaneously, mutual, and attached as children in one event
  (ResolveFightAction.js:57-120); Elusive skips return damage; Skirmish skips forward damage;
  armor applies per hit unless ignored.
- Then a destruction EventWindow (DestroyedAbilityWindow) covers everything that died.
- Damage splitting beyond single-target fights uses AllocateDamagePrompt (1.6).

### 3.4 Game-state check (server/game/game.js:1414-1455)

Engine-only, but its outputs create windows:

- Recalculates persistent effects, may flag control transfers (`takeControl`), destroys every
  creature with `power <= 0` or `damage >= power` (via `destroy()` action - which opens the
  standard destruction windows), refreshes armor tokens, and checks terminal conditions.
- Runs after every resolved ability and inside every EventWindow; your interaction layer must
  tolerate a destroy cascade interrupting any prompt.

### 3.5 What a player can NEVER do via the interaction layer

- Act while another player owns the current prompt (except AllPlayerPrompt-style simultaneous
  prompts and their own triggered windows).
- Choose a target illegal per `cardCondition`/`canAffect`; the server re-validates every click
  (`checkCardCondition`, selectcardprompt.js:243-254).
- Undo a resolved ability - only letter-of-the-prompt Cancel buttons (and manual mode) abort
  pending prompts, never past resolves.
- Look at their facedown deck unless an effect reveals it (CardVisibility rules +
  `revealTargets`).

---

## 4. Implementation checklist for the interaction layer

1. Prompt queue is FIFO pipeline: only the topmost step accepts input; each prompt step owns a
   uuid and validates every incoming button/card click against it
   (uiprompt.js:78-87; game.js:867-883).
2. Prompt payloads must include: promptTitle, menuTitle, buttons[] (text + arg + command +
   uuid), controls[] (targeting overlay), selectCard/selectOrder flags, and card lists where
   relevant. Copy `activePrompt()` shapes from SetCardPrompt/HandlerMenuPrompt for compatibility.
3. Support all prompt variants in section 1, including simultaneous (AllPlayerPrompt) prompts and
   the allocation prompts with incremental counters.
4. Correct waiting prompts per non-choosing player, with waitingPromptTitle overrides.
5. Manual-mode affordances: Cancel Prompt buttons everywhere, drag-and-drop movement,
   stat/key/house change commands (GameCommands.js).
6. Player option settings that change behavior: `confirmOneClick` (one actionable card click asks
   before resolving; Card.js:1263) and `orderForcedAbilities` (trigger ordering; default OFF on
   server per test/server notes, ON in test harness).
7. Remember the two "cancel" paths: prompt-level Cancel buttons vs hard pipeline cancel
   (`pipeline.cancelStep`, used by mid-change hand-card reselection in game.js:430-447).
8. Every autoresolution must still trigger its effect: auto forge in KeyPhase, autoresolve of
   single/forced choices, and auto done of optional windows when nothing to choose.
