# core/storage

Storage settings and file storage infrastructure for KMP applications, featuring encrypted local caching, REST-based file operations via shared foundation routes, and real-time **WebSocket-driven** updates. This module maintains a slim footprint, depending strictly on `:core:common`.

---

## What it provides

### 1. Wiring (DI)
- **[StorageComponent]:** The central entry point. Assembles storage settings, file upload/download APIs, repositories, and UI components.
- **Internal Modules:**
    - `StorageApiModule`: Provides REST clients for storage settings and file upload/download.
    - `StorageRepositoryModule`: Binds repositories for storage settings and file management.
    - `StorageUseCaseModule`: Provides use cases for storage operations.
    - `StorageWebSocketsModule`: Handles network-level WebSocket registration for storage events.

### 2. Persistence & Repositories
- **[StorageSettingsRepository]:** Source of truth for storage configuration and limits with encrypted caching.
- **[StorageFileRepository]:** Handles file upload, download, deletion, and file metadata operations.

### 3. Mocks
- **[StorageComponentMock]:** Provides mocked implementations for testing and Compose previews.

---

## Usage

### 1. Initialization
Construct the [StorageComponent] by passing the core infrastructure shared with [CommonComponent]:

```kotlin
val storageComponent = StorageComponent(
    httpClient = commonComponent.httpClient,
    webSocketService = commonComponent.webSocketService,
    encryptedSettings = commonComponent.encryptedSettings,
    parentScope = appScope
)
```

---
[StorageComponent]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/storage/di/StorageComponent.kt
[StorageComponentMock]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/storage/mock/di/StorageComponentMock.kt
[StorageSettingsRepository]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/storage/repository/StorageSettingsRepository.kt
[StorageFileRepository]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/storage/repository/StorageFileRepository.kt
