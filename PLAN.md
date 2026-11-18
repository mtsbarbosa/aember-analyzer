# Project Plan: KeyForge Mass-Match Simulator (`aember-analyzer`)

> **Current status (2026-11-17):** Core engine, deck import, REST/CLI API, and a baseline TacticalAI are implemented and tested. Matches end naturally by keys. The project is ready for mass-simulation harness and deck scoring work.

## 1. Objective

Build a self-contained Kotlin + Spring Boot + PostgreSQL (Docker) service that:

1. Imports and persists KeyForge decks from Master Vault / keyteki data.
2. Simulates thousands of KeyForge matches between decks using an AI-driven player model.
3. Rates deck performance via matchup matrices, Bradley-Terry/Elo strength estimates, and
   per-card/per-archetype attribution.
4. Emits debug logs and structured reports so results are reproducible and auditable.

Architecture is **Hexagonal (Ports & Adapters)**. Gradle build with unit and integration tests.

---

## 2. Source of Truth for Rules

Rules, card database, and engine semantics are reverse-documented in `aember-analyzer/docs/` and
verified against `~/work/keyteki`.

- Core rules: `docs/01-core-rules/`
- Card mechanics: `docs/02-card-mechanics/`
- Traits/pips: `docs/03-traits-and-pips/`
- Sets: `docs/04-sets/`
- Deck import / Master Vault API: `docs/05-decks-and-servers/`
- Per-card reference: `docs/07-card-database/`
- AI decision guide: `docs/decision-tree/ai-player-stage-decisions.md`

The simulation harness will drive a Kotlin port of the rule engine. Where possible, card scripts
and pack data will be ingested from `keyteki/master-vault-data/packs/*.json` and `keyteki/server/game/cards/`
to avoid re-authoring hundreds of card implementations by hand.

---

## 3. Technology Stack

| Layer | Choice |
| --- | --- |
| Language | Kotlin 2.x |
| Framework | Spring Boot 3.x (Web + Data JPA/ JDBC) |
| Build | Gradle (Kotlin DSL) |
| Database | PostgreSQL 16, run via Docker Compose locally |
| Migrations | Flyway |
| Tests | JUnit 5 + Kotest/AssertJ, Testcontainers for integration tests |
| Serialization | Jackson / kotlinx.serialization |
| Logging | SLF4J + Logback; structured JSON for simulation events |
| Async sims | Virtual threads (JDK 21) + `CompletableFuture` or coroutines |
| Containerization | Docker + docker-compose.yml for Postgres |

---

## 4. Hexagonal Architecture

```text
src/
  main/kotlin/com/aember/analyzer/
    domain/               # Pure business logic, no framework deps
      model/              # Deck, Card, GameState, Player, MatchResult, etc.
      engine/             # KeyForge rule engine port interfaces + core implementations
      ai/                 # AI policy, profiles, search, humanization
      simulation/         # Match harness, batch orchestration, scoring
      ports/              # Driven + driving ports
    application/          # Use cases / services (Spring-managed)
    infrastructure/       # Adapters: DB, HTTP (Master Vault), config, logging
    adapter/              # Incoming adapters: REST, CLI runners, scheduled jobs
  test/
    unit/                 # Domain + engine unit tests
    integration/          # DB + Spring context + Testcontainers
```

### Core ports

- `DeckImportPort` — fetch a deck by UUID from Master Vault or local JSON.
- `DeckRepository` — persist/retrieve decks, cards, houses, enhancements.
- `CardCatalogPort` — load canonical card definitions (from keyteki pack JSON).
- `GameEnginePort` — run one match given two decks, seating, seed, and two AI profiles.
- `SimulationReportPort` — write per-game / per-turn / per-decision events.
- `DeckRatingPort` — compute ratings from a finished batch.

---

## 5. Domain Model (MVP subset)

- `Deck`
  - uuid, name, expansionId, houses (3), cards (36 + non-deck), tokenCard, prophecyCards.
- `CardInstance`
  - catalogCardId, house (effective), maverick/anomaly flags, enhancements, count.
- `CatalogCard`
  - id, name, number, house, type, rarity, amber, armor, power, text, keywords, traits, expansion.
- `Match`
  - id, deckA, deckB, seatA, seatB, seed, profileA, profileB, winner, endCause, rounds.
- `TurnLog`
  - matchId, round, player, activeHouse, amber, keys, handSize, boardSummary.
- `DecisionLog`
  - matchId, turn, stage, chosenOption, evalDelta, policyParams (for attribution).
- `DeckScore`
  - deckId, poolId, games, wins, losses, draws, rating, confidence interval.

