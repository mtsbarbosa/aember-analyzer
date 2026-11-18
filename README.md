# aember-analyzer

A Kotlin + Spring Boot + PostgreSQL service that imports **KeyForge** decks and will eventually
simulate thousands of AI-vs-AI matches to produce deck performance ratings.

The project uses the rule knowledge base in `docs/` and the card data in the neighbouring
**keyteki** repository (`~/work/keyteki`) as its source of truth.

---

## What it does today

- Imports and persists KeyForge decks from the Master Vault API or a local JSON file.
- Seeds the PostgreSQL catalog with Core set (`CotA`) card definitions from keyteki pack data.
- Queries imported decks by name, house, expansion, and UUID.
- Runs a Kotlin port of the KeyForge rule engine for the Core set.
- Simulates AI-vs-AI matches with deterministic seeds.
- Exposes REST endpoints and CLI commands for deck import, deck queries, and match simulation.
- Tracks implementation fidelity of each card against real KeyForge rules and produces a match-level fidelity score.
- Runs unit and integration tests with embedded PostgreSQL.

## What is coming

- More hand-ported Core cards (currently most cards run as vanilla or partially).
- Configurable, humanized AI player profiles.
- Mass-match simulation harness with seat alternation and batch reporting.
- Deck rating reports (matchup matrix + strength-of-schedule win rate).

---

## Tech stack

| Layer | Technology |
| --- | --- |
| Language | Kotlin 1.9.25, JDK 21 |
| Framework | Spring Boot 3.3 |
| Build | Gradle 8.5 (wrapper included) |
| Database | PostgreSQL 16 |
| Migrations | Flyway |
| Tests | JUnit 5, AssertJ, embedded PostgreSQL (`io.zonky.test:embedded-postgres`) |
| Container runtime | Docker or Podman (both work) |

---

## Prerequisites

- JDK 21
- Podman or Docker with compose support
- Access to the keyteki repo at `~/work/keyteki` (configurable)

If you are using **Podman**, make sure `podman-compose` is installed. The `docker-compose.yml`
uses the fully-qualified image name `docker.io/library/postgres:16-alpine`, so it works with
both runtimes.

---

## Quick start

### 1. Start PostgreSQL

```bash
# Podman
podman-compose up -d

# Docker
docker compose up -d
```

The database is available at `localhost:54320` with user `aember`, password `aember`,
database `aember`.

### 2. Run the application

```bash
./gradlew bootRun
```

The app starts on `http://localhost:8080` and automatically seeds the Core set catalog if the
`catalog_cards` table is empty.

### 3. Run tests

```bash
./gradlew test
```

Tests use an embedded PostgreSQL instance, so they do not require a running container.

---

## Commands

### Import a deck from a local Master Vault JSON file

```bash
./gradlew bootRun --args "import-deck-file src/test/resources/decks/cota-deck.json"
```

### Import a deck from the Master Vault API

```bash
./gradlew bootRun --args "import-deck <master-vault-deck-uuid>"
```

Example:

```bash
./gradlew bootRun --args "import-deck eb5d4c4a-5957-4276-ab9a-0d1b19f42e81"
```

### Simulate a match between two imported decks

```bash
./gradlew bootRun --args "simulate <deck-uuid-a> <deck-uuid-b> [seed] [aiA] [aiB]"
```

Example:

```bash
./gradlew bootRun --args "simulate eb5d4c4a-5957-4276-ab9a-0d1b19f42e81 aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee 42 tactical aggressive"
```

### REST API

| Method | Endpoint | Description |
| --- | --- | --- |
| POST | `/api/decks/import/{uuid}` | Import a deck by Master Vault UUID |
| GET | `/api/decks` | List imported decks (optional filters below) |
| GET | `/api/decks?name={substring}` | Filter decks by name (case-insensitive substring) |
| GET | `/api/decks?house={house}` | Filter decks that include a house (e.g., `Dis`, `Untamed`) |
| GET | `/api/decks?expansion={id}` | Filter decks by expansion id (e.g., `341` for CotA) |
| GET | `/api/decks/{uuid}` | Get full details for a single deck (cards included) |
| GET | `/api/simulate/{deckA}/{deckB}?seed=42&aiA=tactical&aiB=aggressive` | Run a single simulation (slim summary by default); `aiA`/`aiB` can be `tactical`, `greedy`, `random`, `aggressive`, `economic` |
| GET | `/api/simulate/{deckA}/{deckB}?seed=42` with `X-Debug: true` | Run a simulation and return the full game state + fidelity log + match statistics |

Examples:

```bash
# Import a deck
curl -X POST http://localhost:8080/api/decks/import/eb5d4c4a-5957-4276-ab9a-0d1b19f42e81

# List all imported decks
curl "http://localhost:8080/api/decks"

# Filter by name, house, or expansion
curl "http://localhost:8080/api/decks?name=shadow"
curl "http://localhost:8080/api/decks?house=Dis"
curl "http://localhost:8080/api/decks?expansion=341"
curl "http://localhost:8080/api/decks?house=Untamed&expansion=341"

# Get a single deck
curl "http://localhost:8080/api/decks/eb5d4c4a-5957-4276-ab9a-0d1b19f42e81"

# Simulate a match (slim summary by default)
curl "http://localhost:8080/api/simulate/eb5d4c4a-5957-4276-ab9a-0d1b19f42e81/aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee?seed=42"

# Simulate with different AI profiles
curl "http://localhost:8080/api/simulate/eb5d4c4a-5957-4276-ab9a-0d1b19f42e81/aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee?seed=42&aiA=aggressive&aiB=economic"

# Full verbose output for debugging
curl -H "X-Debug: true" "http://localhost:8080/api/simulate/eb5d4c4a-5957-4276-ab9a-0d1b19f42e81/aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee?seed=42"
```

---

## Configuration

All configuration lives in `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:54320/aember
    username: aember
    password: aember

keyteki:
  data:
    path: /home/mateus/work/keyteki/master-vault-data

aember:
  simulation:
    fidelity-threshold: 70.0
```

- `spring.datasource.*` points at the PostgreSQL container.
- `keyteki.data.path` is where the `master-vault-data/packs/*.json` files live.
- `aember.simulation.fidelity-threshold` is the minimum acceptable match fidelity score
  (0-100). Simulations below this score log a warning.

Test-specific overrides are in `src/test/resources/application-test.yml`.

---

## Project structure

The project follows **Hexagonal Architecture** (ports & adapters):

```text
src/main/kotlin/com/aember/analyzer/
  domain/            # Pure business logic
    model/           # Deck, CatalogCard, enums, etc.
    engine/          # Rule engine (turn structure, actions, events)
    ai/              # AI policy, profiles, humanization
    simulation/      # Match harness, batch orchestration, scoring
    ports/           # Driven and driving ports
  application/       # Use cases / application services
  infrastructure/    # Adapters: DB, HTTP, config, logging
  adapter/           # Incoming adapters: REST controllers, CLI runner
```

Key components:

- `CardCatalogPort` / `JpaCardCatalogAdapter` — canonical card catalog.
- `DeckImportPort` / `MasterVaultDeckImportAdapter` — Master Vault HTTP import.
- `DeckRepository` / `JpaDeckRepository` — deck persistence.
- `AemberCliRunner` — CLI entry point.
- `CatalogSeedConfig` — seeds Core cards on startup.

---

## Database migrations

Migrations are in `src/main/resources/db/migration/`:

- `V1__catalog.sql` — `expansions`, `houses`, `catalog_cards`.
- `V2__decks.sql` — `decks`, `deck_cards`, `deck_houses`.

Run migrations automatically on startup via Flyway.

---

## Development workflow

### Build

```bash
./gradlew build -x test   # compile and package
./gradlew build           # compile, test, package
```

### Format / static analysis

The project uses Kotlin standard formatting. Run the compiler checks with:

```bash
./gradlew compileKotlin compileTestKotlin
```

### Adding a new set to the catalog

1. Place the pack JSON in the keyteki `master-vault-data/packs/` directory.
2. Add the expansion to `JpaCardCatalogAdapter.seedHousesAndExpansion()`.
3. Update `CatalogSeedConfig` or create a one-off command to import the new pack.

For the MVP, only **Core / Call of the Archons** is seeded automatically.

### Adding hand-ported cards

Cards for the Core set live under `domain/engine/cards/` as Kotlin classes that implement
card-specific actions. Each card is wired into the engine by its slugified title (e.g.
`anger`, `bad-penny`).

### Engine status

The engine now supports the Core-set keywords, zones, and a growing set of card texts:

- **Keywords**: `alpha`, `omega`, `elusive`, `skirmish`, `taunt`, `hazardous`, `assault`, `deploy`
  (placement is always flank for now).
- **Zones**: `archives` (pickup in house phase), `purged` (remove-from-game zone), `chains`
  (reduce draw and shed each refill).
