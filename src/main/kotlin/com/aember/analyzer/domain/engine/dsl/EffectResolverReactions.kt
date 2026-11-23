package com.aember.analyzer.domain.engine.dsl

import com.aember.analyzer.domain.engine.model.GameState
import com.aember.analyzer.domain.engine.model.InPlayCard
import com.aember.analyzer.domain.engine.model.PlayerState
import com.aember.analyzer.domain.engine.model.Zone
import com.aember.analyzer.domain.model.CardType

/** Event-bus reactions, repeat loops, and the batch-(c) artifact/conditional specials. */
internal fun EffectResolver.registerReactionEffects() {
    register<ActivateTurnReaction> { game, player, card, effect ->
        // Register the played card's subscription reactions at game level so they
        // keep firing until end of turn even when the card left play (actions).
        library?.scriptFor(card.cardId)?.subscriptionReactions(card)?.forEach { reaction ->
            game.turnScopedReactions += TurnScopedReaction(
                ownerIndex = player.index,
                activeThroughTurn = game.turn,
                check = { event -> reaction.matches(game, player, card, event) },
                fire = { g, p ->
                    g.log("${card.cardId} reacts: ${reaction.notes.ifEmpty { effect.tag }}")
                    resolve(g, p, card, reaction.effect)
                }
            )
        }
    }
    register<ReadyHost> { game, _, sourceCard, effect -> readyHost(game, sourceCard, effect) }
    register<RepeatWhile> { game, player, sourceCard, effect ->
        var repeats = 0
        while (repeats < effect.maxRepeats && evaluateCondition(game, player, effect.condition)) {
            val before = stateSignature(game)
            resolveInternal(game, player, sourceCard, effect.effect)
            repeats++
            // Progress guard: an iteration that changed nothing (e.g. a steal
            // blocked by The Vaultkeeper) can never flip the condition, so
            // stop instead of burning the remaining repeats.
            if (stateSignature(game) == before) break
        }
    }
    register<RepeatForEach> { _, _, _, _ ->
        // Not used for simple cards yet.
    }
    register<UseOpponentArtifact> { game, player, _, _ -> useOpponentArtifact(game, player) }
    register<DealDamagePerHouseCardInHand> { game, player, _, effect ->
        dealDamagePerHouseCardInHand(game, player, effect)
    }
    register<PurgeCreatureFromDiscard> { game, player, _, _ -> purgeCreatureFromDiscard(game, player) }
    register<ReturnArtifactOrFlankCreatureToHand> { game, player, _, _ ->
        returnArtifactOrFlankCreatureToHand(game, player)
    }
    register<ReturnChosenHouseCreaturesFromDiscard> { game, player, _, effect ->
        returnChosenHouseCreaturesFromDiscard(game, player, effect)
    }
}

internal fun EffectResolver.readyHost(game: GameState, sourceCard: InPlayCard, effect: ReadyHost) {
    val hostId = sourceCard.hostId ?: return
    val host = findCreatureInPlay(game, hostId) ?: return
    host.exhausted = false
    if (effect.houseUntilEndOfTurn != null && host.house != effect.houseUntilEndOfTurn) {
        host.house = effect.houseUntilEndOfTurn
        game.log("${host.cardId} belongs to house ${effect.houseUntilEndOfTurn} until end of turn")
    }
    game.log("${host.cardId} readies")
}

internal fun EffectResolver.useOpponentArtifact(game: GameState, player: PlayerState) {
    val opponent = game.opponent(player)
    val artifact = opponent.artifacts.firstOrNull { candidate ->
        !candidate.exhausted &&
            library?.scriptFor(candidate.cardId)?.actionAbilities()?.any { it != "Omni" } == true
    }
    if (artifact == null) {
        game.log("${player.name} finds no usable opponent artifact")
        return
    }
    val ability = library?.scriptFor(artifact.cardId)?.actionAbilities()?.first { it != "Omni" }
        ?: return
    artifact.exhausted = true
    game.log("${player.name} uses ${opponent.name}'s ${artifact.cardId} as if it were theirs")
    library.scriptFor(artifact.cardId).resolveActionAbility(ability, game, player, artifact)
}

