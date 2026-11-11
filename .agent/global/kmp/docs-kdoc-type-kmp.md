---
description: Mandatory KDoc requirements for KMP components (Implementation classes, Decompose, AppError, Slots)
globs: "**/*.kt"
alwaysApply: true
---

# Documentation Requirements by Type — KMP Platform Additions

This document defines specific KDoc patterns for KMP component types. It builds upon `docs-kdoc-type-basics.md`.

## 1. Implementation Classes (Service & Manager Implementations)
- **Concrete Behavior:** Mandatory class-level KDoc explaining **how** the class achieves its contract (e.g., "Uses Android DataStore for encrypted persistence", "Wraps Ktor auth provider").
- **No Redundancy on Overrides:** Do **not** duplicate interface documentation on `override` members unless the implementation adds specific side effects or platform-specific constraints (e.g., Wasm-specific threading notes).

## 2. Error Models (`AppError` Sealed Hierarchies)
For every `AppError` sealed hierarchy (e.g., `SecurityError`, `UserError`):
- **Structure:** Class-level KDoc explaining the domain failure meaning, error `code`, and how constructor `args` are mapped for localization.
- **Variants:** Each variant must describe:
  - The specific failure scenario it represents.
  - `@param` for constructor parameters.
  - **Args Policy:** Document which arguments are safe for UI display vs. strictly for internal logging.

## 3. Decompose Component & Wiring Classes (`*Component`)
Every Decompose component class must have class-level KDoc describing:
- **Dependencies:** What it requires via constructor (e.g., `componentContext`, `baseUrl`, `parentScope`).
- **Provisions & State:** What `Value<ScreenState>` or child slots it exposes to the Compose UI layer.
- **Initialization & Handlers:** Explicitly state if `init()` or handler registration methods must be called (e.g., "Registers `WebSocketMessageHandler` on creation").
- **Aggregates:** Root components (like `ClientUserComponent`) must list all child components and sub-navigation stacks they coordinate.

## 4. Decompose Slot Navigation & Dialogs (`childSlot`)
For state models, dialog configs, and child slot wrappers:
- **Config Classes (`*DialogConfig`):** Document each `@Serializable` configuration variant and parameters passed into modal overlays.
- **Child Classes (`*DialogChild`):** Document each `Child` variant, specifying whether it is a stateless data object or holds a nested child component.

## 5. Localization & Error Parsers (`AppErrorParser`)
Every feature implementation of `AppErrorParser` must document:
- **Handled Codes:** Which error code domain it manages (e.g., `ERROR_USER_*`).
- **Fallback Policy:** Explicitly state its fallback strategy for unhandled error codes.
- **Resource Linking:** Link to the specific `Res.string` keys it utilizes.

## 6. Multiplatform Nuances
- **Target Specifics:** For any logic that behaves differently or requires workarounds across targets (Android vs. WasmJs/iOS), the KDoc **must** explicitly highlight these target-specific nuances.
