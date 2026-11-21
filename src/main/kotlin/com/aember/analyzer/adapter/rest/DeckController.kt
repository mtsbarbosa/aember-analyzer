package com.aember.analyzer.adapter.rest

import com.aember.analyzer.application.DeckImportService
import com.aember.analyzer.application.DeckQueryService
import com.aember.analyzer.domain.model.Deck
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/decks")
class DeckController(
    private val deckImportService: DeckImportService,
    private val deckQueryService: DeckQueryService
) {

    @PostMapping("/import/{uuid}")
    fun importDeck(@PathVariable uuid: String): ResponseEntity<Deck> {
        val deck = deckImportService.importByUuid(uuid)
        return ResponseEntity.ok(deck)
    }

    @GetMapping
    fun searchDecks(
        @RequestParam name: String? = null,
        @RequestParam house: String? = null,
        @RequestParam expansion: Int? = null
    ): ResponseEntity<List<DeckSummaryDto>> {
        val results = deckQueryService.search(name = name, house = house, expansion = expansion)
        return ResponseEntity.ok(results.map { it.toSummaryDto() })
    }

    @GetMapping("/{uuid}")
    fun getDeck(@PathVariable uuid: String): ResponseEntity<DeckDetailDto> {
        val deck = deckQueryService.findByUuid(uuid)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(deck.toDetailDto())
    }
}
