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
            is CreatureAndNeighbors -> chooseCreature(game, selector.base, source).firstOrNull()?.let { base ->
                val controller = game.player(base.controllerIndex)
                val idx = controller.creatures.indexOf(base)
                if (idx >= 0) {
                    controller.creatures.filterIndexed { i, _ -> i in (idx - 1)..(idx + 1) }
                } else {
                    listOf(base)
                }
            } ?: emptyList()
            is EachOtherUndamagedCreature -> {
                val opponentPlayer = game.players[1 - source.controllerIndex]
                (sourcePlayer.creatures + opponentPlayer.creatures)
                    .filter { it.cardId != source.cardId && it.damage == 0 }
            }
            is AllCreaturesOfChosenHouse -> {
                val opponentPlayer = game.players[1 - source.controllerIndex]
                val chosenHouse = (sourcePlayer.creatures + opponentPlayer.creatures)
                    .groupingBy { it.house }
                    .eachCount()
                    .maxByOrNull { it.value }
                    ?.key
                if (chosenHouse != null) {
                    game.players.flatMap { it.creatures }.filter { it.house == chosenHouse }
                } else {
                    emptyList()
                }
            }
            is LastDamagedTarget -> {
                val id = game.lastDamagedTargetId
                game.players.flatMap { it.creatures }.filter { it.instanceId == id }
            }
        }
        val sorted = when (selector) {
            is FriendlyCreature, is FriendlyCreatureWithTrait, is FriendlyCreatureWithHouse, is OtherFriendlyCreatureWithHouse -> candidates.sortedByDescending { it.power }
            is EnemyCreature, is EnemyCreatureWithTrait, is CreatureWithKeyword, is CreatureAndNeighbors, is AllCreaturesOfChosenHouse -> candidates.sortedByDescending { it.power }
            is AnyCreature -> {
                val (enemies, friendlies) = candidates.partition { it.controllerIndex != source.controllerIndex }
                enemies.sortedByDescending { it.power } + friendlies.sortedByDescending { it.power }
            }
            is EachOtherUndamagedCreature -> candidates
            is LastDamagedTarget -> candidates
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
            is CreatureAndNeighbors -> Scope.ALL
            is EachOtherUndamagedCreature -> Scope.ALL
            is AllCreaturesOfChosenHouse -> Scope.ALL
            is LastDamagedTarget -> Scope.ONE
        }
        return if (scope == Scope.ONE) sorted.take(1) else sorted
    }
}
