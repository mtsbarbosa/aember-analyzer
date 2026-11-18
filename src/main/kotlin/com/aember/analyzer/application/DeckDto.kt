package com.aember.analyzer.application

import com.aember.analyzer.domain.model.Deck
import com.aember.analyzer.domain.model.DeckCard

data class DeckSummaryDto(
    val uuid: String,
    val name: String,
    val expansion: Int,
    val identity: String,
    val houses: List<String>,
    val cardCount: Int,
    val isAlliance: Boolean
)

data class DeckCardDto(
    val cardId: String,
    val count: Int,
    val house: String,
    val maverick: Boolean,
    val anomaly: Boolean,
    val enhancements: List<String>,
    val isNonDeck: Boolean
)

data class DeckDetailDto(
    val uuid: String,
    val name: String,
    val expansion: Int,
    val identity: String,
    val houses: List<String>,
    val cards: List<DeckCardDto>,
    val isAlliance: Boolean
)

fun Deck.toSummaryDto(): DeckSummaryDto = DeckSummaryDto(
    uuid = uuid,
    name = name,
    expansion = expansion,
    identity = identity,
    houses = houses.map { it.name },
    cardCount = cards.sumOf { it.count },
    isAlliance = isAlliance
)

fun Deck.toDetailDto(): DeckDetailDto = DeckDetailDto(
    uuid = uuid,
    name = name,
    expansion = expansion,
    identity = identity,
    houses = houses.map { it.name },
    cards = cards.map { it.toDto() },
    isAlliance = isAlliance
)

private fun DeckCard.toDto(): DeckCardDto = DeckCardDto(
    cardId = cardId,
    count = count,
    house = effectiveHouse.name,
    maverick = maverick,
    anomaly = anomaly,
    enhancements = enhancements,
    isNonDeck = isNonDeck
)
