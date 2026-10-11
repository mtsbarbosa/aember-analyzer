# 03 - Amber and Forging

This document describes how amber (the resource), bonus icons, and key forging work in the
keyteki engine (the open-source KeyForge implementation). Every claim cites a keyteki source
path so an agent can verify or re-derive the rule from code.

## 1. Amber locations

Amber exists in exactly these tracked places:

- **Player pool**: `player.amber`, an integer starting at 0 (`server/game/player.js:277`).
  Forging is paid from this pool, plus any alternative "amber source" effects
  (see section 5.4, "Paying from alternative amber sources").
- **On creatures (captured)**: an `amber` token on a card in play. `card.amber` returns the
  token count (`server/game/Card.js:1135-1137`).
- **Bonus icons (printed/enhanced)**: not amber at all until the card is played and its bonus
  icons resolve (see section 3).

### 1.1 Captured amber does NOT count toward forging

- Captured amber lives as a token on the creature, not in the player pool. The forge cost
  check only reads `this.amber` (pool), alternative `forgeAmberSource` effects, and
  `forgeWithOpponentsAmber` effects (`server/game/player.js:910-917`).
- On-creature amber is therefore inert for forging unless a card effect explicitly moves it
  (e.g. "move 1 amber from a creature to your pool", implemented per-card with
  `game.actions.gainAmber`).
- **When a creature leaves play or is destroyed, all amber on it goes to the controller's
  opponent's pool, not the controller's.** This is framework behavior in
  `Card.onLeavesPlay()`: if the card is a creature with an `amber` token it resolves
  `gainAmber({ amount: this.amber })` for `this.controller.opponent`
  (`server/game/Card.js:750-755`); the token map is then cleared (`this.tokens = {}`,
  `server/game/Card.js:760`).
- A card granting "spend amber on this card when forging" uses the `forgeAmberSource` effect
  see "Paying from alternative amber sources"; absent such an effect, the forge prompts cannot select those tokens.

## 2. Gaining amber into the pool

- **Framework gain**: `game.actions.gainAmber` is `ModifyAmberAction` with a positive amount;
  it emits the `onModifyAmber` event and calls `player.modifyAmber(amount)`
  (`server/game/GameActions/ModifyAmberAction.js:24-30`). `modifyAmber` clamps the pool at a
  floor of 0 (`server/game/player.js:759-761`).
- **Reaping**: `ResolveReapAction` raises `onReap` and, in its handler, calls
  `game.actions.gainAmber({ reap: true })` for the reaping player
  (`server/game/GameActions/ResolveReapAction.js:20-22`). The default reap gain is exactly
  1 amber; bonus icons on the reaped card are NOT auto-resolved on reap (bonus icons only
  resolve when playing a card). A creature also raises `onUseCard` as a child event of the
  reap event (`ResolveReapAction.js:24-30`).
- **Card effects**: any card that reads "gain X amber" is implemented with
  `ability.actions.gainAmber(...)`, which routes into `ModifyAmberAction`.
- **Bonus icons**: the `amber` bonus icon gains exactly 1 amber per icon when the card is
  played (`server/game/GameActions/ResolveBonusIconsAction.js:185-195`).

## 3. Bonus icons and enhancement amber

### 3.1 Where bonus icons come from

- Card data stored under `master-vault-data/packs/<SET>.json` carries `amber` (printed bonus
  amber; e.g. 1 or 2 pips) and `enhancements` (an array of enhancement icons added by the
  KeyForge Enhancement mechanic, e.g. `["amber"]` or house names like `"staralliance"`).
- `Card` constructor stores both: `this.cardPrintedAmber = cardData.amber` and
  `this.enhancements = cardData.enhancements` (`server/game/Card.js:72-75`).
- The `bonusIcons` getter builds the list: one `amber` entry per printed amber pip, then the
  `enhancements` array appended (`server/game/Card.js:265-278`). Effects may override it:
  `copyCard` copies the copied card's icons, `modifyBonusIcons` replaces the list entirely
  (used by e.g. `playAsCopy`) (`server/game/Card.js:266-272`,
  `server/game/helpers/playAsCopy.js:133`).
- House-name enhancements (e.g. an "Enhance (Star Alliance)" pip) do NOT produce amber; they
  are stripped by `getResolvableBonusIcons()`, which filters out icons whose normalized name
  is a house (`server/game/Card.js:697-702`). House enhancements instead influence which
  houses are available for the active house choice via `getHouseEnhancements()`
  (`server/game/Card.js:688-695`, `server/game/player.js:778-815`).

### 3.2 Resolving bonus icons (play, and only play)

- Bonus icons resolve when the card is played, via `ResolveBonusIconsAction`
  (`server/game/GameActions/ResolveBonusIconsAction.js`). The action's event handler iterates
  `event.card.getResolvableBonusIcons()` and queues one resolution step per icon, honoring
  `resolveBonusIconsAdditionalTime` effects that repeat icons
  (`ResolveBonusIconsAction.js:342-358`).
