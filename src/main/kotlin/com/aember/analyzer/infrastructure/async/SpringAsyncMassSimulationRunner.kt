package com.aember.analyzer.infrastructure.async

import com.aember.analyzer.domain.ports.MassSimulationRunner
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.core.task.TaskExecutor
import org.springframework.stereotype.Component

@Component
class SpringAsyncMassSimulationRunner(
    @Qualifier("massSimulationTaskExecutor") private val taskExecutor: TaskExecutor
) : MassSimulationRunner {

    override fun runAsync(task: Runnable) {
        taskExecutor.execute(task)
    }
}
