# PLAN — Card & engine fidelity (all Core cards, reusable for later sets)

## Context

The first 42-deck CotA ladder (25,830 matches, 0 failures, 0 draws) gave precise ratings
(95% bootstrap intervals ≈ ±0.7 Aember Score on the legacy scale, ≈ ±2 on the current one) but only moderate agreement with the owner's
hand-assigned tiers: **Spearman 0.44, pairwise agreement 67.5%**. Top (S+/A) and bottom (C/Z)
tiers separate; the middle does not. Outliers: Seraphina (S+) ranked 36/42, Torsen (S) 41/42,
Chang (A) 3/42, Patricia (Z) 17/42. Since sampling noise is small, the remaining error is
**model error**: the simulator does not play the cards the way the real game does.

Measured state of card coverage (`CoreSetDslCards.kt`, 367 generated cards):

| Level        | Cards                                                         |
|--------------|---------------------------------------------------------------|
| FULL         | 110                                                           |
| PARTIAL      | 244 (of which **179 are `play(NoEffect)` — they do nothing**) |
| APPROXIMATED | 13                                                            |

Blocking text patterns among the 257 non-FULL cards (a card can have several): "Play:" text 109,
"if" 51, "may" 34, fight/reap triggers 34, "for each" 31, discard 23, after/before 22, static
auras 22, capture 14, "until/next turn" 13, power counters 13, house naming 7, "destroyed:" 7,
"instead" 7, armor/ward 6. By type: creatures 99, actions 87, artifacts 49, upgrades 20.

Two further problems besides raw coverage:
1. **The fidelity label is declarative.** The generator writes `FULL`/`PARTIAL`; nothing proves a
   `FULL` card behaves like the real card. The 70% match-level threshold hides how wrong
   individual cards are.
2. **There is a free oracle.** keyteki implements every Core card
   (`~/work/keyteki/server/game/cards/01-Core/*.js`, 363 files) with a behavioural spec for each
   (`~/work/keyteki/test/server/cards/01-Core/*.spec.js`, 363 files). 44 cards use
   `persistentEffect`, 32 use interrupts/reactions.

**Goal:** every Core card implemented and *verified* against keyteki behaviour, measured by an
honest, test-backed fidelity number, with the ratings re-run after each batch to confirm the
model improves. The same pipeline must work for later sets (AoA, WC, …).

**Non-goal:** fitting ratings to the owner's tiers. Tiers are a subjective sanity signal, not a
training target; overfitting to them would defeat the purpose.

## Phase 0 — Measure honestly (small, do first)

1. **Coverage inventory tool** (`tools/fidelity-inventory.py`, reads `CotA.json` +
   `CoreSetDslCards.kt` + `CoreCards.kt`): one row per card → level, type, house, blocking pattern
   tags (the classification used above), keyteki features used (persistentEffect, reaction,
   interrupt, target prompt), and **pool impact** = number of the 42 pool decks containing it.
   Output `docs/fidelity/core-inventory.csv` (checked in) and a summary table.
2. **Priority score** = pool impact × (1 + blocking-pattern cluster size / 100). Use it to order
   work inside each phase. First targets: the cards in Seraphina, Torsen, Chang, Patricia and
   Lazzaro (the biggest outliers) — they are the cheapest way to see whether the hypothesis
   (“fidelity explains the rating error”) holds.
3. **Make the label honest.** Add `verified: Boolean` to the fidelity entry; a card is only
   `FULL` in reports if it has a passing parity test (Phase 1). Untested cards report as
   `UNVERIFIED`. Extend `FidelityReportService` to return both “declared” and “verified” scores.
4. **Per-card fidelity in match output**, aggregated across a batch (which cards were played at
   what level, weighted by plays) so a ladder reports its lowest-fidelity cards, not just a
   mean. Surface it in `GET /api/mass-simulations/{uuid}` (new `fidelity.cards` section).

Files: `tools/fidelity-inventory.py` (new), `domain/engine/fidelity/*`,
`application/MassSimulationService.kt`, `adapter/rest/MassSimulationDtos.kt`.

## Phase 1 — Parity-test harness against keyteki

1. **Scenario test DSL in Kotlin** mirroring keyteki’s `setupTest`:
   `scenario { player1 { house; inPlay; hand; discard; deck; amber; keys }; player2 {…} }`
   → builds a `GameState`; helpers `play(card)`, `reap/fight/use(card, target)`, `endTurn()`,
   and assertions on amber, damage, location, exhausted, keys. Lives in
   `src/test/kotlin/.../engine/support/Scenario.kt`.
2. **Scriptable choices.** Specs rely on `clickCard`/`clickPrompt`. The engine decides targets via
   the AI/`TargetChooser`; add an injectable `ChoiceProvider` (test = pre-scripted answers,
   production = current AI behaviour) so tests are deterministic. Reuse
   `domain/engine/dsl/TargetChooser.kt`.
