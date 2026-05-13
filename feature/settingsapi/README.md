# feature/settings-api

Public API surface and networking implementation for **Global Settings**. This module provides the communication bridge to fetch application-level configurations and metadata using Ktor. It serves as the networking provider for settings-related features within the KMP SDK.

---

## What it provides

### 1. Wiring (DI)
- **[SettingsApiComponent]:** The primary entry point for the module. It assembles the network layer and exposes the [GlobalSettingsApi] for external use.
- **[SettingsNetworkModule]:** An internal module responsible for instantiating Ktor-backed network services and managing their dependencies.

### 2. Networking
- **[GlobalSettingsApi]:** The core interface defining REST operations for retrieving global application settings.
- **[KtorGlobalSettingsApi]:** A concrete implementation of [GlobalSettingsApi] powered by the **Ktor HTTP Client**. It leverages foundation route constants (`OpenGlobalSettingsRoutes`) and the `callResult` utility to ensure standardized [AppResult] error handling.

---

## Usage

### 1. Initialization
Construct the `SettingsApiComponent` by providing a shared `HttpClient` (usually configured in `core/common`) that includes the base URL and necessary interceptors.

```kotlin
val settingsApiComponent = SettingsApiComponent(
httpClient = commonComponent.httpClient
)

val globalSettingsApi = settingsApiComponent.globalSettingsApi
```

### 2. Fetching Global Settings
Use the API to retrieve the [GlobalSettingsPayload], which contains the current application configuration.

---

[SettingsApiComponent]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/settingsapi/di/SettingsApiComponent.kt
[SettingsNetworkModule]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/settingsapi/di/SettingsNetworkModule.kt
[KtorGlobalSettingsApi]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/settingsapi/network/globalsettings/KtorGlobalSettingsApi.kt
[GlobalSettingsApi]: ../../core/settings/src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/settings/network/globalsettings/GlobalSettingsApi.kt