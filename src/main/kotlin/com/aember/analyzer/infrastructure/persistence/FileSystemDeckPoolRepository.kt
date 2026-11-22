package com.aember.analyzer.infrastructure.persistence

import com.aember.analyzer.domain.model.DeckPool
import com.aember.analyzer.domain.rating.PoolDeckRating
import com.aember.analyzer.domain.ports.DeckPoolRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.file.Files
import java.nio.file.Path
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.io.path.createDirectories
import kotlin.io.path.exists
import kotlin.io.path.isRegularFile
import kotlin.io.path.name
import kotlin.io.path.readText

/**
 * File-system adapter for deck pools and pool-rating results.
 *
 * Layout:
 *   {storagePath}/pools/{key}.json                     — pool membership
 *   {storagePath}/pools/{key}/ratings/{deckUuid}-{yyyyMMdd-HHmmss}.json — rating snapshots
 */
@Component
class FileSystemDeckPoolRepository(
    @Value("\${aember.simulation.storage-path:.simulations}") private val storagePath: String,
    private val objectMapper: ObjectMapper
) : DeckPoolRepository {

    private val basePath: Path by lazy {
        Path.of(storagePath).toAbsolutePath().normalize().resolve("pools")
    }
    private val ratingTimestampFormatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss")

    override fun save(pool: DeckPool) {
        val file = poolFile(pool.key)
        file.parent.createDirectories()
        writeJsonAtomic(file, objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(pool))
    }

    override fun findByKey(key: String): DeckPool? {
        val file = poolFile(key)
        return if (file.exists()) objectMapper.readValue(file.readText()) else null
    }

    override fun saveRating(key: String, rating: PoolDeckRating) {
        val dir = ratingsDir(key)
        dir.createDirectories()
        val timestamp = LocalDateTime.now().format(ratingTimestampFormatter)
        var file = dir.resolve("${rating.deckUuid}-$timestamp.json")
        // Timestamps have second precision; ensure rapid saves do not overwrite.
        var suffix = 1
        while (file.exists()) {
            file = dir.resolve("${rating.deckUuid}-$timestamp-$suffix.json")
            suffix++
        }
        writeJsonAtomic(file, objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rating))
    }

    override fun findRatingHistory(key: String, deckUuid: String?): List<PoolDeckRating> {
        val dir = ratingsDir(key)
        if (!dir.exists()) return emptyList()
        return Files.list(dir).use { stream ->
            stream.filter { it.isRegularFile() }
                .filter { deckUuid == null || it.name.startsWith("$deckUuid-") }
                .toList()
                .sortedByDescending { it.name } // timestamp suffix keeps files chronologically ordered
                .map { objectMapper.readValue<PoolDeckRating>(it.readText()) }
        }
    }

    private fun poolFile(key: String): Path = basePath.resolve("$key.json")

    private fun ratingsDir(key: String): Path = basePath.resolve(key).resolve("ratings")

    private fun writeJsonAtomic(path: Path, content: String) {
        // Write to a temp file in the same directory and atomically move it into
        // place so concurrent readers never observe a partial JSON document.
        val temp = path.resolveSibling("${path.fileName}.tmp-${java.util.UUID.randomUUID()}")
        Files.writeString(temp, content)
        try {
            Files.move(temp, path, java.nio.file.StandardCopyOption.REPLACE_EXISTING, java.nio.file.StandardCopyOption.ATOMIC_MOVE)
        } catch (e: java.nio.file.AtomicMoveNotSupportedException) {
            Files.move(temp, path, java.nio.file.StandardCopyOption.REPLACE_EXISTING)
        }
    }
}
