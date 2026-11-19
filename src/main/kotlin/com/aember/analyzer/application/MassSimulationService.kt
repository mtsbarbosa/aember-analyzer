package com.aember.analyzer.application

import com.aember.analyzer.domain.model.MassSimulationBatch
import com.aember.analyzer.domain.model.MassSimulationMatchResult
import com.aember.analyzer.domain.model.MassSimulationStatus
import com.aember.analyzer.domain.ports.DeckRepository
import com.aember.analyzer.domain.ports.MassSimulationRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class MassSimulationService(
    private val deckRepository: DeckRepository,
    private val repository: MassSimulationRepository,
    private val asyncExecutor: MassSimulationAsyncExecutor
) {

    private val logger = LoggerFactory.getLogger(MassSimulationService::class.java)

    fun runMassSimulation(request: MassSimulationRequest): MassSimulationBatchDto {
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

        asyncExecutor.execute(uuid, request)

        return toDto(batch)
    }

    fun getBatch(uuid: String): MassSimulationBatchDto {
        val batch = repository.findByUuid(uuid)
            ?: throw IllegalArgumentException("Batch not found: $uuid")
        return toDto(batch)
    }

    fun listBatches(): List<MassSimulationBatchSummaryDto> {
        return repository.findAll().map { toSummaryDto(it) }
    }

    fun getMatchResults(batchUuid: String): List<MassSimulationMatchResultDto> {
        val batch = repository.findByUuid(batchUuid)
            ?: throw IllegalArgumentException("Batch not found: $batchUuid")
        return repository.findResultsByBatchUuid(batchUuid).map { toMatchResultDto(it, batch) }
    }

    fun getMatchLog(batchUuid: String, matchResultId: Int): MassSimulationMatchLogDto {
        val log = repository.findLog(batchUuid, matchResultId)
            ?: throw IllegalArgumentException("Log not found for batch $batchUuid match $matchResultId")
        return MassSimulationMatchLogDto(
            matchResultId = log.matchResultId,
            logEntries = log.logEntries,
            fidelityEntries = log.fidelityEntries
        )
    }

    private fun toDto(batch: MassSimulationBatch): MassSimulationBatchDto {
        return MassSimulationBatchDto(
            uuid = batch.uuid,
            status = batch.status,
            deckA = DeckRefDto(batch.deckUuidA, batch.deckNameA),
            deckB = DeckRefDto(batch.deckUuidB, batch.deckNameB),
            aiA = batch.aiA,
            aiB = batch.aiB,
            matchCount = batch.matchCount,
            winsA = batch.winsA,
            winsB = batch.winsB,
            draws = batch.draws,
            avgRounds = batch.avgRounds,
            avgTurns = batch.avgTurns,
            avgFidelity = batch.avgFidelity,
            errorMessage = batch.errorMessage,
            createdAt = batch.createdAt,
            completedAt = batch.completedAt
        )
    }

    private fun toSummaryDto(batch: MassSimulationBatch): MassSimulationBatchSummaryDto {
        return MassSimulationBatchSummaryDto(
            uuid = batch.uuid,
            status = batch.status,
            deckA = DeckRefDto(batch.deckUuidA, batch.deckNameA),
            deckB = DeckRefDto(batch.deckUuidB, batch.deckNameB),
            matchCount = batch.matchCount,
            winsA = batch.winsA,
            winsB = batch.winsB,
            draws = batch.draws,
            createdAt = batch.createdAt
        )
    }

    private fun toMatchResultDto(result: MassSimulationMatchResult, batch: MassSimulationBatch): MassSimulationMatchResultDto {
        val winnerName = when (result.winnerIndex) {
            0 -> batch.deckNameA
            1 -> batch.deckNameB
            else -> null
        }
        return MassSimulationMatchResultDto(
            id = result.id,
            seed = result.seed,
            winnerIndex = result.winnerIndex,
            winnerName = winnerName,
            endCause = result.endCause,
            turns = result.turns,
            rounds = result.rounds,
            fidelityScore = result.fidelityScore,
            statistics = result.statistics.toDto()
        )
    }
}
