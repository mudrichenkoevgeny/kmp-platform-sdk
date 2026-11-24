# KMP Platform SDK — Project Standards (Index)

This document is the entry point for architectural and coding standards. These rules ensure consistency across the modular Kotlin Multiplatform (Android & Wasm) ecosystem for all contributors and agents.

## Core Principles

- **Precedence:** Local project standards (found in `.agent/`) override any global IDE, linter, or agent defaults.
- **SDK Nature:** This is a **foundational library**, not a standalone application. Avoid hardcoded assumptions; prefer configuration via constructor parameters, DI, and optional wiring in `sample*`.
- **Explicit API Mode:** All SDK modules compile with `explicitApi()`. Every public declaration must declare explicit visibility modifiers and return types.
- **FQN Forbidden:** Do not write fully qualified names (FQN) in expressions, types, or generics. Use imports or typealiases. Permitted only inside `@Deprecated(..., replaceWith = ReplaceWith(...))` import parameters, or when technically unavoidable (must be explained in the task summary).
- **No Agent-Generated Implementation Comments (Preserve KDoc & Developer TODOs):** Do not write inline implementation comments (`//` or `/* ... */`) or generate new `TODO`/`FIXME` markers inside code bodies; logic must be self-documenting. Never delete pre-existing developer `// TODO:` or `// FIXME:` comments unless the task explicitly resolves them. **CRITICAL:** KDoc documentation (`/** ... */`) on classes, interfaces, properties, and functions is structured API documentation, NOT code comments. The agent must NEVER delete or strip KDoc documentation during code edits or refactorings! All KDoc must be written in English.
- **No Trailing Commas:** Do not use trailing commas at the end of argument, parameter, or entry lists.
- **Null Safety (Strict Prohibition of `!!`):** The non-null assertion operator (`!!`) is strictly forbidden in production code (allowed only in test files). Handle nullability gracefully using safe calls (`?.`), Elvis operators (`?:`), safe casts (`as?`), or explicit smart-casts. Use `requireNotNull(...)` or `checkNotNull(...)` with descriptive messages only when asserting invariants or unrecoverable errors, never as a substitute for business logic error handling.
- **Mandatory UI Testing & Previews:** Every created UI screen (`*Screen.kt`), dialog, or component must be accompanied by its corresponding Unit/UI Test (`*Test.kt`), Screenshot Test (`*ScreenshotTest.kt` with Roborazzi), and Compose Previews using dedicated preview annotations (`@PreviewScreenSizes` / `@PreviewDialogSizes` / `@PreviewComponentSizes`, `@PreviewThemes`, `@PreviewFontScales`). Preview composables must wrap content in standard preview containers (`ScreenPreviewContainer`, `DialogPreviewContainer`, or `ComponentPreviewContainer`).
- **Mandatory UI Localization:** Production UI code must strictly use the custom `shared-foundation` localization system (e.g., `CommonModuleStrings` via `LocalCommonStrings.current`). Hardcoded string literals are strictly forbidden in production UI code (permitted only in tests and previews).
- **Domain ID Conversions:** Always use `string.toXxxIdOrThrow()` / `string.toXxxIdOrNull()` for converting strings to domain value class IDs (e.g. `UserId`, `UserSessionId`), and `.asHexDashString()` for converting IDs to string. Do not construct `XxxId(Uuid.parse(string))` directly.
- **Navigation, Screen Params & Slot Navigation:** Decompose screen destinations requiring arguments must encapsulate parameters inside dedicated, serializable `*Params` data classes (e.g. `UserDetailParams(val userIdValue: String)`). Universal deep link routing uses `shared-foundation` `NavigationRoute` contracts with modular `*DeepLinkParser` / `*UrlFormatter` implementations (with required `baseHost: String`), `toFullUrl()` formatting, and app-specific `*AppDeepLinkRoute` aggregators. Stack `handleBack` methods use Decompose `navigation.navigate` stack replacement to safely destroy top screens when navigating to parents. List mutations notify `ListOwner` instances across `ChildStack.items` via `.forEach { ... }`. Wasm history utilizes `SetupBrowserHistory(mainComponent, baseUrl)` with ancestor path seeding to align UI and Browser Back button behavior. Modal dialogs and bottom sheets use Decompose `childSlot`: slot entities reside in separate files inside a `dialog/` subpackage
  (`<ScreenName>DialogConfig.kt` and `<ScreenName>DialogChild.kt`); `Config` (annotated with `@Serializable`) and `Child` entities are `sealed class` hierarchies with KDoc on all public members.
