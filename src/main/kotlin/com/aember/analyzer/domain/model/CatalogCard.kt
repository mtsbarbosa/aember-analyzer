package com.aember.analyzer.domain.model

data class CatalogCard(
    val id: String,
    val name: String,
    val number: String,
    val image: String,
    val expansion: Int,
    val house: House,
    val type: CardType,
    val rarity: Rarity,
    val amber: Int,
    val armor: Int?,
    val power: Int?,
    val text: String,
    val keywords: List<String>,
    val traits: List<String>
)
