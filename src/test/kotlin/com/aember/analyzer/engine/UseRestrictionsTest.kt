package com.aember.analyzer.engine

import com.aember.analyzer.domain.engine.action.ActionGenerator
import com.aember.analyzer.domain.engine.action.ChooseHouseAction
import com.aember.analyzer.domain.engine.action.PlayCardAction
import com.aember.analyzer.domain.engine.action.ReapUse
import com.aember.analyzer.domain.engine.action.UseCreatureAction
import com.aember.analyzer.domain.model.House
import com.aember.analyzer.engine.support.CoreCardLibrary
import com.aember.analyzer.engine.support.scenario
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

/**
 * Phase 2.3 — use-restriction auras and card-self restrictions:
 * Grommid, Tireless Crocag, Pitlord, Ember Imp, The Vaultkeeper, Witch of the Wilds.
 * Action legality comes from ActionGenerator; steal blocking from the resolver.
 */
class UseRestrictionsTest {

    private val library = CoreCardLibrary.create()
    private val generator = ActionGenerator(library)

    @Test
    fun `Grommid prevents its controller from playing creatures`() {
        val scenario = scenario {
            player1 {
                house(House.MARS)
                inPlay("grommid")
                hand("john-smyth", "commpod")
            }
        }
        scenario.run {
            chooseHouse(House.MARS)
            val actions = generator.generate(game)
            assertThat(actions.none { it is PlayCardAction && it.cardId == "john-smyth" }).isTrue()
            assertThat(actions.any { it is PlayCardAction && it.cardId == "commpod" }).isTrue()
        }
    }

    @Test
    fun `Grommid does not restrict the opponent`() {
        val scenario = scenario {
            player1 {
                house(House.MARS)
                inPlay("grommid")
            }
            player2 {
                house(House.DIS)
                hand("dust-imp")
            }
        }
        scenario.run {
            endTurn()
            chooseHouse(House.DIS)
            val actions = generator.generate(game)
            assertThat(actions.any { it is PlayCardAction && it.cardId == "dust-imp" }).isTrue()
        }
    }

    @Test
    fun `Tireless Crocag cannot reap but can fight`() {
        val scenario = scenario {
            player1 {
                house(House.BROBNAR)
                inPlay("tireless-crocag", power = 7)
            }
            player2 {
                inPlay("umbra", power = 2)
            }
        }
        scenario.run {
            chooseHouse(House.BROBNAR)
            val actions = generator.generate(game)
            assertThat(actions.none { it is UseCreatureAction && it.useType is ReapUse }).isTrue()
            assertThat(actions.any { it is UseCreatureAction }).isTrue()
        }
    }

    @Test
    fun `Tireless Crocag can be used as if it belonged to the active house`() {
        val scenario = scenario {
            player1 {
                house(House.BROBNAR, House.LOGOS)
                inPlay("tireless-crocag", power = 7)
            }
            player2 {
                inPlay("umbra", power = 2)
            }
        }
        scenario.run {
            chooseHouse(House.LOGOS)
            val actions = generator.generate(game)
            assertThat(actions.any { it is UseCreatureAction }).isTrue()
            assertThat(actions.none { it is UseCreatureAction && it.useType is ReapUse }).isTrue()
        }
    }

    @Test
    fun `Tireless Crocag is destroyed when the opponent has no creatures`() {
        val scenario = scenario {
            player1 {
                house(House.BROBNAR)
                inPlay("tireless-crocag", power = 7)
                hand("anger")
            }
            player2 {
                inPlay("umbra", power = 2)
            }
        }
        scenario.run {
            chooseHouse(House.BROBNAR)
            assertThat(p1.creatures.map { it.cardId }).containsExactly("tireless-crocag")
            // Destroying the last enemy creature destroys Crocag at the same
            // state check (keyteki terminal condition).
            fight("tireless-crocag", "umbra")
            assertThat(p1.creatures).isEmpty()
            assertThat(p1.discard).contains("tireless-crocag")
        }
    }

