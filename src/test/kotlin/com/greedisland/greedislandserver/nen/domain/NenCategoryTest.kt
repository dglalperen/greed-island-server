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
}