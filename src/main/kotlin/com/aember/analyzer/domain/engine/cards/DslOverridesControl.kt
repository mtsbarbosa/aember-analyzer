package com.aember.analyzer.domain.engine.cards

import com.aember.analyzer.domain.engine.dsl.*
import com.aember.analyzer.domain.engine.fidelity.FidelityLevel
import com.aember.analyzer.domain.model.House

/** Turn control: denials, take-control, house choices, play grants and everything not covered above. */
val dslCardOverridesControl: List<Pair<String, CardDefinition>> = listOf(
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
    card("skippy-timehog") {

        play(DenyCardUseNextTurn)
        fidelity(FidelityLevel.FULL, "Opponent cannot use any cards next turn; plays and discards still allowed")
    
    },
    card("scrambler-storm") {

        play(DenyActionCardsNextTurn)
        fidelity(FidelityLevel.FULL, "Opponent cannot play action cards on their next turn")
    
    },
    card("soft-landing") {

        play(NextPlaysEnterReady(1))
        fidelity(FidelityLevel.FULL, "The next creature or artifact you play this turn enters play ready")
    
    },
    card("nocturnal-maneuver") {

        play(Exhaust(AnyCreature(Scope.ALL), count = 3))
        fidelity(FidelityLevel.FULL, "Exhausts up to 3 creatures")
    
    },
    card("control-the-weak") {

        // Effect implemented manually in CoreCards.kt to choose the best house to deny.
        play(NoEffect)
        fidelity(FidelityLevel.PARTIAL, "House-denial effect implemented via card script")
    
    },
    card("shadow-self") {

        play(NoEffect)
        fidelity(FidelityLevel.FULL, "Manual override implements damage redirection and zero fight damage")
    
    },
    card("smiling-ruth") {

        reap(TakeControlOfEnemyFlankCreature(SelfPlayer))
        fidelity(FidelityLevel.FULL, "All effect sentences mapped")
    
    },
    card("martian-hounds") {

        play(AddPowerCountersDynamic(
            { game, _ -> game.players.sumOf { it.creatures.count { c -> c.damage > 0 } } * 2 },
            FriendlyCreature()
        ))
        fidelity(FidelityLevel.FULL, "Play: give a creature two +1 power counters per damaged creature in play")

    },
    card("phase-shift") {

        play(MayPlayNonHouseThisTurn(House.LOGOS))
        fidelity(FidelityLevel.FULL, "Play: you may play one non-Logos card this turn")

    },

    // ---- Phase 3 batch (d) ----,
    card("emp-blast") {

        play(EmpBlastBurst)
        fidelity(FidelityLevel.FULL, "Play: stun each Mars/Robot creature and destroy each artifact")

    },
    card("sigil-of-brotherhood") {

        omni(Sequence(listOf(SacrificeSelf, MayUseHouseThisTurn(House.SANCTUM))))
        fidelity(FidelityLevel.FULL, "Omni: sacrifice Sigil of Brotherhood; you may use friendly Sanctum creatures for the remainder of the turn")

    },
    card("combat-pheromones") {

        omni(Sequence(listOf(SacrificeSelf, AllowUseUpToTwoMarsCards)))
        fidelity(FidelityLevel.FULL, "Omni: sacrifice Combat Pheromones; you may use up to 2 Mars cards for the remainder of the turn")

    },
    card("lifeward") {

        omni(Sequence(listOf(SacrificeSelf, DenyCreaturePlaysNextTurn)))
        fidelity(FidelityLevel.FULL, "Omni: sacrifice Lifeward; your opponent cannot play creatures on their next turn")

    },
    card("shatter-storm") {

        play(LoseAllAmberAndOpponentLosesTriple)
        fidelity(FidelityLevel.FULL, "Play: lose all your amber; your opponent loses triple that amount")

    },
    card("virtuous-works") {
        fidelity(FidelityLevel.VANILLA, "(Vanilla) — no ability text")
    },
    card("dust-pixie") {
        fidelity(FidelityLevel.VANILLA, "(Vanilla) — no ability text")
    },

    card("follow-the-leader") {

        play(AllCreaturesMayFightThisTurn)
        fidelity(FidelityLevel.FULL, "Play: for the remainder of the turn, each friendly creature may fight")

    },
    card("brothers-in-battle") {

        play(ChosenHouseMayFightThisTurn())
        fidelity(FidelityLevel.FULL, "Play: choose a house (greedy); each friendly creature of that house may fight this turn")

    },
    card("horseman-of-war") {

        play(AllCreaturesFightOnlyThisTurn)
        fidelity(FidelityLevel.FULL, "Play: for the remainder of the turn each friendly creature is usable as the active house but can only fight")

    },
    card("dimension-door") {

        play(ReapStealsInstead)
        fidelity(FidelityLevel.FULL, "Play: for the remainder of the turn, amber you gain from reaping is stolen from your opponent instead")

    },
    card("deipno-spymaster") {

        omni(Sequence(listOf(AllowUseFriendlyCreaturesThisTurn(1))))
        fidelity(FidelityLevel.FULL, "Omni: choose a friendly creature; you may use it this turn")

    },
    card("horseman-of-pestilence") {

        val plague = DealDamage(1, CreatureWithoutTrait("horseman"))
        play(plague)
        reap(plague)
        fight(plague)
        fidelity(FidelityLevel.FULL, "Play/Fight/Reap: deal 1 damage to each non-Horseman creature")

    },
    card("sniffer") {

        action("Make each creature lose elusive this turn", LoseElusiveThisTurn)
        fidelity(FidelityLevel.FULL, "Action: for the remainder of the turn, each creature loses elusive")

    },
    card("scout") {

        play(GrantSkirmishAndFight(2))
        fidelity(FidelityLevel.FULL, "Play: up to 2 friendly creatures gain skirmish this turn, then fight with them one at a time")

    },
    card("blypyp") {

        reap(NextCreatureOfHouseEntersReady(House.MARS))
        fidelity(FidelityLevel.FULL, "Reap: the next Mars creature you play this turn enters play ready")

    },
    card("numquid-the-fair") {

        // The first destruction is unconditional; only the repeats depend on the board count.
        play(Sequence(listOf(DestroyCreatureNow, RepeatWhile(OpponentHasMoreCreatures, DestroyCreatureNow))))
        fidelity(FidelityLevel.FULL, "Play: destroy an enemy creature; repeat while your opponent still controls more creatures than you")

    },

)
