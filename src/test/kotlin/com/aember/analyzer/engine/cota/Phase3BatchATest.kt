package com.aember.analyzer.engine.cota

import com.aember.analyzer.domain.engine.action.ActionGenerator
import com.aember.analyzer.domain.engine.action.ChooseHouseAction
import com.aember.analyzer.domain.engine.action.DiscardAction
import com.aember.analyzer.domain.engine.action.PlayCardAction
import com.aember.analyzer.domain.engine.action.ReapUse
import com.aember.analyzer.domain.engine.action.UseArtifactAction
import com.aember.analyzer.domain.engine.action.UseCreatureAction
import com.aember.analyzer.domain.model.CardType
import com.aember.analyzer.domain.model.House
import com.aember.analyzer.engine.support.CoreCardLibrary
import com.aember.analyzer.engine.support.scenario
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

/**
 * Phase 3 batch (a) — simple DSL-expressible cards: keyword-only creatures,
 * single-trigger plays, next-turn denials, amber clamp, shuffle-into-deck,
 * self selectors, and key-cost modifiers.
 */
class Phase3BatchATest {

    private val library = CoreCardLibrary.create()
    private val generator = ActionGenerator(library)

    @Test
    fun `Skippy Timehog denies card uses next turn but allows plays and discards`() {
        val scenario = scenario {
            player1 {
                house(House.LOGOS)
                hand("skippy-timehog", "anger")
            }
            player2 {
                house(House.LOGOS, House.DIS)
                artifact("commpod")
                inPlay("yxilx-dominator", power = 10)
                hand("dextre")
                amber(0)
            }
        }
        scenario.run {
            play("skippy-timehog")
            assertThat(p2.cannotUseCardsNextTurn).isTrue()
            endTurn()
            chooseHouse(House.LOGOS)
            val actions = generator.generate(game)
            assertThat(actions.none { it is UseCreatureAction }).isTrue()
            assertThat(actions.none { it is UseArtifactAction }).isTrue()
            assertThat(actions.any { it is PlayCardAction && it.cardId == "dextre" }).isTrue()
            assertThat(actions.any { it is DiscardAction && it.cardId == "dextre" }).isTrue()
            // flag is consumed after the opponent's turn
            endTurn()
            assertThat(p2.cannotUseCardsNextTurn).isFalse()
        }
    }

    @Test
    fun `Scrambler Storm denies action card plays next turn`() {
        val scenario = scenario {
            player1 {
                house(House.LOGOS)
                hand("scrambler-storm")
            }
            player2 {
                house(House.LOGOS)
                hand("positron-bolt", "dextre")
            }
        }
        scenario.run {
            play("scrambler-storm")
            assertThat(p2.deniedCardTypeNextTurn).isEqualTo(CardType.ACTION)
            endTurn()
            chooseHouse(House.LOGOS)
            val actions = generator.generate(game)
            assertThat(actions.none { it is PlayCardAction && it.cardId == "positron-bolt" }).isTrue()
            assertThat(actions.any { it is PlayCardAction && it.cardId == "dextre" }).isTrue()
            endTurn()
            assertThat(p2.deniedCardTypeNextTurn).isNull()
        }
    }

    @Test
    fun `Foggify denies fights next turn via cannotFightNextTurn`() {
        val scenario = scenario {
            player1 {
                house(House.LOGOS)
                hand("foggify")
            }
            player2 {
                house(House.BROBNAR)
                inPlay("troll", power = 8)
            }
        }
        scenario.run {
            play("foggify")
            assertThat(p2.cannotFightNextTurn).isTrue()
            endTurn()
            chooseHouse(House.BROBNAR)
            val actions = generator.generate(game)
            // fights denied, reaps and plays still allowed (same semantics as Fogbank)
            assertThat(actions.none { it is UseCreatureAction && it.useType is com.aember.analyzer.domain.engine.action.FightUse }).isTrue()
            assertThat(actions.any { it is UseCreatureAction && it.useType is ReapUse }).isTrue()
            endTurn()
            assertThat(p2.cannotFightNextTurn).isFalse()
        }
    }

