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

    @Test
    fun `should calculate average fidelity score`() {
        val game = GameState(
            players = listOf(
                PlayerState(0, "A", Deck(uuid = "a", name = "A", expansion = 341, identity = "a", houses = emptyList(), cards = emptyList())),
                PlayerState(1, "B", Deck(uuid = "b", name = "B", expansion = 341, identity = "b", houses = emptyList(), cards = emptyList()))
            ),
            fidelityLog = mutableListOf(
                FidelityEntry(1, "full-card", FidelityLevel.FULL, "play"),
                FidelityEntry(1, "partial-card", FidelityLevel.PARTIAL, "play"),
                FidelityEntry(1, "vanilla-card", FidelityLevel.VANILLA, "play")
            )
        )

        val report = FidelityReportService.report(game)

        assertThat(report.score).isEqualTo((100 + 70 + 10) / 3.0)
        assertThat(report.levelCounts[FidelityLevel.FULL]).isEqualTo(1)
        assertThat(report.levelCounts[FidelityLevel.PARTIAL]).isEqualTo(1)
        assertThat(report.levelCounts[FidelityLevel.VANILLA]).isEqualTo(1)
    }

    @Test
    fun `should pass threshold when score is high enough`() {
        val report = FidelityReportService.report(
            GameState(
                players = emptyList(),
                fidelityLog = mutableListOf(FidelityEntry(1, "c", FidelityLevel.FULL, "play"))
            )
        )
        assertThat(FidelityReportService.passesThreshold(report, 90.0)).isTrue()
    }

    @Test
    fun `should fail threshold when score is too low`() {
        val report = FidelityReportService.report(
            GameState(
                players = emptyList(),
                fidelityLog = mutableListOf(FidelityEntry(1, "c", FidelityLevel.VANILLA, "play"))
            )
        )
        assertThat(FidelityReportService.passesThreshold(report, 50.0)).isFalse()
    }
}
