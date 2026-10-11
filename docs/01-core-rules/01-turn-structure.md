# KeyForge Core Rules - Turn Structure (exact engine order)

Source of truth: keyteki engine (`C:\Users\Mateus\work\keyteki`). Everything below is the actual
implemented order of play, read from the pipeline code. Official rulebook phase names are noted
where they differ from code names.

Key files:

- Game bootstrap and per-round loop: server/game/game.js
- Phase base class (skip-step support, phase begin/end events): server/game/gamesteps/phase.js
- Setup: server/game/gamesteps/setup/setupphase.js (+ the prompts in the same folder)
- Key phase: server/game/gamesteps/key/KeyPhase.js
- House phase: server/game/gamesteps/house/HousePhase.js
- Main phase / action window: server/game/gamesteps/main/MainPhase.js,
  server/game/gamesteps/main/actionwindow.js
- Ready phase: server/game/gamesteps/ReadyPhase.js
- Draw phase: server/game/gamesteps/draw/drawphase.js
- End of round: `Game.endRound` in server/game/game.js:1525-1565
- Final turn: server/game/gamesteps/FinalTurn.js, server/game/gamesteps/TimeLimit.js

Engine concept: the game runs a linear "pipeline" of steps. Each Phase queues its substeps, and
prompts (UI questions) become pipeline steps that block until answered. `beginRound()` re-queues a
fresh round after the last one, making the loop.

---

## A. Game bootstrap

1. `Game.initialise()` (server/game/game.js:925-960):
   - Drops players who left, starts the optional game-time limit (start type
     `whenSetupFinished`, game.js:936-940).
   - Calls `player.initialise()` for each player (prepare decks, reset keys/amber/turn,
     server/game/player.js:274-282).
   - Builds `allCards` from both decks.
   - Initialises the pipeline with exactly two steps:
     1. `new SetupPhase(this)`
     2. `new SimpleStep(this, () => this.beginRound())` (game.js:950-953)
   - Sets `round = 1` and starts the pipeline (`this.continue()`).

## B. Setup phase (order inside SetupPhase)

`SetupPhase` constructor order (server/game/gamesteps/setup/setupphase.js:10-25) - this is the
EXACT setup order:

1. Deck introduction messages (phase start hook, setupphase.js:27-59): each player's deck is
   announced with a keyforgegame.com link plus "with N chains" if the player begins with chains
   (skipped for sealed/alliance formats or when deck lists are hidden).
   If `game.adaptiveFirstDeck` is set, also announces "{deck} will go first".
2. `adaptivedeckselection`: `AdaptiveDeckSelectionPrompt` (adaptive-bo1 only) - both players
   choose which deck to bid on; identical choice opens bidding, different choices may swap decks.
3. `firstplayerselection`: `FirstPlayerSelection` - both players see buttons for each player name
   plus "random" (server/game/gamesteps/setup/FirstPlayerSelection.js:21-29). First button
   clicked decides; if nobody pre-decides, random shuffle at completion; adaptive decks override.
   Sets `game.activePlayer` and `game.firstPlayer`.
4. `setupBegin` - applies any "any location" persistent effects to all cards (setupphase.js:62-66).
5. `GameStartPrompt` - both players must click "Start the Game" (server/game/gamesteps/setup/GameStartPrompt.js).
   This also starts the optional game clock via `onGameStarted` (server/game/gamesteps/TimeLimit.js:14-30).
6. `applyFirstPlayerHandSizeBonus` - the first player receives a temporary +1 hand-size effect,
   so that their opening refill draw is 7 instead of 6, subject to chains (setupphase.js:68-76).
7. `drawStartingHands` - each player's deck is shuffled and filled via a `refill` draw to
   `maxHandSize` (so normally 6, or 7 for the first player, minus chains; see section E3)
   (setupphase.js:78-86). Sets `game.startingHandsDrawn = true`.
8. `removeFirstPlayerHandSizeBonus` - removes the temporary +1 effect (setupphase.js:88-97).
9. `firstPlayerEffects` - applies the First Turn Rule as a lasting effect on the first player:
   once any card has been played, used, or discarded in the game, that player can no longer play
   or discard from their active house normally. Implemented as `noActiveHouseForPlay`, consulted
   at cost-paying time for plays (server/game/costs.js:138-148) and for disard actions
   (server/game/BaseActions/DiscardAction.js:24). Official rulebook First Turn Rule: "During the
   first player's first turn, that player may only play or discard one card from their hand."
   Using cards already in play is NOT restricted. Card abilities that allow more plays still
   work (the effect only blocks the plain active-house allowance).
