package com.aember.analyzer.application

import com.aember.analyzer.domain.model.MassSimulationBatch
import com.aember.analyzer.domain.model.MassSimulationMatchLog
import com.aember.analyzer.domain.model.MassSimulationMatchResult
import com.aember.analyzer.domain.model.MassSimulationRequest
import com.aember.analyzer.domain.model.MassSimulationStatus
import com.aember.analyzer.domain.ports.DeckRepository
import com.aember.analyzer.domain.ports.MassSimulationRepository
import com.aember.analyzer.domain.ports.MassSimulationRunner
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

@Service
class MassSimulationService(
    private val deckRepository: DeckRepository,
    private val repository: MassSimulationRepository,
    private val runner: MassSimulationRunner,
    private val simulationService: SimulationService
) {

    private val logger = LoggerFactory.getLogger(MassSimulationService::class.java)

    fun runMassSimulation(request: MassSimulationRequest): MassSimulationBatch {
        val deckA = deckRepository.findByUuid(request.deckUuidA)
            ?: throw IllegalArgumentException("Deck not found: ${request.deckUuidA}")
        val deckB = deckRepository.findByUuid(request.deckUuidB)
            ?: throw IllegalArgumentException("Deck not found: ${request.deckUuidB}")

        if (request.matchCount <= 0) {
            throw IllegalArgumentException("matchCount must be positive")
        }

        val uuid = UUID.randomUUID().toString()
        val batch = MassSimulationBatch(
            uuid = uuid,
            status = MassSimulationStatus.PENDING,
            deckUuidA = request.deckUuidA,
            deckNameA = deckA.name,
            deckUuidB = request.deckUuidB,
            deckNameB = deckB.name,
            aiA = request.aiA,
            aiB = request.aiB,
            matchCount = request.matchCount
        )
        repository.save(batch)
        logger.info("Created mass simulation batch {} with {} matches", uuid, request.matchCount)

        runner.runAsync { executeBatch(uuid, request) }

        return batch
    }

    fun getBatch(uuid: String): MassSimulationBatch {
        return repository.findByUuid(uuid)
            ?: throw IllegalArgumentException("Batch not found: $uuid")
    }

    fun listBatches(): List<MassSimulationBatch> {
        return repository.findAll()
    }

    fun getMatchResults(batchUuid: String): List<MassSimulationMatchResult> {
        val batch = repository.findByUuid(batchUuid)
            ?: throw IllegalArgumentException("Batch not found: $batchUuid")
        return repository.findResultsByBatchUuid(batchUuid)
    }

    fun getMatchLog(batchUuid: String, matchResultId: Int): MassSimulationMatchLog {
        return repository.findLog(batchUuid, matchResultId)
            ?: throw IllegalArgumentException("Log not found for batch $batchUuid match $matchResultId")
    }

    private fun executeBatch(batchUuid: String, request: MassSimulationRequest) {
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
                    statistics = result.gameState.players.map {
                        it.statistics.copy(housesChosen = it.statistics.housesChosen.toMutableMap())
                    }
                )
                repository.saveResult(matchResult)

                if (request.recordLogs) {
                    repository.saveLog(
                        MassSimulationMatchLog(
                            matchResultId = i,
                            batchUuid = batchUuid,
                            seed = seed,
                            logEntries = result.gameState.log.toList()
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
