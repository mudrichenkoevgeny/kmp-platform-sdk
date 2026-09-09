---
description: Constraints for Compose Preview helpers and UI testing boundaries
alwaysApply: true
---

# Compose Preview Helpers — No Tests

This document defines the testing boundaries for UI-related tooling and preview-specific code in the `kmp-platform-sdk`.

## 1. Scope Restriction
- **No UI Tests for Previews:** **Do not** add UI tests (Compose UI tests, Robolectric `runComposeUiTest`, etc.) for types that exist **strictly** to support **Compose Multiplatform IDE previews**.
- **No Unit Tests for Data Providers:** **Do not** create dedicated `*Test.kt` classes whose sole purpose is to verify **preview sample data** or **`PreviewParameterProvider.values`** sequences.

## 2. Examples of Out-of-Scope Subjects
The following components must remain untested by automated suites:
- `PreviewParameterProvider` implementations.
- Data classes or objects created specifically for `@PreviewParameter` injection.
- UI components designed exclusively for documentation or internal tooling previews.

## 3. Verification Strategy
- **Visual Feedback:** Rely on the **IDE Preview** (`@Preview`) and manual inspection of the `sample` application for these tooling-specific items.
- **Real Component Testing:** Focus automated testing efforts on the **real `@Composable`** screens and the **Decompose Components** that provide the actual production state, rather than the mock data used to render them in the IDE.

## 4. Coding Standards (Review)
- **Placement:** Keep preview-specific logic inside the `ui` package of the relevant feature module, or in the `sample` app if it is not part of the core SDK.
- **Naming:** Follow the project conventions for preview helpers (e.g., `*PreviewParameterProvider`) to clearly distinguish them from production code.

## 5. Mandatory Compose Previews
- **All UI components** (screens, list items, custom buttons, cards, and other views) **must** be annotated with `@Preview` (and `@InternalApi` where appropriate).
- Previews must wrap the component in `MaterialTheme` (and `Surface` where needed) and provide `LocalErrorParser provides AppErrorParserMock` via `CompositionLocalProvider` using representative mock data (`*Mock` functions).

---
*Refer to `AGENTS.md` for the full list of project standards.*
