package com.greedisland.greedislandserver.nen.infrastructure

import com.greedisland.greedislandserver.nen.application.HatsuRepository
import com.greedisland.greedislandserver.nen.domain.Hatsu
import com.greedisland.greedislandserver.nen.domain.LifeStakeVow
import com.greedisland.greedislandserver.nen.domain.NenCategory
import com.greedisland.greedislandserver.nen.domain.TargetRestrictedVow
import com.greedisland.greedislandserver.nen.domain.TimeWindowVow
import org.springframework.stereotype.Repository

@Repository
class InMemoryHatsuRepository: HatsuRepository {

    private val store = mutableMapOf<String, Hatsu>(
        // Plain enhancement punch — no vows, no discount.
        "jajanken-rock" to Hatsu("Jajanken: Rock", NenCategory.ENHANCEMENT, 60.0),

        // All-or-nothing gamble: staked on the user's life for a huge discount.
        "first-comes-rock" to Hatsu(
            "First Comes Rock", NenCategory.ENHANCEMENT, 250.0,
            listOf(LifeStakeVow),
        ),

        // Kurapika's chain: only usable against the Phantom Troupe (target-restricted).
        "chain-jail" to Hatsu(
            "Chain Jail", NenCategory.CONJURATION, 200.0,
            listOf(TargetRestrictedVow(setOf("uvogin", "chrollo", "nobunaga"))),
        ),

        // Night-only transmutation: usable 20:00-04:00, discounted for the tight window.
        "moonlit-edge" to Hatsu(
            "Moonlit Edge", NenCategory.TRANSMUTATION, 180.0,
            listOf(TimeWindowVow(20, 4)),
        ),

        // Doubly bound: night-only AND life-staked — deepest discount in the store.
        "final-gambit" to Hatsu(
            "Final Gambit", NenCategory.SPECIALIZATION, 500.0,
            listOf(TimeWindowVow(0, 3), LifeStakeVow),
        ),
    )

    override fun findById(id: String): Hatsu? = store[id]
}