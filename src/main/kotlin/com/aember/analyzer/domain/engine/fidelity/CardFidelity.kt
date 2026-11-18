package com.aember.analyzer.domain.engine.fidelity

data class CardFidelity(
    val level: FidelityLevel,
    val notes: String = "",
    val cardId: String = ""
)
