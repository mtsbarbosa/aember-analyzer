package com.aember.analyzer.domain.model

import com.aember.analyzer.domain.engine.model.EndCause
import com.aember.analyzer.domain.engine.model.MatchStatistics

/**
 * Result of a single match within a mass-simulation batch.
 */
data class MassSimulationMatchResult(
    val id: Int,
    val batchUuid: String,
    val seed: Long,
    val winnerIndex: Int?,
    val endCause: EndCause?,
    val turns: Int,
    val rounds: Int,
    val fidelityScore: Double,
    val statistics: MatchStatistics
)
