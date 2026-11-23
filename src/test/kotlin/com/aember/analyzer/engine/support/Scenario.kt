package com.aember.analyzer.engine.support

import com.aember.analyzer.domain.engine.CardLibrary
import com.aember.analyzer.domain.engine.SeededIdGenerator
import com.aember.analyzer.domain.engine.TurnPhases
import com.aember.analyzer.domain.engine.action.ActionExecutor
import com.aember.analyzer.domain.engine.action.ChooseHouseAction
import com.aember.analyzer.domain.engine.action.DiscardAction
import com.aember.analyzer.domain.engine.action.EndTurnAction
import com.aember.analyzer.domain.engine.action.FightUse
import com.aember.analyzer.domain.engine.action.PlayCardAction
import com.aember.analyzer.domain.engine.action.ReapUse
import com.aember.analyzer.domain.engine.action.RemoveStunUse
import com.aember.analyzer.domain.engine.action.UseArtifactAction
import com.aember.analyzer.domain.engine.action.UseCreatureAction
import com.aember.analyzer.domain.engine.action.ActionAbilityUse
import com.aember.analyzer.domain.engine.model.EndCause
import com.aember.analyzer.domain.engine.model.GameState
import com.aember.analyzer.domain.engine.model.InPlayCard
import com.aember.analyzer.domain.engine.model.PlayerState
import com.aember.analyzer.domain.engine.model.Zone
import com.aember.analyzer.domain.model.CardType
import com.aember.analyzer.domain.model.Deck
import com.aember.analyzer.domain.model.House
import java.util.Random

/**
 * Scenario test DSL mirroring keyteki's `setupTest`, built on the real engine.
 *
 * ```kotlin
 * val scenario = scenario {
 *     player1 {
 *         house(House.BROBNAR)
 *         inPlay("tunk", power = 4)
 *         hand("punch")
 *         amber(2)
 *     }
 *     player2 {
 *         house(House.UNTAMED)
 *         inPlay("umbra", power = 2)
 *         amber(4)
 *     }
 * }
 * scenario.run {
 *     play("punch", target = "umbra")
 * }
 * assertThat(scenario.p2.creatures.first().damage).isEqualTo(3)
 * ```
 *
 * The scenario starts the first player's turn (per-turn flag reset + key phase), so amber
 * at or above the key cost forges immediately — set starting amber below the cost unless
 * you are testing the forge itself. `play`/`reap`/`fight`/`useAbility` auto-choose the
 * card's house; pass `house = ...` to override.
 */
fun scenario(block: ScenarioSpec.() -> Unit): Scenario {
    val spec = ScenarioSpec().apply(block)
    return spec.build()
}

class ScenarioSpec {

    internal val specs = listOf(PlayerSpec(0), PlayerSpec(1))
    internal var firstPlayer: Int = 0
    internal var targetChooser: ScriptedTargetChooser = ScriptedTargetChooser()

    fun player1(block: PlayerSpec.() -> Unit) = specs[0].block()
    fun player2(block: PlayerSpec.() -> Unit) = specs[1].block()

    /** First player goes first. Defaults to player 1. */
    fun firstPlayer(index: Int) {
        firstPlayer = index
    }

    /** Pre-script a target choice for an effect prompt (see [ScriptedTargetChooser]). */
    fun chooseTargets(configure: ScriptedTargetChooser.() -> Unit) {
        targetChooser.configure()
    }

    internal fun build(): Scenario {
        val library = CoreCardLibrary.create(targetChooser)
        val players = specs.mapIndexed { index, spec ->
            val houses = spec.houses.toList().ifEmpty {
                (spec.hand + spec.deck + spec.discard + spec.archives + spec.inPlay.map { it.cardId })
                    .mapNotNull { library.cardData(it)?.house }
                    .distinct()
            }
            PlayerState(
                index = index,
                name = "Player ${index + 1}",
                deck = Deck(
                    uuid = "scenario-deck-$index",
                    name = "Scenario Deck ${index + 1}",
                    expansion = 341,
                    identity = "scenario",
                    houses = houses,
                    cards = emptyList()
                ),
                library = spec.deck.toMutableList(),
                hand = spec.hand.toMutableList(),
                discard = spec.discard.toMutableList(),
                archives = spec.archives.toMutableList(),
                purged = spec.purged.toMutableList(),
                amber = spec.amber,
                keys = spec.keys,
                chains = spec.chains,
                creatures = spec.inPlay.filter { it.type == CardType.CREATURE }
                    .mapIndexed { n, creatureSpec -> creatureSpec.toInPlayCard(index, n, library) }.toMutableList(),
                artifacts = spec.inPlay.filter { it.type == CardType.ARTIFACT }
                    .mapIndexed { n, artifactSpec -> artifactSpec.toInPlayCard(index, n, library) }.toMutableList()
            )
        }
        val game = GameState(
            players = players,
            activePlayerIndex = firstPlayer,
            firstPlayerIndex = firstPlayer
        )
        game.rng = Random(0)
        game.amberRedirector = { player, amount ->
            if (amount <= 0) null
            else game.players
                .filter { it.index != player.index }
                .flatMap { it.creatures }
                .find { it.controllerIndex != player.index && library.scriptFor(it.cardId).capturesOpponentAmberGains() }
        }
        // The scenario starts at the beginning of the first player's turn.
        TurnPhases.startTurn(game, library)
        game.actionsThisPhase = 0
        game.endPhaseRightNow = false
        val executor = ActionExecutor(library, SeededIdGenerator(1L))
        return Scenario(game, library, executor)
    }
}

