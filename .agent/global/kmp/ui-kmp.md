---
description: UI standards for Jetpack Compose Multiplatform, Decompose, previews, and screenshot testing
globs: "**/*.kt"
alwaysApply: true
---

# Compose UI, Previews, and Testing Standards — KMP Platform Additions

This document defines standards for UI components, screens, preview tooling, and testing boundaries for KMP applications.

## 1. UI Localization Enforcement
- **Production UI:** All user-facing text in production `@Composable` functions must strictly use Compose Multiplatform localized resources (`Res.string.*` via `stringResource(...)`). Hardcoded string literals in production UI code are strictly forbidden.
- **Tests & Previews Exception:** Non-localized raw string literals and test tags are permitted **only** inside unit tests, screenshot tests, and Compose preview composables.

## 2. Mandatory Previews for Screens and Components
- **UI Components:** Every reusable UI component (`Core*` or custom composable) must provide Compose previews annotated with `@PreviewComponentSizes`, `@PreviewThemes`, and `@PreviewFontScales`.
- **UI Screens:** Every screen composable (`*Screen.kt`) must provide Compose previews annotated with `@PreviewScreenSizes`, `@PreviewThemes`, `@PreviewFontScales`, and state providers (`*PreviewParameterProvider`).
- **Annotations & Visibility:** All preview composables must be marked `private` and annotated with `@InternalApi`.
- **Theme & Composition Local Wrapper:** Previews must wrap content in the application's root theme container and supply required composition local mocks to prevent preview rendering crashes (e.g. `LocalErrorParser provides AppErrorParserMock`).

## 3. Mandatory Companion Tests
- **Screen & Component Coverage:** Every created UI screen (`*Screen.kt`) or UI component must be accompanied by both:
  1. **Compose UI / Unit Test (`*Test.kt`):** Verifies interactive behavior, clicks, states, and semantics.
  2. **Screenshot Test (`*ScreenshotTest.kt`):** Uses Roborazzi with Robolectric in `src/androidHostTest` to capture visual regression baselines.

## 4. Scope Restriction for Preview Tooling
- **No UI Tests for Preview Tooling:** **Do not** add UI tests for types that exist **strictly** to support IDE previews (e.g. `PreviewParameterProvider` classes).
- **No Unit Tests for Data Providers:** **Do not** create `*Test.kt` classes whose sole purpose is to verify preview sample data sequences.

## 5. Verification Strategy
- **Visual Feedback:** Rely on IDE Previews (`@Preview`) for rapid layout inspection.
- **Automated Verification:** Use companion unit tests (`*Test.kt`) and Roborazzi screenshot tests (`*ScreenshotTest.kt`) for regression prevention.

## 6. Decompose Navigation Hooks
- **Child Navigation Hooks:** Root Decompose components must provide `navigateTo(destination: Destination)` functions to allow app-level deep link handlers to programmatically push nested destinations.