internal fun EffectResolver.dealDamagePerHouseCardInHand(game: GameState, player: PlayerState, effect: DealDamagePerHouseCardInHand) {
    val count = player.hand.count { library?.cardData(it)?.house == effect.house }
    if (count == 0) {
        game.log("${player.name} reveals no ${effect.house} cards from hand")
        return
    }
    game.log("${player.name} reveals $count ${effect.house} card(s) from hand")
    val sourceCard = InPlayCard(
        instanceId = "mothergun-reveal",
        cardId = "mothergun",
        ownerIndex = player.index,
        controllerIndex = player.index,
        zone = Zone.PLAY_AREA,
        house = effect.house,
        type = CardType.ARTIFACT
    )
    dealDamage(game, sourceCard, DealDamage(count, effect.target))
}

internal fun EffectResolver.purgeCreatureFromDiscard(game: GameState, player: PlayerState) {
    game.lastPurgedFromDiscard = 0
    // Either player's discard pile; greedy: purge from the opponent's pile first.
    val piles = listOf(game.opponent(player).discard, player.discard)
    for (pile in piles) {
        val creature = pile.firstOrNull { library?.cardData(it)?.type == CardType.CREATURE }
        if (creature != null) {
            pile.remove(creature)
            player.purged.add(creature)
            game.lastPurgedFromDiscard = 1
            game.log("${player.name} purges ${cardName(creature)} from a discard pile")
            return
        }
    }
    game.log("${player.name} finds no creature in any discard pile")
}

internal fun EffectResolver.returnArtifactOrFlankCreatureToHand(game: GameState, player: PlayerState) {
    val opponent = game.opponent(player)
    val artifact = opponent.artifacts.firstOrNull()
    if (artifact != null) {
        opponent.artifacts.remove(artifact)
        val owner = game.player(artifact.ownerIndex)
        owner.hand.add(artifact.cardId)
        game.log("${artifact.cardId} returns to hand")
        return
    }
    // Opponent's strongest flank creature.
    val target = opponent.creatures.filter { creature ->
        val battleline = opponent.creatures
        battleline.size == 1 || battleline.first().instanceId == creature.instanceId ||
            battleline.last().instanceId == creature.instanceId
    }.maxByOrNull { it.power }
    if (target == null) {
        game.log("${player.name} finds no artifact or flank creature to return")
        return
    }
    val controller = game.player(target.controllerIndex)
    controller.creatures.remove(target)
    target.upgrades.forEach { upgrade -> controller.discard.add(upgrade.cardId) }
    target.upgrades.clear()
    val owner = game.player(target.ownerIndex)
    owner.hand.add(target.cardId)
    game.log("${target.cardId} returns to hand")
    if (library != null) {
        val event = com.aember.analyzer.domain.engine.events.CardLeftPlay(target.instanceId, target.cardId, target.ownerIndex, target.controllerIndex)
        com.aember.analyzer.domain.engine.events.EventBus.deliver(library, game, controller, target, event)
        com.aember.analyzer.domain.engine.events.EventBus.emit(library, game, event)
    }
}

internal fun EffectResolver.returnChosenHouseCreaturesFromDiscard(game: GameState, player: PlayerState, effect: ReturnChosenHouseCreaturesFromDiscard) {
    val chosen = com.aember.analyzer.domain.model.House.entries
        .map { house -> house to player.discard.count { cardId ->
            val data = library?.cardData(cardId)
            data?.type == CardType.CREATURE && data.house == house
        } }
        .filter { it.second > 0 }
        .maxByOrNull { it.second }
        ?.first ?: run {
        game.log("${player.name} has no creatures in discard; nothing returns")
        return
    }
    val returned = player.discard.filter { cardId ->
        val data = library?.cardData(cardId)
        data?.type == CardType.CREATURE && data.house == chosen
    }
    player.discard.removeAll(returned)
    player.hand.addAll(returned)
    game.log("${player.name} returns ${returned.size} $chosen creature(s) from discard to hand")
    if (effect.gainChains > 0) {
        player.chains += effect.gainChains
        game.log("${player.name} gains ${effect.gainChains} chain(s)")
    }
}
