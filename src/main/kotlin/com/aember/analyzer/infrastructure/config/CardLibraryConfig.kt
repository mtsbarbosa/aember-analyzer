package com.aember.analyzer.infrastructure.config

import com.aember.analyzer.domain.engine.CardLibrary
import com.aember.analyzer.domain.engine.cards.Anger
import com.aember.analyzer.domain.engine.cards.BadPenny
import com.aember.analyzer.domain.engine.cards.Drumble
import com.aember.analyzer.domain.engine.cards.EtherSpider
import com.aember.analyzer.domain.engine.cards.ShadowSelf
import com.aember.analyzer.domain.engine.cards.SpeedSigil
import com.aember.analyzer.domain.engine.cards.coreSetCards
import com.aember.analyzer.domain.engine.dsl.DslCardScript
import com.aember.analyzer.domain.engine.dsl.EffectResolver
import com.aember.analyzer.domain.ports.CardCatalogPort
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CardLibraryConfig {

    @Bean
    fun cardLibrary(catalog: CardCatalogPort): CardLibrary {
        val allCards = catalog.loadAll().associateBy { it.id }
        val library = CardLibrary(allCards)
        val resolver = EffectResolver(library = library)

        // Register auto-generated Core set DSL definitions.
        coreSetCards.forEach { (id, definition) ->
            library.register(id, DslCardScript(definition, resolver))
        }

        // Manual overrides for cards the DSL generator cannot express cleanly.
        library.register("bad-penny", BadPenny)
        library.register("anger", Anger)
        library.register("drumble", Drumble)
        library.register("ether-spider", EtherSpider)
        library.register("shadow-self", ShadowSelf)
        library.register("speed-sigil", SpeedSigil)

        return library
    }
}
