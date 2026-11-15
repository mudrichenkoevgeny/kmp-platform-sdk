# KMP Platform SDK — Development Roadmap

This document outlines the strategic roadmap for improving code quality, architectural consistency, testing infrastructure, and CI/CD automation across the `kmp-platform-sdk` ecosystem (Android & Wasm). As a multiplatform foundational library with Compose Multiplatform and Decompose, backward compatibility, strict layering, and cross-platform determinism are paramount.

## 1. Static Analysis & Compiler Configuration

- [ ] **Strict Kotlin Compiler Configuration (Typecheck)**
  - [ ] Treat Warnings as Errors (`-Werror`): Enforce zero-warning policy across all module targets (`commonMain`, `androidMain`, `wasmJsMain`).
  - [x] Explicit API Mode (`-Xexplicit-api=strict`): Mandatory for all public SDK modules (`core/*`, `feature/*`) to guarantee explicit visibility modifiers (`public`, `internal`) and explicit return types, preventing internal implementation leakage.
- [x] **Detekt 2.x (Static Analysis)**
  - [x] Gradle Integration: Setup `io.gitlab.arturbosch.detekt` with centralized baseline and `config/detekt/detekt.yml`.
  - [ ] `UnsafeCallOnNullableType`: Absolute ban on the `!!` operator across production source sets (`commonMain`, `androidMain`, `wasmJsMain`).
  - [ ] `MandatoryBracesIfStatements`: Mandatory curly braces on all `if`/`else` control flow branches.
  - [ ] Custom / Formatting PSI checks: Ban inline implementation comments (`//`, `/* */`) inside function bodies (preserving structured KDoc `/** */`).
  - [ ] `when` Expression Control: Ban assignments and function calls inside `when (...)` condition subjects.
- [ ] **Ktlint & Formatting Control**
  - [x] Gradle Plugin: Setup `org.jlleitschuh.gradle.ktlint` driven by `.editorconfig`.
  - [x] Strict prohibition of trailing commas (`ktlint_standard_trailing-comma-on-call-site = disabled`, `ktlint_standard_trailing-comma-on-declaration-site = disabled`).
  - [ ] Strict prohibition of Fully Qualified Names (FQN) in expressions, types, and generics (`no-empty-first-line-at-class-body`, import optimization).
  - [ ] Consistent import ordering, wildcard import bans, and automated unused import stripping.
- [ ] **Lefthook (Local Pre-Commit Automation)**
  - [ ] Repository-Level Hooks (`lefthook.yml`): Run fast verification on staged files before commit (`ktlintCheck`, `detekt`, and fast Konsist structural checks).

## 2. Architectural Control (Konsist)

Konsist enforces the modular boundaries and architectural invariants defined in `.agent/` across the codebase:

- [ ] **Module Hierarchy & Dependency Inversion**
  - [ ] Verify that `core:common` is a pure leaf infrastructure module with zero dependencies on `feature/*` or other `core/*` modules.
  - [ ] Enforce that `feature` modules only depend on core infrastructure and explicitly allowed domain dependencies.
- [ ] **Single Declaration Per File**
  - [ ] Verify that every public `interface`, `class`, or `object` resides in its own dedicated `.kt` file matching the declaration name.
- [ ] **Decompose Component & State Standards**
  - [ ] Verify that all Decompose component interfaces expose observable state strictly via `com.arkivanov.decompose.value.Value<T>`.
  - [ ] Forbid mutable properties (`var`) on public component interfaces.
  - [ ] Enforce that modal/dialog slot navigation exposes `Value<ChildSlot<Config, Child>>`.
- [ ] **Dialog Architecture Compliance**
  - [ ] Verify that dialog entities reside in a `dialog/` subpackage.
  - [ ] Enforce that `<ScreenName>DialogConfig` and `<ScreenName>DialogChild` are declared as `sealed class` hierarchies in separate files.
  - [ ] Enforce that all `*DialogConfig` classes are annotated with `@kotlinx.serialization.Serializable`.
- [ ] **Value Class ID Contract**
  - [ ] Verify that every value class ending with `Id` (e.g. `UserId`, `UserSessionId`) is accompanied by extension functions `asHexDashString()`, `toXxxIdOrNull()`, and `toXxxIdOrThrow()`.
- [ ] **Storage & Persistence Isolation**
  - [ ] Forbid direct imports of Android `SharedPreferences`, `DataStore`, or Web `IndexedDB`/`localStorage` inside `feature/*` modules. All persistence must go through `EncryptedSettings` abstractions.
- [ ] **Structured KDoc Verification**
  - [ ] Enforce structured KDoc on every public class, interface, and public member function across the SDK API surface.

## 3. UI, Design Tokens & Localization Guard

- [ ] **UI Localization Enforcement (Konsist / Detekt)**
  - [ ] Verify that `@Composable` functions do not pass raw string literals to UI text consumers (e.g., `Text`, buttons, dialog labels).
  - [ ] Require localized resources (`Res.string.*` via `stringResource(...)`).
