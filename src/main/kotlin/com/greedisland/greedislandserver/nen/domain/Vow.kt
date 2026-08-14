package com.greedisland.greedislandserver.nen.domain

import com.greedisland.greedislandserver.nen.domain.cast.CastContext

/** Aura freed per hour the ability is *not* usable — harsher restriction, bigger payoff. */
private const val DISCOUNT_PER_RESTRICTED_HOUR: Double = 2.5

sealed interface Vow {
    val auraDiscount: Double

    fun isSatisfied(context: CastContext): Boolean
}

data class TimeWindowVow(val fromHour: Int, val toHour: Int) : Vow {
    private val usableHours: Int =
        if (fromHour <= toHour) toHour - fromHour else 24 - fromHour + toHour

    // Fewer usable hours = harsher restriction = bigger payoff. A full 24h window grants nothing.
    override val auraDiscount: Double = (24 - usableHours) * DISCOUNT_PER_RESTRICTED_HOUR

    override fun isSatisfied(context: CastContext): Boolean {
        val now = context.currentHour
        return if (fromHour <= toHour) {
            now in fromHour until toHour
        } else {
            now !in toHour..<fromHour
        }
    }
}

data class TargetRestrictedVow(val allowedTargetIds: Set<String>) : Vow {
    override val auraDiscount: Double = 75.0

    override fun isSatisfied(context: CastContext): Boolean = context.targetId in allowedTargetIds
}

data object LifeStakeVow : Vow {
    override val auraDiscount: Double = 200.0

    override fun isSatisfied(context: CastContext): Boolean = true
}
