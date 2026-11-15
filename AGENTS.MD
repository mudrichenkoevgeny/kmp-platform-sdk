# KMP Platform SDK — Project Standards (Index)

This document is the entry point for architectural and coding standards. These rules ensure consistency across the modular Kotlin Multiplatform (Android & Wasm) ecosystem for all contributors and AI agents.

## Core Principles

- **Precedence:** Local project standards (found in `.agent/`) override any global IDE, linter, or AI assistant defaults.
- **SDK Nature:** This is a **foundational library**, not a standalone application. Avoid hardcoded assumptions; prefer configuration via constructor parameters, DI, and optional wiring in `sample*`.
- **FQN Forbidden:** Do not write fully qualified names (FQN) in expressions, types, or generics. Use imports or typealiases. Permitted only inside `@Deprecated(..., replaceWith = ReplaceWith(...))` import parameters, or when technically unavoidable (must be explained in the task summary).
- **No Agent-Generated Implementation Comments (Preserve KDoc & Developer TODOs):** Do not write inline implementation comments (`//` or `/* ... */`) or generate new `TODO`/`FIXME` markers inside code bodies; logic must be self-documenting. Never delete pre-existing developer `// TODO:` or `// FIXME:` comments unless the task explicitly resolves them. **CRITICAL:** KDoc documentation (`/** ... */`) on classes, interfaces, properties, and functions is structured API documentation, NOT code comments. AI must NEVER delete or strip KDoc documentation during code edits or refactorings! All KDoc must be written in English.
- **No Trailing Commas:** Do not use trailing commas at the end of argument, parameter, or entry lists.
- **Null Safety (Strict Prohibition of `!!`):** The non-null assertion operator (`!!`) is strictly forbidden in production code (allowed only in test files). Handle nullability gracefully using safe calls (`?.`), Elvis operators (`?:`), safe casts (`as?`), or explicit smart-casts. Use `requireNotNull(...)` or `checkNotNull(...)` with descriptive messages only when asserting invariants or unrecoverable errors, never as a substitute for business logic error handling.
- **Mandatory UI Testing & Previews:** Every created UI screen (`*Screen.kt`) or component must be accompanied by its corresponding Unit/UI Test (`*Test.kt`), Screenshot Test (`*ScreenshotTest.kt` with Roborazzi), and Compose Previews using dedicated preview annotations (`@PreviewComponentSizes`, `@PreviewScreenSizes`, `@PreviewThemes`, `@PreviewFontScales`).
- **Mandatory UI Localization:** Production UI code must strictly use localized string resources (`Res.string.*` via `stringResource(...)`). Hardcoded string literals are strictly forbidden in production UI code (permitted only in tests and previews).
- **Domain ID Conversions:** Always use `string.toXxxIdOrThrow()` / `string.toXxxIdOrNull()` for converting strings to domain value class IDs (e.g. `UserId`, `UserSessionId`), and `.asHexDashString()` for converting IDs to string. Do not construct `XxxId(Uuid.parse(string))` directly.
- **Navigation, Screen Params & Slot Navigation:** Decompose screen destinations requiring arguments must encapsulate parameters inside dedicated, serializable `*Params` data classes (e.g. `UserDetailParams(val userIdValue: String)`). Universal deep link routing uses `shared-foundation` `NavigationRoute` contracts with modular `*DeepLinkParser` / `*UrlFormatter` implementations (with required `baseHost: String`), `toFullUrl()` formatting, and app-specific `*AppDeepLinkRoute` aggregators. Stack `handleBack` methods use Decompose `navigation.navigate` stack replacement to safely destroy top screens when navigating to parents. List mutations notify `ListOwner` instances across `ChildStack.items` via `.forEach { ... }`. Wasm history utilizes `SetupBrowserHistory(mainComponent, baseUrl)` with ancestor path seeding to align UI and Browser Back button behavior. Modal dialogs and bottom sheets use Decompose `childSlot`: slot entities reside in separate files inside a `dialog/` subpackage
  (`<ScreenName>DialogConfig.kt` and `<ScreenName>DialogChild.kt`); `Config` (annotated with `@Serializable`) and `Child` entities are `sealed class` hierarchies with KDoc on all public members.
