package com.aember.analyzer.domain.engine.dsl

import com.aember.analyzer.domain.engine.ForgeRules
import com.aember.analyzer.domain.engine.events.EventBus
import com.aember.analyzer.domain.engine.model.GameState
import com.aember.analyzer.domain.engine.model.InPlayCard
import com.aember.analyzer.domain.engine.model.PlayerState
import com.aember.analyzer.domain.engine.model.Zone
import com.aember.analyzer.domain.model.CardType
import com.aember.analyzer.domain.model.House

/** Turn/forge control: denials, key cheats, take-control, play grants, per-hand effects. */
internal fun EffectResolver.registerControlEffects() {
    register<DenyHouseNextTurn> { game, sourcePlayer, _, effect ->
        val target = targetPlayer(game, sourcePlayer, effect.target)
        target.deniedHouseNextTurn = effect.house
        game.log("${sourcePlayer.name} names ${effect.house} with Control the Weak; ${target.name} cannot choose it next turn")
    }
    register<DenyCardUseNextTurn> { game, player, _, _ -> game.opponent(player).cannotUseCardsNextTurn = true }
    register<DenyActionCardsNextTurn> { game, player, _, _ -> game.opponent(player).deniedCardTypeNextTurn = CardType.ACTION }
    register<DenyCreaturePlaysNextTurn> { game, player, _, _ -> game.opponent(player).deniedCardTypeNextTurn = CardType.CREATURE }
    register<DenyCreatureFightsNextTurn> { game, player, _, _ -> game.opponent(player).cannotFightNextTurn = true }
    register<SkipForgeNextTurn> { game, sourcePlayer, _, effect ->
        targetPlayer(game, sourcePlayer, effect.player).skipsForgeNextTurn = true
        game.log("${sourcePlayer.name} plays Miasma; ${targetPlayer(game, sourcePlayer, effect.player).name} cannot forge a key next turn")
    }
    register<ForgeKey> { game, player, _, effect -> forgeKey(game, player, effect) }
    register<TakeControlUntilEndOfTurn> { game, sourcePlayer, sourceCard, effect ->
        takeControlUntilEndOfTurn(game, sourcePlayer, sourceCard, effect)
    }
    register<TakeControlOfEnemyFlankCreature> { game, sourcePlayer, _, effect ->
        takeControlOfEnemyFlankCreature(game, targetPlayer(game, sourcePlayer, effect.player))
    }
    register<MayPlayNonHouseThisTurn> { game, player, _, effect ->
        player.mayPlayNonHouseThisTurn = effect.notHouse
        game.log("${player.name} may play one card of a house other than ${effect.notHouse} this turn")
    }
    register<NextPlaysEnterReady> { _, player, _, effect ->
        player.nextPlaysEnterReady += effect.count
    }
    register<PlayTopCardOfHouse> { game, sourcePlayer, _, effect ->
        playTopCardOfHouse(game, targetPlayer(game, sourcePlayer, effect.player), effect.house)
    }
    register<AddTopCardOfHouseToHand> { game, sourcePlayer, _, effect ->
        addTopCardOfHouseToHand(game, targetPlayer(game, sourcePlayer, effect.player), effect.house)
    }
    register<DrawPerCardInHand> { game, player, _, effect -> drawPerCardInHand(game, player, effect) }
    register<ReadyCreaturesOfHousePerCardInHand> { game, player, _, effect -> readyCreaturesOfHousePerCardInHand(game, player, effect) }
    register<GainChains> { game, player, _, effect -> targetPlayer(game, player, effect.player).chains += effect.amount }
    register<LoseChains> { game, player, _, effect ->
        val target = targetPlayer(game, player, effect.player)
        target.chains = (target.chains - effect.amount).coerceAtLeast(0)
    }
    register<DrawOnCardPlayThisTurn> { _, player, _, effect ->
        player.drawOnCardPlayActive = true
        player.drawOnCardPlayHouse = effect.house
    }
    register<PlayTopCardIfChosenHouse> { game, player, _, _ ->
        ensureLibraryNotEmpty(game, player)
        // Greedy house: the one with the most cards in the deck.
        val chosen = player.library.mapNotNull { library?.cardData(it) }
            .groupingBy { it.house }
            .eachCount()
            .maxByOrNull { it.value }?.key
            ?: return@register
        val top = player.library.firstOrNull()
        val topData = top?.let { library?.cardData(it) }
        game.log("${player.name} chooses $chosen and reveals ${top?.let { cardName(it) } ?: "nothing"}")
        if (topData == null || topData.house != chosen) return@register
        // Play the revealed card through the real play path when bridged.
        val bridge = playBridge
        val isUpgrade = topData.type == CardType.UPGRADE
        val host = player.creatures.maxByOrNull { it.power }
        if (isUpgrade && host == null) {
            // No creature to attach to: the card stays on top of the deck.
            game.log("${player.name} has no creature to attach ${cardName(top)} to")
            return@register
        }
        if (bridge != null) {
            player.library.removeFirst()
            player.hand.add(top)
            bridge.playCardFromHand(game, player, top, if (isUpgrade) host?.instanceId else null)
        } else {
            player.library.removeFirst()
            player.discard.add(top)
            game.log("${player.name} plays ${cardName(top)} from Chaos Portal (approximated)")
        }
    }
    register<MayUseHouseThisTurn> { game, player, _, effect ->
        player.mayUseHouseThisTurn = effect.house
        game.log("${player.name} may use friendly ${effect.house} creatures for the remainder of the turn")
    }
    register<AllowUseUpToTwoMarsCards> { game, player, _, _ ->
        val marsCards = (player.creatures.filter { it.house == House.MARS } + player.artifacts.filter { it.house == House.MARS })
            .sortedByDescending { it.power }
            .take(2)
        marsCards.forEach { player.mayUseCardIdsThisTurn.add(it.instanceId) }
        if (marsCards.isEmpty()) {
            game.log("${player.name} has no Mars cards to make usable")
        } else {
            game.log("${player.name} may use ${marsCards.joinToString(", ") { it.cardId }} for the remainder of the turn")
        }
    }
    register<OpponentNextForgeGivesAmber> { game, player, _, _ ->
        game.opponent(player).nextForgeGivesAmberTo = player.index
        game.log("${player.name} claims ${game.opponent(player).name}'s remaining amber if they forge a key next turn")
    }
    register<NextCreatureOfHouseEntersReady> { _, player, _, effect ->
        player.nextCreatureOfHouseEntersReady = effect.house
    }
    register<AllCreaturesMayFightThisTurn> { _, player, _, _ ->
        player.allMayFightThisTurn = true
    }
    register<ChosenHouseMayFightThisTurn> { game, player, _, effect ->
        // Greedy house: the one with the most friendly creatures.
        val chosen = effect.house ?: player.creatures.groupingBy { it.house }
            .eachCount().maxByOrNull { it.value }?.key
        if (chosen != null) {
            player.mayFightHouseThisTurn = chosen
            game.log("Friendly $chosen creatures may fight for the remainder of the turn")
        }
    }
    register<AllCreaturesFightOnlyThisTurn> { _, player, _, _ ->
        player.allCreaturesFightOnlyThisTurn = true
    }
    register<AllowUseFriendlyCreaturesThisTurn> { game, player, sourceCard, effect ->
        val chosen = player.creatures.filter { it.instanceId != sourceCard.instanceId }
            .sortedByDescending { it.power }.take(effect.count)
        chosen.forEach { player.mayUseCardIdsThisTurn.add(it.instanceId) }
        if (chosen.isNotEmpty()) {
            game.log("${player.name} may use ${chosen.joinToString(", ") { it.cardId }} for the remainder of the turn")
        }
    }
    register<RevealOpponentHand> { game, player, _, _ ->
        val opponent = game.opponent(player)
        game.log("${player.name} looks at ${opponent.name}'s hand: ${opponent.hand.joinToString(", ") { cardName(it) }}")
    }
    register<TriggerOtherCreatureReap> { game, player, sourceCard, _ ->
        val others = game.players.flatMap { it.creatures }.filter { it.instanceId != sourceCard.instanceId }
        // Greedy: strongest other creature (usually the biggest reap benefit).
        val target = others.maxByOrNull { it.power } ?: run {
            game.log("${sourceCard.cardId} has no other creature to trigger")
            return@register
        }
        val script = library?.scriptFor(target.cardId) ?: return@register
        game.log("${sourceCard.cardId} triggers ${target.cardId}'s reap effect as if ${player.name} controlled it")
        script.onReap(game, player, target)
        val event = com.aember.analyzer.domain.engine.events.CreatureReaped(target.controllerIndex, target.instanceId, target.cardId)
        EventBus.deliver(library, game, player, sourceCard, event)
    }
    register<ClaimDestroyedFightTarget> { game, player, sourceCard, effect ->
        val event = game.currentEvent
        if (event !is com.aember.analyzer.domain.engine.events.FightResolved) return@register
        // The source may be the attacker (claims the defender) or the defender (claims the attacker).
        val sourceAttacked = event.attackerInstanceId == sourceCard.instanceId
        val victimDestroyed = if (sourceAttacked) event.defenderDestroyed else event.attackerDestroyed
        if (!victimDestroyed) return@register
        val victimId = if (sourceAttacked) event.defenderInstanceId else event.attackerInstanceId
        // The destroyed creature is still on the board at FightResolved time
        // (moribund); take it directly instead of waiting for the state check.
        val destroyed = findCreatureInPlay(game, victimId) ?: return@register
        val controller = game.player(destroyed.controllerIndex)
        controller.creatures.remove(destroyed)
        destroyed.upgrades.forEach { upgrade -> game.player(upgrade.ownerIndex).discard.add(upgrade.cardId) }
        destroyed.upgrades.clear()
        if (effect.gainAmber > 0) {
            game.player(destroyed.ownerIndex).purged.add(destroyed.cardId)
            gainAmber(game, player, effect.gainAmber)
            game.log("${destroyed.cardId} is purged and ${player.name} gains ${effect.gainAmber} amber")
        } else {
            // Permanent control (until it leaves play); not registered in temporaryControllers.
            destroyed.controllerIndex = player.index
            destroyed.damage = 0
            destroyed.exhausted = true
            player.creatures.add(destroyed)
            game.log("${player.name} puts ${destroyed.cardId} into play under their control")
        }
    }
}

