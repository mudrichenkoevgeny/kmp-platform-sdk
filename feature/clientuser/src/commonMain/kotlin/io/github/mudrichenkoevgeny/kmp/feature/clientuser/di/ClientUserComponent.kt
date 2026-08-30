package io.github.mudrichenkoevgeny.kmp.feature.clientuser.di

import com.arkivanov.decompose.ComponentContext
import io.github.mudrichenkoevgeny.kmp.core.common.di.CommonComponent
import io.github.mudrichenkoevgeny.kmp.core.security.di.SecurityComponent
import io.github.mudrichenkoevgeny.kmp.core.settings.di.SettingsComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.auth.UserAuthServices
import io.github.mudrichenkoevgeny.kmp.feature.user.di.UserStorageModule
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth.AuthStorage
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.login.root.ClientLoginRootComponent
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.login.root.ClientLoginRootComponentImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/**
 * Feature-level dependency root for the user module.
 *
 * Requires a fully wired `CommonComponent` (HTTP client, WebSocket service, encrypted settings),
 * `SettingsComponent`, `SecurityComponent`, a concrete [AuthStorage], and platform [UserAuthServices].
 *
 * Exposes repositories, auth-related use cases, the user WebSocket handler, and a factory for the
 * login dialog Decompose tree ([ClientLoginRootComponent]).
 *
 * @param commonComponent Shared infrastructure from `core:common`.
 * @param settingsComponent Global settings repository access from `core:settings`.
 * @param securityComponent Security settings and validators from `core:security`.
 * @param authStorage Token and auth-settings persistence implementing [AuthStorage].
 * @param authServices Optional platform services (e.g. Google Sign-In); may provide a null Google delegate.
 * @param parentScope Optional coroutine scope for repository-driven work; if null, an internal supervisor scope is used.
 */
class ClientUserComponent(
    val commonComponent: CommonComponent,
    val settingsComponent: SettingsComponent,
    val securityComponent: SecurityComponent,
    val authStorage: AuthStorage,
    val authServices: UserAuthServices,
    parentScope: CoroutineScope? = null
) {

    private val componentScope = parentScope
        ?: CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val storageModule = UserStorageModule(commonComponent.encryptedSettings)

    private val networkModule = ClientUserNetworkModule(
        httpClient = commonComponent.httpClient
    )

    private val clientUserRepositoryModule = ClientUserRepositoryModule(
        networkModule,
        authStorage,
        storageModule,
        commonComponent.webSocketService,
        componentScope
    )
    val loginRepository get() = clientUserRepositoryModule.loginRepository
    val registrationRepository get() = clientUserRepositoryModule.registrationRepository
    val userRepository get() = clientUserRepositoryModule.userRepository
    val passwordRepository get() = clientUserRepositoryModule.resetPasswordRepository
    val authSettingsRepository get() = clientUserRepositoryModule.openAuthSettingsRepository

    val passwordPolicyValidator get() = securityComponent.passwordPolicyValidator

    private val useCaseModule = ClientUserUseCaseModule(
        clientUserRepositoryModule = clientUserRepositoryModule,
        authStorage = authStorage,
        storageModule = storageModule,
        authServices = authServices,
        userConfigurationApi = networkModule.userConfigurationApi,
        openAuthSettingsRepository = clientUserRepositoryModule.openAuthSettingsRepository,
        globalSettingsRepository = settingsComponent.globalSettingsRepository,
        securitySettingsRepository = securityComponent.securitySettingsRepository
    )
    val refreshTokenUseCase get() = useCaseModule.refreshTokenUseCase
    val loginByEmailUseCase get() = useCaseModule.loginByEmailUseCase
    val loginByPhoneUseCase get() = useCaseModule.loginByPhoneUseCase
    val sendLoginConfirmationToPhoneUseCase get() = useCaseModule.sendLoginConfirmationToPhoneUseCase
    val loginByGoogleUseCase get() = useCaseModule.loginByGoogleUseCase
    val refreshAuthSettingsUseCase get() = useCaseModule.refreshAuthSettingsUseCase
    val sendRegistrationConfirmationToEmailUseCase get() = useCaseModule.sendRegistrationConfirmationToEmailUseCase
    val registrationByEmailUseCase get() = useCaseModule.registrationByEmailUseCase
    val getAvailableUserAuthProvidersUseCase get() = useCaseModule.getAvailableUserAuthProvidersUseCase
    val resetEmailPasswordUseCase get() = useCaseModule.resetEmailPasswordUseCase
    val sendResetPasswordConfirmationToEmailUseCase get() = useCaseModule.sendResetPasswordConfirmationToEmailUseCase
    val refreshUserConfigurationUseCase get() = useCaseModule.refreshUserConfigurationUseCase

    private val userWebSocketModule = ClientUserWebSocketModule(
        userStorage = storageModule.userStorage,
        authStorage = authStorage,
        refreshTokenUseCase = refreshTokenUseCase,
        scope = componentScope
    )
    val userWebSocketMessageHandler get() = userWebSocketModule.userWebSocketMessageHandler

    fun createLoginRootDialogComponent(
        componentContext: ComponentContext,
        onFinished: () -> Unit
    ): ClientLoginRootComponent = ClientLoginRootComponentImpl(
        componentContext = componentContext,
        settingsComponent = settingsComponent,
        securityComponent = securityComponent,
        clientUserComponent = this,
        onFinished = onFinished
    )
}
