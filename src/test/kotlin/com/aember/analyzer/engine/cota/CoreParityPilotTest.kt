package com.aember.analyzer.engine.cota

import com.aember.analyzer.domain.engine.dsl.AnyCreature
import com.aember.analyzer.domain.model.House
import com.aember.analyzer.engine.support.scenario
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

/**
 * Phase 1 pilot of the keyteki parity harness: ten Core cards that are declared FULL get
 * parity tests mirroring their keyteki specs (`~/work/keyteki/test/server/cards/01-Core`).
 * These tests prove the Scenario DSL; any failure here is a real bug in a card we currently
 * count as done. The original spec text is quoted in each test so reviewers can compare.
 */
class CoreParityPilotTest {

    // Punch.spec.js: "should deal 3 damage to a creature"
    @Test
    fun `punch deals 3 damage to a creature`() {
        val scenario = scenario {
            player1 {
                house(House.BROBNAR)
                hand("punch")
                amber(0)
            }
            player2 {
                house(House.UNTAMED)
                inPlay("dew-faerie")
                amber(0)
            }
            chooseTargets { rule(AnyCreature::class.java) { game, _ ->
                listOf(game.players[1].creatures.first { it.cardId == "dew-faerie" })
            } }
        }
        scenario.run {
            play("punch")
        }
        assertThat(scenario.p1.discard).contains("punch")
        assertThat(scenario.p2.creatures).isEmpty()
        assertThat(scenario.p2.discard).contains("dew-faerie")
    }

    // Punch.spec.js: "should be able to target friendly creatures"
    @Test
    fun `punch can target friendly creatures`() {
        val scenario = scenario {
            player1 {
                house(House.BROBNAR)
                hand("punch")
                inPlay("bumpsy")
            }
            player2 {
                house(House.UNTAMED)
            }
            chooseTargets { rule(AnyCreature::class.java) { game, _ ->
                listOf(game.players[0].creatures.first { it.cardId == "bumpsy" })
            } }
        }
        scenario.run {
            play("punch")
        }
        assertThat(scenario.p1.creatures.first { it.cardId == "bumpsy" }.damage).isEqualTo(3)
    }

    // LavaBall.spec.js: "should deal 4 damage with 2 splash"
    @Test
    fun `lava ball deals 4 damage with 2 splash to neighbors`() {
        val scenario = scenario {
            player1 {
                house(House.BROBNAR)
                hand("lava-ball")
            }
            player2 {
                house(House.UNTAMED)
                inPlay("snufflegator")
                inPlay("ancient-bear")
                inPlay("hunting-witch")
            }
            chooseTargets { rule(AnyCreature::class.java) { game, _ ->
                listOf(game.players[1].creatures.first { it.cardId == "ancient-bear" })
            } }
        }
        scenario.run {
            play("lava-ball")
        }
        val creatures = scenario.p2.creatures
        assertThat(creatures.first { it.cardId == "ancient-bear" }.damage).isEqualTo(4)
        assertThat(creatures.first { it.cardId == "snufflegator" }.damage).isEqualTo(2)
        assertThat(creatures.none { it.cardId == "hunting-witch" }).isTrue()
        assertThat(scenario.p2.discard).contains("hunting-witch")
    }

    // DustImp.spec.js: "should gain 2 amber when destroyed"
    @Test
    fun `dust imp gains 2 amber when destroyed`() {
        val scenario = scenario {
            player1 {
                house(House.DIS)
                inPlay("dust-imp")
            }
            player2 {
                house(House.SHADOWS)
                inPlay("dodger")
            }
        }
        scenario.run {
            fight("dust-imp", "dodger")
        }
        assertThat(scenario.p1.amber).isEqualTo(2)
        assertThat(scenario.p1.creatures.none { it.cardId == "dust-imp" }).isTrue()
        assertThat(scenario.p1.discard).contains("dust-imp")
    }

