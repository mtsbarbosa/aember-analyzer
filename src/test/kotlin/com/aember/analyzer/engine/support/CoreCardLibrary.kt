package com.aember.analyzer.engine.support

import com.aember.analyzer.domain.engine.CardLibrary
import com.aember.analyzer.domain.engine.dsl.GreedyTargetChooser
import com.aember.analyzer.domain.engine.dsl.TargetChooser
import com.aember.analyzer.domain.model.CatalogCard
import com.aember.analyzer.infrastructure.config.buildCoreCardLibrary
import com.aember.analyzer.infrastructure.persistence.JsonCardCatalogLoader
import com.fasterxml.jackson.databind.ObjectMapper

/**
 * Builds the real Core-set [CardLibrary] (catalog + generated DSL + manual scripts) without
 * Spring, so parity tests run the exact production card implementations.
 *
 * The catalog comes from the keyteki Master Vault data (same source the app seeds from);
 * override the location with the `KEYTEKI_DATA_PATH` environment variable.
 */
object CoreCardLibrary {

    val keytekiDataPath: String =
        System.getenv("KEYTEKI_DATA_PATH") ?: "/home/mateus/work/keyteki/master-vault-data"

    /** Full Core-set catalog, shared by the library and by tests that audit card coverage. */
    val cards: List<CatalogCard> by lazy {
        val loader = JsonCardCatalogLoader(ObjectMapper(), keytekiDataPath)
        loader.loadCoreSet()
    }

    private val catalog: Map<String, CatalogCard> by lazy {
        cards.associateBy { it.id }
    }

    fun create(targetChooser: TargetChooser = GreedyTargetChooser()): CardLibrary =
        buildCoreCardLibrary(catalog, targetChooser)
}
