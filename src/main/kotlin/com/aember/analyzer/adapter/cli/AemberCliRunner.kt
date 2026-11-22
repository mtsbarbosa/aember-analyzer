package com.aember.analyzer.adapter.cli

import com.aember.analyzer.application.DeckImportService
import com.aember.analyzer.application.MassSimulationService
import com.aember.analyzer.application.SimulationService
import com.aember.analyzer.domain.model.MassSimulationRequest
import com.aember.analyzer.domain.model.MassSimulationBatch
import com.aember.analyzer.domain.model.MassSimulationStatus
import com.aember.analyzer.domain.model.MultiDeckSimulationRequest
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import java.nio.file.Files
import java.nio.file.Path
import kotlin.system.exitProcess

@Component
class AemberCliRunner(
    private val deckImportService: DeckImportService,
    private val simulationService: SimulationService,
    private val massSimulationService: MassSimulationService
) : CommandLineRunner {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun run(vararg args: String?) {
        if (args.isEmpty()) {
            return
        }

        when (args[0]) {
            "import-deck" -> {
                val uuid = args.getOrNull(1)
                    ?: throw IllegalArgumentException("Usage: import-deck <uuid>")
                logger.info("CLI import-deck invoked for $uuid")
                val deck = deckImportService.importByUuid(uuid)
                println("Imported deck: ${deck.name} (${deck.uuid}) with ${deck.cards.size} distinct cards")
                exitProcess(0)
            }
            "import-deck-file" -> {
                val filePath = args.getOrNull(1)
                    ?: throw IllegalArgumentException("Usage: import-deck-file <path>")
                logger.info("CLI import-deck-file invoked for $filePath")
                val deck = deckImportService.importFromFile(Path.of(filePath))
                println("Imported deck from file: ${deck.name} (${deck.uuid}) with ${deck.cards.size} distinct cards")
                exitProcess(0)
            }
            "simulate" -> {
                val deckA = args.getOrNull(1) ?: throw IllegalArgumentException("Usage: simulate <deck-uuid-a> <deck-uuid-b> [seed] [aiA] [aiB]")
                val deckB = args.getOrNull(2) ?: throw IllegalArgumentException("Usage: simulate <deck-uuid-a> <deck-uuid-b> [seed] [aiA] [aiB]")
                val seed = args.getOrNull(3)?.toLong() ?: System.currentTimeMillis()
                val aiA = args.getOrNull(4) ?: "tactical"
                val aiB = args.getOrNull(5) ?: "tactical"
                logger.info("CLI simulate invoked for $deckA ($aiA) vs $deckB ($aiB) with seed $seed")
                val result = simulationService.simulate(deckA, deckB, seed, aiA = aiA, aiB = aiB)
                println("Match result: ${result.gameState.players[result.gameState.winnerIndex ?: -1].name} wins by ${result.gameState.endCause}")
                println("Turns: ${result.gameState.turn}, Rounds: ${result.gameState.round}")
                result.gameState.log.takeLast(20).forEach { println("  ${it.message}") }

                println("Fidelity score: %.1f%%".format(result.fidelity.score))
                if (result.belowFidelityThreshold) {
                    println("WARNING: fidelity is below configured threshold")
                }
                println("Fidelity distribution: ${result.fidelity.levelCounts}")
                if (result.fidelity.lowestCards.isNotEmpty()) {
                    println("Lowest-fidelity cards seen:")
                    result.fidelity.lowestCards.forEach { (card, level) -> println("  $card -> $level") }
                }

                result.gameState.players.forEach { player ->
                    val stats = player.statistics
                    println("${player.name} statistics: reaps=${stats.reaps}, fights=${stats.fights}, actionAbilities=${stats.actionAbilitiesUsed}, omni=${stats.omniAbilitiesUsed}, played=${stats.cardsPlayed}, discarded=${stats.cardsDiscarded}, keys=${stats.keysForged}, total=${stats.totalActions()}")
                }
                exitProcess(0)
            }
            "mass-simulate" -> {
                val deckA = args.getOrNull(1) ?: throw IllegalArgumentException("Usage: mass-simulate <deck-uuid-a> <deck-uuid-b> <match-count> [aiA] [aiB] [recordLogs]")
                val deckB = args.getOrNull(2) ?: throw IllegalArgumentException("Usage: mass-simulate <deck-uuid-a> <deck-uuid-b> <match-count> [aiA] [aiB] [recordLogs]")
                val matchCount = args.getOrNull(3)?.toIntOrNull()
                    ?: throw IllegalArgumentException("Usage: mass-simulate <deck-uuid-a> <deck-uuid-b> <match-count> [aiA] [aiB] [recordLogs]")
                val aiA = args.getOrNull(4) ?: "tactical"
                val aiB = args.getOrNull(5) ?: "tactical"
                val recordLogs = args.getOrNull(6)?.toBooleanStrictOrNull() ?: true

                logger.info("CLI mass-simulate invoked for $deckA ($aiA) vs $deckB ($aiB), $matchCount matches")
                val batch = massSimulationService.runMassSimulation(
                    MassSimulationRequest(
                        deckUuidA = deckA,
                        deckUuidB = deckB,
                        matchCount = matchCount,
                        aiA = aiA,
                        aiB = aiB,
                        recordLogs = recordLogs
                    )
                )
                println("Started mass simulation batch: ${batch.uuid}")
                println("Polling for completion...")

                val completedBatch = pollForCompletion(batch.uuid)
                println("\nBatch status: ${completedBatch.status}")
                println("Matches: ${completedBatch.matchCount}")
                println("Wins A (${completedBatch.deckNameA}): ${completedBatch.winsA}")
                println("Wins B (${completedBatch.deckNameB}): ${completedBatch.winsB}")
                println("Draws: ${completedBatch.draws}")
                println("Avg rounds: ${completedBatch.avgRounds?.let { "%.2f".format(it) } ?: "-"}")
                println("Avg turns: ${completedBatch.avgTurns?.let { "%.2f".format(it) } ?: "-"}")
                println("Avg fidelity: ${completedBatch.avgFidelity?.let { "%.2f%%".format(it) } ?: "-"}")
                if (completedBatch.errorMessage != null) {
                    println("Error: ${completedBatch.errorMessage}")
                }
                exitProcess(0)
            }
            "mass-simulate-decks" -> {
                val rawArgs = args.filterNotNull()
                val debugLogs = "--debug-logs" in rawArgs
                val positional = rawArgs.drop(1).filter { it != "--debug-logs" }
                val listFile = positional.getOrNull(0)
                    ?: throw IllegalArgumentException("Usage: mass-simulate-decks <deck-list-file> <matches-per-pairing> [ai] [seed] [--debug-logs]")
                val matchesPerPairing = positional.getOrNull(1)?.toIntOrNull()
                    ?: throw IllegalArgumentException("Usage: mass-simulate-decks <deck-list-file> <matches-per-pairing> [ai] [seed] [--debug-logs]")
                val ai = positional.getOrNull(2) ?: "tactical"
                val baseSeed = positional.getOrNull(3)?.toLongOrNull() ?: 0L

                val deckUuids = Files.readAllLines(Path.of(listFile))
                    .map { it.trim() }
                    .filter { it.isNotEmpty() && !it.startsWith("#") }

                logger.info(
                    "CLI mass-simulate-decks invoked with {} decks, {} matches per pairing (ai={}, baseSeed={}, debugLogs={})",
                    deckUuids.size, matchesPerPairing, ai, baseSeed, debugLogs
                )
                val batch = massSimulationService.runMultiDeckSimulation(
                    MultiDeckSimulationRequest(
                        deckUuids = deckUuids,
                        matchesPerPairing = matchesPerPairing,
                        ai = ai,
                        baseSeed = baseSeed
                    ),
                    recordLogs = debugLogs
                )
                println("Started multi-deck simulation batch: ${batch.uuid}")
                println("Decks: ${batch.deckPool.size}, pairings: ${batch.matchesPerPairing?.let { batch.deckPool.size * (batch.deckPool.size - 1) / 2 } ?: "-"}, total matches: ${batch.matchCount}")
                println("Polling for completion...")

                val completedBatch = pollForCompletion(batch.uuid)
                println("\nBatch status: ${completedBatch.status}")
                println("Matches: ${completedBatch.completedCount}/${completedBatch.matchCount} completed, ${completedBatch.failedCount} failed")
                println("Avg rounds: ${completedBatch.avgRounds?.let { "%.2f".format(it) } ?: "-"}")
                println("Avg fidelity: ${completedBatch.avgFidelity?.let { "%.2f%%".format(it) } ?: "-"}")
                if (completedBatch.errorMessage != null) {
                    println("Error: ${completedBatch.errorMessage}")
                }

                if (completedBatch.status == MassSimulationStatus.COMPLETED) {
                    println("\nDeck ratings (sorted by Aember Score):")
                    println(String.format("%-30s %8s %6s %6s %6s %10s %8s %8s", "Deck", "Score", "W", "L", "D", "WinRate", "SoS", "Pct"))
                    massSimulationService.getRatings(batch.uuid).forEach { rating ->
                        println(
                            String.format(
                                "%-30s %8.1f %6d %6d %6d %9.1f%% %7.1f%% %7.1f%%%s",
                                rating.deckName.take(30),
                                rating.aemberScore,
                                rating.wins,
                                rating.losses,
                                rating.draws,
                                rating.winRate * 100,
                                rating.sos * 100,
                                rating.percentile,
                                if (rating.lowConfidence) " (low confidence)" else ""
                            )
                        )
                    }
                }
                exitProcess(0)
            }
            else -> {
                // Unknown CLI command; let the application continue (web mode).
                logger.warn("Unknown CLI command: ${args[0]}")
            }
        }
    }

    private fun pollForCompletion(uuid: String, timeoutMs: Long = 300_000, intervalMs: Long = 500): MassSimulationBatch {
        val start = System.currentTimeMillis()
        while (System.currentTimeMillis() - start < timeoutMs) {
            val batch = massSimulationService.getBatch(uuid)
            if (batch.status == MassSimulationStatus.COMPLETED || batch.status == MassSimulationStatus.FAILED) {
                return batch
            }
            Thread.sleep(intervalMs)
        }
        throw IllegalStateException("Timed out waiting for mass simulation batch $uuid")
    }
}
