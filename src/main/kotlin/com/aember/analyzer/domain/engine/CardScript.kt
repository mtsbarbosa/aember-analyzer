package com.aember.analyzer.domain.engine

import com.aember.analyzer.domain.engine.fidelity.CardFidelity
import com.aember.analyzer.domain.engine.fidelity.FidelityLevel
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
     * Called separately for attacker and defender damage.
     */
    fun modifyFightDamage(isAttacker: Boolean, baseDamage: Int): Int = baseDamage

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
     * Static ability: if true, amber gained by an opponent is captured by this card instead.
     */
    fun capturesOpponentAmberGains(): Boolean = false

    /**
     * Static ability: if true, neighboring friendly creatures are considered protected.
     * Used for placement decisions (e.g., Shadow Self, taunt creatures).
     */
    fun protectsNeighbors(): Boolean = false

    /**
     * Static upgrade ability: the controller of the attached creature's opponent pays this
     * much extra amber to forge keys (e.g., Jammer Pack).
     */
    fun opponentKeyCostModifier(): Int = 0

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
