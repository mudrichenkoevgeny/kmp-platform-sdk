---
description: Project identity, module boundaries, convention plugins, and SDK infrastructure standards
alwaysApply: true
---

# KMP Platform SDK — Overview

## Project Identity
- **Type:** Kotlin Multiplatform **client SDK (library)** and Gradle convention plugin ecosystem. Provides shared foundation and UI for Android and Web (Wasm).
- **Publishing:** Maven Central via Vanniktech Maven Publishing (`com.vanniktech.maven.publish`). Central `group` and `version` are managed in root `gradle.properties`.
- **Coordinates:** `io.github.mudrichenkoevgeny:kmp-platform-sdk-*` and `io.github.mudrichenkoevgeny:kmp-platform-sdk-gradle-plugin`.
- **BOM:** Located at `:bom` for consistent version alignment across all consumer modules.

## Tech Stack & Infrastructure
- **Kotlin Multiplatform:** Kotlin, Coroutines, Kotlinx Serialization, Kotlinx DateTime.
- **UI & Design Tokens:** Compose Multiplatform (`composeResources` + generated `Res`). Design tokens synchronized from `platform-design-system` into `GeneratedDesignTokens.kt`.
- **Navigation:** Decompose (Component-based architecture) + Universal Deep Link routing (`shared-foundation`).
- **Networking:** Ktor Client (HTTP + WebSockets).
- **Security & Storage:** Android DataStore, Google Tink, platform `EncryptedSettings`.
- **Quality & Static Analysis:** Detekt with custom rules in `:tools:detekt-rules`, Ktlint, Binary Compatibility Validator.
- **Build Logic & Convention Plugins:** `tools/gradle-plugin` (included build providing `kmp-platform-quality`, `kmp-platform-kotlin`, `kmp-platform-publish`, `kmp-platform-agent-sync`). All library and plugin versions are centrally managed in `gradle/libs.versions.toml`.
- **Platforms:** Android (`compileSdk`, `minSdk` configured in `libs.versions.toml`) and WasmJs (`wasmJs { browser() }`).

## Module Structure

### Core Modules (`core/*`)
*Infrastructure primitives and shared abstractions.*

- **`core/common`:** The foundation. Ktor client bootstrap, WebSocket lifecycle, `AppError` model, error parsing pipeline (Chain of Responsibility), `EncryptedSettings` base, and `CoreTheme` wiring.
- **`core/settings`:** Global application settings logic, Ktor network client for global settings, encrypted caching, and reactive state management.
- **`core/security`:** Password policy validation, MFA state management primitives, Ktor network client for security settings, and domain-specific security error parsing.
- **`core/storage`:** File storage infrastructure, upload/download REST clients, storage settings, and reactive WebSocket updates.

### Feature Modules (`feature/*`)
*Pluggable domain logic and shared UI components.*

- **`feature/user`:** Base Identity & Auth logic. Core models, use cases, session lifecycle, and encrypted token storage.
- **`feature/clientuser`:** Identity solution for standard users. Multi-method auth (Email, Phone, Google OAuth), Decompose components, and Compose Multiplatform UI.
- **`feature/managementuser`:** Administrative identity solution. Management auth, session control, resource oversight, audit log inspection, and Decompose admin flows.

### Tools Modules (`tools/*`)
*Build-time plugins, generators, and static analysis extensions.*

- **`tools/gradle-plugin`:** Included build (`kmp-platform-sdk-gradle-plugin`) defining SDK convention plugins (`kmp-platform-quality`, `kmp-platform-kotlin`, `kmp-platform-publish`, `kmp-platform-agent-sync`) and packaging AI agent rules from `.agent/global/kmp`.
- **`tools/detekt-rules`:** Custom Detekt rules module (`kmp-platform-sdk-detekt-rules`) enforcing KMP platform code standards ([module README](../../tools/detekt-rules/README.md)).
- **`tools/project-generator`:** Internal JVM generator triggered via `./gradlew initProject` / `./gradlew scaffoldProject` for scaffolding clean, minimal, production-ready downstream KMP application repositories (`<name>-kmp`) on disk ([module README](../../tools/project-generator/README.md)).

### Infrastructure & Reference
- **`testing`:** Shared testing infrastructure, Robolectric setup, `AppErrorParserMock`, screenshot capture utilities (`captureAppScreen`), and repository-wide Konsist architecture suite ([module README](../../testing/README.md)).
- **`bom`:** Bill of Materials (`kmp-platform-sdk-bom`) for consumer version alignment.
- **`sampleclient`** & **`samplemanagement`:** Reference host applications (`composeApp`, `androidApp`) demonstrating initialization of `CommonComponent`, error parser registration, deep link routing, and UI integration. Never move SDK or business logic into sample modules.

## Boundaries & Dependencies
- **`core/common` is the leaf:** It must not depend on any other core or `feature/*` modules.
- **Core-to-Core:** Modules depend on `core/common` and needed core abstractions.
- **Feature-to-Core:** Feature modules aggregate core modules (e.g., `feature/user` depends on `core/security`, `core/settings`, `core/storage`, and `core/common`).
- **Tools Isolation:** `tools/*` modules are build-time infrastructure and do not depend on runtime SDK modules.
- **Composition Root:** All graph wiring happens in host applications (`sampleclient` / `samplemanagement`). Features must remain host-agnostic and rely on provided configuration (e.g., `baseUrl`).
- **Sample Rule:** Host modules are for integration reference only. **Never move SDK or business logic into sample modules.**

## Agent Rules Distribution Architecture
- **Global Rules (`.agent/global/`):** Universal standards (`common/`, `kotlin/`, `kmp/`) packaged into `tools/gradle-plugin` (`agent-rules/kmp`) and distributed to consumer projects via `kmp-platform-agent-sync`.
- **Local Rules (`.agent/local/`):** SDK-internal repository rules (`project-overview.md`, `architecture-patterns.md`, `localization.md`, `ui.md`, `testing.md`).
- **Index:** Maintained in root `AGENTS.MD`.

---
*Refer to `AGENTS.MD` for the full index of project standards.*
