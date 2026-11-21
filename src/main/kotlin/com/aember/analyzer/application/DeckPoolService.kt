package com.aember.analyzer.application

import com.aember.analyzer.domain.engine.model.MatchStatistics
import com.aember.analyzer.domain.model.DeckPool
import com.aember.analyzer.domain.model.DeckRef
import com.aember.analyzer.domain.model.MassSimulationBatchType
import com.aember.analyzer.domain.model.MassSimulationMatchResult
import com.aember.analyzer.domain.model.MassSimulationStatus
import com.aember.analyzer.domain.rating.PoolDeckRating
import com.aember.analyzer.domain.rating.RatingAggregator
import com.aember.analyzer.domain.ports.DeckPoolRepository
import com.aember.analyzer.domain.ports.DeckRepository
import com.aember.analyzer.domain.ports.MassSimulationRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.nio.file.Files
import java.nio.file.Path
import java.time.Instant
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger
import kotlin.io.path.exists
import kotlin.math.log10
import kotlin.math.sqrt

/**
 * Manages named deck pools and computes single-deck ratings against a pool.
 *
 * Rating a deck runs `matchesPerPairing` matches (seats alternating) against
 * every pool member in parallel. If a stored pool-ladder batch exists whose
 * deck pool covers all opponents, the ladder's stored results are replayed,
 * the fresh matches are added, and the whole pool (rated deck included) is
 * refit with the ladder methodology ([RatingAggregator.ratings]). The deck's
 * Aember Score is anchored on the pool median, making it directly comparable
 * with the ladder. Without a stored ladder, only raw stats are reported.
 */
