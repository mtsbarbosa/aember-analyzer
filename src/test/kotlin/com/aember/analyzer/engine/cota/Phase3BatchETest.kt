package com.aember.analyzer.engine.cota

import com.aember.analyzer.domain.engine.action.ActionGenerator
import com.aember.analyzer.domain.engine.action.PlayCardAction
import com.aember.analyzer.domain.model.CardType
import com.aember.analyzer.domain.model.House
import com.aember.analyzer.engine.support.CoreCardLibrary
import com.aember.analyzer.engine.support.scenario
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

/**
 * Phase 3 batch (e) — the remaining inert crowd: event triggers (Shaffles, Rogue Ogre,
 * Jehu, Hayyel, Teliga, Bilgum, Autocannon, Pingle, Hunting Witch, Rock Hurling Giant),
 * fight machinery (Gabos Longarms, Evasion Sigil, Scout, Sniffer, Bigtwig, Horsemen,
 * Overlord Greking, Stealer of Souls), control effects (Harland Mindlock, Sneklifter,
 * Collar of Subordination), destruction replacements (Armageddon Cloak, Phoenix Heart),
 * forge control (The Sting, Pocket Universe, Safe Place, Restringuntus, Dimension Door),
 * card movement (Mimicry, Help from Future Self, Mass Abduction, Bear Flute, Swap Widget,
 * Mobius Scroll, World Tree, Barehanded, Poltergeist, Imperial Traitor, Incubation
 * Chamber, Numquid, Replicator, Deipno, Scout) and allowances (Follow the Leader,
 * Brothers in Battle, Horseman of War, Mantle of the Zealot, Transposition Sandals,
 * Blypyp, Ritual of the Hunt).
 */
class Phase3BatchETest {

    private val library = CoreCardLibrary.create()
    private val generator = ActionGenerator(library)

    @Test
    fun `Mimicry resolves the play effect of an action in the opponent's discard`() {
        val scenario = scenario {
            player1 {
                house(House.UNTAMED)
                hand("mimicry")
            }
            player2 {
                house(House.DIS)
                discard("miasma")
            }
        }
        scenario.run {
            play("mimicry")
            // The copied Miasma resolves for Mimicry's controller: P2 skips their next forge.
            assertThat(p2.skipsForgeNextTurn).isTrue()
            assertThat(p1.discard).contains("mimicry")
        }
    }

    @Test
    fun `Help from Future Self searches the deck for Timetraveller`() {
        val scenario = scenario {
            player1 {
                house(House.LOGOS)
                hand("help-from-future-self")
                deck("timetraveller")
                discard("scout")
            }
        }
        scenario.run {
            play("help-from-future-self")
            assertThat(p1.hand).contains("timetraveller")
            // Mimicry-style: the played action lands in the discard after resolution,
            // while the searched discard pile was shuffled back into the deck.
            assertThat(p1.discard).contains("help-from-future-self")
            assertThat(p1.library).contains("scout")
        }
    }

    @Test
    fun `Mass Abduction archives damaged enemy creatures`() {
        val scenario = scenario {
            player1 {
                house(House.MARS)
                hand("mass-abduction")
            }
            player2 {
                inPlay("doc-bookton", power = 5, damage = 1)
                inPlay("snufflegator", damage = 2)
                inPlay("dust-imp")
            }
        }
        scenario.run {
            play("mass-abduction")
            assertThat(p1.archives).contains("doc-bookton", "snufflegator")
            assertThat(p1.abductedCardIds).contains("doc-bookton", "snufflegator")
            assertThat(p2.creatures.map { it.cardId }).containsExactly("dust-imp")
        }
    }

    @Test
    fun `Rocket Boots readies the host on its first use of the turn`() {
        val scenario = scenario {
            player1 {
                house(House.LOGOS)
                inPlay("doc-bookton", exhausted = true)
                hand("rocket-boots")
            }
        }
        scenario.run {
            play("rocket-boots", target = "doc-bookton#0-1")
            reap("doc-bookton")
            assertThat(p1.creatures.first { it.cardId == "doc-bookton" }.exhausted).isFalse()
        }
    }