class PlayerSpec internal constructor(val index: Int) {

    internal val houses = mutableListOf<House>()
    internal val hand = mutableListOf<String>()
    internal val deck = mutableListOf<String>()
    internal val discard = mutableListOf<String>()
    internal val archives = mutableListOf<String>()
    internal val purged = mutableListOf<String>()
    internal val inPlay = mutableListOf<CardSpec>()
    internal var amber = 0
    internal var keys = 0
    internal var chains = 0

    /** Houses this player may choose (defaults to the houses of every card mentioned). */
    fun house(vararg house: House) {
        houses.addAll(house)
    }

    fun inPlay(cardId: String, power: Int? = null, armor: Int? = null, exhausted: Boolean = false, stunned: Boolean = false, damage: Int = 0, capturedAmber: Int = 0) {
        inPlay.add(CardSpec(cardId, CardType.CREATURE, power, armor, exhausted, stunned, damage, capturedAmber))
    }

    fun artifact(cardId: String, exhausted: Boolean = false) {
        inPlay.add(CardSpec(cardId, CardType.ARTIFACT, null, null, exhausted, false, 0, 0))
    }

    fun hand(vararg cardId: String) {
        hand.addAll(cardId)
    }

    /** Library in draw order: the first argument is the top card. */
    fun deck(vararg cardId: String) {
        deck.addAll(cardId)
    }

    fun discard(vararg cardId: String) {
        discard.addAll(cardId)
    }

    fun archives(vararg cardId: String) {
        archives.addAll(cardId)
    }

    fun purged(vararg cardId: String) {
        purged.addAll(cardId)
    }

    fun amber(amount: Int) {
        amber = amount
    }

    fun keys(amount: Int) {
        keys = amount
    }

    fun chains(amount: Int) {
        chains = amount
    }
}

internal class CardSpec(
    val cardId: String,
    val type: CardType,
    val power: Int?,
    val armor: Int?,
    val exhausted: Boolean,
    val stunned: Boolean,
    val damage: Int,
    val capturedAmber: Int
) {
    fun toInPlayCard(ownerIndex: Int, ordinal: Int, library: CardLibrary): InPlayCard {
        val catalog = library.cardData(cardId)
            ?: error("Card '$cardId' is not in the Core catalog; check the id (engine ids are sanitized keyteki ids)")
        return InPlayCard(
            instanceId = "$cardId#$ownerIndex-${ordinal + 1}",
            cardId = cardId,
            ownerIndex = ownerIndex,
            controllerIndex = ownerIndex,
            zone = Zone.PLAY_AREA,
            house = catalog.house,
            type = catalog.type,
            exhausted = exhausted,
            power = power ?: catalog.power ?: 0,
            armor = armor ?: catalog.armor ?: 0,
            damage = damage,
            capturedAmber = capturedAmber,
            stun = stunned,
            keywords = catalog.keywords.associate { entry ->
                val parts = entry.split(":")
                parts[0].lowercase() to (parts.getOrNull(1)?.toIntOrNull() ?: 1)
            },
            traits = catalog.traits.map { it.lowercase() }
        )
    }
}

