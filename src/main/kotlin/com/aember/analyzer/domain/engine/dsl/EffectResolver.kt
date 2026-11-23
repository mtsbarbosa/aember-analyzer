package com.aember.analyzer.domain.engine.dsl

import com.aember.analyzer.domain.engine.CardLibrary
import com.aember.analyzer.domain.engine.ForgeRules
import com.aember.analyzer.domain.engine.effects.EffectRegistry
import com.aember.analyzer.domain.engine.events.CardLeftPlay
import com.aember.analyzer.domain.engine.events.CreatureDestroyed
import com.aember.analyzer.domain.engine.events.EventBus
import com.aember.analyzer.domain.engine.events.FightResolved
import com.aember.analyzer.domain.engine.model.GameState
import com.aember.analyzer.domain.engine.model.InPlayCard
import com.aember.analyzer.domain.engine.model.PlayerState
import com.aember.analyzer.domain.engine.model.Zone
import com.aember.analyzer.domain.engine.model.randomWith
import com.aember.analyzer.domain.model.CardType
import com.aember.analyzer.domain.model.House
import kotlin.math.min
import kotlin.reflect.KClass

/**
 * Executes declarative card effects against the game state.
 */
class EffectResolver(
    private val targetChooser: TargetChooser = GreedyTargetChooser(),
    private val library: CardLibrary? = null
) {

    private val handlers = mutableMapOf<KClass<out Effect>, (GameState, PlayerState, InPlayCard, Effect) -> Unit>()

    init {
        register<NoEffect> { _, _, _, _ -> }
        register<GainAmber> { game, player, _, effect -> gainAmber(game, player, effect.amount) }
        register<GainAmberOnTriggerThisTurn> { _, player, _, effect ->
            player.turnTriggerAmber[effect.trigger] = player.turnTriggerAmber.getOrDefault(effect.trigger, 0) + effect.amount
        }
        register<DrawOnCardPlayThisTurn> { _, player, _, effect ->
            player.drawOnCardPlayActive = true
            player.drawOnCardPlayHouse = effect.house
        }
        register<DenyHouseNextTurn> { game, sourcePlayer, _, effect ->
            val target = targetPlayer(game, sourcePlayer, effect.target)
            target.deniedHouseNextTurn = effect.house
            game.log("${sourcePlayer.name} names ${effect.house} with Control the Weak; ${target.name} cannot choose it next turn")
        }
        register<TakeControlUntilEndOfTurn> { game, sourcePlayer, _, effect ->
            takeControlUntilEndOfTurn(game, sourcePlayer, effect)
        }
        register<PlayTopCardOfHouse> { game, sourcePlayer, _, effect ->
            playTopCardOfHouse(game, targetPlayer(game, sourcePlayer, effect.player), effect.house)
        }
        register<AddTopCardOfHouseToHand> { game, sourcePlayer, _, effect ->
            addTopCardOfHouseToHand(game, targetPlayer(game, sourcePlayer, effect.player), effect.house)
        }
        register<DiscardTopDeck> { game, sourcePlayer, _, effect ->
            discardTopDeck(game, targetPlayer(game, sourcePlayer, effect.player))
        }
        register<GainAmberEqualToOpponentKeys> { game, sourcePlayer, _, effect ->
            val target = targetPlayer(game, sourcePlayer, effect.player)
            val amount = game.opponent(target).keys
            if (amount > 0) gainAmber(game, target, amount)
        }
        register<TakeControlOfEnemyFlankCreature> { game, sourcePlayer, _, effect ->
            takeControlOfEnemyFlankCreature(game, targetPlayer(game, sourcePlayer, effect.player))
        }
        register<SkipForgeNextTurn> { game, sourcePlayer, _, effect ->
            targetPlayer(game, sourcePlayer, effect.player).skipsForgeNextTurn = true
            game.log("${sourcePlayer.name} plays Miasma; ${targetPlayer(game, sourcePlayer, effect.player).name} cannot forge a key next turn")
        }
        register<ReturnCreaturesOfHouseFromDiscardToHand> { game, sourcePlayer, _, effect ->
            returnCreaturesOfHouseFromDiscardToHand(game, targetPlayer(game, sourcePlayer, effect.player))
        }
        register<StealAmber> { game, player, _, effect -> stealAmber(game, player, effect.amount) }
        register<CaptureAmber> { game, player, _, effect -> captureAmber(game, player, effect) }
        register<LoseAmber> { game, player, _, effect -> loseAmber(game, targetPlayer(game, player, effect.player), effect.amount) }
        register<DealDamage> { game, _, sourceCard, effect -> dealDamage(game, sourceCard, effect) }
        register<Destroy> { game, player, _, effect -> destroy(game, player, effect) }
        register<Draw> { game, player, _, effect -> drawCards(game, targetPlayer(game, player, effect.player), effect.amount) }
        register<DiscardRandom> { game, player, _, effect -> discardRandom(game, targetPlayer(game, player, effect.player), effect.amount) }
        register<ReturnToHand> { game, _, sourceCard, effect -> returnToHand(game, sourceCard, effect) }
        register<Archive> { game, player, _, effect -> archive(game, player, effect) }
        register<Purge> { game, _, _, effect -> purge(game, effect) }
        register<Heal> { game, _, _, effect -> heal(game, effect) }
        register<Ready> { game, _, _, effect -> ready(game, effect) }
        register<Fight> { game, _, _, effect -> fight(game, effect) }
        register<GainChains> { game, player, _, effect -> targetPlayer(game, player, effect.player).chains += effect.amount }
        register<LoseChains> { game, player, _, effect ->
            val target = targetPlayer(game, player, effect.player)
            target.chains = (target.chains - effect.amount).coerceAtLeast(0)
        }
        register<Stun> { game, _, _, effect -> stun(game, effect) }
        register<Unstun> { game, _, _, effect -> unstun(game, effect) }
        register<DestroyMostPowerful> { game, player, _, effect -> destroyMostPowerful(game, player, effect.amount) }
        register<DestroyLeastPowerful> { game, player, _, effect -> destroyLeastPowerful(game, player, effect.amount) }
        register<DestroyWeakCreatures> { game, player, _, effect -> destroyWeakCreatures(game, player, effect.powerThreshold) }
        register<ReadyAndFight> { game, player, _, effect -> readyAndFight(game, player, effect) }
        register<ReadyAndUse> { game, _, _, effect -> readyAndUse(game, effect) }
        register<UseCreature> { game, _, _, effect -> useCreature(game, effect) }
        register<ReturnFromDiscardToHand> { game, player, _, effect -> returnFromDiscardToHand(game, targetPlayer(game, player, effect.player), effect.amount) }
        register<PlayTopCard> { game, player, _, effect -> playTopCard(game, targetPlayer(game, player, effect.player)) }
        register<SwapDeckAndDiscard> { game, player, _, effect -> swapDeckAndDiscard(game, targetPlayer(game, player, effect.player)) }
        register<PurgeFromDiscard> { game, player, _, effect -> purgeFromDiscard(game, targetPlayer(game, player, effect.player), effect.amount) }
        register<ArchiveTopDeck> { game, player, _, effect -> archiveTopDeck(game, targetPlayer(game, player, effect.player)) }
        register<ArchiveFromDiscard> { game, player, _, effect -> archiveFromDiscard(game, targetPlayer(game, player, effect.player), effect.amount) }
        register<ArchiveFromHand> { game, player, _, effect -> archiveFromHand(game, targetPlayer(game, player, effect.player), effect.amount) }
        register<ArchiveCreatureFromHand> { game, player, _, effect -> archiveCreatureFromHand(game, targetPlayer(game, player, effect.player), effect.amount) }
        register<LoseAllAmber> { game, player, _, effect -> loseAllAmber(game, targetPlayer(game, player, effect.player)) }
        register<GainAmberOpponent> { game, _, _, effect -> gainAmberOpponent(game, effect.amount) }
        register<LoseAmberOpponent> { game, player, _, effect ->
            loseAmber(game, game.opponent(player), effect.amount)
        }
        register<EventSideGainsAmber> { game, _, _, effect -> eventSideGainsAmber(game, effect) }
        register<EventSideLosesAmber> { game, _, _, effect -> eventSideLosesAmber(game, effect) }
        register<StealFrom> { game, player, _, effect -> stealFrom(game, player, effect) }
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
        register<Exhaust> { game, _, _, effect -> exhaust(game, effect) }
        register<ShuffleHandAndDiscardIntoDeck> { game, player, _, effect -> shuffleHandAndDiscardIntoDeck(game, targetPlayer(game, player, effect.player)) }
        register<ReturnAllCreaturesOfHouseToHand> { game, _, _, effect -> returnAllCreaturesOfHouseToHand(game, effect) }
        register<ForgeKey> { game, player, _, effect -> forgeKey(game, player, effect) }
        register<DrawPerCardInHand> { game, player, _, effect -> drawPerCardInHand(game, player, effect) }
        register<ReadyCreaturesOfHousePerCardInHand> { game, player, _, effect -> readyCreaturesOfHousePerCardInHand(game, player, effect) }
        register<AddPowerCounters> { game, _, sourceCard, effect ->
            // Power counters permanently raise the creature's base power; the
            // EffectRegistry then re-applies static auras on top of it.
            sourceCard.power += effect.amount
            sourceCard.basePower += effect.amount
            game.log("${sourceCard.cardId} gets ${effect.amount} +1 power counter(s) (power: ${sourceCard.power})")
        }
        register<SacrificeSelf> { game, _, sourceCard, _ ->
            val owner = game.player(sourceCard.ownerIndex)
            if (!owner.creatures.remove(sourceCard)) {
                owner.artifacts.remove(sourceCard)
            }
            owner.discard.add(sourceCard.cardId)
            game.log("${sourceCard.cardId} is sacrificed")
            if (library != null) {
                EventBus.emit(
                    library, game,
                    CardLeftPlay(sourceCard.instanceId, sourceCard.cardId, sourceCard.ownerIndex, sourceCard.controllerIndex)
                )
            }
        }
        register<RepeatForEach> { _, _, _, _ ->
            // Not used for simple cards yet.
        }
    }

    fun resolve(
        game: GameState,
        sourcePlayer: PlayerState,
        sourceCard: InPlayCard,
        effect: Effect
    ) {
        // Reset ephemeral trackers at the start of a top-level effect.
        if (sourceCard.instanceId != "dummy") {
            game.lastStealAmount = 0
            game.lastDamageDestroyed = false
            game.lastDamagedTargetId = null
        }
        resolveInternal(game, sourcePlayer, sourceCard, effect)
    }

    private fun resolveInternal(
        game: GameState,
        sourcePlayer: PlayerState,
        sourceCard: InPlayCard,
        effect: Effect
    ) {
        when (effect) {
            is Sequence -> effect.effects.forEach { resolveInternal(game, sourcePlayer, sourceCard, it) }
            is Conditional -> {
                if (evaluateCondition(game, sourcePlayer, effect.condition)) {
                    resolveInternal(game, sourcePlayer, sourceCard, effect.thenEffect)
                } else {
                    resolveInternal(game, sourcePlayer, sourceCard, effect.elseEffect)
                }
            }
            else -> {
                val handler = handlers[effect::class]
                    ?: throw IllegalArgumentException("No handler for ${effect::class.simpleName}")
                handler(game, sourcePlayer, sourceCard, effect)
            }
        }
    }

    private inline fun <reified T : Effect> register(
        crossinline handler: (GameState, PlayerState, InPlayCard, T) -> Unit
    ) {
        handlers[T::class] = { game, player, card, effect ->
            @Suppress("UNCHECKED_CAST")
            handler(game, player, card, effect as T)
        }
    }

    private fun gainAmber(game: GameState, player: PlayerState, amount: Int) {
        game.gainAmber(player, amount, "${player.name} gains $amount amber")
    }

    private fun gainAmberOpponent(game: GameState, amount: Int) {
        val opponent = game.opponent()
        game.gainAmber(opponent, amount, "${opponent.name} gains $amount amber")
    }

    private fun stealAmber(game: GameState, player: PlayerState, amount: Int) {
        val opponent = game.opponent()
        if (library != null && EffectRegistry.cannotBeStolenFrom(library, game, opponent)) {
            game.lastStealAmount = 0
            game.log("${opponent.name}'s amber cannot be stolen (${opponent.name}: ${opponent.amber} -> ${opponent.amber})")
            return
        }
        val playerBefore = player.amber
        val opponentBefore = opponent.amber
        val stolen = min(amount, opponent.amber)
        opponent.amber -= stolen
        player.amber += stolen
        game.lastStealAmount = stolen
        game.log(
            "${player.name} steals $stolen amber from ${opponent.name} " +
                "(${player.name}: $playerBefore -> ${player.amber}, ${opponent.name}: $opponentBefore -> ${opponent.amber})"
        )
    }

    private fun captureAmber(game: GameState, player: PlayerState, effect: CaptureAmber) {
        val opponent = game.opponent()
        val playerBefore = player.amber
        val opponentBefore = opponent.amber
        val captured = min(effect.amount, opponent.amber)
        opponent.amber -= captured
        val targets = targetChooser.chooseCreature(game, effect.target, dummySource(player))
        val target = targets.firstOrNull() ?: return
        target.capturedAmber += captured
        game.log(
            "${player.name} captures $captured amber from ${opponent.name} on ${target.cardId} " +
                "(${player.name}: $playerBefore -> ${player.amber}, ${opponent.name}: $opponentBefore -> ${opponent.amber})"
        )
    }

    private fun loseAmber(game: GameState, player: PlayerState, amount: Int) {
        val before = player.amber
        player.amber = (player.amber - amount).coerceAtLeast(0)
        game.log("${player.name} loses $amount amber (${player.name}: $before -> ${player.amber})")
    }

    private fun eventSidePlayer(game: GameState, side: EventSide, subject: EventSubject): PlayerState? {
        return when (val event = game.currentEvent) {
            is CreatureDestroyed -> when (side) {
                EventSide.OWNER -> game.player(event.ownerIndex)
                EventSide.CONTROLLER -> game.player(event.controllerIndex)
                EventSide.CONTROLLER_OPPONENT -> game.opponent(game.player(event.controllerIndex))
            }
            is FightResolved -> when (subject) {
                EventSubject.DEFENDER -> when (side) {
                    EventSide.OWNER -> game.player(event.defenderController)
                    EventSide.CONTROLLER -> game.player(event.defenderController)
                    EventSide.CONTROLLER_OPPONENT -> game.opponent(game.player(event.defenderController))
                }
                EventSubject.ATTACKER -> when (side) {
                    EventSide.OWNER -> game.player(event.attackerController)
                    EventSide.CONTROLLER -> game.player(event.attackerController)
                    EventSide.CONTROLLER_OPPONENT -> game.opponent(game.player(event.attackerController))
                }
                EventSubject.EVENT_CREATURE -> null
            }
            else -> null
        }
    }

    private fun eventSideGainsAmber(game: GameState, effect: EventSideGainsAmber) {
        val beneficiary = eventSidePlayer(game, effect.side, effect.subject) ?: return
        game.gainAmber(beneficiary, effect.amount, "${beneficiary.name} gains ${effect.amount} amber from a reaction")
    }

    private fun eventSideLosesAmber(game: GameState, effect: EventSideLosesAmber) {
        val payer = eventSidePlayer(game, effect.side, effect.subject) ?: return
        loseAmber(game, payer, effect.amount)
    }

    private fun stealFrom(game: GameState, player: PlayerState, effect: StealFrom) {
        val victim = targetPlayer(game, player, effect.from)
        val thief = game.opponent(victim)
        if (library != null && EffectRegistry.cannotBeStolenFrom(library, game, victim)) {
            game.lastStealAmount = 0
            game.log("${victim.name}'s amber cannot be stolen (${victim.name}: ${victim.amber} -> ${victim.amber})")
            return
        }
        val stolen = min(effect.amount, victim.amber)
        val before = victim.amber
        victim.amber -= stolen
        thief.amber += stolen
        game.lastStealAmount = stolen
        game.log(
            "${thief.name} steals $stolen amber from ${victim.name} " +
                "(${thief.name}: ${thief.amber - stolen} -> ${thief.amber}, ${victim.name}: $before -> ${victim.amber})"
        )
    }

    private fun readyHost(game: GameState, sourceCard: InPlayCard, effect: ReadyHost) {
        val hostId = sourceCard.hostId ?: return
        val host = findCreatureInPlay(game, hostId) ?: return
        host.exhausted = false
        if (effect.houseUntilEndOfTurn != null && host.house != effect.houseUntilEndOfTurn) {
            host.house = effect.houseUntilEndOfTurn
            game.log("${host.cardId} belongs to house ${effect.houseUntilEndOfTurn} until end of turn")
        }
        game.log("${host.cardId} readies")
    }

    private fun findCreatureInPlay(game: GameState, instanceId: String): InPlayCard? =
        game.players.asSequence()
            .flatMap { it.creatures.asSequence() }
            .find { it.instanceId == instanceId }

    private fun dealDamage(game: GameState, sourceCard: InPlayCard, effect: DealDamage) {
        val targets = targetChooser.chooseCreature(game, effect.target, sourceCard)
        targets.forEach { target ->
            // Splash lands on the neighbors before the primary target so that
            // lastDamagedTargetId / lastDamageDestroyed refer to the primary target
            // (cards evaluating DamageDestroyedTarget right after the effect).
            if (effect.splash > 0) {
                splashToNeighbors(game, target, effect.splash)
            }
            applyDamage(game, target, effect.amount)
            game.log("${sourceCard.cardId} deals ${effect.amount} damage to ${target.cardId}")
        }
    }

    private fun splashToNeighbors(game: GameState, target: InPlayCard, amount: Int) {
        val controller = game.player(target.controllerIndex)
        val idx = controller.creatures.indexOf(target)
        if (idx == -1) return
        listOfNotNull(controller.creatures.getOrNull(idx - 1), controller.creatures.getOrNull(idx + 1)).forEach { neighbor ->
            applyDamage(game, neighbor, amount)
            game.log("${amount} splash damage dealt to ${neighbor.cardId}")
        }
    }

    private fun destroy(game: GameState, sourcePlayer: PlayerState, effect: Destroy) {
        val targets = targetChooser.chooseCreature(game, effect.target, dummySource(sourcePlayer))
        targets.forEach { target ->
            target.damage = Int.MAX_VALUE
            game.lastDamagedTargetId = target.instanceId
            game.lastDamageDestroyed = true
            game.log("${sourcePlayer.name} destroys ${target.cardId}")
        }
    }

    private fun drawCards(game: GameState, player: PlayerState, amount: Int) {
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

    private fun discardRandom(game: GameState, player: PlayerState, amount: Int) {
        repeat(amount.coerceAtMost(player.hand.size)) {
            val card = player.hand[game.rng.nextInt(player.hand.size)]
            player.hand.remove(card)
            player.discard.add(card)
        }
        game.log("${player.name} discards $amount card(s) at random")
    }

    private fun returnToHand(game: GameState, sourceCard: InPlayCard, effect: ReturnToHand) {
        val targets = targetChooser.chooseCreature(game, effect.target, sourceCard)
        targets.forEach { target ->
            val owner = game.player(target.ownerIndex)
            owner.creatures.remove(target)
            owner.hand.add(target.cardId)
            game.log("${target.cardId} returns to hand")
            val event = CardLeftPlay(target.instanceId, target.cardId, target.ownerIndex, target.controllerIndex)
            if (library != null) {
                EventBus.deliver(library, game, game.player(target.controllerIndex), target, event)
                EventBus.emit(library, game, event)
            }
        }
    }

    private fun archive(game: GameState, sourcePlayer: PlayerState, effect: Archive) {
        val targets = targetChooser.chooseCreature(game, effect.target, dummySource(sourcePlayer))
        targets.forEach { target ->
            val owner = game.player(target.ownerIndex)
            owner.creatures.remove(target)
            owner.archives.add(target.cardId)
            game.log("${target.cardId} is archived")
        }
    }

    private fun purge(game: GameState, effect: Purge) {
        val targets = targetChooser.chooseCreature(game, effect.target, dummySource(game.activePlayer()))
        targets.forEach { target ->
            val owner = game.player(target.ownerIndex)
            owner.creatures.remove(target)
            owner.purged.add(target.cardId)
            game.log("${target.cardId} is purged")
        }
    }

    private fun heal(game: GameState, effect: Heal) {
        val targets = targetChooser.chooseCreature(game, effect.target, dummySource(game.activePlayer()))
        targets.forEach { target ->
            target.damage = (target.damage - effect.amount).coerceAtLeast(0)
            game.log("${target.cardId} heals ${effect.amount}")
        }
    }

    private fun ready(game: GameState, effect: Ready) {
        val targets = targetChooser.chooseCreature(game, effect.target, dummySource(game.activePlayer()))
        targets.forEach { it.exhausted = false }
    }

    private fun fight(game: GameState, effect: Fight) {
        val attackers = targetChooser.chooseCreature(game, effect.attacker, dummySource(game.activePlayer()))
        val defenders = targetChooser.chooseCreature(game, effect.defender, dummySource(game.activePlayer()))
        val attacker = attackers.firstOrNull() ?: return
        val defender = defenders.firstOrNull() ?: return
        defender.damage += attacker.power
        attacker.damage += defender.power
        game.log("${attacker.cardId} fights ${defender.cardId}")
    }

    private fun applyDamage(game: GameState, card: InPlayCard, amount: Int) {
        if (amount <= 0) return
        val actualTarget = resolveDamageTarget(game, card)
        if (actualTarget.ward) {
            actualTarget.ward = false
            return
        }
        val wasAlive = actualTarget.damage < actualTarget.power
        actualTarget.damage += (amount - actualTarget.armor).coerceAtLeast(0)
        game.lastDamagedTargetId = actualTarget.instanceId
        if (wasAlive && actualTarget.damage >= actualTarget.power) {
            game.lastDamageDestroyed = true
        }
    }

    private fun resolveDamageTarget(game: GameState, card: InPlayCard): InPlayCard {
        if (card.traits.contains("specter")) return card
        val controller = game.player(card.controllerIndex)
        val idx = controller.creatures.indexOf(card)
        if (idx == -1) return card
        val left = controller.creatures.getOrNull(idx - 1)
        val right = controller.creatures.getOrNull(idx + 1)
        val redirector = listOfNotNull(left, right).find { library?.scriptFor(it.cardId)?.redirectsNeighborDamage() == true }
        return redirector ?: card
    }

    private fun stun(game: GameState, effect: Stun) {
        val targets = targetChooser.chooseCreature(game, effect.target, dummySource(game.activePlayer()))
        targets.forEach { it.stun = true; game.log("${it.cardId} is stunned") }
    }

    private fun unstun(game: GameState, effect: Unstun) {
        val targets = targetChooser.chooseCreature(game, effect.target, dummySource(game.activePlayer()))
        targets.forEach { it.stun = false; game.log("${it.cardId} is unstunned") }
    }

    private fun destroyMostPowerful(game: GameState, sourcePlayer: PlayerState, amount: Int) {
        val all = game.players.flatMap { it.creatures }.sortedByDescending { it.power + it.damage }
        all.take(amount).forEach { it.damage = Int.MAX_VALUE; game.log("${sourcePlayer.name} destroys ${it.cardId} (most powerful)") }
    }

    private fun destroyLeastPowerful(game: GameState, sourcePlayer: PlayerState, amount: Int) {
        val all = game.players.flatMap { it.creatures }.sortedBy { it.power + it.damage }
        all.take(amount).forEach { it.damage = Int.MAX_VALUE; game.log("${sourcePlayer.name} destroys ${it.cardId} (least powerful)") }
    }

    private fun destroyWeakCreatures(game: GameState, sourcePlayer: PlayerState, powerThreshold: Int) {
        game.players.flatMap { it.creatures }
            .filter { it.power <= powerThreshold }
            .forEach {
                it.damage = Int.MAX_VALUE
                game.log("${sourcePlayer.name} destroys ${it.cardId}")
            }
    }

    private fun readyAndFight(game: GameState, sourcePlayer: PlayerState, effect: ReadyAndFight) {
        val attackers = targetChooser.chooseCreature(game, effect.target, dummySource(sourcePlayer))
        val attacker = attackers.firstOrNull() ?: return
        attacker.exhausted = false
        val defender = game.opponent().creatures.randomWith(game.rng) ?: return
        val attackerDamage = library?.scriptFor(attacker.cardId)?.modifyFightDamage(true, attacker.power) ?: attacker.power
        val defenderDamage = library?.scriptFor(defender.cardId)?.modifyFightDamage(false, defender.power) ?: defender.power
        defender.damage += attackerDamage
        attacker.damage += defenderDamage
        attacker.exhausted = true
        game.log("${attacker.cardId} readies and fights ${defender.cardId}")
        val amberPerFight = sourcePlayer.turnTriggerAmber[TurnTrigger.FRIENDLY_CREATURE_FIGHTS] ?: 0
        if (amberPerFight > 0) {
            game.gainAmber(sourcePlayer, amberPerFight, "${sourcePlayer.name} gains $amberPerFight amber from friendly creature fighting")
        }
    }

    private fun readyAndUse(game: GameState, effect: ReadyAndUse) {
        val targets = targetChooser.chooseCreature(game, effect.target, dummySource(game.activePlayer()))
        targets.forEach { it.exhausted = false; game.log("${it.cardId} readies and is used") }
    }

    private fun useCreature(game: GameState, effect: UseCreature) {
        val targets = targetChooser.chooseCreature(game, effect.target, dummySource(game.activePlayer()))
        targets.forEach { game.log("${it.cardId} is used") }
    }

    private fun returnFromDiscardToHand(game: GameState, player: PlayerState, amount: Int) {
        repeat(amount.coerceAtMost(player.discard.size)) {
            player.hand.add(player.discard.removeLast())
        }
        game.log("${player.name} returns $amount card(s) from discard to hand")
    }

    private fun playTopCard(game: GameState, player: PlayerState) {
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

    private fun swapDeckAndDiscard(game: GameState, player: PlayerState) {
        val oldDeck = player.library.toList()
        player.library.clear()
        player.library.addAll(player.discard)
        player.discard.clear()
        player.discard.addAll(oldDeck)
        player.library.shuffle(game.rng)
        game.log("${player.name} swaps deck and discard pile")
    }

    private fun purgeFromDiscard(game: GameState, player: PlayerState, amount: Int) {
        repeat(amount.coerceAtMost(player.discard.size)) {
            val cardId = player.discard.removeLast()
            player.purged.add(cardId)
        }
        game.log("${player.name} purges $amount card(s) from discard")
    }

    private fun archiveTopDeck(game: GameState, player: PlayerState) {
        val cardId = player.library.removeFirstOrNull() ?: return
        player.archives.add(cardId)
        game.log("${player.name} archives ${cardName(cardId)} from the top of their deck")
    }

    private fun archiveFromDiscard(game: GameState, player: PlayerState, amount: Int) {
        val archived = mutableListOf<String>()
        repeat(amount.coerceAtMost(player.discard.size)) {
            val cardId = player.discard.removeLast()
            archived.add(cardId)
            player.archives.add(cardId)
        }
        val names = archived.joinToString(", ") { cardName(it) }
        game.log("${player.name} archives $amount card(s) from discard: $names")
    }

    private fun archiveFromHand(game: GameState, player: PlayerState, amount: Int) {
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
    private fun archiveCreatureFromHand(game: GameState, player: PlayerState, amount: Int): Boolean {
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

    private fun loseAllAmber(game: GameState, player: PlayerState) {
        val before = player.amber
        val lost = player.amber
        player.amber = 0
        game.log("${player.name} loses all $lost amber (${player.name}: $before -> ${player.amber})")
    }

    private fun returnCreaturesOfHouseFromDiscardToHand(game: GameState, player: PlayerState) {
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

    private fun exhaust(game: GameState, effect: Exhaust) {
        val targets = targetChooser.chooseCreature(game, effect.target, dummySource(game.activePlayer()))
        targets.forEach { it.exhausted = true; game.log("${it.cardId} is exhausted") }
    }

    private fun shuffleHandAndDiscardIntoDeck(game: GameState, player: PlayerState) {
        player.library.addAll(player.hand)
        player.library.addAll(player.discard)
        player.hand.clear()
        player.discard.clear()
        player.library.shuffle(game.rng)
        game.log("${player.name} shuffles hand and discard into deck")
    }

    private fun returnAllCreaturesOfHouseToHand(game: GameState, effect: ReturnAllCreaturesOfHouseToHand) {
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

    private fun forgeKey(game: GameState, player: PlayerState, effect: ForgeKey) {
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

    private fun drawPerCardInHand(game: GameState, player: PlayerState, effect: DrawPerCardInHand) {
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

    private fun readyCreaturesOfHousePerCardInHand(game: GameState, player: PlayerState, effect: ReadyCreaturesOfHousePerCardInHand) {
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

    private fun takeControlUntilEndOfTurn(game: GameState, sourcePlayer: PlayerState, effect: TakeControlUntilEndOfTurn) {
        val targets = targetChooser.chooseCreature(game, effect.target, dummySource(sourcePlayer))
        val target = targets.firstOrNull() ?: return
        val currentOwner = game.players.find { p -> p.creatures.contains(target) } ?: return
        if (currentOwner.index == sourcePlayer.index) return
        currentOwner.creatures.remove(target)
        game.temporaryControllers[target.instanceId] = currentOwner.index
        target.controllerIndex = sourcePlayer.index
        sourcePlayer.creatures.add(target)
        game.log("${sourcePlayer.name} takes control of ${target.cardId} until end of turn")
    }

    private fun playTopCardOfHouse(game: GameState, player: PlayerState, house: House) {
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

    private fun discardTopDeck(game: GameState, player: PlayerState) {
        ensureLibraryNotEmpty(game, player)
        val cardId = player.library.removeFirstOrNull() ?: return
        player.discard.add(cardId)
        game.log("${player.name} destroys the top card of their deck: ${cardName(cardId)}")
    }

    private fun addTopCardOfHouseToHand(game: GameState, player: PlayerState, house: House) {
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

    private fun takeControlOfEnemyFlankCreature(game: GameState, sourcePlayer: PlayerState) {
        if (!sourcePlayer.forgedKeyThisTurn) {
            game.log("${sourcePlayer.name} did not forge a key this turn, so Smiling Ruth takes no creature")
            return
        }
        val opponent = game.opponent(sourcePlayer)
        if (opponent.creatures.isEmpty()) return
        val flanks = listOfNotNull(opponent.creatures.firstOrNull(), opponent.creatures.lastOrNull()).distinctBy { it.instanceId }
        val target = flanks.maxByOrNull { it.power } ?: return
        opponent.creatures.remove(target)
        game.temporaryControllers[target.instanceId] = opponent.index
        target.controllerIndex = sourcePlayer.index
        sourcePlayer.creatures.add(target)
        game.log("${sourcePlayer.name} takes control of ${target.cardId} with Smiling Ruth until end of turn")
    }

    private fun ensureLibraryNotEmpty(game: GameState, player: PlayerState) {
        if (player.library.isEmpty() && player.discard.isNotEmpty()) {
            player.library.addAll(player.discard)
            player.discard.clear()
            player.library.shuffle(game.rng)
        }
    }

    fun evaluateCondition(game: GameState, player: PlayerState, condition: Condition): Boolean {
        val opponent = game.opponent()
        val handlers = mapOf<KClass<out Condition>, (Condition, GameState, PlayerState) -> Boolean>(
            OpponentHasMoreAmber::class to { _, _, _ -> opponent.amber > player.amber },
            SelfHasMoreAmber::class to { _, _, _ -> player.amber > opponent.amber },
            OpponentHasFewerCreatures::class to { _, _, _ -> opponent.creatures.size < player.creatures.size },
            SelfHasFewerCreatures::class to { _, _, _ -> player.creatures.size < opponent.creatures.size },
            TargetIsDamaged::class to { _, _, _ -> false },
            Always::class to { _, _, _ -> true },
            OpponentAmberAtLeast::class to { c, _, _ -> opponent.amber >= (c as OpponentAmberAtLeast).amount },
            OpponentAmberAtMost::class to { c, _, _ -> opponent.amber <= (c as OpponentAmberAtMost).amount },
            OpponentAmberExactly::class to { c, _, _ -> opponent.amber == (c as OpponentAmberExactly).amount },
            SelfAmberAtLeast::class to { c, _, _ -> player.amber >= (c as SelfAmberAtLeast).amount },
            StealSucceeded::class to { _, g, _ -> g.lastStealAmount > 0 },
            DamageDestroyedTarget::class to { _, g, _ -> g.lastDamageDestroyed },
            OpponentForgedKeyLastTurn::class to { _, g, _ -> g.opponent().forgedKeyLastTurn },
            CardsOfHousePlayedThisTurn::class to { c, _, p ->
                val cond = c as CardsOfHousePlayedThisTurn
                (p.cardsPlayedThisTurnByHouse[cond.house] ?: 0) >= cond.amount
            },
            CreatureInHand::class to { _, _, p ->
                p.hand.any { library?.cardData(it)?.type == CardType.CREATURE }
            }
        )
        return handlers[condition::class]?.invoke(condition, game, player) ?: false
    }

    private fun targetPlayer(game: GameState, sourcePlayer: PlayerState, player: TargetPlayer): PlayerState {
        return when (player) {
            is SelfPlayer -> sourcePlayer
            is OpponentPlayer -> game.opponent()
        }
    }

    private fun dummySource(player: PlayerState): InPlayCard {
        return InPlayCard(
            instanceId = "dummy",
            cardId = "dummy",
            ownerIndex = player.index,
            controllerIndex = player.index,
            zone = Zone.PLAY_AREA,
            house = House.BROBNAR,
            type = CardType.CREATURE
        )
    }

    private fun cardName(cardId: String): String {
        return library?.cardData(cardId)?.name ?: cardId
    }
}
