package com.aember.analyzer.domain.engine.cards

import com.aember.analyzer.domain.engine.CardScript
import com.aember.analyzer.domain.engine.dsl.*
import com.aember.analyzer.domain.engine.fidelity.CardFidelity
import com.aember.analyzer.domain.engine.fidelity.FidelityLevel
import com.aember.analyzer.domain.engine.model.GameState
import com.aember.analyzer.domain.engine.model.InPlayCard
import com.aember.analyzer.domain.engine.model.PlayerState
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
        val enemy = game.opponent().creatures.randomOrNull() ?: return
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

    override fun cardEntersReady(): Boolean = true
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

    override fun opponentKeyCostModifier(): Int = 2
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
        notes = "Steals all but 6 of the opponent's amber"
    )

    override fun playable(game: GameState, player: PlayerState): Boolean {
        return game.opponent().amber > 6
    }

    override fun onPlay(game: GameState, player: PlayerState, card: InPlayCard) {
        val opponent = game.opponent()
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
                ForgeKey(extraCost = -6, discountPerCardInHand = false)
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
        reap(Sequence(listOf(ArchiveFromHand(1, SelfPlayer), AddPowerCounters(3))))
        fight(Sequence(listOf(ArchiveFromHand(1, SelfPlayer), AddPowerCounters(3))))
        fidelity(FidelityLevel.FULL, "AI always archives one creature from hand and adds three +1 power counters")
    },
    card("john-smyth") {
        reap(Ready(OtherFriendlyCreatureWithHouse(House.MARS)))
        fight(Ready(OtherFriendlyCreatureWithHouse(House.MARS)))
        fidelity(FidelityLevel.PARTIAL, "Readies another friendly Mars creature; non-Agent restriction not enforced")
    }
)
