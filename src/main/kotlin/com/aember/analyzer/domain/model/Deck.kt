package com.aember.analyzer.domain.model

data class Deck(
    val id: Long? = null,
    val uuid: String,
    val name: String,
    val expansion: Int,
    val identity: String,
    val houses: List<House>,
    val cards: List<DeckCard>,
    val isAlliance: Boolean = false
)
