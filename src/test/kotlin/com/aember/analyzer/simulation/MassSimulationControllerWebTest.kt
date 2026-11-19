package com.aember.analyzer.simulation

import com.aember.analyzer.IntegrationTest
import com.aember.analyzer.application.MassSimulationRequest
import com.aember.analyzer.domain.model.Deck
import com.aember.analyzer.domain.model.DeckCard
import com.aember.analyzer.domain.model.House
import com.aember.analyzer.domain.model.MassSimulationStatus
import com.aember.analyzer.domain.ports.CardCatalogPort
import com.aember.analyzer.domain.ports.DeckRepository
import com.aember.analyzer.infrastructure.persistence.JpaCardCatalogAdapter
import com.aember.analyzer.infrastructure.persistence.JsonCardCatalogLoader
import com.aember.analyzer.infrastructure.persistence.repository.DeckJpaRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.util.UUID

@AutoConfigureMockMvc
class MassSimulationControllerWebTest : IntegrationTest() {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var deckRepository: DeckRepository

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
    fun `POST mass-simulation starts async batch and returns accepted`() {
        val deckA = saveDeck(name = "Deck A")
        val deckB = saveDeck(name = "Deck B")

        val request = MassSimulationRequest(
            deckUuidA = deckA.uuid,
            deckUuidB = deckB.uuid,
            matchCount = 3,
            aiA = "tactical",
            aiB = "tactical",
            recordLogs = true,
            baseSeed = 42
        )

        val responseJson = mockMvc.post("/api/mass-simulations") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isAccepted() }
            jsonPath("$.uuid") { exists() }
            jsonPath("$.status") { value("PENDING") }
            jsonPath("$.matchCount") { value(3) }
            jsonPath("$.deckA.name") { value("Deck A") }
            jsonPath("$.deckB.name") { value("Deck B") }
        }.andReturn().response.contentAsString

        val batchUuid = objectMapper.readTree(responseJson).get("uuid").asText()

        // Poll until completion.
        val completedBatch = pollForBatch(batchUuid)
        assertThat(completedBatch.status).isIn(MassSimulationStatus.COMPLETED, MassSimulationStatus.FAILED)

        if (completedBatch.status == MassSimulationStatus.COMPLETED) {
            assertThat(completedBatch.winsA + completedBatch.winsB + completedBatch.draws).isEqualTo(3)
            assertThat(completedBatch.avgRounds).isNotNull
            assertThat(completedBatch.avgFidelity).isNotNull

            // Verify match results and logs.
            mockMvc.get("/api/mass-simulations/$batchUuid/matches").andExpect {
                status { isOk() }
                jsonPath("$.length()") { value(3) }
                jsonPath("$[0].statistics.reaps") { isNumber() }
            }

            mockMvc.get("/api/mass-simulations/$batchUuid/matches/1/logs").andExpect {
                status { isOk() }
                jsonPath("$.logEntries") { isArray() }
                jsonPath("$.fidelityEntries") { isArray() }
            }
        }
    }

    @Test
    fun `GET mass-simulations lists batches`() {
        val deckA = saveDeck(name = "Deck A")
        val deckB = saveDeck(name = "Deck B")

        val request = MassSimulationRequest(
            deckUuidA = deckA.uuid,
            deckUuidB = deckB.uuid,
            matchCount = 1,
            recordLogs = false,
            baseSeed = 1
        )

        mockMvc.post("/api/mass-simulations") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect { status { isAccepted() } }

        mockMvc.get("/api/mass-simulations").andExpect {
            status { isOk() }
            jsonPath("$[?(@.deckA.name == 'Deck A' && @.matchCount == 1)]") { exists() }
        }
    }

    private fun pollForBatch(uuid: String, timeoutMs: Long = 60_000, intervalMs: Long = 200): com.aember.analyzer.application.MassSimulationBatchDto {
        val start = System.currentTimeMillis()
        while (System.currentTimeMillis() - start < timeoutMs) {
            val response = mockMvc.get("/api/mass-simulations/$uuid").andExpect {
                status { isOk() }
            }.andReturn().response.contentAsString

            val batch = objectMapper.readValue(response, com.aember.analyzer.application.MassSimulationBatchDto::class.java)
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
