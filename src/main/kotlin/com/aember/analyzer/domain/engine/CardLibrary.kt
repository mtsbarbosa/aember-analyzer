package com.aember.analyzer.domain.engine

import com.aember.analyzer.domain.engine.fidelity.FidelityLevel
import com.aember.analyzer.domain.model.CatalogCard

/**
 * Registry of hand-ported card behaviors. Unknown cards fall back to a vanilla script that
 * does nothing, so the engine can still run games while card coverage is incomplete.
 */
class CardLibrary(
    private val catalog: Map<String, CatalogCard>
) {

    private val scripts: MutableMap<String, CardScript> = mutableMapOf()

    /** The DSL resolver created by [com.aember.analyzer.infrastructure.config.buildCoreCardLibrary], if any. */
    var dslResolver: com.aember.analyzer.domain.engine.dsl.EffectResolver? = null

    fun register(cardId: String, script: CardScript) {
        scripts[cardId] = script
    }

    fun scriptFor(cardId: String): CardScript = scripts[cardId] ?: VanillaCardScript

    fun registeredIds(): Set<String> = scripts.keys.toSet()

    fun cardData(cardId: String): CatalogCard? = catalog[cardId]

    fun fidelityFor(cardId: String): FidelityLevel {
        return scriptFor(cardId).fidelity().level
    }
}
