package com.aember.analyzer.domain.rating

import com.aember.analyzer.domain.model.DeckRef
import com.aember.analyzer.domain.model.MassSimulationMatchResult
import kotlin.math.log10
import kotlin.math.sqrt

/**
 * Computes per-deck and per-pair aggregates plus simulation-backed ratings
 * from the match results of a multi-deck round-robin batch.
 *
 * Seat order matters: a result's winnerIndex 0 refers to [MassSimulationMatchResult.deckUuidA]
 * and winnerIndex 1 to deckUuidB. Results without seat UUIDs (two-deck batches) are skipped.
 */
object RatingAggregator {

    fun deckAggregates(
        deckPool: List<DeckRef>,
        results: List<MassSimulationMatchResult>
    ): List<DeckAggregate> {
        val byUuid = results.groupBy { it.pairKey() }
        val record = deckPool.associate { it.uuid to mutableDeckRecord() }

        for (pairResults in byUuid.values) {
            for (result in pairResults) {
                val uuidA = result.deckUuidA ?: continue
                val uuidB = result.deckUuidB ?: continue
                when (result.winnerIndex) {
                    0 -> {
                        record[uuidA]?.let { it.wins++; it.matches++ }
                        record[uuidB]?.let { it.losses++; it.matches++ }
                    }
                    1 -> {
                        record[uuidB]?.let { it.wins++; it.matches++ }
                        record[uuidA]?.let { it.losses++; it.matches++ }
                    }
                    else -> {
                        record[uuidA]?.let { it.draws++; it.matches++ }
                        record[uuidB]?.let { it.draws++; it.matches++ }
                    }
                }
            }
        }

        return deckPool.map { ref ->
            val r = record.getValue(ref.uuid)
            DeckAggregate(
                deckUuid = ref.uuid,
                deckName = ref.name,
                wins = r.wins,
                losses = r.losses,
                draws = r.draws,
                matches = r.matches,
                winRate = winRate(r.wins + 0.5 * r.draws, r.matches)
            )
        }
    }

    fun pairAggregates(
        deckPool: List<DeckRef>,
        results: List<MassSimulationMatchResult>
    ): List<PairAggregate> {
        val poolIndex = deckPool.withIndex().associate { (index, ref) -> ref.uuid to index }
        val grouped = results
            .filter { it.deckUuidA != null && it.deckUuidB != null }
            .groupBy { it.canonicalPairKey(poolIndex) }

        return grouped.map { (key, pairResults) ->
            val (uuidA, uuidB) = key
            val winsA = pairResults.count { it.winnerIndex == 0 }
            val winsB = pairResults.count { it.winnerIndex == 1 }
            PairAggregate(
                deckUuidA = uuidA,
                deckUuidB = uuidB,
                winsA = winsA,
                winsB = winsB,
                draws = pairResults.count { it.winnerIndex == null },
                avgFidelity = pairResults.map { it.fidelityScore }.takeIf { it.isNotEmpty() }?.average()
            )
        }.sortedWith(compareBy({ poolIndex[it.deckUuidA] }, { poolIndex[it.deckUuidB] }))
    }

    fun ratings(
        deckPool: List<DeckRef>,
        results: List<MassSimulationMatchResult>,
        lowConfidenceThreshold: Int = 20
    ): List<DeckRating> {
        if (deckPool.isEmpty()) return emptyList()
        val aggregates = deckAggregates(deckPool, results)
        val n = deckPool.size

        val wins = Array(n) { DoubleArray(n) }
        val games = Array(n) { DoubleArray(n) }
        val index = deckPool.withIndex().associate { (i, ref) -> ref.uuid to i }
        for (result in results) {
            val uuidA = result.deckUuidA ?: continue
            val uuidB = result.deckUuidB ?: continue
            val i = index[uuidA] ?: continue
            val j = index[uuidB] ?: continue
            games[i][j] += 1.0
            games[j][i] += 1.0
            when (result.winnerIndex) {
                0 -> wins[i][j] += 1.0
                1 -> wins[j][i] += 1.0
                else -> {
                    wins[i][j] += 0.5
                    wins[j][i] += 0.5
                }
            }
        }

        val strengths = BradleyTerryCalculator.normalize(
            BradleyTerryCalculator.computeStrengths(wins, games)
        )
        val median = median(strengths)
        val winRateByIndex = aggregates.map { it.winRate }

        return deckPool.mapIndexed { i, ref ->
            val agg = aggregates[i]
            val opponents = (0 until n).filter { j -> j != i && games[i][j] > 0.0 }
            val sos = if (opponents.isEmpty()) 0.0
            else opponents.sumOf { games[i][it] * winRateByIndex[it] } / opponents.sumOf { games[i][it] }

            // Percentile excludes the deck itself so the best deck can reach 100.
            val percentile = if (n <= 1) 100.0
            else (aggregates.count { other ->
                other != agg && other.winRate < agg.winRate
            } + 0.5 * aggregates.count { it != agg && it.winRate == agg.winRate }) / (n - 1) * 100.0

            val margin = if (agg.matches == 0) 0.0
            else 1.96 * sqrt(agg.winRate * (1.0 - agg.winRate) / agg.matches)

            DeckRating(
                deckUuid = ref.uuid,
                deckName = ref.name,
                wins = agg.wins,
                losses = agg.losses,
                draws = agg.draws,
                matches = agg.matches,
                winRate = agg.winRate,
                winRateMargin = margin,
                sos = sos,
                bradleyTerry = strengths[i],
                aemberScore = 50.0 + 10.0 * log10(strengths[i] / median),
                percentile = percentile,
                lowConfidence = agg.matches < lowConfidenceThreshold
            )
        }.sortedByDescending { it.aemberScore }
    }

    private class MutableRecord {
        var wins = 0
        var losses = 0
        var draws = 0
        var matches = 0
    }

    private fun mutableDeckRecord() = MutableRecord()

    private fun winRate(weightedWins: Double, matches: Int): Double =
        if (matches == 0) 0.0 else weightedWins / matches

    private fun MassSimulationMatchResult.pairKey(): String? {
        val a = deckUuidA ?: return null
        val b = deckUuidB ?: return null
        return listOf(a, b).sorted().joinToString("|")
    }

    private fun MassSimulationMatchResult.canonicalPairKey(poolIndex: Map<String, Int>): Pair<String, String> {
        val a = deckUuidA!!
        val b = deckUuidB!!
        return if ((poolIndex[a] ?: 0) <= (poolIndex[b] ?: 0)) a to b else b to a
    }

    private fun median(values: List<Double>): Double {
        if (values.isEmpty()) return 1.0
        val sorted = values.sorted()
        val mid = sorted.size / 2
        return if (sorted.size % 2 == 1) sorted[mid] else (sorted[mid - 1] + sorted[mid]) / 2.0
    }
}
