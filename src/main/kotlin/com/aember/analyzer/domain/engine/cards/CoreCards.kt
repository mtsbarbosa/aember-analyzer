package com.aember.analyzer.domain.engine.cards

import com.aember.analyzer.domain.engine.CardScript
import com.aember.analyzer.domain.engine.fidelity.CardFidelity
import com.aember.analyzer.domain.engine.fidelity.FidelityLevel
import com.aember.analyzer.domain.engine.model.GameState
import com.aember.analyzer.domain.engine.model.InPlayCard
import com.aember.analyzer.domain.engine.model.PlayerState

object GatewayToDis : CardScript {
    override fun fidelity() = CardFidelity(
        level = FidelityLevel.APPROXIMATED,
        notes = "Destroys all creatures and gives chains, but bypasses normal destruction pipeline"
    )

    override fun onPlay(game: GameState, player: PlayerState, card: InPlayCard) {
        game.log("Gateway to Dis destroys all creatures and gives ${player.name} 3 chains")
        val allCreatures = game.players.flatMap { it.creatures }
        allCreatures.forEach {
            it.damage = Int.MAX_VALUE
        }
        player.chains += 3
    }
}

object BadPenny : CardScript {
    override fun fidelity() = CardFidelity(
        level = FidelityLevel.FULL,
        notes = "Destroyed: return from discard to hand"
    )

    override fun onDestroyed(game: GameState, player: PlayerState, card: InPlayCard) {
        game.log("Bad Penny returns to hand")
        // Remove from discard and add to hand instead.
        player.discard.remove(card.cardId)
        player.hand.add(card.cardId)
    }
}

object Anger : CardScript {
    override fun fidelity() = CardFidelity(
        level = FidelityLevel.PARTIAL,
        notes = "Target selection and fight resolution are approximated"
    )

    override fun onPlay(game: GameState, player: PlayerState, card: InPlayCard) {
        val target = player.creatures.firstOrNull() ?: return
        game.log("Anger readies ${target.cardId} and makes it fight")
        target.exhausted = false
        val enemy = game.opponent().creatures.randomOrNull() ?: return
        target.attackedThisTurn = true
        enemy.attackedThisTurn = true
        // Simple fight exchange without re-triggering keyword handling for clarity.
        enemy.damage += target.power
        target.damage += enemy.power
    }
}

object Drumble : CardScript {
    override fun fidelity() = CardFidelity(
        level = FidelityLevel.FULL,
        notes = "Play effect captures all opponent amber when they have 7 or more"
    )

    override fun onPlay(game: GameState, player: PlayerState, card: InPlayCard) {
        val opponent = game.opponent()
        if (opponent.amber >= 7) {
            val captured = opponent.amber
            opponent.amber = 0
            card.capturedAmber += captured
            game.log("${card.cardId} captures all $captured amber from ${opponent.name}")
        }
    }
}

object EtherSpider : CardScript {
    override fun fidelity() = CardFidelity(
        level = FidelityLevel.FULL,
        notes = "Redirects opponent amber gains to captured amber; deals no damage when fighting"
    )

    override fun modifyFightDamage(isAttacker: Boolean, baseDamage: Int): Int = 0

    override fun capturesOpponentAmberGains(): Boolean = true
}

object SpeedSigil : CardScript {
    override fun fidelity() = CardFidelity(
        level = FidelityLevel.FULL,
        notes = "The first creature played each turn enters play ready"
    )

    override fun cardEntersReady(): Boolean = true
}

object ShadowSelf : CardScript {
    override fun fidelity() = CardFidelity(
        level = FidelityLevel.FULL,
        notes = "Deals no damage when fighting; damage to non-Specter neighbors is redirected here"
    )

    override fun modifyFightDamage(isAttacker: Boolean, baseDamage: Int): Int =
        if (isAttacker) 0 else baseDamage

    override fun redirectsNeighborDamage(): Boolean = true
}
