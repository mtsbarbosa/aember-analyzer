package com.aember.analyzer.domain.engine.model

import com.aember.analyzer.domain.model.CardType
import com.aember.analyzer.domain.model.House

/**
 * Represents a card that is currently in play or attached to a creature.
 */
data class InPlayCard(
    val instanceId: String,
    val cardId: String,
    val ownerIndex: Int,
    var controllerIndex: Int,
    var zone: Zone,
    var house: House,
    val type: CardType,
    var exhausted: Boolean = true,
    var power: Int = 0,
    var armor: Int = 0,
    var damage: Int = 0,
    var capturedAmber: Int = 0,
    var stun: Boolean = false,
    var ward: Boolean = false,
    var enrage: Boolean = false,
    var elusiveUsed: Boolean = false,
    var attackedThisTurn: Boolean = false,
    var keywords: Map<String, Int> = emptyMap(),
    var traits: List<String> = emptyList(),
    var upgrades: MutableList<InPlayCard> = mutableListOf(),
    var neighbors: MutableList<String> = mutableListOf()
)

fun InPlayCard.hasKeyword(keyword: Keyword): Boolean = keywordValue(keyword) > 0

fun InPlayCard.keywordValue(keyword: Keyword): Int = keywords[keyword.name.lowercase()] ?: 0
