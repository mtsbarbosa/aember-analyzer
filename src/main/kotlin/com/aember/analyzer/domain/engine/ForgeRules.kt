package com.aember.analyzer.domain.engine

import com.aember.analyzer.domain.engine.model.GameState
import com.aember.analyzer.domain.engine.model.PlayerState

/**
 * Computes the current forge cost for a player, including static key-cost
 * modifiers from upgrades controlled by the opponent (e.g., Jammer Pack).
 */
object ForgeRules {

    const val BASE_KEY_COST = 6

    fun keyCost(library: CardLibrary, game: GameState, player: PlayerState): Int {
        var cost = BASE_KEY_COST
        game.opponent(player).creatures.forEach { creature ->
            creature.upgrades.forEach { upgrade ->
                cost += library.scriptFor(upgrade.cardId).opponentKeyCostModifier()
            }
        }
        return cost
    }
}
