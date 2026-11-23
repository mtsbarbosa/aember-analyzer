package com.aember.analyzer.engine

import com.aember.analyzer.domain.engine.ForgeRules
import com.aember.analyzer.domain.engine.action.ActionExecutor
import com.aember.analyzer.domain.engine.action.PlayCardAction
import com.aember.analyzer.domain.engine.action.UseCreatureAction
import com.aember.analyzer.domain.engine.action.FightUse
import com.aember.analyzer.domain.engine.effects.EffectRegistry
import com.aember.analyzer.domain.engine.model.InPlayCard
import com.aember.analyzer.domain.engine.model.Keyword
import com.aember.analyzer.domain.engine.model.Zone
import com.aember.analyzer.domain.engine.model.hasKeyword
import com.aember.analyzer.domain.model.CardType
import com.aember.analyzer.domain.model.House
import com.aember.analyzer.engine.EngineTestHelper.IncrementingIdGenerator
import com.aember.analyzer.engine.EngineTestHelper.emptyDeck
import com.aember.analyzer.engine.EngineTestHelper.makeGame
import com.aember.analyzer.engine.support.CoreCardLibrary
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

/**
 * Phase 2.1 — persistent static effects (EffectRegistry): power/armor/keyword
 * auras and key-cost modifiers. Uses the real production Core card library.
 */
class PersistentEffectsTest {

    private val library = CoreCardLibrary.create()
    private val idGenerator = IncrementingIdGenerator()

    private fun executor() = ActionExecutor(library, idGenerator)

    private fun game() = makeGame(
        player0Deck = emptyDeck(),
        player1Deck = emptyDeck()
    )

    private fun creature(
        cardId: String,
        owner: Int,
        power: Int,
        house: House = House.BROBNAR,
        traits: List<String> = emptyList(),
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
        power = power,
        traits = traits
    )

    private fun play(game: com.aember.analyzer.domain.engine.model.GameState, cardId: String, targetCreatureId: String? = null, placementIndex: Int? = null) {
        game.activePlayer().hand.add(cardId)
        executor().execute(
            game,
            PlayCardAction(cardId, targetCreatureId = targetCreatureId, placementIndex = placementIndex ?: 0)
        )
    }

    @Test
    fun `Banner of Battle gives friendly creatures plus 1 power`() {
        val game = game()
        val player = game.activePlayer()
        val ally = creature("test-ally", 0, power = 3)
        val enemy = creature("test-enemy", 1, power = 3)
        player.creatures.add(ally)
        game.opponent().creatures.add(enemy)

        play(game, "banner-of-battle")

        assertThat(ally.power).isEqualTo(4)
        assertThat(ally.basePower).isEqualTo(3)
        assertThat(enemy.power).isEqualTo(3)
    }

    @Test
    fun `King of the Crag weakens enemy Brobnar creatures`() {
        val game = game()
        val player = game.activePlayer()
        val enemyGiant = creature("test-giant", 1, power = 5, house = House.BROBNAR)
        val enemyMars = creature("test-martian", 1, power = 5, house = House.MARS)
        game.opponent().creatures.add(enemyGiant)
        game.opponent().creatures.add(enemyMars)

        play(game, "king-of-the-crag")

        assertThat(enemyGiant.power).isEqualTo(3)
        assertThat(enemyMars.power).isEqualTo(5)
    }

    @Test
    fun `removing an aura restores base stats`() {
        val game = game()
        val player = game.activePlayer()
        val ally = creature("test-ally", 0, power = 3)
        player.creatures.add(ally)
        play(game, "banner-of-battle")
        assertThat(ally.power).isEqualTo(4)

        // Destroy the artifact by direct removal + re-evaluation.
        player.artifacts.clear()
        EffectRegistry.recalculate(library, game)

        assertThat(ally.power).isEqualTo(3)
    }

