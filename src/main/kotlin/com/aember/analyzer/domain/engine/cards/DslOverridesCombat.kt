package com.aember.analyzer.domain.engine.cards

import com.aember.analyzer.domain.engine.dsl.*
import com.aember.analyzer.domain.engine.fidelity.FidelityLevel
import com.aember.analyzer.domain.model.House

/** Combat patterns: damage cascades, destroys, heal-then-damage, armor stripping, fight triggers. */
val dslCardOverridesCombat: List<Pair<String, CardDefinition>> = listOf(
    card("lupo-the-scarred") {

        play(DealDamage(2, EnemyCreature()))
        fidelity(FidelityLevel.FULL, "Skirmish keyword; Play: deal 2 damage to an enemy creature")
    
    },
    card("mighty-tiger") {

        play(DealDamage(4, EnemyCreature()))
        fidelity(FidelityLevel.FULL, "Play: deal 4 damage to an enemy creature")
    
    },
    card("foggify") {

        play(DenyCreatureFightsNextTurn)
        fidelity(FidelityLevel.FULL, "Opponent cannot use creatures to fight on their next turn (plays and discards allowed)")
    
    },
    card("lady-maxena") {

        play(Stun(AnyCreature()))
        action("Return Lady Maxena to its owner's hand", ReturnToHand(SelfCreature))
        fidelity(FidelityLevel.FULL, "Play: stun a creature; Action: return Lady Maxena to its owner's hand")
    
    },
    card("shield-of-justice") {

        play(GrantDamageImmunity(FriendlyCreature(Scope.ALL)))
        fidelity(FidelityLevel.FULL, "Friendly creatures cannot be dealt damage for the remainder of the turn")
    
    },
    card("protectrix") {

        reap(Sequence(listOf(Heal(3, AnyCreature(), fully = true), GrantDamageImmunity(LastHealedTarget))))
        fidelity(FidelityLevel.FULL, "Reap: fully heals a creature; it cannot be dealt damage for the remainder of the turn")
    
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
    card("sergeant-zakiel") {

        play(ReadyAndFight(NeighborOfSelf))
        fidelity(FidelityLevel.FULL, "Readies and fights with a neighboring creature")
    
    },
    card("booby-trap") {

        play(DealDamage(4, CreatureNotOnFlank, splash = 2))
        fidelity(FidelityLevel.FULL, "Deals 4 damage with 2 splash to a creature that is not on a flank")
    
    },
    card("blinding-light") {

        play(Stun(AllCreaturesOfChosenHouse))
        fidelity(FidelityLevel.FULL, "All effect sentences mapped")
    
    },
    card("bouncing-deathquark") {

        play(Sequence(listOf(Destroy(EnemyCreature()), Destroy(FriendlyCreature()))))
        fidelity(FidelityLevel.FULL, "All effect sentences mapped")
    
    },
    card("hebe-the-huge") {

        play(DealDamage(2, EachOtherUndamagedCreature))
        fidelity(FidelityLevel.FULL, "All effect sentences mapped")
    
    },
    card("neutron-shark") {

        // Destroy an enemy creature and a friendly creature, then discard the top card.
        // Not modelled: the artifact options and the "not Logos -> trigger again" repeat.
        val neutronEffect = Sequence(listOf(Destroy(EnemyCreature()), Destroy(FriendlyCreature()), DiscardTopDeck(SelfPlayer)))
        play(neutronEffect)
        reap(neutronEffect)
        fight(neutronEffect)
        fidelity(FidelityLevel.PARTIAL, "Destroys an enemy and a friendly creature and discards the top card; artifact options and the Logos repeat are not modelled")
    
    },
    card("smaaash") {

        play(Stun(AnyCreature()))
        fidelity(FidelityLevel.FULL, "All effect sentences mapped")
    
    },
    card("tremor") {

        play(Stun(CreatureAndNeighbors(AnyCreature())))
        fidelity(FidelityLevel.FULL, "All effect sentences mapped")
    
    },
    card("yxilo-bolter") {

        reap(Sequence(listOf(DealDamage(2, AnyCreature()), Conditional(DamageDestroyedTarget, Purge(LastDamagedTarget)))))
        fight(Sequence(listOf(DealDamage(2, AnyCreature()), Conditional(DamageDestroyedTarget, Purge(LastDamagedTarget)))))
        fidelity(FidelityLevel.FULL, "All effect sentences mapped")

    },

    // ---------------------------------------------------------------------
    // Phase 3 batch (c): capture / power counters / conditionals
    // ---------------------------------------------------------------------,
    card("eater-of-the-dead") {

        reap(Sequence(listOf(PurgeCreatureFromDiscard, Conditional(PurgedTarget, AddPowerCounters(1, SelfCreature)))))
        fight(Sequence(listOf(PurgeCreatureFromDiscard, Conditional(PurgedTarget, AddPowerCounters(1, SelfCreature)))))
        fidelity(FidelityLevel.FULL, "Fight/Reap: purge a creature from a discard pile; if you do, gain a +1 power counter")

    },
    card("cleansing-wave") {

        play(HealEach(1, gainPerHealed = 1))
        fidelity(FidelityLevel.FULL, "Play: heal 1 damage from each creature and gain 1A per creature healed")

    },
    card("mothergun") {

        action("Reveal any number of Mars cards from your hand", DealDamagePerHouseCardInHand(House.MARS, AnyCreature()))
        fidelity(FidelityLevel.FULL, "Action: reveals all Mars cards from hand (greedy) and deals damage equal to that count to a creature")

    },
    card("snudge") {

        reap(ReturnArtifactOrFlankCreatureToHand)
        fight(ReturnArtifactOrFlankCreatureToHand)
        fidelity(FidelityLevel.FULL, "Fight/Reap: return the opponent's artifact (or strongest flank creature) to its owner's hand")

    },
    card("positron-bolt") {

        play(DealDamageFlankCascade)
        fidelity(FidelityLevel.FULL, "Play: deal 3 damage to a flank creature, 2 to its neighbor and 1 to the next")

    },
    card("guardian-demon") {

        val healThenDamage = HealThenDamageOther(2)
        play(healThenDamage)
        reap(healThenDamage)
        fight(healThenDamage)
        fidelity(FidelityLevel.FULL, "Play/Fight/Reap: heal up to 2 damage from a creature and deal that amount to another creature")

    },
    card("cooperative-hunting") {

        play(DealDividedDamage(1, EnemyCreature(Scope.ALL)))
        fidelity(FidelityLevel.FULL, "Play: deal 1 damage per friendly creature, divided among creatures")

    },
    card("red-hot-armor") {

        play(StripEnemyArmorAndDamagePerLost)
        fidelity(FidelityLevel.FULL, "Play: each enemy creature loses its armor until end of turn and takes 1 damage per armor lost")

    },
    card("squawker") {

        play(ReadyMarsOrStunNonMars)
        fidelity(FidelityLevel.FULL, "Play: ready a Mars creature or stun a non-Mars creature (greedy)")

    },
    card("special-delivery") {

        omni(Sequence(listOf(
            SacrificeSelf,
            DealDamage(3, FlankCreature),
            Conditional(DamageDestroyedTarget, Purge(LastDamagedTarget))
        )))
        fidelity(FidelityLevel.FULL, "Omni: sacrifice Special Delivery, deal 3 damage to a flank creature; purge it if this destroys it")

    },
    card("lord-golgotha") {

        beforeFight(DealDamageToFightsNeighbors(3))
        fidelity(FidelityLevel.FULL, "Before Fight: deal 3 damage to each neighbor of the creature Lord Golgotha fights")

    },
    card("bulleteye") {

        reap(Destroy(FlankCreature))
        fidelity(FidelityLevel.FULL, "Reap: destroy a flank creature")

    },
    card("qyxxlyx-plague-master") {

        val plague = DealDamageIgnoringArmor(3, AnyCreatureWithTrait("human"))
        reap(plague)
        fight(plague)
        fidelity(FidelityLevel.FULL, "Fight/Reap: deal 3 damage to each Human creature, ignoring armor")

    },
    card("anomaly-exploiter") {

        action("Destroy a damaged creature", Destroy(DamagedCreature))
        fidelity(FidelityLevel.FULL, "Action: destroy a damaged creature")

    },
    card("potion-of-invulnerability") {

        omni(Sequence(listOf(SacrificeSelf, GrantDamageImmunity(FriendlyCreature(Scope.ALL)))))
        fidelity(FidelityLevel.FULL, "Omni: sacrifice Potion of Invulnerability; friendly creatures cannot be dealt damage for the remainder of the turn")

    },
    card("gabos-longarms") {

        beforeFight(ChangeFightDamageTarget)
        fidelity(FidelityLevel.FULL, "Before Fight: deal the fight damage to the chosen creature instead of the one Gabos fights")

    },

)
