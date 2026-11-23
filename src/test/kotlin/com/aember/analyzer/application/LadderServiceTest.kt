package com.aember.analyzer.application

import com.aember.analyzer.domain.model.DeckPool
import com.aember.analyzer.domain.model.DeckRef
import com.aember.analyzer.domain.model.MassSimulationBatch
import com.aember.analyzer.domain.model.MassSimulationBatchType
import com.aember.analyzer.domain.model.MultiDeckSimulationRequest
import com.aember.analyzer.domain.model.MassSimulationStatus
import com.aember.analyzer.domain.rating.DeckRating
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.time.Instant

/**
 * Unit tests for the one-call ladder rerun: previous-batch selection
 * (same pool AND same configuration), comparison maths, tier-label path
 * validation, error surfacing, and the single-flight guard.
 */
class LadderServiceTest {

    private val massSimulationService = Mockito.mock(MassSimulationService::class.java)
    private val deckPoolService = Mockito.mock(DeckPoolService::class.java)
    private val service = LadderService(massSimulationService, deckPoolService, ObjectMapper())

    private val poolUuids = listOf("deck-a", "deck-b", "deck-c")

    private fun request(
        pool: String = "laddertest",
        matchesPerPairing: Int = 30,
        tiers: Map<String, Double>? = null,
        tiersFile: String? = null
    ) = LadderRerunRequest(pool = pool, matchesPerPairing = matchesPerPairing, tiers = tiers, tiersFile = tiersFile)

    /** The exact request the service constructs for the default test request. */
    private fun expectedRequest() = MultiDeckSimulationRequest(
        deckUuids = poolUuids,
        matchesPerPairing = 30,
        ai = "tactical",
        seatAlternation = true,
        baseSeed = 12345L,
        concurrency = null
    )

    private fun rating(uuid: String, aemberScore: Double) = DeckRating(
        deckUuid = uuid,
        deckName = uuid,
        wins = 0, losses = 0, draws = 0, matches = 0,
        winRate = 0.5, winRateMargin = 0.0, sos = 0.5,
        bradleyTerry = 1.0, aemberScore = aemberScore,
        percentile = 0.5, lowConfidence = false
    )

    private fun batch(
        uuid: String,
        status: MassSimulationStatus = MassSimulationStatus.COMPLETED,
        matchesPerPairing: Int = 30,
        ai: String = "tactical",
        seatAlternation: Boolean = true,
        completedAt: Instant = Instant.now()
    ) = MassSimulationBatch(
        uuid = uuid,
        status = status,
        batchType = MassSimulationBatchType.MULTI_DECK,
        aiA = ai,
        aiB = ai,
        matchCount = 0,
        deckPool = poolUuids.map { DeckRef(it, it) },
        ai = ai,
        matchesPerPairing = matchesPerPairing,
        seatAlternation = seatAlternation,
        completedAt = completedAt
    )

