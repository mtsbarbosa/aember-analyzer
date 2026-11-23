package com.aember.analyzer.engine.support

import com.aember.analyzer.domain.engine.dsl.CreatureSelector
import com.aember.analyzer.domain.engine.dsl.GreedyTargetChooser
import com.aember.analyzer.domain.engine.dsl.TargetChooser
import com.aember.analyzer.domain.engine.model.GameState
import com.aember.analyzer.domain.engine.model.InPlayCard

/**
 * Deterministic stand-in for the AI target selection (the engine's `ChoiceProvider` port).
 *
 * keyteki parity specs pre-script every prompt answer via `clickPrompt`. Effects that need a
 * target go through [TargetChooser]; this implementation answers those prompts from a list of
 * scripted rules (first match wins) and falls back to the greedy behaviour used in production
 * simulations when no rule matches.
 *
 * Rules are registered while the scenario is being built:
 * ```kotlin
 * val chooser = ScriptedTargetChooser()
 * chooser.rule<EnemyCreature>("umbra") { game, _ -> game.opponent().creatures.filter { it.cardId == "umbra" } }
 * ```
 */
class ScriptedTargetChooser(
    private val fallback: TargetChooser = GreedyTargetChooser()
) : TargetChooser {

    private class Rule(
        val selectorType: Class<out CreatureSelector>,
        val answer: (GameState, InPlayCard) -> List<InPlayCard>
    )

    private val rules = mutableListOf<Rule>()

    /** Answer every prompt for the given selector type with [answer]. */
    inline fun <reified S : CreatureSelector> rule(noinline answer: (GameState, InPlayCard) -> List<InPlayCard>) {
        rule(S::class.java, answer)
    }

    /** Convenience: always answer the given selector type with the in-play creature of [cardId]. */
    inline fun <reified S : CreatureSelector> answerWithCard(cardId: String) {
        rule(S::class.java) { game, _ -> listOfNotNull(findCard(game, cardId)) }
    }

    fun rule(selectorType: Class<out CreatureSelector>, answer: (GameState, InPlayCard) -> List<InPlayCard>) {
        rules.add(Rule(selectorType, answer))
    }

    override fun chooseCreature(game: GameState, selector: CreatureSelector, source: InPlayCard): List<InPlayCard> {
        val rule = rules.firstOrNull { it.selectorType == selector::class.java }
        if (rule != null) {
            val answer = rule.answer(game, source)
            if (answer.isNotEmpty()) return answer
        }
        return fallback.chooseCreature(game, selector, source)
    }

    companion object {
        fun findCard(game: GameState, cardId: String): InPlayCard? =
            game.players.asSequence().flatMap { it.cardsInPlay().asSequence() }.firstOrNull { it.cardId == cardId }

        fun findCard(game: GameState, controllerIndex: Int, cardId: String): InPlayCard? =
            game.players[controllerIndex].cardsInPlay().firstOrNull { it.cardId == cardId }
    }
}
