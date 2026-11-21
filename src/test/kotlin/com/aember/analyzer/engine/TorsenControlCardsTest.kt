package com.aember.analyzer.engine

import com.aember.analyzer.domain.ai.AISettings
import com.aember.analyzer.domain.ai.TacticalAI
import com.aember.analyzer.domain.engine.CardLibrary
import com.aember.analyzer.domain.engine.ForgeRules
import com.aember.analyzer.domain.engine.action.ActionExecutor
import com.aember.analyzer.domain.engine.action.ActionGenerator
import com.aember.analyzer.domain.engine.action.PlayCardAction
import com.aember.analyzer.domain.engine.action.UseArtifactAction
import com.aember.analyzer.domain.engine.cards.Fogbank
import com.aember.analyzer.domain.engine.cards.LashOfBrokenDreams
import com.aember.analyzer.domain.engine.cards.TooMuchToProtect
import com.aember.analyzer.domain.engine.dsl.CardDefinition
import com.aember.analyzer.domain.engine.dsl.DslCardScript
import com.aember.analyzer.domain.engine.dsl.EffectResolver
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

class TorsenControlCardsTest {

    private val idGenerator = IncrementingIdGenerator()

    private fun catalogCard(cardId: String, text: String, type: CardType = CardType.ACTION): CatalogCard {
        return CatalogCard(
            id = cardId, name = cardId, number = "000", image = "", expansion = 341,
            house = House.DIS, type = type, rarity = Rarity.COMMON, amber = 0,
            armor = null, power = null, text = text, keywords = emptyList(), traits = emptyList()
        )
    }

    private fun libraryWith(): CardLibrary {
        val catalog = mapOf(
            "lash-of-broken-dreams" to catalogCard(
                "lash-of-broken-dreams",
                "Action: Keys cost +3<A> during your opponent's next turn.",
                CardType.ARTIFACT
            ),
            "too-much-to-protect" to catalogCard(
                "too-much-to-protect",
                "Play: Steal all but 6 of your opponent's <A>."
            ),
            "fogbank" to catalogCard(
                "fogbank",
                "Play: Your opponent cannot use creatures to fight on their next turn."
            ),
            "fear" to catalogCard(
                "fear",
                "Play: Return an enemy creature to its owner's hand."
            ),
            "screaming-cave" to catalogCard(
                "screaming-cave",
                "Action: Shuffle your hand and discard pile into your deck.",
                CardType.ARTIFACT
            ),
            "plain-artifact" to catalogCard(
                "plain-artifact",
                "Action: Do something.",
                CardType.ARTIFACT
            )
        )
        val library = CardLibrary(catalog)
        val resolver = EffectResolver(library = library)
        library.register("lash-of-broken-dreams", LashOfBrokenDreams)
        library.register("too-much-to-protect", TooMuchToProtect)
        library.register("fogbank", Fogbank)
        library.register(
            "fear",
            DslCardScript(
                CardDefinition("fear").apply {
                    playEffect = com.aember.analyzer.domain.engine.dsl.ReturnToHand(
                        com.aember.analyzer.domain.engine.dsl.EnemyCreature(com.aember.analyzer.domain.engine.dsl.Scope.ONE)
                    )
                },
                resolver
            )
        )
        return library
    }

    @Test
    fun `Lash of Broken Dreams adds 3 to opponent's next forge cost`() {
        val library = libraryWith()
        val executor = ActionExecutor(library, idGenerator)
        val game = makeGame()
        val player = game.activePlayer()
        val opponent = game.opponent()
        player.artifacts.add(
            com.aember.analyzer.domain.engine.model.InPlayCard(
                instanceId = "lash-a", cardId = "lash-of-broken-dreams", ownerIndex = 0, controllerIndex = 0,
                zone = Zone.PLAY_AREA, house = House.DIS, type = CardType.ARTIFACT
            )
        )

        executor.execute(game, UseArtifactAction("lash-a", "Keys cost 3A during your opponents next turn"))

        assertThat(opponent.keyCostNextTurnBonus).isEqualTo(3)
        assertThat(ForgeRules.keyCost(library, game, opponent) + opponent.keyCostNextTurnBonus).isEqualTo(9)
    }

