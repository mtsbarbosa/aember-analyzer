package com.aember.analyzer.domain.engine.dsl

import com.aember.analyzer.domain.engine.CardLibrary
import com.aember.analyzer.domain.engine.model.GameState
import com.aember.analyzer.domain.engine.model.InPlayCard
import com.aember.analyzer.domain.engine.model.PlayerState
import com.aember.analyzer.domain.engine.model.Zone
import kotlin.math.min

/**
 * Executes declarative card effects against the game state.
 */
class EffectResolver(
    private val targetChooser: TargetChooser = GreedyTargetChooser(),
    private val library: CardLibrary? = null
) {

    fun resolve(
        game: GameState,
        sourcePlayer: PlayerState,
        sourceCard: InPlayCard,
        effect: Effect
    ) {
        when (effect) {
            is NoEffect -> {}
            is GainAmber -> gainAmber(game, sourcePlayer, effect.amount)
            is StealAmber -> stealAmber(game, sourcePlayer, effect.amount)
            is CaptureAmber -> captureAmber(game, sourcePlayer, effect)
            is LoseAmber -> loseAmber(game, targetPlayer(game, sourcePlayer, effect.player), effect.amount)
            is DealDamage -> dealDamage(game, sourceCard, effect)
            is Destroy -> destroy(game, sourcePlayer, effect)
            is Draw -> drawCards(game, targetPlayer(game, sourcePlayer, effect.player), effect.amount)
            is DiscardRandom -> discardRandom(game, targetPlayer(game, sourcePlayer, effect.player), effect.amount)
            is ReturnToHand -> returnToHand(game, sourceCard, effect)
            is Archive -> archive(game, sourcePlayer, effect)
            is Purge -> purge(game, effect)
            is Heal -> heal(game, effect)
            is Ready -> ready(game, effect)
            is Fight -> fight(game, effect)
            is GainChains -> targetPlayer(game, sourcePlayer, effect.player).chains += effect.amount
            is LoseChains -> targetPlayer(game, sourcePlayer, effect.player).chains =
                (targetPlayer(game, sourcePlayer, effect.player).chains - effect.amount).coerceAtLeast(0)
            is Stun -> stun(game, effect)
            is Unstun -> unstun(game, effect)
            is DestroyMostPowerful -> destroyMostPowerful(game, sourcePlayer, effect.amount)
            is DestroyLeastPowerful -> destroyLeastPowerful(game, sourcePlayer, effect.amount)
            is DestroyWeakCreatures -> destroyWeakCreatures(game, sourcePlayer, effect.powerThreshold)
            is ReadyAndFight -> readyAndFight(game, sourcePlayer, effect)
            is ReadyAndUse -> readyAndUse(game, effect)
            is UseCreature -> useCreature(game, effect)
            is ReturnFromDiscardToHand -> returnFromDiscardToHand(game, targetPlayer(game, sourcePlayer, effect.player), effect.amount)
            is PlayTopCard -> playTopCard(game, targetPlayer(game, sourcePlayer, effect.player))
            is SwapDeckAndDiscard -> swapDeckAndDiscard(game, targetPlayer(game, sourcePlayer, effect.player))
            is PurgeFromDiscard -> purgeFromDiscard(game, targetPlayer(game, sourcePlayer, effect.player), effect.amount)
            is ArchiveTopDeck -> archiveTopDeck(game, targetPlayer(game, sourcePlayer, effect.player))
            is ArchiveFromDiscard -> archiveFromDiscard(game, targetPlayer(game, sourcePlayer, effect.player), effect.amount)
            is ArchiveFromHand -> archiveFromHand(game, targetPlayer(game, sourcePlayer, effect.player), effect.amount)
            is LoseAllAmber -> loseAllAmber(game, targetPlayer(game, sourcePlayer, effect.player))
            is GainAmberOpponent -> gainAmberOpponent(game, effect.amount)
            is Exhaust -> exhaust(game, effect)
            is ShuffleHandAndDiscardIntoDeck -> shuffleHandAndDiscardIntoDeck(game, targetPlayer(game, sourcePlayer, effect.player))
            is Sequence -> effect.effects.forEach { resolve(game, sourcePlayer, sourceCard, it) }
            is Conditional -> {
                if (evaluateCondition(game, sourcePlayer, effect.condition)) {
                    resolve(game, sourcePlayer, sourceCard, effect.thenEffect)
                } else {
                    resolve(game, sourcePlayer, sourceCard, effect.elseEffect)
                }
            }
            is RepeatForEach -> {
                // Not used for simple cards yet.
            }
        }
    }

    private fun gainAmber(game: GameState, player: PlayerState, amount: Int) {
        val before = player.amber
        player.amber += amount
        game.log("${player.name} gains $amount amber (${player.name}: $before -> ${player.amber})")
    }

    private fun gainAmberOpponent(game: GameState, amount: Int) {
        val opponent = game.opponent()
        val before = opponent.amber
        opponent.amber += amount
        game.log("${opponent.name} gains $amount amber (${opponent.name}: $before -> ${opponent.amber})")
    }

    private fun stealAmber(game: GameState, player: PlayerState, amount: Int) {
        val opponent = game.opponent()
        val playerBefore = player.amber
        val opponentBefore = opponent.amber
        val stolen = min(amount, opponent.amber)
        opponent.amber -= stolen
        player.amber += stolen
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
            applyDamage(target, effect.amount)
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

    private fun applyDamage(card: InPlayCard, amount: Int) {
        if (card.ward) {
            card.ward = false
            return
        }
        card.damage += (amount - card.armor).coerceAtLeast(0)
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
        defender.damage += attacker.power
        attacker.damage += defender.power
        attacker.exhausted = true
        game.log("${attacker.cardId} readies and fights ${defender.cardId}")
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

    private fun evaluateCondition(game: GameState, player: PlayerState, condition: Condition): Boolean {
        val opponent = game.opponent()
        return when (condition) {
            is OpponentHasMoreAmber -> opponent.amber > player.amber
            is SelfHasMoreAmber -> player.amber > opponent.amber
            is OpponentHasFewerCreatures -> opponent.creatures.size < player.creatures.size
            is SelfHasFewerCreatures -> player.creatures.size < opponent.creatures.size
            is TargetIsDamaged -> false // not used in simple conditions
            is Always -> true
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
            zone = com.aember.analyzer.domain.engine.model.Zone.PLAY_AREA,
            house = com.aember.analyzer.domain.model.House.BROBNAR,
            type = com.aember.analyzer.domain.model.CardType.CREATURE
        )
    }

    private fun cardName(cardId: String): String {
        return library?.cardData(cardId)?.name ?: cardId
    }
}
