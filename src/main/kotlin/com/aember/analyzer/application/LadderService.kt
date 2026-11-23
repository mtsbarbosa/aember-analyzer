package com.aember.analyzer.application

import com.aember.analyzer.domain.model.MassSimulationBatch
import com.aember.analyzer.domain.model.MassSimulationBatchType
import com.aember.analyzer.domain.model.MassSimulationStatus
import com.aember.analyzer.domain.model.MultiDeckSimulationRequest
import com.aember.analyzer.domain.rating.DeckRating
import com.aember.analyzer.domain.rating.TierValidator
import com.aember.analyzer.domain.rating.TierValidation
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.nio.file.Path
import java.time.Instant
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.io.path.exists

/**
 * One-call ladder rerun: starts a round-robin multi-deck batch over a named pool,
 * waits for it to complete, and returns the ratings together with a comparison
 * against the previous completed ladder over the same pool (same configuration)
 * and (optionally) a tier validation against external labels.
 */
@Service
class LadderService(
    private val massSimulationService: MassSimulationService,
    private val deckPoolService: DeckPoolService,
    private val objectMapper: ObjectMapper
) {

    private val logger = LoggerFactory.getLogger(LadderService::class.java)

    /** Guards a rerun: the endpoint blocks a worker thread for minutes, so only one may run. */
    internal val rerunInProgress = AtomicBoolean(false)

    class LadderInProgressException(message: String) : RuntimeException(message)

    fun rerunLadder(request: LadderRerunRequest): LadderRerunResult {
        validatePoolKey(request.pool)
        if (!rerunInProgress.compareAndSet(false, true)) {
            throw LadderInProgressException("A ladder rerun is already in progress; wait for it to finish")
        }
        try {
            return executeRerun(request)
        } finally {
            rerunInProgress.set(false)
        }
    }

    private fun executeRerun(request: LadderRerunRequest): LadderRerunResult {
        val pool = deckPoolService.getPool(request.pool)
        if (pool.deckUuids.size < 2) {
            throw IllegalArgumentException("Pool '${request.pool}' needs at least 2 decks to run a ladder")
        }

        val batch = massSimulationService.runMultiDeckSimulation(
            MultiDeckSimulationRequest(
                deckUuids = pool.deckUuids,
                matchesPerPairing = request.matchesPerPairing,
                ai = request.ai,
                seatAlternation = request.seatAlternation,
                baseSeed = request.baseSeed,
                concurrency = request.concurrency
            ),
            recordLogs = false
        )
        logger.info("Ladder rerun started for pool '{}': batch {}", request.pool, batch.uuid)

        // Block until the batch settles (the runner is async, ~4 min for the full pool).
        val deadline = System.currentTimeMillis() + request.timeoutSeconds * 1000
        var state = massSimulationService.getBatch(batch.uuid)
        while ((state.status == MassSimulationStatus.PENDING || state.status == MassSimulationStatus.RUNNING) &&
            System.currentTimeMillis() < deadline
        ) {
            Thread.sleep(POLL_INTERVAL_MS)
            state = massSimulationService.getBatch(batch.uuid)
        }
        val timedOut = state.status == MassSimulationStatus.PENDING || state.status == MassSimulationStatus.RUNNING

        // A timed-out batch is still running: report the status and stop before
        // reading half-written results.
        if (timedOut) {
            return LadderRerunResult(
                pool = request.pool,
                batchUuid = batch.uuid,
                status = state.status.name,
                timedOut = true,
                matchesPerPairing = request.matchesPerPairing,
                baseSeed = request.baseSeed,
                ai = request.ai,
                completedCount = state.completedCount,
                failedCount = state.failedCount,
                draws = state.draws,
                avgRounds = state.avgRounds,
                declaredFidelity = state.avgFidelity,
                verifiedFidelity = null,
                comparison = null,
                tierValidation = null,
                tierValidationError = null,
                ratings = emptyList()
            )
        }

        val ratings = massSimulationService.getRatings(batch.uuid)
        val fidelityStats = massSimulationService.getCardFidelity(batch.uuid)
        val totalPlays = fidelityStats.sumOf { it.plays }
        val verifiedFidelity =
            if (totalPlays == 0) null
            else fidelityStats.sumOf { it.verifiedAvgScore * it.plays } / totalPlays

        val previous = pickPreviousLadder(
            massSimulationService.listBatches(),
            pool.deckUuids.toSet(),
            batch.uuid,
            request
        )
        val comparison = previous?.let { prev ->
            compareRatings(prev.uuid, massSimulationService.getRatings(prev.uuid), ratings, prev)
        }

        var tierValidation: TierValidation? = null
        var tierValidationError: String? = null
        try {
            resolveTiers(request)?.let { tiers -> tierValidation = massSimulationService.validateTiers(batch.uuid, tiers) }
        } catch (e: Exception) {
            // Surface the reason (malformed file, too few labelled decks) instead of
            // silently reporting "no labels".
            tierValidationError = e.message ?: e::class.simpleName
        }

        return LadderRerunResult(
            pool = request.pool,
            batchUuid = batch.uuid,
            status = state.status.name,
            timedOut = false,
            matchesPerPairing = request.matchesPerPairing,
            baseSeed = request.baseSeed,
            ai = request.ai,
            completedCount = state.completedCount,
            failedCount = state.failedCount,
            draws = state.draws,
            avgRounds = state.avgRounds,
            declaredFidelity = state.avgFidelity,
            verifiedFidelity = verifiedFidelity,
            comparison = comparison,
            tierValidation = tierValidation,
            tierValidationError = tierValidationError,
            ratings = ratings
        )
    }

    private fun validatePoolKey(pool: String) {
        if (!POOL_KEY_PATTERN.matches(pool)) {
            throw IllegalArgumentException("Invalid pool key: '$pool'")
        }
    }

    /**
     * Latest completed multi-deck ladder over the exact same pool **and the same
     * configuration** (matches per pairing, AI, seat alternation), excluding the
     * current batch. Matching the configuration keeps smoke runs from becoming
     * the baseline of a full ladder.
     */
    internal fun pickPreviousLadder(
        batches: List<MassSimulationBatch>,
        poolUuids: Set<String>,
        excludeUuid: String,
        request: LadderRerunRequest
    ): MassSimulationBatch? = pickPreviousLadder(
        batches, poolUuids, excludeUuid,
        request.matchesPerPairing, request.ai, request.seatAlternation
    )

    internal fun pickPreviousLadder(
        batches: List<MassSimulationBatch>,
        poolUuids: Set<String>,
        excludeUuid: String,
        matchesPerPairing: Int,
        ai: String,
        seatAlternation: Boolean
    ): MassSimulationBatch? =
        batches.filter {
            it.uuid != excludeUuid &&
                it.status == MassSimulationStatus.COMPLETED &&
                it.batchType == MassSimulationBatchType.MULTI_DECK &&
                it.deckPool.map { ref -> ref.uuid }.toSet() == poolUuids &&
                it.matchesPerPairing == matchesPerPairing &&
                it.ai == ai &&
                it.seatAlternation == seatAlternation
        }.maxByOrNull { it.completedAt ?: Instant.MIN }

    private fun compareRatings(
        previousBatchUuid: String,
        previousRatings: List<DeckRating>,
        currentRatings: List<DeckRating>,
        previousBatch: MassSimulationBatch
    ): LadderComparison? = compareRatings(previousBatchUuid, previousRatings, currentRatings, previousBatch.matchesPerPairing, previousBatch.ai)

    internal fun compareRatings(
        previousBatchUuid: String,
        previousRatings: List<DeckRating>,
        currentRatings: List<DeckRating>,
        previousMatchesPerPairing: Int?,
        previousAi: String?
    ): LadderComparison? {
        val previousByUuid = previousRatings.associateBy { it.deckUuid }
        val aligned = currentRatings.mapNotNull { rating ->
            previousByUuid[rating.deckUuid]?.let { rating to it }
        }
        if (aligned.size < 2) return null

        val currentScores = aligned.map { it.first.aemberScore }
        val previousScores = aligned.map { it.second.aemberScore }
        val rankCorrelation = TierValidator.spearman(currentScores, previousScores)
        var agree = 0.0
        var total = 0
        for (i in aligned.indices) for (j in i + 1 until aligned.size) {
            val cs = currentScores[i].compareTo(currentScores[j])
            val ps = previousScores[i].compareTo(previousScores[j])
            if (cs == ps) agree += 1.0 else if (cs == 0) agree += 0.5
            total++
        }
        val pairwiseAgreement = if (total == 0) 0.0 else agree / total

        val movers = aligned
            .map { (current, previous) ->
                LadderMover(
                    deckUuid = current.deckUuid,
                    deckName = current.deckName,
                    aemberBefore = previous.aemberScore,
                    aemberAfter = current.aemberScore,
                    delta = current.aemberScore - previous.aemberScore
                )
            }
            .sortedByDescending { kotlin.math.abs(it.delta) }
            .take(6)
        return LadderComparison(
            previousBatchUuid = previousBatchUuid,
            previousMatchesPerPairing = previousMatchesPerPairing,
            previousAi = previousAi,
            rankCorrelation = rankCorrelation,
            pairwiseAgreement = pairwiseAgreement,
            biggestMovers = movers
        )
    }

    /** Tier labels: explicit in the request, else `tiersFile`, else `docs/deck-pool/{pool}-tiers.json` if present. */
    internal fun resolveTiers(request: LadderRerunRequest): Map<String, Double>? {
        request.tiers?.let { return it }
        val defaultPath = "${LadderService.DEFAULT_TIERS_DIR}/${request.pool}-tiers.json"
        val path: Path = Path.of(request.tiersFile ?: defaultPath)
        if (request.tiersFile != null) {
            // Request-supplied paths stay inside the tiers directory.
            if (path.isAbsolute()) throw IllegalArgumentException("tiersFile must be a relative path")
            val normalized = path.normalize().toString()
            if (!normalized.startsWith("${LadderService.DEFAULT_TIERS_DIR}/") || normalized == LadderService.DEFAULT_TIERS_DIR) {
                throw IllegalArgumentException("tiersFile must live under ${LadderService.DEFAULT_TIERS_DIR}/")
            }
        }
        if (!path.exists()) return null
        @Suppress("UNCHECKED_CAST")
        val raw = objectMapper.readValue(path.toFile(), Map::class.java) as Map<String, Any>
        return raw.mapValues { (_, v) -> when (v) {
            is Number -> v.toDouble()
            else -> throw IllegalArgumentException("Tier labels must be numbers: $path")
        } }
    }

    companion object {
        /** Default location of the external tier labels relative to the working directory. */
        const val DEFAULT_TIERS_DIR = "docs/deck-pool"

        /** Pool keys become file names; keep them to plain identifiers. */
        private val POOL_KEY_PATTERN = Regex("[a-zA-Z0-9][a-zA-Z0-9_-]*")

        private const val POLL_INTERVAL_MS = 2_000L
    }
}