    @Test
    fun `Too Much to Protect steals all but 6`() {
        val library = libraryWith()
        val executor = ActionExecutor(library, idGenerator)
        val game = makeGame()
        val player = game.activePlayer()
        val opponent = game.opponent()
        opponent.amber = 10
        player.hand.add("too-much-to-protect")

        executor.execute(game, PlayCardAction("too-much-to-protect"))

        assertThat(player.amber).isEqualTo(4)
        assertThat(opponent.amber).isEqualTo(6)
    }

    @Test
    fun `Too Much to Protect is unplayable when opponent has 6 or fewer amber`() {
        val library = libraryWith()
        val game = makeGame()
        game.opponent().amber = 6

        assertThat(library.scriptFor("too-much-to-protect").playable(game, game.activePlayer())).isFalse()
    }

    @Test
    fun `Fogbank prevents fights on opponent's next turn`() {
        val library = libraryWith()
        val executor = ActionExecutor(library, idGenerator)
        val game = makeGame()
        val player = game.activePlayer()
        val opponent = game.opponent()
        player.creatures.add(creature("attacker", 0, 0))
        opponent.creatures.add(creature("defender", 0, 1))
        player.hand.add("fogbank")
        player.activeHouse = House.DIS

        executor.execute(game, PlayCardAction("fogbank"))
        assertThat(opponent.cannotFightNextTurn).isTrue()

        val generator = ActionGenerator(library)
        game.activePlayerIndex = 1
        opponent.activeHouse = House.BROBNAR
        val actions = generator.generate(game)
        assertThat(actions.none { it is com.aember.analyzer.domain.engine.action.UseCreatureAction &&
            it.useType is com.aember.analyzer.domain.engine.action.FightUse }).isTrue()
        assertThat(actions.any { it is com.aember.analyzer.domain.engine.action.UseCreatureAction &&
            it.useType is com.aember.analyzer.domain.engine.action.ReapUse }).isTrue()
    }

    @Test
    fun `Fear returns exactly one enemy creature`() {
        val library = libraryWith()
        val executor = ActionExecutor(library, idGenerator)
        val game = makeGame()
        val player = game.activePlayer()
        val opponent = game.opponent()
        opponent.creatures.add(creature("enemy-a", 0, 1, power = 5))
        opponent.creatures.add(creature("enemy-b", 1, 1, power = 2))
        player.hand.add("fear")

        executor.execute(game, PlayCardAction("fear"))

        assertThat(opponent.creatures.map { it.cardId }).containsExactly("enemy-b")
        assertThat(opponent.hand).contains("enemy-a")
    }

    @Test
    fun `hand-wipe artifact scores low with a full hand and high with an empty hand`() {
        val library = libraryWith()
        val ai = TacticalAI(library, AISettings.DEFAULT)
        val game = makeGame()
        val player = game.activePlayer()
        player.artifacts.add(
            com.aember.analyzer.domain.engine.model.InPlayCard(
                instanceId = "cave-a", cardId = "screaming-cave", ownerIndex = 0, controllerIndex = 0,
                zone = Zone.PLAY_AREA, house = House.DIS, type = CardType.ARTIFACT
            )
        )
        player.artifacts.add(
            com.aember.analyzer.domain.engine.model.InPlayCard(
                instanceId = "plain-a", cardId = "plain-artifact", ownerIndex = 0, controllerIndex = 0,
                zone = Zone.PLAY_AREA, house = House.DIS, type = CardType.ARTIFACT
            )
        )
        val wipe = UseArtifactAction("cave-a", "Shuffle your hand and discard pile into your deck")
        val plain = UseArtifactAction("plain-a", "Do something")

        player.hand.addAll(listOf("fear", "fear", "fear", "fear", "fear"))
        val fullHandChoice = ai.choose(game, listOf(wipe, plain))
        assertThat((fullHandChoice as UseArtifactAction).instanceId).isEqualTo("plain-a")

        player.hand.clear()
        val emptyHandChoice = ai.choose(game, listOf(wipe, plain))
        assertThat((emptyHandChoice as UseArtifactAction).instanceId).isEqualTo("cave-a")
    }
}
