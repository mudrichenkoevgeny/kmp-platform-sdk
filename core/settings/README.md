# core/settings

Global application **settings** for client apps, featuring encrypted local caching, REST-based loading via shared foundation routes, and real-time **WebSocket-driven** updates. This module maintains a slim footprint, depending strictly on `:core:common`.

---

## What it provides

### 1. Wiring (DI)
- **[SettingsComponent]:** The central entry point. Assembles the storage layer and repository, exposing `globalSettingsStorage` and associated use cases.
- **Internal Modules:**
    - `SettingsStorageModule`: Binds [EncryptedSettings] to security-hardened global storage.
    - `SettingsRepositoryModule`: Links API, Storage, and WebSockets into the repository implementation.
    - `SettingsUseCaseModule`: Provides thin facades for the application layer.
    - `SettingsWebSocketsModule`: Handles network-level WebSocket registration.

### 2. Persistence & Repository
- **[GlobalSettingsRepository]:** The source of truth for app configuration. Features:
    - **Encrypted Local Cache:** Uses [EncryptedGlobalSettingsStorage] to persist data securely.
    - **Concurrency Control:** Mutex-guarded in-memory state to ensure consistency.
    - **Reactive Sync:** Subscribes to `GLOBAL_SETTINGS_UPDATED` events from [WebSocketService] to push updates to the UI instantly.
- **API:** [GlobalSettingsApi] defines the REST contract for manual settings retrieval.

### 3. Domain Use Cases
- **[GetGlobalSettingsUseCase]:** Retrieves the current settings from the repository (cache-first).
- **[RefreshGlobalSettingsUseCase]:** Forces a network refresh via REST to update the local state and persistence.

### 4. Networking
- **[SettingsWebSocketMessageHandler]:** Directs settings-domain WebSocket frames. While it marks frames as handled, the actual data persistence is managed reactively within the repository layer.

### 5. Mocks
- **[SettingsComponentMock]:** Provides mocked implementations of the API, Storage, and Repository for testing and Compose previews.

---

## Usage

### 1. Initialization
Construct the [SettingsComponent] by passing the core infrastructure shared with [CommonComponent]:

```kotlin
val settingsComponent = SettingsComponent(
    webSocketService = commonComponent.webSocketService,
    globalSettingsApi = globalSettingsApi,
    encryptedSettings = commonComponent.encryptedSettings,
    parentScope = appScope // Optional: Defaults to SupervisorJob + Default dispatcher
)
```

### 2. Registration
The settings module does not register parsers automatically. You must register the [SettingsWebSocketMessageHandler] in your host application:

```kotlin
// Register WebSocket handler alongside other core handlers
val settingsHandler = settingsComponent.settingsWebSocketMessageHandler
webSocketService.updateWebSocketMessageHandlers(
    listOf(
        commonHandler,
        securityHandler,
        settingsHandler // Handler for GLOBAL_SETTINGS_UPDATED
    )
)
```

### 3. Consuming Settings
```kotlin
val getSettings = settingsComponent.getGlobalSettingsUseCase
val result = getSettings()

result.fold(
    onSuccess = { settings ->
        // Use your GlobalSettings
    },
    onFailure = { error ->
        // Handle loading failure
    }
)
```

---
[SettingsComponent]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/settings/di/SettingsComponent.kt
[SettingsRepositoryModule]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/settings/di/SettingsRepositoryModule.kt
[SettingsStorageModule]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/settings/di/SettingsStorageModule.kt
[SettingsUseCaseModule]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/settings/di/SettingsUseCaseModule.kt
[SettingsWebSocketsModule]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/settings/di/SettingsWebSocketsModule.kt
[GlobalSettingsApi]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/settings/network/globalsettings/GlobalSettingsApi.kt
[SettingsWebSocketMessageHandler]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/settings/network/websockets/messagehandler/SettingsWebSocketMessageHandler.kt
[GlobalSettingsRepository]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/settings/repository/GlobalSettingsRepository.kt
[GlobalSettingsRepositoryImpl]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/settings/repository/GlobalSettingsRepositoryImpl.kt
[GlobalSettingsStorage]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/settings/storage/globalsettings/GlobalSettingsStorage.kt
[EncryptedGlobalSettingsStorage]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/settings/storage/globalsettings/EncryptedGlobalSettingsStorage.kt
[GetGlobalSettingsUseCase]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/settings/usecase/GetGlobalSettingsUseCase.kt
[RefreshGlobalSettingsUseCase]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/settings/usecase/RefreshGlobalSettingsUseCase.kt
[SettingsComponentMock]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/settings/mock/di/SettingsComponentMock.kt
[EncryptedSettings]: ../common/src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/storage/SettingsFactory.kt
[WebSocketService]: ../common/src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/network/websocket/service/WebSocketService.kt