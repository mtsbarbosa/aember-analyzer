package com.aember.analyzer.domain.engine.cards

import com.aember.analyzer.domain.engine.dsl.*
import com.aember.analyzer.domain.engine.fidelity.FidelityLevel
import com.aember.analyzer.domain.model.House

/** Amber economy: steal/capture/gain/lose, forge and key-cost effects, grafts and clamps. */
val dslCardOverridesAmber: List<Pair<String, CardDefinition>> = listOf(
    card("doorstep-to-heaven") {

        play(ReduceAmberTo(threshold = 6, cap = 5))
        fidelity(FidelityLevel.FULL, "Each player with 6 or more amber is reduced to 5")
    
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
    card("vigor") {

        play(Sequence(listOf(Heal(3, AnyCreature()), Conditional(HealedAtLeast(3), GainAmber(1)))))
        fidelity(FidelityLevel.FULL, "Heals up to 3 damage from a creature; gains 1 amber when 3 was healed")
    
    },
    card("seeker-needle") {

        action(
            "Deal 1D to a creature",
            Sequence(listOf(DealDamage(1, AnyCreature()), Conditional(DamageDestroyedTarget, GainAmber(1))))
        )
        fidelity(FidelityLevel.FULL, "Deals 1 damage; gains 1 amber when the damage destroys the creature")
    
    },
    card("dextre") {

        play(CaptureAmber(1, SelfCreature))
        destroyed(PutSelfOnTopOfDeck)
        fidelity(FidelityLevel.FULL, "Play: captures 1 amber; Destroyed: put Dextre on top of your deck")
    
    },
    card("terms-of-redress") {

        play(CaptureAmber(2, FriendlyCreature()))
        fidelity(FidelityLevel.FULL, "A chosen friendly creature captures 2 amber")
    
    },
    card("ritual-of-balance") {

        action("If your opponent has 6A or more steal 1A", Conditional(OpponentAmberAtLeast(6), StealAmber(1)))
        fidelity(FidelityLevel.FULL, "Steals 1 amber when the opponent has 6 or more amber")
    
    },
    card("burn-the-stockpile") {

        play(Conditional(OpponentAmberAtLeast(7), LoseAmber(4, OpponentPlayer)))
        fidelity(FidelityLevel.FULL, "All effect sentences mapped")
    
    },
    card("dr-escotera") {

        play(GainAmberEqualToOpponentKeys(SelfPlayer))
        fidelity(FidelityLevel.FULL, "All effect sentences mapped")
    
    },
    card("flaxia") {

        play(Conditional(OpponentHasFewerCreatures, GainAmber(2)))
        fidelity(FidelityLevel.FULL, "All effect sentences mapped")
    
    },
    card("ghostly-hand") {

        play(Conditional(OpponentAmberExactly(1), StealAmber(1)))
        fidelity(FidelityLevel.FULL, "All effect sentences mapped")
    
    },
    card("kelifi-dragon") {

        playableIf(SelfAmberAtLeast(7))
        reap(Sequence(listOf(GainAmber(1), DealDamage(5, AnyCreature()))))
        fight(Sequence(listOf(GainAmber(1), DealDamage(5, AnyCreature()))))
        fidelity(FidelityLevel.FULL, "All effect sentences mapped")
    
    },
    card("lomir-flamefist") {

        play(Conditional(OpponentAmberAtLeast(7), LoseAmber(2, OpponentPlayer)))
        fidelity(FidelityLevel.FULL, "All effect sentences mapped")
    
    },
    card("nerve-blast") {

        play(Sequence(listOf(StealAmber(1), Conditional(StealSucceeded, DealDamage(2, AnyCreature())))))
        fidelity(FidelityLevel.FULL, "All effect sentences mapped")
    
    },
    card("neuro-syphon") {

        play(Conditional(OpponentHasMoreAmber, Sequence(listOf(StealAmber(1), Draw(1, SelfPlayer)))))
        fidelity(FidelityLevel.FULL, "All effect sentences mapped")
    
    },
    card("relentless-whispers") {

        play(Sequence(listOf(DealDamage(2, AnyCreature()), Conditional(DamageDestroyedTarget, StealAmber(1)))))
        fidelity(FidelityLevel.FULL, "All effect sentences mapped")
    
    },
    card("shooler") {

        play(Conditional(OpponentAmberAtLeast(4), StealAmber(1)))
        fidelity(FidelityLevel.FULL, "All effect sentences mapped")
    
    },
    card("skeleton-key") {

        action("A friendly creature captures 1A", CaptureAmber(1, FriendlyCreature()))
        fidelity(FidelityLevel.FULL, "All effect sentences mapped")
    
    },
    card("smith") {

        play(Conditional(OpponentHasFewerCreatures, GainAmber(2)))
        fidelity(FidelityLevel.FULL, "All effect sentences mapped")
    
    },
    card("the-terror") {

        play(Conditional(OpponentAmberAtMost(0), GainAmber(2)))
        fidelity(FidelityLevel.FULL, "All effect sentences mapped")
    
    },
    card("warsong") {

        play(GainAmberOnTriggerThisTurn(1, TurnTrigger.FRIENDLY_CREATURE_FIGHTS))
        fidelity(FidelityLevel.FULL, "All effect sentences mapped")
    
    },
    card("hypnotic-command") {

        play(CaptureEach(1, EnemyCreature(Scope.ALL), maxTargets = { _, player ->
            player.creatures.count { it.house == House.MARS }
        }))
        fidelity(FidelityLevel.FULL, "Play: for each friendly Mars creature, an enemy creature captures 1A from its own side")

    },
    card("mindwarper") {

        action("Choose an enemy creature", CaptureEach(1, EnemyCreature()))
        fidelity(FidelityLevel.FULL, "Action: an enemy creature captures 1A from its own side")

    },
    card("pandemonium") {

        play(Sequence(listOf(
            CaptureEach(1, EachFriendlyUndamagedCreature),
            CaptureEach(1, EachEnemyUndamagedCreature, from = SelfPlayer)
        )))
        fidelity(FidelityLevel.FULL, "Play: each undamaged creature captures 1A from its opponent")

    },
    card("honorable-claim") {

        play(CaptureEach(1, FriendlyCreatureWithTrait("knight", Scope.ALL)))
        fidelity(FidelityLevel.FULL, "Play: each friendly Knight creature captures 1A")

    },
    card("unguarded-camp") {

        playableIf(SelfHasMoreCreatures)
        play(CaptureEach(1, FriendlyCreature(Scope.ALL), maxTargets = { game, player ->
            game.opponent(player).creatures.size.let { player.creatures.size - it }
        }))
        fidelity(FidelityLevel.FULL, "Play: one friendly creature per excess creature captures 1A (only playable when ahead on creatures)")

    },
    card("gatekeeper") {

        // keyteki gates only the effect on the amber check: the creature is always playable.
        play(Conditional(OpponentAmberAtLeast(7), CaptureAmberDynamic({ game, player -> (game.opponent(player).amber - 5).coerceAtLeast(0) }, SelfCreature)))
        fidelity(FidelityLevel.FULL, "Play: if the opponent has 7A or more, capture all but 5 of it")

    },
    card("interdimensional-graft") {

        play(OpponentNextForgeGivesAmber)
        fidelity(FidelityLevel.FULL, "Play: if the opponent forges a key next turn, they give you their remaining amber")

    },
    card("selwyn-the-fence") {

        reap(MoveAmberFromOwnCardToPool)
        fight(MoveAmberFromOwnCardToPool)
        fidelity(FidelityLevel.FULL, "Fight/Reap: move 1 amber from one of your cards to your pool")

    },
    card("blood-money") {

        play(PlaceAmberOnEnemyCreature(2))
        fidelity(FidelityLevel.FULL, "Play: place 2 amber from the common supply on an enemy creature")

    },
)
