package com.greedisland.greedislandserver.nen.infrastructure

import com.greedisland.greedislandserver.nen.application.ResolveCastCommand
import com.greedisland.greedislandserver.nen.application.ResolveCastUseCase
import com.greedisland.greedislandserver.nen.domain.CastResult
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class CastController (
    private val resolveCastUseCase: ResolveCastUseCase,
) {
    @PostMapping("/casts/attempt")
    fun attempt(@RequestBody command: ResolveCastCommand):
            CastResult = resolveCastUseCase.resolve(command)
}