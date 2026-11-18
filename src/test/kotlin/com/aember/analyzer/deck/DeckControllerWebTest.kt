package com.aember.analyzer.deck

import com.aember.analyzer.IntegrationTest
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
class DeckControllerWebTest : IntegrationTest() {

    @Autowired
    private lateinit var mockMvc: MockMvc

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
    fun `GET should list decks`() {
        val uuid = UUID.randomUUID().toString()
        saveDeck(uuid = uuid, name = "Shadowmaster Desmares")

        mockMvc.get("/api/decks")
            .andExpect {
                status { isOk() }
                jsonPath("$[0].name") { value("Shadowmaster Desmares") }
                jsonPath("$[0].uuid") { value(uuid) }
            }
    }

    @Test
    fun `GET should filter decks by name`() {
        saveDeck(name = "Shadowmaster Desmares")
        saveDeck(name = "Test Archon Two")

        mockMvc.get("/api/decks") { param("name", "Shadow") }
            .andExpect {
                status { isOk() }
                jsonPath("$.size()") { value(1) }
                jsonPath("$[0].name") { value("Shadowmaster Desmares") }
            }
    }

    @Test
    fun `GET should filter decks by house`() {
        saveDeck(name = "Dis Deck", houses = listOf(House.DIS, House.SANCTUM, House.UNTAMED))
        saveDeck(name = "Brobnar Deck", houses = listOf(House.BROBNAR, House.LOGOS, House.SHADOWS))

        mockMvc.get("/api/decks") { param("house", "Dis") }
            .andExpect {
                status { isOk() }
                jsonPath("$.size()") { value(1) }
                jsonPath("$[0].name") { value("Dis Deck") }
            }
    }

    @Test
    fun `GET should return deck by uuid`() {
        val uuid = UUID.randomUUID().toString()
        saveDeck(uuid = uuid, name = "Shadowmaster Desmares")

        mockMvc.get("/api/decks/$uuid")
            .andExpect {
                status { isOk() }
                jsonPath("$.name") { value("Shadowmaster Desmares") }
                jsonPath("$.cards") { isNotEmpty() }
            }
    }

    @Test
    fun `GET should return 404 for unknown uuid`() {
        mockMvc.get("/api/decks/00000000-0000-0000-0000-000000000000")
            .andExpect {
                status { isNotFound() }
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
                DeckCard(cardId = "bad-penny", count = 1, effectiveHouse = House.SHADOWS, maverick = false, anomaly = false, enhancements = emptyList(), isNonDeck = false)
            )
        )
        return deckRepository.save(deck)
    }
}
