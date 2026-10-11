# Chains

Chains are a penalty resource a player accumulates: while a player has chains, they draw fewer cards during the draw phase, shedding 1 chain per full group of 6.

## State

- `player.chains` - integer counter, minimum 0, no enforced maximum in the engine (`server/game/player.js:34`, `player.js:764`).
- Chains are tracked as a game stat and reported in the player's serialized state (`player.js:1425`).

## How chains hurt: the draw-phase formula

When a player refills their hand at the end of their turn, the number of cards drawn is reduced by `floor((chains + 5) / 6)`:

```
drawCount = maxHandSize - hand.length - floor((chains + 5) / 6)
```

(`server/game/GameActions/DrawAction.js:48`)

- At 1-5 chains: lose 1 card per refill. At 6-11: lose 2. At 12-17: lose 3, and so on.
- A chain is **shed** (counter decremented by 1) each refill while chains > 0, so 6 refills pay off one "bad hand" bracket: net effect is that 6 chains == 6 reduced draws then the chain count is gone.
- `shedChains` is true whenever `player.chains > 0` and is carried on the draw event so reactions can inspect it (`server/game/Events/types.js:471-475`).

## Getting chains

1. **Card effects.** Many powerful/AoE actions ("Play: Gain 1/2/3 chains") grant chains as a balancing cost. Typical engine call: `ModifyChainsAction` (`server/game/GameActions/ModifyChainsAction.js`), which raises `player.chains` by the amount, clamped at 0 for negative. This raises an `onChainIncrease`-style event (`Events/types.js:703-711`).
   Examples: `Gateway to Dis` (01-Core, 3 chains for destroying all creatures), `Extinction` (02-AoA, 1 chain), `Ballcano` (03-WC, 2 chains), `Krrrzzzaaap` (04-MM), `Maelstrom` (05-DT).
   Some cards also GIVE the opponent chains (e.g. `Binding Irons`, 02-AoA: "Your opponent gains 3 chains") - chains are per-player, so they can be imposed on either party.

2. **Raising the tide (Dark Tidings+).** If the game format supports the tide, raising it costs chains: `chainCost: 3 + player.sumEffects('modifyTideCost')` (`server/game/gamesteps/main/actionwindow.js:67`). The `RaiseTideAction`/`ResetTideAction` flows register this cost and apply it as chains when paid (`server/game/GameActions/RaiseTideAction.js:7-41`).

3. **Adaptive deck bidding (setup).** In Adaptive format, players bid chains on the deck they want to play; the loser's player gets the winning bid applied to them (`server/game/gamesteps/setup/ChainBiddingPrompt.js`, `AdaptiveDeckSelectionPrompt.js`: bidding starts at 0 and players outbid by 1, up to the prompt's menu range; the final bid is assigned via `changeStat(..., 'chains', ...)`).

Note: keyteki does **not** implement deck-import chain costs; no chains are attached to a deck's identity at import time. Chains only enter a game through the three sources above.

## Setups interplay

- A player starting the game with chains (e.g. from an adaptive bid) begins with fewer cards in their opening refill for the same reasons - the setup refill is "draws one extra card, subject to chains" per `server/game/gamesteps/setup/setupphase.js:70`.
- Chains never unlock, expire or reset on their own; they only decrease via the draw-phase shed.

## Design summary for implementers

- Treat chains as: per-player int `>= 0`; additive effect boundary on hand refill; each effect that "gains chains" targets exactly one named player; official tiebreaker priority ranks FEWER chains better (`server/game/game.js:574-576`).
