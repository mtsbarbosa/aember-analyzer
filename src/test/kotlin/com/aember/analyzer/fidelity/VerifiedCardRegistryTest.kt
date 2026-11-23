package com.aember.analyzer.fidelity

import com.aember.analyzer.domain.engine.fidelity.FidelityLevel
import com.aember.analyzer.domain.engine.fidelity.VerifiedCardRegistry
import com.aember.analyzer.engine.support.CoreCardLibrary
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

/**
 * Guards the honesty of the verified-fidelity registry: a card may only be listed in
 * `fidelity/verified-cards.txt` while it is declared FULL and has a real parity test.
 *
 * (The full spec-coverage ratchet — "every keyteki spec for a listed card is ported or
 * explicitly blocked" — is planned for Phase 4; the registry entries carry blocked-spec
 * notes as comments until then.)
 */
class VerifiedCardRegistryTest {

    @Test
    fun `every listed card id exists in the Core catalog`() {
        val library = CoreCardLibrary.create()
        val unknown = VerifiedCardRegistry.verifiedCardIds.filter { library.cardData(it) == null }
        assertThat(unknown).describedAs("verified ids missing from the catalog").isEmpty()
    }

    @Test
    fun `every listed card is declared FULL`() {
        val library = CoreCardLibrary.create()
        val notFull = VerifiedCardRegistry.verifiedCardIds.filter { library.fidelityFor(it) != FidelityLevel.FULL }
        assertThat(notFull).describedAs("verified cards that are not declared FULL").isEmpty()
    }

    @Test
    fun `the registry is not empty`() {
        // The verified view is meaningless if nothing is verified; this test also fails
        // loudly if the resource file is moved or renamed.
        assertThat(VerifiedCardRegistry.verifiedCardIds).isNotEmpty()
    }
}
