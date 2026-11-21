package com.aember.analyzer.domain.model

import java.time.Instant

/**
 * A named, versioned pool of decks used for pool-relative ratings.
 *
 * [key] is a short identifier (e.g. "cota"). Pools are stored on disk and can
 * be seeded from a plain text file `{key}-pool.txt` containing one deck UUID
 * per line.
 */
data class DeckPool(
    val key: String,
    val deckUuids: List<String>,
    val updatedAt: Instant
)
