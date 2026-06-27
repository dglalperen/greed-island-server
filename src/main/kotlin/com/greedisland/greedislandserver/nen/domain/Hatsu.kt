package com.greedisland.greedislandserver.nen.domain

data class Hatsu(val name: String, val category: NenCategory, val baseAuraCost: Double) {

    fun effectiveCostFor(userCategory: NenCategory): Double{
        return baseAuraCost / userCategory.affinityWith(this.category)
    }
}
