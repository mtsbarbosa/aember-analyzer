package com.aember.analyzer.domain.engine.model

import com.aember.analyzer.domain.engine.fidelity.FidelityLevel
import com.aember.analyzer.domain.model.Deck

/**
 * Mutable root state for a single KeyForge match.
 */
data class GameState(
    val players: List<PlayerState>,
    var activePlayerIndex: Int = 0,
    var firstPlayerIndex: Int = 0,
    var round: Int = 1,
    var turn: Int = 1,
    var winnerIndex: Int? = null,
    var endCause: EndCause? = null,
    val log: MutableList<LogEntry> = mutableListOf(),
    val fidelityLog: MutableList<FidelityEntry> = mutableListOf(),
    var maxRounds: Int = 40,
    var done: Boolean = false,
    var actionsThisPhase: Int = 0,
    var endPhaseRightNow: Boolean = false,
    val statistics: MatchStatistics = MatchStatistics()
) {

    fun activePlayer(): PlayerState = players[activePlayerIndex]

    fun opponent(): PlayerState = players[1 - activePlayerIndex]

    fun player(index: Int): PlayerState = players[index]

    fun log(message: String) {
        log.add(LogEntry(turn, round, activePlayerIndex, message))
    }

    fun logFidelity(cardId: String, level: FidelityLevel, context: String) {
        fidelityLog.add(FidelityEntry(turn, cardId, level, context))
    }
}

enum class EndCause {
    KEYS,
    TIMEOUT,
    DRAW
}

/**
 * Aggregate counts of in-match actions, useful for analyzing AI behavior and match dynamics.
 */
data class MatchStatistics(
    var reaps: Int = 0,
    var fights: Int = 0,
    var actionAbilitiesUsed: Int = 0,
    var omniAbilitiesUsed: Int = 0,
    var cardsPlayed: Int = 0,
    var cardsDiscarded: Int = 0,
    var keysForged: Int = 0,
    var housesChosen: MutableMap<String, Int> = mutableMapOf()
) {
    fun totalActions(): Int = reaps + fights + actionAbilitiesUsed + omniAbilitiesUsed + cardsPlayed + cardsDiscarded
}

data class LogEntry(
    val turn: Int,
    val round: Int,
    val activePlayerIndex: Int,
    val message: String
)

data class FidelityEntry(
    val turn: Int,
    val cardId: String,
    val level: FidelityLevel,
    val context: String
)
