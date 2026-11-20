package com.aember.analyzer.domain.engine.dsl

sealed interface Effect

object NoEffect : Effect

data class GainAmber(val amount: Int) : Effect

enum class TurnTrigger {
    FRIENDLY_CREATURE_FIGHTS,
    FRIENDLY_CREATURE_REAPS,
    CREATURE_DESTROYED
}

data class GainAmberOnTriggerThisTurn(val amount: Int, val trigger: TurnTrigger) : Effect

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

data class Sequence(val effects: List<Effect>) : Effect

data class Conditional(val condition: Condition, val thenEffect: Effect, val elseEffect: Effect = NoEffect) : Effect

data class RepeatForEach(val target: TargetSelector, val effectFactory: (InPlayCardRef) -> Effect) : Effect

// Reference used inside repeat lambdas
interface InPlayCardRef
