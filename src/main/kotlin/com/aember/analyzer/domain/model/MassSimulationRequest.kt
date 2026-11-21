package com.aember.analyzer.domain.model

data class MassSimulationRequest(
    val deckUuidA: String,
    val deckUuidB: String,
    val matchCount: Int,
    val aiA: String = "tactical",
    val aiB: String = "tactical",
    val recordLogs: Boolean = true,
    val baseSeed: Long = System.currentTimeMillis()
)
