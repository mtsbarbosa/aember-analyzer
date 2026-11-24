package com.aember.analyzer.engine.cota

import com.aember.analyzer.domain.engine.action.ActionGenerator
import com.aember.analyzer.domain.engine.action.ReapUse
import com.aember.analyzer.domain.model.CardType
import com.aember.analyzer.domain.model.House
import com.aember.analyzer.engine.support.CoreCardLibrary
import com.aember.analyzer.engine.support.scenario
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

/**
 * Phase 3 batch (d) — the inert-crowd batch: artifact economy (Tentacus, Customs Office,
 * Gorm of Omm, Whispering Reliquary, Grasping Vines, Veylan Analyst, Anomaly Exploiter),
 * fight-target triggers (Lord Golgotha, Zorg), upgrade-granted reaps (Silent Dagger,
 * Red Planet Ray Gun), damage patterns (Positron Bolt, Guardian Demon, Qyxxlyx,
 * Cooperative Hunting, Red Hot Armor, EMP Blast, Bulleteye, Special Delivery), hand/deck
 * attacks (Deep Probe, Sound the Horns, Squawker), forge/amber effects (Interdimensional
 * Graft, Shatter Storm, Blood Money, Mating Season), turn-scoped may-use (Sigil of
 * Brotherhood, Combat Pheromones), Strange Gizmo, Brain Eater, Faygin, Vezyma, Charge,
 * Masterplan, Lifeward, Potion of Invulnerability, and Valdr's flank bonus.
 */
class Phase3BatchDTest {

    private val library = CoreCardLibrary.create()
    private val generator = ActionGenerator(library)

    @Test
    fun `Gorm of Omm sacrifices itself and destroys an enemy artifact`() {
        val scenario = scenario {
            player1 {
                house(House.SANCTUM)
                artifact("gorm-of-omm")
            }
            player2 {
                artifact("wild-wormhole")
            }
        }
        scenario.run {
            useAbility("gorm-of-omm", "Omni")
            assertThat(p1.discard).contains("gorm-of-omm")
            assertThat(p2.discard).contains("wild-wormhole")
            assertThat(p2.artifacts).isEmpty()
        }
    }

    @Test
    fun `Deep Probe discards enemy creatures of the most common house in their hand`() {
        val scenario = scenario {
            player1 {
                house(House.MARS)
                hand("deep-probe")
            }
            player2 {
                hand("snufflegator", "grommid", "qyxxlyx-plague-master")
            }
        }
        scenario.run {
            play("deep-probe")
            // Two Mars creatures (grommid, dust-imp) vs one Untamed creature: Mars is chosen.
            assertThat(p2.discard).contains("grommid", "qyxxlyx-plague-master")
            assertThat(p2.discard).doesNotContain("snufflegator")
            assertThat(p2.hand).contains("snufflegator")
        }
    }

    @Test
    fun `EMP Blast stuns Mars and Robot creatures and destroys each artifact`() {
        val scenario = scenario {
            player1 {
                house(House.MARS)
                hand("emp-blast")
                inPlay("zorg")
                artifact("nepenthe-seed")
            }
            player2 {
                inPlay("batdrone")
                inPlay("snufflegator")
                artifact("wild-wormhole")
            }
        }
        scenario.run {
            play("emp-blast")
            // Mars creature and Robot-trait creature are stunned; a plain beast is not.
            assertThat(p1.creatures.first { it.cardId == "zorg" }.stun).isTrue()
            assertThat(p2.creatures.first { it.cardId == "batdrone" }.stun).isTrue()
            assertThat(p2.creatures.first { it.cardId == "snufflegator" }.stun).isFalse()
            assertThat(p1.artifacts).isEmpty()
            assertThat(p2.artifacts).isEmpty()
        }
    }

    @Test
    fun `Interdimensional Graft claims the opponent's remaining amber after they forge`() {
        val scenario = scenario {
            player1 {
                house(House.LOGOS)
                hand("interdimensional-graft")
                amber(2)
            }
            player2 {
                amber(8)
            }
        }
        scenario.run {
            play("interdimensional-graft")
            endTurn()
            // Player 2 forges at the start of their turn (8 - 6 = 2 remaining) and
            // gives the remainder to Player 1.
            assertThat(p2.amber).isEqualTo(0)
            // 2 setup + 1 bonus pip + 2 remaining amber from the forge.
            assertThat(p1.amber).isEqualTo(5)
        }
    }

