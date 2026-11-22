package com.aember.analyzer.simulation

import com.aember.analyzer.domain.engine.model.EndCause
import com.aember.analyzer.domain.engine.model.LogEntry
import com.aember.analyzer.domain.engine.model.MatchStatistics
import com.aember.analyzer.domain.model.DeckRef
import com.aember.analyzer.domain.model.MassSimulationBatch
import com.aember.analyzer.domain.model.MassSimulationBatchType
import com.aember.analyzer.domain.model.MassSimulationMatchLog
import com.aember.analyzer.domain.model.MassSimulationMatchResult
import com.aember.analyzer.domain.model.MassSimulationStatus
import com.aember.analyzer.domain.rating.DeckAggregate
import com.aember.analyzer.domain.rating.DeckRating
import com.aember.analyzer.infrastructure.persistence.FileSystemMassSimulationRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path
import java.time.Instant

class FileSystemMassSimulationRepositoryTest {

    @TempDir
    lateinit var tempDir: Path

    private val objectMapper = ObjectMapper().findAndRegisterModules()

    private fun repository() = FileSystemMassSimulationRepository(
        storagePath = tempDir.toString(),
        objectMapper = objectMapper
    )

    @Test
    fun `should save and find batch`() {
        val repo = repository()
        val batch = MassSimulationBatch(
            uuid = "batch-1",
            status = MassSimulationStatus.COMPLETED,
            deckUuidA = "deck-a",
            deckNameA = "Deck A",
            deckUuidB = "deck-b",
            deckNameB = "Deck B",
            aiA = "tactical",
            aiB = "tactical",
            matchCount = 10,
            winsA = 6,
            winsB = 3,
            draws = 1
        )

        repo.save(batch)
        val found = repo.findByUuid("batch-1")

        assertThat(found).isNotNull
        assertThat(found!!.uuid).isEqualTo("batch-1")
        assertThat(found.status).isEqualTo(MassSimulationStatus.COMPLETED)
        assertThat(found.deckNameA).isEqualTo("Deck A")
        assertThat(found.winsA).isEqualTo(6)
        assertThat(found.draws).isEqualTo(1)
    }

    @Test
    fun `should list batches sorted by createdAt descending`() {
        val repo = repository()
        repo.save(pendingBatch("batch-1", createdAt = Instant.parse("2026-11-01T00:00:00Z")))
        repo.save(pendingBatch("batch-2", createdAt = Instant.parse("2026-11-03T00:00:00Z")))
        repo.save(pendingBatch("batch-3", createdAt = Instant.parse("2026-11-02T00:00:00Z")))

        val batches = repo.findAll()

        assertThat(batches).hasSize(3)
        assertThat(batches.map { it.uuid }).containsExactly("batch-2", "batch-3", "batch-1")
    }

    @Test
    fun `should save and find match result`() {
        val repo = repository()
        repo.save(pendingBatch("batch-1"))

        val result = MassSimulationMatchResult(
            id = 1,
            batchUuid = "batch-1",
            seed = 42,
            winnerIndex = 0,
            endCause = EndCause.KEYS,
            turns = 24,
            rounds = 12,
            fidelityScore = 76.5,
            statistics = listOf(
                MatchStatistics(
                    reaps = 5,
                    fights = 3,
                    cardsPlayed = 12,
                    housesChosen = mutableMapOf("Brobnar" to 2, "Dis" to 3)
                ),
                MatchStatistics()
            )
        )

        repo.saveResult(result)
        val results = repo.findResultsByBatchUuid("batch-1")

        assertThat(results).hasSize(1)
        assertThat(results[0].id).isEqualTo(1)
        assertThat(results[0].winnerIndex).isEqualTo(0)
        assertThat(results[0].statistics[0].reaps).isEqualTo(5)
        assertThat(results[0].statistics[0].housesChosen).containsEntry("Dis", 3)
    }

