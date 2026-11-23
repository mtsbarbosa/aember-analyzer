package com.aember.analyzer.domain.rating

/**
 * Simulation-backed rating for one deck within a pool.
 *
 * [bradleyTerry] is the latent strength from the Bradley-Terry maximum-likelihood
 * fit (normalized so the geometric mean is 1). [aemberScore] maps it to a
 * pool-relative scale: 50 + 10 * log10(r_i / r_median).
 */
data class DeckRating(
    val deckUuid: String,
    val deckName: String,
    val wins: Int,
    val losses: Int,
    val draws: Int,
    val matches: Int,
    val winRate: Double,
    val winRateMargin: Double,
    val sos: Double,
    val bradleyTerry: Double,
    val aemberScore: Double,
    val percentile: Double,
    val lowConfidence: Boolean,
    /** 95% bootstrap interval on [aemberScore]; null when not computed. */
    val aemberScoreLow: Double? = null,
    val aemberScoreHigh: Double? = null
)
