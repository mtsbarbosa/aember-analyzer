package com.aember.analyzer.engine

import com.aember.analyzer.domain.engine.TurnPhases
import com.aember.analyzer.domain.engine.action.ActionExecutor
import com.aember.analyzer.domain.engine.action.DiscardAction
import com.aember.analyzer.domain.engine.action.FightUse
import com.aember.analyzer.domain.engine.action.PlayCardAction
import com.aember.analyzer.domain.engine.action.ReapUse
import com.aember.analyzer.domain.engine.action.UseCreatureAction
import com.aember.analyzer.domain.engine.model.GameState
import com.aember.analyzer.domain.engine.model.InPlayCard
import com.aember.analyzer.domain.engine.model.Zone
import com.aember.analyzer.domain.model.CardType
import com.aember.analyzer.domain.model.House
import com.aember.analyzer.engine.EngineTestHelper.IncrementingIdGenerator
import com.aember.analyzer.engine.EngineTestHelper.emptyDeck
import com.aember.analyzer.engine.EngineTestHelper.makeGame
import com.aember.analyzer.engine.support.CoreCardLibrary
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

/**
 * Phase 2.2 — event bus and reactions ("Each time ...", "After ...",
 * "Leaves Play:"). Uses the real production Core card library.
 */
class ReactionsTest {

    private val library = CoreCardLibrary.create()
    private val idGenerator = IncrementingIdGenerator()

    private fun executor() = ActionExecutor(library, idGenerator)

    private fun game() = makeGame(player0Deck = emptyDeck(), player1Deck = emptyDeck())

    private fun creature(
        cardId: String,
        owner: Int,
        power: Int,
        house: House = House.BROBNAR,
        index: Int = 0
    ): InPlayCard = InPlayCard(
        instanceId = "$cardId-$owner-$index",
        cardId = cardId,
        ownerIndex = owner,
        controllerIndex = owner,
        zone = Zone.PLAY_AREA,
        house = house,
        type = CardType.CREATURE,
        exhausted = false,
        power = power
    )

    /** Runs any action so the post-action state check destroys lethal-damaged creatures. */
    private fun flushStateCheck(game: GameState) {
        val player = game.activePlayer()
        val filler = if (player.hand.isEmpty()) "anger" else player.hand.first()
        player.hand.add(filler)
        executor().execute(game, DiscardAction(filler))
    }

    @Test
    fun `Soul Snatcher - destroyed creature's owner gains 1 amber`() {
        val game = game()
        val player = game.activePlayer()
        val opponent = game.opponent()
        val victim = creature("test-victim", 1, power = 3)
        val attacker = creature("test-attacker", 0, power = 5)
        opponent.creatures.add(victim)
        player.creatures.add(attacker)
        player.hand.add("soul-snatcher")

        executor().execute(game, PlayCardAction("soul-snatcher"))
        executor().execute(game, UseCreatureAction(attacker.instanceId, FightUse(victim.instanceId)))
        flushStateCheck(game)

        // The victim (owned by the opponent) was destroyed; its owner gains 1 amber.
        assertThat(opponent.amber).isEqualTo(1)
        assertThat(player.amber).isEqualTo(0)
        assertThat(opponent.creatures).doesNotContain(victim)
    }

    @Test
    fun `Tolas - destroyed creature's opponent gains 1 amber`() {
        val game = game()
        val player = game.activePlayer()
        val opponent = game.opponent()
        val tolas = creature("tolas", 0, power = 4, house = House.UNTAMED)
        val victim = creature("test-victim", 1, power = 2)
        victim.damage = 2
        player.creatures.add(tolas)
        opponent.creatures.add(victim)
        opponent.amber = 0
        player.amber = 0

        flushStateCheck(game)

        // Enemy creature destroyed -> its opponent (the tolas player) gains 1 amber.
        assertThat(player.amber).isEqualTo(1)
        assertThat(opponent.amber).isEqualTo(0)
    }

    @Test
    fun `Pile of Skulls - enemy destroyed on your turn captures 1 amber`() {
        val game = game()
        val player = game.activePlayer()
        val opponent = game.opponent()
        val pile = creature("pile-of-skulls", 0, power = 2, house = House.SHADOWS)
        val ally = creature("test-ally", 0, power = 3, index = 1)
        val victim = creature("test-victim", 1, power = 2)
        victim.damage = 2
        player.creatures.add(pile)
        player.creatures.add(ally)
        opponent.creatures.add(victim)
        opponent.amber = 3

        flushStateCheck(game)

        assertThat(ally.capturedAmber).isEqualTo(1)
        assertThat(opponent.amber).isEqualTo(2)
    }

