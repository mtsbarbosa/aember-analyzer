package com.aember.analyzer.domain.engine.effects

import com.aember.analyzer.domain.engine.model.GameState
import com.aember.analyzer.domain.engine.model.InPlayCard
import com.aember.analyzer.domain.engine.model.Keyword
import com.aember.analyzer.domain.engine.model.PlayerState
import com.aember.analyzer.domain.model.House

/**
 * Kinds of persistent static effects a card in play can project.
 */
enum class StaticEffectKind {
    MODIFY_POWER,
    MODIFY_ARMOR,
    ADD_KEYWORD,
    KEY_COST_MODIFIER
}

/**
 * Which creatures a card-targeted static effect applies to.
 */
sealed interface CreatureScope

object SelfScope : CreatureScope
object NeighborsOfSelfScope : CreatureScope
object FriendlyCreaturesScope : CreatureScope
data class OtherFriendlyCreaturesScope(val house: House? = null, val trait: String? = null) : CreatureScope
object EnemyCreaturesScope : CreatureScope
object AllCreaturesScope : CreatureScope
object FriendlyFlankCreaturesScope : CreatureScope
data class FriendlyOfHouseScope(val house: House) : CreatureScope
data class EnemyOfHouseScope(val house: House) : CreatureScope
data class FriendlyWithTraitScope(val trait: String) : CreatureScope

/** For upgrades: the creature this upgrade is attached to. */
object HostCreatureScope : CreatureScope

/**
 * Who pays the modified key cost.
 */
enum class PayerScope { CONTROLLER, OPPONENT_OF_CONTROLLER, ANY }

/**
 * A single persistent static effect projected by a card in play.
 * The registry evaluates these on every state change (see [EffectRegistry]).
 */
data class StaticEffect(
    val kind: StaticEffectKind,
    val scope: CreatureScope? = null,
    val keyword: Keyword? = null,
    val amount: Int = 0,
    /** Optional dynamic amount (e.g. "+1 power for each amber on it"). */
    val dynamicAmount: ((GameState, InPlayCard) -> Int)? = null,
    val payer: PayerScope = PayerScope.CONTROLLER,
    /** Require the source (or its host) to be on a flank of its controller's battleline. */
    val requireFlank: Boolean = false
)

/**
 * DSL builder for static effects, used from `persistent { ... }` card definitions.
 */
class StaticEffectsBuilder {
    val effects = mutableListOf<StaticEffect>()

    fun modifyPower(amount: Int, scope: CreatureScope, requireFlank: Boolean = false) {
        effects += StaticEffect(StaticEffectKind.MODIFY_POWER, scope = scope, amount = amount, requireFlank = requireFlank)
    }

    fun modifyPower(scope: CreatureScope, dynamic: (GameState, InPlayCard) -> Int) {
        effects += StaticEffect(StaticEffectKind.MODIFY_POWER, scope = scope, dynamicAmount = dynamic)
    }

    fun modifyArmor(amount: Int, scope: CreatureScope, requireFlank: Boolean = false) {
        effects += StaticEffect(StaticEffectKind.MODIFY_ARMOR, scope = scope, amount = amount, requireFlank = requireFlank)
    }

    fun addKeyword(keyword: Keyword, value: Int = 1, scope: CreatureScope) {
        effects += StaticEffect(StaticEffectKind.ADD_KEYWORD, scope = scope, keyword = keyword, amount = value)
    }

    fun modifyKeyCost(amount: Int, payer: PayerScope, requireFlank: Boolean = false) {
        effects += StaticEffect(
            StaticEffectKind.KEY_COST_MODIFIER,
            amount = amount,
            payer = payer,
            requireFlank = requireFlank
        )
    }

    /** Key-cost modifier whose amount is computed per state (e.g. Iron Obelisk). */
    fun modifyKeyCostDynamic(
        payer: PayerScope,
        dynamic: (GameState, InPlayCard) -> Int
    ) {
        effects += StaticEffect(
            StaticEffectKind.KEY_COST_MODIFIER,
            payer = payer,
            dynamicAmount = dynamic
        )
    }
}
