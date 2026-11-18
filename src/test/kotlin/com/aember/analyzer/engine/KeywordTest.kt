package com.aember.analyzer.engine

import com.aember.analyzer.domain.engine.action.*
import com.aember.analyzer.domain.engine.dsl.*
import com.aember.analyzer.domain.engine.model.*
import com.aember.analyzer.engine.EngineTestHelper.creature
import com.aember.analyzer.engine.EngineTestHelper.dslCard
import com.aember.analyzer.engine.EngineTestHelper.makeGame
import com.aember.analyzer.engine.EngineTestHelper.makeLibrary
import com.aember.analyzer.engine.EngineTestHelper.IncrementingIdGenerator
import com.aember.analyzer.engine.EngineTestHelper.playCard
import com.aember.analyzer.domain.model.House
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class KeywordTest {

    private val library = makeLibrary()
    private val idGenerator = IncrementingIdGenerator()
    private val executor = ActionExecutor(library, idGenerator)

    @Test
    fun `elusive prevents damage to defender on the first attack each turn`() {
        val attacker = creature("attacker", 1, power = 3)
        val defender = creature("defender", 2, power = 3, keywords = mapOf("elusive" to 1))
        val game = makeGame(
            player0Creatures = listOf(attacker),
            player1Creatures = listOf(defender)
        )

        executor.execute(game, UseCreatureAction(attacker.instanceId, FightUse(defender.instanceId)))

        assertThat(defender.damage).isEqualTo(0)
        assertThat(attacker.damage).isEqualTo(3)
    }

    @Test
    fun `elusive does not prevent damage on the second attack`() {
        val attacker = creature("attacker", 1, power = 3)
        val defender = creature("defender", 2, power = 3, keywords = mapOf("elusive" to 1))
        defender.elusiveUsed = true
        val game = makeGame(
            player0Creatures = listOf(attacker),
            player1Creatures = listOf(defender)
        )

        executor.execute(game, UseCreatureAction(attacker.instanceId, FightUse(defender.instanceId)))

        assertThat(defender.damage).isEqualTo(3)
        assertThat(attacker.damage).isEqualTo(3)
    }

    @Test
    fun `skirmish prevents counter damage to attacker`() {
        val attacker = creature("attacker", 1, power = 3, keywords = mapOf("skirmish" to 1))
        val defender = creature("defender", 2, power = 5)
        val game = makeGame(
            player0Creatures = listOf(attacker),
            player1Creatures = listOf(defender)
        )

        executor.execute(game, UseCreatureAction(attacker.instanceId, FightUse(defender.instanceId)))

        assertThat(attacker.damage).isEqualTo(0)
        assertThat(defender.damage).isEqualTo(3)
    }

    @Test
    fun `hazardous deals damage to attacker before normal fight damage`() {
        val attacker = creature("attacker", 1, power = 5, armor = 0)
        val defender = creature("defender", 2, power = 1, keywords = mapOf("hazardous" to 2))
        val game = makeGame(
            player0Creatures = listOf(attacker),
            player1Creatures = listOf(defender)
        )

        executor.execute(game, UseCreatureAction(attacker.instanceId, FightUse(defender.instanceId)))

        // Hazardous 2 hits attacker, then defender's power 1 also hits attacker.
        assertThat(attacker.damage).isEqualTo(3)
        assertThat(defender.damage).isEqualTo(5)
    }

    @Test
    fun `assault deals damage to defender before normal fight damage`() {
        val attacker = creature("attacker", 1, power = 1, keywords = mapOf("assault" to 3))
        val defender = creature("defender", 2, power = 4, armor = 0)
        val game = makeGame(
            player0Creatures = listOf(attacker),
            player1Creatures = listOf(defender)
        )

        executor.execute(game, UseCreatureAction(attacker.instanceId, FightUse(defender.instanceId)))

        // Assault 3 + power 1 = 4 damage to defender; defender power 4 counters for 4.
        assertThat(defender.damage).isEqualTo(4)
        assertThat(attacker.damage).isEqualTo(4)
    }

    @Test
    fun `taunt restricts legal fight targets to taunt creatures`() {
        val attacker = creature("attacker", 1, power = 3)
        val taunt = creature("taunt", 2, power = 3, keywords = mapOf("taunt" to 1))
        val nonTaunt = creature("non-taunt", 3, power = 3)
        val game = makeGame(
            player0Creatures = listOf(attacker),
            player1Creatures = listOf(taunt, nonTaunt)
        )
        game.activePlayer().activeHouse = House.BROBNAR

        val generator = ActionGenerator(library)
        val actions = generator.generate(game)
            .filterIsInstance<UseCreatureAction>()
            .filter { it.useType is FightUse }

        val targets = actions.map { (it.useType as FightUse).targetInstanceId }
        assertThat(targets).containsExactly(taunt.instanceId)
    }

    @Test
    fun `alpha prevents a card from being played after other actions this phase`() {
        val game = makeGame()
        game.activePlayer().activeHouse = House.BROBNAR
        game.actionsThisPhase = 1
        val alphaLibrary = makeLibrary(
            mapOf("alpha-card" to listOf("alpha")),
            "alpha-card" to dslCard { play(GainAmber(1)) }
        )
        game.activePlayer().hand.add("alpha-card")

        val generator = ActionGenerator(alphaLibrary)
        val actions = generator.generate(game).filterIsInstance<PlayCardAction>()

        assertThat(actions).isEmpty()
    }

    @Test
    fun `omega ends the main phase immediately after being played`() {
        val game = makeGame()
        game.activePlayer().activeHouse = House.BROBNAR
        val omegaLibrary = makeLibrary(
            mapOf("omega-card" to listOf("omega")),
            "omega-card" to dslCard { play(GainAmber(1)) }
        )
        val omegaExecutor = ActionExecutor(omegaLibrary, idGenerator)

        playCard(game, omegaExecutor, "omega-card")

        assertThat(game.endPhaseRightNow).isTrue()
    }
}
