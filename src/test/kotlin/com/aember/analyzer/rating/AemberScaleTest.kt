package com.aember.analyzer.rating

import com.aember.analyzer.domain.model.DeckRef
import com.aember.analyzer.domain.rating.AemberScale
import com.aember.analyzer.domain.rating.RatingAggregator
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.within
import org.junit.jupiter.api.Test
import kotlin.math.exp

class AemberScaleTest {

    @Test
    fun `median strength scores 50`() {
        assertThat(AemberScale.score(2.0, 2.0)).isCloseTo(50.0, within(1e-12))
    }

    @Test
    fun `one point is about two percentage points of win edge near the median`() {
        // strength ratio giving +1 point: r = e^(1/12.5)
        val r = exp(1.0 / AemberScale.POINTS_PER_LN_UNIT)
        assertThat(AemberScale.score(r, 1.0)).isCloseTo(51.0, within(1e-9))
        val winProb = r / (r + 1.0)
        assertThat((winProb - 0.5) * 100).isCloseTo(2.0, within(0.05))
    }

    @Test
    fun `score is monotonic in strength`() {
        assertThat(AemberScale.score(3.0, 1.0)).isGreaterThan(AemberScale.score(2.0, 1.0))
        assertThat(AemberScale.score(0.5, 1.0)).isLessThan(50.0)
    }

    @Test
    fun `ratings are stamped with the current scale version`() {
        val pool = listOf(DeckRef("a", "A"), DeckRef("b", "B"))
        val ratings = RatingAggregator.ratings(pool, emptyList())
        assertThat(ratings).allSatisfy { assertThat(it.scaleVersion).isEqualTo(AemberScale.VERSION) }
    }
}
