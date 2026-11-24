package com.aember.analyzer.domain.engine.dsl

import com.aember.analyzer.domain.engine.events.CardLeftPlay
import com.aember.analyzer.domain.engine.events.EventBus
import com.aember.analyzer.domain.engine.model.GameState
import com.aember.analyzer.domain.engine.model.InPlayCard
import com.aember.analyzer.domain.engine.model.PlayerState
import com.aember.analyzer.domain.engine.model.Zone
import com.aember.analyzer.domain.engine.model.randomWith
import com.aember.analyzer.domain.model.CardType
import com.aember.analyzer.domain.model.House

/** Card/zone movement: draw, discard, return, archive, purge, deck reshuffles. */
internal fun EffectResolver.registerCardEffects() {
    register<Draw> { game, player, _, effect -> drawCards(
        game,
        targetPlayer(game, player, effect.player),
        effect.amount
    ) }
    register<DiscardRandom> { game, player, _, effect -> discardRandom(
        game,
        targetPlayer(game, player, effect.player),
        effect.amount
    ) }
    register<DiscardTopDeck> { game, sourcePlayer, _, effect ->
        discardTopDeck(game, targetPlayer(game, sourcePlayer, effect.player))
    }
    register<ReturnToHand> { game, _, sourceCard, effect -> returnToHand(game, sourceCard, effect) }
    register<ReturnFromDiscardToHand> { game, player, _, effect -> returnFromDiscardToHand(
        game,
        targetPlayer(game, player, effect.player),
        effect.amount
    ) }
    register<ReturnAllCreaturesOfHouseToHand> { game, _, _, effect -> returnAllCreaturesOfHouseToHand(game, effect) }
    register<ReturnCreaturesOfHouseFromDiscardToHand> { game, sourcePlayer, _, effect ->
        returnCreaturesOfHouseFromDiscardToHand(game, targetPlayer(game, sourcePlayer, effect.player))
    }
    register<Archive> { game, player, sourceCard, effect -> archive(game, player, sourceCard, effect) }
    register<ArchiveTopDeck> { game, player, _, effect -> archiveTopDeck(game, targetPlayer(game, player, effect.player)) }
    register<ArchiveFromDiscard> { game, player, _, effect -> archiveFromDiscard(game, targetPlayer(game, player, effect.player), effect.amount) }
    register<ArchiveFromHand> { game, player, _, effect -> archiveFromHand(game, targetPlayer(game, player, effect.player), effect.amount) }
    register<ArchiveCreatureFromHand> { game, player, _, effect -> archiveCreatureFromHand(game, targetPlayer(game, player, effect.player), effect.amount) }
    register<Purge> { game, player, sourceCard, effect -> purge(game, player, sourceCard, effect) }
    register<PurgeFromDiscard> { game, player, _, effect -> purgeFromDiscard(
        game,
        targetPlayer(game, player, effect.player),
        effect.amount
    ) }
    register<ShuffleSelfIntoDeck> { game, _, sourceCard, _ -> shuffleSelfIntoDeck(game, sourceCard) }
    register<ShuffleHandAndDiscardIntoDeck> { game, player, _, effect -> shuffleHandAndDiscardIntoDeck(
        game,
        targetPlayer(game, player, effect.player)
    ) }
    register<ShuffleCreaturesIntoDeck> { game, player, sourceCard, effect ->
        shuffleCreaturesIntoDeck(game, player, sourceCard, effect)
    }
    register<PutSelfOnTopOfDeck> { game, _, sourceCard, _ -> putSelfOnTopOfDeck(game, sourceCard) }
    register<PlayTopCard> { game, player, _, effect -> playTopCard(game, targetPlayer(game, player, effect.player)) }
    register<SwapDeckAndDiscard> { game, player, _, effect -> swapDeckAndDiscard(
        game,
        targetPlayer(game, player, effect.player)
    ) }
    register<DestroyArtifact> { game, player, sourceCard, effect ->
        val artifacts = game.players.flatMap { it.artifacts }
        val target = when (effect.target) {
            AnyArtifact -> artifacts.sortedByDescending { it.power }.firstOrNull()
            else -> null
        }
        if (target != null) destroyArtifact(game, target)
        else game.log("${player.name} finds no artifact to destroy")
    }
    register<ReturnArtifactToHand> { game, player, sourceCard, effect ->
        var returned = 0
        while (returned < effect.maxCount) {
            // Greedy: the opponent's strongest artifact first, then our own (never the source itself).
            val enemy = game.opponent(player).artifacts.maxByOrNull { it.power }
            val own = player.artifacts.filter { it.instanceId != sourceCard.instanceId }.maxByOrNull { it.power }
            val target = enemy ?: own ?: break
            returnArtifactToHand(game, target)
            returned++
        }
    }
    register<ArchiveFriendlyCreatureOrArtifact> { game, player, sourceCard, _ ->
        // Greedy: prefer a damaged friendly creature (archiving it removes it from
        // danger), else an artifact, else a ready creature.
        val creature = player.creatures
            .filter { it.damage > 0 }
            .maxByOrNull { it.power }
            ?: player.creatures.maxByOrNull { it.damage + it.power }
        val artifact = player.artifacts.maxByOrNull { it.power }
        val target = when {
            creature != null -> creature
            artifact != null -> artifact
            else -> targetChooser.chooseCreature(game, FriendlyCreature(), sourceCard).firstOrNull()
        } ?: run {
            game.log("${player.name} has nothing to archive from play")
            return@register
        }
        if (target.type == CardType.ARTIFACT) {
            player.artifacts.remove(target)
            player.archives.add(target.cardId)
            game.log("${target.cardId} is archived")
        } else {
            player.creatures.remove(target)
            target.upgrades.forEach { upgrade -> player.discard.add(upgrade.cardId) }
            target.upgrades.clear()
            player.archives.add(target.cardId)
            game.log("${target.cardId} is archived")
            if (library != null) {
                EventBus.emit(library, game, CardLeftPlay(target.instanceId, target.cardId, target.ownerIndex, target.controllerIndex))
            }
        }
    }
    register<DiscardEnemyHandCreaturesOfChosenHouse> { game, player, _, _ ->
        val opponent = game.opponent(player)
        val counts = opponent.hand.mapNotNull { library?.cardData(it) }
            .filter { it.type == CardType.CREATURE }
            .groupingBy { it.house }
            .eachCount()
        val chosen = counts.maxByOrNull { it.value }?.key
        if (chosen == null) {
            game.log("${player.name} reveals no creatures in ${opponent.name}'s hand")
            return@register
        }
        game.log("${player.name} chooses $chosen and reveals ${opponent.name}'s hand")
        val discarded = opponent.hand.filter { cardId ->
            val data = library?.cardData(cardId)
            data?.type == CardType.CREATURE && data.house == chosen
        }
        discarded.forEach { cardId -> opponent.hand.remove(cardId); opponent.discard.add(cardId) }
        game.log("${opponent.name} discards ${discarded.size} $chosen creature(s): ${discarded.joinToString(", ") { cardName(it) }}")
    }
    register<ScanDeckForHouseCreatureToHand> { game, player, _, effect ->
        val discarded = mutableListOf<String>()
        var found: String? = null
        while (player.library.isNotEmpty()) {
            val cardId = player.library.removeFirst()
            val data = library?.cardData(cardId)
            if (data?.type == CardType.CREATURE && data.house == effect.house) {
                found = cardId
                break
            }
            discarded.add(cardId)
        }
        if (discarded.isNotEmpty()) {
            player.discard.addAll(discarded)
            game.log("${player.name} discards ${discarded.size} card(s) from the top of their deck")
        }
        if (found != null) {
            player.hand.add(found)
            game.log("${player.name} takes ${cardName(found)} into their hand")
        } else {
            game.log("${player.name} finds no ${effect.house.name.lowercase()} creature in their deck")
        }
    }
    register<ShuffleHouseCreaturesIntoDeck> { game, _, sourceCard, effect ->
        game.players.forEach { p ->
            val creatures = p.creatures.filter { it.house == effect.house && it.ownerIndex == p.index }
            if (creatures.isEmpty()) return@forEach
            if (effect.amberPerEach > 0) {
                gainAmber(game, p, creatures.size * effect.amberPerEach)
            }
            creatures.forEach { leavePlay(game, it, LeaveDestination.SHUFFLE_INTO_DECK, "${it.cardId} is shuffled into ${p.name}'s deck") }
            game.log("${p.name} gains ${creatures.size * effect.amberPerEach} amber for ${creatures.size} creature(s) shuffled into their deck")
        }
    }
    register<StackHandCardBeneath> { game, player, sourceCard, _ ->
        val hand = player.hand.filter { it != sourceCard.cardId }
        // Prefer one-shot action cards; upgrades are the last resort (they need a
        // creature target when played from beneath).
        val cardId = hand.firstOrNull { library?.cardData(it)?.type == CardType.ACTION }
            ?: hand.firstOrNull { library?.cardData(it)?.type != CardType.UPGRADE }
            ?: hand.firstOrNull()
            ?: return@register
        player.hand.remove(cardId)
        sourceCard.beneathCardIds.add(cardId)
        game.log("${player.name} places ${cardName(cardId)} facedown beneath ${sourceCard.cardId}")
    }
    register<PlayBeneathCardAndSacrifice> { game, player, sourceCard, _ ->
        val cardId = sourceCard.beneathCardIds.removeFirstOrNull()
        if (cardId == null) {
            game.log("${sourceCard.cardId} has no card beneath it")
            sacrificeCreature(game, sourceCard)
            return@register
        }
        val bridge = playBridge
        val isUpgrade = library?.cardData(cardId)?.type == CardType.UPGRADE
        val host = player.creatures.maxByOrNull { it.power }
        when {
            bridge == null -> {
                game.log("${player.name} plays ${cardName(cardId)} from beneath ${sourceCard.cardId} (approximated)")
                sacrificeCreature(game, sourceCard)
            }
            isUpgrade && host == null -> {
                // An upgrade without a creature to attach to stays beneath.
                sourceCard.beneathCardIds.add(cardId)
                game.log("${player.name} has no creature to attach ${cardName(cardId)} to")
                sacrificeCreature(game, sourceCard)
            }
            else -> {
                player.hand.add(cardId)
                // Upgrades are played attached to the strongest own creature.
                bridge.playCardFromHand(game, player, cardId, if (isUpgrade) host?.instanceId else null)
                sacrificeCreature(game, sourceCard)
            }
        }
    }
    register<ReturnUrchinToHand> { game, player, _, _ ->
        val urchin = player.creatures.find { it.cardId == "urchin" }
        if (urchin != null) {
            leavePlay(game, urchin, LeaveDestination.HAND, "${urchin.cardId} returns to hand")
            return@register
        }
        if (player.discard.remove("urchin")) {
            player.hand.add("urchin")
            game.log("${player.name} returns urchin from their discard pile to hand")
            return@register
        }
        game.log("${player.name} has no urchin in play or discard")
    }
}