    @Test
    fun `Pitlord forces Dis as the active house`() {
        val scenario = scenario {
            player1 {
                house(House.DIS, House.BROBNAR)
                inPlay("pitlord", power = 9)
            }
        }
        scenario.run {
            val actions = generator.generate(game)
            val houses = actions.filterIsInstance<ChooseHouseAction>().map { it.house }
            assertThat(houses).containsExactly(House.DIS)
        }
    }

    @Test
    fun `Ember Imp limits the opponent to two plays per turn`() {
        val scenario = scenario {
            player1 {
                house(House.BROBNAR)
                hand("punch", "anger")
            }
            player2 {
                house(House.DIS)
                inPlay("ember-imp", power = 2)
            }
        }
        scenario.run {
            endTurn()
            endTurn()
            chooseHouse(House.BROBNAR)
            p1.cardsPlayedThisTurnByHouse[House.BROBNAR] = 1
            assertThat(generator.generate(game).count { it is PlayCardAction }).isGreaterThan(0)
            p1.cardsPlayedThisTurnByHouse[House.BROBNAR] = 2
            assertThat(generator.generate(game).none { it is PlayCardAction }).isTrue()
        }
    }

    @Test
    fun `The Vaultkeeper prevents amber theft`() {
        val scenario = scenario {
            player1 {
                house(House.SHADOWS)
                hand("urchin")
                amber(0)
            }
            player2 {
                house(House.SANCTUM)
                inPlay("the-vaultkeeper", power = 4)
                amber(3)
            }
        }
        scenario.run {
            play("urchin")
            assertThat(p2.amber).isEqualTo(3)
            assertThat(p1.amber).isEqualTo(0)
            assertThat(game.log.any { it.message.contains("cannot be stolen") }).isTrue()
        }
    }

    @Test
    fun `Steal works normally without the Vaultkeeper`() {
        val scenario = scenario {
            player1 {
                house(House.SHADOWS)
                hand("one-last-job")
                amber(0)
            }
            player2 {
                house(House.SANCTUM)
                inPlay("bulwark", power = 5)
                amber(3)
            }
        }
        scenario.run {
            // one-last-job has an amber bonus pip (+1 to its controller before the effect)
            play("one-last-job")
            assertThat(p2.amber).isEqualTo(2)
            assertThat(p1.amber).isEqualTo(2)
        }
    }

    @Test
    fun `Witch of the Wilds allows one off-house Untamed play`() {
        val scenario = scenario {
            player1 {
                house(House.LOGOS, House.UNTAMED)
                inPlay("witch-of-the-wilds", power = 4)
                hand("dew-faerie")
            }
        }
        scenario.run {
            chooseHouse(House.LOGOS)
            assertThat(generator.generate(game).any { it is PlayCardAction && it.cardId == "dew-faerie" }).isTrue()
            p1.cardsPlayedThisTurnByHouse[House.UNTAMED] = 1
            assertThat(generator.generate(game).none { it is PlayCardAction && it.cardId == "dew-faerie" }).isTrue()
        }
    }

    @Test
    fun `Witch of the Wilds does not unlock Untamed while it is the active house`() {
        val scenario = scenario {
            player1 {
                house(House.LOGOS, House.UNTAMED)
                inPlay("witch-of-the-wilds", power = 4)
                hand("dew-faerie")
            }
        }
        scenario.run {
            chooseHouse(House.UNTAMED)
            assertThat(generator.generate(game).any { it is PlayCardAction && it.cardId == "dew-faerie" }).isTrue()
        }
    }

    // ---- Ported keyteki specs (test/server/cards/01-Core) ----

    @Test
    fun `Grommid - actions, artifacts and upgrades playable, creatures not (keyteki)`() {
        val scenario = scenario {
            player1 {
                house(House.MARS)
                inPlay("grommid")
                hand("psychic-network", "commpod", "biomatrix-backup", "zorg")
            }
            player2 {
                amber(3)
                inPlay("doc-bookton", power = 5)
            }
        }
        scenario.run {
            play("psychic-network")
            play("commpod")
            play("biomatrix-backup", target = "grommid#0-1")
            assertThat(generator.generate(game).none { it is PlayCardAction && it.cardId == "zorg" }).isTrue()
        }
    }

