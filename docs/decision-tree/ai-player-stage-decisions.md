# AI Player Decision Analysis - Stage by Stage

How to build an AI that plays KeyForge instead of a human. This assumes the rule knowledge in
`docs/01-core-rules/` and `docs/02-card-mechanics/`; each section cross-references the stage it
covers in `docs/decision-tree/player-decisions-cheatsheet.md`. Focus is WHAT to evaluate, HOW
to search, and WHERE the hard problems are, per game stage.

---

## 1. Framing: what makes KeyForge a particular AI problem

- **The turn is one big, mostly-open decision.** The player picks 1 house, then freely
  sequences up to ~10-20 uses/plays in any order. Unlike Hearthstone-style games, there is no
  resource curve; "energy" is per-card-use exhaustion. This makes the main phase a deep
  sequential planning problem, but with a small branching rule per node.
- **Deterministic core, hidden information on top.** Given both hands and deck orders, a KeyForge
  turn is fully deterministic (event windows aside) - a virtual engine can roll a turn forward.
  Hidden info: opponent hand, both deck orders, and the two discard piles (public, faceup).
- **Deck lists are public knowledge.** KeyForge decks are fixed and published (Master Vault).
  Online, both players can know the exact 36-card lists. So "hidden" state shrinks to
  permutations of known sets, enabling Bayesian hand/deck inference instead of blind guessing.
- **Asymmetric macro decision.** House choice (once per turn) gates almost everything; amber
  economics (forge keys) is the win axis; tempo/defense is the support axis. The AI's core
  competency is alternating between "amber multiplication" and "hosing the opponent" plans.

## 2. Global evaluation function (state -> number)

Everything below leans on one scalar; design it first.

Primary terms (in an ideal weighting):

- chatGlossary -> **Forge differential**: keys forged (3 = win, terminal).
- **Projected extra turns to win**: turns until this player can forge each remaining key at
  expected pricing - `ceil(cost_projection / per-turn amber income)`.
- **Amber velocity**: current pool + expected +1-income per turn (reapers, bonus pips in hand,
  per-turn effects). Weight captured-but-spendable amber separately from stolen-candidate amber.
- **Opponent denial line**: how much of THEIR velocity is threatened by our board (steals,
  fights, enters-play effects), and the symmetrical risk (their steals/fights into our board).
- **Board control** as a means, not an end: creature count weighted by (power/defense/threat).
- **Hand quality**: number of playable-at-active-house options per possible house, broadly
  "cards that look like they win the turn".
- **Deck integrity**: cards remaining in own deck vs opponent's; deck-out risk
  (`discard reshuffles`, but reshuffed decks are weaker), purge/archive imbalance.
- **Chains**: negative term (see `docs/01-core-rules/08-chains.md`) - each 6 chains is roughly a
  lost draw phase PLUS the immediate card deficit; discount heavily when playing long-plan.
- **Defensive tokens**: wards, armor, stun markers on key threats.
- **Tide** (if the format supports it): mostly a tempo currency dominated by whoever raises it
  for value; treat as a resource amortized over 2-3 turns.

Design rules for the evaluation function:

- No term alone should dominate: a "most creatures wins" heuristic will happily re-capture
  instead of stealing.
- Terminal and forced states (forged 3rd key, deck reshuffle loop, chain spiral) must short
  circuit the eval - don't let the search be afraid of a "bad-looking" permanent board trade
  that forges a key.
- Evaluation should run in the VIRTUAL engine (right-side board copy + active player notion),
  not on real server state.

## 3. Stage-by-stage deep analysis

### 3.1 Setup: adaptive deck selection and chain bidding

Decision: which of the two adaptive decks to choose, at what bid.

- Enumerate candidate decks. For each, simulate a v0 heuristic "opening book": count amber
  pips in the 36-card list, count cards that make a creature/attribute ready on entering play,
  count key-cost-modifiers (those are the swing factors), estimate expected cards-per-turn
  with chains (see `docs/01-core-rules/08-chains.md`).
