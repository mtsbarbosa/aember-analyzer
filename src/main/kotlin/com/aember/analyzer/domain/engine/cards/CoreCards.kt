package com.aember.analyzer.domain.engine.cards

import com.aember.analyzer.domain.engine.CardScript
import com.aember.analyzer.domain.engine.dsl.*
import com.aember.analyzer.domain.engine.events.*
import com.aember.analyzer.domain.engine.effects.EnemyOfHouseScope
import com.aember.analyzer.domain.engine.effects.FriendlyCreaturesScope
import com.aember.analyzer.domain.engine.effects.FriendlyFlankCreaturesScope
import com.aember.analyzer.domain.engine.effects.FriendlyOfHouseScope
import com.aember.analyzer.domain.engine.effects.FriendlyWithTraitScope
import com.aember.analyzer.domain.engine.effects.HostCreatureScope
import com.aember.analyzer.domain.engine.effects.NeighborsOfSelfScope
import com.aember.analyzer.domain.engine.effects.OtherFriendlyCreaturesScope
import com.aember.analyzer.domain.engine.effects.SelfScope
import com.aember.analyzer.domain.engine.effects.PayerScope
import com.aember.analyzer.domain.engine.effects.StaticEffect
import com.aember.analyzer.domain.engine.effects.StaticEffectKind
import com.aember.analyzer.domain.engine.fidelity.CardFidelity
import com.aember.analyzer.domain.engine.fidelity.FidelityLevel
import com.aember.analyzer.domain.engine.model.GameState
import com.aember.analyzer.domain.engine.model.InPlayCard
import com.aember.analyzer.domain.model.CardType
import com.aember.analyzer.domain.engine.model.Keyword
import com.aember.analyzer.domain.engine.model.PlayerState
import com.aember.analyzer.domain.engine.model.randomWith
import com.aember.analyzer.domain.model.House

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
        val enemy = game.opponent().creatures.randomWith(game.rng) ?: return
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

    override fun readiesFirstCreatureEachTurn(): Boolean = true
}

object ShadowSelf : CardScript {
    override fun fidelity() = CardFidelity(
        level = FidelityLevel.FULL,
        notes = "Deals no damage when fighting; damage to non-Specter neighbors is redirected here"
    )

    override fun modifyFightDamage(isAttacker: Boolean, baseDamage: Int): Int =
        if (isAttacker) 0 else baseDamage

    override fun redirectsNeighborDamage(): Boolean = true

    override fun protectsNeighbors(): Boolean = true
}

object ControlTheWeak : CardScript {
    override fun fidelity() = CardFidelity(
        level = FidelityLevel.FULL,
        notes = "Names the opponent's strongest house; opponent cannot choose it next turn"
    )

    override fun onPlay(game: GameState, player: PlayerState, card: InPlayCard) {
        val opponent = game.opponent()
        val housePriority = opponent.deck.houses.distinct().sortedByDescending { house ->
            opponent.creatures.count { it.house == house }
        }
        val chosen = housePriority.firstOrNull() ?: opponent.deck.houses.firstOrNull() ?: House.DIS
        opponent.deniedHouseNextTurn = chosen
        game.log("${player.name} names ${chosen.name} with Control the Weak; ${opponent.name} cannot choose it next turn")
    }
}

object Miasma : CardScript {
    override fun fidelity() = CardFidelity(
        level = FidelityLevel.FULL,
        notes = "Opponent skips the forge-a-key step on their next turn"
    )

    override fun onPlay(game: GameState, player: PlayerState, card: InPlayCard) {
        val opponent = game.opponent()
        opponent.skipsForgeNextTurn = true
        game.log("${player.name} plays Miasma; ${opponent.name} cannot forge a key next turn")
    }
}

object TendrilsOfPain : CardScript {
    override fun fidelity() = CardFidelity(
        level = FidelityLevel.FULL,
        notes = "Deals 1 damage to all creatures, plus 3 more if opponent forged a key last turn"
    )

    override fun onPlay(game: GameState, player: PlayerState, card: InPlayCard) {
        val allCreatures = game.players.flatMap { it.creatures }
        val extra = if (game.opponent().forgedKeyLastTurn) 3 else 0
        val total = 1 + extra
        allCreatures.forEach {
            it.damage += (total - it.armor).coerceAtLeast(0)
        }
        game.log("Tendrils of Pain deals $total damage to all creatures")
    }
}

object Arise : CardScript {
    override fun fidelity() = CardFidelity(
        level = FidelityLevel.PARTIAL,
        notes = "Returns creatures of the house with the most creatures in discard; house choice is approximated"
    )

