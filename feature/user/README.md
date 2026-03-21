# feature/user

User **authentication**, **session tokens**, **auth settings**, **user profile/cache**, and **Compose + Decompose** flows for sign-in, registration, and password recovery. Depends on [CommonComponent], [SettingsComponent], and [SecurityComponent]; extends the shared HTTP client with bearer auth and refresh, and plugs into the common error and WebSocket pipelines.

## What it provides

- **Wiring:** [UserComponent] takes `commonComponent`, `settingsComponent`, `securityComponent`, [AuthStorage] (typically [EncryptedAuthStorage] over shared [EncryptedSettings]), [UserAuthServices] (e.g. Google on each platform), and an optional parent coroutine scope. It builds internal [UserNetworkModule], [UserRepositoryModule], [UserUseCaseModule], and [UserStorageModule]. It also exposes `passwordPolicyValidator` from [SecurityComponent] for password validation aligned with security settings.
- **HTTP client:** [AuthHttpClientConfigPlugin] is an [HttpClientConfigPlugin] that installs [setupAuthConfig] (Ktor `Auth` bearer, token load/refresh against your `baseUrl`). Pass it into [CommonComponent] as `httpClientConfigPlugins` together with `accessTokenProvider` / storage aligned with [AuthStorage].
- **Network APIs:** Ktor clients for login, registration, refresh token, password, auth settings, user profile, session, user configuration, and security-related user endpoints — see classes under `network/api` (e.g. [KtorLoginApi], [KtorUserConfigurationApi]).
- **Repositories:** Login, registration, confirmation, refresh token, password, auth settings, and user repositories ([LoginRepositoryImpl], [AuthSettingsRepositoryImpl], [UserRepositoryImpl], and others wired in [UserRepositoryModule]).
- **Storage:** [UserStorage] backed by [EncryptedUserStorage]; tokens and auth state via [AuthStorage] / [EncryptedAuthStorage].
- **Use cases:** Email/phone/Google login, registration and confirmation sends, password reset flow, [RefreshAuthSettingsUseCase], [GetAvailableUserAuthProvidersUseCase], [RefreshUserConfigurationUseCase] (coordinates user configuration API with global, security, and auth settings caches).
- **WebSockets:** [UserWebSocketMessageHandler] handles user-domain socket event types (e.g. unauthorized, auth settings updated); register it with [WebSocketService.updateWebSocketMessageHandlers] alongside common/settings/security handlers.
- **Errors:** [UserError] (client- and server-shaped `AppError` variants) and [UserErrorParser] (`AppErrorParser` with Compose `stringResource`; unknown codes delegate to [CommonErrorParser]).
- **UI:** Decompose graph for the auth stack, e.g. [LoginRootComponent] / [LoginRootComponentImpl] and screens under `ui/screen/auth` (welcome, email/phone login, registration, confirmations). Uses module `composeResources` for strings.
- **Platform auth:** [UserAuthServices] exposes optional [GoogleAuthService]; concrete implementations live in `androidMain`, `wasmJsMain`, and `iosMain` (e.g. [AndroidUserAuthServices], [WasmUserAuthServices], [IosUserAuthServices]).
- **Mocks:** [UserAuthServicesMock] and related test doubles under `mock/`.

## Usage

- Add a dependency on `:feature:user` (or align via the published BOM).
- Construct [EncryptedAuthStorage] (or another [AuthStorage]) with the **same** [EncryptedSettings] instance you pass to [CommonComponent].
- Create [AuthHttpClientConfigPlugin] with runtime `baseUrl` and that storage; pass it into [CommonComponent] `httpClientConfigPlugins`. Ensure [CommonComponent] `accessTokenProvider` matches your auth storage if the rest of the stack reads tokens from it.
- Build [UserComponent] after [SettingsComponent] and [SecurityComponent], reusing `commonComponent.httpClient` and `commonComponent.webSocketService`.
- In the host `init` path: include [UserErrorParser] in `CommonComponent.init(...)` and add [userWebSocketMessageHandler] to the handler list on [WebSocketService].
- For UI, call [UserComponent.createLoginRootDialogComponent] (or embed the same graph your app needs) with a Decompose [ComponentContext].
- Do not hardcode backend URLs inside this module; the host supplies `baseUrl` and OAuth client IDs (see [sample README](../sample/README.md)).

