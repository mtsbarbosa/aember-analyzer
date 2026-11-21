package com.aember.analyzer.domain.ports

import com.aember.analyzer.domain.model.Deck
import java.nio.file.Path

interface DeckImportPort {
    fun importByUuid(uuid: String): Deck
    fun importFromFile(path: Path): Deck
}
