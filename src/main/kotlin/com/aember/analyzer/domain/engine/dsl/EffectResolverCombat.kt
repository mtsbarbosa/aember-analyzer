package com.aember.analyzer.domain.engine.dsl

import com.aember.analyzer.domain.engine.model.GameState
import com.aember.analyzer.domain.engine.model.InPlayCard
import com.aember.analyzer.domain.engine.model.PlayerState
import com.aember.analyzer.domain.engine.model.randomWith
import com.aember.analyzer.domain.model.House
import kotlin.math.min

/** Combat: damage, destruction, fights, heal/stun/ready/exhaust, power counters. */
internal fun EffectResolver.registerCombatEffects() {
    register<DealDamage> { game, _, sourceCard, effect -> dealDamage(game, sourceCard, effect) }
    register<Destroy> { game, player, sourceCard, effect -> destroy(game, player, sourceCard, effect) }
    register<DestroyMostPowerful> { game, player, _, effect -> destroyMostPowerful(game, player, effect.amount) }
    register<DestroyLeastPowerful> { game, player, _, effect -> destroyLeastPowerful(game, player, effect.amount) }
    register<DestroyWeakCreatures> { game, player, _, effect -> destroyWeakCreatures(
        game,
        player,
        effect.powerThreshold
    ) }
    register<Fight> { game, _, _, effect -> fight(game, effect) }
    register<ReadyAndFight> { game, player, sourceCard, effect -> readyAndFight(game, player, sourceCard, effect) }
    register<ReadyAndUse> { game, player, sourceCard, effect -> readyAndUse(game, player, sourceCard, effect) }
    register<UseCreature> { game, player, sourceCard, effect -> useCreature(game, player, sourceCard, effect) }
    register<Stun> { game, player, sourceCard, effect -> stun(game, player, sourceCard, effect) }
    register<Unstun> { game, player, sourceCard, effect -> unstun(game, player, sourceCard, effect) }
    register<Heal> { game, _, sourceCard, effect -> heal(game, sourceCard, effect) }
    register<HealEach> { game, player, _, effect -> healEach(game, player, effect) }
    register<Ready> { game, player, sourceCard, effect -> ready(game, player, sourceCard, effect) }
    register<Exhaust> { game, player, sourceCard, effect -> exhaust(game, player, sourceCard, effect) }
    register<AddPowerCounters> { game, _, sourceCard, effect ->
        addPowerCounters(game, sourceCard, effect.target, effect.amount)
    }
    register<AddPowerCountersDynamic> { game, player, sourceCard, effect ->
        addPowerCounters(game, sourceCard, effect.target, effect.amount(game, player))
    }
    register<GrantDamageImmunity> { game, _, sourceCard, effect ->
        val targets = targetChooser.chooseCreature(game, effect.target, sourceCard)
        targets.forEach { it.immuneToDamageThisTurn = true }
        game.log("${targets.size} creature(s) cannot be dealt damage for the remainder of the turn")
    }
    register<SacrificeSelf> { game, _, sourceCard, _ ->
        sacrificeCreature(game, sourceCard)
    }
    register<Sacrifice> { game, _, sourceCard, effect ->
        val targets = targetChooser.chooseCreature(game, effect.target.allScope(), sourceCard)
        // Sacrificing is a cost: when the selector is friendly-only, give up the cheapest creature.
        val target = (if (effect.target is FriendlyCreature) targets.cheapestFirst() else targets).firstOrNull()
            ?: return@register
        game.lastSacrificeSucceeded = true
        sacrificeCreature(game, target)
    }
    register<DealDamageIgnoringArmor> { game, _, sourceCard, effect ->
        targetChooser.chooseCreature(game, effect.target, sourceCard).forEach { target ->
            applyDamage(game, target, effect.amount, ignoreArmor = true)
            game.log("${sourceCard.cardId} deals ${effect.amount} damage to ${target.cardId} (armor ignored)")
        }
    }
    register<DealDamagePerHouseInPlay> { game, _, sourceCard, effect ->
        val amount = game.players.flatMap { it.creatures }.count { it.house == effect.house } * effect.amountPer
        if (amount <= 0) return@register
        targetChooser.chooseCreature(game, effect.target, sourceCard).forEach { target ->
            applyDamage(game, target, amount)
            game.log("${sourceCard.cardId} deals $amount damage to ${target.cardId}")
        }
    }
    register<DealDamageFlankCascade> { game, _, sourceCard, _ ->
        val flank = targetChooser.chooseCreature(game, FlankCreature, sourceCard).firstOrNull() ?: return@register
        val battleline = game.player(flank.controllerIndex).creatures
        val idx = battleline.indexOf(flank)
        applyDamage(game, flank, 3)
        game.log("${sourceCard.cardId} deals 3 damage to ${flank.cardId}")
        val neighbor = when {
            idx > 0 && idx < battleline.size - 1 -> null // not a flank (safety)
            idx == 0 -> battleline.getOrNull(1)
            else -> battleline.getOrNull(idx - 1)
        }
        if (neighbor != null) {
            applyDamage(game, neighbor, 2)
            game.log("${sourceCard.cardId} deals 2 damage to ${neighbor.cardId}")
            val nextIdx = if (idx == 0) 2 else idx - 2
            battleline.getOrNull(nextIdx)?.let { secondNeighbor ->
                applyDamage(game, secondNeighbor, 1)
                game.log("${sourceCard.cardId} deals 1 damage to ${secondNeighbor.cardId}")
            }
        }
    }
    register<HealThenDamageOther> { game, _, sourceCard, effect ->
        val healTarget = targetChooser.chooseCreature(game, FriendlyCreature(), sourceCard)
            .maxByOrNull { it.damage } ?: return@register
        val healed = min(effect.upTo, healTarget.damage)
        if (healed <= 0) return@register
        healTarget.damage -= healed
        game.lastHealAmount = healed
        game.lastHealedTargetId = healTarget.instanceId
        game.log("${sourceCard.cardId} heals $healed damage from ${healTarget.cardId}")
        val damageTarget = targetChooser.chooseCreature(game, CreatureOtherThanLastHealed, sourceCard).firstOrNull()
            ?: return@register
        applyDamage(game, damageTarget, healed)
        game.log("${sourceCard.cardId} deals $healed damage to ${damageTarget.cardId}")
    }
    register<DealDamageToFightsNeighbors> { game, player, sourceCard, effect ->
        val fightTarget = fightTargetOf(game) ?: return@register
        val controller = game.player(fightTarget.controllerIndex)
        val idx = controller.creatures.indexOf(fightTarget)
        if (idx == -1) return@register
        listOfNotNull(controller.creatures.getOrNull(idx - 1), controller.creatures.getOrNull(idx + 1))
            .forEach { neighbor ->
                applyDamage(game, neighbor, effect.amount)
                game.log("${sourceCard.cardId} deals ${effect.amount} damage to ${neighbor.cardId} (${fightTarget.cardId}'s neighbor)")
            }
    }
    register<StunFightTargetAndNeighbors> { game, _, _, _ ->
        val fightTarget = fightTargetOf(game) ?: return@register
        val controller = game.player(fightTarget.controllerIndex)
        val idx = controller.creatures.indexOf(fightTarget)
        val targets = if (idx == -1) {
            listOf(fightTarget)
        } else {
            listOfNotNull(controller.creatures.getOrNull(idx - 1), fightTarget, controller.creatures.getOrNull(idx + 1))
        }
        targets.forEach { it.stun = true }
        game.log("${targets.joinToString(", ") { it.cardId }} is stunned")
    }
    register<DealDividedDamage> { game, _, sourceCard, effect ->
        val friendlyCount = game.player(sourceCard.controllerIndex).creatures.size
        var amount = friendlyCount * effect.amountPerFriendlyCreature
        if (amount <= 0) return@register
        game.log("${sourceCard.cardId} divides $amount damage among creatures")
        for (target in targetChooser.chooseCreature(game, effect.target.allScope(), sourceCard)) {
            if (amount <= 0) break
            if (target.damage >= target.power) continue
            applyDamage(game, target, 1)
            amount--
        }
        // Leftover damage piles onto the first living target.
        if (amount > 0) {
            targetChooser.chooseCreature(game, effect.target.allScope(), sourceCard).firstOrNull()?.let {
                applyDamage(game, it, amount)
            }
        }
    }
    register<StripEnemyArmorAndDamagePerLost> { game, player, sourceCard, _ ->
        game.opponent(player).creatures.forEach { enemy ->
            val armor = (enemy.armor - enemy.armorStrippedThisTurn).coerceAtLeast(0)
            if (armor <= 0) return@forEach
            enemy.armorStrippedThisTurn += armor
            applyDamage(game, enemy, armor)
            game.log("${enemy.cardId} loses $armor armor and takes $armor damage")
        }
    }
    register<ReadyMarsOrStunNonMars> { game, player, sourceCard, _ ->
        // Greedy: ready an own exhausted Mars creature, else stun an enemy non-Mars
        // creature, else stun an enemy Mars creature.
        val ownMars = player.creatures.filter { it.house == House.MARS && (it.exhausted || it.stun) }.maxByOrNull { it.power }
        if (ownMars != null && !ownMars.stun) {
            ownMars.exhausted = false
            game.log("${ownMars.cardId} readies")
            return@register
        }
        val enemy = game.opponent(player).creatures
        val stunTarget = enemy.firstOrNull { it.house != House.MARS } ?: enemy.firstOrNull()
        if (stunTarget != null) {
            stunTarget.stun = true
            game.log("${stunTarget.cardId} is stunned")
        } else if (ownMars != null) {
            ownMars.exhausted = false
            game.log("${ownMars.cardId} readies")
        } else {
            game.log("${sourceCard.cardId} has no valid target")
        }
    }
}

