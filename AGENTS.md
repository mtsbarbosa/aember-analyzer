# Agent Handoff — aember-analyzer

Last updated: 2026-11-22

## Fidelity Plan Progress (PLAN.md)

- **Phase 0 — done.** `tools/fidelity-inventory.py` (inventory + priority scoring, `docs/fidelity/core-inventory.csv`), verified-fidelity labels (`VerifiedCardRegistry` + `fidelity/verified-cards.txt`, declared vs verified scores in `FidelityReportService`), and batch-level per-card fidelity (`fidelity-cards.json`, `fidelity.cards` section in `GET /api/mass-simulations/{uuid}`).
- **Phase 1 — done.** Scenario test DSL (`src/test/kotlin/.../engine/support/Scenario.kt`), injectable `ScriptedTargetChooser` (ChoiceProvider port), spec porter (`tools/port-keyteki-specs.mjs`), and a 16-test pilot (`CoreParityPilotTest`) covering all portable specs for its 10 cards. The pilot immediately found real bugs in cards we count as done: **Lava Ball's 2D splash damage was missing** (fixed — `DealDamage` now has `splash`; Booby Trap downgraded honestly to PARTIAL), **poison was unimplemented** (fixed — kills damage-surviving victims; Macis Asp kills Dodger), and **a creature destroyed during a fight resolved its own Fight triggers** (fixed — moribund attackers skip `onFight`/`onAfterFight`, matching keyteki's `updateAbilityEvents`).
- **Planner review fixes applied:** registry entries require every portable spec ported (blocked specs documented in `fidelity/verified-cards.txt`, e.g. dodger's Neffru spec needs the AoA card + event bus); porter skeletons compile via `Scenario.player1`/`player2` views and end unfinished tests with `error("TODO")` so they fail loudly; `CardFidelity.verified` removed (registry is the single source of truth); batch aggregation reads level counts directly (no per-play objects); `UNVERIFIED` scores 60 (distinct from PARTIAL 70); `Scenario.play` is strict (fails when the card is not in hand); splash lands on neighbors before the primary target so `lastDamagedTargetId` stays correct.
- **Phase 2.1 — done (persistent / static effects).** `EffectRegistry` (`domain/engine/effects/`) recomputes derived stats from scratch after every action (inside the engine's state-check fixpoint, so aura loss that drops power below damage destroys the creature). `InPlayCard` now keeps `basePower`/`baseArmor`/`baseKeywords` (printed values) next to the effective `power`/`armor`/`keywords`, plus `hostId` for upgrades; power counters raise `basePower` permanently. DSL: `persistent { modifyPower/modifyArmor/addKeyword/modifyKeyCost(...) }` with `CreatureScope` selectors (self, neighbors-of-self, friendly/enemy, flank, house/trait, host-for-upgrades) and dynamic amounts (Yxili Marauder, Mushroom Man). Key-cost modifiers are registry-driven (`ForgeRules` consults `EffectRegistry`; "your opponent" on an upgrade resolves through the host's controller). 16 Core cards implemented (Banner of Battle, King of the Crag, Round Table, Niffle Queen, Grey Monk, Bulwark, Halacor, Staunch Knight, Titan Mechanic, Shoulder Armor, Protect the Weak, Ring of Invisibility, Blood of Titans, Flame-Wreathed, Mushroom Man, Yxili Marauder's aura) + Jammer Pack migrated to the registry (old `opponentKeyCostModifier` hook removed). 20 unit tests in `PersistentEffectsTest`. Use-restriction auras (Tireless Crocag "cannot reap", Grommid "cannot play creatures", Pitlord "must choose Dis", Ember Imp 2-card limit, The Vaultkeeper no-steal) still need Phase 2.2/2.3 work.
- **Phase 2.2 — done first increment (event bus + reactions).** Typed events (`domain/engine/events/`): `CardPlayed`, `CreatureEnteredPlay`, `CreatureReaped`, `FightResolved` (with moribund destroyed flags), `CreatureDestroyed`, `CardLeftPlay`, `KeyForged`; emitted by `ActionExecutor` (play/reap/fight/destroy), `EffectResolver` (return-to-hand, sacrifice), and `TurnPhases` (key forge). Stateless `EventBus` walks cards in play (+ upgrades) and invokes a new `CardScript.onEvent(...)` hook; DSL `reaction(notes, effect, subscription, matches) { game, player, card, event -> ... }`. Destroyed cards cannot react to their own destruction, but their own "Leaves Play:" reactions still fire (Magda the Rat). Turn-scoped subscriptions ("for the remainder of the turn", Looter Goblin / Loot the Bodies) register game-level `TurnScopedReaction`s via `ActivateTurnReaction` so they keep firing after action cards leave play; cleared at end of turn. New effects: `LoseAmberOpponent`, `EventSideGainsAmber`/`EventSideLosesAmber` (owner/controller sides, `EventSubject` for fight participants), `StealFrom`, `ActivateTurnReaction`, `ReadyHost` (host readies + temporary house via `InPlayCard.baseHouse`, restored at turn start). 10 Core cards implemented: Soul Snatcher, Tolas, Pile of Skulls, Krump, Grommid (reaction; "cannot play creatures" restriction still pending), Carlo Phantom, Looter Goblin, Magda the Rat, Brain Stem Antenna, Loot the Bodies — plus Annihilation Ritual (manual script, `purgesDestroyedCreatures()` replacement effect; the old dishonest `reap(GainAmber(1))` Looter and `play(GainAmber(1))` Loot the Bodies were corrected). 13 unit tests in `ReactionsTest`. More reaction cards (Anger-triggered "after you reap" effects, AoA event cards) can now ride the bus.
- **Phase 2.3 — done (use restrictions).** `UseRestriction`/`SelfRestriction` sealed hierarchies (`domain/engine/effects/UseRestriction.kt`) + DSL `restrictions { ... }` builder. Player-level auras are collected by `EffectRegistry.collectUseRestrictions` and enforced in `ActionGenerator` (`CannotPlay` per card type, `PlayLimit` on the opponent's own per-turn play count, `ForcedHouse` filters house choices, `MayPlayHouse` allows a per-turn off-house play counted via `cardsPlayedThisTurnByHouse`) and in `EffectResolver` (`NoSteal` checked against the victim in both `stealAmber` and `stealFrom`, logs "cannot be stolen" and sets `lastStealAmount = 0`). Card-self restrictions surface through `CardScript.selfRestrictions()` (`CannotReap` removes `ReapUse`, `UsableAsActiveHouse` bypasses the house match, `SelfDestruct` runs in the engine's `stateCheck` fixpoint). 6 Core cards implemented: Grommid ("cannot play creatures" + reaction extended to both fight directions and only when Grommid survives, per keyteki), Tireless Crocag (cannot reap + usable as if active house + destroyed while the opponent has no creatures), Pitlord (must choose Dis), Ember Imp (opponent limited to 2 plays/turn), The Vaultkeeper (controller's amber cannot be stolen), Witch of the Wilds (one off-house Untamed play per turn). 23 tests in `UseRestrictionsTest` (12 unit + 11 ported keyteki specs); all 6 ids in `verified-cards.txt` with BLOCKED notes (ward for Grommid/Crocag, Phase Shift for Witch, upgrade/take-control for Crocag). Known keyteki quirk: EmberImp.spec's global play counter is implemented as the opponent's own count (sane reading of the text).
- **Phase 3 batch (a) — first wave done (17 FULL + 1 PARTIAL).** Keyword-only creatures registered FULL (taunt/skirmish/assault/hazardous/poison handled by the keyword engine: Champion Anaphiel, Snufflegator, Macis Asp, Ancient Bear, Briar Grubbling); single-trigger plays (Lupo the Scarred, Mighty Tiger); key-cost auras (Murmook +1, Iron Obelisk +1 per friendly damaged Brobnar via new dynamic key-cost `modifyKeyCostDynamic`); next-turn denials (Foggify reuses `cannotFightNextTurn`, Skippy Timehog new `cannotUseCardsNextTurn` flag stripping ALL creature/artifact uses incl. Omni but not plays/discards, Scrambler Storm new `deniedCardTypeNextTurn` blocking ACTION plays); Doorstep to Heaven (`ReduceAmberTo(6,5)` both players); Timetraveller (`ShuffleSelfIntoDeck` action); Lady Maxena (new `SelfCreature` selector resolving to the source card or an upgrade's host, used for the return-self Action); upgrade keyword grants (Yo Mama Mastery taunt + full-heal play, Way of the Bear assault 2, Way of the Wolf skirmish). Chuff Ape stays **PARTIAL** (taunt + DSL `entersPlayStunned()` done; the Fight/Reap sacrifice-another-and-heal is not implemented). 17 tests in `Phase3BatchATest`. Declared FULL 158 → 175; declared fidelity 86.8% → 88.2%. Ladder: rank corr 0.994 / agreement 97.6% vs the Phase 2.3 ladder; tier validation 0.46 → 0.48 (first gain since Phase 2.2). None of these are in `verified-cards.txt` yet (no ported keyteki specs; they stay FULL-but-UNVERIFIED). **These 18 blocks now live in `dslCardOverrides` (CoreCards.kt).**
- **Phase 3 batch (b) — done (23 FULL).** Repeat-while stealing (Bait and Switch `RepeatWhile`); amber halving (Effervescent Principle `LoseHalfAmber`, both players round down the loss); shuffle-creatures-into-decks (Lost in the Woods `ShuffleCreaturesIntoDeck`, up to 2 friendly + 2 enemy); draw-phase modifiers (Mother +1 own refill, Succubus −1 opponent refill via new `drawRefillBonus`/`drawRefillOpponentPenalty` hooks consumed in `TurnPhases.drawPhase`); play-grants (Soft Landing `NextPlaysEnterReady` flag on `PlayerState`, consumed by creature/artifact plays); turn-scoped damage immunity (Shield of Justice all friendlies, Protectrix healed creature via `GrantDamageImmunity` + `InPlayCard.immuneToDamageThisTurn`, checked in both damage paths, cleared at turn start); fight-time keyword ignores (Niffle Ape `ignoresTauntAndElusiveWhenAttacking` bypasses taunt targeting in `ActionGenerator` and elusive in `ActionExecutor.fight`); reactions (Tunk heals fully after you play another Mars creature via `CreatureEnteredPlay`, Duskrunner upgrade grants its host "Reap: steal 1A" via `CreatureReaped` + hostId); targeting selectors (`NeighborOfSelf` for Sergeant Zakiel, `AnyCreatureExceptLastDamaged` for Twin Bolt Emission / Pawn Sacrifice, `CreatureNotOnFlank` for Booby Trap, `LastHealedTarget`, `Exhaust(count=3)`); heal tracking (`lastHealAmount`/`lastHealedTargetId` + `HealedAtLeast` condition for Vigor; `Heal(fully=true)`); sacrifice targeting (`Sacrifice(selector)` + `SacrificedTarget` condition, Pawn Sacrifice); destroyed-to-deck (Dextre `PutSelfOnTopOfDeck`, library added to the destroy `alreadyMoved` check); pure DSL fixes to FULL (Seeker Needle, Ritual of Balance, Terms of Redress, Fear, Nepenthe Seed — reorder return-before-sacrifice so the greedy pick does not always grab the seed). All target-selection handlers in `EffectResolver` now pass the real source card instead of a dummy (required by `SelfCreature`/`NeighborOfSelf`). 24 tests in `Phase3BatchBTest`. Declared FULL 175 → 198; declared fidelity 88.2% → **92.3%**; no-effect crowd 130 → 118. Ladder: rank corr 0.896 / agreement 87.6% vs the endpoint ladder, avg rounds 11.8 → 13.5, tier validation 0.48 → **0.367 / 64.6%** — the new mechanics genuinely reshuffle deck strength (Bait and Switch steal loops, Lost in the Woods reshuffles, Mother draw engines; Lazzaro +10.6, D. Ewing −7.9), same pattern as the Phase 2.2 dip. Not in `verified-cards.txt` (no ported keyteki specs). **These 23 blocks now live in `dslCardOverrides` too.**
- **Phase 3 batch (b) — audit / cost-direction fix / ablation notes.** `Sacrifice(FriendlyCreature)`, `ShuffleCreaturesIntoDeck` (friendly side) and `Exhaust(count)` pick the **cheapest** friendly creature for costs (`cheapestFirst`: lowest power, then exhausted, then most damaged); ladder-neutral (rank corr 0.999 vs `78b316c7`, agreement 99.3%, tier 0.367 → 0.373). Keyteki-source audit of the four batch-(b) engine cards verified all correct — Bait and Switch steals exactly until the opponent is no longer ahead (5/5 real chains correct), but keyteki gates the play action on the condition, so `playableIf(OpponentHasMoreAmber)` was added (the AI previously wasted the play when ahead in 7/12 audited instances); Lost in the Woods zero-shuffle resolutions were all empty-board plays (legitimate "up to" semantics); Mother/Succubus hand-size signatures match the unit-tested `drawPhase` hooks. Ablation ladder runs (cluster stubbed to NoEffect, tier Spearman vs the SAS labels; reference `fec052e9` with the playableIf fix = 0.374/64.7%): stub bait-and-switch → **1.000 rank corr, tiers identical** (steals are real but almost never flip a match outcome — Lazzaro's rise came from other cards); stub mother+succubus → rank corr **0.981** (biggest rating mover of the audited set), tiers drop to 0.357 (+0.017 contribution when live); stub lost-in-the-woods → rank corr 0.994, tiers drop to **0.315** (+0.059 contribution — the largest single-cluster contributor to tracking the external tiers, refuting the earlier "costly greedy anti-synergy" concern). All three clusters help when live; the gap to 0.48 is genuine reshuffle, not greedy bias. Ablation batches deleted after measurement. Duplicate deterministic run `512e5c6a` (same config/seed) kept on disk but not a reference. CR-review hardening: the remaining `dummySource` uses in `EffectResolver.ready`/`readyAndFight`-adjacent handlers (`TakeControlUntilEndOfTurn`) and `ShuffleCreaturesIntoDeck` now pass the real source card; `RepeatWhile` has a progress guard (breaks when an iteration changes neither amber totals nor creature counts — e.g. a steal blocked by The Vaultkeeper); sacrifice / shuffle-into-deck / put-self-on-deck share one `leavePlay` helper (upgrades fall to their owner's discard, Annihilation Ritual purge replacement applies to discards); `CardScriptRegistrationTest` asserts no `Overridden:` generator stub is left as the registered implementation; 3 regression tests added to `Phase3BatchBTest`.
- **Phase 4 — done (regression guardrails, CR-hardened).** `CoverageRatchetTest` (3 ratchet tests, in `engine/cota/`): (1) the verified-card count must equal the `verifiedMinimum` constant — equality, not a floor, so neither dropping below nor growing past it can be forgotten (bump deliberately); (2) any catalog card with ability text that is **recursively inert** — `NoEffect` triggers, `Sequence(NoEffect, …)` and `Conditional`-with-inert-branches count as inert, which caught 9 more cards whose only content was an `Action("…", NoEffect)` payload — or **unregistered** must be listed in `src/test/resources/fidelity/no-effect-allowlist.txt` (currently 54 inert cards; batch (d) trimmed 36 when implementing them, and the review's recursive check re-added 9); stale entries (card now implemented) fail too; (3) every catalog card has a registered script (this surfaced `virtuous-works`/`dust-pixie`, whose pack text is literally "(Vanilla)" — now registered as VANILLA DSL overrides). Manual `CardScript` objects are exempt. Note: cards with only a *fragment* of the text implemented still pass the inert check — the declared-fidelity levels carry that signal. Spec drift check: `./gradlew specDriftCheck` (or `python3 tools/fidelity-inventory.py --check-drift`) compares the keyteki card-file sources stored in the committed `docs/fidelity/core-inventory.csv` **plus** the pack JSON / `overrides.json` / `.spec.js` digests in `docs/fidelity/keyteki-sources.sha` (written by every inventory run) against the current keyteki checkout and **exits 1** on any change; a missing keyteki checkout fails with a clear message instead of reporting every card as disappeared. Kept out of `check` because CI may not carry keyteki. The "keyword-only text" regex is canonical in `src/test/resources/fidelity/keyword-only.pattern` (read by both the test and the inventory tool; linear `[^()]*` alternation, no nested-quantifier backtracking).
- **Phase 3 batch (d) — done (38 DSL overrides + Valdr manual script).** Artifact economy: `DestroyArtifact`/`ReturnArtifactToHand` + `AnyArtifact` targeting (Gorm of Omm omni, Whispering Reliquary, Grasping Vines), `ArchiveFriendlyCreatureOrArtifact` (Vezyma), `ArtifactUsed` event (Veylan Analyst +1A per own artifact use), and **use/play cost auras** — new `UseRestriction.ArtifactUseCost/ArtifactPlayCost` (Tentacus charges 1A per artifact use incl. Omni, Customs Office 1A per artifact play; the generator suppresses unpayable actions and the executor transfers the amber to the aura controller). Fight-target triggers: `game.currentFightTargetId` transient set around `onBeforeFight` → `DealDamageToFightsNeighbors(3)` (Lord Golgotha) and `StunFightTargetAndNeighbors` (Zorg, also enters stunned). Upgrade-granted reaps ride the Phase 2.2 event bus like Duskrunner: Silent Dagger ("Reap: deal 4D to a flank creature", new `FlankCreature` selector) and Red Planet Ray Gun (`DealDamagePerHouseInPlay`). Damage patterns: Positron Bolt 3/2/1 flank cascade, Guardian Demon heal-up-to-2 → deal-that-amount (`HealThenDamageOther` + `CreatureOtherThanLastHealed`), Qyxxlyx `DealDamageIgnoringArmor` (3D to each Human, new `AnyCreatureWithTrait`), Cooperative Hunting `DealDividedDamage`, Red Hot Armor `StripEnemyArmorAndDamagePerLost` (`InPlayCard.armorStrippedThisTurn`, reset at turn start), EMP Blast `EmpBlastBurst` (stun Mars/Robot-trait, destroy each artifact), Bulleteye, Special Delivery (purge-if-destroyed via `DamageDestroyedTarget` + `Purge(LastDamagedTarget)`), Anomaly Exploiter (new `DamagedCreature` selector). Hand/deck attacks: Deep Probe (greedy house = most enemy creatures in hand → discard each of that house), Sound the Horns (`ScanDeckForHouseCreatureToHand`), Squawker (`ReadyMarsOrStunNonMars`). Forge/amber: Interdimensional Graft (`nextForgeGivesAmberTo` flag consumed in the key phase, cleared unclaimed), Shatter Storm (lose all → opponent loses triple), Blood Money (`PlaceAmberOnEnemyCreature` from the common supply), Mating Season (`ShuffleHouseCreaturesIntoDeck(MARS, amberPerEach=1)`). Turn-scoped may-use: `mayUseHouseThisTurn` (Sigil of Brotherhood — use Sanctum creatures this turn) and `mayUseCardIdsThisTurn` (Combat Pheromones — up to 2 Mars cards), both consumed by the generator. Strange Gizmo (own forge → destroy every creature+artifact, removal is direct because the key phase has no state-check fixpoint), Brain Eater (reaction on `FightResolved` destroyed → draw), Faygin (Urchin from play or discard), Charge (turn-scoped subscription: creatures played this turn deal 2D), Lifeward (`DenyCreaturePlaysNextTurn`), Potion of Invulnerability (turn-scoped damage immunity), Masterplan (`beneathCardIds` on InPlayCard; stacked card is played through the real play path via a new `CardPlayBridge` wired from `GameEngine`/`Scenario` to `ActionExecutor.playCard`; upgrades attach to the strongest creature or stay beneath). **Engine fix found by the Charge test: `CreatureEnteredPlay` was emitted for every played card type (actions/artifacts too) — now creature-only, matching keyteki's onCardPlay.** 33 tests in `Phase3BatchDTest`. Declared FULL 215 → **254**; declared fidelity 93.9% → **96.2%**; no-effect crowd 93 → 54. Ladder (`e30bb33c`, rerun after a crash fix): rank corr 0.966 / agreement 93.1% vs batch (c), avg rounds 13.4, tier validation **0.454 / 67.9%** — highest since Phase 2.2 (biggest movers: Chang −13.4, Public Butcher −6.0, Patricia −5.3 down; Seraphina +3.6, Zeb +3.3 up). The first ladder run (`11184491`) had 233 failed matches — Masterplan's omni crashed when the stacked card was an upgrade (also affected Chaos Portal's deck-top); fixed by attaching upgrades to the strongest creature or leaving them beneath, and the rerun (`e30bb33c`) is clean (0 failures). Not in `verified-cards.txt` (no ported keyteki specs). All new blocks live in `dslCardOverrides` + the generator skip-list; Valdr is a manual `CardScript` using the extended `modifyFightDamage(isAttacker, baseDamage, game, fightTarget)` hook.
- **Phase 5 — ladder measured after each phase.** Each run: 42 decks, 30 matches/pairing, seed 12345, tactical AI.

  | Ladder run                                        | Batch                                                                                                                                                                                                                                                                                                                                                            | Rank corr. vs prev. | Pairwise agr. | Tier Spearman | Tier agreement |
  |---------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------------:|--------------:|--------------:|---------------:|
  | Baseline                                          | `083dc1b0`                                                                                                                                                                                                                                                                                                                                                       |                   — |             — |          0.44 |          67.5% |
  | After Phase 1 (`fad7c062`)                        | splash, poison, moribund triggers                                                                                                                                                                                                                                                                                                                                |                0.98 |         95.7% |          0.50 |          69.3% |
  | After Phase 2.1 (`e72fcf30`)                      | EffectRegistry auras (16 cards)                                                                                                                                                                                                                                                                                                                                  |                0.99 |         96.7% |          0.51 |          69.6% |
  | After Phase 2.2 (`21137849`)                      | event bus + 10 reaction cards                                                                                                                                                                                                                                                                                                                                    |                0.97 |         94.7% |          0.45 |          67.9% |
  | After Phase 2.2 fixes (`928c859e`)                | Biomatrix archive path, event delivery, ForgeKey emit, BSA house reset                                                                                                                                                                                                                                                                                           |                1.00 |          100% |          0.45 |          67.9% |
  | After Phase 2.3 (`c4f549b3`)                      | use-restriction auras (6 cards)                                                                                                                                                                                                                                                                                                                                  |               0.988 |         96.2% |          0.46 |          68.1% |
  | After Phase 2.3 CR fixes (`0be038e2`)             | Vaultkeeper blocks hand-moved amber (Too Much to Protect), Pitlord never leaves a player houseless                                                                                                                                                                                                                                                               |                1.00 |          100% |          0.46 |          68.1% |
  | After Phase 3 batch (a) (`c8bcab7f`)              | 18 simple cards (denials, key-cost, clamp, upgrades)                                                                                                                                                                                                                                                                                                             |               0.994 |         97.6% |          0.48 |          68.8% |
  | After Phase 3 (a) hygiene fixes (`c094c703`)      | shared endMainPhase, return/shuffle by controller + upgrades to discard                                                                                                                                                                                                                                                                                          |                1.00 |         99.9% |          0.48 |          68.9% |
  | Ladder-endpoint CR fixes (`d0238530`)             | config-matched comparison, tier-error surfacing, 409 guard (endpoint change only)                                                                                                                                                                                                                                                                                |                1.00 |          100% |          0.48 |          68.9% |
  | After Phase 3 batch (b) (`78b316c7`)              | 23 cards: RepeatWhile / LoseHalfAmber / shuffle-creatures / draw-phase modifiers / play-grants / damage immunity / ignore-taunt-elusive / Tunk+Duskrunner reactions / targeting selectors                                                                                                                                                                        |               0.896 |         87.6% |          0.37 |          64.6% |
  | After Phase 3 (b) cost-direction fix (`b3218876`) | sacrifice/shuffle-away/exhaust now pick the cheapest friendly (`EffectResolver.cheapestFirst`); ladder-neutral                                                                                                                                                                                                                                                   |               0.999 |         99.3% |         0.373 |              — |
  | Phase 3.5 audit fix (`fec052e9`)                  | Bait and Switch `playableIf(OpponentHasMoreAmber)` (keyteki gates the play); ladder-neutral                                                                                                                                                                                                                                                                      |                1.00 |         99.9% |         0.374 |              — |
  | Override-list refactor (`03ea7290`)               | all hand-edited DSL blocks moved to `dslCardOverrides` (CoreCards.kt) + generator skip-list; regen now byte-identical — Known Gap #12 retired; ladder-neutral                                                                                                                                                                                                    |                1.00 |          100% |         0.374 |              — |
  | Phase 3 batch (c) (`d5aecfb9`) | capture/power-counter mechanics + 17 cards (Hypnotic Command fix, Phase Shift, Arise!, Nexus/Remote Access, Snudge, Cleansing Wave, Mothergun, Eater of the Dead, Mugwump, Francus, Take Hostages, Gatekeeper, Drumble, Pandemonium, Honorable Claim, Unguarded Camp, Mindwarper, Martian Hounds, Yxili Marauder play) + GreedyTargetChooser source-opponent fix | 0.984 | 95.8% | 0.412 | 66.6% |
  | Phase 3 batch (d) (`e30bb33c`) | artifact economy + fight-target triggers + 39 inert cards (Tentacus/Customs Office cost auras, Gorm of Omm, EMP Blast, Deep Probe, Positron Bolt, Guardian Demon, Silent Dagger/Red Planet Ray Gun upgrade-reaps, Lord Golgotha/Zorg, Veylan Analyst, Sigil/Combat Pheromones may-use, Strange Gizmo, Brain Eater, Special Delivery, Masterplan bridge plays, Charge, Valdr flank bonus) | 0.966 | 93.1% | 0.454 | 67.9% |

- **Phase 3 batch (c) — done (17 DSL FULL + arise/drumble via manual scripts).** Capture is a first-class mechanic now: `CaptureAmber` gained a `from` pool owner (default opponent; Pandemonium's enemy half locks away the player's own amber), plus `CaptureAmberDynamic` (Yxili Marauder: 1A per friendly ready Mars creature; Gatekeeper all-but-5; Drumble capture-all with `playableIf(OpponentAmberAtLeast(7))`) and `CaptureEach` (per-creature captures: Pandemonium undamaged both sides, Honorable Claim Knights, Unguarded Camp one friendly per excess creature via `SelfHasMoreCreatures` + `maxTargets` lambda, Mindwarper/Hypnotic Command enemy creatures capture 1 from their own side — Hypnotic Command was dishonestly `TakeControlUntilEndOfTurn` before). Event-driven capture: `EventSubjectCaptures` (Francus after fight-destroyed; Take Hostages turn-scoped via `ActivateTurnReaction` — friendly fighters capture 1A for the remainder of the turn). Power counters generalized: `AddPowerCounters(target)` (default self keeps Zyzzix) + `AddPowerCountersDynamic` (Martian Hounds: 2 per damaged creature in play); Mugwump reaction (fight-destroyed → fully heal + counter); Eater of the Dead Fight/Reap purges from either discard (`PurgeCreatureFromDiscard` + `PurgedTarget` condition). Conditionals: Cleansing Wave `HealEach(gainPerHealed)`; Mothergun `DealDamagePerHouseCardInHand` (reveals all Mars cards, greedy); Arise! (manual script upgraded: greedy house = most creatures in discard, now reading real catalog houses — the Scenario harness decks carry `DeckCard` entries so `deck.cards` lookups work); Nexus/Remote Access `UseOpponentArtifact` (uses the opponent's first ready artifact with an Action ability, as if ours); Snudge `ReturnArtifactOrFlankCreatureToHand` (opponent artifact first, else strongest enemy flank creature); Phase Shift `MayPlayNonHouseThisTurn(LOGOS)` (PlayerState flag consumed by `ActionGenerator`: one off-house non-Logos play per turn, active-house plays don't spend the allowance). Engine fix found by the Nexus test: `GreedyTargetChooser` resolved the opponent relative to the **active** player instead of the source card's controller — wrong whenever an effect's source is used by someone else (e.g. a cannon fired through Nexus); now `game.players[1 - sourcePlayer.index]`. 25 tests in `Phase3BatchCTest`. Declared FULL 198 → 215; declared fidelity 92.3% → **93.9%**; no-effect crowd 107 → 93. Ladder (`d5aecfb9`): rank corr 0.984 / agreement 95.8% vs the override refactor, avg rounds 13.4, tier validation **0.412 / 66.6%** — the first tier gain since Phase 2.2 (was 0.374/64.7%), the capture/power-counter cards genuinely help track the external tiers (biggest movers: "Procione" −4.6, Curtius −4.0). Not in `verified-cards.txt` (no ported keyteki specs). All 17 new blocks live in `dslCardOverrides` + the generator skip-list; arise/drumble keep their manual scripts (the registration guard caught drumble being left in the skip-list without an override).

  Batch health every run: 0 failures, 0 draws, avg rounds ~11.6-13.5. Declared fidelity 85.1% → 86.0% → 86.4% → 86.8% → 88.2% → 92.3% → 93.9% → 96.2%.

  **Phase 2.2 investigation (why tier validation dipped).** The dip is **tier-label staleness, not an
  engine bug**. Evidence:

  - Losses concentrate in specific matchups, not general drift: Zoe/González/Torsen all lost to
    **Ingrid** (−17 to −30pp), and Lazzaro lost to Ulysses/Parthenia/Seraphina (−27 to −30pp).
  - Ingrid's **krump + pile-of-skulls + the-warchest** destroy-and-tax engine was completely dead
    before Phase 2.2 and now works — worth ~5–10pp vs fight-based decks.
  - Lazzaro got genuinely weaker: his **annihilation-ritual** now purges his own destroyed creatures
    (no discard recycling) and **magda-the-rat** taxes him 2A per death — both were free/broken before.
  - Implementations verified against keyteki: `AnnihilationRitual.js` purges *any* creature leaving
    play (no friendly/enemy filter) — matches ours. Krump/pile-of-skulls/magda fire exactly once per
    correct trigger; an 8-match automated audit found zero double-fires and zero wrong-direction steals.

  Conclusion: the hand tiers are **SAS-rating-based, external and fixed** (no re-labeling) — so the
  dip is genuine divergence from that external ordering, localized to decks whose key cards were
  dead before Phase 2.2 (Ingrid up, Lazzaro/González/Zoe down). Expect partial convergence as more
  of those cards reach verified parity; until then treat 0.45/67.9% as the standing reference, not
  a regression. Biggest mover: Lazzaro −5.9 points.

  Highest-priority unverified cards: dust-pixie, vigor, giant-sloth, then the unverified FULL crowd
  led by urchin / shadow-self / charette / silvertooth / tocsin / shooler.

  Diffs: `docs/fidelity/20261123__02_11_20-ladder-phi1-vs-baseline.txt`, `20261123__02_52_50-ladder-phi21-vs-phi1.txt`,
  `20261123__03_19_30-ladder-phi22-vs-phi21.txt`; standings: `docs/deck-pool/cota-ladder.md`. Batch (c) diff: `docs/fidelity/20261123__22_27_55-ladder-batchc-vs-override.txt`; standings: `docs/deck-pool/cota-ladder.md`.

## Project Goal

Build a Kotlin + Spring Boot + PostgreSQL service that imports KeyForge decks, simulates AI-vs-AI matches, and produces deck performance ratings. The source of truth is the neighboring **keyteki** repository at `~/work/keyteki/master-vault-data` plus the docs in `docs/`.

## Tech Stack

- Kotlin 1.9.25, JDK 21
- Spring Boot 3.3, Gradle 8.5 (wrapper included)
- PostgreSQL 16 via Flyway migrations
- Tests: JUnit 5, AssertJ, embedded PostgreSQL (`io.zonky.test:embedded-postgres`)
- Container runtime: Docker or Podman (`docker-compose.yml` uses `docker.io/library/postgres:16-alpine`)

## Quick Commands

```bash
# Start Postgres
podman-compose up -d      # or docker compose up -d

# Build & test
./gradlew build           # compile + test + package
./gradlew test            # unit + integration tests
./gradlew build -x test   # compile only

# Run the app
./gradlew bootRun

# Smoke-test the running app (~5 s, ~30 checks, tears down its own data; see "Smoke test" below)
python3 tools/smoke-test.py

# Rerun the cota ladder in one call (blocking ~4-5 min; writes nothing itself)
tools/rerun-ladder.sh | jq

# generate the reports from new ladder
tools/ladder-report.py

# CLI examples
./gradlew bootRun --args "import-deck-file src/test/resources/decks/cota-deck.json"
./gradlew bootRun --args "import-deck <master-vault-uuid>"
./gradlew bootRun --args "simulate <deck-a> <deck-b> [seed] [aiA] [aiB]"
./gradlew bootRun --args "mass-simulate <deck-a> <deck-b> <match-count> [aiA] [aiB] [recordLogs]"
```

## REST API

The app runs on `http://localhost:8080`.

### Decks

- `POST /api/decks/import/{uuid}` — import a deck from the Master Vault.
- `GET /api/decks` — list imported decks.
  - Query params (optional, combine freely):
    - `name` — case-insensitive substring match on deck name.
    - `house` — filter to decks that include this house (e.g., `Dis`, `Untamed`).
    - `expansion` — filter by expansion id (e.g., `341` for CotA).
- `GET /api/decks/{uuid}` — full deck details (cards included).

### Simulation

- `GET /api/simulate/{deckA}/{deckB}?seed={seed}&aiA={ai}&aiB={ai}` — run an AI-vs-AI match. Returns a slim summary by default.
  - Optional `aiA` / `aiB` values: `tactical` (default), `greedy`, `random`, `aggressive`, `economic`.
  - Summary includes `statistics` with counts for reaps, fights, abilities, cards played/discarded, keys forged, and houses chosen.
- `GET /api/simulate/{deckA}/{deckB}?seed={seed}` with header `X-Debug: true` — return the full verbose game state, fidelity log, and match statistics.

### Mass Simulation

- `POST /api/mass-simulations` — start an async batch of matches between two decks.
  - Request body: `{ "deckUuidA": "...", "deckUuidB": "...", "matchCount": 100, "aiA": "tactical", "aiB": "tactical", "recordLogs": true, "baseSeed": 12345 }`
  - Returns `202 Accepted` with the batch metadata (status starts as `PENDING`).
- `GET /api/mass-simulations` — list all batches.
- `GET /api/mass-simulations/{uuid}` — get batch details plus aggregate results
  (includes a `fidelity.cards` section with play-weighted per-card fidelity, worst-proven first).
- `GET /api/mass-simulations/{uuid}/matches` — list per-match results.
- `GET /api/mass-simulations/{uuid}/matches/{id}/logs` — get the debug log + fidelity log for one match.

Batch status values: `PENDING`, `RUNNING`, `COMPLETED`, `FAILED`.
Batch type values: `TWO_DECK`, `MULTI_DECK`.
Results and logs are stored on disk under `.simulations/` (gitignored), not in Postgres.

### Multi-Deck Round-Robin & Ratings

- `POST /api/mass-simulations/multi` — start an async round-robin across a pool of decks.
  - Header `X-Debug: true` — opt-in per-match verbose log storage (off by default).
  - Request body: `{ "deckUuids": ["...", "..."], "matchesPerPairing": 10, "ai": "tactical", "seatAlternation": true, "baseSeed": 12345, "concurrency": null }`
  - Runs `matchesPerPairing` matches per unordered deck pair; seats alternate per match so first-player bias cancels; deterministic seeds are `baseSeed + globalTaskIndex`.
  - Matches execute in parallel on bounded virtual-thread workers (default concurrency = available processors); per-match failures are recorded as `failedCount` without failing the batch.
  - `IllegalArgumentException` validation errors return `400` via `RestExceptionHandler`.
- `POST /api/mass-simulations/{uuid}/tier-validation` — body `{"tiers": {"<deckUuid>": 6.0, ...}}` (higher = better, e.g. S+=6 … Z=0); returns Spearman rank correlation and pairwise agreement between the ladder's Aember Scores and your hand-assigned tiers (needs ≥ 3 labeled decks). Use it to check whether the rating tracks real strength.
- `GET /api/mass-simulations/{uuid}/ratings` — deck ratings (multi-deck batches only):
  - Win/loss/draw record, raw win rate with ±95% margin, strength of schedule (SoS), Bradley-Terry strength (max-likelihood iterative fit, draws count as half wins, smoothing 0.5), **Aember Score** = `50 + 12.5 * ln(r_i / r_median)` (`AemberScale`, scale v2: ~1 point ≈ 2pp win-rate edge near the median; stored `ratings.json` with an older `scaleVersion` is recomputed on read; pool snapshots in `.simulations/pools/` predating v2 stay on the legacy `10·log10` scale), percentile, and a `lowConfidence` flag (< 20 games), plus `aemberScoreLow` / `aemberScoreHigh` — a 95% stratified-bootstrap interval (200 resamples, seeded) on the Aember Score.
  - Ratings are computed at batch completion and persisted as `ratings.json` in the batch directory; the endpoint serves the stored copy and lazily backfills older batches.

### Ladder Rerun (one call)

- `POST /api/ladder/rerun` — **synchronous** one-call ladder rerun (blocks until the batch completes; ~4–5 min for the full cota pool at the documented defaults). Starts a round-robin batch over a named pool, waits for it, and returns health stats, ratings, a comparison against the previous completed ladder over the same pool, and tier validation in one response. All parameters optional (defaults shown):
  - `{ "pool": "cota", "matchesPerPairing": 30, "baseSeed": 12345, "ai": "tactical", "seatAlternation": true, "concurrency": null, "tiers": null, "tiersFile": null, "timeoutSeconds": 1800 }`
  - Tier labels resolve in order: explicit `tiers` map → `tiersFile` path (must stay under `docs/deck-pool/`) → `docs/deck-pool/{pool}-tiers.json` when it exists; missing labels skip the validation silently, but a **broken** label source (malformed file, fewer than 3 labeled decks) is surfaced as `tierValidationError` instead of vanishing.
  - `comparison` finds the latest completed multi-deck batch with the **same pool and the same configuration** (`matchesPerPairing`, `ai`, `seatAlternation`) and reports rank correlation, pairwise agreement, the previous run's config, and the 6 biggest Aember-Score movers (±3). A smoke run with different parameters can therefore never become the baseline of a full ladder.
  - `timedOut: true` marks a partial result; on timeout the response stops at the status/health fields (no ratings, comparison, or tier validation are read from a still-running batch).
  - Only one rerun runs at a time: a second concurrent call returns **409** (`LadderInProgressException` via `RestExceptionHandler`). The single-flight guard lives in `LadderService.rerunInProgress`.
  - Response shape: `{ pool, batchUuid, status, timedOut, matchesPerPairing, baseSeed, ai, completedCount, failedCount, draws, avgRounds, declaredFidelity, verifiedFidelity, comparison: { previousBatchUuid, previousMatchesPerPairing, previousAi, rankCorrelation, pairwiseAgreement, biggestMovers }, tierValidation: { decksCompared, spearman, pairwiseAgreement } | null, tierValidationError: string | null, ratings: [DeckRatingDto…] }`
  - `IllegalArgumentException` validation errors return `400` via `RestExceptionHandler`.
- `tools/rerun-ladder.sh` — CLI wrapper: single curl with the documented defaults; env overrides `POOL`, `PAIRINGS`, `SEED`, `AI`, `TIMEOUT`, `BASE_URL` (e.g. `tools/rerun-ladder.sh | jq`).

### Teardown

- `DELETE /api/mass-simulations/{uuid}` — delete a finished batch and its on-disk artifacts (`204`; `400` if unknown or still `PENDING`/`RUNNING`).
- `DELETE /api/pools/{key}` — delete a pool file and its stored rating snapshots (`204`; `400` if unknown). Seeded pools such as `cota` re-seed lazily from `{key}-pool.txt` on the next read, but their rating history is gone.

### Smoke test (one command)

`python3 tools/smoke-test.py` hits ~30 checks across decks, single simulation, two-deck and multi-deck batches, ratings, tier validation, pools, rating history and a tiny ladder rerun (~5 s) against `http://localhost:8080`. It uses decks **already imported** (needs ≥ 3; it never calls the Master Vault unless you pass `--import <uuid>…`), creates a temporary `smoke-xxxxxxxx` pool and a few small batches, and **tears everything down** through the DELETE endpoints. Exit code 0 = all passed. Options: `--base-url URL`, `--keep` (skip teardown, prints what was kept), `--skip-ladder`, `--import UUID…`. Standard library only. Start the app first (`./gradlew bootRun`, Postgres up).

### Deck Pools

Named pools (e.g. `cota`) back single-deck rating. Pools persist as `.simulations/pools/{key}.json`
and seed lazily from `{aember.pools.seed-dir}/{key}-pool.txt` (default dir = working dir; `cota-pool.txt`).

- `GET /api/pools/{key}` — pool members.
- `POST /api/pools/{key}/decks` — add an imported deck (body `{"deckUuid": "..."}`); creates the pool if unknown.
- `DELETE /api/pools/{key}/decks/{deckUuid}` — remove a deck.
- `POST /api/pools/{key}/ratings/{deckUuid}?matchesPerPairing=10&baseSeed=0&ai=tactical` — synchronous
  single-deck rating: plays the deck against every pool member (parallel virtual threads, seat
  alternation, ~1s for 220 matches), returns raw record + per-opponent results, and — when a completed
  multi-deck ladder batch covers the pool — an anchored rating: the ladder's stored results plus the
  fresh matches are refit via `RatingAggregator.ratings`, Aember Score anchored on the pool median
  (directly comparable with the ladder), with `aemberScoreLow/High` bootstrap bounds and a `timeouts` count
  (round-limit games counted as draws; a high share compresses differences). Snapshots saved to `.simulations/pools/{key}/ratings/`.
- `GET /api/pools/{key}/ratings/{deckUuid}` — history of stored snapshots.

### curl Examples

```bash
# Import a deck from the Master Vault
curl -X POST http://localhost:8080/api/decks/import/{master-vault-uuid}

# List imported decks
curl "http://localhost:8080/api/decks"

# Filter decks by house and expansion
curl "http://localhost:8080/api/decks?house=Dis&expansion=341"

# Get full deck details (cards included)
curl "http://localhost:8080/api/decks/{deck-uuid}"

# Run a single AI-vs-AI match (slim summary)
curl "http://localhost:8080/api/simulate/{deck-a-uuid}/{deck-b-uuid}?seed=42&aiA=tactical&aiB=tactical"

# Run a single match with full verbose log and fidelity data
curl -H "X-Debug: true" "http://localhost:8080/api/simulate/{deck-a-uuid}/{deck-b-uuid}?seed=42"

# Start an async mass-simulation batch
curl -X POST http://localhost:8080/api/mass-simulations \
  -H "Content-Type: application/json" \
  -d '{
    "deckUuidA": "{deck-a-uuid}",
    "deckUuidB": "{deck-b-uuid}",
    "matchCount": 100,
    "aiA": "tactical",
    "aiB": "tactical",
    "recordLogs": true,
    "baseSeed": 12345
  }'

# Poll the batch until status is COMPLETED or FAILED
curl "http://localhost:8080/api/mass-simulations/{batch-uuid}"

# List per-match results
curl "http://localhost:8080/api/mass-simulations/{batch-uuid}/matches"

# Get debug log + fidelity log for match #1
curl "http://localhost:8080/api/mass-simulations/{batch-uuid}/matches/1/logs"

# Start an async multi-deck round-robin ladder
curl -X POST http://localhost:8080/api/mass-simulations/multi \
  -H "Content-Type: application/json" \
  -d '{
    "deckUuids": ["{deck-1-uuid}", "{deck-2-uuid}", "{deck-3-uuid}"],
    "matchesPerPairing": 10,
    "ai": "tactical",
    "seatAlternation": true,
    "baseSeed": 12345
  }'

# Get deck ratings for a multi-deck batch (persisted as ratings.json)
curl "http://localhost:8080/api/mass-simulations/{batch-uuid}/ratings"

# Deck pools: view members, add a deck, rate a single deck against the pool
curl "http://localhost:8080/api/pools/cota"
curl -X POST http://localhost:8080/api/pools/cota/decks \
  -H "Content-Type: application/json" \
  -d '{ "deckUuid": "{master-vault-uuid}" }'
curl -X DELETE http://localhost:8080/api/pools/cota/decks/{deck-uuid}

# Rate a deck synchronously against the pool (~1s); anchored Aember Score
curl -X POST "http://localhost:8080/api/pools/cota/ratings/{deck-uuid}?matchesPerPairing=10&baseSeed=0&ai=tactical"

# History of stored rating snapshots for a deck
curl "http://localhost:8080/api/pools/cota/ratings/{deck-uuid}"
```

## Architecture

Hexagonal / ports-adapters layout:

```text
src/main/kotlin/com/aember/analyzer/
  domain/              # Pure business logic
    model/             # Deck, CatalogCard, enums
    engine/            # Rule engine, actions, DSL, fidelity
    ai/                # AI policy implementations
    ports/             # Driven/driving ports
  application/         # Use-case services
  infrastructure/      # DB adapters, config, seeding
  adapter/             # REST controllers, CLI runner
```

## Current State

### What works
- PostgreSQL catalog seeded with Core set (`CotA`, expansion 341) card definitions.
- Master Vault deck import by UUID or local JSON file; decks persisted via JPA.
- Full turn structure: setup, key phase, house phase, main phase, ready/draw/end phases.
- Creatures enter play **exhausted** (per keyteki `PutIntoPlayAction`), except when a card says otherwise: Speed Sigil readies the first creature played each turn and Silvertooth readies itself. Out-of-turn plays (e.g. `playTopCard` effects) also enter exhausted.
- Forge cost is computed via `ForgeRules.keyCost`: base 6 plus registry-driven static modifiers (e.g. Jammer Pack +2A, Titan Mechanic −1A on a flank) and one-turn bonuses (e.g. Lash of Broken Dreams +3A via `keyCostNextTurnBonus`).
- Turn denial flags on `PlayerState`: `cannotFightNextTurn` (Fogbank) suppresses fight actions for the flagged player's next turn; `deniedHouseNextTurn` (Control the Weak) and `skipsForgeNextTurn` (Miasma) work the same way.
- Six-name rule: a player can play/use/discard at most 6 cards with the same name per round.
- Rule engine handles Core-set keywords:
  - `alpha`, `omega`, `elusive`, `skirmish`, `taunt`, `hazardous`, `assault`, `deploy`
  - `deploy` currently only allows flank placement (no in-line placement yet).
- Zones: `archives`, `purged`, `chains`.
- AI players: `RandomAI`, `GreedyAI`, `TacticalAI` (default) with configurable profiles (`default`, `aggressive`, `economic`).
  - `TacticalAI` scores houses with a small tempo penalty for no-creature turns (except chain-synergy houses like Library Access), boosts chain cards (Library Access / Wild Wormhole), penalizes removal into empty boards, and places high-value creatures next to protectors (taunt / Shadow Self).
- Match statistics tracked during simulation (reaps, fights, abilities, plays, discards, keys forged, houses chosen).
- Deterministic match simulation via seeds.
- REST endpoints and CLI commands for import and simulation.
- Deck query API: list, filter (by name, house, expansion), and retrieve by UUID.
- Async mass-simulation harness: run `N` matches between two decks, store aggregate results and per-match debug logs on disk under `.simulations/`.
- Multi-deck round-robin harness: parallel virtual-thread execution, seat alternation, per-deck/per-pair aggregates, and Bradley-Terry `Aember Score` ratings via `GET .../ratings`.
- CLI: `mass-simulate-decks <deck-list-file> <matches-per-pairing> [ai] [seed] [--debug-logs]` (default seed 0, deterministic).

### DSL / Card Coverage

- `domain/engine/cards/CoreSetDslCards.kt` contains ~368 of 370 Core cards generated from `tools/generate-core-cards.py`.
- Manual overrides live in `domain/engine/cards/CoreCards.kt`: `BadPenny`, `Anger`, `GatewayToDis`, `Drumble`, `EtherSpider`, `ShadowSelf`, `SpeedSigil`, `ControlTheWeak`, `Miasma`, `TendrilsOfPain`, `Arise`, `Silvertooth`, `YxilxDominator`, `JammerPack`, `BiomatrixBackup`, `LashOfBrokenDreams` (keys +3A next turn via `keyCostNextTurnBonus`), `TooMuchToProtect` (steal all but 6), `Fogbank` (no fights next turn via `cannotFightNextTurn`), plus two manual DSL lists (`marsEngineDslCardOverrides`: Key Abduction, Battle Fleet, Commpod, Phosphorus Stars, Zyzzix the Many, John Smyth; `keyCheatDslCardOverrides`: Key Charge, Chota Hazri, Key of Darkness, Epic Quest — all forge keys outside the forge phase via the `ForgeKey` effect).
- `CardScript` exposes static-ability hooks so generic engine code never checks card names:
  - `cardEntersReady()` — e.g. Speed Sigil, Silvertooth.
  - `cardEntersStunned()` — e.g. Yxilx Dominator.
  - `redirectsNeighborDamage()` — e.g. Shadow Self.
  - `capturesOpponentAmberGains()` — e.g. Ether Spider.
  - `protectsNeighbors()` — used for placement; e.g. Shadow Self (also `taunt` keyword is checked directly).
  - `modifyFightDamage(...)` — e.g. Ether Spider, Shadow Self.
  - `staticEffects()` — persistent static effects evaluated by `EffectRegistry` (power/armor/keyword auras, key-cost modifiers); DSL cards use `persistent { ... }`; e.g. Jammer Pack.
  - `archivesAttachedCreatureOnDestroyed()` — upgrade; e.g. Biomatrix Backup.
- Supported triggers: `Play`, `Reap`, `Fight`, `Before Fight`, `After Fight`, `Before Reap`, `After Reap`, `Destroyed`, `Action`, `Omni`.
- Supported effect types in `domain/engine/dsl/Effect.kt`:
  - `GainAmber`, `GainAmberOnTriggerThisTurn`, `DrawOnCardPlayThisTurn`, `StealAmber`, `CaptureAmber`, `LoseAmber`, `GainAmberOpponent`, `LoseAllAmber`, `GainAmberEqualToOpponentKeys`
  - `DealDamage`, `Destroy`, `DestroyMostPowerful`, `DestroyLeastPowerful`, `DestroyWeakCreatures`
  - `Draw`, `DiscardRandom`, `DiscardTopDeck`
  - `ReturnToHand`, `ReturnFromDiscardToHand`
  - `Archive`, `ArchiveTopDeck`, `ArchiveFromDiscard`, `ArchiveFromHand`
  - `Purge`, `PurgeFromDiscard`
  - `Heal`, `Ready`, `Exhaust`
  - `Fight`, `ReadyAndFight`, `ReadyAndUse`, `UseCreature`
  - `PlayTopCard`, `PlayTopCardOfHouse`, `AddTopCardOfHouseToHand`, `SwapDeckAndDiscard`, `ShuffleHandAndDiscardIntoDeck`
  - `Stun`, `Unstun`
  - `GainChains`, `LoseChains`
  - `DenyHouseNextTurn`, `TakeControlUntilEndOfTurn`, `TakeControlOfEnemyFlankCreature`
  - `SkipForgeNextTurn`, `ReturnCreaturesOfHouseFromDiscardToHand`
  - `ReturnAllCreaturesOfHouseToHand`, `ForgeKey`, `DrawPerCardInHand`, `ReadyCreaturesOfHousePerCardInHand`, `AddPowerCounters`, `SacrificeSelf`
  - `Sequence`, `Conditional`
  - Event-reaction effects (Phase 2.2): `LoseAmberOpponent`, `EventSideGainsAmber`/`EventSideLosesAmber` (+ `EventSubject`), `StealFrom`, `ActivateTurnReaction`, `ReadyHost`
- Event bus (`domain/engine/events/`): typed `GameEvent`s (`CardPlayed`, `CreatureEnteredPlay`, `CreatureReaped`, `FightResolved`, `CreatureDestroyed`, `CardLeftPlay`, `KeyForged`) dispatched by the stateless `EventBus` to `CardScript.onEvent`; DSL `reaction(...) { ... }` with `subscription = true` for turn-scoped reactions (game-level `TurnScopedReaction` list, cleared at end of turn).
- Supported conditions in `domain/engine/dsl/Condition.kt`:
  - `OpponentHasMoreAmber`, `SelfHasMoreAmber`, `OpponentHasFewerCreatures`, `SelfHasFewerCreatures`
  - `OpponentAmberAtLeast(amount)`, `OpponentAmberAtMost(amount)`, `OpponentAmberExactly(amount)`, `SelfAmberAtLeast(amount)`
  - `StealSucceeded`, `DamageDestroyedTarget`
  - `OpponentForgedKeyLastTurn`
  - `CardsOfHousePlayedThisTurn(house, amount)` — per-turn play counter (`cardsPlayedThisTurnByHouse`)
  - `Always`
- Supported creature selectors in `domain/engine/dsl/Target.kt`:
  - `FriendlyCreature`, `EnemyCreature`, `AnyCreature`
  - `FriendlyCreatureWithTrait`, `EnemyCreatureWithTrait`, `CreatureWithKeyword`
  - `FriendlyCreatureWithHouse`, `OtherFriendlyCreatureWithHouse`
  - `CreatureNotOfHouse` (both players)
  - `CreatureAndNeighbors(base)`, `EachOtherUndamagedCreature`, `AllCreaturesOfChosenHouse`, `LastDamagedTarget`
- Play restrictions: `CardDefinition.playableIf(condition)` prevents a card from being generated as a legal play action when the condition is false.

### Fidelity System

- Every card played/used/destroyed logs a `FidelityLevel`:
  - `FULL` — all effect sentences mapped
  - `PARTIAL` — mapped simple parts or text present but not fully implemented
  - `APPROXIMATED` — complex interaction ignored
  - `VANILLA` — truly vanilla card
- `FidelityReportService` computes a match score, level distribution, and lowest-fidelity cards.
- **Verified fidelity (Phase 0):** `VerifiedCardRegistry` reads `src/main/resources/fidelity/verified-cards.txt`
  (card ids with a passing parity test). The report carries both a **declared** score and a
  **verified** score: declared-`FULL` cards without a parity test count as the new
  `FidelityLevel.UNVERIFIED` (score 60, deliberately below PARTIAL's 70) in the verified view. `FidelitySummaryDto` exposes
  `score`, `verifiedScore`, and `unverifiedCount`.
- **Batch per-card fidelity (Phase 0):** every batch accumulates play-weighted per-card stats
  (`CardFidelityAccumulator` → `CardFidelityAggregator`), persisted as `fidelity-cards.json`
  and surfaced in `GET /api/mass-simulations/{uuid}` as a `fidelity.cards` section sorted by
  `verifiedAvgScore` ascending (worst-proven cards first). Batches predating the feature
  return an empty list.
- Default threshold is **70%** (`aember.simulation.fidelity-threshold`) on the declared score.
  Simulations below it log a warning.
- Recent sample matches report ~75–76% fidelity; after the card-improvement pass the Mehitabel vs Galilee matchup averages ~91.3%.

### Parity Harness (Phase 1)

- **Scenario DSL** (`src/test/kotlin/com/aember/analyzer/engine/support/Scenario.kt`): mirrors
  keyteki's `setupTest`. `scenario { player1 { house; inPlay; hand; deck; discard; archives;
  purged; amber; keys; chains }; player2 {…} }` builds a real `GameState` on the production
  Core card library; helpers `play(cardId, target?, flank?, house?, addIfMissing=false)`
  (strict: fails when the card is not in hand), `reap`, `fight(attacker, target)`,
  `useAbility(cardId, abilityId)` (Action/Omni), `discard`, `unstun`, `chooseHouse`,
  `endTurn()` (ready + draw refill + switch + next turn's forge phase), and `startTurn()`.
  Per-player access: `scenario.p1` / `scenario.p2` (PlayerState) and keyteki-style
  `scenario.player1` / `scenario.player2` views for ported skeletons (fail if not active).
- The turn phase logic shared with the engine lives in `domain/engine/TurnPhases.kt`
  (start-turn flag reset, key phase, ready, draw, end, checkGameOver) so the harness cannot
  drift from engine rules.
- **ChoiceProvider port:** `ScriptedTargetChooser` (implements `TargetChooser`) answers effect
  target prompts from pre-scripted rules, falling back to `GreedyTargetChooser`; wire it via
  `scenario { chooseTargets { rule(AnyCreature::class.java) { game, _ -> listOf(...) } } }`.
  The library is built by `CoreCardLibrary.create(targetChooser)` → `buildCoreCardLibrary`
  (shared with `CardLibraryConfig`, so tests use the exact production card set).
- **Verified registry:** a card id goes into `src/main/resources/fidelity/verified-cards.txt`
  only once **every portable keyteki spec for it is ported and passing**; specs that cannot be
  ported yet are documented as `BLOCKED` comments there (e.g. dodger's Neffru spec needs the
  AoA card + the Phase 2 event bus). `VerifiedCardRegistryTest` guards that listed ids exist
  in the catalog and are declared FULL; Phase 4 adds the full spec-coverage ratchet.
- **Spec porter** `tools/port-keyteki-specs.mjs`: parses keyteki `.spec.js` files and emits
  **compilable** Kotlin skeletons (`engine/cota/*ParityTest.kt`) — mechanical actions
  (setupTest/play/reap/fightWith/endTurn) are generated against the `Scenario.player1`/`player2`
  views, and every unfinished test ends with `error("TODO")` so it fails loudly instead of
  passing vacuously.
- The 16-test pilot (`engine/cota/CoreParityPilotTest.kt`) covers all portable specs for
  punch, lava-ball, dust-imp, dodger, doc-bookton, bumpsy, batdrone, dew-faerie, headhunter,
  bad-penny — all listed in `verified-cards.txt`.

### Tests

- `GameEngineTest` — smoke tests for full matches.
- `KeywordTest` — unit tests for `elusive`, `skirmish`, `hazardous`, `assault`, `taunt`, `alpha`, `omega`.
- `EffectAndTriggerTest` — unit tests for all recently added effects and triggers.
- `FidelityReportServiceTest` — declared vs verified fidelity scores, UNVERIFIED reporting, thresholds.
- `CardFidelityAggregatorTest` — per-card play weighting, verified average, declared-level mode, batch `fidelity.cards` DTO ordering.
- `VerifiedCardRegistryTest` — registry entries exist in the catalog and are declared FULL.
- `CoverageRatchetTest` — Phase 4 guardrails: verified-count ratchet (must equal `verifiedMinimum`, bump deliberately), inert-card ratchet (`fidelity/no-effect-allowlist.txt` carries recursively inert cards; stale entries fail too), and every-catalog-card-registered.
- `CoreParityPilotTest` — 16 keyteki parity tests for 10 Core cards (Phase 1; see *Parity Harness* above).
- `PersistentEffectsTest` — Phase 2.1 registry tests: auras (Banner of Battle, King of the Crag, Round Table, Niffle Queen, Grey Monk, Bulwark, Halacor), conditional modifiers (Staunch Knight, Shoulder Armor, Titan Mechanic), upgrade grants (Ring of Invisibility, Protect the Weak, Blood of Titans), dynamic amounts (Yxili Marauder, Mushroom Man), key-cost modifiers (Jammer Pack), aura-loss kills, and effective power driving fight damage.
- `ReactionsTest` — Phase 2.2 event-bus tests: Soul Snatcher / Tolas (destroyed-creature amber), Pile of Skulls (own-turn capture), Krump / Grommid (fight-destroyed reactions), Carlo Phantom (artifact played), Looter Goblin / Loot the Bodies (turn-scoped subscriptions), Magda the Rat (leaves-play steal), Brain Stem Antenna (host ready + temporary Mars house until turn start), Annihilation Ritual (purge replacement).
- `UseRestrictionsTest` — Phase 2.3 restriction tests (12 unit + 11 ported keyteki specs): Grommid (no creature plays / both fight directions / Bad Penny / both-die), Tireless Crocag (no reap, any-house use, self-destruct variants, not contagious), Pitlord (forced house), Ember Imp (2-play limit, lifts when the imp dies), The Vaultkeeper (no steal; capture/lose unaffected), Witch of the Wilds (one off-house Untamed play).
- `Phase3BatchATest` — Phase 3 batch (a): next-turn denials (Skippy/Scrambler/Foggify flags + generator enforcement), Doorstep to Heaven clamp, Timetraveller shuffle-into-deck, Lady Maxena stun + self-return, upgrade keyword grants (Yo Mama Mastery, Way of the Bear/Wolf), key-cost auras (Murmook, Iron Obelisk dynamic), Lupo/Mighty Tiger plays, skirmish + poison interplay, Chuff Ape enters-stunned.
- `Phase3BatchBTest` — Phase 3 batch (b): Bait and Switch repeat-steal, Effervescent Principle halving, Lost in the Woods shuffle, Mother/Succubus draw-phase modifiers, Niffle Ape taunt/elusive ignore, Soft Landing ready-grant, Shield of Justice/Protectrix damage immunity, Vigor heal-gain, Twin Bolt/Pawn Sacrifice distinct targets, Seeker Needle destroy-gain, Sergeant Zakiel neighbor fight, Dextre destroyed-to-deck, Booby Trap non-flank splash, Nocturnal Maneuver exhaust-3, Terms of Redress capture, Duskrunner host-steal, Tunk Mars-heal reaction, Nepenthe Seed omni, Ritual of Balance both branches, Fear.
- `PhaseOneCardsTest` — unit tests for `Control the Weak`, `Library Access`, `Invasion Portal`, `Hypnotic Command`, `Neutron Shark`, `Bouncing Deathquark`, `Dr. Escotera`, `Smiling Ruth`, `Miasma`, `Tendrils of Pain`, `Arise`, `Silvertooth`, `Yxilx Dominator`, and protection-aware placement.
- `MadameCardsTest` — unit tests for the Madame-era cards including Speed Sigil behavior (only the first creature played each turn enters play ready).
- `MarsEngineCardsTest` — unit tests for Key Abduction, Battle Fleet, Commpod, Phosphorus Stars, Zyzzix the Many, John Smyth, Jammer Pack key-cost aura, Biomatrix Backup archive-on-destroy, and enter-ready creatures.
- `TorsenControlCardsTest` — unit tests for Lash of Broken Dreams key-cost bonus, Too Much to Protect, Fogbank fight denial, Fear single-bounce, and the TacticalAI hand-wipe scoring.
- `KeyCheatsTest` — unit tests for Key Charge, Chota Hazri, Key of Darkness (both cost branches), and Epic Quest (Knight archiving + no-cost forge after 7 Sanctum plays).
- `RoundRobinScheduleGeneratorTest` — pair counts, seat alternation, deterministic seeds.
- `BradleyTerryCalculatorTest` / `RatingAggregatorTest` — rating math (dominant decks, circular records, draws as half wins, SoS, aggregates).
- `MultiDeckMassSimulationControllerWebTest` — end-to-end multi-deck batch: 3 decks × 2 matches, aggregates, ratings endpoint, log opt-in, validation errors.
- `LadderServiceTest` — one-call ladder rerun unit tests: previous-batch selection (same pool **and** same matchesPerPairing/ai/seatAlternation; smoke runs never become the baseline), comparison maths on fixed ratings (perfect + inverted orderings), tier-label path validation (only under `docs/deck-pool/`), tier errors surfaced as `tierValidationError`, no-previous-ladder path, invalid pool keys, and the single-flight 409 guard.
- Integration tests for catalog seeding, deck import, and deck queries.

Run `./gradlew test` — all tests currently pass.

## Important Configuration

`src/main/resources/application.yml`:

```yaml
spring.datasource.url: jdbc:postgresql://localhost:54320/aember
spring.datasource.username: aember
spring.datasource.password: aember
keyteki.data.path: /home/mateus/work/keyteki/master-vault-data
aember.simulation.fidelity-threshold: 70.0
```

Test-specific overrides are in `src/test/resources/application-test.yml`.

## Key Files for the Next Agent

| Area               | Files                                                                                                                                                                                         |
|--------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Engine entry point | `domain/engine/GameEngine.kt`, `domain/engine/TurnPhases.kt`                                                                                                                                  |
| Action execution   | `domain/engine/action/ActionExecutor.kt`, `ActionGenerator.kt`                                                                                                                                |
| DSL effects        | `domain/engine/dsl/Effect.kt`, `EffectResolver.kt` (dispatcher + shared helpers) + `EffectResolverAmber/Combat/Cards/Control/Reactions.kt` (handler groups), `CardDsl.kt`, `DslCardScript.kt` |
| Static effects     | `domain/engine/effects/EffectRegistry.kt`, `StaticEffect.kt`, `UseRestriction.kt`                                                                                                             |
| Event bus          | `domain/engine/events/GameEvent.kt`, `EventBus.kt`, `dsl/CardDsl.kt` (reactions)                                                                                                              |
| Card definitions   | `domain/engine/cards/CoreSetDslCards.kt`, `CoreCards.kt`                                                                                                                                      |
| Generator          | `tools/generate-core-cards.py`                                                                                                                                                                |
| Fidelity           | `domain/engine/fidelity/FidelityLevel.kt`, `CardFidelity.kt`, `FidelityReportService.kt`, `VerifiedCardRegistry.kt`, `CardFidelityAggregator.kt`                                              |
| Parity harness     | `src/test/kotlin/com/aember/analyzer/engine/support/*`, `engine/cota/CoreParityPilotTest.kt`, `tools/port-keyteki-specs.mjs`                                                                 |
| AI                 | `domain/ai/TacticalAI.kt`, `AISettings.kt`, `GreedyAI.kt`, `RandomAI.kt`                                                                                                                      |
| Services           | `application/SimulationService.kt`, `MassSimulationService.kt`, `LadderService.kt`, `MassSimulationAsyncExecutor.kt`, `DeckImportService.kt`                                                  |
| Ratings            | `domain/rating/BradleyTerryCalculator.kt`, `RatingAggregator.kt`, `DeckRating.kt`                                                                                                             |
| Storage            | `infrastructure/persistence/FileSystemMassSimulationRepository.kt`, `FileSystemDeckPoolRepository.kt`                                                                                         |
| CLI/REST           | `adapter/cli/AemberCliRunner.kt`, `adapter/rest/SimulationController.kt`, `MassSimulationController.kt`, `LadderController.kt`, `DeckController.kt`, `DeckPoolController.kt`                  |
| Smoke test         | `tools/smoke-test.py`                                                                                                                                                                         |
| Tests              | `src/test/kotlin/com/aember/analyzer/engine/*` (set-generic), `engine/cota/*` (Core-set card tests), `src/test/kotlin/com/aember/analyzer/simulation/*`                                       |

## How to Analyze a Gameplay Log

The full verbose match log is available via `GET /api/simulate/{a}/{b}?seed=...` with header `X-Debug: true`. The log is a chronological list of `LogEntry` objects inside `gameState.log`. Use this guide to validate that a match looks correct.

### 1. Basic sanity checks

For every match, confirm:

- **End cause** is `KEYS` for a normal game, or `TIMEOUT` if it hit `maxRounds` (default 40).
- **Winner** has 3 keys if ended by `KEYS`.
- **Rounds** are reasonable: most CotA games end between 8–16 rounds. Games routinely hitting 40 indicate a bug (infinite loop, AI cannot close out, or engine cannot forge keys).
- **Fidelity score** is above the configured threshold (default 70 %). If it drops below, inspect `lowestCards` for cards whose effects are not fully implemented.

### 2. Turn structure

Each turn should follow this sequence in the log:

```text
--- Turn N / Round M - Player X ---
Player X's hand: ...
Player X does not forge a key (amber: N)   // or "forges a key!"
Player X's tactical AI chooses HOUSE (HOUSE=score, HOUSE=score, HOUSE=score)
Player X chooses HOUSE
... main phase actions ...
Player X ends their turn
Player X readies their cards
```

Red flags:
- Missing hand log → `logHand()` was bypassed or `X-Debug` is false.
- Missing house choice → `housePhase` is broken or the AI skipped it.
- A player takes many consecutive turns → `endPhase` is not switching active player.

### 3. House enforcement

Verify that every card played/used in a turn belongs to the chosen house. A quick way is to grep `chooses` and the following `plays`/`uses`/`reaps with` lines. The only legal cross-house actions are **Omni** abilities.

Example of a legal turn:

```text
Player A chooses SHADOWS
Player A plays Hidden Stash
Player A plays Nexus
Player A plays Nerve Blast
```

Red flag: a `MARS` card is played in a `DIS` turn (unless via an effect like Wild Wormhole, which should be logged separately).

### 4. Six-name rule

A player can play/use/discard at most 6 cards with the same **card name** per round. To verify:

1. Group all `plays <Card>` messages by `(round, activePlayerIndex, cardName)`.
2. Any count > 6 is a bug.

The rule is enforced in `ActionGenerator.canUseCardName`.

### 5. Amber economy

Every amber-changing operation now logs before/after counts:

```text
Player A gains 1 amber from bonus icons (Player A: 0 -> 1)
Player A reaps with dust-imp for 1 amber (Player A: 1 -> 2)
Player A forges a key! Keys: 1 (Player A: 6 -> 0)
Player A steals 1 amber from Player B (Player A: 0 -> 1, Player B: 2 -> 1)
Player A captures 2 amber from Player B on yxilx-dominator (Player A: 1 -> 1, Player B: 3 -> 1)
Player A loses 1 amber (Player A: 2 -> 1)
```

Things to check:
- **Steal/Capture/Lose** cannot reduce the target below 0 amber.
- **Forge** subtracts exactly 6 amber and increments `keys`.
- **Bonus icons** fire before card text (e.g., Hidden Stash with 1 amber pip gives the amber before archiving).
- If an opponent has 0 amber, cards like `Bumpsy`, `Nerve Blast`, `Relentless Whispers` should still resolve but the steal/capture/lose portion is wasted. `TacticalAI` now penalizes playing these cards into an empty opponent amber pool.

### 6. AI decision rationale

When either player uses `TacticalAI`, each decision is logged with its score and runner-up:

```text
Player A's tactical AI chooses MARS (DIS=0.70, MARS=2.10, SHADOWS=1.40)
Player A's tactical AI: play Yxilx Dominator (CREATURE, base=1.45, forgeBonus=0.00) (score=1.02, runner-up play ether-spider=0.95)
Player A's tactical AI: fight rogue-ogre(P6) vs umbra(P2), threat=1.20, willKill=true, boardDelta=1.50, survival=0.50, risk=0.00 (score=2.72, runner-up fight=2.72)
```

Use this to diagnose questionable plays:
- **House choice**: compare the three scores. The chosen house should have the highest score based on playable hand cards + active board cards.
- **Fight vs reap**: when close to forging (amber ≥ 5), reaping should score higher unless the board threat is large.
- **Wasted effects**: if the AI plays a `Bumpsy` into 0 opponent amber, the rationale will not show an `oppAmberPenalty` unless the opponent had 0 amber. If you see repeated wasted effects, the contextual penalty may need tuning.

### 7. Zone changes

Track cards moving between zones:

- **Archives**: should log the card name now: `Player A archives 1 card(s) from hand: Ammonia Clouds`.
- **Purged**: logged as `Player X purges N card(s) from discard`.
- **Discarded**: `Player X discards <card>`.
- **Returned to hand**: logged as `<card> returns to hand`. Watch for `Wardrummer` returning itself — that would be a bug; it should only return *other* friendly Brobnar creatures.

### 8. Combat and keywords

Fight entries should show:

```text
Player A fights with rogue-ogre against umbra
umbra is destroyed
```

Keyword interactions to verify:
- **Elusive**: first fight against an elusive creature in a turn should log `<creature> uses elusive` and deal no damage.
- **Skirmish**: attacker should not take damage.
- **Hazardous/Assault**: damage is logged before normal fight damage.
- **Taunt**: non-taunt attackers cannot target non-taunt defenders if a taunt creature is present.
- **Alpha**: must be the first action of the main phase.
- **Omega**: ends the main phase immediately after resolution.

### 9. Fidelity log

Each card played/used/destroyed may add a `FidelityEntry` to `fidelityLog`. Levels:

- `FULL` — all effect sentences implemented.
- `PARTIAL` — some text not implemented (common for static/ongoing effects).
- `APPROXIMATED` — complex interaction simplified.
- `VANILLA` — no text to implement.

A match score below the threshold means too many cards were `PARTIAL`/`APPROXIMATED`. Check `fidelity.lowestCards` to see which cards dragged the score down.

### 10. Quick analysis script

Use a Python snippet like this to extract common red flags from `result.json`:

```python
import json
from collections import defaultdict

with open('result.json') as f:
    data = json.load(f)

log = data['gameState']['log']

# Six-name rule check
plays = defaultdict(lambda: defaultdict(int))
for e in log:
    if 'plays ' in e['message']:
        card = e['message'].split('plays ')[1].split(' ')[0]
        plays[e['round']][(e['activePlayerIndex'], card)] += 1

for round_, counts in plays.items():
    for (player, card), count in counts.items():
        if count > 6:
            print(f'VIOLATION R{round} P{player} {card}: {count}')

# Amber operations
for e in log:
    if any(x in e['message'] for x in ['gains', 'steals', 'captures', 'loses', 'reaps', 'forges a key']):
        print(e['message'])
```

### 11. When to file a bug

Open an issue or fix directly if you see:

1. Six-name rule violations.
2. Cross-house plays/uses without an Omni or card effect justification.
3. Amber totals going negative.
4. Infinite loops (same card played many times in one turn, or game never ends by keys).
5. Card effect text that resolves but produces no log entry.
6. Fidelity score consistently below threshold for a wide range of seeds.

## Known Gaps / Likely Next Steps

1. **Static / ongoing effects**: power/armor/keyword auras, key-cost modifiers and **use-restriction auras** are registry-driven (Phase 2.1/2.3: `EffectRegistry`). Non-stat statics like damage redirection (Shadow Self) and Ether Spider's amber capture remain `CardScript` hooks until the event bus.
2. **Conditional card texts**: `if`, `for each`, `until`, `may`, `while`, `after`/`before` outside of fight/reap triggers.
3. **Effect-played cards skip use restrictions**: cards played by effects (Wild Wormhole / `PlayTopCard`, Mimicry-style) do not go through `ActionGenerator`, so Grommid's creature ban and Ember Imp's play limit do not apply to them (keyteki blocks them). The next-turn denials (Foggify/Skippy/Scrambler) are equally generator-only, so effect-driven fights (`ReadyAndFight`, Anger) and `PlayTopCard` bypass them too — same family as Known Gap #10. The current `playTopCard` path is itself an approximation (generic power-3 creature, no card script or events), so the two need fixing together when PlayTopCard cards get verified.
4. **Precise targeting**: choose by house, trait, flank, neighbor, or specific creature. Currently, many cards use broad selectors (Booby Trap's "not on a flank" restriction is downgraded to PARTIAL for this reason).
5. **Omni / Action timing**: Omni is implemented as a once-per-turn action ability, but the “as if active house” rule only works for artifacts/creatures with Omni; cross-house action abilities on non-Omni cards are not supported.
6. **Deploy**: only flank placement is enforced; no in-battleline placement yet.
7. **Upgrades**: can be attached, but continuous upgrade effects are not processed.
8. **Reference ladder**: ratings are pool-relative; no curated cross-set ladder exists yet.
9. **More sets** beyond Core (`CotA`) are not seeded.
10. **Effect-driven fights bypass keyword handling**: `ReadyAndFight` (Anger, Relentless Assault) and the `Fight` effect swap raw power damage without elusive/skirmish/assault/hazardous; Phase 2's event-bus work should funnel them through the shared fight resolution in `ActionExecutor`.
11. **Armor is applied per damage event** (`amount - armor`), while keyteki tracks per-turn armor usage; multi-hit interactions may diverge.
12. **Generator drift — RESOLVED.** All hand-edited DSL blocks (110 cards: Phase 2 static/reaction/restriction cards, Phase 3 batches and earlier hand patches) now live in `dslCardOverrides` (CoreCards.kt, registered after the generated set so it always wins); `tools/generate-core-cards.py` carries the matching `OVERRIDDEN_ELSEWHERE` skip-list and regenerating produces a byte-identical file. Ids that live in `marsEngineDslCardOverrides`/`keyCheatDslCardOverrides` are skipped by `dslCardOverrides` to avoid double registration (an inert stub registered later would win). New handwritten cards must be added to BOTH the override list and the generator skip-list.

## Importing Cards from a New Set (keyteki → engine)

Only Core (`CotA`, expansion 341) is wired today. This is the checklist for adding another set
(e.g. `AoA` 435, `WC` 452, `MM` 479, `DT` 496). It follows PLAN.md Phase 7 ("keep generator and
harness set-agnostic; one pool and one ladder per set") and the Core workflow (Phases 0–4).
Steps marked **(code change)** need new code the first time a second set is added — they are
hard-coded for Core now.

**Source data (keyteki checkout at `~/work/keyteki`)**

| What                           | Where                                                                                                                  |
|--------------------------------|------------------------------------------------------------------------------------------------------------------------|
| Card data per set              | `master-vault-data/packs/<Code>.json` (`cards[]`: `id`, `house`, `type`, `text`, `keywords`, `traits`, `expansion`, …) |
| Expansion ids / release order  | `master-vault-data/expansions.json` (`id`, `code`, `pack`); some sets have several ids (WC has 452 + anomaly 453)      |
| Reference card implementations | `server/game/cards/<NN>-<Set>/*.js` (`02-AoA`, `03-WC`, `04-MM`, `05-DT`, …)                                           |
| Reference behaviour specs      | `test/server/cards/<NN>-<Set>/*.spec.js`                                                                               |

Never edit pack files; keyteki corrects bad data in `master-vault-data/overrides.json`.

### 1. Check the data before writing code

1. Pull the keyteki repo and run `npm run validate` in `master-vault-data` if the pack changed.
2. Inspect the pack: `houses`, `types` and `keywords` it uses versus what the engine knows
   (`domain/model/House.kt`, `CardType.kt`, `Keyword.kt`, `JsonCardCatalogLoader.parseHouse/parseType`).
   Known differences from Core:
   - **Houses**: AoA reuses Core houses; WC adds Saurian and Star Alliance; later sets add Unfathomable,
     Ekwidon, Geistoid, Skyborn, Redemption, Ouboros, The Tide, Prophecy/Archon Power (some are not
     in `House.kt` yet — an unknown house makes the loader throw `Unknown house`).
   - **Card types**: `token creature`, `the tide`, `prophecy`, `archon power` appear in later sets.
   - **Mechanics not in the engine** (check `Known Gaps`): exalt, enrage, ward, bonus-icon variants,
     power counters, houses chosen from the deck's own list, etc. Each new mechanic belongs in
     Phase 2 style engine work *before* the cards that need it.
3. **Reprints share ids.** Of AoA's 370 cards 166 already exist in Core (WC 66, MM 60, DT 43), and
   `catalog_cards.id` is the primary key; keyteki also lists the same id several times inside one
   pack (e.g. MM has 13 repeated ids such as `exchange-officer`, variants/anomalies). Card scripts
   are keyed by the same id, so a reprint automatically reuses its existing script and its
   fidelity/verification — decide explicitly which printing's `expansion`/`number` the catalog row
   keeps (keyteki uses the latest `releaseDate`) and de-duplicate within the pack.

### 2. Seed the catalog **(code change)**

- `JsonCardCatalogLoader.loadPack("<Code>.json")` already reads any pack; `loadCoreSet()` is just
  `loadPack("CotA.json")`. IDs go through `sanitizeId` (lowercase, strip `[^a-z0-9-]`).
- `JpaCardCatalogAdapter.seedHousesAndExpansion()` hard-codes expansion 341, and
  `CatalogSeedConfig` seeds only when the catalog is empty. Generalize both: insert the expansion
  rows from `expansions.json`, make seeding incremental (insert ids that are not present yet), and add
  any new houses. Deck import rejects decks whose expansion has no catalog cards
  (`MasterVaultDeckParser`: "Expansion N is not supported"), so a seeded expansion is what makes
  decks of that set importable.
- Tests build their own library from `JsonCardCatalogLoader` (`engine/support/CoreCardLibrary.kt`); extend it
  with a per-set variant that loads the new pack as well.

### 3. Generate the card scripts

1. `tools/generate-core-cards.py` is Core-specific (`BASE/packs/CotA.json` in, `CoreSetDslCards.kt`
   out). Parametrize pack code and output file (`<Set>SetDslCards.kt`) and keep one registered map per set;
   register it in `buildCoreCardLibrary` (`infrastructure/config/CardLibraryConfig.kt`) **before** the
   manual scripts so manual overrides still win. Skip ids already implemented by an earlier set.
2. Reuse generator rules first — they scale to every set. Only extend the generator when a text
   pattern repeats; hand-write a `CardScript` (in a `<Set>Cards.kt`) for one-offs.
3. **Do not run the generator blindly over hand-curated files**: regenerating `CoreSetDslCards.kt`
   once clobbered hand patches. Generate into the new set's file, diff, and keep manual overrides in
   separate lists (see *Known Gaps* #11).
4. Unimplemented text must be `PARTIAL`/`APPROXIMATED`, never silently `FULL`/vanilla.

### 4. Measure coverage

- Run `python3 tools/fidelity-inventory.py` with `--pack ~/work/keyteki/master-vault-data/packs/<Code>.json`
  and `--keyteki-cards ~/work/keyteki/server/game/cards/<NN>-<Set>` (it currently assumes Core files and
  the Core DSL — extend it for the new set's file and write `docs/fidelity/<set>-inventory.csv`).
  Rows show implementation source, declared level, blocking text patterns, keyteki features and
  pool impact (decks of that set's pool containing the card).
- Work in priority order (pool impact × cluster size), batches of ~40 cards (PLAN.md Phase 3).

### 5. Verify against keyteki (parity tests)

1. Port the set's specs with `node tools/port-keyteki-specs.mjs` (`SPEC_DIR` is hard-wired to
   `01-Core`; pass or add the set directory). Generated skeletons fail with `error("TODO")` until you fill
   in expectations.
2. Write parity tests under `src/test/kotlin/.../engine/cota/` using the `Scenario` DSL; quote the
   keyteki spec name in the test.
3. Add a card id to `src/main/resources/fidelity/verified-cards.txt` **only when every portable spec is
   ported and passing**; document specs that cannot be ported yet as `BLOCKED` comments there.
   Declared-`FULL` cards without it are reported as `UNVERIFIED` (score 60).
4. Make `CardScriptRegistrationTest` and `VerifiedCardRegistryTest` cover the new set (they load the Core
   catalog today), so ids with typographic apostrophes or accents cannot silently miss their script.

### 6. Pool and ladder

1. Create one pool per set (`POST /api/pools/<setkey>/decks` or `<setkey>-pool.txt`) with hand-tiered
   decks spanning S+ … Z; import with delays to avoid Master Vault 429s.
2. Save the tier labels as `docs/deck-pool/<set>-tiers.json` (`{deckUuid: tier}`, S+=6 … Z=0) and a pool
   tracker `docs/deck-pool/<set>.md` (import status only, no scores).
3. Run a round-robin ladder (30 matches per pairing, fixed `baseSeed`), then
   `python3 tools/ladder-report.py <batchUuid> --tiers docs/deck-pool/<set>-tiers.json --out docs/deck-pool/<set>-ladder.md`
   for the standings and `POST /api/mass-simulations/{uuid}/tier-validation` for Spearman / pairwise
   agreement. Ratings are pool-relative: scores from different sets are not comparable (*Known Gaps* #7).
4. Compare ladders over time with `tools/ladder-compare.py` (comparison files are stamped `yyyyMMdd__HH_mm_ss-<name>.txt` with the new batch's completion time) and track the verified-fidelity of the
   cards actually played (`fidelity.cards` in the batch details).

### 7. Done checklist

- [ ] Pack data validated; houses / card types / mechanics gap list written down
- [ ] Catalog seeded incrementally; decks of the set import
- [ ] Set DSL file generated and registered; reprints reuse existing scripts
- [ ] Inventory CSV committed; no ability-text card left as unlisted `NoEffect`
- [ ] Parity tests + `verified-cards.txt` for the priority batch; registration tests extended
- [ ] Pool + tiers + ladder generated; tier validation recorded
- [ ] `AGENTS.md` (DSL coverage, tests, Known Gaps) updated

## How to Add a New Card Behavior

1. If the text fits the DSL, add parsing to `tools/generate-core-cards.py` and regenerate `CoreSetDslCards.kt`.
2. If the DSL cannot express it cleanly, add a manual `CardScript` in `domain/engine/cards/CoreCards.kt` and register it in `infrastructure/config/CardLibraryConfig.kt`.
3. Add a unit test in `src/test/kotlin/com/aember/analyzer/engine/`.
4. Run `./gradlew test` before committing.

## Notes for Continuity

- Do **not** commit unless explicitly asked.
- The generator is Python 3 and reads from `~/work/keyteki/master-vault-data/packs/CotA.json`. If that path changes, update `tools/generate-core-cards.py` and `application.yml`.
- The project is intentionally pragmatic: cards with unimplemented text are registered as `PARTIAL` so they still contribute to the fidelity score rather than being silently `VANILLA`.
- Fidelity is match-dependent. A single seed may not represent overall coverage; use multiple seeds or the mass-simulation harness when evaluating progress.
