package com.aember.analyzer.domain.engine.dsl

import com.aember.analyzer.domain.model.House

sealed interface Effect

object NoEffect : Effect

data class GainAmber(val amount: Int) : Effect

enum class TurnTrigger {
    FRIENDLY_CREATURE_FIGHTS,
    FRIENDLY_CREATURE_REAPS,
    CREATURE_DESTROYED
}

data class GainAmberOnTriggerThisTurn(val amount: Int, val trigger: TurnTrigger) : Effect

data class DrawOnCardPlayThisTurn(val house: House? = null, val amount: Int = 1) : Effect

data class DenyHouseNextTurn(val house: House, val target: TargetPlayer) : Effect

data class TakeControlUntilEndOfTurn(val target: CreatureSelector) : Effect

data class PlayTopCardOfHouse(val house: House, val player: TargetPlayer) : Effect

data class AddTopCardOfHouseToHand(val house: House, val player: TargetPlayer) : Effect

data class DiscardTopDeck(val player: TargetPlayer) : Effect

data class GainAmberEqualToOpponentKeys(val player: TargetPlayer) : Effect

data class TakeControlOfEnemyFlankCreature(val player: TargetPlayer) : Effect

data class SkipForgeNextTurn(val player: TargetPlayer) : Effect

data class ReturnCreaturesOfHouseFromDiscardToHand(val player: TargetPlayer) : Effect

data class StealAmber(val amount: Int) : Effect

data class CaptureAmber(val amount: Int, val target: CreatureSelector) : Effect

data class LoseAmber(val amount: Int, val player: TargetPlayer) : Effect

data class DealDamage(val amount: Int, val target: CreatureSelector) : Effect

data class Destroy(val target: CreatureSelector) : Effect

data class Draw(val amount: Int, val player: TargetPlayer) : Effect

data class DiscardRandom(val amount: Int, val player: TargetPlayer) : Effect

data class ReturnToHand(val target: CreatureSelector) : Effect

data class Archive(val target: CreatureSelector) : Effect

data class Purge(val target: CreatureSelector) : Effect

data class Heal(val amount: Int, val target: CreatureSelector) : Effect

data class Ready(val target: CreatureSelector) : Effect

data class Fight(val attacker: CreatureSelector, val defender: CreatureSelector) : Effect

data class GainChains(val amount: Int, val player: TargetPlayer) : Effect

data class LoseChains(val amount: Int, val player: TargetPlayer) : Effect

data class Stun(val target: CreatureSelector) : Effect

data class Unstun(val target: CreatureSelector) : Effect

data class DestroyMostPowerful(val amount: Int) : Effect

data class DestroyLeastPowerful(val amount: Int) : Effect

data class DestroyWeakCreatures(val powerThreshold: Int) : Effect

data class ReadyAndFight(val target: CreatureSelector) : Effect

data class ReadyAndUse(val target: CreatureSelector) : Effect

data class UseCreature(val target: CreatureSelector) : Effect

data class ReturnFromDiscardToHand(val amount: Int, val player: TargetPlayer) : Effect

data class PlayTopCard(val player: TargetPlayer) : Effect

data class SwapDeckAndDiscard(val player: TargetPlayer) : Effect

data class PurgeFromDiscard(val amount: Int, val player: TargetPlayer) : Effect

data class ArchiveTopDeck(val player: TargetPlayer) : Effect

data class ArchiveFromDiscard(val amount: Int, val player: TargetPlayer) : Effect

data class ArchiveFromHand(val amount: Int, val player: TargetPlayer) : Effect

data class LoseAllAmber(val player: TargetPlayer) : Effect

data class GainAmberOpponent(val amount: Int) : Effect

data class Exhaust(val target: CreatureSelector) : Effect

data class ShuffleHandAndDiscardIntoDeck(val player: TargetPlayer) : Effect

data class ReturnAllCreaturesOfHouseToHand(val house: House) : Effect

data class ForgeKey(val extraCost: Int, val discountPerCardInHand: Boolean) : Effect

data class DrawPerCardInHand(val house: House, val player: TargetPlayer) : Effect

data class ReadyCreaturesOfHousePerCardInHand(val house: House, val player: TargetPlayer) : Effect

data class AddPowerCounters(val amount: Int) : Effect

data object SacrificeSelf : Effect

data class Sequence(val effects: List<Effect>) : Effect

data class Conditional(val condition: Condition, val thenEffect: Effect, val elseEffect: Effect = NoEffect) : Effect

data class RepeatForEach(val target: TargetSelector, val effectFactory: (InPlayCardRef) -> Effect) : Effect

// Reference used inside repeat lambdas
interface InPlayCardRef
