package com.aember.analyzer.infrastructure.persistence.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "decks")
class DeckEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val uuid: String = "",
    val name: String = "",
    val identity: String = "",
    val expansionId: Int = 0,
    val isAlliance: Boolean = false,
    val createdAt: Instant = Instant.now(),
    @OneToMany(mappedBy = "deck", cascade = [CascadeType.ALL], orphanRemoval = true)
    val houses: MutableList<DeckHouseEntity> = mutableListOf(),
    @OneToMany(mappedBy = "deck", cascade = [CascadeType.ALL], orphanRemoval = true)
    val cards: MutableList<DeckCardEntity> = mutableListOf()
)
