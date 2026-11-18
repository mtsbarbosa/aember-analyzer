package com.aember.analyzer.domain.engine

import com.aember.analyzer.domain.engine.fidelity.CardFidelity
import com.aember.analyzer.domain.engine.fidelity.FidelityLevel
import com.aember.analyzer.domain.engine.model.GameState
import com.aember.analyzer.domain.engine.model.InPlayCard
import com.aember.analyzer.domain.engine.model.PlayerState

/**
 * Implemented by every hand-ported card. Default implementations are no-ops so cards only
 * override the abilities they actually have.
 */
interface CardScript {

    fun onPlay(game: GameState, player: PlayerState, card: InPlayCard) {}

    fun onReap(game: GameState, player: PlayerState, card: InPlayCard) {}

    fun onFight(game: GameState, player: PlayerState, card: InPlayCard, target: InPlayCard) {}

    fun onBeforeFight(game: GameState, player: PlayerState, card: InPlayCard, target: InPlayCard) {}

    fun onAfterFight(game: GameState, player: PlayerState, card: InPlayCard, target: InPlayCard) {}

    fun onBeforeReap(game: GameState, player: PlayerState, card: InPlayCard) {}

    fun onAfterReap(game: GameState, player: PlayerState, card: InPlayCard) {}

    fun onDestroyed(game: GameState, player: PlayerState, card: InPlayCard) {}

    fun actionAbilities(): List<String> = emptyList()

    fun resolveActionAbility(abilityId: String, game: GameState, player: PlayerState, card: InPlayCard) {}

    fun fidelity(): CardFidelity = CardFidelity(level = FidelityLevel.VANILLA, notes = "No fidelity metadata")
}
