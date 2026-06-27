package com.greedisland.greedislandserver.nen.domain

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

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
}