internal fun EffectResolver.dealDamage(game: GameState, sourceCard: InPlayCard, effect: DealDamage) {
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

internal fun EffectResolver.splashToNeighbors(game: GameState, target: InPlayCard, amount: Int) {
    val controller = game.player(target.controllerIndex)
    val idx = controller.creatures.indexOf(target)
    if (idx == -1) return
    listOfNotNull(controller.creatures.getOrNull(idx - 1), controller.creatures.getOrNull(idx + 1)).forEach { neighbor ->
        applyDamage(game, neighbor, amount)
        game.log("$amount splash damage dealt to ${neighbor.cardId}")
    }
}

internal fun EffectResolver.applyDamage(game: GameState, card: InPlayCard, amount: Int, ignoreArmor: Boolean = false) {
    if (amount <= 0) return
    val actualTarget = resolveDamageTarget(game, card)
    if (actualTarget.immuneToDamageThisTurn) {
        game.log("${actualTarget.cardId} cannot be dealt damage this turn")
        return
    }
    if (actualTarget.ward) {
        actualTarget.ward = false
        return
    }
    val armor = if (ignoreArmor) 0 else (actualTarget.armor - actualTarget.armorStrippedThisTurn).coerceAtLeast(0)
    val wasAlive = actualTarget.damage < actualTarget.power
    actualTarget.damage += (amount - armor).coerceAtLeast(0)
    game.lastDamagedTargetId = actualTarget.instanceId
    if (wasAlive && actualTarget.damage >= actualTarget.power) {
        game.lastDamageDestroyed = true
    }
}

internal fun EffectResolver.fightTargetOf(game: GameState): InPlayCard? {
    val id = game.currentFightTargetId ?: return null
    return game.players.asSequence().flatMap { it.creatures.asSequence() }.find { it.instanceId == id }
}

internal fun EffectResolver.resolveDamageTarget(game: GameState, card: InPlayCard): InPlayCard {
    if (card.traits.contains("specter")) return card
    val controller = game.player(card.controllerIndex)
    val idx = controller.creatures.indexOf(card)
    if (idx == -1) return card
    val left = controller.creatures.getOrNull(idx - 1)
    val right = controller.creatures.getOrNull(idx + 1)
    val redirector = listOfNotNull(left, right).find { library?.scriptFor(it.cardId)?.redirectsNeighborDamage() == true }
    return redirector ?: card
}

internal fun EffectResolver.destroy(game: GameState, sourcePlayer: PlayerState, sourceCard: InPlayCard, effect: Destroy) {
    val targets = targetChooser.chooseCreature(game, effect.target, sourceCard)
    targets.forEach { target ->
        target.damage = Int.MAX_VALUE
        game.lastDamagedTargetId = target.instanceId
        game.lastDamageDestroyed = true
        game.log("${sourcePlayer.name} destroys ${target.cardId}")
    }
}

internal fun destroyMostPowerful(game: GameState, sourcePlayer: PlayerState, amount: Int) {
    val all = game.players.flatMap { it.creatures }.sortedByDescending { it.power + it.damage }
    all.take(amount).forEach { it.damage = Int.MAX_VALUE; game.log("${sourcePlayer.name} destroys ${it.cardId} (most powerful)") }
}

internal fun destroyLeastPowerful(game: GameState, sourcePlayer: PlayerState, amount: Int) {
    val all = game.players.flatMap { it.creatures }.sortedBy { it.power + it.damage }
    all.take(amount).forEach { it.damage = Int.MAX_VALUE; game.log("${sourcePlayer.name} destroys ${it.cardId} (least powerful)") }
}

internal fun destroyWeakCreatures(game: GameState, sourcePlayer: PlayerState, powerThreshold: Int) {
    game.players.flatMap { it.creatures }
        .filter { it.power <= powerThreshold }
        .forEach {
            it.damage = Int.MAX_VALUE
            game.log("${sourcePlayer.name} destroys ${it.cardId}")
        }
}

internal fun EffectResolver.fight(game: GameState, effect: Fight) {
    val attackers = targetChooser.chooseCreature(game, effect.attacker, dummySource(game.activePlayer()))
    val defenders = targetChooser.chooseCreature(game, effect.defender, dummySource(game.activePlayer()))
    val attacker = attackers.firstOrNull() ?: return
    val defender = defenders.firstOrNull() ?: return
    defender.damage += attacker.power
    attacker.damage += defender.power
    game.log("${attacker.cardId} fights ${defender.cardId}")
}

internal fun EffectResolver.readyAndFight(game: GameState, sourcePlayer: PlayerState, sourceCard: InPlayCard, effect: ReadyAndFight) {
    val attackers = targetChooser.chooseCreature(game, effect.target, sourceCard)
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

internal fun EffectResolver.readyAndUse(game: GameState, sourcePlayer: PlayerState, sourceCard: InPlayCard, effect: ReadyAndUse) {
    val targets = targetChooser.chooseCreature(game, effect.target, sourceCard)
    targets.forEach { it.exhausted = false; game.log("${it.cardId} readies and is used") }
}

internal fun EffectResolver.useCreature(game: GameState, sourcePlayer: PlayerState, sourceCard: InPlayCard, effect: UseCreature) {
    val targets = targetChooser.chooseCreature(game, effect.target, sourceCard)
    targets.forEach { game.log("${it.cardId} is used") }
}

internal fun EffectResolver.stun(game: GameState, sourcePlayer: PlayerState, sourceCard: InPlayCard, effect: Stun) {
    val targets = targetChooser.chooseCreature(game, effect.target, sourceCard)
    targets.forEach { it.stun = true; game.log("${it.cardId} is stunned") }
}

internal fun EffectResolver.unstun(game: GameState, sourcePlayer: PlayerState, sourceCard: InPlayCard, effect: Unstun) {
    val targets = targetChooser.chooseCreature(game, effect.target, sourceCard)
    targets.forEach { it.stun = false; game.log("${it.cardId} is unstunned") }
}

internal fun EffectResolver.heal(game: GameState, sourceCard: InPlayCard, effect: Heal) {
    val targets = targetChooser.chooseCreature(game, effect.target, sourceCard)
    targets.forEach { target ->
        val healed = if (effect.fully) target.damage else min(effect.amount, target.damage)
        target.damage = if (effect.fully) 0 else (target.damage - effect.amount).coerceAtLeast(0)
        game.lastHealAmount += healed
        game.lastHealedTargetId = target.instanceId
        game.log(
            if (effect.fully) "${sourceCard.cardId} fully heals ${target.cardId} ($healed damage)"
            else "${target.cardId} heals ${effect.amount}"
        )
    }
}

internal fun healEach(game: GameState, player: PlayerState, effect: HealEach) {
    var healedCount = 0
    game.players.flatMap { it.creatures }.forEach { target ->
        if (target.damage > 0) {
            val healed = min(effect.amount, target.damage)
            target.damage -= healed
            healedCount++
            game.log("${target.cardId} heals $healed")
        }
    }
    if (effect.gainPerHealed > 0 && healedCount > 0) {
        game.gainAmber(player, healedCount * effect.gainPerHealed, "${player.name} gains amber for $healedCount healed creature(s)")
    }
}

internal fun EffectResolver.ready(game: GameState, sourcePlayer: PlayerState, sourceCard: InPlayCard, effect: Ready) {
    val targets = targetChooser.chooseCreature(game, effect.target, sourceCard)
    targets.forEach { it.exhausted = false }
}

internal fun EffectResolver.exhaust(game: GameState, sourcePlayer: PlayerState, sourceCard: InPlayCard, effect: Exhaust) {
    val targets = targetChooser.chooseCreature(game, effect.target, sourceCard)
    val selected = effect.count?.let { n ->
        // Exhausting is only worth anything on ready creatures; enemies first, our own cheapest last.
        val (mine, theirs) = targets.filter { !it.exhausted }.partition { it.controllerIndex == sourceCard.controllerIndex }
        (theirs + mine.cheapestFirst()).take(n)
    } ?: targets
    selected.forEach { it.exhausted = true; game.log("${it.cardId} is exhausted") }
}

internal fun EffectResolver.addPowerCounters(game: GameState, sourceCard: InPlayCard, targetSelector: CreatureSelector, amount: Int) {
    if (amount <= 0) return
    // Power counters permanently raise the creature's base power; the
    // EffectRegistry then re-applies static auras on top of it.
    val targets = targetChooser.chooseCreature(game, targetSelector, sourceCard)
    targets.forEach { target ->
        target.power += amount
        target.basePower += amount
        game.log("${target.cardId} gets $amount +1 power counter(s) (power: ${target.power})")
    }
}
