package com.aember.analyzer.domain.engine.events

import com.aember.analyzer.domain.model.CardType
import com.aember.analyzer.domain.model.House

/**
 * Typed game events used by the reaction system (Phase 2.2). Emitted by the
 * engine after the underlying state change resolves; dispatched by
 * [EventBus] to every card currently in play.
 */
sealed interface GameEvent

data class CardPlayed(
    val playerIndex: Int,
    val cardId: String,
    val type: CardType,
    val house: House
) : GameEvent

data class CreatureEnteredPlay(
    val playerIndex: Int,
    val instanceId: String,
    val cardId: String,
    val house: House
) : GameEvent

data class CreatureReaped(
    val playerIndex: Int,
    val instanceId: String,
    val cardId: String
) : GameEvent

/**
 * A fight fully resolved. Emitted at the end of the fight, before the
 * post-action state check destroys moribund creatures. `defenderDestroyed` /
 * `attackerDestroyed` flag creatures that are moribund (lethal damage, aura
 * included) and will be destroyed by the state check.
 */
data class FightResolved(
    val attackerInstanceId: String,
    val attackerCardId: String,
    val attackerController: Int,
    val defenderInstanceId: String,
    val defenderCardId: String,
    val defenderController: Int,
    val defenderDestroyed: Boolean,
    val attackerDestroyed: Boolean
) : GameEvent

/**
 * A creature was destroyed and removed from play. Emitted after removal;
 * the destroyed card itself no longer receives reactions.
 */
data class CreatureDestroyed(
    val instanceId: String,
    val cardId: String,
    val controllerIndex: Int,
    val ownerIndex: Int
) : GameEvent

/** A card left play (destroyed, returned to hand, purged, sacrificed). */
data class CardLeftPlay(
    val instanceId: String,
    val cardId: String,
    val ownerIndex: Int,
    val controllerIndex: Int
) : GameEvent

data class KeyForged(
    val playerIndex: Int,
    val cost: Int
) : GameEvent

/** A player used an artifact's Action/Omni ability (Veylan Analyst: gain 1A per use). */
data class ArtifactUsed(
    val playerIndex: Int,
    val instanceId: String,
    val cardId: String,
    val abilityId: String
) : GameEvent

data class TurnEnded(
    val playerIndex: Int
) : GameEvent

data class HouseChosen(
    val playerIndex: Int,
    val house: House
) : GameEvent

data class CardDiscarded(
    val playerIndex: Int,
    val cardId: String,
    val house: House
) : GameEvent

/**
 * A fight is about to resolve (Evasion Sigil: discard the top card of the
 * attacker's controller's deck and possibly cancel the fight).
 */
data class FightDeclared(
    val attackerInstanceId: String,
    val attackerCardId: String,
    val defenderInstanceId: String,
    val defenderCardId: String,
    val attackerController: Int
) : GameEvent
