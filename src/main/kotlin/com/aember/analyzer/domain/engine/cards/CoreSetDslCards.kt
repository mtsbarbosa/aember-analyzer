package com.aember.analyzer.domain.engine.cards

import com.aember.analyzer.domain.engine.dsl.*
import com.aember.analyzer.domain.engine.fidelity.FidelityLevel
import com.aember.analyzer.domain.model.House

/**
 * Auto-generated Core set card definitions from keyteki CotA.json.
 * Cards not listed here resolve as vanilla.
 */
val coreSetCards = listOf(
    card("anger") {
            play(ReadyAndFight(FriendlyCreature()))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("barehanded") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("blood-money") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("brothers-in-battle") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("burn-the-stockpile") {
            play(Conditional(OpponentAmberAtLeast(7), LoseAmber(4, OpponentPlayer)))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("champions-challenge") {
            play(Sequence(listOf(Destroy(AnyCreature(Scope.ALL)), Destroy(AnyCreature(Scope.ALL)))))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("cowards-end") {
            play(Sequence(listOf(Destroy(AnyCreature(Scope.ALL)), GainChains(3, SelfPlayer))))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("follow-the-leader") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("lava-ball") {
            play(DealDamage(4, AnyCreature()))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("loot-the-bodies") {
            play(GainAmber(1))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("take-that-smartypants") {
            play(StealAmber(2))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("punch") {
            play(DealDamage(3, AnyCreature()))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("relentless-assault") {
            play(ReadyAndFight(FriendlyCreature(Scope.ALL)))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("smith") {
            play(Conditional(OpponentHasFewerCreatures, GainAmber(2)))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("sound-the-horns") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("tremor") {
            play(Stun(CreatureAndNeighbors(AnyCreature())))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("unguarded-camp") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("warsong") {
            play(GainAmberOnTriggerThisTurn(1, TurnTrigger.FRIENDLY_CREATURE_FIGHTS))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("autocannon") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("banner-of-battle") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("cannon") {
            action("Deal 2D to a creature", DealDamage(2, AnyCreature()))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("gauntlet-of-command") {
            action("Ready and fight with a friendly creature", ReadyAndFight(FriendlyCreature()))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("iron-obelisk") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("mighty-javelin") {
            omni(DealDamage(4, AnyCreature()))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("pile-of-skulls") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("screechbomb") {
            omni(LoseAmber(2, OpponentPlayer))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("the-warchest") {
            action("Gain 1A for each enemy creature that was destroyed in a fight this turn", GainAmber(1))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("bilgum-avalanche") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("valdr") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("bumpsy") {
            play(LoseAmber(1, OpponentPlayer))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("earthshaker") {
            play(DestroyWeakCreatures(3))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("firespitter") {
            beforeFight(DealDamage(1, EnemyCreature(Scope.ALL)))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("ganger-chieftain") {
            play(ReadyAndFight(FriendlyCreature()))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("grenade-snib") {
            destroyed(LoseAmber(2, OpponentPlayer))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("headhunter") {
            fight(GainAmber(1))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("hebe-the-huge") {
            play(DealDamage(2, EachOtherUndamagedCreature))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("kelifi-dragon") {
            playableIf(SelfAmberAtLeast(7))
            reap(Sequence(listOf(GainAmber(1), DealDamage(5, AnyCreature()))))
            fight(Sequence(listOf(GainAmber(1), DealDamage(5, AnyCreature()))))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("king-of-the-crag") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("krump") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("lomir-flamefist") {
            play(Conditional(OpponentAmberAtLeast(7), LoseAmber(2, OpponentPlayer)))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("looter-goblin") {
            reap(GainAmber(1))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("mugwump") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("pingle-who-annoys") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("rock-hurling-giant") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("rogue-ogre") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("smaaash") {
            play(Stun(AnyCreature()))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("tireless-crocag") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("troll") {
            reap(Heal(3, FriendlyCreature()))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("wardrummer") {
            play(ReturnToHand(OtherFriendlyCreatureWithHouse(com.aember.analyzer.domain.model.House.BROBNAR, Scope.ALL)))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("blood-of-titans") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("phoenix-heart") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("yo-mama-mastery") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("a-fair-game") {
            play(GainAmber(1))
            fidelity(FidelityLevel.APPROXIMATED, "Approximated; complex interaction ignored")
        },
    card("arise") {
            play(GainChains(1, SelfPlayer))
            fidelity(FidelityLevel.APPROXIMATED, "Approximated; complex interaction ignored")
        },
    card("control-the-weak") {
            // Effect implemented manually in CoreCards.kt to choose the best house to deny.
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "House-denial effect implemented via card script")
        },
    card("creeping-oblivion") {
            play(PurgeFromDiscard(2, SelfPlayer))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("dance-of-doom") {
            play(Destroy(AnyCreature(Scope.ALL)))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("fear") {
            play(ReturnToHand(EnemyCreature(Scope.ONE)))
            fidelity(FidelityLevel.PARTIAL, "Returns one enemy creature chosen greedily; card text is singular")
        },
    card("gateway-to-dis") {
            play(Sequence(listOf(Destroy(AnyCreature(Scope.ALL)), GainChains(3, SelfPlayer))))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("gongoozle") {
            play(Sequence(listOf(DealDamage(3, AnyCreature()), DiscardRandom(1, OpponentPlayer))))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("guilty-hearts") {
            play(Destroy(AnyCreature(Scope.ALL)))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("hand-of-dis") {
            play(Destroy(AnyCreature()))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("hecatomb") {
            play(Destroy(AnyCreature(Scope.ALL)))
            fidelity(FidelityLevel.APPROXIMATED, "Approximated; complex interaction ignored")
        },
    card("tendrils-of-pain") {
            play(DealDamage(1, AnyCreature(Scope.ALL)))
            fidelity(FidelityLevel.APPROXIMATED, "Approximated; complex interaction ignored")
        },
    card("hysteria") {
            play(ReturnToHand(AnyCreature(Scope.ALL)))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("key-hammer") {
            play(GainAmberOpponent(6))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("mind-barb") {
            play(DiscardRandom(1, OpponentPlayer))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("pandemonium") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("poltergeist") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("red-hot-armor") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("three-fates") {
            play(DestroyMostPowerful(3))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("annihilation-ritual-") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("dominator-bauble") {
            action("Use a friendly creature", UseCreature(FriendlyCreature()))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("key-to-dis") {
            omni(Destroy(AnyCreature(Scope.ALL)))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("lash-of-broken-dreams") {
            action("Keys cost 3A during your opponents next turn", NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("library-of-the-damned") {
            action("Archive a card", ArchiveFromHand(1, SelfPlayer))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("lifeward") {
            omni(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("sacrificial-altar") {
            action("Purge a friendly Human creature from play", NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("screaming-cave") {
            action("Shuffle your hand and discard pile into your deck", ShuffleHandAndDiscardIntoDeck(SelfPlayer))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("soul-snatcher") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("charette") {
            play(CaptureAmber(3, FriendlyCreature()))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("drumble") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("dust-imp") {
            destroyed(GainAmber(2))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("eater-of-the-dead") {
            reap(Purge(AnyCreature()))
            fight(Purge(AnyCreature()))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("ember-imp") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("gabos-longarms") {
            beforeFight(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("overlord-greking") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("guardian-demon") {
            play(NoEffect)
            reap(NoEffect)
            fight(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("master-of-1") {
            reap(Destroy(AnyCreature()))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("master-of-2") {
            reap(Destroy(AnyCreature()))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("master-of-3") {
            reap(Destroy(AnyCreature()))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("pit-demon") {
            action("Steal 1A", StealAmber(1))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("pitlord") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("restringuntus") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("shaffles") {
            play(NoEffect)
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("shooler") {
            play(Conditional(OpponentAmberAtLeast(4), StealAmber(1)))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("snudge") {
            reap(NoEffect)
            fight(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("stealer-of-souls") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("succubus") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("tentacus") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("the-terror") {
            play(Conditional(OpponentAmberAtMost(0), GainAmber(2)))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("tocsin") {
            reap(DiscardRandom(1, OpponentPlayer))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("tolas") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("truebaru") {
            destroyed(GainAmber(5))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("collar-of-subordination") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("flame-wreathed") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("bouncing-deathquark") {
            play(Sequence(listOf(Destroy(EnemyCreature()), Destroy(FriendlyCreature()))))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("dimension-door") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("effervescent-principle") {
            play(GainChains(1, SelfPlayer))
            fidelity(FidelityLevel.PARTIAL, "Some effect sentences not mapped")
        },
    card("foggify") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("help-from-future-self") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("interdimensional-graft") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("knowledge-is-power") {
            play(GainAmber(1))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("labwork") {
            play(ArchiveFromHand(1, SelfPlayer))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("library-access") {
            play(DrawOnCardPlayThisTurn())
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("neuro-syphon") {
            play(Conditional(OpponentHasMoreAmber, Sequence(listOf(StealAmber(1), Draw(1, SelfPlayer)))))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("phase-shift") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("positron-bolt") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("random-access-archives") {
            play(ArchiveTopDeck(SelfPlayer))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("remote-access") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("reverse-time") {
            play(SwapDeckAndDiscard(SelfPlayer))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("scrambler-storm") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("sloppy-labwork") {
            play(ArchiveFromHand(1, SelfPlayer))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("twin-bolt-emission") {
            play(DealDamage(2, AnyCreature()))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("wild-wormhole") {
            play(PlayTopCard(SelfPlayer))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("anomaly-exploiter") {
            action("Destroy a damaged creature", NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("chaos-portal") {
            action("Choose a house", NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("crazy-killing-machine") {
            action("Discard the top card of each players deck", Destroy(AnyCreature()))
            fidelity(FidelityLevel.APPROXIMATED, "Approximated; complex interaction ignored")
        },
    card("library-of-babble") {
            action("Draw a card", Draw(1, SelfPlayer))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("mobius-scroll") {
            action("Archive Mobius Scroll and up to 2 cards from your hand", NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("pocket-universe") {
            action("Move 1A from your pool to Pocket Universe", NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("spangler-box") {
            action("Purge a creature in play", Purge(AnyCreature()))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("spectral-tunneler") {
            action("Choose a creature", Draw(1, SelfPlayer))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("strange-gizmo") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("the-howling-pit") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("batdrone") {
            fight(StealAmber(1))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("brain-eater") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("dextre") {
            play(CaptureAmber(1, FriendlyCreature()))
            destroyed(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("doc-bookton") {
            reap(Draw(1, SelfPlayer))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("dr-escotera") {
            play(GainAmberEqualToOpponentKeys(SelfPlayer))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("dysania") {
            play(GainAmber(1))
            fidelity(FidelityLevel.APPROXIMATED, "Approximated; complex interaction ignored")
        },
    card("ganymede-archivist") {
            reap(ArchiveFromHand(1, SelfPlayer))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("harland-mindlock") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("quixo-the-adventurer") {
            fight(Draw(1, SelfPlayer))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("mother") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("neutron-shark") {
            val neutronEffect = Sequence(listOf(Destroy(AnyCreature()), Conditional(DamageDestroyedTarget, DiscardTopDeck(SelfPlayer))))
            play(neutronEffect)
            reap(neutronEffect)
            fight(neutronEffect)
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("novu-archaeologist") {
            action("Archive a card from your discard pile", ArchiveFromDiscard(1, SelfPlayer))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("ozmo-martianologist") {
            reap(Heal(3, AnyCreature()))
            fight(Heal(3, AnyCreature()))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("psychic-bug") {
            play(NoEffect)
            reap(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("replicator") {
            reap(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("research-smoko") {
            destroyed(ArchiveTopDeck(SelfPlayer))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("skippy-timehog") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("timetraveller") {
            play(Draw(2, SelfPlayer))
            action("Shuffle Timetraveller into your deck", NoEffect)
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("titan-mechanic") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("vespilon-theorist") {
            reap(GainAmber(1))
            fidelity(FidelityLevel.APPROXIMATED, "Approximated; complex interaction ignored")
        },
    card("veylan-analyst") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("experimental-therapy") {
            play(Sequence(listOf(Stun(AnyCreature()), Exhaust(AnyCreature()))))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("rocket-boots") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("transposition-sandals") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("ammonia-clouds") {
            play(DealDamage(3, AnyCreature(Scope.ALL)))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("battle-fleet") {
            play(Draw(1, SelfPlayer))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("deep-probe") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("emp-blast") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("hypnotic-command") {
            play(TakeControlUntilEndOfTurn(EnemyCreature()))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("irradiated-mber") {
            play(DealDamage(3, EnemyCreature(Scope.ALL)))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("key-abduction") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("martian-hounds") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("martians-make-bad-allies") {
            play(GainAmber(1))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("mass-abduction") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("mating-season") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("mothership-support") {
            play(DealDamage(2, AnyCreature()))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("orbital-bombardment") {
            play(DealDamage(2, AnyCreature()))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("phosphorus-stars") {
            play(Sequence(listOf(Stun(AnyCreature(Scope.ALL)), GainChains(2, SelfPlayer))))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("psychic-network") {
            play(StealAmber(1))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("sample-collection") {
            play(Archive(EnemyCreature()))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("shatter-storm") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("soft-landing") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("squawker") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("total-recall") {
            play(GainAmber(1))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("combat-pheromones") {
            omni(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("commpod") {
            action("Reveal any number of Mars cards from your hand", NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("crystal-hive") {
            action("For the remainder of the turn gain 1A each time a creature reaps", GainAmber(1))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("custom-virus") {
            omni(Sequence(listOf(Purge(AnyCreature()), Destroy(AnyCreature(Scope.ALL)))))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("feeding-pit") {
            action("Discard a creature from your hand", GainAmber(1))
            fidelity(FidelityLevel.APPROXIMATED, "Approximated; complex interaction ignored")
        },
    card("invasion-portal") {
            action("Discard cards from the top of your deck until you discard a Mars creature or run out of cards", AddTopCardOfHouseToHand(House.MARS, SelfPlayer))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("incubation-chamber") {
            omni(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("mothergun") {
            action("Reveal any number of Mars cards from your hand", NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("sniffer") {
            action("For the remainder of the turn each creature loses elusive", NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("swap-widget") {
            action("Return a ready friendly Mars creature to your hand", NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("blypyp") {
            reap(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("chuff-ape") {
            reap(NoEffect)
            fight(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("ether-spider") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("grabber-jammer") {
            reap(CaptureAmber(1, FriendlyCreature()))
            fight(CaptureAmber(1, FriendlyCreature()))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("grommid") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("john-smyth") {
            reap(NoEffect)
            fight(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("mindwarper") {
            action("Choose an enemy creature", NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("phylyx-the-disintegrator") {
            action("Your opponent loses 1A for each other friendly Mars creature", LoseAmber(1, OpponentPlayer))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("qyxxlyx-plague-master") {
            reap(NoEffect)
            fight(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("tunk") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("ulyq-megamouth") {
            reap(UseCreature(FriendlyCreature()))
            fight(UseCreature(FriendlyCreature()))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("uxlyx-the-zookeeper") {
            reap(Archive(EnemyCreature()))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("vezyma-thinkdrone") {
            reap(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("yxili-marauder") {
            play(CaptureAmber(1, FriendlyCreature()))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("yxilo-bolter") {
            reap(Sequence(listOf(DealDamage(2, AnyCreature()), Conditional(DamageDestroyedTarget, Purge(LastDamagedTarget)))))
            fight(Sequence(listOf(DealDamage(2, AnyCreature()), Conditional(DamageDestroyedTarget, Purge(LastDamagedTarget)))))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("yxilx-dominator") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("zorg") {
            beforeFight(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("zyzzix-the-many") {
            reap(NoEffect)
            fight(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("biomatrix-backup") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("brain-stem-antenna") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("jammer-pack") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("red-planet-ray-gun") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("begone") {
            play(GainAmber(1))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("blinding-light") {
            play(Stun(AllCreaturesOfChosenHouse))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("charge") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("cleansing-wave") {
            play(Sequence(listOf(Heal(1, AnyCreature()), GainAmber(1))))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("clear-mind") {
            play(Unstun(FriendlyCreature(Scope.ALL)))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("doorstep-to-heaven") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("glorious-few") {
            play(GainAmber(1))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("honorable-claim") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("inspiration") {
            play(ReadyAndUse(FriendlyCreature()))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("mighty-lance") {
            play(DealDamage(3, AnyCreature()))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("oath-of-poverty") {
            play(GainAmber(2))
            fidelity(FidelityLevel.APPROXIMATED, "Approximated; complex interaction ignored")
        },
    card("one-stood-against-many") {
            play(ReadyAndFight(FriendlyCreature()))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("radiant-truth") {
            play(Stun(EnemyCreature(Scope.ALL)))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("shield-of-justice") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("take-hostages") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("terms-of-redress") {
            play(CaptureAmber(2, FriendlyCreature()))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("the-harder-they-come") {
            play(Purge(AnyCreature()))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("the-spirits-way") {
            play(Destroy(AnyCreature(Scope.ALL)))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("epic-quest") {
            play(NoEffect)
            omni(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("gorm-of-omm") {
            omni(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("hallowed-blaster") {
            action("Heal 3 damage from a creature", Heal(3, AnyCreature()))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("potion-of-invulnerability") {
            omni(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("round-table") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("sigil-of-brotherhood") {
            omni(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("whispering-reliquary") {
            action("Return an artifact to its owners hand", NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("bulwark") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("champion-anaphiel") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("champion-tabris") {
            fight(CaptureAmber(1, FriendlyCreature()))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("commander-remiel") {
            reap(UseCreature(FriendlyCreature()))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("duma-the-martyr") {
            destroyed(Draw(2, SelfPlayer))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("francus") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("grey-monk") {
            reap(Heal(2, AnyCreature()))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("hayyel-the-merchant") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("horseman-of-death") {
            play(ReturnFromDiscardToHand(3, SelfPlayer))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("horseman-of-famine") {
            play(DestroyLeastPowerful(1))
            reap(DestroyLeastPowerful(1))
            fight(DestroyLeastPowerful(1))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("horseman-of-pestilence") {
            play(NoEffect)
            reap(NoEffect)
            fight(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("horseman-of-war") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("jehu-the-bureaucrat") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("lady-maxena") {
            play(Stun(AnyCreature()))
            action("Return Lady Maxena to its owners hand", NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("lord-golgotha") {
            beforeFight(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("numquid-the-fair") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("protectrix") {
            reap(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("raiding-knight") {
            play(CaptureAmber(1, FriendlyCreature()))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("sanctum-guardian") {
            reap(NoEffect)
            fight(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("sequis") {
            reap(CaptureAmber(1, FriendlyCreature()))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("sergeant-zakiel") {
            play(ReadyAndFight(FriendlyCreature()))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("staunch-knight") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("gatekeeper") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("the-vaultkeeper") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("veemos-lightbringer") {
            play(Destroy(AnyCreature(Scope.ALL)))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("armageddon-cloak") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("mantle-of-the-zealot") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("protect-the-weak") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("shoulder-armor") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("bait-and-switch") {
            play(Conditional(OpponentHasMoreAmber, StealAmber(1)))
            fidelity(FidelityLevel.PARTIAL, "Repeat-until loop not implemented; steals once when ahead")
        },
    card("booby-trap") {
            play(DealDamage(4, AnyCreature()))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("finishing-blow") {
            play(StealAmber(1))
            fidelity(FidelityLevel.APPROXIMATED, "Approximated; complex interaction ignored")
        },
    card("ghostly-hand") {
            play(Conditional(OpponentAmberExactly(1), StealAmber(1)))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("hidden-stash") {
            play(ArchiveFromHand(1, SelfPlayer))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("imperial-traitor") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("key-of-darkness") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("lights-out") {
            play(ReturnToHand(EnemyCreature(Scope.ALL)))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("miasma") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("nerve-blast") {
            play(Sequence(listOf(StealAmber(1), Conditional(StealSucceeded, DealDamage(2, AnyCreature())))))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("one-last-job") {
            play(StealAmber(1))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("oubliette") {
            play(Purge(AnyCreature()))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("pawn-sacrifice") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("poison-wave") {
            play(DealDamage(2, AnyCreature(Scope.ALL)))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("relentless-whispers") {
            play(Sequence(listOf(DealDamage(2, AnyCreature()), Conditional(DamageDestroyedTarget, StealAmber(1)))))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("routine-job") {
            play(Sequence(listOf(StealAmber(1), StealAmber(1))))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("too-much-to-protect") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("treasure-map") {
            play(GainAmber(3))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("customs-office") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("evasion-sigil") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("longfused-mines") {
            omni(DealDamage(3, EnemyCreature(Scope.ALL)))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("masterplan") {
            play(NoEffect)
            omni(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("safe-place") {
            action("Move 1A from your pool to Safe Place", NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("seeker-needle") {
            action("Deal 1D to a creature", Sequence(listOf(DealDamage(1, AnyCreature()), GainAmber(1))))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("skeleton-key") {
            action("A friendly creature captures 1A", CaptureAmber(1, FriendlyCreature()))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("special-delivery") {
            omni(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("speed-sigil") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("subtle-maul") {
            action("Your opponent discards a random card from their hand", DiscardRandom(1, OpponentPlayer))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("the-sting") {
            action("Sacrifice The Sting", NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("bad-penny") {
            destroyed(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("bulleteye") {
            reap(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("carlo-phantom") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("deipno-spymaster") {
            omni(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("faygin") {
            reap(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("macis-asp") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("mack-the-knife") {
            action("Deal 1D to a creature", Sequence(listOf(DealDamage(1, AnyCreature()), GainAmber(1))))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("magda-the-rat") {
            play(StealAmber(2))
            fidelity(FidelityLevel.PARTIAL, "Some effect sentences not mapped")
        },
    card("mooncurser") {
            fight(StealAmber(1))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("nexus") {
            reap(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("noddy-the-thief") {
            action("Steal 1A", StealAmber(1))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("old-bruno") {
            play(CaptureAmber(3, FriendlyCreature()))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("dodger") {
            fight(StealAmber(1))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("selwyn-the-fence") {
            reap(NoEffect)
            fight(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("shadow-self") {
            play(NoEffect)
            fidelity(FidelityLevel.FULL, "Manual override implements damage redirection and zero fight damage")
        },
    card("silvertooth") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("smiling-ruth") {
            reap(TakeControlOfEnemyFlankCreature(SelfPlayer))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("sneklifter") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("umbra") {
            fight(StealAmber(1))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("urchin") {
            play(StealAmber(1))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("duskrunner") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("ring-of-invisibility") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("silent-dagger") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("cooperative-hunting") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("curiosity") {
            play(Destroy(AnyCreature(Scope.ALL)))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("fertility-chant") {
            play(GainAmberOpponent(2))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("fogbank") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("full-moon") {
            play(GainAmber(1))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("grasping-vines") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("key-charge") {
            play(LoseAmber(1, SelfPlayer))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("lifeweb") {
            play(StealAmber(2))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("lost-in-the-woods") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("mimicry") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("natures-call") {
            play(ReturnToHand(AnyCreature(Scope.ALL)))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("nocturnal-maneuver") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("perilous-wild") {
            play(Destroy(AnyCreature(Scope.ALL)))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("regrowth") {
            play(ReturnFromDiscardToHand(1, SelfPlayer))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("save-the-pack") {
            play(Sequence(listOf(Destroy(AnyCreature(Scope.ALL)), GainChains(1, SelfPlayer))))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("scout") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("stampede") {
            play(StealAmber(2))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("the-common-cold") {
            play(DealDamage(1, AnyCreature(Scope.ALL)))
            fidelity(FidelityLevel.APPROXIMATED, "Approximated; complex interaction ignored")
        },
    card("troop-call") {
            play(ReturnFromDiscardToHand(3, SelfPlayer))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("vigor") {
            play(GainAmber(1))
            fidelity(FidelityLevel.APPROXIMATED, "Approximated; complex interaction ignored")
        },
    card("word-of-returning") {
            play(DealDamage(1, EnemyCreature(Scope.ALL)))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("bear-flute") {
            action("Fully heal an Ancient Bear", NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("nepenthe-seed") {
            omni(ReturnFromDiscardToHand(1, SelfPlayer))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("ritual-of-balance") {
            action("If your opponent has 6A or more steal 1A", StealAmber(1))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("ritual-of-the-hunt") {
            omni(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("world-tree") {
            action("Return a creature from your discard pile to the top of your deck", NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("ancient-bear") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("bigtwig") {
            reap(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("witch-of-the-wilds") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("briar-grubbling") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("chota-hazri") {
            play(LoseAmber(1, SelfPlayer))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("dew-faerie") {
            reap(GainAmber(1))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("flaxia") {
            play(Conditional(OpponentHasFewerCreatures, GainAmber(2)))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("fuzzy-gruen") {
            play(GainAmberOpponent(1))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("giant-sloth") {
            action("Gain 3A", GainAmber(3))
            fidelity(FidelityLevel.APPROXIMATED, "Approximated; complex interaction ignored")
        },
    card("halacor") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("inka-the-spider") {
            play(Stun(AnyCreature()))
            reap(Stun(AnyCreature()))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("kindrith-longshot") {
            reap(DealDamage(2, AnyCreature()))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("snufflegator") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("lupo-the-scarred") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("mighty-tiger") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("murmook") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("mushroom-man") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("niffle-ape") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("niffle-queen") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("piranha-monkeys") {
            play(DealDamage(2, AnyCreature(Scope.ALL)))
            reap(DealDamage(2, AnyCreature(Scope.ALL)))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("teliga") {
            play(NoEffect)
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("hunting-witch") {
            play(NoEffect)
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("witch-of-the-eye") {
            reap(ReturnFromDiscardToHand(1, SelfPlayer))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("way-of-the-bear") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("way-of-the-wolf") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        })
