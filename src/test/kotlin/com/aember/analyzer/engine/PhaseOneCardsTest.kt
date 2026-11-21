package com.aember.analyzer.engine

import com.aember.analyzer.domain.engine.CardLibrary
import com.aember.analyzer.domain.engine.action.ActionExecutor
import com.aember.analyzer.domain.engine.action.PlayCardAction
import com.aember.analyzer.domain.engine.action.UseArtifactAction
import com.aember.analyzer.domain.engine.cards.Arise
import com.aember.analyzer.domain.engine.cards.ControlTheWeak
import com.aember.analyzer.domain.engine.cards.Miasma
import com.aember.analyzer.domain.engine.cards.Silvertooth
import com.aember.analyzer.domain.engine.cards.TendrilsOfPain
import com.aember.analyzer.domain.engine.cards.YxilxDominator
import com.aember.analyzer.domain.engine.dsl.*
import com.aember.analyzer.domain.engine.model.*
import com.aember.analyzer.domain.model.CardType
import com.aember.analyzer.domain.model.CatalogCard
import com.aember.analyzer.domain.model.House
import com.aember.analyzer.domain.model.Rarity
import com.aember.analyzer.engine.EngineTestHelper.IncrementingIdGenerator
import com.aember.analyzer.engine.EngineTestHelper.creature
import com.aember.analyzer.engine.EngineTestHelper.emptyDeck
import com.aember.analyzer.engine.EngineTestHelper.makeGame
import com.aember.analyzer.domain.model.DeckCard
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PhaseOneCardsTest {

    private val idGenerator = IncrementingIdGenerator()

    private fun catalogCard(
        cardId: String,
        house: House,
        type: CardType,
        power: Int? = null,
        amber: Int = 0
    ): CatalogCard {
        return CatalogCard(
            id = cardId,
            name = cardId,
            number = "000",
            image = "",
            expansion = 341,
            house = house,
            type = type,
            rarity = Rarity.COMMON,
            amber = amber,
            armor = null,
            power = power,
            text = "",
            keywords = emptyList(),
            traits = emptyList()
        )
    }

    @Test
    fun `Control the Weak denies opponent the named house next turn`() {
        val catalog = mapOf(
            "control-the-weak" to catalogCard("control-the-weak", House.DIS, CardType.ACTION),
            "logos-card" to catalogCard("logos-card", House.LOGOS, CardType.CREATURE, power = 2),
            "dis-card" to catalogCard("dis-card", House.DIS, CardType.CREATURE, power = 2)
        )
        val library = CardLibrary(catalog)
        library.register("control-the-weak", ControlTheWeak)
        val executor = ActionExecutor(library, idGenerator)
        val disCreature = InPlayCard(
            instanceId = "dis-card-1",
            cardId = "dis-card",
            ownerIndex = 1,
            controllerIndex = 1,
            zone = Zone.PLAY_AREA,
            house = House.DIS,
            type = CardType.CREATURE,
            power = 2
        )
        val game = makeGame(player1Creatures = listOf(disCreature))
        game.activePlayer().hand.add("control-the-weak")

        executor.execute(game, PlayCardAction("control-the-weak"))

        assertThat(game.opponent().deniedHouseNextTurn).isEqualTo(House.DIS)
    }

    @Test
    fun `Library Access draws a card when playing a Logos card`() {
        val library = CardLibrary(mapOf(
            "library-access" to catalogCard("library-access", House.LOGOS, CardType.ACTION),
            "logos-creature" to catalogCard("logos-creature", House.LOGOS, CardType.CREATURE, power = 2)
        ))
        val resolver = EffectResolver(library = library)
        library.register("library-access", DslCardScript(CardDefinition("library-access").apply {
            play(DrawOnCardPlayThisTurn(House.LOGOS))
            fidelity(com.aember.analyzer.domain.engine.fidelity.FidelityLevel.FULL)
        }, resolver))
        library.register("logos-creature", DslCardScript(CardDefinition("logos-creature").apply {
            fidelity(com.aember.analyzer.domain.engine.fidelity.FidelityLevel.VANILLA)
        }, resolver))
        val executor = ActionExecutor(library, idGenerator)
        val game = makeGame()
        game.activePlayer().hand.add("library-access")
        game.activePlayer().hand.add("logos-creature")
        game.activePlayer().library.addAll(listOf("logos-creature", "logos-creature"))

        executor.execute(game, PlayCardAction("library-access"))
        val handBefore = game.activePlayer().hand.size
        executor.execute(game, PlayCardAction("logos-creature"))

        assertThat(game.activePlayer().hand.size).isEqualTo(handBefore) // played 1, drew 1
    }

    @Test
    fun `Invasion Portal action adds the first Mars creature from top of deck to hand`() {
        val library = CardLibrary(mapOf(
            "invasion-portal" to catalogCard("invasion-portal", House.MARS, CardType.ARTIFACT),
            "mars-creature" to catalogCard("mars-creature", House.MARS, CardType.CREATURE, power = 4),
            "non-mars" to catalogCard("non-mars", House.LOGOS, CardType.CREATURE, power = 2)
        ))
        val resolver = EffectResolver(library = library)
        library.register("invasion-portal", DslCardScript(CardDefinition("invasion-portal").apply {
            action("Discard cards from the top of your deck until you discard a Mars creature", AddTopCardOfHouseToHand(House.MARS, SelfPlayer))
            fidelity(com.aember.analyzer.domain.engine.fidelity.FidelityLevel.FULL)
        }, resolver))
        val executor = ActionExecutor(library, idGenerator)
        val game = makeGame(player0Artifacts = listOf(EngineTestHelper.artifact("invasion-portal")))
        val player = game.activePlayer()
        player.library.addAll(listOf("non-mars", "non-mars", "mars-creature"))
        player.artifacts.first().exhausted = false

        executor.execute(game, UseArtifactAction(player.artifacts.first().instanceId, "Discard cards from the top of your deck until you discard a Mars creature"))

        assertThat(player.hand).contains("mars-creature")
        assertThat(player.discard).containsExactly("non-mars", "non-mars")
    }

    @Test
    fun `Hypnotic Command takes control of an enemy creature until end of turn`() {
        val library = CardLibrary(mapOf(
            "hypnotic-command" to catalogCard("hypnotic-command", House.MARS, CardType.ACTION)
        ))
        val resolver = EffectResolver(library = library)
        library.register("hypnotic-command", DslCardScript(CardDefinition("hypnotic-command").apply {
            play(TakeControlUntilEndOfTurn(EnemyCreature()))
            fidelity(com.aember.analyzer.domain.engine.fidelity.FidelityLevel.FULL)
        }, resolver))
        val executor = ActionExecutor(library, idGenerator)
        val enemy = creature("enemy", 1, owner = 1, power = 3)
        val game = makeGame(player1Creatures = listOf(enemy))
        game.activePlayer().hand.add("hypnotic-command")

        executor.execute(game, PlayCardAction("hypnotic-command"))

        assertThat(game.activePlayer().creatures).anyMatch { it.cardId == "enemy" }
        assertThat(game.opponent().creatures).isEmpty()
        assertThat(game.temporaryControllers).containsKey(enemy.instanceId)
    }

    @Test
    fun `Neutron Shark destroys a creature and discards top deck`() {
        val library = CardLibrary(mapOf(
            "neutron-shark" to catalogCard("neutron-shark", House.LOGOS, CardType.CREATURE, power = 3)
        ))
        val resolver = EffectResolver(library = library)
        val effect = Sequence(listOf(Destroy(AnyCreature()), Conditional(DamageDestroyedTarget, DiscardTopDeck(SelfPlayer))))
        library.register("neutron-shark", DslCardScript(CardDefinition("neutron-shark").apply {
            play(effect)
            fidelity(com.aember.analyzer.domain.engine.fidelity.FidelityLevel.FULL)
        }, resolver))
        val executor = ActionExecutor(library, idGenerator)
        val game = makeGame(player1Creatures = listOf(creature("victim", 1, owner = 1, power = 1)))
        game.activePlayer().hand.add("neutron-shark")
        game.activePlayer().library.addAll(listOf("top-card", "other"))

        executor.execute(game, PlayCardAction("neutron-shark"))

        assertThat(game.opponent().creatures).isEmpty()
        assertThat(game.activePlayer().discard).contains("top-card")
    }

    @Test
    fun `High value creature is placed adjacent to a protector`() {
        val catalog = mapOf(
            "shadow-self" to catalogCard("shadow-self", House.SHADOWS, CardType.CREATURE, power = 5),
            "big-threat" to catalogCard("big-threat", House.SHADOWS, CardType.CREATURE, power = 5)
        )
        val library = CardLibrary(catalog)
        library.register("shadow-self", com.aember.analyzer.domain.engine.cards.ShadowSelf)
        val executor = ActionExecutor(library, idGenerator)
        val shadowSelf = InPlayCard(
            instanceId = "shadow-self-1",
            cardId = "shadow-self",
            ownerIndex = 0,
            controllerIndex = 0,
            zone = Zone.PLAY_AREA,
            house = House.SHADOWS,
            type = CardType.CREATURE,
            power = 5
        )
        val game = makeGame(player0Creatures = listOf(shadowSelf))
        game.activePlayer().hand.add("big-threat")

        executor.execute(game, PlayCardAction("big-threat"))

        val creatures = game.activePlayer().creatures
        assertThat(creatures).hasSize(2)
        val bigThreatIdx = creatures.indexOfFirst { it.cardId == "big-threat" }
        val shadowSelfIdx = creatures.indexOfFirst { it.cardId == "shadow-self" }
        assertThat(kotlin.math.abs(bigThreatIdx - shadowSelfIdx)).isEqualTo(1)
    }

    @Test
    fun `Dr Escotera gains amber equal to opponent forged keys`() {
        val library = CardLibrary(mapOf(
            "dr-escotera" to catalogCard("dr-escotera", House.LOGOS, CardType.CREATURE, power = 3)
        ))
        val resolver = EffectResolver(library = library)
        library.register("dr-escotera", DslCardScript(CardDefinition("dr-escotera").apply {
            play(GainAmberEqualToOpponentKeys(SelfPlayer))
            fidelity(com.aember.analyzer.domain.engine.fidelity.FidelityLevel.FULL)
        }, resolver))
        val executor = ActionExecutor(library, idGenerator)
        val game = makeGame()
        game.activePlayer().hand.add("dr-escotera")
        game.opponent().keys = 2

        executor.execute(game, PlayCardAction("dr-escotera"))

        assertThat(game.activePlayer().amber).isEqualTo(2)
    }

    @Test
    fun `Smiling Ruth takes control of enemy flank creature after forging a key`() {
        val library = CardLibrary(mapOf(
            "smiling-ruth" to catalogCard("smiling-ruth", House.SHADOWS, CardType.CREATURE, power = 2)
        ))
        val resolver = EffectResolver(library = library)
        library.register("smiling-ruth", DslCardScript(CardDefinition("smiling-ruth").apply {
            reap(TakeControlOfEnemyFlankCreature(SelfPlayer))
            fidelity(com.aember.analyzer.domain.engine.fidelity.FidelityLevel.FULL)
        }, resolver))
        val executor = ActionExecutor(library, idGenerator)
        val enemyFlank = InPlayCard(
            instanceId = "enemy-flank-1",
            cardId = "enemy-flank",
            ownerIndex = 1,
            controllerIndex = 1,
            zone = Zone.PLAY_AREA,
            house = House.BROBNAR,
            type = CardType.CREATURE,
            power = 5
        )
        val game = makeGame(player1Creatures = listOf(enemyFlank))
        val smilingRuth = InPlayCard(
            instanceId = "smiling-ruth-1",
            cardId = "smiling-ruth",
            ownerIndex = 0,
            controllerIndex = 0,
            zone = Zone.PLAY_AREA,
            house = House.SHADOWS,
            type = CardType.CREATURE,
            power = 2
        )
        game.activePlayer().creatures.add(smilingRuth)
        smilingRuth.exhausted = false
        game.activePlayer().forgedKeyThisTurn = true

        executor.execute(game, com.aember.analyzer.domain.engine.action.UseCreatureAction(smilingRuth.instanceId, com.aember.analyzer.domain.engine.action.ReapUse))

        assertThat(game.activePlayer().creatures.any { it.instanceId == "enemy-flank-1" }).isTrue()
        assertThat(game.temporaryControllers).containsKey("enemy-flank-1")
    }

    @Test
    fun `Bouncing Deathquark destroys two creatures`() {
        val library = CardLibrary(mapOf(
            "bouncing-deathquark" to catalogCard("bouncing-deathquark", House.LOGOS, CardType.ACTION)
        ))
        val resolver = EffectResolver(library = library)
        library.register("bouncing-deathquark", DslCardScript(CardDefinition("bouncing-deathquark").apply {
            play(Sequence(listOf(Destroy(EnemyCreature()), Destroy(FriendlyCreature()))))
            fidelity(com.aember.analyzer.domain.engine.fidelity.FidelityLevel.FULL)
        }, resolver))
        val executor = ActionExecutor(library, idGenerator)
        val game = makeGame(
            player0Creatures = listOf(creature("friendly", 1, owner = 0, power = 1)),
            player1Creatures = listOf(creature("enemy", 1, owner = 1, power = 1))
        )
        game.activePlayer().hand.add("bouncing-deathquark")

        executor.execute(game, PlayCardAction("bouncing-deathquark"))

        assertThat(game.activePlayer().creatures).isEmpty()
        assertThat(game.opponent().creatures).isEmpty()
    }

    @Test
    fun `Miasma prevents opponent from forging a key next turn`() {
        val library = CardLibrary(mapOf(
            "miasma" to catalogCard("miasma", House.DIS, CardType.ACTION)
        ))
        library.register("miasma", Miasma)
        val executor = ActionExecutor(library, idGenerator)
        val game = makeGame()
        game.activePlayer().hand.add("miasma")

        executor.execute(game, PlayCardAction("miasma"))

        assertThat(game.opponent().skipsForgeNextTurn).isTrue()
    }

    @Test
    fun `Tendrils of Pain deals base damage to all creatures`() {
        val library = CardLibrary(mapOf(
            "tendrils-of-pain" to catalogCard("tendrils-of-pain", House.DIS, CardType.ACTION)
        ))
        library.register("tendrils-of-pain", TendrilsOfPain)
        val executor = ActionExecutor(library, idGenerator)
        val game = makeGame(
            player0Creatures = listOf(creature("friendly", 1, owner = 0, power = 3)),
            player1Creatures = listOf(creature("enemy", 1, owner = 1, power = 3))
        )
        game.activePlayer().hand.add("tendrils-of-pain")

        executor.execute(game, PlayCardAction("tendrils-of-pain"))

        assertThat(game.activePlayer().creatures[0].damage).isEqualTo(1)
        assertThat(game.opponent().creatures[0].damage).isEqualTo(1)
    }

    @Test
    fun `Tendrils of Pain deals extra damage when opponent forged a key last turn`() {
        val library = CardLibrary(mapOf(
            "tendrils-of-pain" to catalogCard("tendrils-of-pain", House.DIS, CardType.ACTION)
        ))
        library.register("tendrils-of-pain", TendrilsOfPain)
        val executor = ActionExecutor(library, idGenerator)
        val game = makeGame(
            player0Creatures = listOf(creature("friendly", 1, owner = 0, power = 6)),
            player1Creatures = listOf(creature("enemy", 1, owner = 1, power = 6))
        )
        game.opponent().forgedKeyLastTurn = true
        game.activePlayer().hand.add("tendrils-of-pain")

        executor.execute(game, PlayCardAction("tendrils-of-pain"))

        assertThat(game.activePlayer().creatures[0].damage).isEqualTo(4)
        assertThat(game.opponent().creatures[0].damage).isEqualTo(4)
    }

    @Test
    fun `Arise returns creatures of the most common house in discard to hand and gives a chain`() {
        val library = CardLibrary(mapOf(
            "arise" to catalogCard("arise", House.DIS, CardType.ACTION)
        ))
        library.register("arise", Arise)
        val executor = ActionExecutor(library, idGenerator)
        val deck = emptyDeck().copy(
            cards = listOf(
                DeckCard("dis-creature-1", 1, House.DIS, false, false, emptyList(), false),
                DeckCard("dis-creature-2", 1, House.DIS, false, false, emptyList(), false),
                DeckCard("logos-creature-1", 1, House.LOGOS, false, false, emptyList(), false)
            )
        )
        val game = makeGame(player0Deck = deck)
        game.activePlayer().discard.addAll(listOf("dis-creature-1", "dis-creature-2", "logos-creature-1"))
        game.activePlayer().hand.add("arise")

        executor.execute(game, PlayCardAction("arise"))

        assertThat(game.activePlayer().hand).contains("dis-creature-1", "dis-creature-2")
        assertThat(game.activePlayer().hand).doesNotContain("logos-creature-1")
        assertThat(game.activePlayer().discard).contains("logos-creature-1")
        assertThat(game.activePlayer().discard).doesNotContain("dis-creature-1", "dis-creature-2")
        assertThat(game.activePlayer().chains).isEqualTo(1)
    }

    @Test
    fun `Silvertooth enters play ready`() {
        val library = CardLibrary(mapOf(
            "silvertooth" to catalogCard("silvertooth", House.SHADOWS, CardType.CREATURE, power = 2)
        ))
        library.register("silvertooth", Silvertooth)
        val executor = ActionExecutor(library, idGenerator)
        val game = makeGame()
        game.activePlayer().hand.add("silvertooth")

        executor.execute(game, PlayCardAction("silvertooth"))

        val played = game.activePlayer().creatures.find { it.cardId == "silvertooth" }
        assertThat(played).isNotNull
        assertThat(played!!.exhausted).isFalse()
    }

    @Test
    fun `Yxilx Dominator enters play stunned`() {
        val library = CardLibrary(mapOf(
            "yxilx-dominator" to catalogCard("yxilx-dominator", House.MARS, CardType.CREATURE, power = 5)
        ))
        library.register("yxilx-dominator", YxilxDominator)
        val executor = ActionExecutor(library, idGenerator)
        val game = makeGame()
        game.activePlayer().hand.add("yxilx-dominator")

        executor.execute(game, PlayCardAction("yxilx-dominator"))

        val played = game.activePlayer().creatures.find { it.cardId == "yxilx-dominator" }
        assertThat(played).isNotNull
        assertThat(played!!.stun).isTrue()
    }
}
