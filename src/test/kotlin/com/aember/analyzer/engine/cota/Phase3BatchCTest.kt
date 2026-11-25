package com.aember.analyzer.engine.cota

import com.aember.analyzer.domain.engine.action.ActionGenerator
import com.aember.analyzer.domain.engine.action.PlayCardAction
import com.aember.analyzer.domain.model.House
import com.aember.analyzer.engine.support.CoreCardLibrary
import com.aember.analyzer.engine.support.scenario
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

/**
 * Phase 3 batch (c) — capture / power counters / conditionals:
 * capture pool owners (Mindwarper, Hypnotic Command, Pandemonium, Honorable Claim,
 * Unguarded Camp), all-but-N capture (Gatekeeper, Drumble), turn-scoped fight captures
 * (Take Hostages), fight-destroyed reactions (Francus, Mugwump), power counters
 * (Martian Hounds, Eater of the Dead), Cleansing Wave, Mothergun, Arise!, Nexus /
 * Remote Access, Snudge, Phase Shift.
 */
class Phase3BatchCTest {

    private val library = CoreCardLibrary.create()
    private val generator = ActionGenerator(library)

    @Test
    fun `Mindwarper makes an enemy creature capture from its own side`() {
        val scenario = scenario {
            player1 {
                house(House.MARS)
                inPlay("mindwarper")
            }
            player2 {
                amber(4)
                inPlay("dust-imp")
            }
        }
        scenario.run {
            useAbility("mindwarper", "Choose an enemy creature")
            val dustImp = p2.creatures.first()
            assertThat(dustImp.capturedAmber).isEqualTo(1)
            assertThat(p2.amber).isEqualTo(3)
            assertThat(p1.amber).isEqualTo(0)
        }
    }

    @Test
    fun `Hypnotic Command captures one amber per friendly Mars creature onto enemy creatures`() {
        val scenario = scenario {
            player1 {
                house(House.MARS)
                hand("hypnotic-command")
                inPlay("mindwarper")
                inPlay("yxili-marauder")
            }
            player2 {
                amber(5)
                inPlay("dust-imp")
                inPlay("batdrone")
            }
        }
        scenario.run {
            play("hypnotic-command")
            assertThat(p2.creatures.first { it.cardId == "dust-imp" }.capturedAmber).isEqualTo(1)
            assertThat(p2.creatures.first { it.cardId == "batdrone" }.capturedAmber).isEqualTo(1)
            assertThat(p2.amber).isEqualTo(3)
        }
    }

    @Test
    fun `Pandemonium undamaged creatures capture from their opponents`() {
        val scenario = scenario {
            player1 {
                house(House.DIS)
                hand("pandemonium")
                inPlay("dust-imp", damage = 1)
                inPlay("batdrone")
            }
            player2 {
                amber(3)
                inPlay("umbra", damage = 1)
                inPlay("silvertooth")
            }
            player1 { amber(2) }
        }
        scenario.run {
            play("pandemonium")
            // Friendly undamaged captures 1 from the opponent's pool.
            assertThat(p1.creatures.first { it.cardId == "batdrone" }.capturedAmber).isEqualTo(1)
            assertThat(p1.creatures.first { it.cardId == "dust-imp" }.capturedAmber).isEqualTo(0)
            assertThat(p2.amber).isEqualTo(2)
            // Enemy undamaged captures 1 from our own pool.
            assertThat(p2.creatures.first { it.cardId == "silvertooth" }.capturedAmber).isEqualTo(1)
            assertThat(p2.creatures.first { it.cardId == "umbra" }.capturedAmber).isEqualTo(0)
            // Pandemonium's own bonus pip (+1) minus the capture we lost.
            assertThat(p1.amber).isEqualTo(2)
        }
    }

    @Test
    fun `Honorable Claim makes each friendly Knight capture`() {
        val scenario = scenario {
            player1 {
                house(House.SANCTUM)
                hand("honorable-claim")
                inPlay("raiding-knight")
                inPlay("champion-tabris")
                inPlay("dust-imp")
            }
            player2 {
                amber(5)
            }
        }
        scenario.run {
            play("honorable-claim")
            assertThat(p1.creatures.first { it.cardId == "raiding-knight" }.capturedAmber).isEqualTo(1)
            assertThat(p1.creatures.first { it.cardId == "champion-tabris" }.capturedAmber).isEqualTo(1)
            assertThat(p1.creatures.first { it.cardId == "dust-imp" }.capturedAmber).isEqualTo(0)
            assertThat(p2.amber).isEqualTo(3)
        }
    }

