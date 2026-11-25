# core

Unified foundational module for `kmp-platform-sdk`. Consolidates core infrastructure across four package-isolated domains (`core.common`, `core.settings`, `core.security`, `core.storage`): Ktor HTTP client bootstrap, WebSocket lifecycle management, encrypted settings, platform metadata, error modeling and parsing, Compose theme and UI building blocks, pagination, deep link contracts, global settings, password policies/MFA, and file storage.

---

## Package Boundaries

Internal isolation is enforced via Konsist (`ArchitectureTest`):
- `core.common` — pure leaf package; must not depend on `core.settings`, `core.security`, `core.storage`, or `feature/*`.
- `core.settings`, `core.security`, `core.storage` — depend only on `core.common` and must not cross-depend on each other.

---

## What it provides

### 1. Common (`io.github.mudrichenkoevgeny.kmp.core.common`)
- **Wiring & DI:** [CommonComponent], [EncryptedSettingsComponent] (using [SettingsFactory] and [getSettingsFactory]), [LocalCommonComponent] (`CommonLocalDI.kt`), and `LocalErrorParser`.
- **Networking:** Pre-configured Ktor client with [HttpClientConfigPlugin] and [AccessTokenProvider]; [WebSocketService] ([KtorWebSocketService]) with lifecycle management, pings, and token-aware reconnects; [WebSocketMessageHandler], [CommonWebSocketMessageHandler], and [WebSocketMessageHandlerResult].
- **Error Handling & Result:** [AppResult] (`Success` / `Error`), [AppError], [CommonError], [ErrorId], Chain of Responsibility [AppErrorParser] / [AppErrorParserBuilder] / [CommonErrorParser], `@Composable` error string resolution, and `AppError.log()` via Kermit.
- **Storage:** [EncryptedSettings] backed by Android Jetpack DataStore + Google Tink (AES-GCM) and Wasm `localStorage`.
- **Platform Abstractions:** [ExternalLauncher], [ClientDeviceInfoProvider], and [PlatformRepository].
- **UI & Theme:** [FullscreenLoading], [FullscreenError], [CoreDimens], and `CoreTheme` mapped to generated `PlatformDesignTokens`.
- **Listing & Pagination:** [PaginationState], [PagingFooter], and [OnBottomReached].
- **Navigation & Deep Linking Contracts:** [DeepLinkParser] and [UrlFormatter].
- **Mocks:** `@InternalApi` test doubles — [EncryptedSettingsMock], [WebSocketServiceMock], [AccessTokenProviderMock], [AppErrorParserMock], and [CommonComponentMock].

### 2. Settings (`io.github.mudrichenkoevgeny.kmp.core.settings`)
- **Wiring & DI:** [SettingsComponent] (`SettingsStorageModule`, `SettingsNetworkModule`, `SettingsRepositoryModule`, `SettingsUseCaseModule`, `SettingsWebSocketsModule`).
- **Persistence & Repository:** [OpenGlobalSettingsRepository] ([OpenGlobalSettingsRepositoryImpl]) backed by [EncryptedOpenGlobalSettingsStorage] ([OpenGlobalSettingsStorage]), mutex-guarded state, [OpenGlobalSettingsApi], and reactive `GLOBAL_SETTINGS_UPDATED` sync via [SettingsWebSocketMessageHandler].
- **Use Cases:** [GetOpenGlobalSettingsUseCase], [RefreshOpenGlobalSettingsUseCase], [ObserveOpenGlobalSettingsUseCase].
- **Mocks:** [SettingsComponentMock].

