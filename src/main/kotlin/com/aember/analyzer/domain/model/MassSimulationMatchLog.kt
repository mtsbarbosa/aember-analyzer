package com.aember.analyzer.domain.model

import com.aember.analyzer.domain.engine.model.LogEntry

/**
 * Debug log captured for a single match within a mass-simulation batch.
 */
data class MassSimulationMatchLog(
    val matchResultId: Int,
    val batchUuid: String,
    val seed: Long,
    val logEntries: List<LogEntry>
)
