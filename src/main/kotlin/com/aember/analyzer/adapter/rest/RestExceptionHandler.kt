package com.aember.analyzer.adapter.rest

import com.aember.analyzer.application.LadderService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class RestExceptionHandler {

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(e: IllegalArgumentException): ResponseEntity<Map<String, String>> {
        return ResponseEntity.badRequest().body(mapOf("error" to (e.message ?: "Invalid request")))
    }

    /** Only one ladder rerun may run at a time; a second concurrent call conflicts. */
    @ExceptionHandler(LadderService.LadderInProgressException::class)
    fun handleLadderInProgress(e: LadderService.LadderInProgressException): ResponseEntity<Map<String, String>> {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(mapOf("error" to (e.message ?: "Ladder rerun in progress")))
    }
}
