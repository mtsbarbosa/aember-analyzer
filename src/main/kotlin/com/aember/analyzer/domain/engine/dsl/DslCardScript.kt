package com.aember.analyzer.domain.engine.dsl

import com.aember.analyzer.domain.engine.CardScript
import com.aember.analyzer.domain.engine.effects.SelfRestriction
import com.aember.analyzer.domain.engine.effects.StaticEffect
import com.aember.analyzer.domain.engine.effects.UseRestriction
import com.aember.analyzer.domain.engine.events.GameEvent
import com.aember.analyzer.domain.engine.model.GameState
import com.aember.analyzer.domain.engine.model.InPlayCard
import com.aember.analyzer.domain.engine.model.PlayerState

import com.aember.analyzer.domain.engine.fidelity.CardFidelity

class DslCardScript(
    private val definition: CardDefinition,
    private val resolver: EffectResolver
) : CardScript {

    override fun fidelity(): CardFidelity = definition.fidelity

    override fun staticEffects(): List<StaticEffect> = definition.staticEffects

    override fun useRestrictions(): List<UseRestriction> = definition.useRestrictions

    override fun selfRestrictions(): List<SelfRestriction> = definition.selfRestrictions

    override fun onEvent(game: GameState, player: PlayerState, card: InPlayCard, event: GameEvent) {
        definition.reactions
            .filter { !it.subscription && it.matches(game, player, card, event) }
            .forEach { reaction ->
                game.log("${card.cardId} reacts: ${reaction.notes.ifEmpty { event::class.simpleName }}")
                resolver.resolve(game, player, card, reaction.effect)
            }
    }

    override fun subscriptionReactions(card: InPlayCard): List<Reaction> =
        definition.reactions.filter { it.subscription }

    override fun onPlay(game: GameState, player: PlayerState, card: InPlayCard) {
        if (definition.playEffect != NoEffect) {
            game.log("${card.cardId} effect resolves")
            resolver.resolve(game, player, card, definition.playEffect)
        }
    }

    override fun onReap(game: GameState, player: PlayerState, card: InPlayCard) {
        if (definition.reapEffect != NoEffect) {
            resolver.resolve(game, player, card, definition.reapEffect)
        }
    }

    override fun onFight(game: GameState, player: PlayerState, card: InPlayCard, target: InPlayCard) {
        if (definition.fightEffect != NoEffect) {
            resolver.resolve(game, player, card, definition.fightEffect)
        }
    }

    override fun onBeforeFight(game: GameState, player: PlayerState, card: InPlayCard, target: InPlayCard) {
        if (definition.beforeFightEffect != NoEffect) {
            resolver.resolve(game, player, card, definition.beforeFightEffect)
        }
    }

    override fun onAfterFight(game: GameState, player: PlayerState, card: InPlayCard, target: InPlayCard) {
        if (definition.afterFightEffect != NoEffect) {
            resolver.resolve(game, player, card, definition.afterFightEffect)
        }
    }

    override fun onBeforeReap(game: GameState, player: PlayerState, card: InPlayCard) {
        if (definition.beforeReapEffect != NoEffect) {
            resolver.resolve(game, player, card, definition.beforeReapEffect)
        }
    }

    override fun onAfterReap(game: GameState, player: PlayerState, card: InPlayCard) {
        if (definition.afterReapEffect != NoEffect) {
            resolver.resolve(game, player, card, definition.afterReapEffect)
        }
    }

    override fun onDestroyed(game: GameState, player: PlayerState, card: InPlayCard) {
        if (definition.destroyedEffect != NoEffect) {
            resolver.resolve(game, player, card, definition.destroyedEffect)
        }
    }

    override fun playable(game: GameState, player: PlayerState): Boolean {
        val condition = definition.playCondition ?: return true
        return resolver.evaluateCondition(game, player, condition)
    }

    override fun actionAbilities(): List<String> {
        return definition.actionAbilities.map { it.first } +
            if (definition.omniEffect != NoEffect) listOf("Omni") else emptyList()
    }

    override fun resolveActionAbility(abilityId: String, game: GameState, player: PlayerState, card: InPlayCard) {
        if (abilityId == "Omni" && definition.omniEffect != NoEffect) {
            resolver.resolve(game, player, card, definition.omniEffect)
            return
        }
        val effect = definition.actionAbilities.find { it.first == abilityId }?.second ?: return
        resolver.resolve(game, player, card, effect)
    }
}
