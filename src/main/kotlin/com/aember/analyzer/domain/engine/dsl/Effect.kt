package com.aember.analyzer.domain.engine.dsl

import com.aember.analyzer.domain.engine.model.GameState
import com.aember.analyzer.domain.engine.model.PlayerState
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

/**
 * A creature captures [amount] amber from [from]'s pool onto the selected creature.
 * The pool owner defaults to the opponent; self-capture (Pandemonium's enemy half)
 * passes [SelfPlayer] so the resolving player's own amber is locked away.
 */
data class CaptureAmber(
    val amount: Int,
    val target: CreatureSelector,
    val from: TargetPlayer = OpponentPlayer
) : Effect

data class CaptureAmberDynamic(
    val amount: (GameState, PlayerState) -> Int,
    val target: CreatureSelector,
    val from: TargetPlayer = OpponentPlayer
) : Effect

/** [maxTargets] limits how many creatures capture (Unguarded Camp: one per excess creature). */
data class CaptureEach(
    val amountPerEach: Int = 1,
    val target: CreatureSelector,
    val maxTargets: ((GameState, PlayerState) -> Int)? = null,
    val from: TargetPlayer = OpponentPlayer
) : Effect

/** The fight participant referenced by the dispatched event captures (Francus, Take Hostages). */
data class EventSubjectCaptures(val amount: Int, val subject: EventSubject = EventSubject.ATTACKER) : Effect

data class LoseAmber(val amount: Int, val player: TargetPlayer) : Effect

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

data class StunAndExhaust(val target: CreatureSelector) : Effect

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

/** Nothing is archived when no creature is in hand (Zyzzix the Many). */
data class ArchiveCreatureFromHand(val amount: Int, val player: TargetPlayer) : Effect

data class LoseAllAmber(val player: TargetPlayer) : Effect

data class GainAmberOpponent(val amount: Int) : Effect

data class LoseAmberOpponent(val amount: Int) : Effect

object DenyCardUseNextTurn : Effect

object DenyActionCardsNextTurn : Effect

object DenyCreaturePlaysNextTurn : Effect

object DenyCreatureFightsNextTurn : Effect

/** Each player with [threshold] or more amber is reduced to [cap] (Doorstep to Heaven). */
data class ReduceAmberTo(val threshold: Int, val cap: Int) : Effect

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

/** Amber moves from [from] to that player's opponent (Magda the Rat's "Leaves Play"). */
data class StealFrom(val amount: Int, val from: TargetPlayer) : Effect

/** Activates the source card's turn-scoped reaction subscriptions (Looter Goblin). */
data class ActivateTurnReaction(val tag: String) : Effect

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

data class AddPowerCounters(val amount: Int, val target: CreatureSelector = SelfCreature) : Effect

data class AddPowerCountersDynamic(
    val amount: (GameState, PlayerState) -> Int,
    val target: CreatureSelector
) : Effect

data object SacrificeSelf : Effect

/** Sets `SacrificedTarget` so follow-up effects can gate on the sacrifice. */
data class Sacrifice(val target: CreatureSelector) : Effect

/** Resolves [effect] repeatedly while [condition] holds, up to [maxRepeats] (Bait and Switch). */
data class RepeatWhile(val condition: Condition, val effect: Effect, val maxRepeats: Int = 20) : Effect

/** Both players lose half their amber, rounding the loss down (Effervescent Principle). */
data object LoseHalfAmber : Effect

data class ShuffleCreaturesIntoDeck(val friendlyCount: Int = 2, val enemyCount: Int = 2) : Effect

data object PutSelfOnTopOfDeck : Effect

/** The immunity lasts for the remainder of the turn (Protectrix, Shield of Justice). */
data class GrantDamageImmunity(val target: CreatureSelector) : Effect

data class NextPlaysEnterReady(val count: Int) : Effect

/** Heals every creature in play and gains [gainPerHealed] per creature actually healed (Cleansing Wave). */
data class HealEach(val amount: Int = 1, val gainPerHealed: Int = 0) : Effect

/** Greedy: the house with the most creatures in the discard pile (Arise!). */
data class ReturnChosenHouseCreaturesFromDiscard(val gainChains: Int = 0) : Effect

/** Greedy: the opponent's artifact first, then their strongest flank creature (Snudge). */
data object ReturnArtifactOrFlankCreatureToHand : Effect

/** Uses the opponent's first ready artifact with an Action ability as if it were ours (Nexus, Remote Access). */
data object UseOpponentArtifact : Effect

/** Greedy reveal: all [house] cards in hand, dealing their count as damage (Mothergun). */
data class DealDamagePerHouseCardInHand(val house: House, val target: CreatureSelector) : Effect

/** Sets `lastPurgedFromDiscard` so [PurgedTarget] can gate follow-up effects (Eater of the Dead). */
data object PurgeCreatureFromDiscard : Effect

/**
 * One off-house non-[notHouse] play this turn (Phase Shift); consumed by
 * `ActionGenerator`, spent by the first such play.
 */
data class MayPlayNonHouseThisTurn(val notHouse: House) : Effect

// ---- Phase 3 batch (d) ----

data class DestroyArtifact(val target: TargetSelector = AnyArtifact) : Effect

data class ReturnArtifactToHand(val maxCount: Int = 1) : Effect

data object ArchiveFriendlyCreatureOrArtifact : Effect

/** Reads the fight target from `currentFightTargetId` (Lord Golgotha). */
data class DealDamageToFightsNeighbors(val amount: Int) : Effect

