# KeyForge Core Rules - Setup, Mulligan, Chains, First Player, Adaptive

Source of truth: keyteki (`C:\Users\Mateus\work\keyteki`), primarily:

- server/game/gamesteps/setup/setupphase.js
- server/game/gamesteps/setup/FirstPlayerSelection.js
- server/game/gamesteps/setup/MulliganPrompt.js (file name: mulliganprompt.js)
- server/game/gamesteps/setup/GameStartPrompt.js
- server/game/gamesteps/setup/AdaptiveDeckSelectionPrompt.js
- server/game/gamesteps/setup/ChainBiddingPrompt.js
- server/game/player.js (`initialise`, `prepareDecks`, `takeMulligan`, `drawCardsToHand`)
- server/game/deck.js (`Deck.prepare`)
- server/game/GameActions/DrawAction.js (chain-draw math)
- server/game/game.js (game bootstrap, adaptive swap `reInitialisePlayers`)

Official rulebook references are quoted where behavior differs or where keyteki deliberately
deviates.

---

## 1. What happens before the first card is drawn

### 1.1 Player and deck preparation

1. `Game.initialise()` exists to build players; for each player `Player.initialise()`
   (server/game/player.js:274-282):
   - `prepareDecks()` - instantiates the whole deck (24/36 cards, token card, prophecies) from
     `deckData` via `Deck.prepare` (deck.js:49-90). Card copies are expanded from the deck list's
     `count` fields; maverick/anomaly house overrides applied; `enhancements` attached.
   - `keys = { red: false, blue: false, yellow: false }`, `amber = 0`, `turn = 1`,
     `readyToStart = false`, `chains = 0` (constructor, player.js:34), opponent links set.
2. `Game.reInitialisePlayers(swap)` (game.js:962-987) can be called during setup by adaptive
   prompts: it optionally SWAPS the two players' `deckData`/`houses` (a full deck swap becomes
   possible: each player ends up playing the deck the other brought), then rebuilds cards.

### 1.2 Deck announcement (deck introduction)

`SetupPhase.startPhase()` (server/game/gamesteps/setup/setupphase.js:27-59):

- For every player, when `gameFormat` is NOT `sealed` or `alliance` and deck lists are not
  hidden: message "{player} brings {decklink} to The Crucible", appending
  " with {N} chains" when the player starts with chains.
- If `game.adaptiveFirstDeck` was pre-selected: info alert "{deck} will go first".

That is the entire "deck introduction"; there is no reveal of the deck list unless
`hideDeckLists` is false for the game.

### 1.3 Initial chains

- `player.chains` starts at 0 (player.js:34).
- Chains reach a player through:
  1. Adaptive chain bidding (`game.changeStat(..., 'chains', N)`), ChainBiddingPrompt.js:52;
  2. test/setup harness values (`chains: N` in `setupTest`, test/server/chains.spec.js:16) -
     i.e. the engine supports players STARTING with pre-applied chains;
  3. card effects during the game.
- Chains reduce every `refill`-style draw (starting hand AND each draw phase) by
  `ceil(chains / 6)` and are shed one chain per qualifying draw (see 3.4).

---

## 2. First player selection (exact rules)

Prompt: `FirstPlayerSelection` (server/game/gamesteps/setup/FirstPlayerSelection.js), an
AllPlayerPrompt evaluated simultaneously for both players.

- Active prompt: "{menuTitle} Who will go first?" with a button PER PLAYER NAME plus a `random`
  button (FirstPlayerSelection.js:21-29). Waiting prompt: "Waiting for opponent to choose to
  continue who will go first".
- Completion condition per player (FirstPlayerSelection.js:12-19): complete if this player IS the
  previous game's winner (`game.previousWinner`), OR there is no previous winner recorded (fresh
  match - the prompt effectively auto-completes for both), OR anyone has already clicked a
  button, OR an adaptive first deck is pre-decided.
