package com.aember.analyzer.engine

import com.aember.analyzer.domain.ai.GreedyAI
import com.aember.analyzer.domain.ai.RandomAI
import com.aember.analyzer.domain.ai.TacticalAI
import com.aember.analyzer.domain.engine.CardLibrary
import com.aember.analyzer.domain.engine.GameEngine
import com.aember.analyzer.domain.engine.model.EndCause
import com.aember.analyzer.domain.ports.CardCatalogPort
import com.aember.analyzer.infrastructure.persistence.JsonCardCatalogLoader
import com.aember.analyzer.infrastructure.rest.MasterVaultDeckParser
import com.aember.analyzer.domain.model.Deck
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GameEngineTest {

    private val catalog: CardCatalogPort
    private val deckA: Deck
    private val deckB: Deck

    init {
        val loader = JsonCardCatalogLoader(ObjectMapper(), "/home/mateus/work/keyteki/master-vault-data")
        val cards = loader.loadCoreSet().associateBy { it.id }
        catalog = object : CardCatalogPort {
            override fun loadAll() = cards.values.toList()
            override fun loadByExpansion(expansion: Int) = cards.values.filter { it.expansion == expansion }
            override fun findById(id: String) = cards[id]
        }
        val deckJson = ObjectMapper().readTree(
            javaClass.getResource("/decks/cota-deck.json")!!
        )
        deckA = MasterVaultDeckParser.parse(deckJson, catalog)
        deckB = MasterVaultDeckParser.parse(deckJson, catalog)
    }

    @Test
    fun `should run a match between two greedy AIs to completion`() {
        val library = CardLibrary(catalog.loadAll().associateBy { it.id })
        val engine = GameEngine(library, GreedyAI(), GreedyAI())

        val result = engine.runMatch(deckA, deckB, seed = 42L)

        assertThat(result.done).isTrue()
        assertThat(result.endCause).isNotNull
        assertThat(result.winnerIndex).isNotNull()
        assertThat(result.turn).isGreaterThan(0)
    }

    @Test
    fun `should run a match between two random AIs to completion`() {
        val library = CardLibrary(catalog.loadAll().associateBy { it.id })
        val engine = GameEngine(library, RandomAI(), RandomAI())

        val result = engine.runMatch(deckA, deckB, seed = 123L)

        assertThat(result.done).isTrue()
        assertThat(result.winnerIndex).isNotNull()
    }

    @Test
    fun `should run a match between two tactical AIs to completion and track statistics`() {
        val library = CardLibrary(catalog.loadAll().associateBy { it.id })
        val engine = GameEngine(library, TacticalAI(library), TacticalAI(library))

        val result = engine.runMatch(deckA, deckB, seed = 42L)

        assertThat(result.done).isTrue()
        assertThat(result.endCause).isNotNull
        assertThat(result.winnerIndex).isNotNull()
        result.players.forEach { player ->
            assertThat(player.statistics.totalActions()).isPositive()
            assertThat(player.statistics.housesChosen).isNotEmpty()
        }
        assertThat(result.players.sumOf { it.statistics.cardsPlayed }).isPositive()
    }
}