- [ ] **Compose Preview Coverage**
  - [ ] Validate that every UI screen component (`*Screen.kt`) provides dedicated preview entry points utilizing preview annotations (`@PreviewComponentSizes`, `@PreviewThemes`, `@PreviewFontScales`, `@PreviewScreenSizes`).
- [ ] **Design Tokens Consistency Check**
  - [ ] CI verification ensuring `CoreTheme` (colors, typography, dimensions, shapes) maps directly to `GeneratedDesignTokens.kt`.
  - [ ] Guard `GeneratedDesignTokens.kt` against manual PR modifications (source of truth is `platform-design-system`).
- [ ] **Screenshot Testing (Roborazzi)**
  - [ ] Roborazzi setup in `src/androidUnitTest` for verifying Composable layouts and themes across light/dark and font-scaling configurations.
  - [ ] Baseline images stored in `src/androidUnitTest/snapshots/images/`.

## 4. Navigation, Contracts & Logic Testing

- [ ] **Navigation & Deep Link Contract Tests**
  - [ ] Unit tests for all `*DeepLinkParser` and `*UrlFormatter` implementations verifying extraction of `*Params` data classes and canonical URL construction via `NavigationRoute.toFullUrl(baseUrl)`.
  - [ ] Fallback tests for malformed query parameters and missing paths.
- [ ] **Decompose Stack & Slot Transitions**
  - [ ] Test `handleBack` behavior: verify that navigating back replaces stack elements cleanly via `navigation.navigate` when previous config does not match parent destination, avoiding duplicate configurations.
  - [ ] Test `ChildSlot` dismissal and presentation lifecycles for modal dialogs.
- [ ] **Reactive List Invalidation (ListOwner Synchronization)**
  - [ ] Tests ensuring mutations (deletion/creation) trigger `notify*` extension functions across `ChildStack.items` and propagate to `ListOwner` subscribers.
- [ ] **Browser History Integration (Wasm)**
  - [ ] Unit tests verifying `getAncestorPaths()` and `calculateActivePath` produce consistent URL breadcrumbs for the Wasm browser back stack.
- [ ] **JSON Serialization & DTO Contracts**
  - [ ] Multiplatform serialization tests (`kotlinx.serialization`) verifying wire format compatibility, strict `snake_case` mapping, and `@SerialName` validation.
- [ ] **Error Pipeline & Chain of Responsibility**
  - [ ] Unit tests for feature-specific `AppErrorParser` implementations verifying that domain error codes correctly map to localized strings and that unhandled errors cascade down to the common fallback parser.

## 5. API Backward Compatibility & Consumer ProGuard

- [ ] **Binary Compatibility Validator**
  - [x] Setup `org.jetbrains.kotlinx.binary-compatibility-validator`.
  - [ ] Maintain `.api` dumps for all published modules (`core/*`, `feature/*`, `bom`).
  - [ ] Configure `validationDisabled = true` during active feature development phases to prevent blocking builds on ABI evolution, switching to strict mode prior to major releases.
- [ ] **Consumer R8 / ProGuard Rules (`consumer-rules.pro`)**
  - [ ] Provide and bundle `consumer-rules.pro` in `androidMain` source sets across all `core/*` and `feature/*` modules.
  - [ ] Preserve `@Serializable` companion objects and serializers to guarantee crash-free tree-shaking on consumer Android apps.

## 6. CI/CD Pipeline & GitHub Actions Automation

- [ ] **Pre-Flight Security & Fast Gates**
  - [ ] `gitleaks-action` execution on PRs to prevent secret leakage.
  - [ ] Fast static analysis gate: parallel execution of `ktlintCheck`, `detekt`, and Konsist architecture tests.
- [ ] **Multi-Target Build Matrix**
  - [ ] Parallel compilation and execution across `androidUnitTest` and `wasmJsTest`.
  - [ ] Roborazzi screenshot regression tests in headless Android runner.
- [ ] **Sample Integration Verification**
  - [ ] Assemble `sampleclient` (Android APK & Wasm bundle) and `samplemanagement` to verify end-to-end integration, DI bootstrap, and runtime wiring in realistic host applications.
- [ ] **Zero-Secret CI & Maven Central Publishing**
  - [ ] Tokenless OIDC Trusted Publishing with Sonatype/Maven Central.
  - [ ] In-memory GPG artifact signing for jar, aar, klib, and `:bom` distributions via `com.vanniktech.maven.publish`.
- [ ] **Automated Changelog & Release Notes**
  - [ ] Native GitHub Release generator (`gh release create --generate-notes`) using `.github/release.yml` for category routing (Features, Fixes, Maintenance, Breaking Changes).

## 7. Developer Experience (DX) & Documentation

- [ ] **Dokka V2 Documentation Site**
  - [x] Integrated `org.jetbrains.dokka` plugin using Dokka V2 HTML engine (`./gradlew dokkaGenerate`).
  - [ ] Cross-module linking across all `core/*` and `feature/*` packages.
  - [ ] Automated deployment of generated static API reference docs to GitHub Pages on release.
- [ ] **SDK vs Sample Architectural Guard**
  - [ ] Konsist test verifying that zero SDK logic resides inside `sample/*` modules, preserving `sampleclient` and `samplemanagement` solely as host consumption showcases.
