# samplemanagement

Administrative host applications that demonstrate how to wire **kmp-platform-sdk** modules for internal management staff. It utilizes the management-specific implementations for authentication, session control, and user profile oversight.

## Modules

*   **`samplemanagement:composeApp`** — Kotlin Multiplatform library with shared management UI, **[ManagementAppComponent]**, and platform entry points.
*   **`samplemanagement:androidApp`** — Android application hosting the management portal.

---

## What it provides

- **Root wiring**: **[ManagementAppComponent]** acts as the central DI container for the management application. It aggregates:
    - **Core**: Shared infrastructure components from `core:common`, `core:settings`, and `core:security`.
    - **Feature APIs**: Management-specific REST clients from `feature:settingsapi` and `feature:securityapi`.
    - **Management Domain**: **[ManagementUserComponent]** which provides administrative access to users, sessions, and identifiers.
- **Initialization**: Similar to the client application, it orchestrates the startup sequence via `init()`, registering administrative error parsers and WebSocket handlers.
- **Administrative Logic**: Uses specialized repositories (e.g., `ManagementUserRepository`) that allow staff to oversee and manage resources that are not accessible to standard users.

---

## Usage

1.  **Run**: Use the **`samplemanagement:androidApp`** run configuration for the Android management portal.
2.  **Architecture**: The management app follows the same manual DI pattern as the client app but uses `ManagementUserComponent` to access privileged APIs.

---

## Related module docs

| Module | Path |
| :--- | :--- |
| **core/common** | [README](../core/common/README.md) |
| **feature/managementuser** | [README](../feature/managementuser/README.md) |

---

[ManagementAppComponent]: composeApp/src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/samplemanagement/app/di/ManagementAppComponent.kt
[ManagementUserComponent]: ../feature/managementuser/src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/managementuser/di/ManagementUserComponent.kt