    @Test
    fun `Positron Bolt cascades 3-2-1 damage along the battleline from a flank`() {
        val scenario = scenario {
            player1 {
                house(House.LOGOS)
                hand("positron-bolt")
            }
            player2 {
                inPlay("snufflegator", power = 5)
                inPlay("doc-bookton", power = 5)
                inPlay("dust-imp", power = 5)
            }
        }
        scenario.run {
            play("positron-bolt")
            val battleline = p2.creatures
            assertThat(battleline[0].damage).isEqualTo(3)
            assertThat(battleline[1].damage).isEqualTo(2)
            assertThat(battleline[2].damage).isEqualTo(1)
        }
    }

    @Test
    fun `Tentacus charges the opponent one amber per artifact use`() {
        val scenario = scenario {
            player1 {
                house(House.DIS)
                inPlay("tentacus")
            }
            player2 {
                house(House.LOGOS)
                artifact("wild-wormhole")
                amber(3)
            }
        }
        scenario.run {
            endTurn()
            chooseHouse(House.LOGOS)
            useAbility("wild-wormhole", "Omni")
            assertThat(p2.amber).isEqualTo(2)
            assertThat(p1.amber).isEqualTo(1)
        }
    }

    @Test
    fun `Customs Office charges the opponent one amber per artifact play`() {
        val scenario = scenario {
            player1 {
                house(House.SHADOWS)
                artifact("customs-office")
            }
            player2 {
                house(House.MARS)
                hand("commpod")
                amber(2)
            }
        }
        scenario.run {
            endTurn()
            chooseHouse(House.MARS)
            play("commpod")
            assertThat(p2.amber).isEqualTo(1)
            assertThat(p1.amber).isEqualTo(1)
            assertThat(p2.artifacts).hasSize(1)
        }
    }

    @Test
    fun `Guardian Demon heals up to two and deals the healed amount elsewhere`() {
        val scenario = scenario {
            player1 {
                house(House.DIS)
                hand("guardian-demon")
                inPlay("dust-imp", power = 5, damage = 2)
            }
            player2 {
                inPlay("doc-bookton", power = 6)
            }
        }
        scenario.run {
            play("guardian-demon")
            assertThat(p1.creatures.first { it.cardId == "dust-imp" }.damage).isEqualTo(0)
            assertThat(p2.creatures.first { it.cardId == "doc-bookton" }.damage).isEqualTo(2)
        }
    }

    @Test
    fun `Red Planet Ray Gun grants the host a reap dealing one damage per Mars creature`() {
        val scenario = scenario {
            player1 {
                house(House.MARS)
                inPlay("zorg")
                hand("red-planet-ray-gun")
            }
            player2 {
                inPlay("doc-bookton", power = 5)
                inPlay("snufflegator", power = 5)
            }
        }
        scenario.run {
            play("red-planet-ray-gun", target = "zorg#0-1")
            reap("zorg")
            // One Mars creature in play (the host itself) -> 1 damage to the strongest enemy.
            assertThat(p2.creatures.first { it.cardId == "doc-bookton" }.damage).isEqualTo(1)
        }
    }

    @Test
    fun `Selwyn the Fence moves captured amber to his controller's pool`() {
        val scenario = scenario {
            player1 {
                house(House.SHADOWS)
                inPlay("selwyn-the-fence")
                inPlay("urchin", capturedAmber = 2)
            }
        }
        scenario.run {
            reap("selwyn-the-fence")
            // 1 from the reap + 1 moved from the urchin.
            assertThat(p1.amber).isEqualTo(2)
            assertThat(p1.creatures.first { it.cardId == "urchin" }.capturedAmber).isEqualTo(1)
        }
    }

    @Test
    fun `Silent Dagger grants the host a reap dealing four damage to a flank creature`() {
        val scenario = scenario {
            player1 {
                house(House.SHADOWS)
                inPlay("urchin")
                hand("silent-dagger")
            }
            player2 {
                inPlay("snufflegator", power = 5)
                inPlay("doc-bookton", power = 5)
            }
        }
        scenario.run {
            play("silent-dagger", target = "urchin#0-1")
            reap("urchin")
            // Greedy flank pick: the strongest enemy flank creature.
            assertThat(p2.creatures.first { it.cardId == "snufflegator" }.damage).isEqualTo(4)
        }
    }

