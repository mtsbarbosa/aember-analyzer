package com.aember.analyzer.application

import com.aember.analyzer.domain.model.Deck
import com.aember.analyzer.domain.ports.CardCatalogPort
import com.aember.analyzer.domain.ports.DeckImportPort
import com.aember.analyzer.domain.ports.DeckRepository
import com.aember.analyzer.infrastructure.rest.MasterVaultDeckParser
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.nio.file.Path

@Service
class DeckImportService(
    private val deckImportPort: DeckImportPort,
    private val deckRepository: DeckRepository,
    private val cardCatalog: CardCatalogPort,
    private val objectMapper: ObjectMapper
) {

    @Transactional
    fun importByUuid(uuid: String): Deck {
        val deck = deckImportPort.importByUuid(uuid)
        return deckRepository.save(deck)
    }

    @Transactional
    fun importFromFile(path: Path): Deck {
        val root = objectMapper.readTree(path.toFile())
        val deck = MasterVaultDeckParser.parse(root, cardCatalog)
        return deckRepository.save(deck)
    }
}
