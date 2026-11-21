package com.aember.analyzer.engine

import com.aember.analyzer.domain.engine.CardLibrary
import com.aember.analyzer.domain.engine.action.ActionExecutor
import com.aember.analyzer.domain.engine.action.ActionGenerator
import com.aember.analyzer.domain.engine.action.FightUse
import com.aember.analyzer.domain.engine.action.PlayCardAction
import com.aember.analyzer.domain.engine.action.ReapUse
import com.aember.analyzer.domain.engine.action.UseCreatureAction
import com.aember.analyzer.domain.engine.cards.ShadowSelf
import com.aember.analyzer.domain.engine.cards.coreSetCards
import com.aember.analyzer.domain.engine.dsl.DslCardScript
import com.aember.analyzer.domain.engine.dsl.EffectResolver
import com.aember.analyzer.domain.model.CardType
import com.aember.analyzer.domain.model.CatalogCard
import com.aember.analyzer.domain.model.House
import com.aember.analyzer.domain.engine.model.InPlayCard
import com.aember.analyzer.domain.model.Rarity
import com.aember.analyzer.domain.engine.model.Zone
import com.aember.analyzer.engine.EngineTestHelper.IncrementingIdGenerator
import com.aember.analyzer.engine.EngineTestHelper.creature
import com.aember.analyzer.engine.EngineTestHelper.makeGame
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Tier2AndShadowSelfTest {

    private val idGenerator = IncrementingIdGenerator()

    private val dslCards = setOf(
        "smaaash", "tremor", "blinding-light", "hebe-the-huge", "kelifi-dragon",
        "nerve-blast", "relentless-whispers", "yxilo-bolter"
    )

    private fun libraryWithDslAndShadowSelf(): CardLibrary {
        val catalog = (dslCards + setOf("shadow-self")).associateWith { makeCard(it) }
        val library = CardLibrary(catalog)
        val resolver = EffectResolver(library = library)
        coreSetCards.filter { it.first in dslCards }
            .forEach { (id, definition) -> library.register(id, DslCardScript(definition, resolver)) }
        library.register("shadow-self", ShadowSelf)
        return library
    }

    @Test
    fun `Smaaash stuns a creature`() {
        val executor = ActionExecutor(libraryWithDslAndShadowSelf(), idGenerator)
        val target = creature("x", 1, 1, 3)
        val game = makeGame(player1Creatures = listOf(target))
        game.activePlayer().hand.add("smaaash")

        executor.execute(game, PlayCardAction("smaaash"))

        assertThat(target.stun).isTrue()
    }

    @Test
    fun `Tremor stuns a creature and its neighbors`() {
        val executor = ActionExecutor(libraryWithDslAndShadowSelf(), idGenerator)
        val left = creature("x", 1, 1, 3)
        val mid = creature("x", 2, 1, 4)
        val right = creature("x", 3, 1, 3)
        val game = makeGame(player1Creatures = listOf(left, mid, right))
        game.activePlayer().hand.add("tremor")

        executor.execute(game, PlayCardAction("tremor"))

        assertThat(left.stun).isTrue()
        assertThat(mid.stun).isTrue()
        assertThat(right.stun).isTrue()
    }

    @Test
    fun `Hebe the Huge deals 2 damage to each other undamaged creature`() {
        val executor = ActionExecutor(libraryWithDslAndShadowSelf(), idGenerator)
        val hebe = creature("hebe-the-huge", 1, 0, 6)
        val friendly = creature("x", 2, 0, 3)
        val enemy = creature("y", 1, 1, 3)
        val alreadyDamaged = creature("z", 2, 1, 3).apply { damage = 1 }
        val game = makeGame(
            player0Creatures = listOf(hebe, friendly),
            player1Creatures = listOf(enemy, alreadyDamaged)
        )
        game.activePlayer().hand.add("hebe-the-huge")

        executor.execute(game, PlayCardAction("hebe-the-huge"))

        assertThat(hebe.damage).isEqualTo(0)
        assertThat(friendly.damage).isEqualTo(2)
        assertThat(enemy.damage).isEqualTo(2)
        assertThat(alreadyDamaged.damage).isEqualTo(1)
    }

    @Test
    fun `Kelifi Dragon cannot be played with less than 7 amber`() {
        val library = libraryWithDslAndShadowSelf()
        val game = makeGame()
        game.activePlayer().activeHouse = House.BROBNAR
        game.activePlayer().amber = 6
        game.activePlayer().hand.add("kelifi-dragon")

        val actions = ActionGenerator(library).generate(game)

        assertThat(actions.filterIsInstance<PlayCardAction>()).isEmpty()
    }

    @Test
    fun `Kelifi Dragon can be played with 7 or more amber`() {
        val library = libraryWithDslAndShadowSelf()
        val game = makeGame()
        game.activePlayer().activeHouse = House.BROBNAR
        game.activePlayer().amber = 7
        game.activePlayer().hand.add("kelifi-dragon")

        val actions = ActionGenerator(library).generate(game)

        assertThat(actions.filterIsInstance<PlayCardAction>()).isNotEmpty()
    }

    @Test
    fun `Nerve Blast deals damage only when steal succeeds`() {
        val executor = ActionExecutor(libraryWithDslAndShadowSelf(), idGenerator)
        val enemy = creature("target", 1, 1, 3)
        val game = makeGame(player1Creatures = listOf(enemy))
        game.opponent().amber = 1
        game.activePlayer().hand.add("nerve-blast")

        executor.execute(game, PlayCardAction("nerve-blast"))

        assertThat(game.opponent().amber).isEqualTo(0)
        assertThat(game.activePlayer().amber).isEqualTo(1)
        assertThat(enemy.damage).isEqualTo(2)
    }

    @Test
    fun `Nerve Blast does not deal damage when steal fails`() {
        val executor = ActionExecutor(libraryWithDslAndShadowSelf(), idGenerator)
        val enemy = creature("target", 1, 1, 3)
        val game = makeGame(player1Creatures = listOf(enemy))
        game.opponent().amber = 0
        game.activePlayer().hand.add("nerve-blast")

        executor.execute(game, PlayCardAction("nerve-blast"))

        assertThat(enemy.damage).isEqualTo(0)
    }

    @Test
    fun `Relentless Whispers steals only if damage destroys the creature`() {
        val executor = ActionExecutor(libraryWithDslAndShadowSelf(), idGenerator)
        val weakEnemy = creature("target", 1, 1, 2)
        val game = makeGame(player1Creatures = listOf(weakEnemy))
        game.opponent().amber = 2
        game.activePlayer().hand.add("relentless-whispers")

        executor.execute(game, PlayCardAction("relentless-whispers"))

        assertThat(weakEnemy.damage).isEqualTo(2)
        assertThat(game.opponent().amber).isEqualTo(1)
        assertThat(game.activePlayer().amber).isEqualTo(1)
    }

    @Test
    fun `Relentless Whispers does not steal if creature survives`() {
        val executor = ActionExecutor(libraryWithDslAndShadowSelf(), idGenerator)
        val toughEnemy = creature("target", 1, 1, 5)
        val game = makeGame(player1Creatures = listOf(toughEnemy))
        game.opponent().amber = 2
        game.activePlayer().hand.add("relentless-whispers")

        executor.execute(game, PlayCardAction("relentless-whispers"))

        assertThat(toughEnemy.damage).isEqualTo(2)
        assertThat(game.opponent().amber).isEqualTo(2)
        assertThat(game.activePlayer().amber).isEqualTo(0)
    }

    @Test
    fun `Yxilo Bolter purges creature destroyed by its damage`() {
        val executor = ActionExecutor(libraryWithDslAndShadowSelf(), idGenerator)
        val weakEnemy = creature("target", 1, 1, 2)
        val game = makeGame(player1Creatures = listOf(weakEnemy))
        game.activePlayer().creatures.add(creature("yxilo-bolter", 1, 0, 3).apply { exhausted = false })

        executor.execute(game, UseCreatureAction("yxilo-bolter-1", ReapUse))

        // Reap effect deals 2 damage, destroying weakEnemy, then purges it
        assertThat(game.opponent().creatures).isEmpty()
        assertThat(game.opponent().purged).contains("target")
    }

    @Test
    fun `Shadow Self deals no damage when fighting`() {
        val executor = ActionExecutor(libraryWithDslAndShadowSelf(), idGenerator)
        val shadow = shadowSelf("ss-1", 0)
        val enemy = creature("enemy", 1, 1, 3)
        val game = makeGame(player0Creatures = listOf(shadow), player1Creatures = listOf(enemy))

        executor.execute(game, UseCreatureAction("ss-1", FightUse("enemy-1")))

        assertThat(enemy.damage).isEqualTo(0)
    }

    @Test
    fun `Shadow Self redirects damage from non-Specter neighbors`() {
        val executor = ActionExecutor(libraryWithDslAndShadowSelf(), idGenerator)
        val shadow = shadowSelf("ss-1", 0)
        val neighbor = creature("neighbor", 2, 0, 3)
        val game = makeGame(player0Creatures = listOf(shadow, neighbor))
        game.activePlayer().hand.add("nerve-blast")
        game.opponent().amber = 1

        // With only friendly creatures, Nerve Blast targets neighbor; Shadow Self redirects the damage to itself.
        executor.execute(game, PlayCardAction("nerve-blast"))

        assertThat(neighbor.damage).isEqualTo(0)
        assertThat(shadow.damage).isEqualTo(2)
    }

    private fun makeCard(id: String): CatalogCard {
        return CatalogCard(
            id = id,
            name = id,
            number = "000",
            image = "",
            expansion = 341,
            house = House.BROBNAR,
            type = if (id == "shadow-self") CardType.CREATURE else CardType.ACTION,
            rarity = Rarity.COMMON,
            amber = 0,
            armor = null,
            power = if (id == "shadow-self") 8 else null,
            text = "",
            keywords = emptyList(),
            traits = if (id == "shadow-self") listOf("specter") else emptyList()
        )
    }

    private fun shadowSelf(instanceId: String, owner: Int): InPlayCard {
        return InPlayCard(
            instanceId = instanceId,
            cardId = "shadow-self",
            ownerIndex = owner,
            controllerIndex = owner,
            zone = Zone.PLAY_AREA,
            house = House.SHADOWS,
            type = CardType.CREATURE,
            power = 8,
            traits = listOf("specter"),
            exhausted = false
        )
    }
}
