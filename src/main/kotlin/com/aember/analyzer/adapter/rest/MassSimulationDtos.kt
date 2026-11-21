package com.aember.analyzer.adapter.rest

import com.aember.analyzer.domain.engine.model.EndCause
import com.aember.analyzer.domain.engine.model.LogEntry
import com.aember.analyzer.domain.model.MassSimulationBatch
import com.aember.analyzer.domain.model.MassSimulationMatchLog
import com.aember.analyzer.domain.model.MassSimulationMatchResult
import com.aember.analyzer.domain.model.MassSimulationStatus
import java.time.Instant

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
    val statistics: List<MatchStatisticsDto>
)

data class MassSimulationMatchLogDto(
    val matchResultId: Int,
    val logEntries: List<LogEntry>
)

fun MassSimulationBatch.toDto(): MassSimulationBatchDto = MassSimulationBatchDto(
    uuid = uuid,
    status = status,
    deckA = DeckRefDto(deckUuidA, deckNameA),
    deckB = DeckRefDto(deckUuidB, deckNameB),
    aiA = aiA,
    aiB = aiB,
    matchCount = matchCount,
    winsA = winsA,
    winsB = winsB,
    draws = draws,
    avgRounds = avgRounds,
    avgTurns = avgTurns,
    avgFidelity = avgFidelity,
    errorMessage = errorMessage,
    createdAt = createdAt,
    completedAt = completedAt
)

fun MassSimulationBatch.toSummaryDto(): MassSimulationBatchSummaryDto = MassSimulationBatchSummaryDto(
    uuid = uuid,
    status = status,
    deckA = DeckRefDto(deckUuidA, deckNameA),
    deckB = DeckRefDto(deckUuidB, deckNameB),
    matchCount = matchCount,
    winsA = winsA,
    winsB = winsB,
    draws = draws,
    createdAt = createdAt
)

fun MassSimulationMatchResult.toDto(batch: MassSimulationBatch): MassSimulationMatchResultDto {
    val winnerName = when (winnerIndex) {
        0 -> batch.deckNameA
        1 -> batch.deckNameB
        else -> null
    }
    return MassSimulationMatchResultDto(
        id = id,
        seed = seed,
        winnerIndex = winnerIndex,
        winnerName = winnerName,
        endCause = endCause,
        turns = turns,
        rounds = rounds,
        fidelityScore = fidelityScore,
        statistics = statistics.map { it.toDto() }
    )
}

fun MassSimulationMatchLog.toDto(): MassSimulationMatchLogDto = MassSimulationMatchLogDto(
    matchResultId = matchResultId,
    logEntries = logEntries
)
