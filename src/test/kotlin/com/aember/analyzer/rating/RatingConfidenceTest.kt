package com.aember.analyzer.rating

import com.aember.analyzer.domain.model.DeckRef
import com.aember.analyzer.domain.model.MassSimulationMatchResult
import com.aember.analyzer.domain.rating.RatingAggregator
import com.aember.analyzer.domain.rating.RatingBootstrap
import com.aember.analyzer.domain.rating.TierValidator
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.assertj.core.api.Assertions.within
import org.junit.jupiter.api.Test

class RatingConfidenceTest {

    private val pool = listOf(DeckRef("a", "A"), DeckRef("b", "B"), DeckRef("c", "C"))

    /** a beats b 80%, b beats c 80%, a beats c 90%, [perPair] games each, deterministic pattern. */
    private fun results(perPair: Int): List<MassSimulationMatchResult> {
        var id = 0
        fun games(x: String, y: String, winPct: Int) = (0 until perPair).map { m ->
            val xWins = (m * 100 / perPair) < winPct
            MassSimulationMatchResult(
                id = ++id, batchUuid = "batch", seed = id.toLong(),
                winnerIndex = if (xWins) 0 else 1, endCause = null, turns = 10, rounds = 5,
                fidelityScore = 90.0, statistics = emptyList(), deckUuidA = x, deckUuidB = y
            )
        }
        return games("a", "b", 80) + games("b", "c", 80) + games("a", "c", 90)
    }

    @Test
    fun `bootstrap interval brackets the point estimate and is deterministic`() {
        val data = results(40)
        val point = RatingAggregator.ratings(pool, data).associateBy { it.deckUuid }
        val first = RatingBootstrap.scoreIntervals(pool, data, iterations = 100)
        val second = RatingBootstrap.scoreIntervals(pool, data, iterations = 100)

        assertThat(first).isEqualTo(second)
        for (ref in pool) {
            val interval = first.getValue(ref.uuid)
            assertThat(interval.low).isLessThanOrEqualTo(interval.high)
            assertThat(point.getValue(ref.uuid).aemberScore).isBetween(interval.low - 1.0, interval.high + 1.0)
        }
    }

    @Test
    fun `more matches narrow the interval`() {
        val narrow = RatingBootstrap.scoreIntervals(pool, results(200), iterations = 100).getValue("a")
        val wide = RatingBootstrap.scoreIntervals(pool, results(10), iterations = 100).getValue("a")
        assertThat(narrow.high - narrow.low).isLessThan(wide.high - wide.low)
    }

    @Test
    fun `ratings expose intervals only when bootstrap is requested`() {
        val data = results(20)
        assertThat(RatingAggregator.ratings(pool, data).first().aemberScoreLow).isNull()
        val withCi = RatingAggregator.ratings(pool, data, bootstrapIterations = 50)
        assertThat(withCi).allSatisfy { assertThat(it.aemberScoreLow).isNotNull(); assertThat(it.aemberScoreHigh).isNotNull() }
    }

    @Test
    fun `spearman is 1 for identical order, -1 for reversed and handles ties`() {
        assertThat(TierValidator.spearman(listOf(1.0, 2.0, 3.0), listOf(10.0, 20.0, 30.0))).isCloseTo(1.0, within(1e-9))
        assertThat(TierValidator.spearman(listOf(1.0, 2.0, 3.0), listOf(3.0, 2.0, 1.0))).isCloseTo(-1.0, within(1e-9))
        assertThat(TierValidator.spearman(listOf(1.0, 2.0, 3.0, 4.0), listOf(1.0, 1.0, 2.0, 2.0))).isBetween(0.8, 1.0)
    }

    @Test
    fun `tier validation agrees when ratings follow tiers`() {
        val ratings = RatingAggregator.ratings(pool, results(30))
        val v = TierValidator.validate(ratings, mapOf("a" to 2.0, "b" to 1.0, "c" to 0.0))
        assertThat(v.spearman).isCloseTo(1.0, within(1e-9))
        assertThat(v.pairwiseAgreement).isEqualTo(1.0)
        assertThat(v.decksCompared).isEqualTo(3)
    }

    @Test
    fun `tier validation needs at least three labelled decks`() {
        val ratings = RatingAggregator.ratings(pool, results(10))
        assertThatThrownBy { TierValidator.validate(ratings, mapOf("a" to 1.0, "b" to 0.0)) }
            .isInstanceOf(IllegalArgumentException::class.java)
    }
}
