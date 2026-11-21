package com.aember.analyzer.application

import com.aember.analyzer.domain.model.Deck
import com.aember.analyzer.domain.ports.DeckImportPort
import com.aember.analyzer.domain.ports.DeckRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.nio.file.Path

@Service
class DeckImportService(
    private val deckImportPort: DeckImportPort,
    private val deckRepository: DeckRepository
) {

    @Transactional
    fun importByUuid(uuid: String): Deck {
        val deck = deckImportPort.importByUuid(uuid)
        return deckRepository.save(deck)
    }

    @Transactional
    fun importFromFile(path: Path): Deck {
        val deck = deckImportPort.importFromFile(path)
        return deckRepository.save(deck)
    }
}