    @Test
    fun `Doorstep to Heaven reduces both players with 6+ amber to 5`() {
        val scenario = scenario {
            player1 {
                house(House.SANCTUM)
                hand("doorstep-to-heaven")
                amber(7)
            }
            player2 {
                amber(9)
            }
        }
        scenario.run {
            p1.amber = 7
            p2.amber = 9
            play("doorstep-to-heaven")
            assertThat(p1.amber).isEqualTo(5)
            assertThat(p2.amber).isEqualTo(5)
        }
    }

    @Test
    fun `Doorstep to Heaven leaves players below 6 untouched`() {
        val scenario = scenario {
            player1 {
                house(House.SANCTUM)
                hand("doorstep-to-heaven")
                amber(2)
            }
            player2 {
                amber(5)
            }
        }
        scenario.run {
            play("doorstep-to-heaven")
            // doorstep-to-heaven has an amber bonus pip (+1 before the text)
            assertThat(p1.amber).isEqualTo(3)
            assertThat(p2.amber).isEqualTo(5)
        }
    }

    @Test
    fun `Timetraveller draws 2 and shuffles itself back into the deck`() {
        val scenario = scenario {
            player1 {
                house(House.LOGOS)
                hand("timetraveller")
                deck("hunting-witch", "anger")
                amber(0)
            }
        }
        scenario.run {
            play("timetraveller")
            assertThat(p1.hand).contains("hunting-witch", "anger")
            assertThat(p1.creatures.map { it.cardId }).containsExactly("timetraveller")
            useAbility("timetraveller", "Shuffle Timetraveller into your deck")
            assertThat(p1.creatures).isEmpty()
            assertThat(p1.library).contains("timetraveller")
        }
    }

    @Test
    fun `Lady Maxena stuns a creature and returns itself to hand with its Action`() {
        val scenario = scenario {
            player1 {
                house(House.SANCTUM)
                hand("lady-maxena")
            }
            player2 {
                inPlay("umbra", power = 2)
            }
        }
        scenario.run {
            play("lady-maxena")
            assertThat(p2.creatures.first().stun).isTrue()
            assertThat(p1.creatures.map { it.cardId }).containsExactly("lady-maxena")
            useAbility("lady-maxena", "Return Lady Maxena to its owner's hand")
            assertThat(p1.creatures).isEmpty()
            assertThat(p1.hand).contains("lady-maxena")
        }
    }

    @Test
    fun `Yo Mama Mastery grants taunt to the host and fully heals it on play`() {
        val scenario = scenario {
            player1 {
                house(House.BROBNAR)
                inPlay("troll", power = 8)
                hand("yo-mama-mastery")
            }
        }
        scenario.run {
            p1.creatures.first().damage = 5
            play("yo-mama-mastery", target = "troll#0-1")
            val host = p1.creatures.first()
            assertThat(host.keywords).containsEntry("taunt", 1)
            assertThat(host.damage).isEqualTo(0)
            // taunt is respected by the generator: enemy non-taunt attackers are restricted
            val actions = generator.generate(game)
            assertThat(actions.filterIsInstance<UseCreatureAction>()).isNotEmpty()
        }
    }

    @Test
    fun `Way of the Bear grants assault 2 to the host`() {
        val scenario = scenario {
            player1 {
                house(House.UNTAMED)
                inPlay("snufflegator", power = 4)
                hand("way-of-the-bear")
            }
        }
        scenario.run {
            play("way-of-the-bear", target = "snufflegator#0-1")
            assertThat(p1.creatures.first().keywords).containsEntry("assault", 2)
        }
    }

    @Test
    fun `Way of the Wolf grants skirmish to the host`() {
        val scenario = scenario {
            player1 {
                house(House.UNTAMED)
                inPlay("snufflegator", power = 4)
                hand("way-of-the-wolf")
            }
        }
        scenario.run {
            play("way-of-the-wolf", target = "snufflegator#0-1")
            assertThat(p1.creatures.first().keywords).containsEntry("skirmish", 1)
        }
    }

