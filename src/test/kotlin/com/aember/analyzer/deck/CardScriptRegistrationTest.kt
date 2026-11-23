package com.aember.analyzer.deck

import com.aember.analyzer.IntegrationTest
import com.aember.analyzer.domain.engine.cards.coreSetCards
import com.aember.analyzer.domain.engine.cards.dslCardOverrides
import com.aember.analyzer.domain.engine.cards.keyCheatDslCardOverrides
import com.aember.analyzer.domain.engine.cards.marsEngineDslCardOverrides
import com.aember.analyzer.infrastructure.persistence.JsonCardCatalogLoader
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

/**
 * A script registered under an id the catalog does not know is silently never used
 * (the engine falls back to a vanilla script). Cards whose names contain typographic
 * apostrophes (Coward’s End, Nature’s Call, …) were hit by exactly this. The catalog ids
 * are what [JsonCardCatalogLoader] produces, which is what seeds the real database.
 */
class CardScriptRegistrationTest : IntegrationTest() {

    @Autowired
    lateinit var loader: JsonCardCatalogLoader

    @Test
    fun `every DSL script id exists in the card catalog`() {
        val catalogIds = loader.loadCoreSet().map { it.id }.toSet()
        val ids = (coreSetCards + dslCardOverrides + marsEngineDslCardOverrides + keyCheatDslCardOverrides).map { it.first }
        assertThat(ids.filter { it !in catalogIds }).describedAs("script ids missing from the catalog").isEmpty()
    }

    @Test
    fun `cards with typographic apostrophes have scripts under their catalog id`() {
        val scripted = (coreSetCards + dslCardOverrides + marsEngineDslCardOverrides + keyCheatDslCardOverrides).map { it.first }.toSet()
        assertThat(scripted).contains(
            "coward-s-end", "champion-s-challenge", "nature-s-call", "the-spirit-s-way",
            "irradiated-mber", "annihilation-ritual"
        )
    }

    /**
     * The generator emits inert "Overridden:" placeholder stubs for ids that live in an
     * override list. If an id is later dropped from the override list without removing it
     * from the generator skip-list, the placeholder wins the registration and the card
     * silently becomes a no-op. Every overridden id must therefore be re-implemented
     * somewhere in an override list or a manual script.
     */
    @Test
    fun `no placeholder stub is left as the registered implementation`() {
        val placeholders = coreSetCards.filter { it.second.fidelity.notes.startsWith("Overridden:") }.map { it.first }
        val overridden = (dslCardOverrides + marsEngineDslCardOverrides + keyCheatDslCardOverrides).map { it.first }.toSet()
        assertThat(placeholders.filter { it !in overridden })
            .describedAs("generated stubs whose override is missing (dropped from an override list?)")
            .isEmpty()
    }
}
