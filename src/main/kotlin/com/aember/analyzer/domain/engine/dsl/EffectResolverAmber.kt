package com.aember.analyzer.domain.engine.dsl

import com.aember.analyzer.domain.engine.effects.EffectRegistry
import com.aember.analyzer.domain.engine.events.CreatureDestroyed
import com.aember.analyzer.domain.engine.events.FightResolved
import com.aember.analyzer.domain.engine.model.GameState
import com.aember.analyzer.domain.engine.model.InPlayCard
import com.aember.analyzer.domain.engine.model.PlayerState
import kotlin.math.min

/** Amber economy: gain/lose/steal/capture, event-side amber, amber clamps. */
internal fun EffectResolver.registerAmberEffects() {
    register<GainAmber> { game, player, _, effect -> gainAmber(game, player, effect.amount) }
    register<GainAmberOpponent> { game, _, _, effect -> gainAmberOpponent(game, effect.amount) }
    register<LoseAmber> { game, player, _, effect -> loseAmber(
        game,
        targetPlayer(game, player, effect.player),
        effect.amount
    ) }
    register<LoseAmberOpponent> { game, player, _, effect ->
        loseAmber(game, game.opponent(player), effect.amount)
    }
    register<LoseAllAmber> { game, player, _, effect -> loseAllAmber(game, targetPlayer(game, player, effect.player)) }
    register<StealAmber> { game, player, _, effect -> stealAmber(game, player, effect.amount) }
    register<StealFrom> { game, player, _, effect -> stealFrom(game, player, effect) }
    register<CaptureAmber> { game, player, sourceCard, effect -> captureAmber(game, player, sourceCard, effect) }
    register<CaptureAmberDynamic> { game, player, sourceCard, effect ->
        captureAmber(game, player, sourceCard, effect.amount(game, player), effect.target, effect.from)
    }
    register<CaptureEach> { game, player, sourceCard, effect -> captureEach(game, player, sourceCard, effect) }
    register<EventSubjectCaptures> { game, _, _, effect -> eventSubjectCaptures(game, effect) }
    register<EventSideGainsAmber> { game, _, _, effect -> eventSideGainsAmber(game, effect) }
    register<EventSideLosesAmber> { game, _, _, effect -> eventSideLosesAmber(game, effect) }
    register<ReduceAmberTo> { game, _, _, effect -> reduceAmberTo(game, effect) }
    register<GainAmberEqualToOpponentKeys> { game, sourcePlayer, _, effect ->
        val target = targetPlayer(game, sourcePlayer, effect.player)
        val amount = game.opponent(target).keys
        if (amount > 0) gainAmber(game, target, amount)
    }
    register<GainAmberOnTriggerThisTurn> { _, player, _, effect ->
        player.turnTriggerAmber[effect.trigger] = player.turnTriggerAmber.getOrDefault(effect.trigger, 0) + effect.amount
    }
    register<LoseHalfAmber> { game, _, _, _ ->
        game.players.forEach { player ->
            val lost = player.amber / 2
            if (lost > 0) loseAmber(game, player, lost)
        }
    }
    register<PlaceAmberOnEnemyCreature> { game, player, _, effect ->
        val opponent = game.opponent(player)
        val target = opponent.creatures.maxByOrNull { it.power } ?: return@register
        target.capturedAmber += effect.amount
        game.log("${player.name} places ${effect.amount} amber on ${target.cardId}")
    }
    register<MoveAmberFromOwnCardToPool> { game, player, _, _ ->
        val source = (player.creatures + player.artifacts).filter { it.capturedAmber > 0 }.maxByOrNull { it.capturedAmber }
        if (source == null) {
            game.log("${player.name} has no amber on their cards to move")
            return@register
        }
        source.capturedAmber -= 1
        gainAmber(game, player, 1)
    }
    register<LoseAllAmberAndOpponentLosesTriple> { game, player, _, _ ->
        val lost = player.amber
        if (lost > 0) loseAmber(game, player, lost)
        loseAmber(game, game.opponent(player), lost * 3)
    }
    register<ReapStealsInstead> { _, player, _, _ ->
        player.reapAmberStolenFromOpponent = true
    }
}

internal fun gainAmber(game: GameState, player: PlayerState, amount: Int) {
    game.gainAmber(player, amount, "${player.name} gains $amount amber")
}

internal fun gainAmberOpponent(game: GameState, amount: Int) {
    val opponent = game.opponent()
    game.gainAmber(opponent, amount, "${opponent.name} gains $amount amber")
}

