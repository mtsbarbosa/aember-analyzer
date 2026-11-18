package com.aember.analyzer.deck

import com.aember.analyzer.IntegrationTest
import com.aember.analyzer.application.DeckImportService
import com.aember.analyzer.domain.model.Deck
import com.aember.analyzer.domain.model.DeckCard
import com.aember.analyzer.domain.model.House
import com.aember.analyzer.domain.ports.CardCatalogPort
import com.aember.analyzer.domain.ports.DeckImportPort
import com.aember.analyzer.domain.ports.DeckRepository
import com.aember.analyzer.infrastructure.persistence.JpaCardCatalogAdapter
import com.aember.analyzer.infrastructure.persistence.JsonCardCatalogLoader
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import java.util.UUID

class DeckImportIntegrationTest : IntegrationTest() {

    @MockBean
    private lateinit var deckImportPort: DeckImportPort

    @Autowired
    private lateinit var deckImportService: DeckImportService

    @Autowired
    private lateinit var deckRepository: DeckRepository

    @Autowired
    private lateinit var loader: JsonCardCatalogLoader

    @Autowired
    private lateinit var adapter: JpaCardCatalogAdapter

    @Autowired
    private lateinit var catalog: CardCatalogPort

    @BeforeEach
    fun seed() {
        if (catalog.loadAll().isEmpty()) {
            adapter.seedHousesAndExpansion()
            adapter.seed(loader.loadCoreSet())
        }
    }

    @Test
    fun `should import and persist a deck`() {
        val uuid = UUID.randomUUID().toString()
        val deck = Deck(
            uuid = uuid,
            name = "Test Deck",
            expansion = 341,
            identity = "test-deck",
            houses = listOf(House.DIS, House.SANCTUM, House.UNTAMED),
            cards = listOf(
                DeckCard(cardId = "anger", count = 1, effectiveHouse = House.BROBNAR, maverick = false, anomaly = false, enhancements = emptyList(), isNonDeck = false),
                DeckCard(cardId = "bad-penny", count = 1, effectiveHouse = House.SHADOWS, maverick = false, anomaly = false, enhancements = emptyList(), isNonDeck = false)
            )
        )
        `when`(deckImportPort.importByUuid(uuid)).thenReturn(deck)

        val saved = deckImportService.importByUuid(uuid)

        assertThat(saved.id).isNotNull
        assertThat(deckRepository.findByUuid(uuid)).isNotNull
        assertThat(deckRepository.findByUuid(uuid)!!.cards).hasSize(2)
    }
}
