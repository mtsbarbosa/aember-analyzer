package com.aember.analyzer.application

import com.aember.analyzer.domain.engine.fidelity.FidelityReport
import com.aember.analyzer.domain.engine.model.GameState

data class SimulationOutcome(
    val gameState: GameState,
    val fidelity: FidelityReport,
    val belowFidelityThreshold: Boolean
)
