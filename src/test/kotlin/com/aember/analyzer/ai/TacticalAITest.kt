package com.aember.analyzer.ai

import com.aember.analyzer.domain.ai.AISettings
import com.aember.analyzer.domain.ai.TacticalAI
import com.aember.analyzer.domain.engine.CardLibrary
import com.aember.analyzer.domain.engine.action.FightUse
import com.aember.analyzer.domain.engine.action.UseCreatureAction
import com.aember.analyzer.domain.engine.model.GameState
import com.aember.analyzer.domain.engine.model.InPlayCard
import com.aember.analyzer.domain.engine.model.PlayerState
import com.aember.analyzer.domain.engine.model.Zone
import com.aember.analyzer.domain.model.CardType
import com.aember.analyzer.domain.model.CatalogCard
import com.aember.analyzer.domain.model.House
import com.aember.analyzer.domain.model.Rarity
import com.aember.analyzer.engine.EngineTestHelper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TacticalAITest {

    @Test
    fun `prioritizes amber-control creature as fight target over vanilla creature`() {
        val catalog = mapOf(
            "attacker" to makeCard("attacker", "Attacker", 5, ""),
            "vanilla-target" to makeCard("vanilla-target", "Vanilla", 3, ""),
            "amber-controller" to makeCard(
                "amber-controller",
                "Amber Controller",
                3,
                "Each time an opponent gains amber, capture it."
            )
        )
        val library = CardLibrary(catalog)

        val attacker = creature("attacker", "att-1", 0, 5)
        val vanillaTarget = creature("vanilla-target", "van-1", 1, 3)
        val amberController = creature("amber-controller", "ctrl-1", 1, 3)

        val player0 = PlayerState(index = 0, name = "A", deck = EngineTestHelper.emptyDeck(), creatures = mutableListOf(attacker))
        val player1 = PlayerState(index = 1, name = "B", deck = EngineTestHelper.emptyDeck(), creatures = mutableListOf(vanillaTarget, amberController))
        val game = GameState(players = listOf(player0, player1), activePlayerIndex = 0)

        val actions = listOf(
            UseCreatureAction("att-1", FightUse("van-1")),
            UseCreatureAction("att-1", FightUse("ctrl-1"))
        )

        val ai = TacticalAI(library, AISettings.DEFAULT)
        val chosen = ai.choose(game, actions)

        val chosenTarget = (chosen as UseCreatureAction).useType as FightUse
        assertThat(chosenTarget.targetInstanceId).isEqualTo("ctrl-1")
    }

    @Test
    fun `prioritizes key-forge lock creature as fight target`() {
        val catalog = mapOf(
            "attacker" to makeCard("attacker", "Attacker", 5, ""),
            "vanilla-target" to makeCard("vanilla-target", "Vanilla", 3, ""),
            "key-locker" to makeCard(
                "key-locker",
                "Key Locker",
                3,
                "Your opponent cannot forge keys."
            )
        )
        val library = CardLibrary(catalog)

        val attacker = creature("attacker", "att-1", 0, 5)
        val vanillaTarget = creature("vanilla-target", "van-1", 1, 3)
        val keyLocker = creature("key-locker", "lock-1", 1, 3)

        val player0 = PlayerState(index = 0, name = "A", deck = EngineTestHelper.emptyDeck(), creatures = mutableListOf(attacker))
        val player1 = PlayerState(index = 1, name = "B", deck = EngineTestHelper.emptyDeck(), creatures = mutableListOf(vanillaTarget, keyLocker))
        val game = GameState(players = listOf(player0, player1), activePlayerIndex = 0)

        val actions = listOf(
            UseCreatureAction("att-1", FightUse("van-1")),
            UseCreatureAction("att-1", FightUse("lock-1"))
        )

        val ai = TacticalAI(library, AISettings.DEFAULT)
        val chosen = ai.choose(game, actions)

        val chosenTarget = (chosen as UseCreatureAction).useType as FightUse
        assertThat(chosenTarget.targetInstanceId).isEqualTo("lock-1")
    }

    private fun makeCard(
        id: String,
        name: String,
        power: Int,
        text: String
    ): CatalogCard {
        return CatalogCard(
            id = id,
            name = name,
            number = "000",
            image = "",
            expansion = 341,
            house = House.BROBNAR,
            type = CardType.CREATURE,
            rarity = Rarity.COMMON,
            amber = 0,
            armor = null,
            power = power,
            text = text,
            keywords = emptyList(),
            traits = emptyList()
        )
    }

    private fun creature(cardId: String, instanceId: String, owner: Int, power: Int): InPlayCard {
        return InPlayCard(
            instanceId = instanceId,
            cardId = cardId,
            ownerIndex = owner,
            controllerIndex = owner,
            zone = Zone.PLAY_AREA,
            house = House.BROBNAR,
            type = CardType.CREATURE,
            power = power
        )
    }
}
