package com.aember.analyzer.domain.model

import com.aember.analyzer.domain.engine.model.EndCause
import com.aember.analyzer.domain.engine.model.MatchStatistics

/**
 * Result of a single match within a mass-simulation batch.
 * [statistics] is per player, ordered by player index.
 *
 * [deckUuidA] / [deckUuidB] record the seat order for multi-deck batches
 * (player 0 / player 1). For two-deck batches they stay null and the batch's
 * deckUuidA/B define the seat order.
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
    val statistics: List<MatchStatistics>,
    val deckUuidA: String? = null,
    val deckUuidB: String? = null
)
