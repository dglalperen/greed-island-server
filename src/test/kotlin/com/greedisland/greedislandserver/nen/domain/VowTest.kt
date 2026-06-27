package com.greedisland.greedislandserver.nen.domain

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class VowTest {
    @Test
    fun `TimeVow is in window`(){
        val castContext = CastContext(currentHour = 12)
        val timeVow = TimeWindowVow(fromHour = 10, toHour = 13)

        assertEquals(true, timeVow.isSatisfied(castContext))
    }

    @Test
    fun `TimeVow is not in window`(){
        val castContext = CastContext(currentHour = 13)
        val timeVow = TimeWindowVow(fromHour = 10, toHour = 13)

        assertEquals(false, timeVow.isSatisfied(castContext))
    }

    @Test
    fun `time window wraps past midnight`() {
        val vow = TimeWindowVow(fromHour = 20, toHour = 4)  // 8pm - 4am
        assertTrue(vow.isSatisfied(CastContext(currentHour = 22)))   // evening side
        assertTrue(vow.isSatisfied(CastContext(currentHour = 2)))    // morning side
        assertFalse(vow.isSatisfied(CastContext(currentHour = 10)))  // daytime
        assertFalse(vow.isSatisfied(CastContext(currentHour = 4)))   // end hour excluded
    }

    @Test
    fun `target restriction allows a declared target`() {
        val vow = TargetRestrictedVow(setOf("gon", "killua"))
        assertTrue(vow.isSatisfied(CastContext(currentHour = 0, targetId = "gon")))
    }

    @Test
    fun `target restriction rejects an undeclared target`() {
        val vow = TargetRestrictedVow(setOf("gon", "killua"))
        assertFalse(vow.isSatisfied(CastContext(currentHour = 0, targetId = "hisoka")))
    }

    @Test
    fun `target restriction rejects a missing target`() {
        val vow = TargetRestrictedVow(setOf("gon"))
        assertFalse(vow.isSatisfied(CastContext(currentHour = 0)))  // targetId defaults to null
    }

    @Test
    fun `life stake is always satisfied at cast time`() {
        assertTrue(LifeStakeVow.isSatisfied(CastContext(currentHour = 3)))
    }

    @Test
    fun `a smaller time window grants a bigger discount`() {
        val narrow = TimeWindowVow(10, 12)   // 2h usable  -> 22 restricted * 2.5 = 55
        val wide = TimeWindowVow(8, 20)       // 12h usable -> 12 restricted * 2.5 = 30
        assertEquals(55.0, narrow.auraDiscount, 0.0001)
        assertEquals(30.0, wide.auraDiscount, 0.0001)
        assertTrue(narrow.auraDiscount > wide.auraDiscount)
    }

    @Test
    fun `target and life-stake discounts are fixed`() {
        assertEquals(75.0, TargetRestrictedVow(setOf("gon")).auraDiscount, 0.0001)
        assertEquals(200.0, LifeStakeVow.auraDiscount, 0.0001)
    }
}
