---
description: Architecture, component wiring, bootstrap, and cross-platform design patterns
globs: "**/*.kt"
alwaysApply: true
---

# Architecture and Design Patterns

This document defines the structural standards for the `kmp-platform-sdk`. All modules must adhere to these patterns to ensure consistency across Android and Web (Wasm) targets.

## 1. Modular Hierarchy & Component Model (Decompose)

The SDK follows a strict layering based on the **Decompose** component model to ensure platform-agnostic navigation and state management:

- **UI Layer (Compose Multiplatform):** Stateless Composable functions that observe `Value` or `StateFlow` from components.
- **Component Layer (Decompose):** Lifecycle-aware units (e.g., `LoginComponent`) that own the business logic and navigation state. They act as the "View Model" in KMP.
- **Use Case Layer:** Atomic business operations (e.g., `RefreshSessionUseCase`, `ValidatePasswordUseCase`).
- **Repository Layer:** Orchestrates data flow between Ktor Network clients and `EncryptedSettings` storage.

## 2. SDK Bootstrap & Host Wiring

- **Initialization Sequence:** The host app (or `sample`) is responsible for the manual wiring of the graph:
  1. Instantiate `CommonComponent` with platform-specific `baseUrl` and `platformContext`.
  2. Initialize feature components (e.g., `ClientUserComponent`) by passing the `CommonComponent` and parent `CoroutineScope`.
  3. Call `commonComponent.init()` providing a list of feature-specific `AppErrorParser` implementations.
- **WebSocket Handlers:** Feature modules must provide `WebSocketMessageHandler` implementations, which are registered in the `commonComponent.webSocketService` during the startup sequence.

## 3. Networking & Multi-target Compatibility

- **Ktor Client:** Default configuration is centralized in `core/common`. Modular extensions must be implemented as `HttpClientConfigPlugin`.
- **Concurrency:** Always use the `appScope` provided during component initialization for long-running tasks to ensure operations are cancelled when the application or component is destroyed.
- **Wasm Constraints:** Avoid using APIs that rely on reflection or JVM-specific libraries (like `java.util.*`). Use `kotlinx` equivalents (e.g., `kotlinx-datetime`).

## 4. Error Modeling & Chain of Responsibility

The SDK uses a localized, machine-readable error handling system:

- **AppError:** All failures must be mapped to an `AppError` containing a unique `code` and an `isRetryable` flag.
- **Parsing Pipeline:** `AppErrorParser` uses a Chain of Responsibility.
  - **Feature Parsers:** Map domain-specific codes (e.g., `ERROR_USER_EXPIRED_SESSION`) to localized strings from `Res.strings`.
  - **Common Parser:** Acts as the fallback for networking and generic system errors.
- **UI Integration:** Use `LocalErrorParser.current` via `CompositionLocal` to resolve error strings within Composables.

## 5. Persistence & Security

- **Encrypted Storage:** Use the `EncryptedSettings` abstraction from `core/common`. Do not use platform-specific storage (SharedPreferences/IndexedDB) directly in feature modules.
- **Sensitive Data:** Operations involving passwords or MFA secrets must remain inside the `core/security` or `feature/user` domain logic; never expose raw secrets to the UI layer.

## 6. Naming & Packaging Conventions

- **Package by Feature & Layer:** `io.github.mudrichenkoevgeny.sdk.[module].[feature].[layer]`.
- **Standard Layers:**
  - `ui`: Compose Multiplatform screens and widgets.
  - `component`: Decompose components and screen configurations.
  - `usecase`: Atomic business logic.
  - `repository`: Data orchestration.
  - `network`: Ktor DTOs and API definitions.
  - `di`: Component factories and manual DI wiring.
- **Resource Naming:** Use module prefixes for string IDs to avoid collisions: `error_common_*`, `error_user_*`, `error_security_*`.

## 7. Guidelines for AI

- **Dependency Rule:** `core/common` is the root infrastructure. It must never depend on `feature/*` modules.
- **State Management Rule:** Prefer `StateFlow` for component state and `Channel` for one-time events (Side Effects).
- **KDoc Requirement:** Document all public component interfaces with their expected lifecycle and key collaborators.

---
*Refer to `AGENTS.md` for coding style rules (No FQN, No Comments).*