---

## 6. Engine Strategy

### Option A: Kotlin port of keyteki rules (recommended for scoring)
Port the phase pipeline and effect/action DSL to Kotlin, consuming the same card script patterns
where feasible. This gives full control and reproducibility without a Node runtime.

### Option B: Drive keyteki Node engine in-process
Spawn the keyteki game engine headlessly and feed decisions via stdin/stdout. Faster to bootstrap,
but couples scoring to a JavaScript runtime and makes hexagonal isolation harder.

**Decision: Option A for the long-term product.**

### Engine scope (phased)

1. **MVP engine** — support Core set cards + basic keywords (elusive, skirmish, taunt, deploy,
   alpha, omega, hazardous, assault). Implement turn structure, houses, combat, amber, keys,
   archives, purge, chains, tide.
2. **Expansion waves** — add DT (tide), WoE (tokens), GR/PV (prophecies), then remaining sets.
3. **Full card coverage** — ingest `07-card-database/` card verbs and implement per-card actions
   incrementally.

---

## 7. Deck Import & Storage

### Import flows

1. **By Master Vault UUID**
   - `POST /decks/import/{uuid}`
   - Calls `https://www.keyforgegame.com/api/decks/{uuid}/?links=cards`.
   - Parses same shape as keyteki `DeckService.parseDeckResponse()`.
   - Validates card ids against local catalog; rejects if unknown expansion/card.
   - Stores deck + cards + houses.

2. **Bulk import from keyteki local data**
   - Read `master-vault-data/packs/*.json` into `CatalogCard` table.
   - Optional: import a folder of saved deck JSONs for local-only pools.

### Postgres schema (Flyway migrations)

- `expansions`, `houses`
- `catalog_cards` (canonical card definitions)
- `decks` (Master Vault metadata)
- `deck_cards` (per-deck card entries with overrides/enhancements)
- `deck_houses`
- `simulation_batches`, `matches`, `turn_logs`, `decision_logs`
- `deck_scores`

---

## 8. AI Player Model

One `AIPolicy` interface, multiple implementations/profiles.

### Policy components

- **Evaluator** — `State -> Double` scoring keys, amber velocity, board, hand, chains, etc.
- **Search** — lightweight MCTS / beam search over main-phase action sequences.
- **Humanization** — parameters that introduce plausible suboptimality without cheating.
- **Persona** — static eval biases (aggressive, build, amber, chaos).

### Humanization parameters

| Parameter | Meaning |
| --- | --- |
| `searchDepth` | Main-phase lookahead depth |
| `searchTimeMs` | Budget per big decision |
| `housePoolSize` | Candidate houses considered |
| `mistakeRate` | Chance to pick 2nd/3rd best line |
| `evalNoise` | Gaussian noise on eval scores |
| `greed` | Amber-velocity bias |
| `riskAppetite` | Chain/attrition trade acceptance |
| `persona` | Aggressive / build / amber / chaos weights |

### Difficulty bands

- Novice, Casual, Expert, Strong (reference).
- Each band is just a parameter preset; no special-case logic.

### Non-determinism

- Random seed per match controls shuffle and AI sampling.
- Same `(deckA, deckB, seed, profiles)` produces identical result for reproducibility.
- Across a batch, seeds are generated deterministically from a global seed + index.

---

## 9. Simulation Harness

### Batch workflow

1. User defines:
   - target deck(s) to rate,
   - opponent pool (deck IDs or meta archetypes),
   - AI profiles for both sides,
   - games per matchup,
   - seat alternation policy,
   - global seed.
2. Harness schedules matches (parallel using virtual threads).
3. Each match runs to completion via `GameEnginePort`.
4. Events written to `matches`, `turn_logs`, `decision_logs`.
5. Post-batch: compute `DeckScore` and matchup matrix.

### Match termination causes

- `KEYS` — a player forged 3 keys.
- `TIMEOUT_TIEBREAKER` — round cap reached, tiebreaker ladder applied.
- `TIMEOUT_DRAW` — round cap, tiebreaker still tied.
- `CONCESSION` / `DISCONNECT` (not used in AI sims).
- `STALL` — hard cap on passes/no-op turns.

### Seat & seed handling

- For each pair `(deckA, deckB, i)`, generate two matches with swapped seats using derived seeds:
  `seed = hash(globalSeed, deckA.uuid, deckB.uuid, i, seat)`.
- This removes first-player bias from aggregate scores.

---

## 10. Reporting & Debug Logs

### Structured logs

