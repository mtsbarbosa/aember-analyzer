package com.aember.analyzer.application

import com.aember.analyzer.domain.engine.model.EndCause
import com.aember.analyzer.domain.engine.model.FidelityEntry
import com.aember.analyzer.domain.engine.model.LogEntry
import com.aember.analyzer.domain.model.MassSimulationStatus
import java.time.Instant

data class MassSimulationRequest(
    val deckUuidA: String,
    val deckUuidB: String,
    val matchCount: Int,
    val aiA: String = "tactical",
    val aiB: String = "tactical",
    val recordLogs: Boolean = true,
    val baseSeed: Long = System.currentTimeMillis()
)

data class MassSimulationBatchDto(
    val uuid: String,
    val status: MassSimulationStatus,
    val deckA: DeckRefDto,
    val deckB: DeckRefDto,
    val aiA: String,
    val aiB: String,
    val matchCount: Int,
    val winsA: Int,
    val winsB: Int,
    val draws: Int,
    val avgRounds: Double?,
    val avgTurns: Double?,
    val avgFidelity: Double?,
    val errorMessage: String?,
    val createdAt: Instant,
    val completedAt: Instant?
)

data class MassSimulationBatchSummaryDto(
    val uuid: String,
    val status: MassSimulationStatus,
    val deckA: DeckRefDto,
    val deckB: DeckRefDto,
    val matchCount: Int,
    val winsA: Int,
    val winsB: Int,
    val draws: Int,
    val createdAt: Instant
)

data class MassSimulationMatchResultDto(
    val id: Int,
    val seed: Long,
    val winnerIndex: Int?,
    val winnerName: String?,
    val endCause: EndCause?,
    val turns: Int,
    val rounds: Int,
    val fidelityScore: Double,
    val statistics: MatchStatisticsDto
)

data class MassSimulationMatchLogDto(
    val matchResultId: Int,
    val logEntries: List<LogEntry>,
    val fidelityEntries: List<FidelityEntry>
)
