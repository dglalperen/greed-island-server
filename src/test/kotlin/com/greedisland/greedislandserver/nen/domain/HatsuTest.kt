package com.greedisland.greedislandserver.nen.domain

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class HatsuTest {
    @Test
    fun `same category should pay base cost`(){
        val ability = Hatsu(name="Jajanken", category = NenCategory.ENHANCEMENT, baseAuraCost = 50.0)
        assertEquals(50.0, ability.effectiveCostFor(NenCategory.ENHANCEMENT))
    }

    @Test
    fun `specialization costs infinity for enhancer`(){

        val ability = Hatsu(name="Jajanken", category = NenCategory.SPECIALIZATION, baseAuraCost = 50.0)
        assertEquals(Double.POSITIVE_INFINITY, ability.effectiveCostFor(NenCategory.ENHANCEMENT))
    }
}