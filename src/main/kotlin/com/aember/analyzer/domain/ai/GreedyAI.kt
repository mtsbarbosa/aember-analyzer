package com.aember.analyzer.domain.ai

import com.aember.analyzer.domain.engine.AIPlayer
import com.aember.analyzer.domain.engine.action.*
import com.aember.analyzer.domain.engine.model.GameState
import com.aember.analyzer.domain.model.House

/**
 * A simple greedy heuristic:
 * - House phase: pick the house with the most cards in hand.
 * - Main phase: reap if below 6 amber, otherwise play creatures, then fight, then end turn.
 */
class GreedyAI : AIPlayer {

    override fun choose(game: GameState, actions: List<Action>): Action {
        require(actions.isNotEmpty()) { "No legal actions" }

        val chooseHouse = actions.filterIsInstance<ChooseHouseAction>()
        if (chooseHouse.isNotEmpty()) {
            return chooseHouse.maxBy { houseCount(game, it.house) }
        }

        // Prefer reaping when close to forging
        val player = game.activePlayer()
        if (player.amber < 6) {
            actions.filterIsInstance<UseCreatureAction>()
                .find { it.useType is ReapUse }
                ?.let { return it }
        }

        // Play creatures / artifacts / actions
        actions.filterIsInstance<PlayCardAction>()
            .firstOrNull()
            ?.let { return it }

        // Fight if nothing else
        actions.filterIsInstance<UseCreatureAction>()
            .find { it.useType is FightUse }
            ?.let { return it }

        // End turn if nothing productive remains
        return actions.find { it is EndTurnAction } ?: actions.first()
    }

    private fun houseCount(game: GameState, house: House): Int {
        val player = game.activePlayer()
        return player.hand.count { cardId ->
            val card = game.players.flatMap { it.deck.cards }.find { it.cardId == cardId }
            card?.effectiveHouse == house
        }
    }
}