3. **Spec porter** (`tools/port-keyteki-specs.mjs`): parses a `.spec.js` (setup block, actions,
   `expect` lines) and emits a Kotlin test skeleton per card
   (`src/test/kotlin/.../engine/cards/core/<House>CardsTest.kt`, grouped by house). Mechanical
   parts (setup, simple expects) are generated; prompts/targets are filled by hand. Keeps the
   spec text as a comment so reviewers can compare.
4. Pilot on 10 cards already FULL (e.g. Anger, Lava Ball, Punch) to prove the harness before
   scaling; any failures there are real bugs in cards we currently count as done.

## Phase 2 — Engine capabilities (in dependency order)

Each capability ships with its own unit tests and then unlocks a card cluster. Order chosen by
cluster size × pool impact.

1. **Persistent / static effects** (≈44 keyteki cards, e.g. Bulwark, Staunch Knight, Tentacus,
   Banner of Battle). Introduce a derived-stats pass: `InPlayCard` keeps base values; an
   `EffectRegistry` collects active persistent effects (`modifyPower`, `modifyArmor`,
   `addKeyword`, `cannotX`, key-cost modifiers) with a `match` predicate (neighbors, house,
   friendly/enemy, flank, “while …”) and recomputes after every state change. Replaces the ad-hoc
   `CardScript` hooks in `AGENTS.md` (`cardEntersReady()`, `modifyFightDamage()`, …), which become
   registry entries. Files: `domain/engine/model/InPlayCard.kt`, new
   `domain/engine/effects/EffectRegistry.kt`, `dsl/CardDsl.kt`, `dsl/DslCardScript.kt`.
2. **Event bus + reactions/interrupts** (≈32 cards; fight/reap/after/before/“destroyed:”).
   Typed events (`CardPlayed`, `CreatureReaped`, `FightResolved`, `Destroyed`, `Healed`,
   `KeyForged`, `Readied`, …) with `interrupt` (replaces) and `reaction` (after) phases, as in
   keyteki. Existing triggers (`Play`, `Reap`, `Fight`, `Destroyed`, …) become subscriptions.
   Files: `GameEngine.kt`, `action/ActionExecutor.kt`, `dsl/EffectResolver.kt`.
3. **Expression layer in the DSL**: counts (`for each …`), comparisons, `Optional/may` (AI decides
   via `ChoiceProvider`), duration (`until end of turn`, `next turn`, `while`), `instead`
   replacement. Files: `dsl/Condition.kt`, `dsl/Effect.kt`, `dsl/Target.kt`.
4. **Targeting completeness**: choose by house/trait/flank/neighbor/power, “up to N”, name a
   house. Replaces broad selectors listed under *Known Gaps* #3.
5. **Remaining mechanics**: upgrades with continuous effects (20 cards), artifact Omni/Action
   timing incl. cross-house “as if active house”, capture/exalt/power counters (13/14 cards),
   ward/armor edge cases, in-line `deploy`, play restrictions.

## Phase 3 — Card batches (≈40 cards each)

Work by mechanic cluster, highest priority score first. Per batch:
1. Port the keyteki specs for the batch (Phase 1 porter) → red tests.
2. Implement: extend `tools/generate-core-cards.py` when the pattern is mechanical (regenerate
   `CoreSetDslCards.kt`); otherwise handwritten `CardScript` in `cards/CoreCards.kt` registered in
   `infrastructure/config/CardLibraryConfig.kt`. Prefer generator rules — they scale to later sets.
3. Green tests ⇒ flip `verified = true`; coverage report updated.
4. Re-run the reference ladder (Phase 5) and record ratings movement.

Suggested batch order: (a) the 179 `NoEffect` cards that are simple (damage/amber/draw/ready
variants the DSL already supports); (b) persistent-effect cards; (c) reaction/trigger cards;
(d) “for each”/“if”/“may” conditionals; (e) upgrades and artifacts; (f) remaining oddities
(manual scripts). Target: **100% of Core cards implemented, ≥ 95% verified** (the rest listed
with a reason, e.g. rules that need interactive opponent choices the AI cannot model yet).

## Phase 4 — Regression guardrails

- **Coverage ratchet test:** fails if the verified count drops or a card with ability text is
  registered as `NoEffect` without an entry in an explicit `knownGaps` allowlist.
- **Spec drift check:** the inventory tool fails CI if keyteki gains/changes a Core card file we
  have not re-reviewed (hash of the keyteki card file stored in the inventory CSV).
- Keep the existing suites green (`GameEngineTest`, `KeywordTest`, `RoundRobinScheduleGeneratorTest`,
  rating tests); per-card tests run in the normal `./gradlew test`.

