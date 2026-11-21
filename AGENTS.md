# Agent Handoff — aember-analyzer

Last updated: 2026-11-20

## Project Goal

Build a Kotlin + Spring Boot + PostgreSQL service that imports KeyForge decks, simulates AI-vs-AI matches, and produces deck performance ratings. The source of truth is the neighbouring **keyteki** repository at `~/work/keyteki/master-vault-data` plus the docs in `docs/`.

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
- `GET /api/mass-simulations/{uuid}` — get batch details plus aggregate results.
- `GET /api/mass-simulations/{uuid}/matches` — list per-match results.
- `GET /api/mass-simulations/{uuid}/matches/{id}/logs` — get the debug log + fidelity log for one match.

Batch status values: `PENDING`, `RUNNING`, `COMPLETED`, `FAILED`.
Results and logs are stored on disk under `.simulations/` (gitignored), not in Postgres.

### curl Examples

```bash
# Import a deck from the Master Vault
curl -X POST http://localhost:8080/api/decks/import/{master-vault-uuid}

# List imported decks
curl "http://localhost:8080/api/decks"

# Filter decks by house and expansion
curl "http://localhost:8080/api/decks?house=Dis&expansion=341"

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

### DSL / Card Coverage

- `domain/engine/cards/CoreSetDslCards.kt` contains ~368 of 370 Core cards generated from `tools/generate-core-cards.py`.
- Manual overrides live in `domain/engine/cards/CoreCards.kt`: `BadPenny`, `Anger`, `GatewayToDis`, `Drumble`, `EtherSpider`, `ShadowSelf`, `SpeedSigil`, `ControlTheWeak`, `Miasma`, `TendrilsOfPain`, `Arise`, `Silvertooth`, `YxilxDominator`.
- `CardScript` exposes static-ability hooks so generic engine code never checks card names:
  - `cardEntersReady()` — e.g. Speed Sigil, Silvertooth.
  - `cardEntersStunned()` — e.g. Yxilx Dominator.
  - `redirectsNeighborDamage()` — e.g. Shadow Self.
  - `capturesOpponentAmberGains()` — e.g. Ether Spider.
  - `protectsNeighbors()` — used for placement; e.g. Shadow Self (also `taunt` keyword is checked directly).
  - `modifyFightDamage(...)` — e.g. Ether Spider, Shadow Self.
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
  - `Sequence`, `Conditional`
- Supported conditions in `domain/engine/dsl/Condition.kt`:
  - `OpponentHasMoreAmber`, `SelfHasMoreAmber`, `OpponentHasFewerCreatures`, `SelfHasFewerCreatures`
  - `OpponentAmberAtLeast(amount)`, `OpponentAmberAtMost(amount)`, `OpponentAmberExactly(amount)`, `SelfAmberAtLeast(amount)`
  - `StealSucceeded`, `DamageDestroyedTarget`
  - `OpponentForgedKeyLastTurn`
  - `Always`
- Supported creature selectors in `domain/engine/dsl/Target.kt`:
  - `FriendlyCreature`, `EnemyCreature`, `AnyCreature`
  - `FriendlyCreatureWithTrait`, `EnemyCreatureWithTrait`, `CreatureWithKeyword`
  - `FriendlyCreatureWithHouse`, `OtherFriendlyCreatureWithHouse`
  - `CreatureAndNeighbors(base)`, `EachOtherUndamagedCreature`, `AllCreaturesOfChosenHouse`, `LastDamagedTarget`
- Play restrictions: `CardDefinition.playableIf(condition)` prevents a card from being generated as a legal play action when the condition is false.

### Fidelity System

- Every card played/used/destroyed logs a `FidelityLevel`:
  - `FULL` — all effect sentences mapped
  - `PARTIAL` — mapped simple parts or text present but not fully implemented
  - `APPROXIMATED` — complex interaction ignored
  - `VANILLA` — truly vanilla card
- `FidelityReportService` computes a match score, level distribution, and lowest-fidelity cards.
- Default threshold is **70%** (`aember.simulation.fidelity-threshold`). Simulations below it log a warning.
- Recent sample matches report ~75–76% fidelity; after the card-improvement pass the Mehitabel vs Galilee matchup averages ~91.3%.

### Tests

- `GameEngineTest` — smoke tests for full matches.
- `KeywordTest` — unit tests for `elusive`, `skirmish`, `hazardous`, `assault`, `taunt`, `alpha`, `omega`.
- `EffectAndTriggerTest` — unit tests for all recently added effects and triggers.
- `FidelityReportServiceTest` — fidelity score/threshold tests.
- `PhaseOneCardsTest` — unit tests for `Control the Weak`, `Library Access`, `Invasion Portal`, `Hypnotic Command`, `Neutron Shark`, `Bouncing Deathquark`, `Dr. Escotera`, `Smiling Ruth`, `Miasma`, `Tendrils of Pain`, `Arise`, `Silvertooth`, `Yxilx Dominator`, and protection-aware placement.
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

| Area | Files |
|------|-------|
| Engine entry point | `domain/engine/GameEngine.kt` |
| Action execution | `domain/engine/action/ActionExecutor.kt`, `ActionGenerator.kt` |
| DSL effects | `domain/engine/dsl/Effect.kt`, `EffectResolver.kt`, `CardDsl.kt`, `DslCardScript.kt` |
| Card definitions | `domain/engine/cards/CoreSetDslCards.kt`, `CoreCards.kt` |
| Generator | `tools/generate-core-cards.py` |
| Fidelity | `domain/engine/fidelity/FidelityLevel.kt`, `CardFidelity.kt`, `FidelityReportService.kt` |
| AI | `domain/ai/TacticalAI.kt`, `AISettings.kt`, `GreedyAI.kt`, `RandomAI.kt` |
| Services | `application/SimulationService.kt`, `MassSimulationService.kt`, `MassSimulationAsyncExecutor.kt`, `DeckImportService.kt` |
| Storage | `infrastructure/persistence/FileSystemMassSimulationRepository.kt` |
| CLI/REST | `adapter/cli/AemberCliRunner.kt`, `adapter/rest/SimulationController.kt`, `MassSimulationController.kt`, `DeckController.kt` |
| Tests | `src/test/kotlin/com/aember/analyzer/engine/*`, `src/test/kotlin/com/aember/analyzer/simulation/*` |

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

1. **Static / ongoing effects** are the biggest fidelity gap. Cards like `Bulwark`, `Staunch Knight`, `Tentacus`, `Lash of Broken Dreams`, `Banner of Battle`, etc. only score `PARTIAL` because their continuous auras are not implemented.
2. **Conditional card texts**: `if`, `for each`, `until`, `may`, `while`, `after`/`before` outside of fight/reap triggers.
3. **Precise targeting**: choose by house, trait, flank, neighbor, or specific creature. Currently many cards use broad selectors.
4. **Omni / Action timing**: Omni is implemented as a once-per-turn action ability, but the “as if active house” rule only works for artifacts/creatures with Omni; cross-house action abilities on non-Omni cards are not supported.
5. **Deploy**: only flank placement is enforced; no in-battleline placement yet.
6. **Upgrades**: can be attached, but continuous upgrade effects are not processed.
7. **Deck scoring** (SoS / Bradley-Terry) is not started.
8. **More sets** beyond Core (`CotA`) are not seeded.

## How to Add a New Card Behaviour

1. If the text fits the DSL, add parsing to `tools/generate-core-cards.py` and regenerate `CoreSetDslCards.kt`.
2. If the DSL cannot express it cleanly, add a manual `CardScript` in `domain/engine/cards/CoreCards.kt` and register it in `infrastructure/config/CardLibraryConfig.kt`.
3. Add a unit test in `src/test/kotlin/com/aember/analyzer/engine/`.
4. Run `./gradlew test` before committing.

## Notes for Continuity

- Do **not** commit unless explicitly asked.
- The generator is Python 3 and reads from `~/work/keyteki/master-vault-data/packs/CotA.json`. If that path changes, update `tools/generate-core-cards.py` and `application.yml`.
- The project is intentionally pragmatic: cards with unimplemented text are registered as `PARTIAL` so they still contribute to the fidelity score rather than being silently `VANILLA`.
- Fidelity is match-dependent. A single seed may not represent overall coverage; use multiple seeds or the mass-simulation harness when evaluating progress.
