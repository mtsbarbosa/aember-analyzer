package com.aember.analyzer.simulation

import com.aember.analyzer.domain.engine.model.EndCause
import com.aember.analyzer.domain.engine.model.LogEntry
import com.aember.analyzer.domain.engine.model.MatchStatistics
import com.aember.analyzer.domain.model.MassSimulationBatch
import com.aember.analyzer.domain.model.MassSimulationMatchLog
import com.aember.analyzer.domain.model.MassSimulationMatchResult
import com.aember.analyzer.domain.model.MassSimulationStatus
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
