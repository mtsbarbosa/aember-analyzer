package com.aember.analyzer.domain.ports

import com.aember.analyzer.domain.model.DeckPool
import com.aember.analyzer.domain.rating.PoolDeckRating

/**
 * Driven port for persisting deck pools and pool-rating results.
 */
interface DeckPoolRepository {

    fun save(pool: DeckPool)

    fun findByKey(key: String): DeckPool?

    fun saveRating(key: String, rating: PoolDeckRating)

    fun findRatingHistory(key: String, deckUuid: String? = null): List<PoolDeckRating>
}