10. `MulliganPrompt` - simultaneous all-player prompt: "Keep Starting Hand?"/buttons
    `Keep Hand`, `Mulligan` (server/game/gamesteps/setup/mulliganprompt.js). Mulliganed hands go
    back into the deck, are reshuffled, and the player draws one FEWER card than they had.
11. `startGame` - marks every player `readyToStart = true` and raises `onGameStarted`
    (setupphase.js:111-116).

Setup rules NOT in code (official rules, verify your analyzer against the rulebook): cutting or
extra shuffling the opponent's deck; physically placing identity card and 3 key tokens - keyteki
does not need them since identity is `deckData` and keys are counters.

## C. Round loop (per turn)

`Game.beginRound()` (server/game/game.js:999-1032). Every player turn, in EXACT order:

1. If time has been called and both players completed their post-time turns (`timeIsCalled`,
   game.js:1038-1046), instead run `FinalTurn` (see section F) and stop this branch.
2. Inactivity bookkeeping for the "force pass" feature (game.js:1007-1020): force-pass flag
   cleared; active player's activity timestamp refreshed unless they were force-passed.
3. Raise `onTurnStart { player }` event (game.js:1022).
4. `activePlayer.beginRound()` - resets `keysForgedThisRound` (player.js:294-296).
5. `finalizeBeginRound(0)` - raises `onFinalizeBeginRound` repeatedly (up to 100 iterations)
   until nothing changes; supports "beginning of turn" effect loops (game.js:1048-1068).
6. `KeyPhase` (forge a key).
7. `HousePhase` (choose active house + archives pickup).
8. `MainPhase` (play/use/discard - the one long action window).
9. `ReadyPhase` (ready cards).
10. `DrawPhase` (refill hand).
11. `raiseEndRoundEvent` - raises `onTurnEnd`, whose handler runs `Game.endRound` (game.js:1475-1479).
12. Queue another `beginRound()` - the loop continues for the next player.

The same phase list is also visible as queue order in game.js:1025-1029 - KeyPhase FIRST, Draw
phase LAST (matches the official order of play: 1. Forge A Key, 2. Choose A House, 3. Play
Cards, 4. Ready Cards, 5. Draw Cards).

### Phase mechanics common to all phases (server/game/gamesteps/phase.js)

- On `startPhase` (phase.js:19-42): if the active player has a `skipStep` effect whose value is
  this phase's name, the phase - including all of its substeps - is skipped entirely
  (`getEffects('skipStep').includes(this.name)`, phase.js:20-25).
- Raises `onPhaseStarted { phase }`; sets `game.currentPhase`; logs a chat header
  "X phase - player" (setup logs none).
- On `endPhase` (phase.js:44-49): raises `onPhaseEnd { phase }` then
  `game.resetThingsThisPhase()` (clears cardsUsedThisPhase / cardsPlayedThisPhase /
  cardsDiscardedThisPhase / effectsUsedThisPhase / propheciesActivatedThisPhase /
  gainsTextBoxSourcesThisPhase, game.js:1621-1628).

### Known skip-step effects (as of sets 01-14)

- `skipStep('key')`: e.g. server/game/cards/01-Core/Miasma.js, TheSting.js;
  04-MM/MiasmaBomb.js; 05-DT/RedhandRegistry.js; 07-GR/DarkHeartOfTheForest.js.
- `skipStep('ready')` and `skipStep('draw')` together: 03-WC/BookOfLeq.js.
- `skipStep('ready')`: 12-PV/PoisedStrike.js.
- `skipStep('draw')`: 11-VM25/TheConstant.js.

There is no `skipStep('house')` or `skipStep('main')` in current data.

## D. Each phase, step by step

### D1. KeyPhase (forge a key) - server/game/gamesteps/key/KeyPhase.js

1. If `activePlayer.canForgeKey()` (player.js:884-918): resolve `forgeKey` action.
   No player decision; the forge is automatic as per official rules.
2. If additionally `canForgeKey() && anyEffect('canForgeSecondKeyDuringKeyPhase')`, forge a
   second key immediately (KeyPhase.js:18-30).
3. Otherwise log: "{player} does not forge a key. They have N amber. The current cost is M amber"
   (KeyPhase.js:31-38). Cost = `6 + sumEffects('modifyKeyCost')`.

