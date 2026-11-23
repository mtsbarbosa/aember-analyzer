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

/**
 * Deals [amount] damage to each selected creature and [splash] damage to its neighbors
 * (e.g. Lava Ball deals 4 with 2 splash).
 */
data class DealDamage(val amount: Int, val target: CreatureSelector, val splash: Int = 0) : Effect

data class Destroy(val target: CreatureSelector) : Effect

data class Draw(val amount: Int, val player: TargetPlayer) : Effect

data class DiscardRandom(val amount: Int, val player: TargetPlayer) : Effect

data class ReturnToHand(val target: CreatureSelector) : Effect

data class Archive(val target: CreatureSelector) : Effect

data class Purge(val target: CreatureSelector) : Effect

data class Heal(val amount: Int, val target: CreatureSelector, val fully: Boolean = false) : Effect

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

/**
 * Archives a creature from hand. When no creature is in hand nothing is archived
 * (e.g., Zyzzix the Many's reap/fight ability).
 */
data class ArchiveCreatureFromHand(val amount: Int, val player: TargetPlayer) : Effect

data class LoseAllAmber(val player: TargetPlayer) : Effect

data class GainAmberOpponent(val amount: Int) : Effect

/** The opponent of the player resolving this effect loses amber (min 0). */
data class LoseAmberOpponent(val amount: Int) : Effect

/** Next-turn denials (Skippy Timehog, Scrambler Storm, Foggify). */
object DenyCardUseNextTurn : Effect

object DenyActionCardsNextTurn : Effect

object DenyCreatureFightsNextTurn : Effect

/** Each player with [threshold] or more amber is reduced to [cap] (Doorstep to Heaven). */
data class ReduceAmberTo(val threshold: Int, val cap: Int) : Effect

/** Shuffles the card resolving this effect from play into its owner's deck. */
data object ShuffleSelfIntoDeck : Effect

/**
 * Amber movement tied to the creature referenced by the event currently being
 * dispatched (Phase 2.2 reactions, e.g. Soul Snatcher / Tolas / Krump).
 */
enum class EventSide { OWNER, CONTROLLER, CONTROLLER_OPPONENT }

/** Which fight participant the effect refers to when the event is a FightResolved. */
enum class EventSubject { EVENT_CREATURE, DEFENDER, ATTACKER }

data class EventSideGainsAmber(
    val amount: Int,
    val side: EventSide,
    val subject: EventSubject = EventSubject.EVENT_CREATURE
) : Effect

data class EventSideLosesAmber(
    val amount: Int,
    val side: EventSide,
    val subject: EventSubject = EventSubject.EVENT_CREATURE
) : Effect

/**
 * Amber moves from [from] (relative to the resolving player) to that player's
 * opponent (e.g. Magda the Rat's "Leaves Play: your opponent steals 2A").
 */
data class StealFrom(val amount: Int, val from: TargetPlayer) : Effect

/**
 * Activates a turn-scoped event subscription for the source card (e.g. Looter
 * Goblin "for the remainder of the turn"); consumed by reaction matchers via
 * `PlayerState.eventReactionsThisTurn`.
 */
data class ActivateTurnReaction(val tag: String) : Effect

/** Readies the host creature of an upgrade and optionally changes its house until end of turn. */
data class ReadyHost(val houseUntilEndOfTurn: House? = null) : Effect

data class Exhaust(val target: CreatureSelector, val count: Int? = null) : Effect

data class ShuffleHandAndDiscardIntoDeck(val player: TargetPlayer) : Effect

data class ReturnAllCreaturesOfHouseToHand(val house: House) : Effect

/**
 * Forges a key outside the key forge step. [extraCost] modifies the current key
 * cost; [free] forges at no cost regardless of key-cost auras (e.g., Epic Quest).
 * [discountPerCardInHand] reduces the cost by 1 per card in hand (e.g., Key Abduction).
 */
data class ForgeKey(
    val extraCost: Int = 0,
    val discountPerCardInHand: Boolean = false,
    val free: Boolean = false
) : Effect

data class DrawPerCardInHand(val house: House, val player: TargetPlayer) : Effect

data class ReadyCreaturesOfHousePerCardInHand(val house: House, val player: TargetPlayer) : Effect

data class AddPowerCounters(val amount: Int) : Effect

data object SacrificeSelf : Effect

/** Sacrifices a chosen friendly creature in play (Pawn Sacrifice); sets SacrificedTarget. */
data class Sacrifice(val target: CreatureSelector) : Effect

/**
 * Resolves [effect] repeatedly while [condition] holds, up to [maxRepeats]
 * (Bait and Switch: steal 1 amber while the opponent has more).
 */
data class RepeatWhile(val condition: Condition, val effect: Effect, val maxRepeats: Int = 20) : Effect

/** Each player loses half their amber, rounding down the loss (Effervescent Principle). */
data object LoseHalfAmber : Effect

/**
 * Shuffles up to [friendlyCount] friendly and [enemyCount] enemy creatures into
 * their owners' decks (Lost in the Woods).
 */
data class ShuffleCreaturesIntoDeck(val friendlyCount: Int = 2, val enemyCount: Int = 2) : Effect

/** Moves the resolving card from play to the top of its owner's deck (Dextre). */
data object PutSelfOnTopOfDeck : Effect

/** Chosen creatures cannot be dealt damage for the remainder of the turn. */
data class GrantDamageImmunity(val target: CreatureSelector) : Effect

/** The next [count] creatures/artifacts the resolving player plays enter play ready (Soft Landing). */
data class NextPlaysEnterReady(val count: Int) : Effect

data class Sequence(val effects: List<Effect>) : Effect

data class Conditional(val condition: Condition, val thenEffect: Effect, val elseEffect: Effect = NoEffect) : Effect

data class RepeatForEach(val target: TargetSelector, val effectFactory: (InPlayCardRef) -> Effect) : Effect

// Reference used inside repeat lambdas
interface InPlayCardRef
