package com.aember.analyzer.domain.engine.fidelity

enum class FidelityLevel(val score: Int) {
    FULL(100),
    PARTIAL(70),
    APPROXIMATED(40),
    VANILLA(10),
    MISSING(0)
}
