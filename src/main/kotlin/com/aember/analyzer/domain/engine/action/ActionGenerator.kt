package com.aember.analyzer.domain.engine.action

import com.aember.analyzer.domain.engine.CardLibrary
import com.aember.analyzer.domain.engine.model.GameState
import com.aember.analyzer.domain.engine.model.InPlayCard
import com.aember.analyzer.domain.engine.model.Keyword
import com.aember.analyzer.domain.engine.model.PlayerState
import com.aember.analyzer.domain.engine.model.hasKeyword
import com.aember.analyzer.domain.model.CardType
import com.aember.analyzer.domain.model.House

/**
 * Generates the legal actions available to the active player in the current game state.
 */
class ActionGenerator(
    private val library: CardLibrary
) {

    fun generate(game: GameState): List<Action> {
        val player = game.activePlayer()
        return when {
            player.activeHouse == null -> generateHouseChoices(player)
            else -> generateMainPhaseActions(game, player)
        }
    }

    private fun generateHouseChoices(player: PlayerState): List<Action> {
        return player.deck.houses.distinct()
            .filter { it != player.deniedHouseNextTurn }
            .map { ChooseHouseAction(it) }
    }

    private fun generateMainPhaseActions(game: GameState, player: PlayerState): List<Action> {
        val actions = mutableListOf<Action>()
        val activeHouse = player.activeHouse ?: return emptyList()

        // Play cards from hand
        player.hand.distinct().forEach { cardId ->
            val card = library.cardData(cardId) ?: return@forEach
            if (isHouseMatch(card.house, activeHouse) && canUseCardName(player, cardId)) {
                if (!card.keywords.any { it.startsWith("alpha") } || game.actionsThisPhase == 0) {
                    if (library.scriptFor(cardId).playable(game, player)) {
                        actions.addAll(generatePlayActions(player, cardId, card.type))
                    }
                }
            }
        }

        // Use creatures
        player.creatures.filter { !it.exhausted || it.stun }.forEach { creature ->
            if (isHouseMatch(creature.house, activeHouse) && canUseCardName(player, creature.cardId)) {
                actions.addAll(generateCreatureUses(game, creature))
            }
        }
        if (player.cannotFightNextTurn) {
            actions.removeAll { it is UseCreatureAction && it.useType is FightUse }
        }

        // Use artifacts (non-Omni abilities require the artifact's house)
        player.artifacts.filter { !it.exhausted }.forEach { artifact ->
            if (isHouseMatch(artifact.house, activeHouse) && canUseCardName(player, artifact.cardId)) {
                library.scriptFor(artifact.cardId).actionAbilities()
                    .filter { it != "Omni" }
                    .forEach { ability ->
                        actions.add(UseArtifactAction(artifact.instanceId, ability))
                    }
            }
        }

        // Omni abilities may be used as if the card belonged to the active house.
        player.creatures.filter { !it.exhausted && !it.stun }.forEach { creature ->
            if (canUseCardName(player, creature.cardId)) {
                library.scriptFor(creature.cardId).actionAbilities()
                    .filter { it == "Omni" }
                    .forEach { ability ->
                        actions.add(UseCreatureAction(creature.instanceId, ActionAbilityUse(ability)))
                    }
            }
        }
        player.artifacts.filter { !it.exhausted }.forEach { artifact ->
            if (canUseCardName(player, artifact.cardId)) {
                library.scriptFor(artifact.cardId).actionAbilities()
                    .filter { it == "Omni" }
                    .forEach { ability ->
                        actions.add(UseArtifactAction(artifact.instanceId, ability))
                    }
            }
        }

        // Discard from hand
        player.hand.distinct().forEach { cardId ->
            val card = library.cardData(cardId) ?: return@forEach
            if (isHouseMatch(card.house, activeHouse) && canUseCardName(player, cardId)) {
                actions.add(DiscardAction(cardId))
            }
        }

        actions.add(EndTurnAction)
        return actions
    }

    private fun generatePlayActions(player: PlayerState, cardId: String, type: CardType): List<Action> {
        return when (type) {
            CardType.CREATURE -> {
                // Placement is a player choice: left flank (0) or right flank (size).
                // If the battleline is empty both options resolve to index 0, so de-duplicate.
                val rightIndex = player.creatures.size
                if (rightIndex == 0) {
                    listOf(PlayCardAction(cardId, placementIndex = 0))
                } else {
                    listOf(
                        PlayCardAction(cardId, placementIndex = 0),
                        PlayCardAction(cardId, placementIndex = rightIndex)
                    )
                }
            }
            CardType.ARTIFACT -> listOf(PlayCardAction(cardId))
            CardType.ACTION -> listOf(PlayCardAction(cardId))
            CardType.UPGRADE -> {
                // Attach to any friendly creature
                player.creatures.map { PlayCardAction(cardId, it.instanceId) }
            }
            else -> emptyList()
        }
    }

    private fun generateCreatureUses(game: GameState, creature: InPlayCard): List<Action> {
        val actions = mutableListOf<Action>()
        if (creature.stun) {
            actions.add(UseCreatureAction(creature.instanceId, RemoveStunUse))
            return actions
        }
        actions.add(UseCreatureAction(creature.instanceId, ReapUse))
        val opponentCreatures = game.opponent().creatures
        if (opponentCreatures.isNotEmpty()) {
            val tauntCreatures = opponentCreatures.filter { it.hasKeyword(Keyword.TAUNT) }
            val legalTargets = if (tauntCreatures.isNotEmpty() && !creature.hasKeyword(Keyword.TAUNT)) {
                tauntCreatures
            } else {
                opponentCreatures
            }
            legalTargets.forEach { target ->
                actions.add(UseCreatureAction(creature.instanceId, FightUse(target.instanceId)))
            }
        }
        library.scriptFor(creature.cardId).actionAbilities()
            .filter { it != "Omni" }
            .forEach { ability ->
                actions.add(UseCreatureAction(creature.instanceId, ActionAbilityUse(ability)))
            }
        return actions
    }

    private fun isHouseMatch(cardHouse: House, activeHouse: House): Boolean =
        cardHouse == activeHouse

    private fun canUseCardName(player: PlayerState, cardId: String): Boolean {
        return (player.cardNamesPlayedOrUsedThisRound[cardId] ?: 0) < 6
    }
}
