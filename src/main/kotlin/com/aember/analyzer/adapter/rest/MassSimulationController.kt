package com.aember.analyzer.adapter.rest

import com.aember.analyzer.application.MassSimulationMatchLogDto
import com.aember.analyzer.application.MassSimulationMatchResultDto
import com.aember.analyzer.application.MassSimulationRequest
import com.aember.analyzer.application.MassSimulationBatchDto
import com.aember.analyzer.application.MassSimulationBatchSummaryDto
import com.aember.analyzer.application.MassSimulationService
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
        return ResponseEntity.accepted().body(batch)
    }

    @GetMapping
    fun listBatches(): ResponseEntity<List<MassSimulationBatchSummaryDto>> {
        return ResponseEntity.ok(massSimulationService.listBatches())
    }

    @GetMapping("/{uuid}")
    fun getBatch(@PathVariable uuid: String): ResponseEntity<MassSimulationBatchDto> {
        return ResponseEntity.ok(massSimulationService.getBatch(uuid))
    }

    @GetMapping("/{uuid}/matches")
    fun getMatchResults(@PathVariable uuid: String): ResponseEntity<List<MassSimulationMatchResultDto>> {
        return ResponseEntity.ok(massSimulationService.getMatchResults(uuid))
    }

    @GetMapping("/{uuid}/matches/{id}/logs")
    fun getMatchLog(
        @PathVariable uuid: String,
        @PathVariable id: Int
    ): ResponseEntity<MassSimulationMatchLogDto> {
        return ResponseEntity.ok(massSimulationService.getMatchLog(uuid, id))
    }
}
