# kmp-platform-sdk-testing (`:testing`)

Comprehensive testing harness, cross-module UI test helpers, Roborazzi screenshot utilities, and repository-wide Konsist architecture suite for `kmp-platform-sdk`.

`:testing` allows consumer applications (`sampleclient`, `samplemanagement`) and internal SDK components to write clean unit, UI, and screenshot tests using standardized test fixtures and tools.

---

## Capabilities & Key Features

1. **Repository-Wide Konsist Architecture Suite:**
   Centralized architecture tests enforcing layer isolation, package structure, "1 public declaration per file", Decompose component standards, Compose UI preview completeness (mandatory `Sizes`, `Themes`, `FontScales` previews), and KDoc API documentation across all modules.
2. **UI & Screenshot Testing (Roborazzi):**
   - **`captureAppScreen`** — Standardized Roborazzi test utility for capturing Composable UI snapshots (screens, dialogs, and components) using `GraphicsMode.NATIVE`.
3. **Shared UI Test Helpers:**
   Shared test wrappers and mock configurations for isolated UI component rendering in unit tests and screenshot baseline generation.

---

## Installation & Consumption

In Gradle build scripts (for internal SDK modules):

```kotlin
// build.gradle.kts
dependencies {
    testImplementation(project(":testing"))
    // or for android tests:
    // androidUnitTestImplementation(project(":testing"))
}
```

---

## Architecture & Package Structure

```text
io.github.mudrichenkoevgeny.kmp.testing
├── architecture (jvmTest)
│   └── ArchitectureTest.kt
└── ui (androidMain)
    └── test
        └── RoborazziUtils.kt
```
