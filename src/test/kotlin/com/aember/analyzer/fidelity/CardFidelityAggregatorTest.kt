package com.aember.analyzer.fidelity

import com.aember.analyzer.adapter.rest.toBatchFidelityDto
import com.aember.analyzer.domain.engine.fidelity.CardFidelityAggregator
import com.aember.analyzer.domain.engine.fidelity.FidelityLevel
import com.aember.analyzer.domain.engine.model.FidelityEntry
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CardFidelityAggregatorTest {

    @Test
    fun `aggregates plays and level counts per card`() {
        val stats = CardFidelityAggregator.aggregate(
            listOf(
                FidelityEntry(1, "a", FidelityLevel.FULL, "play"),
                FidelityEntry(2, "a", FidelityLevel.FULL, "use-creature"),
                FidelityEntry(3, "b", FidelityLevel.PARTIAL, "play")
            )
        )

        val a = stats.first { it.cardId == "a" }
        assertThat(a.plays).isEqualTo(2)
        assertThat(a.levelCounts[FidelityLevel.FULL]).isEqualTo(2)
        assertThat(a.avgScore).isEqualTo(100.0)

        val b = stats.first { it.cardId == "b" }
        assertThat(b.plays).isEqualTo(1)
        assertThat(b.avgScore).isEqualTo(70.0)
    }

    @Test
    fun `verified average never exceeds the declared average`() {
        val stats = CardFidelityAggregator.aggregate(
            listOf(
                FidelityEntry(1, "a", FidelityLevel.FULL, "play"),
                FidelityEntry(2, "b", FidelityLevel.PARTIAL, "play")
            )
        )
        // "a" and "b" are not real card ids, so they are not verified: declared FULL counts as UNVERIFIED.
        assertThat(stats.first { it.cardId == "a" }.avgScore).isEqualTo(100.0)
        assertThat(stats.first { it.cardId == "a" }.verifiedAvgScore).isEqualTo(60.0)
        stats.forEach { assertThat(it.verifiedAvgScore).isLessThanOrEqualTo(it.avgScore) }
    }

    @Test
    fun `batch fidelity dto sorts worst-proven cards first and reports both averages`() {
        val stats = listOf(
            CardFidelityAggregator.fromCounts("good", mapOf(FidelityLevel.FULL to 1)),
            CardFidelityAggregator.fromCounts("bad", mapOf(FidelityLevel.PARTIAL to 3))
        )
        val dto = stats.toBatchFidelityDto(batchAvgFidelity = null)

        // An unverified FULL card (score 60) ranks worse than a verified PARTIAL card (70):
        // the unproven claim is the first thing Phase 3 batches must look at.
        assertThat(dto.cards.first().cardId).isEqualTo("good")
        assertThat(dto.cards).hasSize(2)
        assertThat(dto.cards.first { it.cardId == "good" }.declaredLevel).isEqualTo(FidelityLevel.FULL)
        assertThat(dto.avgScore).isEqualTo((100.0 + 3 * 70.0) / 4)
        // "good" has no parity test, so the verified average is capped at the UNVERIFIED score.
        assertThat(dto.verifiedAvgScore).isEqualTo((60.0 + 3 * 70.0) / 4)
    }

    @Test
    fun `declared level is the most common level with ties resolving to the worse level`() {
        val tie = CardFidelityAggregator.fromCounts("t", mapOf(FidelityLevel.FULL to 1, FidelityLevel.PARTIAL to 1))
        assertThat(tie.declaredLevel).isEqualTo(FidelityLevel.PARTIAL)

        val dominant = CardFidelityAggregator.fromCounts("d", mapOf(FidelityLevel.PARTIAL to 4, FidelityLevel.FULL to 1))
        assertThat(dominant.declaredLevel).isEqualTo(FidelityLevel.PARTIAL)
    }
}