    @Test
    fun `Pile of Skulls does not trigger on the opponent's turn`() {
        val game = makeGame(activePlayerIndex = 1, player0Deck = emptyDeck(), player1Deck = emptyDeck())
        val player = game.player(0)
        val opponent = game.activePlayer()
        val pile = creature("pile-of-skulls", 0, power = 2, house = House.SHADOWS)
        val ally = creature("test-ally", 0, power = 3, index = 1)
        val victim = creature("test-victim", 1, power = 2)
        victim.damage = 2
        player.creatures.add(pile)
        player.creatures.add(ally)
        opponent.creatures.add(victim)
        opponent.amber = 3

        // The active player is the opponent: an enemy creature destroyed during THEIR turn.
        flushStateCheck(game)

        assertThat(ally.capturedAmber).isEqualTo(0)
        assertThat(opponent.amber).isEqualTo(3)
    }

    @Test
    fun `Krump - enemy creature destroyed fighting Krump, its controller loses 1 amber`() {
        val game = game()
        val player = game.activePlayer()
        val opponent = game.opponent()
        val krump = creature("krump", 0, power = 5, house = House.BROBNAR)
        val victim = creature("test-victim", 1, power = 3)
        player.creatures.add(krump)
        opponent.creatures.add(victim)
        opponent.amber = 2

        executor().execute(game, UseCreatureAction(krump.instanceId, FightUse(victim.instanceId)))
        flushStateCheck(game)

        assertThat(opponent.amber).isEqualTo(1)
        assertThat(victim.damage).isGreaterThanOrEqualTo(3)
    }

    @Test
    fun `Grommid - enemy creature destroyed fighting Grommid, your opponent loses 1 amber`() {
        val game = game()
        val player = game.activePlayer()
        val opponent = game.opponent()
        val grommid = creature("grommid", 0, power = 5, house = House.BROBNAR)
        val victim = creature("test-victim", 1, power = 3)
        player.creatures.add(grommid)
        opponent.creatures.add(victim)
        opponent.amber = 2

        executor().execute(game, UseCreatureAction(grommid.instanceId, FightUse(victim.instanceId)))
        flushStateCheck(game)

        assertThat(opponent.amber).isEqualTo(1)
    }

    @Test
    fun `Carlo Phantom - playing an artifact steals 1 amber`() {
        val game = game()
        val player = game.activePlayer()
        val opponent = game.opponent()
        val carlo = creature("carlo-phantom", 0, power = 2, house = House.SHADOWS)
        player.creatures.add(carlo)
        player.hand.add("banner-of-battle")
        opponent.amber = 2
        player.amber = 0

        executor().execute(game, PlayCardAction("banner-of-battle"))

        assertThat(player.amber).isEqualTo(1)
        assertThat(opponent.amber).isEqualTo(1)
    }

    @Test
    fun `Looter Goblin - reap activates the turn subscription`() {
        val game = game()
        val player = game.activePlayer()
        val opponent = game.opponent()
        val looter = creature("looter-goblin", 0, power = 1, house = House.SHADOWS)
        val victim = creature("test-victim", 1, power = 2)
        victim.damage = 2
        player.creatures.add(looter)
        opponent.creatures.add(victim)
        player.amber = 0

        executor().execute(game, UseCreatureAction(looter.instanceId, ReapUse))
        flushStateCheck(game)

        // 1 base reap amber + 1 amber per enemy creature destroyed this turn.
        assertThat(player.amber).isEqualTo(2)
    }

    @Test
    fun `Loot the Bodies - play activates the turn subscription`() {
        val game = game()
        val player = game.activePlayer()
        val opponent = game.opponent()
        val victim = creature("test-victim", 1, power = 2)
        victim.damage = 2
        opponent.creatures.add(victim)
        player.hand.add("loot-the-bodies")
        player.amber = 0

        executor().execute(game, PlayCardAction("loot-the-bodies"))
        flushStateCheck(game)

        // The action card left play; the subscription still fired.
        assertThat(player.amber).isEqualTo(1)
    }

