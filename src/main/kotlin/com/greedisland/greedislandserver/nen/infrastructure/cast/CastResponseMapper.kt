package com.greedisland.greedislandserver.nen.infrastructure.cast

import com.fasterxml.jackson.annotation.JsonInclude
import com.greedisland.greedislandserver.nen.domain.cast.CastResult

fun CastResult.toResponse(): CastResponse = when (this){
    is CastResult.Success -> CastResponse(outcome = CastOutcome.SUCCESS, auraSpent = this.auraSpent)
    is CastResult.NotEnoughAura -> CastResponse(outcome = CastOutcome.NOT_ENOUGH_AURA, auraSpent = null, required = this.required, available = this.available)
    is CastResult.VowConditionsNotMet -> CastResponse(outcome = CastOutcome.VOW_CONDITIONS_NOT_MET, auraSpent = null, required = null, available = null, unsatisfiedVows = unsatisfied.map{vow -> vow.toString()})
}
