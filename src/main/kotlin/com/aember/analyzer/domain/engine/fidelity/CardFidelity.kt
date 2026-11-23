package com.aember.analyzer.domain.engine.fidelity

/**
 * Declared fidelity metadata for a card script. The "is this card verified?" question is
 * answered by [VerifiedCardRegistry] (backed by `fidelity/verified-cards.txt`), which is the
 * single source of truth — do not add a per-script verified flag.
 */
data class CardFidelity(
    val level: FidelityLevel,
    val notes: String = "",
    val cardId: String = ""
)
