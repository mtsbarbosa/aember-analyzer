package com.aember.analyzer.engine

import com.aember.analyzer.domain.engine.dsl.DslCardScript
import com.aember.analyzer.domain.engine.dsl.NoEffect
import com.aember.analyzer.engine.support.CoreCardLibrary
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

/**
 * Phase 4 coverage ratchet (PLAN.md):
 *
 * 1. The verified-card count may never drop without a deliberate bump of [VERIFIED_MINIMUM].
 * 2. A catalog card with ability text must not be silently inert ("NoEffect") or unregistered
 *    unless its id is listed in `fidelity/no-effect-allowlist.txt` (the known-gaps allowlist).
 *    Adding a new inert card therefore requires a conscious allowlist entry, and implementing
 *    a card lets its allowlist entry be deleted (stale entries fail the test as unused).
 *
 * Manual `CardScript` objects are exempt — they are handwritten implementations.
 */
class CoverageRatchetTest {

    private val library = CoreCardLibrary.create()

    /** Bump deliberately when the verified set grows; dropping below this fails the test. */
    private val verifiedMinimum = 16

    @Test
    fun `verified card count does not drop below the ratchet`() {
        val verified = verifiedIds()
        assertThat(verified.size)
            .describedAs("verified-cards.txt entry count (raise verifiedMinimum deliberately)")
            .isGreaterThanOrEqualTo(verifiedMinimum)
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

    private fun allCatalogCards() = CoreCardLibrary.cards

    private fun hasAbilityText(raw: String): Boolean {
        val text = raw.replace(0x0b.toChar(), ' ').trim()
        if (text.isEmpty()) return false
        return !KEYWORD_ONLY.matches(text)
    }

    private fun isStructurallyInert(script: DslCardScript): Boolean {
        val d = script.definition
        return d.playEffect == NoEffect &&
            d.reapEffect == NoEffect &&
            d.fightEffect == NoEffect &&
            d.destroyedEffect == NoEffect &&
            d.beforeFightEffect == NoEffect &&
            d.afterFightEffect == NoEffect &&
            d.beforeReapEffect == NoEffect &&
            d.afterReapEffect == NoEffect &&
            d.omniEffect == NoEffect &&
            d.actionAbilities.isEmpty() &&
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
        // Mirrors tools/fidelity-inventory.py KEYWORD_ONLY: text made exclusively of
        // keywords / parenthesised modifiers has no ability content.
        private val KEYWORD_ONLY = Regex(
            "^((alpha|omega|elusive|skirmish|taunt|hazardous\\s*\\d*|assault\\s*\\d*|deploy|" +
                "poison|splash-attack\\s*\\d*|\\(.*?\\))[.\\s\\x0b,]*)*$",
            setOf(RegexOption.IGNORE_CASE, RegexOption.DOT_MATCHES_ALL)
        )
    }
}
