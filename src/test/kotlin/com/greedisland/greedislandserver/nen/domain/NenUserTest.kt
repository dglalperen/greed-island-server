package com.greedisland.greedislandserver.nen.domain

import com.greedisland.greedislandserver.nen.domain.cast.CastContext
import com.greedisland.greedislandserver.nen.domain.cast.CastResult
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

class NenUserTest
{
    @Test
    fun `user can afford hatsu`(){
        val ability = Hatsu("someAbility", NenCategory.TRANSMUTATION, 100.0)
        val nenUser = NenUser("someguid", NenCategory.TRANSMUTATION, 500.0)

        assertEquals(true, nenUser.canCast(ability))
    }

    @Test
    fun `user can exactly afford it`(){
        val ability = Hatsu("someAbility", NenCategory.TRANSMUTATION, 100.0)
        val nenUser = NenUser("someguid", NenCategory.TRANSMUTATION, 100.0)

        assertEquals(true, nenUser.canCast(ability))
    }

    @Test
    fun `user cannot afford hatsu`(){
        val ability = Hatsu("someAbility", NenCategory.SPECIALIZATION, 100.0)
        val nenUser = NenUser("someguid", NenCategory.TRANSMUTATION, 500.0)

        assertEquals(false, nenUser.canCast(ability))
    }

    @Test
    fun `the affinity penalty can push an off-category ability out of reach`() {
        // Transmutation ability for an Enhancer: 100 / 0.8 = 125, more than 100 capacity
        val ability = Hatsu("someAbility", NenCategory.TRANSMUTATION, 100.0)
        val user = NenUser("someguid", NenCategory.ENHANCEMENT, 100.0)

        assertFalse(user.canCast(ability))
    }

    @Test
    fun `a vow can bring an unaffordable ability within reach`() {
        val user = NenUser("someguid", NenCategory.TRANSMUTATION, 80.0)

        val plain = Hatsu("Plain", NenCategory.TRANSMUTATION, 100.0)
        assertFalse(user.canCast(plain))   // 100 > 80

        val vowed = Hatsu("Vowed", NenCategory.TRANSMUTATION, 100.0, listOf<Vow>(TimeWindowVow(20, 4)))
        assertTrue(user.canCast(vowed))    // 100 - 40 = 60 <= 80
    }

    // --- attemptCast: the authoritative decision ---

    @Test
    fun `attemptCast succeeds when vows hold and aura suffices`() {
        val ability = Hatsu("MidnightStrike", NenCategory.TRANSMUTATION, 100.0, listOf<Vow>(TimeWindowVow(20, 4)))
        val user = NenUser("u", NenCategory.TRANSMUTATION, 80.0)

        val result = user.attemptCast(ability, CastContext(currentHour = 22))  // night -> vow satisfied

        assertIs<CastResult.Success>(result)
        assertEquals(60.0, result.auraSpent, 0.0001)  // 100 - 40 discount
    }

    @Test
    fun `attemptCast fails when a vow condition is not met`() {
        val ability = Hatsu("MidnightStrike", NenCategory.TRANSMUTATION, 100.0, listOf<Vow>(TimeWindowVow(20, 4)))
        val user = NenUser("u", NenCategory.TRANSMUTATION, 500.0)

        val result = user.attemptCast(ability, CastContext(currentHour = 12))  // daytime -> vow not met

        assertIs<CastResult.VowConditionsNotMet>(result)
        assertEquals(1, result.unsatisfied.size)
    }

    @Test
    fun `attemptCast fails when aura is insufficient`() {
        val ability = Hatsu("BigOne", NenCategory.TRANSMUTATION, 100.0)
        val user = NenUser("u", NenCategory.TRANSMUTATION, 50.0)

        val result = user.attemptCast(ability, CastContext(currentHour = 0))

        assertIs<CastResult.NotEnoughAura>(result)
        assertEquals(100.0, result.required, 0.0001)
        assertEquals(50.0, result.available, 0.0001)
    }

    @Test
    fun `attemptCast reports an impossible specialization ability as unaffordable`() {
        val ability = Hatsu("Forbidden", NenCategory.SPECIALIZATION, 50.0)
        val user = NenUser("u", NenCategory.TRANSMUTATION, 9999.0)

        val result = user.attemptCast(ability, CastContext(currentHour = 0))

        assertIs<CastResult.NotEnoughAura>(result)
        assertTrue(result.required.isInfinite())
    }
}