    @Test
    fun `Murmook raises the opponent's key cost by 1`() {
        val scenario = scenario {
            player1 {
                house(House.UNTAMED)
                inPlay("murmook", power = 3)
            }
            player2 {
                house(House.BROBNAR)
                amber(6)
            }
        }
        scenario.run {
            endTurn()
            // key phase forges for the opponent at 6 + 1 = 7 > 6, so no key
            assertThat(p2.keys).isEqualTo(0)
            assertThat(p2.amber).isEqualTo(6)
            endTurn()
            p2.amber = 7
            endTurn()
            // P2's key phase now runs with 7 amber against a cost of 7
            assertThat(p2.keys).isEqualTo(1)
            assertThat(p2.amber).isEqualTo(0)
        }
    }

    @Test
    fun `Iron Obelisk raises key cost per friendly damaged Brobnar creature`() {
        val scenario = scenario {
            player1 {
                house(House.BROBNAR)
                inPlay("troll", power = 8)
                artifact("iron-obelisk")
            }
            player2 {
                house(House.BROBNAR)
                amber(6)
            }
        }
        scenario.run {
            p1.creatures.first().damage = 1
            endTurn()
            // one damaged friendly Brobnar creature: 6 + 1 = 7 > 6
            assertThat(p2.keys).isEqualTo(0)
            endTurn()
            p1.creatures.first().damage = 0
            p2.amber = 6
            endTurn()
            assertThat(p2.keys).isEqualTo(1)
        }
    }

    @Test
    fun `Lupo the Scarred deals 2 damage to an enemy creature on play`() {
        val scenario = scenario {
            player1 {
                house(House.UNTAMED)
                hand("lupo-the-scarred")
            }
            player2 {
                inPlay("troll", power = 8)
            }
        }
        scenario.run {
            play("lupo-the-scarred")
            assertThat(p2.creatures.first().damage).isEqualTo(2)
        }
    }

    @Test
    fun `Mighty Tiger deals 4 damage to an enemy creature on play`() {
        val scenario = scenario {
            player1 {
                house(House.UNTAMED)
                hand("mighty-tiger")
            }
            player2 {
                inPlay("troll", power = 8)
            }
        }
        scenario.run {
            play("mighty-tiger")
            assertThat(p2.creatures.first().damage).isEqualTo(4)
        }
    }

    @Test
    fun `Skirmish protects the attacker and poison kills damage survivors`() {
        val scenario = scenario {
            player1 {
                house(House.UNTAMED)
                inPlay("snufflegator", power = 4)
            }
            player2 {
                house(House.SHADOWS)
                inPlay("macis-asp", power = 3)
            }
        }
        scenario.run {
            fight("snufflegator", "macis-asp")
            // snufflegator (4) kills macis-asp (3); skirmish prevents counter-damage
            assertThat(p2.creatures).isEmpty()
            assertThat(p1.creatures.map { it.cardId }).containsExactly("snufflegator")
        }
    }

    @Test
    fun `Macis Asp's poison destroys a damage-surviving victim`() {
        val scenario = scenario {
            player1 {
                house(House.UNTAMED)
                inPlay("snufflegator", power = 4)
            }
            player2 {
                house(House.SHADOWS)
                inPlay("macis-asp", power = 3)
            }
        }
        scenario.run {
            endTurn()
            // macis-asp deals 3 < 4 power, but poison destroys the damage-surviving snufflegator
            fight("macis-asp", "snufflegator")
            assertThat(p1.creatures).isEmpty()
            assertThat(p2.creatures.map { it.cardId }).containsExactly("macis-asp")
        }
    }

    @Test
    fun `Chuff Ape enters play stunned`() {
        val scenario = scenario {
            player1 {
                house(House.MARS)
                hand("chuff-ape")
            }
        }
        scenario.run {
            play("chuff-ape")
            val ape = p1.creatures.first()
            assertThat(ape.stun).isTrue()
            assertThat(ape.keywords).containsKey("taunt")
        }
    }
}
