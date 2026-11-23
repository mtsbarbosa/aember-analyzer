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
    var skipsForgeNextTurn: Boolean = false,
    var keyCostNextTurnBonus: Int = 0,
    var cannotFightNextTurn: Boolean = false,
    var cannotUseCardsNextTurn: Boolean = false,
    var deniedCardTypeNextTurn: com.aember.analyzer.domain.model.CardType? = null,
    /** Soft Landing: the next N creatures/artifacts this player plays enter play ready. */
    var nextPlaysEnterReady: Int = 0,
    /** Phase Shift: this player may play one card of a house other than this one this turn. */
    var mayPlayNonHouseThisTurn: House? = null,
    /** Derived by EffectRegistry.recalculate (e.g. The Vaultkeeper): amber cannot be stolen. */
    var amberCannotBeStolen: Boolean = false,
    val cardsPlayedThisTurnByHouse: MutableMap<House, Int> = mutableMapOf()
) {

    fun cardsInPlay(): List<InPlayCard> = creatures + artifacts

    fun battleline(): List<InPlayCard> = creatures

    /** Total cards played this turn (all houses). */
    val cardsPlayedThisTurn: Int get() = cardsPlayedThisTurnByHouse.values.sum()

    fun hasWon(): Boolean = keys >= 3
}