    override fun onPlay(game: GameState, player: PlayerState, card: InPlayCard) {
        val creaturesInDiscard = player.discard.mapNotNull { cardId ->
            player.deck.cards.find { it.cardId == cardId }?.effectiveHouse
        }
        val chosenHouse = creaturesInDiscard
            .groupingBy { it }
            .eachCount()
            .maxByOrNull { it.value }
            ?.key
        if (chosenHouse == null) {
            game.log("Arise returns no creatures (discard empty)")
            return
        }
        val returned = mutableListOf<String>()
        val iterator = player.discard.iterator()
        while (iterator.hasNext()) {
            val cardId = iterator.next()
            val house = player.deck.cards.find { it.cardId == cardId }?.effectiveHouse
            if (house == chosenHouse) {
                iterator.remove()
                player.hand.add(cardId)
                returned.add(cardId)
            }
        }
        player.chains += 1
        game.log("Arise returns ${returned.size} ${chosenHouse.name.lowercase()} creature(s) to hand and gives ${player.name} 1 chain")
    }
}

object Silvertooth : CardScript {
    override fun fidelity() = CardFidelity(
        level = FidelityLevel.FULL,
        notes = "Enters play ready"
    )

    override fun cardEntersReady(): Boolean = true
}

object YxilxDominator : CardScript {
    override fun fidelity() = CardFidelity(
        level = FidelityLevel.FULL,
        notes = "Taunt; enters play stunned"
    )

    override fun cardEntersStunned(): Boolean = true
}

object JammerPack : CardScript {
    override fun fidelity() = CardFidelity(
        level = FidelityLevel.FULL,
        notes = "Opponent's keys cost +2 amber while attached"
    )

    override fun staticEffects() = listOf(
        StaticEffect(
            StaticEffectKind.KEY_COST_MODIFIER,
            amount = 2,
            payer = PayerScope.OPPONENT_OF_CONTROLLER
        )
    )
}

object LashOfBrokenDreams : CardScript {
    override fun fidelity() = CardFidelity(
        level = FidelityLevel.FULL,
        notes = "Keys cost +3 amber during the opponent's next turn"
    )

    override fun resolveActionAbility(abilityId: String, game: GameState, player: PlayerState, card: InPlayCard) {
        val opponent = game.opponent()
        opponent.keyCostNextTurnBonus += 3
        game.log("${player.name} uses Lash of Broken Dreams; keys cost +3 amber during ${opponent.name}'s next turn")
    }

    override fun actionAbilities(): List<String> = listOf("Keys cost 3A during your opponents next turn")
}

object TooMuchToProtect : CardScript {
    override fun fidelity() = CardFidelity(
        level = FidelityLevel.FULL,
        notes = "Steals all but 6 of the opponent's amber (steals nothing if they have 6 or less)"
    )

    override fun onPlay(game: GameState, player: PlayerState, card: InPlayCard) {
        val opponent = game.opponent()
        if (opponent.amberCannotBeStolen) {
            game.log("${opponent.name}'s amber cannot be stolen (${opponent.name}: ${opponent.amber} -> ${opponent.amber})")
            return
        }
        val stolen = (opponent.amber - 6).coerceAtLeast(0)
        if (stolen <= 0) return
        opponent.amber -= stolen
        player.amber += stolen
        game.log(
            "${player.name} steals all but 6 amber from ${opponent.name} " +
                "(${player.name}: ${player.amber - stolen} -> ${player.amber}, ${opponent.name}: ${opponent.amber + stolen} -> ${opponent.amber})"
        )
    }
}

object Fogbank : CardScript {
    override fun fidelity() = CardFidelity(
        level = FidelityLevel.FULL,
        notes = "Opponent cannot use creatures to fight on their next turn"
    )

    override fun onPlay(game: GameState, player: PlayerState, card: InPlayCard) {
        val opponent = game.opponent()
        opponent.cannotFightNextTurn = true
        game.log("${player.name} plays Fogbank; ${opponent.name} cannot use creatures to fight on their next turn")
    }
}

object BiomatrixBackup : CardScript {
    override fun fidelity() = CardFidelity(
        level = FidelityLevel.FULL,
        notes = "Attached creature goes to its owner's archives instead of being destroyed"
    )

    override fun archivesAttachedCreatureOnDestroyed(): Boolean = true
}

object AnnihilationRitual : CardScript {
    override fun fidelity() = CardFidelity(
        level = FidelityLevel.FULL,
        notes = "Creatures destroyed while this is in play are purged instead of discarded"
    )

    override fun purgesDestroyedCreatures(): Boolean = true
}

/**
 * Manual DSL overrides for cards that forge keys outside the forge phase.
 */
