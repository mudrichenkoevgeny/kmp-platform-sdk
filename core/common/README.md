# core/common

Base for all SDK modules: shared **Ktor HTTP client** bootstrap, **WebSocket** infrastructure with lifecycle management, **encrypted settings** abstraction, **platform** metadata, **error modeling and parsing**, and common **Compose** building blocks. This module serves as the foundation for the KMP SDK and does not depend on other in-repo `core/*` or `feature/*` modules.

## What it provides

### 1. Wiring & DI
- **[CommonComponent]:** The root assembly point for common infrastructure. Manages the lifecycle of networking, storage, and platform modules.
- **[EncryptedSettingsComponent]:** Decouples the core from platform-specific storage by using [SettingsFactory] and [getSettingsFactory].
- **Composition Locals:** [LocalCommonComponent] (via `CommonLocalDI.kt`) and `LocalErrorParser` for injecting the SDK graph and error handling into the UI layer.

### 2. Networking
- **HTTP Client:** Pre-configured Ktor client with support for [HttpClientConfigPlugin] extensions and [AccessTokenProvider] integration.
- **WebSockets:** [WebSocketService] (and [KtorWebSocketService]) manages connection lifecycles, pings, and automatic restarts when the access token changes.
- **Message Handling:** [WebSocketMessageHandler] interface with a [CommonWebSocketMessageHandler] for framework-level events (ping/pong, initialization). Uses [WebSocketMessageHandlerResult] to route frames.
- **Data Models:** [PagedResult] — a generic, [Serializable] model for paginated listings, shared across API, server, and client layers.

### 3. Error Handling & Result
- **Result Pattern:** [AppResult] sealed class (`Success` or `Error`) used as the standard return type for operations to ensure consistent error propagation.
- **Model:** [AppError] interface and [CommonError] sealed class. Includes [ErrorId] (a value class wrapping a `Uuid`) for stable tracking across layers.
- **Parsing:** [AppErrorParser] uses a **Chain of Responsibility** pattern. [AppErrorParserBuilder] allows registering feature-specific parsers that take priority over the [CommonErrorParser].
- **Compose Integration:** `AppErrorParser.parse` is a **@Composable** function, allowing direct resolution of string resources and dynamic arguments via `resolveLocalizedString` during error transformation.
- **Logging:** Extension `AppError.log()` integrates with **Kermit** to provide detailed diagnostics (ID, code, arguments, and stack traces for internal/network errors).

### 4. Storage
- **[EncryptedSettings]:** Platform-agnostic interface for key-value storage.
- **Android:** Backed by **Jetpack DataStore** and encrypted via **Google Tink** (AES-GCM) with keys stored in the Android Keystore.
- **Wasm:** Backed by browser `localStorage`.

### 5. Platform Abstractions
- **[ExternalLauncher]:** Unified API for opening URLs, `mailto:`, and local files (utilizing `FileProvider` on Android).
- **[DeviceInfoProvider]:** Collects platform-specific metadata (OS version, device model, app version) into [ClientDeviceInfo].
- **[PlatformRepository]:** Provides SDK layers with access to immutable device information.

### 6. UI & Theme
- **Components:** [FullscreenLoading] (with configurable delay) and [FullscreenError].
- **Theme:** Centralized [Dimens] and shared theme pieces for consistent multiplatform UI.

### 7. Mocks & Testing
- Extensive **@InternalApi** test doubles for deterministic previews and unit tests:
  - [EncryptedSettingsMock], [WebSocketServiceMock], [AccessTokenProviderMock], [AppErrorParserMock], and `CommonComponentMock`.


## Usage

### 1. Dependency
Add a dependency on `:core:common` in your Gradle build script.

### 2. Initialization
In your root DI component (e.g., `AppComponent`), initialize [CommonComponent] and call its `init` method to register feature-specific parsers and WebSocket handlers.

```kotlin
class AppComponent(
    platformContext: Any? = null,
    deviceInfo: ClientDeviceInfo,
    baseUrl: String,
    authServices: UserAuthServices
) {
    // 1. Prepare platform-specific encrypted settings
    private val encryptedSettingsComponent = EncryptedSettingsComponent(platformContext)
    private val encryptedSettings = encryptedSettingsComponent.encryptedSettings

    // 2. Assemble CommonComponent
    val commonComponent = CommonComponent(
        encryptedSettings = encryptedSettings,
        deviceInfo = deviceInfo,
        baseUrl = baseUrl,
        accessTokenProvider = authStorage,
        appScope = CoroutineScope(SupervisorJob() + Dispatchers.Default),
        platformContext = platformContext
    )

    // 3. Register feature-specific parsers and handlers
    fun init() {
        commonComponent.init(
            appErrorParserSpecificParsers = listOf(SecurityErrorParser, UserErrorParser)
        )

        val handlers = listOf(
            commonComponent.commonWebSocketMessageHandler,
            // ... feature-specific handlers
        )
        commonComponent.webSocketService.updateWebSocketMessageHandlers(handlers)
    }
}
```

### 3. Handling Results & Logging
```kotlin
val result: AppResult<UserData> = repository.getUser()

result.fold(
    onSuccess = { data -> /* process data */ },
    onFailure = { error ->
        error.log() // Detailed diagnostic log via Kermit
        handleError(error)
    }
)
```

### 4. Error Parsing in UI
Use `LocalCommonComponent` to access the parser within your `@Composable` functions. The parser automatically resolves strings from resources:

```kotlin
val parser = LocalCommonComponent.current.appErrorParser
val errorMessage = parser.parse(appError) ?: stringResource(Res.string.unknown_error)

FullscreenError(message = errorMessage)
```

---

[CommonComponent]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/di/CommonComponent.kt
[EncryptedSettingsComponent]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/di/EncryptedSettingsComponent.kt
[LocalCommonComponent]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/di/CommonLocalDI.kt
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
[EncryptedSettings]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/storage/SettingsFactory.kt
[SettingsFactory]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/storage/SettingsFactory.kt
[getSettingsFactory]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/storage/SettingsFactory.kt
[ExternalLauncher]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/platform/externallauncher/ExternalLauncher.kt
[DeviceInfoProvider]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/platform/deviceinfo/DeviceInfoProvider.kt
[PlatformRepository]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/repository/platform/PlatformRepository.kt
[FullscreenLoading]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/ui/component/loading/FullscreenLoading.kt
[FullscreenError]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/ui/component/error/FullscreenError.kt
[Dimens]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/ui/theme/Dimens.kt
[EncryptedSettingsMock]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/mock/storage/EncryptedSettingsMock.kt
[WebSocketServiceMock]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/mock/network/websocket/service/WebSocketServiceMock.kt
[AccessTokenProviderMock]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/mock/network/provider/AccessTokenProviderMock.kt
[AppErrorParserMock]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/mock/error/parser/AppErrorParserMock.kt