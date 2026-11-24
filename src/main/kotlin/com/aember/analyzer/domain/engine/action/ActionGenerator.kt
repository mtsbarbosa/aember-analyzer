package com.aember.analyzer.domain.engine.action

import com.aember.analyzer.domain.engine.CardLibrary
import com.aember.analyzer.domain.engine.effects.EffectRegistry
import com.aember.analyzer.domain.engine.effects.SelfRestriction
import com.aember.analyzer.domain.engine.effects.UseRestriction
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
            player.activeHouse == null -> generateHouseChoices(game, player)
            else -> generateMainPhaseActions(game, player)
        }
    }

    private fun generateHouseChoices(game: GameState, player: PlayerState): List<Action> {
        val forced = EffectRegistry.collectUseRestrictions(library, game)
            .filter { (source, restriction) ->
                restriction is UseRestriction.ForcedHouse && source.controllerIndex == player.index
            }
            .map { (_, restriction) -> (restriction as UseRestriction.ForcedHouse).house }
        val available = player.deck.houses.distinct().filter { it != player.deniedHouseNextTurn }
        // A forced house only applies when it is actually choosable; otherwise the
        // restriction is ignored so the turn can never be left without a legal choice
        // (e.g. Pitlord controlled by a player without Dis, or Dis denied this turn).
        val restricted = available.filter { it in forced }
        return (if (forced.isNotEmpty() && restricted.isNotEmpty()) restricted else available)
            .map { ChooseHouseAction(it) }
    }

    private fun generateMainPhaseActions(game: GameState, player: PlayerState): List<Action> {
        val actions = mutableListOf<Action>()
        val activeHouse = player.activeHouse ?: return emptyList()
        val restrictions = EffectRegistry.collectUseRestrictions(library, game)

        fun blockedByCannotPlay(type: CardType): Boolean =
            restrictions.any { (source, restriction) ->
                restriction is UseRestriction.CannotPlay &&
                    restriction.type == type && source.controllerIndex == player.index
            }

        fun playLimitReached(): Boolean =
            restrictions.any { (source, restriction) ->
                restriction is UseRestriction.PlayLimit &&
                    source.controllerIndex != player.index &&
                    player.cardsPlayedThisTurn >= restriction.maxPerTurn
            }

        fun mayPlayHouse(house: House): Boolean =
            restrictions.any { (source, restriction) ->
                restriction is UseRestriction.MayPlayHouse &&
                    restriction.house == house &&
                    activeHouse != house &&
                    source.controllerIndex == player.index &&
                    (player.cardsPlayedThisTurnByHouse[house] ?: 0) < restriction.perTurn
            }

        /**
         * Phase Shift: may play one card of a house other than [cardHouse] this
         * turn. The allowance is consumed by the first such play (plays of the
         * active house or of [cardHouse] itself do not spend it).
         */
        fun mayPlayNonHouse(cardHouse: House): Boolean {
            val notHouse = player.mayPlayNonHouseThisTurn ?: return false
            if (cardHouse == notHouse || isHouseMatch(cardHouse, activeHouse)) return false
            val used = player.cardsPlayedThisTurnByHouse.entries
                .filter { it.key != notHouse && it.key != activeHouse }
                .sumOf { it.value }
            return used < 1
        }

        val playLimitReached = playLimitReached()

        // Tentacus/Customs Office: the opponent's auras charge amber for playing
        // or using artifacts; an unpayable cost suppresses the action entirely.
        fun artifactPlayCost(): Int =
            restrictions.filter { (source, restriction) ->
                restriction is UseRestriction.ArtifactPlayCost && source.controllerIndex != player.index
            }.sumOf { (_, restriction) -> (restriction as UseRestriction.ArtifactPlayCost).amount }

        fun artifactUseCost(): Int =
            restrictions.filter { (source, restriction) ->
                restriction is UseRestriction.ArtifactUseCost && source.controllerIndex != player.index
            }.sumOf { (_, restriction) -> (restriction as UseRestriction.ArtifactUseCost).amount }

        val artifactPlayCharge = artifactPlayCost()
        val artifactUseCharge = artifactUseCost()

        // Play cards from hand
        if (!playLimitReached) {
            player.hand.distinct().forEach { cardId ->
                val card = library.cardData(cardId) ?: return@forEach
                val houseOk = isHouseMatch(card.house, activeHouse) || mayPlayHouse(card.house) || mayPlayNonHouse(card.house)
                val typeDenied = player.deniedCardTypeNextTurn == card.type
                val costTooHigh = card.type == CardType.ARTIFACT && player.amber < artifactPlayCharge
                if (houseOk && !typeDenied && !costTooHigh && canUseCardName(player, cardId) && !blockedByCannotPlay(card.type)) {
                    if (!card.keywords.any { it.startsWith("alpha") } || game.actionsThisPhase == 0) {
                        if (library.scriptFor(cardId).playable(game, player)) {
                            actions.addAll(generatePlayActions(player, cardId, card.type))
                        }
                    }
                }
            }
        }

        // Use creatures
        player.creatures.filter { !it.exhausted || it.stun }.forEach { creature ->
            val script = library.scriptFor(creature.cardId)
            val usableAsActiveHouse = script.selfRestrictions().any { it is SelfRestriction.UsableAsActiveHouse }
            val mayUseAllowance = creature.house == player.mayUseHouseThisTurn || creature.instanceId in player.mayUseCardIdsThisTurn
            if ((isHouseMatch(creature.house, activeHouse) || usableAsActiveHouse || mayUseAllowance) &&
                canUseCardName(player, creature.cardId)
            ) {
                actions.addAll(generateCreatureUses(game, creature, script))
            }
        }
        if (player.cannotFightNextTurn) {
            actions.removeAll { it is UseCreatureAction && it.useType is FightUse }
        }

        // Use artifacts (non-Omni abilities require the artifact's house)
        player.artifacts.filter { !it.exhausted }.forEach { artifact ->
            val mayUseAllowance = artifact.instanceId in player.mayUseCardIdsThisTurn
            val costTooHigh = player.amber < artifactUseCharge
            if ((isHouseMatch(artifact.house, activeHouse) || mayUseAllowance) && !costTooHigh &&
                canUseCardName(player, artifact.cardId)
            ) {
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
            if (canUseCardName(player, artifact.cardId) && player.amber >= artifactUseCharge) {
                library.scriptFor(artifact.cardId).actionAbilities()
                    .filter { it == "Omni" }
                    .forEach { ability ->
                        actions.add(UseArtifactAction(artifact.instanceId, ability))
                    }
            }
        }

        // Skippy Timehog: "cannot use any cards next turn" — plays and discards
        // are still allowed, creature/artifact uses (incl. Omni) are not. Applied
        // after all use generation so it strips every use action.
        if (player.cannotUseCardsNextTurn) {
            actions.removeAll { it is UseCreatureAction || it is UseArtifactAction }
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

    private fun generateCreatureUses(
        game: GameState,
        creature: InPlayCard,
        script: com.aember.analyzer.domain.engine.CardScript
    ): List<Action> {
        val actions = mutableListOf<Action>()
        if (creature.stun) {
            actions.add(UseCreatureAction(creature.instanceId, RemoveStunUse))
            return actions
        }
        val cannotReap = script.selfRestrictions().any { it is SelfRestriction.CannotReap }
        if (!cannotReap) {
            actions.add(UseCreatureAction(creature.instanceId, ReapUse))
        }
        val opponentCreatures = game.opponent().creatures
        if (opponentCreatures.isNotEmpty()) {
            val tauntCreatures = opponentCreatures.filter { it.hasKeyword(Keyword.TAUNT) }
            val ignoresTaunt = script.ignoresTauntAndElusiveWhenAttacking()
            val legalTargets = if (tauntCreatures.isNotEmpty() && !creature.hasKeyword(Keyword.TAUNT) && !ignoresTaunt) {
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
