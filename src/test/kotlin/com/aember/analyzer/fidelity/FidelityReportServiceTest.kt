package com.aember.analyzer.fidelity

import com.aember.analyzer.domain.engine.fidelity.FidelityLevel
import com.aember.analyzer.domain.engine.fidelity.FidelityReportService
import com.aember.analyzer.domain.engine.model.FidelityEntry
import com.aember.analyzer.domain.engine.model.GameState
import com.aember.analyzer.domain.engine.model.PlayerState
import com.aember.analyzer.domain.model.Deck
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class FidelityReportServiceTest {

    private fun game(entries: List<FidelityEntry>) = GameState(
        players = listOf(
            PlayerState(0, "A", Deck(uuid = "a", name = "A", expansion = 341, identity = "a", houses = emptyList(), cards = emptyList())),
            PlayerState(1, "B", Deck(uuid = "b", name = "B", expansion = 341, identity = "b", houses = emptyList(), cards = emptyList()))
        ),
        fidelityLog = entries.toMutableList()
    )

    @Test
    fun `should calculate average fidelity score`() {
        val report = FidelityReportService.report(
            game(
                listOf(
                    FidelityEntry(1, "full-card", FidelityLevel.FULL, "play"),
                    FidelityEntry(1, "partial-card", FidelityLevel.PARTIAL, "play"),
                    FidelityEntry(1, "vanilla-card", FidelityLevel.VANILLA, "play")
                )
            ).fidelityLog,
            verifiedCardIds = emptySet()
        )

        assertThat(report.score).isEqualTo((100 + 70 + 10) / 3.0)
        assertThat(report.levelCounts[FidelityLevel.FULL]).isEqualTo(1)
        assertThat(report.levelCounts[FidelityLevel.PARTIAL]).isEqualTo(1)
        assertThat(report.levelCounts[FidelityLevel.VANILLA]).isEqualTo(1)
    }

    @Test
    fun `declared FULL entries count as UNVERIFIED without a parity test`() {
        val report = FidelityReportService.report(
            game(
                listOf(
                    FidelityEntry(1, "full-card", FidelityLevel.FULL, "play"),
                    FidelityEntry(1, "partial-card", FidelityLevel.PARTIAL, "play")
                )
            ).fidelityLog,
            verifiedCardIds = emptySet()
        )

        assertThat(report.verifiedLevelCounts[FidelityLevel.UNVERIFIED]).isEqualTo(1)
        assertThat(report.verifiedLevelCounts[FidelityLevel.PARTIAL]).isEqualTo(1)
        assertThat(report.unverifiedCount).isEqualTo(1)
        // Declared view is unchanged.
        assertThat(report.levelCounts[FidelityLevel.FULL]).isEqualTo(1)
    }

    @Test
    fun `verified FULL entries stay FULL in the verified view`() {
        val report = FidelityReportService.report(
            game(listOf(FidelityEntry(1, "full-card", FidelityLevel.FULL, "play"))).fidelityLog,
            verifiedCardIds = setOf("full-card")
        )

        assertThat(report.verifiedLevelCounts[FidelityLevel.FULL]).isEqualTo(1)
        assertThat(report.unverifiedCount).isEqualTo(0)
        assertThat(report.verifiedScore).isEqualTo(100.0)
        assertThat(report.score).isEqualTo(100.0)
    }

    @Test
    fun `verified score is more conservative than declared score`() {
        val report = FidelityReportService.report(
            game(
                listOf(
                    FidelityEntry(1, "full-card", FidelityLevel.FULL, "play"),
                    FidelityEntry(1, "another-full-card", FidelityLevel.FULL, "play")
                )
            ).fidelityLog,
            verifiedCardIds = emptySet()
        )

        assertThat(report.score).isEqualTo(100.0)
        assertThat(report.verifiedScore).isEqualTo(60.0)
        assertThat(report.lowestVerifiedCards).containsExactly("another-full-card" to FidelityLevel.UNVERIFIED, "full-card" to FidelityLevel.UNVERIFIED)
    }

    @Test
    fun `should pass threshold when score is high enough`() {
        val report = FidelityReportService.report(
            game(listOf(FidelityEntry(1, "c", FidelityLevel.FULL, "play"))).fidelityLog,
            verifiedCardIds = emptySet()
        )
        assertThat(FidelityReportService.passesThreshold(report, 90.0)).isTrue()
    }

    @Test
    fun `should fail threshold when score is too low`() {
        val report = FidelityReportService.report(
            game(listOf(FidelityEntry(1, "c", FidelityLevel.VANILLA, "play"))).fidelityLog,
            verifiedCardIds = emptySet()
        )
        assertThat(FidelityReportService.passesThreshold(report, 50.0)).isFalse()
    }
}
