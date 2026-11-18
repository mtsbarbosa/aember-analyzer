package com.aember.analyzer.infrastructure.persistence.repository

import com.aember.analyzer.infrastructure.persistence.entity.DeckEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface DeckJpaRepository : JpaRepository<DeckEntity, Long>, JpaSpecificationExecutor<DeckEntity> {
    fun findByUuid(uuid: String): DeckEntity?
    fun existsByUuid(uuid: String): Boolean
}
