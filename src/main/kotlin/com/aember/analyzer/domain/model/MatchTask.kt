package com.aember.analyzer.domain.model

/**
 * A single match to be simulated within a multi-deck batch.
 *
 * [deckUuidA] is seated as player 0 and [deckUuidB] as player 1; the schedule
 * generator alternates seats across matches of the same pairing.
 * [seed] is deterministic: baseSeed + global task index.
 */
data class MatchTask(
    val taskId: Int,
    val pairIndex: Int,
    val matchIndexInPair: Int,
    val deckUuidA: String,
    val deckUuidB: String,
    val seed: Long
)

/**
 * Generates the full round-robin schedule for a pool of decks.
 *
 * Deck pairs are produced in canonical order (i < j over the given UUID order)
 * so schedules are stable for a given input list.
 */
object RoundRobinScheduleGenerator {

    fun generate(
        deckUuids: List<String>,
        matchesPerPairing: Int,
        seatAlternation: Boolean = true,
        baseSeed: Long
    ): List<MatchTask> {
        require(matchesPerPairing > 0) { "matchesPerPairing must be positive" }
        val tasks = mutableListOf<MatchTask>()
        var taskId = 0
        var pairIndex = 0
        for (i in deckUuids.indices) {
            for (j in i + 1 until deckUuids.size) {
                for (m in 0 until matchesPerPairing) {
                    val swap = seatAlternation && m % 2 == 1
                    tasks += MatchTask(
                        taskId = taskId,
                        pairIndex = pairIndex,
                        matchIndexInPair = m,
                        deckUuidA = if (swap) deckUuids[j] else deckUuids[i],
                        deckUuidB = if (swap) deckUuids[i] else deckUuids[j],
                        seed = baseSeed + taskId
                    )
                    taskId++
                }
                pairIndex++
            }
        }
        return tasks
    }

    fun pairings(deckCount: Int): Int = deckCount * (deckCount - 1) / 2
}