    // Dodger.spec.js: "should cause steal 1A after fight."
    // (keyteki's setup fights Nexus, which is elusive; we use a non-elusive target to also
    // assert the kill.)
    @Test
    fun `dodger steals 1 amber after fighting`() {
        val scenario = scenario {
            player1 {
                house(House.SHADOWS)
                inPlay("dodger")
            }
            player2 {
                house(House.UNTAMED)
                inPlay("snufflegator")
                amber(2)
            }
        }
        scenario.run {
            fight("dodger", "snufflegator")
        }
        assertThat(scenario.p1.amber).isEqualTo(1)
        assertThat(scenario.p2.amber).isEqualTo(1)
        assertThat(scenario.p1.creatures.any { it.cardId == "dodger" }).isTrue()
        assertThat(scenario.p2.creatures.none { it.cardId == "snufflegator" }).isTrue()
    }

    // Dodger.spec.js: "should not steal lose 1A if destroyed."
    // Macis Asp (skirmish only protects itself while attacking) deals 3 damage back and its
    // poison destroys Dodger even though Dodger survives the damage — a destroyed creature
    // must not resolve its own Fight trigger.
    @Test
    fun `dodger does not steal when destroyed by the fight`() {
        val scenario = scenario {
            player1 {
                house(House.SHADOWS)
                inPlay("dodger")
            }
            player2 {
                house(House.SHADOWS)
                inPlay("macis-asp")
                amber(2)
            }
        }
        scenario.run {
            fight("dodger", "macis-asp")
        }
        assertThat(scenario.p1.creatures.none { it.cardId == "dodger" }).isTrue()
        assertThat(scenario.p1.discard).contains("dodger")
        assertThat(scenario.p1.amber).isEqualTo(0)
        assertThat(scenario.p2.amber).isEqualTo(2)
        assertThat(scenario.p2.creatures.none { it.cardId == "macis-asp" }).isTrue()
    }

    // DocBookton.spec.js: "should draw a card when reaped"
    @Test
    fun `doc bookton draws a card when reaped`() {
        val scenario = scenario {
            player1 {
                house(House.LOGOS)
                inPlay("doc-bookton")
                deck("dextre")
            }
            player2 {
                house(House.DIS)
            }
        }
        scenario.run {
            reap("doc-bookton")
        }
        assertThat(scenario.p1.hand).contains("dextre")
        assertThat(scenario.p1.hand).hasSize(1)
    }

    // Bumpsy.spec.js: "should make opponent lose 1 amber on play"
    @Test
    fun `bumpsy makes opponent lose 1 amber on play`() {
        val scenario = scenario {
            player1 {
                house(House.BROBNAR)
                hand("bumpsy")
            }
            player2 {
                house(House.UNTAMED)
                amber(3)
            }
        }
        scenario.run {
            play("bumpsy")
        }
        assertThat(scenario.p2.amber).isEqualTo(2)
        assertThat(scenario.p1.creatures.any { it.cardId == "bumpsy" }).isTrue()
    }

    // Batdrone.spec.js: "should have skirmish and take no damage when fighting"
    @Test
    fun `batdrone has skirmish and steals amber when fighting`() {
        val scenario = scenario {
            player1 {
                house(House.LOGOS)
                inPlay("batdrone")
            }
            player2 {
                house(House.BROBNAR)
                inPlay("snufflegator")
                amber(2)
            }
        }
        scenario.run {
            fight("batdrone", "snufflegator")
        }
        val batdrone = scenario.p1.creatures.first { it.cardId == "batdrone" }
        assertThat(batdrone.damage).isEqualTo(0)
        assertThat(scenario.p2.creatures.first { it.cardId == "snufflegator" }.damage).isEqualTo(2)
        assertThat(scenario.p1.amber).isEqualTo(1)
        assertThat(scenario.p2.amber).isEqualTo(1)
    }

    // Batdrone.spec.js: "should steal 1 amber when fighting" (Troll takes 2, survives at 8 power)
    @Test
    fun `batdrone steals 1 amber when fighting`() {
        val scenario = scenario {
            player1 {
                house(House.LOGOS)
                inPlay("batdrone")
            }
            player2 {
                house(House.BROBNAR)
                inPlay("troll")
                amber(3)
            }
        }
        scenario.run {
            fight("batdrone", "troll")
        }
        assertThat(scenario.p1.amber).isEqualTo(1)
        assertThat(scenario.p2.amber).isEqualTo(2)
        assertThat(scenario.p1.creatures.any { it.cardId == "batdrone" }).isTrue()
    }

