package com.aember.analyzer.infrastructure.rest

import com.aember.analyzer.domain.model.CardSlug
import com.aember.analyzer.domain.model.Deck
import com.aember.analyzer.domain.model.DeckCard
import com.aember.analyzer.domain.model.House
import com.aember.analyzer.domain.ports.CardCatalogPort
import com.aember.analyzer.domain.ports.DeckImportException
import com.fasterxml.jackson.databind.JsonNode

object MasterVaultDeckParser {

    fun parse(root: JsonNode, catalog: CardCatalogPort): Deck {
        val deckNode = root.get("data") ?: throw DeckImportException("Missing deck data")
        val uuid = deckNode.get("id")?.asText()
            ?: throw DeckImportException("Missing deck id")
        val name = deckNode.get("name")?.asText() ?: uuid
        val expansion = deckNode.get("expansion")?.asInt()
            ?: throw DeckImportException("Missing expansion in deck $uuid")
        val houses = deckNode.get("_links")?.get("houses")?.map {
            parseHouse(it.asText())
        } ?: emptyList()
        val cardUuidList = deckNode.get("_links")?.get("cards")?.map { it.asText() }
            ?: emptyList()
        val bonusIcons = deckNode.get("bonus_icons")?.associate {
            it.get("card_id").asText() to it.get("bonus_icons").map { icon -> icon.asText() }
        } ?: emptyMap<String, List<String>>()

        val linkedCards = root.get("_linked")?.get("cards")?.associateBy { it.get("id").asText() }
            ?: emptyMap<String, JsonNode>()

        if (catalog.loadByExpansion(expansion).isEmpty()) {
            throw DeckImportException("Expansion $expansion is not supported")
        }

        val cardCounts = cardUuidList.groupingBy { it }.eachCount()
        val deckCards = cardCounts.mapNotNull { (cardUuid, count) ->
            val linkedCard = linkedCards[cardUuid]
                ?: throw DeckImportException("Missing linked card data for $cardUuid")
            parseDeckCard(linkedCard, count, bonusIcons[cardUuid] ?: emptyList(), catalog)
        }

        return Deck(
            uuid = uuid,
            name = name,
            expansion = expansion,
            identity = CardSlug.fromTitle(name),
            houses = houses,
            cards = deckCards
        )
    }

    private fun parseDeckCard(
        node: JsonNode,
        count: Int,
        bonusIcons: List<String>,
        catalog: CardCatalogPort
    ): DeckCard {
        val title = node.get("card_title")?.asText()
            ?: throw DeckImportException("Missing card title")
        val slug = CardSlug.fromTitle(title)
        val catalogCard = catalog.findById(slug)
            ?: throw DeckImportException("Unknown card: $title ($slug)")

        val isMaverick = node.get("is_maverick")?.asBoolean() == true
        val isAnomaly = node.get("is_anomaly")?.asBoolean() == true
        val apiHouse = node.get("house")?.asText()?.let { parseHouse(it) } ?: catalogCard.house
        val effectiveHouse = when {
            isMaverick || isAnomaly -> apiHouse
            else -> catalogCard.house
        }
        val isNonDeck = node.get("is_non_deck")?.asBoolean() == true

        return DeckCard(
            cardId = slug,
            count = count,
            effectiveHouse = effectiveHouse,
            maverick = isMaverick,
            anomaly = isAnomaly,
            enhancements = bonusIcons,
            isNonDeck = isNonDeck,
            imageUrl = node.get("front_image")?.asText()
        )
    }

    private fun parseHouse(value: String): House =
        House.entries.firstOrNull { it.name.equals(value.replace(" ", ""), ignoreCase = true) }
            ?: throw DeckImportException("Unknown house: $value")
}