    @Test
    fun `Valdr deals two extra damage when attacking an enemy flank creature`() {
        val scenario = scenario {
            player1 {
                house(House.BROBNAR)
                inPlay("valdr", power = 4)
            }
            player2 {
                inPlay("doc-bookton", power = 5)
                inPlay("dust-imp", power = 7)
            }
        }
        scenario.run {
            // Right-flank enemy is the last creature; the flank pick is via the fight
            // target we choose explicitly.
            fight("valdr", "dust-imp")
            assertThat(p2.creatures.first { it.cardId == "dust-imp" }.damage).isEqualTo(6)
        }
    }

    @Test
    fun `Brain Eater draws after a creature is destroyed fighting it`() {
        val scenario = scenario {
            player1 {
                house(House.LOGOS)
                inPlay("brain-eater", power = 5)
                deck("neutron-shark")
            }
            player2 {
                inPlay("dust-imp", power = 1)
            }
        }
        scenario.run {
            val handBefore = p1.hand.size
            fight("brain-eater", "dust-imp")
            assertThat(p2.creatures).isEmpty()
            assertThat(p1.hand.size).isEqualTo(handBefore + 1)
        }
    }

    @Test
    fun `Strange Gizmo destroys each creature and artifact after its owner forges a key`() {
        val scenario = scenario {
            player1 {
                house(House.LOGOS)
                hand("strange-gizmo")
                amber(12)
                inPlay("dust-imp")
                artifact("wild-wormhole")
            }
            player2 {
                inPlay("doc-bookton")
                artifact("nepenthe-seed")
            }
        }
        scenario.run {
            play("strange-gizmo")
            endTurn()
            endTurn()
            // Player 1 forges at the start of their next turn (second key), then the gizmo fires.
            assertThat(p1.keys).isEqualTo(2)
            assertThat(p1.creatures).isEmpty()
            assertThat(p2.creatures).isEmpty()
            assertThat(p1.artifacts).isEmpty()
            assertThat(p2.artifacts).isEmpty()
        }
    }

    @Test
    fun `Cooperative Hunting divides one damage per friendly creature among enemies`() {
        val scenario = scenario {
            player1 {
                house(House.UNTAMED)
                hand("cooperative-hunting")
                inPlay("snufflegator")
                inPlay("dust-imp")
                inPlay("urchin")
            }
            player2 {
                inPlay("doc-bookton", power = 5)
                inPlay("batdrone", power = 5)
            }
        }
        scenario.run {
            play("cooperative-hunting")
            assertThat(p2.creatures.first { it.cardId == "doc-bookton" }.damage).isEqualTo(2)
            assertThat(p2.creatures.first { it.cardId == "batdrone" }.damage).isEqualTo(1)
        }
    }

    @Test
    fun `Red Hot Armor strips enemy armor until end of turn and deals damage per point lost`() {
        val scenario = scenario {
            player1 {
                house(House.DIS)
                hand("red-hot-armor")
            }
            player2 {
                inPlay("snufflegator", power = 5, armor = 2)
                inPlay("doc-bookton", power = 5)
            }
        }
        scenario.run {
            play("red-hot-armor")
            val gator = p2.creatures.first { it.cardId == "snufflegator" }
            assertThat(gator.armorStrippedThisTurn).isEqualTo(2)
            assertThat(gator.damage).isEqualTo(2)
            assertThat(p2.creatures.first { it.cardId == "doc-bookton" }.damage).isEqualTo(0)
        }
    }

    @Test
    fun `Sound the Horns discards the deck top until a Brobnar creature, then takes it to hand`() {
        val scenario = scenario {
            player1 {
                house(House.BROBNAR)
                hand("sound-the-horns")
                deck("dust-imp", "grommid", "valdr")
            }
        }
        scenario.run {
            play("sound-the-horns")
            assertThat(p1.discard).contains("dust-imp", "grommid")
            assertThat(p1.hand).contains("valdr")
            assertThat(p1.library).isEmpty()
        }
    }

    @Test
    fun `Squawker readies an own exhausted Mars creature when there is no better target`() {
        val scenario = scenario {
            player1 {
                house(House.MARS)
                hand("squawker")
                inPlay("zorg", exhausted = true)
            }
        }
        scenario.run {
            play("squawker")
            assertThat(p1.creatures.first { it.cardId == "zorg" }.exhausted).isFalse()
        }
    }

