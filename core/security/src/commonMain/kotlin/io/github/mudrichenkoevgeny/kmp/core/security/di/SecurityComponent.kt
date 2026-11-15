package io.github.mudrichenkoevgeny.kmp.core.security.di

import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.messagehandler.WebSocketMessageHandler
import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.service.WebSocketService
import io.github.mudrichenkoevgeny.kmp.core.common.storage.EncryptedSettings
import io.github.mudrichenkoevgeny.kmp.core.security.network.securitysettings.OpenSecuritySettingsApi
import io.github.mudrichenkoevgeny.kmp.core.security.repository.OpenSecuritySettingsRepository
import io.github.mudrichenkoevgeny.kmp.core.security.storage.securitysettings.OpenSecuritySettingsStorage
import io.github.mudrichenkoevgeny.kmp.core.security.usecase.GetOpenSecuritySettingsUseCase
import io.github.mudrichenkoevgeny.kmp.core.security.usecase.ObserveOpenSecuritySettingsUseCase
import io.github.mudrichenkoevgeny.kmp.core.security.usecase.RefreshOpenSecuritySettingsUseCase
import io.github.mudrichenkoevgeny.kmp.core.security.usecase.ValidatePasswordUseCase
import io.github.mudrichenkoevgeny.shared.foundation.core.security.passwordpolicy.validator.PasswordPolicyValidator
import io.github.mudrichenkoevgeny.shared.foundation.core.security.passwordpolicy.validator.PasswordPolicyValidatorImpl
import io.ktor.client.HttpClient
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
public class SecurityComponent(
    webSocketService: WebSocketService,
    httpClient: HttpClient,
    encryptedSettings: EncryptedSettings,
    openSecuritySettingsApi: OpenSecuritySettingsApi? = null,
    parentScope: CoroutineScope? = null
) {
    private val componentScope = parentScope
        ?: CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val storageModule by lazy {
        SecurityStorageModule(
            encryptedSettings
        )
    }
    public val securitySettingsStorage: OpenSecuritySettingsStorage get() = storageModule.openSecuritySettingsStorage

    public val passwordPolicyValidator: PasswordPolicyValidator by lazy {
        PasswordPolicyValidatorImpl()
    }

    private val networkModule by lazy {
        SecurityNetworkModule(httpClient)
    }

    private val repositoryModule by lazy {
        SecurityRepositoryModule(
            openSecuritySettingsApi = openSecuritySettingsApi ?: networkModule.openSecuritySettingsApi,
            openSecuritySettingsStorage = securitySettingsStorage,
            webSocketService = webSocketService,
            repositoryScope = componentScope
        )
    }
    public val securitySettingsRepository: OpenSecuritySettingsRepository
        get() =
            repositoryModule.openSecuritySettingsRepository

    private val useCaseModule by lazy {
        SecurityUseCaseModule(
            openSecuritySettingsRepository = securitySettingsRepository,
            passwordPolicyValidator = passwordPolicyValidator
        )
    }
    public val refreshSecuritySettingsUseCase: RefreshOpenSecuritySettingsUseCase
        get() = useCaseModule.refreshOpenSecuritySettingsUseCase
    public val getOpenSecuritySettingsUseCase: GetOpenSecuritySettingsUseCase
        get() = useCaseModule.getOpenSecuritySettingsUseCase
    public val observeSecuritySettingsUseCase: ObserveOpenSecuritySettingsUseCase
        get() = useCaseModule.observeOpenSecuritySettingsUseCase
    public val validatePasswordUseCase: ValidatePasswordUseCase
        get() = useCaseModule.validatePasswordUseCase

    private val webSocketsModule by lazy {
        SecurityWebSocketModule()
    }

    public val securityWebSocketMessageHandler: WebSocketMessageHandler
        get() = webSocketsModule.securityWebSocketMessageHandler
}
