package com.greedisland.greedislandserver.nen.domain

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class HatsuTest {
    @Test
    fun `same category should pay base cost`(){
        val ability = Hatsu(name="Jajanken", category = NenCategory.ENHANCEMENT, baseAuraCost = 50.0)
        assertEquals(50.0, ability.effectiveCostFor(NenCategory.ENHANCEMENT))
    }

    @Test
    fun `specialization costs infinity for enhancer`(){
        val ability = Hatsu(name="JajankenSpecialization", category = NenCategory.SPECIALIZATION, baseAuraCost = 50.0)
        assertEquals(Double.POSITIVE_INFINITY, ability.effectiveCostFor(NenCategory.ENHANCEMENT))
    }

    @Test
    fun `an off-category ability costs more`() {
        // Transmutation ability cast by an Enhancer: affinity 0.8 -> 100 / 0.8 = 125
        val ability = Hatsu("BorrowedSkill", NenCategory.TRANSMUTATION, 100.0)
        assertEquals(125.0, ability.effectiveCostFor(NenCategory.ENHANCEMENT), 0.0001)
    }

    @Test
    fun `a vow reduces the effective cost`() {
        // TimeWindowVow(20, 4): 8h window -> 16 restricted hours * 2.5 = 40 discount
        val vows = listOf<Vow>(TimeWindowVow(20, 4))
        val ability = Hatsu("MidnightStrike", NenCategory.TRANSMUTATION, 100.0, vows)
        assertEquals(60.0, ability.effectiveCostFor(NenCategory.TRANSMUTATION), 0.0001)
    }

    @Test
    fun `multiple vows stack their discounts`() {
        val vows = listOf(TimeWindowVow(20, 4), TargetRestrictedVow(setOf("gon")))  // 40 + 75 = 115
        val ability = Hatsu("BoundOath", NenCategory.TRANSMUTATION, 200.0, vows)
        assertEquals(85.0, ability.effectiveCostFor(NenCategory.TRANSMUTATION), 0.0001)
    }

    @Test
    fun `heavy vows cannot push the cost below zero`() {
        val vows = listOf<Vow>(LifeStakeVow)  // 200 discount on a 50 ability
        val ability = Hatsu("DeadMansPledge", NenCategory.TRANSMUTATION, 50.0, vows)
        assertEquals(0.0, ability.effectiveCostFor(NenCategory.TRANSMUTATION), 0.0001)
    }

    @Test
    fun `no vow can make a specialization ability castable by others`() {
        val ability = Hatsu("ForbiddenArt", NenCategory.SPECIALIZATION, 50.0, listOf<Vow>(LifeStakeVow))
        assertTrue(ability.effectiveCostFor(NenCategory.ENHANCEMENT).isInfinite())
    }
}
