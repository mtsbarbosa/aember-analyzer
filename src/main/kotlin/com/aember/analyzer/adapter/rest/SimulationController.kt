package com.aember.analyzer.adapter.rest

import com.aember.analyzer.application.SimulationResponse
import com.aember.analyzer.application.SimulationService
import com.aember.analyzer.application.toSummary
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/simulate")
class SimulationController(
    private val simulationService: SimulationService
) {

    @GetMapping("/{deckA}/{deckB}")
    fun simulate(
        @PathVariable deckA: String,
        @PathVariable deckB: String,
        @RequestParam seed: Long? = null,
        @RequestParam aiA: String? = null,
        @RequestParam aiB: String? = null,
        @RequestHeader("X-Debug", required = false, defaultValue = "false") debug: Boolean
    ): ResponseEntity<SimulationResponse> {
        val result = simulationService.simulate(
            deckA,
            deckB,
            seed ?: System.currentTimeMillis(),
            aiA = aiA ?: "tactical",
            aiB = aiB ?: "tactical"
        )
        val response = if (debug) {
            result
        } else {
            result.gameState.toSummary(result.fidelity, result.belowFidelityThreshold)
        }
        return ResponseEntity.ok(response)
    }
}