Note: there is no implemented "bridge a key for 0" mechanic in sets 01-14; a zero-cost forge is
just `modifyKeyCost` totaling -6 or less (or a card action paying the whole cost for you). Cards
may also forge with amber on cards (`forgeAmberSource`) or the opponent's pool
(`forgeWithOpponentsAmber`).

### D2. HousePhase - server/game/gamesteps/house/HousePhase.js

1. `chooseHouse`: builds the legal house list from `getAvailableHouses()`:
   - 0 choices: log "has no legal choices for active house this turn, and so must play without an
     active house" and continue (HousePhase.js:37-42).
   - Otherwise: `promptWithHandlerMenu` "Choose Active House" / "Choose which house you want to
     activate this turn" (HousePhase.js:19-36). On choose: log "{player} chooses {house} as their
     active house this turn", raise `onChooseActiveHouse { player, house }`, then
     `activePlayer.activeHouse = house.text`.
   - The chosen house is gone at end of turn (`activeHouse = null`, game.js:1543) but a turn can
     re-choose mid-turn only where card effects allow it (e.g. `stopHouseChoice` variants) -
     normally it is locked for the turn.
2. `takeCardsFromArchives` (HousePhase.js:45-141):
   - Skipped entirely if the player has a `takeArchives` restriction.
   - If `chooseCardsFromArchives` effect present: multi-select prompt "Do you wish to take cards
     in archives into your hand?" with an "All Cards" button; selection is optional (`numCards: 0`,
     `optional: true`). Only cards the controller owns can be taken if
     `opponentCardsCannotLeaveArchives` is in play.
   - Otherwise: simple Yes/No handler menu "Do you wish to take all the cards in archives into
     your hand?". Yes moves all (owned) archive cards to hand.
   - Raises `onArchivesAddedToHand` where applicable (Events/types.js:110).

This pickup is a house-phase free action and does not count against the active house; it happens
even if the player had no legal house choice.

### D3. MainPhase - server/game/gamesteps/main/MainPhase.js, .../main/actionwindow.js

Single substep: one `ActionWindow` (a `UiPrompt`) that loops until the player ends the turn.

Active player controls:

- Click a card in play: `UseAction` if usable, else `card.use(player)` (actionwindow.js:8-25).
  `Card.use` (server/game/Card.js:1256-1307) either auto-resolves the single legal action or shows
  the "Play {card}:" / "Choose an ability:" handler menu with all legal actions (Fight, Reap,
  remove stun, Action/Omni abilities, play-as-upgrade from hand, discard) plus Cancel.
- Drag a hand card:
  - To play area: the single legal Play action auto-resolves; multiple play actions open "How
    would you like to play this card?" (e.g. Play creature / Play as upgrade) with a Cancel
    button (actionwindow.js:27-46).
  - To discard: resolves `DiscardAction` (only legal when active house matches or card ignores
    houses; the First Turn Rule `noActiveHouseForPlay` also gates this,
    BaseActions/DiscardAction.js:24).
- Click the tide marker: `RaiseTideAction` with cost `3 + modifyTideCost` chains; confirm Yes/No
  prompt (actionwindow.js:64-85).
- Click one of their prophecies: `ActivateProphecyAction`; confirm Yes/No prompt
  (actionwindow.js:87-107).
- End Turn button (`menuCommand 'done'`, actionwindow.js:162-179): if any card in hand or play
  still has a legal action, or a prophecy can be activated, a confirm prompt
  "Are you sure you want to end your turn?" Yes/No appears; otherwise the turn ends immediately.
- `/manual` chat command path: while manual-mode active, a "manual" button opens a card select
  prompt that just logs "uses {card}'s ability" (actionwindow.js:147-159).

Phase-ending interruptions (actionwindow.js:109-124):

- `game.endPhaseRightNow` - a card effect can end the phase instantly; every action queues a
  follow-up `checkForPhaseEnding` step that watches for it.
- `game.omegaCard` - a card with Omega was played this window: log "played {card} which has
  Omega, ending this step" and end the phase.

The waiting opponent sees only "Waiting for opponent" (actionwindow.js:136-138). Non-active
players take no actions here beyond triggered-ability prompts (section E and doc 02).

### D4. ReadyPhase - server/game/gamesteps/ReadyPhase.js

