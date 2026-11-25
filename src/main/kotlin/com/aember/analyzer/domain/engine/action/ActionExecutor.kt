package com.aember.analyzer.domain.engine.action

import com.aember.analyzer.domain.engine.CardLibrary
import com.aember.analyzer.domain.engine.dsl.TurnTrigger
import com.aember.analyzer.domain.engine.effects.EffectRegistry
import com.aember.analyzer.domain.engine.effects.SelfRestriction
import com.aember.analyzer.domain.engine.events.CardLeftPlay
import com.aember.analyzer.domain.engine.events.CardPlayed
import com.aember.analyzer.domain.engine.events.CreatureDestroyed
import com.aember.analyzer.domain.engine.events.CreatureEnteredPlay
import com.aember.analyzer.domain.engine.events.CreatureReaped
import com.aember.analyzer.domain.engine.events.ArtifactUsed
import com.aember.analyzer.domain.engine.events.CardDiscarded
import com.aember.analyzer.domain.engine.events.FightDeclared
import com.aember.analyzer.domain.engine.events.HouseChosen
import com.aember.analyzer.domain.engine.events.EventBus
import com.aember.analyzer.domain.engine.events.FightResolved
import com.aember.analyzer.domain.engine.effects.UseRestriction
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
) : com.aember.analyzer.domain.engine.dsl.CardPlayBridge {

    /** Masterplan/Chaos Portal: play a card that was just moved into the player's hand. */
    override fun playCardFromHand(game: GameState, player: PlayerState, cardId: String, targetCreatureId: String?) {
        playCard(game, PlayCardAction(cardId, targetCreatureId = targetCreatureId))
    }

    /** Scout: fight through the real fight resolution (keywords, triggers, events). */
    override fun fightWith(
        game: GameState,
        player: PlayerState,
        attackerInstanceId: String,
        targetInstanceId: String
    ): Boolean {
        val target = game.opponent(player).creatures.find { it.instanceId == targetInstanceId } ?: return false
        execute(game, UseCreatureAction(attackerInstanceId, FightUse(target.instanceId)))
        return true
    }


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
        player.deniedHouseNextTurn = null
        game.log("${player.name} chooses $house")
        player.statistics.housesChosen[house.name] = (player.statistics.housesChosen[house.name] ?: 0) + 1
        EventBus.emit(library, game, HouseChosen(player.index, house))
    }

    private fun playCard(game: GameState, action: PlayCardAction) {
        val player = game.activePlayer()
        val cardId = action.cardId
        require(player.hand.remove(cardId)) { "Card $cardId not in hand" }

        val catalog = library.cardData(cardId)
            ?: throw IllegalStateException("Unknown card: $cardId")
        logCardFidelity(game, cardId, "play")

        // Customs Office: playing an artifact costs amber paid to the aura's controller.
        if (catalog.type == CardType.ARTIFACT) {
            val charge = artifactCostCharge(game, player, play = true)
            require(player.amber >= charge) { "Cannot pay artifact play cost ($charge amber)" }
            payArtifactCost(game, player, charge)
        }

        // Alpha: can only be the first thing played/used/discarded this phase.
        if (catalog.keywords.any { it.startsWith("alpha") } && game.actionsThisPhase > 0) {
            throw IllegalStateException("${catalog.name} has Alpha and cannot be played after other actions this phase")
        }

        val instanceId = idGenerator.nextId(cardId)
        // Creatures enter play exhausted unless a card says otherwise
        // (Speed Sigil readies the first creature played each turn, by either
        // player; Silvertooth readies itself). Artifacts also enter exhausted.
        val isFirstCreatureThisTurn = player.creaturesPlayedThisTurn == 0
        val globalEntersReady = catalog.type == CardType.CREATURE && isFirstCreatureThisTurn &&
            game.players.any { p ->
                p.cardsInPlay().any { library.scriptFor(it.cardId).readiesFirstCreatureEachTurn() }
            }
        val selfEntersReady = catalog.type == CardType.CREATURE && library.scriptFor(cardId).cardEntersReady()
        var entersReady = selfEntersReady || globalEntersReady
        // Blypyp: the next creature of the marked house enters play ready.
        val houseEntersReady = catalog.type == CardType.CREATURE && player.nextCreatureOfHouseEntersReady == catalog.house
        if (houseEntersReady) {
            player.nextCreatureOfHouseEntersReady = null
        }
        entersReady = entersReady || houseEntersReady
        // Soft Landing: the next creature/artifact this player plays enters ready.
        if (!entersReady && player.nextPlaysEnterReady > 0 &&
            (catalog.type == CardType.CREATURE || catalog.type == CardType.ARTIFACT)
        ) {
            entersReady = true
            player.nextPlaysEnterReady--
        }
        val entersStunned = catalog.type == CardType.CREATURE && library.scriptFor(cardId).cardEntersStunned()
        val inPlay = InPlayCard(
            instanceId = instanceId,
            cardId = cardId,
            ownerIndex = player.index,
            controllerIndex = player.index,
            zone = Zone.PLAY_AREA,
            house = catalog.house,
            type = catalog.type,
            exhausted = !entersReady,
            power = catalog.power ?: 0,
            armor = catalog.armor ?: 0,
            keywords = parseKeywords(catalog.keywords),
            traits = catalog.traits.map { it.lowercase() },
            stun = entersStunned
        )

        // Resolve bonus amber pips before card text.
        if (catalog.amber > 0) {
            game.gainAmber(player, catalog.amber, "${player.name} gains ${catalog.amber} amber from bonus icons")
        }

        when (catalog.type) {
            CardType.CREATURE -> {
                val insertIndex = action.placementIndex.coerceIn(0, player.creatures.size)
                player.creatures.add(insertIndex, inPlay)
                player.creaturesPlayedThisTurn++
                when {
                    entersStunned -> game.log("${player.name} plays ${catalog.name} (enters stunned)")
                    entersReady -> game.log("${player.name} plays ${catalog.name} (enters ready)")
                    else -> game.log("${player.name} plays ${catalog.name}")
                }
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
                inPlay.hostId = target.instanceId
                target.upgrades.add(inPlay)
                game.log("${player.name} upgrades ${target.cardId} with ${catalog.name}")
                library.scriptFor(cardId).onPlay(game, player, inPlay)
            }
            else -> throw IllegalStateException("Cannot play card type ${catalog.type}")
        }

        player.cardNamesPlayedOrUsedThisRound[cardId] = (player.cardNamesPlayedOrUsedThisRound[cardId] ?: 0) + 1
        player.cardsPlayedThisTurnByHouse[catalog.house] = (player.cardsPlayedThisTurnByHouse[catalog.house] ?: 0) + 1
        player.statistics.cardsPlayed++
        game.actionsThisPhase++

        // Library Access-style effect: draw when playing another card.
        if (player.drawOnCardPlayActive) {
            val drawHouse = player.drawOnCardPlayHouse
            if (drawHouse == null || drawHouse == catalog.house) {
                drawCards(game, player, 1)
                game.log("${player.name} draws 1 card from effect")
            }
        }

        if (catalog.keywords.any { it.startsWith("omega") }) {
            game.endPhaseRightNow = true
            game.log("Omega ends the main phase")
        }

        // Reactions ("After you play ...", "Each time a creature enters play ...").
        // CreatureEnteredPlay only fires for creatures (keyteki's onCardPlay creature check).
        if (catalog.type == CardType.CREATURE) {
            EventBus.emit(library, game, CreatureEnteredPlay(player.index, instanceId, cardId, catalog.house))
        }
        EventBus.emit(library, game, CardPlayed(player.index, cardId, catalog.type, catalog.house))
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
                creature.usedThisTurn++
                // Dimension Door: reap amber is taken from the opponent instead of the supply.
                if (player.reapAmberStolenFromOpponent) {
                    val opponent = game.opponent()
                    val stolen = minOf(1, opponent.amber)
                    opponent.amber -= stolen
                    player.amber += stolen
                    game.log(
                        "${player.name} reaps with ${creature.cardId}, taking the amber from ${opponent.name} " +
                            "(${player.name}: ${player.amber - stolen} -> ${player.amber}, ${opponent.name} -> ${opponent.amber})"
                    )
                } else {
                    game.gainAmber(player, 1, "${player.name} reaps with ${creature.cardId} for 1 amber")
                }
                library.scriptFor(creature.cardId).onReap(game, player, creature)
                library.scriptFor(creature.cardId).onAfterReap(game, player, creature)
                EventBus.emit(library, game, CreatureReaped(player.index, creature.instanceId, creature.cardId))
                player.cardNamesPlayedOrUsedThisRound[creature.cardId] = (player.cardNamesPlayedOrUsedThisRound[creature.cardId] ?: 0) + 1
                player.statistics.reaps++
                game.actionsThisPhase++
            }
            is FightUse -> {
                val target = game.opponent().creatures.find { it.instanceId == action.useType.targetInstanceId }
                    ?: throw IllegalStateException("Fight target not found")
                creature.exhausted = true
                creature.usedThisTurn++
                fight(game, player, creature, target)
                player.cardNamesPlayedOrUsedThisRound[creature.cardId] = (player.cardNamesPlayedOrUsedThisRound[creature.cardId] ?: 0) + 1
                player.statistics.fights++
                game.actionsThisPhase++
            }
            is ActionAbilityUse -> {
                creature.exhausted = true
                creature.usedThisTurn++
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
                creature.usedThisTurn++
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
        // Tentacus: using an artifact costs amber paid to the aura's controller
        // (both Action abilities and Omni, per keyteki's triggered-action check).
        val charge = artifactCostCharge(game, player, play = false)
        require(player.amber >= charge) { "Cannot pay artifact use cost ($charge amber)" }
        payArtifactCost(game, player, charge)
        artifact.exhausted = true
        game.log("${player.name} uses ${artifact.cardId}'s ${action.abilityId}")
        library.scriptFor(artifact.cardId).resolveActionAbility(action.abilityId, game, player, artifact)
        player.cardNamesPlayedOrUsedThisRound[artifact.cardId] = (player.cardNamesPlayedOrUsedThisRound[artifact.cardId] ?: 0) + 1
        trackAbilityUse(game, action.abilityId)
        game.actionsThisPhase++
        EventBus.emit(library, game, ArtifactUsed(player.index, artifact.instanceId, artifact.cardId, action.abilityId))
    }

    /** Sum of ArtifactPlayCost/ArtifactUseCost auras projected by the opponents of `player`. */
    private fun artifactCostCharge(game: GameState, player: PlayerState, play: Boolean): Int =
        EffectRegistry.collectUseRestrictions(library, game)
            .filter { (source, restriction) ->
                (if (play) restriction is UseRestriction.ArtifactPlayCost else restriction is UseRestriction.ArtifactUseCost) &&
                    source.controllerIndex != player.index
            }
            .sumOf { (_, restriction) ->
                when (restriction) {
                    is UseRestriction.ArtifactPlayCost -> restriction.amount
                    is UseRestriction.ArtifactUseCost -> restriction.amount
                    else -> 0
                }
            }

    /** Transfers a charged artifact cost to the aura owners (evenly, remainder to the first). */
    private fun payArtifactCost(game: GameState, player: PlayerState, charge: Int) {
        if (charge <= 0) return
        val owners = EffectRegistry.collectUseRestrictions(library, game)
            .filter { (source, restriction) ->
                (restriction is UseRestriction.ArtifactPlayCost || restriction is UseRestriction.ArtifactUseCost) &&
                    source.controllerIndex != player.index
            }
            .map { it.first.controllerIndex }
            .distinct()
        if (owners.isEmpty()) return
        val before = player.amber
        player.amber -= charge
        val per = charge / owners.size
        val remainder = charge % owners.size
        owners.forEachIndexed { index, ownerIndex ->
            game.gainAmber(game.player(ownerIndex), per + if (index == 0) remainder else 0, "${game.player(ownerIndex).name} gains amber from artifact taxes")
        }
        game.log("${player.name} pays $charge amber for an artifact (${player.name}: $before -> ${player.amber})")
    }

    private fun discard(game: GameState, cardId: String) {
        val player = game.activePlayer()
        require(player.hand.remove(cardId)) { "Card $cardId not in hand" }
        player.discard.add(cardId)
        game.log("${player.name} discards $cardId")
        player.cardNamesPlayedOrUsedThisRound[cardId] = (player.cardNamesPlayedOrUsedThisRound[cardId] ?: 0) + 1
        player.statistics.cardsDiscarded++
        game.actionsThisPhase++
        val catalog = library.cardData(cardId)
        if (catalog != null) {
            EventBus.emit(library, game, CardDiscarded(player.index, cardId, catalog.house))
        }
    }

    private fun trackAbilityUse(game: GameState, abilityId: String) {
        val stats = game.activePlayer().statistics
        when (abilityId) {
            "Omni" -> stats.omniAbilitiesUsed++
            else -> stats.actionAbilitiesUsed++
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

        // Evasion Sigil reacts to the fight being declared and may cancel it.
        EventBus.emit(
            library, game,
            FightDeclared(
                attackerInstanceId = attacker.instanceId,
                attackerCardId = attacker.cardId,
                defenderInstanceId = defender.instanceId,
                defenderCardId = defender.cardId,
                attackerController = attacker.controllerIndex
            )
        )
        if (game.cancelCurrentFight) {
            game.log("The fight is cancelled (Evasion Sigil)")
            game.cancelCurrentFight = false
            return
        }
        // Before Fight triggers resolve before Assault/Hazardous. The fight
        // target is exposed for Before Fight effects (Lord Golgotha, Zorg).
        game.currentFightTargetId = defender.instanceId
        library.scriptFor(attacker.cardId).onBeforeFight(game, player, attacker, defender)
        game.currentFightTargetId = null

        // Assault and Hazardous resolve before normal fight damage.
        val assault = attacker.keywordValue(Keyword.ASSAULT)
        if (assault > 0) {
            game.log("Assault $assault hits ${defender.cardId}")
            dealDamage(game, defender, assault)
        }
        val hazardous = defender.keywordValue(Keyword.HAZARDOUS)
        if (hazardous > 0) {
            game.log("Hazardous $hazardous hits ${attacker.cardId}")
            dealDamage(game, attacker, hazardous)
        }

        // If a participant was killed by assault/hazardous, cancel the rest of the fight.
        if (attacker.damage >= attacker.power || defender.damage >= defender.power) {
            game.log("Fight cancelled because a participant was destroyed")
            return
        }

        // Elusive: first time this creature is attacked each turn, it is dealt no damage.
        // Niffle Ape-style attackers ignore elusive.
        val elusive = defender.hasKeyword(Keyword.ELUSIVE) && !defender.elusiveUsed &&
            !library.scriptFor(attacker.cardId).ignoresTauntAndElusiveWhenAttacking()
        if (elusive) {
            defender.elusiveUsed = true
            game.log("${defender.cardId} uses elusive")
        }

        // Skirmish: attacker takes no damage.
        val skirmish = attacker.hasKeyword(Keyword.SKIRMISH)

        // Normal fight damage. Cards may modify the damage they deal when fighting.
        val attackerDamage = library.scriptFor(attacker.cardId).modifyFightDamage(true, attacker.power, game, defender)
        val defenderDamage = library.scriptFor(defender.cardId).modifyFightDamage(false, if (skirmish) 0 else defender.power, game, attacker)

        dealDamage(game, attacker, defenderDamage)
        if (!elusive) {
            // Gabos Longarms: the attacker's damage may be redirected to another creature.
            val redirect = game.fightDamageRedirectId?.let { id ->
                game.players.asSequence().flatMap { it.creatures.asSequence() }.find { it.instanceId == id }
            }
            game.fightDamageRedirectId = null
            if (redirect != null) {
                dealDamage(game, redirect, attackerDamage)
                game.log("$attackerDamage damage goes to ${redirect.cardId} instead of ${defender.cardId}")
            } else {
                dealDamage(game, defender, attackerDamage)
            }
        }

        // Poison: any damage dealt by this creature's power during a fight destroys the
        // damaged creature, even if it survives the damage itself.
        if (!elusive && attackerDamage > 0 && attacker.hasKeyword(Keyword.POISON)) {
            game.log("${defender.cardId} is destroyed by ${attacker.cardId}'s poison")
            defender.damage = Int.MAX_VALUE
        }
        if (defenderDamage > 0 && defender.hasKeyword(Keyword.POISON)) {
            game.log("${attacker.cardId} is destroyed by ${defender.cardId}'s poison")
            attacker.damage = Int.MAX_VALUE
        }

        // A creature destroyed during the fight does not resolve its own Fight / After Fight
        // triggers (keyteki: destroyed fighting creatures are moribund).
        val attackerDestroyed = attacker.damage >= attacker.power
        if (!attackerDestroyed) {
            library.scriptFor(attacker.cardId).onFight(game, player, attacker, defender)
            library.scriptFor(attacker.cardId).onAfterFight(game, player, attacker, defender)
        }

        EventBus.emit(
            library, game,
            FightResolved(
                attackerInstanceId = attacker.instanceId,
                attackerCardId = attacker.cardId,
                attackerController = attacker.controllerIndex,
                defenderInstanceId = defender.instanceId,
                defenderCardId = defender.cardId,
                defenderController = defender.controllerIndex,
                defenderDestroyed = defender.damage >= defender.power,
                attackerDestroyed = attackerDestroyed
            )
        )

        val amberPerFight = player.turnTriggerAmber[TurnTrigger.FRIENDLY_CREATURE_FIGHTS] ?: 0
        if (amberPerFight > 0) {
            game.gainAmber(player, amberPerFight, "${player.name} gains $amberPerFight amber from friendly creature fighting")
        }
    }

    private fun dealDamage(game: GameState, card: InPlayCard, amount: Int) {
        if (amount <= 0) return
        val actualTarget = resolveDamageTarget(game, card)
        if (actualTarget.immuneToDamageThisTurn) {
            game.log("${actualTarget.cardId} cannot be dealt damage this turn")
            return
        }
        if (actualTarget.ward) {
            actualTarget.ward = false
            game.log("${actualTarget.cardId}'s ward prevents $amount damage")
            return
        }
        val effective = (amount - actualTarget.armor).coerceAtLeast(0)
        actualTarget.damage += effective
        if (actualTarget.instanceId != card.instanceId) {
            game.log("$amount damage redirected from ${card.cardId} to ${actualTarget.cardId}")
        }
    }

    private fun resolveDamageTarget(game: GameState, card: InPlayCard): InPlayCard {
        if (card.traits.contains("specter")) return card
        val controller = game.player(card.controllerIndex)
        val idx = controller.creatures.indexOf(card)
        if (idx == -1) return card
        val left = controller.creatures.getOrNull(idx - 1)
        val right = controller.creatures.getOrNull(idx + 1)
        val redirector = listOfNotNull(left, right).find { library.scriptFor(it.cardId).redirectsNeighborDamage() }
        return redirector ?: card
    }

    private fun stateCheck(game: GameState) {
        // Recompute static-effect auras and destroy lethal-damaged creatures until stable.
        // Destroying a creature can invalidate auras (e.g. removing a power buff kills
        // damaged neighbors), so re-evaluate after every removal.
        while (true) {
            EffectRegistry.recalculate(library, game)
            val dead = game.players.flatMap { player ->
                player.creatures.filter { it.damage >= it.power || it.power <= 0 }.map { player to it }
            }.toMutableList()
            // Self-destruct conditions (Phase 2.3, e.g. Tireless Crocag with no enemy creatures).
            game.players.forEach { player ->
                player.creatures.forEach { creature ->
                    library.scriptFor(creature.cardId).selfRestrictions().forEach { restriction ->
                        if (restriction is SelfRestriction.SelfDestruct &&
                            restriction.condition(game, player, creature) &&
                            dead.none { it.second.instanceId == creature.instanceId }
                        ) {
                            dead += player to creature
                        }
                    }
                }
            }
            if (dead.isEmpty()) return
            dead.forEach { (player, creature) -> destroyCreature(game, player, creature) }
        }
    }

    private fun destroyCreature(game: GameState, player: PlayerState, creature: InPlayCard) {
        logCardFidelity(game, creature.cardId, "destroyed")
        // Upgrade destruction replacements (Armageddon Cloak, Phoenix Heart).
        for (upgrade in creature.upgrades.toList()) {
            if (library.scriptFor(upgrade.cardId).onHostWouldBeDestroyed(game, player, upgrade, creature)) {
                creature.upgrades.remove(upgrade)
                player.discard.add(upgrade.cardId)
                emitLeavePlayForUpgrade(game, player, upgrade)
                return
            }
        }
        val archivesInstead = creature.upgrades.any { library.scriptFor(it.cardId).archivesAttachedCreatureOnDestroyed() }
        if (archivesInstead) {
            // The creature is archived; upgrades fall off and are discarded.
            creature.upgrades.forEach { upgrade ->
                player.discard.add(upgrade.cardId)
                game.log("${upgrade.cardId} goes to ${player.name}'s discard")
                emitLeavePlayForUpgrade(game, player, upgrade)
            }
            player.creatures.remove(creature)
            player.archives.add(creature.cardId)
            game.log("${creature.cardId} goes to ${player.name}'s archives instead of being destroyed")
            // Destruction was replaced: it left play but was not destroyed.
            emitLeavePlay(game, creature, destroyed = false)
            return
        }
        library.scriptFor(creature.cardId).onDestroyed(game, player, creature)
        // Move upgrades to discard
        creature.upgrades.forEach { upgrade ->
            player.discard.add(upgrade.cardId)
            emitLeavePlayForUpgrade(game, player, upgrade)
        }
        player.creatures.remove(creature)
        // If a script moved the card (e.g. Bad Penny to hand, Dextre to deck), do not also discard it.
        val alreadyMoved = player.hand.contains(creature.cardId)
                || player.archives.contains(creature.cardId)
                || player.purged.contains(creature.cardId)
                || player.library.contains(creature.cardId)
        if (!alreadyMoved) {
            // Annihilation Ritual: creatures leaving play are purged instead of discarded.
            val purgedInstead = game.players.any { p ->
                p.cardsInPlay().any { library.scriptFor(it.cardId).purgesDestroyedCreatures() }
            }
            if (purgedInstead) {
                player.purged.add(creature.cardId)
                game.log("${creature.cardId} is purged instead of discarded")
            } else {
                player.discard.add(creature.cardId)
            }
        }
        game.log("${creature.cardId} is destroyed")
        emitLeavePlay(game, creature)
    }

    /** Upgrade left play with (or instead of) its host: let the upgrade react (Collar of Subordination). */
    private fun emitLeavePlayForUpgrade(game: GameState, player: PlayerState, upgrade: InPlayCard) {
        val event = CardLeftPlay(upgrade.instanceId, upgrade.cardId, upgrade.ownerIndex, upgrade.controllerIndex)
        EventBus.deliver(library, game, player, upgrade, event)
        EventBus.emit(library, game, event)
    }

    /** Destroyed/removal reactions fire after the card left play, so it cannot react to itself. */
    private fun emitLeavePlay(game: GameState, creature: InPlayCard, destroyed: Boolean = true) {
        // "Leaves Play:" reactions owned by the leaving card itself (e.g. Magda the Rat)
        // fire even though the card is already gone from play.
        EventBus.deliver(
            library, game, game.player(creature.controllerIndex), creature,
            CardLeftPlay(creature.instanceId, creature.cardId, creature.ownerIndex, creature.controllerIndex)
        )
        if (destroyed) EventBus.emit(
            library, game,
            CreatureDestroyed(
                instanceId = creature.instanceId,
                cardId = creature.cardId,
                controllerIndex = creature.controllerIndex,
                ownerIndex = creature.ownerIndex
            )
        )
        EventBus.emit(
            library, game,
            CardLeftPlay(
                instanceId = creature.instanceId,
                cardId = creature.cardId,
                ownerIndex = creature.ownerIndex,
                controllerIndex = creature.controllerIndex
            )
        )
    }

    private fun findCreature(game: GameState, instanceId: String): InPlayCard? {
        return game.players.asSequence()
            .flatMap { it.creatures.asSequence() }
            .find { it.instanceId == instanceId }
    }

    private fun drawCards(game: GameState, player: PlayerState, amount: Int) {
        repeat(amount) {
            if (player.library.isEmpty()) {
                if (player.discard.isEmpty()) return
                player.library.addAll(player.discard)
                player.discard.clear()
                player.library.shuffle(game.rng)
            }
            if (player.library.isNotEmpty()) {
                player.hand.add(player.library.removeFirst())
            }
        }
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
