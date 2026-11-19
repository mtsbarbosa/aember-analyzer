package com.aember.analyzer.domain.engine.action

import com.aember.analyzer.domain.engine.CardLibrary
import com.aember.analyzer.domain.engine.model.*
import com.aember.analyzer.domain.engine.model.Keyword
import com.aember.analyzer.domain.model.CardType
import com.aember.analyzer.domain.model.House
import com.aember.analyzer.domain.engine.IdGenerator

/**
 * Executes a single action and mutates the game state.
 */
class ActionExecutor(
    private val library: CardLibrary,
    private val idGenerator: IdGenerator
) {

    fun execute(game: GameState, action: Action) {
        when (action) {
            is ChooseHouseAction -> chooseHouse(game, action.house)
            is PlayCardAction -> playCard(game, action)
            is UseCreatureAction -> useCreature(game, action)
            is UseArtifactAction -> useArtifact(game, action)
            is DiscardAction -> discard(game, action.cardId)
            is EndTurnAction -> game.log("${game.activePlayer().name} ends their turn")
        }
        stateCheck(game)
    }

    private fun chooseHouse(game: GameState, house: House) {
        val player = game.activePlayer()
        player.activeHouse = house
        game.statistics.housesChosen[house.name] = (game.statistics.housesChosen[house.name] ?: 0) + 1
        game.log("${player.name} chooses $house")
    }

    private fun playCard(game: GameState, action: PlayCardAction) {
        val player = game.activePlayer()
        val cardId = action.cardId
        require(player.hand.remove(cardId)) { "Card $cardId not in hand" }

        val catalog = library.cardData(cardId)
            ?: throw IllegalStateException("Unknown card: $cardId")
        logCardFidelity(game, cardId, "play")

        // Alpha: can only be the first thing played/used/discarded this phase.
        if (catalog.keywords.any { it.startsWith("alpha") } && game.actionsThisPhase > 0) {
            throw IllegalStateException("${catalog.name} has Alpha and cannot be played after other actions this phase")
        }

        val instanceId = idGenerator.nextId(cardId)
        val inPlay = InPlayCard(
            instanceId = instanceId,
            cardId = cardId,
            ownerIndex = player.index,
            controllerIndex = player.index,
            zone = Zone.PLAY_AREA,
            house = catalog.house,
            type = catalog.type,
            exhausted = true,
            power = catalog.power ?: 0,
            armor = catalog.armor ?: 0,
            keywords = parseKeywords(catalog.keywords),
            traits = catalog.traits.map { it.lowercase() }
        )

        // Resolve bonus amber pips before card text.
        if (catalog.amber > 0) {
            game.gainAmber(player, catalog.amber, "${player.name} gains ${catalog.amber} amber from bonus icons")
        }

        when (catalog.type) {
            CardType.CREATURE -> {
                player.creatures.add(inPlay)
                game.log("${player.name} plays ${catalog.name}")
                library.scriptFor(cardId).onPlay(game, player, inPlay)
            }
            CardType.ARTIFACT -> {
                player.artifacts.add(inPlay)
                game.log("${player.name} plays ${catalog.name}")
                library.scriptFor(cardId).onPlay(game, player, inPlay)
            }
            CardType.ACTION -> {
                game.log("${player.name} plays ${catalog.name}")
                library.scriptFor(cardId).onPlay(game, player, inPlay)
                player.discard.add(cardId)
            }
            CardType.UPGRADE -> {
                val targetId = action.targetCreatureId
                    ?: throw IllegalArgumentException("Upgrade requires a target creature")
                val target = findCreature(game, targetId)
                    ?: throw IllegalStateException("Target creature not found")
                inPlay.zone = Zone.UPGRADE
                target.upgrades.add(inPlay)
                game.log("${player.name} upgrades ${target.cardId} with ${catalog.name}")
                library.scriptFor(cardId).onPlay(game, player, inPlay)
            }
            else -> throw IllegalStateException("Cannot play card type ${catalog.type}")
        }

        player.cardNamesPlayedOrUsedThisRound[cardId] = (player.cardNamesPlayedOrUsedThisRound[cardId] ?: 0) + 1
        game.statistics.cardsPlayed++
        game.actionsThisPhase++
        if (catalog.keywords.any { it.startsWith("omega") }) {
            game.endPhaseRightNow = true
            game.log("Omega ends the main phase")
        }
    }

    private fun useCreature(game: GameState, action: UseCreatureAction) {
        val player = game.activePlayer()
        val creature = player.creatures.find { it.instanceId == action.instanceId }
            ?: throw IllegalStateException("Creature not found")
        logCardFidelity(game, creature.cardId, "use-creature")

        when (action.useType) {
            is ReapUse -> {
                library.scriptFor(creature.cardId).onBeforeReap(game, player, creature)
                creature.exhausted = true
                game.gainAmber(player, 1, "${player.name} reaps with ${creature.cardId} for 1 amber")
                library.scriptFor(creature.cardId).onReap(game, player, creature)
                library.scriptFor(creature.cardId).onAfterReap(game, player, creature)
                player.cardNamesPlayedOrUsedThisRound[creature.cardId] = (player.cardNamesPlayedOrUsedThisRound[creature.cardId] ?: 0) + 1
                game.statistics.reaps++
                game.actionsThisPhase++
            }
            is FightUse -> {
                val target = game.opponent().creatures.find { it.instanceId == action.useType.targetInstanceId }
                    ?: throw IllegalStateException("Fight target not found")
                creature.exhausted = true
                fight(game, player, creature, target)
                player.cardNamesPlayedOrUsedThisRound[creature.cardId] = (player.cardNamesPlayedOrUsedThisRound[creature.cardId] ?: 0) + 1
                game.statistics.fights++
                game.actionsThisPhase++
            }
            is ActionAbilityUse -> {
                creature.exhausted = true
                game.log("${player.name} uses ${creature.cardId}'s ${action.useType.abilityId}")
                library.scriptFor(creature.cardId).resolveActionAbility(
                    action.useType.abilityId, game, player, creature
                )
                player.cardNamesPlayedOrUsedThisRound[creature.cardId] = (player.cardNamesPlayedOrUsedThisRound[creature.cardId] ?: 0) + 1
                trackAbilityUse(game, action.useType.abilityId)
                game.actionsThisPhase++
            }
            is RemoveStunUse -> {
                creature.stun = false
                creature.exhausted = true
                game.log("${player.name} removes stun from ${creature.cardId}")
                player.cardNamesPlayedOrUsedThisRound[creature.cardId] = (player.cardNamesPlayedOrUsedThisRound[creature.cardId] ?: 0) + 1
                game.actionsThisPhase++
            }
        }
    }

    private fun useArtifact(game: GameState, action: UseArtifactAction) {
        val player = game.activePlayer()
        val artifact = player.artifacts.find { it.instanceId == action.instanceId }
            ?: throw IllegalStateException("Artifact not found")
        logCardFidelity(game, artifact.cardId, "use-artifact")
        artifact.exhausted = true
        game.log("${player.name} uses ${artifact.cardId}'s ${action.abilityId}")
        library.scriptFor(artifact.cardId).resolveActionAbility(action.abilityId, game, player, artifact)
        player.cardNamesPlayedOrUsedThisRound[artifact.cardId] = (player.cardNamesPlayedOrUsedThisRound[artifact.cardId] ?: 0) + 1
        trackAbilityUse(game, action.abilityId)
        game.actionsThisPhase++
    }

    private fun discard(game: GameState, cardId: String) {
        val player = game.activePlayer()
        require(player.hand.remove(cardId)) { "Card $cardId not in hand" }
        player.discard.add(cardId)
        game.log("${player.name} discards $cardId")
        player.cardNamesPlayedOrUsedThisRound[cardId] = (player.cardNamesPlayedOrUsedThisRound[cardId] ?: 0) + 1
        game.statistics.cardsDiscarded++
        game.actionsThisPhase++
    }

    private fun trackAbilityUse(game: GameState, abilityId: String) {
        when (abilityId) {
            "Omni" -> game.statistics.omniAbilitiesUsed++
            else -> game.statistics.actionAbilitiesUsed++
        }
    }

    private fun fight(
        game: GameState,
        player: PlayerState,
        attacker: InPlayCard,
        defender: InPlayCard
    ) {
        game.log("${player.name} fights with ${attacker.cardId} against ${defender.cardId}")
        attacker.attackedThisTurn = true
        defender.attackedThisTurn = true
        attacker.exhausted = true

        // Before Fight triggers resolve before Assault/Hazardous.
        library.scriptFor(attacker.cardId).onBeforeFight(game, player, attacker, defender)

        // Assault and Hazardous resolve before normal fight damage.
        val assault = attacker.keywordValue(Keyword.ASSAULT)
        if (assault > 0) {
            game.log("Assault $assault hits ${defender.cardId}")
            dealDamage(defender, assault)
        }
        val hazardous = defender.keywordValue(Keyword.HAZARDOUS)
        if (hazardous > 0) {
            game.log("Hazardous $hazardous hits ${attacker.cardId}")
            dealDamage(attacker, hazardous)
        }

        // If a participant was killed by assault/hazardous, cancel the rest of the fight.
        if (attacker.damage >= attacker.power || defender.damage >= defender.power) {
            game.log("Fight cancelled because a participant was destroyed")
            return
        }

        // Elusive: first time this creature is attacked each turn, it is dealt no damage.
        val elusive = defender.hasKeyword(Keyword.ELUSIVE) && !defender.elusiveUsed
        if (elusive) {
            defender.elusiveUsed = true
            game.log("${defender.cardId} uses elusive")
        }

        // Skirmish: attacker takes no damage.
        val skirmish = attacker.hasKeyword(Keyword.SKIRMISH)

        // Normal fight damage. Cards may modify the damage they deal when fighting.
        val attackerDamage = library.scriptFor(attacker.cardId).modifyFightDamage(true, attacker.power)
        val defenderDamage = library.scriptFor(defender.cardId).modifyFightDamage(false, if (skirmish) 0 else defender.power)

        dealDamage(attacker, defenderDamage)
        if (!elusive) {
            dealDamage(defender, attackerDamage)
        }

        library.scriptFor(attacker.cardId).onFight(game, player, attacker, defender)
        library.scriptFor(attacker.cardId).onAfterFight(game, player, attacker, defender)
    }

    private fun dealDamage(card: InPlayCard, amount: Int) {
        if (amount <= 0) return
        if (card.ward) {
            card.ward = false
            return
        }
        val effective = (amount - card.armor).coerceAtLeast(0)
        card.damage += effective
    }

    private fun stateCheck(game: GameState) {
        // Destroy creatures with lethal damage
        game.players.forEach { player ->
            val dead = player.creatures.filter { it.damage >= it.power || it.power <= 0 }
            dead.forEach { destroyCreature(game, player, it) }
        }
    }

    private fun destroyCreature(game: GameState, player: PlayerState, creature: InPlayCard) {
        logCardFidelity(game, creature.cardId, "destroyed")
        library.scriptFor(creature.cardId).onDestroyed(game, player, creature)
        // Move upgrades to discard
        creature.upgrades.forEach { player.discard.add(it.cardId) }
        player.creatures.remove(creature)
        // If a script moved the card (e.g. Bad Penny to hand), do not also discard it.
        val alreadyMoved = player.hand.contains(creature.cardId)
                || player.archives.contains(creature.cardId)
                || player.purged.contains(creature.cardId)
        if (!alreadyMoved) {
            player.discard.add(creature.cardId)
        }
        game.log("${creature.cardId} is destroyed")
    }

    private fun findCreature(game: GameState, instanceId: String): InPlayCard? {
        return game.players.asSequence()
            .flatMap { it.creatures.asSequence() }
            .find { it.instanceId == instanceId }
    }

    private fun logCardFidelity(game: GameState, cardId: String, context: String) {
        val level = library.fidelityFor(cardId)
        game.logFidelity(cardId, level, context)
    }

    private fun parseKeywords(keywords: List<String>): Map<String, Int> {
        return keywords.associate { entry ->
            val parts = entry.split(":")
            val name = parts[0].lowercase()
            val value = parts.getOrNull(1)?.toIntOrNull() ?: 1
            name to value
        }
    }
}
