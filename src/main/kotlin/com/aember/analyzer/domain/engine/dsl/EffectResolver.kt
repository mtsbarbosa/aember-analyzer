package com.aember.analyzer.domain.engine.dsl

import com.aember.analyzer.domain.engine.CardLibrary
import com.aember.analyzer.domain.engine.model.GameState
import com.aember.analyzer.domain.engine.model.InPlayCard
import com.aember.analyzer.domain.engine.model.PlayerState
import com.aember.analyzer.domain.engine.model.Zone
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
        register<LoseAllAmber> { game, player, _, effect -> loseAllAmber(game, targetPlayer(game, player, effect.player)) }
        register<GainAmberOpponent> { game, _, _, effect -> gainAmberOpponent(game, effect.amount) }
        register<Exhaust> { game, _, _, effect -> exhaust(game, effect) }
        register<ShuffleHandAndDiscardIntoDeck> { game, player, _, effect -> shuffleHandAndDiscardIntoDeck(game, targetPlayer(game, player, effect.player)) }
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

    private fun dealDamage(game: GameState, sourceCard: InPlayCard, effect: DealDamage) {
        val targets = targetChooser.chooseCreature(game, effect.target, sourceCard)
        targets.forEach { target ->
            applyDamage(game, target, effect.amount)
            game.log("${sourceCard.cardId} deals ${effect.amount} damage to ${target.cardId}")
        }
    }

    private fun destroy(game: GameState, sourcePlayer: PlayerState, effect: Destroy) {
        val targets = targetChooser.chooseCreature(game, effect.target, dummySource(sourcePlayer))
        targets.forEach { target ->
            target.damage = Int.MAX_VALUE
            game.log("${sourcePlayer.name} destroys ${target.cardId}")
        }
    }

    private fun drawCards(game: GameState, player: PlayerState, amount: Int) {
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
        game.log("${player.name} draws $amount card(s)")
    }

    private fun discardRandom(game: GameState, player: PlayerState, amount: Int) {
        repeat(amount.coerceAtMost(player.hand.size)) {
            val card = player.hand.random()
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
        val defender = game.opponent().creatures.randomOrNull() ?: return
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
                house = player.activeHouse ?: com.aember.analyzer.domain.model.House.BROBNAR,
                type = com.aember.analyzer.domain.model.CardType.CREATURE,
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
        player.library.shuffle(java.util.Random())
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
            val cardId = player.hand.random()
            player.hand.remove(cardId)
            archived.add(cardId)
            player.archives.add(cardId)
        }
        val names = archived.joinToString(", ") { cardName(it) }
        game.log("${player.name} archives $amount card(s) from hand: $names")
    }

    private fun loseAllAmber(game: GameState, player: PlayerState) {
        val before = player.amber
        val lost = player.amber
        player.amber = 0
        game.log("${player.name} loses all $lost amber (${player.name}: $before -> ${player.amber})")
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
        player.library.shuffle(java.util.Random())
        game.log("${player.name} shuffles hand and discard into deck")
    }

    fun evaluateCondition(game: GameState, player: PlayerState, condition: Condition): Boolean {
        val opponent = game.opponent()
        return when (condition) {
            is OpponentHasMoreAmber -> opponent.amber > player.amber
            is SelfHasMoreAmber -> player.amber > opponent.amber
            is OpponentHasFewerCreatures -> opponent.creatures.size < player.creatures.size
            is SelfHasFewerCreatures -> player.creatures.size < opponent.creatures.size
            is TargetIsDamaged -> false // not used in simple conditions
            is Always -> true
            is OpponentAmberAtLeast -> opponent.amber >= condition.amount
            is OpponentAmberAtMost -> opponent.amber <= condition.amount
            is OpponentAmberExactly -> opponent.amber == condition.amount
            is SelfAmberAtLeast -> player.amber >= condition.amount
            is StealSucceeded -> game.lastStealAmount > 0
            is DamageDestroyedTarget -> game.lastDamageDestroyed
        }
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
            house = com.aember.analyzer.domain.model.House.BROBNAR,
            type = com.aember.analyzer.domain.model.CardType.CREATURE
        )
    }

    private fun cardName(cardId: String): String {
        return library?.cardData(cardId)?.name ?: cardId
    }
}