1. `promptForEntrenched` (ReadyPhase.js:18-53): if the player has a `ready` restriction, stop.
   Collect exhausted friends with the `entrench` keyword that can be readied; if any exist,
   show an unlimited multi-select "Select entrenched creatures to keep exhausted" with a Done
   button. (Entrench is a newer keyword: a flagged creature STAYS exhausted this phase.)
2. `readyCards` (ReadyPhase.js:68-104):
   - Stop if the player-level `ready` restriction denies it.
   - Pre-filter cards that `wouldReadyThisPhase` (ReadyPhase.js:59-66): currently exhausted, not
     an entrenched creature the player declined, `readiesDuringReadyPhase()`, and passes the
     per-card `ready` restriction.
   - If none, nothing happens (no event, no message - important for Storm-Surge-style blockers).
   - Otherwise raise ONE `onCardsReadied { cards, context }` event; listeners may mutate
     `event.cards`; then log "{player} readies their cards" and `card.ready()` each.

### D5. DrawPhase - server/game/gamesteps/draw/drawphase.js

One substep: `draw { refill: true }` for the active player (drawphase.js:10-14).

Refill math (server/game/GameActions/DrawAction.js:42-55):

- `amount = maxHandSize - hand.length - floor((chains + 5) / 6)` i.e. minus `ceil(chains/6)`.
- If amount > 0 and the player HAS chains (`shedChains = chains > 0`) they shed exactly 1 chain
  after drawing (DrawAction.js:76-83, log "sheds 1 chain to N chains").
- Cards are drawn one at a time; if the deck empties mid-draw, `drawWithEmptyDeck` shuffles the
  discard pile into a fresh deck and continues (player.js:184-227). If discard is also empty, the
  player simply draws as many as exist.
- Overfill: nothing happens; if a player has MORE than maxHandSize cards they are not trimmed.
- Players with `drawOneAtATimeDuringTurn` effects get their draws wrapped as separate
  one-card events (DrawAction.js:88-124).

### D6. End of round - server/game/game.js:1525-1565

1. If the finishing player `canForgeKey()`, log/alert "{player} declares Check!".
2. `activePlayer.endRound()` - clears each in-play card's `new` flag; `player.turn++`
   (player.js:298-304).
3. Reset per-round trackers (`cardNamesPlayedOrUsed`, `cardsUsed`, `omegaCard`, `cardsPlayed`,
   `cardsDiscarded`) and per-phase trackers (`resetThingsThisPhase`).
4. Every in-play card runs `card.endRound()` (turn-scoped effect expiry on cards).
5. `activePlayer.activeHouse = null`.
6. Active player passes to the opponent UNLESS the finishing player has an `anotherTurn` effect
   (game.js:1545-1547).
7. Log "End of turn {round}", list both players' amber and key counts.
8. Increment `game.round` only if the new active player has taken the same number of turns as
   their opponent (game.js:1555-1560) - i.e. a round is a full cycle through both players.
9. Alert "Turn {round} - {player}".
10. `checkForTimeExpired` (game.js:989-993) records the round if the time limit is already
    exhausted (feeds the final-turn trigger condition).

## E. Player-agnostic / shared windows inside the loop

These interrupt normal flow at any point (detailed spec in doc 02):

- Raised events open an `EventWindow` for every triggered effect: first `interrupt` listeners
  (which may mutate or cancel the event), then the event resolves, then `reaction` listeners
  resolve (server/game/Events/EventWindow.js:14-23).
- Destruction cascades open a dedicated `DestroyedAbilityWindow` batching all Destroyed: and
  leaves-play triggers before cards hit the discard
  (server/game/gamesteps/DestroyedAbilityWindow.js:19-35).
- Simultaneous delayed effects open a `SimultaneousEffectWindow`
  (server/game/gamesteps/SimultaneousEffectWindow.js, opened from `game.openSimultaneousEffectWindow`,
  game.js:1100-1106).

## F. Time limit, final turn, and tie-breakers

- The optional game clock starts on `onGameStarted` (server/game/gamesteps/TimeLimit.js:14-30)
  and fires `game.timeExpired()` (emit `onTimeExpired`, game.js:1375-1377) when the configured
  number of minutes elapses.
- When time is called mid-turn: an alert states the plan - "Time has been called. {0} finishes
  their turn, {1} takes a turn, and {0} takes their final turn until their 'Forge a Key' step has
  been completed..." (TimeLimit.js:38-45).