    // Batdrone.spec.js: "should not steal amber when opponent has no amber"
    @Test
    fun `batdrone does not steal when opponent has no amber`() {
        val scenario = scenario {
            player1 {
                house(House.LOGOS)
                inPlay("batdrone")
            }
            player2 {
                house(House.BROBNAR)
                inPlay("troll")
                amber(0)
            }
        }
        scenario.run {
            fight("batdrone", "troll")
        }
        assertThat(scenario.p1.amber).isEqualTo(0)
        assertThat(scenario.p2.amber).isEqualTo(0)
    }

    // Batdrone.spec.js: "should not steal with hazardous creatures"
    // Briar Grubbling's Hazardous 5 kills Batdrone before the fight resolves, so its
    // Fight trigger never fires.
    @Test
    fun `batdrone does not steal when killed by hazardous`() {
        val scenario = scenario {
            player1 {
                house(House.LOGOS)
                inPlay("batdrone")
            }
            player2 {
                house(House.BROBNAR)
                inPlay("briar-grubbling")
                amber(3)
            }
        }
        scenario.run {
            fight("batdrone", "briar-grubbling")
        }
        assertThat(scenario.p1.creatures.none { it.cardId == "batdrone" }).isTrue()
        assertThat(scenario.p1.discard).contains("batdrone")
        assertThat(scenario.p2.creatures.first { it.cardId == "briar-grubbling" }.damage).isEqualTo(0)
        assertThat(scenario.p1.amber).isEqualTo(0)
        assertThat(scenario.p2.amber).isEqualTo(3)
    }

    // DewFaerie.spec.js: "should gain 1 amber when reaped"
    @Test
    fun `dew faerie gains 1 amber when reaped`() {
        val scenario = scenario {
            player1 {
                house(House.UNTAMED)
                inPlay("dew-faerie")
            }
            player2 {
                house(House.DIS)
            }
        }
        scenario.run {
            reap("dew-faerie")
        }
        // 1 amber from the reap itself plus 1 from Dew Faerie's Reap effect (keyteki expects 2).
        assertThat(scenario.p1.amber).isEqualTo(2)
    }

    // Elusive keyword (DewFaerie text): the first fight against it deals no damage.
    @Test
    fun `dew faerie uses elusive against the first attack`() {
        val scenario = scenario {
            player1 {
                house(House.UNTAMED)
                inPlay("dew-faerie")
            }
            player2 {
                house(House.BROBNAR)
                inPlay("giant-sloth")
            }
        }
        scenario.run {
            endTurn()
            fight("giant-sloth", "dew-faerie")
        }
        assertThat(scenario.p1.creatures.first { it.cardId == "dew-faerie" }.damage).isEqualTo(0)
        assertThat(scenario.p2.creatures.first { it.cardId == "giant-sloth" }.damage).isEqualTo(2)
    }

    // HeadHunter.spec.js: Fight: Gain 1<A>.
    @Test
    fun `headhunter gains 1 amber when fighting`() {
        val scenario = scenario {
            player1 {
                house(House.BROBNAR)
                inPlay("headhunter")
            }
            player2 {
                house(House.DIS)
                inPlay("umbra")
            }
        }
        scenario.run {
            fight("headhunter", "umbra")
        }
        assertThat(scenario.p1.amber).isEqualTo(1)
        assertThat(scenario.p2.creatures.none { it.cardId == "umbra" }).isTrue()
    }

    // BadPenny.spec.js: "should return to hand"
    @Test
    fun `bad penny returns to hand when destroyed`() {
        val scenario = scenario {
            player1 {
                house(House.SHADOWS)
                inPlay("bad-penny")
            }
            player2 {
                house(House.DIS)
                inPlay("dodger")
            }
        }
        scenario.run {
            endTurn()
            fight("dodger", "bad-penny")
        }
        assertThat(scenario.p1.hand).contains("bad-penny")
        assertThat(scenario.p1.discard).doesNotContain("bad-penny")
        assertThat(scenario.p1.creatures.none { it.cardId == "bad-penny" }).isTrue()
    }
}