val keyCheatDslCardOverrides: List<Pair<String, CardDefinition>> = listOf(
    card("key-charge") {
        play(Sequence(listOf(
            LoseAmber(1, SelfPlayer),
            Conditional(SelfAmberAtLeast(1), ForgeKey(extraCost = 0, discountPerCardInHand = false))
        )))
        playableIf(SelfAmberAtLeast(7))
        fidelity(FidelityLevel.FULL, "Loses 1 amber, then forges at current cost when affordable")
    },
    card("chota-hazri") {
        play(Sequence(listOf(
            LoseAmber(1, SelfPlayer),
            Conditional(SelfAmberAtLeast(1), ForgeKey(extraCost = 0, discountPerCardInHand = false))
        )))
        fidelity(FidelityLevel.FULL, "Loses 1 amber, then forges at current cost when affordable")
    },
    card("key-of-darkness") {
        play(Conditional(
            OpponentAmberAtMost(0),
            ForgeKey(extraCost = 2, discountPerCardInHand = false),
            ForgeKey(extraCost = 6, discountPerCardInHand = false)
        ))
        fidelity(FidelityLevel.FULL, "Forges at +6 cost, or +2 when the opponent has no amber")
    },
    card("epic-quest") {
        play(Archive(FriendlyCreatureWithTrait("knight", Scope.ALL)))
        omni(Conditional(
            CardsOfHousePlayedThisTurn(House.SANCTUM, 7),
            Sequence(listOf(
                SacrificeSelf,
                ForgeKey(free = true, discountPerCardInHand = false)
            ))
        ))
        fidelity(FidelityLevel.FULL, "Omni: sacrifices Epic Quest and forges at no cost after 7+ Sanctum cards played this turn")
    }
)

/**
 * Manual DSL overrides for the Mars engine cards (Key Abduction / Battle Fleet
 * package) that the generated Core set definitions cannot express.
 */
val marsEngineDslCardOverrides: List<Pair<String, CardDefinition>> = listOf(
    card("key-abduction") {
        play(Sequence(listOf(
            ReturnAllCreaturesOfHouseToHand(House.MARS),
            ForgeKey(extraCost = 9, discountPerCardInHand = true)
        )))
        fidelity(FidelityLevel.FULL, "Returns all Mars creatures to their owners' hands, then forges a key at +9 cost reduced by 1 per card in hand")
    },
    card("battle-fleet") {
        play(DrawPerCardInHand(House.MARS, SelfPlayer))
        fidelity(FidelityLevel.FULL, "AI reveals all Mars cards in hand and draws one per card")
    },
    card("commpod") {
        action("Reveal any number of Mars cards from your hand", ReadyCreaturesOfHousePerCardInHand(House.MARS, SelfPlayer))
        fidelity(FidelityLevel.FULL, "AI reveals all Mars cards in hand; readies that many friendly Mars creatures")
    },
    card("phosphorus-stars") {
        play(Sequence(listOf(
            Stun(CreatureNotOfHouse(House.MARS, Scope.ALL)),
            GainChains(2, SelfPlayer)
        )))
        fidelity(FidelityLevel.FULL, "Stuns each non-Mars creature and gives 2 chains")
    },
    card("zyzzix-the-many") {
        reap(Conditional(CreatureInHand, Sequence(listOf(ArchiveCreatureFromHand(1, SelfPlayer), AddPowerCounters(3)))))
        fight(Conditional(CreatureInHand, Sequence(listOf(ArchiveCreatureFromHand(1, SelfPlayer), AddPowerCounters(3)))))
        fidelity(FidelityLevel.FULL, "AI archives one creature from hand (if any) and adds three +1 power counters")
    },
    card("john-smyth") {
        reap(Ready(OtherFriendlyCreatureWithHouse(House.MARS)))
        fight(Ready(OtherFriendlyCreatureWithHouse(House.MARS)))
        fidelity(FidelityLevel.PARTIAL, "Readies another friendly Mars creature; non-Agent restriction not enforced")
    }
)

/**
 * Hand-written DSL definitions for cards whose behavior is too specific for
 * generator rules (Phase 2 static/reaction/restriction cards, Phase 3 batches
 * and earlier hand patches). The generated file keeps `NoEffect` placeholders
 * for these ids; this list wins at registration (see buildCoreCardLibrary), so
 * regenerating CoreSetDslCards.kt is safe. Ids also present in the
 * marsEngine/keyCheat lists below are intentionally NOT repeated here.
 */
