package com.aember.analyzer.infrastructure.persistence.repository

import com.aember.analyzer.infrastructure.persistence.entity.CatalogCardEntity
import org.springframework.data.jpa.repository.JpaRepository

interface CatalogCardJpaRepository : JpaRepository<CatalogCardEntity, String> {
    fun findByExpansionId(expansionId: Int): List<CatalogCardEntity>
}
