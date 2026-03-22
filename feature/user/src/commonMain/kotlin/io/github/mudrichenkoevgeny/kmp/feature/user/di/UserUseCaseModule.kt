package io.github.mudrichenkoevgeny.kmp.feature.user.di

import io.github.mudrichenkoevgeny.kmp.core.security.repository.securitysettings.SecuritySettingsRepository
import io.github.mudrichenkoevgeny.kmp.core.settings.repository.globalsettings.GlobalSettingsRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.auth.UserAuthServices
import io.github.mudrichenkoevgeny.kmp.feature.user.auth.google.DisabledGoogleAuthService
import io.github.mudrichenkoevgeny.kmp.feature.user.network.api.configuration.UserConfigurationApi
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.settings.AuthSettingsRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth.AuthStorage
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.login.LoginByEmailUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.login.LoginByGoogleUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.login.LoginByPhoneUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.login.SendLoginConfirmationToPhoneUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.password.ResetEmailPasswordUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.password.SendResetPasswordConfirmationToEmailUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.refreshtoken.RefreshTokenUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.registration.RegistrationByEmailUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.registration.SendRegistrationConfirmationToEmailUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.settings.GetAvailableUserAuthProvidersUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.settings.RefreshAuthSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.configuration.RefreshUserConfigurationUseCase

/**
 * Wires user-facing use cases from repositories, [AuthStorage], [UserAuthServices], and cross-cutting settings APIs.
 *
 * @param repositoryModule User repositories for auth and profile.
 * @param authStorage Token and cached auth settings.
 * @param storageModule User-scoped storage for post-login data.
 * @param authServices Platform auth helpers; missing Google service falls back to [DisabledGoogleAuthService].
 * @param userConfigurationApi Remote user configuration endpoint.
 * @param globalSettingsRepository From `core:settings`.
 * @param securitySettingsRepository From `core:security`.
 * @param authSettingsRepository Auth provider and policy snapshot repository.
 */
internal class UserUseCaseModule(
    private val repositoryModule: UserRepositoryModule,
    private val authStorage: AuthStorage,
    private val storageModule: UserStorageModule,
    private val authServices: UserAuthServices,
    private val userConfigurationApi: UserConfigurationApi,
    private val globalSettingsRepository: GlobalSettingsRepository,
    private val securitySettingsRepository: SecuritySettingsRepository,
    private val authSettingsRepository: AuthSettingsRepository
) {

    // Auth
    val refreshTokenUseCase by lazy {
        RefreshTokenUseCase(
            refreshTokenRepository = repositoryModule.refreshTokenRepository,
            authStorage = authStorage
        )
    }

    val loginByEmailUseCase by lazy {
        LoginByEmailUseCase(
            loginRepository = repositoryModule.loginRepository,
            authStorage = authStorage,
            userStorage = storageModule.userStorage
        )
    }

    val loginByPhoneUseCase by lazy {
        LoginByPhoneUseCase(
            loginRepository = repositoryModule.loginRepository,
            authStorage = authStorage,
            userStorage = storageModule.userStorage
        )
    }

    val sendLoginConfirmationToPhoneUseCase by lazy {
        SendLoginConfirmationToPhoneUseCase(
            loginRepository = repositoryModule.loginRepository
        )
    }

    val loginByGoogleUseCase by lazy {
        LoginByGoogleUseCase(
            authService = authServices.googleAuth ?: DisabledGoogleAuthService(),
            loginRepository = repositoryModule.loginRepository,
            authStorage = authStorage,
            userStorage = storageModule.userStorage
        )
    }

    val registrationByEmailUseCase by lazy {
        RegistrationByEmailUseCase(
            registrationRepository = repositoryModule.registrationRepository,
            authStorage = authStorage,
            userStorage = storageModule.userStorage
        )
    }

    val sendRegistrationConfirmationToEmailUseCase by lazy {
        SendRegistrationConfirmationToEmailUseCase(
            registrationRepository = repositoryModule.registrationRepository
        )
    }

    val refreshAuthSettingsUseCase by lazy {
        RefreshAuthSettingsUseCase(
            authSettingsRepository = repositoryModule.authSettingsRepository
        )
    }

    val getAvailableUserAuthProvidersUseCase by lazy {
        GetAvailableUserAuthProvidersUseCase(
            authSettingsRepository = repositoryModule.authSettingsRepository
        )
    }

    val resetEmailPasswordUseCase by lazy {
        ResetEmailPasswordUseCase(
            passwordRepository = repositoryModule.passwordRepository
        )
    }

    val sendResetPasswordConfirmationToEmailUseCase by lazy {
        SendResetPasswordConfirmationToEmailUseCase(
            passwordRepository = repositoryModule.passwordRepository
        )
    }

    // Configuration
    val refreshUserConfigurationUseCase by lazy {
        RefreshUserConfigurationUseCase(
            userConfigurationApi = userConfigurationApi,
            globalSettingsRepository = globalSettingsRepository,
            securitySettingsRepository = securitySettingsRepository,
            authSettingsRepository = authSettingsRepository
        )
    }
}