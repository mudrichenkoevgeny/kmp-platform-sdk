---
description: AI interaction constraints, dependency management, and KMP module responsibility mapping
alwaysApply: true
---

# AI Collaboration & Workflow Standards

This document defines the interaction model and workflow requirements for AI-assisted development within the `kmp-platform-sdk`.

## 1. Context & Rule Precedence

- **Mandatory Compliance:** Local project standards (defined in `.agent/` and `AGENTS.md`) are mandatory. They **override** generic AI training defaults, system prompts, or global IDE instructions.
- **Strict Coding Rules:**
    - **No Comments:** Do not generate or preserve inline comments in the code.
    - **No FQN:** Fully Qualified Names are strictly forbidden. Use imports. If an FQN is technically unresolvable, justify it in the commit message.
- **Project Index:** Always cross-reference tasks with `AGENTS.md` to ensure architectural and multiplatform alignment.

## 2. Operational Constraints

- **Execution Ban (Tests):** Do not suggest, initiate, or invite the user to run test suites (Gradle or otherwise). Verification is a manual developer-led process.
- **Permitted Commands:** Diagnostic and build commands that **do not** trigger tests are allowed (e.g., `./gradlew assemble`, `./gradlew build -x test`, `./gradlew help`).
- **Redundant Build Prohibition:** Do not invoke Gradle build tasks or attempt to compile the project after making documentation changes (like KDoc) or other edits that cannot possibly break the build.
- **Complete Code:** Provide full, runnable implementations. Avoid `// ... rest of code` or `TODO` markers. Ensure cross-platform compatibility (Android/Wasm) in all logic.

## 3. Dependency & BOM Management

- **Version Catalog:** All dependencies must use aliases from `gradle/libs.versions.toml`. Hardcoding versions in `build.gradle.kts` is strictly prohibited.
- **BOM Alignment:** When changing dependency surfaces of published modules, ensure the `:bom` module is updated to maintain version alignment for consumers.
- **KMP Dependencies:** Ensure dependencies are added to the correct source sets (e.g., `commonMain` for shared logic, `androidMain` for Android-specifics).

## 4. Module Responsibility Mapping

Ensure code is placed in the correct module based on the following taxonomy:

| Category | Core Module (`core/`) - Infra | Feature Module (`feature/`) - Domain | SDK Root Module |
| :--- | :--- | :--- | :--- |
| **Foundation** | `common`: Ktor client, WebSocket service, `AppError` pipe, `EncryptedSettings` base. | — | — |
| **Settings** | `settings`: Global config logic, Ktor network client, encrypted state management. | — | — |
| **Security** | `security`: Password policies, MFA state, Ktor network client, security error parsing. | — | — |
| **Identity Base** | — | `user`: Core identity models, base auth use cases, encrypted session storage. | — |
| **Client Identity** | — | `clientuser`: Multi-method auth (Email, Phone, Google), Decompose UI & navigation for client apps. | — |
| **Management Identity** | — | `managementuser`: Admin auth, session control, resource oversight, audit log, Decompose UI for admin apps. | — |
| **Alignment** | — | — | `bom`: Bill of Materials (Gradle platform) for SDK version consistency. |

**Strict Boundary:** Avoid moving app-specific logic from the `sample` module into SDK core modules. Core modules must remain generic, cross-platform, and configuration-driven.

## 5. Architectural Workflow

- **Errors:** To add an error, extend the relevant `AppError` hierarchy, update its `AppErrorParser`, and add localized strings to `strings.xml` in all locales.
- **Networking:** Feature-specific HTTP behavior must be an `HttpClientConfigPlugin`. Register these in the host app initialization.
- **UI & Navigation:** Use **Decompose** for logic/navigation and **Compose Multiplatform** for rendering. Ensure all UI is host-agnostic.
- **WebSockets:** Implement `WebSocketMessageHandler` in the feature module and register it via `webSocketService` in the host wiring phase.

## 6. Communication Protocol

- **Direct Execution:** Provide only the requested technical output.
- **No Meta-Talk:** Do not include personal remarks, "I hope this helps," or skip between unrelated topics.
- **Validation:** Before finishing, do a self-check: "Did I use any FQN?", "Did I add comments?", "Is this compatible with Wasm?", "Are components documented?".

---
*Refer to `AGENTS.md` for the full list of project standards.*