package com.aember.analyzer.domain.engine.effects

import com.aember.analyzer.domain.engine.CardLibrary
import com.aember.analyzer.domain.engine.model.GameState
import com.aember.analyzer.domain.engine.model.InPlayCard
import com.aember.analyzer.domain.engine.model.PlayerState
import com.aember.analyzer.domain.engine.model.hasKeyword

/**
 * Collects and evaluates the persistent static effects of every card in play.
 *
 * The registry is stateless: it recomputes derived stats from scratch on every
 * [recalculate] call (which runs after each action, inside the engine's state
 * check). `InPlayCard` keeps its printed values in `basePower` / `baseArmor` /
 * `baseKeywords`; `power` / `armor` / `keywords` hold the effective values
 * while the effect is active.
 */
object EffectRegistry {

    /** All cards that can project static effects: creatures, artifacts and upgrades. */
    private fun sources(game: GameState): List<InPlayCard> {
        val cards = mutableListOf<InPlayCard>()
        game.players.forEach { player ->
            cards += player.cardsInPlay()
            player.creatures.forEach { cards += it.upgrades }
        }
        return cards
    }

    fun recalculate(library: CardLibrary, game: GameState) {
        val projected = sources(game)
            .flatMap { source -> library.scriptFor(source.cardId).staticEffects().map { source to it } }
            .filter { (source, effect) -> effect.requireFlank == false || isFlank(game, source) }

        game.players.forEach { it.amberCannotBeStolen = cannotBeStolenFrom(library, game, it) }
        game.players.forEach { player ->
            player.creatures.forEach { creature ->
                applyTo(library, game, projected, creature)
                creature.upgrades.forEach { applyTo(library, game, projected, it) }
            }
        }
    }

    private fun applyTo(
        library: CardLibrary,
        game: GameState,
        projected: List<Pair<InPlayCard, StaticEffect>>,
        target: InPlayCard
    ) {
        var power = target.basePower
        var armor = target.baseArmor
        val keywords = target.baseKeywords.toMutableMap()
        projected.forEach { (source, effect) ->
            if (effect.scope == null) return@forEach
            if (!matchesTarget(game, source, effect.scope!!, target)) return@forEach
            val amount = effect.dynamicAmount?.invoke(game, source) ?: effect.amount
            when (effect.kind) {
                StaticEffectKind.MODIFY_POWER -> power += amount
                StaticEffectKind.MODIFY_ARMOR -> armor += amount
                StaticEffectKind.ADD_KEYWORD -> {
                    val kw = effect.keyword!!
                    keywords[kw.name.lowercase()] = maxOf(keywords[kw.name.lowercase()] ?: 0, amount)
                }
                StaticEffectKind.KEY_COST_MODIFIER -> Unit
            }
        }
        target.power = power
        target.armor = armor
        target.keywords = keywords
    }

    /**
     * All use restrictions currently projected by cards in play, paired with
     * their source card (for controller checks).
     */
    fun collectUseRestrictions(
        library: CardLibrary,
        game: GameState
    ): List<Pair<InPlayCard, UseRestriction>> {
        return sources(game).flatMap { source ->
            library.scriptFor(source.cardId).useRestrictions().map { source to it }
        }
    }

    /** True when `owner` (the prospective victim) has a no-steal restriction in play. */
    fun cannotBeStolenFrom(library: CardLibrary, game: GameState, owner: PlayerState): Boolean {
        val cards = owner.cardsInPlay() + owner.creatures.flatMap { it.upgrades }
        return cards.any { library.scriptFor(it.cardId).useRestrictions().contains(UseRestriction.NoSteal) }
    }

    /**
     * Sum of active key-cost modifiers for `payer` (the player trying to forge).
     */
    fun keyCostModifier(library: CardLibrary, game: GameState, payer: PlayerState): Int {
        var modifier = 0
        sources(game).forEach { source ->
            library.scriptFor(source.cardId).staticEffects()
                .filter { it.kind == StaticEffectKind.KEY_COST_MODIFIER }
                .forEach { effect ->
                    if (effect.requireFlank && !isFlank(game, source)) return@forEach
                    // "Your opponent" on an upgrade refers to the host creature's
                    // controller (the card text is granted to the host).
                    val effectiveController = (selfOf(game, source) ?: source).controllerIndex
                    val applies = when (effect.payer) {
                        PayerScope.CONTROLLER -> effectiveController == payer.index
                        PayerScope.OPPONENT_OF_CONTROLLER -> effectiveController != payer.index
                        PayerScope.ANY -> true
                    }
                    if (applies) modifier += effect.amount
                }
        }
        return modifier
    }

    private fun matchesTarget(
        game: GameState,
        source: InPlayCard,
        scope: CreatureScope,
        target: InPlayCard
    ): Boolean {
        val self = selfOf(game, source) ?: return false
        val friendly = target.controllerIndex == source.controllerIndex
        return when (scope) {
            is SelfScope -> target.instanceId == self.instanceId
            is NeighborsOfSelfScope -> {
                val neighbors = neighborsOf(game, self)
                neighbors.any { it.instanceId == target.instanceId }
            }
            is FriendlyCreaturesScope -> friendly
            is OtherFriendlyCreaturesScope -> friendly && target.instanceId != self.instanceId &&
                (scope.house == null || target.house == scope.house) &&
                (scope.trait == null || target.traits.contains(scope.trait))
            is EnemyCreaturesScope -> !friendly
            is AllCreaturesScope -> true
            is FriendlyFlankCreaturesScope -> friendly && isFlank(game, target)
            is FriendlyOfHouseScope -> friendly && target.house == scope.house
            is EnemyOfHouseScope -> !friendly && target.house == scope.house
            is FriendlyWithTraitScope -> friendly && target.traits.contains(scope.trait)
            is HostCreatureScope -> target.instanceId == self.instanceId
        }
    }

    /** For upgrades the effective "self" is the host creature. */
    private fun selfOf(game: GameState, source: InPlayCard): InPlayCard? {
        val hostId = source.hostId ?: return source
        return game.players.asSequence()
            .flatMap { it.creatures.asSequence() }
            .find { it.instanceId == hostId }
    }

    private fun neighborsOf(game: GameState, card: InPlayCard): List<InPlayCard> {
        val controller = game.player(card.controllerIndex)
        val idx = controller.creatures.indexOfFirst { it.instanceId == card.instanceId }
        if (idx == -1) return emptyList()
        return listOfNotNull(
            controller.creatures.getOrNull(idx - 1),
            controller.creatures.getOrNull(idx + 1)
        )
    }

    fun isFlank(game: GameState, card: InPlayCard): Boolean {
        val effective = selfOf(game, card) ?: return false
        val controller = game.player(effective.controllerIndex)
        val battleline = controller.creatures
        if (battleline.size <= 1) return true
        return battleline.first().instanceId == effective.instanceId ||
            battleline.last().instanceId == effective.instanceId
    }
}
