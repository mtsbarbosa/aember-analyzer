package com.aember.analyzer.application

import com.aember.analyzer.domain.engine.fidelity.CardFidelityAggregator
import com.aember.analyzer.domain.engine.fidelity.CardFidelityStats
import com.aember.analyzer.domain.engine.fidelity.FidelityLevel
import com.aember.analyzer.domain.engine.model.FidelityEntry
import com.aember.analyzer.domain.model.Deck
import com.aember.analyzer.domain.model.DeckRef
import com.aember.analyzer.domain.model.MassSimulationBatch
import com.aember.analyzer.domain.model.MassSimulationBatchType
import com.aember.analyzer.domain.model.MassSimulationMatchLog
import com.aember.analyzer.domain.model.MassSimulationMatchResult
import com.aember.analyzer.domain.model.MassSimulationRequest
import com.aember.analyzer.domain.model.MassSimulationStatus
import com.aember.analyzer.domain.model.MatchTask
import com.aember.analyzer.domain.model.MultiDeckSimulationRequest
import com.aember.analyzer.domain.model.RoundRobinScheduleGenerator
import com.aember.analyzer.domain.rating.AemberScale
import com.aember.analyzer.domain.rating.DeckRating
import com.aember.analyzer.domain.rating.RatingAggregator
import com.aember.analyzer.domain.rating.TierValidation
import com.aember.analyzer.domain.rating.TierValidator
import com.aember.analyzer.domain.ports.DeckRepository
import com.aember.analyzer.domain.ports.MassSimulationRepository
import com.aember.analyzer.domain.ports.MassSimulationRunner
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

