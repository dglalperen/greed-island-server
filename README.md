# Greed Island Server

An authoritative backend for a multiplayer Nen system, inspired by the Greed Island
arc of *Hunter × Hunter*. The long-term dream is a full multiplayer game; the current
focus is getting the **Nen engine** right as a solo prototype.

## The idea

In *Hunter × Hunter*, characters develop personal abilities (**Hatsu**) built on their
innate Nen category, and they can trade power for restrictions through self-imposed
**vows**. This project models that as a deterministic rules engine:

- Every user has one **Nen category** (Enhancement, Emission, Manipulation,
  Specialization, Conjuration, Transmutation). Categories have an **affinity** to one
  another — using an ability outside your category costs more aura, and Specialization
  abilities are simply out of reach for non-Specialists.
- An ability's real cost is governed by a **power budget**: a base aura cost, scaled by
  the caster's affinity, reduced by any vows whose conditions currently hold. The server
  — not the player — decides whether a cast is legal and how much aura it burns.

### Why a server decides, not the player

The vision is to let players define abilities in natural language and have an LLM
**compile that description into a structured, validated spec** of vetted primitives —
never into raw, merge-ready code. A deterministic engine then rules on legality and
strength via the power budget. The LLM proposes; the backend disposes. This keeps the
game fair and abuse-resistant no matter how creative a player's phrasing is.

## Tech stack

- **Kotlin** on **JDK 21**
- **Spring Boot 4.1** — Spring MVC with virtual threads (not reactive)
- **Spring Data JDBC** + **PostgreSQL** + **Flyway** migrations
- **Gradle** (Kotlin DSL)
- **Testcontainers** for tests against a real Postgres

## Architecture

Feature-first, clean architecture. Each feature (starting with `nen`) is split into
three layers, with dependencies pointing inward:

```
nen/
├── domain          the rules — pure Kotlin, zero framework dependencies
├── application     use cases — orchestration + ports (repository interfaces)
└── infrastructure  the outside world — REST controllers, persistence
```

- **domain** — `NenCategory`, `Hatsu`, `Vow` (a sealed hierarchy), `NenUser`, and the
  authoritative `attemptCast` decision returning a sealed `CastResult`. No Spring here.
- **application** — use cases such as `ResolveCastUseCase`, depending only on repository
  *interfaces* it defines. It knows nothing about HTTP or SQL.
- **infrastructure** — implements those interfaces and exposes them over HTTP.

## Status

Early and growing.

- ✅ Domain engine — categories, affinity, vows, cast resolution (fully unit-tested)
- ✅ Application layer — repository ports + cast-resolution use case
- 🚧 Infrastructure — REST endpoint and persistence (in progress)

## Running

Requires Docker (Testcontainers provides Postgres for tests and local runs).

```bash
./gradlew test    # run the test suite
./gradlew build   # compile and test
```
