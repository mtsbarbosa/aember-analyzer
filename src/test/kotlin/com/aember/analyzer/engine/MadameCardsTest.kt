package com.aember.analyzer.engine

import com.aember.analyzer.domain.engine.CardLibrary
import com.aember.analyzer.domain.engine.action.ActionExecutor
import com.aember.analyzer.domain.engine.action.FightUse
import com.aember.analyzer.domain.engine.action.PlayCardAction
import com.aember.analyzer.domain.engine.action.UseArtifactAction
import com.aember.analyzer.domain.engine.action.UseCreatureAction
import com.aember.analyzer.domain.engine.cards.Drumble
import com.aember.analyzer.domain.engine.cards.EtherSpider
import com.aember.analyzer.domain.engine.cards.SpeedSigil
import com.aember.analyzer.domain.engine.cards.coreSetCards
import com.aember.analyzer.domain.engine.cards.dslCardOverrides
import com.aember.analyzer.domain.engine.dsl.DslCardScript
import com.aember.analyzer.domain.engine.dsl.EffectResolver
import com.aember.analyzer.domain.engine.dsl.TurnTrigger
import com.aember.analyzer.domain.engine.model.InPlayCard
import com.aember.analyzer.domain.engine.model.Zone
import com.aember.analyzer.domain.model.CardType
import com.aember.analyzer.domain.model.CatalogCard
import com.aember.analyzer.domain.model.House
import com.aember.analyzer.domain.model.Rarity
import com.aember.analyzer.engine.EngineTestHelper.IncrementingIdGenerator
import com.aember.analyzer.engine.EngineTestHelper.creature
import com.aember.analyzer.engine.EngineTestHelper.makeGame
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class MadameCardsTest {

    private val idGenerator = IncrementingIdGenerator()

    private fun libraryWithMadameCards(): CardLibrary {
        val catalog = mapOf(
            "warsong" to makeCard("warsong", "Warsong", 0, "Play: For the remainder of the turn, gain 1 amber each time a friendly creature fights.", CardType.ARTIFACT),
            "speed-sigil" to makeCard("speed-sigil", "Speed Sigil", 0, "The first creature played each turn enters play ready.", CardType.ARTIFACT),
            "gauntlet-of-command" to makeCard("gauntlet-of-command", "Gauntlet of Command", 0, "Action: Ready and fight with a friendly creature.", CardType.ARTIFACT),
            "skeleton-key" to makeCard("skeleton-key", "Skeleton Key", 0, "Action: A friendly creature captures 1 amber.", CardType.ARTIFACT),
            "fighter" to makeCard("fighter", "Fighter", 3, "", CardType.CREATURE),
            "defender" to makeCard("defender", "Defender", 2, "", CardType.CREATURE),
            "punch" to makeCard("punch", "Punch", 0, "", CardType.ACTION)
        )
        val library = CardLibrary(catalog)
        val resolver = EffectResolver(library = library)
        (coreSetCards + dslCardOverrides).filter { it.first in setOf("warsong", "gauntlet-of-command", "skeleton-key") }
            .forEach { (id, definition) -> library.register(id, DslCardScript(definition, resolver)) }
        library.register("drumble", Drumble)
        library.register("ether-spider", EtherSpider)
        library.register("speed-sigil", SpeedSigil)
        return library
    }

    @Test
    fun `Warsong grants 1 amber per friendly fight for the turn`() {
        val library = libraryWithMadameCards()
        val executor = ActionExecutor(library, idGenerator)
        val attacker = creature("fighter", 1, 0, 3).apply { exhausted = false }
        val target = creature("defender", 1, 1, 2)
        val game = makeGame(player0Creatures = listOf(attacker), player1Creatures = listOf(target))
        game.activePlayer().hand.add("warsong")

        executor.execute(game, PlayCardAction("warsong"))
        executor.execute(game, UseCreatureAction("fighter-1", FightUse("defender-1")))

        assertThat(game.activePlayer().amber).isEqualTo(1)
    }

    @Test
    fun `Warsong does not grant amber on fights after the turn ends`() {
        val library = libraryWithMadameCards()
        val executor = ActionExecutor(library, idGenerator)
        val attacker = creature("fighter", 1, 0, 3).apply { exhausted = false }
        val target = creature("defender", 1, 1, 2)
        val game = makeGame(player0Creatures = listOf(attacker), player1Creatures = listOf(target))
        game.activePlayer().hand.add("warsong")

        executor.execute(game, PlayCardAction("warsong"))
        game.activePlayer().turnTriggerAmber[TurnTrigger.FRIENDLY_CREATURE_FIGHTS] = 0 // simulate turn end
        executor.execute(game, UseCreatureAction("fighter-1", FightUse("defender-1")))

        assertThat(game.activePlayer().amber).isEqualTo(0)
    }

    @Test
    fun `Speed Sigil makes first creature each turn enter ready`() {
        val library = libraryWithMadameCards()
        val executor = ActionExecutor(library, idGenerator)
        val game = makeGame()
        game.activePlayer().hand.add("speed-sigil")
        game.activePlayer().hand.add("fighter")

        executor.execute(game, PlayCardAction("speed-sigil"))
        executor.execute(game, PlayCardAction("fighter"))

        val played = game.activePlayer().creatures.find { it.cardId == "fighter" }
        assertThat(played).isNotNull
        assertThat(played!!.exhausted).isFalse()
    }

    @Test
    fun `Speed Sigil readies only the first creature played each turn`() {
        val library = libraryWithMadameCards()
        val executor = ActionExecutor(library, idGenerator)
        val game = makeGame()
        game.activePlayer().hand.add("speed-sigil")
        game.activePlayer().hand.add("fighter")
        game.activePlayer().hand.add("fighter")

        executor.execute(game, PlayCardAction("speed-sigil"))
        executor.execute(game, PlayCardAction("fighter"))
        executor.execute(game, PlayCardAction("fighter", placementIndex = 1))

        val creatures = game.activePlayer().creatures.filter { it.cardId == "fighter" }
        assertThat(creatures).hasSize(2)
        assertThat(creatures[0].exhausted).isFalse()
        assertThat(creatures[1].exhausted).isTrue()
    }

    @Test
    fun `Speed Sigil readies the first creature played by the opposing player`() {
        val library = libraryWithMadameCards()
        val executor = ActionExecutor(library, idGenerator)
        // Player 1 is active; the sigil belongs to player 0.
        val game = makeGame(activePlayerIndex = 1)
        game.players[0].artifacts.add(
            InPlayCard(
                instanceId = "sigil-1", cardId = "speed-sigil", ownerIndex = 0, controllerIndex = 0,
                zone = Zone.PLAY_AREA, house = House.UNTAMED, type = CardType.ARTIFACT
            )
        )
        game.players[1].hand.add("defender")

        executor.execute(game, PlayCardAction("defender"))

        val played = game.players[1].creatures.find { it.cardId == "defender" }
        assertThat(played).isNotNull
        assertThat(played!!.exhausted).isFalse()
    }

    @Test
    fun `Speed Sigil does not ready artifacts`() {
        val library = libraryWithMadameCards()
        val executor = ActionExecutor(library, idGenerator)
        val game = makeGame()
        game.activePlayer().hand.add("speed-sigil")
        game.activePlayer().hand.add("warsong")
        game.activePlayer().hand.add("fighter")

        executor.execute(game, PlayCardAction("speed-sigil"))
        executor.execute(game, PlayCardAction("warsong"))
        // The sigil's slot is not consumed by the artifact; the creature still readies.
        executor.execute(game, PlayCardAction("fighter"))

        val artifact = game.activePlayer().artifacts.find { it.cardId == "warsong" }
        val played = game.activePlayer().creatures.find { it.cardId == "fighter" }
        assertThat(artifact!!.exhausted).isTrue()
        assertThat(played!!.exhausted).isFalse()
    }

    @Test
    fun `creatures without a readying effect enter play exhausted`() {
        val library = libraryWithMadameCards()
        val executor = ActionExecutor(library, idGenerator)
        val game = makeGame()
        game.activePlayer().hand.add("fighter")

        executor.execute(game, PlayCardAction("fighter"))

        val played = game.activePlayer().creatures.find { it.cardId == "fighter" }
        assertThat(played).isNotNull
        assertThat(played!!.exhausted).isTrue()
    }

    @Test
    fun `Gauntlet of Command readies and fights a friendly creature`() {
        val library = libraryWithMadameCards()
        val executor = ActionExecutor(library, idGenerator)
        val friendly = creature("fighter", 1, 0, 3).apply { exhausted = true }
        val enemy = creature("defender", 1, 1, 2)
        val game = makeGame(player0Creatures = listOf(friendly), player1Creatures = listOf(enemy))
        game.activePlayer().artifacts.add(
            InPlayCard(
                instanceId = "gauntlet-1",
                cardId = "gauntlet-of-command",
                ownerIndex = 0,
                controllerIndex = 0,
                zone = Zone.PLAY_AREA,
                house = House.BROBNAR,
                type = CardType.ARTIFACT,
                exhausted = false
            )
        )

        executor.execute(game, UseArtifactAction("gauntlet-1", "Ready and fight with a friendly creature"))

        assertThat(friendly.exhausted).isTrue() // fighting exhausts it
        assertThat(enemy.damage).isEqualTo(3)
        assertThat(friendly.damage).isEqualTo(2)
    }

    @Test
    fun `Skeleton Key captures 1 amber on a friendly creature`() {
        val library = libraryWithMadameCards()
        val executor = ActionExecutor(library, idGenerator)
        val friendly = creature("fighter", 1, 0, 3)
        val game = makeGame(player0Creatures = listOf(friendly))
        game.opponent().amber = 5
        game.activePlayer().artifacts.add(
            InPlayCard(
                instanceId = "key-1",
                cardId = "skeleton-key",
                ownerIndex = 0,
                controllerIndex = 0,
                zone = Zone.PLAY_AREA,
                house = House.SHADOWS,
                type = CardType.ARTIFACT,
                exhausted = false
            )
        )

        executor.execute(game, UseArtifactAction("key-1", "A friendly creature captures 1A"))

        assertThat(game.opponent().amber).isEqualTo(4)
        assertThat(friendly.capturedAmber).isEqualTo(1)
    }

    private fun makeCard(
        id: String,
        name: String,
        power: Int,
        text: String,
        type: CardType
    ): CatalogCard {
        return CatalogCard(
            id = id,
            name = name,
            number = "000",
            image = "",
            expansion = 341,
            house = House.BROBNAR,
            type = type,
            rarity = Rarity.COMMON,
            amber = 0,
            armor = null,
            power = if (type == CardType.CREATURE) power else null,
            text = text,
            keywords = emptyList(),
            traits = emptyList()
        )
    }
}
