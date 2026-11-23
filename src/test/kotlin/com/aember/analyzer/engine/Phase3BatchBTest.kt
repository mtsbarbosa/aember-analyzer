package com.aember.analyzer.engine

import com.aember.analyzer.domain.engine.action.ActionGenerator
import com.aember.analyzer.domain.engine.action.FightUse
import com.aember.analyzer.domain.engine.dsl.AnyCreature
import com.aember.analyzer.engine.support.ScriptedTargetChooser
import com.aember.analyzer.domain.engine.action.UseCreatureAction
import com.aember.analyzer.domain.model.House
import com.aember.analyzer.engine.support.CoreCardLibrary
import com.aember.analyzer.engine.support.scenario
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

/**
 * Phase 3 batch (b) — repeat-while stealing, amber halving, creature-shuffling,
 * draw-phase modifiers, play-grants, turn-scoped damage immunity, fight-time
 * keyword ignores, reactions (Tunk / Duskrunner), and small targeting selectors.
 */
class Phase3BatchBTest {

    private val library = CoreCardLibrary.create()
    private val generator = ActionGenerator(library)

    @Test
    fun `Bait and Switch steals repeatedly while the opponent has more amber`() {
        val scenario = scenario {
            player1 {
                house(House.SHADOWS)
                hand("bait-and-switch")
                amber(0)
            }
            player2 { amber(3) }
        }
        scenario.run {
            play("bait-and-switch")
            // 0 vs 3 -> steal; 1 vs 2 -> steal; 2 vs 1 -> stop.
            assertThat(p1.amber).isEqualTo(2)
            assertThat(p2.amber).isEqualTo(1)
        }
    }

    @Test
    fun `Effervescent Principle halves both players' amber and gains a chain`() {
        val scenario = scenario {
            player1 {
                house(House.LOGOS)
                hand("effervescent-principle")
                amber(5)
            }
            player2 { amber(4) }
        }
        scenario.run {
            play("effervescent-principle")
            assertThat(p1.amber).isEqualTo(3)
            assertThat(p2.amber).isEqualTo(2)
            assertThat(p1.chains).isEqualTo(1)
        }
    }

    @Test
    fun `Lost in the Woods shuffles chosen creatures into their owners' decks`() {
        val scenario = scenario {
            player1 {
                house(House.UNTAMED)
                hand("lost-in-the-woods")
                inPlay("snufflegator")
                inPlay("grommid")
            }
            player2 {
                inPlay("umbra")
                inPlay("silvertooth")
            }
        }
        scenario.run {
            play("lost-in-the-woods")
            assertThat(p1.creatures).isEmpty()
            assertThat(p2.creatures).isEmpty()
            assertThat(p1.library).containsExactlyInAnyOrder("snufflegator", "grommid")
            assertThat(p2.library).containsExactlyInAnyOrder("umbra", "silvertooth")
        }
    }

    @Test
    fun `Mother refills the controller's hand to one additional card`() {
        val scenario = scenario {
            player1 {
                house(House.LOGOS)
                inPlay("mother")
                hand("seeker-needle", "dextre", "twin-bolt-emission", "soft-landing", "vigor")
                deck("anger", "lava-ball")
            }
            player2 {}
        }
        scenario.run {
            assertThat(p1.hand.size).isEqualTo(5)
            endTurn()
            assertThat(p1.hand.size).isEqualTo(7)
        }
    }

    @Test
    fun `Succubus refills the opponent's hand to one less card`() {
        val scenario = scenario {
            player1 {
                house(House.DIS)
                inPlay("succubus")
                hand("fear")
            }
            player2 {
                hand("bait-and-switch", "effervescent-principle", "shooler", "silvertooth")
                deck("anger", "lava-ball")
            }
        }
        scenario.run {
            endTurn()
            // Player 2's refill happens at the end of their own turn.
            endTurn()
            assertThat(p2.hand.size).isEqualTo(5)
        }
    }

    @Test
    fun `Niffle Ape ignores taunt targeting and elusive`() {
        val scenario = scenario {
            player1 {
                house(House.UNTAMED)
                inPlay("niffle-ape")
            }
            player2 {
                house(House.SHADOWS)
                inPlay("bulwark") // taunt
                inPlay("umbra", power = 1) // elusive (weakened so the ape kills it)
            }
        }
        scenario.run {
            chooseHouse(House.UNTAMED)
            val actions = generator.generate(game)
            val fightTargets = actions.filterIsInstance<UseCreatureAction>()
                .filter { it.useType is FightUse }
                .map { (it.useType as FightUse).targetInstanceId }
            assertThat(fightTargets).containsAll(listOf("bulwark#1-1", "umbra#1-2"))
            fight("niffle-ape", "umbra")
            // Elusive is ignored: umbra takes the ape's full power and is destroyed.
            assertThat(p2.creatures.none { it.cardId == "umbra" }).isTrue()
        }
    }

