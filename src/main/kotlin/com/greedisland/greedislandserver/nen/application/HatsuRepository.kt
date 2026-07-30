package com.greedisland.greedislandserver.nen.application

import com.greedisland.greedislandserver.nen.domain.Hatsu

interface HatsuRepository {
    fun findById(id: String): Hatsu?
}