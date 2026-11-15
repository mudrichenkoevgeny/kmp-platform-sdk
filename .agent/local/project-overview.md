---
description: Project identity, module boundaries, convention plugins, and SDK infrastructure standards
alwaysApply: true
---

# KMP Platform SDK — Overview

## Project Identity
- **Type:** Kotlin Multiplatform **client SDK (library)** and Gradle convention plugin ecosystem. Provides shared foundation and UI for Android and Web (Wasm).
- **Publishing:** Maven Central via Vanniktech Maven Publishing (`com.vanniktech.maven.publish`). Central `group=io.github.mudrichenkoevgeny` and `version=0.0.3` are managed in root `gradle.properties`.
- **Coordinates:** `io.github.mudrichenkoevgeny:kmp-platform-sdk-*` and `io.github.mudrichenkoevgeny:kmp-platform-sdk-gradle-plugin`.
- **BOM:** Located at `:bom` for consistent version alignment across all consumer modules.

## Tech Stack & Infrastructure
- **Kotlin Multiplatform:** Kotlin 2.4.21, Coroutines, Kotlinx Serialization, Kotlinx DateTime.
- **UI & Design Tokens:** Compose Multiplatform 1.12.2 (`composeResources` + generated `Res`). Design tokens synchronized from `platform-design-system` into `GeneratedDesignTokens.kt`.
- **Navigation:** Decompose 3.5.0 (Component-based architecture) + Universal Deep Link routing (`shared-foundation`).
- **Networking:** Ktor Client 3.6.0 (HTTP + WebSockets).
- **Security & Storage:** Android DataStore 1.3.0, Google Tink 1.24.0, platform `EncryptedSettings`.
- **Quality & Static Analysis:** Detekt 2.0.0-alpha.6 with custom rules in `:tools:detekt-rules`, Ktlint 14.2.0, Binary Compatibility Validator.
- **Build Logic & Convention Plugins:** `tools/gradle-plugin` (included build providing `kmp-platform-quality`, `kmp-platform-kotlin`, `kmp-platform-publish`, `kmp-platform-agent-sync`).
- **Platforms:** Android (`compileSdk 37`, `minSdk 24`) and WasmJs (`wasmJs { browser() }`).

## Module Structure

### Core Modules (`core/*`)
*Infrastructure primitives and shared abstractions.*

- **`core/common`:** The foundation. Ktor client bootstrap, WebSocket lifecycle, `AppError` model, error parsing pipeline (Chain of Responsibility), `EncryptedSettings` base, and `CoreTheme` wiring.
- **`core/settings`:** Global application settings logic, Ktor network client for global settings, encrypted caching, and reactive state management.
- **`core/security`:** Password policy validation, MFA state management primitives, Ktor network client for security settings, and domain-specific security error parsing.
- **`core/storage`:** Multiplatform storage primitives and key-value encryption abstractions.
- **`core/testing`:** Shared testing infrastructure, Robolectric setup, `AppErrorParserMock`, and screenshot capture utilities (`captureAppScreen`).

### Feature Modules (`feature/`)
*Pluggable domain logic and shared UI components.*

- **`feature/user`:** Base Identity & Auth logic. Core models, use cases, session lifecycle, and encrypted token storage.
- **`feature/clientuser`:** Identity solution for standard users. Multi-method auth (Email, Phone, Google OAuth), Decompose components, and Compose Multiplatform UI.
- **`feature/managementuser`:** Administrative identity solution. Management auth, session control, resource oversight, audit log inspection, and Decompose admin flows.

### Tools & Plugins (`tools/*`)
- **`tools/gradle-plugin`:** Included build (`kmp-platform-sdk-gradle-plugin`) defining SDK convention plugins (`kmp-platform-quality`, `kmp-platform-kotlin`, `kmp-platform-publish`, `kmp-platform-agent-sync`) and packaging AI agent rules from `.agent/global/kmp`.
- **`tools/detekt-rules`:** Custom Detekt rules module (`kmp-platform-sdk-detekt-rules`) enforcing KMP platform code standards.

### Distribution & Samples
- **`bom`:** Bill of Materials (Gradle platform) for consumer version alignment.
- **`sampleclient`** & **`samplemanagement`:** Reference host applications (`composeApp`, `androidApp`) demonstrating initialization of `CommonComponent`, error parser registration, deep link routing, and UI integration.

## Boundaries & Dependencies
- **`core/common` is the leaf:** It must not depend on any other core or `feature/*` modules.
- **Feature-to-Core:** Feature modules depend on `core/common` and relevant core primitives (e.g., `feature/user` depends on `core/security`).
- **Tooling Isolation:** `tools/gradle-plugin` and `tools/detekt-rules` encapsulate build logic and static analysis rules.
- **Composition Root:** All graph wiring happens in host applications (`sampleclient` / `samplemanagement`). Features must remain host-agnostic and rely on provided configuration (e.g., `baseUrl`).
- **Sample Rule:** Host modules are for integration reference only. **Never move SDK or business logic into sample modules.**

## Agent Rules Distribution Architecture
- **Global Rules (`.agent/global/`):** Universal standards (`common/`, `kotlin/`, `kmp/`) packaged into `tools/gradle-plugin` (`agent-rules/kmp`) and distributed to consumer projects via `kmp-platform-agent-sync`.
- **Local Rules (`.agent/local/`):** SDK-internal repository rules (`project-overview.md`, `architecture-patterns.md`, `localization.md`, `ui.md`, `testing.md`).
- **Index:** Maintained in root `AGENTS.MD`.

---
*Refer to `AGENTS.MD` for the full index of project standards.*
