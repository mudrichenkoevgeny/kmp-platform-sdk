---
description: Mandatory KDoc requirements by component type (KMP Components, Errors, Identifiers, UI State)
globs: "**/*.kt"
alwaysApply: true
---

# Documentation Requirements by Type — Mandatory

This document defines specific KDoc patterns and API standards for the various architectural building blocks of the `kmp-platform-sdk`.

## 1. Public API & Interfaces
- **Class-level:** Must define the role, domain responsibility, and "Source of Truth" (e.g., "Main entry point for MFA session lifecycle").
- **Contract DoD:** Every public member (`fun` or `val`) must have a summary + `@param` for all parameters + `@return` (if not `Unit`).
- **Implicit Scope:** Requests to "document this module/package" automatically include all public interface members. Class-level-only KDoc is insufficient.

## 2. Component & Wiring Classes (`*Component`, `*Module`)
Every DI/Component class must have class-level KDoc describing:
- **Dependencies:** What it requires via constructor (e.g., `platformContext`, `parentScope`).
- **Provisions:** What it exposes to the parent or UI layer.
- **Initialization:** Explicitly state if `init()` or other lifecycle methods must be called before accessing properties (e.g., "Must call `commonComponent.init()` to register error parsers").
- **Aggregates:** Root components (like `ClientUserComponent`) must list the sub-components they coordinate.

## 3. Implementation Classes
- **Concrete Behavior:** Mandatory class-level KDoc explaining **how** it achieves the contract (e.g., "Uses Android DataStore for encrypted persistence", "Wraps Ktor's auth provider").
- **No Redundancy:** Do **not** duplicate documentation on `override` members unless the implementation adds specific side effects or platform-specific constraints (e.g., Wasm-specific threading notes).

## 4. Error Models (`AppError` Sealed Hierarchies)
For every hierarchy (e.g., `SecurityError`, `UserError`):
- **Structure:** Class-level KDoc explaining the meaning of the `code` and how `args` are used for localization.
- **Variants:** Each variant must describe:
  - The specific failure scenario it represents.
  - `@param` for constructor parameters.
  - **Args Policy:** Which arguments are safe for UI display vs. which are for internal logging.

## 5. Value Classes & Identifiers
For every `@JvmInline value class` wrapping a UUID or ID:
- **Standard API Requirements:**
  1. `asString()` (or `asHexDashString()`): Returns the canonical string representation.
  2. `generate()`: Companion function to create a new instance.
- **Parsing Extensions:** In the same file, provide:
  - `String.toXxxOrNull()` for safe, graceful parsing.
  - `String.toXxxOrThrow()` for cases where invalid input violates the contract.

## 6. Localization & Error Parsers (`AppErrorParser`)
Every implementation of `AppErrorParser` must document:
- **Handled Codes:** Which error code domain it manages (e.g., `ERROR_USER_*`).
- **Fallback Policy:** Explicitly state that it delegates unknown codes to `CommonErrorParser`.
- **Resource Linking:** Link to the specific `Res.strings` keys it utilizes.

## 7. UI & Navigation Models (Decompose / Compose)
For state models (e.g., `*ScreenState`):
- **Lifecycle:** Explain the transitions between states (e.g., `Initial` -> `Loading` -> `Content`).
- **UI Intent:** Each variant must clarify what the UI should render or what user interaction is expected (e.g., "Show biometric prompt").
- **Decompose Configs:** Document the navigation stack configurations and which components they instantiate.

## 8. Guidelines for AI
- **Multiplatform Note:** For any logic that behaves differently on Android vs. Wasm, the KDoc **must** highlight these platform-specific nuances.
- **Self-Contained Docs:** Do not use "See [OtherComponent]" as the sole documentation. Every contract must be understandable within its own file.

---
*Refer to `AGENTS.md` for the full list of project standards.*