    @Test
    fun `Unguarded Camp limits captures to the creature excess`() {
        val scenario = scenario {
            player1 {
                house(House.UNTAMED)
                hand("unguarded-camp")
                inPlay("ancient-bear")
                inPlay("dust-imp")
                inPlay("batdrone")
            }
            player2 {
                amber(4)
                inPlay("umbra")
            }
        }
        scenario.run {
            play("unguarded-camp")
            // 3 creatures vs 1 → only 2 friendly creatures capture 1 each.
            val captured = p1.creatures.count { it.capturedAmber == 1 }
            assertThat(captured).isEqualTo(2)
            assertThat(p2.amber).isEqualTo(2)
        }
    }

    @Test
    fun `Unguarded Camp is not playable when not ahead on creatures`() {
        val scenario = scenario {
            player1 {
                house(House.UNTAMED)
                hand("unguarded-camp")
                inPlay("ancient-bear")
            }
            player2 {
                inPlay("umbra")
                inPlay("silvertooth")
            }
        }
        scenario.run {
            chooseHouse(House.UNTAMED)
            assertThat(generator.generate(game).none { it is PlayCardAction && it.cardId == "unguarded-camp" })
                .isTrue()
        }
    }

    @Test
    fun `Gatekeeper captures all but 5 amber when the opponent has 7 or more`() {
        val scenario = scenario {
            player1 {
                house(House.SANCTUM)
                hand("gatekeeper")
                inPlay("raiding-knight")
            }
            player2 {
                amber(9)
            }
        }
        scenario.run {
            play("gatekeeper")
            val gatekeeper = p1.creatures.first { it.cardId == "gatekeeper" }
            assertThat(gatekeeper.capturedAmber).isEqualTo(4)
            assertThat(p2.amber).isEqualTo(5)
        }
    }

    @Test
    fun `Gatekeeper is playable below 7 opponent amber but captures nothing`() {
        val scenario = scenario {
            player1 {
                house(House.SANCTUM)
                hand("gatekeeper")
            }
            player2 {
                amber(6)
            }
        }
        scenario.run {
            chooseHouse(House.SANCTUM)
            // keyteki gates only the capture; the creature itself can always be played.
            assertThat(generator.generate(game).any { it is PlayCardAction && it.cardId == "gatekeeper" })
                .isTrue()
            play("gatekeeper")
            assertThat(p2.amber).isEqualTo(6)
            assertThat(p1.creatures.first { it.cardId == "gatekeeper" }.capturedAmber).isEqualTo(0)
        }
    }

    @Test
    fun `Drumble captures all opponent amber when they have 7 or more`() {
        val scenario = scenario {
            player1 {
                house(House.DIS)
                hand("drumble")
            }
            player2 {
                amber(8)
            }
        }
        scenario.run {
            play("drumble")
            assertThat(p1.creatures.first { it.cardId == "drumble" }.capturedAmber).isEqualTo(8)
            assertThat(p2.amber).isEqualTo(0)
        }
    }

    @Test
    fun `Take Hostages makes friendly fighters capture until end of turn`() {
        val scenario = scenario {
            player1 {
                house(House.SANCTUM)
                hand("take-hostages")
                inPlay("raiding-knight", power = 5)
            }
            player2 {
                amber(3)
                inPlay("umbra", power = 2)
            }
        }
        scenario.run {
            play("take-hostages")
            fight("raiding-knight", "umbra")
            assertThat(p1.creatures.first { it.cardId == "raiding-knight" }.capturedAmber).isEqualTo(1)
            assertThat(p2.amber).isEqualTo(2)
        }
    }

    @Test
    fun `Francus captures after destroying an enemy in a fight`() {
        val scenario = scenario {
            player1 {
                house(House.SANCTUM)
                inPlay("francus", power = 6)
            }
            player2 {
                amber(3)
                inPlay("dust-imp", power = 2)
            }
        }
        scenario.run {
            fight("francus", "dust-imp")
            assertThat(p1.creatures.first { it.cardId == "francus" }.capturedAmber).isEqualTo(1)
            // dust-imp's Destroyed effect gains 2 amber; Francus captures 1 of the 5.
            assertThat(p2.amber).isEqualTo(4)
        }
    }

