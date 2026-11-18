package com.aember.analyzer.application

import com.aember.analyzer.domain.engine.fidelity.FidelityLevel
import com.aember.analyzer.domain.engine.fidelity.FidelityReport
import com.aember.analyzer.domain.engine.model.EndCause
import com.aember.analyzer.domain.engine.model.GameState
import com.aember.analyzer.domain.engine.model.LogEntry
import com.aember.analyzer.domain.engine.model.PlayerState

sealed class SimulationResponse

data class SimulationResultDto(
    val gameState: GameState,
    val fidelity: FidelityReport,
    val belowFidelityThreshold: Boolean,
    val statistics: MatchStatisticsDto = gameState.statistics.toDto()
) : SimulationResponse()

data class PlayerSummaryDto(
    val index: Int,
    val name: String,
    val deckUuid: String,
    val deckName: String,
    val keys: Int,
    val amber: Int,
    val chains: Int,
    val creatureCount: Int,
    val artifactCount: Int,
    val handCount: Int,
    val discardCount: Int,
    val libraryCount: Int,
    val archivesCount: Int,
    val purgedCount: Int
)

data class SimulationSummaryDto(
    val deckA: DeckRefDto,
    val deckB: DeckRefDto,
    val winnerIndex: Int?,
    val winnerName: String?,
    val endCause: EndCause?,
    val turns: Int,
    val rounds: Int,
    val firstPlayerIndex: Int,
    val players: List<PlayerSummaryDto>,
    val fidelity: FidelitySummaryDto,
    val belowFidelityThreshold: Boolean,
    val recentLog: List<LogEntry>,
    val statistics: MatchStatisticsDto
) : SimulationResponse()

data class DeckRefDto(
    val uuid: String,
    val name: String
)

data class FidelitySummaryDto(
    val score: Double,
    val levelCounts: Map<FidelityLevel, Int>,
    val lowestCards: List<Pair<String, FidelityLevel>>
)

data class MatchStatisticsDto(
    val reaps: Int,
    val fights: Int,
    val actionAbilitiesUsed: Int,
    val omniAbilitiesUsed: Int,
    val cardsPlayed: Int,
    val cardsDiscarded: Int,
    val keysForged: Int,
    val totalActions: Int,
    val housesChosen: Map<String, Int>
)

private const val RECENT_LOG_SIZE = 30

fun GameState.toSummary(): SimulationSummaryDto {
    val playerSummaries = players.map { it.toSummary() }
    val winIndex = winnerIndex
    return SimulationSummaryDto(
        deckA = DeckRefDto(players[0].deck.uuid, players[0].deck.name),
        deckB = DeckRefDto(players[1].deck.uuid, players[1].deck.name),
        winnerIndex = winIndex,
        winnerName = winIndex?.let { players[it].name },
        endCause = endCause,
        turns = turn,
        rounds = round,
        firstPlayerIndex = firstPlayerIndex,
        players = playerSummaries,
        fidelity = FidelitySummaryDto(
            score = 0.0,
            levelCounts = emptyMap(),
            lowestCards = emptyList()
        ),
        belowFidelityThreshold = false,
        recentLog = log.takeLast(RECENT_LOG_SIZE),
        statistics = statistics.toDto()
    )
}

fun GameState.toSummary(fidelityReport: FidelityReport, belowThreshold: Boolean): SimulationSummaryDto {
    return toSummary().copy(
        fidelity = FidelitySummaryDto(
            score = fidelityReport.score,
            levelCounts = fidelityReport.levelCounts,
            lowestCards = fidelityReport.lowestCards
        ),
        belowFidelityThreshold = belowThreshold,
        statistics = statistics.toDto()
    )
}

fun com.aember.analyzer.domain.engine.model.MatchStatistics.toDto(): MatchStatisticsDto = MatchStatisticsDto(
    reaps = reaps,
    fights = fights,
    actionAbilitiesUsed = actionAbilitiesUsed,
    omniAbilitiesUsed = omniAbilitiesUsed,
    cardsPlayed = cardsPlayed,
    cardsDiscarded = cardsDiscarded,
    keysForged = keysForged,
    totalActions = totalActions(),
    housesChosen = housesChosen.toMap()
)

private fun PlayerState.toSummary(): PlayerSummaryDto = PlayerSummaryDto(
    index = index,
    name = name,
    deckUuid = deck.uuid,
    deckName = deck.name,
    keys = keys,
    amber = amber,
    chains = chains,
    creatureCount = creatures.size,
    artifactCount = artifacts.size,
    handCount = hand.size,
    discardCount = discard.size,
    libraryCount = library.size,
    archivesCount = archives.size,
    purgedCount = purged.size
)
