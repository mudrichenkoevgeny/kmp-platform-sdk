# core/common

Base for all SDK modules: shared **Ktor HTTP client** bootstrap, **WebSocket** plumbing, **encrypted settings** abstraction, **device/platform** metadata, **error model and parsing**, lightweight **Compose** building blocks, and **Decompose** helpers. Does not depend on other in-repo `core/*` or `feature/*` modules.

## What it provides

- **Wiring:** [CommonComponent] assembles storage, platform services, HTTP client, WebSocket service, and error parsing; call [CommonComponent.init] after construction to register feature parsers and use [CommonComponent.appErrorParser].
- **HTTP client:** [HttpClientCommonConfig], [HttpClientConfigPlugin] for feature-specific Ktor tweaks; token integration via [AccessTokenProvider].
- **WebSockets:** [WebSocketService], [KtorWebSocketService], [WebSocketMessageHandler], [CommonWebSocketMessageHandler]; host apps register handlers with [WebSocketService.updateWebSocketMessageHandlers].
- **Errors:** [AppError], [CommonError], [CommonErrorParser], [AppErrorParser], [AppErrorParserBuilder], [AppErrorParserResolver].
- **Storage:** [EncryptedSettings], [CommonStorage] ([EncryptedCommonStorage]), [SettingsFactory] (platform factories).
- **Platform:** [DeviceInfo], [DeviceInfoProvider], [PlatformRepository], [ExternalLauncher] / [ExternalLauncherFactory].
- **Results:** [AppResult] and helpers in the same package.
- **UI (shared):** [FullscreenError], [FullscreenOverlayLoading], [Dimens], theme-related pieces under `ui/theme`.
- **Mocks / tests:** [MockEncryptedSettings], [MockWebSocketService], [mockCommonComponent], and related fakes under `mock/`.
- **Compose / DI locals:** [LocalCommonComponent], [LocalErrorParser] for passing the common graph into UI.

## Usage

- Add a dependency on `:core:common` (or align versions via the published BOM where applicable).
- Construct [CommonComponent] with runtime values: `encryptedSettings`, `deviceInfo`, `baseUrl`, optional `httpClientConfigPlugins`, `accessTokenProvider`, `appScope`, optional `platformContext`.
- Call [CommonComponent.init] with a list of feature-owned [AppErrorParser] implementations (e.g. security, user) before reading localized errors from [CommonComponent.appErrorParser].
- Pass feature [HttpClientConfigPlugin] instances from their owning modules into `httpClientConfigPlugins`.
- After [CommonComponent.init], install combined [WebSocketMessageHandler] lists on [CommonComponent.webSocketService].
- Feature modules must not hardcode host URLs or assume a specific app entry point; only `sample/*` should demonstrate full wiring.

[CommonComponent]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/di/CommonComponent.kt
[CommonComponent.init]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/di/CommonComponent.kt
[CommonComponent.appErrorParser]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/di/CommonComponent.kt
[HttpClientCommonConfig]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/network/httpclient/HttpClientCommonConfig.kt
[HttpClientConfigPlugin]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/network/httpclient/HttpClientConfigPlugin.kt
[AccessTokenProvider]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/network/provider/AccessTokenProvider.kt
[WebSocketService]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/network/websocket/service/WebSocketService.kt
[KtorWebSocketService]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/network/websocket/service/KtorWebSocketService.kt
[WebSocketMessageHandler]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/network/websocket/messagehandler/WebSocketMessageHandler.kt
[CommonWebSocketMessageHandler]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/network/websocket/messagehandler/CommonWebSocketMessageHandler.kt
[WebSocketService.updateWebSocketMessageHandlers]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/network/websocket/service/WebSocketService.kt
[AppError]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/error/model/AppError.kt
[CommonError]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/error/model/CommonError.kt
[CommonErrorParser]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/error/parser/CommonErrorParser.kt
[AppErrorParser]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/error/parser/AppErrorParser.kt
[AppErrorParserBuilder]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/error/parser/AppErrorParserBuilder.kt
[AppErrorParserResolver]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/error/parser/AppErrorParserResolver.kt
[EncryptedSettings]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/storage/EncryptedSettings.kt
[CommonStorage]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/storage/common/CommonStorage.kt
[EncryptedCommonStorage]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/storage/common/EncryptedCommonStorage.kt
[SettingsFactory]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/storage/SettingsFactory.kt
[DeviceInfo]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/platform/deviceinfo/model/DeviceInfo.kt
[DeviceInfoProvider]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/platform/deviceinfo/DeviceInfoProvider.kt
[PlatformRepository]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/repository/platform/PlatformRepository.kt
[ExternalLauncher]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/platform/externallauncher/ExternalLauncher.kt
[ExternalLauncherFactory]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/platform/externallauncher/ExternalLauncherFactory.kt
[AppResult]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/result/AppResult.kt
[FullscreenError]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/ui/component/error/FullscreenError.kt
[FullscreenOverlayLoading]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/ui/component/loading/FullscreenOverlayLoading.kt
[Dimens]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/ui/theme/Dimens.kt
[MockEncryptedSettings]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/mock/storage/MockEncryptedSettings.kt
[MockWebSocketService]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/mock/network/websockets/MockWebSocketService.kt
[mockCommonComponent]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/mock/di/CommonComponentMock.kt
[LocalCommonComponent]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/di/LocalCommonComponent.kt
[LocalErrorParser]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/di/LocalCommonComponent.kt
