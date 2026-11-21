package com.aember.analyzer.deck

import com.aember.analyzer.IntegrationTest
import com.aember.analyzer.domain.engine.CardLibrary
import com.aember.analyzer.domain.engine.fidelity.FidelityLevel
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@Disabled("Diagnostic report — run manually with --tests DeckFidelityReportTest")
class DeckFidelityReportTest : IntegrationTest() {

    @Autowired
    lateinit var cardLibrary: CardLibrary

    private val galilee = listOf(
        "control-the-weak", "red-hot-armor", "drumble", "dust-imp", "ember-imp",
        "overlord-greking", "shaffles", "the-terror", "tocsin", "flame-wreathed",
        "ammonia-clouds", "hypnotic-command", "shatter-storm", "invasion-portal",
        "mothergun", "ether-spider", "grabber-jammer", "john-smyth", "mindwarper",
        "ulyq-megamouth", "yxili-marauder", "yxilx-dominator", "hidden-stash",
        "nerve-blast", "relentless-whispers", "bad-penny", "macis-asp", "nexus",
        "old-bruno", "shadow-self", "umbra", "duskrunner"
    )

    private val mehitabel = listOf(
        "arise", "control-the-weak", "tendrils-of-pain", "lash-of-broken-dreams",
        "drumble", "dust-imp", "ember-imp", "shaffles", "shooler", "succubus",
        "the-terror", "bouncing-deathquark", "labwork", "library-access", "phase-shift",
        "remote-access", "sloppy-labwork", "wild-wormhole", "chaos-portal",
        "library-of-babble", "batdrone", "dr-escotera", "neutron-shark", "booby-trap",
        "ghostly-hand", "miasma", "nerve-blast", "relentless-whispers", "speed-sigil",
        "macis-asp", "dodger", "silvertooth", "smiling-ruth", "umbra"
    )

    @Test
    fun `print fidelity report for Mehitabel and Galilee`() {
        println("\n=== Galilee ===")
        report(galilee)
        println("\n=== Mehitabel ===")
        report(mehitabel)
    }

    private fun report(cardIds: List<String>) {
        val counts = mutableMapOf<FidelityLevel, Int>()
        cardIds.sorted().forEach { cardId ->
            val level = cardLibrary.fidelityFor(cardId)
            counts[level] = (counts[level] ?: 0) + 1
            if (level != FidelityLevel.FULL && level != FidelityLevel.VANILLA) {
                val card = cardLibrary.cardData(cardId)
                println("$cardId -> $level | ${card?.text ?: ""}")
            }
        }
        println("Summary: $counts")
    }
}