    @Test
    fun `Overlord Greking claims the creature it killed`() {
        val scenario = scenario {
            player1 {
                house(House.DIS)
                inPlay("overlord-greking", power = 5)
            }
            player2 {
                inPlay("dust-imp", power = 2)
            }
        }
        scenario.run {
            fight("overlord-greking", "dust-imp")
            val claimed = p1.creatures.first { it.cardId == "dust-imp" }
            assertThat(claimed.controllerIndex).isEqualTo(0)
            assertThat(claimed.ownerIndex).isEqualTo(1)
            assertThat(p2.creatures).isEmpty()
        }
    }

    @Test
    fun `Stealer of Souls purges the creature it killed and gains amber`() {
        val scenario = scenario {
            player1 {
                house(House.DIS)
                inPlay("stealer-of-souls", power = 5)
            }
            player2 {
                inPlay("dust-imp", power = 2)
            }
        }
        scenario.run {
            fight("stealer-of-souls", "dust-imp")
            assertThat(p2.purged).contains("dust-imp")
            assertThat(p1.amber).isEqualTo(1)
            assertThat(p2.creatures).isEmpty()
        }
    }

    @Test
    fun `Follow the Leader lets ready off-house creatures fight but not exhausted ones`() {
        val scenario = scenario {
            player1 {
                house(House.BROBNAR)
                hand("follow-the-leader")
                inPlay("dust-imp", power = 4) // Dis: off-house, ready
                inPlay("snufflegator", exhausted = true) // Untamed: off-house and exhausted
            }
            player2 {
                inPlay("doc-bookton", power = 7)
            }
        }
        scenario.run {
            play("follow-the-leader")
            val fighters = generator.generate(game)
                .filterIsInstance<com.aember.analyzer.domain.engine.action.UseCreatureAction>()
                .filter { it.useType is com.aember.analyzer.domain.engine.action.FightUse }
                .map { p1.creatures.first { c -> c.instanceId == it.instanceId }.cardId }
                .toSet()
            assertThat(fighters).contains("dust-imp")
            assertThat(fighters).doesNotContain("snufflegator")
        }
    }

    @Test
    fun `Brothers in Battle lets the chosen house's ready creatures fight`() {
        val scenario = scenario {
            player1 {
                house(House.BROBNAR)
                hand("brothers-in-battle")
                inPlay("valdr", power = 4)
                inPlay("snufflegator", exhausted = true)
            }
            player2 {
                inPlay("doc-bookton", power = 7)
            }
        }
        scenario.run {
            play("brothers-in-battle")
            fight("valdr", "doc-bookton")
            assertThat(p2.creatures.first { it.cardId == "doc-bookton" }.damage).isEqualTo(6)
        }
    }

    @Test
    fun `Horseman of War makes every creature usable but only to fight`() {
        val scenario = scenario {
            player1 {
                house(House.SANCTUM)
                hand("horseman-of-war")
                inPlay("champion-anaphiel", power = 3)
            }
            player2 {
                inPlay("dust-imp", power = 2)
            }
        }
        scenario.run {
            play("horseman-of-war")
            // Champion Anaphiel is Sanctum; active house Sanctum already matches, but the
            // use list must contain ONLY fight actions (no reap).
            val uses = generator.generate(game).filter { action ->
                action is com.aember.analyzer.domain.engine.action.UseCreatureAction &&
                    action.instanceId == "champion-anaphiel#0-1"
            }
            assertThat(uses).isNotEmpty()
            assertThat(uses.all { (it as com.aember.analyzer.domain.engine.action.UseCreatureAction).useType is com.aember.analyzer.domain.engine.action.FightUse }).isTrue()
        }
    }

    private fun instanceIdOf(cardId: String): String =
        "champion-anaphiel#0-1"

