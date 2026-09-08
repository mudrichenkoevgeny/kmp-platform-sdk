package io.github.mudrichenkoevgeny.kmp.core.security.di

import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.service.WebSocketService
import io.github.mudrichenkoevgeny.kmp.core.common.storage.EncryptedSettings
import io.github.mudrichenkoevgeny.kmp.core.security.network.securitysettings.OpenSecuritySettingsApi
import io.github.mudrichenkoevgeny.shared.foundation.core.security.passwordpolicy.validator.PasswordPolicyValidator
import io.github.mudrichenkoevgeny.shared.foundation.core.security.passwordpolicy.validator.PasswordPolicyValidatorImpl
import io.github.mudrichenkoevgeny.kmp.core.security.storage.securitysettings.OpenSecuritySettingsStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/**
 * Root wiring component for `core/security`.
 *
 * Assembles storage and password policy validation. Exposes:
 * - [OpenSecuritySettingsStorage] (`securitySettingsStorage`)
 * - [PasswordPolicyValidator] (`passwordPolicyValidator`)
 *
 * Constructor dependencies:
 * - [EncryptedSettings]: backing store for encrypted security settings persistence.
 * - `parentScope`: optional scope for repository coroutines; if null, a supervisor scope on the default dispatcher is created.
 */
class SecurityComponent(
    webSocketService: WebSocketService,
    openSecuritySettingsApi: OpenSecuritySettingsApi,
    encryptedSettings: EncryptedSettings,
    parentScope: CoroutineScope? = null
) {
    private val componentScope = parentScope
        ?: CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val storageModule by lazy {
        SecurityStorageModule(
            encryptedSettings
        )
    }
    val securitySettingsStorage get() = storageModule.openSecuritySettingsStorage

    val passwordPolicyValidator: PasswordPolicyValidator by lazy {
        PasswordPolicyValidatorImpl()
    }

    private val repositoryModule by lazy {
        SecurityRepositoryModule(
            openSecuritySettingsApi = openSecuritySettingsApi,
            openSecuritySettingsStorage = securitySettingsStorage,
            webSocketService = webSocketService,
            repositoryScope = componentScope
        )
    }
    val securitySettingsRepository get() = repositoryModule.openSecuritySettingsRepository

    private val useCaseModule by lazy {
        SecurityUseCaseModule(
            openSecuritySettingsRepository = securitySettingsRepository,
            passwordPolicyValidator = passwordPolicyValidator
        )
    }
    val refreshSecuritySettingsUseCase get() = useCaseModule.refreshOpenSecuritySettingsUseCase
    val validatePasswordUseCase get() = useCaseModule.validatePasswordUseCase

    private val webSocketsModule by lazy {
        SecurityWebSocketModule()
    }

    val securityWebSocketMessageHandler get() = webSocketsModule.securityWebSocketMessageHandler
}