package com.aember.analyzer.domain.engine.fidelity

import com.aember.analyzer.domain.engine.model.GameState
import com.aember.analyzer.domain.engine.model.FidelityEntry

/**
 * Computes match fidelity reports.
 *
 * Every match produces two views over the same fidelity log:
 *
 * - the **declared** view (`score`, `levelCounts`): what card scripts claim. This is the
 *   historical metric and drives the match-level threshold.
 * - the **verified** view (`verifiedScore`, `verifiedLevelCounts`): what is actually proven.
 *   Entries whose card declared [FidelityLevel.FULL] but has no passing parity test
 *   (see [VerifiedCardRegistry]) count as [FidelityLevel.UNVERIFIED] instead, making the
 *   verified score an honest lower bound on real coverage.
 */
object FidelityReportService {

    fun report(game: GameState): FidelityReport = report(game.fidelityLog, VerifiedCardRegistry.verifiedCardIds)

    fun report(fidelityLog: List<FidelityEntry>, verifiedCardIds: Set<String>): FidelityReport {
        if (fidelityLog.isEmpty()) {
            return FidelityReport(0.0, 0.0, emptyMap(), emptyMap(), emptyList(), emptyList())
        }
        val grouped = fidelityLog.groupingBy { it.level }.eachCount()
        val total = fidelityLog.size
        val score = fidelityLog.sumOf { it.level.score } / total.toDouble()

        val verifiedEntries = fidelityLog.map { entry ->
            if (entry.level == FidelityLevel.FULL && entry.cardId !in verifiedCardIds) {
                entry.copy(level = FidelityLevel.UNVERIFIED)
            } else {
                entry
            }
        }
        val verifiedGrouped = verifiedEntries.groupingBy { it.level }.eachCount()
        val verifiedScore = verifiedEntries.sumOf { it.level.score } / total.toDouble()

        val byCard = fidelityLog.groupBy { it.cardId }
            .mapValues { entries -> entries.value.first().level }
        val lowest = byCard.entries
            .sortedWith(compareBy({ it.value.score }, { it.key }))
            .take(10)
            .map { it.key to it.value }

        val verifiedByCard = verifiedEntries.groupBy { it.cardId }
            .mapValues { entries -> entries.value.first().level }
        val lowestVerified = verifiedByCard.entries
            .sortedWith(compareBy({ it.value.score }, { it.key }))
            .take(10)
            .map { it.key to it.value }

        return FidelityReport(score, verifiedScore, grouped, verifiedGrouped, lowest, lowestVerified)
    }

    fun passesThreshold(report: FidelityReport, threshold: Double): Boolean = report.score >= threshold
}

data class FidelityReport(
    val score: Double,
    val verifiedScore: Double,
    val levelCounts: Map<FidelityLevel, Int>,
    val verifiedLevelCounts: Map<FidelityLevel, Int>,
    val lowestCards: List<Pair<String, FidelityLevel>>,
    val lowestVerifiedCards: List<Pair<String, FidelityLevel>>
) {
    val unverifiedCount: Int get() = verifiedLevelCounts[FidelityLevel.UNVERIFIED] ?: 0
}
