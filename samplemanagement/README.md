# samplemanagement

Administrative host applications that demonstrate how to wire **kmp-platform-sdk** modules for internal management staff. It utilizes the management-specific implementations for authentication, session control, resource oversight, and user profile management.

## Modules

*   **`samplemanagement:composeApp`** — Kotlin Multiplatform library with shared management UI, **[ManagementAppComponent]**, and platform entry points (`wasmJs`, `android`).
*   **`samplemanagement:androidApp`** — Android application hosting the management portal.

---

## What it provides

- **Root wiring**: **[ManagementAppComponent]** acts as the central DI container for the management application. It aggregates:
    - **Core**: Shared infrastructure components from `core:common`, `core:settings`, and `core:security`.
    - **Management Domain**: **[ManagementUserComponent]** which provides administrative access to users, sessions, identifiers, and audit logs.
- **Initialization**: Orchestrates the startup sequence via `init()`, registering administrative error parsers (`UserErrorParser`, `SecurityErrorParser`) and WebSocket message handlers.
- **Administrative Logic**: Uses specialized repositories (e.g., `ManagementUserRepository`, `ManagementSessionRepository`, `ManagementAuditRepository`) that allow staff to oversee and manage resources that are not accessible to standard users.
- **Tab Navigation**:
    - **Primary Tab**: **Management** (`Config.Management`, path `/`) — Main administrative portal dashboard (`ManagementMainNavigation.Main`).
    - **Secondary Tab**: **User** (`Config.User`, path `/user`) — Staff self-service profile and security settings (`ClientUserNavigation.Main`).
- **Universal Deep Linking**: Implements `ManagementAppDeepLinkParser` and `ManagementAppDeepLinkRoute` to handle cross-platform routing (`/users/*`, `/settings/*`, `/audit/*`, `/sessions/*`, `/identifiers/*`, `/user/*`) with authentication guards and tab selection.
- **Wasm Browser History Integration**: `SetupBrowserHistory(mainComponent, baseUrl)` automatically seeds parent path chains (`getAncestorPaths`) into native `window.history` on direct deep link entry, maintaining consistent behavior between the UI Back button and the Browser native Back button. Dynamically tracks state parameters (`totp_step`, `login_by_totp_mode`, `unlock_method`).

---

## Usage

1.  **Run**: Use the **`samplemanagement:androidApp`** run configuration for the Android management portal, or `wasmJsBrowserRun` task for the Web version.
2.  **Architecture**: The management app follows the same manual DI pattern as the client app but uses `ManagementUserComponent` to access privileged APIs.

---

## Related module docs

| Module | Path |
| :--- | :--- |
| **core/common** | [README](../core/common/README.md) |
| **core/settings** | [README](../core/settings/README.md) |
| **core/security** | [README](../core/security/README.md) |
| **feature/user** | [README](../feature/user/README.md) |
| **feature/managementuser** | [README](../feature/managementuser/README.md) |

---

[ManagementAppComponent]: composeApp/src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/samplemanagement/app/di/ManagementAppComponent.kt
[ManagementUserComponent]: ../feature/managementuser/src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/managementuser/di/ManagementUserComponent.kt