val dslCardOverrides: List<Pair<String, CardDefinition>> = listOf(
    card("champion-anaphiel") {

        play(NoEffect)
        fidelity(FidelityLevel.FULL, "Taunt keyword; handled by the keyword engine")
    
    },
    card("snufflegator") {

        play(NoEffect)
        fidelity(FidelityLevel.FULL, "Skirmish keyword; handled by the keyword engine")
    
    },
    card("macis-asp") {

        play(NoEffect)
        fidelity(FidelityLevel.FULL, "Skirmish + Poison keywords; handled by the keyword engine")
    
    },
    card("ancient-bear") {

        play(NoEffect)
        fidelity(FidelityLevel.FULL, "Assault 2 keyword; handled by the keyword engine")
    
    },
    card("briar-grubbling") {

        play(NoEffect)
        fidelity(FidelityLevel.FULL, "Hazardous 5 keyword; handled by the keyword engine")
    
    },
    card("lupo-the-scarred") {

        play(DealDamage(2, EnemyCreature()))
        fidelity(FidelityLevel.FULL, "Skirmish keyword; Play: deal 2 damage to an enemy creature")
    
    },
    card("mighty-tiger") {

        play(DealDamage(4, EnemyCreature()))
        fidelity(FidelityLevel.FULL, "Play: deal 4 damage to an enemy creature")
    
    },
    card("murmook") {

        persistent { modifyKeyCost(1, PayerScope.OPPONENT_OF_CONTROLLER) }
        fidelity(FidelityLevel.FULL, "Opponent's keys cost +1 amber (persistent key-cost modifier)")
    
    },
    card("iron-obelisk") {

        persistent { modifyKeyCostDynamic(PayerScope.OPPONENT_OF_CONTROLLER) { game, source ->
            game.player(source.controllerIndex).creatures.count { it.house == House.BROBNAR && it.damage > 0 }
        } }
        fidelity(FidelityLevel.FULL, "Opponent's keys cost +1 amber per friendly damaged Brobnar creature")
    
    },
    card("foggify") {

        play(DenyCreatureFightsNextTurn)
        fidelity(FidelityLevel.FULL, "Opponent cannot use creatures to fight on their next turn (plays and discards allowed)")
    
    },
    card("skippy-timehog") {

        play(DenyCardUseNextTurn)
        fidelity(FidelityLevel.FULL, "Opponent cannot use any cards next turn; plays and discards still allowed")
    
    },
    card("scrambler-storm") {

        play(DenyActionCardsNextTurn)
        fidelity(FidelityLevel.FULL, "Opponent cannot play action cards on their next turn")
    
    },
    card("doorstep-to-heaven") {

        play(ReduceAmberTo(threshold = 6, cap = 5))
        fidelity(FidelityLevel.FULL, "Each player with 6 or more amber is reduced to 5")
    
    },
    card("timetraveller") {

        play(Draw(2, SelfPlayer))
        action("Shuffle Timetraveller into your deck", ShuffleSelfIntoDeck)
        fidelity(FidelityLevel.FULL, "Play: draw 2 cards; Action: shuffle Timetraveller into its owner's deck")
    
    },
    card("lady-maxena") {

        play(Stun(AnyCreature()))
        action("Return Lady Maxena to its owner's hand", ReturnToHand(SelfCreature))
        fidelity(FidelityLevel.FULL, "Play: stun a creature; Action: return Lady Maxena to its owner's hand")
    
    },
    card("yo-mama-mastery") {

        play(Heal(99, SelfCreature))
        persistent { addKeyword(Keyword.TAUNT, 1, HostCreatureScope) }
        fidelity(FidelityLevel.FULL, "Upgrade: host gains taunt; Play: fully heal this creature")
    
    },
    card("way-of-the-bear") {

        persistent { addKeyword(Keyword.ASSAULT, 2, HostCreatureScope) }
        fidelity(FidelityLevel.FULL, "Upgrade: host gains assault 2")
    
    },
    card("way-of-the-wolf") {

        persistent { addKeyword(Keyword.SKIRMISH, 1, HostCreatureScope) }
        fidelity(FidelityLevel.FULL, "Upgrade: host gains skirmish")
    
    },
    card("chuff-ape") {

        reap(NoEffect)
        fight(NoEffect)
        entersPlayStunned()
        fidelity(FidelityLevel.PARTIAL, "Taunt + enters play stunned implemented; Fight/Reap sacrifice-another-and-heal not implemented")
    
    },
    card("bait-and-switch") {

        // keyteki gates the play action on the opponent having more amber.
        playableIf(OpponentHasMoreAmber)
        play(RepeatWhile(OpponentHasMoreAmber, StealAmber(1)))
        fidelity(FidelityLevel.FULL, "Steals 1 amber repeatedly while the opponent has more amber than you")
    
    },
    card("effervescent-principle") {

        play(Sequence(listOf(LoseHalfAmber, GainChains(1, SelfPlayer))))
        fidelity(FidelityLevel.FULL, "Each player loses half their amber (rounding down the loss); gain 1 chain")
    
    },
    card("lost-in-the-woods") {

        play(ShuffleCreaturesIntoDeck(friendlyCount = 2, enemyCount = 2))
        fidelity(FidelityLevel.FULL, "Shuffles 2 friendly and 2 enemy creatures into their owners' decks")
    
    },
    card("mother") {

        drawRefillBonus(1)
        fidelity(FidelityLevel.FULL, "Draw step: refills your hand to 1 additional card")
    
    },
    card("succubus") {

        opponentRefillPenalty(1)
        fidelity(FidelityLevel.FULL, "Draw step: your opponent refills their hand to 1 less card")
    
    },
    card("niffle-ape") {

        ignoresTauntAndElusive()
        fidelity(FidelityLevel.FULL, "While Niffle Ape is attacking, ignore taunt and elusive")
    
    },
    card("soft-landing") {

        play(NextPlaysEnterReady(1))
        fidelity(FidelityLevel.FULL, "The next creature or artifact you play this turn enters play ready")
    
    },
    card("shield-of-justice") {

        play(GrantDamageImmunity(FriendlyCreature(Scope.ALL)))
        fidelity(FidelityLevel.FULL, "Friendly creatures cannot be dealt damage for the remainder of the turn")
    
    },
    card("protectrix") {

        reap(Sequence(listOf(Heal(3, AnyCreature(), fully = true), GrantDamageImmunity(LastHealedTarget))))
        fidelity(FidelityLevel.FULL, "Reap: fully heals a creature; it cannot be dealt damage for the remainder of the turn")
    
    },
    card("vigor") {

        play(Sequence(listOf(Heal(3, AnyCreature()), Conditional(HealedAtLeast(3), GainAmber(1)))))
        fidelity(FidelityLevel.FULL, "Heals up to 3 damage from a creature; gains 1 amber when 3 was healed")
    
    },
    card("twin-bolt-emission") {

        play(Sequence(listOf(DealDamage(2, AnyCreature()), DealDamage(2, AnyCreatureExceptLastDamaged))))
        fidelity(FidelityLevel.FULL, "Deals 2 damage to a creature and 2 to a different creature")
    
    },
    card("pawn-sacrifice") {

        play(
            Sequence(
                listOf(
                    Sacrifice(FriendlyCreature()),
                    Conditional(
                        SacrificedTarget,
                        Sequence(listOf(DealDamage(3, AnyCreature()), DealDamage(3, AnyCreatureExceptLastDamaged)))
                    )
                )
            )
        )
        fidelity(FidelityLevel.FULL, "Sacrifices a friendly creature; deals 3 damage each to 2 creatures")
    
    },
    card("seeker-needle") {

        action(
            "Deal 1D to a creature",
            Sequence(listOf(DealDamage(1, AnyCreature()), Conditional(DamageDestroyedTarget, GainAmber(1))))
        )
        fidelity(FidelityLevel.FULL, "Deals 1 damage; gains 1 amber when the damage destroys the creature")
    
    },
    card("sergeant-zakiel") {

        play(ReadyAndFight(NeighborOfSelf))
        fidelity(FidelityLevel.FULL, "Readies and fights with a neighboring creature")
    
    },
    card("dextre") {

        play(CaptureAmber(1, SelfCreature))
        destroyed(PutSelfOnTopOfDeck)
        fidelity(FidelityLevel.FULL, "Play: captures 1 amber; Destroyed: put Dextre on top of your deck")
    
    },
    card("booby-trap") {

        play(DealDamage(4, CreatureNotOnFlank, splash = 2))
        fidelity(FidelityLevel.FULL, "Deals 4 damage with 2 splash to a creature that is not on a flank")
    
    },
    card("nocturnal-maneuver") {

        play(Exhaust(AnyCreature(Scope.ALL), count = 3))
        fidelity(FidelityLevel.FULL, "Exhausts up to 3 creatures")
    
    },
    card("terms-of-redress") {

        play(CaptureAmber(2, FriendlyCreature()))
        fidelity(FidelityLevel.FULL, "A chosen friendly creature captures 2 amber")
    
    },
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
    card("nepenthe-seed") {

        // Return before the sacrifice so the greedy discard pick does not
        // always grab the seed itself (keyteki resolves sacrifice first).
        omni(Sequence(listOf(ReturnFromDiscardToHand(1, SelfPlayer), SacrificeSelf)))
        fidelity(FidelityLevel.FULL, "Omni: sacrifices Nepenthe Seed and returns a card from your discard pile to your hand")
    
    },
    card("ritual-of-balance") {

        action("If your opponent has 6A or more steal 1A", Conditional(OpponentAmberAtLeast(6), StealAmber(1)))
        fidelity(FidelityLevel.FULL, "Steals 1 amber when the opponent has 6 or more amber")
    
    },
    card("fear") {

        play(ReturnToHand(EnemyCreature(Scope.ONE)))
        fidelity(FidelityLevel.FULL, "Returns one enemy creature to its owner's hand")
    
    },
    card("banner-of-battle") {

        persistent {
            modifyPower(1, FriendlyCreaturesScope)
        }
        fidelity(FidelityLevel.FULL, "Aura: each friendly creature gets +1 power")
    
    },
    card("blinding-light") {

        play(Stun(AllCreaturesOfChosenHouse))
        fidelity(FidelityLevel.FULL, "All effect sentences mapped")
    
    },
    card("blood-of-titans") {

        persistent {
            modifyPower(5, HostCreatureScope)
        }
        fidelity(FidelityLevel.FULL, "Upgrade: attached creature gets +5 power")
    
    },
    card("bouncing-deathquark") {

        play(Sequence(listOf(Destroy(EnemyCreature()), Destroy(FriendlyCreature()))))
        fidelity(FidelityLevel.FULL, "All effect sentences mapped")
    
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
    card("bulwark") {

        persistent {
            modifyArmor(2, NeighborsOfSelfScope)
        }
        fidelity(FidelityLevel.FULL, "Aura: each of Bulwark's neighbors gets +2 armor")
    
    },
    card("burn-the-stockpile") {

        play(Conditional(OpponentAmberAtLeast(7), LoseAmber(4, OpponentPlayer)))
        fidelity(FidelityLevel.FULL, "All effect sentences mapped")
    
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
    card("control-the-weak") {

        // Effect implemented manually in CoreCards.kt to choose the best house to deny.
        play(NoEffect)
        fidelity(FidelityLevel.PARTIAL, "House-denial effect implemented via card script")
    
    },
    card("dr-escotera") {

        play(GainAmberEqualToOpponentKeys(SelfPlayer))
        fidelity(FidelityLevel.FULL, "All effect sentences mapped")
    
    },
    card("ember-imp") {

    play(NoEffect)
    restrictions {
        opponentPlayLimit(2)
    }
    fidelity(FidelityLevel.FULL, "Opponent cannot play more than 2 cards each turn (enforced on the opponent's own play count)")

    },
    card("flame-wreathed") {

        persistent {
            modifyPower(2, HostCreatureScope)
            addKeyword(Keyword.HAZARDOUS, value = 2, scope = HostCreatureScope)
        }
        fidelity(FidelityLevel.FULL, "Upgrade: attached creature gets +2 power and gains hazardous 2")
    
    },
    card("flaxia") {

        play(Conditional(OpponentHasFewerCreatures, GainAmber(2)))
        fidelity(FidelityLevel.FULL, "All effect sentences mapped")
    
    },
    card("ghostly-hand") {

        play(Conditional(OpponentAmberExactly(1), StealAmber(1)))
        fidelity(FidelityLevel.FULL, "All effect sentences mapped")
    
    },
    card("grey-monk") {

        persistent {
            modifyArmor(1, FriendlyCreaturesScope)
        }
        reap(Heal(2, AnyCreature()))
        fidelity(FidelityLevel.FULL, "Aura: each friendly creature gets +1 armor; Reap heals 2 damage")
    
    },
    card("grommid") {

    restrictions {
        cannotPlay(CardType.CREATURE)
    }
    reaction(
        "after an enemy creature is destroyed fighting Grommid, your opponent loses 1 amber",
        effect = LoseAmberOpponent(1)
    ) { _, player, card, event ->
        event is FightResolved && (
            (event.attackerCardId == card.cardId && event.attackerController == player.index &&
                event.defenderDestroyed && !event.attackerDestroyed) ||
                (event.defenderCardId == card.cardId && event.defenderController == player.index &&
                    event.attackerDestroyed && !event.defenderDestroyed)
            )
    }
    fidelity(FidelityLevel.FULL, "Controller cannot play creatures; after an enemy creature is destroyed fighting Grommid, your opponent loses 1 amber")

    },
    card("halacor") {

        persistent {
            addKeyword(Keyword.SKIRMISH, scope = FriendlyFlankCreaturesScope)
        }
        fidelity(FidelityLevel.FULL, "Aura: each friendly flank creature gains skirmish")
    
    },
    card("hebe-the-huge") {

        play(DealDamage(2, EachOtherUndamagedCreature))
        fidelity(FidelityLevel.FULL, "All effect sentences mapped")
    
    },
    card("hypnotic-command") {

        play(TakeControlUntilEndOfTurn(EnemyCreature()))
        fidelity(FidelityLevel.FULL, "All effect sentences mapped")
    
    },
    card("invasion-portal") {

        action("Discard cards from the top of your deck until you discard a Mars creature or run out of cards", AddTopCardOfHouseToHand(House.MARS, SelfPlayer))
        fidelity(FidelityLevel.FULL, "All effect sentences mapped")
    
    },
    card("kelifi-dragon") {

        playableIf(SelfAmberAtLeast(7))
        reap(Sequence(listOf(GainAmber(1), DealDamage(5, AnyCreature()))))
        fight(Sequence(listOf(GainAmber(1), DealDamage(5, AnyCreature()))))
        fidelity(FidelityLevel.FULL, "All effect sentences mapped")
    
    },
    card("king-of-the-crag") {

        persistent {
            modifyPower(-2, EnemyOfHouseScope(House.BROBNAR))
        }
        fidelity(FidelityLevel.FULL, "Aura: each enemy Brobnar creature gets -2 power")
    
    },
    card("krump") {

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
    card("library-access") {

        play(DrawOnCardPlayThisTurn())
        fidelity(FidelityLevel.FULL, "All effect sentences mapped")
    
    },
    card("lomir-flamefist") {

        play(Conditional(OpponentAmberAtLeast(7), LoseAmber(2, OpponentPlayer)))
        fidelity(FidelityLevel.FULL, "All effect sentences mapped")
    
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
    card("mushroom-man") {

        persistent {
            modifyPower(SelfScope) { game, source ->
                3 * (3 - game.player(source.controllerIndex).keys)
            }
        }
        fidelity(FidelityLevel.FULL, "Aura: gets +3 power for each unforged key you have")
    
    },
    card("nerve-blast") {

        play(Sequence(listOf(StealAmber(1), Conditional(StealSucceeded, DealDamage(2, AnyCreature())))))
        fidelity(FidelityLevel.FULL, "All effect sentences mapped")
    
    },
    card("neuro-syphon") {

        play(Conditional(OpponentHasMoreAmber, Sequence(listOf(StealAmber(1), Draw(1, SelfPlayer)))))
        fidelity(FidelityLevel.FULL, "All effect sentences mapped")
    
    },
    card("neutron-shark") {

        val neutronEffect = Sequence(listOf(Destroy(AnyCreature()), Conditional(DamageDestroyedTarget, DiscardTopDeck(SelfPlayer))))
        play(neutronEffect)
        reap(neutronEffect)
        fight(neutronEffect)
        fidelity(FidelityLevel.FULL, "All effect sentences mapped")
    
    },
    card("niffle-queen") {

        persistent {
            modifyPower(1, OtherFriendlyCreaturesScope(trait = "beast"))
            modifyPower(1, OtherFriendlyCreaturesScope(trait = "niffle"))
        }
        fidelity(FidelityLevel.FULL, "Aura: each other friendly Beast/Niffle creature gets +1 power")
    
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
    card("pitlord") {

    play(NoEffect)
    restrictions {
        forcedHouse(House.DIS)
    }
    fidelity(FidelityLevel.FULL, "While Pitlord is in play, its controller must choose Dis as active house")

    },
    card("protect-the-weak") {

        persistent {
            modifyArmor(1, HostCreatureScope)
            addKeyword(Keyword.TAUNT, scope = HostCreatureScope)
        }
        fidelity(FidelityLevel.FULL, "Upgrade: attached creature gets +1 armor and taunt")
    
    },
    card("relentless-whispers") {

        play(Sequence(listOf(DealDamage(2, AnyCreature()), Conditional(DamageDestroyedTarget, StealAmber(1)))))
        fidelity(FidelityLevel.FULL, "All effect sentences mapped")
    
    },
    card("ring-of-invisibility") {

        persistent {
            addKeyword(Keyword.ELUSIVE, scope = HostCreatureScope)
            addKeyword(Keyword.SKIRMISH, scope = HostCreatureScope)
        }
        fidelity(FidelityLevel.FULL, "Upgrade: attached creature gains elusive and skirmish")
    
    },
    card("round-table") {

        persistent {
            modifyPower(1, FriendlyWithTraitScope("knight"))
            addKeyword(Keyword.TAUNT, scope = FriendlyWithTraitScope("knight"))
        }
        fidelity(FidelityLevel.FULL, "Aura: each friendly Knight gets +1 power and taunt")
    
    },
    card("shadow-self") {

        play(NoEffect)
        fidelity(FidelityLevel.FULL, "Manual override implements damage redirection and zero fight damage")
    
    },
    card("shooler") {

        play(Conditional(OpponentAmberAtLeast(4), StealAmber(1)))
        fidelity(FidelityLevel.FULL, "All effect sentences mapped")
    
    },
    card("shoulder-armor") {

        persistent {
            modifyPower(2, HostCreatureScope, requireFlank = true)
            modifyArmor(2, HostCreatureScope, requireFlank = true)
        }
        fidelity(FidelityLevel.FULL, "Upgrade: attached creature gets +2 armor and +2 power while on a flank")
    
    },
    card("skeleton-key") {

        action("A friendly creature captures 1A", CaptureAmber(1, FriendlyCreature()))
        fidelity(FidelityLevel.FULL, "All effect sentences mapped")
    
    },
    card("smaaash") {

        play(Stun(AnyCreature()))
        fidelity(FidelityLevel.FULL, "All effect sentences mapped")
    
    },
    card("smiling-ruth") {

        reap(TakeControlOfEnemyFlankCreature(SelfPlayer))
        fidelity(FidelityLevel.FULL, "All effect sentences mapped")
    
    },
    card("smith") {

        play(Conditional(OpponentHasFewerCreatures, GainAmber(2)))
        fidelity(FidelityLevel.FULL, "All effect sentences mapped")
    
    },
    card("soul-snatcher") {

        reaction(
            "each time a creature is destroyed, its owner gains 1 amber",
            effect = EventSideGainsAmber(1, EventSide.OWNER)
        ) { _, _, _, event -> event is CreatureDestroyed }
        fidelity(FidelityLevel.FULL, "Reaction: each destroyed creature's owner gains 1 amber")
    
    },
    card("staunch-knight") {

        persistent {
            modifyPower(2, SelfScope, requireFlank = true)
        }
        fidelity(FidelityLevel.FULL, "Aura: gets +2 power while on a flank")
    
    },
    card("the-terror") {

        play(Conditional(OpponentAmberAtMost(0), GainAmber(2)))
        fidelity(FidelityLevel.FULL, "All effect sentences mapped")
    
    },
    card("the-vaultkeeper") {

    play(NoEffect)
    restrictions {
        noSteal()
    }
    fidelity(FidelityLevel.FULL, "Controller's amber cannot be stolen")

    },
    card("tireless-crocag") {

    play(NoEffect)
    restrictions {
        selfCannotReap()
        selfUsableAsActiveHouse()
        selfDestructIf { game, player, _ -> game.opponent(player).creatures.isEmpty() }
    }
    fidelity(FidelityLevel.FULL, "Cannot reap; usable as if it belonged to the active house; destroyed while the opponent has no creatures in play")

    },
    card("titan-mechanic") {

        persistent {
            modifyKeyCost(-1, PayerScope.ANY, requireFlank = true)
        }
        fidelity(FidelityLevel.FULL, "Aura: while on a flank, each key (either player's) costs -1 amber")
    
    },
    card("tolas") {

        reaction(
            "each time a creature is destroyed, its opponent gains 1 amber",
            effect = EventSideGainsAmber(1, EventSide.CONTROLLER_OPPONENT)
        ) { _, _, _, event -> event is CreatureDestroyed }
        fidelity(FidelityLevel.FULL, "Elusive; reaction: each destroyed creature's opponent gains 1 amber")
    
    },
    card("tremor") {

        play(Stun(CreatureAndNeighbors(AnyCreature())))
        fidelity(FidelityLevel.FULL, "All effect sentences mapped")
    
    },
    card("warsong") {

        play(GainAmberOnTriggerThisTurn(1, TurnTrigger.FRIENDLY_CREATURE_FIGHTS))
        fidelity(FidelityLevel.FULL, "All effect sentences mapped")
    
    },
    card("witch-of-the-wilds") {

    play(NoEffect)
    restrictions {
        mayPlayHouse(House.UNTAMED, perTurn = 1)
    }
    fidelity(FidelityLevel.FULL, "During each turn in which Untamed is not the active house, the controller may play one Untamed card")

    },
    card("yxili-marauder") {

        play(CaptureAmber(1, FriendlyCreature()))
        persistent {
            modifyPower(SelfScope) { _, source -> source.capturedAmber }
        }
        fidelity(FidelityLevel.PARTIAL, "Aura (+1 power per amber on it) implemented; play captures a fixed 1 amber instead of 1 per friendly ready Mars creature")
    
    },
    card("yxilo-bolter") {

        reap(Sequence(listOf(DealDamage(2, AnyCreature()), Conditional(DamageDestroyedTarget, Purge(LastDamagedTarget)))))
        fight(Sequence(listOf(DealDamage(2, AnyCreature()), Conditional(DamageDestroyedTarget, Purge(LastDamagedTarget)))))
        fidelity(FidelityLevel.FULL, "All effect sentences mapped")
    
    },
)
