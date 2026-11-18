package com.aember.analyzer.domain.engine

interface IdGenerator {
    fun nextId(prefix: String): String
}

class SeededIdGenerator(seed: Long) : IdGenerator {
    private val random = java.util.Random(seed)
    private var counter = 0

    override fun nextId(prefix: String): String {
        counter++
        return "$prefix-${random.nextLong().toULong().toString(36)}-$counter"
    }
}
