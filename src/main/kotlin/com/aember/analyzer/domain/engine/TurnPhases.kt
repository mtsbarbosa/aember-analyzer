package com.aember.analyzer.domain.engine

import com.aember.analyzer.domain.engine.effects.EffectRegistry
import com.aember.analyzer.domain.engine.events.EventBus
import com.aember.analyzer.domain.engine.events.KeyForged
import com.aember.analyzer.domain.engine.events.TurnEnded
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
                it.immuneToDamageThisTurn = false
                it.armorStrippedThisTurn = 0
                it.usedThisTurn = 0
                it.grantedKeywordsThisTurn.clear()
                it.removedKeywordsThisTurn.clear()
                // Restore temporary house changes (e.g. Brain Stem Antenna).
                if (it.house != it.baseHouse) it.house = it.baseHouse
            }
            p.turnTriggerAmber.clear()
            p.creaturesPlayedThisTurn = 0
            p.drawOnCardPlayActive = false
            p.drawOnCardPlayHouse = null
            p.forgedKeyThisTurn = false
            p.cardsPlayedThisTurnByHouse.clear()
            p.nextPlaysEnterReady = 0
            p.mayPlayNonHouseThisTurn = null
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
        // The Sting: the controller skips their forge step entirely while it is in play.
        if (EffectRegistry.skipsForgeStep(library, game, player)) {
            game.log("${player.name} skips their forge key step (The Sting)")
            return
        }
        // Pocket Universe / Safe Place: amber captured on own cards may fund the forge.
        val spendable = EffectRegistry.keyForgeAmberOnCards(library, game, player)
        val availableOnCards = spendable.sumOf { it.second }
        val effectiveCost = (cost - availableOnCards).coerceAtLeast(0)
        while (canForgeKey(player, effectiveCost)) {
            val before = player.amber
            player.amber -= effectiveCost
            player.keys += 1
            player.forgedKeyThisTurn = true
            player.statistics.keysForged++
            // Consume the amber that came off cards first (cheapest card first).
            var remaining = cost - effectiveCost
            for ((card, available) in spendable) {
                if (remaining <= 0) break
                val spent = minOf(remaining, available)
                card.capturedAmber -= spent
                remaining -= spent
                game.log("${player.name} spends $spent amber on ${card.cardId} for the key")
            }
            // The-sting: the amber spent by the opponent's forge goes to the redirector.
            val redirector = EffectRegistry.forgeAmberRedirectTarget(library, game, player)
            if (redirector != null && effectiveCost > 0) {
                redirector.amber += effectiveCost
                game.log("${player.name}'s forge amber goes to ${redirector.name} (The Sting)")
            }
            game.log("${player.name} forges a key! Keys: ${player.keys} (${player.name}: $before -> ${player.amber})")
            EventBus.emit(library, game, KeyForged(player.index, cost))
            if (!player.canForgeSecondKey) break
            player.canForgeSecondKey = false
        }
        if (player.amber < effectiveCost) {
            game.log("${player.name} does not forge a key (amber: ${player.amber})")
        }
        // Interdimensional Graft: when this player forges a key on their next turn,
        // all remaining amber goes to the graft's owner (claimed once per forge).
        player.nextForgeGivesAmberTo?.let { creditorIndex ->
            if (player.forgedKeyThisTurn && player.amber > 0) {
                val before = player.amber
                val creditor = game.player(creditorIndex)
                creditor.amber += player.amber
                player.amber = 0
                game.log("${player.name} gives all $before remaining amber to ${creditor.name} (Interdimensional Graft)")
            }
            player.nextForgeGivesAmberTo = null
        }
    }

    private fun canForgeKey(player: PlayerState, cost: Int): Boolean =
        player.amber >= cost && player.keys < 3

    fun readyPhase(game: GameState) {
        val player = game.activePlayer()
        player.cardsInPlay().forEach { it.exhausted = false }
        game.log("${player.name} readies their cards")
    }

    fun drawPhase(game: GameState, library: CardLibrary) {
        val player = game.activePlayer()
        // Draw-phase modifiers (Mother refills its controller to 1 more card,
        // Succubus refills the opponent to 1 less card).
        val modifier = game.players.flatMap { it.cardsInPlay() }.sumOf { card ->
            val script = library.scriptFor(card.cardId)
            if (card.controllerIndex == player.index) script.drawRefillBonus() else -script.drawRefillOpponentPenalty()
        }
        val handSize = (6 + modifier - ceil(player.chains / 6.0).toInt()).coerceAtLeast(0)
        val toDraw = (handSize - player.hand.size).coerceAtLeast(0)
        if (toDraw > 0) {
            draw(game, player, toDraw)
            if (player.chains > 0) {
                player.chains = (player.chains - 1).coerceAtLeast(0)
            }
        }
    }

    fun endPhase(game: GameState, library: CardLibrary, isGameOver: (GameState) -> Boolean = { false }) {
        val player = game.activePlayer()
        // End-of-turn triggers fire before anything is cleared (Shaffles, Rogue Ogre).
        EventBus.emit(library, game, TurnEnded(player.index))
        player.activeHouse = null
        game.turnScopedReactions.clear()
        // Turn-scoped may-use/fight allowances (Sigil of Brotherhood, Combat Pheromones,
        // Follow the Leader, Brothers in Battle, Horseman of War, Dimension Door, Blypyp).
        player.mayUseHouseThisTurn = null
        player.mayUseCardIdsThisTurn.clear()
        player.allMayFightThisTurn = false
        player.mayFightHouseThisTurn = null
        player.allCreaturesFightOnlyThisTurn = false
        player.reapAmberStolenFromOpponent = false
        player.nextCreatureOfHouseEntersReady = null
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
     * Consumes the per-turn denial flags of the player whose main phase just ended
     * (Fogbank/Foggify fight denial, Skippy Timehog use denial, Scrambler Storm
     * action-play denial). Shared by the engine loop and the scenario harness.
     */
    fun endMainPhase(game: GameState) {
        val player = game.activePlayer()
        player.cannotFightNextTurn = false
        player.cannotUseCardsNextTurn = false
        player.deniedCardTypeNextTurn = null
    }

    /**
     * The shared end-of-turn sequence (ready, draw refill, turn switch). Returns true when
     * the game has ended, so callers can stop without starting the next turn.
     */
    fun finishTurn(game: GameState, library: CardLibrary): Boolean {
        readyPhase(game)
        drawPhase(game, library)
        if (checkGameOver(game)) return true
        endPhase(game, library) { checkGameOver(it) }
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
