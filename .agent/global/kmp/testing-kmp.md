---
description: Multiplatform testing strategy, test fixtures, Roborazzi captureAppScreen, and serialization boundaries
globs: "**/*Test*.kt"
alwaysApply: true
---

# Multiplatform Testing Standards — KMP Platform Additions

This document defines testing constraints, test fixture standards, and screenshot tooling for KMP applications and SDK modules. It builds upon `kotlin-testing-basics.md`.

## 1. Common vs Platform Testing Strategy

- **Common Source Sets (`commonTest`):** Must use portable **`kotlin.test`** assertions. Never use JVM-only assertion or mocking libraries in `commonTest`.
- **Mocking Policy:**
  - For `commonTest`: Use **hand-written Fakes, Mocks, or Stubs**. Reflection-based mocking frameworks are strictly forbidden in multiplatform common code.
  - For platform-specific source sets (`jvmTest`, `androidHostTest`): MockK and platform mock frameworks are permitted.
- **Coroutines Testing:** Always wrap suspending test invocations in `runTest` from `kotlinx-coroutines-test`.

## 2. Test Fixtures & Shared Factories

- **No Inline Dummy Objects:** Never construct ad-hoc inline fixtures (`dummyUser`, `dummyPayload`) inside test bodies. Use centralized factory functions from `shared-foundation-testing` (e.g., `createTestUserPrivate()`, `createTestAuditEventSummary()`).

## 3. Screenshot Testing (Roborazzi & `captureAppScreen`)

- **Execution Target:** UI screenshot regression tests must reside in `src/androidHostTest` utilizing Robolectric and Roborazzi.
- **Screen Capture Utility:** Use the `captureAppScreen` helper provided by `:core:testing` (`androidMain`) for consistent screen rendering and baseline capture.
- **Naming Standard:** Use the `[Subject]ScreenshotTest.kt` naming convention.
- **Baseline Directory:** Golden baseline images must be stored under `src/androidHostTest/snapshots/images/` and tracked in version control.
- **Gradle Tasks:**
  - Verify baselines: `./gradlew :module:verifyRoborazziDebug --tests "com.example.SubjectScreenshotTest"` (or `./gradlew verifyRoborazzi` when explicitly requested)
  - Record/update baselines: `./gradlew :module:recordRoborazziDebug --tests "com.example.SubjectScreenshotTest"`

## 4. Infrastructure & Serialization Verification Boundaries

- **Encrypted Storage:** Platform storage components (like `EncryptedSettings`) must have concrete verification in both `androidHostTest` and `commonTest`.
- **Serialization Suite:** All shared DTOs and WebSocket envelope models must have automated round-trip serialization tests.

## 5. Targeted Test Execution

- **Scoped Verification:** Never run full-repository test suites (`./gradlew test`, `./gradlew check`) automatically unless explicitly requested. Whenever writing new tests, fixing a bug, or modifying production/UI logic, compile and run the specific affected test class or method (e.g., `./gradlew :module:testDebugUnitTest --tests "com.example.SubjectTest"` and `./gradlew :module:verifyRoborazziDebug --tests "com.example.SubjectScreenshotTest"` for UI changes).
