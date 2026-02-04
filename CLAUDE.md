# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working in this repository.

## Repository Purpose

**Xesar-Connect** is a Kotlin/JVM library implementing MQTT communication with the **EVVA Xesar** access control system.

It provides bidirectional communication for:

- **Commands** (grant/revoke access, update entities)
- **Events** (door status changes, access attempts, system updates)
- **Queries** (fetching data from the Xesar system)

**Compatibility**
- Tested with **EVVA Xesar 3.1**
- Tested with **MQTT API 1.2.1**

---

## Golden Rules (Read First)

When making changes:

1. **Preserve public API stability**
    - Do not rename or restructure exported APIs unless explicitly requested.
2. **Follow existing patterns**
    - This repo has a consistent structure for commands/events/queries and extensions.
3. **Prefer small, incremental diffs**
    - Avoid sweeping refactors unless the task demands it.
4. **Do not introduce new libraries**
    - Unless explicitly requested.
5. **Keep concurrency safe**
    - This project is coroutine-based and stateful in key areas.

---

## Build & Test Commands

```bash
./gradlew build
./gradlew test
./gradlew spotlessApply
./gradlew :xesar-connect:test --tests "com.open200.xesar.connect.ClassName"
```

### Before submitting changes

Run at minimum:

```bash
./gradlew spotlessApply test
```

---

## Project Architecture

### Source Structure

```
xesar-connect/src/main/kotlin/com/open200/xesar/connect/
├── XesarConnect.kt          # Main public API (stateful connection/session)
├── Config.kt                # Configuration (MQTT, certs, API properties)
├── XesarMqttClient.kt       # Low-level MQTT protocol layer
├── Topics.kt                # MQTT topic definitions + topic builder logic
├── extension/               # Domain operations as extension functions
│   ├── XesarConnectPersonExt.kt
│   ├── XesarConnectIdentificationMediumExt.kt
│   └── ...
├── messages/
│   ├── command/             # Command payloads (*Mapi.kt suffix)
│   ├── event/               # Event payloads (*Created, *Changed, *Deleted)
│   └── query/               # Query response types
├── filters/                 # Message filtering (CommandIdFilter, TopicFilter, etc.)
└── exception/               # Custom exceptions
```

---

## Core Design Patterns

### 1) Domain operations via extension functions

All CRUD/domain operations live as extension functions on `XesarConnect`.

- One file per domain model (`Person`, `TimeProfile`, etc.)
- Naming and structure should match existing extensions

✅ Do:
- Add a new `XesarConnectXyzExt.kt` when introducing a new domain

❌ Don’t:
- Add domain operations directly into `XesarConnect.kt`

---

### 2) Message categories

This repo has three main message families:

#### Commands
- Async operations
- Use `*Mapi` suffix
- Tracked by **command ID**
- Usually return a command response type

#### Events
- Notifications from the system
- Typically:
    - `*Created`
    - `*Changed`
    - `*Deleted`

#### Queries
- Request/response for data retrieval
- Usually model a strongly typed response structure

---

### 3) Coroutines + async patterns

- All async operations use Kotlin coroutines
- Methods ending in `Async` return `Deferred<T>`
- Streaming subscriptions use `Flow<T>`

Best practices in this repo:
- Prefer structured concurrency
- Avoid `GlobalScope`
- Avoid blocking calls in suspend functions

---

### 4) MQTT topics

Topic format:

`xs3/1/[userId|ces]/[event|command]`

If changing topic behavior:
- Ensure compatibility with existing topic builders
- Add tests for parsing + topic formatting

---

## Testing

### Tooling

- **Kotest** (FunSpec style)
- **mockk** for mocking
- Integration tests: **testcontainers** with Mosquitto MQTT broker

### Conventions

- Fixtures live in:
    - `src/test/kotlin/.../util/fixture/`
- Tests marked with `@EnableTestLocally` are **not expected to run in CI**
    - Do not rely on them for core correctness

### When adding new features

Add at least one:
- Unit test for payload parsing/serialization
- Test for command tracking and response correlation (if applicable)

---

## Code Style & Formatting

- Formatter: **ktfmt** via Spotless
- Indent: **4 spaces**
- Max line length: **100 characters**

If formatting changes appear unrelated to your task:
- Avoid touching those lines (keep diffs focused)

---

## Commit & PR Expectations

Commit message format:
- **Angular Conventional Commits**
- Include issue references when relevant:
    - `Closes: #<issue>`

---

## Key Dependencies

- MQTT: Eclipse Paho (`org.eclipse.paho.client.mqttv3`)
- Serialization: `kotlinx-serialization` (Ktor JSON)
- Coroutines: `kotlinx-coroutines-core`
- Logging: `kotlin-logging`
    - Consumers must add logback for actual output
- Security: **BouncyCastle provider** required for certificate processing

---

## Configuration Notes

Certificates can be loaded via:

```kotlin
Config.configureFromZip(pathToZip)
Config.configureFromPaths(caCertPath, clientCertPath, keyPath)
```

When modifying configuration:
- Keep API backwards compatible
- Prefer adding overloads instead of changing existing function signatures

---

## Error Handling Expectations

- Prefer domain-specific exceptions from `exception/`
- Do not swallow MQTT errors silently
- When mapping errors, preserve the original cause

---

## When Unsure

- Copy patterns from existing code
- Prefer **explicitness over cleverness**
- Avoid introducing new architectural styles
- If behavior is unclear, add a test that documents expectations
