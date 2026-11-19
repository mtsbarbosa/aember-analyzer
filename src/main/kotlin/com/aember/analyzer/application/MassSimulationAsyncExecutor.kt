package com.aember.analyzer.application

import com.aember.analyzer.domain.model.MassSimulationBatch
import com.aember.analyzer.domain.model.MassSimulationMatchLog
import com.aember.analyzer.domain.model.MassSimulationMatchResult
import com.aember.analyzer.domain.model.MassSimulationStatus
import com.aember.analyzer.domain.ports.MassSimulationRepository
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class MassSimulationAsyncExecutor(
    private val simulationService: SimulationService,
    private val repository: MassSimulationRepository
) {

    private val logger = LoggerFactory.getLogger(MassSimulationAsyncExecutor::class.java)

    @Async("massSimulationTaskExecutor")
    fun execute(batchUuid: String, request: MassSimulationRequest) {
        logger.info("Starting mass simulation batch {}: {} matches", batchUuid, request.matchCount)
        try {
            val batch = repository.findByUuid(batchUuid)
            if (batch == null) {
                logger.error("Batch {} not found, aborting", batchUuid)
                return
            }

            repository.save(batch.copy(status = MassSimulationStatus.RUNNING))
            val results = mutableListOf<MassSimulationMatchResult>()

            for (i in 1..request.matchCount) {
                val seed = request.baseSeed + (i - 1)
                logger.debug("Batch {} running match {}/{} with seed {}", batchUuid, i, request.matchCount, seed)

                val result = simulationService.simulate(
                    request.deckUuidA,
                    request.deckUuidB,
                    seed,
                    request.aiA,
                    request.aiB
                )

                val matchResult = MassSimulationMatchResult(
                    id = i,
                    batchUuid = batchUuid,
                    seed = seed,
                    winnerIndex = result.gameState.winnerIndex,
                    endCause = result.gameState.endCause,
                    turns = result.gameState.turn,
                    rounds = result.gameState.round,
                    fidelityScore = result.fidelity.score,
                    statistics = result.gameState.statistics
                )
                repository.saveResult(matchResult)

                if (request.recordLogs) {
                    repository.saveLog(
                        MassSimulationMatchLog(
                            matchResultId = i,
                            batchUuid = batchUuid,
                            seed = seed,
                            logEntries = result.gameState.log.toList(),
                            fidelityEntries = result.gameState.fidelityLog.toList()
                        )
                    )
                }

                results.add(matchResult)
            }

            val completedBatch = batch.copy(
                status = MassSimulationStatus.COMPLETED,
                winsA = results.count { it.winnerIndex == 0 },
                winsB = results.count { it.winnerIndex == 1 },
                draws = results.count { it.winnerIndex == null },
                avgRounds = safeAverage(results.map { it.rounds.toDouble() }),
                avgTurns = safeAverage(results.map { it.turns.toDouble() }),
                avgFidelity = safeAverage(results.map { it.fidelityScore }),
                completedAt = Instant.now()
            )
            repository.save(completedBatch)
            logger.info("Completed mass simulation batch {}", batchUuid)
        } catch (e: Exception) {
            logger.error("Mass simulation batch {} failed", batchUuid, e)
            val batch = repository.findByUuid(batchUuid)
            if (batch != null) {
                repository.save(
                    batch.copy(
                        status = MassSimulationStatus.FAILED,
                        errorMessage = e.message,
                        completedAt = Instant.now()
                    )
                )
            }
        }
    }

    private fun safeAverage(values: List<Double>): Double? {
        if (values.isEmpty()) return null
        return values.sum() / values.size
    }
}
