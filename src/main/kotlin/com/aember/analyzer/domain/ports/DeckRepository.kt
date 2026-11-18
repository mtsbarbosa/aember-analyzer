package com.aember.analyzer.domain.ports

import com.aember.analyzer.domain.model.Deck
import com.aember.analyzer.domain.model.House

data class DeckSearchCriteria(
    val name: String? = null,
    val house: House? = null,
    val expansion: Int? = null
)

interface DeckRepository {
    fun save(deck: Deck): Deck
    fun findByUuid(uuid: String): Deck?
    fun existsByUuid(uuid: String): Boolean
    fun search(criteria: DeckSearchCriteria): List<Deck>
}