data class LadderRerunRequest(
    val pool: String = "cota",
    val matchesPerPairing: Int = 30,
    val baseSeed: Long = 12345L,
    val ai: String = "tactical",
    val seatAlternation: Boolean = true,
    val concurrency: Int? = null,
    /** External tier labels (higher = better, e.g. S+=6 … Z=0); overrides tiersFile. */
    val tiers: Map<String, Double>? = null,
    /** Path to a `{deckUuid: tier}` JSON file under `docs/deck-pool/`; defaults to `{pool}-tiers.json` there. */
    val tiersFile: String? = null,
    /** How long to wait for the batch before returning a partial result. */
    val timeoutSeconds: Long = 1800
)

data class LadderComparison(
    val previousBatchUuid: String,
    /** Configuration of the previous run, so a mismatched baseline is visible to consumers. */
    val previousMatchesPerPairing: Int?,
    val previousAi: String?,
    val rankCorrelation: Double?,
    val pairwiseAgreement: Double?,
    val biggestMovers: List<LadderMover>
)

data class LadderMover(
    val deckUuid: String,
    val deckName: String,
    val aemberBefore: Double,
    val aemberAfter: Double,
    val delta: Double
)

data class LadderRerunResult(
    val pool: String,
    val batchUuid: String,
    val status: String,
    val timedOut: Boolean,
    val matchesPerPairing: Int,
    val baseSeed: Long,
    val ai: String,
    val completedCount: Int,
    val failedCount: Int,
    val draws: Int,
    val avgRounds: Double?,
    val declaredFidelity: Double?,
    val verifiedFidelity: Double?,
    val comparison: LadderComparison?,
    val tierValidation: TierValidation?,
    val tierValidationError: String?,
    val ratings: List<DeckRating>
)
