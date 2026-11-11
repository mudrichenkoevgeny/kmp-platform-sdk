---
description: Local Design Tokens integration, GeneratedDesignTokens, font assets, and CoreTheme wiring
globs: "**/*.kt"
alwaysApply: true
---

# Design Tokens & Theme Integration Standards (kmp-platform-sdk)

This document defines local theme wiring and design token integration standards for the `kmp-platform-sdk`.

## 1. Design Tokens & Theme Integration
- **Single Source of Truth:** All visual design tokens (colors, spacing, sizing, radius, elevation) and font assets (`assets/fonts/ttf/*.ttf`) originate from the [platform-design-system](https://github.com/mudrichenkoevgeny/platform-design-system) repository.
- **Generated Tokens Artifact:** `GeneratedDesignTokens.kt` (`core/common/src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/ui/theme/tokens/GeneratedDesignTokens.kt`) is generated automatically by the design system toolchain. **Manual modifications to `GeneratedDesignTokens.kt` are strictly prohibited.**
- **Font Assets Location:** Physical font files (`.ttf` / `.otf`) are copied directly from `platform-design-system` (`assets/fonts/ttf/`) into `core/common/src/commonMain/composeResources/font/` so Compose Resources can generate `Res.font.*` accessors.
- **Theme Wiring:** Theme primitives in `CoreTheme` (`Color.kt`, `Dimens.kt`, `Shapes.kt`, `Typography.kt`) must map their values directly from `GeneratedDesignTokens`.

## 2. SDK Preview Containers
- **SDK Theme Container:** SDK Compose previews must wrap content in `CoreTheme` (or `ScreenPreviewContainer` for screens).
