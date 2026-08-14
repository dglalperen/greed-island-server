package com.greedisland.greedislandserver.nen.domain

import com.greedisland.greedislandserver.nen.domain.cast.CastContext
import com.greedisland.greedislandserver.nen.domain.cast.CastResult

class NenUser(val id: String, val category: NenCategory, var auraCapacity: Double) {

    fun canCast(hatsu: Hatsu): Boolean =
        hatsu.effectiveCostFor(category) <= auraCapacity

    /**
     * The authoritative cast decision: every vow's condition must currently hold,
     * and the user must afford the (discounted) effective cost.
     *
     * Pure decision — it does NOT spend aura; the caller applies [CastResult.Success.auraSpent].
     */
    fun attemptCast(hatsu: Hatsu, context: CastContext): CastResult {
        val unsatisfied = hatsu.vows.filter { vow -> !vow.isSatisfied(context) }
        if (unsatisfied.isNotEmpty()) {
            return CastResult.VowConditionsNotMet(unsatisfied)
        }

        val cost = hatsu.effectiveCostFor(category)
        if (cost > auraCapacity) {
            return CastResult.NotEnoughAura(required = cost, available = auraCapacity)
        }

        return CastResult.Success(auraSpent = cost)
    }
}
