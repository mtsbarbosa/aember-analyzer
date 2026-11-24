package com.aember.analyzer.domain.engine

import com.aember.analyzer.domain.engine.fidelity.CardFidelity
import com.aember.analyzer.domain.engine.fidelity.FidelityLevel
import com.aember.analyzer.domain.engine.effects.StaticEffect
import com.aember.analyzer.domain.engine.events.GameEvent
import com.aember.analyzer.domain.engine.model.GameState
import com.aember.analyzer.domain.engine.model.InPlayCard
import com.aember.analyzer.domain.engine.model.PlayerState

/**
 * Implemented by every hand-ported card. Default implementations are no-ops so cards only
 * override the abilities they actually have.
 */
interface CardScript {

    fun onPlay(game: GameState, player: PlayerState, card: InPlayCard) {}

    fun onReap(game: GameState, player: PlayerState, card: InPlayCard) {}

    fun onFight(game: GameState, player: PlayerState, card: InPlayCard, target: InPlayCard) {}

    fun onBeforeFight(game: GameState, player: PlayerState, card: InPlayCard, target: InPlayCard) {}

    fun onAfterFight(game: GameState, player: PlayerState, card: InPlayCard, target: InPlayCard) {}

    /**
     * Allows a card to modify the damage it deals during the normal fight damage step.
     * Called separately for attacker and defender damage; [fightTarget] is the creature
     * this card is fighting (Valdr: +2 damage against an enemy flank creature).
     */
    fun modifyFightDamage(
        isAttacker: Boolean,
        baseDamage: Int,
        game: com.aember.analyzer.domain.engine.model.GameState? = null,
        fightTarget: com.aember.analyzer.domain.engine.model.InPlayCard? = null
    ): Int = baseDamage

    /**
     * Static ability: if true, this creature enters play ready (e.g., Silvertooth).
     */
    fun cardEntersReady(): Boolean = false

    /**
     * Static aura: if true, the first creature played by this controller each turn
     * enters play ready (e.g., Speed Sigil).
     */
    fun readiesFirstCreatureEachTurn(): Boolean = false

    /**
     * Static ability: if true, this creature enters play stunned.
     */
    fun cardEntersStunned(): Boolean = false

    /**
     * Static ability: if true, damage that would be dealt to a non-Specter neighbor is redirected to this card.
     */
    fun redirectsNeighborDamage(): Boolean = false

    /**
     * Static ability: if true, this creature ignores taunt targeting restrictions
     * and elusive when it is the attacker (Niffle Ape).
     */
    fun ignoresTauntAndElusiveWhenAttacking(): Boolean = false

    /**
     * Draw-phase modifier: while this card is in play controlled by a player, that
     * player's draw step refills their hand to [amount] additional cards (Mother: 1).
     */
    fun drawRefillBonus(): Int = 0

    /**
     * Draw-phase modifier: while this card is in play controlled by a player, the
     * opponent's draw step refills their hand to [amount] fewer cards (Succubus: 1).
     */
    fun drawRefillOpponentPenalty(): Int = 0

    /**
     * Static ability: if true, amber gained by an opponent is captured by this card instead.
     */
    fun capturesOpponentAmberGains(): Boolean = false

    /**
     * Static ability: if true, neighboring friendly creatures are considered protected.
     * Used for placement decisions (e.g., Shadow Self, taunt creatures).
     */
    fun protectsNeighbors(): Boolean = false

    /**
     * Persistent static effects projected while this card is in play
     * (power/armor/keyword auras, key-cost modifiers). Evaluated by
     * [com.aember.analyzer.domain.engine.effects.EffectRegistry] after every
     * state change.
     */
    fun staticEffects(): List<StaticEffect> = emptyList()

    /**
     * Event reaction (Phase 2.2): invoked by the [com.aember.analyzer.domain.engine.events.EventBus]
     * for every game event while this card is in play. "Each time ..." /
     * "After ..." reactions belong here; own triggers keep using onPlay/onReap/...
     */
    fun onEvent(game: GameState, player: PlayerState, card: InPlayCard, event: GameEvent) {}

    /**
     * Turn-scoped subscription reactions granted by this card (used by the
     * `ActivateTurnReaction` effect, e.g. Looter Goblin, Loot the Bodies).
     */
    fun subscriptionReactions(card: InPlayCard): List<com.aember.analyzer.domain.engine.dsl.Reaction> = emptyList()

    /**
     * Use restrictions this card projects onto players while in play
     * (Phase 2.3: cannot-play, play limits, forced house, no-steal, may-play-house).
     */
    fun useRestrictions(): List<com.aember.analyzer.domain.engine.effects.UseRestriction> = emptyList()

    /**
     * Restrictions that apply to this card itself (cannot reap, usable as
     * active house, self-destruct conditions).
     */
    fun selfRestrictions(): List<com.aember.analyzer.domain.engine.effects.SelfRestriction> = emptyList()

    /**
     * Static replacement: when true, creatures destroyed while this card is in
     * play are purged instead of discarded (e.g., Annihilation Ritual).
     */
    fun purgesDestroyedCreatures(): Boolean = false

    /**
     * Static upgrade ability: if true, the attached creature goes to its owner's archives
     * instead of being destroyed (e.g., Biomatrix Backup).
     */
    fun archivesAttachedCreatureOnDestroyed(): Boolean = false

    fun onBeforeReap(game: GameState, player: PlayerState, card: InPlayCard) {}

    fun onAfterReap(game: GameState, player: PlayerState, card: InPlayCard) {}

    fun onDestroyed(game: GameState, player: PlayerState, card: InPlayCard) {}

    /**
     * Returns true if the card can legally be played from hand right now.
     */
    fun playable(game: GameState, player: PlayerState): Boolean = true

    fun actionAbilities(): List<String> = emptyList()

    fun resolveActionAbility(abilityId: String, game: GameState, player: PlayerState, card: InPlayCard) {}

    fun fidelity(): CardFidelity = CardFidelity(level = FidelityLevel.VANILLA, notes = "No fidelity metadata")
}
