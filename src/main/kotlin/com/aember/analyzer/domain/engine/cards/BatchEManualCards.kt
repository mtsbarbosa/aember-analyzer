package com.aember.analyzer.domain.engine.cards

import com.aember.analyzer.domain.engine.CardScript
import com.aember.analyzer.domain.engine.dsl.EffectResolver
import com.aember.analyzer.domain.engine.dsl.SacrificeSelf
import com.aember.analyzer.domain.engine.effects.HostCreatureScope
import com.aember.analyzer.domain.engine.effects.RedirectOpponentForgeAmber
import com.aember.analyzer.domain.engine.effects.SkipOwnForgeStep
import com.aember.analyzer.domain.engine.effects.SpendCapturedAmberOnKeys
import com.aember.analyzer.domain.engine.effects.StaticBehavior
import com.aember.analyzer.domain.engine.effects.StaticEffect
import com.aember.analyzer.domain.engine.effects.StaticEffectKind
import com.aember.analyzer.domain.engine.events.CardLeftPlay
import com.aember.analyzer.domain.engine.events.GameEvent
import com.aember.analyzer.domain.engine.fidelity.CardFidelity
import com.aember.analyzer.domain.engine.fidelity.FidelityLevel
import com.aember.analyzer.domain.engine.model.GameState
import com.aember.analyzer.domain.engine.model.InPlayCard
import com.aember.analyzer.domain.engine.model.Keyword
import com.aember.analyzer.domain.engine.model.PlayerState
import com.aember.analyzer.domain.model.House

/** Phase 3 batch (e) manual scripts: control upgrades, destruction replacements, forge control. */

object ArmageddonCloak : CardScript {
    override fun fidelity() = CardFidelity(
        level = FidelityLevel.FULL,
        notes = "Grants the host hazardous 2; when the host would be destroyed, fully heal it and destroy the cloak instead"
    )

    override fun staticEffects(): List<StaticEffect> = listOf(
        StaticEffect(StaticEffectKind.ADD_KEYWORD, scope = HostCreatureScope, keyword = Keyword.HAZARDOUS, amount = 2)
    )

    override fun onHostWouldBeDestroyed(game: GameState, player: PlayerState, upgrade: InPlayCard, host: InPlayCard): Boolean {
        host.damage = 0
        game.log("${host.cardId} is fully healed and ${upgrade.cardId} is destroyed instead")
        return true
    }
}

object PhoenixHeart : CardScript {
    override fun fidelity() = CardFidelity(
        level = FidelityLevel.FULL,
        notes = "When the host would be destroyed, return it to its owner's hand and deal 3 damage to each creature in play"
    )

    override fun onHostWouldBeDestroyed(game: GameState, player: PlayerState, upgrade: InPlayCard, host: InPlayCard): Boolean {
        // Other upgrades fall off to their owner's discard; the host returns to hand.
        host.upgrades.filter { it.instanceId != upgrade.instanceId }.forEach { other ->
            game.player(other.ownerIndex).discard.add(other.cardId)
        }
        host.upgrades.clear()
        player.creatures.remove(host)
        game.player(host.ownerIndex).hand.add(host.cardId)
        game.log("${host.cardId} returns to its owner's hand instead of being destroyed")
        // 3 damage to each creature in play (armor is not applied by this raw path).
        game.players.flatMap { it.creatures }.forEach { creature -> creature.damage += 3 }
        game.log("Phoenix Heart deals 3 damage to each creature in play")
        return true
    }
}

object CollarOfSubordination : CardScript {
    override fun fidelity() = CardFidelity(
        level = FidelityLevel.FULL,
        notes = "You control the attached creature; control returns when the collar leaves play"
    )

    override fun onPlay(game: GameState, player: PlayerState, card: InPlayCard) {
        val host = card.hostId?.let { id -> game.players.asSequence().flatMap { it.creatures.asSequence() }.find { it.instanceId == id } }
            ?: return
        host.controllerIndex = player.index
        game.log("${player.name} takes control of ${host.cardId} (Collar of Subordination)")
    }

    override fun onEvent(game: GameState, player: PlayerState, card: InPlayCard, event: GameEvent) {
        if (event is CardLeftPlay && event.instanceId == card.instanceId) {
            val host = game.players.asSequence().flatMap { it.creatures.asSequence() }
                .find { it.instanceId == card.hostId }
            if (host != null && host.controllerIndex != host.ownerIndex) {
                host.controllerIndex = host.ownerIndex
                game.log("${host.cardId} returns to ${game.player(host.ownerIndex).name}'s control")
            }
        }
    }
}

object HarlandMindlock : CardScript {
    override fun fidelity() = CardFidelity(
        level = FidelityLevel.FULL,
        notes = "Play: take control of an enemy flank creature until Harland Mindlock leaves play"
    )

    override fun onPlay(game: GameState, player: PlayerState, card: InPlayCard) {
        val opponent = game.opponent(player)
        if (opponent.creatures.isEmpty()) return
        val flanks = listOfNotNull(opponent.creatures.firstOrNull(), opponent.creatures.lastOrNull())
            .distinctBy { it.instanceId }
        val target = flanks.maxByOrNull { it.power } ?: return
        opponent.creatures.remove(target)
        target.controllerIndex = player.index
        player.creatures.add(target)
        game.log("${player.name} takes control of ${target.cardId} with Harland Mindlock")
    }

    override fun onEvent(game: GameState, player: PlayerState, card: InPlayCard, event: GameEvent) {
        if (event is CardLeftPlay && event.instanceId == card.instanceId) {
            // Taken creatures sit in Harland's controller's battleline with a foreign owner.
            val ownerPlayer = game.player(card.ownerIndex)
            ownerPlayer.creatures.filter { it.ownerIndex != card.ownerIndex }.toList().forEach { creature ->
                ownerPlayer.creatures.remove(creature)
                creature.controllerIndex = creature.ownerIndex
                game.player(creature.ownerIndex).creatures.add(creature)
                game.log("${creature.cardId} returns to ${game.player(creature.ownerIndex).name}'s control")
            }
        }
    }
}

