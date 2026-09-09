---
description: Project identity, KMP module boundaries, and SDK infrastructure standards
alwaysApply: true
---

# KMP Platform SDK — Overview

## Project identity
- **Type:** Kotlin Multiplatform **client SDK (library)**. Provides shared foundation and UI for Android and Web (Wasm).
- **Publishing:** Maven Central via `com.vanniktech.maven.publish`; versions are managed in the root `build.gradle.kts`.
- **Coordinates:** Group `io.github.mudrichenkoevgeny`, artifact IDs follow `kmp-platform-sdk-*` convention.
- **BOM:** Located at `:bom` for consistent version alignment across all consumer modules.

## Tech stack
- **Kotlin:** 2.x, Coroutines, Kotlinx Serialization.
- **UI:** Compose Multiplatform (including `composeResources` + generated `Res`).
- **Navigation:** Decompose (Component-based architecture).
- **Networking:** Ktor Client 3.x (HTTP + WebSockets).
- **Security & Storage:** Android DataStore (encrypted), Google Tink, and platform-specific `EncryptedSettings` abstractions.
- **Logging:** Kermit.
- **Platforms:** Android and WasmJs are the primary targets.

## Module Structure

### Core Modules (`core/`)
*Infrastructure primitives and shared abstractions.*

- **`core/common`:** The foundation. Ktor client bootstrap, WebSocket lifecycle, `AppError` model, error parsing pipeline (Chain of Responsibility), and platform metadata.
- **`core/settings`:** Global application settings logic, encrypted storage implementations, and reactive state management.
- **`core/security`:** Password policy validation, MFA state management primitives, and domain-specific security error parsing.

### Feature Modules (`feature/`)
*Pluggable domain logic and shared UI components.*

- **`feature/auditapi`:** Administrative audit log viewing and event inspection features for management users.
- **`feature/securityapi`:** Ktor-based networking for security policy management and MFA requirement synchronization.
- **`feature/settingsapi`:** Ktor-based networking implementation for fetching and syncing global application configurations.
- **`feature/user`:** Base Identity & Auth logic. Core models, use cases, and token storage.
- **`feature/clientuser`:** Identity solution for standard users. Multi-method auth (Email, Phone, Google), and **Decompose** components for auth flows.
- **`feature/managementuser`:** Administrative identity solution. Management-specific auth, session control, and resource oversight.

### Other
- **`bom`:** Bill of Materials (Gradle platform).
- **`sampleclient`** & **`samplemanagement`:** Reference host applications demonstrating initialization of `CommonComponent`, error parser registration, and UI integration via `CompositionLocalProvider`.

## Boundaries & Dependencies
- **`core/common` is the leaf:** It must not depend on any `feature/*` modules.
- **Feature-to-Core:** Feature modules depend on `core/common` and relevant core primitives (e.g., `feature/user` depends on `core/security`).
- **Composition Root:** All wiring happens in the host application or `sample/`. Features must remain host-agnostic and rely on provided configuration (e.g., `baseUrl`).
- **Sample Rule:** The `sample/` module is for integration reference only. **Never move SDK or business logic into sample.**

## Guidelines for AI
- All architectural and coding standards are located in the `.agent/` directory.
- Refer to `AGENTS.md` for the full index of standards.
- **Initialization Rule:** When proposing changes, respect the `init()` sequence: `CommonComponent` -> `HttpClientConfigPlugin` -> `AppErrorParser` registration.
- **UI Rule:** Ensure all UI components are compatible with both Android and Wasm targets by avoiding JVM-only or Web-only APIs without platform abstractions.

---
*Refer to `AGENTS.md` for the full list of project standards.*