    @Test
    fun `aura loss that drops power below damage destroys the creature`() {
        val game = game()
        val player = game.activePlayer()
        val weak = creature("test-weak", 1, power = 2)
        weak.damage = 1
        game.opponent().creatures.add(weak)

        // King of the Crag gives enemy Brobnar creatures -2 power: 2 - 2 = 0, lethal.
        play(game, "king-of-the-crag")

        assertThat(game.opponent().creatures).doesNotContain(weak)
        assertThat(game.opponent().discard).contains("test-weak")
    }

    @Test
    fun `Bulwark gives its neighbours plus 2 armor`() {
        val game = game()
        val player = game.activePlayer()
        val left = creature("test-left", 0, power = 3)
        val bulwark = creature("bulwark", 0, power = 4, index = 1)
        val right = creature("test-right", 0, power = 3)
        player.creatures.add(left)
        player.creatures.add(bulwark)
        player.creatures.add(right)

        EffectRegistry.recalculate(library, game)

        assertThat(left.armor).isEqualTo(2)
        assertThat(right.armor).isEqualTo(2)
        assertThat(bulwark.armor).isEqualTo(bulwark.baseArmor)
    }

    @Test
    fun `Staunch Knight gets plus 2 power only while on a flank`() {
        val game = game()
        val player = game.activePlayer()
        val knight = creature("staunch-knight", 0, power = 4)
        val middle = creature("test-mid", 0, power = 3)
        player.creatures.add(knight)
        player.creatures.add(middle)

        EffectRegistry.recalculate(library, game)
        assertThat(knight.power).isEqualTo(6)

        // Move the knight to the middle of a three-creature battleline: not a flank anymore.
        val right = creature("test-right", 0, power = 3, index = 2)
        player.creatures.clear()
        player.creatures.add(middle)
        player.creatures.add(knight)
        player.creatures.add(right)
        EffectRegistry.recalculate(library, game)
        assertThat(knight.power).isEqualTo(4)
    }

    @Test
    fun `Shoulder Armor buffs a flank creature only`() {
        val game = game()
        val player = game.activePlayer()
        val flank = creature("test-flank", 0, power = 3)
        val middle = creature("test-mid", 0, power = 3)
        val right = creature("test-right", 0, power = 3, index = 2)
        player.creatures.add(flank)
        player.creatures.add(middle)
        player.creatures.add(right)

        play(game, "shoulder-armor", targetCreatureId = flank.instanceId)
        assertThat(flank.power).isEqualTo(5)
        assertThat(flank.armor).isEqualTo(2)
        assertThat(middle.power).isEqualTo(3)

        play(game, "shoulder-armor", targetCreatureId = middle.instanceId)
        assertThat(middle.power).isEqualTo(3)
        assertThat(middle.armor).isEqualTo(0)
    }

    @Test
    fun `Ring of Invisibility grants elusive`() {
        val game = makeGame(activePlayerIndex = 1, player0Deck = emptyDeck(), player1Deck = emptyDeck())
        val player1 = game.activePlayer()
        val player0 = game.player(0)
        val host = creature("test-host", 0, power = 3)
        val attacker = creature("test-attacker", 1, power = 3, index = 0, house = House.MARS)
        player0.creatures.add(host)
        player1.creatures.add(attacker)

        player1.hand.add("ring-of-invisibility")
        executor().execute(game, PlayCardAction("ring-of-invisibility", targetCreatureId = host.instanceId))
        assertThat(host.keywords).containsEntry("elusive", 1).containsEntry("skirmish", 1)

        executor().execute(game, UseCreatureAction(attacker.instanceId, FightUse(host.instanceId)))

        // Elusive: the host takes no damage on the first attack; the attacker eats the counter.
        assertThat(host.damage).isEqualTo(0)
        assertThat(attacker.damage).isEqualTo(3)
    }