/** Before Fight (Zorg). */
data object StunFightTargetAndNeighbors : Effect

data class DealDamageIgnoringArmor(val amount: Int, val target: CreatureSelector) : Effect

/** 3/2/1 damage along a battleline starting at a flank creature (Positron Bolt). */
data object DealDamageFlankCascade : Effect

data class HealThenDamageOther(val upTo: Int) : Effect

data class DealDamagePerHouseInPlay(val house: House, val amountPer: Int, val target: CreatureSelector) : Effect

data class DealDividedDamage(val amountPerFriendlyCreature: Int, val target: CreatureSelector) : Effect

data object StripEnemyArmorAndDamagePerLost : Effect

/** The amber comes from the common supply, not from either player's pool (Blood Money). */
data class PlaceAmberOnEnemyCreature(val amount: Int) : Effect

data object MoveAmberFromOwnCardToPool : Effect

data object LoseAllAmberAndOpponentLosesTriple : Effect

data class ShuffleHouseCreaturesIntoDeck(val house: House, val amberPerEach: Int = 0) : Effect

/** Greedy house: the one with the most enemy creatures in hand (Deep Probe). */
data object DiscardEnemyHandCreaturesOfChosenHouse : Effect

/** Greedy house: the one with the most copies in own deck (Chaos Portal). */
data object PlayTopCardIfChosenHouse : Effect

data class ScanDeckForHouseCreatureToHand(val house: House) : Effect

/** Greedy: ready an own exhausted Mars creature, else stun an enemy creature (Squawker). */
data object ReadyMarsOrStunNonMars : Effect

data object StackHandCardBeneath : Effect

data object PlayBeneathCardAndSacrifice : Effect

data object DestroyAllCreaturesAndArtifacts : Effect

data object EmpBlastBurst : Effect

data object ReturnUrchinToHand : Effect

data class MayUseHouseThisTurn(val house: House) : Effect

data object AllowUseUpToTwoMarsCards : Effect

data object OpponentNextForgeGivesAmber : Effect

// ---- Phase 3 batch (e) ----

data object PlayCopyOfOpponentDiscardAction : Effect

data class SearchForCreatureToHand(
    val cardName: String,
    val shuffleDiscardIntoDeck: Boolean = false,
    val maxCards: Int = Int.MAX_VALUE
) : Effect

/** Fully heals every [cardName] in play; if none, searches deck and discard for them (Bear Flute). */
data class HealOrSearchAncientCreatures(val cardName: String) : Effect

data class NextCreatureOfHouseEntersReady(val house: House) : Effect

data class DealDamageToEventCreature(val amount: Int) : Effect

data class CreatureWithoutTrait(val trait: String, val scope: Scope = Scope.ALL) : CreatureSelector

/** The triggered creature does not exhaust (Replicator). */
data object TriggerOtherCreatureReap : Effect

data object RevealOpponentHand : Effect

data class PurgeEnemyHandCardOfHouse(val house: House) : Effect

data class ArchiveHouseCreatureFromHand(val house: House) : Effect

data object ArchiveSelf : Effect

/** Abducted cards return to their owner's hand when leaving the archives (Mass Abduction). */
data class AbductEnemyDamagedCreatures(val amount: Int) : Effect

/** [gainAmber] > 0 purges the destroyed creature instead of claiming it (Stealer of Souls). */
data class ClaimDestroyedFightTarget(val gainAmber: Int = 0) : Effect

data object SwapWithOtherFriendlyCreature : Effect

/** Upgrade action: swaps the host with another friendly creature (Transposition Sandals). */
data object SwapHostAndMayUseOther : Effect

data class RotateHouseCreatures(val house: House) : Effect

data object UseArtifactAndDestroyIt : Effect

data object ReturnDiscardCreatureToTopOfDeck : Effect

/** Exhausted creatures included (Follow the Leader). */
data object AllCreaturesMayFightThisTurn : Effect

/** Greedy house: the one with the most friendly creatures (Brothers in Battle). */
data class ChosenHouseMayFightThisTurn(val house: House? = null) : Effect

/** Usable as the active house, but only fight uses are offered (Horseman of War). */
data object AllCreaturesFightOnlyThisTurn : Effect

data object ReapStealsInstead : Effect

data class AllowUseFriendlyCreaturesThisTurn(val count: Int) : Effect

data class GrantSkirmishAndFight(val count: Int) : Effect

data object LoseElusiveThisTurn : Effect

/** Excludes the fight target and the source card (Gabos Longarms). */
data object ChangeFightDamageTarget : Effect

/** Discards the top card of the attacker's controller's deck; cancels the fight when it matches the active house (Evasion Sigil). */
data object EvasionSigilDiscardAndCancel : Effect

data object PlayCreatureFromDiscard : Effect

data object HealSelfFully : Effect

data object PurgeFriendlyHumanAndPlayFromDiscard : Effect

data object ReturnAllArtifactsToTopOfDeck : Effect

/** Removes the creature immediately instead of waiting for the state-check fixpoint (Numquid's repeat loop). */
data object DestroyCreatureNow : Effect

data class Sequence(val effects: List<Effect>) : Effect

data class Conditional(val condition: Condition, val thenEffect: Effect, val elseEffect: Effect = NoEffect) : Effect

data class RepeatForEach(val target: TargetSelector, val effectFactory: (InPlayCardRef) -> Effect) : Effect

// Reference used inside repeat lambdas
interface InPlayCardRef
