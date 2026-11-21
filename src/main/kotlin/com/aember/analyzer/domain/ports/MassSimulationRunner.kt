package com.aember.analyzer.domain.ports

interface MassSimulationRunner {
    fun runAsync(task: Runnable)
}
