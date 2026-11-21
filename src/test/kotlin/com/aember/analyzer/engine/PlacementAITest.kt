package com.aember.analyzer.engine

import com.aember.analyzer.domain.ai.TacticalAI
import com.aember.analyzer.domain.engine.CardLibrary
import com.aember.analyzer.domain.engine.action.ActionGenerator
import com.aember.analyzer.domain.engine.action.PlayCardAction
import com.aember.analyzer.domain.engine.model.*
import com.aember.analyzer.domain.model.CardType
import com.aember.analyzer.domain.model.CatalogCard
import com.aember.analyzer.domain.model.House
import com.aember.analyzer.domain.model.Rarity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PlacementAITest {

    private fun catalogCard(
        cardId: String,
        house: House,
        type: CardType,
        power: Int? = null,
        keywords: List<String> = emptyList()
    ): CatalogCard {
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
            power = power,
            text = "",
            keywords = keywords,
            traits = emptyList()
        )
    }

    @Test
    fun `ActionGenerator offers both flank placements for a creature`() {
        val library = CardLibrary(mapOf(
            "bruiser" to catalogCard("bruiser", House.BROBNAR, CardType.CREATURE, power = 3),
            "taunt-guard" to catalogCard("taunt-guard", House.BROBNAR, CardType.CREATURE, power = 4, keywords = listOf("taunt"))
        ))
        val generator = ActionGenerator(library)
        val game = EngineTestHelper.makeGame(
            player0Creatures = listOf(EngineTestHelper.creature("taunt-guard", 1, power = 4, keywords = mapOf("taunt" to 1)))
        )
        game.activePlayer().activeHouse = House.BROBNAR
        game.activePlayer().hand.add("bruiser")

        val actions = generator.generate(game).filterIsInstance<PlayCardAction>().filter { it.cardId == "bruiser" }

        assertThat(actions).hasSize(2)
        assertThat(actions.map { it.placementIndex }).containsExactlyInAnyOrder(0, 1)
    }

    @Test
    fun `TacticalAI places a high value creature next to a protector`() {
        val library = CardLibrary(mapOf(
            "heavy-hitter" to catalogCard("heavy-hitter", House.BROBNAR, CardType.CREATURE, power = 5),
            "taunt-guard" to catalogCard("taunt-guard", House.BROBNAR, CardType.CREATURE, power = 4, keywords = listOf("taunt"))
        ))
        val generator = ActionGenerator(library)
        val ai = TacticalAI(library)
        val game = EngineTestHelper.makeGame(
            player0Creatures = listOf(EngineTestHelper.creature("taunt-guard", 1, power = 4, keywords = mapOf("taunt" to 1)))
        )
        game.activePlayer().activeHouse = House.BROBNAR
        game.activePlayer().hand.add("heavy-hitter")

        val actions = generator.generate(game)
        val chosen = ai.choose(game, actions) as PlayCardAction

        assertThat(chosen.cardId).isEqualTo("heavy-hitter")
        // Taunt is on the left flank (index 0), so placing at index 0 puts the new creature next to it.
        assertThat(chosen.placementIndex).isEqualTo(0)
    }
}
