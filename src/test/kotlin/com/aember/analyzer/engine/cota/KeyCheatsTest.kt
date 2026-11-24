package com.aember.analyzer.engine.cota

import com.aember.analyzer.domain.engine.CardLibrary
import com.aember.analyzer.domain.engine.ForgeRules
import com.aember.analyzer.domain.engine.action.ActionExecutor
import com.aember.analyzer.domain.engine.action.PlayCardAction
import com.aember.analyzer.domain.engine.action.UseArtifactAction
import com.aember.analyzer.domain.engine.cards.keyCheatDslCardOverrides
import com.aember.analyzer.domain.engine.dsl.CardDefinition
import com.aember.analyzer.domain.engine.dsl.DslCardScript
import com.aember.analyzer.domain.engine.dsl.EffectResolver
import com.aember.analyzer.domain.engine.model.GameState
import com.aember.analyzer.domain.engine.model.InPlayCard
import com.aember.analyzer.domain.engine.model.Zone
import com.aember.analyzer.domain.model.CardType
import com.aember.analyzer.domain.model.CatalogCard
import com.aember.analyzer.domain.model.House
import com.aember.analyzer.domain.model.Rarity
import com.aember.analyzer.engine.EngineTestHelper
import com.aember.analyzer.engine.EngineTestHelper.IncrementingIdGenerator
import com.aember.analyzer.engine.EngineTestHelper.creature
import com.aember.analyzer.engine.EngineTestHelper.emptyDeck
import com.aember.analyzer.engine.EngineTestHelper.makeGame
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class KeyCheatsTest {

    private val idGenerator = IncrementingIdGenerator()

    private fun catalogCard(cardId: String, house: House, type: CardType, power: Int? = null, traits: List<String> = emptyList()): CatalogCard {
        return CatalogCard(
            id = cardId, name = cardId, number = "000", image = "", expansion = 341,
            house = house, type = type, rarity = Rarity.COMMON, amber = 0,
            armor = null, power = power, text = "", keywords = emptyList(), traits = traits
        )
    }

    private fun libraryWith(): CardLibrary {
        val catalog = mapOf(
            "key-charge" to catalogCard("key-charge", House.UNTAMED, CardType.ACTION),
            "chota-hazri" to catalogCard("chota-hazri", House.UNTAMED, CardType.CREATURE, power = 1),
            "key-of-darkness" to catalogCard("key-of-darkness", House.SHADOWS, CardType.ACTION),
            "epic-quest" to catalogCard("epic-quest", House.SANCTUM, CardType.ARTIFACT),
            "knight-1" to catalogCard("knight-1", House.SANCTUM, CardType.CREATURE, power = 3, traits = listOf("Knight")),
            "non-knight" to catalogCard("non-knight", House.SANCTUM, CardType.CREATURE, power = 3)
        )
        val library = CardLibrary(catalog)
        val resolver = EffectResolver(library = library)
        keyCheatDslCardOverrides.forEach { (id, definition) ->
            library.register(id, DslCardScript(definition, resolver))
        }
        return library
    }

    @Test
    fun `Key Charge forges a key after losing 1 amber`() {
        val library = libraryWith()
        val executor = ActionExecutor(library, idGenerator)
        val game = makeGame(player0Deck = EngineTestHelper.emptyDeck())
        val player = game.activePlayer()
        player.amber = 7
        player.hand.add("key-charge")

        executor.execute(game, PlayCardAction("key-charge"))

        assertThat(player.keys).isEqualTo(1)
        assertThat(player.amber).isEqualTo(0) // 7 - 1 - 6
    }

    @Test
    fun `Key Charge does not forge when the player cannot afford it`() {
        val library = libraryWith()
        val executor = ActionExecutor(library, idGenerator)
        val game = makeGame()
        val player = game.activePlayer()
        player.amber = 3
        player.hand.add("key-charge")

        executor.execute(game, PlayCardAction("key-charge"))

        assertThat(player.keys).isEqualTo(0)
        assertThat(player.amber).isEqualTo(2)
    }

    @Test
    fun `Chota Hazri forges a key after losing 1 amber`() {
        val library = libraryWith()
        val executor = ActionExecutor(library, idGenerator)
        val game = makeGame()
        val player = game.activePlayer()
        player.amber = 8
        player.hand.add("chota-hazri")

        executor.execute(game, PlayCardAction("chota-hazri"))

        assertThat(player.creatures.map { it.cardId }).contains("chota-hazri")
        assertThat(player.keys).isEqualTo(1)
        assertThat(player.amber).isEqualTo(1) // 8 - 1 - 6
    }

    @Test
    fun `Key of Darkness forges at +6 normally`() {
        val library = libraryWith()
        val executor = ActionExecutor(library, idGenerator)
        val game = makeGame()
        val player = game.activePlayer()
        game.opponent().amber = 5
        player.amber = 14
        player.hand.add("key-of-darkness")

        executor.execute(game, PlayCardAction("key-of-darkness"))

        assertThat(player.keys).isEqualTo(1)
        assertThat(player.amber).isEqualTo(2) // 14 - (6 + 6)
    }

    @Test
    fun `Key of Darkness forges at +2 when the opponent has no amber`() {
        val library = libraryWith()
        val executor = ActionExecutor(library, idGenerator)
        val game = makeGame()
        val player = game.activePlayer()
        game.opponent().amber = 0
        player.amber = 9
        player.hand.add("key-of-darkness")

        executor.execute(game, PlayCardAction("key-of-darkness"))

        assertThat(player.keys).isEqualTo(1)
        assertThat(player.amber).isEqualTo(1) // 9 - (6 + 2)
    }

    @Test
    fun `Epic Quest play archives friendly Knights`() {
        val library = libraryWith()
        val executor = ActionExecutor(library, idGenerator)
        val game = makeGame()
        val player = game.activePlayer()
        val knight = creature("knight-1", 0, 0).copy(house = House.SANCTUM, traits = listOf("knight"))
        val other = creature("non-knight", 1, 0).copy(house = House.SANCTUM, traits = listOf("elf"))
        player.creatures.add(knight)
        player.creatures.add(other)
        player.hand.add("epic-quest")

        executor.execute(game, PlayCardAction("epic-quest"))

        assertThat(player.archives).containsExactly("knight-1")
        assertThat(player.creatures).containsExactly(other)
    }

    @Test
    fun `Epic Quest omni forges at no cost after 7 Sanctum plays`() {
        val library = libraryWith()
        val executor = ActionExecutor(library, idGenerator)
        val game = makeGame()
        val player = game.activePlayer()
        player.amber = 5
        val epicQuest = InPlayCard(
            instanceId = "epic-quest-a", cardId = "epic-quest", ownerIndex = 0, controllerIndex = 0,
            zone = Zone.PLAY_AREA, house = House.SANCTUM, type = CardType.ARTIFACT
        )
        player.artifacts.add(epicQuest)
        repeat(7) { player.cardsPlayedThisTurnByHouse[House.SANCTUM] = (player.cardsPlayedThisTurnByHouse[House.SANCTUM] ?: 0) + 1 }

        executor.execute(game, UseArtifactAction("epic-quest-a", "Omni"))

        assertThat(player.keys).isEqualTo(1)
        assertThat(player.amber).isEqualTo(5) // forge at no cost
        assertThat(player.artifacts).doesNotContain(epicQuest)
        assertThat(player.discard).contains("epic-quest")
    }

    @Test
    fun `Epic Quest omni does nothing before 7 Sanctum plays`() {
        val library = libraryWith()
        val executor = ActionExecutor(library, idGenerator)
        val game = makeGame()
        val player = game.activePlayer()
        player.amber = 5
        val epicQuest = InPlayCard(
            instanceId = "epic-quest-a", cardId = "epic-quest", ownerIndex = 0, controllerIndex = 0,
            zone = Zone.PLAY_AREA, house = House.SANCTUM, type = CardType.ARTIFACT
        )
        player.artifacts.add(epicQuest)
        repeat(6) { player.cardsPlayedThisTurnByHouse[House.SANCTUM] = (player.cardsPlayedThisTurnByHouse[House.SANCTUM] ?: 0) + 1 }

        executor.execute(game, UseArtifactAction("epic-quest-a", "Omni"))

        assertThat(player.keys).isEqualTo(0)
        assertThat(player.amber).isEqualTo(5)
        assertThat(player.artifacts).contains(epicQuest)
    }
}
