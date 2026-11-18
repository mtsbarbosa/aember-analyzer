package com.aember.analyzer.infrastructure.persistence.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes

@Entity
@Table(name = "catalog_cards")
class CatalogCardEntity(
    @Id
    val id: String = "",
    val name: String = "",
    val number: String? = null,
    val image: String? = null,
    val expansionId: Int = 0,
    val houseId: Int = 0,
    val type: String = "",
    val rarity: String = "",
    val amber: Int = 0,
    val armor: Int? = null,
    val power: Int? = null,
    @JdbcTypeCode(SqlTypes.LONG32VARCHAR)
    val text: String? = null,
    @JdbcTypeCode(SqlTypes.ARRAY)
    val keywords: List<String> = emptyList(),
    @JdbcTypeCode(SqlTypes.ARRAY)
    val traits: List<String> = emptyList()
)