/** The bridge for playing a card through the real play path (ActionExecutor.playCard). */
internal interface CardPlayBridge {
    fun playCardFromHand(game: GameState, player: PlayerState, cardId: String, targetCreatureId: String? = null)
}

internal fun EffectResolver.destroyArtifact(game: GameState, artifact: InPlayCard) {
    val controller = game.player(artifact.controllerIndex)
    if (!controller.artifacts.remove(artifact)) return
    val owner = game.player(artifact.ownerIndex)
    owner.discard.add(artifact.cardId)
    game.log("${artifact.cardId} is destroyed")
    if (library != null) {
        EventBus.emit(
            library, game,
            CardLeftPlay(artifact.instanceId, artifact.cardId, artifact.ownerIndex, artifact.controllerIndex)
        )
    }
}

internal fun EffectResolver.returnArtifactToHand(game: GameState, artifact: InPlayCard) {
    val controller = game.player(artifact.controllerIndex)
    if (!controller.artifacts.remove(artifact)) return
    val owner = game.player(artifact.ownerIndex)
    owner.hand.add(artifact.cardId)
    game.log("${artifact.cardId} returns to hand")
    if (library != null) {
        EventBus.emit(
            library, game,
            CardLeftPlay(artifact.instanceId, artifact.cardId, artifact.ownerIndex, artifact.controllerIndex)
        )
    }
}

