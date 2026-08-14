package com.greedisland.greedislandserver.nen.infrastructure.cast

import com.fasterxml.jackson.annotation.JsonInclude

enum class CastOutcome {
    SUCCESS,
    NOT_ENOUGH_AURA,
    VOW_CONDITIONS_NOT_MET
}

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CastResponse(
    val outcome: CastOutcome,
    val auraSpent: Double? = null,
    val required: Double? = null,
    val available: Double? = null,
    val unsatisfiedVows: List<String>? = null
)
