package com.aember.analyzer.domain.engine.cards

import com.aember.analyzer.domain.engine.dsl.*
import com.aember.analyzer.domain.engine.events.*
import com.aember.analyzer.domain.engine.fidelity.FidelityLevel
import com.aember.analyzer.domain.model.CardType
import com.aember.analyzer.domain.model.House

/** Use restrictions and may-use allowances (Phase 2.3 + Sigil/Combat Pheromones/Tentacus/Customs Office). */
val dslCardOverridesRestrictions: List<Pair<String, CardDefinition>> = listOf(
    card("ember-imp") {

    play(NoEffect)
    restrictions {
        opponentPlayLimit(2)
    }
    fidelity(FidelityLevel.FULL, "Opponent cannot play more than 2 cards each turn (enforced on the opponent's own play count)")

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
    card("pitlord") {

    play(NoEffect)
    restrictions {
        forcedHouse(House.DIS)
    }
    fidelity(FidelityLevel.FULL, "While Pitlord is in play, its controller must choose Dis as active house")

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
    card("witch-of-the-wilds") {

    play(NoEffect)
    restrictions {
        mayPlayHouse(House.UNTAMED, perTurn = 1)
    }
    fidelity(FidelityLevel.FULL, "During each turn in which Untamed is not the active house, the controller may play one Untamed card")

    },
    card("tentacus") {

        restrictions { opponentArtifactUseCost(1) }
        fidelity(FidelityLevel.FULL, "Your opponent pays you 1 amber to use an artifact")

    },
    card("customs-office") {

        restrictions { opponentArtifactPlayCost(1) }
        fidelity(FidelityLevel.FULL, "Your opponent pays you 1 amber to play an artifact")

    },
)
