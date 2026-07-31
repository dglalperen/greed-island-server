package com.greedisland.greedislandserver.nen.infrastructure

import com.greedisland.greedislandserver.nen.application.NenUserRepository
import com.greedisland.greedislandserver.nen.domain.NenCategory
import com.greedisland.greedislandserver.nen.domain.NenUser
import org.springframework.stereotype.Repository

@Repository
class InMemoryNenUserRepository: NenUserRepository {
    private val store = mutableMapOf<String, NenUser>(
        "gon" to NenUser("gon", NenCategory.ENHANCEMENT, 500.0),
        "killua" to NenUser("killua", NenCategory.TRANSMUTATION, 450.0),
        "kurapika" to NenUser("kurapika", NenCategory.CONJURATION, 380.0),
        "hisoka" to NenUser("hisoka", NenCategory.TRANSMUTATION, 520.0),
        "chrollo" to NenUser("chrollo", NenCategory.SPECIALIZATION, 600.0),
        "novice" to NenUser("novice", NenCategory.EMISSION, 80.0),
    )

    override fun findById(id: String): NenUser? = store[id]
}