internal fun drawCards(game: GameState, player: PlayerState, amount: Int) {
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
    game.log("${player.name} draws $amount card(s)")
}

internal fun discardRandom(game: GameState, player: PlayerState, amount: Int) {
    repeat(amount.coerceAtMost(player.hand.size)) {
        val card = player.hand[game.rng.nextInt(player.hand.size)]
        player.hand.remove(card)
        player.discard.add(card)
    }
    game.log("${player.name} discards $amount card(s) at random")
}

internal fun EffectResolver.discardTopDeck(game: GameState, player: PlayerState) {
    ensureLibraryNotEmpty(game, player)
    val cardId = player.library.removeFirstOrNull() ?: return
    player.discard.add(cardId)
    game.log("${player.name} destroys the top card of their deck: ${cardName(cardId)}")
}

internal fun EffectResolver.returnToHand(game: GameState, sourceCard: InPlayCard, effect: ReturnToHand) {
    val targets = targetChooser.chooseCreature(game, effect.target, sourceCard)
    targets.forEach { target ->
        // Removal is by controller so taken-control creatures are found too.
        val controller = game.player(target.controllerIndex)
        controller.creatures.remove(target)
        // Upgrades fall off to the discard (mirrors the destroy path).
        target.upgrades.forEach { upgrade -> controller.discard.add(upgrade.cardId) }
        target.upgrades.clear()
        val owner = game.player(target.ownerIndex)
        owner.hand.add(target.cardId)
        game.log("${target.cardId} returns to hand")
        val event = CardLeftPlay(target.instanceId, target.cardId, target.ownerIndex, target.controllerIndex)
        if (library != null) {
            EventBus.deliver(library, game, game.player(target.controllerIndex), target, event)
            EventBus.emit(library, game, event)
        }
    }
}

