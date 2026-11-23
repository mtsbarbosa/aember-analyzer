package com.aember.analyzer.simulation

import com.aember.analyzer.domain.model.DeckPool
import com.aember.analyzer.domain.rating.PoolDeckRating
import com.aember.analyzer.infrastructure.persistence.FileSystemDeckPoolRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Files
import java.nio.file.Path
import java.time.Instant

class FileSystemDeckPoolRepositoryTest {

    @TempDir
    lateinit var tempDir: Path

    private val objectMapper = ObjectMapper()
        .registerModule(KotlinModule.Builder().build())
        .registerModule(JavaTimeModule())

    private fun repository() = FileSystemDeckPoolRepository(tempDir.toString(), objectMapper)

    private fun rating(deckUuid: String) = PoolDeckRating(
        poolKey = "cota",
        deckUuid = deckUuid,
        deckName = "Deck $deckUuid",
        ai = "tactical",
        matchesPerPairing = 10,
        baseSeed = 0,
        wins = 5,
        losses = 4,
        draws = 1,
        matches = 10,
        winRate = 0.55,
        winRateMargin = 0.3,
        opponents = emptyList(),
        failedMatches = 0,
        anchorBatchUuid = "batch-1",
        anchorRatedAt = Instant.now(),
        bradleyTerry = 1.05,
        aemberScore = 50.2,
        percentile = 60.0,
        lowConfidence = false
    )

    @Test
    fun `should save and find a pool`() {
        val repo = repository()
        val pool = DeckPool(key = "cota", deckUuids = listOf("uuid-1", "uuid-2"), updatedAt = Instant.now())

        repo.save(pool)

        val found = repo.findByKey("cota")
        assertThat(found).isNotNull
        assertThat(found!!.key).isEqualTo("cota")
        assertThat(found.deckUuids).containsExactly("uuid-1", "uuid-2")
        assertThat(found.updatedAt).isEqualTo(pool.updatedAt)
        assertThat(repo.findByKey("missing")).isNull()
    }

    @Test
    fun `should save and find rating history filtered by deck`() {
        val repo = repository()
        repo.saveRating("cota", rating("deck-a"))
        repo.saveRating("cota", rating("deck-b"))
        repo.saveRating("other", rating("deck-a"))

        val allCota = repo.findRatingHistory("cota")
        assertThat(allCota).hasSize(2)

        val onlyDeckA = repo.findRatingHistory("cota", "deck-a")
        assertThat(onlyDeckA).hasSize(1)
        assertThat(onlyDeckA[0].deckUuid).isEqualTo("deck-a")
        assertThat(onlyDeckA[0].aemberScore).isEqualTo(50.2)
        assertThat(onlyDeckA[0].anchorBatchUuid).isEqualTo("batch-1")

        assertThat(repo.findRatingHistory("cota", "deck-b")).hasSize(1)
        assertThat(repo.findRatingHistory("cota", "deck-c")).isEmpty()
        assertThat(repo.findRatingHistory("missing", "deck-a")).isEmpty()
    }

    @Test
    fun `history file names are unique per call`() {
        val repo = repository()
        repo.saveRating("cota", rating("deck-a"))
        repo.saveRating("cota", rating("deck-a"))

        val dir = tempDir.resolve("pools").resolve("cota").resolve("ratings")
        val files = Files.list(dir).use { it.toList() }

        // Timestamps have second precision; rapid saves must not overwrite each other.
        assertThat(files).hasSize(2)
        val history = repo.findRatingHistory("cota", "deck-a")
        assertThat(history).hasSize(2)
        assertThat(history.map { it.wins }).containsOnly(5)
    }

    @Test
    fun `delete removes pool file and rating snapshots`() {
        val repo = repository()
        repo.save(DeckPool(key = "smoke-x", deckUuids = listOf("a"), updatedAt = Instant.now()))
        repo.saveRating("smoke-x", rating("a"))

        assertThat(repo.delete("smoke-x")).isTrue()
        assertThat(repo.findByKey("smoke-x")).isNull()
        assertThat(repo.findRatingHistory("smoke-x")).isEmpty()
        assertThat(repo.delete("smoke-x")).isFalse()
    }

    @Test
    fun `delete rejects path traversal keys`() {
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException::class.java) {
            repository().delete("../etc")
        }
    }
}