class Scenario internal constructor(
    val game: GameState,
    private val library: CardLibrary,
    private val executor: ActionExecutor
) {

    val p1: PlayerState get() = game.players[0]
    val p2: PlayerState get() = game.players[1]

    /** keyteki-style per-player views used by ported parity skeletons (`player1.play(...)`). */
    val player1: PlayerView get() = PlayerView(p1)
    val player2: PlayerView get() = PlayerView(p2)

    val active: PlayerState get() = game.activePlayer()

    fun player(index: Int): PlayerState = game.players[index]

    /** Explicitly enter the house phase with [house]. */
    fun chooseHouse(house: House) {
        executor.execute(game, ChooseHouseAction(house))
    }

    /** Remove a stun without spending an action is not supported; this spends the action like the engine does. */
    fun unstun(cardId: String) {
        chooseHouseIfNecessary(cardId)
        executor.execute(game, UseCreatureAction(instanceOf(cardId), RemoveStunUse))
    }

    /**
     * Plays a card from the active player's hand. Fails if the card is not in hand
     * (keyteki specs set hands up front); pass `addIfMissing = true` to escape hatch
     * for cards gained mid-test.
     */
    fun play(cardId: String, target: String? = null, flank: Int? = null, house: House? = null, addIfMissing: Boolean = false) {
        val player = game.activePlayer()
        require(cardId in player.hand || addIfMissing) {
            "Card $cardId is not in ${player.name}'s hand — fix the scenario setup instead of relying on auto-add"
        }
        chooseHouseIfNecessary(cardId, house)
        if (cardId !in player.hand) player.hand.add(cardId)
        val catalog = library.cardData(cardId) ?: error("Unknown card $cardId")
        if (catalog.type == CardType.UPGRADE) {
            requireNotNull(target) { "Upgrades need a target creature: play(\"$cardId\", target = ...)" }
            executor.execute(game, PlayCardAction(cardId, targetCreatureId = target))
            return
        }
        val placement = when {
            flank != null -> flank
            catalog.type != CardType.CREATURE || player.creatures.isEmpty() -> 0
            else -> player.creatures.size // right flank, like a typical keyteki "on the flank" play
        }
        executor.execute(game, PlayCardAction(cardId, targetCreatureId = target, placementIndex = placement))
    }

    fun reap(cardId: String, house: House? = null) {
        chooseHouseIfNecessary(cardId, house)
        executor.execute(game, UseCreatureAction(instanceOf(cardId), ReapUse))
    }

    fun fight(attackerCardId: String, targetCardId: String, house: House? = null) {
        chooseHouseIfNecessary(attackerCardId, house)
        val attacker = creatureOf(attackerCardId) ?: error("No creature '$attackerCardId' in play for the active player")
        val target = game.opponent().creatures.firstOrNull { it.cardId == targetCardId }
            ?: error("Opponent has no creature '$targetCardId'")
        executor.execute(game, UseCreatureAction(attacker.instanceId, FightUse(target.instanceId)))
    }

    /** Use a named action ability of a creature or artifact, e.g. the card's Action or Omni text. */
    fun useAbility(cardId: String, abilityId: String, house: House? = null) {
        chooseHouseIfNecessary(cardId, house)
        val card = game.activePlayer().cardsInPlay().firstOrNull { it.cardId == cardId }
            ?: error("No card '$cardId' in play for the active player")
        if (card.type == CardType.CREATURE) {
            executor.execute(game, UseCreatureAction(card.instanceId, ActionAbilityUse(abilityId)))
        } else {
            executor.execute(game, UseArtifactAction(card.instanceId, abilityId))
        }
    }

    fun discard(cardId: String) {
        executor.execute(game, DiscardAction(cardId))
    }

    /**
     * Ends the current player's turn (ready, draw refill, switch) and starts the next
     * player's turn (per-turn flag reset + key phase), exactly like the engine loop.
     */
    fun endTurn() {
        executor.execute(game, EndTurnAction)
        game.activePlayer().cannotFightNextTurn = false
        if (TurnPhases.finishTurn(game)) return
        startTurn()
    }

    /** Starts the next turn's fixed steps without playing any action (forge phase etc.). */
    fun startTurn() {
        game.log("--- Turn ${game.turn} / Round ${game.round} - ${game.activePlayer().name} ---")
        TurnPhases.startTurn(game, library)
        game.actionsThisPhase = 0
        game.endPhaseRightNow = false
    }

    private fun chooseHouseIfNecessary(cardId: String, house: House? = null) {
        val player = game.activePlayer()
        val chosen = house ?: library.cardData(cardId)?.house ?: error("Unknown card $cardId")
        if (player.activeHouse == null) {
            executor.execute(game, ChooseHouseAction(chosen))
        }
    }

    private fun instanceOf(cardId: String): String =
        creatureOf(cardId)?.instanceId ?: error("No creature '$cardId' in play for the active player")

    private fun creatureOf(cardId: String): InPlayCard? =
        game.activePlayer().creatures.firstOrNull { it.cardId == cardId }

    /**
     * keyteki-style per-player façade (`this.player1.play(...)` in specs). Every action
     * asserts that the player is currently active, so ported skeletons fail loudly when the
     * turn order does not match the spec.
     */
    inner class PlayerView internal constructor(val player: PlayerState) {

        private fun requireActive() {
            require(player.index == game.activePlayerIndex) {
                "${player.name} is not the active player; end the previous turn first"
            }
        }

        fun chooseHouse(house: House) {
            requireActive()
            this@Scenario.chooseHouse(house)
        }

        fun play(cardId: String, target: String? = null, flank: Int? = null, house: House? = null, addIfMissing: Boolean = false) {
            requireActive()
            this@Scenario.play(cardId, target, flank, house, addIfMissing)
        }

        fun reap(cardId: String, house: House? = null) {
            requireActive()
            this@Scenario.reap(cardId, house)
        }

        fun fight(attackerCardId: String, targetCardId: String, house: House? = null) {
            requireActive()
            this@Scenario.fight(attackerCardId, targetCardId, house)
        }

        fun useAbility(cardId: String, abilityId: String, house: House? = null) {
            requireActive()
            this@Scenario.useAbility(cardId, abilityId, house)
        }

        fun discard(cardId: String) {
            requireActive()
            this@Scenario.discard(cardId)
        }

        fun endTurn() {
            requireActive()
            this@Scenario.endTurn()
        }
    }
}
