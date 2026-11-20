package com.aember.analyzer.domain.engine.dsl

import com.aember.analyzer.domain.engine.fidelity.CardFidelity
import com.aember.analyzer.domain.engine.fidelity.FidelityLevel

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
    var fidelity: CardFidelity = CardFidelity(level = FidelityLevel.VANILLA, notes = "")
)

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

fun CardDefinition.fidelity(level: FidelityLevel, notes: String = "") {
    fidelity = CardFidelity(level = level, notes = notes, cardId = cardId)
}