### 3. Security (`io.github.mudrichenkoevgeny.kmp.core.security`)
- **Wiring & DI:** [SecurityComponent] (`SecurityStorageModule`, `SecurityNetworkModule`, `SecurityRepositoryModule`, `SecurityUseCaseModule`, `SecurityWebSocketModule`).
- **Persistence & Repository:** [OpenSecuritySettingsRepository] ([OpenSecuritySettingsRepositoryImpl]) backed by [EncryptedOpenSecuritySettingsStorage] ([OpenSecuritySettingsStorage]), mutex-guarded state, [OpenSecuritySettingsApi], and reactive `SECURITY_SETTINGS_UPDATED` sync via [SecurityWebSocketMessageHandler].
- **Password Policy:** [PasswordPolicyValidator].
- **Use Cases:** [ValidatePasswordUseCase], [GetOpenSecuritySettingsUseCase], [RefreshOpenSecuritySettingsUseCase], [ObserveOpenSecuritySettingsUseCase].
- **Error Handling:** [SecurityError] and [SecurityErrorParser].
- **Mocks:** [SecurityComponentMock].

### 4. Storage (`io.github.mudrichenkoevgeny.kmp.core.storage`)
- **Wiring & DI:** [StorageComponent] (`StorageStorageModule`, `StorageNetworkModule`, `StorageRepositoryModule`, `StorageUseCaseModule`).
- **Persistence & Repositories:** [OpenStorageSettingsRepository] ([OpenStorageSettingsRepositoryImpl]), [ManagementStorageSettingsRepository] ([ManagementStorageSettingsRepositoryImpl]), and [StorageRepository] ([OpenStorageRepositoryImpl], [ManagementStorageRepositoryImpl], [SelfManagementStorageRepositoryImpl]).
- **File Uploader:** [DirectFileUploader] ([KtorDirectFileUploader]).
- **Use Cases:** [UploadFileUseCase], [GetFilesUseCase], [GetFileDetailUseCase], [DeleteFileUseCase], [DeleteFilesBatchUseCase], [GetManagementStorageSettingsUseCase], [RefreshManagementStorageSettingsUseCase], [UpdateManagementStorageSettingsUseCase], [ResetRemoteManagementStorageSettingsUseCase].
- **Mocks:** [StorageComponentMock].

---

## Usage

### 1. Dependency
Add `:core` (or `io.github.mudrichenkoevgeny:kmp-platform-sdk-core` via BOM) to your Gradle build script.

### 2. Initialization & Wiring
```kotlin
val encryptedSettingsComponent = EncryptedSettingsComponent(platformContext)
val encryptedSettings = encryptedSettingsComponent.encryptedSettings

val commonComponent = CommonComponent(
    encryptedSettings = encryptedSettings,
    clientDeviceInfo = clientDeviceInfo,
    baseUrl = baseUrl,
    accessTokenProvider = authStorage,
    appScope = appScope,
    platformContext = platformContext
)

val settingsComponent = SettingsComponent(
    webSocketService = commonComponent.webSocketService,
    httpClient = commonComponent.httpClient,
    encryptedSettings = encryptedSettings,
    parentScope = appScope
)

val securityComponent = SecurityComponent(
    webSocketService = commonComponent.webSocketService,
    httpClient = commonComponent.httpClient,
    encryptedSettings = encryptedSettings,
    parentScope = appScope
)

val storageComponent = StorageComponent(
    httpClient = commonComponent.httpClient,
    encryptedSettings = encryptedSettings,
    webSocketService = commonComponent.webSocketService,
    parentScope = appScope
)
```

### 3. Register Parsers & WebSocket Handlers
```kotlin
commonComponent.init(
    appErrorParserSpecificParsers = listOf(
        SecurityErrorParser,
        UserErrorParser
    )
)

commonComponent.webSocketService.updateWebSocketMessageHandlers(
    listOf(
        commonComponent.commonWebSocketMessageHandler,
        settingsComponent.settingsWebSocketMessageHandler,
        securityComponent.securityWebSocketMessageHandler,
        storageComponent.storageWebSocketMessageHandler
    )
)
```

### 4. Error Handling & Use Cases
```kotlin
val result = settingsComponent.getOpenGlobalSettingsUseCase()
result.fold(
    onSuccess = { settings -> handleSettings(settings) },
    onFailure = { error ->
        error.log()
        handleError(error)
    }
)
```

---

