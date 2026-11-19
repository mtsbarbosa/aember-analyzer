package com.aember.analyzer.domain.model

import java.time.Instant

enum class MassSimulationStatus {
    PENDING,
    RUNNING,
    COMPLETED,
    FAILED
}

data class MassSimulationBatch(
    val uuid: String,
    val status: MassSimulationStatus,
    val deckUuidA: String,
    val deckNameA: String,
    val deckUuidB: String,
    val deckNameB: String,
    val aiA: String,
    val aiB: String,
    val matchCount: Int,
    val winsA: Int = 0,
    val winsB: Int = 0,
    val draws: Int = 0,
    val avgRounds: Double? = null,
    val avgTurns: Double? = null,
    val avgFidelity: Double? = null,
    val errorMessage: String? = null,
    val createdAt: Instant = Instant.now(),
    val completedAt: Instant? = null
)
