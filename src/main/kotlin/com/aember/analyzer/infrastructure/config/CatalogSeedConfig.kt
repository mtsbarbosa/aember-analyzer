package com.aember.analyzer.infrastructure.config

import com.aember.analyzer.infrastructure.persistence.JsonCardCatalogLoader
import com.aember.analyzer.infrastructure.persistence.JpaCardCatalogAdapter
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order

@Configuration
@Profile("!test")
class CatalogSeedConfig {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    fun seedCatalog(
        loader: JsonCardCatalogLoader,
        adapter: JpaCardCatalogAdapter
    ): CommandLineRunner = CommandLineRunner {
        if (adapter.loadAll().isEmpty()) {
            logger.info("Seeding card catalog from keyteki data")
            adapter.seedHousesAndExpansion()
            val cards = loader.loadCoreSet()
            adapter.seed(cards)
            logger.info("Seeded ${cards.size} catalog cards")
        } else {
            logger.info("Card catalog already populated; skipping seed")
        }
    }
}