## Related module docs

- [core/common](../core/common/README.md)
- [core/settings](../core/settings/README.md)
- [core/security](../core/security/README.md)

[CommonComponent]: ../core/common/src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/di/CommonComponent.kt
[SettingsComponent]: ../core/settings/src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/settings/di/SettingsComponent.kt
[SecurityComponent]: ../core/security/src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/security/di/SecurityComponent.kt
[EncryptedSettings]: ../core/common/src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/storage/EncryptedSettings.kt
[HttpClientConfigPlugin]: ../core/common/src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/network/httpclient/HttpClientConfigPlugin.kt
[WebSocketService.updateWebSocketMessageHandlers]: ../core/common/src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/network/websocket/service/WebSocketService.kt
[CommonErrorParser]: ../core/common/src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/error/parser/CommonErrorParser.kt

[UserComponent]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/di/UserComponent.kt
[UserNetworkModule]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/di/UserNetworkModule.kt
[UserRepositoryModule]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/di/UserRepositoryModule.kt
[UserUseCaseModule]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/di/UserUseCaseModule.kt
[UserStorageModule]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/di/UserStorageModule.kt

[AuthHttpClientConfigPlugin]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/network/httpclient/AuthHttpClientConfigPlugin.kt
[setupAuthConfig]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/network/httpclient/HttpClientAuthConfig.kt
[KtorLoginApi]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/network/api/auth/login/KtorLoginApi.kt
[KtorUserConfigurationApi]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/network/api/configuration/KtorUserConfigurationApi.kt

[LoginRepositoryImpl]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/repository/auth/login/LoginRepositoryImpl.kt
[AuthSettingsRepositoryImpl]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/repository/auth/settings/AuthSettingsRepositoryImpl.kt
[UserRepositoryImpl]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/repository/user/UserRepositoryImpl.kt

[UserStorage]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/storage/user/UserStorage.kt
[EncryptedUserStorage]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/storage/user/EncryptedUserStorage.kt
[AuthStorage]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/storage/auth/AuthStorage.kt
[EncryptedAuthStorage]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/storage/auth/EncryptedAuthStorage.kt

[RefreshAuthSettingsUseCase]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/usecase/auth/settings/RefreshAuthSettingsUseCase.kt
[GetAvailableUserAuthProvidersUseCase]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/usecase/auth/settings/GetAvailableUserAuthProvidersUseCase.kt
[RefreshUserConfigurationUseCase]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/usecase/configuration/RefreshUserConfigurationUseCase.kt

[UserWebSocketMessageHandler]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/network/websocket/messagehandler/UserWebSocketMessageHandler.kt
[UserError]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/error/model/UserError.kt
[UserErrorParser]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/error/pasrer/UserErrorParser.kt

[LoginRootComponent]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/ui/screen/auth/login/root/LoginRootComponent.kt
[LoginRootComponentImpl]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/ui/screen/auth/login/root/LoginRootComponentImpl.kt

[UserAuthServices]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/auth/UserAuthServices.kt
[GoogleAuthService]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/auth/google/GoogleAuthService.kt
[AndroidUserAuthServices]: src/androidMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/auth/AndroidUserAuthServices.kt
[WasmUserAuthServices]: src/wasmJsMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/auth/WasmUserAuthServices.kt
[IosUserAuthServices]: src/iosMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/auth/IosUserAuthServices.kt

[UserAuthServicesMock]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/mock/auth/UserAuthServicesMock.kt
