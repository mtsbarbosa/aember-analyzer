package com.aember.analyzer.domain.engine.action

import com.aember.analyzer.domain.model.House

sealed interface Action

data class ChooseHouseAction(val house: House) : Action

data class PlayCardAction(val cardId: String, val targetCreatureId: String? = null) : Action

data class UseCreatureAction(
    val instanceId: String,
    val useType: CreatureUseType
) : Action

sealed interface CreatureUseType
object ReapUse : CreatureUseType
data class FightUse(val targetInstanceId: String) : CreatureUseType
data class ActionAbilityUse(val abilityId: String) : CreatureUseType
object RemoveStunUse : CreatureUseType

data class UseArtifactAction(
    val instanceId: String,
    val abilityId: String
) : Action

data class DiscardAction(val cardId: String) : Action

object EndTurnAction : Action
