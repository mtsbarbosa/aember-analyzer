package com.aember.analyzer.domain.rating

/**
 * Compares simulated ratings with an external ground truth (e.g. hand-assigned
 * tiers) using Spearman rank correlation. A high value means the simulator
 * ranks decks the way the human labels do; it is the most direct check of
 * whether the rating tracks real deck strength.
 */
data class TierValidation(
    val decksCompared: Int,
    val spearman: Double,
    val pairwiseAgreement: Double
)

object TierValidator {

    /** [tiers]: higher = better (e.g. S+=6 ... Z=0). Decks missing from either side are ignored. */
    fun validate(ratings: List<DeckRating>, tiers: Map<String, Double>): TierValidation {
        val common = ratings.filter { it.deckUuid in tiers }
        require(common.size >= 3) { "Need at least 3 rated decks with a tier to validate" }
        val scores = common.map { it.aemberScore }
        val labels = common.map { tiers.getValue(it.deckUuid) }
        return TierValidation(common.size, spearman(scores, labels), pairwiseAgreement(scores, labels))
    }

    fun spearman(x: List<Double>, y: List<Double>): Double {
        require(x.size == y.size && x.size >= 2) { "Need two equal-length series of size >= 2" }
        val rx = ranks(x)
        val ry = ranks(y)
        val mx = rx.average()
        val my = ry.average()
        var cov = 0.0; var vx = 0.0; var vy = 0.0
        for (i in rx.indices) {
            cov += (rx[i] - mx) * (ry[i] - my)
            vx += (rx[i] - mx) * (rx[i] - mx)
            vy += (ry[i] - my) * (ry[i] - my)
        }
        return if (vx == 0.0 || vy == 0.0) 0.0 else cov / kotlin.math.sqrt(vx * vy)
    }

    /** Fraction of tier-distinct deck pairs that the rating orders the same way. */
    fun pairwiseAgreement(scores: List<Double>, labels: List<Double>): Double {
        var agree = 0.0
        var total = 0
        for (i in scores.indices) for (j in i + 1 until scores.size) {
            if (labels[i] == labels[j]) continue
            total++
            val s = scores[i].compareTo(scores[j])
            val l = labels[i].compareTo(labels[j])
            if (s == l) agree += 1.0 else if (s == 0) agree += 0.5
        }
        return if (total == 0) 0.0 else agree / total
    }

    /** Average ranks (ties share the mean rank). */
    private fun ranks(values: List<Double>): List<Double> {
        val order = values.indices.sortedBy { values[it] }
        val ranks = DoubleArray(values.size)
        var i = 0
        while (i < order.size) {
            var j = i
            while (j + 1 < order.size && values[order[j + 1]] == values[order[i]]) j++
            val avg = (i + j) / 2.0 + 1.0
            for (k in i..j) ranks[order[k]] = avg
            i = j + 1
        }
        return ranks.toList()
    }
}
