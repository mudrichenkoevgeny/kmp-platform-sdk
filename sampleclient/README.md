# sample

Reference host applications that demonstrate how to wire **kmp-platform-sdk** modules for Android and Wasm (browser) using a manual DI pattern (Component-based).

## Modules

*   **`sample:composeApp`** — Kotlin Multiplatform library with shared Compose UI, **[ClientAppComponent]**, Decompose screens, and platform entry points (`wasmJs`, **`android`**).
*   **`sample:androidApp`** — Android application that depends on `composeApp` and hosts **[MainActivity]**.

---

## What it provides

- **Root wiring**: **[ClientAppComponent]** acts as the central DI container. It aggregates:
    - **Core**: **[EncryptedSettingsComponent]** and **[CommonComponent]** (provides `HttpClient` and `WebSocketService`).
    - **Feature APIs**: `SettingsApiComponent` and `SecurityApiComponent` for network communication.
    - **Domain Components**: **[SettingsComponent]**, **[SecurityComponent]**, and **[ClientUserComponent]** for business logic and state.
- **Initialization**: **[ClientAppComponent.init]** orchestrates the startup sequence:
    - Attaches `authHttpClientConfigPlugin` to the Ktor pipeline.
    - Registers domain-specific error parsers (**SecurityErrorParser**, **UserErrorParser**).
    - Installs a combined list of **WebSocket handlers** from all modules.
- **Startup data**: Parallel refresh of configuration via **[SyncDataUseCase]**. Android and Wasm targets trigger `refreshUserConfigurationUseCase()` and establish WebSocket connections after successful initialization.
- **UI Architecture**: Uses **Decompose** for navigation and lifecycle management. **[RootContent]** provides:
    - A **splash screen** during the initialization phase.
    - `CompositionLocalProvider` for shared components (`LocalCommonComponent`, `LocalErrorParser`, `LocalClientAppComponent`).
    - Hosting for **[MainScreen]** or auth flows.
- **Platform Hosts**:
    - **Android**: **[AndroidApp]** initializes **[ClientAppComponent]** with `BuildConfig.BASE_URL`, **[AndroidDeviceInfoProvider]**, and **[AndroidUserAuthServices]**.
    - **Wasm**: **[main]** builds the component using **[WasmDeviceInfoProvider]** and **[WasmUserAuthServices]**, running the app in a browser viewport.

---

## Configuration

**`composeApp`** uses the `buildConfig` plugin to manage environment-specific variables. Key fields like `BASE_URL`, `GOOGLE_WEB_CLIENT_ID`, and `APP_VERSION` are defined in **[composeApp build.gradle.kts]**.

> The sample environment is selected via the `app.env` property (supporting `dev`, `test`, or `prod` blocks).

---

## Usage

1.  **Run**: Use the **`sample:androidApp`** run configuration for Android, or the `wasmJsBrowserRun` task for the Web version.
2.  **Reference**: To integrate the SDK into your own project, mirror the pattern in **[ClientAppComponent]**:
    *   Initialize core components first.
    *   Link feature API components to the shared `HttpClient`.
    *   Pass shared storage and network objects to domain components.
    *   Call `init()` to wire the error and WebSocket pipelines before launching the UI.

---

## Related module docs

| Module | Path |
| :--- | :--- |
| **core/common** | [README](../core/common/README.md) |
| **core/settings** | [README](../core/settings/README.md) |
| **core/security** | [README](../core/security/README.md) |
| **feature/settingsapi** | [README](../feature/settingsapi/README.md) |
| **feature/securityapi** | [README](../feature/securityapi/README.md) |
| **feature/clientuser** | [README](../feature/clientuser/README.md) |

---

[ClientAppComponent]: composeApp/src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/sampleclient/app/di/ClientAppComponent.kt
[EncryptedSettingsComponent]: ../core/common/src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/di/EncryptedSettingsComponent.kt
[CommonComponent]: ../core/common/src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/di/CommonComponent.kt
[SettingsComponent]: ../core/settings/src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/settings/di/SettingsComponent.kt
[SecurityComponent]: ../core/security/src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/security/di/SecurityComponent.kt
[ClientUserComponent]: ../feature/clientuser/src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/clientuser/di/ClientUserComponent.kt

[ClientAppComponent.init]: composeApp/src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/sampleclient/app/di/ClientAppComponent.kt
[SyncDataUseCase]: composeApp/src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/sampleclient/app/usecase/SyncDataUseCase.kt

[RootContent]: composeApp/src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/sampleclient/app/ui/root/RootContent.kt
[MainScreen]: composeApp/src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/sampleclient/app/ui/screen/main/MainScreen.kt

[AndroidApp]: androidApp/src/main/kotlin/io/github/mudrichenkoevgeny/kmp/sampleclient/android/AndroidApp.kt
[MainActivity]: androidApp/src/main/kotlin/io/github/mudrichenkoevgeny/kmp/sampleclient/android/MainActivity.kt
[AndroidDeviceInfoProvider]: ../core/common/src/androidMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/platform/deviceinfo/AndroidDeviceInfoProvider.kt
[AndroidUserAuthServices]: ../feature/user/src/androidMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/auth/AndroidUserAuthServices.kt

[main]: composeApp/src/wasmJsMain/kotlin/io/github/mudrichenkoevgeny/kmp/sampleclient/app/main.kt
[WasmDeviceInfoProvider]: ../core/common/src/wasmJsMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/platform/deviceinfo/WasmDeviceInfoProvider.kt
[WasmUserAuthServices]: ../feature/user/src/wasmJsMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/auth/WasmUserAuthServices.kt

[composeApp build.gradle.kts]: composeApp/build.gradle.kts
