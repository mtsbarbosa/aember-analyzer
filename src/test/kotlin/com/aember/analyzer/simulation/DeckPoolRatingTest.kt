package com.aember.analyzer.simulation

import com.aember.analyzer.IntegrationTest
import com.aember.analyzer.application.DeckPoolService
import com.aember.analyzer.application.SimulationService
import com.aember.analyzer.domain.model.Deck
import com.aember.analyzer.domain.model.DeckCard
import com.aember.analyzer.domain.model.DeckPool
import com.aember.analyzer.domain.model.DeckRef
import com.aember.analyzer.domain.model.House
import com.aember.analyzer.domain.model.MassSimulationBatch
import com.aember.analyzer.domain.model.MassSimulationBatchType
import com.aember.analyzer.domain.model.MassSimulationStatus
import com.aember.analyzer.domain.rating.DeckRating
import com.aember.analyzer.domain.ports.CardCatalogPort
import com.aember.analyzer.domain.ports.DeckPoolRepository
import com.aember.analyzer.domain.ports.DeckRepository
import com.aember.analyzer.infrastructure.persistence.JpaCardCatalogAdapter
import com.aember.analyzer.infrastructure.persistence.JsonCardCatalogLoader
import com.aember.analyzer.infrastructure.persistence.FileSystemMassSimulationRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.nio.file.Files
import java.time.Instant
import java.util.UUID

class DeckPoolRatingTest : IntegrationTest() {

    @Autowired
    private lateinit var deckRepository: DeckRepository

    @Autowired
    private lateinit var simulationService: SimulationService

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var catalog: CardCatalogPort

    @Autowired
    private lateinit var adapter: JpaCardCatalogAdapter

    @Autowired
    private lateinit var loader: JsonCardCatalogLoader

    private lateinit var poolRepo: InMemoryDeckPoolRepository
    private lateinit var massSimRepo: FileSystemMassSimulationRepository
    private lateinit var service: DeckPoolService
    private lateinit var tempDir: java.nio.file.Path

    @BeforeEach
    fun setUp() {
        if (catalog.loadAll().isEmpty()) {
            adapter.seedHousesAndExpansion()
            adapter.seed(loader.loadCoreSet())
        }
        tempDir = Files.createTempDirectory("pool-rating-test")
        poolRepo = InMemoryDeckPoolRepository()
        massSimRepo = FileSystemMassSimulationRepository(tempDir.toString(), objectMapper)
        service = DeckPoolService(
            deckRepository = deckRepository,
            poolRepository = poolRepo,
            massSimulationRepository = massSimRepo,
            simulationService = simulationService,
            seedDir = tempDir.toString()
        )
    }

    @Test
    fun `rates a deck against a pool with anchored ladder rating`() {
        val poolA = saveDeck("Pool Deck A")
        val poolB = saveDeck("Pool Deck B")
        val challenger = saveDeck("Challenger Deck")

        // Seed a stored ladder: poolA and poolB split their matches evenly.
        // A third deck in the ladder pool is not played by the challenger; the
        // anchor refit must still resolve its name from the ladder's deck pool.
        val ladderOnly = saveDeck("Ladder Only Deck")
        val batch = MassSimulationBatch(
            uuid = UUID.randomUUID().toString(),
            status = MassSimulationStatus.COMPLETED,
            batchType = MassSimulationBatchType.MULTI_DECK,
            aiA = "tactical",
            aiB = "tactical",
            matchCount = 4,
            deckPool = listOf(
                DeckRef(poolA.uuid, poolA.name),
                DeckRef(poolB.uuid, poolB.name),
                DeckRef(ladderOnly.uuid, ladderOnly.name)
            ),
            ai = "random",
            seatAlternation = true,
            completedAt = Instant.now()
        )
        massSimRepo.save(batch)
        massSimRepo.saveRatings(
            batch.uuid,
            listOf(
                deckRating(poolA.uuid, poolA.name, aemberScore = 50.0, bradleyTerry = 1.0),
                deckRating(poolB.uuid, poolB.name, aemberScore = 50.0, bradleyTerry = 1.0)
            )
        )
        // One match per seat per pairing; seat alternation over 4 matches.
        for (i in 0 until 4) {
            val swap = i % 2 == 1
            massSimRepo.saveResult(
                com.aember.analyzer.domain.model.MassSimulationMatchResult(
                    id = i + 1,
                    batchUuid = batch.uuid,
                    seed = 100L + i,
                    winnerIndex = if (i < 2) 0 else 1,
                    endCause = com.aember.analyzer.domain.engine.model.EndCause.KEYS,
                    turns = 10,
                    rounds = 10,
                    fidelityScore = 90.0,
                    statistics = emptyList(),
                    deckUuidA = if (swap) poolB.uuid else poolA.uuid,
                    deckUuidB = if (swap) poolA.uuid else poolB.uuid
                )
            )
        }

        service.addToPool("testpool", poolA.uuid)
        service.addToPool("testpool", poolB.uuid)

        val rating = service.rateDeckAgainstPool(
            poolKey = "testpool",
            deckUuid = challenger.uuid,
            matchesPerPairing = 2,
            baseSeed = 1,
            ai = "random"
        )

        assertThat(rating.poolKey).isEqualTo("testpool")
        assertThat(rating.deckUuid).isEqualTo(challenger.uuid)
        assertThat(rating.matches).isEqualTo(4) // 2 opponents x 2 matches
        assertThat(rating.failedMatches).isEqualTo(0)
        assertThat(rating.opponents).hasSize(2)
        rating.opponents.forEach { assertThat(it.matches).isEqualTo(2) }
        // Every opponent played the challenger: totals add up.
        assertThat(rating.opponents.sumOf { it.matches }).isEqualTo(4)
        assertThat(rating.anchorBatchUuid).isEqualTo(batch.uuid)
        assertThat(rating.bradleyTerry).isNotNull()
        assertThat(rating.aemberScore).isNotNull()
        assertThat(rating.aemberScore).isFinite()
        assertThat(rating.percentile).isBetween(0.0, 100.0)

        // The rating was persisted for history.
        val history = service.ratingHistory("testpool", challenger.uuid)
        assertThat(history).hasSize(1)
        assertThat(history[0].deckUuid).isEqualTo(challenger.uuid)

        // The pool was persisted too.
        assertThat(poolRepo.findByKey("testpool")!!.deckUuids)
            .containsExactlyInAnyOrder(poolA.uuid, poolB.uuid)
    }