internal fun EffectResolver.stealAmber(game: GameState, player: PlayerState, amount: Int) {
    val opponent = game.opponent()
    if (library != null && EffectRegistry.cannotBeStolenFrom(library, opponent)) {
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

internal fun EffectResolver.captureAmber(
    game: GameState,
    player: PlayerState,
    sourceCard: InPlayCard,
    effect: CaptureAmber
) {
    captureAmber(game, player, sourceCard, effect.amount, effect.target, effect.from)
}

internal fun EffectResolver.captureAmber(
    game: GameState,
    player: PlayerState,
    sourceCard: InPlayCard,
    amount: Int,
    targetSelector: CreatureSelector,
    from: TargetPlayer
) {
    val poolOwner = targetPlayer(game, player, from)
    val poolBefore = poolOwner.amber
    val captured = min(amount, poolOwner.amber)
    poolOwner.amber -= captured
    val targets = targetChooser.chooseCreature(game, targetSelector, sourceCard)
    val target = targets.firstOrNull() ?: return
    target.capturedAmber += captured
    game.log(
        "${player.name} captures $captured amber from ${poolOwner.name} on ${target.cardId} " +
            "(${poolOwner.name}: $poolBefore -> ${poolOwner.amber})"
    )
}

internal fun EffectResolver.captureEach(game: GameState, player: PlayerState, sourceCard: InPlayCard, effect: CaptureEach) {
    val poolOwner = targetPlayer(game, player, effect.from)
    val poolBefore = poolOwner.amber
    var targets = targetChooser.chooseCreature(game, effect.target, sourceCard)
    effect.maxTargets?.let { max -> targets = targets.take(max(game, player)) }
    var total = 0
    for (target in targets) {
        val captured = min(effect.amountPerEach, poolOwner.amber)
        if (captured <= 0) break
        poolOwner.amber -= captured
        target.capturedAmber += captured
        total += captured
        game.log("${target.cardId} captures $captured amber from ${poolOwner.name}")
    }
    if (total == 0) {
        game.log("${poolOwner.name} has no amber to capture (${poolOwner.name}: $poolBefore -> ${poolOwner.amber})")
    }
}

internal fun EffectResolver.eventSubjectCaptures(game: GameState, effect: EventSubjectCaptures) {
    val event = game.currentEvent
    val creature = when (event) {
        is FightResolved -> when (effect.subject) {
            EventSubject.ATTACKER -> findCreatureInPlay(game, event.attackerInstanceId)
            EventSubject.DEFENDER -> findCreatureInPlay(game, event.defenderInstanceId)
            EventSubject.EVENT_CREATURE -> null
        }
        else -> null
    } ?: return
    val controller = game.player(creature.controllerIndex)
    val poolOwner = game.opponent(controller)
    val captured = min(effect.amount, poolOwner.amber)
    if (captured <= 0) return
    poolOwner.amber -= captured
    creature.capturedAmber += captured
    game.log("${creature.cardId} captures $captured amber from ${poolOwner.name} (fight reaction)")
}

internal fun loseAmber(game: GameState, player: PlayerState, amount: Int) {
    val before = player.amber
    player.amber = (player.amber - amount).coerceAtLeast(0)
    game.log("${player.name} loses $amount amber (${player.name}: $before -> ${player.amber})")
}

internal fun loseAllAmber(game: GameState, player: PlayerState) {
    val before = player.amber
    val lost = player.amber
    player.amber = 0
    game.log("${player.name} loses all $lost amber (${player.name}: $before -> ${player.amber})")
}

internal fun eventSidePlayer(game: GameState, side: EventSide, subject: EventSubject): PlayerState? {
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

internal fun eventSideGainsAmber(game: GameState, effect: EventSideGainsAmber) {
    val beneficiary = eventSidePlayer(game, effect.side, effect.subject) ?: return
    game.gainAmber(beneficiary, effect.amount, "${beneficiary.name} gains ${effect.amount} amber from a reaction")
}

internal fun eventSideLosesAmber(game: GameState, effect: EventSideLosesAmber) {
    val payer = eventSidePlayer(game, effect.side, effect.subject) ?: return
    loseAmber(game, payer, effect.amount)
}

internal fun EffectResolver.stealFrom(game: GameState, player: PlayerState, effect: StealFrom) {
    val victim = targetPlayer(game, player, effect.from)
    val thief = game.opponent(victim)
    if (library != null && EffectRegistry.cannotBeStolenFrom(library, victim)) {
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

internal fun reduceAmberTo(game: GameState, effect: ReduceAmberTo) {
    game.players.forEach { player ->
        if (player.amber >= effect.threshold) {
            val before = player.amber
            player.amber = effect.cap
            game.log("${player.name} is reduced to ${effect.cap} amber (${player.name}: $before -> ${player.amber})")
        }
    }
}