    @Test
    fun `Soft Landing makes the next creature enter play ready`() {
        val scenario = scenario {
            player1 {
                house(House.MARS)
                hand("soft-landing", "tunk")
            }
            player2 {}
        }
        scenario.run {
            play("soft-landing")
            play("tunk")
            val tunk = p1.creatures.first { it.cardId == "tunk" }
            assertThat(tunk.exhausted).isFalse()
        }
    }

    @Test
    fun `Shield of Justice protects friendly creatures for the turn`() {
        val scenario = scenario {
            player1 {
                house(House.SANCTUM)
                hand("shield-of-justice", "lava-ball")
                inPlay("snufflegator")
                inPlay("bulwark")
            }
            player2 {}
        }
        scenario.run {
            // Aim the lava ball at a protected friendly creature: no damage lands anywhere.
            play("shield-of-justice")
            play("lava-ball")
            assertThat(p1.creatures.first { it.cardId == "snufflegator" }.damage).isEqualTo(0)
            assertThat(p1.creatures.first { it.cardId == "bulwark" }.damage).isEqualTo(0)
        }
    }

    @Test
    fun `Protectrix fully heals a creature and it cannot be dealt damage for the turn`() {
        val scenario = scenario {
            chooseTargets {
                rule<AnyCreature> { game, _ ->
                    listOfNotNull(ScriptedTargetChooser.findCard(game, 0, "snufflegator"))
                }
            }
            player1 {
                house(House.SANCTUM)
                inPlay("protectrix")
                inPlay("snufflegator", damage = 3)
                hand("lava-ball")
            }
            player2 { amber(2) }
        }
        scenario.run {
            reap("protectrix")
            val snuff = p1.creatures.first { it.cardId == "snufflegator" }
            assertThat(snuff.damage).isEqualTo(0)
            // Lava Ball aimed at the healed creature is fully blocked.
            play("lava-ball")
            assertThat(snuff.damage).isEqualTo(0)
        }
    }

    @Test
    fun `Vigor heals 3 damage and gains 1 amber only when 3 was healed`() {
        val scenario = scenario {
            chooseTargets {
                rule<AnyCreature> { game, _ ->
                    listOfNotNull(ScriptedTargetChooser.findCard(game, 0, "snufflegator"))
                }
            }
            player1 {
                house(House.UNTAMED)
                hand("vigor")
                inPlay("snufflegator", damage = 3)
                amber(0)
            }
            player2 { amber(1) }
        }
        scenario.run {
            play("vigor")
            assertThat(p1.creatures.first { it.cardId == "snufflegator" }.damage).isEqualTo(0)
            // 1 bonus pip + 1 amber for healing the full 3 damage.
            assertThat(p1.amber).isEqualTo(2)
        }
    }

    @Test
    fun `Twin Bolt Emission hits two different creatures`() {
        val scenario = scenario {
            player1 {
                house(House.LOGOS)
                hand("twin-bolt-emission")
            }
            player2 {
                inPlay("umbra", power = 5)
                inPlay("silvertooth", power = 5)
            }
        }
        scenario.run {
            play("twin-bolt-emission")
            assertThat(p2.creatures.map { it.damage }).containsExactlyInAnyOrder(2, 2)
        }
    }

    @Test
    fun `Pawn Sacrifice sacrifices a friendly creature and deals 3 to two creatures`() {
        val scenario = scenario {
            player1 {
                house(House.SHADOWS)
                hand("pawn-sacrifice")
                inPlay("bumpsy")
            }
            player2 {
                inPlay("umbra", power = 5)
                inPlay("silvertooth", power = 5)
            }
        }
        scenario.run {
            play("pawn-sacrifice")
            assertThat(p1.discard).contains("bumpsy")
            assertThat(p1.creatures).isEmpty()
            assertThat(p2.creatures.map { it.damage }).containsExactlyInAnyOrder(3, 3)
        }
    }

    @Test
    fun `Pawn Sacrifice pays with the weakest friendly creature`() {
        val scenario = scenario {
            player1 {
                house(House.SHADOWS)
                hand("pawn-sacrifice")
                inPlay("bumpsy", power = 6)
                inPlay("umbra", power = 2)
            }
            player2 {
                inPlay("silvertooth", power = 5)
                inPlay("grommid", power = 5)
            }
        }
        scenario.run {
            play("pawn-sacrifice")
            assertThat(p1.creatures.map { it.cardId }).containsExactly("bumpsy")
        }
    }

