package com.aember.analyzer.domain.engine.cards

import com.aember.analyzer.domain.engine.dsl.*
import com.aember.analyzer.domain.engine.fidelity.FidelityLevel

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
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("blood-money") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("brothers-in-battle") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("burn-the-stockpile") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("champion-s-challenge") {
            play(Sequence(listOf(Destroy(AnyCreature(Scope.ALL)), Destroy(AnyCreature(Scope.ALL)))))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("coward-s-end") {
            play(Sequence(listOf(Destroy(AnyCreature(Scope.ALL)), GainChains(3, SelfPlayer))))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("follow-the-leader") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("lava-ball") {
            play(DealDamage(4, AnyCreature(), splash = 2))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("loot-the-bodies") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
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
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("sound-the-horns") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("tremor") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("unguarded-camp") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("warsong") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("autocannon") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("banner-of-battle") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
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
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("mighty-javelin") {
            omni(DealDamage(4, AnyCreature()))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("pile-of-skulls") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
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
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
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
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("kelifi-dragon") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("king-of-the-crag") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("krump") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("lomir-flamefist") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("looter-goblin") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("mugwump") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("pingle-who-annoys") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("rock-hurling-giant") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("rogue-ogre") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("smaaash") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("tireless-crocag") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
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
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("phoenix-heart") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("yo-mama-mastery") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
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
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
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
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
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
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("poltergeist") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("red-hot-armor") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("three-fates") {
            play(DestroyMostPowerful(3))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("annihilation-ritual") {
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
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("sacrificial-altar") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("screaming-cave") {
            action("Shuffle your hand and discard pile into your deck", ShuffleHandAndDiscardIntoDeck(SelfPlayer))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("soul-snatcher") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
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
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("ember-imp") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("gabos-longarms") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("overlord-greking") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("guardian-demon") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
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
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("restringuntus") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("shaffles") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("shooler") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("snudge") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("stealer-of-souls") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("succubus") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("tentacus") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("the-terror") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("tocsin") {
            reap(DiscardRandom(1, OpponentPlayer))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("tolas") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
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
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("bouncing-deathquark") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("dimension-door") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("effervescent-principle") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("foggify") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("help-from-future-self") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("interdimensional-graft") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
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
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("neuro-syphon") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("phase-shift") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("positron-bolt") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("random-access-archives") {
            play(ArchiveTopDeck(SelfPlayer))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("remote-access") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("reverse-time") {
            play(SwapDeckAndDiscard(SelfPlayer))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("scrambler-storm") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("sloppy-labwork") {
            play(ArchiveFromHand(1, SelfPlayer))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("twin-bolt-emission") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("wild-wormhole") {
            play(PlayTopCard(SelfPlayer))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("anomaly-exploiter") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("chaos-portal") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
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
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
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
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
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
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("dextre") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("doc-bookton") {
            reap(Draw(1, SelfPlayer))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("dr-escotera") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
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
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("neutron-shark") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
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
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("replicator") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("research-smoko") {
            destroyed(ArchiveTopDeck(SelfPlayer))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("skippy-timehog") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("timetraveller") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("titan-mechanic") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("vespilon-theorist") {
            reap(GainAmber(1))
            fidelity(FidelityLevel.APPROXIMATED, "Approximated; complex interaction ignored")
        },
    card("veylan-analyst") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
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
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("ammonia-clouds") {
            play(DealDamage(3, AnyCreature(Scope.ALL)))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("battle-fleet") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("deep-probe") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("emp-blast") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("hypnotic-command") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("irradiated-mber") {
            play(DealDamage(3, EnemyCreature(Scope.ALL)))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("key-abduction") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("martian-hounds") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("martians-make-bad-allies") {
            play(GainAmber(1))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("mass-abduction") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("mating-season") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
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
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
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
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("soft-landing") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("squawker") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("total-recall") {
            play(GainAmber(1))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("combat-pheromones") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("commpod") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
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
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("incubation-chamber") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("mothergun") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("sniffer") {
            action("For the remainder of the turn each creature loses elusive", NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("swap-widget") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("blypyp") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("chuff-ape") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
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
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("john-smyth") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("mindwarper") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("phylyx-the-disintegrator") {
            action("Your opponent loses 1A for each other friendly Mars creature", LoseAmber(1, OpponentPlayer))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("qyxxlyx-plague-master") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("tunk") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
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
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("yxili-marauder") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("yxilo-bolter") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("yxilx-dominator") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("zorg") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("zyzzix-the-many") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("biomatrix-backup") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("brain-stem-antenna") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("jammer-pack") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("red-planet-ray-gun") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("begone") {
            play(GainAmber(1))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("blinding-light") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("charge") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("cleansing-wave") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("clear-mind") {
            play(Unstun(FriendlyCreature(Scope.ALL)))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("doorstep-to-heaven") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("glorious-few") {
            play(GainAmber(1))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("honorable-claim") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
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
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("take-hostages") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("terms-of-redress") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("the-harder-they-come") {
            play(Purge(AnyCreature()))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("the-spirit-s-way") {
            play(Destroy(AnyCreature(Scope.ALL)))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("virtuous-works") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("epic-quest") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("gorm-of-omm") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("hallowed-blaster") {
            action("Heal 3 damage from a creature", Heal(3, AnyCreature()))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("potion-of-invulnerability") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("round-table") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("sigil-of-brotherhood") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("whispering-reliquary") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("bulwark") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("champion-anaphiel") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
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
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("grey-monk") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("hayyel-the-merchant") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
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
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("horseman-of-war") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("jehu-the-bureaucrat") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("lady-maxena") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("lord-golgotha") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("numquid-the-fair") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("protectrix") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("raiding-knight") {
            play(CaptureAmber(1, FriendlyCreature()))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("sanctum-guardian") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("sequis") {
            reap(CaptureAmber(1, FriendlyCreature()))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("sergeant-zakiel") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("staunch-knight") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("gatekeeper") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("the-vaultkeeper") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
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
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("shoulder-armor") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("bait-and-switch") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("booby-trap") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("finishing-blow") {
            play(StealAmber(1))
            fidelity(FidelityLevel.APPROXIMATED, "Approximated; complex interaction ignored")
        },
    card("ghostly-hand") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
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
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
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
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
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
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("poison-wave") {
            play(DealDamage(2, AnyCreature(Scope.ALL)))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("relentless-whispers") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
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
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("evasion-sigil") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("longfused-mines") {
            omni(DealDamage(3, EnemyCreature(Scope.ALL)))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("masterplan") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("safe-place") {
            action("Move 1A from your pool to Safe Place", NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("seeker-needle") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("skeleton-key") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("special-delivery") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
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
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("carlo-phantom") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("deipno-spymaster") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("faygin") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("macis-asp") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("mack-the-knife") {
            action("Deal 1D to a creature", Sequence(listOf(DealDamage(1, AnyCreature()), GainAmber(1))))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("magda-the-rat") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("mooncurser") {
            fight(StealAmber(1))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("nexus") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
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
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("shadow-self") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("silvertooth") {
            play(NoEffect)
            fidelity(FidelityLevel.PARTIAL, "Effect text present but not yet implemented")
        },
    card("smiling-ruth") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
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
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("ring-of-invisibility") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("silent-dagger") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("cooperative-hunting") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
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
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("key-charge") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("lifeweb") {
            play(StealAmber(2))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("lost-in-the-woods") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("mimicry") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("nature-s-call") {
            play(ReturnToHand(AnyCreature(Scope.ALL)))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("nocturnal-maneuver") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
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
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
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
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("word-of-returning") {
            play(DealDamage(1, EnemyCreature(Scope.ALL)))
            fidelity(FidelityLevel.PARTIAL, "Mapped simple parts; complex interaction not implemented")
        },
    card("bear-flute") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("nepenthe-seed") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("ritual-of-balance") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("ritual-of-the-hunt") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("world-tree") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("ancient-bear") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("bigtwig") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("witch-of-the-wilds") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("briar-grubbling") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("chota-hazri") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("dew-faerie") {
            reap(GainAmber(1))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("dust-pixie") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("flaxia") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
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
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
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
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("lupo-the-scarred") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("mighty-tiger") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("murmook") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("mushroom-man") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("niffle-ape") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("niffle-queen") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("piranha-monkeys") {
            play(DealDamage(2, AnyCreature(Scope.ALL)))
            reap(DealDamage(2, AnyCreature(Scope.ALL)))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("teliga") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("hunting-witch") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("witch-of-the-eye") {
            reap(ReturnFromDiscardToHand(1, SelfPlayer))
            fidelity(FidelityLevel.FULL, "All effect sentences mapped")
        },
    card("way-of-the-bear") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            },
    card("way-of-the-wolf") {
                // Implemented by an override list in CoreCards.kt; not generated.
                play(NoEffect)
                fidelity(FidelityLevel.PARTIAL, "Overridden: real implementation lives in an override list")
            })
