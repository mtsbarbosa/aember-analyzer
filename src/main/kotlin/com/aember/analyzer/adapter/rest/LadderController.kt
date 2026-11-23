package com.aember.analyzer.adapter.rest

import com.aember.analyzer.application.LadderComparison
import com.aember.analyzer.application.LadderMover
import com.aember.analyzer.application.LadderRerunRequest
import com.aember.analyzer.application.LadderService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * One-call ladder rerun: starts a round-robin batch over a deck pool, waits for
 * completion, and returns ratings, comparison against the previous ladder and
 * tier validation in a single response.
 */
@RestController
@RequestMapping("/api/ladder")
class LadderController(
    private val ladderService: LadderService
) {

    /**
     * Synchronous (blocking) rerun; expect the response after a few minutes for a
     * full pool. All parameters are optional; see [LadderRerunRequest] defaults.
     */
    @PostMapping("/rerun")
    fun rerun(@RequestBody request: LadderRerunRequest?): ResponseEntity<LadderRerunResponseDto> {
        val result = ladderService.rerunLadder(request ?: LadderRerunRequest())
        return ResponseEntity.ok(result.toDto())
    }
}

data class LadderMoverDto(
    val deckUuid: String,
    val deckName: String,
    val aemberBefore: Double,
    val aemberAfter: Double,
    val delta: Double
)

data class LadderComparisonDto(
    val previousBatchUuid: String,
    val previousMatchesPerPairing: Int?,
    val previousAi: String?,
    val rankCorrelation: Double?,
    val pairwiseAgreement: Double?,
    val biggestMovers: List<LadderMoverDto>
)

data class LadderRerunResponseDto(
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
    val comparison: LadderComparisonDto?,
    val tierValidation: com.aember.analyzer.domain.rating.TierValidation?,
    val tierValidationError: String?,
    val ratings: List<DeckRatingDto>
)

fun LadderMover.toDto() = LadderMoverDto(deckUuid, deckName, aemberBefore, aemberAfter, delta)

fun LadderComparison.toDto() = LadderComparisonDto(
    previousBatchUuid = previousBatchUuid,
    previousMatchesPerPairing = previousMatchesPerPairing,
    previousAi = previousAi,
    rankCorrelation = rankCorrelation,
    pairwiseAgreement = pairwiseAgreement,
    biggestMovers = biggestMovers.map { it.toDto() }
)

fun com.aember.analyzer.application.LadderRerunResult.toDto() = LadderRerunResponseDto(
    pool = pool,
    batchUuid = batchUuid,
    status = status,
    timedOut = timedOut,
    matchesPerPairing = matchesPerPairing,
    baseSeed = baseSeed,
    ai = ai,
    completedCount = completedCount,
    failedCount = failedCount,
    draws = draws,
    avgRounds = avgRounds,
    declaredFidelity = declaredFidelity,
    verifiedFidelity = verifiedFidelity,
    comparison = comparison?.toDto(),
    tierValidation = tierValidation,
    tierValidationError = tierValidationError,
    ratings = ratings.map { it.toDto() }
)