    @Test
    fun `Grommid - controller of the creature it attacks loses 1A when Grommid lives (keyteki)`() {
        val scenario = scenario {
            player1 {
                house(House.MARS)
                inPlay("grommid")
            }
            player2 {
                amber(3)
                inPlay("doc-bookton", power = 5)
            }
        }
        scenario.run {
            fight("grommid", "doc-bookton")
            assertThat(p2.amber).isEqualTo(2)
            assertThat(p1.creatures.first().damage).isEqualTo(5)
        }
    }

    @Test
    fun `Grommid - Bad Penny returns to hand and the opponent still loses 1A (keyteki)`() {
        val scenario = scenario {
            player1 {
                house(House.MARS)
                inPlay("grommid")
            }
            player2 {
                amber(3)
                inPlay("bad-penny", power = 1)
            }
        }
        scenario.run {
            fight("grommid", "bad-penny")
            assertThat(p2.hand.map { it }).contains("bad-penny")
            assertThat(p1.creatures.first().damage).isEqualTo(1)
            assertThat(p2.amber).isEqualTo(2)
        }
    }

    @Test
    fun `Grommid - no amber loss when both creatures die (keyteki)`() {
        val scenario = scenario {
            player1 {
                house(House.MARS)
                inPlay("grommid")
            }
            player2 {
                amber(3)
                inPlay("ancient-bear", power = 10)
            }
        }
        scenario.run {
            fight("grommid", "ancient-bear")
            assertThat(p1.creatures).isEmpty()
            assertThat(p2.creatures).isEmpty()
            assertThat(p2.amber).isEqualTo(3)
        }
    }

    @Test
    fun `Grommid - creature attacking it dies and its controller loses 1A (keyteki)`() {
        val scenario = scenario {
            player1 {
                house(House.MARS)
                inPlay("grommid")
            }
            player2 {
                amber(3)
                inPlay("doc-bookton", power = 5)
            }
        }
        scenario.run {
            endTurn()
            fight("doc-bookton", "grommid")
            assertThat(p2.amber).isEqualTo(2)
            assertThat(p1.creatures.first().damage).isEqualTo(5)
        }
    }

    @Test
    fun `The Vaultkeeper - capture and lose still work (keyteki)`() {
        val scenario = scenario {
            player1 {
                house(House.LOGOS, House.BROBNAR)
                hand("dextre", "bumpsy")
                amber(0)
            }
            player2 {
                house(House.SANCTUM)
                amber(4)
                inPlay("the-vaultkeeper", power = 4)
            }
        }
        scenario.run {
            play("dextre")
            assertThat(p2.amber).isEqualTo(3)
            assertThat(p1.creatures.first().capturedAmber).isEqualTo(1)
            play("bumpsy")
            assertThat(p2.amber).isEqualTo(2)
        }
    }

    @Test
    fun `Ember Imp - limit to 2 plays, lift after the imp dies (keyteki)`() {
        val scenario = scenario {
            player1 {
                house(House.SHADOWS)
                inPlay("bulleteye", power = 2)
                hand("umbra", "urchin", "dodger")
            }
            player2 {
                house(House.DIS)
                inPlay("ember-imp", power = 2)
            }
        }
        scenario.run {
            play("umbra")
            play("urchin")
            assertThat(generator.generate(game).none { it is PlayCardAction && it.cardId == "dodger" }).isTrue()
            fight("bulleteye", "ember-imp")
            assertThat(p2.creatures).isEmpty()
            assertThat(generator.generate(game).any { it is PlayCardAction && it.cardId == "dodger" }).isTrue()
        }
    }

    @Test
    fun `Tireless Crocag - destroyed immediately after being played if opponent has no creatures (keyteki)`() {
        val scenario = scenario {
            player1 {
                house(House.BROBNAR)
                hand("tireless-crocag")
            }
        }
        scenario.run {
            play("tireless-crocag")
            assertThat(p1.creatures).isEmpty()
            assertThat(p1.discard).contains("tireless-crocag")
        }
    }

