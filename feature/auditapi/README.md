# feature/auditapi

Public API surface and networking implementation for **Audit Logs & Security Events**. This module provides the communication bridge to fetch administrative audit logs and event details using Ktor, along with Compose/Decompose UI components. It serves as the provider for audit-related management features within the KMP SDK.

---

## What it provides

### 1. Wiring (DI)
- **[AuditApiComponent]:** The primary entry point for the module. It assembles the network layer, repositories, use cases, and exposes root component factories for Compose/Decompose UI integration.

### 2. Networking
- **[ManagementAuditApi]:** The core interface defining REST operations for administrative audit logs (`GET_AUDIT_EVENTS`, `GET_AUDIT_EVENT`).
- **[KtorManagementAuditApi]:** A concrete implementation of [ManagementAuditApi] powered by the **Ktor HTTP Client**. It leverages foundation route constants (`ManagementAuditRoutes`) and the `callResult` utility to ensure standardized [AppResult] error handling.

### 3. UI & Navigation
- **[AuditApiRootComponent]:** Manages the Decompose navigation stack between audit event listing and event detail screens.
- **[AuditEventsScreen] & [AuditEventDetailScreen]:** Compose UI views for viewing paginated audit events and inspecting individual event details.

---

## Usage

### 1. Initialization
Construct the `AuditApiComponent` by providing a shared `HttpClient` (usually configured in `core/common`) along with required composite parsers for audit actions, resources, and metadata keys.

```kotlin
val auditApiComponent = AuditApiComponent(
    httpClient = commonComponent.httpClient,
    compositeAuditActionTypeParser = compositeAuditActionTypeParser,
    compositeAuditResourceTypeParser = compositeAuditResourceTypeParser,
    compositeAuditMetadataKeyParser = compositeAuditMetadataKeyParser
)
```

### 2. Creating UI Components
Use the component factory to create the root audit navigation tree for Decompose:

```kotlin
val auditRootComponent = auditApiComponent.createAuditApiRootComponent(
    componentContext = componentContext,
    onBack = { /* handle back */ }
)
```

---

[AuditApiComponent]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/auditapi/di/AuditApiComponent.kt
[ManagementAuditApi]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/auditapi/network/api/ManagementAuditApi.kt
[KtorManagementAuditApi]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/auditapi/network/api/KtorManagementAuditApi.kt
[AuditApiRootComponent]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/auditapi/ui/screen/root/AuditApiRootComponent.kt
[AuditEventsScreen]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/auditapi/ui/screen/events/AuditEventsScreen.kt
[AuditEventDetailScreen]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/auditapi/ui/screen/detail/AuditEventDetailScreen.kt
