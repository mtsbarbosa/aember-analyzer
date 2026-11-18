package com.aember.analyzer.domain.engine.dsl

import com.aember.analyzer.domain.engine.CardScript
import com.aember.analyzer.domain.engine.model.GameState
import com.aember.analyzer.domain.engine.model.InPlayCard
import com.aember.analyzer.domain.engine.model.PlayerState

import com.aember.analyzer.domain.engine.fidelity.CardFidelity

class DslCardScript(
    private val definition: CardDefinition,
    private val resolver: EffectResolver
) : CardScript {

    override fun fidelity(): CardFidelity = definition.fidelity

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
