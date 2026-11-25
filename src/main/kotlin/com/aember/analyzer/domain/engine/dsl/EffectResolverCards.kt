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
    register<Archive> { game, _, sourceCard, effect -> archive(game, sourceCard, effect) }
    register<ArchiveTopDeck> { game, player, _, effect -> archiveTopDeck(game, targetPlayer(game, player, effect.player)) }
    register<ArchiveFromDiscard> { game, player, _, effect -> archiveFromDiscard(game, targetPlayer(game, player, effect.player), effect.amount) }
    register<ArchiveFromHand> { game, player, _, effect -> archiveFromHand(game, targetPlayer(game, player, effect.player), effect.amount) }
    register<ArchiveCreatureFromHand> { game, player, _, effect -> archiveCreatureFromHand(game, targetPlayer(game, player, effect.player), effect.amount) }
    register<Purge> { game, _, sourceCard, effect -> purge(game, sourceCard, effect) }
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
    register<ShuffleCreaturesIntoDeck> { game, _, sourceCard, effect ->
        shuffleCreaturesIntoDeck(game, sourceCard, effect)
    }
    register<PutSelfOnTopOfDeck> { game, _, sourceCard, _ -> putSelfOnTopOfDeck(game, sourceCard) }
    register<PlayTopCard> { game, player, _, effect -> playTopCard(game, targetPlayer(game, player, effect.player)) }
    register<SwapDeckAndDiscard> { game, player, _, effect -> swapDeckAndDiscard(
        game,
        targetPlayer(game, player, effect.player)
    ) }
    register<DestroyArtifact> { game, player, sourceCard, effect ->
        // Greedy: an opponent's artifact first (never the source itself), our own only as a last resort.
        val enemyArtifacts = game.players[1 - sourceCard.controllerIndex].artifacts
        val ownArtifacts = game.players[sourceCard.controllerIndex].artifacts.filter { it.instanceId != sourceCard.instanceId }
        val target = when (effect.target) {
            AnyArtifact -> (enemyArtifacts.sortedByDescending { it.power } + ownArtifacts.sortedByDescending { it.power }).firstOrNull()
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
    register<ShuffleHouseCreaturesIntoDeck> { game, _, _, effect ->
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
    register<PlayBeneathCardAndSacrifice> { game, player, sourceCard, _ ->        val cardId = sourceCard.beneathCardIds.removeFirstOrNull()
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
    register<PlayCopyOfOpponentDiscardAction> { game, player, sourceCard, _ ->
        if (game.playCopyDepth >= 5) {
            game.log("${player.name}'s copy chain stops (Mimicry depth limit)")
            return@register
        }
        val opponent = game.opponent(player)
        val chosen = opponent.discard.lastOrNull { library?.cardData(it)?.type == CardType.ACTION }
        if (chosen == null) {
            game.log("${player.name} finds no action card in ${opponent.name}'s discard")
            return@register
        }
        game.log("${player.name} plays Mimicry as a copy of ${cardName(chosen)}")
        val copyCard = InPlayCard(
            instanceId = sourceCard.instanceId,
            cardId = chosen,
            ownerIndex = player.index,
            controllerIndex = player.index,
            zone = Zone.PLAY_AREA,
            house = library?.cardData(chosen)?.house ?: House.BROBNAR,
            type = CardType.ACTION
        )
        game.playCopyDepth++
        library?.scriptFor(chosen)?.onPlay(game, player, copyCard)
        game.playCopyDepth--
    }
    register<SearchForCreatureToHand> { game, player, _, effect ->
        val found = mutableListOf<String>()
        player.discard.filter { it == effect.cardName && library?.cardData(it)?.type == CardType.CREATURE }
            .forEach { found.add(it) }
        player.library.filter { it == effect.cardName && library?.cardData(it)?.type == CardType.CREATURE }
            .forEach { found.add(it) }
        val taken = found.take(effect.maxCards)
        if (taken.isEmpty()) {
            game.log("${player.name} finds no ${effect.cardName} in deck or discard")
        } else {
            // Remove exactly the chosen copies (one occurrence each), discard first.
            taken.forEach { id -> if (!player.discard.remove(id)) player.library.remove(id) }
            player.hand.addAll(taken)
            game.log("${player.name} searches and puts ${taken.joinToString(", ") { cardName(it) }} into their hand")
        }
        // The shuffle is unconditional (keyteki sequences it after the search).
        if (effect.shuffleDiscardIntoDeck && player.discard.isNotEmpty()) {
            player.library.addAll(player.discard)
            player.discard.clear()
            player.library.shuffle(game.rng)
            game.log("${player.name} shuffles their discard pile into their deck")
        }
    }
    register<HealOrSearchAncientCreatures> { game, player, sourceCard, effect ->
        val inPlay = game.players.flatMap { it.creatures }.filter { it.cardId == effect.cardName }
        if (inPlay.isNotEmpty()) {
            inPlay.forEach { it.damage = 0 }
            game.log("${sourceCard.cardId} fully heals the ${effect.cardName} in play")
            return@register
        }
        // No [cardName] in play: search deck and discard for each copy.
        val found = (player.discard + player.library).filter { it == effect.cardName }
        if (found.isEmpty()) {
            game.log("${player.name} finds no ${effect.cardName} anywhere")
            return@register
        }
        player.discard.removeAll(found)
        player.library.removeAll(found)
        player.hand.addAll(found)
        game.log("${player.name} puts ${found.joinToString(", ") { cardName(it) }} into their hand and shuffles their discard pile into their deck")
        if (player.discard.isNotEmpty()) {
            player.library.addAll(player.discard)
            player.discard.clear()
            player.library.shuffle(game.rng)
        }
    }
    register<ArchiveHouseCreatureFromHand> { game, player, _, effect ->
        val candidates = player.hand.filter { library?.cardData(it)?.type == CardType.CREATURE && library.cardData(it)?.house == effect.house }
        val cardId = candidates.firstOrNull()
        if (cardId == null) {
            game.log("${player.name} has no ${effect.house.name.lowercase()} creature to archive from hand")
            return@register
        }
        player.hand.remove(cardId)
        player.archives.add(cardId)
        game.log("${player.name} reveals ${cardName(cardId)} and archives it")
    }
    register<ArchiveSelf> { game, _, sourceCard, _ ->
        leavePlay(game, sourceCard, LeaveDestination.ARCHIVES, "${sourceCard.cardId} is archived")
    }
    register<AbductEnemyDamagedCreatures> { game, player, _, effect ->
        val damaged = game.opponent(player).creatures.filter { it.damage > 0 }
            .sortedByDescending { it.power }.take(effect.amount)
        if (damaged.isEmpty()) {
            game.log("${player.name} has no damaged enemy creatures to abduct")
            return@register
        }
        damaged.forEach { creature ->
            val controller = game.player(creature.controllerIndex)
            controller.creatures.remove(creature)
            creature.upgrades.forEach { upgrade -> game.player(upgrade.ownerIndex).discard.add(upgrade.cardId) }
            creature.upgrades.clear()
            player.archives.add(creature.cardId)
            player.abductedCardIds.add(creature.cardId)
            game.log("${player.name} abducts ${creature.cardId} into their archives")
            if (library != null) {
                EventBus.emit(library, game, CardLeftPlay(creature.instanceId, creature.cardId, creature.ownerIndex, creature.controllerIndex))
            }
        }
    }
    register<SwapWithOtherFriendlyCreature> { game, player, sourceCard, _ ->
        val others = player.creatures.filter { it.instanceId != sourceCard.instanceId }
        val other = others.maxByOrNull { it.power } ?: run {
            game.log("${sourceCard.cardId} has no other friendly creature to swap with")
            return@register
        }
        val idxSource = player.creatures.indexOf(sourceCard)
        val idxOther = player.creatures.indexOf(other)
        if (idxSource >= 0 && idxOther >= 0) {
            player.creatures[idxSource] = other
            player.creatures[idxOther] = sourceCard
            game.log("${sourceCard.cardId} and ${other.cardId} swap places in the battleline")
        }
    }
    register<SwapHostAndMayUseOther> { game, player, sourceCard, _ ->
        val hostId = sourceCard.hostId ?: return@register
        val host = findCreatureInPlay(game, hostId) ?: return@register
        val others = player.creatures.filter { it.instanceId != hostId }
        val other = others.maxByOrNull { it.power } ?: run {
            game.log("${sourceCard.cardId} has no other friendly creature to swap with")
            return@register
        }
        val idxHost = player.creatures.indexOf(host)
        val idxOther = player.creatures.indexOf(other)
        if (idxHost >= 0 && idxOther >= 0) {
            player.creatures[idxHost] = other
            player.creatures[idxOther] = host
            game.log("${host.cardId} and ${other.cardId} swap places in the battleline")
        }
        player.mayUseCardIdsThisTurn.add(other.instanceId)
        game.log("${player.name} may use ${other.cardId} this turn")
    }
    register<RotateHouseCreatures> { game, player, _, effect ->
        val ready = player.creatures.filter { it.house == effect.house && !it.exhausted && !it.stun }.maxByOrNull { it.power }
        if (ready == null) {
            game.log("${player.name} has no ready ${effect.house.name.lowercase()} creature to return")
            return@register
        }
        leavePlay(game, ready, LeaveDestination.HAND, "${ready.cardId} returns to hand")
        player.hand.add(ready.cardId)
        // A different-named creature of that house enters play (real play path) and readies.
        val handMatch = player.hand.filter { cardId ->
            val data = library?.cardData(cardId)
            data?.type == CardType.CREATURE && data.house == effect.house && cardId != ready.cardId
        }.maxByOrNull { library?.cardData(it)?.power ?: 0 }
        if (handMatch == null) {
            game.log("${player.name} has no other ${effect.house.name.lowercase()} creature in hand")
            return@register
        }
        val bridge = playBridge
        if (bridge != null) {
            bridge.playCardFromHand(game, player, handMatch)
        } else {
            player.hand.remove(handMatch)
            player.discard.add(handMatch)
            game.log("${player.name} plays ${cardName(handMatch)} (approximated)")
        }
        val entered = player.creatures.lastOrNull { it.cardId == handMatch }
        if (entered != null) {
            entered.exhausted = false
            game.log("${entered.cardId} readies")
        }
    }
    register<UseArtifactAndDestroyIt> { game, player, _, _ ->
        // Any player's artifact with an Action ability; greedy: the opponent's first.
        val artifact = game.opponent(player).artifacts.firstOrNull { candidate ->
            !candidate.exhausted &&
                library?.scriptFor(candidate.cardId)?.actionAbilities()?.any { it != "Omni" } == true
        } ?: player.artifacts.firstOrNull { candidate ->
            !candidate.exhausted &&
                library?.scriptFor(candidate.cardId)?.actionAbilities()?.any { it != "Omni" } == true
        }
        if (artifact == null) {
            game.log("${player.name} finds no usable artifact")
            return@register
        }
        val ability = library?.scriptFor(artifact.cardId)?.actionAbilities()?.first { it != "Omni" } ?: return@register
        artifact.exhausted = true
        game.log("${player.name} uses ${artifact.cardId} as if it were theirs")
        library.scriptFor(artifact.cardId).resolveActionAbility(ability, game, player, artifact)
        destroyArtifact(game, artifact)
    }
    register<ReturnDiscardCreatureToTopOfDeck> { game, player, _, _ ->
        val cardId = player.discard.lastOrNull { library?.cardData(it)?.type == CardType.CREATURE }
        if (cardId == null) {
            game.log("${player.name} has no creature in their discard pile")
            return@register
        }
        player.discard.remove(cardId)
        player.library.add(0, cardId)
        game.log("${player.name} puts ${cardName(cardId)} on top of their deck")
    }
    register<PlayCreatureFromDiscard> { game, player, _, _ ->
        val cardId = player.discard.lastOrNull { library?.cardData(it)?.type == CardType.CREATURE }
        if (cardId == null) {
            game.log("${player.name} has no creature in their discard pile to play")
            return@register
        }
        player.discard.remove(cardId)
        val bridge = playBridge
        if (bridge != null) {
            player.hand.add(cardId)
            bridge.playCardFromHand(game, player, cardId)
        } else {
            player.discard.add(cardId)
            game.log("${player.name} plays ${cardName(cardId)} from their discard pile (approximated)")
        }
    }
    register<PurgeFriendlyHumanAndPlayFromDiscard> { game, player, sourceCard, _ ->
        val target = targetChooser.chooseCreature(game, FriendlyCreatureWithTrait("human"), sourceCard).firstOrNull()
        if (target == null) {
            game.log("${player.name} has no friendly Human creature to purge")
            return@register
        }
        game.player(target.ownerIndex).creatures.remove(target)
        target.upgrades.forEach { upgrade -> game.player(upgrade.ownerIndex).discard.add(upgrade.cardId) }
        target.upgrades.clear()
        game.player(target.ownerIndex).purged.add(target.cardId)
        game.log("${target.cardId} is purged from play")
        // Play a creature from the discard pile (real play path when bridged).
        val cardId = player.discard.lastOrNull { library?.cardData(it)?.type == CardType.CREATURE }
        if (cardId == null) {
            game.log("${player.name} has no creature in their discard pile to play")
            return@register
        }
        player.discard.remove(cardId)
        val bridge = playBridge
        if (bridge != null) {
            player.hand.add(cardId)
            bridge.playCardFromHand(game, player, cardId)
        } else {
            player.discard.add(cardId)
            game.log("${player.name} plays ${cardName(cardId)} from their discard pile (approximated)")
        }
    }
    register<ReturnAllArtifactsToTopOfDeck> { game, _, _, _ ->
        game.players.forEach { p ->
            p.artifacts.toList().forEach { artifact ->
                leavePlay(game, artifact, LeaveDestination.TOP_OF_DECK, "${artifact.cardId} goes on top of its owner's deck")
            }
        }
    }
    register<PurgeEnemyHandCardOfHouse> { game, player, _, effect ->
        val opponent = game.opponent(player)
        val cardId = opponent.hand.firstOrNull { library?.cardData(it)?.house == effect.house }
        if (cardId == null) {
            game.log("${player.name} finds no ${effect.house.name.lowercase()} card in ${opponent.name}'s hand")
            return@register
        }
        opponent.hand.remove(cardId)
        player.purged.add(cardId)
        game.log("${player.name} looks at ${opponent.name}'s hand and purges ${cardName(cardId)}")
    }
}

/** The bridge for playing a card through the real play path (ActionExecutor.playCard). */
internal interface CardPlayBridge {
    fun playCardFromHand(game: GameState, player: PlayerState, cardId: String, targetCreatureId: String? = null)

    /** Real keyword-aware fight (Scout's granted fights). */
    fun fightWith(game: GameState, player: PlayerState, attackerInstanceId: String, targetInstanceId: String): Boolean = false
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

internal fun EffectResolver.archive(game: GameState, sourceCard: InPlayCard, effect: Archive) {
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

internal fun EffectResolver.purge(game: GameState, sourceCard: InPlayCard, effect: Purge) {
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

internal fun EffectResolver.shuffleCreaturesIntoDeck(
    game: GameState,
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
internal enum class LeaveDestination { DISCARD, SHUFFLE_INTO_DECK, TOP_OF_DECK, HAND, ARCHIVES, PURGED }

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
    card.upgrades.forEach { upgrade ->
        game.player(upgrade.ownerIndex).discard.add(upgrade.cardId)
        if (library != null) {
            val upgradeEvent = CardLeftPlay(upgrade.instanceId, upgrade.cardId, upgrade.ownerIndex, upgrade.controllerIndex)
            EventBus.deliver(library, game, game.player(upgrade.controllerIndex), upgrade, upgradeEvent)
            EventBus.emit(library, game, upgradeEvent)
        }
    }
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
        LeaveDestination.ARCHIVES -> {
            // Card-id archives are only read (never extracted) by the current engine;
            // Mass Abduction's "leaves the archives -> owner's hand" rule is handled
            // in the abduct handler's tracking and applies whenever an extraction
            // path is added.
            owner.archives.add(card.cardId)
            game.log(logLine)
        }
        LeaveDestination.PURGED -> {
            owner.purged.add(card.cardId)
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
    return library != null && game.players.any { p ->
        p.cardsInPlay().any { library.scriptFor(it.cardId).purgesDestroyedCreatures() }
    }
}

internal fun EffectResolver.sacrificeCreature(game: GameState, card: InPlayCard) {
    leavePlay(game, card, LeaveDestination.DISCARD, "${card.cardId} is sacrificed")
}
