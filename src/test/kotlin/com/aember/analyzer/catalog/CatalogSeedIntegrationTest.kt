package com.aember.analyzer.catalog

import com.aember.analyzer.IntegrationTest
import com.aember.analyzer.domain.ports.CardCatalogPort
import com.aember.analyzer.infrastructure.persistence.JpaCardCatalogAdapter
import com.aember.analyzer.infrastructure.persistence.JsonCardCatalogLoader
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class CatalogSeedIntegrationTest : IntegrationTest() {

    @Autowired
    private lateinit var loader: JsonCardCatalogLoader

    @Autowired
    private lateinit var adapter: JpaCardCatalogAdapter

    @Autowired
    private lateinit var catalog: CardCatalogPort

    @BeforeEach
    fun seed() {
        if (catalog.loadAll().isEmpty()) {
            adapter.seedHousesAndExpansion()
            adapter.seed(loader.loadCoreSet())
        }
    }

    @Test
    fun `should seed Core set catalog cards`() {
        val cards = catalog.loadByExpansion(341)
        assertThat(cards).isNotEmpty
        assertThat(cards).allMatch { it.id.isNotBlank() }
    }

    @Test
    fun `should find a known Core card by id`() {
        val anger = catalog.findById("anger")
        assertThat(anger).isNotNull
        assertThat(anger!!.name).isEqualTo("Anger")
        assertThat(anger.house).isEqualTo(com.aember.analyzer.domain.model.House.BROBNAR)
    }
}
