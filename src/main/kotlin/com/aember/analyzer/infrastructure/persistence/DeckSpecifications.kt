package com.aember.analyzer.infrastructure.persistence

import com.aember.analyzer.domain.ports.DeckSearchCriteria
import com.aember.analyzer.infrastructure.persistence.entity.DeckEntity
import com.aember.analyzer.infrastructure.persistence.entity.DeckHouseEntity
import org.springframework.data.jpa.domain.Specification

object DeckSpecifications {

    fun withCriteria(criteria: DeckSearchCriteria, houseId: Int?): Specification<DeckEntity> {
        return Specification.where(nullableName(criteria.name))
            .and(nullableExpansion(criteria.expansion))
            .and(nullableHouse(houseId))
    }

    private fun nullableName(name: String?): Specification<DeckEntity> =
        Specification { root, _, cb ->
            name?.let {
                cb.like(cb.lower(root.get("name")), "%${it.lowercase()}%")
            }
        }

    private fun nullableExpansion(expansion: Int?): Specification<DeckEntity> =
        Specification { root, _, cb ->
            expansion?.let {
                cb.equal(root.get<Int>("expansionId"), it)
            }
        }

    private fun nullableHouse(houseId: Int?): Specification<DeckEntity> =
        Specification { root, query, cb ->
            houseId?.let {
                val subquery = query.subquery(Long::class.java)
                val dhRoot = subquery.from(DeckHouseEntity::class.java)
                subquery.select(cb.literal(1L))
                subquery.where(
                    cb.equal(dhRoot.get<DeckEntity>("deck"), root),
                    cb.equal(dhRoot.get<Int>("houseId"), houseId)
                )
                cb.exists(subquery)
            }
        }
}
