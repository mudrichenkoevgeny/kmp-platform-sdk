package io.github.mudrichenkoevgeny.kmp.feature.managementuser.di

import io.github.mudrichenkoevgeny.kmp.core.security.repository.SecuritySettingsRepository
import io.github.mudrichenkoevgeny.kmp.core.settings.repository.GlobalSettingsRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.auth.UserAuthServices
import io.github.mudrichenkoevgeny.kmp.feature.user.auth.google.DisabledGoogleAuthService
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.configuration.ManagementUserConfigurationApi
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.auth.settings.ManagementAuthSettingsRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth.AuthStorage
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.login.LoginByEmailUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.resetpassword.ResetEmailPasswordUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.resetpassword.SendResetPasswordConfirmationToEmailUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.refreshtoken.RefreshTokenUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.settings.GetAvailableUserAuthProvidersUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.auth.settings.RefreshAuthSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.configuration.RefreshManagementUserConfigurationUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.di.UserStorageModule
import io.github.mudrichenkoevgeny.kmp.feature.user.model.apptype.AppType
import kotlin.getValue

/**
 * Wires user-facing use cases from repositories, [AuthStorage], [UserAuthServices], and cross-cutting settings APIs.
 *
 * @param managementUserRepositoryModule User repositories for auth and profile.
 * @param authStorage Token and cached auth settings.
 * @param storageModule User-scoped storage for post-login data.
 * @param authServices Platform auth helpers; missing Google service falls back to [DisabledGoogleAuthService].
 * @param managementUserConfigurationApi Remote user configuration endpoint.
 * @param managementAuthSettingsRepository Auth provider and policy snapshot repository.
 */
internal class ManagementUserUseCaseModule(
    private val managementUserRepositoryModule: ManagementUserRepositoryModule,
    private val authStorage: AuthStorage,
    private val storageModule: UserStorageModule,
    private val authServices: UserAuthServices,
    private val managementUserConfigurationApi: ManagementUserConfigurationApi,
    private val managementAuthSettingsRepository: ManagementAuthSettingsRepository,
    private val globalSettingsRepository: GlobalSettingsRepository,
    private val securitySettingsRepository: SecuritySettingsRepository
) {

    // Auth
    val refreshTokenUseCase by lazy {
        RefreshTokenUseCase(
            refreshTokenRepository = managementUserRepositoryModule.selfManagementRefreshTokenRepository,
            authStorage = authStorage
        )
    }

    val loginByEmailUseCase by lazy {
        LoginByEmailUseCase(
            loginRepository = managementUserRepositoryModule.selfManagementLoginRepository,
            authStorage = authStorage,
            userStorage = storageModule.userStorage
        )
    }

    val refreshAuthSettingsUseCase by lazy {
        RefreshAuthSettingsUseCase(
            managementAuthSettingsRepository = managementUserRepositoryModule.managementAuthSettingsRepository
        )
    }

    val resetEmailPasswordUseCase by lazy {
        ResetEmailPasswordUseCase(
            resetPasswordRepository = managementUserRepositoryModule.selfManagementResetPasswordRepository
        )
    }

    val sendResetPasswordConfirmationToEmailUseCase by lazy {
        SendResetPasswordConfirmationToEmailUseCase(
            resetPasswordRepository = managementUserRepositoryModule.selfManagementResetPasswordRepository
        )
    }

    val getAvailableUserAuthProvidersUseCase by lazy {
        GetAvailableUserAuthProvidersUseCase(
            appType = AppType.MANAGEMENT
        )
    }

    val refreshUserConfigurationUseCase by lazy {
        RefreshManagementUserConfigurationUseCase(
            userConfigurationApi = managementUserConfigurationApi,
            globalSettingsRepository = globalSettingsRepository,
            securitySettingsRepository = securitySettingsRepository,
            managementAuthSettingsRepository = managementAuthSettingsRepository
        )
    }
}