- menuCommand (FirstPlayerSelection.js:35-57): whichever button a player clicks SETS the first
  player immediately:
  - click own name -> "{player} chooses to go first";
  - click opponent name -> "{player} chooses to go second" (opponent first);
  - click `random` -> "{player} chooses to randomize the first player"; the actual assignment
    happens at `onCompleted` (below).
- `onCompleted` (FirstPlayerSelection.js:59-76), fallback rules in order:
  1. If `game.adaptiveFirstDeck` exists, the player whose `deckData === adaptiveFirstDeck`
     becomes first player ("{player} becomes the first player!").
  2. Else if `game.activePlayer` is unset (everyone pressed random):
     both players are shuffled; the survivor is first player: "{player} won the flip and is
     first player".
  3. Finally `game.firstPlayer = game.activePlayer` (kept for the post-time tiebreaker).
- Official rules note: official play randomizes the first player ("Randomly determine who is the
  first player"); in a match series the loser of the previous game picks first player. Keyteki
  models this series rule with `previousWinner` auto-completion - the returning winner does not
  get re-asked (FirstPlayerSelection.js:13-14). keyteki also allows an explicit negotiated pick,
  which is a house rule on top of the official random rule; document it as a keyteki addon.

## 3. Starting hands and mulligan (exact rules)

### 3.1 Hand-size bonus for the first player

`applyFirstPlayerHandSizeBonus` (setupphase.js:68-76): the first player receives a
`untilPlayerTurnEnd` `modifyHandSize(1)` effect, so the simultaneous refill draw they get in step
3.2 is 7 cards instead of 6 (still reduced by chains). `removeFirstPlayerHandSizeBonus`
(setupphase.js:88-97) strips it immediately after drawing.

Official equivalence: rulebook setup step 6 = "The first player draws a starting hand of seven
cards. The other player draws a starting hand of six cards." The keyteki implementation reaches
the same numbers through the max-hand-size draw.

### 3.2 Draw starting hands

`drawStartingHands` (setupphase.js:78-86): for EACH player, resolve `shuffleDeck()` then a
`draw({ refill: true })`. Refill math (server/game/GameActions/DrawAction.js:42-55):

- `amount = maxHandSize - hand.length - floor((chains + 5) / 6)` (= 6 or 7, minus
  `ceil(chains/6)` for chained players).
- Chains at setup do NOT shed chains (the player had none yet at the draw) but DO reduce count.
- Sets `game.startingHandsDrawn = true`.

IMPLEMENTATION DIFFERENCE vs official rules: official rules say chains reduce draws made "at the
start of a turn" during the draw-cards step; the rulebook's setup does NOT visibly reduce the
opening hand for chained decks (it gates the mulligan redraw instead: "they do not shed a chain
from the mulligan, but do draw one fewer ... as per the normal mulligan"). Keyteki's use of
`refill` applies the chain reduction to the OPENING hand as well. If your analyzer must mirror
official rules, apply chain reduction ONLY to draw-phase refills and mulligan redraws, not to the
raw starting hand of 6/7.

### 3.3 Mulligan

Prompt: `MulliganPrompt` (server/game/gamesteps/setup/mulliganprompt.js), simultaneous
AllPlayerPrompt:

- Active prompt: "Keep Starting Hand?" buttons `Keep Hand` / `Mulligan` (mulliganprompt.js:14-23).
- Both players answer simultaneously; the FIRST player's choice is logged immediately,
  the second player's is buffered and logged at completion so neither hints early
  (mulliganprompt.js:40-53, 56-61).
- `player.takeMulligan()` (server/game/player.js:246-256):
  1. `size = hand.length`.
  2. Move ALL hand cards to deck; shuffle deck.
  3. `drawCardsToHand(size - 1)` - one FEWER card than you had. This is a plain (non-refill)
     draw: chains are neither shed nor additionally subtracted here.
  4. `mulliganDecided = true`.
- There is NO limit on mulligans: each player has exactly ONE opportunity (completion condition
  locks the prompt decision, mulliganprompt.js:31-38) - maximum one mulligan per player per game.
- A mulligan is OPTIONAL; "Keep Hand" is a valid selection.
- Official rulebook equivalence: "Each player, starting with the first player, has one
  opportunity to mulligan their starting hand by shuffling it back into their deck and drawing a
  new starting hand with one fewer card in it. After a player chooses to mulligan, that player
  must keep the new starting hand." (Also, chained decks: no chain shed from mulligan, redraw is
  one fewer than the pre-mulligan hand - keyteki matches.)
- keyteki runs the two mulligans simultaneously rather than strictly first-then-second; the
  result is identical because each mulligan only touches the mulliganing player's own deck.

### 3.4 Empty deck / out of cards during setup or play

- `drawCardsToHand(numCards)` (server/game/player.js:184-212): draws as many as the deck holds;
  if more are needed and the DISCARD pile is not empty, `drawWithEmptyDeck()` moves the whole
  discard back into deck and shuffles it (message "{player} attempts to draw with an empty deck,
  so they shuffle their discard pile to reset their deck", player.js:214-227), then recursively
  draws the remainder.
- If discard is also empty, the player just draws fewer. There is no loss, no penalty, and no
  "milled out" condition - KeyForge has no deck-out loss (official rules: "If a player needs to
  draw cards ... and cannot because their deck is empty, that player shuffles their discard pile
  to reset their deck, and then continues to draw").
- Smaller-than-base decks (sealed variants/alliance experiments) work identically; the engine has
  no minimum deck-size enforcement at runtime, only deck-verification at selection time.

### 3.5 First Turn Rule setup (what keyteki encodes)

`firstPlayerEffects` (setupphase.js:99-109): during setup, a lasting-effect is placed on the
first player that makes their normal active-house play allowance vanish the moment any card has
been played, used, or discarded this game (`noActiveHouseForPlay` consulted in
server/game/costs.js:138-148 for plays and server/game/BaseActions/DiscardAction.js:24 for
discards). Net effect = official First Turn Rule ("During the first player's first turn, that
player may only play or discard one card from their hand."). Card-granted allowances still work;
using cards already in play is unrestricted.

### 3.6 Start game confirmation

`GameStartPrompt` (server/game/gamesteps/setup/GameStartPrompt.js) - BOTH players click "Start
the Game". While waiting: "Waiting for opponent to indicate they are ready". The menu shows
"{player} will go first" with the active player interpolated. This step exists after first
player selection and before hands, exactly as listed in `setupphase.js` initialise order. It is
also what arms the optional game clock (`onGameStarted` -> `TimeLimit.startTimer`,
server/game/gamesteps/TimeLimit.js:17-20). After the prompt resolves, `startGame()` sets
`readyToStart = true` for all players and raises `onGameStarted` (setupphase.js:111-116).

---

## 4. Adaptive (Best of 1) setup - deck selection and chain bidding

Only for `gameFormat === 'adaptive-bo1'` (client/Components/Games/GameFormats.jsx:18),
driven by two prompts and a swap:

### 4.1 AdaptiveDeckSelectionPrompt

(server/game/gamesteps/setup/AdaptiveDeckSelectionPrompt.js) - FIRST setup prompt even before
first-player selection.

- On construction: if `gameFormat === 'adaptive-bo1'` and no pre-set
  `game.adaptiveFirstDeck`, randomly pick one player's deck as `adaptiveFirstDeck` (:95-100) -
  this deck wins first-player ties.
- Active prompt: "Choose the deck to bid on", ALL players simultaneously; buttons = the deck each
  player brought, identified by `deckData.uuid`/name (:25-54).
- `menuCommand` records the selection per player into `game.adaptive.selection`.
- `onCompleted` (:56-93):
  - Same deck chosen by both (same uuid): message "Both players have selected {deck}. Bidding
    will start at 0 chains by {owner}" and QUEUE a ChainBiddingPrompt (mid-setup prompt, see
    4.2). NOTE: the selection may be the deck EITHER player brought - the "owner" here is the
    player who OWNS the deck data.
  - Different decks: each player gets announced as playing the deck they picked, and if the
    overall winner deck is not a player's own, `game.reInitialisePlayers(true)` swaps the two
    decks entirely (the DECKS swap owners, matchmaking-adaptive style) (:77-92).

### 4.2 ChainBiddingPrompt

(server/game/gamesteps/setup/ChainBiddingPrompt.js) - runs inside setup when both players
selected the same deck.

- Bid deck owner is pre-marked as "clicked" (they never bid for their own deck) (:3-11).
- Active prompt (opponent only effectively): "Bid chains for selected deck" with buttons
  `adaptive.chains + 1 ... + 10` and a `Pass` button (:17-28). Bid MINIMUM is
  `current bid + 1`; there is no upper bound beyond the 10-button window.
- Pass (:30-54): the OTHER player wins the bidding:
  - message "{winner} has won the bid with {N} chains and will play {decklink}";
  - `game.changeStat(winner, 'chains', adaptive.chains, true)` applies the chain handicap to the
    winner (they now owe chains);
  - `game.adaptive.biddingWinner = winner name`.
- Higher bid (:55-59): message "{player} bids {N} chains", `adaptive.chains = N`, and the button
  set shifts (10 values above the new floor, always > current).
- The prompt closes when both players (including the bid-deck owner via the initial-only mark)
  have "clicked" - practically, when the opponent passes.
- Rematch / continuing handling (chain persistence, service-side): the /swap command
  (server/game/chatcommands.js:418) allows rematching in adaptive-bo1 to re-run the sequence.

### 4.3 Adaptive and first player interplay

- `adaptiveFirstDeck` (random or pre-set) forces that deck's owner to be the first player; the
  FirstPlayerSelection prompt auto-completes when it is set
  (FirstPlayerSelection.js:12-19, 59-67).
- If a chain bid actually happened (deck biddingWinner known), the winner starts chains per 4.2;
  chains are then visible in the deck announcement message ("with N chains",
  setupphase.js:47) - meaning the announcement prompt runs AFTER bidding even though
  logic order in setupphase lists the announcement first (it is emitted when the START step
  executes, which is after the adaptive chain prompt's queue step resolves; note setupphase
  queues AdaptiveDeckSelectionPrompt as the first step (setupphase.js:13-24) BEFORE the
  announcement is emitted - quiet unless your analyzer reorders).

---

## 5. Setup checklist (definitive numbered list, keyteki order)

Setup pipeline from `SetupPhase` constructor (setupphase.js:10-25):

1. (Adaptive-bo1 only) AdaptiveDeckSelectionPrompt -> possible ChainBiddingPrompt chain bid;
   possible full deck swap via `reInitialisePlayers(true)`.
2. FirstPlayerSelection (may auto-complete; sets activePlayer + firstPlayer).
3. setupBegin: apply any location persistent effects to all cards.
4. GameStartPrompt: both players confirm "Start the Game" (arms the optional time limit).
5. applyFirstPlayerHandSizeBonus: +1 hand size on the first player this "turn".
6. drawStartingHands: shuffle + refill draw for BOTH players (6/7 minus ceil(chains/6)).
7. removeFirstPlayerHandSizeBonus.
8. firstPlayerEffects: First Turn Rule lasting effect.
9. MulliganPrompt: simultaneous optional mulligans (each player max once; redraw = one FEWER).
10. startGame: `readyToStart = true` for all; raise `onGameStarted` (clock starts here).

Immediately after setup, `Game.beginRound()` runs for the first player - see
`01-turn-structure.md` for the turn loop.

## 6. What keyteki deliberately does NOT implement at setup

- No opponent deck cut / inspection (official rulebook has players offer their deck for a final
  cut; keyteki only self-shuffles at drawStartingHands).
- No physical board assembly (identity card, key tokens) - the Archon identity lives in
  `deckData`, keys are three boolean flags.
- No pre-game reveal of card list when `hideDeckLists` is on, or in sealed/alliance.
- No "Legacy" identity handling - see `00-overview.md` section 6.