    @Test
    fun `Squawker stuns an enemy non-Mars creature otherwise`() {
        val scenario = scenario {
            player1 {
                house(House.MARS)
                hand("squawker")
            }
            player2 {
                inPlay("snufflegator")
            }
        }
        scenario.run {
            play("squawker")
            assertThat(p2.creatures.first { it.cardId == "snufflegator" }.stun).isTrue()
        }
    }

    @Test
    fun `Sigil of Brotherhood lets the controller use Sanctum creatures for the turn`() {
        val scenario = scenario {
            player1 {
                house(House.MARS)
                artifact("sigil-of-brotherhood")
                inPlay("champion-anaphiel", exhausted = true)
            }
        }
        scenario.run {
            useAbility("sigil-of-brotherhood", "Omni")
            assertThat(p1.discard).contains("sigil-of-brotherhood")
            // Off-house (Mars active) Sanctum creature use is now legal.
            reap("champion-anaphiel", house = House.MARS)
            assertThat(p1.amber).isEqualTo(1)
        }
    }

    @Test
    fun `Special Delivery sacrifices itself, deals 3 to a flank creature and purges it when destroyed`() {
        val scenario = scenario {
            player1 {
                house(House.SHADOWS)
                artifact("special-delivery")
            }
            player2 {
                inPlay("dust-imp", power = 2)
            }
        }
        scenario.run {
            useAbility("special-delivery", "Omni")
            assertThat(p1.discard).contains("special-delivery")
            assertThat(p2.purged).contains("dust-imp")
            assertThat(p2.creatures).isEmpty()
        }
    }

    @Test
    fun `Whispering Reliquary returns an artifact to its owner's hand`() {
        val scenario = scenario {
            player1 {
                house(House.SANCTUM)
                artifact("whispering-reliquary")
            }
            player2 {
                artifact("wild-wormhole")
            }
        }
        scenario.run {
            useAbility("whispering-reliquary", "Return an artifact to its owner's hand")
            assertThat(p2.hand).contains("wild-wormhole")
            assertThat(p2.artifacts).isEmpty()
        }
    }

    @Test
    fun `Faygin returns an Urchin from the discard pile to hand on reap`() {
        val scenario = scenario {
            player1 {
                house(House.SHADOWS)
                inPlay("faygin")
                discard("urchin")
            }
        }
        scenario.run {
            reap("faygin")
            assertThat(p1.hand).contains("urchin")
            assertThat(p1.discard).doesNotContain("urchin")
        }
    }

    @Test
    fun `Lord Golgotha deals 3 damage to the fight target's neighbors before the fight`() {
        val scenario = scenario {
            player1 {
                house(House.SANCTUM)
                inPlay("lord-golgotha", power = 5)
            }
            player2 {
                inPlay("snufflegator", power = 5)
                inPlay("doc-bookton", power = 6)
                inPlay("dust-imp", power = 5)
            }
        }
        scenario.run {
            fight("lord-golgotha", "doc-bookton")
            val battleline = p2.creatures
            assertThat(battleline[0].damage).isEqualTo(3)
            assertThat(battleline[1].damage).isEqualTo(5)
            assertThat(battleline[2].damage).isEqualTo(3)
        }
    }

    @Test
    fun `Zorg enters stunned and stuns the fight target and its neighbors`() {
        val scenario = scenario {
            player1 {
                house(House.MARS)
                inPlay("zorg", power = 5, stunned = true)
            }
            player2 {
                inPlay("snufflegator", power = 5)
                inPlay("doc-bookton", power = 6)
                inPlay("dust-imp", power = 5)
            }
        }
        scenario.run {
            assertThat(p1.creatures.first { it.cardId == "zorg" }.stun).isTrue()
            unstun("zorg")
            fight("zorg", "doc-bookton")
            val battleline = p2.creatures
            assertThat(battleline[0].stun).isTrue()
            assertThat(battleline[1].stun).isTrue()
            assertThat(battleline[2].stun).isTrue()
        }
    }

