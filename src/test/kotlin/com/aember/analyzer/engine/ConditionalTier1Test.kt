package com.aember.analyzer.engine

import com.aember.analyzer.domain.engine.CardLibrary
import com.aember.analyzer.domain.engine.action.ActionExecutor
import com.aember.analyzer.domain.engine.action.PlayCardAction
import com.aember.analyzer.domain.engine.cards.coreSetCards
import com.aember.analyzer.domain.engine.cards.dslCardOverrides
import com.aember.analyzer.domain.engine.dsl.DslCardScript
import com.aember.analyzer.domain.engine.dsl.EffectResolver
import com.aember.analyzer.domain.model.CardType
import com.aember.analyzer.domain.model.CatalogCard
import com.aember.analyzer.domain.model.House
import com.aember.analyzer.domain.model.Rarity
import com.aember.analyzer.engine.EngineTestHelper.IncrementingIdGenerator
import com.aember.analyzer.engine.EngineTestHelper.creature
import com.aember.analyzer.engine.EngineTestHelper.emptyDeck
import com.aember.analyzer.engine.EngineTestHelper.makeGame
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ConditionalTier1Test {

    private val idGenerator = IncrementingIdGenerator()

    private val tier1Cards = setOf(
        "burn-the-stockpile",
        "smith",
        "shooler",
        "neuro-syphon",
        "lomir-flamefist",
        "the-terror",
        "flaxia",
        "ghostly-hand"
    )

    private fun libraryWithTier1Cards(): CardLibrary {
        val catalog = tier1Cards.associateWith { makeCard(it) }
        val library = CardLibrary(catalog)
        val resolver = EffectResolver(library = library)
        (coreSetCards + dslCardOverrides).filter { it.first in tier1Cards }
            .forEach { (id, definition) -> library.register(id, DslCardScript(definition, resolver)) }
        return library
    }

    @Test
    fun `Burn the Stockpile removes 4 amber only when opponent has 7 or more`() {
        val executor = ActionExecutor(libraryWithTier1Cards(), idGenerator)
        val game = makeGame()
        game.opponent().amber = 7
        game.activePlayer().hand.add("burn-the-stockpile")

        executor.execute(game, PlayCardAction("burn-the-stockpile"))

        assertThat(game.opponent().amber).isEqualTo(3)
    }

    @Test
    fun `Burn the Stockpile does nothing when opponent has less than 7 amber`() {
        val executor = ActionExecutor(libraryWithTier1Cards(), idGenerator)
        val game = makeGame()
        game.opponent().amber = 6
        game.activePlayer().hand.add("burn-the-stockpile")

        executor.execute(game, PlayCardAction("burn-the-stockpile"))

        assertThat(game.opponent().amber).isEqualTo(6)
    }

    @Test
    fun `Smith gains 2 amber when player controls more creatures`() {
        val executor = ActionExecutor(libraryWithTier1Cards(), idGenerator)
        val game = makeGame(player0Creatures = listOf(creature("x", 1), creature("x", 2)))
        game.activePlayer().hand.add("smith")

        executor.execute(game, PlayCardAction("smith"))

        assertThat(game.activePlayer().amber).isEqualTo(2)
    }

    @Test
    fun `Smith does nothing when player does not control more creatures`() {
        val executor = ActionExecutor(libraryWithTier1Cards(), idGenerator)
        val game = makeGame()
        game.activePlayer().hand.add("smith")

        executor.execute(game, PlayCardAction("smith"))

        assertThat(game.activePlayer().amber).isEqualTo(0)
    }

    @Test
    fun `Shooler steals 1 amber only when opponent has 4 or more`() {
        val executor = ActionExecutor(libraryWithTier1Cards(), idGenerator)
        val game = makeGame()
        game.opponent().amber = 4
        game.activePlayer().hand.add("shooler")

        executor.execute(game, PlayCardAction("shooler"))

        assertThat(game.opponent().amber).isEqualTo(3)
        assertThat(game.activePlayer().amber).isEqualTo(1)
    }

    @Test
    fun `Shooler does nothing when opponent has less than 4 amber`() {
        val executor = ActionExecutor(libraryWithTier1Cards(), idGenerator)
        val game = makeGame()
        game.opponent().amber = 3
        game.activePlayer().hand.add("shooler")

        executor.execute(game, PlayCardAction("shooler"))

        assertThat(game.opponent().amber).isEqualTo(3)
        assertThat(game.activePlayer().amber).isEqualTo(0)
    }

    @Test
    fun `Neuro Syphon steals 1 and draws 1 when opponent has more amber`() {
        val executor = ActionExecutor(libraryWithTier1Cards(), idGenerator)
        val game = makeGame()
        game.opponent().amber = 2
        game.activePlayer().amber = 1
        game.activePlayer().library.add("drawn-card")
        game.activePlayer().hand.add("neuro-syphon")
        val handBefore = game.activePlayer().hand.size

        executor.execute(game, PlayCardAction("neuro-syphon"))

        assertThat(game.opponent().amber).isEqualTo(1)
        assertThat(game.activePlayer().amber).isEqualTo(2)
        assertThat(game.activePlayer().hand.size).isEqualTo(handBefore) // play -1, draw +1
    }

    @Test
    fun `Neuro Syphon does nothing when opponent does not have more amber`() {
        val executor = ActionExecutor(libraryWithTier1Cards(), idGenerator)
        val game = makeGame()
        game.opponent().amber = 1
        game.activePlayer().amber = 2
        game.activePlayer().library.add("drawn-card")
        game.activePlayer().hand.add("neuro-syphon")
        val handBefore = game.activePlayer().hand.size

        executor.execute(game, PlayCardAction("neuro-syphon"))

        assertThat(game.opponent().amber).isEqualTo(1)
        assertThat(game.activePlayer().amber).isEqualTo(2)
        assertThat(game.activePlayer().hand.size).isEqualTo(handBefore - 1)
    }

    @Test
    fun `Lomir Flamefist removes 2 amber only when opponent has 7 or more`() {
        val executor = ActionExecutor(libraryWithTier1Cards(), idGenerator)
        val game = makeGame()
        game.opponent().amber = 8
        game.activePlayer().hand.add("lomir-flamefist")

        executor.execute(game, PlayCardAction("lomir-flamefist"))

        assertThat(game.opponent().amber).isEqualTo(6)
    }

    @Test
    fun `The Terror gains 2 amber only when opponent has 0 amber`() {
        val executor = ActionExecutor(libraryWithTier1Cards(), idGenerator)
        val game = makeGame()
        game.opponent().amber = 0
        game.activePlayer().hand.add("the-terror")

        executor.execute(game, PlayCardAction("the-terror"))

        assertThat(game.activePlayer().amber).isEqualTo(2)
    }

    @Test
    fun `The Terror does nothing when opponent has amber`() {
        val executor = ActionExecutor(libraryWithTier1Cards(), idGenerator)
        val game = makeGame()
        game.opponent().amber = 1
        game.activePlayer().hand.add("the-terror")

        executor.execute(game, PlayCardAction("the-terror"))

        assertThat(game.activePlayer().amber).isEqualTo(0)
    }

    @Test
    fun `Flaxia gains 2 amber when player controls more creatures`() {
        val executor = ActionExecutor(libraryWithTier1Cards(), idGenerator)
        val game = makeGame(player0Creatures = listOf(creature("x", 1)))
        game.activePlayer().hand.add("flaxia")

        executor.execute(game, PlayCardAction("flaxia"))

        assertThat(game.activePlayer().amber).isEqualTo(2)
    }

    @Test
    fun `Ghostly Hand steals exactly 1 amber when opponent has exactly 1`() {
        val executor = ActionExecutor(libraryWithTier1Cards(), idGenerator)
        val game = makeGame()
        game.opponent().amber = 1
        game.activePlayer().hand.add("ghostly-hand")

        executor.execute(game, PlayCardAction("ghostly-hand"))

        assertThat(game.opponent().amber).isEqualTo(0)
        assertThat(game.activePlayer().amber).isEqualTo(1)
    }

    @Test
    fun `Ghostly Hand does nothing when opponent does not have exactly 1 amber`() {
        val executor = ActionExecutor(libraryWithTier1Cards(), idGenerator)
        val game = makeGame()
        game.opponent().amber = 2
        game.activePlayer().hand.add("ghostly-hand")

        executor.execute(game, PlayCardAction("ghostly-hand"))

        assertThat(game.opponent().amber).isEqualTo(2)
        assertThat(game.activePlayer().amber).isEqualTo(0)
    }

    private fun makeCard(id: String): CatalogCard {
        return CatalogCard(
            id = id,
            name = id,
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
            keywords = emptyList(),
            traits = emptyList()
        )
    }
}
