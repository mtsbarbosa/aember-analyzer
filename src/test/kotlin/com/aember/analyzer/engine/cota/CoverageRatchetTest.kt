package com.aember.analyzer.engine.cota

import com.aember.analyzer.domain.engine.dsl.Conditional
import com.aember.analyzer.domain.engine.dsl.DslCardScript
import com.aember.analyzer.domain.engine.dsl.Effect
import com.aember.analyzer.domain.engine.dsl.NoEffect
import com.aember.analyzer.domain.engine.dsl.RepeatForEach
import com.aember.analyzer.domain.engine.dsl.Sequence
import com.aember.analyzer.engine.support.CoreCardLibrary
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

/**
 * Phase 4 coverage ratchet (PLAN.md):
 *
 * 1. The verified-card count may not change without a deliberate bump of [VERIFIED_MINIMUM]:
 *    dropping below it fails, and growing past it fails too so the bump cannot be forgotten.
 * 2. A catalog card with ability text must not be silently inert (recursively effect-free:
 *    `Sequence(NoEffect, NoEffect)` and `Conditional`-wrapped `NoEffect` count as inert) or
 *    unregistered unless its id is listed in `fidelity/no-effect-allowlist.txt` (the
 *    known-gaps allowlist). Adding a new inert card therefore requires a conscious allowlist
 *    entry, and implementing a card lets its allowlist entry be deleted (stale entries fail
 *    the test as unused).
 * 3. Every catalog card must have a registered script — unregistered cards fail even when
 *    they would be vanilla.
 *
 * Manual `CardScript` objects are exempt — they are handwritten implementations.
 * Note: cards where only a *fragment* of the text is implemented still pass the inert
 * check (it only catches fully inert implementations); the declared-fidelity levels and
 * the inventory carry that honest signal.
 */
class CoverageRatchetTest {

    private val library = CoreCardLibrary.create()

    /** The exact expected verified-card count; bump it when a card gets its parity test. */
    private val verifiedMinimum = 16

    @Test
    fun `verified card count matches the ratchet`() {
        val verified = verifiedIds()
        assertThat(verified.size)
            .describedAs("verified-cards.txt entry count (bump verifiedMinimum deliberately when it changes)")
            .isEqualTo(verifiedMinimum)
    }

    @Test
    fun `cards with ability text are not inert without an allowlist entry`() {
        val allowlist = noEffectAllowlist()
        val offenders = mutableListOf<String>()
        val usedAllowlistEntries = mutableSetOf<String>()

        for (card in allCatalogCards()) {
            if (!hasAbilityText(card.text)) continue
            val script = library.scriptFor(card.id)
            val inert = when (script) {
                is DslCardScript -> isStructurallyInert(script)
                else -> script === com.aember.analyzer.domain.engine.VanillaCardScript
            }
            if (!inert) continue
            if (card.id in allowlist) {
                usedAllowlistEntries += card.id
            } else {
                offenders += card.id
            }
        }

        val stale = allowlist - usedAllowlistEntries
        assertThat(offenders)
            .describedAs(
                "cards with ability text that are inert without a known-gaps entry " +
                    "(implement them or add their id to fidelity/no-effect-allowlist.txt)"
            )
            .isEmpty()
        assertThat(stale)
            .describedAs("allowlist entries whose cards are no longer inert (delete them)")
            .isEmpty()
    }

    @Test
    fun `every catalog card has a registered script`() {
        val unregistered = allCatalogCards().filterNot { it.id in library.registeredIds() }
        assertThat(unregistered.map { it.id })
            .describedAs("catalog cards without a registered CardScript")
            .isEmpty()
    }

    private fun allCatalogCards() = CoreCardLibrary.cards

    private fun hasAbilityText(raw: String): Boolean {
        val text = raw.replace(0x0b.toChar(), ' ').trim()
        if (text.isEmpty()) return false
        return !KEYWORD_ONLY.matches(text)
    }

    private fun isStructurallyInert(script: DslCardScript): Boolean {
        val d = script.definition
        return isEffectInert(d.playEffect) &&
            isEffectInert(d.reapEffect) &&
            isEffectInert(d.fightEffect) &&
            isEffectInert(d.destroyedEffect) &&
            isEffectInert(d.beforeFightEffect) &&
            isEffectInert(d.afterFightEffect) &&
            isEffectInert(d.beforeReapEffect) &&
            isEffectInert(d.afterReapEffect) &&
            isEffectInert(d.omniEffect) &&
            d.actionAbilities.all { isEffectInert(it.second) } &&
            d.staticEffects.isEmpty() &&
            d.useRestrictions.isEmpty() &&
            d.selfRestrictions.isEmpty() &&
            d.reactions.isEmpty() &&
            d.playCondition == null &&
            !d.entersPlayStunned &&
            !d.ignoresTauntElusive &&
            d.drawRefillBonusAmount == 0 &&
            d.opponentRefillPenaltyAmount == 0
    }

    /**
     * Recursively effect-free: `NoEffect` itself, `Sequence` of inert effects and
     * `Conditional` with inert branches all resolve to nothing. `RepeatForEach` counts
     * as inert because its handler is currently a no-op.
     */
    private fun isEffectInert(effect: Effect): Boolean = when (effect) {
        NoEffect -> true
        is Sequence -> effect.effects.all { isEffectInert(it) }
        is Conditional -> isEffectInert(effect.thenEffect) && isEffectInert(effect.elseEffect)
        is RepeatForEach -> true
        else -> false
    }

    private fun verifiedIds(): Set<String> =
        readLines("/fidelity/verified-cards.txt")

    private fun noEffectAllowlist(): Set<String> =
        readLines("/fidelity/no-effect-allowlist.txt")

    private fun readLines(resource: String): Set<String> {
        val stream = javaClass.getResourceAsStream(resource)
            ?: error("missing resource $resource")
        return stream.bufferedReader().readLines()
            .map { it.split("#")[0].trim() }
            .filter { it.isNotEmpty() }
            .toSet()
    }

    private companion object {
        // The canonical pattern lives in src/test/resources/fidelity/keyword-only.pattern;
        // tools/fidelity-inventory.py reads the same file so the two cannot drift.
        private val KEYWORD_ONLY = Regex(
            javaClass.getResourceAsStream("/fidelity/keyword-only.pattern")
                ?.bufferedReader()?.readLines()?.firstOrNull { it.isNotBlank() && !it.startsWith("#") }
                ?: error("missing resource /fidelity/keyword-only.pattern"),
            RegexOption.IGNORE_CASE
        )
    }
}
