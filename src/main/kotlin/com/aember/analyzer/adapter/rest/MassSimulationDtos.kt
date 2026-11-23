package com.aember.analyzer.adapter.rest

import com.aember.analyzer.domain.engine.model.EndCause
import com.aember.analyzer.domain.engine.model.LogEntry
import com.aember.analyzer.domain.model.DeckRef
import com.aember.analyzer.domain.model.MassSimulationBatch
import com.aember.analyzer.domain.model.MassSimulationBatchType
import com.aember.analyzer.domain.model.MassSimulationMatchLog
import com.aember.analyzer.domain.model.MassSimulationMatchResult
import com.aember.analyzer.domain.model.MassSimulationStatus
import com.aember.analyzer.domain.rating.DeckAggregate
import com.aember.analyzer.domain.rating.DeckRating
import com.aember.analyzer.domain.rating.PairAggregate
import java.time.Instant

data class MassSimulationBatchDto(
    val uuid: String,
    val status: MassSimulationStatus,
    val batchType: MassSimulationBatchType = MassSimulationBatchType.TWO_DECK,
    val deckA: DeckRefDto? = null,
    val deckB: DeckRefDto? = null,
    val deckPool: List<DeckRefDto> = emptyList(),
    val aiA: String? = null,
    val aiB: String? = null,
    val ai: String? = null,
    val matchesPerPairing: Int? = null,
    val seatAlternation: Boolean? = null,
    val matchCount: Int,
    val completedCount: Int = 0,
    val failedCount: Int = 0,
    val winsA: Int = 0,
    val winsB: Int = 0,
    val draws: Int = 0,
    val avgRounds: Double? = null,
    val avgTurns: Double? = null,
    val avgFidelity: Double? = null,
    val deckAggregates: List<DeckAggregateDto> = emptyList(),
    val pairAggregates: List<PairAggregateDto> = emptyList(),
    val errorMessage: String? = null,
    val createdAt: Instant,
    val completedAt: Instant? = null
)

data class MassSimulationBatchSummaryDto(
    val uuid: String,
    val status: MassSimulationStatus,
    val batchType: MassSimulationBatchType,
    val deckA: DeckRefDto? = null,
    val deckB: DeckRefDto? = null,
    val deckPool: List<DeckRefDto> = emptyList(),
    val matchCount: Int,
    val winsA: Int = 0,
    val winsB: Int = 0,
    val draws: Int = 0,
    val createdAt: Instant
)

data class DeckAggregateDto(
    val deckUuid: String,
    val deckName: String,
    val wins: Int,
    val losses: Int,
    val draws: Int,
    val matches: Int,
    val winRate: Double
)

data class PairAggregateDto(
    val deckUuidA: String,
    val deckUuidB: String,
    val winsA: Int,
    val winsB: Int,
    val draws: Int,
    val avgFidelity: Double?
)

data class DeckRatingDto(
    val deckUuid: String,
    val deckName: String,
    val wins: Int,
    val losses: Int,
    val draws: Int,
    val matches: Int,
    val winRate: Double,
    val winRateMargin: Double,
    val sos: Double,
    val bradleyTerry: Double,
    val aemberScore: Double,
    val percentile: Double,
    val lowConfidence: Boolean,
    val aemberScoreLow: Double? = null,
    val aemberScoreHigh: Double? = null
)

data class MassSimulationMatchResultDto(
    val id: Int,
    val seed: Long,
    val deckUuidA: String?,
    val deckUuidB: String?,
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

fun DeckRef.toDto(): DeckRefDto = DeckRefDto(uuid, name)


fun MassSimulationBatch.toDto(): MassSimulationBatchDto = MassSimulationBatchDto(
    uuid = uuid,
    status = status,
    batchType = batchType,
    deckA = deckNameA?.let { DeckRefDto(deckUuidA ?: "", it) },
    deckB = deckNameB?.let { DeckRefDto(deckUuidB ?: "", it) },
    deckPool = deckPool.map { it.toDto() },
    aiA = aiA,
    aiB = aiB,
    ai = ai,
    matchesPerPairing = matchesPerPairing,
    seatAlternation = seatAlternation,
    matchCount = matchCount,
    completedCount = completedCount,
    failedCount = failedCount,
    winsA = winsA,
    winsB = winsB,
    draws = draws,
    avgRounds = avgRounds,
    avgTurns = avgTurns,
    avgFidelity = avgFidelity,
    deckAggregates = deckAggregates.map { it.toDto() },
    pairAggregates = pairAggregates.map { it.toDto() },
    errorMessage = errorMessage,
    createdAt = createdAt,
    completedAt = completedAt
)

fun MassSimulationBatch.toSummaryDto(): MassSimulationBatchSummaryDto = MassSimulationBatchSummaryDto(
    uuid = uuid,
    status = status,
    batchType = batchType,
    deckA = deckNameA?.let { DeckRefDto(deckUuidA ?: "", it) },
    deckB = deckNameB?.let { DeckRefDto(deckUuidB ?: "", it) },
    deckPool = deckPool.map { it.toDto() },
    matchCount = matchCount,
    winsA = winsA,
    winsB = winsB,
    draws = draws,
    createdAt = createdAt
)

fun DeckAggregate.toDto(): DeckAggregateDto = DeckAggregateDto(
    deckUuid = deckUuid,
    deckName = deckName,
    wins = wins,
    losses = losses,
    draws = draws,
    matches = matches,
    winRate = winRate
)

fun PairAggregate.toDto(): PairAggregateDto = PairAggregateDto(
    deckUuidA = deckUuidA,
    deckUuidB = deckUuidB,
    winsA = winsA,
    winsB = winsB,
    draws = draws,
    avgFidelity = avgFidelity
)

fun DeckRating.toDto(): DeckRatingDto = DeckRatingDto(
    deckUuid = deckUuid,
    deckName = deckName,
    wins = wins,
    losses = losses,
    draws = draws,
    matches = matches,
    winRate = winRate,
    winRateMargin = winRateMargin,
    sos = sos,
    bradleyTerry = bradleyTerry,
    aemberScore = aemberScore,
    percentile = percentile,
    lowConfidence = lowConfidence,
    aemberScoreLow = aemberScoreLow,
    aemberScoreHigh = aemberScoreHigh
)

fun MassSimulationMatchResult.toDto(batch: MassSimulationBatch): MassSimulationMatchResultDto {
    val winnerUuid = when (winnerIndex) {
        0 -> deckUuidA ?: batch.deckUuidA
        1 -> deckUuidB ?: batch.deckUuidB
        else -> null
    }
    val winnerName = winnerUuid?.let { uuid ->
        batch.deckPool.firstOrNull { it.uuid == uuid }?.name
            ?: when (winnerIndex) {
                0 -> batch.deckNameA
                1 -> batch.deckNameB
                else -> null
            }
    }
    return MassSimulationMatchResultDto(
        id = id,
        seed = seed,
        deckUuidA = deckUuidA ?: batch.deckUuidA,
        deckUuidB = deckUuidB ?: batch.deckUuidB,
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
