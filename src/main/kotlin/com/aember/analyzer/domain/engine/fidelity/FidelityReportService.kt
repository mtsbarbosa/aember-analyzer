package com.aember.analyzer.domain.engine.fidelity

import com.aember.analyzer.domain.engine.model.GameState

object FidelityReportService {

    fun report(game: GameState): FidelityReport {
        if (game.fidelityLog.isEmpty()) {
            return FidelityReport(0.0, emptyMap(), emptyList())
        }
        val grouped = game.fidelityLog.groupingBy { it.level }.eachCount()
        val total = game.fidelityLog.size
        val score = game.fidelityLog.sumOf { it.level.score } / total.toDouble()
        val byCard = game.fidelityLog.groupBy { it.cardId }
            .mapValues { entries -> entries.value.first().level }
        val lowest = byCard.entries
            .sortedBy { it.value.score }
            .take(10)
            .map { it.key to it.value }
        return FidelityReport(score, grouped, lowest)
    }

    fun passesThreshold(report: FidelityReport, threshold: Double): Boolean = report.score >= threshold
}

data class FidelityReport(
    val score: Double,
    val levelCounts: Map<FidelityLevel, Int>,
    val lowestCards: List<Pair<String, FidelityLevel>>
)
