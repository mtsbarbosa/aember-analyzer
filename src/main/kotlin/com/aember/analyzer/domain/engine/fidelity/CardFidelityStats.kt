package com.aember.analyzer.domain.engine.fidelity

/**
 * Per-card fidelity aggregate over a set of matches (usually a mass-simulation batch).
 *
 * Every [FidelityEntry] logged during a match is one play/use/destroy event for a card;
 * [plays] counts those events, [levelCounts] breaks them down by declared level and the
 * averages are event-weighted. `verifiedAvgScore` applies the honest verified view
 * (declared FULL without a parity test counts as UNVERIFIED).
 */
data class CardFidelityStats(
    val cardId: String,
    val plays: Int,
    val levelCounts: Map<FidelityLevel, Int>,
    val avgScore: Double,
    val verifiedAvgScore: Double
) {
    /** The most common declared level; ties resolve to the worse level (conservative). */
    val declaredLevel: FidelityLevel get() = levelCounts.entries
        .sortedWith(compareBy({ -it.value }, { it.key.score }))
        .firstOrNull()?.key ?: FidelityLevel.MISSING
}

/**
 * Builds [CardFidelityStats] directly from level counts (no per-play object churn) so a
 * 26k-match ladder aggregates in milliseconds.
 */
internal class CardFidelityCounts(
    val cardId: String,
    val counts: MutableMap<FidelityLevel, Int> = mutableMapOf()
) {
    fun add(level: FidelityLevel, amount: Int = 1) {
        counts[level] = (counts[level] ?: 0) + amount
    }

    val plays: Int get() = counts.values.sum()

    fun toStats(): CardFidelityStats {
        val plays = plays
        val avg = if (plays == 0) 0.0 else counts.entries.sumOf { (level, count) -> level.score * count } / plays.toDouble()
        val verifiedAvg = if (plays == 0) 0.0 else counts.entries.sumOf { (level, count) ->
            val effective = if (level == FidelityLevel.FULL && !VerifiedCardRegistry.isVerified(cardId)) FidelityLevel.UNVERIFIED else level
            effective.score * count
        } / plays.toDouble()
        return CardFidelityStats(cardId, plays, counts.toMap(), avg, verifiedAvg)
    }
}
