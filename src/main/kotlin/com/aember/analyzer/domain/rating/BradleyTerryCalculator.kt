package com.aember.analyzer.domain.rating

import kotlin.math.abs

/**
 * Iterative maximum-likelihood fit of Bradley-Terry strengths.
 *
 * Given observed wins between decks, estimates strengths r_i > 0 such that the
 * expected probability of i beating j is r_i / (r_i + r_j). The update rule is:
 *
 *   r_i <- (wins_i + smoothing) / sum_j( games_ij / (r_i + r_j) )
 *
 * [smoothing] keeps decks with zero wins in the fit (analogous to a half-win
 * pseudo-count) and prevents degenerate zero strengths.
 */
object BradleyTerryCalculator {

    fun computeStrengths(
        wins: Array<DoubleArray>,
        games: Array<DoubleArray>,
        smoothing: Double = 0.5,
        maxIterations: Int = 200,
        tolerance: Double = 1e-10
    ): List<Double> {
        val n = wins.size
        require(n == games.size) { "wins and games matrices must have the same size" }
        val winsTotal = DoubleArray(n) { i -> wins[i].sum() + smoothing }
        val r = DoubleArray(n) { 1.0 }
        val next = DoubleArray(n)

        for (iteration in 0 until maxIterations) {
            for (i in 0 until n) {
                var denom = 0.0
                for (j in 0 until n) {
                    if (j != i && games[i][j] > 0.0) {
                        denom += games[i][j] / (r[i] + r[j])
                    }
                }
                next[i] = if (denom > 0.0) winsTotal[i] / denom else 1.0
            }
            val maxDelta = (0 until n).maxOf { abs(next[it] - r[it]) }
            System.arraycopy(next, 0, r, 0, n)
            if (maxDelta < tolerance) break
        }

        return r.toList()
    }

    fun normalize(strengths: List<Double>): List<Double> {
        val geometricMean = kotlin.math.exp(strengths.map { kotlin.math.ln(it) }.average())
        return strengths.map { it / geometricMean }
    }
}
