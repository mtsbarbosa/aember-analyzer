package com.aember.analyzer.domain.engine

import com.aember.analyzer.domain.engine.action.*
import com.aember.analyzer.domain.engine.model.*
import com.aember.analyzer.domain.model.Deck

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
        // Masterplan/Chaos Portal play cards through the real play path.
        library.dslResolver?.playBridge = executor
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
        // All in-game randomness derives from the match seed.
        game.rng = random

        setup(game, random)

        while (!game.done && game.round <= game.maxRounds) {
            val player = game.activePlayer()
            game.log("--- Turn ${game.turn} / Round ${game.round} - ${player.name} ---")
            logHand(game, player)

            TurnPhases.startTurn(game, library)
            if (checkGameOver(game)) break

            housePhase(game, actionGenerator, executor)
            if (checkGameOver(game)) break

            mainPhase(game, actionGenerator, executor)
            if (checkGameOver(game)) break

            if (TurnPhases.finishTurn(game, library)) break
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
            TurnPhases.draw(game, player, handSize)
        }
        game.log("Setup complete. First player: ${game.activePlayer().name}")
    }

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
        TurnPhases.endMainPhase(game)
    }

    private fun checkGameOver(game: GameState): Boolean = TurnPhases.checkGameOver(game)

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
