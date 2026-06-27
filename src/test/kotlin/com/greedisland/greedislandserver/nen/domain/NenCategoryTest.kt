package com.greedisland.greedislandserver.nen.domain

import kotlin.test.Test
import kotlin.test.assertEquals

class NenCategoryTest {
    @Test
    fun `enhancers affinity of transmutation is 80%`(){
        assertEquals(0.8, NenCategory.ENHANCEMENT.affinityWith(NenCategory.TRANSMUTATION), 0.0001)
    }

    @Test
    fun `enhancers affinity of specialization is 0%`(){
        assertEquals(0.0, NenCategory.ENHANCEMENT.affinityWith(NenCategory.SPECIALIZATION), 0.0001)
    }

    @Test
    fun `a category has full affinity with itself`() {
        assertEquals(1.0, NenCategory.ENHANCEMENT.affinityWith(NenCategory.ENHANCEMENT), 0.0001)
    }

    @Test
    fun `two steps apart on the hexagon is 60 percent`() {
        // ENHANCEMENT(0) -> MANIPULATION(2): ring distance 2
        assertEquals(0.6, NenCategory.ENHANCEMENT.affinityWith(NenCategory.MANIPULATION), 0.0001)
    }

    @Test
    fun `opposite categories are 40 percent`() {
        // EMISSION(1) <-> CONJURATION(4): ring distance 3
        assertEquals(0.4, NenCategory.EMISSION.affinityWith(NenCategory.CONJURATION), 0.0001)
    }

    @Test
    fun `a specialist keeps full affinity with their own category`() {
        assertEquals(1.0, NenCategory.SPECIALIZATION.affinityWith(NenCategory.SPECIALIZATION), 0.0001)
    }

    @Test
    fun `the specialization block is one-directional`() {
        // No one can borrow Specialization...
        assertEquals(0.0, NenCategory.ENHANCEMENT.affinityWith(NenCategory.SPECIALIZATION), 0.0001)
        // ...but a Specialist reaching outward pays only the normal hexagon penalty.
        assertEquals(0.4, NenCategory.SPECIALIZATION.affinityWith(NenCategory.ENHANCEMENT), 0.0001)
    }

    @Test
    fun `affinity is symmetric between two non-specialists`() {
        val a = NenCategory.ENHANCEMENT.affinityWith(NenCategory.TRANSMUTATION)
        val b = NenCategory.TRANSMUTATION.affinityWith(NenCategory.ENHANCEMENT)
        assertEquals(a, b, 0.0001)
    }
}
