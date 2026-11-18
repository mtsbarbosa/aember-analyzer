package com.aember.analyzer.domain.engine.dsl

sealed interface Condition

object OpponentHasMoreAmber : Condition
object SelfHasMoreAmber : Condition
object OpponentHasFewerCreatures : Condition
object SelfHasFewerCreatures : Condition
object TargetIsDamaged : Condition
object Always : Condition
