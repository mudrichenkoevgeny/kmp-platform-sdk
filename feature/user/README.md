# feature/user

Kotlin Multiplatform feature module for user identity, authentication flows, session-oriented HTTP behavior, and related UI (Compose + Decompose). It depends on `core:common`, `core:settings`, and `core:security`. This module provides a complete identity solution, including multi-factor authentication (MFA), social login, and profile management.

## What it provides

### 1. Authentication & Identity
- **Multi-Factor Auth:** Support for Email + Password, Phone OTP (One-Time Password), and Google Sign-In.
- **Provider Discovery:** [GetAvailableUserAuthProvidersUseCase] checks backend settings to dynamically show available login methods.
- **Google Auth:** Platform-specific implementations using **Jetpack Credential Manager** (Android) and **JS Interop** (Wasm/Web).
- **Registration Flows:** Complete registration logic for email and phone, including confirmation code handling via [SendRegistrationConfirmationToEmailUseCase].

### 2. Session & Security
- **Token Management:** [AuthStorage] handles encrypted persistence of access and refresh tokens.
- **Auto-Refresh:** [RefreshTokenUseCase] integrates with the core network layer to transparently refresh sessions.
- **Password Recovery:** Complete flow for email-based reset via [ResetEmailPasswordComponent] and associated UseCases.
- **Account Lifecycle:** [RestoreUserUseCase] and [ScheduleUserDeletionUseCase] for managing account status.
- **Security API:** Dedicated [UserSecurityApi] for managing sensitive user security settings and verification.

### 3. Reactive Infrastructure
- **WebSocket Integration:** [UserWebSocketMessageHandler] listens for real-time identity updates, such as profile changes or session invalidation.
- **Single Source of Truth:** Repositories combine [EncryptedSettings] cache with remote API calls, exposing data via `StateFlow`.
- **Throttling:** [ConfirmationRepository] manages client-side cooldowns for sending verification codes (SMS/Email) using [ConfirmationType].

### 4. UI & Navigation
- **Decompose Routing:** [LoginRootComponent] manages a full navigation stack (**Welcome** -> **Login** -> **Registration** -> **Reset Password**).
- **Validation:** [FieldValidator] provides conservative client-side checks for emails and phone numbers.
- **Legal Integration:** Standardized [LegalFooter] for Privacy Policy and Terms of Service links.
- **UI Components:** Reusable [AuthProviderGrid] and [AuthProviderButton] for consistent social login presentation.

### 5. Network & API Facades
- **Ktor Implementation:** Feature-rich API layers including [KtorLoginApi], [KtorRegistrationApi], and [KtorSessionApi].
- **Modular Networking:** Separate modules for configuration, identifiers, and user security to ensure clean separation of concerns.
- **Error Handling:** Integrated [UserErrorParser] to map backend responses to domain-specific [UserError] models.

## Usage

### 1. Dependency & DI
The module is designed as a standalone feature requiring core dependencies to be injected. Initialize the UserComponent by providing the necessary collaborators:

```kotlin
val userComponent = UserComponent(
    commonComponent = commonComponent,
    settingsComponent = settingsComponent,
    securityComponent = securityComponent,
    authStorage = encryptedAuthStorage,
    authServices = platformAuthServices
)
```

### 2. System Initialization
To enable automatic token management, real-time state synchronization, and specialized error parsing, register the module components in your CommonComponent during application startup:

```kotlin
fun init() {
    commonComponent.httpClientConfigPlugins.add(
        userComponent.authHttpClientConfigPlugin
    )

    commonComponent.init(
        appErrorParserSpecificParsers = listOf(UserErrorParser)
    )

    commonComponent.webSocketService.updateWebSocketMessageHandlers(
        listOf(userComponent.userWebSocketMessageHandler)
    )
}
```

### 3. UI Integration (Decompose)
To launch the authentication flow (Welcome -> Login -> OTP), use the LoginRootComponent factory:

```kotlin
val loginRoot = LoginRootComponentImpl(
    componentContext = childContext("login_root"),
    settingsComponent = settingsComponent,
    securityComponent = securityComponent,
    userComponent = userComponent,
    onFinished = { }
)
```

---

## Package map

| Package | Role |
|:---|:---|
| `...user.auth` | [UserAuthServices] and [GoogleAuthService] — platform actuals (Android/JS/iOS). |
| `...user.di` | [UserComponent] and Dagger modules wiring Network, Repositories, and Storage. |
| `...user.error` | [UserError], [UserErrorParser], and [ClientUserErrorCodes] for typed handling. |
| `...user.model` | Domain entities: Session, Profile, and [ConfirmationKey] / [ConfirmationType]. |
| `...user.network.api` | Ktor implementations for Auth (Login/Reg), Session, and User Security APIs. |
| `...user.network.auth` | Low-level configuration: [AuthAttributes] and client setup helpers. |
| `...user.network.httpclient` | [AuthHttpClientConfigPlugin] — core bearer token & auto-refresh logic. |
| `...user.network.websocket` | [UserWebSocketMessageHandler] for reactive profile and session updates. |
| `...user.repository` | Repositories orchestrating APIs & storage (Login, Reg, ResetPassword, Session). |
| `...user.storage` | [AuthStorage] (tokens) and [UserStorage] (profile) with encrypted persistence. |
| `...user.ui.screen` | Decompose components & UI for Login (Email/Phone/Google) and Registration. |
| `...user.ui.component` | Reusable UI: [AuthProviderGrid], [AuthProviderButton], and [LegalFooter]. |
| `...user.usecase` | Atomic logic: [LoginByEmailUseCase], [RefreshTokenUseCase], [ResetEmailPasswordUseCase]. |
| `...user.mock` | Mocks for tests: [UserComponentMock], [UserAuthServicesMock], and mock repositories. |
| `...user.utils` | Internal helpers including [FieldValidator] for input logic. |

## Source set notes

- **commonMain:** Shared logic, UI, and contracts.
- **androidMain:** Android credentials / Google auth wiring ([AndroidUserAuthServices](src/androidMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/auth/AndroidUserAuthServices.kt)).
- **wasmJsMain:** Browser-oriented Google auth interop and [WasmUserAuthServices](src/wasmJsMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/auth/WasmUserAuthServices.kt).
- **iosMain:** Placeholders / iOS-oriented auth services for future enablement.

## Resources

Compose Multiplatform resources for this module are generated with `publicResClass = true` and `packageOfResClass = io.github.mudrichenkoevgeny.kmp.feature.user` (see `build.gradle.kts`). String keys for user errors use the `error_user_*` prefix per project conventions. Client-side input validation is handled by the [FieldValidator].

---

[UserComponent]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/di/UserComponent.kt
[UserAuthServices]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/auth/UserAuthServices.kt
[UserError]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/error/model/UserError.kt
[UserErrorParser]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/error/pasrer/UserErrorParser.kt
[AuthStorage]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/storage/auth/AuthStorage.kt
[UserStorage]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/storage/user/UserStorage.kt
[FieldValidator]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/utils/FieldValidator.kt
[AuthHttpClientConfigPlugin]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/network/httpclient/AuthHttpClientConfigPlugin.kt
[UserWebSocketMessageHandler]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/network/websocket/messagehandler/UserWebSocketMessageHandler.kt
[AuthProviderGrid]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/ui/component/auth/AuthProviderGrid.kt
[LegalFooter]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/feature/user/ui/component/legal/LegalFooter.kt