    @Test
    fun `Blood Money places two amber from the common supply on an enemy creature`() {
        val scenario = scenario {
            player1 {
                house(House.BROBNAR)
                hand("blood-money")
            }
            player2 {
                inPlay("doc-bookton")
            }
        }
        scenario.run {
            play("blood-money")
            assertThat(p2.creatures.first { it.cardId == "doc-bookton" }.capturedAmber).isEqualTo(2)
            assertThat(p1.amber).isEqualTo(0)
            assertThat(p2.amber).isEqualTo(0)
        }
    }

    @Test
    fun `Veylan Analyst gains one amber each time its controller uses an artifact`() {
        val scenario = scenario {
            player1 {
                house(House.LOGOS)
                inPlay("veylan-analyst")
                artifact("wild-wormhole")
            }
        }
        scenario.run {
            useAbility("wild-wormhole", "Omni")
            assertThat(p1.amber).isEqualTo(1)
        }
    }

    @Test
    fun `Combat Pheromones marks two Mars cards usable for the turn`() {
        val scenario = scenario {
            player1 {
                house(House.LOGOS)
                artifact("combat-pheromones")
                inPlay("zorg", exhausted = true)
                inPlay("qyxxlyx-plague-master", exhausted = true)
            }
        }
        scenario.run {
            useAbility("combat-pheromones", "Omni")
            assertThat(p1.discard).contains("combat-pheromones")
            assertThat(p1.mayUseCardIdsThisTurn).hasSize(2)
            // Off-house Mars creature use is now legal.
            reap("zorg", house = House.LOGOS)
            assertThat(p1.amber).isEqualTo(1)
        }
    }

    @Test
    fun `Bulleteye destroys a flank creature on reap`() {
        val scenario = scenario {
            player1 {
                house(House.SHADOWS)
                inPlay("bulleteye")
            }
            player2 {
                inPlay("snufflegator", power = 5)
                inPlay("doc-bookton", power = 5)
            }
        }
        scenario.run {
            reap("bulleteye")
            // Greedy flank pick destroys the strongest enemy flank creature.
            assertThat(p2.creatures).hasSize(1)
            assertThat(p2.discard).contains("snufflegator")
        }
    }

    @Test
    fun `Qyxxlyx deals 3 armor-ignoring damage to each Human creature`() {
        val scenario = scenario {
            player1 {
                house(House.MARS)
                inPlay("qyxxlyx-plague-master", power = 6)
                inPlay("doc-bookton", power = 1)
            }
            player2 {
                inPlay("doc-bookton", power = 5, armor = 2)
                inPlay("snufflegator", power = 3)
            }
        }
        scenario.run {
            fight("qyxxlyx-plague-master", "snufflegator")
            // Both Humans take 3 damage ignoring armor; the armored enemy Human ignores its armor.
            assertThat(p2.creatures.first { it.cardId == "doc-bookton" }.damage).isEqualTo(3)
            // The 1-power friendly Human is destroyed by the plague.
            assertThat(p1.creatures.map { it.cardId }).doesNotContain("doc-bookton")
            // The fight itself still resolved: the fight target died to the 6 power damage.
            assertThat(p2.creatures.map { it.cardId }).doesNotContain("snufflegator")
        }
    }

    @Test
    fun `Vezyma Thinkdrone archives a damaged friendly creature on reap`() {
        val scenario = scenario {
            player1 {
                house(House.MARS)
                inPlay("vezyma-thinkdrone")
                inPlay("dust-imp", damage = 1)
            }
        }
        scenario.run {
            reap("vezyma-thinkdrone")
            assertThat(p1.archives).contains("dust-imp")
            assertThat(p1.creatures).hasSize(1)
        }
    }

    @Test
    fun `Anomaly Exploiter destroys a damaged creature`() {
        val scenario = scenario {
            player1 {
                house(House.LOGOS)
                artifact("anomaly-exploiter")
            }
            player2 {
                inPlay("doc-bookton", damage = 1)
                inPlay("dust-imp")
            }
        }
        scenario.run {
            useAbility("anomaly-exploiter", "Destroy a damaged creature")
            assertThat(p2.discard).contains("doc-bookton")
            assertThat(p2.creatures).hasSize(1)
        }
    }

