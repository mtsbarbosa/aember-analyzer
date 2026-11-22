package com.aember.analyzer.simulation

import com.aember.analyzer.application.MassSimulationService
import com.aember.analyzer.application.SimulationOutcome
import com.aember.analyzer.application.SimulationService
import com.aember.analyzer.domain.engine.fidelity.FidelityReport
import com.aember.analyzer.domain.engine.model.EndCause
import com.aember.analyzer.domain.engine.model.GameState
import com.aember.analyzer.domain.engine.model.PlayerState
import com.aember.analyzer.domain.model.Deck
import com.aember.analyzer.domain.model.House
import com.aember.analyzer.domain.model.MassSimulationBatch
import com.aember.analyzer.domain.model.MassSimulationMatchLog
import com.aember.analyzer.domain.model.MassSimulationMatchResult
import com.aember.analyzer.domain.model.MultiDeckSimulationRequest
import com.aember.analyzer.domain.model.MassSimulationStatus
import com.aember.analyzer.domain.rating.DeckRating
import com.aember.analyzer.domain.ports.DeckRepository
import com.aember.analyzer.domain.ports.MassSimulationRepository
import com.aember.analyzer.domain.ports.MassSimulationRunner
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * Verifies that batch progress ([MassSimulationBatch.completedCount]) is
 * incremented while a multi-deck batch is RUNNING.
 */
class MassSimulationServiceProgressTest {

    @Test
    fun `completedCount increments while the batch is RUNNING`() {
        val repository = InMemoryMassSimulationRepository()

        // Two decks x 3 matches per pairing -> 3 matches with seeds 0, 1, 2.
        // Each simulated match blocks until the test releases it.
        val matchLatches = listOf(CountDownLatch(1), CountDownLatch(1), CountDownLatch(1))
        val simulationService = Mockito.mock(
            SimulationService::class.java,
            Mockito.withSettings().defaultAnswer { invocation ->
                val index = invocation.arguments[2] as Long
                matchLatches[index.toInt()].await(5, TimeUnit.SECONDS)
                fakeOutcome(winnerIndex = (index % 2).toInt())
            }
        )

        val service = MassSimulationService(
            deckRepository = FakeDeckRepository(),
            repository = repository,
            runner = object : MassSimulationRunner {
                override fun runAsync(task: Runnable) {
                    task.run()
                }
            },
            simulationService = simulationService
        )

        val request = MultiDeckSimulationRequest(
            deckUuids = listOf("deck-a", "deck-b"),
            matchesPerPairing = 3,
            baseSeed = 0
        )
        val started = CompletableFuture.runAsync { service.runMultiDeckSimulation(request, recordLogs = false) }

        // Release match 0 so its result is saved while match 1 is still blocked.
        matchLatches[0].countDown()

        // Match 0 finishes: progress must become 1 while the batch is still RUNNING.
        assertThat(
            awaitUntil { repository.batches.values.firstOrNull()?.completedCount == 1 }
        ).isTrue()
        val running = repository.batches.values.first()
        assertThat(running.status).isEqualTo(MassSimulationStatus.RUNNING)
        assertThat(running.completedCount).isEqualTo(1)

        matchLatches.drop(1).forEach { it.countDown() }
        started.get(5, TimeUnit.SECONDS)

        assertThat(
            awaitUntil { repository.batches.values.first().status == MassSimulationStatus.COMPLETED }
        ).isTrue()
        val completed = repository.batches.values.first()
        assertThat(completed.completedCount).isEqualTo(3)
        assertThat(completed.failedCount).isEqualTo(0)
    }

    private fun awaitUntil(
        timeoutMs: Long = 5000,
        predicate: () -> Boolean
    ): Boolean {
        val deadline = System.currentTimeMillis() + timeoutMs
        while (System.currentTimeMillis() < deadline) {
            if (predicate()) return true
            Thread.sleep(10)
        }
        return predicate()
    }

    private fun fakeOutcome(winnerIndex: Int): SimulationOutcome {
        val players = listOf(
            PlayerState(index = 0, name = "Player A", deck = fakeDeck("Player A")),
            PlayerState(index = 1, name = "Player B", deck = fakeDeck("Player B"))
        )
        val state = GameState(players = players, winnerIndex = winnerIndex, endCause = EndCause.KEYS)
        state.round = 10
        state.turn = 20
        return SimulationOutcome(state, FidelityReport(score = 90.0, levelCounts = emptyMap(), lowestCards = emptyList()), false)    }

    private fun fakeDeck(name: String) = Deck(
        uuid = name.lowercase().replace(" ", "-"),
        name = name,
        expansion = 341,
        identity = name.lowercase().replace(" ", "-"),
        houses = listOf(House.DIS, House.SANCTUM, House.UNTAMED),
        cards = emptyList()
    )

    private class FakeDeckRepository : DeckRepository {
        private val decks = mutableMapOf<String, Deck>()

        init {
            listOf("deck-a", "deck-b").forEach {
                decks[it] = fakeDeckName(it)
            }
        }

        private fun fakeDeckName(uuid: String) = Deck(
            uuid = uuid,
            name = uuid,
            expansion = 341,
            identity = uuid,
            houses = listOf(House.DIS, House.SANCTUM, House.UNTAMED),
            cards = emptyList()
        )

        override fun save(deck: Deck): Deck = deck.also { decks[it.uuid] = it }
        override fun findByUuid(uuid: String): Deck? = decks[uuid]
        override fun existsByUuid(uuid: String): Boolean = uuid in decks
        override fun search(criteria: com.aember.analyzer.domain.ports.DeckSearchCriteria): List<Deck> = decks.values.toList()
    }

    private class InMemoryMassSimulationRepository : MassSimulationRepository {
        val batches = java.util.concurrent.ConcurrentHashMap<String, MassSimulationBatch>()
        val results = java.util.concurrent.CopyOnWriteArrayList<MassSimulationMatchResult>()

        override fun save(batch: MassSimulationBatch) {
            batches[batch.uuid] = batch
        }

        override fun findByUuid(uuid: String): MassSimulationBatch? = batches[uuid]

        override fun findAll(): List<MassSimulationBatch> = batches.values.toList()

        override fun saveResult(result: MassSimulationMatchResult) {
            results += result
        }

        override fun findResultsByBatchUuid(uuid: String): List<MassSimulationMatchResult> =
            results.filter { it.batchUuid == uuid }

        override fun saveLog(log: MassSimulationMatchLog) = Unit

        override fun findLog(batchUuid: String, matchResultId: Int): MassSimulationMatchLog? = null

        override fun saveRatings(batchUuid: String, ratings: List<DeckRating>) = Unit

        override fun findRatings(batchUuid: String): List<DeckRating>? = null
    }
}
