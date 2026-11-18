package com.aember.analyzer.infrastructure.persistence.repository

import com.aember.analyzer.infrastructure.persistence.entity.HouseEntity
import org.springframework.data.jpa.repository.JpaRepository

interface HouseJpaRepository : JpaRepository<HouseEntity, Int> {
    fun findByName(name: String): HouseEntity?
}
