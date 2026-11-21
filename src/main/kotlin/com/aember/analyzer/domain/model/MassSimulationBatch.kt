package com.aember.analyzer.domain.model

import com.aember.analyzer.domain.rating.DeckAggregate
import com.aember.analyzer.domain.rating.PairAggregate
import java.time.Instant

enum class MassSimulationStatus {
    PENDING,
    RUNNING,
    COMPLETED,
    FAILED
}

enum class MassSimulationBatchType {
    TWO_DECK,
    MULTI_DECK
}

data class DeckRef(
    val uuid: String,
    val name: String
)

data class MassSimulationBatch(
    val uuid: String,
    val status: MassSimulationStatus,
    val batchType: MassSimulationBatchType = MassSimulationBatchType.TWO_DECK,
    val deckUuidA: String? = null,
    val deckNameA: String? = null,
    val deckUuidB: String? = null,
    val deckNameB: String? = null,
    val aiA: String,
    val aiB: String,
    val matchCount: Int,
    val deckPool: List<DeckRef> = emptyList(),
    val ai: String? = null,
    val matchesPerPairing: Int? = null,
    val seatAlternation: Boolean? = null,
    val completedCount: Int = 0,
    val failedCount: Int = 0,
    val winsA: Int = 0,
    val winsB: Int = 0,
    val draws: Int = 0,
    val avgRounds: Double? = null,
    val avgTurns: Double? = null,
    val avgFidelity: Double? = null,
    val deckAggregates: List<DeckAggregate> = emptyList(),
    val pairAggregates: List<PairAggregate> = emptyList(),
    val errorMessage: String? = null,
    val createdAt: Instant = Instant.now(),
    val completedAt: Instant? = null
)
