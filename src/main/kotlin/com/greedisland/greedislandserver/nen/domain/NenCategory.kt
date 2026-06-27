package com.greedisland.greedislandserver.nen.domain

import kotlin.math.abs

enum class NenCategory(val hexPosition: Int) {
    ENHANCEMENT(0),
    EMISSION(1),
    MANIPULATION(2),
    SPECIALIZATION(3),
    CONJURATION(4),
    TRANSMUTATION(5);

    fun affinityWith(other: NenCategory): Double {
        if(other.hexPosition == 3 && this.hexPosition != 3) return 0.0

        val rawGap = abs(this.hexPosition - other.hexPosition)

        val ringDistance = minOf(rawGap, 6 - rawGap)

        return 1.0 - (ringDistance * 0.2)
    }
}