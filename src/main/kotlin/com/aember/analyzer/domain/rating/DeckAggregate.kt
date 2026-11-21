package com.aember.analyzer.domain.rating

/**
 * Aggregate win/loss record for one deck within a multi-deck batch.
 */
data class DeckAggregate(
    val deckUuid: String,
    val deckName: String,
    val wins: Int,
    val losses: Int,
    val draws: Int,
    val matches: Int,
    val winRate: Double
)

/**
 * Aggregate record for one canonical deck pair (A/B is the canonical pool order,
 * not the per-match seat order).
 */
data class PairAggregate(
    val deckUuidA: String,
    val deckUuidB: String,
    val winsA: Int,
    val winsB: Int,
    val draws: Int,
    val avgFidelity: Double?
)
