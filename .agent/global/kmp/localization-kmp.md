---
description: Compose Multiplatform localization structure, naming conventions, and resource integrity
globs: "**/composeResources/**/*.xml"
alwaysApply: true
---

# Localization Standards — KMP Platform Additions

This document defines universal structure, formatting, and naming standards for Compose Multiplatform localized resources (`composeResources`).

## 1. Directory Structure
- **Path Standard:** Localization resources must reside strictly under: `<sourceSet>/composeResources/values*/strings.xml` (typically `src/commonMain/composeResources/values*/strings.xml`).
- **Language Codes:** Use standard ISO codes as directory suffixes (e.g., `values` for default/fallback, `values-ru`, `values-en`).
- **File Integrity:** Keep exactly **one** `strings.xml` per locale directory. Do not split into multiple files (e.g., `errors.xml`, `ui.xml`).
- **No File Suffixes:** The file name must strictly remain `strings.xml`. The directory name (`values-xx`) is the sole indicator of the locale.

## 2. UI Naming Conventions
- **Prefix Management:** Use stable, domain-driven resource keys to prevent collisions across modules:
  - UI Elements: `ui_[feature]_[element]_[action]` (e.g., `ui_auth_button_login`, `ui_profile_label_email`)
  - Common Controls: `ui_common_[control]` (e.g., `ui_common_action_retry`, `ui_common_action_cancel`)

## 3. UI Usage Enforcement
- **Production UI:** All user-facing text strings in production `@Composable` functions must be retrieved via `stringResource(Res.string.*)`. Hardcoded string literals in production UI code are strictly forbidden.
- **Tests & Previews Exception:** Raw string literals are permitted only inside unit tests, screenshot tests, and Compose preview composables.

## 4. Message Formatting
- **Placeholders:** Use standard XML string placeholders (e.g., `%1$s`, `%2$d`) for dynamic values.
- **Conciseness:** Keep localized strings descriptive but brief to prevent UI clipping across varying screen sizes and platforms.

## 5. Resource Integrity (Definition of Done)
When adding or modifying localized strings:
- **Locales Parity:** Add the corresponding `<string>` entry in **every** existing locale directory within the module to prevent runtime crashes.