    @Test
    fun `Ring of Invisibility grants skirmish`() {
        val game = game()
        val player = game.activePlayer()
        val host = creature("test-host", 0, power = 3)
        val target = creature("test-target", 1, power = 5, index = 0, house = House.MARS)
        player.creatures.add(host)
        game.opponent().creatures.add(target)

        play(game, "ring-of-invisibility", targetCreatureId = host.instanceId)

        executor().execute(game, UseCreatureAction(host.instanceId, FightUse(target.instanceId)))

        // Skirmish: the attacking host takes no counter-damage.
        assertThat(host.damage).isEqualTo(0)
        assertThat(target.damage).isEqualTo(3)
    }

    @Test
    fun `Grey Monk gives friendly creatures plus 1 armor`() {
        val game = game()
        val player = game.activePlayer()
        val monk = creature("grey-monk", 0, power = 3)
        val ally = creature("test-ally", 0, power = 3)
        player.creatures.add(monk)
        player.creatures.add(ally)
        val enemy = creature("test-enemy", 1, power = 3)
        game.opponent().creatures.add(enemy)

        EffectRegistry.recalculate(library, game)

        assertThat(monk.armor).isEqualTo(1)
        assertThat(ally.armor).isEqualTo(1)
        assertThat(enemy.armor).isEqualTo(0)
    }

    @Test
    fun `Niffle Queen buffs other friendly Niffle and Beast creatures but not herself`() {
        val game = game()
        val player = game.activePlayer()
        val queen = creature("niffle-queen", 0, power = 6, traits = listOf("beast", "niffle"))
        val ape = creature("niffle-ape", 0, power = 3, traits = listOf("beast", "niffle"), index = 1)
        val stranger = creature("test-stranger", 0, power = 3, index = 2)
        player.creatures.add(queen)
        player.creatures.add(ape)
        player.creatures.add(stranger)

        EffectRegistry.recalculate(library, game)

        // The ape has both traits, so it benefits from both sentences.
        assertThat(ape.power).isEqualTo(5)
        assertThat(queen.power).isEqualTo(6)
        assertThat(stranger.power).isEqualTo(3)
    }

    @Test
    fun `Round Table gives friendly Knights plus 1 power and taunt`() {
        val game = game()
        val player = game.activePlayer()
        val knight = creature("test-knight", 0, power = 4, traits = listOf("knight"))
        val rogue = creature("test-rogue", 0, power = 4, index = 1)
        player.creatures.add(knight)
        player.creatures.add(rogue)

        play(game, "round-table")

        assertThat(knight.power).isEqualTo(5)
        assertThat(knight.hasKeyword(Keyword.TAUNT)).isTrue()
        assertThat(rogue.power).isEqualTo(4)
        assertThat(rogue.hasKeyword(Keyword.TAUNT)).isFalse()
    }

    @Test
    fun `Yxili Marauder gets plus 1 power per captured amber`() {
        val game = game()
        val player = game.activePlayer()
        val marauder = creature("yxili-marauder", 0, power = 2, house = House.MARS)
        marauder.capturedAmber = 3
        player.creatures.add(marauder)

        EffectRegistry.recalculate(library, game)

        assertThat(marauder.power).isEqualTo(5)
    }

    @Test
    fun `Mushroom Man gets plus 3 power per unforged key`() {
        val game = game()
        val player = game.activePlayer()
        player.keys = 1
        val mushroom = creature("mushroom-man", 0, power = 2)
        player.creatures.add(mushroom)

        EffectRegistry.recalculate(library, game)

        assertThat(mushroom.power).isEqualTo(2 + 6) // 2 unforged keys

        player.keys = 3
        EffectRegistry.recalculate(library, game)
        assertThat(mushroom.power).isEqualTo(2)
    }