    @Test
    fun `Tireless Crocag - stays while the opponent has creatures, dies when they run out (keyteki)`() {
        val scenario = scenario {
            player1 {
                house(House.BROBNAR)
                hand("tireless-crocag", "punch")
            }
            player2 {
                inPlay("umbra", power = 2)
            }
        }
        scenario.run {
            play("tireless-crocag")
            assertThat(p1.creatures.map { it.cardId }).contains("tireless-crocag")
            play("punch")
            assertThat(p2.creatures).isEmpty()
            assertThat(p1.discard).contains("tireless-crocag")
        }
    }

    @Test
    fun `Tireless Crocag - destroys itself when the opponent's creature dies attacking it (keyteki)`() {
        val scenario = scenario {
            player1 {
                house(House.BROBNAR)
                hand("tireless-crocag")
            }
            player2 {
                inPlay("ancient-bear", power = 5)
            }
        }
        scenario.run {
            play("tireless-crocag")
            endTurn()
            fight("ancient-bear", "tireless-crocag")
            assertThat(p1.discard).contains("tireless-crocag")
            assertThat(p2.creatures).isEmpty()
        }
    }

    @Test
    fun `Tireless Crocag - does not make other creatures usable in any house (keyteki)`() {
        val scenario = scenario {
            player1 {
                house(House.BROBNAR, House.LOGOS)
                inPlay("tireless-crocag", power = 7)
                inPlay("ancient-bear", power = 5)
            }
            player2 {
                inPlay("umbra", power = 2)
            }
        }
        scenario.run {
            chooseHouse(House.LOGOS)
            val actions = generator.generate(game)
            val crocagActions = actions.filterIsInstance<UseCreatureAction>()
                .filter { it.instanceId.startsWith("tireless-crocag") }
            assertThat(crocagActions).isNotEmpty()
            assertThat(actions.filterIsInstance<UseCreatureAction>()
                .none { it.instanceId.startsWith("ancient-bear") }).isTrue()
        }
    }

    @Test
    fun `Witch of the Wilds - one Untamed play, then blocked (keyteki)`() {
        val scenario = scenario {
            player1 {
                house(House.LOGOS)
                inPlay("witch-of-the-wilds", power = 4)
                hand("ancient-bear", "snufflegator")
            }
        }
        scenario.run {
            chooseHouse(House.LOGOS)
            play("ancient-bear")
            assertThat(p1.creatures.map { it.cardId }).contains("ancient-bear")
            assertThat(generator.generate(game).none { it is PlayCardAction && it.cardId == "snufflegator" }).isTrue()
        }
    }

    @Test
    fun `The Vaultkeeper also blocks Too Much to Protect`() {
        val scenario = scenario {
            player1 {
                house(House.SANCTUM)
                hand("too-much-to-protect")
                amber(0)
            }
            player2 {
                house(House.SANCTUM)
                inPlay("the-vaultkeeper", power = 4)
                amber(9)
            }
        }
        scenario.run {
            // The engine recalculates after every action; the scenario setup hasn't yet.
            com.aember.analyzer.domain.engine.effects.EffectRegistry.recalculate(library, game)
            play("too-much-to-protect")
            assertThat(p2.amber).isEqualTo(9)
            assertThat(p1.amber).isEqualTo(1) // bonus pip only
        }
    }

    @Test
    fun `Pitlord never leaves a player without a legal house`() {
        val scenario = scenario {
            player1 {
                house(House.DIS, House.BROBNAR)
                inPlay("pitlord", power = 9)
            }
        }
        scenario.run {
            // Dis denied by Control the Weak: restriction yields rather than leaving no choice.
            p1.deniedHouseNextTurn = House.DIS
            assertThat(generator.generate(game).filterIsInstance<ChooseHouseAction>().map { it.house })
                .containsExactly(House.BROBNAR)
        }
    }

    @Test
    fun `Pitlord under a controller without Dis does not force an impossible house`() {
        val scenario = scenario {
            player1 {
                house(House.BROBNAR)
                inPlay("pitlord", power = 9)
            }
        }
        scenario.run {
            assertThat(generator.generate(game).filterIsInstance<ChooseHouseAction>().map { it.house })
                .containsExactly(House.BROBNAR)
        }
    }
}