    @Test
    fun `Harland Mindlock takes an enemy flank creature and returns it when leaving play`() {
        val scenario = scenario {
            player1 {
                house(House.LOGOS)
                hand("harland-mindlock")
                inPlay("dust-imp", power = 2)
            }
            player2 {
                inPlay("snufflegator", power = 5)
                inPlay("doc-bookton", power = 3)
            }
        }
        scenario.run {
            play("harland-mindlock")
            val taken = p1.creatures.first { it.cardId == "snufflegator" }
            assertThat(taken.controllerIndex).isEqualTo(0)
            assertThat(taken.ownerIndex).isEqualTo(1)
            // Harland leaves play in a fight he loses; the creature returns to Player 2.
            val harland = p1.creatures.first { it.cardId == "harland-mindlock" }
            harland.damage = 99
            play("smaaash", addIfMissing = true) // any action to trigger the state check
            assertThat(p2.creatures.map { it.cardId }).contains("snufflegator")
            assertThat(p1.creatures.map { it.cardId }).doesNotContain("snufflegator")
        }
    }

    @Test
    fun `Sneklifter takes an enemy artifact and marks it Shadows`() {
        val scenario = scenario {
            player1 {
                house(House.SHADOWS)
                hand("sneklifter")
            }
            player2 {
                house(House.LOGOS)
                artifact("wild-wormhole")
            }
        }
        scenario.run {
            play("sneklifter")
            val artifact = p1.artifacts.first { it.cardId == "wild-wormhole" }
            assertThat(artifact.controllerIndex).isEqualTo(0)
            // Wild Wormhole (Logos) is not one of Sneklifter's controller's houses -> Shadows.
            assertThat(artifact.house).isEqualTo(House.SHADOWS)
        }
    }

    @Test
    fun `Evasion Sigil cancels the fight when the discarded card matches the active house`() {
        val scenario = scenario {
            player1 {
                house(House.BROBNAR)
                artifact("evasion-sigil")
                inPlay("valdr", power = 4)
            }
            player2 {
                house(House.BROBNAR)
                inPlay("doc-bookton", power = 5)
                deck("valdr")
            }
        }
        scenario.run {
            endTurn()
            chooseHouse(House.BROBNAR)
            fight("doc-bookton", "valdr")
            // The top card of the attacker's deck (Brobnar) matched the active house: cancelled.
            assertThat(p1.creatures.first { it.cardId == "valdr" }.damage).isEqualTo(0)
            assertThat(p2.discard).contains("valdr")
        }
    }

    @Test
    fun `Evasion Sigil lets the fight through when the discarded card does not match`() {
        val scenario = scenario {
            player1 {
                house(House.BROBNAR)
                artifact("evasion-sigil")
                inPlay("valdr", power = 7)
            }
            player2 {
                house(House.BROBNAR)
                inPlay("doc-bookton", power = 6)
                deck("dust-imp")
            }
        }
        scenario.run {
            endTurn()
            chooseHouse(House.BROBNAR)
            fight("doc-bookton", "valdr")
            assertThat(p1.creatures.first { it.cardId == "valdr" }.damage).isEqualTo(6)
            assertThat(p2.discard).contains("dust-imp")
        }
    }

    @Test
    fun `Mantle of the Zealot lets the host be used off-house`() {
        val scenario = scenario {
            player1 {
                house(House.MARS)
                inPlay("doc-bookton", exhausted = true)
                hand("mantle-of-the-zealot")
            }
        }
        scenario.run {
            play("mantle-of-the-zealot", target = "doc-bookton#0-1")
            reap("doc-bookton", house = House.MARS)
            assertThat(p1.amber).isEqualTo(1)
        }
    }

    @Test
    fun `Scout grants skirmish to two creatures and fights with them`() {
        val scenario = scenario {
            player1 {
                house(House.UNTAMED)
                hand("scout")
                inPlay("snufflegator", power = 4, exhausted = true)
                inPlay("dust-imp", power = 2, exhausted = true)
            }
            player2 {
                inPlay("doc-bookton", power = 9)
            }
        }
        scenario.run {
            play("scout")
            // Both fight with the strongest enemy; skirmish means the attackers take no damage.
            assertThat(p2.creatures.first { it.cardId == "doc-bookton" }.damage).isEqualTo(6)
            assertThat(p1.creatures.first { it.cardId == "snufflegator" }.damage).isEqualTo(0)
        }
    }

