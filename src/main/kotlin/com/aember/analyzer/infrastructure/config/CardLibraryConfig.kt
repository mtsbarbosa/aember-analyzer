package com.aember.analyzer.infrastructure.config

import com.aember.analyzer.domain.engine.CardLibrary
import com.aember.analyzer.domain.engine.cards.Anger
import com.aember.analyzer.domain.engine.cards.AnnihilationRitual
import com.aember.analyzer.domain.engine.cards.Arise
import com.aember.analyzer.domain.engine.cards.BadPenny
import com.aember.analyzer.domain.engine.cards.BiomatrixBackup
import com.aember.analyzer.domain.engine.cards.ControlTheWeak
import com.aember.analyzer.domain.engine.cards.Drumble
import com.aember.analyzer.domain.engine.cards.EtherSpider
import com.aember.analyzer.domain.engine.cards.Fogbank
import com.aember.analyzer.domain.engine.cards.Valdr
import com.aember.analyzer.domain.engine.cards.JammerPack
import com.aember.analyzer.domain.engine.cards.keyCheatDslCardOverrides
import com.aember.analyzer.domain.engine.cards.LashOfBrokenDreams
import com.aember.analyzer.domain.engine.cards.marsEngineDslCardOverrides
import com.aember.analyzer.domain.engine.cards.Miasma
import com.aember.analyzer.domain.engine.cards.dslCardOverrides
import com.aember.analyzer.domain.engine.cards.ShadowSelf
import com.aember.analyzer.domain.engine.cards.Silvertooth
import com.aember.analyzer.domain.engine.cards.SpeedSigil
import com.aember.analyzer.domain.engine.cards.TendrilsOfPain
import com.aember.analyzer.domain.engine.cards.TooMuchToProtect
import com.aember.analyzer.domain.engine.cards.YxilxDominator
import com.aember.analyzer.domain.engine.cards.coreSetCards
import com.aember.analyzer.domain.engine.dsl.DslCardScript
import com.aember.analyzer.domain.engine.dsl.EffectResolver
import com.aember.analyzer.domain.engine.dsl.GreedyTargetChooser
import com.aember.analyzer.domain.engine.dsl.TargetChooser
import com.aember.analyzer.domain.model.CatalogCard
import com.aember.analyzer.domain.ports.CardCatalogPort
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CardLibraryConfig {

    @Bean
    fun cardLibrary(catalog: CardCatalogPort): CardLibrary {
        val allCards = catalog.loadAll().associateBy { it.id }
        return buildCoreCardLibrary(allCards)
    }
}

/**
 * Registers every Core-set script (generated DSL, DSL overrides and manual scripts) on a
 * library backed by the given catalog. Shared by the Spring bean and the scenario test
 * harness so tests exercise the exact production card set.
 */
fun buildCoreCardLibrary(catalog: Map<String, CatalogCard>, targetChooser: TargetChooser = GreedyTargetChooser()): CardLibrary {
    val library = CardLibrary(catalog)
    val resolver = EffectResolver(library = library, targetChooser = targetChooser)
    library.dslResolver = resolver

    // Register auto-generated Core set DSL definitions.
    coreSetCards.forEach { (id, definition) ->
        library.register(id, DslCardScript(definition, resolver))
    }

    // Manual DSL overrides for cards the generator cannot express cleanly.
    marsEngineDslCardOverrides.forEach { (id, definition) ->
        library.register(id, DslCardScript(definition, resolver))
    }
    keyCheatDslCardOverrides.forEach { (id, definition) ->
        library.register(id, DslCardScript(definition, resolver))
    }
    dslCardOverrides.forEach { (id, definition) ->
        library.register(id, DslCardScript(definition, resolver))
    }

    // Manual overrides for cards the DSL generator cannot express cleanly.
    library.register("bad-penny", BadPenny)
    library.register("anger", Anger)
    library.register("drumble", Drumble)
    library.register("ether-spider", EtherSpider)
    library.register("shadow-self", ShadowSelf)
    library.register("speed-sigil", SpeedSigil)
    library.register("control-the-weak", ControlTheWeak)
    library.register("miasma", Miasma)
    library.register("tendrils-of-pain", TendrilsOfPain)
    library.register("arise", Arise)
    library.register("silvertooth", Silvertooth)
    library.register("yxilx-dominator", YxilxDominator)
    library.register("jammer-pack", JammerPack)
    library.register("biomatrix-backup", BiomatrixBackup)
    library.register("annihilation-ritual", AnnihilationRitual)
    library.register("lash-of-broken-dreams", LashOfBrokenDreams)
    library.register("too-much-to-protect", TooMuchToProtect)
    library.register("fogbank", Fogbank)
    library.register("valdr", Valdr)

    return library
}
