package com.aember.analyzer.domain.engine.cards

import com.aember.analyzer.domain.engine.dsl.*
import com.aember.analyzer.domain.engine.events.*
import com.aember.analyzer.domain.engine.fidelity.FidelityLevel
import com.aember.analyzer.domain.model.CardType
import com.aember.analyzer.domain.model.House

/** Event-bus reactions and turn-scoped subscriptions (Phase 2.2, batch (b) upgrades, Charge/Strange Gizmo/Brain Eater). */
val dslCardOverridesReactions: List<Pair<String, CardDefinition>> = listOf(
    card("duskrunner") {

        reaction(
            "the host gains 'Reap: steal 1 amber'",
            StealAmber(1),
            matches = { _, _, card, event ->
                event is CreatureReaped && card.hostId == event.instanceId
            }
        )
        fidelity(FidelityLevel.FULL, "Upgrade: the attached creature gains 'Reap: steal 1 amber'")
    
    },
    card("tunk") {

        reaction(
            "after you play another Mars creature, fully heal Tunk",
            Heal(3, SelfCreature, fully = true),
            matches = { _, player, card, event ->
                event is CreatureEnteredPlay && event.house == House.MARS &&
                    event.playerIndex == player.index && event.cardId != card.cardId
            }
        )
        fidelity(FidelityLevel.FULL, "Fully heals Tunk after you play another Mars creature")
    
    },
    card("brain-stem-antenna") {

        reaction(
            "after you play a Mars creature, ready this creature and it belongs to house Mars until end of turn",
            effect = ReadyHost(House.MARS)
        ) { _, player, card, event ->
            val hostId = card.hostId
            event is CardPlayed && event.house == House.MARS && event.type == CardType.CREATURE &&
                hostId != null && player.creatures.any { it.instanceId == hostId } &&
                event.playerIndex == player.index
        }
        fidelity(FidelityLevel.PARTIAL, "Upgrade: after you play a Mars creature the host readies and counts as Mars until end of turn; house counting beyond the turn reset not modeled")
    
    },
    card("carlo-phantom") {

        reaction(
            "each time you play an artifact, steal 1 amber",
            effect = StealAmber(1),
            matches = { _, player, _, event ->
            event is CardPlayed && event.type == CardType.ARTIFACT && event.playerIndex == player.index
            }
        )
        fidelity(FidelityLevel.FULL, "Elusive, skirmish; reaction: each artifact you play steals 1 amber")
    
    },
    card("krump") {

        reaction(
            "after an attacking enemy creature is destroyed fighting Krump, its controller loses 1 amber",
            effect = EventSideLosesAmber(1, EventSide.CONTROLLER, EventSubject.ATTACKER),
            matches = { _, player, card, event ->
                event is FightResolved && event.attackerDestroyed &&
                    event.defenderInstanceId == card.instanceId && event.defenderController == player.index &&
                    event.attackerController != player.index
            }
        )

        reaction(
            "after an enemy creature is destroyed fighting Krump, its controller loses 1 amber",
            effect = EventSideLosesAmber(1, EventSide.CONTROLLER, EventSubject.DEFENDER),
            matches = { _, player, card, event ->
                event is FightResolved && event.defenderDestroyed &&
                    event.attackerCardId == card.cardId && event.attackerController == player.index &&
                    event.defenderController != player.index
            }
        )
        fidelity(FidelityLevel.FULL, "Reaction: enemy creature destroyed fighting Krump -> its controller loses 1 amber")
    
    },
    card("loot-the-bodies") {

        play(ActivateTurnReaction("enemy creature destroyed"))
        reaction(
            "for the remainder of the turn, gain 1 amber each time an enemy creature is destroyed",
            effect = GainAmber(1),
            subscription = true,
            matches = { _, player, _, event ->
            event is CreatureDestroyed && event.controllerIndex != player.index
            }
        )
        fidelity(FidelityLevel.FULL, "Play activates a turn-scoped subscription; gain 1 amber per enemy creature destroyed this turn")
    
    },
    card("looter-goblin") {

        reap(ActivateTurnReaction("enemy creature destroyed"))
        reaction(
            "for the remainder of the turn, gain 1 amber each time an enemy creature is destroyed",
            effect = GainAmber(1),
            subscription = true,
            matches = { _, player, _, event ->
            event is CreatureDestroyed && event.controllerIndex != player.index
            }
        )
        fidelity(FidelityLevel.FULL, "Reap activates a turn-scoped subscription; gain 1 amber per enemy creature destroyed this turn")
    
    },
    card("magda-the-rat") {

        play(StealAmber(2))
        reaction(
            "leaves play: your opponent steals 2 amber",
            effect = StealFrom(2, SelfPlayer),
            matches = { _, _, card, event ->
                event is CardLeftPlay && event.instanceId == card.instanceId
            }
        )
        fidelity(FidelityLevel.FULL, "Play: steal 2 amber; leaves play: your opponent steals 2 amber")
    
    },
    card("pile-of-skulls") {

        reaction(
            "each time an enemy creature is destroyed during your turn, a friendly creature captures 1 amber",
            effect = CaptureAmber(1, FriendlyCreature())
        ) { game, player, _, event ->
            event is CreatureDestroyed && event.controllerIndex != player.index &&
                game.activePlayerIndex == player.index
        }
        fidelity(FidelityLevel.FULL, "Reaction: enemy creature destroyed on your turn -> friendly creature captures 1 amber")
    
    },
    card("soul-snatcher") {

        reaction(
            "each time a creature is destroyed, its owner gains 1 amber",
            effect = EventSideGainsAmber(1, EventSide.OWNER)
        ) { _, _, _, event -> event is CreatureDestroyed }
        fidelity(FidelityLevel.FULL, "Reaction: each destroyed creature's owner gains 1 amber")
    
    },
    card("tolas") {

        reaction(
            "each time a creature is destroyed, its opponent gains 1 amber",
            effect = EventSideGainsAmber(1, EventSide.CONTROLLER_OPPONENT)
        ) { _, _, _, event -> event is CreatureDestroyed }
        fidelity(FidelityLevel.FULL, "Elusive; reaction: each destroyed creature's opponent gains 1 amber")
    
    },
    card("take-hostages") {

        play(ActivateTurnReaction("friendly creature fights"))
        reaction(
            "for the remainder of the turn, each time a friendly creature fights, it captures 1A",
            effect = EventSubjectCaptures(1, EventSubject.ATTACKER),
            subscription = true,
            matches = { _, player, _, event ->
                event is FightResolved && event.attackerController == player.index
            }
        )
        fidelity(FidelityLevel.FULL, "Play: friendly fighters capture 1A for the remainder of the turn")

    },
    card("francus") {

        reaction(
            "after an attacking enemy creature is destroyed fighting Francus, Francus captures 1A",
            effect = EventSubjectCaptures(1, EventSubject.DEFENDER),
            matches = { _, player, card, event ->
                event is FightResolved && event.attackerDestroyed &&
                    event.defenderInstanceId == card.instanceId && event.defenderController == player.index &&
                    event.attackerController != player.index
            }
        )

        reaction(
            "after an enemy creature is destroyed fighting Francus, Francus captures 1A",
            effect = EventSubjectCaptures(1, EventSubject.ATTACKER),
            matches = { _, player, card, event ->
                event is FightResolved && event.defenderDestroyed &&
                    event.attackerCardId == card.cardId && event.attackerController == player.index &&
                    event.defenderController != player.index
            }
        )
        fidelity(FidelityLevel.FULL, "Reaction: after an enemy creature is destroyed fighting Francus, Francus captures 1A")

    },
    card("mugwump") {

        reaction(
            "after an attacking enemy creature is destroyed fighting Mugwump, fully heal Mugwump and give it a +1 power counter",
            effect = Sequence(listOf(Heal(1, SelfCreature, fully = true), AddPowerCounters(1, SelfCreature))),
            matches = { _, player, card, event ->
                event is FightResolved && event.attackerDestroyed &&
                    event.defenderInstanceId == card.instanceId && event.defenderController == player.index &&
                    event.attackerController != player.index
            }
        )

        reaction(
            "after an enemy creature is destroyed fighting Mugwump, fully heal Mugwump and give it a +1 power counter",
            effect = Sequence(listOf(Heal(1, SelfCreature, fully = true), AddPowerCounters(1, SelfCreature))),
            matches = { _, player, card, event ->
                event is FightResolved && event.defenderDestroyed &&
                    event.attackerCardId == card.cardId && event.attackerController == player.index &&
                    event.defenderController != player.index
            }
        )
        fidelity(FidelityLevel.FULL, "Reaction: after an enemy creature is destroyed fighting Mugwump, Mugwump fully heals and gains a +1 power counter")

    },
    card("red-planet-ray-gun") {

        reaction(
            "the host gains 'Reap: deal 1 damage per Mars creature in play'",
            DealDamagePerHouseInPlay(House.MARS, 1, AnyCreature()),
            matches = { _, _, card, event ->
                event is CreatureReaped && card.hostId == event.instanceId
            }
        )
        fidelity(FidelityLevel.FULL, "Upgrade: the attached creature gains 'Reap: deal 1 damage per Mars creature in play'")

    },
    card("silent-dagger") {

        reaction(
            "the host gains 'Reap: deal 4 damage to a flank creature'",
            DealDamage(4, FlankCreature),
            matches = { _, _, card, event ->
                event is CreatureReaped && card.hostId == event.instanceId
            }
        )
        fidelity(FidelityLevel.FULL, "Upgrade: the attached creature gains 'Reap: deal 4 damage to a flank creature'")

    },
    card("brain-eater") {

        reaction(
            "after a creature is destroyed fighting Brain Eater, draw a card",
            Draw(1, SelfPlayer),
            matches = { _, _, card, event ->
                event is FightResolved && (
                    (event.attackerInstanceId == card.instanceId && event.defenderDestroyed) ||
                        (event.defenderInstanceId == card.instanceId && event.attackerDestroyed)
                    )
            }
        )
        fidelity(FidelityLevel.FULL, "After a creature is destroyed fighting Brain Eater, draw a card")

    },
    card("strange-gizmo") {

        reaction(
            "after you forge a key, destroy each creature and artifact",
            DestroyAllCreaturesAndArtifacts,
            matches = { _, player, _, event ->
                event is KeyForged && event.playerIndex == player.index
            }
        )
        fidelity(FidelityLevel.FULL, "After you forge a key, destroy each creature and artifact")

    },
    card("charge") {

        play(ActivateTurnReaction("creatures you play deal 2 damage"))
        reaction(
            "for the remainder of the turn, each creature you play deals 2 damage to an enemy creature",
            effect = DealDamage(2, EnemyCreature()),
            subscription = true,
            matches = { _, player, _, event ->
                event is CreatureEnteredPlay && event.playerIndex == player.index
            }
        )
        fidelity(FidelityLevel.FULL, "Play: for the remainder of the turn, each creature you play gains 'Play: deal 2 damage to an enemy creature'")

    },

    // ---- Phase 3 batch (e) ----

    card("shaffles") {

        reaction(
            "at the end of your turn, your opponent loses 1 amber",
            LoseAmberOpponent(1),
            matches = { _, player, _, event ->
                event is TurnEnded && event.playerIndex == player.index
            }
        )
        fidelity(FidelityLevel.FULL, "At the end of your turn, your opponent loses 1 amber")

    },
    card("rogue-ogre") {

        reaction(
            "at the end of your turn, if you played exactly one card this turn, heal 2 and capture 1",
            effect = Sequence(listOf(Heal(2, SelfCreature), CaptureAmber(1, SelfCreature))),
            matches = { _, player, _, event ->
                event is TurnEnded && event.playerIndex == player.index && player.cardsPlayedThisTurn == 1
            }
        )
        fidelity(FidelityLevel.FULL, "End of your turn with exactly one card played: heal 2 damage and capture 1 amber")

    },
    card("jehu-the-bureaucrat") {

        reaction(
            "after you choose Sanctum as your active house, gain 2 amber",
            GainAmber(2),
            matches = { _, player, _, event ->
                event is HouseChosen && event.playerIndex == player.index && event.house == House.SANCTUM
            }
        )
        fidelity(FidelityLevel.FULL, "After you choose Sanctum as your active house, gain 2 amber")

    },
    card("hayyel-the-merchant") {

        reaction(
            "each time you play an artifact, gain 1 amber",
            GainAmber(1),
            matches = { _, player, _, event ->
                event is CardPlayed && event.playerIndex == player.index && event.type == CardType.ARTIFACT
            }
        )
        fidelity(FidelityLevel.FULL, "Each time you play an artifact, gain 1 amber")

    },
    card("teliga") {

        reaction(
            "each time your opponent plays a creature, gain 1 amber",
            GainAmber(1),
            matches = { _, player, _, event ->
                event is CreatureEnteredPlay && event.playerIndex != player.index
            }
        )
        fidelity(FidelityLevel.FULL, "Each time your opponent plays a creature, gain 1 amber")

    },
    card("bilgum-avalanche") {

        reaction(
            "after you forge a key, deal 2 damage to each enemy creature",
            DealDamage(2, EnemyCreature(Scope.ALL)),
            matches = { _, player, _, event ->
                event is KeyForged && event.playerIndex == player.index
            }
        )
        fidelity(FidelityLevel.FULL, "After you forge a key, deal 2 damage to each enemy creature")

    },
    card("autocannon") {

        reaction(
            "deal 1 damage to each creature after it enters play",
            DealDamageToEventCreature(1),
            matches = { _, _, _, event -> event is CreatureEnteredPlay }
        )
        fidelity(FidelityLevel.FULL, "Deal 1 damage to each creature after it enters play")

    },
    card("pingle-who-annoys") {

        reaction(
            "deal 1 damage to each enemy creature after it enters play",
            DealDamageToEventCreature(1),
            matches = { _, player, _, event ->
                event is CreatureEnteredPlay && event.playerIndex != player.index
            }
        )
        fidelity(FidelityLevel.FULL, "Elusive; deal 1 damage to each enemy creature after it enters play")

    },
    card("overlord-greking") {

        reaction(
            "after an enemy creature is destroyed fighting Overlord Greking, put it into play under your control",
            ClaimDestroyedFightTarget(0),
            matches = { _, _, card, event ->
                event is FightResolved && (
                    (event.attackerInstanceId == card.instanceId && event.defenderDestroyed) ||
                        (event.defenderInstanceId == card.instanceId && event.attackerDestroyed)
                    )
            }
        )
        fidelity(FidelityLevel.FULL, "After an enemy creature is destroyed fighting Overlord Greking, put it into play under your control")

    },
    card("stealer-of-souls") {

        reaction(
            "after an enemy creature is destroyed fighting Stealer of Souls, purge it and gain 1 amber",
            ClaimDestroyedFightTarget(gainAmber = 1),
            matches = { _, _, card, event ->
                event is FightResolved && (
                    (event.attackerInstanceId == card.instanceId && event.defenderDestroyed) ||
                        (event.defenderInstanceId == card.instanceId && event.attackerDestroyed)
                    )
            }
        )
        fidelity(FidelityLevel.FULL, "After an enemy creature is destroyed fighting Stealer of Souls, purge it and gain 1 amber")

    },
    card("rocket-boots") {

        reaction(
            "the host readies when used for the first time in a turn (reap)",
            Ready(SelfCreature),
            matches = { g, _, card, event ->
                event is CreatureReaped && card.hostId == event.instanceId &&
                    g.players.asSequence().flatMap { it.creatures.asSequence() }
                        .find { it.instanceId == event.instanceId }?.usedThisTurn == 1
            }
        )
        reaction(
            "the host readies when used for the first time in a turn (fight)",
            Ready(SelfCreature),
            matches = { g, _, card, event ->
                event is FightResolved && card.hostId == event.attackerInstanceId &&
                    g.players.asSequence().flatMap { it.creatures.asSequence() }
                        .find { it.instanceId == event.attackerInstanceId }?.usedThisTurn == 1
            }
        )
        fidelity(FidelityLevel.FULL, "Upgrade: the host gains Fight/Reap: if this is the first time it was used this turn, ready it")

    },

    card("hunting-witch") {

        reaction(
            "each time you play another creature, gain 1 amber",
            GainAmber(1),
            matches = { _, player, card, event ->
                event is CreatureEnteredPlay && event.playerIndex == player.index && event.instanceId != card.instanceId
            }
        )
        fidelity(FidelityLevel.FULL, "Each time you play another creature, gain 1 amber")

    },
    card("rock-hurling-giant") {

        reaction(
            "each time you discard a Brobnar card from your hand, deal 4 damage to a creature",
            DealDamage(4, AnyCreature()),
            matches = { _, player, _, event ->
                event is CardDiscarded && event.playerIndex == player.index && event.house == House.BROBNAR
            }
        )
        fidelity(FidelityLevel.FULL, "During your turn, each time you discard a Brobnar card from your hand, deal 4 damage to a creature")

    },

)