internal fun EffectResolver.forgeKey(game: GameState, player: PlayerState, effect: ForgeKey) {
    if (player.keys >= 3) return
    val cost = if (effect.free) {
        0
    } else {
        val baseCost = if (library != null) ForgeRules.keyCost(library, game, player) else ForgeRules.BASE_KEY_COST
        val rawCost = baseCost + effect.extraCost + player.keyCostNextTurnBonus -
            (if (effect.discountPerCardInHand) player.hand.size else 0)
        // The one-turn bonus applies during this turn only; consume it here so a
        // mid-turn cheat forge does not double-dip at the next key phase.
        player.keyCostNextTurnBonus = 0
        rawCost.coerceAtLeast(0)
    }
    if (player.amber < cost) {
        game.log("${player.name} cannot forge a key (cost: $cost, amber: ${player.amber})")
        return
    }
    val before = player.amber
    player.amber -= cost
    player.keys += 1
    player.forgedKeyThisTurn = true
    player.statistics.keysForged++
    game.log("${player.name} forges a key at cost $cost! Keys: ${player.keys} (${player.name}: $before -> ${player.amber})")
    if (library != null) EventBus.emit(library, game, com.aember.analyzer.domain.engine.events.KeyForged(player.index, cost))
}

internal fun EffectResolver.takeControlUntilEndOfTurn(game: GameState, sourcePlayer: PlayerState, sourceCard: InPlayCard, effect: TakeControlUntilEndOfTurn) {
    val targets = targetChooser.chooseCreature(game, effect.target, sourceCard)
    val target = targets.firstOrNull() ?: return
    val currentOwner = game.players.find { p -> p.creatures.contains(target) } ?: return
    if (currentOwner.index == sourcePlayer.index) return
    currentOwner.creatures.remove(target)
    game.temporaryControllers[target.instanceId] = currentOwner.index
    target.controllerIndex = sourcePlayer.index
    sourcePlayer.creatures.add(target)
    game.log("${sourcePlayer.name} takes control of ${target.cardId} until end of turn")
}

