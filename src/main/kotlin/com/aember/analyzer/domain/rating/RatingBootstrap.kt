package com.aember.analyzer.domain.rating

import com.aember.analyzer.domain.model.DeckRef
import com.aember.analyzer.domain.model.MassSimulationMatchResult
import kotlin.random.Random

/** Percentile confidence interval on an Aember Score. */
data class ScoreInterval(val low: Double, val high: Double)

/**
 * Bootstrap confidence intervals for Aember Scores.
 *
 * Match results are resampled with replacement within each unordered deck pair
 * (stratified, so every pairing keeps its sample size), the Bradley-Terry model
 * is refit on each resample, and the 2.5th / 97.5th percentiles of the resulting
 * scores form a 95% interval. Seeded, so output is deterministic.
 */
object RatingBootstrap {

    fun scoreIntervals(
        deckPool: List<DeckRef>,
        results: List<MassSimulationMatchResult>,
        iterations: Int = 200,
        seed: Long = 42L,
        /** Decks whose median defines the score origin; null = whole pool (ladder semantics). */
        referenceUuids: Set<String>? = null,
        confidence: Double = 0.95
    ): Map<String, ScoreInterval> {
        if (iterations <= 0 || deckPool.isEmpty()) return emptyMap()
        val strata = results
            .filter { it.deckUuidA != null && it.deckUuidB != null }
            .groupBy { listOf(it.deckUuidA!!, it.deckUuidB!!).sorted() }
            .values.toList()
        if (strata.isEmpty()) return emptyMap()

        val rng = Random(seed)
        val samples = deckPool.associate { it.uuid to ArrayList<Double>(iterations) }
        repeat(iterations) {
            val resampled = strata.flatMap { group -> List(group.size) { group[rng.nextInt(group.size)] } }
            val strengths = RatingAggregator.strengths(deckPool, resampled)
            val reference = deckPool.indices
                .filter { referenceUuids == null || deckPool[it].uuid in referenceUuids }
                .map { strengths[it] }
            val median = RatingAggregator.median(reference)
            deckPool.forEachIndexed { i, ref ->
                samples.getValue(ref.uuid) += AemberScale.score(strengths[i], median)
            }
        }

        val tail = (1.0 - confidence) / 2.0
        return samples.mapValues { (_, values) ->
            val sorted = values.sorted()
            ScoreInterval(percentile(sorted, tail), percentile(sorted, 1.0 - tail))
        }
    }

    private fun percentile(sorted: List<Double>, p: Double): Double {
        val pos = p * (sorted.size - 1)
        val lo = pos.toInt()
        val hi = minOf(lo + 1, sorted.size - 1)
        return sorted[lo] + (sorted[hi] - sorted[lo]) * (pos - lo)
    }
}