## Phase 5 — Measure the effect on ratings

After each batch (or at least each phase) re-run the **same 42-deck ladder** with fixed
`baseSeed` and 30 matches/pairing (≈26k matches) and compare to the stored baseline
(`083dc1b0-1e96-49ce-8433-1c630b3e2ea8`):

- Spearman / pairwise agreement via `POST /api/mass-simulations/{uuid}/tier-validation`
  (baseline 0.44 / 67.5%).
- Rank movement of the outlier decks (Seraphina, Torsen, Chang, Patricia, Lazzaro).
- Verified-fidelity of cards actually played (Phase 0.4), not just the declared match score.
- Draw/timeout rate (currently 0).

Add a small CLI/script `tools/ladder-compare.py` to diff two batches’ ratings. Expectation:
agreement should rise as fidelity rises. If it does not, the residual error is the AI or subjective
tiers, which is itself useful information.

## Phase 6 — AI quality (after card fidelity, separate effort)

`TacticalAI` is part of the model. Once cards are verified, rerun the ladder with the other
profiles (`aggressive`, `economic`, `greedy`) and check whether some deck styles (archive/combo,
control) are systematically misrated. Possible follow-ups: lookahead, deck-archetype profiles.
Out of scope until Phases 0–5 land, except 6a below (cheap, and it affects how honest the board-wipe
cards in Phase 3 are).

### Phase 6a — Situational card valuation (hold / play / discard timing)

**Problem.** `TacticalAI` scores a play from its card type only (action ≈ 1.0 + 0.3/amber), discard has a
flat weight, and `scoreHouse` only counts playable cards. So a board wipe is played the first turn its
house is chosen (even on an empty enemy board or a board where we are ahead), and it can be discarded as
fodder even when its house is about to be playable. Same for soft wipes (Lost in the Woods, Gateway to Dis,
Hypnotic-style resets) and any "situational" card. Because the rating is only as good as the play
quality, this bias also distorts how wipe-heavy decks rate.

**Design (heuristic, no lookahead).**
1. **Tag situational cards from data, not names.** A small resource `ai/situational-cards.txt`
   (`card-id  WIPE_FULL|WIPE_SOFT|…`), seeded from the inventory (text "destroy each/all creatures",
   "shuffle/return … creatures", "each creature takes damage"). Cards not tagged keep today's scoring.
2. **Board-delta value for wipes.** Value = enemy board worth destroyed − own board worth lost (worth =
   power + keywords + captured amber, reusing `evaluateCreatureThreat`). Play now when the delta
   exceeds a threshold; below it the play gets a *hold penalty* (≈ −their base score), so other plays and
   the discard slot win. Soft wipes use the same delta with their actual effect (e.g. shuffle-2-and-2).
3. **Override when forced.** Ignore the hold penalty when (a) the enemy board is oppressive (delta very
   large, or enemy amber/keys put us within 1–2 turns of losing) or (b) the wipe is the only
   remaining play for the house. Never hold a wipe forever: after N turns held, relax the threshold.
4. **Anticipation (the "I'll play that house soon" case).** Estimate `P(house playable within k turns)`
   from: other cards of that house in hand, unseen cards of that house left in the deck (deck not yet
   reshuffled ⇒ fewer left), and turns since that house was last chosen. Use it for:
   - **Discard choice:** keep value = situational value × P(play soon); discard the wipe only when P is
     low *or* our board is already strong *or* the house cannot realistically come back (the user's
     "played that house often, deck didn't reshuffle" case). Never discard a wipe while another
     low-value card is available.
   - **Archive instead of discard.** For a held situational card whose house is *probably* coming back
     (or whose condition is likely to be met later), archiving beats discarding: it is safe from
     discard/hand-wipe effects and is picked up at the next archive-take. Cost: it uses the archive
     slot and the card is only recoverable by taking the whole archive, so prefer it when the archive is
     empty or holds low-value cards, and when no key-cheat/hand-size pressure says otherwise.
   - **House choice:** bonus for the wipe's house when delta is high; small penalty for deploying creatures
     of other houses into a board we are about to wipe when the wipe is likely next turn.
5. **Sequencing inside the turn.** When the wipe and creatures share the house, play the wipe *first*, then
   deploy creatures onto the cleared board (and the reverse for creatures that should not be wiped).
6. **Other situational cards that fit the same framework** (second pass, only if the first moves ratings):
   steal/capture cards when the opponent has few amber (already partly penalised), hand-disruption
   when the opponent's hand is small, archive-instead-of-discard for a held wipe (Archives exist in the
   engine), forge-race awareness (a wipe does not stop a forge next turn), skip wipes on an empty enemy
   board.