internal fun returnFromDiscardToHand(game: GameState, player: PlayerState, amount: Int) {
    repeat(amount.coerceAtMost(player.discard.size)) {
        player.hand.add(player.discard.removeLast())
    }
    game.log("${player.name} returns $amount card(s) from discard to hand")
}

internal fun returnAllCreaturesOfHouseToHand(game: GameState, effect: ReturnAllCreaturesOfHouseToHand) {
    game.players.forEach { player ->
        player.creatures.filter { it.house == effect.house }.forEach { creature ->
            // Attached upgrades fall off to the owner's discard when the host leaves play.
            creature.upgrades.forEach { upgrade -> player.discard.add(upgrade.cardId) }
            player.creatures.remove(creature)
            player.hand.add(creature.cardId)
            game.log("${creature.cardId} returns to ${player.name}'s hand")
        }
    }
}

internal fun EffectResolver.returnCreaturesOfHouseFromDiscardToHand(game: GameState, player: PlayerState) {
    // The player would choose a house; we approximate by picking the house with the most creatures in discard.
    val creaturesInDiscard = player.discard.mapNotNull { library?.cardData(it) }
        .filter { it.type == CardType.CREATURE }
    val chosenHouse = creaturesInDiscard
        .groupingBy { it.house }
        .eachCount()
        .maxByOrNull { it.value }
        ?.key
    if (chosenHouse == null) {
        game.log("${player.name} returns no creatures from discard (no creatures to choose)")
        return
    }
    val returned = mutableListOf<String>()
    val iterator = player.discard.iterator()
    while (iterator.hasNext()) {
        val cardId = iterator.next()
        val card = library?.cardData(cardId)
        if (card?.type == CardType.CREATURE && card.house == chosenHouse) {
            iterator.remove()
            player.hand.add(cardId)
            returned.add(cardId)
        }
    }
    player.chains += 1
    game.log("${player.name} chooses ${chosenHouse.name.lowercase()} and returns ${returned.size} creature(s) from discard to hand, gaining 1 chain")
}

internal fun EffectResolver.archive(game: GameState, sourcePlayer: PlayerState, sourceCard: InPlayCard, effect: Archive) {
    val targets = targetChooser.chooseCreature(game, effect.target, sourceCard)
    targets.forEach { target ->
        val owner = game.player(target.ownerIndex)
        owner.creatures.remove(target)
        owner.archives.add(target.cardId)
        game.log("${target.cardId} is archived")
    }
}

internal fun EffectResolver.archiveTopDeck(game: GameState, player: PlayerState) {
    val cardId = player.library.removeFirstOrNull() ?: return
    player.archives.add(cardId)
    game.log("${player.name} archives ${cardName(cardId)} from the top of their deck")
}

internal fun EffectResolver.archiveFromDiscard(game: GameState, player: PlayerState, amount: Int) {
    val archived = mutableListOf<String>()
    repeat(amount.coerceAtMost(player.discard.size)) {
        val cardId = player.discard.removeLast()
        archived.add(cardId)
        player.archives.add(cardId)
    }
    val names = archived.joinToString(", ") { cardName(it) }
    game.log("${player.name} archives $amount card(s) from discard: $names")
}

