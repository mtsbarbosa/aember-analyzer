package com.aember.analyzer.infrastructure.persistence

import com.aember.analyzer.domain.model.Deck
import com.aember.analyzer.domain.model.DeckCard
import com.aember.analyzer.domain.model.House
import com.aember.analyzer.domain.ports.DeckRepository
import com.aember.analyzer.domain.ports.DeckSearchCriteria
import com.aember.analyzer.infrastructure.persistence.entity.DeckCardEntity
import com.aember.analyzer.infrastructure.persistence.entity.DeckEntity
import com.aember.analyzer.infrastructure.persistence.entity.DeckHouseEntity
import com.aember.analyzer.infrastructure.persistence.entity.HouseEntity
import com.aember.analyzer.infrastructure.persistence.repository.DeckJpaRepository
import com.aember.analyzer.infrastructure.persistence.repository.HouseJpaRepository
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class JpaDeckRepository(
    private val deckJpaRepository: DeckJpaRepository,
    private val houseJpaRepository: HouseJpaRepository
) : DeckRepository {

    override fun save(deck: Deck): Deck {
        val houseByName = houseJpaRepository.findAll().associateBy { it.name.uppercase() }
        val entity = DeckEntity(
            id = deck.id,
            uuid = deck.uuid,
            name = deck.name,
            identity = deck.identity,
            expansionId = deck.expansion,
            isAlliance = deck.isAlliance
        )
        entity.houses.addAll(deck.houses.map { house ->
            DeckHouseEntity(
                deck = entity,
                houseId = houseId(houseByName, house)
            )
        })
        entity.cards.addAll(deck.cards.map { card ->
            DeckCardEntity(
                deck = entity,
                cardId = card.cardId,
                count = card.count,
                houseId = houseId(houseByName, card.effectiveHouse),
                maverick = card.maverick,
                anomaly = card.anomaly,
                enhancements = card.enhancements,
                isNonDeck = card.isNonDeck,
                prophecyId = card.prophecyId,
                imageUrl = card.imageUrl
            )
        })

        val saved = deckJpaRepository.save(entity)
        return toDomain(saved, houseByName)
    }

    @Transactional(readOnly = true)
    override fun findByUuid(uuid: String): Deck? {
        val houseByName = houseJpaRepository.findAll().associateBy { it.name.uppercase() }
        return deckJpaRepository.findByUuid(uuid)?.let { toDomain(it, houseByName) }
    }

    @Transactional(readOnly = true)
    override fun existsByUuid(uuid: String): Boolean = deckJpaRepository.existsByUuid(uuid)

    @Transactional(readOnly = true)
    override fun search(criteria: DeckSearchCriteria): List<Deck> {
        val houseByName = houseJpaRepository.findAll().associateBy { it.name.uppercase() }
        val houseId = criteria.house?.let { houseByName[it.name.uppercase()]?.id }
        val spec = DeckSpecifications.withCriteria(criteria, houseId)
        return deckJpaRepository.findAll(spec, Sort.by("name"))
            .map { toDomain(it, houseByName) }
    }

    private fun houseId(houseByName: Map<String, *>, house: House): Int {
        val name = house.name.lowercase().replaceFirstChar { it.uppercase() }
        return houseByName[name.uppercase()]?.let { (it as? HouseEntity)?.id }
            ?: throw IllegalStateException("Unknown house: $house")
    }

    private fun toDomain(entity: DeckEntity, houseByName: Map<String, *>): Deck {
        val idByHouseId = houseByName.values
            .filterIsInstance<HouseEntity>()
            .associateBy({ it.id }, { it.name })
        return Deck(
            id = entity.id,
            uuid = entity.uuid,
            name = entity.name,
            expansion = entity.expansionId,
            identity = entity.identity,
            houses = entity.houses.map { houseName(idByHouseId, it.houseId) },
            cards = entity.cards.map { card ->
                DeckCard(
                    cardId = card.cardId,
                    count = card.count,
                    effectiveHouse = houseName(idByHouseId, card.houseId),
                    maverick = card.maverick,
                    anomaly = card.anomaly,
                    enhancements = card.enhancements,
                    isNonDeck = card.isNonDeck,
                    prophecyId = card.prophecyId,
                    imageUrl = card.imageUrl
                )
            },
            isAlliance = entity.isAlliance
        )
    }

    private fun houseName(idByHouseId: Map<Int, String>, houseId: Int): House {
        val name = idByHouseId[houseId] ?: "Brobnar"
        return House.entries.first { it.name.equals(name.replace(" ", ""), ignoreCase = true) }
    }
}