- Bid rule of thumb: the "unfairness" price is derived from expected key-tempo difference over
  ~4 turns, expressed as chains; each chain loses about `1/6 card per turn` (see the shed
  formula). Compare vs the win probability estimate.
- First player choice: in general first player takes tempo (7-card opening hand via the +1
  hand-size effect, and swings board first); second player sometimes preferred in
  amber-scaling/expected-late archetypes when the "double forge later" tempo swing matters more
  than the early tempo. Formalize as: simulate turns 1-6 baseline for both seat assignments and
  take the better expected result rather than a fixed rule.

### 3.2 Mulligan (per player)

- Objective: maximize expected value of opening 6 participating in your FIRST turn's planned
  action instead of "total card strength".
- Score a hand by: (a) does it enable a 3+ action first turn in the draft house; (b) does it
  include archetypal early catalyst cards (reapers that give amber, cheap deployers, entry
  effects, 0-cost or "free-play" style cards, e.g. the DinoBeast/DinoBot suite that reads
  "creature with +1/+1 if it has amber" - see engine verb sets in `07-card-database/` files).
- Mulligan threshold: keep if top turn-1 line judged ">= median" over simulated redraws
  (~a few thousand subsamples of the deck list); otherwise mulligan. Never mulligan into an
  empty house gap deck - if the hand is 1-house, count redraw fills the same house bias.
- Chains interplay: chains reduce the refill from the FIRST end-of-turn, so a mulligan under
  chains is effectively trading a card for variety; generally hold under heavy chains.

### 3.3 Key phase

- In baseline engine behavior the key phase auto-forges when affordable
  (`docs/01-core-rules/03-aember-and-forging.md`) - there is no "forge or not" decision for the
  standard case. AI still needs to plan around the CONSEQUENCE: forge timing is a public clock.
- Real decision points live in card text: keys that cost `modifyKeyCost`, cannot forge
  (`skipStep('key')`), or "forge a key with 0" - see key-cost-modifiers.
- AI-relevant micro-check: if you could forge NEXT turn only through opponent denial (their
  steals, capture, or key-cost increases), treat "can I forge this turn" as a forcing-move
  trigger. When a forge is possible and the alternative path also forges, usually forge now to
  lock the resource since captured/stolen amber conversions are one-way.

### 3.4 House choice (once per turn, usually the dominant decision)

Treat as a 2-4 class classification with lookahead, not a static score. Enumerate candidate
houses = available legal houses `Player.getAvailableHouses()` (usually the deck's 3; under
changeHouse effects could include enemy/single-card houses).

For each candidate house, compute a "turn value profile" (a few quick searches of 2-7-deep
main-phase sequences, section 3.5):

- Amber projected this turn (reapers, bonus icons, "on use" triggers),
- Board swing (power dealt, creatures destroyed/lifted),
- Hand trajectory (draws, discards, archives, purge, look effects),
- Denial of opponent plans (unforge key, hand destruction, warning shots),
- Positional structure on own battleline (filling gaps, ducks behind taunt, wide vs tall).

Select the house maximizing `win-projection(delta after turn)` not `single-turn value` - the
latter leads to chronic "I must always reap" (house with reapers) looping, which loses to
opponent key-timing.

Special rules the AI must respect at this stage:

- If the player has 6+ amber and the next turn is the opponent's: consider houses with
  defensive or "withhold" value (taunt stacking, wards, kill-on-fight) instead of maximizing
  current turn amber - remember that "announcing Check!" is public information, so you can
  weight the safe-delay options against forcing the forge now.
- `cardNamesPlayedOrUsed` bookkeeping stuff (see `docs/01-core-rules/00-overview.md` section
  10) - "each of these once per turn" effects constrain repeats.

### 3.5 Main phase: the sequencing problem

Model: stateful machine at depth `k` (k is usually <= 8-14 actions to top out). Actions:

- Play: creature (with a choice of slot), artifact, upgrade (with host), action card.
- Use: reap (choose amber target), fight (choose target), or Action/Omni ability.
- Special actions inside the action window: manual effects the engine lets you (raise tide via
  prompt, activate prophecy), "discard from hand" when required.
- Do-nothing (pass the action window).

Search taxonomy (recommended):

- **Reap value** - short (1-2-deep): local eval, no search. Good default when the hand is thin
  or board state is near-forging.
- **Fast search** - engine-simulated MCTS on the reduced action space (play/use with each
  default target collapsed to "best per usual eval"), limited to the acting player's decision
  tree assuming a passive reaction opponent.
- **Deep search** - main phase only, with event-window rollouts virtualized. Needs a virtual
  engine (see section 4), because a fight that replays an `onFight` handler can cascade destroys
  and change the board completely.

Ordering heuristics (before any search fires, prune hard):

1. Resolve bonus amber gains (reap cards, steal/capture) FIRST when they feed a "this turn"
   threshold (forge lock), LAST when they would be converted into opponent losses.
2. Play creatures before using existing ones when the new creature makes reaps stronger
   (neighbor effects, e.g. centurion-style `modifyPower` per amber on card).
3. Fight before reap when the defender threatens to destroy/block your own reaps next turn
   (opponents creature trades with your reaper), otherwise reap first.
4. Use action/omni abilities that ALTER board composition beforeActions that count creatures
   (composition-only evals are order-dependent - run counting effects last).
