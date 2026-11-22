package com.aember.analyzer.adapter.rest

import com.aember.analyzer.application.DeckPoolService
import com.aember.analyzer.domain.model.DeckPool
import com.aember.analyzer.domain.rating.PoolDeckRating
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * REST adapter for named deck pools.
 *
 * - `GET    /api/pools/{key}`                        — pool members
 * - `POST   /api/pools/{key}/decks`                  — add an imported deck to the pool
 * - `DELETE /api/pools/{key}/decks/{deckUuid}`       — remove a deck from the pool
 * - `POST   /api/pools/{key}/ratings/{deckUuid}`     — rate a deck against the pool (synchronous)
 * - `GET    /api/pools/{key}/ratings/{deckUuid}`     — history of stored ratings for a deck
 */
@RestController
@RequestMapping("/api/pools")
class DeckPoolController(
    private val deckPoolService: DeckPoolService
) {

    @GetMapping("/{key}")
    fun getPool(@PathVariable key: String): ResponseEntity<DeckPoolDto> {
        return ResponseEntity.ok(deckPoolService.getPool(key).toDto())
    }

    @PostMapping("/{key}/decks")
    fun addToPool(
        @PathVariable key: String,
        @RequestBody request: PoolAddDeckRequest
    ): ResponseEntity<DeckPoolDto> {
        return ResponseEntity.ok(deckPoolService.addToPool(key, request.deckUuid).toDto())
    }

    @DeleteMapping("/{key}/decks/{deckUuid}")
    fun removeFromPool(
        @PathVariable key: String,
        @PathVariable deckUuid: String
    ): ResponseEntity<DeckPoolDto> {
        return ResponseEntity.ok(deckPoolService.removeFromPool(key, deckUuid).toDto())
    }

    @PostMapping("/{key}/ratings/{deckUuid}")
    fun rateDeck(
        @PathVariable key: String,
        @PathVariable deckUuid: String,
        @RequestParam(defaultValue = "10") matchesPerPairing: Int,
        @RequestParam(defaultValue = "0") baseSeed: Long,
        @RequestParam(defaultValue = "tactical") ai: String,
        @RequestParam(required = false) concurrency: Int?
    ): ResponseEntity<PoolDeckRatingDto> {
        val rating = deckPoolService.rateDeckAgainstPool(
            poolKey = key,
            deckUuid = deckUuid,
            matchesPerPairing = matchesPerPairing,
            baseSeed = baseSeed,
            ai = ai,
            concurrency = concurrency
        )
        return ResponseEntity.ok(rating.toDto())
    }

    @GetMapping("/{key}/ratings/{deckUuid}")
    fun ratingHistory(
        @PathVariable key: String,
        @PathVariable deckUuid: String
    ): ResponseEntity<List<PoolDeckRatingDto>> {
        return ResponseEntity.ok(deckPoolService.ratingHistory(key, deckUuid).map { it.toDto() })
    }
}

data class PoolAddDeckRequest(val deckUuid: String)

data class DeckPoolDto(
    val key: String,
    val deckCount: Int,
    val deckUuids: List<String>,
    val updatedAt: String?
)

private fun DeckPool.toDto() = DeckPoolDto(
    key = key,
    deckCount = deckUuids.size,
    deckUuids = deckUuids,
    updatedAt = updatedAt?.toString()
)

data class PoolDeckRatingDto(
    val poolKey: String,
    val deckUuid: String,
    val deckName: String,
    val ai: String,
    val matchesPerPairing: Int,
    val baseSeed: Long,
    val wins: Int,
    val losses: Int,
    val draws: Int,
    val matches: Int,
    val winRate: Double,
    val winRateMargin: Double,
    val opponents: List<OpponentResultDto>,
    val failedMatches: Int,
    val anchorBatchUuid: String?,
    val anchorRatedAt: String?,
    val bradleyTerry: Double?,
    val aemberScore: Double?,
    val percentile: Double?,
    val lowConfidence: Boolean
)

data class OpponentResultDto(
    val deckUuid: String,
    val deckName: String,
    val wins: Int,
    val losses: Int,
    val draws: Int,
    val matches: Int,
    val winRate: Double
)

private fun PoolDeckRating.toDto() = PoolDeckRatingDto(
    poolKey = poolKey,
    deckUuid = deckUuid,
    deckName = deckName,
    ai = ai,
    matchesPerPairing = matchesPerPairing,
    baseSeed = baseSeed,
    wins = wins,
    losses = losses,
    draws = draws,
    matches = matches,
    winRate = winRate,
    winRateMargin = winRateMargin,
    opponents = opponents.map {
        OpponentResultDto(
            deckUuid = it.deckUuid,
            deckName = it.deckName,
            wins = it.wins,
            losses = it.losses,
            draws = it.draws,
            matches = it.matches,
            winRate = it.winRate
        )
    },
    failedMatches = failedMatches,
    anchorBatchUuid = anchorBatchUuid,
    anchorRatedAt = anchorRatedAt?.toString(),
    bradleyTerry = bradleyTerry,
    aemberScore = aemberScore,
    percentile = percentile,
    lowConfidence = lowConfidence
)
