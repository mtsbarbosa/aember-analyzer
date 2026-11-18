package com.aember.analyzer.domain.model

data class DeckCard(
    val cardId: String,
    val count: Int,
    val effectiveHouse: House,
    val maverick: Boolean,
    val anomaly: Boolean,
    val enhancements: List<String>,
    val isNonDeck: Boolean,
    val prophecyId: Int? = null,
    val imageUrl: String? = null
)