    @Test
    fun `Lifeward stops the opponent from playing creatures on their next turn`() {
        val scenario = scenario {
            player1 {
                house(House.DIS)
                artifact("lifeward")
            }
            player2 {
                house(House.UNTAMED)
                hand("snufflegator")
            }
        }
        scenario.run {
            useAbility("lifeward", "Omni")
            assertThat(p1.discard).contains("lifeward")
            endTurn()
            chooseHouse(House.UNTAMED)
            val creaturePlays = generator.generate(game).filter { action ->
                action is com.aember.analyzer.domain.engine.action.PlayCardAction &&
                    library.cardData(action.cardId)?.type == CardType.CREATURE
            }
            assertThat(creaturePlays).isEmpty()
        }
    }

    @Test
    fun `Potion of Invulnerability makes friendly creatures immune to damage for the turn`() {
        val scenario = scenario {
            player1 {
                house(House.SANCTUM)
                artifact("potion-of-invulnerability")
                inPlay("champion-anaphiel", power = 4)
            }
            player2 {
                house(House.BROBNAR)
                inPlay("valdr", power = 4)
            }
        }
        scenario.run {
            useAbility("potion-of-invulnerability", "Omni")
            assertThat(p1.discard).contains("potion-of-invulnerability")
            // The immunity lasts for the remainder of Player 1's turn.
            fight("champion-anaphiel", "valdr")
            assertThat(p1.creatures.first { it.cardId == "champion-anaphiel" }.damage).isEqualTo(0)
        }
    }

    @Test
    fun `Shatter Storm loses all own amber and makes the opponent lose triple`() {
        val scenario = scenario {
            player1 {
                house(House.MARS)
                hand("shatter-storm")
                amber(4)
            }
            player2 {
                amber(10)
            }
        }
        scenario.run {
            play("shatter-storm")
            assertThat(p1.amber).isEqualTo(0)
            assertThat(p2.amber).isEqualTo(0)
        }
    }

    @Test
    fun `Mating Season shuffles each Mars creature into its owner's deck with amber per creature`() {
        val scenario = scenario {
            player1 {
                house(House.MARS)
                hand("mating-season")
                amber(0)
                inPlay("zorg")
                inPlay("snufflegator")
            }
            player2 {
                amber(0)
                inPlay("qyxxlyx-plague-master")
                inPlay("doc-bookton")
            }
        }
        scenario.run {
            play("mating-season")
            assertThat(p1.creatures).hasSize(1)
            assertThat(p2.creatures).hasSize(1)
            // 1 bonus pip on the card + 1 per own creature shuffled.
            assertThat(p1.amber).isEqualTo(2)
            assertThat(p2.amber).isEqualTo(1)
            assertThat(p1.library).contains("zorg")
            assertThat(p2.library).contains("qyxxlyx-plague-master")
        }
    }

    @Test
    fun `Grasping Vines returns up to three artifacts to their owners' hands`() {
        val scenario = scenario {
            player1 {
                house(House.UNTAMED)
                hand("grasping-vines")
                artifact("nepenthe-seed")
            }
            player2 {
                artifact("wild-wormhole")
                artifact("commpod")
            }
        }
        scenario.run {
            play("grasping-vines")
            assertThat(p1.hand).contains("nepenthe-seed")
            assertThat(p2.hand).contains("wild-wormhole", "commpod")
        }
    }

    @Test
    fun `Charge makes each creature played this turn deal 2 damage to an enemy creature`() {
        val scenario = scenario {
            player1 {
                house(House.UNTAMED)
                hand("charge", "snufflegator")
            }
            player2 {
                inPlay("doc-bookton", power = 5)
            }
        }
        scenario.run {
            play("charge")
            play("snufflegator")
            assertThat(p2.creatures.first { it.cardId == "doc-bookton" }.damage).isEqualTo(2)
        }
    }

    @Test
    fun `Masterplan stacks a hand card and plays it from beneath on omni`() {
        val scenario = scenario {
            player1 {
                house(House.SHADOWS)
                hand("masterplan", "wild-wormhole")
            }
        }
        scenario.run {
            play("masterplan")
            val masterplan = p1.artifacts.first { it.cardId == "masterplan" }
            assertThat(masterplan.beneathCardIds).containsExactly("wild-wormhole")
            useAbility("masterplan", "Omni")
            // Wild Wormhole was really played (action cards land in the discard after resolution)
            // and Masterplan was sacrificed.
            assertThat(p1.discard).contains("wild-wormhole", "masterplan")
            assertThat(p1.hand).isEmpty()
        }
    }
}
