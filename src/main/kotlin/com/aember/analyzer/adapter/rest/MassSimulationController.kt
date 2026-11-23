package com.aember.analyzer.adapter.rest

import com.aember.analyzer.application.MassSimulationService
import com.aember.analyzer.domain.rating.TierValidation
import com.aember.analyzer.domain.model.MassSimulationRequest
import com.aember.analyzer.domain.model.MultiDeckSimulationRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/mass-simulations")
class MassSimulationController(
    private val massSimulationService: MassSimulationService
) {

    @PostMapping
    fun runMassSimulation(@RequestBody request: MassSimulationRequest): ResponseEntity<MassSimulationBatchDto> {
        val batch = massSimulationService.runMassSimulation(request)
        return ResponseEntity.accepted().body(batch.toDto())
    }

    @PostMapping("/multi")
    fun runMultiDeckSimulation(
        @RequestBody request: MultiDeckSimulationRequest,
        @RequestHeader(value = "X-Debug", required = false) debug: String?
    ): ResponseEntity<MassSimulationBatchDto> {
        val recordLogs = debug?.toBooleanStrictOrNull() ?: false
        val batch = massSimulationService.runMultiDeckSimulation(request, recordLogs = recordLogs)
        return ResponseEntity.accepted().body(batch.toDto())
    }

    @GetMapping
    fun listBatches(): ResponseEntity<List<MassSimulationBatchSummaryDto>> {
        return ResponseEntity.ok(massSimulationService.listBatches().map { it.toSummaryDto() })
    }

    @GetMapping("/{uuid}")
    fun getBatch(@PathVariable uuid: String): ResponseEntity<MassSimulationBatchDto> {
        val batch = massSimulationService.getBatch(uuid)
        val fidelityCards = massSimulationService.getCardFidelity(uuid)
        return ResponseEntity.ok(batch.toDto(fidelityCards))
    }

    @DeleteMapping("/{uuid}")
    fun deleteBatch(@PathVariable uuid: String): ResponseEntity<Void> {
        massSimulationService.deleteBatch(uuid)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/{uuid}/ratings")
    fun getRatings(@PathVariable uuid: String): ResponseEntity<List<DeckRatingDto>> {
        return ResponseEntity.ok(massSimulationService.getRatings(uuid).map { it.toDto() })
    }

    /** Body: `{ "tiers": { "<deckUuid>": 6.0, ... } }` — higher is better (e.g. S+=6 ... Z=0). */
    @PostMapping("/{uuid}/tier-validation")
    fun validateTiers(
        @PathVariable uuid: String,
        @RequestBody request: TierValidationRequest
    ): ResponseEntity<TierValidation> {
        return ResponseEntity.ok(massSimulationService.validateTiers(uuid, request.tiers))
    }

    @GetMapping("/{uuid}/matches")
    fun getMatchResults(@PathVariable uuid: String): ResponseEntity<List<MassSimulationMatchResultDto>> {
        val batch = massSimulationService.getBatch(uuid)
        val results = massSimulationService.getMatchResults(uuid)
        return ResponseEntity.ok(results.map { it.toDto(batch) })
    }

    @GetMapping("/{uuid}/matches/{id}/logs")
    fun getMatchLog(
        @PathVariable uuid: String,
        @PathVariable id: Int
    ): ResponseEntity<MassSimulationMatchLogDto> {
        return ResponseEntity.ok(massSimulationService.getMatchLog(uuid, id).toDto())
    }
}

data class TierValidationRequest(val tiers: Map<String, Double>)