- **Hand-ported Core cards**: ~368 cards are defined in
  `domain/engine/cards/CoreSetDslCards.kt` via a declarative effect DSL. The generator lives at
  `tools/generate-core-cards.py`. Cards with text we cannot yet resolve are registered as
  `PARTIAL` so they still contribute to the fidelity score; only truly vanilla cards score
  `VANILLA`.
- **Triggers supported**: `Play`, `Reap`, `Fight`, `Before Fight`, `After Fight`, `Before Reap`,
  `After Reap`, `Destroyed`, `Action` (creatures and artifacts), and `Omni`.
- **Fidelity tracking**: every card played/used/destroyed logs a `FidelityLevel` (`FULL`,
  `PARTIAL`, `APPROXIMATED`, `VANILLA`). `FidelityReportService` computes a match score and
  the lowest-fidelity cards seen. The default threshold is 70% and is configurable in
  `application.yml`.
- **Manual overrides**: `bad-penny`, `anger`.

**Still not implemented**: tide, tokens, prophecies, upgrades that grant continuous abilities,
constant/ongoing effects (e.g. static armor/power changes, key-cost changes), conditional
"if/until/for each" card texts, and precise target selection (house, trait, flank, neighbor).

---

## Testing

### Unit tests

Unit tests cover pure domain logic and parsing. They do not require a database.

Example:

```bash
./gradlew test --tests "com.aember.analyzer.deck.MasterVaultDeckParserTest"
```

### Integration tests

Integration tests spin up an embedded PostgreSQL instance and exercise the Spring context,
Flyway migrations, and JPA repositories.

Example:

```bash
./gradlew test --tests "com.aember.analyzer.catalog.CatalogSeedIntegrationTest"
./gradlew test --tests "com.aember.analyzer.deck.DeckImportIntegrationTest"
./gradlew test --tests "com.aember.analyzer.deck.DeckQueryIntegrationTest"
./gradlew test --tests "com.aember.analyzer.deck.DeckControllerWebTest"
```

### Engine tests

```bash
./gradlew test --tests "com.aember.analyzer.engine.GameEngineTest"
```

### Test fixtures

- `src/test/resources/decks/cota-deck.json` — a real Core set Master Vault deck response.
- `src/test/resources/decks/cota-deck-2.json` — the same list with a different uuid/name for
  simulation tests.

---

## Troubleshooting

### `docker: command not found`

Install Docker or use Podman with `podman-compose`. The compose file is compatible with both.

### `FATAL: role "aember" does not exist`

The container was probably started with a different configuration. Remove the old volume and
recreate it:

```bash
podman-compose down -v
podman-compose up -d
```

### `Expansion 341 is not supported`

The catalog is empty. Make sure `keyteki.data.path` points at the correct directory and the
app has seeded the Core set. The seed runs automatically in the default profile.

---

## Knowledge base

This repo still contains the original agent-ready KeyForge spec in `docs/`:

- `docs/01-core-rules/` — turn structure, phases, combat, abilities, keywords.
- `docs/02-card-mechanics/` — card effect categories.
- `docs/03-traits-and-pips/` — traits and enhancement pips.
- `docs/04-sets/` — per-set summaries.
- `docs/05-decks-and-servers/` — Master Vault import details.
- `docs/07-card-database/` — per-card reference.
- `docs/decision-tree/ai-player-stage-decisions.md` — AI design reference.

See the original directory map below.

```text
docs/
  01-core-rules/       Vanilla game rules
  02-card-mechanics/   Card effect categories & patterns
  03-traits-and-pips/  Trait families, pips, enhancements
  04-sets/             One doc per KeyForge set
  05-decks-and-servers/ Master Vault import & deck data
  06-sources/          Traceability back into keyteki
  07-card-database/    Card-by-card reference per set
  decision-tree/       Agent-oriented cheat sheets
  tools/               generate-card-db.mjs
```

---

## Roadmap

1. ✅ Project skeleton, Postgres, Flyway, tests.
2. ✅ Core set catalog + deck import/persistence.
3. ✅ Basic Kotlin rule engine: phases, combat, actions, simple AI.
4. ✅ Core keywords + archives/chains/purged support.
5. ✅ Core cards hand-ported via DSL (~95 cards).
6. ✅ Fidelity tracking + configurable threshold (default 70%).
7. ✅ Expanded DSL to cover most Core cards (~368 of 370).
8. 🔄 Implement static/ongoing effects and remaining complex interactions.
7. ⏳ Configurable AI profiles + humanization layer.
8. ⏳ Mass-match simulation harness + deterministic batch runner.
9. ⏳ Reporting, deck scoring, and card attribution.

---

## License

This project is a private research/experimentation codebase. License terms to be decided.
