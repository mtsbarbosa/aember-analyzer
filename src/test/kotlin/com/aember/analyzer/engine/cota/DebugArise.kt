package com.aember.analyzer.engine.cota

import com.aember.analyzer.domain.model.House
import com.aember.analyzer.engine.support.scenario
import org.junit.jupiter.api.Test

class DebugAriseTest {
    @Test
    fun debug() {
        val scenario = scenario {
            player1 {
                house(House.UNTAMED)
                hand("arise")
                discard("ancient-bear", "snufflegator", "dust-imp")
            }
        }
        scenario.run {
            play("arise")
            println("HAND=${p1.hand} DISCARD=${p1.discard} CHAINS=${p1.chains} AMBER=${p1.amber}")
            game.log.forEach { println("LOG: ${it.message}") }
        }
    }
}
