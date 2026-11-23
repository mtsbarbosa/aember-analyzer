package com.aember.analyzer.domain.engine.fidelity

/**
 * [UNVERIFIED] is never declared by a card script; it is assigned by the verified view of a
 * fidelity report (see [FidelityReportService]) when a card declared as [FULL] has no passing
 * parity test. Its score is deliberately more pessimistic than [PARTIAL] so an untested
 * "full" claim is distinguishable from an honestly-declared partial implementation in the
 * verified score.
 */
enum class FidelityLevel(val score: Int) {
    FULL(100),
    UNVERIFIED(60),
    PARTIAL(70),
    APPROXIMATED(40),
    VANILLA(10),
    MISSING(0)
}
