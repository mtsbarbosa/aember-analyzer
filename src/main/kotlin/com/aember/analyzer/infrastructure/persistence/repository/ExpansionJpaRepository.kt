package com.aember.analyzer.infrastructure.persistence.repository

import com.aember.analyzer.infrastructure.persistence.entity.ExpansionEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ExpansionJpaRepository : JpaRepository<ExpansionEntity, Int>
