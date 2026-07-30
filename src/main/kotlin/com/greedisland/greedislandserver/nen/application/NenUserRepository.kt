package com.greedisland.greedislandserver.nen.application

import com.greedisland.greedislandserver.nen.domain.NenUser

interface NenUserRepository {
    fun findById(id: String): NenUser?
}