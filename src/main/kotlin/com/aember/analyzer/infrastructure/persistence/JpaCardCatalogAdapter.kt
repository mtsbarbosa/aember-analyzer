package com.aember.analyzer.infrastructure.persistence

import com.aember.analyzer.domain.model.CardType
import com.aember.analyzer.domain.model.CatalogCard
import com.aember.analyzer.domain.model.House
import com.aember.analyzer.domain.model.Rarity
import com.aember.analyzer.domain.ports.CardCatalogPort
import com.aember.analyzer.infrastructure.persistence.entity.CatalogCardEntity
import com.aember.analyzer.infrastructure.persistence.entity.ExpansionEntity
import com.aember.analyzer.infrastructure.persistence.entity.HouseEntity
import com.aember.analyzer.infrastructure.persistence.repository.CatalogCardJpaRepository
import com.aember.analyzer.infrastructure.persistence.repository.ExpansionJpaRepository
import com.aember.analyzer.infrastructure.persistence.repository.HouseJpaRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(readOnly = true)
class JpaCardCatalogAdapter(
    private val catalogCardRepository: CatalogCardJpaRepository,
    private val expansionRepository: ExpansionJpaRepository,
    private val houseRepository: HouseJpaRepository
) : CardCatalogPort {

    override fun loadAll(): List<CatalogCard> {
        val houses = houseRepository.findAll().associateBy { it.id }
        return catalogCardRepository.findAll().map { toDomain(it, houses) }
    }

    override fun loadByExpansion(expansion: Int): List<CatalogCard> {
        val houses = houseRepository.findAll().associateBy { it.id }
        return catalogCardRepository.findByExpansionId(expansion).map { toDomain(it, houses) }
    }

    override fun findById(id: String): CatalogCard? {
        val houses = houseRepository.findAll().associateBy { it.id }
        return catalogCardRepository.findById(id).orElse(null)?.let { toDomain(it, houses) }
    }

    @Transactional
    fun seed(cards: List<CatalogCard>) {
        val housesByName = houseRepository.findAll().associateBy { it.name.uppercase() }
        val expansionsById = expansionRepository.findAll().associateBy { it.id }

        val entities = cards.map { card ->
            val houseId = housesByName[card.house.name]?.id
                ?: throw IllegalStateException("Unknown house: ${card.house}")
            CatalogCardEntity(
                id = card.id,
                name = card.name,
                number = card.number,
                image = card.image,
                expansionId = card.expansion,
                houseId = houseId,
                type = card.type.name.lowercase(),
                rarity = card.rarity.name.lowercase(),
                amber = card.amber,
                armor = card.armor,
                power = card.power,
                text = card.text,
                keywords = card.keywords,
                traits = card.traits
            )
        }

        catalogCardRepository.saveAll(entities)
    }

    @Transactional
    fun seedHousesAndExpansion() {
        if (houseRepository.count() == 0L) {
            houseRepository.saveAll(House.entries.map { HouseEntity(name = it.name.lowercase().replaceFirstChar { c -> c.uppercase() }) })
        }
        if (!expansionRepository.existsById(341)) {
            expansionRepository.save(ExpansionEntity(id = 341, code = "CotA", name = "Call of the Archons"))
        }
    }

    private fun toDomain(entity: CatalogCardEntity, houses: Map<Int, HouseEntity>): CatalogCard {
        val houseName = houses[entity.houseId]?.name ?: "brobnar"
        return CatalogCard(
            id = entity.id,
            name = entity.name,
            number = entity.number ?: "",
            image = entity.image ?: "",
            expansion = entity.expansionId,
            house = House.entries.first { it.name.equals(houseName.replace(" ", ""), ignoreCase = true) },
            type = CardType.valueOf(entity.type.uppercase()),
            rarity = Rarity.valueOf(entity.rarity.uppercase()),
            amber = entity.amber,
            armor = entity.armor,
            power = entity.power,
            text = entity.text ?: "",
            keywords = entity.keywords,
            traits = entity.traits
        )
    }
}
