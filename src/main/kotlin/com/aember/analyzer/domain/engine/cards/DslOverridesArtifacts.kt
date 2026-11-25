package com.aember.analyzer.domain.engine.cards

import com.aember.analyzer.domain.engine.dsl.*
import com.aember.analyzer.domain.engine.events.*
import com.aember.analyzer.domain.engine.fidelity.FidelityLevel
import com.aember.analyzer.domain.model.House

/** Artifact economy: artifact targeting, artifact returns, opponent-artifact use (Gorm of Omm, Whispering Reliquary, Nexus/Remote Access, Vezyma). */
val dslCardOverridesArtifacts: List<Pair<String, CardDefinition>> = listOf(
    card("nexus") {

        reap(UseOpponentArtifact)
        fidelity(FidelityLevel.FULL, "Elusive keyword; Reap: use an opponent's ready artifact (first one with an Action ability) as if it were yours")

    },
    card("remote-access") {

        play(UseOpponentArtifact)
        fidelity(FidelityLevel.FULL, "Play: use an opponent's ready artifact (first one with an Action ability) as if it were yours")

    },
    card("gorm-of-omm") {

        omni(Sequence(listOf(SacrificeSelf, DestroyArtifact())))
        fidelity(FidelityLevel.FULL, "Omni: sacrifice Gorm of Omm and destroy an artifact")

    },
    card("whispering-reliquary") {

        action("Return an artifact to its owner's hand", ReturnArtifactToHand())
        fidelity(FidelityLevel.FULL, "Action: return an artifact to its owner's hand")

    },
    card("veylan-analyst") {

        reaction(
            "each time you use an artifact, gain 1 amber",
            GainAmber(1),
            matches = { _, player, _, event ->
                event is ArtifactUsed && event.playerIndex == player.index
            }
        )
        fidelity(FidelityLevel.FULL, "Each time you use an artifact, gain 1 amber")

    },
    card("vezyma-thinkdrone") {

        reap(ArchiveFriendlyCreatureOrArtifact)
        fidelity(FidelityLevel.FULL, "Reap: archive a friendly creature or artifact from play")

    },
    card("grasping-vines") {

        play(ReturnArtifactToHand(maxCount = 3))
        fidelity(FidelityLevel.FULL, "Play: return up to 3 artifacts to their owners' hands")

    },

    card("poltergeist") {

        play(UseArtifactAndDestroyIt)
        fidelity(FidelityLevel.FULL, "Play: use an artifact controlled by any player as if it were yours, then destroy it")

    },
    card("imperial-traitor") {

        play(PurgeEnemyHandCardOfHouse(House.SANCTUM))
        fidelity(FidelityLevel.FULL, "Play: look at the opponent's hand; purge a Sanctum card in it")

    },
    card("incubation-chamber") {

        omni(ArchiveHouseCreatureFromHand(House.MARS))
        fidelity(FidelityLevel.FULL, "Omni: reveal a Mars creature from your hand and archive it")

    },
    card("mobius-scroll") {

        action("Archive Mobius Scroll and up to 2 cards from your hand", Sequence(listOf(ArchiveSelf, ArchiveFromHand(2, SelfPlayer))))
        fidelity(FidelityLevel.FULL, "Action: archive Mobius Scroll and up to 2 cards from your hand")

    },
    card("swap-widget") {

        action("Rotate a Mars creature out and another one in", RotateHouseCreatures(House.MARS))
        fidelity(FidelityLevel.FULL, "Action: return a ready friendly Mars creature to hand, play a different-named Mars creature from hand, then ready it")

    },
    card("evasion-sigil") {

        reaction(
            "before a creature fights, discard the top card of its controller's deck; cancel the fight when it matches the active house",
            EvasionSigilDiscardAndCancel,
            matches = { _, _, _, event -> event is FightDeclared }
        )
        fidelity(FidelityLevel.FULL, "Before a creature fights, discard the top card of its controller's deck; if it is of the active house, the fight is cancelled")

    },
    card("world-tree") {

        action("Return a creature from your discard pile to the top of your deck", ReturnDiscardCreatureToTopOfDeck)
        fidelity(FidelityLevel.FULL, "Action: return a creature from your discard pile to the top of your deck")

    },

    card("barehanded") {

        play(ReturnAllArtifactsToTopOfDeck)
        fidelity(FidelityLevel.FULL, "Play: put each artifact on top of its owner's deck")

    },
    card("ritual-of-the-hunt") {

        omni(Sequence(listOf(SacrificeSelf, MayUseHouseThisTurn(House.UNTAMED))))
        fidelity(FidelityLevel.FULL, "Omni: sacrifice Ritual of the Hunt; you may use friendly Untamed creatures for the remainder of the turn")

    },

)