- **Design Tokens & Theme:** Design tokens, font assets, and vector icons originate from `shared-foundation` and are generated/extracted automatically at build time via the `shared-foundation-design-system` Gradle plugin (`build/generated/...`). `CoreTheme` maps colors, dimensions, shapes, and typography directly from generated `PlatformDesignTokens`.
- **Mandatory Bug Fix Test Coverage:** Whenever fixing a bug or regression, you must write automated test coverage (unit or integration test) and execute it via a targeted test run to verify that the bug is resolved and cannot resurface.
- **Targeted Test Execution:** Never auto-run full-project test suites (`test`, `check` tasks) unless explicitly requested. However, whenever writing new tests, fixing a bug, or modifying production logic, the agent MUST compile and run the specific affected test class or method (`--tests`) to verify the code compiles and passes.
- **Complete Implementation:** All created or modified files must contain complete, production-ready code. Placeholder code, stubs, and partial omissions (`// ... rest of code`, `TODO()`) are strictly prohibited unless explicitly requested.
- **Single Declaration Per File:** Every primary public `interface`, `class`, or `object` must be placed in its own separate, dedicated file matching the declaration name. Never combine an interface and its implementation class or multiple unrelated public declarations in a single file (exceptions apply only to `sealed` hierarchies and extension parsers directly bound to a `value class` in the same file).
- **No Redundant Builds:** Skip Gradle build and test tasks **only** when changes are strictly limited to Markdown files (`*.md`) or text inside KDoc blocks (`/** ... */`). Any modification to executable code, tests, Gradle scripts, or version catalogs requires compilation/test verification.
- **Mandatory Sub-Agent Audit (Dual-Agent Review):** After implementing non-trivial code changes, new features, or complex refactorings, the agent MUST automatically invoke an independent reviewer sub-agent via the `task` tool to audit modified files against `AGENTS.MD` guidelines, type safety, edge cases, and error handling before presenting the final result.
- **Git Mutation Ban (Read-Only Allowed):** NEVER run mutating git commands (`git add`, `git commit`, `git push`, `git reset`, `git checkout`, `git stash`, etc.) via shell. Version control, staging, and committing are strictly the developer's responsibility. Read-only diagnostic git commands (`git status`, `git diff`, `git log`, `git show`) are permitted.

## Module Map

### Core Modules (`core/*`)

*Shared infrastructure for Android and Wasm targets.*

- **`core/common`:** Ktor bootstrap, WebSocket lifecycle management, `EncryptedSettings` abstraction, platform metadata, `CoreTheme`, and Chain of Responsibility error parsing.
- **`core/security`:** Password policy validation, MFA state primitives, Ktor network client for security settings, and localized security error domains.
- **`core/settings`:** Global application configuration logic, Ktor network client for global settings, encrypted caching, and reactive state management.
- **`core/storage`:** File storage infrastructure, upload/download REST clients, storage settings, and reactive WebSocket updates.

### Feature Modules (`feature/*`)

*Domain-specific logic, Network APIs, and Compose/Decompose UI.*

- **`feature/user`:** Base Identity & Auth logic. Includes core models, use cases, and encrypted storage for sessions.
- **`feature/clientuser`:** Identity solution for standard user applications. Includes multi-method auth (Email, Phone, Google), and shared Decompose navigation flows with Compose Multiplatform UI.
- **`feature/managementuser`:** Administrative identity solution for internal staff, providing resource oversight, session management, and administrative audit log inspection.

### Tooling, Infrastructure & Reference

