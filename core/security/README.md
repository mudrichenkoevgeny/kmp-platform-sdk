# core/security

**Security settings** for client apps (password policy from the backend), encrypted cache, REST + WebSocket refresh paths, **password validation**, and localized **security errors** via Compose resources. Depends on `:core:common` only.

## What it provides

- **Wiring:** [SecurityComponent] exposes storage, API, repository, `PasswordPolicyValidator` (from shared foundation), refresh and validate use cases, and [SecurityWebSocketMessageHandler].
- **Persistence:** [SecuritySettingsStorage], [EncryptedSecuritySettingsStorage].
- **Network:** [SecuritySettingsApi], [KtorSecuritySettingsApi].
- **Domain:** [SecuritySettings] (wraps foundation password policy model), mapper [toSecuritySettings].
- **Repository:** [SecuritySettingsRepository], [SecuritySettingsRepositoryImpl] (same layering pattern as settings: mutex, cache, `SECURITY_SETTINGS_UPDATED` subscription).
- **Validation:** [ValidatePasswordUseCase] combines repository policy with [PasswordPolicyValidator] (foundation implementation provided by the component).
- **Errors:** [SecurityError], [SecurityErrorCodes], [SecurityErrorParser] (register with [CommonComponent.init] in the host).
- **Mocks:** [mockSecurityComponent].

## Usage

- Add `:core:security` and ensure `:core:common` is available.
- Construct [SecurityComponent] with shared [EncryptedSettings], the same Ktor `HttpClient` instance as [CommonComponent], and [WebSocketService] aligned with [CommonComponent].
- Register [SecurityErrorParser] in `appErrorParserSpecificParsers` when calling [CommonComponent.init].
- Register [SecurityWebSocketMessageHandler] together with other module handlers on [WebSocketService].
- Map password validation failures using [SecurityError] types; unknown parser codes should still fall back via [CommonErrorParser] as documented in `core/common`.

[SecurityComponent]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/security/di/SecurityComponent.kt
[SecurityWebSocketMessageHandler]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/security/network/websocket/messagehandler/SecurityWebSocketMessageHandler.kt
[SecuritySettingsStorage]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/security/storage/securitysettings/SecuritySettingsStorage.kt
[EncryptedSecuritySettingsStorage]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/security/storage/securitysettings/EncryptedSecuritySettingsStorage.kt
[SecuritySettingsApi]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/security/network/api/securitysettings/SecuritySettingsApi.kt
[KtorSecuritySettingsApi]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/security/network/api/securitysettings/KtorSecuritySettingsApi.kt
[SecuritySettings]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/security/model/securitysettings/SecuritySettings.kt
[toSecuritySettings]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/security/mapper/securitysettings/SecuritySettingsMapper.kt
[SecuritySettingsRepository]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/security/repository/securitysettings/SecuritySettingsRepository.kt
[SecuritySettingsRepositoryImpl]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/security/repository/securitysettings/SecuritySettingsRepositoryImpl.kt
[ValidatePasswordUseCase]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/security/usecase/ValidatePasswordUseCase.kt
[SecurityError]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/security/error/model/SecurityError.kt
[SecurityErrorCodes]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/security/error/naming/SecurityErrorCodes.kt
[SecurityErrorParser]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/security/error/parser/SecurityErrorParser.kt
[mockSecurityComponent]: src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/security/mock/di/MockSecurityComponent.kt
[EncryptedSettings]: ../common/src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/storage/EncryptedSettings.kt
[WebSocketService]: ../common/src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/network/websocket/service/WebSocketService.kt
[CommonComponent]: ../common/src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/di/CommonComponent.kt
[CommonComponent.init]: ../common/src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/di/CommonComponent.kt
[CommonErrorParser]: ../common/src/commonMain/kotlin/io/github/mudrichenkoevgeny/kmp/core/common/error/parser/CommonErrorParser.kt