    @Test
    fun `Lost in the Woods shuffles away the weakest friendly creatures`() {
        val scenario = scenario {
            player1 {
                house(House.UNTAMED)
                hand("lost-in-the-woods")
                inPlay("bumpsy", power = 6)
                inPlay("grommid", power = 5)
                inPlay("snufflegator", power = 2)
                inPlay("umbra", power = 1)
            }
            player2 {}
        }
        scenario.run {
            play("lost-in-the-woods")
            assertThat(p1.creatures.map { it.cardId }).containsExactlyInAnyOrder("bumpsy", "grommid")
        }
    }

    @Test
    fun `Nocturnal Maneuver exhausts enemies first and only ready creatures of ours`() {
        val scenario = scenario {
            player1 {
                house(House.UNTAMED)
                hand("nocturnal-maneuver")
                inPlay("bumpsy", power = 6)
                inPlay("snufflegator", power = 2)
            }
            player2 { inPlay("umbra") }
        }
        scenario.run {
            play("nocturnal-maneuver")
            assertThat(p2.creatures.single().exhausted).isTrue()
            assertThat(p1.creatures.first { it.cardId == "snufflegator" }.exhausted).isTrue()
            assertThat(p1.creatures.count { it.exhausted }).isGreaterThanOrEqualTo(1)
        }
    }

    @Test
    fun `Seeker Needle gains amber only when its damage destroys the creature`() {
        val scenario = scenario {
            player1 {
                house(House.SHADOWS)
                artifact("seeker-needle")
                amber(0)
            }
            player2 {
                inPlay("silvertooth", power = 1)
            }
        }
        scenario.run {
            useAbility("seeker-needle", "Deal 1D to a creature")
            assertThat(p1.amber).isEqualTo(1)
            assertThat(p2.creatures).isEmpty()
        }
    }

    @Test
    fun `Sergeant Zakiel readies a neighbor and fights with it`() {
        val scenario = scenario {
            player1 {
                house(House.SANCTUM)
                inPlay("bumpsy")
                hand("sergeant-zakiel")
            }
            player2 {
                inPlay("umbra", power = 2)
            }
        }
        scenario.run {
            play("sergeant-zakiel", flank = 0)
            // Bumpsy was exhausted; Zakiel's play readies it and fights the enemy.
            assertThat(p2.creatures).isEmpty()
        }
    }

    @Test
    fun `Dextre captures an amber and goes on top of the deck when destroyed`() {
        val scenario = scenario {
            player1 {
                house(House.LOGOS)
                hand("dextre", "coward-s-end")
            }
            player2 { amber(1) }
        }
        scenario.run {
            play("dextre")
            assertThat(p1.creatures.first { it.cardId == "dextre" }.capturedAmber).isEqualTo(1)
            play("coward-s-end")
            assertThat(p1.library.firstOrNull()).isEqualTo("dextre")
            assertThat(p1.discard).doesNotContain("dextre")
        }
    }

    @Test
    fun `Booby Trap hits a non-flank creature with splash to its neighbors`() {
        val scenario = scenario {
            player1 {
                house(House.SHADOWS)
                hand("booby-trap")
            }
            player2 {
                inPlay("umbra", power = 8)
                inPlay("silvertooth", power = 8)
                inPlay("shooler", power = 8)
            }
        }
        scenario.run {
            play("booby-trap")
            val creatures = p2.creatures
            // The middle creature takes 4; both flank neighbors take 2 splash.
            assertThat(creatures.map { it.damage }).containsExactly(2, 4, 2)
        }
    }

    @Test
    fun `Nocturnal Maneuver exhausts up to three creatures`() {
        val scenario = scenario {
            player1 {
                house(House.UNTAMED)
                hand("nocturnal-maneuver")
                inPlay("snufflegator")
                inPlay("grommid")
            }
            player2 {
                inPlay("umbra")
            }
        }
        scenario.run {
            play("nocturnal-maneuver")
            assertThat(p1.creatures.all { it.exhausted }).isTrue()
            assertThat(p2.creatures.first().exhausted).isTrue()
        }
    }

    @Test
    fun `Terms of Redress makes a friendly creature capture two amber`() {
        val scenario = scenario {
            player1 {
                house(House.SANCTUM)
                hand("terms-of-redress")
                inPlay("bumpsy")
            }
            player2 { amber(2) }
        }
        scenario.run {
            play("terms-of-redress")
            assertThat(p1.creatures.first { it.cardId == "bumpsy" }.capturedAmber).isEqualTo(2)
            assertThat(p2.amber).isEqualTo(0)
        }
    }

