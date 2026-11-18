package com.aember.analyzer

import io.zonky.test.db.postgres.embedded.EmbeddedPostgres
import org.junit.jupiter.api.BeforeAll
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource

@ActiveProfiles("test")
@SpringBootTest
abstract class IntegrationTest {

    companion object {
        private lateinit var embeddedPostgres: EmbeddedPostgres

        @JvmStatic
        @BeforeAll
        fun startPostgres() {
            if (!::embeddedPostgres.isInitialized) {
                embeddedPostgres = EmbeddedPostgres.start()
                Runtime.getRuntime().addShutdownHook(Thread { embeddedPostgres.close() })
            }
        }

        @JvmStatic
        @DynamicPropertySource
        fun configure(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url") { embeddedPostgres.getJdbcUrl("postgres", "postgres") }
            registry.add("spring.datasource.username") { "postgres" }
            registry.add("spring.datasource.password") { "postgres" }
        }
    }
}
