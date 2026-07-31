package com.greedisland.greedislandserver.nen.application

import com.greedisland.greedislandserver.nen.domain.CastContext
import org.springframework.stereotype.Service
import com.greedisland.greedislandserver.nen.domain.CastResult


data class ResolveCastCommand(
    val userId: String,
    val hatsuId: String,
    val currentHour: Int,
    val targetId: String? = null
)

@Service
class ResolveCastUseCase (
    private val userRepository: NenUserRepository,
    private val hatsuRepository: HatsuRepository
){
    fun resolve(command: ResolveCastCommand): CastResult{
        val user = userRepository.findById(command.userId)
            ?: throw NoSuchElementException("No user: ${command.userId}")
        val hatsu = hatsuRepository.findById(command.hatsuId)
            ?: throw NoSuchElementException("No hatsu: ${command.hatsuId}")

        val context = CastContext(command.currentHour,command.targetId)

        return user.attemptCast(hatsu,context)
    }
}