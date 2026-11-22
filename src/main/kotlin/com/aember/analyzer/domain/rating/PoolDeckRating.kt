package com.aember.analyzer.domain.rating

import java.time.Instant

/**
 * Result of rating a single deck against a deck pool.
 *
 * The deck plays `matchesPerPairing` matches (seats alternating) against every
 * pool member. [bradleyTerry] / [aemberScore] / [percentile] are anchored on a
 * stored pool ladder: the ladder's stored results plus the fresh matches are
 * refit as one pool with the ladder methodology ([RatingAggregator.ratings]),
 * and the Aember Score is anchored on the refit pool median (opponents only,
 * so the score is directly comparable with the ladder). They are null when no
 * stored ladder covering the pool exists.
 */
data class PoolDeckRating(
    val poolKey: String,
    val deckUuid: String,
    val deckName: String,
    val ai: String,
    val matchesPerPairing: Int,
    val baseSeed: Long,
    val wins: Int,
    val losses: Int,
    val draws: Int,
    val matches: Int,
    val winRate: Double,
    val winRateMargin: Double,
    val opponents: List<OpponentResult>,
    val failedMatches: Int,
    val anchorBatchUuid: String?,
    val anchorRatedAt: Instant?,
    val bradleyTerry: Double?,
    val aemberScore: Double?,
    val percentile: Double?,
    val lowConfidence: Boolean
) {
    data class OpponentResult(
        val deckUuid: String,
        val deckName: String,
        val wins: Int,
        val losses: Int,
        val draws: Int,
        val matches: Int,
        val winRate: Double
    )
}
