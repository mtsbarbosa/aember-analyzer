# Deck Manipulation

Out-of-play cards live in six zones per player: **deck** (ordered), **hand**, **discard** (unordered), **archives**, **purged** (removed from game), and **under card** (attached beneath another card visually). Movement between zones is `player.moveCard(card, zone)` (`server/game/player.js`); every GameAction for these zones raises the corresponding `onCardMoved` event so reactions can fire.

## Draw

`draw` (`server/game/GameActions/DrawAction.js`) is the central draw mechanic with two modes:

- Plain draws: `ability.actions.draw({ amount: 3 })` - can target any player, including the opponent.
- Refill mode: `ability.actions.draw({ refill: true })` draws up to `player.maxHandSize` (6 base, see hand-and-turn-manipulation.md) minus the hand's current size **minus the chain penalty** (`Math.floor((player.chains + 5) / 6)`). When chains > 0, one chain is shed after a full refill draw, which the log announces.

Two engine facts worth copying:

- Drawing from an empty deck auto-recycles: `player.drawWithEmptyDeck()` (`player.js`, lines ~217-227) moves the whole discard pile back into the deck and shuffles, so decks are never exhausted in-game.
- Cards with `drawOneAtATimeDuringTurn` effects split a draw into one-card events so reactions resolve between each card (`getEventArray`).

## Look at top of deck

There is no dedicated look action. The standard pattern is reacting to `player.deck[0]` via a select/skip prompt:

- `(server/game/cards/04-MM/ScoutPete.js)` - `Play/Fight/Reap: Look at the top card of your deck. You may discard that card.` Implementation: `discard` with a `promptWithHandlerMenu` offering the top card plus a "Leave on top of deck" no-op choice, followed by an `alwaysTriggers` `then` that logs "leave it on top" only when the discard event was cancelled.
- Deck visibility state (`isTopCardOfDeckVisible`) is tracked so a shuffled deck with a known-top card re-announces the card (`player.shuffleDeck`, `player.js` line ~232).

## Search

`search` (`server/game/GameActions/SearchAction.js`) lets a player find named/matched cards in deck and discard pile.

- Key properties: `cardName`, `cardCondition`, `location: ['deck', 'discard']` style restrictions; results go to hand, archives, deck top, or into play depending on the card's own follow-up actions.
- The search event records `searchedCards` so `then` blocks can gate follow-up effects ("If you do, ...").
- Search almost always ends with `shuffleDeck`.

Example: `(server/game/cards/01-Core/BearFlute.js)` - `Action: ... If there are no Ancient Bears in play, search your deck and discard pile and put each Ancient Bear from them into your hand. If you do, shuffle your discard pile into your deck.` The `search` runs with a conditional `cardName`, and the `then` uses `preThenEvents[0].searchedCards.length` to trigger `returnToDeck({ shuffle: true })` over the discard pile.

## Discard from hand

- `chosenDiscard({ amount })` - player chooses which cards to discard.
- `discardAtRandom({ amount })` - engine picks randomly (`server/game/GameActions/RandomDiscardAction.js`). `(server/game/cards/01-Core/MindBarb.js)` and `(server/game/cards/01-Core/Tocsin.js)` are minimal examples: `Play: Discard 1 random card from your opponent's hand` -> `gameAction: ability.actions.discardAtRandom()`.
- Remember scrap abilities fire on **voluntary and forced discards from hand** (see docs/card-abilities.md), so discarding is an ability trigger, not just a destruction of resources.

## Discard from deck (milling)

`discardTopOfDeck({ amount })` discards N top cards and reports them; reactions to `onCardDiscarded` still fire. Cards like `(server/game/cards/09-ToC/Suzerain.js)` instead **purge** the top card of a chosen player's deck - mechanically identical zone traversal, different destination.

## Purge

`purge` (`server/game/GameActions/PurgeAction.js`) removes cards **from the game**.