**Implementation notes.** New `SituationalValuation` helper used by `scorePlayCard`, the discard scoring in
`scoreAction`, and `scoreHouse`; gated behind an `AISettings.situationalValuation` flag so the default
profile stays reproducible (A/B in the same ladder harness). The AI logs the rationale string
(`hold wipe: delta=…`) like the existing tactical rationale.

**Verification.** Unit tests on constructed boards (hold with strong own board, play when behind, forced
play when opponent is about to win, keep-vs-discard with/without the house in hand, wipe-before-creatures
sequencing). Then A/B: same pool/seed with the flag off vs on; compare ratings of wipe-heavy decks
(movers) and tier validation. Expect small, localized movement; a large global shift means the
thresholds are wrong, not that the AI is better. Do **not** tune against the hand tiers.

### Phase 6b — Key cheats and other condition-gated cards

Same framework as 6a, different gate. Key cheats (Key Charge, Chota Hazri, Key of Darkness, Epic Quest —
implemented via `ForgeKey`) are only strong when their condition holds, so the AI should *hold* them
until it can fulfil it, not fire them at the first opportunity (or discard them as fodder).

1. **Gate = "can I cash this in within k turns?"** estimated per card from the engine state:
   - amber-cost keys: current amber vs. the effective forge cost (`ForgeRules.keyCost`, including
     modifiers) plus the card's own cost/penalty — play when the resulting forge actually happens, and
     avoid the line where the cost is paid but the forge fails;
   - progress conditions (Epic Quest: Sanctum plays so far this game/turn, `cardsPlayedThisTurnByHouse`):
     hold while achievable, with a rising keep value as progress accumulates;
   - board conditions (a specific creature/artifact in play, a flank, an empty enemy board): hold while
     the condition is plausible from hand + board, play when satisfied.
2. **Tempo comparison.** Compare "forge now with the cheat" against "forge normally next turn": the cheat
   is worth most when it forges a key the opponent could not prevent or when it is a *turn faster* than
   the normal forge; worth little when we would forge anyway.
3. **Same hold/override/decay rules as 6a** — override when it wins the game (3rd key) or when the
   opponent is about to; decay the hold so a never-satisfiable cheat eventually gets archived/discarded.
4. **Keep vs. archive vs. discard** reuse the 6a decision: keep when the condition is likely soon,
   archive when it is plausible later, discard only when it is unreachable (e.g. opponent steals our
   amber lead, key-cost raisers in play, condition cards already destroyed).
5. **Other condition-gated cards that can reuse the gate** (second pass): cards with "if you have N
   keys/amber/creatures" bonuses, `playableIf` cards (the engine already refuses illegal plays), and
   cards whose payoff depends on opponent state (steal when they have ≥ N amber).

**Verification (6b).** Scenario tests: Key Charge held when amber is short and played when the forge will
go through; Epic Quest kept at 5–6 Sanctum plays and discarded when impossible; override on the winning
key; archive choice when the archive is free. A/B ladder like 6a; expect movement mostly in key-cheat
decks, so report per-deck movers and keep the rest of the pool as a control group.

**Caveats.** Heuristics, not lookahead; thresholds are guesses to be checked against logs. The Phase 3
wipe cards should be audited against keyteki before the AI is tuned around them, otherwise we tune
around a mis-implemented card.

## Phase 7 — Prepare for more sets

Keep generator and harness set-agnostic (`tools/generate-core-cards.py` → parametrise pack path
and output file; parity porter reads `<set>/*.spec.js`). Seed catalogs for AoA/WC when Core is
done; one pool and one ladder per set. A cross-set anchor ladder remains a later problem
(*Known Gaps* #7).

## Verification (end to end)

1. `./gradlew test` — all existing tests plus the new per-card parity tests pass.
2. `python3 tools/fidelity-inventory.py` — inventory shows verified count increasing, no card with
   ability text silently `NoEffect`.
3. Start the app, run the reference ladder (`POST /api/mass-simulations/multi`, 42 decks,
   30/pairing, fixed seed), poll to `COMPLETED`, then call `/ratings` and
   `/tier-validation`; compare with the baseline batch above.
4. Spot-check logs (`X-Debug: true`) for a few outlier decks following the *How to Analyze a
   Gameplay Log* checklist in `AGENTS.md`.

## Process notes

- Branch per phase, e.g. `feature/fidelity-measurement`, `feature/effect-registry`,
  `feature/fidelity-batch-N`. Do not commit unless explicitly asked.
- Update `AGENTS.md` (Current State, Known Gaps, tests list) as each phase lands.
- Phase 0 and the Phase 1 pilot are the first deliverables (about a day of work); they also
  validate whether fidelity is really the main driver before investing in the large engine work
  of Phase 2.
- On approval, this plan is also copied to `/home/mateus/work/aember-analyzer/PLAN.md` (the
  location the owner asked for; plan mode only allows writing this file until then).
