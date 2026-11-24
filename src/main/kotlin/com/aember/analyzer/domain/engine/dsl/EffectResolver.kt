package com.aember.analyzer.domain.engine.dsl

import com.aember.analyzer.domain.engine.CardLibrary
import com.aember.analyzer.domain.engine.model.GameState
import com.aember.analyzer.domain.engine.model.InPlayCard
import com.aember.analyzer.domain.engine.model.PlayerState
import com.aember.analyzer.domain.engine.model.Zone
import com.aember.analyzer.domain.model.CardType
import com.aember.analyzer.domain.model.House
import kotlin.reflect.KClass

/**
 * Executes declarative card effects against the game state.
 *
 * The dispatcher and shared helpers live here; the per-effect handlers are
 * grouped by mechanic into `EffectResolverAmber`, `EffectResolverCombat`,
 * `EffectResolverCards`, `EffectResolverControl` and `EffectResolverReactions`
 * as internal extension functions on this class.
 */
class EffectResolver(
    internal val targetChooser: TargetChooser = GreedyTargetChooser(),
    internal val library: CardLibrary? = null
) {

    internal val handlers = mutableMapOf<KClass<out Effect>, (GameState, PlayerState, InPlayCard, Effect) -> Unit>()

    /** Bridge to the real play path (ActionExecutor.playCard), wired by GameEngine / Scenario. */
    internal var playBridge: CardPlayBridge? = null

    /** All known [Condition] types must be mapped here (guarded by EffectResolverRegistrationTest). */
    internal val conditionHandlers: Map<KClass<out Condition>, (Condition, GameState, PlayerState) -> Boolean> = mapOf(
        OpponentHasMoreAmber::class to { _, g, p -> g.opponent(p).amber > p.amber },
        SelfHasMoreAmber::class to { _, g, p -> p.amber > g.opponent(p).amber },
        OpponentHasFewerCreatures::class to { _, g, p -> g.opponent(p).creatures.size < p.creatures.size },
        SelfHasFewerCreatures::class to { _, g, p -> p.creatures.size < g.opponent(p).creatures.size },
        SelfHasMoreCreatures::class to { _, g, p -> p.creatures.size > g.opponent(p).creatures.size },
        TargetIsDamaged::class to { _, _, _ -> false },
        Always::class to { _, _, _ -> true },
        OpponentAmberAtLeast::class to { c, g, p -> g.opponent(p).amber >= (c as OpponentAmberAtLeast).amount },
        OpponentAmberAtMost::class to { c, g, p -> g.opponent(p).amber <= (c as OpponentAmberAtMost).amount },
        OpponentAmberExactly::class to { c, g, p -> g.opponent(p).amber == (c as OpponentAmberExactly).amount },
        SelfAmberAtLeast::class to { c, _, p -> p.amber >= (c as SelfAmberAtLeast).amount },
        StealSucceeded::class to { _, g, _ -> g.lastStealAmount > 0 },
        DamageDestroyedTarget::class to { _, g, _ -> g.lastDamageDestroyed },
        SacrificedTarget::class to { _, g, _ -> g.lastSacrificeSucceeded },
        PurgedTarget::class to { _, g, _ -> g.lastPurgedFromDiscard > 0 },
        HealedAtLeast::class to { c, g, _ -> g.lastHealAmount >= (c as HealedAtLeast).amount },
        OpponentForgedKeyLastTurn::class to { _, g, p -> g.opponent(p).forgedKeyLastTurn },
        CardsOfHousePlayedThisTurn::class to { c, _, p ->
            val cond = c as CardsOfHousePlayedThisTurn
            (p.cardsPlayedThisTurnByHouse[cond.house] ?: 0) >= cond.amount
        },
        CreatureInHand::class to { _, _, p ->
            p.hand.any { library?.cardData(it)?.type == CardType.CREATURE }
        }
    )

    init {
        register<NoEffect> { _, _, _, _ -> }
        registerAmberEffects()
        registerCombatEffects()
        registerCardEffects()
        registerControlEffects()
        registerReactionEffects()
    }

    fun resolve(
        game: GameState,
        sourcePlayer: PlayerState,
        sourceCard: InPlayCard,
        effect: Effect
    ) {
        // Reset ephemeral trackers at the start of a top-level effect.
        if (sourceCard.instanceId != "dummy") {
            game.lastStealAmount = 0
            game.lastDamageDestroyed = false
            game.lastDamagedTargetId = null
            game.lastHealAmount = 0
            game.lastHealedTargetId = null
            game.lastSacrificeSucceeded = false
        }
        resolveInternal(game, sourcePlayer, sourceCard, effect)
    }

    internal fun resolveInternal(
        game: GameState,
        sourcePlayer: PlayerState,
        sourceCard: InPlayCard,
        effect: Effect
    ) {
        when (effect) {
            is Sequence -> effect.effects.forEach { resolveInternal(game, sourcePlayer, sourceCard, it) }
            is Conditional -> {
                if (evaluateCondition(game, sourcePlayer, effect.condition)) {
                    resolveInternal(game, sourcePlayer, sourceCard, effect.thenEffect)
                } else {
                    resolveInternal(game, sourcePlayer, sourceCard, effect.elseEffect)
                }
            }
            else -> {
                val handler = handlers[effect::class]
                    ?: throw IllegalArgumentException("No handler for ${effect::class.simpleName}")
                handler(game, sourcePlayer, sourceCard, effect)
            }
        }
    }

    internal inline fun <reified T : Effect> register(
        crossinline handler: (GameState, PlayerState, InPlayCard, T) -> Unit
    ) {
        handlers[T::class] = { game, player, card, effect ->
            @Suppress("UNCHECKED_CAST")
            handler(game, player, card, effect as T)
        }
    }

    fun evaluateCondition(game: GameState, player: PlayerState, condition: Condition): Boolean {
        val opponent = game.opponent()
        val handler = conditionHandlers[condition::class] ?: return false
        return handler(condition, game, player)
    }

    internal fun targetPlayer(game: GameState, sourcePlayer: PlayerState, player: TargetPlayer): PlayerState {
        return when (player) {
            is SelfPlayer -> sourcePlayer
            is OpponentPlayer -> game.opponent()
        }
    }

    internal fun findCreatureInPlay(game: GameState, instanceId: String): InPlayCard? =
        game.players.asSequence()
            .flatMap { it.creatures.asSequence() }
            .find { it.instanceId == instanceId }

    internal fun ensureLibraryNotEmpty(game: GameState, player: PlayerState) {
        if (player.library.isEmpty() && player.discard.isNotEmpty()) {
            player.library.addAll(player.discard)
            player.discard.clear()
            player.library.shuffle(game.rng)
        }
    }

    internal fun dummySource(player: PlayerState): InPlayCard {
        return InPlayCard(
            instanceId = "dummy",
            cardId = "dummy",
            ownerIndex = player.index,
            controllerIndex = player.index,
            zone = Zone.PLAY_AREA,
            house = House.BROBNAR,
            type = CardType.CREATURE
        )
    }

    internal fun cardName(cardId: String): String {
        return library?.cardData(cardId)?.name ?: cardId
    }
}

/** Cost-style choices (sacrifice, shuffle away): lowest power first, then already exhausted, then damaged. */
internal fun List<InPlayCard>.cheapestFirst(): List<InPlayCard> =
    sortedWith(compareBy<InPlayCard> { it.power }.thenBy { !it.exhausted }.thenBy { -it.damage })

internal fun CreatureSelector.allScope(): CreatureSelector =
    if (this is FriendlyCreature) FriendlyCreature(Scope.ALL) else this

/**
 * Lightweight observable-state signature for the [RepeatWhile] progress
 * guard: amber totals and creature counts. Effects that change neither
 * cannot make progress toward flipping a condition.
 */
internal fun stateSignature(game: GameState): Pair<List<Int>, Int> =
    game.players.map { it.amber } to game.players.sumOf { it.creatures.size }
