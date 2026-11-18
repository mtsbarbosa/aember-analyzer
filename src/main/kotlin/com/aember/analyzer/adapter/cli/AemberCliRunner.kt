package com.aember.analyzer.adapter.cli

import com.aember.analyzer.application.DeckImportService
import com.aember.analyzer.application.SimulationService
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import java.nio.file.Path
import kotlin.system.exitProcess

@Component
class AemberCliRunner(
    private val deckImportService: DeckImportService,
    private val simulationService: SimulationService
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

                val stats = result.statistics
                println("Match statistics: reaps=${stats.reaps}, fights=${stats.fights}, actionAbilities=${stats.actionAbilitiesUsed}, omni=${stats.omniAbilitiesUsed}, played=${stats.cardsPlayed}, discarded=${stats.cardsDiscarded}, keys=${stats.keysForged}, total=${stats.totalActions}")
                exitProcess(0)
            }
            else -> {
                // Unknown CLI command; let the application continue (web mode).
                logger.warn("Unknown CLI command: ${args[0]}")
            }
        }
    }
}