internal fun takeControlOfEnemyFlankCreature(game: GameState, sourcePlayer: PlayerState) {
    if (!sourcePlayer.forgedKeyThisTurn) {
        game.log("${sourcePlayer.name} did not forge a key this turn, so Smiling Ruth takes no creature")
        return
    }
    val opponent = game.opponent(sourcePlayer)
    if (opponent.creatures.isEmpty()) return
    val flanks = listOfNotNull(opponent.creatures.firstOrNull(), opponent.creatures.lastOrNull()).distinctBy { it.instanceId }
    val target = flanks.maxByOrNull { it.power } ?: return
    opponent.creatures.remove(target)
    // keyteki: a lasting effect, not 'until end of turn' - the creature stays under our control.
    target.controllerIndex = sourcePlayer.index
    sourcePlayer.creatures.add(target)
    game.log("${sourcePlayer.name} takes control of ${target.cardId} with Smiling Ruth")
}

internal fun EffectResolver.playTopCardOfHouse(game: GameState, player: PlayerState, house: House) {
    ensureLibraryNotEmpty(game, player)
    val discarded = mutableListOf<String>()
    var played: String? = null
    while (player.library.isNotEmpty()) {
        val cardId = player.library.removeFirst()
        val card = library?.cardData(cardId)
        if (card?.house == house && card.type == CardType.CREATURE) {
            played = cardId
            break
        } else {
            discarded.add(cardId)
        }
    }
    if (discarded.isNotEmpty()) {
        player.discard.addAll(discarded)
        game.log("${player.name} discards ${discarded.size} card(s) from Invasion Portal: ${discarded.joinToString(", ") { cardName(it) }}")
    }
    if (played != null) {
        val catalog = library?.cardData(played) ?: return
        val inPlay = InPlayCard(
            instanceId = java.util.UUID.randomUUID().toString(),
            cardId = played,
            ownerIndex = player.index,
            controllerIndex = player.index,
            zone = Zone.PLAY_AREA,
            house = catalog.house,
            type = CardType.CREATURE,
            exhausted = true,
            power = catalog.power ?: 0,
            armor = catalog.armor ?: 0,
            keywords = catalog.keywords.map { it.lowercase() }.associateWith { 1 },
            traits = catalog.traits.map { it.lowercase() }
        )
        player.creatures.add(inPlay)
        game.log("${player.name} plays ${cardName(played)} from Invasion Portal")
    } else {
        game.log("${player.name} finds no ${house.name.lowercase()} creature from Invasion Portal")
    }
}