    @Test
    fun `Magda the Rat - leaves play reaction`() {
        val game = game()
        val player = game.activePlayer()
        val opponent = game.opponent()
        val magda = creature("magda-the-rat", 0, power = 2, house = House.SHADOWS)
        player.creatures.add(magda)
        player.hand.add("magda-the-rat")
        opponent.amber = 4
        player.amber = 0

        executor().execute(game, PlayCardAction("magda-the-rat"))
        assertThat(player.amber).isEqualTo(2)
        assertThat(opponent.amber).isEqualTo(2)

        // Destroy Magda: her own Leaves Play reaction fires (opponent steals 2).
        magda.damage = 10
        flushStateCheck(game)

        assertThat(player.amber).isEqualTo(0)
        assertThat(opponent.amber).isEqualTo(4)
        assertThat(player.creatures).doesNotContain(magda)
    }

    @Test
    fun `Brain Stem Antenna - Mars play readies the host until end of turn`() {
        val game = game()
        val player = game.activePlayer()
        val host = creature("test-host", 0, power = 3, house = House.MARS)
        host.exhausted = true
        player.creatures.add(host)
        player.hand.add("brain-stem-antenna")
        player.hand.add("smiling-ruth") // stand-in Mars creature? no — use a real Mars card

        executor().execute(game, PlayCardAction("brain-stem-antenna", targetCreatureId = host.instanceId))

        val marsCreatureId = "zorg" // enters stunned, but still a Mars creature play
        player.hand.remove("smiling-ruth")
        player.hand.add(marsCreatureId)
        executor().execute(game, PlayCardAction(marsCreatureId))

        assertThat(host.exhausted).isFalse()
        assertThat(host.house).isEqualTo(House.MARS)
    }

    @Test
    fun `temporary house change resets at turn start`() {
        val game = game()
        val player = game.activePlayer()
        val host = creature("test-host", 0, power = 3, house = House.MARS)
        host.exhausted = true
        player.creatures.add(host)
        player.hand.add("brain-stem-antenna")
        player.hand.add("zorg")

        executor().execute(game, PlayCardAction("brain-stem-antenna", targetCreatureId = host.instanceId))
        executor().execute(game, PlayCardAction("zorg"))
        assertThat(host.house).isEqualTo(House.MARS)

        TurnPhases.startTurn(game, library)
        assertThat(host.house).isEqualTo(House.MARS)

        host.house = House.DIS
        TurnPhases.startTurn(game, library)
        assertThat(host.house).isEqualTo(House.MARS) // restored from baseHouse
    }

    @Test
    fun `Annihilation Ritual - destroyed creatures are purged instead of discarded`() {
        val game = game()
        val player = game.activePlayer()
        val opponent = game.opponent()
        val victim = creature("test-victim", 1, power = 3)
        victim.damage = 3
        opponent.creatures.add(victim)
        player.hand.add("annihilation-ritual")
        opponent.discard.clear()

        executor().execute(game, PlayCardAction("annihilation-ritual"))
        flushStateCheck(game)

        assertThat(opponent.purged).contains("test-victim")
        assertThat(opponent.discard).doesNotContain("test-victim")
        assertThat(opponent.creatures).doesNotContain(victim)
    }

    @Test
    fun `Soul Snatcher does not react when Biomatrix Backup archives the creature instead`() {
        val game = game()
        val player = game.activePlayer()
        val opponent = game.opponent()
        val victim = creature("test-victim", 1, power = 3)
        val backup = creature("biomatrix-backup", 1, power = 0)
        victim.upgrades.add(backup)
        val attacker = creature("test-attacker", 0, power = 5)
        opponent.creatures.add(victim)
        player.creatures.add(attacker)
        player.hand.add("soul-snatcher")

        executor().execute(game, PlayCardAction("soul-snatcher"))
        executor().execute(game, UseCreatureAction(attacker.instanceId, FightUse(victim.instanceId)))
        flushStateCheck(game)

        assertThat(opponent.archives).contains("test-victim")
        assertThat(opponent.amber).isEqualTo(0)
    }

    @Test
    fun `temporary house change is restored at end of turn`() {
        val game = game()
        val c = creature("test-host", 0, power = 3)
        game.activePlayer().creatures.add(c)
        c.house = House.MARS

        TurnPhases.endPhase(game)

        assertThat(c.house).isEqualTo(c.baseHouse)
    }
}
