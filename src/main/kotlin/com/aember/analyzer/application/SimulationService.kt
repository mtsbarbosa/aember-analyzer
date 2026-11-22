package com.aember.analyzer.application

import com.aember.analyzer.domain.ai.AISettings
import com.aember.analyzer.domain.ai.GreedyAI
import com.aember.analyzer.domain.ai.TacticalAI
import com.aember.analyzer.domain.engine.AIPlayer
import com.aember.analyzer.domain.engine.CardLibrary
import com.aember.analyzer.domain.engine.GameEngine
import com.aember.analyzer.domain.engine.fidelity.FidelityReportService
import com.aember.analyzer.domain.ai.RandomAI
import org.slf4j.LoggerFactory
import com.aember.analyzer.domain.model.Deck
import com.aember.analyzer.domain.ports.DeckRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import kotlin.random.Random

@Service
class SimulationService(
    private val deckRepository: DeckRepository,
    private val cardLibrary: CardLibrary,
    @Value("\${aember.simulation.fidelity-threshold:50.0}") private val fidelityThreshold: Double
) {

    private val logger = LoggerFactory.getLogger(SimulationService::class.java)

    fun simulate(
        deckUuidA: String,
        deckUuidB: String,
        seed: Long,
        aiA: String = "tactical",
        aiB: String = "tactical"
    ): SimulationOutcome {
        val deckA = deckRepository.findByUuid(deckUuidA)
            ?: throw IllegalArgumentException("Deck not found: $deckUuidA")
        val deckB = deckRepository.findByUuid(deckUuidB)
            ?: throw IllegalArgumentException("Deck not found: $deckUuidB")
        return simulate(deckA, deckB, seed, aiA, aiB)
    }

    fun simulate(
        deckA: Deck,
        deckB: Deck,
        seed: Long,
        aiA: String = "tactical",
        aiB: String = "tactical"
    ): SimulationOutcome {
        val playerA = createAI(aiA)
        val playerB = createAI(aiB)
        val engine = GameEngine(cardLibrary, playerA, playerB)
        val gameState = engine.runMatch(deckA, deckB, seed)
        val fidelityReport = FidelityReportService.report(gameState)
        val belowThreshold = !FidelityReportService.passesThreshold(fidelityReport, fidelityThreshold)

        if (belowThreshold) {
            logger.warn(
                "Match fidelity {}% is below threshold {}%. Lowest cards: {}",
                String.format("%.1f", fidelityReport.score),
                fidelityThreshold,
                fidelityReport.lowestCards.map { "${it.first}=${it.second}" }
            )
        }

        return SimulationOutcome(gameState, fidelityReport, belowThreshold)
    }

    private fun createAI(type: String): AIPlayer {
        return when (type.lowercase()) {
            "greedy" -> GreedyAI()
            "random" -> RandomAI()
            "tactical" -> TacticalAI(cardLibrary, AISettings.DEFAULT)
            "aggressive" -> TacticalAI(cardLibrary, AISettings.AGGRESSIVE)
            "economic" -> TacticalAI(cardLibrary, AISettings.ECONOMIC)
            else -> {
                logger.warn("Unknown AI type '{}', defaulting to tactical", type)
                TacticalAI(cardLibrary, AISettings.DEFAULT)
            }
        }
    }
}
