package com.aember.analyzer.engine

import com.aember.analyzer.domain.engine.CardLibrary
import com.aember.analyzer.domain.engine.IdGenerator
import com.aember.analyzer.domain.engine.action.ActionExecutor
import com.aember.analyzer.domain.engine.action.PlayCardAction
import com.aember.analyzer.domain.engine.dsl.*
import com.aember.analyzer.domain.engine.model.*
import com.aember.analyzer.domain.model.CardType
import com.aember.analyzer.domain.model.CatalogCard
import com.aember.analyzer.domain.model.Deck
import com.aember.analyzer.domain.model.DeckCard
import com.aember.analyzer.domain.model.House
import com.aember.analyzer.domain.model.Rarity

object EngineTestHelper {

    fun emptyDeck(houses: List<House> = listOf(House.BROBNAR, House.DIS, House.UNTAMED)): Deck {
        return Deck(
            uuid = "test-deck",
            name = "Test Deck",
            expansion = 341,
            identity = "test",
            houses = houses,
            cards = emptyList()
        )
    }

    fun makeLibrary(vararg scripts: Pair<String, DslCardScript>): CardLibrary {
        return makeLibrary(emptyMap(), *scripts)
    }

    fun makeLibrary(
        keywords: Map<String, List<String>>,
        vararg scripts: Pair<String, DslCardScript>
    ): CardLibrary {
        val catalog = scripts.map { (cardId, _) ->
            cardId to CatalogCard(
                id = cardId,
                name = cardId,
                number = "000",
                image = "",
                expansion = 341,
                house = House.BROBNAR,
                type = CardType.ACTION,
                rarity = Rarity.COMMON,
                amber = 0,
                armor = null,
                power = null,
                text = "",
                keywords = keywords[cardId] ?: emptyList(),
                traits = emptyList()
            )
        }.toMap()
        val library = CardLibrary(catalog)
        scripts.forEach { library.register(it.first, it.second) }
        return library
    }

    fun makeGame(
        player0Creatures: List<InPlayCard> = emptyList(),
        player1Creatures: List<InPlayCard> = emptyList(),
        player0Artifacts: List<InPlayCard> = emptyList(),
        player1Artifacts: List<InPlayCard> = emptyList(),
        activePlayerIndex: Int = 0
    ): GameState {
        val player0 = PlayerState(
            index = 0,
            name = "Player A",
            deck = emptyDeck(),
            creatures = player0Creatures.toMutableList(),
            artifacts = player0Artifacts.toMutableList()
        )
        val player1 = PlayerState(
            index = 1,
            name = "Player B",
            deck = emptyDeck(),
            creatures = player1Creatures.toMutableList(),
            artifacts = player1Artifacts.toMutableList()
        )
        return GameState(
            players = listOf(player0, player1),
            activePlayerIndex = activePlayerIndex
        )
    }

    fun creature(
        cardId: String,
        index: Int,
        owner: Int = 0,
        power: Int = 3,
        armor: Int = 0,
        keywords: Map<String, Int> = emptyMap(),
        exhausted: Boolean = false,
        stun: Boolean = false
    ): InPlayCard {
        return InPlayCard(
            instanceId = "$cardId-$index",
            cardId = cardId,
            ownerIndex = owner,
            controllerIndex = owner,
            zone = Zone.PLAY_AREA,
            house = House.BROBNAR,
            type = CardType.CREATURE,
            exhausted = exhausted,
            power = power,
            armor = armor,
            keywords = keywords,
            stun = stun
        )
    }

    fun artifact(
        cardId: String,
        owner: Int = 0,
        house: House = House.BROBNAR,
        exhausted: Boolean = false
    ): InPlayCard {
        return InPlayCard(
            instanceId = "$cardId-artifact",
            cardId = cardId,
            ownerIndex = owner,
            controllerIndex = owner,
            zone = Zone.PLAY_AREA,
            house = house,
            type = CardType.ARTIFACT,
            exhausted = exhausted
        )
    }

    fun dslCard(effect: CardDefinition.() -> Unit): DslCardScript {
        val definition = CardDefinition("test-card").apply(effect)
        return DslCardScript(definition, EffectResolver())
    }

    fun playCard(game: GameState, executor: ActionExecutor, cardId: String) {
        game.activePlayer().hand.add(cardId)
        executor.execute(game, PlayCardAction(cardId))
    }

    class IncrementingIdGenerator(private var counter: Long = 1) : IdGenerator {
        override fun nextId(prefix: String): String = "${prefix}-${counter++}"
    }
}