- Each icon resolution builds a synthetic `AbilityContext` whose `source` is a
  `BonusIconSource`, not the card itself; this keeps source-trait/type checks from treating
  the icon as "dealt by" the card (`ResolveBonusIconsAction.js:160-182`,
  `server/game/BonusIconSource.js:16-35`).
- Icon types and their precise effects (`ResolveBonusIconsAction.js:184-340`):
  - `amber`: gain 1 amber to the pool.
  - `capture`: prompt to choose one of your creatures; capture 1 amber onto it.
  - `damage`: prompt to choose any creature; deal 1 damage to it.
  - `discard`: discard 1 card from your hand.
  - `draw`: draw 1 card.
  - `steal`: steal 1 amber (only if opponent has amber). This is normally reachable only
    through a replacement (see below).
  - `power`: add a +1 power counter to a chosen creature.
  - `token-creature`: make a token creature from your deck's token card.
- **Replacement ("may resolve X as Y")**: effects granting `mayResolveBonusIconsAs`
  (e.g. replacing an amber icon with capture) are offered as prompt choices per icon.
  Terminals (`steal`, `token-creature`) cannot be further replaced; a source already used in
  the chain cannot be reused (`ResolveBonusIconsAction.js:10-56, 58-158`).

## 4. Capturing, stealing, transferring

All amounts are clamped by the victim's available pool amber:

- **Capture** (`CaptureAction`, `server/game/GameActions/CaptureAction.js`): moves amber from
  the chosen player's pool (default: the capturing player's opponent) onto a creature in play
  as an `amber` token. Emits `onCapture`. Defaults: `amount = 1`, source player is
  `context.player.opponent`; requires the source player to have amber unless
  `allowNoAmber`. If the applied amount exceeds the pool it clamps
  (`CaptureAction.js:58-76`). Cards granting `captureFromPool` capture without removing
  amber from the pool (`CaptureAction.js:67-72`, registered in
  `server/game/effects.js:165`).
- **Steal** (`StealAction`, `server/game/GameActions/StealAction.js`): moves amber from the
  victim player to the acting player (two separate pool adjustments). Emits `onStealAmber`.
  Subject to `abilityRestrictions` preventing steal; a prevention message names the
  preventing effect's source (`StealAction.js:24-56`). `stealFromPool` steals without
  decreasing the victim pool (`StealAction.js:65-67`).
- **Transfer** (`TransferAmberAction`,
  `server/game/GameActions/TransferAmberAction.js`): moves amber from a target player to
  that player's opponent (a "give/take" move, distinct from steal only in rules framing).
  Emits `onTransferAmber` (`TransferAmberAction.js:24-36`).

## 5. Forging keys

### 5.1 State

- `player.keys = { red: bool, blue: bool, yellow: bool }`; a player needs exactly three keys
  forged to win. `keysForgedThisRound` tracks how many were forged in the current round
  (`server/game/player.js:1332-1334` forge marks them; reset happens with round setup).
- `canForgeKey(modifier = 0, keyColor = '')`
  (`server/game/player.js:884-918`) checks, in order:
  1. The player can pass the `forge` restriction check.
  2. Not all keys are already forged.
  3. If a `keyColor` was specified it must still be unforged.
  4. If `keysForgedThisRound.length > 1` and the player has
     `cannotForgeMoreThan2KeysInATurn`, forging is blocked.
  5. Pool amber + alternative amber sources + opponent-pool-usable amber is at least the
     current key cost.
- The prompt order for choosing which key to forge is **Red, Blue, Yellow**; that is the
  order `getUnforgedKeys()` hard-codes (`server/game/player.js:1376-1382`). Key colors have
  no per-color cost in this engine; `modifyKeyCost` applies to every key.

### 5.2 Key phase (automatic forging)

- The Key Phase runs a single step: if the active player `canForgeKey()`, it resolves
  `game.actions.forgeKey()` with a framework context. After the first forge, if the player
  `canForgeKey()` again AND has the `canForgeSecondKeyDuringKeyPhase` effect, a second forge
  resolves (`server/game/gamesteps/key/KeyPhase.js:10-30`).
- If the player cannot forge, a chat message announces their amber and the current cost
  (`KeyPhase.js:31-38`).

### 5.3 The forge action and cost

- `ForgeAction` (`server/game/GameActions/ForgeAction.js`):
  - Properties default to `modifier = 0`, `atNoCost = false`, optional `keyColor`.
  - `getModifier(player)`: if `atNoCost`, returns `-player.getCurrentKeyCost()` (i.e. the
    total modifier reduces the cost to 0); otherwise the explicit `modifier`
    (`ForgeAction.js:21-23`).
  - `canAffect` gates on `player.canForgeKey(modifier, keyColor)` (`ForgeAction.js:25-30`).
  - The `onForgeKey` event handler calls `player.forgeKey(modifier, keyColor)` and records
    `event.amberSpent` (`ForgeAction.js:36-44`).
- `player.forgeKey(modifier, keyColor)` (`server/game/player.js:943-1017`):
  - `cost = max(0, getCurrentKeyCost() + modifier)`.
  - Payment sources are gathered, then paid through a prompt sequence (5.4), the key color is
    chosen if multiple remain (5.5), and `finalizeForge` executes the payment.