@Service
class DeckPoolService(
    private val deckRepository: DeckRepository,
    private val poolRepository: DeckPoolRepository,
    private val massSimulationRepository: MassSimulationRepository,
    private val simulationService: SimulationService,
    @Value("\${aember.pools.seed-dir:.}") private val seedDir: String
) {

    private val logger = LoggerFactory.getLogger(DeckPoolService::class.java)

    fun getPool(key: String): DeckPool {
        return poolRepository.findByKey(key) ?: seedPool(key)
    }

    fun addToPool(key: String, deckUuid: String): DeckPool {
        val deck = deckRepository.findByUuid(deckUuid)
            ?: throw IllegalArgumentException("Deck not found: $deckUuid (import it first)")
        // Creating a pool by adding its first deck is allowed even without a seed file.
        val pool = poolRepository.findByKey(key)
            ?: DeckPool(key = key, deckUuids = emptyList(), updatedAt = Instant.now())
                .also { poolRepository.save(it) }
        if (deckUuid in pool.deckUuids) return pool
        val updated = pool.copy(deckUuids = pool.deckUuids + deck.uuid, updatedAt = Instant.now())
        poolRepository.save(updated)
        logger.info("Added deck {} to pool {}", deck.name, key)
        return updated
    }

    fun removeFromPool(key: String, deckUuid: String): DeckPool {
        val pool = getPool(key)
        if (deckUuid !in pool.deckUuids) return pool
        val updated = pool.copy(deckUuids = pool.deckUuids - deckUuid, updatedAt = Instant.now())
        poolRepository.save(updated)
        logger.info("Removed deck {} from pool {}", deckUuid, key)
        return updated
    }

    fun rateDeckAgainstPool(
        poolKey: String,
        deckUuid: String,
        matchesPerPairing: Int = 10,
        baseSeed: Long = 0L,
        ai: String = "tactical",
        concurrency: Int? = null
    ): PoolDeckRating {
        require(matchesPerPairing > 0) { "matchesPerPairing must be positive" }

        val pool = getPool(poolKey)
        require(pool.deckUuids.isNotEmpty()) { "Pool '$poolKey' is empty" }

        val deck = deckRepository.findByUuid(deckUuid)
            ?: throw IllegalArgumentException("Deck not found: $deckUuid (import it first)")

        val opponentUuids = pool.deckUuids.filter { it != deckUuid }
        require(opponentUuids.isNotEmpty()) { "Pool '$poolKey' has no other decks to play against" }
        val opponents = opponentUuids.map { uuid ->
            deckRepository.findByUuid(uuid)
                ?: throw IllegalArgumentException("Pool deck not imported: $uuid")
        }

        val anchor = findAnchorBatch(opponentUuids)

        val tasks = opponentUuids.flatMapIndexed { pairIndex, opponent ->
            (0 until matchesPerPairing).map { m ->
                // Seat alternation so first-player bias cancels.
                Triple(opponent, m % 2 == 1, baseSeed + (pairIndex * matchesPerPairing + m).toLong())
            }
        }

        val wins = AtomicInteger()
        val losses = AtomicInteger()
        val draws = AtomicInteger()
        val failed = AtomicInteger()
        val perOpponent = java.util.concurrent.ConcurrentHashMap<String, IntArray>()
        val outcomes = java.util.concurrent.ConcurrentLinkedQueue<MatchOutcome>()

        val workers = (concurrency ?: Runtime.getRuntime().availableProcessors()).coerceAtLeast(1)
        val executor = Executors.newFixedThreadPool(workers, Thread.ofVirtual().name("pool-sim-", 0).factory())
        try {
            val futures = tasks.map { (opponentUuid, swap, seed) ->
                executor.submit(Callable {
                    try {
                        val deckA = if (swap) deckRepository.findByUuid(opponentUuid)!! else deck
                        val deckB = if (swap) deck else deckRepository.findByUuid(opponentUuid)!!
                        val result = simulationService.simulate(deckA, deckB, seed, ai, ai)
                        when (result.gameState.winnerIndex) {
                            null -> draws.incrementAndGet()
                            0 -> if (swap) losses.incrementAndGet() else wins.incrementAndGet()
                            else -> if (swap) wins.incrementAndGet() else losses.incrementAndGet()
                        }
                        outcomes += MatchOutcome(
                            deckUuidA = deckA.uuid,
                            deckUuidB = deckB.uuid,
                            winnerIndex = result.gameState.winnerIndex,
                            endCause = result.gameState.endCause,
                            turns = result.gameState.turn,
                            rounds = result.gameState.round,
                            fidelityScore = result.fidelity.score,
                            statistics = result.gameState.players.map {
                                it.statistics.copy(housesChosen = it.statistics.housesChosen.toMutableMap())
                            }
                        )
                        perOpponent.getOrPut(opponentUuid) { IntArray(3) }.also { counts ->
                            synchronized(counts) {
                                when (result.gameState.winnerIndex) {
                                    null -> counts[2]++
                                    0 -> if (swap) counts[1]++ else counts[0]++
                                    else -> if (swap) counts[0]++ else counts[1]++
                                }
                            }
                        }
                    } catch (e: Exception) {
                        logger.error("Pool rating match failed: {} vs {} seed {}", deckUuid, opponentUuid, seed, e)
                        failed.incrementAndGet()
                    }
                })
            }
            futures.forEach { it.get() }
        } finally {
            executor.shutdown()
        }

        val totalMatches = wins.get() + losses.get() + draws.get()
        val winRate = if (totalMatches == 0) 0.0 else (wins.get() + 0.5 * draws.get()) / totalMatches
        val margin = if (totalMatches == 0) 0.0 else 1.96 * sqrt(winRate * (1 - winRate) / totalMatches)

        val opponentResults = opponents.map { ref ->
            val counts = perOpponent[ref.uuid] ?: IntArray(3)
            val played = counts[0] + counts[1] + counts[2]
            PoolDeckRating.OpponentResult(
                deckUuid = ref.uuid,
                deckName = ref.name,
                wins = counts[0],
                losses = counts[1],
                draws = counts[2],
                matches = played,
                winRate = if (played == 0) 0.0 else (counts[0] + 0.5 * counts[2]) / played
            )
        }.sortedByDescending { it.winRate }

        var bradleyTerry: Double? = null
        var aemberScore: Double? = null
        var percentile: Double? = null
        var anchorBatchUuid: String? = null
        var anchorRatedAt: Instant? = null
        if (anchor != null) {
            // Consistent anchored rating: replay the ladder batch's stored results,
            // add the fresh matches against the rated deck, and refit the whole
            // pool (deck included) with the same methodology as the ladder itself.
            val nameByUuid = (opponents.map { it.uuid to it.name } + (deck.uuid to deck.name)).toMap()
            val combinedPool = (anchor.batch.deckPool.map { it.uuid } + deckUuid).distinct()
            val combinedRefs = combinedPool.map { uuid -> DeckRef(uuid, nameByUuid.getValue(uuid)) }
            val combinedResults = anchor.results + freshResults(anchor.batch.uuid, outcomes)

            val ratings = RatingAggregator.ratings(combinedRefs, combinedResults)
            val newRating = ratings.first { it.deckUuid == deckUuid }
            val poolStrengths = ratings.filter { it.deckUuid != deckUuid }.map { it.bradleyTerry }
            val poolMedian = median(poolStrengths)

            bradleyTerry = newRating.bradleyTerry
            aemberScore = 50.0 + 10.0 * log10(newRating.bradleyTerry / poolMedian)
            val poolScores = poolStrengths.map { 50.0 + 10.0 * log10(it / poolMedian) }
            percentile = (poolScores.count { it < aemberScore } +
                0.5 * poolScores.count { it == aemberScore }) / poolScores.size * 100.0
            anchorBatchUuid = anchor.batch.uuid
            anchorRatedAt = anchor.batch.completedAt
        }

        val rating = PoolDeckRating(
            poolKey = poolKey,
            deckUuid = deckUuid,
            deckName = deck.name,
            ai = ai,
            matchesPerPairing = matchesPerPairing,
            baseSeed = baseSeed,
            wins = wins.get(),
            losses = losses.get(),
            draws = draws.get(),
            matches = totalMatches,
            winRate = winRate,
            winRateMargin = margin,
            opponents = opponentResults,
            failedMatches = failed.get(),
            anchorBatchUuid = anchorBatchUuid,
            anchorRatedAt = anchorRatedAt,
            bradleyTerry = bradleyTerry,
            aemberScore = aemberScore,
            percentile = percentile,
            lowConfidence = totalMatches < 20
        )
        poolRepository.saveRating(poolKey, rating)
        return rating
    }

    fun ratingHistory(poolKey: String, deckUuid: String): List<PoolDeckRating> {
        getPool(poolKey)
        return poolRepository.findRatingHistory(poolKey, deckUuid)
    }

    /**
     * Most recent completed pool ladder (multi-deck batch) whose deck pool
     * covers all opponents and whose results are stored on disk, or null when
     * none exists (rating then reports raw stats only).
     */
    private fun findAnchorBatch(opponentUuids: List<String>): AnchorLadder? {
        val batches = massSimulationRepository.findAll()
            .filter {
                it.batchType == MassSimulationBatchType.MULTI_DECK &&
                    it.status == MassSimulationStatus.COMPLETED
            }
            .sortedByDescending { it.completedAt }
        for (batch in batches) {
            val covers = opponentUuids.all { opp -> batch.deckPool.any { it.uuid == opp } }
            if (!covers) continue
            val results = massSimulationRepository.findResultsByBatchUuid(batch.uuid)
            if (results.isEmpty()) continue
            logger.info("Anchoring pool rating on ladder batch {} ({} decks, {} stored matches)", batch.uuid, batch.deckPool.size, results.size)
            return AnchorLadder(batch, results)
        }
        logger.warn("No stored pool ladder covers the opponents; rating will report raw stats only")
        return null
    }

    private data class AnchorLadder(
        val batch: com.aember.analyzer.domain.model.MassSimulationBatch,
        val results: List<MassSimulationMatchResult>
    )

    private data class MatchOutcome(
        val deckUuidA: String,
        val deckUuidB: String,
        val winnerIndex: Int?,
        val endCause: com.aember.analyzer.domain.engine.model.EndCause?,
        val turns: Int,
        val rounds: Int,
        val fidelityScore: Double,
        val statistics: List<MatchStatistics>
    )

    private fun freshResults(
        batchUuid: String,
        outcomes: Collection<MatchOutcome>
    ): List<MassSimulationMatchResult> {
        return outcomes.mapIndexed { index, outcome ->
            MassSimulationMatchResult(
                id = -(index + 1),
                batchUuid = batchUuid,
                seed = 0,
                winnerIndex = outcome.winnerIndex,
                endCause = outcome.endCause,
                turns = outcome.turns,
                rounds = outcome.rounds,
                fidelityScore = outcome.fidelityScore,
                statistics = outcome.statistics,
                deckUuidA = outcome.deckUuidA,
                deckUuidB = outcome.deckUuidB
            )
        }
    }

    private fun seedPool(key: String): DeckPool {
        val file = Path.of(seedDir, "$key-pool.txt")
        require(file.exists()) {
            "Unknown deck pool: $key (no stored pool and no seed file ${file.toAbsolutePath()})"
        }
        val uuids = Files.readAllLines(file)
            .map { it.trim() }
            .filter { it.isNotEmpty() && !it.startsWith("#") }
        require(uuids.isNotEmpty()) { "Pool seed file ${file.toAbsolutePath()} is empty" }
        val pool = DeckPool(key = key, deckUuids = uuids, updatedAt = Instant.now())
        poolRepository.save(pool)
        logger.info("Seeded pool {} with {} decks from {}", key, uuids.size, file.toAbsolutePath())
        return pool
    }

    private fun median(values: List<Double>): Double {
        if (values.isEmpty()) return 1.0
        val sorted = values.sorted()
        val mid = sorted.size / 2
        return if (sorted.size % 2 == 1) sorted[mid] else (sorted[mid - 1] + sorted[mid]) / 2.0
    }
}