internal fun EffectResolver.archiveFromHand(game: GameState, player: PlayerState, amount: Int) {
    val archived = mutableListOf<String>()
    repeat(amount.coerceAtMost(player.hand.size)) {
        val cardId = player.hand.randomWith(game.rng) ?: return
        player.hand.remove(cardId)
        archived.add(cardId)
        player.archives.add(cardId)
    }
    val names = archived.joinToString(", ") { cardName(it) }
    game.log("${player.name} archives ${archived.size} card(s) from hand: $names")
}

/**
 * Archives creature cards from hand (Zyzzix the Many). Returns whether any
 * creature was archived, so follow-up effects can be gated on it.
 */
internal fun EffectResolver.archiveCreatureFromHand(game: GameState, player: PlayerState, amount: Int): Boolean {
    val creaturesInHand = player.hand.filter { library?.cardData(it)?.type == CardType.CREATURE }.toMutableList()
    if (creaturesInHand.isEmpty()) {
        game.log("${player.name} has no creature to archive from hand")
        return false
    }
    val archived = mutableListOf<String>()
    repeat(amount.coerceAtMost(creaturesInHand.size)) {
        val cardId = creaturesInHand.removeFirst()
        player.hand.remove(cardId)
        archived.add(cardId)
        player.archives.add(cardId)
    }
    val names = archived.joinToString(", ") { cardName(it) }
    game.log("${player.name} archives ${archived.size} creature(s) from hand: $names")
    return true
}

internal fun EffectResolver.purge(game: GameState, sourcePlayer: PlayerState, sourceCard: InPlayCard, effect: Purge) {
    val targets = targetChooser.chooseCreature(game, effect.target, sourceCard)
    targets.forEach { target ->
        val owner = game.player(target.ownerIndex)
        owner.creatures.remove(target)
        owner.purged.add(target.cardId)
        game.log("${target.cardId} is purged")
    }
}

internal fun purgeFromDiscard(game: GameState, player: PlayerState, amount: Int) {
    repeat(amount.coerceAtMost(player.discard.size)) {
        val cardId = player.discard.removeLast()
        player.purged.add(cardId)
    }
    game.log("${player.name} purges $amount card(s) from discard")
}

internal fun EffectResolver.shuffleSelfIntoDeck(game: GameState, sourceCard: InPlayCard) {
    // Removal is by controller so taken-control cards are found too.
    val controller = game.player(sourceCard.controllerIndex)
    val removed = controller.creatures.remove(sourceCard) || controller.artifacts.remove(sourceCard)
    if (removed) {
        // Upgrades fall off to the discard (mirrors the destroy path).
        sourceCard.upgrades.forEach { upgrade -> controller.discard.add(upgrade.cardId) }
        sourceCard.upgrades.clear()
        val owner = game.player(sourceCard.ownerIndex)
        owner.library.add(sourceCard.cardId)
        owner.library.shuffle(game.rng)
        game.log("${sourceCard.cardId} shuffles into ${owner.name}'s deck")
        if (library != null) {
            EventBus.emit(library, game, CardLeftPlay(sourceCard.instanceId, sourceCard.cardId, sourceCard.ownerIndex, sourceCard.controllerIndex))
        }
    }
}

internal fun shuffleHandAndDiscardIntoDeck(game: GameState, player: PlayerState) {
    player.library.addAll(player.hand)
    player.library.addAll(player.discard)
    player.hand.clear()
    player.discard.clear()
    player.library.shuffle(game.rng)
    game.log("${player.name} shuffles hand and discard into deck")
}

/** Shuffles chosen friendly/enemy creatures into their owners' decks (Lost in the Woods). */
internal fun EffectResolver.shuffleCreaturesIntoDeck(
    game: GameState,
    player: PlayerState,
    sourceCard: InPlayCard,
    effect: ShuffleCreaturesIntoDeck
) {
    val shuffled = mutableListOf<InPlayCard>()
    shuffled += targetChooser.chooseCreature(game, FriendlyCreature(Scope.ALL), sourceCard)
        .cheapestFirst().take(effect.friendlyCount)
    shuffled += targetChooser.chooseCreature(game, EnemyCreature(Scope.ALL), sourceCard).take(effect.enemyCount)
    shuffled.forEach { target ->
        leavePlay(game, target, LeaveDestination.SHUFFLE_INTO_DECK, "${target.cardId} shuffles into its owner's deck")
    }
}

