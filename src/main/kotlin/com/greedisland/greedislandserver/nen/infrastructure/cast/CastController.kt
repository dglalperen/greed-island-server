package com.greedisland.greedislandserver.nen.infrastructure.cast

import com.greedisland.greedislandserver.nen.application.cast.ResolveCastCommand
import com.greedisland.greedislandserver.nen.application.cast.ResolveCastUseCase
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class CastController (
    private val resolveCastUseCase: ResolveCastUseCase,
) {
    @PostMapping("/casts/attempt")
    fun attempt(@RequestBody command: ResolveCastCommand): CastResponse =
        resolveCastUseCase.resolve(command).toResponse()

    @ExceptionHandler(NoSuchElementException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleNotFound(e: NoSuchElementException): Map<String,String> = mapOf("error" to (e.message ?: "Not found"))
}
