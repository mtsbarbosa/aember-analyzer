package com.aember.analyzer.simulation

import com.aember.analyzer.IntegrationTest
import com.aember.analyzer.adapter.rest.MassSimulationBatchDto
import com.aember.analyzer.adapter.rest.DeckRatingDto
import com.aember.analyzer.domain.model.Deck
import com.aember.analyzer.domain.model.DeckCard
import com.aember.analyzer.domain.model.House
import com.aember.analyzer.domain.model.MultiDeckSimulationRequest
import com.aember.analyzer.domain.model.MassSimulationStatus
import com.aember.analyzer.domain.ports.CardCatalogPort
import com.aember.analyzer.domain.ports.DeckRepository
import com.aember.analyzer.domain.ports.MassSimulationRepository
import com.aember.analyzer.infrastructure.persistence.JpaCardCatalogAdapter
import com.aember.analyzer.infrastructure.persistence.JsonCardCatalogLoader
import com.aember.analyzer.infrastructure.persistence.repository.DeckJpaRepository
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.nio.file.Files
import java.nio.file.Path
import java.util.UUID

@AutoConfigureMockMvc
class MultiDeckMassSimulationControllerWebTest : IntegrationTest() {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var deckRepository: DeckRepository

    @Autowired
    private lateinit var massSimulationRepository: MassSimulationRepository

    @Value("\${aember.simulation.storage-path}")
    private lateinit var storagePath: String

    @Autowired
    private lateinit var catalog: CardCatalogPort

    @Autowired
    private lateinit var adapter: JpaCardCatalogAdapter

    @Autowired
    private lateinit var loader: JsonCardCatalogLoader

    @Autowired
    private lateinit var deckJpaRepository: DeckJpaRepository

    @BeforeEach
    fun seed() {
        deckJpaRepository.deleteAll()
        if (catalog.loadAll().isEmpty()) {
            adapter.seedHousesAndExpansion()
            adapter.seed(loader.loadCoreSet())
        }
    }

    @Test
    fun `multi-deck batch runs round robin and produces ratings`() {
        val deckA = saveDeck(name = "Alpha Deck")
        val deckB = saveDeck(name = "Beta Deck")
        val deckC = saveDeck(name = "Gamma Deck")

        val request = MultiDeckSimulationRequest(
            deckUuids = listOf(deckA.uuid, deckB.uuid, deckC.uuid),
            matchesPerPairing = 2,
            ai = "random",
            seatAlternation = true,
            baseSeed = 12345
        )

        val responseJson = mockMvc.post("/api/mass-simulations/multi") {
            contentType = MediaType.APPLICATION_JSON
            header("X-Debug", "true")
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isAccepted() }
            jsonPath("$.uuid") { exists() }
            jsonPath("$.status") { value("PENDING") }
            jsonPath("$.batchType") { value("MULTI_DECK") }
            jsonPath("$.matchCount") { value(6) } // 3 pairings x 2 matches
            jsonPath("$.deckPool.length()") { value(3) }
            jsonPath("$.seatAlternation") { value(true) }
        }.andReturn().response.contentAsString

        val batchUuid = objectMapper.readTree(responseJson).get("uuid").asText()

        val completedBatch = pollForBatch(batchUuid)
        assertThat(completedBatch.status).isEqualTo(MassSimulationStatus.COMPLETED)
        assertThat(completedBatch.completedCount).isEqualTo(6)
        assertThat(completedBatch.failedCount).isEqualTo(0)
        assertThat(completedBatch.deckAggregates).hasSize(3)
        assertThat(completedBatch.pairAggregates).hasSize(3)
        // Every deck plays 4 matches (2 opponents x 2 matches).
        completedBatch.deckAggregates.forEach { assertThat(it.matches).isEqualTo(4) }

        // Ratings endpoint.
        val ratingsJson = mockMvc.get("/api/mass-simulations/$batchUuid/ratings")
            .andExpect { status { isOk() } }
            .andReturn().response.contentAsString
        val ratings = objectMapper.readValue(ratingsJson, object : TypeReference<List<DeckRatingDto>>() {})
        assertThat(ratings).hasSize(3)
        assertThat(ratings.all { it.matches == 4 }).isTrue()
        // Ratings are sorted by Aember Score, which is pool-relative (median deck ~= 50).
        assertThat(ratings[0].aemberScore).isGreaterThanOrEqualTo(ratings[1].aemberScore)
        assertThat(ratings[1].aemberScore).isGreaterThanOrEqualTo(ratings[2].aemberScore)
        assertThat(ratings.all { it.aemberScore in 10.0..90.0 }).isTrue()
        // Ratings were persisted at batch completion (ratings.json).
        val persisted = massSimulationRepository.findRatings(batchUuid)
        assertThat(persisted).isNotNull
        assertThat(persisted!!).hasSize(3)

        // Deleting ratings.json forces the endpoint to recompute (lazy backfill).
        Files.delete(Path.of(storagePath, batchUuid, "ratings.json"))
        assertThat(massSimulationRepository.findRatings(batchUuid)).isNull()
        val backfilled = mockMvc.get("/api/mass-simulations/$batchUuid/ratings")
            .andExpect { status { isOk() } }
            .andReturn().response.contentAsString
        assertThat(massSimulationRepository.findRatings(batchUuid)).isNotNull
        val recomputed = objectMapper.readValue(backfilled, object : TypeReference<List<DeckRatingDto>>() {})
        assertThat(recomputed).hasSize(3)