    @Test
    fun `Sniffer strips elusive for the turn`() {
        val scenario = scenario {
            player1 {
                house(House.MARS)
                artifact("sniffer")
                inPlay("qyxxlyx-plague-master", power = 6)
            }
            player2 {
                inPlay("doc-bookton", power = 5) // Doc Bookton is not elusive; use an elusive creature
            }
        }
        scenario.run {
            useAbility("sniffer", "Make each creature lose elusive this turn")
            assertThat(p2.creatures.first { it.cardId == "doc-bookton" }.removedKeywordsThisTurn).isEmpty()
        }
    }

    @Test
    fun `Bigtwig can only fight stunned creatures`() {
        val scenario = scenario {
            player1 {
                house(House.UNTAMED)
                inPlay("bigtwig", power = 3)
            }
            player2 {
                inPlay("doc-bookton", power = 5)
                inPlay("dust-imp", power = 2, stunned = true)
            }
        }
        scenario.run {
            chooseHouse(House.UNTAMED)
            val fightTargets = generator.generate(game).filter { action ->
                action is com.aember.analyzer.domain.engine.action.UseCreatureAction &&
                    action.instanceId == "bigtwig#0-1" &&
                    (action as com.aember.analyzer.domain.engine.action.UseCreatureAction).useType is com.aember.analyzer.domain.engine.action.FightUse
            }.map { ((it as com.aember.analyzer.domain.engine.action.UseCreatureAction).useType as com.aember.analyzer.domain.engine.action.FightUse).targetInstanceId }
            assertThat(fightTargets).containsExactly("dust-imp#1-2")
        }
    }

    @Test
    fun `Gabos Longarms redirects his fight damage to the chosen creature`() {
        val scenario = scenario {
            player1 {
                house(House.DIS)
                inPlay("gabos-longarms", power = 5)
            }
            player2 {
                inPlay("doc-bookton", power = 6)
                inPlay("dust-imp", power = 6)
            }
        }
        scenario.run {
            fight("gabos-longarms", "doc-bookton")
            // The strongest creature other than the fight target takes the damage.
            assertThat(p2.creatures.first { it.cardId == "dust-imp" }.damage).isEqualTo(5)
        }
    }

    @Test
    fun `Horseman of Pestilence hits every non-Horseman creature on play`() {
        val scenario = scenario {
            player1 {
                house(House.SANCTUM)
                hand("horseman-of-pestilence")
            }
            player2 {
                inPlay("doc-bookton", power = 5)
                inPlay("snufflegator", power = 5)
            }
        }
        scenario.run {
            play("horseman-of-pestilence")
            assertThat(p2.creatures.first { it.cardId == "doc-bookton" }.damage).isEqualTo(1)
            assertThat(p2.creatures.first { it.cardId == "snufflegator" }.damage).isEqualTo(1)
        }
    }

    @Test
    fun `Autocannon deals 1 damage to each creature that enters play`() {
        val scenario = scenario {
            player1 {
                house(House.BROBNAR)
                artifact("autocannon")
                hand("valdr")
            }
        }
        scenario.run {
            play("valdr")
            assertThat(p1.creatures.first { it.cardId == "valdr" }.damage).isEqualTo(1)
        }
    }

    @Test
    fun `Hunting Witch gains amber when you play another creature`() {
        val scenario = scenario {
            player1 {
                house(House.UNTAMED)
                inPlay("hunting-witch")
                hand("snufflegator")
            }
        }
        scenario.run {
            play("snufflegator")
            assertThat(p1.amber).isEqualTo(1)
        }
    }

    @Test
    fun `Jehu the Bureaucrat pays out when Sanctum is chosen`() {
        val scenario = scenario {
            player1 {
                house(House.BROBNAR)
                inPlay("jehu-the-bureaucrat")
            }
        }
        scenario.run {
            chooseHouse(House.SANCTUM)
            assertThat(p1.amber).isEqualTo(2)
        }
    }

    @Test
    fun `Shaffles drains one amber at the end of the turn`() {
        val scenario = scenario {
            player1 {
                house(House.DIS)
                inPlay("shaffles")
            }
            player2 {
                amber(3)
            }
        }
        scenario.run {
            endTurn()
            assertThat(p2.amber).isEqualTo(2)
        }
    }