@Service
class MassSimulationService(
    private val deckRepository: DeckRepository,
    private val repository: MassSimulationRepository,
    private val runner: MassSimulationRunner,
    private val simulationService: SimulationService
) {

    private val logger = LoggerFactory.getLogger(MassSimulationService::class.java)

    private companion object {
        const val BOOTSTRAP_ITERATIONS = 200
    }

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

    fun runMultiDeckSimulation(
        request: MultiDeckSimulationRequest,
        recordLogs: Boolean = false
    ): MassSimulationBatch {
        if (request.deckUuids.size < 2) {
            throw IllegalArgumentException("At least 2 decks are required for a multi-deck batch")
        }
        if (request.deckUuids.distinct().size != request.deckUuids.size) {
            throw IllegalArgumentException("Duplicate deck UUIDs are not allowed")
        }
        if (request.matchesPerPairing <= 0) {
            throw IllegalArgumentException("matchesPerPairing must be positive")
        }

        val decks = request.deckUuids.map { uuid ->
            deckRepository.findByUuid(uuid) ?: throw IllegalArgumentException("Deck not found: $uuid")
        }

        val tasks = RoundRobinScheduleGenerator.generate(
            deckUuids = request.deckUuids,
            matchesPerPairing = request.matchesPerPairing,
            seatAlternation = request.seatAlternation,
            baseSeed = request.baseSeed
        )

        val uuid = UUID.randomUUID().toString()
        val batch = MassSimulationBatch(
            uuid = uuid,
            status = MassSimulationStatus.PENDING,
            batchType = MassSimulationBatchType.MULTI_DECK,
            aiA = request.ai,
            aiB = request.ai,
            matchCount = tasks.size,
            deckPool = decks.map { DeckRef(it.uuid, it.name) },
            ai = request.ai,
            matchesPerPairing = request.matchesPerPairing,
            seatAlternation = request.seatAlternation
        )
        repository.save(batch)
        logger.info(
            "Created multi-deck simulation batch {} with {} decks and {} matches ({} pairings)",
            uuid, decks.size, tasks.size, RoundRobinScheduleGenerator.pairings(decks.size)
        )

        val decksByUuid = decks.associateBy { it.uuid }
        runner.runAsync { executeMultiDeckBatch(uuid, decksByUuid, tasks, request, recordLogs) }

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
        repository.findByUuid(batchUuid)
            ?: throw IllegalArgumentException("Batch not found: $batchUuid")
        return repository.findResultsByBatchUuid(batchUuid)
    }

    fun getMatchLog(batchUuid: String, matchResultId: Int): MassSimulationMatchLog {
        return repository.findLog(batchUuid, matchResultId)
            ?: throw IllegalArgumentException("Log not found for batch $batchUuid match $matchResultId")
    }

    fun getRatings(batchUuid: String): List<DeckRating> {
        val batch = repository.findByUuid(batchUuid)
            ?: throw IllegalArgumentException("Batch not found: $batchUuid")
        if (batch.batchType != MassSimulationBatchType.MULTI_DECK) {
            throw IllegalArgumentException("Ratings are only available for multi-deck batches")
        }
        repository.findRatings(batchUuid)
            ?.takeIf { stored -> stored.all { it.scaleVersion == AemberScale.VERSION } }
            ?.let { return it }
        val results = repository.findResultsByBatchUuid(batchUuid)
        val ratings = RatingAggregator.ratings(batch.deckPool, results, bootstrapIterations = BOOTSTRAP_ITERATIONS)
        if (batch.status == MassSimulationStatus.COMPLETED) {
            repository.saveRatings(batchUuid, ratings)
        }
        return ratings
    }

    /** Spearman check of a batch's ratings against external tier labels (higher = better). */
    fun validateTiers(batchUuid: String, tiers: Map<String, Double>): TierValidation =
        TierValidator.validate(getRatings(batchUuid), tiers)

    /** Per-card fidelity aggregate (weighted by plays) for a batch; empty for batches that predate the aggregate. */
    fun getCardFidelity(batchUuid: String): List<CardFidelityStats> {
        repository.findByUuid(batchUuid)
            ?: throw IllegalArgumentException("Batch not found: $batchUuid")
        return repository.findCardFidelity(batchUuid) ?: emptyList()
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
            val fidelityCounts = CardFidelityAccumulator()

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
                fidelityCounts.add(result.gameState.fidelityLog)

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
                completedCount = results.size,
                winsA = results.count { it.winnerIndex == 0 },
                winsB = results.count { it.winnerIndex == 1 },
                draws = results.count { it.winnerIndex == null },
                avgRounds = safeAverage(results.map { it.rounds.toDouble() }),
                avgTurns = safeAverage(results.map { it.turns.toDouble() }),
                avgFidelity = safeAverage(results.map { it.fidelityScore }),
                completedAt = Instant.now()
            )
            repository.save(completedBatch)
            repository.saveCardFidelity(batchUuid, fidelityCounts.toStats())
            logger.info("Completed mass simulation batch {}", batchUuid)
        } catch (e: Exception) {
            logger.error("Mass simulation batch {} failed", batchUuid, e)
            markBatchFailed(batchUuid, e)
        }
    }

    private fun executeMultiDeckBatch(
        batchUuid: String,
        decksByUuid: Map<String, Deck>,
        tasks: List<MatchTask>,
        request: MultiDeckSimulationRequest,
        recordLogs: Boolean
    ) {
        logger.info("Starting multi-deck simulation batch {}: {} matches across {} decks", batchUuid, tasks.size, decksByUuid.size)
        try {
            val batch = repository.findByUuid(batchUuid)
            if (batch == null) {
                logger.error("Batch {} not found, aborting", batchUuid)
                return
            }

            repository.save(batch.copy(status = MassSimulationStatus.RUNNING))

            val concurrency = (request.concurrency ?: Runtime.getRuntime().availableProcessors()).coerceAtLeast(1)
            val progress = AtomicInteger(0)
            val progressInterval = (tasks.size / 20).coerceAtLeast(1)
            val updateLock = Any()

            fun updateProgress(force: Boolean = false) {
                synchronized(updateLock) {
                    val done = progress.get()
                    if (!force && done % progressInterval != 0) return
                    val current = repository.findByUuid(batchUuid) ?: return
                    if (current.status == MassSimulationStatus.RUNNING) {
                        // Never move the counter backwards if a stale writer raced ahead.
                        repository.save(current.copy(completedCount = maxOf(current.completedCount, done)))
                    }
                }
            }

            val executor = Executors.newFixedThreadPool(
                concurrency,
                Thread.ofVirtual().name("multi-sim-", 0).factory()
            )
            val fidelityCounts = CardFidelityAccumulator()
            try {
                val futures = tasks.map { task ->
                    executor.submit(Callable {
                        try {
                            val outcome = simulateMultiDeckMatch(batchUuid, task, decksByUuid, request.ai, recordLogs)
                            fidelityCounts.add(outcome.gameState.fidelityLog)
                        } catch (e: Exception) {
                            logger.error("Multi-deck batch {} match {} failed", batchUuid, task.taskId, e)
                            return@Callable
                        } finally {
                            progress.incrementAndGet()
                            updateProgress()
                        }
                    })
                }
                futures.forEach { it.get() }
            } finally {
                executor.shutdown()
            }

            val completedBatch = repository.findByUuid(batchUuid)
            if (completedBatch == null) {
                logger.error("Batch {} not found at completion, aborting", batchUuid)
                return
            }

            val results = repository.findResultsByBatchUuid(batchUuid)
            val failedCount = tasks.size - results.size
            if (failedCount > 0) {
                logger.warn("Multi-deck batch {} had {} failed matches", batchUuid, failedCount)
            }

            repository.save(
                completedBatch.copy(
                    status = MassSimulationStatus.COMPLETED,
                    completedCount = results.size,
                    failedCount = failedCount,
                    avgRounds = safeAverage(results.map { it.rounds.toDouble() }),
                    avgTurns = safeAverage(results.map { it.turns.toDouble() }),
                    avgFidelity = safeAverage(results.map { it.fidelityScore }),
                    deckAggregates = RatingAggregator.deckAggregates(completedBatch.deckPool, results),
                    pairAggregates = RatingAggregator.pairAggregates(completedBatch.deckPool, results),
                    completedAt = Instant.now()
                )
            )
            repository.saveRatings(batchUuid, RatingAggregator.ratings(completedBatch.deckPool, results, bootstrapIterations = BOOTSTRAP_ITERATIONS))
            repository.saveCardFidelity(batchUuid, fidelityCounts.toStats())
            logger.info("Completed multi-deck simulation batch {}", batchUuid)
        } catch (e: Exception) {
            logger.error("Multi-deck mass simulation batch {} failed", batchUuid, e)
            markBatchFailed(batchUuid, e)
        }
    }

    private fun simulateMultiDeckMatch(
        batchUuid: String,
        task: MatchTask,
        decksByUuid: Map<String, Deck>,
        ai: String,
        recordLogs: Boolean
    ): SimulationOutcome {
        val deckA = decksByUuid.getValue(task.deckUuidA)
        val deckB = decksByUuid.getValue(task.deckUuidB)
        val result = simulationService.simulate(deckA, deckB, task.seed, ai, ai)

        val matchResult = MassSimulationMatchResult(
            id = task.taskId + 1,
            batchUuid = batchUuid,
            seed = task.seed,
            winnerIndex = result.gameState.winnerIndex,
            endCause = result.gameState.endCause,
            turns = result.gameState.turn,
            rounds = result.gameState.round,
            fidelityScore = result.fidelity.score,
            statistics = result.gameState.players.map {
                it.statistics.copy(housesChosen = it.statistics.housesChosen.toMutableMap())
            },
            deckUuidA = task.deckUuidA,
            deckUuidB = task.deckUuidB
        )
        repository.saveResult(matchResult)

        if (recordLogs) {
            repository.saveLog(
                MassSimulationMatchLog(
                    matchResultId = matchResult.id,
                    batchUuid = batchUuid,
                    seed = task.seed,
                    logEntries = result.gameState.log.toList()
                )
            )
        }
        return result
    }

    private fun markBatchFailed(batchUuid: String, e: Exception) {
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

    private fun safeAverage(values: List<Double>): Double? {
        if (values.isEmpty()) return null
        return values.sum() / values.size
    }
}

/**
 * Thread-safe per-card fidelity counter fed from every match of a batch and converted
 * into play-weighted [CardFidelityStats] once the batch completes.
 */
internal class CardFidelityAccumulator {

    private val counts = java.util.concurrent.ConcurrentHashMap<String, java.util.concurrent.ConcurrentMap<FidelityLevel, Int>>()

    fun add(entries: List<FidelityEntry>) {
        entries.groupBy { it.cardId }.forEach { (cardId, cardEntries) ->
            counts.compute(cardId) { _, existing ->
                val map = existing ?: java.util.concurrent.ConcurrentHashMap()
                cardEntries.forEach { map.merge(it.level, 1, Int::plus) }
                map
            }
        }
    }

    fun toStats(): List<CardFidelityStats> =
        counts.map { (cardId, levelCounts) -> CardFidelityAggregator.fromCounts(cardId, levelCounts) }
}
