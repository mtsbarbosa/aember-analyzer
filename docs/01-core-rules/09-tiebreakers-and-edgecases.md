# Tiebreakers, End of Turn and Edge Cases

## Time-limit tiebreakers (implemented, `server/game/game.js:534-594`)

When the game clock expires, the timing sequence is announced first (`server/game/TimeLimit.js:40`):

> "Time has been called. {0} finishes their turn, {1} takes a turn, and {0} takes their final turn until their 'Forge a Key' step has been completed."

Then the comparison:

1. **Simulated forge:** each player who has 6 or more amber forges a key (amber -6, first unforged `keys` slot set true). This means a player about to forge during their final turn still banks the key before tiebreaker comparison.
2. **Most forged keys** wins (`player.getForgedKeys()`).
3. **Most remaining amber** wins.
4. **Fewest chains** wins (`player.chains`).
5. **Most friendly creatures** in play (`p.creaturesInPlay.length`).
6. **First player** of the setup wins.

If still tied after step 6, the game ends without a recorded winner.

## Final turn (`server/game/gamesteps/FinalTurn.js`)

Once time is called, remaining turns are flagged as "final". Per the time-limit message above: current player completes their turn, the other player takes one normal turn, and the first player replays turns until their Forge a Key step has been completed - i.e. a player may require multiple extra turns if they cannot forge yet (6+ amber is guaranteed by the Step 1 simulation at that point, but the check is on completing the key phase).

## Edge cases worth encoding

### Board / rules state
- **Deck out:** when drawing/emptying the deck, keyteki reshuffles the discard pile to rebuild the deck; a player never loses by decking out.
- **Amber on captured cards:** amber captured on a creature belongs to whoever controls the creature's opponent side on refund (see 02-card-mechanics/aember-manipulation.md); destroyed-thief ordering is handled by the destroyed window.
- **Moribund cards:** cards about to leave play still count as in play until the window completes (see 05-combat.md).
- **Win check:** won by forging 3 keys; `checkWinCondition` in `server/game/game.js` runs after each relevant event; tie/time scenarios funnel into the sequence above.
- **Infinite loop guard:** 10-pass breaker in ability resolution, see 02-card-mechanics/timing-precedence.md.

### Manual / UI edge cases (from `server/game/gamesteps/ManualModePrompt.js`, `ManualKeyForgePrompt.js`, `server/game/GameCommands.js`)
- Manual mode lets a host pause and arbitrate any rules situation not representable by the engine at the cost of a chat-only interaction.
- `ManualKeyForgePrompt` lets a player force-forge during a key-cost anomaly not representable normally (e.g. a card turned the forge cost into a nonstandard condition); keys are forged at 0 with amber adjustment done by the opponent if the prompt demands it.

### Files to cross-read
- `server/game/game.js` - tiebreaker implementation (`checkTimeWinCondition`).
- `server/game/TimeLimit.js` - turn-granting sequence on time out.
- `server/game/gamesteps/FinalTurn.js` - final-turn bookkeeping.
- `01-turn-structure.md` - full turn order to place the above in context.
