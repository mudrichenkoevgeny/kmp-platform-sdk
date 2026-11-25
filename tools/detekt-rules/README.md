# KMP Platform SDK Detekt Rules (`:tools:detekt-rules`)

Custom static analysis rules for [Detekt](https://detekt.dev/) enforcing specific Compose Multiplatform and architectural standards for the `kmp-platform-sdk` ecosystem.

---

## Capabilities & Key Features

1. **`NoRawStringInComposeUiRule`:**
   Enforces that hardcoded string literals are not passed directly to Compose UI elements (like `Text`, `Button`, `Icon`, etc.). Requires developers to use the localized string system (e.g., `LocalCommonStrings.current`).
   - *Ignores tests and `@Preview` functions.*
   - *Ignores explicitly allowed argument names like `testTag` and `key`.*

2. **`ScreenshotTestGraphicsModeRule`:**
   Enforces that Robolectric screenshot test classes (classes ending with `ScreenshotTest`) declare `@GraphicsMode(GraphicsMode.Mode.NATIVE)` to ensure deterministic rendering across OS environments.

---

## Installation & Integration

This rule set is packaged and distributed as part of the `kmp-platform-sdk-gradle-plugin`. It is automatically applied to modules that use the `kmp-platform-quality` convention plugin.

The rules are configured via the centralized `detekt-kmp.yml` file located in the Gradle plugin resources.
