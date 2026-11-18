package com.aember.analyzer.simulation

import com.aember.analyzer.IntegrationTest
import com.aember.analyzer.application.DeckImportService
import com.aember.analyzer.domain.model.Deck
import com.aember.analyzer.domain.model.DeckCard
import com.aember.analyzer.domain.model.House
import com.aember.analyzer.domain.ports.CardCatalogPort
import com.aember.analyzer.domain.ports.DeckRepository
import com.aember.analyzer.infrastructure.persistence.JpaCardCatalogAdapter
import com.aember.analyzer.infrastructure.persistence.JsonCardCatalogLoader
import com.aember.analyzer.infrastructure.persistence.repository.DeckJpaRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import java.util.UUID

@AutoConfigureMockMvc
class SimulationControllerWebTest : IntegrationTest() {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var deckRepository: DeckRepository

    @Autowired
    private lateinit var deckImportService: DeckImportService

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
    fun `GET simulate without debug header returns slim summary`() {
        val deckA = saveDeck(name = "Deck A")
        val deckB = saveDeck(name = "Deck B")

        mockMvc.get("/api/simulate/${deckA.uuid}/${deckB.uuid}?seed=42")
            .andExpect {
                status { isOk() }
                jsonPath("$.deckA.name") { value("Deck A") }
                jsonPath("$.deckB.name") { value("Deck B") }
                jsonPath("$.winnerIndex") { exists() }
                jsonPath("$.turns") { isNumber() }
                jsonPath("$.rounds") { isNumber() }
                jsonPath("$.players[0].creatureCount") { isNumber() }
                jsonPath("$.players[0].deckUuid") { value(deckA.uuid) }
                jsonPath("$.fidelity.score") { isNumber() }
                jsonPath("$.recentLog") { isArray() }
                jsonPath("$.gameState") { doesNotExist() }
            }
    }

    @Test
    fun `GET simulate with X-Debug true returns full verbose response`() {
        val deckA = saveDeck(name = "Deck A")
        val deckB = saveDeck(name = "Deck B")

        mockMvc.get("/api/simulate/${deckA.uuid}/${deckB.uuid}?seed=42") {
            header("X-Debug", "true")
        }
            .andExpect {
                status { isOk() }
                jsonPath("$.gameState") { exists() }
                jsonPath("$.gameState.players[0].deck.name") { value("Deck A") }
                jsonPath("$.gameState.log") { isArray() }
                jsonPath("$.fidelity.score") { isNumber() }
                jsonPath("$.recentLog") { doesNotExist() }
            }
    }

    @Test
    fun `GET simulate with X-Debug false returns slim summary`() {
        val deckA = saveDeck(name = "Deck A")
        val deckB = saveDeck(name = "Deck B")

        mockMvc.get("/api/simulate/${deckA.uuid}/${deckB.uuid}?seed=42") {
            header("X-Debug", "false")
        }
            .andExpect {
                status { isOk() }
                jsonPath("$.gameState") { doesNotExist() }
                jsonPath("$.recentLog") { isArray() }
            }
    }

    @Test
    fun `GET simulate returns match statistics in slim summary`() {
        val deckA = saveDeck(name = "Deck A")
        val deckB = saveDeck(name = "Deck B")

        mockMvc.get("/api/simulate/${deckA.uuid}/${deckB.uuid}?seed=42")
            .andExpect {
                status { isOk() }
                jsonPath("$.statistics.reaps") { isNumber() }
                jsonPath("$.statistics.fights") { isNumber() }
                jsonPath("$.statistics.cardsPlayed") { isNumber() }
                jsonPath("$.statistics.totalActions") { isNumber() }
                jsonPath("$.statistics.housesChosen") { isMap() }
            }
    }

    @Test
    fun `GET simulate accepts aiA and aiB query params`() {
        val deckA = saveDeck(name = "Deck A")
        val deckB = saveDeck(name = "Deck B")

        mockMvc.get("/api/simulate/${deckA.uuid}/${deckB.uuid}?seed=42&aiA=greedy&aiB=random")
            .andExpect {
                status { isOk() }
                jsonPath("$.winnerIndex") { exists() }
                jsonPath("$.statistics.totalActions") { isNumber() }
            }
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