- **Cost formula**: `getCurrentKeyCost() = sumEffects('modifyKeyCost') + 6`
  (`server/game/player.js:935-937`). The base cost is always 6; all `modifyKeyCost`
  effects stack additively. There is no "lowest cost first" color logic because colors do
  not have separate costs.
- "Forge a key with 0 amber" style effects (e.g. Blorb hive, Epic Quest, The Long Con) are
  implemented with `modifier: -context.player.getCurrentKeyCost()`, making the effective
  cost 0 while still consuming pool amber of 0. `atNoCost: true` (e.g. Sample42C) achieves
  the same via `getModifier` (`server/game/cards/06-WoE/BlorbHive.js:28-30`,
  `server/game/cards/12-PV/Sample42C.js:21`).
- Unforging is symmetric: `UnforgeAction` raises `onUnforgeKey` and calls
  `player.unforgeKey()` which prompts for which forged key (if several) to unforge
  (`server/game/GameActions/UnforgeAction.js`, `server/game/game.js:1526`,
  `server/game/player.js:1337-1374`).

### 5.4 Paying from alternative amber sources

- `getAmberSources()` returns in-play cards usable to pay via the `forgeAmberSource` effect:
  - controller's cards with on-card amber tokens (`player: 'controller'`,
    `sourceType: 'onCard'`),
  - opponent's cards with tokens the player may spend (`player: 'opponent'`),
  - cards that count as 1 amber themselves (`sourceType: 'card'`)
    (`server/game/player.js:817-847`; effect factory `server/game/effects.js:72`).
- `canForgeKey` counts available alternative amber with
  `getAmberValueFromSource` (`token count + 1 if the card counts as amber`)
  (`server/game/player.js:867-882, 910-913`).
- During payment, players are prompted: "Select an amber source to use", then how many
  tokens to spend from it, and whether to also spend the card itself as 1 amber
  (`server/game/player.js:1019-1254`).
- `finalizeForge` then removes tokens, discards cards spent as amber (also removing their
  ward), deducts `modifiedCost` from the pool, applies `redirectForgeAmber` effects (e.g. The
  Sting redirects the full cost to the opponent), sets the key, and logs
  (`server/game/player.js:1284-1335`).
- Spending from the **opponent's pool** is supported by `forgeWithOpponentsAmber` effects
  (capped by `getMaxAmberFromOpponentPool`);   prompts ask how much to spend from the
  opponent's pool (`server/game/player.js:920-933, 950-1016`).

### 5.5 Choosing the key color

- `chooseKeyToForge` filters unforged keys by `keyColor` if one was forced by the card
  (e.g. the Aemberdrake trio in `09-AS` force red/blue/yellow). If more than one color
  remains, the player is prompted "Which key would you like to forge?" with choices Red,
  Blue, Yellow in that order; a forced single color is chosen automatically
  (`server/game/player.js:1256-1282`).

### 5.6 Manual forging (judging / dev mode)

- In manual mode a player can request forging a key of a given color, which opens
  `ManualKeyForgePrompt` (an `AllPlayerPrompt`) asking the opponent to allow it. On
  completion the key is simply set: `keys[color] = true` and pushed to
  `keysForgedThisRound`; no amber is spent and no forge event fires
  (`server/game/gamesteps/ManualKeyForgePrompt.js:1-62`).

### 5.7 Second keys in the key phase and related restrictions

- `canForgeSecondKeyDuringKeyPhase` (registered in `server/game/effects.js:172-173`) allows
  the key phase to forge a second key, gated behind a fresh `canForgeKey()` check
  (`KeyPhase.js:19-30`).
- `cannotForgeMoreThan2KeysInATurn` (registered at `server/game/effects.js:174-175`) caps
  forges once `keysForgedThisRound.length > 1` (`server/game/player.js:902-908`).
- Card-triggered forges outside the key phase use `ForgeAction` directly and are subject to
  `canForgeKey` and any `forge` restriction effects.

## 6. Query helpers

- `getStats()` exposes `amber`, `chains`, `keys`, `keyCost` for the client
  (`server/game/player.js:1422-1436`).
- `SignsPointToYes` and similar cards compare `activePlayer.amber >= getCurrentKeyCost()`
  ("can afford a key without spending card-external sources") is possible by combining
  `player.amber` with `getCurrentKeyCost()` directly in card code
  (`server/game/cards/12-PV/SignsPointToYes.js:10`).

## 7. Gotchas for an analyzer

- On-card amber is NOT the player's pool; captured amber belongs to the card, and on
  `onLeavesPlay` it goes to the CONTROLLER'S OPPONENT (not the player who originally
  captured it).
- `modifyKeyCost` sums over ALL effects, including negative ones; costs can be driven to 0
  (or lower, clamped at 0 by `Math.max(0, ...)` in
  `player.forgeKey`, `server/game/player.js:944`).
- Reaps never resolve bonus icons; bonus icons resolve on play only.
- The `atNoCost` flag on `ForgeAction` computes the modifier dynamically from the current
  cost, so it respects cost modifiers at resolution time rather than construction time.