    @Test
    fun `rating without stored ladder reports raw stats only`() {
        val poolDeck = saveDeck("Lone Pool Deck")
        val challenger = saveDeck("Unanchored Challenger")

        service.addToPool("noladder", poolDeck.uuid)

        val rating = service.rateDeckAgainstPool(
            poolKey = "noladder",
            deckUuid = challenger.uuid,
            matchesPerPairing = 2,
            baseSeed = 7,
            ai = "random"
        )

        assertThat(rating.matches).isEqualTo(2)
        assertThat(rating.anchorBatchUuid).isNull()
        assertThat(rating.bradleyTerry).isNull()
        assertThat(rating.aemberScore).isNull()
        assertThat(rating.percentile).isNull()
        // Margin depends on the record split; it is always finite and bounded.
        assertThat(rating.winRateMargin).isFinite()
        assertThat(rating.winRateMargin).isBetween(0.0, 1.97)
    }

    @Test
    fun `unknown pool key without seed file fails`() {
        val deck = saveDeck("Some Deck")

        assertThatThrownBy {
            service.rateDeckAgainstPool("missing", deck.uuid)
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("Unknown deck pool: missing")
    }

    @Test
    fun `addToPool seeds an unknown pool from the seed file`() {
        val seededDeck = saveDeck("Seeded Pool Deck")
        val addedDeck = saveDeck("Added Deck")
        Files.write(
            tempDir.resolve("addpool-pool.txt"),
            listOf("# comment", seededDeck.uuid, "")
        )

        val pool = service.addToPool("addpool", addedDeck.uuid)

        // The seed file content is kept, not silently discarded by the first add.
        assertThat(pool.deckUuids).containsExactly(seededDeck.uuid, addedDeck.uuid)
        assertThat(poolRepo.findByKey("addpool")!!.deckUuids)
            .containsExactly(seededDeck.uuid, addedDeck.uuid)
    }

    @Test
    fun `addToPool creates an empty pool without a seed file`() {
        val deck = saveDeck("Fresh Pool Deck")

        val pool = service.addToPool("freshpool", deck.uuid)

        assertThat(pool.deckUuids).containsExactly(deck.uuid)
    }

    @Test
    fun `anchor ignores a ladder batch run with a different ai`() {
        val poolDeck = saveDeck("Tactical Ladder Deck")
        val challenger = saveDeck("Random Challenger")

        val batch = MassSimulationBatch(
            uuid = UUID.randomUUID().toString(),
            status = MassSimulationStatus.COMPLETED,
            batchType = MassSimulationBatchType.MULTI_DECK,
            aiA = "tactical",
            aiB = "tactical",
            matchCount = 2,
            deckPool = listOf(DeckRef(poolDeck.uuid, poolDeck.name)),
            ai = "tactical",
            seatAlternation = true,
            completedAt = Instant.now()
        )
        massSimRepo.save(batch)
        massSimRepo.saveResult(
            com.aember.analyzer.domain.model.MassSimulationMatchResult(
                id = 1,
                batchUuid = batch.uuid,
                seed = 1,
                winnerIndex = 0,
                endCause = com.aember.analyzer.domain.engine.model.EndCause.KEYS,
                turns = 10,
                rounds = 10,
                fidelityScore = 90.0,
                statistics = emptyList(),
                deckUuidA = poolDeck.uuid,
                deckUuidB = poolDeck.uuid
            )
        )

        service.addToPool("aimix", poolDeck.uuid)

        val rating = service.rateDeckAgainstPool(
            poolKey = "aimix",
            deckUuid = challenger.uuid,
            matchesPerPairing = 1,
            baseSeed = 0,
            ai = "random"
        )

        // The tactical ladder must not anchor a random-AI rating.
        assertThat(rating.anchorBatchUuid).isNull()
        assertThat(rating.bradleyTerry).isNull()
    }

    @Test
    fun `rating an unimported deck fails`() {
        service.addToPool("seeded", saveDeck("Pool Member").uuid)

        assertThatThrownBy {
            service.rateDeckAgainstPool("seeded", UUID.randomUUID().toString())
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("Deck not found")
    }

    @Test
    fun `pool seeds from text file when unknown`() {
        val deck = saveDeck("Seeded Pool Deck")
        Files.write(
            tempDir.resolve("filepool-pool.txt"),
            listOf("# comment", deck.uuid, "")
        )

        val pool = service.getPool("filepool")

        assertThat(pool.deckUuids).containsExactly(deck.uuid)
        // Persisted after seeding.
        assertThat(poolRepo.findByKey("filepool")!!.deckUuids).containsExactly(deck.uuid)
    }

    @Test
    fun `removeFromPool drops the deck and keeps the rest`() {
        val deckA = saveDeck("Remove A")
        val deckB = saveDeck("Remove B")
        service.addToPool("removal", deckA.uuid)
        service.addToPool("removal", deckB.uuid)

        val pool = service.removeFromPool("removal", deckA.uuid)

        assertThat(pool.deckUuids).containsExactly(deckB.uuid)
        assertThat(poolRepo.findByKey("removal")!!.deckUuids).containsExactly(deckB.uuid)
        // Removing an unknown deck is a no-op.
        assertThat(service.removeFromPool("removal", UUID.randomUUID().toString()).deckUuids)
            .containsExactly(deckB.uuid)
    }

    @Test
    fun `rating a pool member plays only against the other members`() {
        val deckA = saveDeck("Member A")
        val deckB = saveDeck("Member B")
        service.addToPool("selfpool", deckA.uuid)
        service.addToPool("selfpool", deckB.uuid)

        val rating = service.rateDeckAgainstPool(
            poolKey = "selfpool",
            deckUuid = deckA.uuid,
            matchesPerPairing = 2,
            baseSeed = 3,
            ai = "random"
        )

        // 1 opponent (deckB) x 2 matches; deckA does not play itself.
        assertThat(rating.matches).isEqualTo(2)
        assertThat(rating.opponents.map { it.deckUuid }).containsExactly(deckB.uuid)
    }

    private fun deckRating(
        uuid: String,
        name: String,
        aemberScore: Double,
        bradleyTerry: Double
    ) = DeckRating(
        deckUuid = uuid,
        deckName = name,
        wins = 2,
        losses = 2,
        draws = 0,
        matches = 4,
        winRate = 0.5,
        winRateMargin = 0.49,
        sos = 0.5,
        bradleyTerry = bradleyTerry,
        aemberScore = aemberScore,
        percentile = 50.0,
        lowConfidence = false
    )

    private fun saveDeck(name: String): Deck {
        val deck = Deck(
            uuid = UUID.randomUUID().toString(),
            name = name,
            expansion = 341,
            identity = name.lowercase().replace(" ", "-"),
            houses = listOf(House.DIS, House.SANCTUM, House.UNTAMED),
            cards = listOf(
                DeckCard("anger", 1, House.BROBNAR, false, false, emptyList(), false),
                DeckCard("bad-penny", 1, House.SHADOWS, false, false, emptyList(), false),
                DeckCard("nexus", 1, House.SHADOWS, false, false, emptyList(), false),
                DeckCard("tocsin", 1, House.DIS, false, false, emptyList(), false),
                DeckCard("dust-imp", 1, House.DIS, false, false, emptyList(), false),
                DeckCard("ember-imp", 1, House.DIS, false, false, emptyList(), false),
                DeckCard("doc-bookton", 1, House.LOGOS, false, false, emptyList(), false),
                DeckCard("bumpsy", 1, House.BROBNAR, false, false, emptyList(), false),
                DeckCard("valdr", 1, House.BROBNAR, false, false, emptyList(), false)
            )
        )
        return deckRepository.save(deck)
    }

    private class InMemoryDeckPoolRepository : DeckPoolRepository {
        val pools = mutableMapOf<String, DeckPool>()
        val stored = mutableListOf<Pair<String, PoolRatingRecord>>()

        override fun save(pool: DeckPool) {
            pools[pool.key] = pool
        }

        override fun findByKey(key: String): DeckPool? = pools[key]

        override fun saveRating(key: String, rating: com.aember.analyzer.domain.rating.PoolDeckRating) {
            stored += key to PoolRatingRecord(rating)
        }

        override fun findRatingHistory(
            key: String,
            deckUuid: String?
        ): List<com.aember.analyzer.domain.rating.PoolDeckRating> {
            return stored
                .filter { it.first == key && (deckUuid == null || it.second.rating.deckUuid == deckUuid) }
                .map { it.second.rating }
        }
    }

    private data class PoolRatingRecord(val rating: com.aember.analyzer.domain.rating.PoolDeckRating)
}
