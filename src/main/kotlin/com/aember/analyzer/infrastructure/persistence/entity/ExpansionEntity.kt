package com.aember.analyzer.infrastructure.persistence.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "expansions")
class ExpansionEntity(
    @Id
    val id: Int = 0,
    val code: String = "",
    val name: String = "",
    val releaseDate: java.time.LocalDate? = null
)
