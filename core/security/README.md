# core/security

**Security settings** management for client applications, including backend-driven password policies, encrypted persistence, and real-time updates via WebSockets. Provides **password validation** against active policies and localized **security errors** (MFA, TOTP, Rate-limiting) via Compose resources.

---

## What it provides

### 1. Wiring (DI)
- **[SecurityComponent]:** The root entry point. Assembles internal modules and exposes `securitySettingsStorage` and `passwordPolicyValidator`.
- **Internal Modules:**
    - `SecurityStorageModule`: Binds [EncryptedSettings] to security-specific storage.
    - `SecurityRepositoryModule`: Coordinates API, Storage, and WebSockets.
    - `SecurityUseCaseModule`: Builds use cases on top of the repository.
    - `SecurityWebSocketModule`: Provides Ktor-backed API and WebSocket handlers.

### 2. Persistence & Repository
- **[SecuritySettingsRepository]:** Orchestrates security settings logic. Features:
    - **Encrypted Cache:** Uses [EncryptedSecuritySettingsStorage] via [SecuritySettingsStorage] port.
    - **Mutex Guarding:** Thread-safe in-memory state management.
    - **Reactive Updates:** Exposes a `Flow` of settings and automatically updates the cache when receiving `SECURITY_SETTINGS_UPDATED` frames via [WebSocketService].

### 3. Domain Use Cases
- **[ValidatePasswordUseCase]:** Validates candidate passwords against the current [PasswordPolicy] from the repository using the foundation-level [PasswordPolicyValidator]. Returns [SecurityError] on failure.
- **[RefreshSecuritySettingsUseCase]:** Manages manual REST-based updates of security metadata.

### 4. Networking
- **[SecuritySettingsApi]:** REST boundary for fetching password policies and security configs.
- **[SecurityWebSocketMessageHandler]:** Listens for security-domain events. Note that persistence is handled by the repository which subscribes to these events internally.

### 5. Error Handling
- **[SecurityError]:** Specialized [AppError] variants for security flows (MFA, TOTP, Policy violations). Only `PasswordPolicyUnavailable` is marked as retryable.
- **[SecurityErrorParser]:** An [AppErrorParser] that handles security-specific codes, including rate-limiting for OTP requests and MFA challenges.

### 6. Mocks
- **[SecurityComponentMock]:** Fully configured mock component for UI previews and unit testing, providing mocked storage, repositories, and APIs.

---

## Usage

### 1. Initialization
Construct [SecurityComponent] by providing core infrastructure dependencies:

```kotlin
val securityComponent = SecurityComponent(
    webSocketService = commonComponent.webSocketService,
    securitySettingsApi = securitySettingsApi,
    encryptedSettings = commonComponent.encryptedSettings,
    parentScope = appScope
)
```

### 2. Registration
Register the error parser and WebSocket handler during host application startup:

```kotlin
// Register error parser in CommonComponent.init
commonComponent.init(
    appErrorParserSpecificParsers = listOf(SecurityErrorParser)
)

// Register WebSocket handler
val securityHandler = securityComponent.securityWebSocketMessageHandler
webSocketService.updateWebSocketMessageHandlers(listOf(securityHandler))
```

### 3. Password Validation
```kotlin
val validatePassword = securityComponent.validatePasswordUseCase
val result = validatePassword("my_password_123")

if (result is AppResult.Error) {
    val error = result.error as? SecurityError
    // Handle policy violation or PasswordPolicyUnavailable
}
```

---

[SecurityComponent]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/security/di/SecurityComponent.kt
[SecuritySettingsRepository]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/security/repository/SecuritySettingsRepository.kt
[SecuritySettingsStorage]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/security/storage/securitysettings/SecuritySettingsStorage.kt
[EncryptedSecuritySettingsStorage]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/security/storage/securitysettings/EncryptedSecuritySettingsStorage.kt
[SecuritySettingsApi]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/security/network/securitysettings/SecuritySettingsApi.kt
[SecurityWebSocketMessageHandler]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/security/network/websocket/messagehandler/SecurityWebSocketMessageHandler.kt
[ValidatePasswordUseCase]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/security/usecase/ValidatePasswordUseCase.kt
[RefreshSecuritySettingsUseCase]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/security/usecase/RefreshSecuritySettingsUseCase.kt
[SecurityError]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/security/error/model/SecurityError.kt
[SecurityErrorParser]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/security/error/parser/SecurityErrorParser.kt
[SecurityComponentMock]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/security/mock/di/SecurityComponentMock.kt