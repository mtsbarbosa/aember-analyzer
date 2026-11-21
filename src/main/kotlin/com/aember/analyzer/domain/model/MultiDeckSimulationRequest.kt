package com.aember.analyzer.domain.model

/**
 * Request for a multi-deck round-robin simulation batch.
 *
 * Runs [matchesPerPairing] matches for every unordered deck pair in [deckUuids].
 * When [seatAlternation] is true, seats are swapped on odd match indices so the
 * first-player bias cancels out. The same AI policy ([ai]) is used for both players.
 */
data class MultiDeckSimulationRequest(
    val deckUuids: List<String>,
    val matchesPerPairing: Int = 10,
    val ai: String = "tactical",
    val seatAlternation: Boolean = true,
    val baseSeed: Long = System.currentTimeMillis(),
    val concurrency: Int? = null
)