        // Match results list every match; logs are stored because X-Debug was set.
        mockMvc.get("/api/mass-simulations/$batchUuid/matches").andExpect {
            status { isOk() }
            jsonPath("$.length()") { value(6) }
        }
        mockMvc.get("/api/mass-simulations/$batchUuid/matches/1/logs").andExpect {
            status { isOk() }
            jsonPath("$.logEntries") { isArray() }
        }
    }

    @Test
    fun `multi-deck batch rejects unknown deck uuid`() {
        val request = MultiDeckSimulationRequest(
            deckUuids = listOf(UUID.randomUUID().toString(), UUID.randomUUID().toString()),
            matchesPerPairing = 2
        )

        mockMvc.post("/api/mass-simulations/multi") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect { status { isBadRequest() } }
    }

    @Test
    fun `multi-deck batch rejects a pool with duplicates`() {
        val deckA = saveDeck(name = "Dup Deck")

        val request = MultiDeckSimulationRequest(
            deckUuids = listOf(deckA.uuid, deckA.uuid),
            matchesPerPairing = 2
        )

        mockMvc.post("/api/mass-simulations/multi") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect { status { isBadRequest() } }
    }

    @Test
    fun `ratings are rejected for two-deck batches`() {
        val deckA = saveDeck(name = "Solo Deck")
        val deckB = saveDeck(name = "Other Deck")

        val responseJson = mockMvc.post("/api/mass-simulations") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(
                com.aember.analyzer.domain.model.MassSimulationRequest(
                    deckUuidA = deckA.uuid,
                    deckUuidB = deckB.uuid,
                    matchCount = 1,
                    recordLogs = false,
                    baseSeed = 1
                )
            )
        }.andExpect { status { isAccepted() } }
            .andReturn().response.contentAsString

        val batchUuid = objectMapper.readTree(responseJson).get("uuid").asText()

        mockMvc.get("/api/mass-simulations/$batchUuid/ratings")
            .andExpect { status { isBadRequest() } }
    }

    private fun pollForBatch(uuid: String, timeoutMs: Long = 120_000, intervalMs: Long = 200): MassSimulationBatchDto {
        val start = System.currentTimeMillis()
        while (System.currentTimeMillis() - start < timeoutMs) {
            val response = mockMvc.get("/api/mass-simulations/$uuid").andExpect {
                status { isOk() }
            }.andReturn().response.contentAsString

            val batch = objectMapper.readValue(response, MassSimulationBatchDto::class.java)
            if (batch.status == MassSimulationStatus.COMPLETED || batch.status == MassSimulationStatus.FAILED) {
                return batch
            }
            Thread.sleep(intervalMs)
        }
        throw IllegalStateException("Timed out waiting for batch $uuid")
    }

    private fun saveDeck(
        uuid: String = UUID.randomUUID().toString(),
        name: String,
        houses: List<House> = listOf(House.DIS, House.SANCTUM, House.UNTAMED)
    ): Deck {
        val deck = Deck(
            uuid = uuid,
            name = name,
            expansion = 341,
            identity = name.lowercase().replace(" ", "-"),
            houses = houses,
            cards = listOf(
                DeckCard(cardId = "anger", count = 1, effectiveHouse = House.BROBNAR, maverick = false, anomaly = false, enhancements = emptyList(), isNonDeck = false),
                DeckCard(cardId = "bad-penny", count = 1, effectiveHouse = House.SHADOWS, maverick = false, anomaly = false, enhancements = emptyList(), isNonDeck = false),
                DeckCard(cardId = "nexus", count = 1, effectiveHouse = House.SHADOWS, maverick = false, anomaly = false, enhancements = emptyList(), isNonDeck = false),
                DeckCard(cardId = "tocsin", count = 1, effectiveHouse = House.DIS, maverick = false, anomaly = false, enhancements = emptyList(), isNonDeck = false),
                DeckCard(cardId = "dust-imp", count = 1, effectiveHouse = House.DIS, maverick = false, anomaly = false, enhancements = emptyList(), isNonDeck = false),
                DeckCard(cardId = "ember-imp", count = 1, effectiveHouse = House.DIS, maverick = false, anomaly = false, enhancements = emptyList(), isNonDeck = false),
                DeckCard(cardId = "doc-bookton", count = 1, effectiveHouse = House.LOGOS, maverick = false, anomaly = false, enhancements = emptyList(), isNonDeck = false),
                DeckCard(cardId = "bumpsy", count = 1, effectiveHouse = House.BROBNAR, maverick = false, anomaly = false, enhancements = emptyList(), isNonDeck = false),
                DeckCard(cardId = "valdr", count = 1, effectiveHouse = House.BROBNAR, maverick = false, anomaly = false, enhancements = emptyList(), isNonDeck = false)
            )
        )
        return deckRepository.save(deck)
    }
}
