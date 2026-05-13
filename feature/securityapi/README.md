# feature/securityapi

Public API surface and networking implementation for **Security Settings**. This module provides the communication bridge to fetch backend-driven security configurations, such as password policies and MFA requirements, using Ktor. It acts as the networking provider for security-related features in the KMP SDK.

---

## What it provides

### 1. Wiring (DI)
- **[SecurityApiComponent]:** The root wiring point for the module. It assembles the network layer and exposes the [SecuritySettingsApi] for external consumption.
- **[SecurityNetworkModule]:** An internal module that manages the instantiation of Ktor-backed network services.

### 2. Networking
- **[SecuritySettingsApi]:** The primary interface for security-related REST operations.
- **[KtorSecuritySettingsApi]:** A concrete implementation of [SecuritySettingsApi] using the **Ktor HTTP Client**. It utilizes centralized route constants (`OpenSecuritySettingsRoutes`) and the `callResult` utility for consistent [AppResult] wrapping.

---

## Usage

### 1. Initialization
The `SecurityApiComponent` requires a pre-configured `HttpClient` (typically provided by `core/common`) to handle base URLs, serialization, and authentication headers.

```kotlin
val securityApiComponent = SecurityApiComponent(
    httpClient = commonComponent.httpClient
)

val securitySettingsApi = securityApiComponent.securitySettingsApi
```

### 2. Fetching Security Settings
Use the exposed API to retrieve [SecuritySettingsPayload], which contains policies and configuration metadata.

---
[SecurityApiComponent]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/securityapi/di/SecurityApiComponent.kt
[SecurityNetworkModule]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/securityapi/di/SecurityNetworkModule.kt
[KtorSecuritySettingsApi]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/securityapi/network/securitysettings/KtorSecuritySettingsApi.kt
[SecuritySettingsApi]: ../../core/security/src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/security/network/securitysettings/SecuritySettingsApi.kt