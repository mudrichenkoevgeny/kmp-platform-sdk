# core/settings

Global application **settings** for client apps: encrypted local cache, REST load via shared foundation routes, **WebSocket-driven** updates, and a small use-case surface. Depends on `:core:common` only (not on `core/security` or `feature/*`).

## What it provides

- **Wiring:** [SettingsComponent] exposes storage, API, repository, refresh/get use cases, and [SettingsWebSocketMessageHandler] for host registration.
- **Persistence:** [GlobalSettingsStorage], [EncryptedGlobalSettingsStorage] on top of [EncryptedSettings] from `core/common`.
- **Network:** [GlobalSettingsApi], [KtorGlobalSettingsApi] for the global settings endpoint.
- **Domain:** [GlobalSettings] model and [toGlobalSettings] mapper from foundation wire types.
- **Repository:** [GlobalSettingsRepository], [GlobalSettingsRepositoryImpl] (in-memory state, mutex, socket subscription for `GLOBAL_SETTINGS_UPDATED`).
- **Use cases:** [GetGlobalSettingsUseCase], [RefreshGlobalSettingsUseCase].
- **Mocks:** [mockSettingsComponent] for tests and previews.

## Usage

- Add `:core:settings` (and ensure `:core:common` is on the classpath).
- Build [SettingsComponent] with the **same** [EncryptedSettings], shared Ktor `HttpClient`, and [WebSocketService] instances you pass into [CommonComponent].
- Call [CommonComponent.init] and register parsers as required by your app; settings module does not register parsers by itself.
- Register [SettingsWebSocketMessageHandler] in the combined handler list on [WebSocketService] (see `sample` for ordering together with common, security, and user handlers).
- Do not embed host-specific URLs or secrets inside this module; pass `baseUrl` and tokens through `core/common` as documented there.

[SettingsComponent]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/settings/di/SettingsComponent.kt
[SettingsWebSocketMessageHandler]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/settings/network/websockets/messagehandler/SettingsWebSocketMessageHandler.kt
[GlobalSettingsStorage]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/settings/storage/globalsettings/GlobalSettingsStorage.kt
[EncryptedGlobalSettingsStorage]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/settings/storage/globalsettings/EncryptedGlobalSettingsStorage.kt
[EncryptedSettings]: ../common/src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/storage/EncryptedSettings.kt
[GlobalSettingsApi]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/settings/network/api/globalsettings/GlobalSettingsApi.kt
[KtorGlobalSettingsApi]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/settings/network/api/globalsettings/KtorGlobalSettingsApi.kt
[GlobalSettings]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/settings/model/globalsettings/GlobalSettings.kt
[toGlobalSettings]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/settings/mapper/globalsettings/GlobalSettingsMapper.kt
[GlobalSettingsRepository]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/settings/repository/globalsettings/GlobalSettingsRepository.kt
[GlobalSettingsRepositoryImpl]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/settings/repository/globalsettings/GlobalSettingsRepositoryImpl.kt
[GetGlobalSettingsUseCase]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/settings/usecase/GetGlobalSettingsUseCase.kt
[RefreshGlobalSettingsUseCase]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/settings/usecase/RefreshGlobalSettingsUseCase.kt
[mockSettingsComponent]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/settings/mock/di/MockSettingsComponent.kt
[CommonComponent]: ../common/src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/di/CommonComponent.kt
[WebSocketService]: ../common/src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/network/websocket/service/WebSocketService.kt
