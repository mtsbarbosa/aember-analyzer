package com.aember.analyzer.domain.engine.cards

import com.aember.analyzer.domain.engine.dsl.*
import com.aember.analyzer.domain.engine.effects.EnemyOfHouseScope
import com.aember.analyzer.domain.engine.effects.FriendlyCreaturesScope
import com.aember.analyzer.domain.engine.effects.FriendlyFlankCreaturesScope
import com.aember.analyzer.domain.engine.effects.FriendlyWithTraitScope
import com.aember.analyzer.domain.engine.effects.HostCreatureScope
import com.aember.analyzer.domain.engine.effects.NeighborsOfSelfScope
import com.aember.analyzer.domain.engine.effects.OtherFriendlyCreaturesScope
import com.aember.analyzer.domain.engine.effects.SelfScope
import com.aember.analyzer.domain.engine.effects.PayerScope
import com.aember.analyzer.domain.engine.fidelity.FidelityLevel
import com.aember.analyzer.domain.engine.model.Keyword
import com.aember.analyzer.domain.model.House

/** Persistent/static effects (Phase 2.1 auras, keyword grants, key-cost modifiers, enter-play flags). */
val dslCardOverridesStatic: List<Pair<String, CardDefinition>> = listOf(
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
    card("banner-of-battle") {

        persistent {
            modifyPower(1, FriendlyCreaturesScope)
        }
        fidelity(FidelityLevel.FULL, "Aura: each friendly creature gets +1 power")
    
    },
    card("blood-of-titans") {

        persistent {
            modifyPower(5, HostCreatureScope)
        }
        fidelity(FidelityLevel.FULL, "Upgrade: attached creature gets +5 power")
    
    },
    card("bulwark") {

        persistent {
            modifyArmor(2, NeighborsOfSelfScope)
        }
        fidelity(FidelityLevel.FULL, "Aura: each of Bulwark's neighbors gets +2 armor")
    
    },
    card("flame-wreathed") {

        persistent {
            modifyPower(2, HostCreatureScope)
            addKeyword(Keyword.HAZARDOUS, value = 2, scope = HostCreatureScope)
        }
        fidelity(FidelityLevel.FULL, "Upgrade: attached creature gets +2 power and gains hazardous 2")
    
    },
    card("grey-monk") {

        persistent {
            modifyArmor(1, FriendlyCreaturesScope)
        }
        reap(Heal(2, AnyCreature()))
        fidelity(FidelityLevel.FULL, "Aura: each friendly creature gets +1 armor; Reap heals 2 damage")
    
    },
    card("halacor") {

        persistent {
            addKeyword(Keyword.SKIRMISH, scope = FriendlyFlankCreaturesScope)
        }
        fidelity(FidelityLevel.FULL, "Aura: each friendly flank creature gains skirmish")
    
    },
    card("king-of-the-crag") {

        persistent {
            modifyPower(-2, EnemyOfHouseScope(House.BROBNAR))
        }
        fidelity(FidelityLevel.FULL, "Aura: each enemy Brobnar creature gets -2 power")
    
    },
    card("mushroom-man") {

        persistent {
            modifyPower(SelfScope) { game, source ->
                3 * (3 - game.player(source.controllerIndex).keys)
            }
        }
        fidelity(FidelityLevel.FULL, "Aura: gets +3 power for each unforged key you have")
    
    },
    card("niffle-queen") {

        persistent {
            modifyPower(1, OtherFriendlyCreaturesScope(trait = "beast"))
            modifyPower(1, OtherFriendlyCreaturesScope(trait = "niffle"))
        }
        fidelity(FidelityLevel.FULL, "Aura: each other friendly Beast/Niffle creature gets +1 power")
    
    },
    card("protect-the-weak") {

        persistent {
            modifyArmor(1, HostCreatureScope)
            addKeyword(Keyword.TAUNT, scope = HostCreatureScope)
        }
        fidelity(FidelityLevel.FULL, "Upgrade: attached creature gets +1 armor and taunt")
    
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
    card("shoulder-armor") {

        persistent {
            modifyPower(2, HostCreatureScope, requireFlank = true)
            modifyArmor(2, HostCreatureScope, requireFlank = true)
        }
        fidelity(FidelityLevel.FULL, "Upgrade: attached creature gets +2 armor and +2 power while on a flank")
    
    },
    card("staunch-knight") {

        persistent {
            modifyPower(2, SelfScope, requireFlank = true)
        }
        fidelity(FidelityLevel.FULL, "Aura: gets +2 power while on a flank")
    
    },
    card("titan-mechanic") {

        persistent {
            modifyKeyCost(-1, PayerScope.ANY, requireFlank = true)
        }
        fidelity(FidelityLevel.FULL, "Aura: while on a flank, each key (either player's) costs -1 amber")
    
    },
    card("yxili-marauder") {

        play(CaptureAmberDynamic({ _, player -> player.creatures.count { it.house == House.MARS && !it.exhausted } }, SelfCreature))
        persistent {
            modifyPower(SelfScope) { _, source -> source.capturedAmber }
        }
        fidelity(FidelityLevel.FULL, "Aura: +1 power per amber on it; Play: capture 1A per friendly ready Mars creature")

    },
    card("zorg") {

        entersPlayStunned()
        beforeFight(StunFightTargetAndNeighbors)
        fidelity(FidelityLevel.FULL, "Zorg enters play stunned; Before Fight: stun the creature it fights and each neighbor")

    },

    card("bigtwig") {

        restrictions { selfOnlyFightStunned() }
        reap(StunAndExhaust(AnyCreature()))
        fidelity(FidelityLevel.FULL, "Bigtwig can only fight stunned creatures; Reap: stun and exhaust a creature")

    },
    card("mantle-of-the-zealot") {

        restrictions { selfUsableAsActiveHouse() }
        fidelity(FidelityLevel.FULL, "Upgrade: the attached creature may be used as if it belonged to the active house")

    },
    card("the-howling-pit") {

        drawRefillBonus(1)
        // A negative penalty makes the OPPONENT refill to 1 additional card.
        opponentRefillPenalty(-1)
        fidelity(FidelityLevel.FULL, "During their draw step, each player refills their hand to 1 additional card")

    },

)
