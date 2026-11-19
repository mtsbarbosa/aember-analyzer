package com.aember.analyzer.engine

import com.aember.analyzer.domain.engine.CardLibrary
import com.aember.analyzer.domain.engine.action.ActionExecutor
import com.aember.analyzer.domain.engine.action.FightUse
import com.aember.analyzer.domain.engine.action.PlayCardAction
import com.aember.analyzer.domain.engine.action.UseCreatureAction
import com.aember.analyzer.domain.engine.cards.Drumble
import com.aember.analyzer.domain.engine.cards.EtherSpider
import com.aember.analyzer.domain.engine.model.GameState
import com.aember.analyzer.domain.engine.model.InPlayCard
import com.aember.analyzer.domain.engine.model.PlayerState
import com.aember.analyzer.domain.engine.model.Zone
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

class AmberControlCardsTest {

    private val idGenerator = IncrementingIdGenerator()

    private fun libraryWithAmberControlCards(): CardLibrary {
        val catalog = mapOf(
            "drumble" to CatalogCard(
                id = "drumble",
                name = "Drumble",
                number = "082",
                image = "",
                expansion = 341,
                house = House.DIS,
                type = CardType.CREATURE,
                rarity = Rarity.UNCOMMON,
                amber = 0,
                armor = 0,
                power = 2,
                text = "Elusive. Play: If your opponent has 7<A> or more, capture all of it.",
                keywords = listOf("elusive"),
                traits = listOf("imp")
            ),
            "ether-spider" to CatalogCard(
                id = "ether-spider",
                name = "Ether Spider",
                number = "192",
                image = "",
                expansion = 341,
                house = House.MARS,
                type = CardType.CREATURE,
                rarity = Rarity.UNCOMMON,
                amber = 0,
                armor = 0,
                power = 7,
                text = "Ether Spider deals no damage when fighting. Each <A> that would be added to your opponent's pool is captured by Ether Spider instead.",
                keywords = emptyList(),
                traits = listOf("beast")
            ),
            "punch" to CatalogCard(
                id = "punch",
                name = "Punch",
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
        )
        val library = CardLibrary(catalog)
        library.register("drumble", Drumble)
        library.register("ether-spider", EtherSpider)
        return library
    }

    @Test
    fun `Drumble captures all opponent amber when they have 7 or more`() {
        val library = libraryWithAmberControlCards()
        val executor = ActionExecutor(library, idGenerator)
        val game = makeGame()
        game.activePlayer().hand.add("drumble")
        game.opponent().amber = 8

        executor.execute(game, PlayCardAction("drumble"))

        assertThat(game.opponent().amber).isEqualTo(0)
        val drumble = game.activePlayer().creatures.find { it.cardId == "drumble" }
        assertThat(drumble).isNotNull
        assertThat(drumble!!.capturedAmber).isEqualTo(8)
    }

    @Test
    fun `Drumble does nothing when opponent has less than 7 amber`() {
        val library = libraryWithAmberControlCards()
        val executor = ActionExecutor(library, idGenerator)
        val game = makeGame()
        game.activePlayer().hand.add("drumble")
        game.opponent().amber = 5

        executor.execute(game, PlayCardAction("drumble"))

        assertThat(game.opponent().amber).isEqualTo(5)
        val drumble = game.activePlayer().creatures.find { it.cardId == "drumble" }
        assertThat(drumble!!.capturedAmber).isEqualTo(0)
    }

    @Test
    fun `Ether Spider redirects opponent reap amber to captured amber`() {
        val library = libraryWithAmberControlCards()
        val executor = ActionExecutor(library, idGenerator)
        val etherSpider = creature("ether-spider", 1, owner = 1, power = 7)
        val reaper = creature("reaper", 2, owner = 0, power = 3)
        val game = makeGame(player0Creatures = listOf(reaper), player1Creatures = listOf(etherSpider))
        reaper.exhausted = false

        executor.execute(game, UseCreatureAction(reaper.instanceId, com.aember.analyzer.domain.engine.action.ReapUse))

        assertThat(game.activePlayer().amber).isEqualTo(0)
        assertThat(etherSpider.capturedAmber).isEqualTo(1)
    }

    @Test
    fun `Ether Spider redirects direct amber gain effects`() {
        val etherSpider = creature("ether-spider", 1, owner = 1, power = 7)
        val game = makeGame(player1Creatures = listOf(etherSpider))
        game.activePlayer().amber = 0

        game.gainAmber(game.activePlayer(), 3, "test gain")

        assertThat(game.activePlayer().amber).isEqualTo(0)
        assertThat(etherSpider.capturedAmber).isEqualTo(3)
    }

    @Test
    fun `Ether Spider deals no damage as attacker`() {
        val library = libraryWithAmberControlCards()
        val executor = ActionExecutor(library, idGenerator)
        val spider = creature("ether-spider", 1, owner = 0, power = 7)
        spider.exhausted = false
        val defender = creature("defender", 2, owner = 1, power = 3)
        val game = makeGame(player0Creatures = listOf(spider), player1Creatures = listOf(defender))

        executor.execute(game, UseCreatureAction(spider.instanceId, FightUse(defender.instanceId)))

        assertThat(defender.damage).isEqualTo(0)
        assertThat(spider.damage).isEqualTo(3)
    }

    @Test
    fun `Ether Spider deals no damage as defender`() {
        val library = libraryWithAmberControlCards()
        val executor = ActionExecutor(library, idGenerator)
        val spider = creature("ether-spider", 1, owner = 1, power = 7)
        val attacker = creature("attacker", 2, owner = 0, power = 3)
        attacker.exhausted = false
        val game = makeGame(player0Creatures = listOf(attacker), player1Creatures = listOf(spider))

        executor.execute(game, UseCreatureAction(attacker.instanceId, FightUse(spider.instanceId)))

        assertThat(spider.damage).isEqualTo(3)
        assertThat(attacker.damage).isEqualTo(0)
    }
}
