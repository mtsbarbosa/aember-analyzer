package com.aember.analyzer.domain.engine

import com.aember.analyzer.domain.engine.events.EventBus
import com.aember.analyzer.domain.engine.events.KeyForged
import com.aember.analyzer.domain.engine.model.EndCause
import com.aember.analyzer.domain.engine.model.GameState
import com.aember.analyzer.domain.engine.model.PlayerState
import kotlin.math.ceil

/**
 * Turn-phase logic shared between [GameEngine] and the scenario test harness, so both run
 * the exact same ready/draw/end/forge rules. Internal: production code enters through
 * [GameEngine], tests through the scenario DSL.
 */
internal object TurnPhases {

    /** Per-turn flag reset plus the key (forge) phase; run at the start of every turn. */
    fun startTurn(game: GameState, library: CardLibrary) {
        game.players.forEach { p ->
            p.creatures.forEach {
                it.elusiveUsed = false
                // Restore temporary house changes (e.g. Brain Stem Antenna).
                if (it.house != it.baseHouse) it.house = it.baseHouse
            }
            p.turnTriggerAmber.clear()
            p.creaturesPlayedThisTurn = 0
            p.drawOnCardPlayActive = false
            p.drawOnCardPlayHouse = null
            p.forgedKeyThisTurn = false
            p.cardsPlayedThisTurnByHouse.clear()
        }
        keyPhase(game, library)
    }

    fun keyPhase(game: GameState, library: CardLibrary) {
        val player = game.activePlayer()
        // The one-turn bonus applies during this turn only; consume it even when
        // the forge step is skipped (e.g. Miasma), so it cannot leak to later turns.
        val cost = ForgeRules.keyCost(library, game, player) + player.keyCostNextTurnBonus
        player.keyCostNextTurnBonus = 0
        if (player.skipsForgeNextTurn) {
            player.skipsForgeNextTurn = false
            game.log("${player.name} cannot forge a key this turn")
            return
        }
        while (canForgeKey(player, cost)) {
            val before = player.amber
            player.amber -= cost
            player.keys += 1
            player.forgedKeyThisTurn = true
            player.statistics.keysForged++
            game.log("${player.name} forges a key! Keys: ${player.keys} (${player.name}: $before -> ${player.amber})")
            EventBus.emit(library, game, KeyForged(player.index, cost))
            if (!player.canForgeSecondKey) break
            player.canForgeSecondKey = false
        }
        if (player.amber < cost) {
            game.log("${player.name} does not forge a key (amber: ${player.amber})")
        }
    }

    private fun canForgeKey(player: PlayerState, cost: Int): Boolean =
        player.amber >= cost && player.keys < 3

    fun readyPhase(game: GameState) {
        val player = game.activePlayer()
        player.cardsInPlay().forEach { it.exhausted = false }
        game.log("${player.name} readies their cards")
    }

    fun drawPhase(game: GameState) {
        val player = game.activePlayer()
        val handSize = 6 - ceil(player.chains / 6.0).toInt()
        val toDraw = (handSize - player.hand.size).coerceAtLeast(0)
        if (toDraw > 0) {
            draw(game, player, toDraw)
            if (player.chains > 0) {
                player.chains = (player.chains - 1).coerceAtLeast(0)
            }
        }
    }

    fun endPhase(game: GameState, isGameOver: (GameState) -> Boolean = { false }) {
        val player = game.activePlayer()
        player.activeHouse = null
        game.turnScopedReactions.clear()
        // Temporary house changes (Brain Stem Antenna) last "for the remainder of the turn".
        game.players.forEach { p -> p.creatures.forEach { if (it.house != it.baseHouse) it.house = it.baseHouse } }
        if (isGameOver(game)) return

        // Remember whether this player forged a key this turn for "previous turn" checks.
        player.forgedKeyLastTurn = player.forgedKeyThisTurn

        // Revert any temporary creature control from effects like Hypnotic Command.
        revertTemporaryControl(game)

        // Switch active player
        game.activePlayerIndex = 1 - game.activePlayerIndex
        game.turn += 1
        if (game.activePlayerIndex == game.firstPlayerIndex) {
            game.round += 1
            game.players.forEach { it.cardNamesPlayedOrUsedThisRound.clear() }
        }
    }

    /**
     * The shared end-of-turn sequence (ready, draw refill, turn switch). Returns true when
     * the game has ended, so callers can stop without starting the next turn.
     */
    fun finishTurn(game: GameState): Boolean {
        readyPhase(game)
        drawPhase(game)
        if (checkGameOver(game)) return true
        endPhase(game) { checkGameOver(it) }
        return checkGameOver(game)
    }

    private fun revertTemporaryControl(game: GameState) {
        val iterator = game.temporaryControllers.iterator()
        while (iterator.hasNext()) {
            val (instanceId, originalControllerIndex) = iterator.next()
            val currentOwner = game.players.find { p -> p.creatures.any { it.instanceId == instanceId } }
            val creature = currentOwner?.creatures?.find { it.instanceId == instanceId }
            if (creature != null) {
                if (currentOwner.index != originalControllerIndex) {
                    currentOwner.creatures.remove(creature)
                    creature.controllerIndex = originalControllerIndex
                    game.player(originalControllerIndex).creatures.add(creature)
                    game.log("${creature.cardId} returns to ${game.player(originalControllerIndex).name}'s control")
                }
            }
            iterator.remove()
        }
    }

    fun draw(game: GameState, player: PlayerState, amount: Int) {
        repeat(amount) {
            if (player.library.isEmpty()) {
                if (player.discard.isEmpty()) return
                player.library.addAll(player.discard)
                player.discard.clear()
                player.library.shuffle(game.rng)
            }
            if (player.library.isNotEmpty()) {
                player.hand.add(player.library.removeFirst())
            }
        }
    }

    fun checkGameOver(game: GameState): Boolean {
        game.players.forEach { player ->
            if (player.hasWon()) {
                game.winnerIndex = player.index
                game.endCause = EndCause.KEYS
                game.done = true
                game.log("${player.name} wins by forging 3 keys!")
                return true
            }
        }
        return false
    }
}
