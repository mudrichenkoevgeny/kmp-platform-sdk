---
description: Unit and integration test standards, KMP testing frameworks, and manual execution rule
globs: "**/src/**/*Test/**/*.kt"
alwaysApply: true
---

# Testing Standards and Conventions

This document defines the testing methodology for the `kmp-platform-sdk`. These rules ensure that tests are portable across KMP targets (Android/Wasm) and remain modular.

## 1. Operational Policy (Strict)
- **Manual Execution Only:** Test runs must be triggered explicitly by the developer (e.g., via Gradle).
- **Execution Ban:** AI agents are **strictly prohibited** from initiating test suites or suggesting the user run them.
- **Runnable Code:** AI must write complete, runnable test code without `TODO` placeholders.

## 2. Dependency Management (Hard Requirement)
- **Explicit Declaration:** Every module's `build.gradle.kts` must declare **all** test dependencies explicitly (JUnit, MockK, etc.) for the appropriate source sets.
- **No Transitive Assumptions:** Never assume a library is available via transitive dependencies.
- **Version Catalog:** Use aliases from `gradle/libs.versions.toml`.
- **Verification:** Before adding/editing tests, verify and update `testImplementation` (or `commonTest.dependencies`) in `build.gradle.kts` if needed.

## 3. Framework & Tooling
- **JVM/Android Source Sets:** Use **JUnit 5 (Jupiter)** and **MockK**.
- **Common Source Sets (`commonTest`):** Prefer **`kotlin.test`** assertions to ensure portability across Android and Wasm targets.
- **Mocking Strategy:**
  - Use MockK for JVM-based tests.
  - For `commonTest`, prefer **Hand-written Fakes/Stubs** or test doubles to avoid JVM-only library constraints.
- **Coroutines:** Use `runTest` from `kotlinx-coroutines-test` for all suspending logic.

## 4. Organization & Naming
- **One Test Class Per Unit:** Use one dedicated test file per primary subject (Type, Mapper, Use Case, or Repository).
- **Mirror Structure:** Test files must reside in the same package as the subject under test.
- **Nomenclature:**
  - Standard Tests: `[Subject]Test.kt`.
  - Integration/Component Tests: `[Component]IntegrationTest.kt`.
- **Test Names:** Use descriptive backticked names in English: ``fun `should return error when session is expired`()``.

## 5. Coding Standards in Tests
- **No FQN:** Fully Qualified Names are **strictly forbidden**. Use imports.
- **No Comments:** Tests must be self-documenting. No narrative comments.
- **Test Constants:** If a literal (String, UUID, Key) is used **more than once** in a test class, it **must** be extracted into a `private const val`.
- **Isolation:** Tests must be stateless and not rely on execution order.

## 6. Scope of Testing
- **Observable Behavior:** Focus on public APIs, Decompose components, Repositories, and Use Cases.
- **Serialization:** Ensure `core/common` serialization logic is tested for all DTOs and WebSocket messages.
- **Platform Logic:** For components with platform-specific implementations (e.g., `EncryptedSettings`), verify behavior in both `androidMain` and `commonMain` where possible.
- **Exclusions:** No UI/Unit tests for Preview-only helpers (`PreviewParameterProvider`, etc.).

---
*Refer to `AGENTS.md` for the full list of project standards.*