    @Test
    fun `Mugwump fully heals and gains a power counter after destroying an enemy`() {
        val scenario = scenario {
            player1 {
                house(House.BROBNAR)
                inPlay("mugwump", power = 6, damage = 3)
            }
            player2 {
                inPlay("dust-imp", power = 2)
            }
        }
        scenario.run {
            fight("mugwump", "dust-imp")
            val mugwump = p1.creatures.first { it.cardId == "mugwump" }
            assertThat(mugwump.damage).isEqualTo(0)
            assertThat(mugwump.power).isEqualTo(7)
        }
    }

    @Test
    fun `Martian Hounds gives two power counters per damaged creature`() {
        val scenario = scenario {
            chooseTargets {
                rule<com.aember.analyzer.domain.engine.dsl.AnyCreature> { game, _ ->
                    listOfNotNull(com.aember.analyzer.engine.support.ScriptedTargetChooser.findCard(game, "dust-imp"))
                }
            }
            player1 {
                house(House.MARS)
                hand("martian-hounds")
                inPlay("dust-imp", power = 3, damage = 1)
                inPlay("batdrone", power = 3, damage = 2)
            }
            player2 {
                inPlay("umbra", power = 5, damage = 1)
            }
        }
        scenario.run {
            play("martian-hounds")
            // 3 damaged creatures in play → 6 counters on the scripted target.
            val dustImp = p1.creatures.first { it.cardId == "dust-imp" }
            assertThat(dustImp.basePower).isEqualTo(9)
        }
    }

    @Test
    fun `Eater of the Dead purges from a discard pile and gains a power counter`() {
        val scenario = scenario {
            player1 {
                house(House.DIS)
                discard("snufflegator")
                inPlay("eater-of-the-dead")
            }
        }
        scenario.run {
            reap("eater-of-the-dead")
            assertThat(p1.purged).contains("snufflegator")
            assertThat(p1.discard).doesNotContain("snufflegator")
            // Eater of the Dead's printed power is 4; the counter raises it to 5.
            assertThat(p1.creatures.first { it.cardId == "eater-of-the-dead" }.basePower).isEqualTo(5)
        }
    }

    @Test
    fun `Eater of the Dead gains no counter when no creature is in discard`() {
        val scenario = scenario {
            player1 {
                house(House.DIS)
                inPlay("eater-of-the-dead")
            }
        }
        scenario.run {
            reap("eater-of-the-dead")
            assertThat(p1.creatures.first { it.cardId == "eater-of-the-dead" }.basePower).isEqualTo(4)
        }
    }

    @Test
    fun `Cleansing Wave heals each creature and gains one amber per heal`() {
        val scenario = scenario {
            player1 {
                house(House.SANCTUM)
                hand("cleansing-wave")
                inPlay("dust-imp", power = 5, damage = 2)
                inPlay("batdrone", power = 3, damage = 1)
            }
            player2 {
                inPlay("umbra", power = 5, damage = 3)
            }
        }
        scenario.run {
            play("cleansing-wave")
            assertThat(p1.amber).isEqualTo(3)
            assertThat(p1.creatures.first { it.cardId == "dust-imp" }.damage).isEqualTo(1)
            assertThat(p1.creatures.first { it.cardId == "batdrone" }.damage).isEqualTo(0)
            assertThat(p2.creatures.first { it.cardId == "umbra" }.damage).isEqualTo(2)
        }
    }

    @Test
    fun `Mothergun deals damage equal to the Mars cards revealed`() {
        val scenario = scenario {
            player1 {
                house(House.MARS)
                artifact("mothergun")
                hand("mindwarper", "commpod")
            }
            player2 {
                inPlay("umbra", power = 5)
            }
        }
        scenario.run {
            useAbility("mothergun", "Reveal any number of Mars cards from your hand")
            assertThat(p2.creatures.first { it.cardId == "umbra" }.damage).isEqualTo(2)
        }
    }

    @Test
    fun `Arise returns the house with the most creatures in discard and gains a chain`() {
        val scenario = scenario {
            player1 {
                house(House.UNTAMED)
                hand("arise")
                discard("ancient-bear", "snufflegator", "dust-imp")
            }
        }
        scenario.run {
            play("arise")
            assertThat(p1.hand).contains("ancient-bear", "snufflegator")
            assertThat(p1.hand).doesNotContain("dust-imp")
            assertThat(p1.discard).contains("dust-imp")
            assertThat(p1.chains).isEqualTo(1)
        }
    }

