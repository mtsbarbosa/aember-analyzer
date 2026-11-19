package com.aember.analyzer.infrastructure.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.TaskExecutor
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

@Configuration
@EnableAsync
class AsyncConfig {

    @Bean("massSimulationTaskExecutor")
    fun massSimulationTaskExecutor(): TaskExecutor {
        val executor = ThreadPoolTaskExecutor()
        executor.setCorePoolSize(2)
        executor.setMaxPoolSize(4)
        executor.setQueueCapacity(100)
        executor.setThreadNamePrefix("mass-sim-")
        executor.initialize()
        return executor
    }
}