    @Test
    fun `should save and find match log`() {
        val repo = repository()
        repo.save(pendingBatch("batch-1"))
        repo.saveResult(
            MassSimulationMatchResult(
                id = 1,
                batchUuid = "batch-1",
                seed = 42,
                winnerIndex = 0,
                endCause = EndCause.KEYS,
                turns = 10,
                rounds = 5,
                fidelityScore = 80.0,
                statistics = listOf(MatchStatistics())
            )
        )

        val log = MassSimulationMatchLog(
            matchResultId = 1,
            batchUuid = "batch-1",
            seed = 42,
            logEntries = listOf(LogEntry(1, 1, 0, "test message"))
        )

        repo.saveLog(log)
        val found = repo.findLog("batch-1", 1)

        assertThat(found).isNotNull
        assertThat(found!!.logEntries).hasSize(1)
        assertThat(found.logEntries[0].message).isEqualTo("test message")
    }

    @Test
    fun `should roundtrip multi-deck batch with pool and aggregates`() {
        val repo = repository()
        val batch = MassSimulationBatch(
            uuid = "multi-1",
            status = MassSimulationStatus.COMPLETED,
            batchType = MassSimulationBatchType.MULTI_DECK,
            aiA = "tactical",
            aiB = "tactical",
            matchCount = 2,
            deckPool = listOf(DeckRef("deck-a", "Deck A"), DeckRef("deck-b", "Deck B")),
            ai = "tactical",
            matchesPerPairing = 2,
            seatAlternation = true,
            completedCount = 2,
            failedCount = 0,
            deckAggregates = listOf(
                DeckAggregate("deck-a", "Deck A", wins = 1, losses = 0, draws = 1, matches = 2, winRate = 0.75)
            )
        )

        repo.save(batch)
        val found = repo.findByUuid("multi-1")

        assertThat(found).isNotNull
        assertThat(found!!.batchType).isEqualTo(MassSimulationBatchType.MULTI_DECK)
        assertThat(found.deckPool).hasSize(2)
        assertThat(found.deckPool[0].name).isEqualTo("Deck A")
        assertThat(found.matchesPerPairing).isEqualTo(2)
        assertThat(found.seatAlternation).isTrue()
        assertThat(found.completedCount).isEqualTo(2)
        assertThat(found.deckAggregates).hasSize(1)
        assertThat(found.deckAggregates[0].winRate).isEqualTo(0.75)
    }

    @Test
    fun `should save and find batch ratings`() {
        val repo = repository()
        val ratings = listOf(
            DeckRating(
                deckUuid = "deck-a", deckName = "Deck A", wins = 6, losses = 3, draws = 1, matches = 10,
                winRate = 0.65, winRateMargin = 0.3, sos = 0.5, bradleyTerry = 1.4,
                aemberScore = 52.3, percentile = 75.0, lowConfidence = false
            )
        )

        repo.saveRatings("batch-1", ratings)
        val found = repo.findRatings("batch-1")

        assertThat(found).isNotNull
        assertThat(found).hasSize(1)
        assertThat(found!![0].deckUuid).isEqualTo("deck-a")
        assertThat(found[0].aemberScore).isEqualTo(52.3)
        assertThat(found[0].bradleyTerry).isEqualTo(1.4)
        assertThat(repo.findRatings("missing-batch")).isNull()
    }

    @Test
    fun `should save and find match result with seat uuids`() {        val repo = repository()
        repo.save(pendingBatch("batch-1"))

        val result = MassSimulationMatchResult(
            id = 1,
            batchUuid = "batch-1",
            seed = 42,
            winnerIndex = 1,
            endCause = EndCause.KEYS,
            turns = 20,
            rounds = 10,
            fidelityScore = 88.0,
            statistics = listOf(MatchStatistics(), MatchStatistics()),
            deckUuidA = "deck-b",
            deckUuidB = "deck-a"
        )

        repo.saveResult(result)
        val results = repo.findResultsByBatchUuid("batch-1")

        assertThat(results).hasSize(1)
        assertThat(results[0].deckUuidA).isEqualTo("deck-b")
        assertThat(results[0].deckUuidB).isEqualTo("deck-a")
        assertThat(results[0].winnerIndex).isEqualTo(1)
    }

    private fun pendingBatch(uuid: String, createdAt: Instant = Instant.now()): MassSimulationBatch {
        return MassSimulationBatch(
            uuid = uuid,
            status = MassSimulationStatus.PENDING,
            deckUuidA = "deck-a",
            deckNameA = "Deck A",
            deckUuidB = "deck-b",
            deckNameB = "Deck B",
            aiA = "tactical",
            aiB = "tactical",
            matchCount = 1,
            createdAt = createdAt
        )
    }
}