    @Test
    fun `pool with fewer than two decks fails`() {
        Mockito.`when`(deckPoolService.getPool("laddertest"))
            .thenReturn(DeckPool("laddertest", listOf("deck-a"), Instant.now()))
        assertThatThrownBy { service.rerunLadder(request()) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("at least 2 decks")
    }

    @Test
    fun `invalid pool key fails`() {
        assertThatThrownBy { service.rerunLadder(request(pool = "../evil")) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("Invalid pool key")
    }

    @Test
    fun `concurrent rerun is rejected`() {
        service.rerunInProgress.set(true)
        try {
            assertThatThrownBy { service.rerunLadder(request()) }
                .isInstanceOf(LadderService.LadderInProgressException::class.java)
        } finally {
            service.rerunInProgress.set(false)
        }
    }

    @Test
    fun `no previous ladder yields null comparison`() {
        Mockito.`when`(deckPoolService.getPool("laddertest"))
            .thenReturn(DeckPool("laddertest", poolUuids, Instant.now()))
        Mockito.`when`(
            massSimulationService.runMultiDeckSimulation(expectedRequest(), recordLogs = false)
        ).thenReturn(batch("current", status = MassSimulationStatus.PENDING))
        Mockito.`when`(massSimulationService.getBatch("current")).thenReturn(batch("current"))
        Mockito.`when`(massSimulationService.getRatings("current"))
            .thenReturn(poolUuids.mapIndexed { i, u -> rating(u, 50.0 + i) })
        Mockito.`when`(massSimulationService.getCardFidelity("current")).thenReturn(emptyList())
        Mockito.`when`(massSimulationService.listBatches()).thenReturn(emptyList())

        val result = service.rerunLadder(request())

        assertThat(result.comparison).isNull()
        assertThat(result.tierValidation).isNull()
        assertThat(result.tierValidationError).isNull()
        assertThat(result.timedOut).isFalse()
        assertThat(result.ratings).hasSize(3)
    }

    @Test
    fun `comparison maths on fixed ratings`() {
        Mockito.`when`(deckPoolService.getPool("laddertest"))
            .thenReturn(DeckPool("laddertest", poolUuids, Instant.now()))
        Mockito.`when`(
            massSimulationService.runMultiDeckSimulation(expectedRequest(), recordLogs = false)
        ).thenReturn(batch("current", status = MassSimulationStatus.PENDING))
        Mockito.`when`(massSimulationService.getBatch("current")).thenReturn(batch("current"))
        Mockito.`when`(massSimulationService.getRatings("current"))
            .thenReturn(listOf(rating("deck-a", 75.0), rating("deck-b", 65.0), rating("deck-c", 55.0)))
        Mockito.`when`(massSimulationService.getCardFidelity("current")).thenReturn(emptyList())
        Mockito.`when`(massSimulationService.listBatches()).thenReturn(listOf(batch("previous")))
        Mockito.`when`(massSimulationService.getRatings("previous"))
            .thenReturn(listOf(rating("deck-a", 70.0), rating("deck-b", 60.0), rating("deck-c", 50.0)))

        val result = service.rerunLadder(request())

        val comparison = result.comparison!!
        assertThat(comparison.previousBatchUuid).isEqualTo("previous")
        assertThat(comparison.previousMatchesPerPairing).isEqualTo(30)
        assertThat(comparison.rankCorrelation).isEqualTo(1.0)
        assertThat(comparison.pairwiseAgreement).isEqualTo(1.0)
        assertThat(comparison.biggestMovers.map { it.delta }).containsExactly(5.0, 5.0, 5.0)
    }

    @Test
    fun `comparison detects inverted orderings`() {
        val previous = listOf(rating("deck-a", 70.0), rating("deck-b", 60.0), rating("deck-c", 50.0))
        val current = listOf(rating("deck-a", 50.0), rating("deck-b", 60.0), rating("deck-c", 70.0))
        val comparison = service.compareRatings("prev", previous, current, 30, "tactical")!!
        assertThat(comparison.rankCorrelation).isEqualTo(-1.0)
        assertThat(comparison.pairwiseAgreement).isEqualTo(0.0)
    }

    @Test
    fun `pickPreviousLadder ignores batches with a different configuration`() {
        val batches = listOf(
            batch("smoke-run", matchesPerPairing = 2),
            batch("other-ai", ai = "greedy"),
            batch("no-alternation", seatAlternation = false),
            batch("matching", matchesPerPairing = 30)
        )
        val picked = service.pickPreviousLadder(
            batches, poolUuids.toSet(), excludeUuid = "current",
            matchesPerPairing = 30, ai = "tactical", seatAlternation = true
        )
        assertThat(picked?.uuid).isEqualTo("matching")
    }

    @Test
    fun `pickPreviousLadder returns null when nothing matches`() {
        val picked = service.pickPreviousLadder(
            listOf(batch("smoke-run", matchesPerPairing = 2)),
            poolUuids.toSet(), excludeUuid = "current",
            matchesPerPairing = 30, ai = "tactical", seatAlternation = true
        )
        assertThat(picked).isNull()
    }

    @Test
    fun `tiersFile outside docs slash deck-pool is rejected`() {
        assertThatThrownBy { service.resolveTiers(request(tiersFile = "/etc/passwd")) }
            .isInstanceOf(IllegalArgumentException::class.java)
        assertThatThrownBy { service.resolveTiers(request(tiersFile = "other/dir/tiers.json")) }
            .isInstanceOf(IllegalArgumentException::class.java)
        assertThatThrownBy { service.resolveTiers(request(tiersFile = "docs/deck-pool/../../etc/passwd")) }
            .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `tier validation errors are surfaced not swallowed`() {
        Mockito.`when`(deckPoolService.getPool("laddertest"))
            .thenReturn(DeckPool("laddertest", poolUuids, Instant.now()))
        Mockito.`when`(
            massSimulationService.runMultiDeckSimulation(expectedRequest(), recordLogs = false)
        ).thenReturn(batch("current", status = MassSimulationStatus.PENDING))
        Mockito.`when`(massSimulationService.getBatch("current")).thenReturn(batch("current"))
        Mockito.`when`(massSimulationService.getRatings("current"))
            .thenReturn(poolUuids.mapIndexed { i, u -> rating(u, 50.0 + i) })
        Mockito.`when`(massSimulationService.getCardFidelity("current")).thenReturn(emptyList())
        Mockito.`when`(massSimulationService.listBatches()).thenReturn(emptyList())
        // Only two of three decks labelled: validateTiers requires >= 3.
        Mockito.`when`(
            massSimulationService.validateTiers("current", mapOf("deck-a" to 6.0, "deck-b" to 3.0))
        ).thenThrow(IllegalArgumentException("Need at least 3 rated decks with a tier to validate"))

        val result = service.rerunLadder(request(tiers = mapOf("deck-a" to 6.0, "deck-b" to 3.0)))

        assertThat(result.tierValidation).isNull()
        assertThat(result.tierValidationError).contains("at least 3")
    }
}
