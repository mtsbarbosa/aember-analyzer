package com.aember.analyzer.domain.ai

import com.aember.analyzer.domain.engine.AIPlayer
import com.aember.analyzer.domain.engine.CardLibrary
import com.aember.analyzer.domain.engine.action.*
import com.aember.analyzer.domain.engine.model.*
import com.aember.analyzer.domain.model.CardType
import com.aember.analyzer.domain.model.CatalogCard
import com.aember.analyzer.domain.model.House

/**
 * A heuristic AI that evaluates the board state before choosing an action.
 *
 * It considers:
 * - Forge urgency (prefer reaping when close to a key)
 * - Board control delta (potential amber generation of both players)
 * - Threat removal (high-value opponent creatures)
 * - Action abilities as tactical plays
 *
 * Decision rationale is written to the game log so it can be inspected in verbose output.
 */
class TacticalAI(
    private val library: CardLibrary,
    private val settings: AISettings = AISettings.DEFAULT
) : AIPlayer {

    override fun choose(game: GameState, actions: List<Action>): Action {
        require(actions.isNotEmpty()) { "No legal actions" }

        val chooseHouseActions = actions.filterIsInstance<ChooseHouseAction>()
        if (chooseHouseActions.isNotEmpty()) {
            return chooseHouse(game, chooseHouseActions)
        }

        return chooseMainPhaseAction(game, actions)
    }

    private fun chooseHouse(game: GameState, actions: List<ChooseHouseAction>): Action {
        val scored = actions.map { ScoredAction(it, scoreHouse(game, it.house), "score=${format(scoreHouse(game, it.house))}") }
        val best = scored.maxBy { it.score }
        val action = best.action as ChooseHouseAction
        val player = game.activePlayer()

        val details = scored.joinToString(", ") { "${(it.action as ChooseHouseAction).house}=${format(it.score)}" }
        game.log("${player.name}'s tactical AI chooses ${action.house} ($details)")
        return action
    }

    private fun chooseMainPhaseAction(game: GameState, actions: List<Action>): Action {
        val scored = actions.map { scoreAction(game, it) }
        val best = scored.maxBy { it.score }
        val runnerUp = scored.filter { it.action != best.action }.maxByOrNull { it.score }

        val player = game.activePlayer()
        val runnerUpText = runnerUp?.let { ", runner-up ${formatAction(it.action)}=${format(it.score)}" } ?: ""
        game.log("${player.name}'s tactical AI: ${best.rationale} (score=${format(best.score)}$runnerUpText)")

        return best.action
    }

    private fun scoreHouse(game: GameState, house: House): Double {
        val player = game.activePlayer()

        // Cards in hand that can be played this turn
        val playableCards = player.hand.count { cardId ->
            val card = player.deck.cards.find { it.cardId == cardId }
            card?.effectiveHouse == house
        }

        // Unexhausted creatures of this house that can act
        val activeCreatures = player.creatures.count {
            !it.exhausted && !it.stun && it.house == house
        }

        // Artifacts of this house with usable abilities
        val activeArtifacts = player.artifacts.count {
            !it.exhausted && it.house == house
        }

        return playableCards * settings.playCardWeight +
            activeCreatures * (settings.reapWeight + settings.fightWeight) * 0.5 +
            activeArtifacts * settings.actionAbilityWeight
    }

    private fun scoreAction(game: GameState, action: Action): ScoredAction {
        return when (action) {
            is PlayCardAction -> scorePlayCard(game, action)
            is UseCreatureAction -> scoreUseCreature(game, action)
            is UseArtifactAction -> scoreUseArtifact(game, action)
            is DiscardAction -> ScoredAction(action, settings.discardWeight, "discard")
            is EndTurnAction -> ScoredAction(action, 0.0, "end turn")
            else -> ScoredAction(action, 0.0, "unknown action")
        }
    }

    private fun scorePlayCard(game: GameState, action: PlayCardAction): ScoredAction {
        val player = game.activePlayer()
        val opponent = game.opponent()
        val catalog = library.cardData(action.cardId)
            ?: return ScoredAction(action, 0.0, "play unknown card")

        val base = when (catalog.type) {
            CardType.CREATURE -> 1.0 + (catalog.power ?: 0) * 0.05
            CardType.ARTIFACT -> 0.8
            CardType.ACTION -> 1.0 + catalog.amber * 0.3
            CardType.UPGRADE -> 0.6
            else -> 0.5
        }

        // Slightly prefer cards that help forge when close
        val forgeBonus = if (player.amber >= settings.forgeUrgencyThreshold) {
            catalog.amber * settings.forgeUrgencyBonus * 0.2
        } else 0.0

        // Contextual penalty: effects that steal/capture/force opponent to lose amber are
        // wasted when the opponent has no amber.
        val opponentAmberPenalty = opponentAmberEffectPenalty(catalog, opponent)

        val score = (base + forgeBonus - opponentAmberPenalty) * settings.playCardWeight
        val rationale = buildString {
            append("play ${catalog.name} (${catalog.type}, base=${format(base)}, forgeBonus=${format(forgeBonus)})")
            if (opponentAmberPenalty > 0) append(", oppAmberPenalty=${format(opponentAmberPenalty)}")
        }
        return ScoredAction(action, score, rationale)
    }

    /**
     * Detects cards whose Play effect relies on the opponent having amber (steal, capture,
     * or force the opponent to lose amber). When the opponent has none, the effect is wasted.
     */
    private fun opponentAmberEffectPenalty(catalog: CatalogCard, opponent: PlayerState): Double {
        if (opponent.amber > 0) return 0.0
        val text = catalog.text.lowercase()
        val wastesOpponentAmber = (
            (text.contains("opponent loses") || text.contains("steal") || text.contains("capture")) &&
                text.contains("amber")
        )
        return if (wastesOpponentAmber) 0.5 else 0.0
    }

    private fun scoreUseCreature(game: GameState, action: UseCreatureAction): ScoredAction {
        return when (action.useType) {
            is ReapUse -> scoreReap(game, action)
            is FightUse -> scoreFight(game, action)
            is ActionAbilityUse -> scoreCreatureAbility(action)
            is RemoveStunUse -> ScoredAction(action, 0.3, "remove stun")
        }
    }

    private fun scoreReap(game: GameState, action: UseCreatureAction): ScoredAction {
        val player = game.activePlayer()
        val amberValue = 1.0

        val forgeUrgency = when {
            player.amber >= 6 -> 0.0 // already forging this turn, extra amber less urgent
            player.amber >= settings.forgeUrgencyThreshold -> settings.forgeUrgencyBonus
            player.amber >= settings.forgeUrgencyThreshold - 1 -> settings.secondKeyUrgencyBonus
            else -> 0.0
        }

        val score = (amberValue + forgeUrgency) * settings.reapWeight
        val rationale = "reap (amber=${player.amber}, urgency=${format(forgeUrgency)})"
        return ScoredAction(action, score, rationale)
    }

    private fun scoreFight(game: GameState, action: UseCreatureAction): ScoredAction {
        val player = game.activePlayer()
        val opponent = game.opponent()
        val fightUse = action.useType as FightUse
        val attacker = player.creatures.find { it.instanceId == action.instanceId }
            ?: return ScoredAction(action, 0.0, "fight with missing creature")
        val target = opponent.creatures.find { it.instanceId == fightUse.targetInstanceId }
            ?: return ScoredAction(action, 0.0, "fight missing target")

        val targetThreat = evaluateCreatureThreat(target)
        val willKill = attacker.power >= target.power
        val killValue = if (willKill) targetThreat * settings.threatRemovalWeight else targetThreat * 0.3

        val survivalBonus = if (target.power < attacker.power) 0.5 else 0.0
        val attackerThreat = evaluateCreatureThreat(attacker)
        val riskPenalty = if (target.power >= attacker.power) attackerThreat * 0.5 else 0.0

        val myProduction = amberGenerationPotential(player)
        val opponentProduction = amberGenerationPotential(opponent)
        val boardControlDelta = (opponentProduction - myProduction) * settings.boardControlWeight

        val score = (killValue + survivalBonus - riskPenalty + boardControlDelta) * settings.fightWeight
        val rationale = buildString {
            append("fight ${attacker.cardId}(P${attacker.power}) vs ${target.cardId}(P${target.power})")
            append(", threat=${format(targetThreat)}")
            append(", willKill=$willKill")
            append(", boardDelta=${format(boardControlDelta)}")
            append(", survival=${format(survivalBonus)}")
            append(", risk=${format(riskPenalty)}")
        }
        return ScoredAction(action, score, rationale)
    }

    private fun scoreCreatureAbility(action: UseCreatureAction): ScoredAction {
        val use = action.useType as ActionAbilityUse
        // Omni is often powerful; generic action abilities get a moderate tactical score
        val abilityBase = if (use.abilityId == "Omni") 1.2 else 0.9
        val score = abilityBase * settings.actionAbilityWeight
        val rationale = "use ${action.instanceId}'s ${use.abilityId}"
        return ScoredAction(action, score, rationale)
    }

    private fun scoreUseArtifact(game: GameState, action: UseArtifactAction): ScoredAction {
        val player = game.activePlayer()
        val artifact = player.artifacts.find { it.instanceId == action.instanceId }
        val score = 1.0 * settings.actionAbilityWeight
        val rationale = "use artifact ${artifact?.cardId ?: action.instanceId}'s ${action.abilityId}"
        return ScoredAction(action, score, rationale)
    }

    private fun evaluateCreatureThreat(creature: InPlayCard): Double {
        var threat = creature.power * 0.1 + creature.capturedAmber * 0.5
        if (creature.hasKeyword(Keyword.SKIRMISH)) threat += 1.0
        if (creature.hasKeyword(Keyword.HAZARDOUS)) threat += 1.0
        if (creature.hasKeyword(Keyword.ELUSIVE)) threat += 0.5
        if (creature.hasKeyword(Keyword.TAUNT)) threat += 0.5
        if (creature.hasKeyword(Keyword.ASSAULT)) threat += 0.5
        return threat
    }

    private fun amberGenerationPotential(player: PlayerState): Int {
        return player.creatures.count { !it.exhausted && !it.stun }
    }

    private fun formatAction(action: Action): String = when (action) {
        is PlayCardAction -> "play ${action.cardId}"
        is UseCreatureAction -> when (action.useType) {
            is ReapUse -> "reap"
            is FightUse -> "fight"
            is ActionAbilityUse -> "ability"
            is RemoveStunUse -> "unstun"
        }
        is UseArtifactAction -> "artifact ability"
        is DiscardAction -> "discard"
        is EndTurnAction -> "end turn"
        is ChooseHouseAction -> "house ${action.house}"
    }

    private fun format(value: Double): String = String.format("%.2f", value)

    private data class ScoredAction(
        val action: Action,
        val score: Double,
        val rationale: String
    )
}
