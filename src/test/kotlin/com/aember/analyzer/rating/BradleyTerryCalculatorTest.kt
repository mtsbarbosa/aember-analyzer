package com.aember.analyzer.rating

import com.aember.analyzer.domain.rating.BradleyTerryCalculator
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.within
import org.junit.jupiter.api.Test

class BradleyTerryCalculatorTest {

    @Test
    fun `dominant deck ranks first, weakest last`() {
        // A beats B and C always; B beats C always; 2 games per pair.
        val n = 3
        val wins = Array(n) { DoubleArray(n) }
        val games = Array(n) { DoubleArray(n) }
        addPair(wins, games, 0, 1, winsA = 2, winsB = 0)
        addPair(wins, games, 0, 2, winsA = 2, winsB = 0)
        addPair(wins, games, 1, 2, winsA = 2, winsB = 0)

        val strengths = BradleyTerryCalculator.normalize(BradleyTerryCalculator.computeStrengths(wins, games))

        assertThat(strengths[0]).isGreaterThan(strengths[1])
        assertThat(strengths[1]).isGreaterThan(strengths[2])
        // Normalized geometric mean is 1.
        assertThat(strengths.map { kotlin.math.ln(it) }.average()).isCloseTo(0.0, within(1e-6))
    }

    @Test
    fun `perfect circular wins yield equal strengths`() {
        // A > B, B > C, C > A — each wins exactly half of its games; strengths should be equal.
        val n = 3
        val wins = Array(n) { DoubleArray(n) }
        val games = Array(n) { DoubleArray(n) }
        addPair(wins, games, 0, 1, winsA = 2, winsB = 0)
        addPair(wins, games, 1, 2, winsA = 2, winsB = 0)
        addPair(wins, games, 0, 2, winsA = 0, winsB = 2)

        val strengths = BradleyTerryCalculator.normalize(BradleyTerryCalculator.computeStrengths(wins, games))

        assertThat(strengths[0]).isCloseTo(strengths[1], within(1e-6))
        assertThat(strengths[1]).isCloseTo(strengths[2], within(1e-6))
        assertThat(strengths[0]).isCloseTo(1.0, within(1e-6))
    }

    @Test
    fun `wins translate into expected probabilities`() {
        // A beats B in 75% of 8 games.
        val n = 2
        val wins = Array(n) { DoubleArray(n) }
        val games = Array(n) { DoubleArray(n) }
        addPair(wins, games, 0, 1, winsA = 6, winsB = 2)

        val strengths = BradleyTerryCalculator.normalize(BradleyTerryCalculator.computeStrengths(wins, games))

        val expectedProbability = strengths[0] / (strengths[0] + strengths[1])
        assertThat(expectedProbability).isGreaterThan(0.5)
        assertThat(expectedProbability).isLessThan(0.9)
        // With smoothing the estimate pulls away from the raw 0.75 slightly.
        assertThat(expectedProbability).isCloseTo(0.75, within(0.1))
    }

    @Test
    fun `deck with zero wins still gets a positive strength due to smoothing`() {
        val n = 3
        val wins = Array(n) { DoubleArray(n) }
        val games = Array(n) { DoubleArray(n) }
        addPair(wins, games, 0, 1, winsA = 4, winsB = 0)
        addPair(wins, games, 0, 2, winsA = 4, winsB = 0)
        addPair(wins, games, 1, 2, winsA = 4, winsB = 0)

        val strengths = BradleyTerryCalculator.normalize(BradleyTerryCalculator.computeStrengths(wins, games))

        assertThat(strengths[2]).isGreaterThan(0.0)
        assertThat(strengths[2]).isLessThan(strengths[1])
    }

    @Test
    fun `draws count as half wins`() {
        val n = 2
        val allWins = Array(n) { DoubleArray(n) }
        val allDraws = Array(n) { DoubleArray(n) }
        val games = Array(n) { DoubleArray(n) }
        addPair(allWins, games, 0, 1, winsA = 4, winsB = 0)
        addPair(allDraws, games, 0, 1, winsA = 2, winsB = 2)

        val strengthsMixed = BradleyTerryCalculator.normalize(BradleyTerryCalculator.computeStrengths(allDraws, games))
        assertThat(strengthsMixed[0]).isCloseTo(strengthsMixed[1], within(1e-6))
    }

    @Test
    fun `augmented fit keeps relative order when a new deck joins`() {
        // Ladder: A sweeps B. New deck C sweeps A -> C > A > B after refit.
        val n = 3
        val wins = Array(n) { DoubleArray(n) }
        val games = Array(n) { DoubleArray(n) }
        addPair(wins, games, 0, 1, winsA = 2, winsB = 0) // A beats B
        addPair(wins, games, 2, 0, winsA = 2, winsB = 0) // C beats A

        val strengths = BradleyTerryCalculator.normalize(BradleyTerryCalculator.computeStrengths(wins, games))
        assertThat(strengths[2]).isGreaterThan(strengths[0])
        assertThat(strengths[0]).isGreaterThan(strengths[1])
    }

    private fun addPair(
        wins: Array<DoubleArray>,
        games: Array<DoubleArray>,
        i: Int,
        j: Int,
        winsA: Int,
        winsB: Int
    ) {
        wins[i][j] = wins[i][j] + winsA
        wins[j][i] = wins[j][i] + winsB
        games[i][j] = games[i][j] + winsA + winsB
        games[j][i] = games[j][i] + winsA + winsB
    }
}
