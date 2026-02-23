package io.github.mudrichenkoevgeny.kmp.feature.user.di

import com.arkivanov.decompose.ComponentContext
import io.github.mudrichenkoevgeny.kmp.core.common.di.CommonComponent
import io.github.mudrichenkoevgeny.kmp.core.security.di.SecurityComponent
import io.github.mudrichenkoevgeny.kmp.core.settings.di.SettingsComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.auth.UserAuthServices
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth.AuthStorage
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.login.root.LoginRootComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.login.root.LoginRootComponentImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class UserComponent(
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

    private val networkModule = UserNetworkModule(commonComponent.httpClient)

    private val repositoryModule = UserRepositoryModule(networkModule, authStorage, storageModule, commonComponent.webSocketService, componentScope)
    val loginRepository get() = repositoryModule.loginRepository
    val registrationRepository get() = repositoryModule.registrationRepository
    val userRepository get() = repositoryModule.userRepository

    val passwordPolicyValidator get() = securityComponent.passwordPolicyValidator

    private val useCaseModule = UserUseCaseModule(
        repositoryModule = repositoryModule,
        authStorage = authStorage,
        storageModule = storageModule,
        authServices = authServices,
        userConfigurationApi = networkModule.userConfigurationApi,
        globalSettingsRepository = settingsComponent.globalSettingsRepository,
        securitySettingsRepository = securityComponent.securitySettingsRepository,
        authSettingsRepository = repositoryModule.authSettingsRepository
    )
    val loginByEmailUseCase get() = useCaseModule.loginByEmailUseCase
    val loginByPhoneUseCase get() = useCaseModule.loginByPhoneUseCase
    val sendLoginConfirmationToPhoneUseCase get() = useCaseModule.sendLoginConfirmationToPhoneUseCase
    val loginByGoogleUseCase get() = useCaseModule.loginByGoogleUseCase
    val refreshAuthSettingsUseCase get() = useCaseModule.refreshAuthSettingsUseCase
    val sendRegistrationConfirmationToEmailUseCase get() = useCaseModule.sendRegistrationConfirmationToEmailUseCase
    val registrationByEmailUseCase get() = useCaseModule.registrationByEmailUseCase
    val refreshUserConfigurationUseCase get() = useCaseModule.refreshUserConfigurationUseCase
    val getAvailableUserAuthProvidersUseCase get() = useCaseModule.getAvailableUserAuthProvidersUseCase

    fun createLoginRootDialogComponent(
        componentContext: ComponentContext,
        onFinished: () -> Unit
    ): LoginRootComponent {
        return LoginRootComponentImpl(
            componentContext = componentContext,
            settingsComponent = settingsComponent,
            securityComponent = securityComponent,
            userComponent = this,
            onFinished = onFinished
        )
    }
}