- **`tools/gradle-plugin`:** Included build (`kmp-platform-sdk-gradle-plugin`) providing SDK convention plugins (`kmp-platform-quality`, `kmp-platform-kotlin`, `kmp-platform-publish`, `kmp-platform-agent-sync`).
- **`tools/detekt-rules`:** Custom Detekt static analysis rules enforcing platform coding conventions.
- **`tools/project-generator`:** Internal JVM module for scaffolding clean, minimal, production-ready downstream KMP application repositories (`<name>-kmp`) on disk.
- **`testing`:** Shared testing infrastructure, Robolectric setup, `AppErrorParserMock`, screenshot capture utilities (`captureAppScreen`), and repository-wide Konsist architecture suite.
- **`bom`:** Bill of Materials (`kmp-platform-sdk-bom`) for dependency version alignment.
- **`sampleclient` & `samplemanagement`:** Reference host applications demonstrating `CommonComponent` initialization, error parser registration, deep link routing, and UI integration. Never move SDK or business logic into sample modules.

## Detailed Standards (`.agent/`)

### Global Shared Standards (`.agent/global/`)
- **Common (`.agent/global/common/`):**
  - **[`agent-workflow-basics.md`](.agent/global/common/agent-workflow-basics.md)** — Universal agent interaction constraints, operational rules (git mutation ban, targeted test execution), dependency rules, and architectural layering.
- **Kotlin (`.agent/global/kotlin/`):**
  - **[`kotlin-coding-style-basics.md`](.agent/global/kotlin/kotlin-coding-style-basics.md)** — Naming, `when` subject rules, brace requirements, FQN/Comments ban, Trailing Commas ban, `!!` ban, companion objects, value classes, and `.value` usage.
  - **[`docs-kdoc-basics.md`](.agent/global/kotlin/docs-kdoc-basics.md)** — Language standards, link resolution rules, and KDoc Definition of Done.
  - **[`docs-kdoc-type-basics.md`](.agent/global/kotlin/docs-kdoc-type-basics.md)** — Specific KDoc patterns for DTOs, Error Codes, Sealed types, Routes, and Navigation Screen Params.
  - **[`kotlin-testing-basics.md`](.agent/global/kotlin/kotlin-testing-basics.md)** — Unit and integration test standards, mocking policy, and targeted execution rules.
- **KMP (`.agent/global/kmp/`):**
  - **[`agent-workflow-kmp.md`](.agent/global/kmp/agent-workflow-kmp.md)** — KMP-specific agent engineering constraints and target platform boundaries.
  - **[`architecture-patterns-kmp.md`](.agent/global/kmp/architecture-patterns-kmp.md)** — MVI architecture, Decompose component hierarchy, slot navigation, and state flows.
  - **[`docs-kdoc-type-kmp.md`](.agent/global/kmp/docs-kdoc-type-kmp.md)** — KDoc rules for KMP-specific components and expect/actual declarations.
  - **[`localization-kmp.md`](.agent/global/kmp/localization-kmp.md)** — `shared-foundation` localization system integration rules.
  - **[`testing-kmp.md`](.agent/global/kmp/testing-kmp.md)** — Robolectric UI testing and Roborazzi screenshot testing standards.
  - **[`ui-kmp.md`](.agent/global/kmp/ui-kmp.md)** — Compose Multiplatform UI guidelines and preview container standards.

### Local Standards (`.agent/local/`)
- **[`project-overview.md`](.agent/local/project-overview.md)** — Project identity, module boundaries, and convention plugins.
- **[`architecture-patterns.md`](.agent/local/architecture-patterns.md)** — SDK bootstrap lifecycle and Decompose slot navigation.
- **[`localization.md`](.agent/local/localization.md)** — Embedded strings in `shared-foundation`.
- **[`testing.md`](.agent/local/testing.md)** — Local test environment, Konsist architecture suite, and test assertions reuse.
- **[`ui.md`](.agent/local/ui.md)** — Design tokens build integration, theme wiring, and preview containers.
