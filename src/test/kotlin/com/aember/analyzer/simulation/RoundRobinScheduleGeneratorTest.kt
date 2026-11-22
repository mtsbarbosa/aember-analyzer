package com.aember.analyzer.simulation

import com.aember.analyzer.domain.model.RoundRobinScheduleGenerator
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class RoundRobinScheduleGeneratorTest {

    @Test
    fun `generates one match per task with correct pair count`() {
        val decks = listOf("a", "b", "c", "d")

        val tasks = RoundRobinScheduleGenerator.generate(
            deckUuids = decks,
            matchesPerPairing = 2,
            seatAlternation = true,
            baseSeed = 1000
        )

        // 4 decks -> 6 pairings, 2 matches per pairing = 12 tasks
        assertThat(tasks).hasSize(12)
        assertThat(tasks.map { it.taskId }).isEqualTo((0 until 12).toList())
        assertThat(tasks.map { it.seed }).isEqualTo((1000L until 1012L).toList())
        assertThat(tasks.groupBy { it.pairIndex }.keys).hasSize(6)
    }

    @Test
    fun `every unordered pair appears exactly matchesPerPairing times`() {
        val decks = listOf("a", "b", "c", "d", "e")

        val tasks = RoundRobinScheduleGenerator.generate(
            deckUuids = decks,
            matchesPerPairing = 3,
            seatAlternation = true,
            baseSeed = 0
        )

        val pairCounts = tasks.groupBy { listOf(it.deckUuidA, it.deckUuidB).sorted() }
        assertThat(pairCounts.keys).hasSize(10) // 5 * 4 / 2
        pairCounts.values.forEach { assertThat(it).hasSize(3) }
    }

    @Test
    fun `seat alternation swaps decks on odd match indices`() {
        val decks = listOf("a", "b")

        val tasks = RoundRobinScheduleGenerator.generate(
            deckUuids = decks,
            matchesPerPairing = 4,
            seatAlternation = true,
            baseSeed = 7
        )

        assertThat(tasks[0].deckUuidA).isEqualTo("a")
        assertThat(tasks[0].deckUuidB).isEqualTo("b")
        assertThat(tasks[1].deckUuidA).isEqualTo("b")
        assertThat(tasks[1].deckUuidB).isEqualTo("a")
        assertThat(tasks[2].deckUuidA).isEqualTo("a")
        assertThat(tasks[3].deckUuidA).isEqualTo("b")
    }

    @Test
    fun `no alternation keeps canonical seat order`() {
        val decks = listOf("a", "b")

        val tasks = RoundRobinScheduleGenerator.generate(
            deckUuids = decks,
            matchesPerPairing = 3,
            seatAlternation = false,
            baseSeed = 7
        )

        tasks.forEach {
            assertThat(it.deckUuidA).isEqualTo("a")
            assertThat(it.deckUuidB).isEqualTo("b")
        }
    }

    @Test
    fun `schedule is deterministic for the same inputs`() {
        val decks = listOf("a", "b", "c")

        val first = RoundRobinScheduleGenerator.generate(decks, 2, true, 42)
        val second = RoundRobinScheduleGenerator.generate(decks, 2, true, 42)

        assertThat(first).isEqualTo(second)
    }

    @Test
    fun `rejects non-positive matchesPerPairing`() {
        assertThatThrownBy {
            RoundRobinScheduleGenerator.generate(listOf("a", "b"), 0, true, 1)
        }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `pairings helper computes D times D minus one over two`() {
        assertThat(RoundRobinScheduleGenerator.pairings(2)).isEqualTo(1)
        assertThat(RoundRobinScheduleGenerator.pairings(10)).isEqualTo(45)
        assertThat(RoundRobinScheduleGenerator.pairings(50)).isEqualTo(1225)
    }
}
