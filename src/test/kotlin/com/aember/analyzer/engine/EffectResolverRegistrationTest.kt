package com.aember.analyzer.engine

import com.aember.analyzer.domain.engine.dsl.Condition
import com.aember.analyzer.domain.engine.dsl.Conditional
import com.aember.analyzer.domain.engine.dsl.Effect
import com.aember.analyzer.domain.engine.dsl.EffectResolver
import com.aember.analyzer.domain.engine.dsl.Sequence
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.reflect.KClass

/**
 * Structural ratchet: every Effect and Condition subclass must be registered
 * on the resolver. Unregistered effects throw at match time ("No handler for
 * X") and unmapped conditions fail silently (evaluateCondition returns false),
 * so neither is caught by the compiler.
 */
class EffectResolverRegistrationTest {

    private val resolver = EffectResolver()

    @Test
    fun `every Effect subclass has a registered handler`() {
        val missing = sealedSubclassesRecursive(Effect::class)
            .filter { it != Sequence::class && it != Conditional::class } // dispatched structurally
            .filterNot { resolver.handlers.containsKey(it) }
            .map { it.simpleName }
        assertThat(missing).isEmpty()
    }

    @Test
    fun `every Condition subclass has an evaluate handler`() {
        val missing = sealedSubclassesRecursive(Condition::class)
            .filterNot { resolver.conditionHandlers.containsKey(it) }
            .map { it.simpleName }
        assertThat(missing).isEmpty()
    }

    private fun sealedSubclassesRecursive(root: KClass<out Any>): List<KClass<out Any>> {
        val result = mutableListOf<KClass<out Any>>()
        val queue = ArrayDeque<KClass<out Any>>()
        queue.add(root)
        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            val children = current.sealedSubclasses
            result.addAll(children)
            queue.addAll(children)
        }
        return result
    }
}
