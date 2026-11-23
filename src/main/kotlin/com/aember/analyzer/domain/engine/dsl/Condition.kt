package com.aember.analyzer.domain.engine.dsl

import com.aember.analyzer.domain.model.House

sealed interface Condition

object OpponentHasMoreAmber : Condition
object SelfHasMoreAmber : Condition
object OpponentHasFewerCreatures : Condition
object SelfHasFewerCreatures : Condition
object SelfHasMoreCreatures : Condition
object TargetIsDamaged : Condition
object Always : Condition

data class OpponentAmberAtLeast(val amount: Int) : Condition
data class OpponentAmberAtMost(val amount: Int) : Condition
data class OpponentAmberExactly(val amount: Int) : Condition
data class SelfAmberAtLeast(val amount: Int) : Condition

object StealSucceeded : Condition
object DamageDestroyedTarget : Condition
object OpponentForgedKeyLastTurn : Condition

/** True when the last Sacrifice effect in this resolution removed a creature. */
object SacrificedTarget : Condition

/** True when the last PurgeCreatureFromDiscard effect purged a card (Eater of the Dead). */
object PurgedTarget : Condition

/** True when at least [amount] damage was healed by the last Heal effect in this resolution. */
data class HealedAtLeast(val amount: Int) : Condition

/**
 * True when the source player has at least one creature card in hand.
 */
object CreatureInHand : Condition

/**
 * True when the player has played at least [amount] cards of [house] during the current turn.
 */
data class CardsOfHousePlayedThisTurn(val house: House, val amount: Int) : Condition