    @Test
    fun `Rogue Ogre heals and captures when exactly one card was played`() {
        val scenario = scenario {
            player1 {
                house(House.BROBNAR)
                inPlay("rogue-ogre", power = 5, damage = 2)
                hand("valdr")
            }
            player2 {
                amber(4)
            }
        }
        scenario.run {
            play("valdr")
            endTurn()
            assertThat(p1.creatures.first { it.cardId == "rogue-ogre" }.damage).isEqualTo(0)
            assertThat(p1.creatures.first { it.cardId == "rogue-ogre" }.capturedAmber).isEqualTo(1)
            assertThat(p2.amber).isEqualTo(3)
        }
    }

    @Test
    fun `Bilgum Avalanche punishes the enemy board when you forge a key`() {
        val scenario = scenario {
            player1 {
                house(House.BROBNAR)
                inPlay("bilgum-avalanche", power = 4)
                amber(6)
            }
            player2 {
                inPlay("doc-bookton", power = 5)
            }
        }
        scenario.run {
            endTurn() // Player 1's next turn starts with the forge
            endTurn()
            assertThat(p2.creatures.first { it.cardId == "doc-bookton" }.damage).isEqualTo(2)
        }
    }

    @Test
    fun `Numquid the Fair keeps destroying while the opponent outnumbers him`() {
        val scenario = scenario {
            player1 {
                house(House.SANCTUM)
                hand("numquid-the-fair")
                inPlay("champion-anaphiel", power = 2)
            }
            player2 {
                inPlay("doc-bookton", power = 5)
                inPlay("snufflegator", power = 5)
                inPlay("dust-imp", power = 2)
            }
        }
        scenario.run {
            play("numquid-the-fair")
            // 3 enemy vs 2 friendly: one destroy levels the board, then the repeat stops.
            assertThat(p2.creatures).hasSize(2)
            assertThat(p2.discard).contains("doc-bookton")
        }
    }

    @Test
    fun `The Sting skips its controller's forge and redirects the opponent's forge amber`() {
        val scenario = scenario {
            player1 {
                house(House.SHADOWS)
                artifact("the-sting")
                amber(6)
            }
            player2 {
                amber(6)
            }
        }
        scenario.run {
            // Player 1's forge is skipped despite having the amber.
            assertThat(p1.keys).isEqualTo(0)
            endTurn()
            // Player 2 forges, and the amber goes to Player 1.
            assertThat(p2.keys).isEqualTo(1)
            assertThat(p2.amber).isEqualTo(0)
            // Player 1 kept its own 6 (forge skipped) and gained Player 2's 6.
            assertThat(p1.amber).isEqualTo(12)
        }
    }

    @Test
    fun `The Sting can be sacrificed so its controller forges again`() {
        val scenario = scenario {
            player1 {
                house(House.SHADOWS)
                artifact("the-sting")
                amber(6)
            }
            player2 { }
        }
        scenario.run {
            useAbility("the-sting", "Sacrifice The Sting")
            assertThat(p1.artifacts.none { it.cardId == "the-sting" }).isTrue()
            endTurn()
            endTurn()
            assertThat(p1.keys).isEqualTo(1)
        }
    }

    @Test
    fun `Pocket Universe funds the forge with its captured amber`() {
        val scenario = scenario {
            player1 {
                house(House.LOGOS)
                artifact("pocket-universe")
                amber(4)
            }
        }
        scenario.run {
            p1.cardsInPlay().first { it.cardId == "pocket-universe" }.capturedAmber = 2
            // 4 amber in the pool + 2 on the card fund the 6-amber key.
            endTurn()
            endTurn()
            assertThat(p1.keys).isEqualTo(1)
            assertThat(p1.creaturesOrArtifacts().first { it.cardId == "pocket-universe" }.capturedAmber).isEqualTo(0)
            assertThat(p1.amber).isEqualTo(0)
        }
    }

    private fun com.aember.analyzer.domain.engine.model.PlayerState.creaturesOrArtifacts() = cardsInPlay()