- Destination zone is `purged`; purged cards are out of every normal zone. `locationsHiddenFromPurge = ['hand', 'archives']` blocks an ability from purging its **own source card** blindly out of hand or archives (`canAffect`), so self-purge wording must come from a visible zone.
- Purging tracks provenance: `purgedBy` lets artifacts later restore or count purged cards (`this.purgedBy.purgedCards.push(card)`).
- Gigantic handling: purging a previously composed gigantic from discard prompts to choose a half, and event-time logic only fires `onCardLeavesPlay` if the card is still in play (an earlier effect in the same window may have moved it).
- Purged cards never re-enter normal zones unless a card explicitly returns them.

Examples: `(server/game/cards/01-Core/TheHarderTheyCome.js)` - "Purge a creature with power 5 or higher" (a plain targeted purge from play). `(server/game/cards/01-Core/CustomVirus.js)` - "Purge a creature from your hand. If you do, destroy each creature that shares a trait with it" - note the `then` block checking `onCardPurged` events to gate on the purge actually resolving. `(server/game/cards/07-GR/WellOfMemory.js)` - "Purge any number of cards from your hand. For each card purged this way, return a card from your discard pile to your hand" (purge count drives a follow-up target selector).

## Archive

Archives are a face-down personal pile that cards can later retrieve from. The rule pack treats archives as "second hand for key cards only":

- `archive` (`server/game/GameActions/ArchiveAction.js`) moves cards to a player's archives. Ownership routing: `owner: true` (default) sends the card to its **owner's** archives even if the effect's controller archives an enemy card; `opponent: true` archives into the opponent's pile instead. Cards archived from play fire `onCardLeavesPlay` (amber refunds etc.) before moving.
- Message text varies by origin: from play/discard/purged or `reveal: true` the archived card is named; from hand it logs "archive a card" (no identity leak).
- Cards leave archives via the **house phase choice**: at the start of the turn the active player may take (all or, with the `chooseCardsFromArchives` effect, selected) cards from their archives into their hand (`server/game/gamesteps/house/HousePhase.js` `takeCardsFromArchives`, raising `onArchivesAddedToHand`). Restrictions involved: `takeArchives` (deny the whole step) and `opponentCardsCannotLeaveArchives` (enemy-owned cards stay put).
- Leave-archive routing is the mechanic behind `abduct` (`server/game/GameActions/AbductAction.js`): a card flagged `abducted` returns to its **owner's hand** instead of its owner's discard when it leaves the abductor's archives, and this replacement is implemented centrally (also checked in PurgeAction) rather than per-card.

Examples:

- `(server/game/cards/01-Core/HiddenStash.js)` - `Play: Archive a card.` from hand: `target: { location: 'hand', controller: 'self', gameAction: ability.actions.archive() }`.
- `(server/game/cards/07-GR/Echofly.js)` - `Scrap: Archive the topmost action card from your discard pile` (archive from discard) plus an action that returns discard cards to hand; `(server/game/cards/05-DT/ProfGarwynne.js)` uses `returnToHand({ location: 'archives' })` to pull archived cards back into hand.

## Deck shuffling and recycle

- `shuffleDeck` action and `returnToDeck({ shuffle: true })` both raise `onDeckShuffled`; a notable reaction target (e.g. prophecy fulfillment).
- Whole-discard-pile returns get a special `shuffledDiscardIntoDeck` flag on the shuffle event (`server/game/GameActions/ReturnToDeckAction.js` line ~78), letting cards like `(server/game/cards/07-GR/SabiraTheMedium.js)` react accurately to that specific shuffle.
- Cards that empty hands (`discardEntireLocation`) run a "triggered discard" protocol: both players reveal and may order scrap/lost abilities - active player first unless the `orderForcedAbilities` option is enabled.

## Under cards (embedded cards)

`placeUnder({ parent })` stores cards beneath other cards (purge-tracking creatures, prophecy decks, gigantic halves between plays). The parent `clearDependentCards()` sweeps them on leaving play (`server/game/Card.js` line ~766) - each child resolves `onLeavesPlay` before the parent is gone.