5. Keep backup "second play" cards in hand if that doesn't risk opponent on-turn denial
   (they can't act during your turn besides reaction windows), except vs known reaction
   cards (they still cannot respond out-of-turn in KeyForge).

Amber threshold policy:

- If projected play can gather `>= cost` to a key: prefer lines that trigger forge this turn
  (Even if they "waste" tempo) - the opponent cannot interrupt between amber gain and forge
  except via rare on-turn triggers.
- If NOT: stopping just below the cost line is usually bad; overshoot so at least one steal
  blowout cannot return you below the line.

### 3.6 Within-card micro-decisions (targets, allocations)

These drive most of the "value leakage" in a naive AI.

- **Fights**: pick target by `expected damage dealt / expected damage taken` with overloaded
  multipliers for (a) killing an opponent amber-carrier (capture release - releases to YOUR
  pool, see `02-card-mechanics/aember-manipulation.md`), (b) killing an "on-destroy" chain
  hub (a card that has reactions that spawn/search), (c) taking their taunt.
  Consider elusive (usually target the elusive with a non-assault high-power unit),
  ward (prefer non-ward targets unless the damage is heavy enough to pop the ward and still
  kill).
- **Creature destruction/steal**: enumerate budget: how many steal triggers can I spend per
  tempo unit. Steal is best spent: (a) when the opponent can forge next turn, or (b) on
  their last key. Otherwise treat it as generic "damage".
- **Captures (on own creatures)**: prefer creatures with ward, armor, or future "captured amber
  counts as my forge amber" potential.
- **Upgrade host selection**: prefer host that will be attacked (absorbs armor), not the
  reaping engine; prefer contract the upgrade itself (e.g. a "dino" cycle from MM that rewards
  exalting).
- **Creature placement (battleline slot)**: leave gap between your taunt and your reapers;
  put splash-relevant creatures adjacent to the cheapest units; keep "while adjacent"
  engines together.
- **Hand discard (mind-bar / random discard etc.)**: no real choice, but "discard your OWN
  worst card" effects require a quality scoring of own hand (reuse hand-eval from mulligan).
- **Archive/purge targeting**: archive own cards that are re-usable later, purge opponents'
  cards in the least interactive piles first (opponent discard vs opponent deck). Observes
  "public knowledge" - both discards visible.

### 3.7 Reaction/interrupt windows

When a window opens (e.g. opponent fight, onDamageDealt handlers), AI must answer fast and
consistently; doing nothing is the default.

Principles:

- Only spend a reaction when it (a) prevents a KEY-loss (their steal => your under-6 reset)
  or boards a material tempo change, or (b) feeds your own key-time threshold.
- Ward-obligation: if a ward on your board feels "capable of absorbing a key damage" then
  prefer to spend it ONLY on fatal damage, not a 1-damage poke.
- "Sacrifice to pay" style "pay X or Y" handlers: reflect on which resource you value less -
  usually answers can be derived: sacrifice the least-synergistic card.
- Never flip on reaction windows during states where the opponent is about to forge a key
  this same window (their `onForgeKey` effects), unless you can deny the forge itself.
- Key reactive rule: resolving reactions on a "FORCED" prompt (nothing you can dodge) and
  "optional" ones (you may pass) must be separated in code; passing should be the default on
  optional ones unless positive-expected-value again.

### 3.8 Ready & Draw phases (end of turn)

- No active decisions except in some card cases (refill due keeping less hand? not supported -
  draw is full).
- AI must ensure it queued all "end of turn" or "at end of your turn" triggers BEFORE passing
  (their resolution is part of eval; check `DelayedEffect` round-boundary firing in
  `server/game/DelayedEffect.js` and the `endRound` flow in `01-core-rules/01-turn-structure.md`).

### 3.9 Final turn / time-out

- Once time is called, the plan becomes a pure optimization over the tiebreaker ladder
  (see `docs/01-core-rules/09-tiebreakers-and-edgecases.md`).
- Tiebreaker order is: keys forged -> most amber -> fewest chains -> most friendly creatures ->
  first player. So:
  - Prefer a plan that banks your 6-amber key BEFORE the final-turn sequence ends (forging on
    your own final turn is what the ladder's step 1 simulates).
  - If keys tie, maximize amber pool on your final turn, then minimize chains - late in the
    game usually do NOT raise the tide, since it costs chains that count directly in the
    tiebreaker, while "most friendly creatures" argues for keeping board units alive at the end.

### 3.10 Manual effects / prompts UI layer

Anything the engine shows as a click-yes/no against the action window (raise tide, prophecy
activation, discard-to-hand-storage in house phase) is a "spend or hold" decision. Generally:

- Tide raise timing: raising during your own tempo turn usually raises your own amber or
  damage value; raising it to deny or to fuel an opponent-tide-dependent card is the rarer,
  more deliberate call (see `docs/04-sets/05-dt.md` timing notes).
- Prophecy activation timing rules (see `docs/04-sets/12-pv.md`).

## 4. Virtual engine / search architecture

To search, the AI needs a sandbox that can execute keyteki's semantic event model:

- EventWindow + interrupt/reaction phases, popping sub-events (damage, destruction) with
  `moribund` batching (see `docs/01-core-rules/06-abilities-and-timing.md`).
- Snapshot/rollback: proved with `createSnapshot()` on cards in fights
  (`ResolveFightAction.js:50-52`). Construct a whole-engine snapshot for search branches;
  persist state after every player decision node.
- Playout determinism: simulate only the ACTIVE side's free choices using a policy with
  sampling for the opponent's hidden hand (opponent models below). The engine's event windows
  can be driven by a recorded decision policy so replays are reproducible.

Performance realities:

- Full MCTS with virtual engine at 30-60 actions per turn is feasible only with:
  (a) action-space pruning to the top ~6 candidate houses + top-3 sub-actions each,
  (b) tree reuse across decisions inside one turn,
  (c) heuristics-in-the-loop as the rollout policy.
- Anything below the main-phase search threshold: direct heuristics (section 3.6), because
  branching in the windows (N-target prompts, allocation prompts) explodes beyond search.

## 5. Opponent modeling (hidden state)

- **Hand inference**: deck list public + played/discarded/used cards known -> posterior over
  remaining ~30-card superset. Track a sparse "list of unknown cards in hand" plus counts by
  house. Useful outputs: probability the opponent's next turn can forge / can stop you /
  contains steal capacity.
- **Deck order inference**: only from cards they draw at turn start (visible to you only
  count-wise, never identity) - model as uniform random over unknown remaining; just track
  "expected deck size" and "expected deck top cards" post-shuffle only when shuffle events
  replay (they are public, e.g. archive pickup).
- **Type model**: a simple "flip-rate" of aggressive outcomes (steal frequency, fight
  frequency, defense) learned selfplay. Useful as a prior for reaction decisions (section 3.7)
  and for deciding whether holding your own combo cards is safe.

## 6. Failure modes to design against

| Failure | Cause | Mitigation |
| --- | --- | --- |
| "Reap every turn" loop | Myopic single-turn amber | House choice decides on win-projection, not turn value |
| Losing tempo to steal | Treated own pool as safe | Evaluation includes opponent's projected steal power |
| Ward abuse neglect | Spent ward on pokes | Ward restricted to fatal/near-fatal damage unless surplus |
| Taunt blindfight | Target selection ignores taunt | Follows `attackDueToTaunt` restriction at both selection AND resolution (`docs/01-core-rules/05-combat.md` section 1) |
| Death spiral into chains | Taking chains for tempo without refill plan | Chains term is compounded (6 chains -> lost draw for 6 turns) |
| Operational memory holes | Ignoring `cardsUsed`/`once this turn` limits | Must simulate with the same state trackers as the real engine (`docs/01-core-rules/00-overview.md` section 10) |
| Infinite window stall | Naively replaying loops on reaction chains | Hard cap branch depth; the real engine caps at 10 passes |

## 7. Benchmark ladder (how to measure the AI)

1. v0 rule-based - play only the heuristics, no search. Should beat random ~100% and lose to
   competent humans. Good for CI crash testing of the virtual engine.
2. v1 heuristic + light search (main phase MCTS with 1s budget) - baseline self-play eval.
3. v2 strong search - larger budget, opponent hand posterior included. Observed metric: games
   to stable ~50% self-play at decreasing temperature.
4. v3 learned - imitation on human replays then self-play fine tune with the same virtual
   engine; compare against heuristic-free v1. Do not trust learned eval without auditing on
   hand-built scenarios (e.g. forge-or-block choice with known deck lists).

Track three canonical diagnostic positions: (a) 5 amber vs 5 amber both with full boards,
(b) trailing one key behind with 12 chains, (c) time-out tie with 6+ amber on both pools and
tiebreakers in play. If "v1" can traverse these without hand-written rules, the AI is viable.

## 8. Humanizing the AI (playable, believable opponents)

An optimal KeyForge AI is mostly a design problem already solved; the interesting product
requirement is tuning it DOWN into something that plays like a person. This section turns the
engine above into dials.

### 8.1 What humanization means (and does not mean)

- It narrows strengths; it NEVER grants the AI hidden information. Reactive speed, exact
  card-counting, and depth of analysis are the levers - never peeking at the opponent hand or
  deck order.
- A humanized AI should lose for recognizable reasons: right plan, wrong depth; right plan,
  wrong timing; right plan, mis-executed target. Not for "random click" reasons - pure noise
  reads as broken, not as human.

### 8.2 The humanization parameter block

Implement one `AIProfile` object consumed by every decision site from sections 3.1-3.10:

| Parameter | Effect | Typical human range |
| --- | --- | --- |
| `searchDepth` | Main-phase MCTS lookahead (in actions) | novice 2, casual 4, strong 8-14 |
| `searchTimeMs` | Time budget per node | novice 100-300ms on big decisions only |
| `housePoolSize` | How many candidate houses even reach the search | 2 for novices (usually "the reaper house" + fallback), 3-4 for stronger profiles |
| `mistakeRate` | Probability per decision of picking the 2nd/3rd best line instead of the best | novice 12-20%, casual 4-8%, strong 0.5-2% |
| `targetNoise` | Occasionally pick a plausible-but-wrong fight/discard target (weighted by eval gap) | folded into `mistakeRate` |
| `evalNoise` | Multiply the eval score by `(1 + N(0, sigma))` per node during search | sigma novice 0.25, casual 0.10, strong 0.03 |
| `greed` | Coefficient boost on amber velocity (greedy players over-shoot the forge line, cautious players bank defense) | 0.8-1.3 |
| `riskAppetite` | How low the win-projection delta must be before accepting chains/attrition trades | novice low (avoids chain tax), expert high |
| `reactionDelay` | Artificial think time on forced windows (see 8.3) | 300ms-3s |
| `persona` | Static preference biases (see 8.5) | per-profile |

Key property: parameters must degrade GRACEFULLY. Hard-coding "wow, the novice forgot to reap"
needs to emerge from `mistakeRate` + `searchDepth 2` + `evalNoise`, not from special-case bugs.

### 8.3 Human thinking time

- Shape latency by decision magnitude, not uniform sampling:
  - Instant (<300ms): forced prompts, single-target allocations, obvious passes.
  - Quick (0.5-2s): fight targets, upgrade hosts, discards.
  - Deliberate (3-10s, occasionally 15-20s "edge case walkaway"): house choice, forge-turn
    threshold lines, tide raises, prophecy activations, and any reaction window where a
    scriptable counterplay exists.
- Add jitter and rare "long thinks" (a re-roll of the decision with +1 `searchDepth` that
  still completes at human speed, then may end with the same answer). Humans stare at boards
  before making the choice they were going to make anyway.
- Never reply instantly to reaction windows after the opponent's fight resolves - sub-second
  reaction timing is the single biggest "this is a bot" tell.

### 8.4 Depth clipping per stage (apply to sections 3.1-3.10)

The sections above assume full analysis; human profiles clip them:

- **Setup (3.1)**: novice bids chains as a fixed number stared from a table, not a computed
  expectation; expert computes but rounds.
- **Mulligan (3.2)**: novice uses ~50 redraw simulations and a threshold rule; expert uses
  full expectation but caps at a few thousand.
- **Key phase (3.3)**: no clipping needed - the engine forces the choice; humans "look
  regretful" only in theory.
- **House choice (3.4)**: this is where depth matters most. Novice profiles evaluate each house
  with the myopic single-turn sampler; expert profiles get 2-action deep rollouts per house.
- **Main phase (3.5)**: cap `searchDepth` and prune top-N (`housePoolSize`, plus action pool
  crop to ~5-8 candidates before search). The heuristic pre-pruning order (3.5's ordering
  heuristics) IS how humans manually sequence - keep those on for every profile.
- **Micro-decisions (3.6)**: use the plain heuristics with `evalNoise` + `mistakeRate`; never
  run search targets here for novice profiles.
- **Reactions (3.7)**: delay by `reactionDelay`, and with probability ~`mistakeRate` treat the
  optional reactivity as "misplayed" (e.g. ward spent on a non-fatal hit but only when the
  mistake evaluates close - never on absurdly wrong spends).
- **Final turns (3.9)**: novices lose track of the tiebreakers; add per-profile "tiebreaker
  awareness probability" - when unaware, fall back to amber-maximizing on the last turn, which
  is what many human players actually do.

### 8.5 Persona layer

Same engine, different tastes - represent as small permanent eval-weights/restrictions:

- `aggressive`: boost fight targets, prefers tempo over banking (spends amber pips earlier).
- `build`: boosts board-control term, reluctant to trade reapers for fights; hates losing
  creatures to hazardous.
- `amber`: tilts house choice toward whatever maximizes pool regardless of board.
- `chaos`: raises `mistakeRate` but keeps it plausible; suits "casual night" lobbies.

Persona must be a KNOWN bias, not an alternate personality hidden in the eval - expose it in
lobby text so the opponent knows who they are playing ("shadows greed", "control-ish").

### 8.6 Consistent-but-flawed memory

Real humans forget or misremember:

- Track per-profile "attention set" - variables the AI maintains (like forge threshold,
  remaining steal budget). Novice profiles update these lazily (preserving stale values for
  1-3 decisions before noticing). Expert maintains them exactly.
- Mislabeled threat ratings: occasionally downgrade a board threat that the evaluation would
  flag strongly - visible as "the bot ignored Server Rationing that turn".
- All of this only for the AI's OWN simulation state. Its reaction NECESSITY (cannot play vs
  will-be-dead) must NEVER use stale data - that breaks fairness in the other direction.

### 8.7 Difficulty bands from parameter combos

| Band | Params | Expected result |
| --- | --- | --- |
| Novice | depth 2, mistakeRate 15%, evalNoise 0.25, greedy 1.25, slow reactions, forgetful | Beaten by every board-regular; makes plausible amateur mistakes |
| Casual | depth 4, mistakeRate 6%, mild persona | Roughly local-store power level |
| Expert | depth 8 with clip, mistakeRate 1%, exact memory, deliberate pacing | Strong but beatable; this is the "good player" cap |
| Strong (reference) | full sections 3-7 | Never shipped to lobby; self-play calibration + benchmark harness only |

Tune jointly against the benchmark ladder (section 7) - a band should sit ~55/45 favorite
against the band below it, forming a smooth ladder rather than cliffs.

### 8.8 Red flags to audit in "humanized" play

- Repeats the same misplay pattern every turn (mistakes should catalogue from a distribution,
  not a fixed bug).
- Perfect conversion of tempo (turn ends with exactly 6 amber every time) - tag as non-human;
  humans overshoot and undershoot; emulate by sampling an amber "aim point" per person within
  +-2 of optimum and playing to that.
- Denies information a human could not have (never peek), despite all other realism work.
- Latency drops = tells the client it's thinking? Ensure server-side think time is added to
  act-by-act prompts not whole-turn batch (i.e. apply per-prompt delays, not one turn pause).

## 9. Mass match simulation for deck scoring

A harness that plays MANY matches between few decks and turns the results into deck scores.
This is a different consumer than sections 3-8: the AI is the measurement instrument, so its
flaws are the analysis's flaws.

### 9.1 Use the REAL engine, not the virtual one, for scoring

- For batch statistics, drive the actual keyteki engine in-process
  (`server/game/game.js`): deterministic, rule-faithful, and already implements every card
  in `07-card-database/`. Feed player decisions from your policy directly into the stream of
  prompts (`01-core-rules/02-phases-and-decisions.md` enumerates them all); no network layer.
- Keep the virtual sandbox (section 4) for SEARCH inside the AI policy. But never let scores
  come from two different engines - validate the virtual engine against the real one on a
  fixed sample of full matches (bitwise/board-hash comparison) before trusting search outputs.

### 9.2 Determinism and reproducibility

- Seed the engine explicitly: `seed = sha(deckA, deckB, index)`; log engine version + rule
  docs commit. Re-run any match bit-identically.
- KeyForge games can loop or stall - enforce the game-level time limit path in bulk sims
  (`server/game/TimeLimit.js`): cap rounds (e.g. 40), classify finished games by end cause:
  `keys`, `concession`, `timeout-tiebreaker`, `timeout-draw`, `stall/violated-10-pass` and
  exclude/engine-check the last two rather than counting them silently.
- Alternate seats with paired seeds (both seat assignments of the same seed family) or you
  will bake first-player advantage into your "deck score".

### 9.3 Sample sizing

- Win-rate per matchup needs roughly 1,000+ games for a +/-3% margin; per-DECK aggregate
  scores want 20-50k+ games per deck (spread over many opponents).
- Mirror matches: keep (they still measure deck consistency) but tag them.
- Shuffle variance is a KEYFORGE-specific noise term, bigger than in normal card games
  (single copies, fixed 36 cards) - seat- and mulligan-stratify your samples.

### 9.4 Logging schema (the "insights" layer)

Per game:

- deck ids, profiles, seeds, seat assignments, winner, end cause, rounds elapsed,
  keys forged per player by round, final amber / chains / board sizes, time-out tiebreak path.
- Per turn: pool amber, key cost (with active modifiers), hand size, board (power sums,
  creature count, taunt/ward counts), chains.
- Per decision (attribution): eval-score delta awarded by the policy (before/after),
  chosen action class (play/use type, target archetype), and which card performed it. This is
  what later answers "WHY does this deck lose" - without per-decision logs you cannot
  attribute a loss to, say, no early reapers vs key-cost-modifier density.

### 9.5 Statistical scoring of decks

- Primary: matchup matrix win rates (deck-level), plus league aggregate. A single scalar
  LOSES information: KeyForge matchups are non-transitive (rock-paper-scissors shapes are
  common), so report the matrix and use scalars only as summaries.
- Fit a Bradley-Terry / Elo-style model on the matrix to get win-probability-adjusted deck
  strength (ratings adjusted for the strength of the opponents each deck met).
- Second-order metrics with deck-level meaning: turns-to-3-keys distribution, timeout rate,
  chain accumulation, steal/steal-taken totals.
- Card attribution: from per-decision logs, per-card (a) play/use frequency, (b) win-rate
  delta in matches where it was played vs not played (careful: drawing it is not independent -
  use it as descriptive "insight", not causal), (c) eval-delta contribution per use.
- Deck-feature regression (why): regress win prob on deck archetypes features - counts of
  key-cost modifiers, steals (own + opponent-facing), recursion (discard-to-hand), taunt
  density, token creature support, prophecy cards (sets 12/14), amber pips sum, etc. This
  gives "deck archetype" explanations alongside the scalar rating.
- Confidence: bootstrap CIs; report matchup-level variance. A deck's score is conditional on
  the OPPONENT POOL chosen (see "pool of decks" below) - publish scoring with the pool
  composition, or two pools (broad random pool vs tournament-meta pool).

### 9.6 Measurement pitfalls

| Pitfall | Cause | Handling |
| --- | --- | --- |
| AI favors its own archetypes | policy trained/heuristic-built on same decks | score profiles with ZERO knowledge of what is being scored (blind policy), or use two orthogonal profiles |
| Opponent-strength bias | deck encountered weak opponents inflates rating | Bradley-Terry/Elo fit, or round-robin so every deck meets the same pool |
| "Stall games counted as losses" | engine time-out path mishandled | classify end cause per game and audit deviant classes separately (9.2) |
| Turn-1 house choice dominates | strong profiles keep playing first mover in seat-alternated seeds | see seat alternation 9.2; report seat-split win rates too |
| Deck legality drift | deck legality requirements (chains, restricted lists) mismatched between decks | pre-filter decks for the chosen format before simulating (see `docs/05-decks-and-servers/master-vault-import.md`) |
| Seed leakage exhaustion | 2k seeds, same 2k games | per-deck disjoint seed ranges; monitor hist. of identical boards |

### 9.7 Builder-agent checklist

1. Import decks: `docs/05-decks-and-servers/` (Master Vault fetch, deck data organization).
2. Validate each deck before the run (legality, awards, chains).
3. Build the harness around the real engine (9.1), policy = `AIProfile` bands (8.7) with a
   BLIND policy for scoring (9.6), seat-alternating seeds (9.2).
4. Run the sample sizes of 9.3; log per-decision data (9.4).
5. Analyze: matrix + Bradley-Terry, card attribution, feature regression (9.5).
6. Sanity gates: audit timeout/stall classes, seat symmetric-ness, mirror consistency; only
   then publish deck scores WITH pool description and profile version.

---

Companion docs: `docs/01-core-rules/02-phases-and-decisions.md` (machine-level enumeration of
every prompt the AI's policy head must answer), `docs/decision-tree/player-decisions-cheatsheet.md`.
