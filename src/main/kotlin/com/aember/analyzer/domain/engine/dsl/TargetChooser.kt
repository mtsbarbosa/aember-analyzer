package com.aember.analyzer.domain.engine.dsl

import com.aember.analyzer.domain.engine.model.GameState
import com.aember.analyzer.domain.engine.model.InPlayCard
import com.aember.analyzer.domain.engine.model.PlayerState

interface TargetChooser {
    fun chooseCreature(game: GameState, selector: CreatureSelector, source: InPlayCard): List<InPlayCard>
}

class GreedyTargetChooser : TargetChooser {

    override fun chooseCreature(
        game: GameState,
        selector: CreatureSelector,
        source: InPlayCard
    ): List<InPlayCard> {
        val sourcePlayer = game.player(source.controllerIndex)
        val opponent = game.opponent()
        val candidates = when (selector) {
            is FriendlyCreature -> sourcePlayer.creatures
            is EnemyCreature -> opponent.creatures
            is AnyCreature -> sourcePlayer.creatures + opponent.creatures
            is FriendlyCreatureWithTrait -> sourcePlayer.creatures.filter { it.traits.contains(selector.trait.lowercase()) }
            is EnemyCreatureWithTrait -> opponent.creatures.filter { it.traits.contains(selector.trait.lowercase()) }
            is CreatureWithKeyword -> (sourcePlayer.creatures + opponent.creatures).filter { it.keywords.containsKey(selector.keyword.lowercase()) }
            is FriendlyCreatureWithHouse -> sourcePlayer.creatures.filter { it.house == selector.house }
            is OtherFriendlyCreatureWithHouse -> sourcePlayer.creatures.filter { it.house == selector.house && it.instanceId != source.instanceId }
        }
        val sorted = when (selector) {
            is FriendlyCreature, is FriendlyCreatureWithTrait, is FriendlyCreatureWithHouse, is OtherFriendlyCreatureWithHouse -> candidates.sortedByDescending { it.power }
            is EnemyCreature, is EnemyCreatureWithTrait, is AnyCreature, is CreatureWithKeyword -> candidates.sortedByDescending { it.power }
        }
        val scope = when (selector) {
            is FriendlyCreature -> selector.scope
            is EnemyCreature -> selector.scope
            is AnyCreature -> selector.scope
            is FriendlyCreatureWithTrait -> selector.scope
            is EnemyCreatureWithTrait -> selector.scope
            is CreatureWithKeyword -> selector.scope
            is FriendlyCreatureWithHouse -> selector.scope
            is OtherFriendlyCreatureWithHouse -> selector.scope
        }
        return if (scope == Scope.ONE) sorted.take(1) else sorted
    }
}
