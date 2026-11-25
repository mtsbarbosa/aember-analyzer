package com.aember.analyzer.domain.engine.cards

import com.aember.analyzer.domain.engine.dsl.*
import com.aember.analyzer.domain.engine.fidelity.FidelityLevel
import com.aember.analyzer.domain.model.House

/** Card movement: hand/deck/discard/archive/purge reshuffles, deck scans, Masterplan stacking. */
val dslCardOverridesCardFlow: List<Pair<String, CardDefinition>> = listOf(
    card("timetraveller") {

        play(Draw(2, SelfPlayer))
        action("Shuffle Timetraveller into your deck", ShuffleSelfIntoDeck)
        fidelity(FidelityLevel.FULL, "Play: draw 2 cards; Action: shuffle Timetraveller into its owner's deck")
    
    },
    card("lost-in-the-woods") {

        play(ShuffleCreaturesIntoDeck(friendlyCount = 2, enemyCount = 2))
        fidelity(FidelityLevel.FULL, "Shuffles 2 friendly and 2 enemy creatures into their owners' decks")
    
    },
    card("nepenthe-seed") {

        // Return before the sacrifice so the greedy discard pick does not
        // always grab the seed itself (keyteki resolves sacrifice first).
        omni(Sequence(listOf(ReturnFromDiscardToHand(1, SelfPlayer), SacrificeSelf)))
        fidelity(FidelityLevel.FULL, "Omni: sacrifices Nepenthe Seed and returns a card from your discard pile to your hand")
    
    },
    card("fear") {

        play(ReturnToHand(EnemyCreature(Scope.ONE)))
        fidelity(FidelityLevel.FULL, "Returns one enemy creature to its owner's hand")
    
    },
    card("invasion-portal") {

        action("Discard cards from the top of your deck until you discard a Mars creature or run out of cards", AddTopCardOfHouseToHand(House.MARS, SelfPlayer))
        fidelity(FidelityLevel.FULL, "All effect sentences mapped")
    
    },
    card("library-access") {

        play(DrawOnCardPlayThisTurn())
        fidelity(FidelityLevel.FULL, "All effect sentences mapped")
    
    },
    card("deep-probe") {

        play(DiscardEnemyHandCreaturesOfChosenHouse)
        fidelity(FidelityLevel.FULL, "Play: choose a house (greedy) and discard each enemy creature of that house from their hand")

    },
    card("chaos-portal") {

        action("Choose a house and play the top card of your deck if it is of that house", PlayTopCardIfChosenHouse)
        fidelity(FidelityLevel.FULL, "Action: choose a house (greedy); if the top card of your deck is of that house, play it")

    },
    card("sound-the-horns") {

        play(ScanDeckForHouseCreatureToHand(House.BROBNAR))
        fidelity(FidelityLevel.FULL, "Play: discard from your deck until a Brobnar creature is discarded, then put it into your hand")

    },
    card("faygin") {

        reap(ReturnUrchinToHand)
        fidelity(FidelityLevel.FULL, "Reap: return an Urchin from play or from your discard pile to your hand")

    },
    card("mating-season") {

        play(ShuffleHouseCreaturesIntoDeck(House.MARS, amberPerEach = 1))
        fidelity(FidelityLevel.FULL, "Play: shuffle each Mars creature into its owner's deck; each player gains 1 amber per own creature shuffled")

    },
    card("masterplan") {

        play(StackHandCardBeneath)
        omni(PlayBeneathCardAndSacrifice)
        fidelity(FidelityLevel.FULL, "Play: place a card from your hand facedown beneath Masterplan; Omni: play it and sacrifice Masterplan")

    },

    // The generator never emitted these two (their pack text is the literal "(Vanilla)");
    // registered explicitly so every catalog card has a script.,

    card("help-from-future-self") {

        play(SearchForCreatureToHand("timetraveller", shuffleDiscardIntoDeck = true, maxCards = 1))
        fidelity(FidelityLevel.FULL, "Play: search your deck and discard pile for a Timetraveller and put it into your hand; shuffle your discard pile into your deck")

    },
    card("bear-flute") {

        action("Fully heal an Ancient Bear or search for them", HealOrSearchAncientCreatures("ancient-bear"))
        fidelity(FidelityLevel.FULL, "Action: fully heal an Ancient Bear; if there is none in play, search your deck and discard pile for each Ancient Bear, put them into your hand and shuffle your discard pile into your deck")

    },
    card("mass-abduction") {

        play(AbductEnemyDamagedCreatures(3))
        fidelity(FidelityLevel.FULL, "Play: put up to 3 damaged enemy creatures into your archives; if they leave the archives they return to their owner's hand")

    },
    card("mimicry") {

        play(PlayCopyOfOpponentDiscardAction)
        fidelity(FidelityLevel.FULL, "Play: treat Mimicry as a copy of an action card in your opponent's discard pile (its play effect resolves)")

    },
    card("sanctum-guardian") {

        reap(SwapWithOtherFriendlyCreature)
        fight(SwapWithOtherFriendlyCreature)
        fidelity(FidelityLevel.FULL, "Fight/Reap: swap Sanctum Guardian with another friendly creature in your battleline (taunt from keywords)")

    },
    card("transposition-sandals") {

        action("Swap this creature with another friendly creature; you may use that creature this turn", SwapHostAndMayUseOther)
        fidelity(FidelityLevel.FULL, "Upgrade: the host gains Action: swap it with another friendly creature and you may use that creature this turn")

    },
    card("replicator") {

        reap(TriggerOtherCreatureReap)
        fidelity(FidelityLevel.FULL, "Reap: trigger the reap effect of another creature in play as if you controlled it; that creature does not exhaust")

    },
    card("psychic-bug") {

        play(RevealOpponentHand)
        reap(RevealOpponentHand)
        fidelity(FidelityLevel.FULL, "Play/Reap: look at your opponent's hand")

    },

    card("sacrificial-altar") {

        action("Purge a friendly Human creature and play a creature from your discard pile", PurgeFriendlyHumanAndPlayFromDiscard)
        fidelity(FidelityLevel.FULL, "Action: purge a friendly Human creature from play; if you do, play a creature from your discard pile")

    },

)
