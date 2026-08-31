package io.github.mudrichenkoevgeny.kmp.feature.managementuser.di

import com.arkivanov.decompose.ComponentContext
import io.github.mudrichenkoevgeny.kmp.core.common.di.CommonComponent
import io.github.mudrichenkoevgeny.kmp.core.security.di.SecurityComponent
import io.github.mudrichenkoevgeny.kmp.core.settings.di.SettingsComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.auth.UserAuthServices
import io.github.mudrichenkoevgeny.kmp.feature.user.di.UserStorageModule
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth.AuthStorage
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.auth.login.root.ManagementLoginRootComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.auth.login.root.ManagementLoginRootComponentImpl
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
 * login dialog Decompose tree ([ManagementLoginRootComponent]).
 *
 * @param commonComponent Shared infrastructure from `core:common`.
 * @param settingsComponent Global settings repository access from `core:settings`.
 * @param securityComponent Security settings and validators from `core:security`.
 * @param authStorage Token and auth-settings persistence implementing [AuthStorage].
 * @param authServices Optional platform services (e.g. Google Sign-In); may provide a null Google delegate.
 * @param parentScope Optional coroutine scope for repository-driven work; if null, an internal supervisor scope is used.
 */
class ManagementUserComponent(
    val commonComponent: CommonComponent,
    val settingsComponent: SettingsComponent,
    val securityComponent: SecurityComponent,
    val authStorage: AuthStorage,
    val authServices: UserAuthServices,
    parentScope: CoroutineScope? = null
) {
    private val componentScope = parentScope ?: CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val storageModule = UserStorageModule(commonComponent.encryptedSettings)
    private val networkModule = ManagementUserNetworkModule(httpClient = commonComponent.httpClient)
    private val repositoryModule = ManagementUserRepositoryModule(
        networkModule, authStorage, storageModule, commonComponent.webSocketService, componentScope
    )

    /** Repository for self-management login (email/totp). */
    val loginRepository get() = repositoryModule.selfManagementLoginRepository

    /** Repository for current management user profile. */
    val userRepository get() = repositoryModule.selfManagementUserRepository

    /** Repository for password recovery in management context. */
    val passwordRepository get() = repositoryModule.selfManagementResetPasswordRepository

    /** Repository for management-specific authentication settings. */
    val authSettingsRepository get() = repositoryModule.managementAuthSettingsRepository

    /** Validator for password rules from core security. */
    val passwordPolicyValidator get() = securityComponent.passwordPolicyValidator

    private val useCaseModule = ManagementUserUseCaseModule(
        managementUserRepositoryModule = repositoryModule,
        authStorage = authStorage,
        storageModule = storageModule,
        authServices = authServices,
        managementUserConfigurationApi = networkModule.userConfigurationApi,
        managementAuthSettingsRepository = repositoryModule.managementAuthSettingsRepository,
        globalSettingsRepository = settingsComponent.globalSettingsRepository,
        securitySettingsRepository = securityComponent.securitySettingsRepository
    )

    /** Refreshes the management session. */
    val refreshTokenUseCase get() = useCaseModule.refreshTokenUseCase

    /** Signs in as manager via email. */
    val loginByEmailUseCase get() = useCaseModule.loginByEmailUseCase

    /** Forces refresh of auth settings. */
    val refreshAuthSettingsUseCase get() = useCaseModule.refreshAuthSettingsUseCase

    /** Resets management password. */
    val resetEmailPasswordUseCase get() = useCaseModule.resetEmailPasswordUseCase

    /** Requests password reset code. */
    val sendResetPasswordConfirmationToEmailUseCase get() = useCaseModule.sendResetPasswordConfirmationToEmailUseCase

    /** Returns auth providers for management app. */
    val getAvailableUserAuthProvidersUseCase get() = useCaseModule.getAvailableUserAuthProvidersUseCase

    /** Refreshes full configuration for the management user. */
    val refreshUserConfigurationUseCase get() = useCaseModule.refreshUserConfigurationUseCase

    private val userWebSocketModule = ManagementUserWebSocketModule(
        userStorage = storageModule.userStorage,
        authStorage = authStorage,
        refreshTokenUseCase = refreshTokenUseCase,
        scope = componentScope
    )

    /** WebSocket handler for management push events. */
    val userWebSocketMessageHandler get() = userWebSocketModule.userWebSocketMessageHandler

    /**
     * Creates the root Decompose component for management login flow.
     *
     * @param componentContext Decompose context.
     * @param onFinished Invoked on successful login.
     */
    fun createLoginRootDialogComponent(
        componentContext: ComponentContext,
        onFinished: () -> Unit
    ): ManagementLoginRootComponent = ManagementLoginRootComponentImpl(
        componentContext = componentContext,
        settingsComponent = settingsComponent,
        securityComponent = securityComponent,
        managementUserComponent = this,
        onFinished = onFinished
    )
}
