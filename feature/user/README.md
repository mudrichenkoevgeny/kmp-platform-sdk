# feature/user

Kotlin Multiplatform feature module for **user identity, authentication flows, session-oriented HTTP behavior, and related UI** (Compose + Decompose). It depends on `core:common`, `core:settings`, and `core:security`; it must not embed host-app specifics—runtime wiring lives in the host (see `sample/*`).

## Host integration (summary)

1. **HTTP:** Pass [AuthHttpClientConfigPlugin](src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/network/httpclient/AuthHttpClientConfigPlugin.kt) into `CommonComponent` via `httpClientConfigPlugins` so the shared Ktor client attaches auth headers and token refresh behavior using your [AuthStorage](src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/storage/auth/AuthStorage.kt) implementation.
2. **Errors:** Register [UserErrorParser](src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/error/pasrer/UserErrorParser.kt) in `CommonComponent.init(...)` so user-domain error codes resolve to localized strings; unknown codes fall back to `CommonErrorParser`.
3. **WebSockets:** Register [UserWebSocketMessageHandler](src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/network/websocket/messagehandler/UserWebSocketMessageHandler.kt) with the app’s `WebSocketService` (alongside other handlers).
4. **DI:** Construct [UserComponent](src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/di/UserComponent.kt) with `CommonComponent`, `SettingsComponent`, `SecurityComponent`, `AuthStorage`, and platform [UserAuthServices](src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/auth/UserAuthServices.kt) (e.g. Google Sign-In on Android/Wasm).

## Package map

| Package | Role |
|--------|------|
| `...user.di` | [UserComponent](src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/di/UserComponent.kt) and internal modules that wire storage, Ktor APIs, repositories, and use cases. |
| `...user.auth` | [UserAuthServices](src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/auth/UserAuthServices.kt); platform `actual` aggregators (e.g. Android, Wasm, iOS stubs). |
| `...user.auth.google` | [GoogleAuthService](src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/auth/google/GoogleAuthService.kt) abstraction and platform implementations / disabled fallback. |
| `...user.model.*` | Domain models: auth payload, tokens, session, user profile identifiers, confirmation keys, auth settings, remote user configuration. |
| `...user.network.api.*` | Ktor-backed API facades: login, registration, password, refresh token, auth settings, session, user profile, security password/user-identifiers, user configuration. |
| `...user.network.auth` | Low-level auth configuration helpers and attributes used when installing client plugins. |
| `...user.network.httpclient` | [AuthHttpClientConfigPlugin](src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/network/httpclient/AuthHttpClientConfigPlugin.kt) and related Ktor setup. |
| `...user.network.websocket.messagehandler` | [UserWebSocketMessageHandler](src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/network/websocket/messagehandler/UserWebSocketMessageHandler.kt) for user-related socket event types. |
| `...user.repository.*` | Repositories orchestrating APIs, confirmation helpers, and storage (login, registration, password, refresh token, auth settings, user, security, session, confirmation). |
| `...user.storage.auth` | [AuthStorage](src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/storage/auth/AuthStorage.kt) contract for tokens and cached auth settings. |
| `...user.storage.user` | Encrypted persistence for user-scoped data (e.g. profile cache) via `EncryptedUserStorage`. |
| `...user.usecase.*` | Use cases invoked from UI/components: login (email/phone/Google), registration, password reset, refresh token, auth settings refresh, provider discovery, user configuration refresh. |
| `...user.mapper.*` | Mapping between DTOs / foundation types and feature models. |
| `...user.error.model` | [UserError](src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/error/model/UserError.kt) sealed hierarchy implementing `AppError` for client-side user errors. |
| `...user.error.naming` | [ClientUserErrorCodes](src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/error/naming/ClientUserErrorCodes.kt) for codes originating in the client. |
| `...user.error.pasrer` | [UserErrorParser](src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/error/pasrer/UserErrorParser.kt) (package name retains the historical `pasrer` spelling). |
| `...user.ui.screen.auth.*` | Decompose components, screen state, and Compose UI for login (welcome, email, phone), registration by email, reset password. |
| `...user.ui.component.*` | Reusable auth UI pieces (provider grid/items, legal footer). |
| `...user.mock.*` | Test/support fakes: [mockUserComponent](src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/mock/di/UserComponentMock.kt), mock auth storage, mock auth services, sample user models. |
| `...user.utils` | Small helpers such as [FieldValidator](src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/utils/FieldValidator.kt). |

## Source set notes

- **commonMain:** Shared logic, UI, and contracts.
- **androidMain:** Android credentials / Google auth wiring ([AndroidUserAuthServices](src/androidMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/auth/AndroidUserAuthServices.kt)).
- **wasmJsMain:** Browser-oriented Google auth interop and [WasmUserAuthServices](src/wasmJsMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/auth/WasmUserAuthServices.kt).
- **iosMain:** Placeholders / iOS-oriented auth services for future enablement.

## Resources

Compose Multiplatform resources for this module are generated with `publicResClass = true` and `packageOfResClass = io.github.mudrichenkoevgeny.kmp.feature.user` (see `build.gradle.kts`). String keys for user errors use the `error_user_*` prefix per project conventions.
