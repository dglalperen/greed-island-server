package com.greedisland.greedislandserver.nen.domain

class NenUser(val id: String, val category: NenCategory, var auraCapacity: Double) {
    fun canCast(hatsu: Hatsu): Boolean =
        hatsu.effectiveCostFor(category) <= auraCapacity
}