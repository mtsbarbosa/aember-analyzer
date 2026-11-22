package com.aember.analyzer.infrastructure.persistence

import com.aember.analyzer.domain.model.MassSimulationBatch
import com.aember.analyzer.domain.model.MassSimulationMatchLog
import com.aember.analyzer.domain.model.MassSimulationMatchResult
import com.aember.analyzer.domain.rating.DeckRating
import com.aember.analyzer.domain.ports.MassSimulationRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.file.Files
import java.nio.file.Path
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.io.path.exists
import kotlin.io.path.isDirectory
import kotlin.io.path.name
import kotlin.io.path.readText

/**
 * File-system adapter for mass-simulation artifacts.
 *
 * Layout:
 *   {storagePath}/{batchUuid}/batch.json
 *   {storagePath}/{batchUuid}/ratings.json
 *   {storagePath}/{batchUuid}/matches/{index}-{yyyyMMdd-HHmmss}-{seed}/result.json
 *   {storagePath}/{batchUuid}/matches/{index}-{yyyyMMdd-HHmmss}-{seed}/log.json
 */
@Component
class FileSystemMassSimulationRepository(
    @Value("\${aember.simulation.storage-path:.simulations}") private val storagePath: String,
    private val objectMapper: ObjectMapper
) : MassSimulationRepository {

    private val basePath: Path by lazy { Path.of(storagePath).toAbsolutePath().normalize() }
    private val matchTimestampFormatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss")

    override fun save(batch: MassSimulationBatch) {
        val path = batchPath(batch.uuid)
        Files.createDirectories(path)
        writeJson(path.resolve(BATCH_FILE), batch)
    }

    override fun findByUuid(uuid: String): MassSimulationBatch? {
        val file = batchPath(uuid).resolve(BATCH_FILE)
        return if (file.exists()) readJson(file) else null
    }

    override fun findAll(): List<MassSimulationBatch> {
        if (!basePath.exists()) return emptyList()
        return Files.list(basePath).use { stream ->
            stream.filter { it.isDirectory() }
                .toList()
                .mapNotNull { findByUuid(it.name) }
                .sortedByDescending { it.createdAt }
        }
    }

    override fun saveResult(result: MassSimulationMatchResult) {
        val dir = matchPath(result.batchUuid, result.id, result.seed)
        Files.createDirectories(dir)
        writeJson(dir.resolve(RESULT_FILE), result)
    }

    override fun findResultsByBatchUuid(uuid: String): List<MassSimulationMatchResult> {
        val matchesDir = batchPath(uuid).resolve(MATCHES_DIR)
        if (!matchesDir.exists()) return emptyList()
        return Files.list(matchesDir).use { stream ->
            stream.filter { it.isDirectory() }
                .toList()
                .mapNotNull { dir ->
                    val resultFile = dir.resolve(RESULT_FILE)
                    if (resultFile.exists()) readJson<MassSimulationMatchResult>(resultFile) else null
                }
                .sortedBy { it.id }
        }
    }

    override fun saveLog(log: MassSimulationMatchLog) {
        val dir = findMatchDir(log.batchUuid, log.matchResultId)
            ?: matchPath(log.batchUuid, log.matchResultId, log.seed)
        Files.createDirectories(dir)
        writeJson(dir.resolve(LOG_FILE), log)
    }

    override fun findLog(batchUuid: String, matchResultId: Int): MassSimulationMatchLog? {
        val dir = findMatchDir(batchUuid, matchResultId) ?: return null
        val logFile = dir.resolve(LOG_FILE)
        return if (logFile.exists()) readJson(logFile) else null
    }

    override fun saveRatings(batchUuid: String, ratings: List<DeckRating>) {
        val dir = batchPath(batchUuid)
        Files.createDirectories(dir)
        writeJson(dir.resolve(RATINGS_FILE), ratings)
    }

    override fun findRatings(batchUuid: String): List<DeckRating>? {
        val file = batchPath(batchUuid).resolve(RATINGS_FILE)
        if (!file.exists()) return null
        return objectMapper.readValue(file.readText())
    }

    private fun findMatchDir(batchUuid: String, matchResultId: Int): Path? {
        val matchesDir = batchPath(batchUuid).resolve(MATCHES_DIR)
        if (!matchesDir.exists()) return null
        val prefix = String.format("%04d-", matchResultId)
        return Files.list(matchesDir).use { stream ->
            stream.filter { it.isDirectory() && it.name.startsWith(prefix) }
                .toList()
                .firstOrNull()
        }
    }

    private fun batchPath(uuid: String): Path = basePath.resolve(uuid)

    private fun matchPath(batchUuid: String, matchId: Int, seed: Long): Path {
        val timestamp = LocalDateTime.now().format(matchTimestampFormatter)
        val dirName = String.format("%04d-%s-%d", matchId, timestamp, seed)
        return batchPath(batchUuid).resolve(MATCHES_DIR).resolve(dirName)
    }

    private fun writeJson(path: Path, value: Any) {
        // Write to a temp file in the same directory and atomically move it into
        // place so concurrent readers never observe a partial JSON document.
        val temp = path.resolveSibling("${path.fileName}.tmp-${java.util.UUID.randomUUID()}")
        Files.writeString(temp, objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(value))
        try {
            Files.move(temp, path, java.nio.file.StandardCopyOption.REPLACE_EXISTING, java.nio.file.StandardCopyOption.ATOMIC_MOVE)
        } catch (e: java.nio.file.AtomicMoveNotSupportedException) {
            Files.move(temp, path, java.nio.file.StandardCopyOption.REPLACE_EXISTING)
        }
    }

    private inline fun <reified T> readJson(path: Path): T {
        return objectMapper.readValue(path.readText())
    }

    companion object {
        private const val BATCH_FILE = "batch.json"
        private const val RESULT_FILE = "result.json"
        private const val LOG_FILE = "log.json"
        private const val RATINGS_FILE = "ratings.json"
        private const val MATCHES_DIR = "matches"
    }
}
