package com.aember.analyzer.domain.ports

import com.aember.analyzer.domain.engine.fidelity.CardFidelityStats
import com.aember.analyzer.domain.model.MassSimulationBatch
import com.aember.analyzer.domain.model.MassSimulationMatchLog
import com.aember.analyzer.domain.model.MassSimulationMatchResult
import com.aember.analyzer.domain.rating.DeckRating

/**
 * Driven port for persisting and retrieving mass-simulation artifacts.
 */
interface MassSimulationRepository {

    fun save(batch: MassSimulationBatch)

    fun findByUuid(uuid: String): MassSimulationBatch?

    fun findAll(): List<MassSimulationBatch>

    fun saveResult(result: MassSimulationMatchResult)

    fun findResultsByBatchUuid(uuid: String): List<MassSimulationMatchResult>

    fun saveLog(log: MassSimulationMatchLog)

    fun findLog(batchUuid: String, matchResultId: Int): MassSimulationMatchLog?

    fun saveRatings(batchUuid: String, ratings: List<DeckRating>)

    fun findRatings(batchUuid: String): List<DeckRating>?

    /** Batch-level per-card fidelity aggregate, weighted by plays. */
    fun saveCardFidelity(batchUuid: String, stats: List<CardFidelityStats>)

    fun findCardFidelity(batchUuid: String): List<CardFidelityStats>?

    /** Removes the whole batch directory (batch, matches, ratings, fidelity); returns false if it did not exist. */
    fun delete(batchUuid: String): Boolean
}
