package com.aember.analyzer.infrastructure.persistence

import com.aember.analyzer.domain.model.CardType
import com.aember.analyzer.domain.model.CatalogCard
import com.aember.analyzer.domain.model.House
import com.aember.analyzer.domain.model.Rarity
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.file.Path

@Component
class JsonCardCatalogLoader(
    private val objectMapper: ObjectMapper,
    @Value("\${keyteki.data.path}") private val basePath: String
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun loadCoreSet(): List<CatalogCard> = loadPack("CotA.json")

    fun loadPack(fileName: String): List<CatalogCard> {
        val path = Path.of(basePath, "packs", fileName)
        logger.info("Loading card catalog from $path")
        val root = objectMapper.readTree(path.toFile())
        val cardsNode = root.get("cards") ?: return emptyList()
        return cardsNode.mapNotNull { parseCard(it) }
    }

    private fun parseCard(node: JsonNode): CatalogCard? {
        val id = node.get("id")?.asText() ?: return null
        return CatalogCard(
            id = sanitizeId(id),
            name = node.get("name")?.asText() ?: id,
            number = node.get("number")?.asText() ?: "",
            image = node.get("image")?.asText() ?: "",
            expansion = node.get("expansion")?.asInt() ?: 0,
            house = parseHouse(node.get("house")?.asText() ?: return null),
            type = parseType(node.get("type")?.asText() ?: return null),
            rarity = parseRarity(node.get("rarity")?.asText() ?: "Common"),
            amber = node.get("amber")?.asInt() ?: 0,
            armor = node.get("armor")?.takeIf { !it.isNull }?.asInt(),
            power = node.get("power")?.takeIf { !it.isNull }?.asInt(),
            text = node.get("text")?.asText() ?: "",
            keywords = node.get("keywords")?.map { it.asText() } ?: emptyList(),
            traits = node.get("traits")?.map { it.asText() } ?: emptyList()
        )
    }

    private fun parseHouse(value: String): House =
        House.entries.firstOrNull { it.name.equals(value.replace(" ", ""), ignoreCase = true) }
            ?: throw IllegalArgumentException("Unknown house: $value")

    private fun parseType(value: String): CardType =
        when (value.lowercase()) {
            "action" -> CardType.ACTION
            "artifact" -> CardType.ARTIFACT
            "creature" -> CardType.CREATURE
            "upgrade" -> CardType.UPGRADE
            "token creature" -> CardType.TOKEN_CREATURE
            "prophecy" -> CardType.PROPHECY
            else -> throw IllegalArgumentException("Unknown card type: $value")
        }

    private fun parseRarity(value: String): Rarity =
        when (value.lowercase()) {
            "common" -> Rarity.COMMON
            "uncommon" -> Rarity.UNCOMMON
            "rare" -> Rarity.RARE
            else -> Rarity.SPECIAL
        }

    private fun sanitizeId(id: String): String =
        id.lowercase().replace("[^a-z0-9\\-]".toRegex(), "")
}