- Each subsequent `beginRound` is recorded in `playedRoundsAfterTime` (game.js:989-993). Once both
  players have completed one post-time turn, `timeIsCalled()` starts `FinalTurn`
  (game.js:1038-1046) instead of a normal round.
- `FinalTurn` (server/game/gamesteps/FinalTurn.js:31-50):
  1. Alert "Time has been called. {player} takes their final turn until their 'Forge a Key' step
     has been completed."
  2. Raise `onTurnStart`, run `activePlayer.beginRound()`.
  3. Queue `KeyPhase` (forge) and only that.
  4. Then `endFinalTurn`: alert "Final turn has ended", set `finalTurnCompleted = true`, call
     `game.checkWinCondition()`.
- `checkWinCondition` (game.js:607-630): first winner-not-yet-recorded check - all 3 keys forged
  wins; if the time limit AND final turn are complete, run
  `checkTimeWinCondition` (game.js:543-601), the tie-breaker ladder:

  1. Each player with >= 6 amber forges a key immediately (`amber -= 6`, keys[next unforged] = true).
  2. Most forged keys wins.
  3. Most remaining amber wins.
  4. Fewest chains wins.
  5. Most creatures in play wins.
  6. First player (the set-aside `game.firstPlayer`) wins.

- A tied/finished game can be continued past the win prompt; `continuePlaying` re-opens the
  post-game menu without clearing the original winner (game.js:638-661).

## G. Force pass (inactivity)

Not a rules mechanic but part of turn flow:

- If the active player sends no game commands for 5 minutes (less on repeat), the game flags
  inactivity and offers the opponent a force-pass (server/game/game.js:194-246).
- `forcePass` (game.js:1482-1523) wipes the remaining pipeline (cancels prompts), queues
  `raiseEndRoundEvent` then `beginRound` - i.e. it force-ends the stalled player's turn cleanly.

## H. Manual mode

- Toggled with the `/manual` chat command or settings toggle; since it can corrupt game state it
  requires opponent approval via a simultaneous `ManualModePrompt` (Allow Yes/No)
  (server/game/gamesteps/ManualModePrompt.js:16-45; server/game/chatcommands.js:310-319). Turning
  it OFF is unilateral (chatcommands.js:310-313).
- While active (server/game/player.js:324-360, server/game/GameCommands.js:27-101):
  - Any drag-and-drop between legal zones is allowed and logged ("manually moves {card} ...").
  - `changeStat` (amber/keys/armor/etc.), `modifyKey`, `changeActiveHouse` become client-driven.
  - Every select and menu prompt gains a "Cancel Prompt" button
    (server/game/gamesteps/selectcardprompt.js:192-194,
    server/game/gamesteps/handlermenuprompt.js:73-75).
  - Token creatures can be created from the deck top via `/token-creature`
    (chatcommands.js:321-358).
  - Card menus (MenuCommands.js) allow moving cards to hand/deck/archives/purged manually.
- `manualModeUsed` is recorded in game stats (true if manual mode was ever used in the game).

## I. Checklist summary (the definitive loop)

Setup:

1. Announce decks (+ chains).
2. Adaptive deck selection (adaptive only).
3. (Possible) Chain bidding (adaptive only).
4. First player selection.
5. Apply location persistent effects.
6. Start Game confirmation (both players).
7. First player +1 hand size bonus.
8. Refill draws (starting hands) for both players.
9. Remove hand-size bonus.
10. First Turn Rule effect applied.
11. Mulligan prompt (both players, simultaneous).
12. readyToStart = true; onGameStarted (clock may start).

Each turn:

13. (Time-called branch -> FinalTurn: onTurnStart, beginRound, KeyPhase, win check.)
14. onTurnStart; player.beginRound (reset keysForgedThisRound).
15. finalizeBeginRound loop (onFinalizeBeginRound until stable).
16. Key phase: automatic forge (x2 allowed with effect) or "does not forge" log.
17. House phase: choose active house; optional archive pickup.
18. Main phase: play / use / discard / raise tide / activate prophecy / end turn (with confirm),
    with Omega or effects possibly ending it early.
19. Ready phase: entrench selection (if any), ready eligible cards via onCardsReadied.
20. Draw phase: refill to maxHandSize minus ceil(chains/6), shed 1 chain if applicable, reshuffle
    discard if the deck runs dry.
21. End round: "Check!" notice, per-round resets, active player reversal (unless anotherTurn),
    round increment, next turn.
