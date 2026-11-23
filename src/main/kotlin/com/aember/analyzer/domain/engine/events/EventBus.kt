package com.aember.analyzer.domain.engine.events

import com.aember.analyzer.domain.engine.CardLibrary
import com.aember.analyzer.domain.engine.model.GameState
import com.aember.analyzer.domain.engine.model.InPlayCard
import com.aember.analyzer.domain.engine.model.PlayerState

/**
 * Dispatches [GameEvent]s to reaction handlers. Stateless like
 * `EffectRegistry`: on every emit it walks all cards in play (creatures,
 * artifacts and upgrades) and invokes their `onEvent` hook. Cards that left
 * play no longer receive reactions; the card being destroyed does not react
 * to its own destruction because the event is emitted after removal.
 */
object EventBus {

    fun emit(library: CardLibrary, game: GameState, event: GameEvent) {
        game.players.forEach { player ->
            player.cardsInPlay().forEach { dispatch(library, game, player, it, event) }
            player.creatures.forEach { creature ->
                creature.upgrades.forEach { dispatch(library, game, player, it, event) }
            }
        }
        // Turn-scoped subscriptions (e.g. Looter Goblin, Loot the Bodies) fire
        // after the in-play dispatch, independently of the reacting card's zone.
        game.turnScopedReactions.toList().forEach { subscription ->
            if (subscription.activeThroughTurn >= game.turn && subscription.check(event)) {
                val previous = game.currentEvent
                game.currentEvent = event
                try {
                    subscription.fire(game, game.player(subscription.ownerIndex))
                } finally {
                    game.currentEvent = previous
                }
            }
        }
    }

    /** Delivers an event to a single card (also used for cards that already left play). */
    fun deliver(
        library: CardLibrary,
        game: GameState,
        player: PlayerState,
        card: InPlayCard,
        event: GameEvent
    ) = dispatch(library, game, player, card, event)

    private fun dispatch(
        library: CardLibrary,
        game: GameState,
        player: PlayerState,
        card: InPlayCard,
        event: GameEvent
    ) {
        val previous = game.currentEvent
        game.currentEvent = event
        try {
            library.scriptFor(card.cardId).onEvent(game, player, card, event)
        } finally {
            game.currentEvent = previous
        }
    }
}
