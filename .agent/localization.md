---
description: Compose localization resource layout and naming conventions
globs: "**/composeResources/**/*.xml"
alwaysApply: true
---

# Localization (Compose Multiplatform Resources)

This document defines the structure and naming standards for all localized resources within the `kmp-platform-sdk`. These resources are managed via the Compose Multiplatform resource system.

## 1. Directory Structure
- Localization resources must reside strictly under: `*/src/*/composeResources/values*/strings.xml`.
- **Language Codes:** Use standard ISO codes as folder name suffixes (e.g., `values`, `values-ru`, `values-en`).
- **File Integrity:** Keep exactly **one** `strings.xml` per locale folder. Do not split into multiple files (e.g., `errors.xml`, `ui.xml`).
- **Hierarchy Example:**
  - `core/common/src/commonMain/composeResources/values/strings.xml` (Default/English)
  - `core/common/src/commonMain/composeResources/values-ru/strings.xml`

## 2. Naming Conventions
- **Prefix Management:** Use stable, domain-driven resource IDs to prevent collisions across modules:
  - Common Errors: `error_common_*`
  - User Errors: `error_user_*`
  - Security Errors: `error_security_*`
  - UI Elements: `ui_[feature]_[element]_*` (e.g., `ui_user_button_login`)
- **Code Alignment:** Resource IDs must strictly match the codes used in `AppError` hierarchies and their corresponding `AppErrorParser` implementations.

## 3. Message Formatting
- **Placeholders:** Use standard XML string placeholders (e.g., `%1$s`, `%d`) that correspond to the `args` provided in the `AppError` models.
- **Conciseness:** Keep localized strings descriptive but brief, avoiding generic filler text that might overflow on small mobile screens or Wasm-based web layouts.

## 4. Resource Integrity (Definition of Done)
When adding new error codes or UI text:
- **Locales Parity:** Add the corresponding `<string>` tag in **every** existing locale folder within the module.
- **Fallback Rule:** Ensure that unknown or unmapped codes are gracefully handled by the `CommonErrorParser` fallback logic.
- **No Suffixes:** Filenames must remain `strings.xml`. The directory name (`values-xx`) is the sole indicator of the locale.

---
*Refer to `AGENTS.md` for the full list of project standards.*