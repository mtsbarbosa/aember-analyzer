package com.aember.analyzer.domain.engine

import com.aember.analyzer.domain.engine.action.*
import com.aember.analyzer.domain.engine.model.*
import com.aember.analyzer.domain.model.Deck
import kotlin.math.ceil

/**
 * Runs a single KeyForge match between two decks.
 */
class GameEngine(
    private val library: CardLibrary,
    private val aiA: AIPlayer,
    private val aiB: AIPlayer
) {

    fun runMatch(deckA: Deck, deckB: Deck, seed: Long): GameState {
        val random = java.util.Random(seed)
        val idGenerator = SeededIdGenerator(seed)
        val executor = ActionExecutor(library, idGenerator)
        val actionGenerator = ActionGenerator(library)

        val playerA = createPlayer(0, "Player A", deckA)
        val playerB = createPlayer(1, "Player B", deckB)
        val game = GameState(
            players = listOf(playerA, playerB),
            activePlayerIndex = random.nextInt(2),
            firstPlayerIndex = random.nextInt(2)
        )
        game.amberRedirector = { player, amount ->
            findAmberRedirector(game, player, amount)
        }

        setup(game, random)

        while (!game.done && game.round <= game.maxRounds) {
            val player = game.activePlayer()
            game.log("--- Turn ${game.turn} / Round ${game.round} - ${player.name} ---")
            logHand(game, player)

            // Reset per-turn flags at the start of each turn.
            game.players.forEach { p ->
                p.creatures.forEach { it.elusiveUsed = false }
                p.turnTriggerAmber.clear()
                p.creaturesPlayedThisTurn = 0
                p.drawOnCardPlayActive = false
                p.drawOnCardPlayHouse = null
                p.forgedKeyThisTurn = false
                p.cardsPlayedThisTurnByHouse.clear()
            }

            keyPhase(game)
            if (checkGameOver(game)) break

            housePhase(game, actionGenerator, executor)
            if (checkGameOver(game)) break

            mainPhase(game, actionGenerator, executor)
            if (checkGameOver(game)) break

            readyPhase(game)
            drawPhase(game)
            endPhase(game)
        }

        if (!game.done) {
            game.endCause = EndCause.TIMEOUT
            resolveTimeoutWinner(game)
        }

        return game
    }

    private fun findAmberRedirector(game: GameState, player: PlayerState, amount: Int): InPlayCard? {
        if (amount <= 0) return null
        return game.players
            .filter { it.index != player.index }
            .flatMap { it.creatures }
            .find { it.controllerIndex != player.index && library.scriptFor(it.cardId).capturesOpponentAmberGains() }
    }

    private fun createPlayer(index: Int, name: String, deck: Deck): PlayerState {
        return PlayerState(
            index = index,
            name = name,
            deck = deck,
            library = deck.cards.flatMap { card -> List(card.count) { card.cardId } }.toMutableList()
        )
    }

    private fun setup(game: GameState, random: java.util.Random) {
        game.activePlayerIndex = game.firstPlayerIndex
        game.players.forEach { player ->
            player.library.shuffle(random)
            val handSize = if (player.index == game.firstPlayerIndex) 7 else 6
            draw(player, handSize)
        }
        game.log("Setup complete. First player: ${game.activePlayer().name}")
    }

    private fun keyPhase(game: GameState) {
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
            if (!player.canForgeSecondKey) break
            player.canForgeSecondKey = false
        }
        if (player.amber < cost) {
            game.log("${player.name} does not forge a key (amber: ${player.amber})")
        }
    }

    private fun canForgeKey(player: PlayerState, cost: Int): Boolean = player.amber >= cost && player.keys < 3

    private fun logHand(game: GameState, player: PlayerState) {
        val handNames = player.hand.map { cardId ->
            library.cardData(cardId)?.name ?: cardId
        }
        game.log("${player.name}'s hand: ${handNames.joinToString(", ")}")
    }

    private fun housePhase(
        game: GameState,
        actionGenerator: ActionGenerator,
        executor: ActionExecutor
    ) {
        val player = game.activePlayer()
        val ai = if (player.index == 0) aiA else aiB
        val choices = actionGenerator.generate(game)
        val choice = ai.choose(game, choices)
        require(choice is ChooseHouseAction) { "House phase must choose a house" }
        executor.execute(game, choice)

        // Archives pickup is a free action in the house phase.
        if (player.archives.isNotEmpty()) {
            val archived = player.archives.size
            player.hand.addAll(player.archives)
            player.archives.clear()
            game.log("${player.name} takes $archived archived card(s) into hand")
        }
    }

    private fun mainPhase(
        game: GameState,
        actionGenerator: ActionGenerator,
        executor: ActionExecutor
    ) {
        game.actionsThisPhase = 0
        game.endPhaseRightNow = false
        var ended = false
        var safety = 0
        while (!ended && safety < 200) {
            safety++
            val player = game.activePlayer()
            val ai = if (player.index == 0) aiA else aiB
            val choices = actionGenerator.generate(game)
            if (choices.isEmpty()) break
            val choice = ai.choose(game, choices)
            executor.execute(game, choice)
            if (choice is EndTurnAction || game.endPhaseRightNow || checkGameOver(game)) {
                ended = true
            }
        }
        game.activePlayer().cannotFightNextTurn = false
    }

    private fun readyPhase(game: GameState) {
        val player = game.activePlayer()
        player.cardsInPlay().forEach { it.exhausted = false }
        game.log("${player.name} readies their cards")
    }

    private fun drawPhase(game: GameState) {
        val player = game.activePlayer()
        val handSize = 6 - ceil(player.chains / 6.0).toInt()
        val toDraw = (handSize - player.hand.size).coerceAtLeast(0)
        if (toDraw > 0) {
            draw(player, toDraw)
            if (player.chains > 0) {
                player.chains = (player.chains - 1).coerceAtLeast(0)
            }
        }
    }

    private fun draw(player: PlayerState, amount: Int) {
        repeat(amount) {
            if (player.library.isEmpty()) {
                if (player.discard.isEmpty()) return
                player.library.addAll(player.discard)
                player.discard.clear()
                player.library.shuffle(java.util.Random())
            }
            if (player.library.isNotEmpty()) {
                player.hand.add(player.library.removeFirst())
            }
        }
    }

    private fun endPhase(game: GameState) {
        val player = game.activePlayer()
        player.activeHouse = null
        if (checkGameOver(game)) return

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

    private fun checkGameOver(game: GameState): Boolean {
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

    private fun resolveTimeoutWinner(game: GameState) {
        val p0 = game.players[0]
        val p1 = game.players[1]
        game.winnerIndex = when {
            p0.keys > p1.keys -> 0
            p1.keys > p0.keys -> 1
            p0.amber > p1.amber -> 0
            p1.amber > p0.amber -> 1
            p0.chains < p1.chains -> 0
            p1.chains < p0.chains -> 1
            p0.creatures.size > p1.creatures.size -> 0
            p1.creatures.size > p0.creatures.size -> 1
            else -> game.firstPlayerIndex
        }
        game.done = true
        game.log("Timeout: winner resolved by tiebreaker")
    }
}
