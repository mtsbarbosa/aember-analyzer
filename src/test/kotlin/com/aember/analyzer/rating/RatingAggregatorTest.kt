package com.aember.analyzer.rating

import com.aember.analyzer.domain.engine.model.EndCause
import com.aember.analyzer.domain.engine.model.MatchStatistics
import com.aember.analyzer.domain.model.DeckRef
import com.aember.analyzer.domain.model.MassSimulationMatchResult
import com.aember.analyzer.domain.rating.RatingAggregator
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.within
import org.junit.jupiter.api.Test

class RatingAggregatorTest {

    private val pool = listOf(
        DeckRef("deck-a", "Deck A"),
        DeckRef("deck-b", "Deck B"),
        DeckRef("deck-c", "Deck C")
    )

    @Test
    fun `deck aggregates count wins, losses and draws across seats`() {
        val results = listOf(
            result(seed = 1, winnerIndex = 0, deckUuidA = "deck-a", deckUuidB = "deck-b"),
            result(seed = 2, winnerIndex = 1, deckUuidA = "deck-b", deckUuidASeatB = true, deckUuidB = "deck-a"),
            result(seed = 3, winnerIndex = null, deckUuidA = "deck-a", deckUuidB = "deck-b"),
            result(seed = 4, winnerIndex = 0, deckUuidA = "deck-a", deckUuidB = "deck-c")
        )

        val aggregates = RatingAggregator.deckAggregates(pool, results)
        val byUuid = aggregates.associateBy { it.deckUuid }

        assertThat(byUuid["deck-a"]!!.matches).isEqualTo(4)
        assertThat(byUuid["deck-a"]!!.wins).isEqualTo(2)
        assertThat(byUuid["deck-a"]!!.losses).isEqualTo(1)
        assertThat(byUuid["deck-a"]!!.draws).isEqualTo(1)

        assertThat(byUuid["deck-b"]!!.matches).isEqualTo(3)
        assertThat(byUuid["deck-b"]!!.wins).isEqualTo(1)
        assertThat(byUuid["deck-b"]!!.losses).isEqualTo(1)
        assertThat(byUuid["deck-b"]!!.draws).isEqualTo(1)

        assertThat(byUuid["deck-c"]!!.matches).isEqualTo(1)
        assertThat(byUuid["deck-c"]!!.losses).isEqualTo(1)
    }

    @Test
    fun `pair aggregates use canonical pool order`() {
        val results = listOf(
            result(seed = 1, winnerIndex = 0, deckUuidA = "deck-c", deckUuidB = "deck-a"),
            result(seed = 2, winnerIndex = 1, deckUuidA = "deck-c", deckUuidB = "deck-a")
        )

        val pairAggregates = RatingAggregator.pairAggregates(pool, results)

        assertThat(pairAggregates).hasSize(1)
        val pair = pairAggregates[0]
        // deck-a comes first in the pool, so the canonical pair is (a, c).
        assertThat(pair.deckUuidA).isEqualTo("deck-a")
        assertThat(pair.deckUuidB).isEqualTo("deck-c")
        // Winner of seat 0 (deck-c) counts as wins for deck-c = winsB in canonical order.
        assertThat(pair.winsA).isEqualTo(1)
        assertThat(pair.winsB).isEqualTo(1)
    }

    @Test
    fun `ratings rank decks and compute Aember Score relative to median`() {
        // deck-a beats everyone; deck-b loses to a and beats c; deck-c loses to both.
        val results = mutableListOf<MassSimulationMatchResult>()
        repeat(4) { results += result(seed = it.toLong(), winnerIndex = 0, deckUuidA = "deck-a", deckUuidB = "deck-b") }
        repeat(4) { results += result(seed = 10L + it, winnerIndex = 0, deckUuidA = "deck-a", deckUuidB = "deck-c") }
        repeat(4) { results += result(seed = 20L + it, winnerIndex = 0, deckUuidA = "deck-b", deckUuidB = "deck-c") }

        val ratings = RatingAggregator.ratings(pool, results)

        assertThat(ratings.map { it.deckUuid }).containsExactly("deck-a", "deck-b", "deck-c")
        assertThat(ratings[0].aemberScore).isGreaterThan(50.0)
        assertThat(ratings[2].aemberScore).isLessThan(50.0)
        // median deck scores 50
        assertThat(ratings[1].aemberScore).isCloseTo(50.0, within(1.0))
        assertThat(ratings[0].percentile).isGreaterThan(50.0)
        assertThat(ratings[2].percentile).isLessThan(50.0)
        assertThat(ratings.all { it.lowConfidence }).isTrue()
    }

    @Test
    fun `sos averages opponents win rates`() {
        // deck-c only ever plays deck-a (win rate 100%) -> sos == 1.0
        val results = listOf(
            result(seed = 1, winnerIndex = 0, deckUuidA = "deck-a", deckUuidB = "deck-c"),
            result(seed = 2, winnerIndex = 0, deckUuidA = "deck-a", deckUuidB = "deck-c")
        )

        val ratings = RatingAggregator.ratings(pool, results)
        val byUuid = ratings.associateBy { it.deckUuid }

        assertThat(byUuid["deck-c"]!!.sos).isCloseTo(1.0, within(1e-9))
        // deck-b played nobody -> sos 0, and 2 matches threshold flags low confidence
        assertThat(byUuid["deck-b"]!!.matches).isEqualTo(0)
        assertThat(byUuid["deck-b"]!!.sos).isEqualTo(0.0)
        assertThat(byUuid["deck-a"]!!.lowConfidence).isTrue()
        assertThat(byUuid["deck-c"]!!.lowConfidence).isTrue()
    }

    private fun result(
        seed: Long,
        winnerIndex: Int?,
        deckUuidA: String,
        deckUuidB: String,
        deckUuidASeatB: Boolean = false
    ): MassSimulationMatchResult {
        // deckUuidASeatB lets a test express that "deckUuidA" param actually sat in seat 1.
        val seatA = if (deckUuidASeatB) deckUuidB else deckUuidA
        val seatB = if (deckUuidASeatB) deckUuidA else deckUuidB
        return MassSimulationMatchResult(
            id = seed.toInt(),
            batchUuid = "batch",
            seed = seed,
            winnerIndex = winnerIndex,
            endCause = if (winnerIndex == null) EndCause.TIMEOUT else EndCause.KEYS,
            turns = 20,
            rounds = 10,
            fidelityScore = 90.0,
            statistics = listOf(MatchStatistics(), MatchStatistics()),
            deckUuidA = seatA,
            deckUuidB = seatB
        )
    }
}
