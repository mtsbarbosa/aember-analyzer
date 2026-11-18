package com.aember.analyzer.infrastructure.rest

import com.aember.analyzer.domain.model.Deck
import com.aember.analyzer.domain.ports.CardCatalogPort
import com.aember.analyzer.domain.ports.DeckImportException
import com.aember.analyzer.domain.ports.DeckImportPort
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.web.client.RestClientException

@Component
class MasterVaultDeckImportAdapter(
    private val cardCatalog: CardCatalogPort,
    private val objectMapper: ObjectMapper
) : DeckImportPort {

    private val logger = LoggerFactory.getLogger(javaClass)
    private val restClient = RestClient.builder()
        .baseUrl("https://www.keyforgegame.com")
        .build()

    override fun importByUuid(uuid: String): Deck {
        logger.info("Importing deck $uuid from Master Vault")
        val response = try {
            restClient.get()
                .uri("/api/decks/{uuid}/?links=cards", uuid)
                .retrieve()
                .body(String::class.java)
                ?: throw DeckImportException("Empty response for deck $uuid")
        } catch (e: RestClientException) {
            throw DeckImportException("Failed to fetch deck $uuid", e)
        }

        if (response.startsWith("<")) {
            throw DeckImportException("Invalid response from Master Vault for deck $uuid")
        }

        return MasterVaultDeckParser.parse(objectMapper.readTree(response), cardCatalog)
    }
}
