package com.aember.analyzer.domain.engine.effects

import com.aember.analyzer.domain.engine.model.GameState
import com.aember.analyzer.domain.engine.model.InPlayCard
import com.aember.analyzer.domain.engine.model.PlayerState
import com.aember.analyzer.domain.model.CardType
import com.aember.analyzer.domain.model.House

/**
 * Use restrictions projected by a card in play onto one of the players
 * (Phase 2.3). Collected by [EffectRegistry.collectUseRestrictions] and
 * consulted by `ActionGenerator` / `EffectResolver`.
 */
sealed interface UseRestriction {

    /** The controller cannot play cards of this type (e.g. Grommid: creatures). */
    data class CannotPlay(val type: CardType) : UseRestriction

    /**
     * The opponent of the controller cannot play more than [maxPerTurn] cards
     * per turn (e.g. Ember Imp).
     */
    data class PlayLimit(val maxPerTurn: Int) : UseRestriction

    /** The controller must choose [house] as active house (e.g. Pitlord). */
    data class ForcedHouse(val house: House) : UseRestriction

    /** The controller's amber cannot be stolen (e.g. The Vaultkeeper). */
    object NoSteal : UseRestriction

    /**
     * The controller may play up to [perTurn] cards of [house] per turn even
     * when it is not the active house (e.g. Witch of the Wilds).
     */
    data class MayPlayHouse(val house: House, val perTurn: Int = 1) : UseRestriction

    /**
     * The controller's opponent must pay [amount] amber to the restriction
     * source's controller to use an artifact (Tentacus).
     */
    data class ArtifactUseCost(val amount: Int) : UseRestriction

    /**
     * The controller's opponent must pay [amount] amber to the restriction
     * source's controller to play an artifact (Customs Office).
     */
    data class ArtifactPlayCost(val amount: Int) : UseRestriction
}

/**
 * Card-targeted restrictions that apply to the card itself, not to a player
 * (e.g. Tireless Crocag).
 */
sealed interface SelfRestriction {

    /** This card cannot reap. */
    object CannotReap : SelfRestriction

    /** This card may be used as if it belonged to the active house. */
    object UsableAsActiveHouse : SelfRestriction

    /** This card can only fight stunned creatures (Bigtwig). */
    object OnlyFightStunned : SelfRestriction

    /**
     * This card is destroyed while [condition] holds (checked in the engine's
     * state check, e.g. Tireless Crocag when the opponent has no creatures).
     */
    data class SelfDestruct(
        val condition: (GameState, PlayerState, InPlayCard) -> Boolean
    ) : SelfRestriction
}

/** DSL builder for `restrictions { ... }` card definitions. */
class UseRestrictionsBuilder {
    val auraRestrictions = mutableListOf<UseRestriction>()
    val selfRestrictions = mutableListOf<SelfRestriction>()

    fun cannotPlay(type: CardType) {
        auraRestrictions += UseRestriction.CannotPlay(type)
    }

    fun opponentPlayLimit(maxPerTurn: Int) {
        auraRestrictions += UseRestriction.PlayLimit(maxPerTurn)
    }

    fun forcedHouse(house: House) {
        auraRestrictions += UseRestriction.ForcedHouse(house)
    }

    fun noSteal() {
        auraRestrictions += UseRestriction.NoSteal
    }

    fun mayPlayHouse(house: House, perTurn: Int = 1) {
        auraRestrictions += UseRestriction.MayPlayHouse(house, perTurn)
    }

    fun opponentArtifactUseCost(amount: Int) {
        auraRestrictions += UseRestriction.ArtifactUseCost(amount)
    }

    fun opponentArtifactPlayCost(amount: Int) {
        auraRestrictions += UseRestriction.ArtifactPlayCost(amount)
    }

    fun selfCannotReap() {
        selfRestrictions += SelfRestriction.CannotReap
    }

    fun selfUsableAsActiveHouse() {
        selfRestrictions += SelfRestriction.UsableAsActiveHouse
    }

    fun selfOnlyFightStunned() {
        selfRestrictions += SelfRestriction.OnlyFightStunned
    }

    fun selfDestructIf(condition: (GameState, PlayerState, InPlayCard) -> Boolean) {
        selfRestrictions += SelfRestriction.SelfDestruct(condition)
    }
}
