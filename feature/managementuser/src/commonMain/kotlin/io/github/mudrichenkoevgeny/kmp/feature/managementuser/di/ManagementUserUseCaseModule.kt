package io.github.mudrichenkoevgeny.kmp.feature.managementuser.di

import io.github.mudrichenkoevgeny.kmp.feature.user.auth.UserAuthServices
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.configuration.ManagementUserConfigurationApi
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.auth.settings.ManagementAuthSettingsRepository
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.globalsettings.ManagementGlobalSettingsRepository
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.security.settings.ManagementSecuritySettingsRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.di.UserStorageModule
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth.AuthStorage
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.login.LoginByEmailUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.login.LoginByTotpRecoveryCodeUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.login.LoginByTotpUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.refreshtoken.RefreshTokenUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.resetpassword.ResetEmailPasswordUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.resetpassword.SendResetPasswordConfirmationToEmailUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.settings.GetAvailableUserAuthProvidersUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.AddUserIdentifierEmailUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.AddUserIdentifierPhoneUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.DeleteUserIdentifierUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.EmailChangePasswordUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.GetUserIdentifiersUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.SendAddEmailIdentifierConfirmationUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.SendAddPhoneIdentifierConfirmationUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.security.DisableTotpUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.security.EnableTotpUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.security.GetRecoveryCodesUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.security.RegenerateRecoveryCodesUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.security.SetupTotpUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session.DeleteAllOtherSessionsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session.DeleteSessionUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session.GetSessionsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session.LogoutUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.RestoreUserUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.ScheduleUserDeletionUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.auth.settings.GetManagementAuthSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.auth.settings.RefreshManagementAuthSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.auth.settings.SaveRemoteAuthSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.configuration.RefreshManagementUserConfigurationUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.globalsettings.GetManagementGlobalSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.globalsettings.RefreshManagementGlobalSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.globalsettings.SaveRemoteGlobalSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.security.settings.GetManagementSecuritySettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.security.settings.RefreshManagementSecuritySettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.security.settings.SaveRemoteSecuritySettingsUseCase

/**
 * Internal dependency wiring for management use cases.
 */
