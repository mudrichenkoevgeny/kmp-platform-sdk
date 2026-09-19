---
description: Constraints for Compose Preview helpers, mandatory UI testing, and localization rules
alwaysApply: true
---

# Compose UI, Previews, and Testing Standards

This document defines standards for UI components, screens, preview tooling, and testing boundaries in the `kmp-platform-sdk`.

## 1. UI Localization Enforcement
- **Production UI:** All user-facing text in production `@Composable` functions must strictly use Compose Multiplatform localized resources (`Res.string.*` via `stringResource(...)`). Hardcoded string literals in production UI code are strictly forbidden.
- **Tests & Previews Exception:** Non-localized raw string literals and test tags are permitted **only** inside unit tests, screenshot tests, and Compose preview composables.

## 2. Mandatory Previews for Screens and Components
- **UI Components:** Every reusable UI component (`Core*` or custom composable) must provide Compose previews annotated with `@ComponentSizePreviews`, `@ThemePreviews`, and `@FontScalePreviews`.
- **UI Screens:** Every screen composable (`*Screen.kt`) must provide Compose previews annotated with `@ScreenSizePreviews`, `@ThemePreviews`, `@FontScalePreviews`, and state providers (`*PreviewParameterProvider`).
- **Annotations & Visibility:** All preview composables must be marked `private` and annotated with `@InternalApi`.
- **Theme Wrapper:** Previews must wrap content in `CoreTheme` (or `ScreenPreviewContainer` for screens) and provide necessary composition locals (such as `LocalErrorParser provides AppErrorParserMock`).

## 3. Mandatory Companion Tests
- **Screen & Component Coverage:** Every created UI screen (`*Screen.kt`) or UI component must be accompanied by both:
  1. **Compose UI / Unit Test (`*Test.kt`):** Verifies interactive behavior, clicks, states, and semantics.
  2. **Screenshot Test (`*ScreenshotTest.kt`):** Uses Roborazzi with Robolectric in `src/androidUnitTest` to capture visual regression baselines.

## 4. Scope Restriction for Preview Tooling
- **No UI Tests for Preview Tooling:** **Do not** add UI tests for types that exist **strictly** to support IDE previews (e.g. `PreviewParameterProvider` classes).
- **No Unit Tests for Data Providers:** **Do not** create `*Test.kt` classes whose sole purpose is to verify preview sample data sequences.

## 5. Verification Strategy
- **Visual Feedback:** Rely on IDE Previews (`@Preview`) for rapid layout inspection.
- **Automated Verification:** Use companion unit tests (`*Test.kt`) and Roborazzi screenshot tests (`*ScreenshotTest.kt`) for regression prevention.

---
*Refer to `AGENTS.md` for the full list of project standards.*
