package com.aember.analyzer.domain.engine.dsl

import com.aember.analyzer.domain.model.House

sealed interface TargetSelector

object Self : TargetSelector

enum class Scope { ONE, ALL }

sealed interface CreatureSelector : TargetSelector

data class FriendlyCreature(val scope: Scope = Scope.ONE) : CreatureSelector
data class EnemyCreature(val scope: Scope = Scope.ONE) : CreatureSelector
data class AnyCreature(val scope: Scope = Scope.ONE) : CreatureSelector
data class FriendlyCreatureWithTrait(val trait: String, val scope: Scope = Scope.ONE) : CreatureSelector
data class EnemyCreatureWithTrait(val trait: String, val scope: Scope = Scope.ONE) : CreatureSelector
data class CreatureWithKeyword(val keyword: String, val scope: Scope = Scope.ONE) : CreatureSelector
data class FriendlyCreatureWithHouse(val house: House, val scope: Scope = Scope.ONE) : CreatureSelector
data class OtherFriendlyCreatureWithHouse(val house: House, val scope: Scope = Scope.ONE) : CreatureSelector

sealed interface TargetPlayer
data object SelfPlayer : TargetPlayer
data object OpponentPlayer : TargetPlayer
