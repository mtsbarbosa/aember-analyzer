package com.aember.analyzer.adapter.rest

import com.aember.analyzer.application.MassSimulationService
import com.aember.analyzer.domain.model.MassSimulationRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
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

    @GetMapping
    fun listBatches(): ResponseEntity<List<MassSimulationBatchSummaryDto>> {
        return ResponseEntity.ok(massSimulationService.listBatches().map { it.toSummaryDto() })
    }

    @GetMapping("/{uuid}")
    fun getBatch(@PathVariable uuid: String): ResponseEntity<MassSimulationBatchDto> {
        return ResponseEntity.ok(massSimulationService.getBatch(uuid).toDto())
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
