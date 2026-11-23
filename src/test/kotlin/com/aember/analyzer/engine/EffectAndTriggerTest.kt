package com.aember.analyzer.engine

import com.aember.analyzer.domain.engine.action.*
import com.aember.analyzer.domain.engine.dsl.*
import com.aember.analyzer.domain.engine.model.*
import com.aember.analyzer.domain.model.House
import com.aember.analyzer.engine.EngineTestHelper.IncrementingIdGenerator
import com.aember.analyzer.engine.EngineTestHelper.artifact
import com.aember.analyzer.engine.EngineTestHelper.creature
import com.aember.analyzer.engine.EngineTestHelper.dslCard
import com.aember.analyzer.engine.EngineTestHelper.makeGame
import com.aember.analyzer.engine.EngineTestHelper.makeLibrary
import com.aember.analyzer.engine.EngineTestHelper.playCard
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class EffectAndTriggerTest {

    private val idGenerator = IncrementingIdGenerator()

    @Test
    fun `stun and unstun effects toggle stun flag`() {
        val target = creature("target", 1)
        val game = makeGame(player0Creatures = listOf(target))
        val library = makeLibrary("stunner" to dslCard { play(Stun(AnyCreature())) })
        val executor = ActionExecutor(library, idGenerator)

        playCard(game, executor, "stunner")

        assertThat(target.stun).isTrue()
    }

    @Test
    fun `destroy most powerful destroys the highest power creature`() {
        val weak = creature("weak", 1, power = 1)
        val strong = creature("strong", 2, power = 6)
        val game = makeGame(player0Creatures = listOf(weak, strong))
        val library = makeLibrary("fates" to dslCard { play(DestroyMostPowerful(1)) })
        val executor = ActionExecutor(library, idGenerator)

        playCard(game, executor, "fates")

        assertThat(strong.damage).isEqualTo(Int.MAX_VALUE)
        assertThat(weak.damage).isEqualTo(0)
    }

    @Test
    fun `destroy least powerful destroys the lowest power creature`() {
        val weak = creature("weak", 1, power = 1)
        val strong = creature("strong", 2, power = 6)
        val game = makeGame(player0Creatures = listOf(weak, strong))
        val library = makeLibrary("famine" to dslCard { play(DestroyLeastPowerful(1)) })
        val executor = ActionExecutor(library, idGenerator)

        playCard(game, executor, "famine")

        assertThat(weak.damage).isEqualTo(Int.MAX_VALUE)
        assertThat(strong.damage).isEqualTo(0)
    }

    @Test
    fun `ready and fight readies an exhausted creature and makes it fight`() {
        val attacker = creature("attacker", 1, power = 3, exhausted = true)
        val defender = creature("defender", 2, power = 1)
        val game = makeGame(player0Creatures = listOf(attacker), player1Creatures = listOf(defender))
        val library = makeLibrary("anger" to dslCard { play(ReadyAndFight(FriendlyCreature())) })
        val executor = ActionExecutor(library, idGenerator)

        playCard(game, executor, "anger")

        assertThat(attacker.exhausted).isTrue() // fighting exhausts it again
        assertThat(defender.damage).isEqualTo(3)
    }

    @Test
    fun `return from discard to hand moves cards`() {
        val game = makeGame()
        game.activePlayer().discard.addAll(listOf("a", "b", "c"))
        val library = makeLibrary("regrowth" to dslCard { play(ReturnFromDiscardToHand(2, SelfPlayer)) })
        val executor = ActionExecutor(library, idGenerator)

        playCard(game, executor, "regrowth")

        assertThat(game.activePlayer().hand).hasSize(2)
        assertThat(game.activePlayer().discard).hasSize(2)
    }

    @Test
    fun `archive from hand moves cards to archives`() {
        val game = makeGame()
        game.activePlayer().hand.addAll(listOf("a", "b"))
        val library = makeLibrary("labwork" to dslCard { play(ArchiveFromHand(1, SelfPlayer)) })
        val executor = ActionExecutor(library, idGenerator)

        playCard(game, executor, "labwork")

        assertThat(game.activePlayer().archives).hasSize(1)
        assertThat(game.activePlayer().hand).hasSize(1)
    }

    @Test
    fun `purge from discard removes cards from game`() {
        val game = makeGame()
        game.activePlayer().discard.addAll(listOf("a", "b"))
        val library = makeLibrary("oblivion" to dslCard { play(PurgeFromDiscard(2, SelfPlayer)) })
        val executor = ActionExecutor(library, idGenerator)

        playCard(game, executor, "oblivion")

        assertThat(game.activePlayer().purged).hasSize(2)
        assertThat(game.activePlayer().discard).hasSize(1)
        assertThat(game.activePlayer().discard).contains("oblivion")
    }

    @Test
    fun `swap deck and discard exchanges zones`() {
        val game = makeGame()
        game.activePlayer().library.addAll(listOf("deck1", "deck2"))
        game.activePlayer().discard.addAll(listOf("disc1", "disc2", "disc3"))
        val library = makeLibrary("reverse-time" to dslCard { play(SwapDeckAndDiscard(SelfPlayer)) })
        val executor = ActionExecutor(library, idGenerator)

        playCard(game, executor, "reverse-time")

        assertThat(game.activePlayer().library.toSet()).containsExactlyInAnyOrder("disc1", "disc2", "disc3")
        assertThat(game.activePlayer().discard.toSet()).containsExactlyInAnyOrder("deck1", "deck2", "reverse-time")
    }

    @Test
    fun `play top card puts a library card into play`() {
        val game = makeGame()
        game.activePlayer().library.add("top-card")
        val library = makeLibrary("wormhole" to dslCard { play(PlayTopCard(SelfPlayer)) })
        val executor = ActionExecutor(library, idGenerator)

        playCard(game, executor, "wormhole")

        assertThat(game.activePlayer().creatures.map { it.cardId }).contains("top-card")
        assertThat(game.activePlayer().library).isEmpty()
    }

    @Test
    fun `lose all amber empties amber pool`() {
        val game = makeGame()
        game.activePlayer().amber = 7
        val library = makeLibrary("shatter" to dslCard { play(LoseAllAmber(SelfPlayer)) })
        val executor = ActionExecutor(library, idGenerator)

        playCard(game, executor, "shatter")

        assertThat(game.activePlayer().amber).isEqualTo(0)
    }

    @Test
    fun `gain amber opponent increases opponent amber`() {
        val game = makeGame()
        val library = makeLibrary("fertility" to dslCard { play(GainAmberOpponent(2)) })
        val executor = ActionExecutor(library, idGenerator)

        playCard(game, executor, "fertility")

        assertThat(game.opponent().amber).isEqualTo(2)
    }

    @Test
    fun `exhaust effect exhausts a creature`() {
        val target = creature("target", 1, exhausted = false)
        val game = makeGame(player0Creatures = listOf(target))
        val library = makeLibrary("tire" to dslCard { play(Exhaust(AnyCreature())) })
        val executor = ActionExecutor(library, idGenerator)

        playCard(game, executor, "tire")

        assertThat(target.exhausted).isTrue()
    }

    @Test
    fun `shuffle hand and discard into deck combines zones`() {
        val game = makeGame()
        game.activePlayer().hand.addAll(listOf("h1", "h2"))
        val library = makeLibrary("screaming" to dslCard { play(ShuffleHandAndDiscardIntoDeck(SelfPlayer)) })
        val executor = ActionExecutor(library, idGenerator)

        playCard(game, executor, "screaming")

        assertThat(game.activePlayer().library).hasSize(2)
        assertThat(game.activePlayer().hand).isEmpty()
        assertThat(game.activePlayer().discard).containsExactly("screaming")
    }

    @Test
    fun `before fight and after fight triggers resolve around combat`() {
        val attacker = creature("attacker", 1, power = 5)
        val defender = creature("defender", 2, power = 2)
        val game = makeGame(player0Creatures = listOf(attacker), player1Creatures = listOf(defender))
        game.activePlayer().activeHouse = House.BROBNAR
        val library = makeLibrary(
            "attacker" to dslCard {
                beforeFight(DealDamage(1, EnemyCreature()))
                afterFight(GainAmber(1))
            }
        )
        val executor = ActionExecutor(library, idGenerator)

        executor.execute(game, UseCreatureAction(attacker.instanceId, FightUse(defender.instanceId)))

        // Before fight 1 damage to the defender + normal 5 power = 6 damage; the attacker
        // survives (2 < 5) and the after-fight trigger gives 1 amber.
        assertThat(defender.damage).isEqualTo(6)
        assertThat(game.activePlayer().amber).isEqualTo(1)
    }

    @Test
    fun `a creature destroyed during the fight does not resolve its own fight triggers`() {
        // keyteki: reactions are unregistered when a card leaves play (updateAbilityEvents),
        // so a destroyed attacker's Fight/After Fight abilities never resolve (Dodger spec:
        // "should not steal lose 1A if destroyed").
        val attacker = creature("attacker", 1, power = 1)
        val defender = creature("defender", 2, power = 5)
        val game = makeGame(player0Creatures = listOf(attacker), player1Creatures = listOf(defender))
        game.activePlayer().activeHouse = House.BROBNAR
        val library = makeLibrary(
            "attacker" to dslCard {
                afterFight(GainAmber(1))
            }
        )
        val executor = ActionExecutor(library, idGenerator)

        executor.execute(game, UseCreatureAction(attacker.instanceId, FightUse(defender.instanceId)))

        assertThat(attacker.damage).isGreaterThanOrEqualTo(attacker.power)
        assertThat(game.activePlayer().amber).isEqualTo(0)
    }

    @Test
    fun `before reap and after reap triggers resolve around reap`() {
        val reaper = creature("reaper", 1)
        val game = makeGame(player0Creatures = listOf(reaper))
        game.activePlayer().activeHouse = House.BROBNAR
        val library = makeLibrary(
            "reaper" to dslCard {
                beforeReap(Heal(3, FriendlyCreature()))
                afterReap(GainAmber(1))
            }
        )
        val executor = ActionExecutor(library, idGenerator)

        executor.execute(game, UseCreatureAction(reaper.instanceId, ReapUse))

        assertThat(game.activePlayer().amber).isEqualTo(2) // 1 from reap + 1 after reap
    }

    @Test
    fun `omni ability can be used even when card house does not match active house`() {
        val item = artifact("omni-rod", house = House.DIS, exhausted = false)
        val game = makeGame(player0Artifacts = listOf(item))
        game.activePlayer().activeHouse = House.BROBNAR
        val library = makeLibrary("omni-rod" to dslCard { omni(GainAmber(2)) })
        val executor = ActionExecutor(library, idGenerator)

        val generator = ActionGenerator(library)
        val actions = generator.generate(game).filterIsInstance<UseArtifactAction>()

        assertThat(actions).anyMatch { it.instanceId == item.instanceId && it.abilityId == "Omni" }

        executor.execute(game, UseArtifactAction(item.instanceId, "Omni"))

        assertThat(game.activePlayer().amber).isEqualTo(2)
        assertThat(item.exhausted).isTrue()
    }

    @Test
    fun `creature action ability resolves its effect`() {
        val actor = creature("actor", 1, exhausted = false)
        val game = makeGame(player0Creatures = listOf(actor))
        game.activePlayer().activeHouse = House.BROBNAR
        val library = makeLibrary("actor" to dslCard { action("Steal", StealAmber(1)) })
        val executor = ActionExecutor(library, idGenerator)
        game.opponent().amber = 3

        executor.execute(game, UseCreatureAction(actor.instanceId, ActionAbilityUse("Steal")))

        assertThat(game.activePlayer().amber).isEqualTo(1)
        assertThat(game.opponent().amber).isEqualTo(2)
    }

    @Test
    fun `earthshaker destroys creatures with power 3 or lower`() {
        val weakFriendly = creature("weak-friendly", 1, owner = 0, power = 2)
        val strongFriendly = creature("strong-friendly", 2, owner = 0, power = 5)
        val weakEnemy = creature("weak-enemy", 3, owner = 1, power = 3)
        val strongEnemy = creature("strong-enemy", 4, owner = 1, power = 7)
        val game = makeGame(
            player0Creatures = listOf(weakFriendly, strongFriendly),
            player1Creatures = listOf(weakEnemy, strongEnemy)
        )
        val library = makeLibrary("earthshaker" to dslCard { play(DestroyWeakCreatures(3)) })
        val executor = ActionExecutor(library, idGenerator)

        playCard(game, executor, "earthshaker")

        assertThat(weakFriendly.damage).isEqualTo(Int.MAX_VALUE)
        assertThat(weakEnemy.damage).isEqualTo(Int.MAX_VALUE)
        assertThat(strongFriendly.damage).isEqualTo(0)
        assertThat(strongEnemy.damage).isEqualTo(0)
    }

    @Test
    fun `wardrummer returns only other friendly brobnar creatures to hand`() {
        val wardrummer = creature("wardrummer", 1, owner = 0, power = 3)
        val brobnarFriend = creature("brobnar-friend", 2, owner = 0, power = 4)
        val disFriend = creature("dis-friend", 3, owner = 0, power = 2)
        wardrummer.house = House.BROBNAR
        brobnarFriend.house = House.BROBNAR
        disFriend.house = House.DIS
        val game = makeGame(player0Creatures = listOf(wardrummer, brobnarFriend, disFriend))
        val library = makeLibrary("wardrummer" to dslCard { play(ReturnToHand(OtherFriendlyCreatureWithHouse(House.BROBNAR, Scope.ALL))) })
        val executor = ActionExecutor(library, idGenerator)

        playCard(game, executor, "wardrummer")

        assertThat(game.activePlayer().creatures.map { it.cardId }).containsExactly("wardrummer", "dis-friend")
        assertThat(game.activePlayer().hand).containsExactly("brobnar-friend")
    }
}
