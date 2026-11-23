package com.aember.analyzer.deck

import com.aember.analyzer.IntegrationTest
import com.aember.analyzer.domain.engine.cards.coreSetCards
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
        val ids = (coreSetCards + marsEngineDslCardOverrides + keyCheatDslCardOverrides).map { it.first }
        assertThat(ids.filter { it !in catalogIds }).describedAs("script ids missing from the catalog").isEmpty()
    }

    @Test
    fun `cards with typographic apostrophes have scripts under their catalog id`() {
        val scripted = (coreSetCards + marsEngineDslCardOverrides + keyCheatDslCardOverrides).map { it.first }.toSet()
        assertThat(scripted).contains(
            "coward-s-end", "champion-s-challenge", "nature-s-call", "the-spirit-s-way",
            "irradiated-mber", "annihilation-ritual"
        )
    }
}
