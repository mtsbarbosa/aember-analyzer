package com.aember.analyzer.domain.ai

/**
 * Configurable parameters that drive [TacticalAI] decision-making.
 *
 * All weights are relative; higher values make the AI prefer that action type.
 */
data class AISettings(
    val name: String = "tactical",
    val reapWeight: Double = 1.0,
    val fightWeight: Double = 1.0,
    val actionAbilityWeight: Double = 0.8,
    val playCardWeight: Double = 0.7,
    val discardWeight: Double = 0.1,
    val boardControlWeight: Double = 0.5,
    val threatRemovalWeight: Double = 0.6,
    val amberControlThreatWeight: Double = 1.0,
    val forgeUrgencyThreshold: Int = 5,
    val forgeUrgencyBonus: Double = 2.0,
    val secondKeyUrgencyBonus: Double = 1.0
) {
    companion object {
        /**
         * Default tactical profile: balances reaping, fighting, and ability usage.
         */
        val DEFAULT = AISettings()

        /**
         * Aggressive profile: prioritizes board control and combat over amber generation.
         */
        val AGGRESSIVE = AISettings(
            name = "aggressive",
            reapWeight = 0.8,
            fightWeight = 1.3,
            actionAbilityWeight = 1.0,
            boardControlWeight = 0.8,
            threatRemovalWeight = 1.0
        )

        /**
         * Economic profile: prioritizes reaping and forging keys quickly.
         */
        val ECONOMIC = AISettings(
            name = "economic",
            reapWeight = 1.4,
            fightWeight = 0.6,
            actionAbilityWeight = 0.6,
            forgeUrgencyBonus = 3.0
        )
    }
}
