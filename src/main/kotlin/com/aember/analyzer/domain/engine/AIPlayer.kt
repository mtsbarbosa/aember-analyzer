package com.aember.analyzer.domain.engine

import com.aember.analyzer.domain.engine.action.Action
import com.aember.analyzer.domain.engine.model.GameState

interface AIPlayer {
    fun choose(game: GameState, actions: List<Action>): Action
}
