package com.greedisland.greedislandserver.nen.domain

/**
 * The authoritative outcome of attempting to cast a Hatsu.
 *
 * A sealed result means every call site must handle each outcome (the compiler
 * enforces it in a `when`), and failures carry the *reason* — and the numbers
 * behind it — instead of a bare `false`.
 */
sealed interface CastResult {
    /** The cast is allowed. [auraSpent] is the (discounted) cost the caller should deduct. */
    data class Success(val auraSpent: Double) : CastResult

    /** One or more vow conditions are not currently met (e.g. it isn't night yet). */
    data class VowConditionsNotMet(val unsatisfied: List<Vow>) : CastResult

    /** The user cannot afford the effective cost right now. */
    data class NotEnoughAura(val required: Double, val available: Double) : CastResult
}
