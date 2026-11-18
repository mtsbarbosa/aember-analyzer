package com.aember.analyzer.domain.ports

import com.aember.analyzer.domain.model.Deck

interface DeckImportPort {
    fun importByUuid(uuid: String): Deck
}