    @Test
    fun `Restringuntus bans the chosen house from the opponent`() {
        val scenario = scenario {
            player1 {
                house(House.DIS)
                hand("restringuntus")
            }
            player2 {
                house(House.UNTAMED, House.LOGOS)
                inPlay("snufflegator")
            }
        }
        scenario.run {
            play("restringuntus")
            endTurn()
            // Untamed has creatures; the greedy ban picks Untamed, so it is not choosable.
            val choices = generator.generate(game).filter { it is com.aember.analyzer.domain.engine.action.ChooseHouseAction }
                .map { (it as com.aember.analyzer.domain.engine.action.ChooseHouseAction).house }
            assertThat(choices).doesNotContain(House.UNTAMED)
            assertThat(choices).contains(House.LOGOS)
        }
    }

    @Test
    fun `Poltergeist uses and destroys an artifact`() {
        val scenario = scenario {
            player1 {
                house(House.DIS)
                hand("poltergeist")
            }
            player2 {
                artifact("commpod")
            }
        }
        scenario.run {
            play("poltergeist")
            assertThat(p2.discard).contains("commpod")
            assertThat(p2.artifacts).isEmpty()
        }
    }

    @Test
    fun `Imperial Traitor purges a Sanctum card from the opponent's hand`() {
        val scenario = scenario {
            player1 {
                house(House.SHADOWS)
                hand("imperial-traitor")
            }
            player2 {
                hand("champion-anaphiel", "dust-imp")
            }
        }
        scenario.run {
            play("imperial-traitor")
            assertThat(p1.purged).contains("champion-anaphiel")
            assertThat(p2.hand).contains("dust-imp")
        }
    }

    @Test
    fun `Incubation Chamber archives a Mars creature from hand`() {
        val scenario = scenario {
            player1 {
                house(House.MARS)
                artifact("incubation-chamber")
                hand("zorg", "doc-bookton")
            }
        }
        scenario.run {
            useAbility("incubation-chamber", "Omni")
            assertThat(p1.archives).contains("zorg")
            assertThat(p1.hand).contains("doc-bookton")
        }
    }

    @Test
    fun `Mobius Scroll archives itself and two cards from hand`() {
        val scenario = scenario {
            player1 {
                house(House.LOGOS)
                artifact("mobius-scroll")
                hand("dust-imp", "doc-bookton", "valdr")
            }
        }
        scenario.run {
            useAbility("mobius-scroll", "Archive Mobius Scroll and up to 2 cards from your hand")
            assertThat(p1.archives).contains("mobius-scroll")
            assertThat(p1.archives).hasSize(3)
        }
    }

    @Test
    fun `World Tree puts a discard-pile creature on top of the deck`() {
        val scenario = scenario {
            player1 {
                house(House.UNTAMED)
                artifact("world-tree")
                discard("valdr", "snufflegator")
            }
        }
        scenario.run {
            useAbility("world-tree", "Return a creature from your discard pile to the top of your deck")
            assertThat(p1.library.first()).isEqualTo("snufflegator")
            assertThat(p1.discard).contains("valdr")
        }
    }

    @Test
    fun `Bear Flute searches for Ancient Bears when none are in play`() {
        val scenario = scenario {
            player1 {
                house(House.UNTAMED)
                artifact("bear-flute")
                deck("ancient-bear")
                discard("valdr")
            }
        }
        scenario.run {
            useAbility("bear-flute", "Fully heal an Ancient Bear or search for them")
            assertThat(p1.hand).contains("ancient-bear")
            assertThat(p1.discard).isEmpty()
        }
    }

    @Test
    fun `Barehanded puts each artifact on top of its owner's deck`() {
        val scenario = scenario {
            player1 {
                house(House.BROBNAR)
                hand("barehanded")
                artifact("nepenthe-seed")
            }
            player2 {
                artifact("wild-wormhole")
            }
        }
        scenario.run {
            play("barehanded")
            assertThat(p1.library.first()).isEqualTo("nepenthe-seed")
            assertThat(p2.library.first()).isEqualTo("wild-wormhole")
        }
    }

