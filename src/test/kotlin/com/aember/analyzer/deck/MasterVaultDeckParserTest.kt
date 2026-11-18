package com.aember.analyzer.deck

import com.aember.analyzer.domain.model.CardType
import com.aember.analyzer.domain.model.House
import com.aember.analyzer.domain.ports.CardCatalogPort
import com.aember.analyzer.infrastructure.persistence.JsonCardCatalogLoader
import com.aember.analyzer.infrastructure.rest.MasterVaultDeckParser
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MasterVaultDeckParserTest {

    private lateinit var catalog: CardCatalogPort

    @BeforeAll
    fun setup() {
        val loader = JsonCardCatalogLoader(ObjectMapper(), "/home/mateus/work/keyteki/master-vault-data")
        val cards = loader.loadCoreSet().associateBy { it.id }
        catalog = object : CardCatalogPort {
            override fun loadAll() = cards.values.toList()
            override fun loadByExpansion(expansion: Int) = cards.values.filter { it.expansion == expansion }
            override fun findById(id: String) = cards[id]
        }
    }

    @Test
    fun `should parse a Core set deck fixture`() {
        val json = ObjectMapper().readTree(
            javaClass.getResource("/decks/cota-deck.json")!!
        )

        val deck = MasterVaultDeckParser.parse(json, catalog)

        assertThat(deck.name).isEqualTo("Shadowmaster Desmares")
        assertThat(deck.expansion).isEqualTo(341)
        assertThat(deck.houses).containsExactly(House.DIS, House.SANCTUM, House.UNTAMED)
        assertThat(deck.cards).hasSize(33)
        assertThat(deck.cards.sumOf { it.count }).isEqualTo(36)
        assertThat(deck.cards).allMatch { it.cardId.isNotBlank() }
    }

    @Test
    fun `should map card types correctly`() {
        val json = ObjectMapper().readTree(
            javaClass.getResource("/decks/cota-deck.json")!!
        )
        val deck = MasterVaultDeckParser.parse(json, catalog)

        val creatures = deck.cards.filter { catalog.findById(it.cardId)!!.type == CardType.CREATURE }.sumOf { it.count }
        val actions = deck.cards.filter { catalog.findById(it.cardId)!!.type == CardType.ACTION }.sumOf { it.count }
        val artifacts = deck.cards.filter { catalog.findById(it.cardId)!!.type == CardType.ARTIFACT }.sumOf { it.count }
        val upgrades = deck.cards.filter { catalog.findById(it.cardId)!!.type == CardType.UPGRADE }.sumOf { it.count }

        assertThat(creatures + actions + artifacts + upgrades).isEqualTo(36)
    }
}
