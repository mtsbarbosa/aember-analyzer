package com.aember.analyzer.engine

import com.aember.analyzer.domain.engine.action.ActionGenerator
import com.aember.analyzer.domain.engine.action.DiscardAction
import com.aember.analyzer.domain.engine.action.PlayCardAction
import com.aember.analyzer.domain.engine.action.UseArtifactAction
import com.aember.analyzer.domain.engine.action.UseCreatureAction
import com.aember.analyzer.domain.engine.dsl.*
import com.aember.analyzer.domain.engine.model.GameState
import com.aember.analyzer.domain.engine.model.PlayerState
import com.aember.analyzer.domain.model.CardType
import com.aember.analyzer.domain.model.CatalogCard
import com.aember.analyzer.domain.model.House
import com.aember.analyzer.domain.model.Rarity
import com.aember.analyzer.engine.EngineTestHelper.artifact
import com.aember.analyzer.engine.EngineTestHelper.creature
import com.aember.analyzer.engine.EngineTestHelper.dslCard
import com.aember.analyzer.engine.EngineTestHelper.emptyDeck
import com.aember.analyzer.engine.EngineTestHelper.makeLibrary
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SixNameRuleTest {

    private fun catalogFor(cardId: String, type: CardType, house: House = House.BROBNAR): CatalogCard {
        return CatalogCard(
            id = cardId,
            name = cardId,
            number = "000",
            image = "",
            expansion = 341,
            house = house,
            type = type,
            rarity = Rarity.COMMON,
            amber = 0,
            armor = null,
            power = if (type == CardType.CREATURE) 3 else null,
            text = "",
            keywords = emptyList(),
            traits = emptyList()
        )
    }

    private fun makeGameWithPlayer(player: PlayerState, activePlayerIndex: Int = 0): GameState {
        val otherPlayer = PlayerState(
            index = 1,
            name = "Player B",
            deck = emptyDeck()
        )
        return GameState(
            players = listOf(player, otherPlayer),
            activePlayerIndex = activePlayerIndex
        )
    }

    @Test
    fun `playing a card increments the six name counter`() {
        val player = PlayerState(
            index = 0,
            name = "Player A",
            deck = emptyDeck(),
            hand = mutableListOf("repeatable"),
            activeHouse = House.BROBNAR
        )
        val game = makeGameWithPlayer(player)
        val library = makeLibrary("repeatable" to dslCard { play(GainAmber(1)) })
        val generator = ActionGenerator(library)

        repeat(6) {
            val actions = generator.generate(game).filterIsInstance<PlayCardAction>()
            assertThat(actions).anyMatch { it.cardId == "repeatable" }
            actions.first { it.cardId == "repeatable" }.let { action ->
                player.hand.remove(action.cardId)
                player.discard.add(action.cardId)
                player.cardNamesPlayedOrUsedThisRound[action.cardId] = (player.cardNamesPlayedOrUsedThisRound[action.cardId] ?: 0) + 1
            }
            player.hand.add("repeatable")
        }

        val seventh = generator.generate(game).filterIsInstance<PlayCardAction>()
        assertThat(seventh).noneMatch { it.cardId == "repeatable" }
    }

    @Test
    fun `using a creature increments the six name counter`() {
        val repeatable = creature("repeatable", 1, power = 3)
        val player = PlayerState(
            index = 0,
            name = "Player A",
            deck = emptyDeck(),
            creatures = mutableListOf(repeatable),
            activeHouse = House.BROBNAR
        )
        val game = makeGameWithPlayer(player)
        val library = makeLibrary("repeatable" to dslCard { reap(GainAmber(1)) })
        val generator = ActionGenerator(library)

        repeat(6) {
            repeatable.exhausted = false
            val actions = generator.generate(game).filterIsInstance<UseCreatureAction>()
                .filter { it.instanceId == repeatable.instanceId }
            assertThat(actions).isNotEmpty
            player.cardNamesPlayedOrUsedThisRound[repeatable.cardId] = (player.cardNamesPlayedOrUsedThisRound[repeatable.cardId] ?: 0) + 1
        }

        repeatable.exhausted = false
        val seventh = generator.generate(game).filterIsInstance<UseCreatureAction>()
            .filter { it.instanceId == repeatable.instanceId }
        assertThat(seventh).isEmpty()
    }

    @Test
    fun `using an artifact increments the six name counter`() {
        val repeatable = artifact("repeatable", house = House.BROBNAR)
        val player = PlayerState(
            index = 0,
            name = "Player A",
            deck = emptyDeck(),
            artifacts = mutableListOf(repeatable),
            activeHouse = House.BROBNAR
        )
        val game = makeGameWithPlayer(player)
        val library = makeLibrary("repeatable" to dslCard { action("Use", GainAmber(1)) })
        val generator = ActionGenerator(library)

        repeat(6) {
            repeatable.exhausted = false
            val actions = generator.generate(game).filterIsInstance<UseArtifactAction>()
                .filter { it.instanceId == repeatable.instanceId }
            assertThat(actions).isNotEmpty
            player.cardNamesPlayedOrUsedThisRound[repeatable.cardId] = (player.cardNamesPlayedOrUsedThisRound[repeatable.cardId] ?: 0) + 1
        }

        repeatable.exhausted = false
        val seventh = generator.generate(game).filterIsInstance<UseArtifactAction>()
            .filter { it.instanceId == repeatable.instanceId }
        assertThat(seventh).isEmpty()
    }

    @Test
    fun `discarding a card increments the six name counter`() {
        val player = PlayerState(
            index = 0,
            name = "Player A",
            deck = emptyDeck(),
            hand = mutableListOf("repeatable"),
            activeHouse = House.BROBNAR
        )
        val game = makeGameWithPlayer(player)
        val library = makeLibrary("repeatable" to dslCard { play(GainAmber(1)) })
        val generator = ActionGenerator(library)

        repeat(6) {
            val actions = generator.generate(game).filterIsInstance<DiscardAction>()
            assertThat(actions).anyMatch { it.cardId == "repeatable" }
            actions.first { it.cardId == "repeatable" }.let { action ->
                player.cardNamesPlayedOrUsedThisRound[action.cardId] = (player.cardNamesPlayedOrUsedThisRound[action.cardId] ?: 0) + 1
            }
        }

        val seventh = generator.generate(game).filterIsInstance<DiscardAction>()
        assertThat(seventh).noneMatch { it.cardId == "repeatable" }
    }

    @Test
    fun `counter resets when a new round begins`() {
        val player = PlayerState(
            index = 0,
            name = "Player A",
            deck = emptyDeck(),
            hand = mutableListOf("repeatable"),
            activeHouse = House.BROBNAR
        )
        player.cardNamesPlayedOrUsedThisRound["repeatable"] = 6
        val game = makeGameWithPlayer(player)
        game.firstPlayerIndex = 0
        val library = makeLibrary("repeatable" to dslCard { play(GainAmber(1)) })
        val generator = ActionGenerator(library)

        assertThat(generator.generate(game).filterIsInstance<PlayCardAction>())
            .noneMatch { it.cardId == "repeatable" }

        // Simulate end-of-round reset.
        game.players.forEach { it.cardNamesPlayedOrUsedThisRound.clear() }

        assertThat(generator.generate(game).filterIsInstance<PlayCardAction>())
            .anyMatch { it.cardId == "repeatable" }
    }
}
