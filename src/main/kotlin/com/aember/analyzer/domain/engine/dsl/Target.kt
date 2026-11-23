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

/**
 * Selects creatures that do not belong to [house], across both players.
 */
data class CreatureNotOfHouse(val house: House, val scope: Scope = Scope.ONE) : CreatureSelector

/**
 * Selects a target creature using [base], then expands the result to include its immediate neighbors.
 */
data class CreatureAndNeighbors(val base: CreatureSelector = AnyCreature()) : CreatureSelector

/**
 * Selects the source card itself (for an upgrade, its host creature).
 */
data object SelfCreature : CreatureSelector

/**
 * Selects all undamaged creatures other than the source card.
 */
data object EachOtherUndamagedCreature : CreatureSelector

/**
 * Selects all creatures of the house with the most enemy creatures in play.
 */
data object AllCreaturesOfChosenHouse : CreatureSelector

/**
 * Selects the creature most recently damaged during the current effect resolution.
 */
data object LastDamagedTarget : CreatureSelector

/**
 * Selects a creature other than the one most recently damaged during the current
 * effect resolution (Twin Bolt Emission, Pawn Sacrifice: two different creatures).
 */
data object AnyCreatureExceptLastDamaged : CreatureSelector

/**
 * Selects the creature most recently healed during the current effect resolution
 * (Protectrix: "fully heal a creature ... that creature cannot be dealt damage").
 */
data object LastHealedTarget : CreatureSelector

/**
 * Selects the creatures adjacent to the resolving creature in its controller's
 * battleline (Sergeant Zakiel: "ready and fight with a neighboring creature").
 */
data object NeighborOfSelf : CreatureSelector

/**
 * Selects creatures that are not on a flank of their controller's battleline
 * (Booby Trap: "a creature that is not on a flank").
 */
data object CreatureNotOnFlank : CreatureSelector

sealed interface TargetPlayer
data object SelfPlayer : TargetPlayer
data object OpponentPlayer : TargetPlayer
