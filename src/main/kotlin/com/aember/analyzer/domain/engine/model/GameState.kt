package com.aember.analyzer.domain.engine.model

import com.aember.analyzer.domain.engine.fidelity.FidelityLevel

data class GameState(
    val players: List<PlayerState>,
    var activePlayerIndex: Int = 0,
    var firstPlayerIndex: Int = 0,
    var round: Int = 1,
    var turn: Int = 1,
    var winnerIndex: Int? = null,
    var endCause: EndCause? = null,
    val log: MutableList<LogEntry> = mutableListOf(),
    val fidelityLog: MutableList<FidelityEntry> = mutableListOf(),
    var maxRounds: Int = 40,
    var done: Boolean = false,
    var actionsThisPhase: Int = 0,
    var endPhaseRightNow: Boolean = false,
    // Temporary state used inside a single effect resolution.
    var lastStealAmount: Int = 0,
    var lastDamageDestroyed: Boolean = false,
    var lastDamagedTargetId: String? = null,
    var lastHealAmount: Int = 0,
    var lastHealedTargetId: String? = null,
    var lastSacrificeSucceeded: Boolean = false,
    var lastPurgedFromDiscard: Int = 0,

    /** The creature currently being fought (Before Fight triggers read it: Lord Golgotha, Zorg). */
    var currentFightTargetId: String? = null,

    /**
     * Gabos Longarms: when set during a fight, the attacker's damage goes to this
     * creature instead of the fight target.
     */
    var fightDamageRedirectId: String? = null,

    /** Evasion Sigil: cancel the fight currently being resolved. */
    var cancelCurrentFight: Boolean = false,

    /** Mimicry: recursion guard for chained play-as-copy resolutions. */
    var playCopyDepth: Int = 0,

    /** The event currently being dispatched by the EventBus, for event-aware effects. */
    var currentEvent: com.aember.analyzer.domain.engine.events.GameEvent? = null,

    /**
     * Turn-scoped event subscriptions (Phase 2.2, e.g. Looter Goblin / Loot the
     * Bodies); fired by the EventBus until the end of the activating turn.
     */
    val turnScopedReactions: MutableList<com.aember.analyzer.domain.engine.dsl.TurnScopedReaction> = mutableListOf(),

    // Tracks creatures temporarily controlled by an effect (instanceId -> original controller index).
    val temporaryControllers: MutableMap<String, Int> = mutableMapOf(),

    /**
     * Seeded per-match random source used for every in-game shuffle and random
     * pick so matches are reproducible from their seed. GameEngine seeds this
     * from the match seed; the default instance is only for ad-hoc usage.
     */
    var rng: java.util.Random = java.util.Random()
) {

    fun activePlayer(): PlayerState = players[activePlayerIndex]

    fun opponent(): PlayerState = players[1 - activePlayerIndex]

    fun opponent(of: PlayerState): PlayerState = players[1 - of.index]

    fun player(index: Int): PlayerState = players[index]

    fun log(message: String) {
        log.add(LogEntry(turn, round, activePlayerIndex, message))
    }

    fun logFidelity(cardId: String, level: FidelityLevel, context: String) {
        fidelityLog.add(FidelityEntry(turn, cardId, level, context))
    }

    /**
     * Lambda used to find a card that redirects an opponent's amber gain.
     * Injected by the engine so GameState stays decoupled from specific card scripts.
     */
    var amberRedirector: ((player: PlayerState, amount: Int) -> InPlayCard?)? = null

    /**
     * Adds amber to a player, redirecting it if an opponent controls a redirector.
     */
    fun gainAmber(player: PlayerState, amount: Int, logMessage: String) {
        val redirector = amberRedirector?.invoke(player, amount)
        if (redirector != null && amount > 0) {
            val before = redirector.capturedAmber
            redirector.capturedAmber += amount
            log("$logMessage, but ${redirector.cardId} captures it (${redirector.cardId}: $before -> ${redirector.capturedAmber})")
        } else {
            val before = player.amber
            player.amber += amount
            log("$logMessage (${player.name}: $before -> ${player.amber})")
        }
    }
}

enum class EndCause {
    KEYS,
    TIMEOUT,
}

/**
 * Picks a random element using the given (seeded) random source, or null when empty.
 */
fun <T> List<T>.randomWith(rng: java.util.Random): T? =
    if (isEmpty()) null else this[rng.nextInt(size)]

/**
 * Aggregate counts of in-match actions, useful for analyzing AI behavior and match dynamics.
 */
data class MatchStatistics(
    var reaps: Int = 0,
    var fights: Int = 0,
    var actionAbilitiesUsed: Int = 0,
    var omniAbilitiesUsed: Int = 0,
    var cardsPlayed: Int = 0,
    var cardsDiscarded: Int = 0,
    var keysForged: Int = 0,
    var housesChosen: MutableMap<String, Int> = mutableMapOf()
) {
    fun totalActions(): Int = reaps + fights + actionAbilitiesUsed + omniAbilitiesUsed + cardsPlayed + cardsDiscarded
}

data class LogEntry(
    val turn: Int,
    val round: Int,
    val activePlayerIndex: Int,
    val message: String
)

data class FidelityEntry(
    val turn: Int,
    val cardId: String,
    val level: FidelityLevel,
    val context: String
)
