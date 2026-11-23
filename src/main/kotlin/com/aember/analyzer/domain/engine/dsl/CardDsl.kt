package com.aember.analyzer.domain.engine.dsl

import com.aember.analyzer.domain.engine.events.GameEvent
import com.aember.analyzer.domain.engine.effects.SelfRestriction
import com.aember.analyzer.domain.engine.effects.StaticEffectsBuilder
import com.aember.analyzer.domain.engine.effects.UseRestriction
import com.aember.analyzer.domain.engine.effects.UseRestrictionsBuilder
import com.aember.analyzer.domain.engine.fidelity.CardFidelity
import com.aember.analyzer.domain.engine.fidelity.FidelityLevel
import com.aember.analyzer.domain.engine.model.GameState
import com.aember.analyzer.domain.engine.model.InPlayCard
import com.aember.analyzer.domain.engine.model.PlayerState

class CardDefinition(
    val cardId: String,
    var playEffect: Effect = NoEffect,
    var playCondition: Condition? = null,
    var reapEffect: Effect = NoEffect,
    var fightEffect: Effect = NoEffect,
    var destroyedEffect: Effect = NoEffect,
    var beforeFightEffect: Effect = NoEffect,
    var afterFightEffect: Effect = NoEffect,
    var beforeReapEffect: Effect = NoEffect,
    var afterReapEffect: Effect = NoEffect,
    var omniEffect: Effect = NoEffect,
    var actionAbilities: List<Pair<String, Effect>> = emptyList(),
    var staticEffects: List<com.aember.analyzer.domain.engine.effects.StaticEffect> = emptyList(),
    var useRestrictions: List<UseRestriction> = emptyList(),
    var selfRestrictions: List<SelfRestriction> = emptyList(),
    var entersPlayStunned: Boolean = false,
    var reactions: List<Reaction> = emptyList(),
    var fidelity: CardFidelity = CardFidelity(level = FidelityLevel.VANILLA, notes = "")
)

/**
 * An event reaction ("Each time ...", "After ...") declared on a card. The
 * matcher receives the game, the reacting card's controller, the card itself
 * and the event; when it matches, [effect] resolves for that controller.
 *
 * `subscription = true` marks turn-scoped reactions ("for the remainder of the
 * turn ..."): they are activated by the `ActivateTurnReaction` effect and fire
 * from the game-level subscription list instead of the in-play dispatch, so
 * they also work for action cards that left play.
 */
class Reaction(
    val notes: String,
    val matches: (GameState, PlayerState, InPlayCard, GameEvent) -> Boolean,
    val effect: Effect,
    val subscription: Boolean = false
)

/**
 * A turn-scoped event subscription registered by `ActivateTurnReaction` and
 * fired by the EventBus until the end of the activating turn. `check` is the
 * per-event matcher; `fire` resolves the reaction effect.
 */
class TurnScopedReaction(
    val ownerIndex: Int,
    val activeThroughTurn: Int,
    val check: (GameEvent) -> Boolean,
    val fire: (GameState, PlayerState) -> Unit
)

fun CardDefinition.reaction(
    notes: String = "",
    effect: Effect,
    subscription: Boolean = false,
    matches: (GameState, PlayerState, InPlayCard, GameEvent) -> Boolean
) {
    reactions = reactions + Reaction(notes, matches, effect, subscription)
}

fun card(cardId: String, block: CardDefinition.() -> Unit): Pair<String, CardDefinition> {
    val def = CardDefinition(cardId)
    def.block()
    return cardId to def
}

fun CardDefinition.play(effect: Effect) {
    playEffect = effect
}

fun CardDefinition.playableIf(condition: Condition) {
    playCondition = condition
}

fun CardDefinition.reap(effect: Effect) {
    reapEffect = effect
}

fun CardDefinition.fight(effect: Effect) {
    fightEffect = effect
}

fun CardDefinition.destroyed(effect: Effect) {
    destroyedEffect = effect
}

fun CardDefinition.beforeFight(effect: Effect) {
    beforeFightEffect = effect
}

fun CardDefinition.afterFight(effect: Effect) {
    afterFightEffect = effect
}

fun CardDefinition.beforeReap(effect: Effect) {
    beforeReapEffect = effect
}

fun CardDefinition.afterReap(effect: Effect) {
    afterReapEffect = effect
}

fun CardDefinition.omni(effect: Effect) {
    omniEffect = effect
}

fun CardDefinition.action(name: String, effect: Effect) {
    actionAbilities = actionAbilities + (name to effect)
}

/**
 * Declares the persistent static effects this card projects while in play
 * (auras, keyword grants, key-cost modifiers).
 */
fun CardDefinition.persistent(block: StaticEffectsBuilder.() -> Unit) {
    staticEffects = StaticEffectsBuilder().apply(block).effects
}

/**
 * Declares the use restrictions this card enforces while in play
 * (Phase 2.3): player-level restrictions (cannot-play, play limits, forced
 * house, no-steal, may-play-house) and card-self restrictions (cannot reap,
 * usable as active house, self-destruct conditions).
 */
fun CardDefinition.restrictions(block: UseRestrictionsBuilder.() -> Unit) {
    val builder = UseRestrictionsBuilder().apply(block)
    useRestrictions = builder.auraRestrictions
    selfRestrictions = builder.selfRestrictions
}

/** This creature enters play stunned (e.g. Chuff Ape). */
fun CardDefinition.entersPlayStunned() {
    entersPlayStunned = true
}

fun CardDefinition.fidelity(level: FidelityLevel, notes: String = "") {
    fidelity = CardFidelity(level = level, notes = notes, cardId = cardId)
}
