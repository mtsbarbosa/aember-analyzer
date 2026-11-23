package com.aember.analyzer.domain.rating

import kotlin.math.ln

/**
 * Aember Score scale: `50 + K * ln(r / r_median)`.
 *
 * K = 12.5 means one point is roughly 2 percentage points of win-rate edge against an
 * equally strong deck near the median (win edge ≈ 0.25 * ln(r1/r2)), so 2 points is a subtle
 * advantage and 5 points a clear one — the feel players know from SAS ratings.
 * Version 1 (legacy) was `50 + 10 * log10(r / r_median)`, i.e. about 5.8pp per point.
 */
object AemberScale {
    const val VERSION = 2
    const val POINTS_PER_LN_UNIT = 12.5

    fun score(strength: Double, median: Double): Double = 50.0 + POINTS_PER_LN_UNIT * ln(strength / median)
}