[CommonComponent]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/di/CommonComponent.kt
[EncryptedSettingsComponent]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/di/EncryptedSettingsComponent.kt
[LocalCommonComponent]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/di/CommonLocalDI.kt
[CommonComponentMock]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/mock/di/CommonComponentMock.kt
[AccessTokenProvider]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/network/provider/AccessTokenProvider.kt
[HttpClientConfigPlugin]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/network/httpclient/HttpClientConfigPlugin.kt
[WebSocketService]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/network/websocket/service/WebSocketService.kt
[KtorWebSocketService]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/network/websocket/service/KtorWebSocketService.kt
[WebSocketMessageHandler]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/network/websocket/messagehandler/WebSocketMessageHandler.kt
[CommonWebSocketMessageHandler]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/network/websocket/messagehandler/CommonWebSocketMessageHandler.kt
[WebSocketMessageHandlerResult]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/network/websocket/messagehandler/WebSocketMessageHandlerResult.kt
[AppError]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/error/model/AppError.kt
[CommonError]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/error/model/CommonError.kt
[ErrorId]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/error/model/ErrorId.kt
[AppResult]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/result/AppResult.kt
[AppErrorParser]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/error/parser/AppErrorParser.kt
[AppErrorParserBuilder]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/error/parser/AppErrorParserBuilder.kt
[CommonErrorParser]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/error/parser/CommonErrorParser.kt
[EncryptedSettings]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/storage/EncryptedSettings.kt
[SettingsFactory]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/storage/SettingsFactory.kt
[getSettingsFactory]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/storage/SettingsFactory.kt
[ExternalLauncher]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/platform/externallauncher/ExternalLauncher.kt
[ClientDeviceInfoProvider]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/platform/clientdeviceinfo/ClientDeviceInfoProvider.kt
[PlatformRepository]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/repository/platform/PlatformRepository.kt
[FullscreenLoading]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/ui/component/loading/FullscreenLoading.kt
[FullscreenError]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/ui/component/error/FullscreenError.kt
[CoreDimens]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/ui/theme/CoreDimens.kt
[EncryptedSettingsMock]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/mock/storage/EncryptedSettingsMock.kt
[WebSocketServiceMock]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/mock/network/websocket/service/WebSocketServiceMock.kt
[AccessTokenProviderMock]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/mock/network/provider/AccessTokenProviderMock.kt
[AppErrorParserMock]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/mock/error/parser/AppErrorParserMock.kt
[PaginationState]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/infrastructure/listing/PaginationState.kt
[PagingFooter]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/ui/component/listing/PagingFooter.kt
[OnBottomReached]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/ui/component/listing/PagingUtils.kt
[DeepLinkParser]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/navigation/deeplink/DeepLinkParser.kt
[UrlFormatter]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/navigation/deeplink/UrlFormatter.kt
[SettingsComponent]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/settings/di/SettingsComponent.kt
[OpenGlobalSettingsApi]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/settings/network/globalsettings/OpenGlobalSettingsApi.kt
[SettingsWebSocketMessageHandler]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/settings/network/websockets/messagehandler/SettingsWebSocketMessageHandler.kt
[OpenGlobalSettingsRepository]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/settings/repository/OpenGlobalSettingsRepository.kt
[OpenGlobalSettingsRepositoryImpl]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/settings/repository/OpenGlobalSettingsRepositoryImpl.kt
[OpenGlobalSettingsStorage]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/settings/storage/globalsettings/OpenGlobalSettingsStorage.kt
[EncryptedOpenGlobalSettingsStorage]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/settings/storage/globalsettings/EncryptedOpenGlobalSettingsStorage.kt
[GetOpenGlobalSettingsUseCase]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/settings/usecase/GetOpenGlobalSettingsUseCase.kt
[RefreshOpenGlobalSettingsUseCase]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/settings/usecase/RefreshOpenGlobalSettingsUseCase.kt
[ObserveOpenGlobalSettingsUseCase]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/settings/usecase/ObserveOpenGlobalSettingsUseCase.kt
[SettingsComponentMock]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/settings/mock/di/SettingsComponentMock.kt
[SecurityComponent]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/security/di/SecurityComponent.kt
[OpenSecuritySettingsRepository]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/security/repository/OpenSecuritySettingsRepository.kt
[OpenSecuritySettingsRepositoryImpl]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/security/repository/OpenSecuritySettingsRepositoryImpl.kt
[OpenSecuritySettingsStorage]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/security/storage/securitysettings/OpenSecuritySettingsStorage.kt
[EncryptedOpenSecuritySettingsStorage]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/security/storage/securitysettings/EncryptedOpenSecuritySettingsStorage.kt
[OpenSecuritySettingsApi]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/security/network/securitysettings/OpenSecuritySettingsApi.kt
[SecurityWebSocketMessageHandler]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/security/network/websocket/messagehandler/SecurityWebSocketMessageHandler.kt
[ValidatePasswordUseCase]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/security/usecase/ValidatePasswordUseCase.kt
[RefreshOpenSecuritySettingsUseCase]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/security/usecase/RefreshOpenSecuritySettingsUseCase.kt
[GetOpenSecuritySettingsUseCase]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/security/usecase/GetOpenSecuritySettingsUseCase.kt
[ObserveOpenSecuritySettingsUseCase]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/security/usecase/ObserveOpenSecuritySettingsUseCase.kt
[PasswordPolicyValidator]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/security/di/SecurityComponent.kt
[SecurityError]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/security/error/model/SecurityError.kt
[SecurityErrorParser]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/security/error/parser/SecurityErrorParser.kt
[SecurityComponentMock]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/security/mock/di/SecurityComponentMock.kt
[StorageComponent]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/storage/di/StorageComponent.kt
[StorageComponentMock]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/storage/mock/di/StorageComponentMock.kt
[OpenStorageSettingsRepository]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/storage/repository/open/OpenStorageSettingsRepository.kt
[OpenStorageSettingsRepositoryImpl]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/storage/repository/open/OpenStorageSettingsRepositoryImpl.kt
[ManagementStorageSettingsRepository]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/storage/repository/management/ManagementStorageSettingsRepository.kt
[ManagementStorageSettingsRepositoryImpl]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/storage/repository/management/ManagementStorageSettingsRepositoryImpl.kt
[StorageRepository]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/storage/repository/base/StorageRepository.kt
[OpenStorageRepositoryImpl]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/storage/repository/open/OpenStorageRepositoryImpl.kt
[ManagementStorageRepositoryImpl]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/storage/repository/management/ManagementStorageRepositoryImpl.kt
[SelfManagementStorageRepositoryImpl]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/storage/repository/management/SelfManagementStorageRepositoryImpl.kt
[DirectFileUploader]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/storage/uploader/DirectFileUploader.kt
[KtorDirectFileUploader]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/storage/uploader/KtorDirectFileUploader.kt
[UploadFileUseCase]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/storage/usecase/base/UploadFileUseCase.kt
[GetFilesUseCase]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/storage/usecase/base/GetFilesUseCase.kt
[GetFileDetailUseCase]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/storage/usecase/base/GetFileDetailUseCase.kt
[DeleteFileUseCase]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/storage/usecase/base/DeleteFileUseCase.kt
[DeleteFilesBatchUseCase]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/storage/usecase/base/DeleteFilesBatchUseCase.kt
[GetManagementStorageSettingsUseCase]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/storage/usecase/management/GetManagementStorageSettingsUseCase.kt
[RefreshManagementStorageSettingsUseCase]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/storage/usecase/management/RefreshManagementStorageSettingsUseCase.kt
[UpdateManagementStorageSettingsUseCase]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/storage/usecase/management/UpdateManagementStorageSettingsUseCase.kt
[ResetRemoteManagementStorageSettingsUseCase]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/storage/usecase/management/ResetRemoteManagementStorageSettingsUseCase.kt
