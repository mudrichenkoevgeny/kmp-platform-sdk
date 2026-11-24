# KMP Platform SDK Gradle Plugin (`:tools:gradle-plugin`)

Included Gradle build (`build-logic`) providing convention plugins to standardize build configurations, static analysis, publishing, and Kotlin Multiplatform setup across the `kmp-platform-sdk` ecosystem.

---

## Capabilities & Key Features

This module provides the following Gradle convention plugins:

1. **`kmp-platform-quality`**
   - Configures Detekt (with custom rules from `:tools:detekt-rules` and centralized `detekt-kmp.yml`).
   - Configures Ktlint via `org.jlleitschuh.gradle.ktlint`.
   - Sets up Binary Compatibility Validator (`bcv`).
2. **`kmp-platform-kotlin`**
   - Standardizes the Kotlin compiler options (e.g., `-Werror`, explicit API mode).
   - Configures Compose compiler flags.
3. **`kmp-platform-publish`**
   - Configures Maven Central publishing via Vanniktech Maven Publish plugin.
   - Handles POM configuration and GPG signing.
4. **`kmp-platform-agent-sync`**
   - Syncs agent rules and standards (`AGENTS.MD` and `.agent/`) to consumer projects to enforce consistency.

---

## Installation & Consumption

These plugins are automatically included via Gradle's `includeBuild` mechanism in the root `settings.gradle.kts`.

To apply them in a module's `build.gradle.kts`:

```kotlin
plugins {
    alias(libs.plugins.kmp.platform.quality)
    alias(libs.plugins.kmp.platform.kotlin)
    alias(libs.plugins.kmp.platform.publish)
}
```
