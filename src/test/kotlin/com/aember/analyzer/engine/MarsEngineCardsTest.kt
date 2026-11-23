package com.aember.analyzer.engine

import com.aember.analyzer.domain.engine.CardLibrary
import com.aember.analyzer.domain.engine.ForgeRules
import com.aember.analyzer.domain.engine.action.ActionExecutor
import com.aember.analyzer.domain.engine.action.PlayCardAction
import com.aember.analyzer.domain.engine.action.ReapUse
import com.aember.analyzer.domain.engine.action.UseArtifactAction
import com.aember.analyzer.domain.engine.action.UseCreatureAction
import com.aember.analyzer.domain.engine.cards.BiomatrixBackup
import com.aember.analyzer.domain.engine.cards.JammerPack
import com.aember.analyzer.domain.engine.cards.marsEngineDslCardOverrides
import com.aember.analyzer.domain.engine.dsl.CardDefinition
import com.aember.analyzer.domain.engine.dsl.DslCardScript
import com.aember.analyzer.domain.engine.dsl.EffectResolver
import com.aember.analyzer.domain.engine.model.GameState
import com.aember.analyzer.domain.engine.model.InPlayCard
import com.aember.analyzer.domain.engine.model.Zone
import com.aember.analyzer.domain.model.CardType
import com.aember.analyzer.domain.model.CatalogCard
import com.aember.analyzer.domain.model.DeckCard
import com.aember.analyzer.domain.model.House
import com.aember.analyzer.domain.model.Rarity
import com.aember.analyzer.engine.EngineTestHelper.IncrementingIdGenerator
import com.aember.analyzer.engine.EngineTestHelper.creature
import com.aember.analyzer.engine.EngineTestHelper.emptyDeck
import com.aember.analyzer.engine.EngineTestHelper.makeGame
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class MarsEngineCardsTest {

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

    private fun libraryWith(vararg extra: Pair<String, com.aember.analyzer.domain.engine.CardScript>): CardLibrary {
        val catalog = marsEngineDslCardOverrides
            .map { (id, _) -> id to catalogCard(id, House.MARS, CardType.ACTION, power = null) }
            .toMap()
            .plus(extra.map { (id, _) -> id to catalogCard(id, House.MARS, CardType.CREATURE, power = 2) })
        val library = CardLibrary(catalog)
        val resolver = EffectResolver(library = library)
        marsEngineDslCardOverrides.forEach { (id, definition) ->
            library.register(id, DslCardScript(definition, resolver))
        }
        extra.forEach { (id, script) -> library.register(id, script) }
        return library
    }

    private fun deckWith(vararg cardIds: String) = emptyDeck().copy(
        cards = cardIds.map { DeckCard(it, 1, House.MARS, false, false, emptyList(), false) }
    )

    private fun marsCreature(cardId: String, index: Int, owner: Int, power: Int = 3, exhausted: Boolean = false) =
        creature(cardId, index, owner, power, exhausted = exhausted).copy(house = House.MARS)

    @Test
    fun `Key Abduction returns Mars creatures and forges at adjusted cost`() {
        val library = libraryWith()
        val executor = ActionExecutor(library, idGenerator)
        val game = makeGame(
            player0Deck = deckWith("key-abduction", "battle-fleet"),
            player1Deck = deckWith("zyzzix-the-many")
        )
        val player = game.activePlayer()
        val opponent = game.opponent()
        player.creatures.add(marsCreature("mars-own", 0, 0))
        opponent.creatures.add(marsCreature("mars-enemy", 0, 1))
        player.creatures.add(creature("brobnar-own", 1, 0))
        player.amber = 20
        player.hand.add("key-abduction")

        executor.execute(game, PlayCardAction("key-abduction"))

        assertThat(player.hand).contains("mars-own")
        assertThat(opponent.hand).contains("mars-enemy")
        assertThat(opponent.creatures).noneMatch { it.house == House.MARS }
        assertThat(player.creatures.map { it.cardId }).contains("brobnar-own")
        assertThat(player.keys).isEqualTo(1)
        assertThat(player.amber).isEqualTo(6) // 20 - (6 + 9 - 1 hand card after the Mars returns)
    }

    @Test
    fun `Battle Fleet draws one card per Mars card in hand`() {
        val library = libraryWith()
        val executor = ActionExecutor(library, idGenerator)
        val game = makeGame(player0Deck = deckWith("battle-fleet", "zyzzix-the-many", "john-smyth"))
        val player = game.activePlayer()
        player.hand.add("battle-fleet")
        player.hand.add("zyzzix-the-many")
        player.hand.add("john-smyth")
        player.library.addAll(listOf("battle-fleet", "zyzzix-the-many", "john-smyth", "key-abduction"))

        executor.execute(game, PlayCardAction("battle-fleet"))

        assertThat(player.hand.size).isEqualTo(4)
    }

    @Test
    fun `Commpod readies Mars creatures up to Mars cards in hand`() {
        val library = libraryWith()
        val executor = ActionExecutor(library, idGenerator)
        val game = makeGame(player0Deck = deckWith("zyzzix-the-many"))
        val player = game.activePlayer()
        player.artifacts.add(
            InPlayCard(
                instanceId = "commpod-a", cardId = "commpod", ownerIndex = 0, controllerIndex = 0,
                zone = Zone.PLAY_AREA, house = House.MARS, type = CardType.ARTIFACT
            )
        )
        player.hand.add("zyzzix-the-many")
        val tired = marsCreature("mars-1", 0, 0, exhausted = true)
        val tired2 = marsCreature("mars-2", 1, 0, exhausted = true)
        val fresh = marsCreature("mars-3", 2, 0)
        player.creatures.addAll(listOf(tired, tired2, fresh))

        executor.execute(game, UseArtifactAction("commpod-a", "Reveal any number of Mars cards from your hand"))

        assertThat(tired.exhausted).isFalse()
        assertThat(tired2.exhausted).isTrue()
        assertThat(fresh.exhausted).isFalse()
    }

    @Test
    fun `Commpod readies a stunned creature but keeps its stun`() {
        val library = libraryWith()
        val executor = ActionExecutor(library, idGenerator)
        val game = makeGame(player0Deck = deckWith("zyzzix-the-many"))
        val player = game.activePlayer()
        player.artifacts.add(
            InPlayCard(
                instanceId = "commpod-a", cardId = "commpod", ownerIndex = 0, controllerIndex = 0,
                zone = Zone.PLAY_AREA, house = House.MARS, type = CardType.ARTIFACT
            )
        )
        player.hand.add("zyzzix-the-many")
        val stunned = marsCreature("mars-stunned", 0, 0, exhausted = true).apply { stun = true }
        player.creatures.add(stunned)

        executor.execute(game, UseArtifactAction("commpod-a", "Reveal any number of Mars cards from your hand"))

        // Ready clears exhaustion only; stun is removed by using the creature.
        assertThat(stunned.exhausted).isFalse()
        assertThat(stunned.stun).isTrue()
    }

    @Test
    fun `Phosphorus Stars stuns every non-Mars creature and gives 2 chains`() {
        val library = libraryWith()
        val executor = ActionExecutor(library, idGenerator)
        val game = makeGame(player0Deck = deckWith("phosphorus-stars"))
        val player = game.activePlayer()
        val opponent = game.opponent()
        player.creatures.add(creature("brobnar-a", 0, 0))
        opponent.creatures.add(creature("dis-b", 0, 1))
        opponent.creatures.add(marsCreature("mars-b", 1, 1))
        player.hand.add("phosphorus-stars")

        executor.execute(game, PlayCardAction("phosphorus-stars"))

        assertThat(player.creatures.first { it.cardId == "brobnar-a" }.stun).isTrue()
        assertThat(opponent.creatures.first { it.cardId == "dis-b" }.stun).isTrue()
        assertThat(opponent.creatures.first { it.cardId == "mars-b" }.stun).isFalse()
        assertThat(player.chains).isEqualTo(2)
    }

    @Test
    fun `Zyzzix archives from hand and gains power counters on reap`() {
        // Register john-smyth as a creature so Zyzzix can archive it from hand.
        val library = libraryWith("john-smyth" to object : com.aember.analyzer.domain.engine.CardScript {})
        val executor = ActionExecutor(library, idGenerator)
        val game = makeGame(player0Deck = deckWith("zyzzix-the-many"))
        val player = game.activePlayer()
        val zyzzix = marsCreature("zyzzix-the-many", 0, 0)
        player.creatures.add(zyzzix)
        player.hand.add("john-smyth")

        executor.execute(game, UseCreatureAction(zyzzix.instanceId, ReapUse))

        assertThat(player.archives).containsExactly("john-smyth")
        assertThat(zyzzix.power).isEqualTo(6)
        assertThat(player.amber).isEqualTo(1)
    }

    @Test
    fun `Zyzzix archives nothing without a creature in hand`() {
        val library = libraryWith()
        val executor = ActionExecutor(library, idGenerator)
        val game = makeGame(player0Deck = deckWith("zyzzix-the-many"))
        val player = game.activePlayer()
        val zyzzix = marsCreature("zyzzix-the-many", 0, 0)
        player.creatures.add(zyzzix)
        player.hand.add("battle-fleet")

        executor.execute(game, UseCreatureAction(zyzzix.instanceId, ReapUse))

        assertThat(player.archives).isEmpty()
        // No counters gained; power stays at the base value.
        assertThat(zyzzix.power).isEqualTo(3)
        assertThat(player.amber).isEqualTo(1)
    }

    @Test
    fun `John Smyth readies another friendly Mars creature on reap`() {
        val library = libraryWith()
        val executor = ActionExecutor(library, idGenerator)
        val game = makeGame(player0Deck = deckWith("john-smyth"))
        val player = game.activePlayer()
        val john = marsCreature("john-smyth", 0, 0)
        val friend = marsCreature("mars-friend", 1, 0, power = 5, exhausted = true)
        player.creatures.addAll(listOf(john, friend))
        player.hand.add("key-abduction")

        executor.execute(game, UseCreatureAction(john.instanceId, ReapUse))

        assertThat(friend.exhausted).isFalse()
    }

    @Test
    fun `Jammer Pack makes opponent keys cost 2 more`() {
        val library = libraryWith("jammer-pack" to JammerPack)
        val game = makeGame(player0Deck = deckWith("jammer-pack"), player1Deck = deckWith("john-smyth"))
        val player = game.activePlayer()
        val opponent = game.opponent()
        val host = creature("host", 0, 1).copy(house = House.MARS)
        opponent.creatures.add(host)
        host.upgrades.add(
            InPlayCard(
                instanceId = "jammer-pack-u", cardId = "jammer-pack", ownerIndex = 0, controllerIndex = 0,
                zone = Zone.UPGRADE, house = House.MARS, type = CardType.UPGRADE, hostId = host.instanceId
            )
        )

        assertThat(ForgeRules.keyCost(library, game, player)).isEqualTo(8)
        assertThat(ForgeRules.keyCost(library, game, opponent)).isEqualTo(6)
    }

    @Test
    fun `ForgeKey respects the key-cost aura`() {
        val library = libraryWith("jammer-pack" to JammerPack)
        val resolver = EffectResolver(library = library)
        val game = makeGame(player0Deck = deckWith("battle-fleet"), player1Deck = deckWith("john-smyth"))
        val player = game.activePlayer()
        val opponent = game.opponent()
        val host = creature("host", 0, 1).copy(house = House.MARS)
        opponent.creatures.add(host)
        host.upgrades.add(
            InPlayCard(
                instanceId = "jammer-pack-u", cardId = "jammer-pack", ownerIndex = 0, controllerIndex = 0,
                zone = Zone.UPGRADE, house = House.MARS, type = CardType.UPGRADE
            )
        )
        player.amber = 8
        val source = marsCreature("battle-fleet", 0, 0)
        host.upgrades[0].hostId = host.instanceId

        resolver.resolve(game, player, source, com.aember.analyzer.domain.engine.dsl.ForgeKey(extraCost = 0, discountPerCardInHand = false))

        assertThat(player.keys).isEqualTo(1)
        assertThat(player.amber).isEqualTo(0)
    }

    @Test
    fun `Biomatrix Backup sends the attached creature to archives instead of destroy`() {
        val library = libraryWith("biomatrix-backup" to BiomatrixBackup)
        val executor = ActionExecutor(library, idGenerator)
        val game = makeGame(
            player0Deck = deckWith("biomatrix-backup", "battle-fleet"),
            player1Deck = deckWith("john-smyth"),
            activePlayerIndex = 1
        )
        val owner = game.players[0]
        val attacker = game.players[1]
        val host = marsCreature("mars-host", 0, 0)
        owner.creatures.add(host)
        host.upgrades.add(
            InPlayCard(
                instanceId = "biomatrix-u", cardId = "biomatrix-backup", ownerIndex = 0, controllerIndex = 0,
                zone = Zone.UPGRADE, house = House.MARS, type = CardType.UPGRADE
            )
        )
        val destroyer = creature("destroyer", 0, 1).copy(house = House.BROBNAR)
        attacker.creatures.add(destroyer)

        executor.execute(game, UseCreatureAction(destroyer.instanceId, com.aember.analyzer.domain.engine.action.FightUse(host.instanceId)))

        assertThat(owner.creatures).noneMatch { it.cardId == "mars-host" }
        assertThat(owner.archives).contains("mars-host")
        // Attached upgrades fall off to the discard when the host is archived.
        assertThat(owner.discard).contains("biomatrix-backup")
        assertThat(owner.archives).doesNotContain("biomatrix-backup")
        assertThat(owner.discard).doesNotContain("mars-host")
    }

    @Test
    fun `creatures without a readying effect enter play exhausted`() {
        val plainCreature = object : com.aember.analyzer.domain.engine.CardScript {}
        val library = libraryWith("mars-plain" to plainCreature)
        val executor = ActionExecutor(library, idGenerator)
        val game = makeGame(player0Deck = deckWith("mars-plain"))
        game.activePlayer().hand.add("mars-plain")

        executor.execute(game, PlayCardAction("mars-plain"))

        val played = game.activePlayer().creatures.first { it.cardId == "mars-plain" }
        assertThat(played.exhausted).isTrue()
    }
}
