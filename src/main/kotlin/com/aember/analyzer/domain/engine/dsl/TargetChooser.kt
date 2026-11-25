package com.aember.analyzer.domain.engine.dsl

import com.aember.analyzer.domain.engine.model.GameState
import com.aember.analyzer.domain.engine.model.InPlayCard

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
        val opponent = game.players[1 - sourcePlayer.index]
        val candidates = when (selector) {
            is FriendlyCreature -> sourcePlayer.creatures
            is EnemyCreature -> opponent.creatures
            is AnyCreature -> sourcePlayer.creatures + opponent.creatures
            is FriendlyCreatureWithTrait -> sourcePlayer.creatures.filter { it.traits.contains(selector.trait.lowercase()) }
            is EnemyCreatureWithTrait -> opponent.creatures.filter { it.traits.contains(selector.trait.lowercase()) }
            is CreatureWithKeyword -> (sourcePlayer.creatures + opponent.creatures).filter { it.keywords.containsKey(selector.keyword.lowercase()) }
            is FriendlyCreatureWithHouse -> sourcePlayer.creatures.filter { it.house == selector.house }
            is OtherFriendlyCreatureWithHouse -> sourcePlayer.creatures.filter { it.house == selector.house && it.instanceId != source.instanceId }
            is CreatureNotOfHouse -> (sourcePlayer.creatures + opponent.creatures).filter { it.house != selector.house }
            is CreatureAndNeighbors -> chooseCreature(game, selector.base, source).firstOrNull()?.let { base ->
                val controller = game.player(base.controllerIndex)
                val idx = controller.creatures.indexOf(base)
                if (idx >= 0) {
                    controller.creatures.filterIndexed { i, _ -> i in (idx - 1)..(idx + 1) }
                } else {
                    listOf(base)
                }
            } ?: emptyList()
            is SelfCreature -> {
                val host = source.hostId?.let { id ->
                    game.players.asSequence().flatMap { it.creatures.asSequence() }.find { it.instanceId == id }
                }
                listOf(host ?: source)
            }
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
            is AnyCreatureExceptLastDamaged -> {
                val id = game.lastDamagedTargetId
                (sourcePlayer.creatures + opponent.creatures).filter { it.instanceId != id }
            }
            is LastHealedTarget -> {
                val id = game.lastHealedTargetId
                game.players.flatMap { it.creatures }.filter { it.instanceId == id }
            }
            is NeighborOfSelf -> {
                val controller = game.player(source.controllerIndex)
                val idx = controller.creatures.indexOf(source)
                if (idx >= 0) {
                    listOfNotNull(controller.creatures.getOrNull(idx - 1), controller.creatures.getOrNull(idx + 1))
                        .distinctBy { it.instanceId }
                } else {
                    emptyList()
                }
            }
            is CreatureNotOnFlank -> {
                (sourcePlayer.creatures + opponent.creatures).filter { creature ->
                    val battleline = game.player(creature.controllerIndex).creatures
                    val isFlank = battleline.size == 1 || battleline.first().instanceId == creature.instanceId ||
                        battleline.last().instanceId == creature.instanceId
                    !isFlank
                }
            }
            is EachFriendlyUndamagedCreature -> sourcePlayer.creatures.filter { it.damage == 0 }
            is EachEnemyUndamagedCreature -> opponent.creatures.filter { it.damage == 0 }
            is FlankCreature -> (sourcePlayer.creatures + opponent.creatures).filter { creature ->
                val battleline = game.player(creature.controllerIndex).creatures
                battleline.size == 1 || battleline.first().instanceId == creature.instanceId ||
                    battleline.last().instanceId == creature.instanceId
            }
            is DamagedCreature -> (sourcePlayer.creatures + opponent.creatures).filter { it.damage > 0 }
            is AnyCreatureWithTrait -> (sourcePlayer.creatures + opponent.creatures)
                .filter { it.traits.contains(selector.trait.lowercase()) }
            is CreatureOtherThanLastHealed -> (sourcePlayer.creatures + opponent.creatures)
                .filter { it.instanceId != game.lastHealedTargetId }
            is CreatureWithoutTrait -> (sourcePlayer.creatures + opponent.creatures)
                .filter { !it.traits.contains(selector.trait.lowercase()) }
        }
        val sorted = when (selector) {
            is FriendlyCreature, is FriendlyCreatureWithTrait, is FriendlyCreatureWithHouse, is OtherFriendlyCreatureWithHouse -> candidates.sortedByDescending { it.power }
            is EnemyCreature, is EnemyCreatureWithTrait, is CreatureWithKeyword, is CreatureAndNeighbors, is AllCreaturesOfChosenHouse -> candidates.sortedByDescending { it.power }
            is CreatureNotOfHouse -> candidates.sortedByDescending { it.power }
            is AnyCreature -> {
                val (enemies, friendlies) = candidates.partition { it.controllerIndex != source.controllerIndex }
                enemies.sortedByDescending { it.power } + friendlies.sortedByDescending { it.power }
            }
            is EachOtherUndamagedCreature -> candidates
            is SelfCreature -> candidates
            is LastDamagedTarget -> candidates
            is AnyCreatureExceptLastDamaged -> {
                val (enemies, friendlies) = candidates.partition { it.controllerIndex != source.controllerIndex }
                enemies.sortedByDescending { it.power } + friendlies.sortedByDescending { it.power }
            }
            is LastHealedTarget -> candidates
            is NeighborOfSelf -> candidates.sortedByDescending { it.power }
            is CreatureNotOnFlank -> {
                val (enemies, friendlies) = candidates.partition { it.controllerIndex != source.controllerIndex }
                enemies.sortedByDescending { it.power } + friendlies.sortedByDescending { it.power }
            }
            is EachFriendlyUndamagedCreature, is EachEnemyUndamagedCreature -> candidates
            is FlankCreature -> {
                val (enemies, friendlies) = candidates.partition { it.controllerIndex != source.controllerIndex }
                enemies.sortedByDescending { it.power } + friendlies.sortedByDescending { it.power }
            }
            is DamagedCreature -> {
                val (enemies, friendlies) = candidates.partition { it.controllerIndex != source.controllerIndex }
                enemies.sortedByDescending { it.power } + friendlies.sortedByDescending { it.power }
            }
            is AnyCreatureWithTrait -> candidates
            is CreatureOtherThanLastHealed -> {
                val (enemies, friendlies) = candidates.partition { it.controllerIndex != source.controllerIndex }
                enemies.sortedByDescending { it.power } + friendlies.sortedByDescending { it.power }
            }
            is CreatureWithoutTrait -> {
                val (enemies, friendlies) = candidates.partition { it.controllerIndex != source.controllerIndex }
                enemies.sortedByDescending { it.power } + friendlies.sortedByDescending { it.power }
            }
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
            is CreatureNotOfHouse -> selector.scope
            is CreatureAndNeighbors -> Scope.ALL
            is EachOtherUndamagedCreature -> Scope.ALL
            is SelfCreature -> Scope.ONE
            is AllCreaturesOfChosenHouse -> Scope.ALL
            is LastDamagedTarget -> Scope.ONE
            is AnyCreatureExceptLastDamaged -> Scope.ONE
            is LastHealedTarget -> Scope.ONE
            is NeighborOfSelf -> Scope.ONE
            is CreatureNotOnFlank -> Scope.ONE
            is EachFriendlyUndamagedCreature, is EachEnemyUndamagedCreature -> Scope.ALL
            is FlankCreature -> Scope.ONE
            is DamagedCreature -> Scope.ONE
            is AnyCreatureWithTrait -> selector.scope
            is CreatureWithoutTrait -> selector.scope
            is CreatureOtherThanLastHealed -> Scope.ONE
        }
        return if (scope == Scope.ONE) sorted.take(1) else sorted
    }
}
