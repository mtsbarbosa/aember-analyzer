package com.aember.analyzer.infrastructure.persistence

import com.aember.analyzer.domain.model.CardType
import com.aember.analyzer.domain.model.CatalogCard
import com.aember.analyzer.domain.model.House
import com.aember.analyzer.domain.model.Rarity
import com.aember.analyzer.infrastructure.persistence.entity.CatalogCardEntity

object CatalogCardMapper {

    fun toDomain(entity: CatalogCardEntity): CatalogCard = CatalogCard(
        id = entity.id,
        name = entity.name,
        number = entity.number ?: "",
        image = entity.image ?: "",
        expansion = entity.expansionId,
        house = House.entries.first { it.name.equals(entity.houseId.toString(), ignoreCase = true) }
            // houseId is numeric; we will map via house table in repository
            .let { throw IllegalStateException("use repository mapping") },
        type = CardType.valueOf(entity.type.uppercase()),
        rarity = Rarity.valueOf(entity.rarity.uppercase()),
        amber = entity.amber,
        armor = entity.armor,
        power = entity.power,
        text = entity.text ?: "",
        keywords = entity.keywords,
        traits = entity.traits
    )

    fun toEntity(domain: CatalogCard, houseId: Int): CatalogCardEntity = CatalogCardEntity(
        id = domain.id,
        name = domain.name,
        number = domain.number,
        image = domain.image,
        expansionId = domain.expansion,
        houseId = houseId,
        type = domain.type.name.lowercase(),
        rarity = domain.rarity.name.lowercase(),
        amber = domain.amber,
        armor = domain.armor,
        power = domain.power,
        text = domain.text,
        keywords = domain.keywords,
        traits = domain.traits
    )
}
