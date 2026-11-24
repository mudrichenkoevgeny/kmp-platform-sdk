---
description: KMP architecture standards, Decompose components, Compose UI, and source set rules
alwaysApply: true
---

# Agent Workflow Standards — KMP Platform Additions

This document defines the KMP-specific extensions and architectural workflow requirements for KMP projects. It builds upon `agent-workflow-basics.md`.

## 1. KMP Layering & Module Boundaries

Ensure code is placed in the correct module based on the generic KMP architectural layering:

- **Core Infrastructure Layer (`core/*`):** Low-level multiplatform primitives (Ktor client engines, `EncryptedSettings` abstractions, `AppError` pipeline, security/crypto algorithms). Core modules MUST remain target-agnostic, generic, and decoupled from high-level host application workflows.
- **Feature Layer (`feature/*`):** Modular domain capabilities, use cases, Decompose components (`*Component`), Compose Multiplatform screens (`*Screen`), and feature-specific navigation contracts.
- **Application / Host Layer:** Root application entry points (`androidApp`, `wasmApp`, `desktopApp`, `iosApp`, or `sample*`). Responsible for DI graph instantiation, root navigation host, and platform context provision.
- **Strict Boundary:** Never bleed host app-specific logic or UI views into shared `core/*` or `feature/*` library modules.

## 2. KMP Source Sets & Dependency Allocation

- **KMP Source Sets:** Ensure dependencies and source files are assigned to the correct multiplatform source sets (`commonMain` for cross-platform shared logic, `androidMain` for Android-specific implementations, `wasmJsMain` for Wasm JS logic, `iosMain` for iOS target logic).

## 3. KMP Architectural Workflow

- **Errors Workflow:** To add a new domain error, extend the relevant `AppError` sealed hierarchy, update its corresponding `AppErrorParser` implementation, and add localized string resource keys to `strings.xml` across all supported locale folders (`values/`, `values-ru/`).
- **Logic & Navigation (Decompose):** Component logic and navigation MUST be implemented using **Decompose** components (`*Component`). State is exposed as Decompose `Value<ScreenState>`. Modal dialogs/sheets use Decompose `childSlot`.
- **UI Rendering (Compose Multiplatform):** UI rendering MUST be implemented using **Compose Multiplatform** (`@Composable` functions). Composables are stateless consumers of component state.
- **Networking Extensions:** Feature-specific HTTP behavior must be encapsulated as an `HttpClientConfigPlugin` and registered during host app startup.
- **Stateless WebSockets:** Implement `WebSocketMessageHandler` in feature modules and register via `webSocketService` during host wiring.
- **Multi-Target Constraints:** Ensure all common code in `commonMain` is strictly compatible with all enabled target platforms (Android, WasmJs, iOS). Do not use JVM-only APIs (e.g., `java.util.*`, reflection) in common source sets.