/** Moves the resolving card from play to the top of its owner's deck (Dextre). */
internal fun EffectResolver.putSelfOnTopOfDeck(game: GameState, sourceCard: InPlayCard) {
    val owner = game.player(sourceCard.ownerIndex)
    leavePlay(game, sourceCard, LeaveDestination.TOP_OF_DECK, "${sourceCard.cardId} is put on top of ${owner.name}'s deck")
}

internal fun playTopCard(game: GameState, player: PlayerState) {
    val cardId = player.library.removeFirstOrNull() ?: return
    player.creatures.add(
        InPlayCard(
            instanceId = java.util.UUID.randomUUID().toString(),
            cardId = cardId,
            ownerIndex = player.index,
            controllerIndex = player.index,
            zone = Zone.PLAY_AREA,
            house = player.activeHouse ?: House.BROBNAR,
            type = CardType.CREATURE,
            exhausted = true,
            power = 3
        )
    )
    game.log("${player.name} plays the top card of their deck ($cardId)")
}

internal fun swapDeckAndDiscard(game: GameState, player: PlayerState) {
    val oldDeck = player.library.toList()
    player.library.clear()
    player.library.addAll(player.discard)
    player.discard.clear()
    player.discard.addAll(oldDeck)
    player.library.shuffle(game.rng)
    game.log("${player.name} swaps deck and discard pile")
}

/** Where a card that leaves play through an effect ends up. */
internal enum class LeaveDestination { DISCARD, SHUFFLE_INTO_DECK, TOP_OF_DECK, HAND }

/**
 * Shared "creature leaves play" removal for effect-driven removals (sacrifice,
 * shuffle-into-deck, put-self-on-deck). Mirrors the destroy path in
 * [com.aember.analyzer.domain.engine.action.ActionExecutor.destroyCreature]:
 * upgrades fall off to their owner's discard and the Annihilation Ritual
 * purge replacement applies to discards (not to explicit deck destinations).
 */
internal fun EffectResolver.leavePlay(game: GameState, card: InPlayCard, destination: LeaveDestination, logLine: String) {
    val controller = game.player(card.controllerIndex)
    val removed = controller.creatures.remove(card) || controller.artifacts.remove(card)
    if (!removed) return
    // Upgrades fall off to their owner's discard (mirrors the destroy path).
    card.upgrades.forEach { upgrade -> game.player(upgrade.ownerIndex).discard.add(upgrade.cardId) }
    card.upgrades.clear()
    val owner = game.player(card.ownerIndex)
    when (destination) {
        LeaveDestination.SHUFFLE_INTO_DECK -> {
            owner.library.add(card.cardId)
            owner.library.shuffle(game.rng)
            game.log(logLine)
        }
        LeaveDestination.TOP_OF_DECK -> {
            owner.library.add(0, card.cardId)
            game.log(logLine)
        }
        LeaveDestination.HAND -> {
            owner.hand.add(card.cardId)
            game.log(logLine)
        }
        LeaveDestination.DISCARD -> {
            // If a script already moved the card (Bad Penny to hand, Dextre to deck,
            // …), do not also discard it.
            val alreadyMoved = owner.hand.contains(card.cardId) || owner.archives.contains(card.cardId) ||
                owner.purged.contains(card.cardId) || owner.library.contains(card.cardId)
            when {
                alreadyMoved -> game.log(logLine)
                purgeReplacementActive(game) -> {
                    owner.purged.add(card.cardId)
                    game.log("${card.cardId} is purged instead of discarded")
                }
                else -> {
                    owner.discard.add(card.cardId)
                    game.log(logLine)
                }
            }
        }
    }
    if (library != null) {
        EventBus.emit(
            library, game,
            CardLeftPlay(card.instanceId, card.cardId, card.ownerIndex, card.controllerIndex)
        )
    }
}

internal fun EffectResolver.purgeReplacementActive(game: GameState): Boolean {
    if (library == null) return false
    return game.players.any { p ->
        p.cardsInPlay().any { library.scriptFor(it.cardId).purgesDestroyedCreatures() }
    }
}

/** Removes a creature from play and puts it in its owner's discard (sacrifice). */
internal fun EffectResolver.sacrificeCreature(game: GameState, card: InPlayCard) {
    leavePlay(game, card, LeaveDestination.DISCARD, "${card.cardId} is sacrificed")
}
