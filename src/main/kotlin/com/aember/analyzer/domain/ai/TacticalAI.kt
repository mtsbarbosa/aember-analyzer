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

        // Creatures in hand of this house (future tempo)
        val creaturesInHand = player.hand.count { cardId ->
            val deckCard = player.deck.cards.find { it.cardId == cardId }
            val catalog = library.cardData(cardId)
            deckCard?.effectiveHouse == house && catalog?.type == CardType.CREATURE
        }

        // Unexhausted creatures of this house that can act
        val activeCreatures = player.creatures.count {
            !it.exhausted && !it.stun && it.house == house
        }

        // Artifacts of this house with usable abilities
        val activeArtifacts = player.artifacts.count {
            !it.exhausted && it.house == house
        }

        val chainSynergy = hasHouseChainSynergy(player, house)

        val baseScore = playableCards * settings.playCardWeight +
            activeCreatures * (settings.reapWeight + settings.fightWeight) * 0.5 +
            creaturesInHand * settings.playCardWeight * 0.7 +
            activeArtifacts * settings.actionAbilityWeight

        // Small flat penalty for houses that offer plays but no creatures, unless the hand
        // has chain synergy (e.g. Library Access) that makes the turn productive anyway.
        val hasNoTempo = activeCreatures == 0 && creaturesInHand == 0
        val setupPenalty = if (hasNoTempo && !chainSynergy && playableCards > 0) -0.25 * playableCards else 0.0

        return (baseScore + setupPenalty).coerceAtLeast(0.0)
    }

    private fun hasHouseChainSynergy(player: PlayerState, house: House): Boolean {
        return player.hand.any { cardId ->
            val deckCard = player.deck.cards.find { it.cardId == cardId }
            val catalog = library.cardData(cardId)
            if (deckCard?.effectiveHouse != house || catalog == null) return@any false
            val text = catalog.text.lowercase()
            (text.contains("each time you play") && text.contains("draw a card")) ||
                text.contains("play the top card of your deck")
        }
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

        // Bonus for cards that get stronger with more cards of the same house in hand.
        val chainBonus = chainSynergyBonus(catalog, player)

        // Penalty for removal cards that lack enough targets.
        val removalPenalty = removalTargetPenalty(catalog, game)

        // Placement bonus: high-value creatures next to protectors are more survivable.
        val placementBonus = scoreCreaturePlacement(action, catalog, player)

        val score = (base + forgeBonus + chainBonus - opponentAmberPenalty - removalPenalty + placementBonus) * settings.playCardWeight
        val rationale = buildString {
            append("play ${catalog.name} (${catalog.type}, base=${format(base)}, forgeBonus=${format(forgeBonus)})")
            if (chainBonus > 0) append(", chainBonus=${format(chainBonus)}")
            if (opponentAmberPenalty > 0) append(", oppAmberPenalty=${format(opponentAmberPenalty)}")
            if (removalPenalty > 0) append(", removalPenalty=${format(removalPenalty)}")
            if (placementBonus != 0.0) append(", placementBonus=${format(placementBonus)}")
        }
        return ScoredAction(action, score, rationale)
    }

    /**
     * Bonus for cards that reward playing many cards of the same house this turn,
     * e.g. Library Access (draw on each Logos play) and Wild Wormhole (play top card).
     */
    private fun chainSynergyBonus(catalog: CatalogCard, player: PlayerState): Double {
        val text = catalog.text.lowercase()
        val sameHouseCardsInHand = player.hand.count { cardId ->
            val deckCard = player.deck.cards.find { it.cardId == cardId }
            deckCard?.effectiveHouse == catalog.house
        }
        return when {
            text.contains("each time you play a") && text.contains("draw a card") -> {
                sameHouseCardsInHand * 0.3
            }
            text.contains("play the top card of your deck") -> {
                sameHouseCardsInHand * 0.15
            }
            else -> 0.0
        }
    }

    /**
     * Penalizes removal cards when there are not enough worthwhile targets on the board.
     */
    private fun removalTargetPenalty(catalog: CatalogCard, game: GameState): Double {
        val text = catalog.text.lowercase()
        val totalCreatures = game.players.sumOf { it.creatures.size }
        val enemyCreatures = game.opponent().creatures.size

        val isBouncingDeathquark = text.contains("destroy a creature") && text.contains("destroy another creature")
        if (isBouncingDeathquark && totalCreatures < 2) return 0.8

        val isNeutronShark = catalog.id == "neutron-shark" ||
            (text.contains("destroy a creature") && text.contains("destroy the top card of your deck"))
        if (isNeutronShark && totalCreatures < 1) return 0.8

        // Generic one-shot removal is slightly worse into an empty board.
        if (text.contains("destroy a creature") && enemyCreatures == 0) return 0.4

        return 0.0
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
        val amberControlThreat = evaluateAmberControlThreat(library.cardData(target.cardId))
        val rationale = buildString {
            append("fight ${attacker.cardId}(P${attacker.power}) vs ${target.cardId}(P${target.power})")
            append(", threat=${format(targetThreat)}")
            append(", willKill=$willKill")
            append(", boardDelta=${format(boardControlDelta)}")
            append(", survival=${format(survivalBonus)}")
            append(", risk=${format(riskPenalty)}")
            if (amberControlThreat > 0.0) append(", amberControl=${format(amberControlThreat)}")
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
        val catalog = artifact?.let { library.cardData(it.cardId) }
        val text = catalog?.text?.lowercase() ?: ""
        val wipesHand = text.contains("shuffle your hand") && text.contains("into your deck")
        val base = when {
            wipesHand && player.hand.size <= 1 -> 1.4
            wipesHand -> 0.1
            else -> 1.0
        }
        val score = base * settings.actionAbilityWeight
        val rationale = buildString {
            append("use artifact ${artifact?.cardId ?: action.instanceId}'s ${action.abilityId}")
            if (wipesHand) append(", handWipe with ${player.hand.size} cards in hand")
        }
        return ScoredAction(action, score, rationale)
    }

    private fun evaluateCreatureThreat(creature: InPlayCard): Double {
        var threat = creature.power * 0.1 + creature.capturedAmber * 0.5
        if (creature.hasKeyword(Keyword.SKIRMISH)) threat += 1.0
        if (creature.hasKeyword(Keyword.HAZARDOUS)) threat += 1.0
        if (creature.hasKeyword(Keyword.ELUSIVE)) threat += 0.5
        if (creature.hasKeyword(Keyword.TAUNT)) threat += 0.5
        if (creature.hasKeyword(Keyword.ASSAULT)) threat += 0.5
        val catalog = library.cardData(creature.cardId)
        threat += evaluateAmberControlThreat(catalog) * settings.amberControlThreatWeight
        return threat
    }

    /**
     * Detects ongoing amber-control effects from card text.
     * Higher values make the AI prioritize removing lock/tax creatures
     * like Ether Spider, Drumble, and Grabber-Jammer.
     */
    private fun evaluateAmberControlThreat(catalog: CatalogCard?): Double {
        if (catalog == null) return 0.0
        val text = catalog.text.lowercase()

        // Prevents forging keys entirely.
        if (text.contains("cannot forge")) return 2.0

        // Captures amber whenever the opponent gains amber.
        if (text.contains("gains amber") && text.contains("capture")) return 1.8

        // Forces opponent to pay more amber to forge keys.
        if (text.contains("keys cost") || (text.contains("forge") && text.contains("additional") && text.contains("amber"))) {
            return 1.5
        }

        // Recurring steal/capture on reap/fight/action.
        val hasRecurringTrigger = text.contains("reap:") || text.contains("fight:") || text.contains("action:")
        if (hasRecurringTrigger && text.contains("amber") && (text.contains("steal") || text.contains("capture"))) {
            return 1.2
        }

        // One-time steal/capture/opponent-loses-amber effects.
        if (text.contains("amber") && (text.contains("steal") || text.contains("capture") || text.contains("opponent loses"))) {
            return 0.5
        }

        return 0.0
    }

    private fun amberGenerationPotential(player: PlayerState): Int {
        return player.creatures.count { !it.exhausted && !it.stun }
    }

    /**
     * Scores creature placement. High-value creatures get a small bonus for being placed
     * adjacent to a protector (taunt / Shadow Self), because they are more likely to survive.
     */
    private fun scoreCreaturePlacement(action: PlayCardAction, catalog: CatalogCard, player: PlayerState): Double {
        if (catalog.type != CardType.CREATURE) return 0.0
        if (player.creatures.isEmpty()) return 0.0
        if (!isHighValueCreature(catalog)) return 0.0

        val protectors = player.creatures.mapIndexed { index, creature ->
            val isProtector = creature.hasKeyword(Keyword.TAUNT) ||
                library.scriptFor(creature.cardId).protectsNeighbors()
            index to isProtector
        }.filter { it.second }

        if (protectors.isEmpty()) return 0.0

        val leftProtector = protectors.minByOrNull { it.first }?.first == 0
        val rightProtector = protectors.maxByOrNull { it.first }?.first == player.creatures.size - 1

        val preferredIndex = when {
            leftProtector && rightProtector -> if (player.creatures.size <= 2) 0 else player.creatures.size
            leftProtector -> 0
            rightProtector -> player.creatures.size
            else -> player.creatures.size
        }

        return if (action.placementIndex == preferredIndex) 0.3 else 0.0
    }

    private fun isHighValueCreature(catalog: CatalogCard): Boolean {
        if ((catalog.power ?: 0) >= 4) return true
        val text = catalog.text.lowercase()
        val recurringAmber = (text.contains("reap:") || text.contains("fight:")) &&
            text.contains("amber") &&
            (text.contains("gain") || text.contains("steal") || text.contains("capture"))
        if (recurringAmber) return true
        val keywords = catalog.keywords.map { it.lowercase() }
        if (keywords.contains("skirmish") || keywords.contains("assault") || keywords.contains("hazardous")) return true
        return false
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
