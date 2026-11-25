package com.aember.analyzer.domain.engine.model

import com.aember.analyzer.domain.model.CardType
import com.aember.analyzer.domain.model.House

/**
 * Represents a card that is currently in play or attached to a creature.
 *
 * `power`, `armor` and `keywords` hold the *effective* values (base + active
 * static effects, see [com.aember.analyzer.domain.engine.effects.EffectRegistry]);
 * the printed values are kept in `basePower` / `baseArmor` / `baseKeywords`.
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
    var immuneToDamageThisTurn: Boolean = false,
    var armorStrippedThisTurn: Int = 0,
    /** Cards placed facedown beneath this card (Masterplan). */
    val beneathCardIds: MutableList<String> = mutableListOf(),
    val grantedKeywordsThisTurn: MutableMap<String, Int> = mutableMapOf(),
    val removedKeywordsThisTurn: MutableSet<String> = mutableSetOf(),
    var usedThisTurn: Int = 0,
    /** House stored on the card at play/ability time (Restringuntus). */
    var chosenHouse: House? = null,
    var keywords: Map<String, Int> = emptyMap(),
    var traits: List<String> = emptyList(),
    var upgrades: MutableList<InPlayCard> = mutableListOf(),
    var neighbors: MutableList<String> = mutableListOf(),
    /** Instance id of the creature this upgrade is attached to, when zone == UPGRADE. */
    var hostId: String? = null
) {
    var basePower: Int = power
    var baseArmor: Int = armor
    var baseKeywords: Map<String, Int> = keywords
    /** Printed house; temporary house changes (Brain Stem Antenna) restore this at turn start. */
    var baseHouse: House = house
}

fun InPlayCard.hasKeyword(keyword: Keyword): Boolean = keywordValue(keyword) > 0

fun InPlayCard.keywordValue(keyword: Keyword): Int = keywords[keyword.name.lowercase()] ?: 0