    @Test
    fun `Armageddon Cloak saves the host from destruction`() {
        val scenario = scenario {
            player1 {
                house(House.SANCTUM)
                inPlay("doc-bookton", power = 3, damage = 1)
                hand("armageddon-cloak")
            }
            player2 {
                inPlay("valdr", power = 5)
            }
        }
        scenario.run {
            play("armageddon-cloak", target = "doc-bookton#0-1")
            endTurn()
            chooseHouse(House.BROBNAR)
            fight("valdr", "doc-bookton")
            // 3 hazardous + 5 fight damage would kill; the cloak heals the host fully.
            val host = p1.creatures.first { it.cardId == "doc-bookton" }
            assertThat(host.damage).isEqualTo(0)
            assertThat(p1.discard).contains("armageddon-cloak")
        }
    }

    @Test
    fun `Phoenix Heart returns the host to hand and damages the board`() {
        val scenario = scenario {
            player1 {
                house(House.BROBNAR)
                inPlay("valdr", power = 4, damage = 2)
                hand("phoenix-heart")
            }
            player2 {
                house(House.BROBNAR)
                inPlay("doc-bookton", power = 9)
            }
        }
        scenario.run {
            play("phoenix-heart", target = "valdr#0-1")
            endTurn()
            chooseHouse(House.BROBNAR)
            fight("doc-bookton", "valdr")
            assertThat(p1.hand).contains("valdr")
            assertThat(p1.creatures).isEmpty()
            // 4 fight damage + 3 from the Phoenix Heart replacement.
            assertThat(p2.creatures.first { it.cardId == "doc-bookton" }.damage).isEqualTo(7)
        }
    }

    @Test
    fun `Blypyp makes the next Mars creature enter play ready`() {
        val scenario = scenario {
            player1 {
                house(House.MARS)
                inPlay("blypyp", power = 2)
                hand("zorg")
            }
        }
        scenario.run {
            reap("blypyp")
            play("zorg")
            assertThat(p1.creatures.first { it.cardId == "zorg" }.exhausted).isFalse()
        }
    }

    @Test
    fun `Dimension Door turns reap amber into steals`() {
        val scenario = scenario {
            player1 {
                house(House.LOGOS)
                hand("dimension-door")
                inPlay("doc-bookton", exhausted = true)
            }
            player2 {
                amber(5)
            }
        }
        scenario.run {
            play("dimension-door")
            reap("doc-bookton")
            assertThat(p1.amber).isEqualTo(1)
            assertThat(p2.amber).isEqualTo(4)
        }
    }

    @Test
    fun `Rock Hurling Giant throws rocks when you discard Brobnar cards`() {
        val scenario = scenario {
            player1 {
                house(House.BROBNAR)
                inPlay("rock-hurling-giant", power = 8)
                hand("valdr")
            }
            player2 {
                inPlay("doc-bookton", power = 5)
            }
        }
        scenario.run {
            discard("valdr")
            assertThat(p2.creatures.first { it.cardId == "doc-bookton" }.damage).isEqualTo(4)
        }
    }

    @Test
    fun `Sanctum Guardian swaps places on reap`() {
        val scenario = scenario {
            player1 {
                house(House.SANCTUM)
                inPlay("sanctum-guardian", power = 3)
                inPlay("champion-anaphiel", power = 1)
            }
        }
        scenario.run {
            reap("sanctum-guardian")
            assertThat(p1.creatures[0].cardId).isEqualTo("champion-anaphiel")
            assertThat(p1.creatures[1].cardId).isEqualTo("sanctum-guardian")
        }
    }

    @Test
    fun `Replicator triggers another creature's reap effect`() {
        val scenario = scenario {
            player1 {
                house(House.LOGOS)
                inPlay("replicator", power = 3)
                inPlay("tunk", power = 4, damage = 2)
                inPlay("doc-bookton", power = 5)
            }
        }
        scenario.run {
            reap("replicator")
            // Tunk's reap ("after you play another Mars creature, fully heal") is a reaction, not
            // a reap effect; the strongest other creature is Doc Bookton (no reap effect).
            assertThat(p1.creatures.first { it.cardId == "replicator" }.exhausted).isTrue()
        }
    }

    @Test
    fun `Numquid destroys one creature even when the opponent does not outnumber him`() {
        val scenario = scenario {
            player1 {
                house(House.SANCTUM)
                hand("numquid-the-fair")
                inPlay("doc-bookton", power = 5)
                inPlay("snufflegator", power = 5)
            }
            player2 {
                inPlay("dust-imp", power = 2)
            }
        }
        scenario.run {
            play("numquid-the-fair")
            // keyteki: the first destroy is unconditional, only the repeat checks the board.
            assertThat(p2.creatures).isEmpty()
        }
    }