internal fun EffectResolver.addTopCardOfHouseToHand(game: GameState, player: PlayerState, house: House) {
    ensureLibraryNotEmpty(game, player)
    val discarded = mutableListOf<String>()
    var found: String? = null
    while (player.library.isNotEmpty()) {
        val cardId = player.library.removeFirst()
        val card = library?.cardData(cardId)
        if (card?.house == house && card.type == CardType.CREATURE) {
            found = cardId
            break
        } else {
            discarded.add(cardId)
        }
    }
    if (discarded.isNotEmpty()) {
        player.discard.addAll(discarded)
        game.log("${player.name} discards ${discarded.size} card(s) from Invasion Portal: ${discarded.joinToString(", ") { cardName(it) }}")
    }
    if (found != null) {
        player.hand.add(found)
        game.log("${player.name} puts ${cardName(found)} into hand from Invasion Portal")
    } else {
        game.log("${player.name} finds no ${house.name.lowercase()} creature from Invasion Portal")
    }
}

internal fun EffectResolver.drawPerCardInHand(game: GameState, player: PlayerState, effect: DrawPerCardInHand) {
    val target = targetPlayer(game, player, effect.player)
    val count = target.hand.count { cardId ->
        target.deck.cards.find { it.cardId == cardId }?.effectiveHouse == effect.house
    }
    if (count == 0) {
        game.log("${target.name} reveals no ${effect.house.name.lowercase()} cards from hand")
        return
    }
    game.log("${target.name} reveals $count ${effect.house.name.lowercase()} card(s) from hand")
    drawCards(game, target, count)
}

internal fun EffectResolver.readyCreaturesOfHousePerCardInHand(game: GameState, player: PlayerState, effect: ReadyCreaturesOfHousePerCardInHand) {
    val target = targetPlayer(game, player, effect.player)
    val count = target.hand.count { cardId ->
        target.deck.cards.find { it.cardId == cardId }?.effectiveHouse == effect.house
    }
    // Readying only clears exhaustion; a stunned creature keeps its stun
    // until it is used (KeyForge rulebook).
    val readied = target.creatures
        .filter { it.house == effect.house && it.exhausted }
        .take(count)
    readied.forEach { it.exhausted = false }
    game.log("${target.name} readies ${readied.size} ${effect.house.name.lowercase()} creature(s)")
}
