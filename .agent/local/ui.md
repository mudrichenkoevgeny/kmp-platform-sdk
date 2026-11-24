---
description: Local Design Tokens integration, PlatformDesignTokens, font assets, vector icons, and CoreTheme wiring
globs: "**/*.kt"
alwaysApply: true
---

# Design Tokens & Theme Integration Standards (kmp-platform-sdk)

This document defines local theme wiring and design token integration standards for the `kmp-platform-sdk`.

## 1. Design Tokens & Theme Integration
- **Single Source of Truth:** All visual design tokens (colors, spacing, sizing, radius, elevation), font assets, and vector icon drawables originate from `shared-foundation` and are generated/extracted automatically at build time via the `shared-foundation-design-system` Gradle plugin.
- **Generated Tokens Artifact:** `PlatformDesignTokens.kt` is generated automatically during build into `build/generated/sources/designTokens/...`. Manual edits or committing generated token files is prohibited.
- **Font & Icon Assets Extraction:** Font files (`pt_sans_*.ttf`) and vector icon drawables are extracted automatically into Compose Multiplatform resources during build per module's `iconModules` configuration (`sharedDesignSystem { iconModules.set(...) }`) so Compose Resources generates `Res.font.*` and `Res.drawable.*` accessors.
- **Theme Wiring:** Theme primitives in `CoreTheme` (`Color.kt`, `CoreDimens.kt`, `Shapes.kt`, `Typography.kt`) must map their values directly from generated `PlatformDesignTokens`.

## 2. SDK Preview Containers
- **SDK Theme Container:** SDK Compose previews must wrap content in `CoreTheme` (or `ScreenPreviewContainer` for screens).
