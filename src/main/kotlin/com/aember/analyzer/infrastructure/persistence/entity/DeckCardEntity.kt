package com.aember.analyzer.infrastructure.persistence.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes

@Entity
@Table(name = "deck_cards")
class DeckCardEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @ManyToOne
    @JoinColumn(name = "deck_id")
    val deck: DeckEntity? = null,
    val cardId: String = "",
    val count: Int = 1,
    val houseId: Int = 0,
    val maverick: Boolean = false,
    val anomaly: Boolean = false,
    @JdbcTypeCode(SqlTypes.ARRAY)
    val enhancements: List<String> = emptyList(),
    val isNonDeck: Boolean = false,
    val prophecyId: Int? = null,
    val imageUrl: String? = null
)