internal class ManagementUserUseCaseModule(
    private val managementUserRepositoryModule: ManagementUserRepositoryModule,
    private val authStorage: AuthStorage,
    private val storageModule: UserStorageModule,
    private val authServices: UserAuthServices,
    private val managementUserConfigurationApi: ManagementUserConfigurationApi,
    private val managementAuthSettingsRepository: ManagementAuthSettingsRepository,
    private val managementGlobalSettingsRepository: ManagementGlobalSettingsRepository,
    private val managementSecuritySettingsRepository: ManagementSecuritySettingsRepository
) {

    // Auth
    /** Refreshes the management session. */
    val refreshTokenUseCase by lazy {
        RefreshTokenUseCase(
            refreshTokenRepository = managementUserRepositoryModule.selfManagementRefreshTokenRepository,
            authStorage = authStorage
        )
    }

    /** Signs in as manager via email. */
    val loginByEmailUseCase by lazy {
        LoginByEmailUseCase(
            loginRepository = managementUserRepositoryModule.selfManagementLoginRepository,
            authStorage = authStorage,
            userStorage = storageModule.userStorage
        )
    }

    /** Signs in as manager via TOTP code. */
    val loginByTotpUseCase by lazy {
        LoginByTotpUseCase(
            loginRepository = managementUserRepositoryModule.selfManagementLoginRepository,
            authStorage = authStorage,
            userStorage = storageModule.userStorage
        )
    }

    /** Completes MFA flow via recovery code. */
    val loginByTotpRecoveryCodeUseCase by lazy {
        LoginByTotpRecoveryCodeUseCase(
            loginRepository = managementUserRepositoryModule.selfManagementLoginRepository,
            authStorage = authStorage,
            userStorage = storageModule.userStorage
        )
    }

    /** Forces a network refresh of auth settings. */
    val refreshManagementAuthSettingsUseCase by lazy {
        RefreshManagementAuthSettingsUseCase(
            managementAuthSettingsRepository = managementUserRepositoryModule.managementAuthSettingsRepository
        )
    }

    /** Forces a network refresh of global settings. */
    val refreshManagementGlobalSettingsUseCase by lazy {
        RefreshManagementGlobalSettingsUseCase(
            managementGlobalSettingsRepository = managementGlobalSettingsRepository
        )
    }

    /** Forces a network refresh of security settings. */
    val refreshManagementSecuritySettingsUseCase by lazy {
        RefreshManagementSecuritySettingsUseCase(
            managementSecuritySettingsRepository = managementSecuritySettingsRepository
        )
    }

    /** Resets the management password. */
    val resetEmailPasswordUseCase by lazy {
        ResetEmailPasswordUseCase(
            resetPasswordRepository = managementUserRepositoryModule.selfManagementResetPasswordRepository
        )
    }

    /** Requests a reset code for email. */
    val sendResetPasswordConfirmationToEmailUseCase by lazy {
        SendResetPasswordConfirmationToEmailUseCase(
            resetPasswordRepository = managementUserRepositoryModule.selfManagementResetPasswordRepository
        )
    }

    /** Returns auth providers allowed for management context. */
    val getAvailableUserAuthProvidersUseCase by lazy {
        GetAvailableUserAuthProvidersUseCase(
            appType = io.github.mudrichenkoevgeny.kmp.feature.user.model.apptype.AppType.MANAGEMENT,
            openAuthSettingsRepository = null
        )
    }

    /** Refreshes full configuration for the management user. */
    val refreshUserConfigurationUseCase by lazy {
        RefreshManagementUserConfigurationUseCase(
            userConfigurationApi = managementUserConfigurationApi,
            managementGlobalSettingsRepository = managementGlobalSettingsRepository,
            managementSecuritySettingsRepository = managementSecuritySettingsRepository,
            managementAuthSettingsRepository = managementAuthSettingsRepository
        )
    }

    /** Signs out the manager. */
    val logoutUseCase by lazy {
        LogoutUseCase(
            sessionRepository = managementUserRepositoryModule.selfManagementSessionRepository,
            authStorage = authStorage,
            userStorage = storageModule.userStorage
        )
    }

    /** Schedules account deletion. */
    val scheduleUserDeletionUseCase by lazy {
        ScheduleUserDeletionUseCase(
            userRepository = managementUserRepositoryModule.selfManagementUserRepository
        )
    }

    /** Restores deleted account. */
    val restoreUserUseCase by lazy {
        RestoreUserUseCase(
            userRepository = managementUserRepositoryModule.selfManagementUserRepository
        )
    }

    /** Initiates TOTP setup. */
    val setupTotpUseCase by lazy {
        SetupTotpUseCase(
            userSecurityRepository = managementUserRepositoryModule.selfManagementUserSecurityRepository
        )
    }

    /** Enables TOTP. */
    val enableTotpUseCase by lazy {
        EnableTotpUseCase(
            userSecurityRepository = managementUserRepositoryModule.selfManagementUserSecurityRepository
        )
    }

    /** Disables TOTP. */
    val disableTotpUseCase by lazy {
        DisableTotpUseCase(
            userSecurityRepository = managementUserRepositoryModule.selfManagementUserSecurityRepository
        )
    }

    /** Retrieves MFA recovery codes. */
    val getRecoveryCodesUseCase by lazy {
        GetRecoveryCodesUseCase(
            userSecurityRepository = managementUserRepositoryModule.selfManagementUserSecurityRepository
        )
    }

    /** Regenerates MFA recovery codes. */
    val regenerateRecoveryCodesUseCase by lazy {
        RegenerateRecoveryCodesUseCase(
            userSecurityRepository = managementUserRepositoryModule.selfManagementUserSecurityRepository
        )
    }

    /** Returns active sessions. */
    val getSessionsUseCase by lazy {
        GetSessionsUseCase(
            sessionRepository = managementUserRepositoryModule.selfManagementSessionRepository
        )
    }

    /** Revokes specific session. */
    val deleteSessionUseCase by lazy {
        DeleteSessionUseCase(
            sessionRepository = managementUserRepositoryModule.selfManagementSessionRepository
        )
    }

    /** Revokes all other sessions. */
    val deleteAllOtherSessionsUseCase by lazy {
        DeleteAllOtherSessionsUseCase(
            sessionRepository = managementUserRepositoryModule.selfManagementSessionRepository
        )
    }

    /** Returns account identifiers. */
    val getUserIdentifiersUseCase by lazy {
        GetUserIdentifiersUseCase(
            identifierRepository = managementUserRepositoryModule.selfManagementIdentifierRepository
        )
    }

    /** Removes identifier. */
    val deleteUserIdentifierUseCase by lazy {
        DeleteUserIdentifierUseCase(
            identifierRepository = managementUserRepositoryModule.selfManagementIdentifierRepository
        )
    }

    /** Sends email confirmation for linking. */
    val sendAddEmailIdentifierConfirmationUseCase by lazy {
        SendAddEmailIdentifierConfirmationUseCase(
            identifierRepository = managementUserRepositoryModule.selfManagementIdentifierRepository
        )
    }

    /** Links new email. */
    val addUserIdentifierEmailUseCase by lazy {
        AddUserIdentifierEmailUseCase(
            identifierRepository = managementUserRepositoryModule.selfManagementIdentifierRepository
        )
    }

    /** Sends phone confirmation for linking. */
    val sendAddPhoneIdentifierConfirmationUseCase by lazy {
        SendAddPhoneIdentifierConfirmationUseCase(
            identifierRepository = managementUserRepositoryModule.selfManagementIdentifierRepository
        )
    }

    /** Links new phone. */
    val addUserIdentifierPhoneUseCase by lazy {
        AddUserIdentifierPhoneUseCase(
            identifierRepository = managementUserRepositoryModule.selfManagementIdentifierRepository
        )
    }

    /** Changes password. */
    val emailChangePasswordUseCase by lazy {
        EmailChangePasswordUseCase(
            identifierRepository = managementUserRepositoryModule.selfManagementIdentifierRepository
        )
    }

    /** Returns management auth settings. */
    val getManagementAuthSettingsUseCase by lazy {
        GetManagementAuthSettingsUseCase(
            managementAuthSettingsRepository = managementUserRepositoryModule.managementAuthSettingsRepository
        )
    }

    /** Saves remote auth settings. */
    val saveRemoteAuthSettingsUseCase by lazy {
        SaveRemoteAuthSettingsUseCase(
            managementAuthSettingsRepository = managementUserRepositoryModule.managementAuthSettingsRepository
        )
    }

    /** Returns management global settings. */
    val getManagementGlobalSettingsUseCase by lazy {
        GetManagementGlobalSettingsUseCase(
            managementGlobalSettingsRepository = managementUserRepositoryModule.managementGlobalSettingsRepository
        )
    }

    /** Saves remote global settings. */
    val saveRemoteGlobalSettingsUseCase by lazy {
        SaveRemoteGlobalSettingsUseCase(
            managementGlobalSettingsRepository = managementUserRepositoryModule.managementGlobalSettingsRepository
        )
    }

    /** Returns management security settings. */
    val getManagementSecuritySettingsUseCase by lazy {
        GetManagementSecuritySettingsUseCase(
            managementSecuritySettingsRepository = managementUserRepositoryModule.managementSecuritySettingsRepository
        )
    }

    /** Saves remote security settings. */
    val saveRemoteSecuritySettingsUseCase by lazy {
        SaveRemoteSecuritySettingsUseCase(
            managementSecuritySettingsRepository = managementUserRepositoryModule.managementSecuritySettingsRepository
        )
    }
}
