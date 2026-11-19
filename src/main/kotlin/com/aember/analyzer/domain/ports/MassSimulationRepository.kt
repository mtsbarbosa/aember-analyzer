package com.aember.analyzer.domain.ports

import com.aember.analyzer.domain.model.MassSimulationBatch
import com.aember.analyzer.domain.model.MassSimulationMatchLog
import com.aember.analyzer.domain.model.MassSimulationMatchResult

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
}