object Sneklifter : CardScript {
    override fun fidelity() = CardFidelity(
        level = FidelityLevel.FULL,
        notes = "Play: take control of an enemy artifact; while controlled it counts as Shadows unless it matches one of your houses"
    )

    override fun onPlay(game: GameState, player: PlayerState, card: InPlayCard) {
        val opponent = game.opponent(player)
        val target = opponent.artifacts.maxByOrNull { it.capturedAmber * 10 + it.power } ?: return
        opponent.artifacts.remove(target)
        target.controllerIndex = player.index
        if (target.house !in player.deck.houses) {
            target.baseHouse = target.house
            target.house = House.SHADOWS
        }
        player.artifacts.add(target)
        game.log("${player.name} takes control of ${target.cardId} with Sneklifter")
    }

    override fun onEvent(game: GameState, player: PlayerState, card: InPlayCard, event: GameEvent) {
        if (event is CardLeftPlay && event.instanceId == card.instanceId) {
            // Return every taken artifact (controller != owner) to its owner and
            // undo the Shadows house override applied while under our control.
            game.players.forEach { p ->
                p.artifacts.filter { it.controllerIndex != it.ownerIndex }.toList().forEach { artifact ->
                    p.artifacts.remove(artifact)
                    artifact.controllerIndex = artifact.ownerIndex
                    if (artifact.house != artifact.baseHouse) {
                        val original = artifact.baseHouse
                        artifact.house = original
                        artifact.baseHouse = original
                    }
                    game.player(artifact.ownerIndex).artifacts.add(artifact)
                    game.log("${artifact.cardId} returns to ${game.player(artifact.ownerIndex).name}'s control")
                }
            }
        }
    }
}

class TheSting(private val resolver: EffectResolver) : CardScript {
    override fun fidelity() = CardFidelity(
        level = FidelityLevel.FULL,
        notes = "Skip your forge step; you get all amber spent by your opponent when forging keys. Action: sacrifice The Sting"
    )

    override fun staticBehaviors(): List<StaticBehavior> = listOf(
        SkipOwnForgeStep,
        RedirectOpponentForgeAmber
    )

    override fun actionAbilities(): List<String> = listOf(SACRIFICE_ABILITY)

    override fun resolveActionAbility(abilityId: String, game: GameState, player: PlayerState, card: InPlayCard) {
        if (abilityId == SACRIFICE_ABILITY) resolver.resolve(game, player, card, SacrificeSelf)
    }

    override fun onPlay(game: GameState, player: PlayerState, card: InPlayCard) {
        game.log("${player.name} plays The Sting; they skip their forge step and collect the opponent's forge amber")
    }

    private companion object {
        const val SACRIFICE_ABILITY = "Sacrifice The Sting"
    }
}

private const val MOVE_ABILITY = "Move 1 amber from your pool onto this card"

object PocketUniverse : CardScript {
    override fun fidelity() = CardFidelity(
        level = FidelityLevel.FULL,
        notes = "Amber on Pocket Universe may be spent when forging keys; Action: move 1 amber from your pool onto it"
    )

    override fun actionAbilities(): List<String> = listOf(MOVE_ABILITY)

    override fun resolveActionAbility(abilityId: String, game: GameState, player: PlayerState, card: InPlayCard) {
        if (abilityId != MOVE_ABILITY || player.amber < 1) return
        player.amber -= 1
        card.capturedAmber += 1
        game.log("${player.name} moves 1 amber from their pool onto Pocket Universe (${card.capturedAmber} on it)")
    }

    override fun staticBehaviors(): List<StaticBehavior> = listOf(SpendCapturedAmberOnKeys)

    override fun onPlay(game: GameState, player: PlayerState, card: InPlayCard) {
        // Nothing on play; the Action moves amber from the pool onto the card.
    }
}

object SafePlace : CardScript {
    override fun fidelity() = CardFidelity(
        level = FidelityLevel.FULL,
        notes = "Amber on Safe Place may be spent when forging keys; Action: move 1 amber from your pool onto it"
    )

    override fun actionAbilities(): List<String> = listOf(MOVE_ABILITY)

    override fun resolveActionAbility(abilityId: String, game: GameState, player: PlayerState, card: InPlayCard) {
        if (abilityId != MOVE_ABILITY || player.amber < 1) return
        player.amber -= 1
        card.capturedAmber += 1
        game.log("${player.name} moves 1 amber from their pool onto Safe Place (${card.capturedAmber} on it)")
    }

    override fun staticBehaviors(): List<StaticBehavior> = listOf(SpendCapturedAmberOnKeys)
}

object Restringuntus : CardScript {
    override fun fidelity() = CardFidelity(
        level = FidelityLevel.FULL,
        notes = "Play: choose a house; your opponent cannot choose it as their active house until Restringuntus leaves play"
    )

    override fun onPlay(game: GameState, player: PlayerState, card: InPlayCard) {
        // Greedy: the opponent's most-played house (deck houses ordered by their creatures in play).
        val opponent = game.opponent(player)
        val chosen = opponent.creatures.groupingBy { it.house }
            .eachCount()
            .maxByOrNull { it.value }?.key
            ?: opponent.deck.houses.firstOrNull()
            ?: return
        card.chosenHouse = chosen
        game.log("${player.name} chooses $chosen; ${opponent.name} cannot choose it while Restringuntus is in play")
    }

    override fun bansHouseChoice(card: InPlayCard): House? = card.chosenHouse
}
