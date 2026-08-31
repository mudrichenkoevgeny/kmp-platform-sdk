# KMP Platform SDK — Project Standards (Index)

This document is the entry point for architectural and coding standards. These rules ensure consistency across the modular Kotlin Multiplatform (Android & Wasm) ecosystem for all contributors and AI agents.

## Core Principles

- **Precedence:** Local project standards (found in `.agent/`) override any global IDE, linter, or AI assistant defaults.
- **SDK Nature:** This is a **foundational library**, not a standalone application. Avoid hardcoded assumptions; prefer configuration via constructor parameters, DI, and optional wiring in `sample/*`.
- **FQN Forbidden:** Do not write fully qualified names (FQN) in expressions, types, or generics. Use imports or typealiases. Minimal FQN is allowed only if technically unavoidable (must be explained in the commit).
- **No Comments:** Do not write comments in the code. Logic must be self-documenting through naming and structural clarity.
- **Manual Test Execution:** AI agents must not run tests or suggest running them. Execution is always triggered explicitly by the developer.

## Module Map

### Core Modules (`core/*`)
*Shared infrastructure for Android and Wasm targets.*

- **`core/common`:** Ktor bootstrap, WebSocket lifecycle management, `EncryptedSettings` abstraction, platform metadata, and Chain of Responsibility error parsing.
- **`core/settings`:** Global application configuration logic, encrypted caching, and reactive state management.
- **`core/security`:** Password policy validation, MFA state primitives, and localized security error domains.

### Feature Modules (`feature/*`)
*Domain-specific logic, Network APIs, and Compose/Decompose UI.*

- **`feature/settingsapi`:** Ktor-based networking implementation for fetching global configurations.
- **`feature/securityapi`:** Ktor implementation for security policies and MFA requirement sync.
- **`feature/user`:** Base Identity & Auth logic. Includes core models, use cases and encrypted storage for sessions.
- **`feature/clientuser`:** Identity solution for standard user applications. Includes multi-method auth (Email, Phone, Google), and shared Decompose navigation flows with Compose Multiplatform UI.
- **`feature/managementuser`:** Administrative identity solution for internal staff, providing resource oversight and session management.

### Distribution (`bom`)
- **`bom`:** Bill of Materials (Gradle platform) to ensure version alignment across all SDK modules.

## Detailed Standards (`.agent/`)

- **`project-overview.md`** — Project identity, KMP targets (Android/Wasm), module boundaries, and repository layout.
- **`architecture-patterns.md`** — Decompose component tree rules, WebSocket message handler registration, and Error Parser chaining.
- **`docs-kdoc-basics.md`** — Language standards (English) and KDoc Definition of Done (mandatory for every public interface member).
- **`docs-kdoc-type-requirements.md`** — Specific KDoc patterns for DTOs, Sealed classes, and platform-specific implementations.
- **`localization.md`** — Compose Multiplatform resource layout and naming conventions for UI strings.
- **`kotlin-coding-style.md`** — Naming conventions, `when` subject rules, brace requirements (no one-line `if`), and the FQN/Comments ban.
- **`ui-compose-decompose.md`** — Rules for `CompositionLocal` usage, state hoisting in Decompose components, and Wasm-specific UI constraints.
- **`testing-conventions.md`** — Test conventions, manual execution policy, and `PreviewParameterProvider` guidance for Compose.
- **`ai-collaboration-workflow.md`** — AI constraints, Version Catalog (libs.versions.toml) management, and SDK vs Sample application boundaries.