    @Test
    fun `Titan Mechanic reduces key cost while on a flank`() {
        val game = game()
        val player = game.activePlayer()
        val mechanic = creature("titan-mechanic", 0, power = 6, house = House.SANCTUM)
        player.creatures.add(mechanic)

        assertThat(ForgeRules.keyCost(library, game, player)).isEqualTo(5)

        val other = creature("test-other", 0, power = 3, index = 1)
        val middle = creature("test-mid", 0, power = 3, index = 2)
        player.creatures.add(other)
        player.creatures.add(middle)
        // Mechanic is still on a flank (leftmost).
        assertThat(ForgeRules.keyCost(library, game, player)).isEqualTo(5)

        // Push the mechanic to the middle.
        player.creatures.remove(mechanic)
        player.creatures.add(1, mechanic)
        assertThat(ForgeRules.keyCost(library, game, player)).isEqualTo(6)
    }

    @Test
    fun `Titan Mechanic reduces key cost for both players`() {
        val game = game()
        val player = game.activePlayer()
        player.creatures.add(creature("titan-mechanic", 0, power = 6, house = House.SANCTUM))

        assertThat(ForgeRules.keyCost(library, game, game.opponent())).isEqualTo(5)
    }

    @Test
    fun `Jammer Pack adds 2 to the opponent's key cost`() {
        val game = game()
        val player = game.activePlayer()
        val opponent = game.opponent()
        val enemy = creature("test-enemy", 1, power = 3)
        opponent.creatures.add(enemy)

        play(game, "jammer-pack", targetCreatureId = enemy.instanceId)

        // "This creature gains: your opponent's keys cost +2" — from the HOST's perspective,
        // so the opponent of the host's controller pays.
        assertThat(ForgeRules.keyCost(library, game, player)).isEqualTo(8)
        assertThat(ForgeRules.keyCost(library, game, opponent)).isEqualTo(6)
    }

    @Test
    fun `Protect the Weak grants armor and taunt to the host`() {
        val game = game()
        val player = game.activePlayer()
        val host = creature("test-host", 0, power = 3)
        player.creatures.add(host)

        play(game, "protect-the-weak", targetCreatureId = host.instanceId)

        assertThat(host.armor).isEqualTo(1)
        assertThat(host.hasKeyword(Keyword.TAUNT)).isTrue()
    }

    @Test
    fun `Blood of Titans and Flame-Wreathed power up the host`() {
        val game = game()
        val player = game.activePlayer()
        val host = creature("test-host", 0, power = 3)
        player.creatures.add(host)

        play(game, "blood-of-titans", targetCreatureId = host.instanceId)
        assertThat(host.power).isEqualTo(8)
    }

    @Test
    fun `Halacor grants skirmish to friendly flank creatures`() {
        val game = game()
        val player = game.activePlayer()
        val flanker = creature("test-flanker", 0, power = 3)
        val halacor = creature("halacor", 0, power = 4, index = 1)
        val mid = creature("test-mid", 0, power = 3, index = 2)
        val right = creature("test-right", 0, power = 3, index = 3)
        player.creatures.add(flanker)
        player.creatures.add(halacor)
        player.creatures.add(mid)
        player.creatures.add(right)

        EffectRegistry.recalculate(library, game)

        assertThat(flanker.hasKeyword(Keyword.SKIRMISH)).isTrue()
        assertThat(mid.hasKeyword(Keyword.SKIRMISH)).isFalse()
    }

    @Test
    fun `aura boosts fight damage through effective power`() {
        val game = game()
        val player = game.activePlayer()
        val queen = creature("niffle-queen", 0, power = 6, traits = listOf("beast", "niffle"))
        val ape = creature("niffle-ape", 0, power = 3, traits = listOf("beast", "niffle"), index = 1)
        val victim = creature("test-victim", 1, power = 10)
        player.creatures.add(queen)
        player.creatures.add(ape)
        game.opponent().creatures.add(victim)
        EffectRegistry.recalculate(library, game)

        // Niffle Queen's aura: the ape has both traits (+2), so it fights for 5 damage.
        executor().execute(game, UseCreatureAction(ape.instanceId, FightUse(victim.instanceId)))

        assertThat(victim.damage).isEqualTo(5)
    }
}