- `simulation_batch_started`
- `match_started`
- `turn_summary`
- `decision_made`
- `match_ended`
- `simulation_batch_ended`

### Reports generated

- Matchup matrix CSV/JSON.
- Per-deck rating with confidence intervals.
- Turn-level timeline for any selected match.
- Decision attribution: which cards/actions produced the highest eval deltas.
- Deck feature regression: which archetype features correlate with wins.

### Debuggability

- Replay any match from `(seed, deck pair, profiles)`.
- Per-turn board snapshots stored as JSON.
- Human-readable text log alongside structured events.

---

## 11. Testing Strategy

### Unit tests

- Engine rules: phase order, key forge, combat damage, chains, mulligan.
- Evaluator: verify scalar terms behave as expected on synthetic states.
- AI policy: verify profiles produce different action distributions.
- Deck import: parsing of sample Master Vault JSON.

### Integration tests

- Testcontainers PostgreSQL for Flyway migrations and repositories.
- Spring context smoke test.
- End-to-end: import two real decks, run one match, assert match ends with a winner.

### Test data

- Commit small sample deck JSONs under `src/test/resources/decks/`.
- Use a minimal card catalog subset to keep tests fast.

---

## 12. Implementation Phases

### Phase 0 — Skeleton ✅

- Gradle project with Spring Boot, Flyway, embedded PostgreSQL tests.
- Docker Compose for Postgres.
- Hexagonal package layout + CI-friendly test tasks.

### Phase 1 — Catalog & Deck import ✅

- Ingest `master-vault-data/packs/*.json` into `catalog_cards`.
- Master Vault UUID import endpoint + persistence.
- REST endpoints: `POST /decks/import/{uuid}`, `GET /decks/{uuid}`, `GET /decks` with filters.
- Integration tests for import and storage.

### Phase 2 — Core engine ✅

- Turn structure: setup, key, house, main, ready, draw phases.
- Card types and actions: play, use, reap, fight, discard.
- Keywords: elusive, skirmish, taunt, deploy, alpha, omega, hazardous, assault.
- Core-set card DSL (~368/370 cards) + manual overrides.
- Zones: archives, purged, chains.
- Six-name rule.
- Unit tests for each rule area.

### Phase 3 — AI baseline ✅

- `GreedyAI` and `RandomAI` baselines.
- `TacticalAI` with heuristic scoring for reaping, fighting, action abilities, and house choice.
- Configurable `AISettings` with `default`, `aggressive`, and `economic` presets.
- AI decision rationale logged in verbose output.
- Per-match statistics (reaps, fights, abilities, plays, discards, keys forged).

### Phase 4 — Simulation harness & reports 🔄 (current focus)

- Batch scheduler, seat alternation, deterministic seeds.
- Database logging of matches/turns/decisions.
- Report generation: matrix, ratings, card attribution.
- Integration test: run 100 games between two decks and produce a report.

### Phase 5 — Expansion coverage & optimization

- Add tide, tokens, prophecies.
- Expand card coverage set by set.
- Performance tuning: parallelization, engine snapshots, virtual threads.
- Optional: MCTS search, opponent hand inference.

---

## 13. Open Decisions

1. **Engine port vs keyteki Node driver** — ✅ Option A (Kotlin port driven by internal DSL).
2. **Card script strategy** — ✅ Small DSL (`Effect.kt`) + Python generator for Core cards, manual overrides for complex interactions.
3. **Opponent pool for ratings** — fixed meta pool, random pool, or ladder of archetypes?
4. **Scoring scalar** — Bradley-Terry only, or also publish a simpler win-rate with strength-of-schedule?
5. **Web UI** — out of scope for MVP; keep REST/CLI only?

---

## 14. Success Criteria

- [x] Can import any legal Master Vault deck and store it.
- [x] Can run deterministic matches between two decks from a seed.
- [ ] Can run 1,000+ matches in a batch harness.
- [ ] Reports deck win rate, matchup matrix, and per-card attribution.
- [x] AI profiles (random, greedy, tactical with aggressive/economic presets) are selectable per player.
- [x] Integration tests pass with embedded PostgreSQL.
- [x] Debug replay reproduces any match from its seed.

---

## 15. Next Steps

1. Implement mass-simulation harness (Phase 4): schedule many matches, persist results, handle seat alternation.
2. Generate deck performance reports: win rate, matchup matrix, Bradley-Terry ratings.
3. Expand `TacticalAI` contextual evaluation (ongoing): static effects, precise targeting, action-ability value estimation.
4. Add more sets beyond Core (`CotA`).
5. Decide and implement opponent pool strategy for ratings (fixed meta, random, or ladder).