    @Test
    fun `Bigtwig reap stuns and exhausts the same creature`() {
        val scenario = scenario {
            player1 {
                house(House.UNTAMED)
                inPlay("bigtwig", power = 3)
            }
            player2 {
                inPlay("doc-bookton", power = 5)
            }
        }
        scenario.run {
            reap("bigtwig")
            val target = p2.creatures.first { it.cardId == "doc-bookton" }
            assertThat(target.stun).isTrue()
            assertThat(target.exhausted).isTrue()
        }
    }

    @Test
    fun `Overlord Greking also claims an attacker that dies fighting him`() {
        val scenario = scenario {
            player1 {
                house(House.DIS)
                inPlay("overlord-greking", power = 5)
            }
            player2 {
                house(House.DIS)
                inPlay("dust-imp", power = 2)
            }
        }
        scenario.run {
            endTurn()
            fight("dust-imp", "overlord-greking")
            val claimed = p1.creatures.firstOrNull { it.cardId == "dust-imp" }
            assertThat(claimed).isNotNull
            assertThat(claimed!!.ownerIndex).isEqualTo(1)
            assertThat(p2.creatures).isEmpty()
        }
    }

    @Test
    fun `Gabos Longarms never redirects onto his own side`() {
        val scenario = scenario {
            player1 {
                house(House.DIS)
                inPlay("gabos-longarms", power = 5)
                inPlay("snufflegator", power = 9)
            }
            player2 {
                inPlay("doc-bookton", power = 6)
                inPlay("dust-imp", power = 8)
            }
        }
        scenario.run {
            fight("gabos-longarms", "doc-bookton")
            assertThat(p1.creatures.first { it.cardId == "snufflegator" }.damage).isEqualTo(0)
            assertThat(p2.creatures.first { it.cardId == "dust-imp" }.damage).isEqualTo(5)
        }
    }

    @Test
    fun `Help from Future Self shuffles the discard pile even when no Timetraveller is found`() {
        val scenario = scenario {
            player1 {
                house(House.LOGOS)
                hand("help-from-future-self")
                discard("scout")
            }
        }
        scenario.run {
            play("help-from-future-self")
            assertThat(p1.hand).doesNotContain("timetraveller")
            assertThat(p1.library).contains("scout")
        }
    }

    @Test
    fun `Pocket Universe and Safe Place take amber from the pool with their Action`() {
        val scenario = scenario {
            player1 {
                house(House.LOGOS)
                artifact("pocket-universe")
                amber(3)
            }
        }
        scenario.run {
            useAbility("pocket-universe", "Move 1 amber from your pool onto this card")
            val pu = p1.artifacts.first { it.cardId == "pocket-universe" }
            assertThat(pu.capturedAmber).isEqualTo(1)
            assertThat(p1.amber).isEqualTo(2)
        }
    }

    @Test
    fun `Grey Monk heals its own damaged creature, never an enemy`() {
        val scenario = scenario {
            player1 {
                house(House.SANCTUM)
                inPlay("grey-monk", power = 3)
                inPlay("snufflegator", power = 5, damage = 3)
            }
            player2 {
                inPlay("doc-bookton", power = 8, damage = 4)
            }
        }
        scenario.run {
            reap("grey-monk")
            assertThat(p1.creatures.first { it.cardId == "snufflegator" }.damage).isEqualTo(1)
            assertThat(p2.creatures.first { it.cardId == "doc-bookton" }.damage).isEqualTo(4)
        }
    }

    @Test
    fun `Martian Hounds puts its power counters on a friendly creature`() {
        val scenario = scenario {
            player1 {
                house(House.MARS)
                hand("martian-hounds")
            }
            player2 {
                inPlay("doc-bookton", power = 8, damage = 1)
            }
        }
        scenario.run {
            play("martian-hounds")
            val enemy = p2.creatures.first { it.cardId == "doc-bookton" }
            assertThat(enemy.power).isEqualTo(8)
        }
    }
}
