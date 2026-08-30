package io.github.mudrichenkoevgeny.kmp.feature.clientuser.di

import io.github.mudrichenkoevgeny.kmp.core.security.repository.SecuritySettingsRepository
import io.github.mudrichenkoevgeny.kmp.core.settings.repository.GlobalSettingsRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.auth.UserAuthServices
import io.github.mudrichenkoevgeny.kmp.feature.user.auth.google.DisabledGoogleAuthService
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.network.api.configuration.UserConfigurationApi
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.settings.OpenAuthSettingsRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.di.UserStorageModule
import io.github.mudrichenkoevgeny.kmp.feature.user.model.apptype.AppType
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth.AuthStorage
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.login.LoginByEmailUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.login.LoginByGoogleUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.resetpassword.ResetEmailPasswordUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.resetpassword.SendResetPasswordConfirmationToEmailUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.refreshtoken.RefreshTokenUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.login.LoginByPhoneUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.login.SendLoginConfirmationToPhoneUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.registration.RegistrationByEmailUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.registration.SendRegistrationConfirmationToEmailUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.settings.GetAvailableUserAuthProvidersUseCase
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.usecase.auth.settings.RefreshAuthSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.usecase.configuration.RefreshClientUserConfigurationUseCase

/**
 * Wires user-facing use cases from repositories, [AuthStorage], [UserAuthServices], and cross-cutting settings APIs.
 *
 * @param clientUserRepositoryModule User repositories for auth and profile.
 * @param authStorage Token and cached auth settings.
 * @param storageModule User-scoped storage for post-login data.
 * @param authServices Platform auth helpers; missing Google service falls back to [DisabledGoogleAuthService].
 * @param userConfigurationApi Remote user configuration endpoint.
 * @param openAuthSettingsRepository Auth provider and policy snapshot repository.
 */
internal class ClientUserUseCaseModule(
    private val clientUserRepositoryModule: ClientUserRepositoryModule,
    private val authStorage: AuthStorage,
    private val storageModule: UserStorageModule,
    private val authServices: UserAuthServices,
    private val userConfigurationApi: UserConfigurationApi,
    private val openAuthSettingsRepository: OpenAuthSettingsRepository,
    private val globalSettingsRepository: GlobalSettingsRepository,
    private val securitySettingsRepository: SecuritySettingsRepository
) {

    // Auth
    val refreshTokenUseCase by lazy {
        RefreshTokenUseCase(
            refreshTokenRepository = clientUserRepositoryModule.refreshTokenRepository,
            authStorage = authStorage
        )
    }

    val loginByEmailUseCase by lazy {
        LoginByEmailUseCase(
            loginRepository = clientUserRepositoryModule.loginRepository,
            authStorage = authStorage,
            userStorage = storageModule.userStorage
        )
    }

    val loginByPhoneUseCase by lazy {
        LoginByPhoneUseCase(
            loginRepository = clientUserRepositoryModule.loginRepository,
            authStorage = authStorage,
            userStorage = storageModule.userStorage
        )
    }

    val sendLoginConfirmationToPhoneUseCase by lazy {
        SendLoginConfirmationToPhoneUseCase(
            loginRepository = clientUserRepositoryModule.loginRepository
        )
    }

    val loginByGoogleUseCase by lazy {
        LoginByGoogleUseCase(
            authService = authServices.googleAuth ?: DisabledGoogleAuthService(),
            loginRepository = clientUserRepositoryModule.loginRepository,
            authStorage = authStorage,
            userStorage = storageModule.userStorage
        )
    }

    val registrationByEmailUseCase by lazy {
        RegistrationByEmailUseCase(
            registrationRepository = clientUserRepositoryModule.registrationRepository,
            authStorage = authStorage,
            userStorage = storageModule.userStorage
        )
    }

    val sendRegistrationConfirmationToEmailUseCase by lazy {
        SendRegistrationConfirmationToEmailUseCase(
            registrationRepository = clientUserRepositoryModule.registrationRepository
        )
    }

    val refreshAuthSettingsUseCase by lazy {
        RefreshAuthSettingsUseCase(
            openAuthSettingsRepository = openAuthSettingsRepository
        )
    }

    val getAvailableUserAuthProvidersUseCase by lazy {
        GetAvailableUserAuthProvidersUseCase(
            appType = AppType.CLIENT,
            openAuthSettingsRepository = openAuthSettingsRepository
        )
    }

    val resetEmailPasswordUseCase by lazy {
        ResetEmailPasswordUseCase(
            resetPasswordRepository = clientUserRepositoryModule.resetPasswordRepository
        )
    }

    val sendResetPasswordConfirmationToEmailUseCase by lazy {
        SendResetPasswordConfirmationToEmailUseCase(
            resetPasswordRepository = clientUserRepositoryModule.resetPasswordRepository
        )
    }

    val refreshUserConfigurationUseCase by lazy {
        RefreshClientUserConfigurationUseCase(
            userConfigurationApi = userConfigurationApi,
            globalSettingsRepository = globalSettingsRepository,
            securitySettingsRepository = securitySettingsRepository,
            openAuthSettingsRepository = openAuthSettingsRepository
        )
    }
}