    @Test
    fun `Nexus uses an opponent artifact as if it were ours`() {
        val scenario = scenario {
            player1 {
                house(House.MARS)
                inPlay("nexus")
            }
            player2 {
                artifact("cannon")
                inPlay("umbra", power = 5)
            }
        }
        scenario.run {
            reap("nexus")
            // The cannon fires for the Nexus controller; the greedy AnyCreature
            // pick resolves relative to the cannon's controller, so the Nexus
            // itself (strongest creature on that side) takes the damage.
            assertThat(p1.creatures.first { it.cardId == "nexus" }.damage).isEqualTo(2)
            val cannon = p2.artifacts.first { it.cardId == "cannon" }
            assertThat(cannon.exhausted).isTrue()
        }
    }

    @Test
    fun `Remote Access uses an opponent artifact on play`() {
        val scenario = scenario {
            player1 {
                house(House.MARS)
                hand("remote-access")
                deck("dust-imp")
            }
            player2 {
                artifact("library-of-babble")
            }
        }
        scenario.run {
            play("remote-access")
            // Library of Babble draws for the Remote Access controller.
            assertThat(p1.hand).contains("dust-imp")
            assertThat(p2.artifacts.first { it.cardId == "library-of-babble" }.exhausted).isTrue()
        }
    }

    @Test
    fun `Snudge returns the opponent artifact to its owner's hand`() {
        val scenario = scenario {
            player1 {
                house(House.SHADOWS)
                inPlay("snudge")
            }
            player2 {
                artifact("cannon")
            }
        }
        scenario.run {
            reap("snudge")
            assertThat(p2.artifacts).isEmpty()
            assertThat(p2.hand).contains("cannon")
        }
    }

    @Test
    fun `Snudge returns the strongest enemy flank creature when no artifact exists`() {
        val scenario = scenario {
            player1 {
                house(House.SHADOWS)
                inPlay("snudge")
            }
            player2 {
                inPlay("umbra", power = 5)
                inPlay("silvertooth", power = 3)
            }
        }
        scenario.run {
            reap("snudge")
            // umbra is the left flank creature (5 > 3).
            assertThat(p2.creatures.any { it.cardId == "umbra" }).isFalse()
            assertThat(p2.creatures.any { it.cardId == "silvertooth" }).isTrue()
            assertThat(p2.hand).contains("umbra")
        }
    }

    @Test
    fun `Phase Shift allows one non-Logos play this turn`() {
        val scenario = scenario {
            player1 {
                house(House.LOGOS, House.UNTAMED)
                hand("phase-shift")
                hand("dew-faerie")
                hand("ancient-bear")
            }
        }
        scenario.run {
            play("phase-shift")
            assertThat(generator.generate(game).any { it is PlayCardAction && it.cardId == "dew-faerie" })
                .isTrue()
            // Spend the allowance.
            p1.cardsPlayedThisTurnByHouse[House.UNTAMED] = 1
            assertThat(generator.generate(game).none { it is PlayCardAction && it.cardId == "dew-faerie" })
                .isTrue()
        }
    }

    @Test
    fun `Phase Shift does not unlock plays while the card's own house is not active`() {
        val scenario = scenario {
            player1 {
                house(House.LOGOS, House.UNTAMED)
                hand("dew-faerie")
            }
        }
        scenario.run {
            chooseHouse(House.UNTAMED)
            assertThat(generator.generate(game).any { it is PlayCardAction && it.cardId == "dew-faerie" })
                .isTrue()
        }
    }

    @Test
    fun `Phase Shift cannot be spent on a Logos card`() {
        val scenario = scenario {
            player1 {
                house(House.LOGOS, House.UNTAMED)
                hand("phase-shift")
                hand("library-of-babble")
                hand("dew-faerie")
            }
        }
        scenario.run {
            play("phase-shift")
            // library-of-babble matches the active house anyway.
            assertThat(generator.generate(game).any { it is PlayCardAction && it.cardId == "library-of-babble" })
                .isTrue()
            // ...and it must not consume the Phase Shift allowance.
            assertThat(generator.generate(game).any { it is PlayCardAction && it.cardId == "dew-faerie" })
                .isTrue()
        }
    }
}