- **Design Tokens & Theme:** All design tokens and font assets originate from [platform-design-system](https://github.com/mudrichenkoevgeny/platform-design-system) (`assets/fonts/ttf/*.ttf` -> `core/common/.../composeResources/font/`). `GeneratedDesignTokens.kt` is a read-only artifact (**never edit manually**). `CoreTheme` maps colors, dimensions, shapes, and typography directly from `GeneratedDesignTokens`.
- **Mandatory Complete Implementation:** All created or modified files must contain complete, production-ready code. Placeholder code, dummy components, `TODO()` markers, and default generator stubs are strictly prohibited unless the developer explicitly asks for a stub.
- **Mandatory Bug Fix Test Coverage:** When fixing a bug, edge-case defect, or regression, AI must write automated test coverage (unit or integration test) and execute it via a targeted test run to verify the fix and prevent future regressions.
- **Targeted Test Execution:** Never auto-run full-repository test suites (`./gradlew test`, `./gradlew check`) unless explicitly requested. However, whenever writing new tests, fixing a bug, or modifying production/UI logic, the AI agent MUST compile and run the specific affected test class or method (`--tests`) to verify the code compiles and passes.
- **Single Declaration Per File:** Every primary public `interface`, `class`, or `object` must be placed in its own separate, dedicated file matching the declaration name. Never combine an interface and its implementation class or multiple unrelated public declarations in a single file (exceptions apply only to `sealed` hierarchies and extension parsers directly bound to a `value class` in the same file).
- **No Redundant Builds:** Skip Gradle build and test tasks **only** when changes are strictly limited to Markdown files (`*.md`) or text inside KDoc blocks (`/** ... */`). Any modification to executable code, tests, Gradle scripts, or version catalogs requires compilation/test verification.
- **Mandatory Sub-Agent Audit (Dual-Agent Review):** After implementing non-trivial code changes, new features, or complex refactorings, the AI agent MUST automatically invoke an independent reviewer sub-agent via the `task` tool to audit modified files against `AGENTS.MD` guidelines, type safety, edge cases, and error handling before presenting the final result.
- **Git Mutation Ban (Read-Only Allowed):** NEVER run mutating git commands (`git add`, `git commit`, `git push`, `git reset`, `git checkout`, `git stash`, etc.) via shell. Version control, staging, and committing are strictly the developer's responsibility. Read-only diagnostic git commands (`git status`, `git diff`, `git log`, `git show`) are permitted.

## Module Map

### Core Modules (`core/*`)

*Shared infrastructure for Android and Wasm targets.*

- **`core/common`:** Ktor bootstrap, WebSocket lifecycle management, `EncryptedSettings` abstraction, platform metadata, `CoreTheme`, and Chain of Responsibility error parsing.
- **`core/security`:** Password policy validation, MFA state primitives, Ktor network client for security settings, and localized security error domains.
- **`core/settings`:** Global application configuration logic, Ktor network client for global settings, encrypted caching, and reactive state management.
- **`core/storage`:** Multiplatform storage primitives and key-value encryption abstractions.
- **`core/testing`:** Shared testing infrastructure, Robolectric setup, `AppErrorParserMock`, and screenshot capture utilities (`captureAppScreen`).

### Feature Modules (`feature/*`)

*Domain-specific logic, Network APIs, and Compose/Decompose UI.*

- **`feature/user`:** Base Identity & Auth logic. Includes core models, use cases, and encrypted storage for sessions.
- **`feature/clientuser`:** Identity solution for standard user applications. Includes multi-method auth (Email, Phone, Google), and shared Decompose navigation flows with Compose Multiplatform UI.
- **`feature/managementuser`:** Administrative identity solution for internal staff, providing resource oversight, session management, and administrative audit log inspection.

### Tools, Distribution & Samples

- **`tools/gradle-plugin`:** Included build (`kmp-platform-sdk-gradle-plugin`) providing SDK convention plugins (`kmp-platform-quality`, `kmp-platform-kotlin`, `kmp-platform-publish`, `kmp-platform-agent-sync`).
- **`tools/detekt-rules`:** Custom Detekt rules module (`kmp-platform-sdk-detekt-rules`) enforcing KMP platform code standards.
- **`bom`:** Bill of Materials (Gradle platform) to ensure version alignment across all SDK modules.
- **`sampleclient` & `samplemanagement`:** Reference host applications demonstrating `CommonComponent` initialization, error parser registration, deep link routing, and UI integration. Never move SDK or business logic into sample modules.

## Detailed Standards (`.agent/`)

### Global Shared Standards (`.agent/global/`)

- **Common (`.agent/global/common/`):**
    - **`ai-collaboration-basics.md`** — Universal AI interaction constraints, operational rules (git mutation ban, targeted test execution), dependency rules, and architectural layering.
- **Kotlin (`.agent/global/kotlin/`):**
    - **`kotlin-coding-style-basics.md`** — Naming, `when` subject rules, brace requirements, FQN/Comments ban, Trailing Commas ban, `!!` ban, companion objects, value classes, and `.value` usage.
    - **`docs-kdoc-basics.md`** — Language standards, link resolution rules, and KDoc Definition of Done.
    - **`docs-kdoc-type-basics.md`** — Specific KDoc patterns for DTOs, Error Codes, Sealed types, Routes, and Navigation Screen Params.
    - **`kotlin-testing-basics.md`** — Unit and integration test standards, mocking policy, and targeted execution rules.
- **KMP Platform (`.agent/global/kmp/`):**
    - **`ai-collaboration-kmp.md`** — KMP Platform SDK interaction constraints, Decompose component model, and SDK workflow.
    - **`docs-kdoc-type-kmp.md`** — Specific KDoc patterns for KMP components (Decompose configs, UI state, AppError hierarchies).
    - **`localization-kmp.md`** — Compose Multiplatform localization structure, naming conventions, and resource integrity.
    - **`ui-kmp.md`** — UI standards for Jetpack Compose Multiplatform, Decompose, previews, and screenshot testing.
    - **`testing-kmp.md`** — Multiplatform testing strategy (`commonTest` portability, MockK policy, `runTest`, Roborazzi workflow, and targeted execution).
    - **`architecture-patterns-kmp.md`** — Universal KMP architecture, Decompose component model, stack navigation, slot dialogs, and ListOwner.

### Local Standards (`.agent/local/`)

- **`project-overview.md`** — Project identity, KMP targets (Android/Wasm), module boundaries, and repository layout.
- **`architecture-patterns.md`** — Internal bootstrapping, AppError pipeline, listing infrastructure, and specific deep link routing for kmp-platform-sdk.
- **`localization.md`** — Error resource key conventions and AppError alignment for kmp-platform-sdk.
- **`ui.md`** — Design tokens integration, GeneratedDesignTokens, font assets, and CoreTheme wiring.
- **`testing.md`** — Test fixtures, shared-foundation factories, and SDK component testing rules.
