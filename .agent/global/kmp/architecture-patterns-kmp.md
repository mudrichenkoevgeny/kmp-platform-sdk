---
description: Universal KMP architecture, Decompose component model, navigation, back handling, slot dialogs, and ListOwner
globs: "**/*.kt"
alwaysApply: true
---

# Architecture and Design Patterns — KMP Platform Additions

This document defines universal architectural standards, Decompose lifecycle management, and UI integration patterns across KMP projects.

## 1. Modular Hierarchy & Component Model (Decompose)
The architecture follows a strict layering based on the **Decompose** component model:
- **UI Layer (Compose Multiplatform):** Stateless `@Composable` functions that observe Decompose `Value<ScreenState>` from components.
- **Component Layer (Decompose):** Lifecycle-aware units (`*Component`) that encapsulate business logic, UI state, and navigation. Components act as the platform-agnostic "ViewModel".
- **Use Case Layer:** Atomic business operations.
- **Repository Layer:** Data orchestration between network APIs and local persistence.

## 2. Concurrency & Multi-Target Constraints
- **Component Scopes:** Decompose components manage async operations via `componentCoroutineScope()`. Scopes must be cancelled automatically when the component's Decompose lifecycle is destroyed.
- **Wasm Constraints:** Strictly avoid APIs relying on reflection or JVM-specific libraries (such as `java.util.*` or `java.time.*`). Use `kotlinx` equivalents (`kotlinx-datetime`, `kotlinx-serialization`, `kotlin.uuid`).

## 3. Standard Packaging Layers
Organize code cleanly across architectural boundaries:
- `ui`: Compose Multiplatform screens, widgets, and previews.
- `component`: Decompose components, state models, and navigation configs.
- `usecase`: Atomic domain business logic.
- `repository`: Data orchestration and caching.
- `network`: API clients, network envelopes, and DTOs.
- `di`: Component factories and dependency injection wiring.

## 4. Stack Navigation & Back Handling (`handleBack`)
Root navigation components must implement safe deep link back handling:
- **Direct Back Navigation:** Check if `previousConfig` in the stack equals the expected `parentDestination`. If so, execute `navigation.pop()`.
- **Deep Link Back Navigation:** If navigating back from a direct deep link where the stack does not contain the parent, inspect and navigate safely:
  ```kotlin
  navigation.navigate { currentStack ->
      val index = currentStack.indexOfLast { it == parentDestination || it::class == parentDestination::class }
      if (index >= 0) currentStack.take(index + 1) else currentStack.dropLast(1) + parentDestination
  }
  ```
- **Lifecycle Safety:** This safely drops intermediate screens or appends the parent, ensuring old screens trigger `onDestroy()` without leaking memory.

## 5. Decompose Slot Navigation & Dialog Architecture
Modal dialogs, action sheets, and overlays managed via Decompose `childSlot` follow a strict two-type model:
- **One Declaration Per File:** `<ScreenName>DialogConfig.kt` and `<ScreenName>DialogChild.kt` must reside in separate files inside a dedicated `dialog/` package.
- **Sealed Class Type Hierarchy:**
  - `Config`: Must be declared as `@Serializable sealed class` to survive process death via `StateKeeper`.
  - `Child`: Must be declared as `sealed class`.
- **Stateless Dialogs:** Simple prompts or confirmation alerts use `data object <DialogName> : <ScreenName>DialogChild()`.
- **Stateful Dialogs:** Dialogs requiring business logic or data fetching encapsulate a child component: `data class <DialogName>(val component: <DialogName>DialogComponent) : <ScreenName>DialogChild()`.

## 6. Reactive List Mutation Synchronization (`ListOwner`)
When mutating items from a detail screen that affect list screens in the backstack:
- **Owner Interfaces:** Define `*ListOwner` interfaces with mutation notifications (e.g., `notifyItemDeleted(id)`).
- **Delegation:** Stack `Child` wrappers delegate implementation directly to their child components: `class GlobalList(...) : Child, ListOwner by component`.
- **Broadcasting:** Screens trigger mutations by iterating active stack items: `stack.value.items.mapNotNull { it.instance as? ListOwner }.forEach { ... }`.

## 7. Universal Deep Links & Wasm Browser History
- **Route Serialization:** Implement typed navigation routes implementing `NavigationRoute` or `ParameterizedNavigationRoute<P>` from `shared-foundation`, using `NavigationRoute.toFullUrl(baseUrl)`.
- **Wasm History Integration:** Applications register `SetupBrowserHistory(mainComponent, baseUrl)` using `getAncestorPaths()` to synchronize browser history with Decompose stack navigation.
