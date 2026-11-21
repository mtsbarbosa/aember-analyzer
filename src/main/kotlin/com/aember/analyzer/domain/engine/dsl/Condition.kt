package com.aember.analyzer.domain.engine.dsl

sealed interface Condition

object OpponentHasMoreAmber : Condition
object SelfHasMoreAmber : Condition
object OpponentHasFewerCreatures : Condition
object SelfHasFewerCreatures : Condition
object TargetIsDamaged : Condition
object Always : Condition

data class OpponentAmberAtLeast(val amount: Int) : Condition
data class OpponentAmberAtMost(val amount: Int) : Condition
data class OpponentAmberExactly(val amount: Int) : Condition
data class SelfAmberAtLeast(val amount: Int) : Condition

object StealSucceeded : Condition
object DamageDestroyedTarget : Condition
object OpponentForgedKeyLastTurn : Condition
