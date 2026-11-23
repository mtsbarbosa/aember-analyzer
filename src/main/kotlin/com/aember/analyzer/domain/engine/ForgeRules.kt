package com.aember.analyzer.domain.engine

import com.aember.analyzer.domain.engine.effects.EffectRegistry
import com.aember.analyzer.domain.engine.model.GameState
import com.aember.analyzer.domain.engine.model.PlayerState

/**
 * Computes the current forge cost for a player, including static key-cost
 * modifiers projected by cards in play (e.g., Jammer Pack, Titan Mechanic).
 */
object ForgeRules {

    const val BASE_KEY_COST = 6

    fun keyCost(library: CardLibrary, game: GameState, player: PlayerState): Int {
        return BASE_KEY_COST + EffectRegistry.keyCostModifier(library, game, player)
    }
}
