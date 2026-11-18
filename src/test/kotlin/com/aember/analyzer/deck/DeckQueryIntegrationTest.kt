package com.aember.analyzer.deck

import com.aember.analyzer.IntegrationTest
import com.aember.analyzer.application.DeckQueryService
import com.aember.analyzer.domain.model.Deck
import com.aember.analyzer.domain.model.DeckCard
import com.aember.analyzer.domain.model.House
import com.aember.analyzer.domain.ports.CardCatalogPort
import com.aember.analyzer.domain.ports.DeckRepository
import com.aember.analyzer.infrastructure.persistence.JpaCardCatalogAdapter
import com.aember.analyzer.infrastructure.persistence.JsonCardCatalogLoader
import com.aember.analyzer.infrastructure.persistence.entity.ExpansionEntity
import com.aember.analyzer.infrastructure.persistence.repository.DeckJpaRepository
import com.aember.analyzer.infrastructure.persistence.repository.ExpansionJpaRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.UUID

class DeckQueryIntegrationTest : IntegrationTest() {

    @Autowired
    private lateinit var deckRepository: DeckRepository

    @Autowired
    private lateinit var deckQueryService: DeckQueryService

    @Autowired
    private lateinit var catalog: CardCatalogPort

    @Autowired
    private lateinit var adapter: JpaCardCatalogAdapter

    @Autowired
    private lateinit var loader: JsonCardCatalogLoader

    @Autowired
    private lateinit var expansionRepository: ExpansionJpaRepository

    @Autowired
    private lateinit var deckJpaRepository: DeckJpaRepository

    @BeforeEach
    fun seed() {
        deckJpaRepository.deleteAll()
        if (catalog.loadAll().isEmpty()) {
            adapter.seedHousesAndExpansion()
            adapter.seed(loader.loadCoreSet())
        }
        if (!expansionRepository.existsById(435)) {
            expansionRepository.save(ExpansionEntity(id = 435, code = "WC", name = "Worlds Collide"))
        }
    }

    @Test
    fun `should list all imported decks`() {
        saveDeck(name = "Alpha Deck", houses = listOf(House.DIS, House.SANCTUM, House.UNTAMED))
        saveDeck(name = "Beta Deck", houses = listOf(House.BROBNAR, House.LOGOS, House.SHADOWS))

        val results = deckQueryService.search()

        assertThat(results).hasSize(2)
        assertThat(results.map { it.name }).containsExactlyInAnyOrder("Alpha Deck", "Beta Deck")
    }

    @Test
    fun `should filter decks by name`() {
        saveDeck(name = "Shadowmaster Desmares")
        saveDeck(name = "Test Archon Two")

        val results = deckQueryService.search(name = "Shadow")

        assertThat(results).hasSize(1)
        assertThat(results.first().name).isEqualTo("Shadowmaster Desmares")
    }

    @Test
    fun `should filter decks by house`() {
        saveDeck(name = "Dis Deck", houses = listOf(House.DIS, House.SANCTUM, House.UNTAMED))
        saveDeck(name = "Brobnar Deck", houses = listOf(House.BROBNAR, House.LOGOS, House.SHADOWS))

        val results = deckQueryService.search(house = "Dis")

        assertThat(results).hasSize(1)
        assertThat(results.first().name).isEqualTo("Dis Deck")
    }

    @Test
    fun `should filter decks by expansion`() {
        saveDeck(name = "CotA Deck", expansion = 341)
        saveDeck(name = "Other Deck", expansion = 435)

        val results = deckQueryService.search(expansion = 341)

        assertThat(results).hasSize(1)
        assertThat(results.first().name).isEqualTo("CotA Deck")
    }

    @Test
    fun `should combine filters`() {
        saveDeck(name = "Matching Deck", houses = listOf(House.DIS, House.UNTAMED), expansion = 341)
        saveDeck(name = "Non-Matching Deck", houses = listOf(House.BROBNAR, House.LOGOS, House.SHADOWS), expansion = 341)

        val results = deckQueryService.search(house = "Untamed", expansion = 341)

        assertThat(results).hasSize(1)
        assertThat(results.first().name).isEqualTo("Matching Deck")
    }

    @Test
    fun `should return empty list when no deck matches`() {
        saveDeck(name = "Visible Deck")

        val results = deckQueryService.search(name = "Nonexistent")

        assertThat(results).isEmpty()
    }

    @Test
    fun `should retrieve a single deck by uuid`() {
        val uuid = UUID.randomUUID().toString()
        saveDeck(uuid = uuid, name = "Findable Deck")

        val deck = deckQueryService.findByUuid(uuid)

        assertThat(deck).isNotNull
        assertThat(deck!!.name).isEqualTo("Findable Deck")
        assertThat(deck.cards).isNotEmpty
    }

    @Test
    fun `should return null when deck uuid is unknown`() {
        saveDeck(name = "Some Deck")

        val deck = deckQueryService.findByUuid("00000000-0000-0000-0000-000000000000")

        assertThat(deck).isNull()
    }

    private fun saveDeck(
        uuid: String = UUID.randomUUID().toString(),
        name: String,
        expansion: Int = 341,
        houses: List<House> = listOf(House.DIS, House.SANCTUM, House.UNTAMED)
    ): Deck {
        val deck = Deck(
            uuid = uuid,
            name = name,
            expansion = expansion,
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
