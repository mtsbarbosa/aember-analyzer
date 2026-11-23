package com.aember.analyzer.domain.engine.fidelity

import java.io.FileNotFoundException

/**
 * Registry of card ids that have a passing parity test against keyteki behaviour.
 *
 * The list lives in `src/main/resources/fidelity/verified-cards.txt` (one engine card id per
 * line, `#` for comments) so the fidelity report, the ratchet test and the inventory tool can
 * all read the same source of truth. A card is only treated as FULL in the *verified* view of
 * a fidelity report while its id appears here.
 */
object VerifiedCardRegistry {

    val verifiedCardIds: Set<String> by lazy { load() }

    fun isVerified(cardId: String): Boolean = cardId in verifiedCardIds

    private fun load(): Set<String> {
        val stream = VerifiedCardRegistry::class.java.getResourceAsStream(VERIFIED_CARDS_RESOURCE)
            ?: throw FileNotFoundException(
                "Missing classpath resource $VERIFIED_CARDS_RESOURCE; create it (possibly empty) " +
                    "so the verified fidelity view has a source of truth"
            )
        return stream.bufferedReader().readLines()
            .map { it.substringBefore('#').trim() }
            .filter { it.isNotEmpty() }
            .toSet()
    }

    private const val VERIFIED_CARDS_RESOURCE = "/fidelity/verified-cards.txt"
}
