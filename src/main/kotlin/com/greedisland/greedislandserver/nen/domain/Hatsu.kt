package com.greedisland.greedislandserver.nen.domain

data class Hatsu(val name: String, val category: NenCategory, val baseAuraCost: Double, val vows: List<Vow> = emptyList()) {

    fun effectiveCostFor(userCategory: NenCategory): Double{
        val rawCost =  baseAuraCost / userCategory.affinityWith(this.category)
        return (rawCost - vows.sumOf { it.auraDiscount }).coerceAtLeast(0.0)
    }
}
