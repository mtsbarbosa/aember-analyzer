package com.aember.analyzer.application

import com.aember.analyzer.domain.model.House
import com.aember.analyzer.domain.ports.DeckRepository
import com.aember.analyzer.domain.ports.DeckSearchCriteria
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class DeckQueryService(
    private val deckRepository: DeckRepository
) {

    fun findByUuid(uuid: String): DeckDetailDto? =
        deckRepository.findByUuid(uuid)?.toDetailDto()

    fun search(
        name: String? = null,
        house: String? = null,
        expansion: Int? = null
    ): List<DeckSummaryDto> {
        val houseEnum = house?.let { parseHouse(it) }
        val criteria = DeckSearchCriteria(
            name = name?.takeIf { it.isNotBlank() },
            house = houseEnum,
            expansion = expansion
        )
        return deckRepository.search(criteria).map { it.toSummaryDto() }
    }

    private fun parseHouse(value: String): House =
        House.entries.firstOrNull { it.name.equals(value.replace(" ", ""), ignoreCase = true) }
            ?: throw IllegalArgumentException("Unknown house: $value")
}
