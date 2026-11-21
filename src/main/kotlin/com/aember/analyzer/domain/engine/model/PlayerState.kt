package com.aember.analyzer.domain.engine.model

import com.aember.analyzer.domain.engine.dsl.TurnTrigger
import com.aember.analyzer.domain.model.Deck
import com.aember.analyzer.domain.model.House

/**
 * Mutable game state for one player.
 */
data class PlayerState(
    val index: Int,
    val name: String,
    val deck: Deck,
    val library: MutableList<String> = mutableListOf(),
    val hand: MutableList<String> = mutableListOf(),
    val discard: MutableList<String> = mutableListOf(),
    val archives: MutableList<String> = mutableListOf(),
    val purged: MutableList<String> = mutableListOf(),
    val creatures: MutableList<InPlayCard> = mutableListOf(),
    val artifacts: MutableList<InPlayCard> = mutableListOf(),
    var keys: Int = 0,
    var amber: Int = 0,
    var chains: Int = 0,
    val statistics: MatchStatistics = MatchStatistics(),
    var activeHouse: House? = null,
    var canForgeSecondKey: Boolean = false,
    val cardNamesPlayedOrUsedThisRound: MutableMap<String, Int> = mutableMapOf(),
    val turnTriggerAmber: MutableMap<TurnTrigger, Int> = mutableMapOf(),
    var creaturesPlayedThisTurn: Int = 0,
    var deniedHouseNextTurn: House? = null,
    var drawOnCardPlayActive: Boolean = false,
    var drawOnCardPlayHouse: House? = null,
    var forgedKeyThisTurn: Boolean = false,
    var forgedKeyLastTurn: Boolean = false,
    var skipsForgeNextTurn: Boolean = false
) {

    fun cardsInPlay(): List<InPlayCard> = creatures + artifacts

    fun battleline(): List<InPlayCard> = creatures

    fun hasWon(): Boolean = keys >= 3
}
