package io.github.mudrichenkoevgeny.kmp.core.security.di

import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.service.WebSocketService
import io.github.mudrichenkoevgeny.kmp.core.common.storage.EncryptedSettings
import io.github.mudrichenkoevgeny.shared.foundation.core.security.passwordpolicy.validator.PasswordPolicyValidator
import io.github.mudrichenkoevgeny.shared.foundation.core.security.passwordpolicy.validator.PasswordPolicyValidatorImpl
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class SecurityComponent(
    encryptedSettings: EncryptedSettings,
    httpClient: HttpClient,
    webSocketService: WebSocketService,
    parentScope: CoroutineScope? = null
) {
    private val componentScope = parentScope
        ?: CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val storageModule by lazy {
        SecurityStorageModule(
            encryptedSettings
        )
    }
    val securitySettingsStorage get() = storageModule.securitySettingsStorage

    private val networkModule by lazy {
        SecurityNetworkModule(
            httpClient
        )
    }
    val securitySettingsApi get() = networkModule.securitySettingsApi
    val securityWebSocketMessageHandler get() = networkModule.securityWebSocketMessageHandler

    private val repositoryModule by lazy {
        SecurityRepositoryModule(
            securitySettingsApi = securitySettingsApi,
            securitySettingsStorage = securitySettingsStorage,
            webSocketService = webSocketService,
            repositoryScope = componentScope
        )
    }
    val securitySettingsRepository get() = repositoryModule.securitySettingsRepository

    val passwordPolicyValidator: PasswordPolicyValidator by lazy {
        PasswordPolicyValidatorImpl()
    }

    private val useCaseModule by lazy {
        SecurityUseCaseModule(
            securitySettingsRepository = securitySettingsRepository,
            passwordPolicyValidator = passwordPolicyValidator
        )
    }
    val refreshSecuritySettingsUseCase get() = useCaseModule.refreshSecuritySettingsUseCase
    val validatePasswordUseCase get() = useCaseModule.validatePasswordUseCase
}