package com.aember.analyzer.domain.ai

import com.aember.analyzer.domain.engine.AIPlayer
import com.aember.analyzer.domain.engine.action.*
import com.aember.analyzer.domain.engine.model.GameState
import kotlin.random.Random

/**
 * Chooses uniformly at random from all legal actions.
 */
class RandomAI(private val random: Random = Random.Default) : AIPlayer {

    override fun choose(game: GameState, actions: List<Action>): Action {
        require(actions.isNotEmpty()) { "No legal actions" }
        return actions.random(random)
    }
}
