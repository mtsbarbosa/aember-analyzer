package com.aember.analyzer.domain.engine.fidelity

import com.aember.analyzer.domain.engine.model.FidelityEntry

/**
 * Aggregates fidelity log entries across matches into per-card statistics,
 * weighted by the number of times each card was actually played.
 */
object CardFidelityAggregator {

    fun aggregate(entries: List<FidelityEntry>): List<CardFidelityStats> {
        if (entries.isEmpty()) return emptyList()
        val byCard = entries.groupBy { it.cardId }
        return byCard.map { (cardId, cardEntries) ->
            val counts = CardFidelityCounts(cardId)
            cardEntries.groupingBy { it.level }.eachCount().forEach { (level, count) -> counts.add(level, count) }
            counts.toStats()
        }
    }

    /** Folds raw per-card level counts (already summed across matches) into stats. */
    fun fromCounts(cardId: String, counts: Map<FidelityLevel, Int>): CardFidelityStats {
        val accumulator = CardFidelityCounts(cardId)
        counts.forEach { (level, count) -> accumulator.add(level, count) }
        return accumulator.toStats()
    }
}