    @Test
    fun `Duskrunner makes its host steal one amber on reap`() {
        val scenario = scenario {
            player1 {
                house(House.SHADOWS)
                inPlay("snufflegator")
                hand("duskrunner")
                amber(0)
            }
            player2 { amber(2) }
        }
        scenario.run {
            play("duskrunner", target = "snufflegator#0-1")
            reap("snufflegator", house = House.UNTAMED)
            // Reap amber + stolen amber.
            assertThat(p1.amber).isEqualTo(2)
            assertThat(p2.amber).isEqualTo(1)
        }
    }

    @Test
    fun `Tunk fully heals after you play another Mars creature`() {
        val scenario = scenario {
            player1 {
                house(House.MARS)
                inPlay("tunk", damage = 2)
                hand("john-smyth")
            }
            player2 {}
        }
        scenario.run {
            play("john-smyth")
            assertThat(p1.creatures.first { it.cardId == "tunk" }.damage).isEqualTo(0)
        }
    }

    @Test
    fun `Nepenthe Seed sacrifices itself and returns a card from discard`() {
        val scenario = scenario {
            player1 {
                house(House.UNTAMED)
                artifact("nepenthe-seed")
                discard("anger")
            }
            player2 {}
        }
        scenario.run {
            useAbility("nepenthe-seed", "Omni")
            assertThat(p1.hand).contains("anger")
            // The sacrificed seed itself went to the discard pile.
            assertThat(p1.discard).contains("nepenthe-seed")
            assertThat(p1.artifacts.none { it.cardId == "nepenthe-seed" }).isTrue()
        }
    }

    @Test
    fun `Ritual of Balance steals one amber when the opponent has six or more`() {
        val scenario = scenario {
            player1 {
                house(House.UNTAMED)
                artifact("ritual-of-balance")
                amber(0)
            }
            player2 { amber(6) }
        }
        scenario.run {
            useAbility("ritual-of-balance", "If your opponent has 6A or more steal 1A")
            assertThat(p1.amber).isEqualTo(1)
            assertThat(p2.amber).isEqualTo(5)
        }
    }

    @Test
    fun `Ritual of Balance does nothing when the opponent has less than six amber`() {
        val scenario = scenario {
            player1 {
                house(House.UNTAMED)
                artifact("ritual-of-balance")
                amber(0)
            }
            player2 { amber(5) }
        }
        scenario.run {
            useAbility("ritual-of-balance", "If your opponent has 6A or more steal 1A")
            assertThat(p1.amber).isEqualTo(0)
            assertThat(p2.amber).isEqualTo(5)
        }
    }

    @Test
    fun `Fear returns an enemy creature to its owner's hand`() {
        val scenario = scenario {
            player1 {
                house(House.DIS)
                hand("fear")
            }
            player2 {
                house(House.SHADOWS)
                inPlay("umbra")
            }
        }
        scenario.run {
            play("fear")
            assertThat(p2.creatures).isEmpty()
            assertThat(p2.hand).contains("umbra")
        }
    }

    @Test
    fun `Bait and Switch stops when the steal is blocked (The Vaultkeeper)`() {
        val scenario = scenario {
            player1 {
                house(House.SHADOWS)
                hand("bait-and-switch")
                amber(0)
            }
            player2 {
                house(House.SANCTUM)
                inPlay("the-vaultkeeper", power = 4)
                amber(3)
            }
        }
        scenario.run {
            play("bait-and-switch")
            assertThat(p2.amber).isEqualTo(3)
            assertThat(p1.amber).isEqualTo(0)
            // Progress guard: the blocked steal must not retry until maxRepeats.
            assertThat(game.log.count { it.message.contains("cannot be stolen") }).isEqualTo(1)
        }
    }

    @Test
    fun `Sacrifice honours the Annihilation Ritual purge replacement`() {
        val scenario = scenario {
            player1 {
                house(House.SHADOWS)
                hand("pawn-sacrifice")
                inPlay("bumpsy")
                artifact("annihilation-ritual")
            }
            player2 {
                inPlay("umbra", power = 5)
            }
        }
        scenario.run {
            play("pawn-sacrifice")
            assertThat(p1.purged).contains("bumpsy")
            assertThat(p1.discard).doesNotContain("bumpsy")
        }
    }

    @Test
    fun `Ready resolves against the real source card (John Smyth readies another Mars creature)`() {
        val scenario = scenario {
            player1 {
                house(House.MARS)
                inPlay("john-smyth")
                inPlay("blypyp", exhausted = true)
            }
            player2 { }
        }
        scenario.run {
            reap("john-smyth")
            // John Smyth Reap: ready ANOTHER friendly Mars creature. With the old
            // dummy source the "another" filter excluded nothing, so John Smyth
            // readied himself right after reaping.
            assertThat(p1.creatures.first { it.cardId == "blypyp" }.exhausted).isFalse()
            assertThat(p1.creatures.first { it.cardId == "john-smyth" }.exhausted).isTrue()
        }
    }
}
