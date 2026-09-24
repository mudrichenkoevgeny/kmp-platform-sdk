package io.github.mudrichenkoevgeny.kmp.feature.managementuser.di

import io.github.mudrichenkoevgeny.kmp.core.security.repository.OpenSecuritySettingsRepository
import io.github.mudrichenkoevgeny.kmp.core.settings.repository.OpenGlobalSettingsRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.network.api.configuration.OpenUserConfigurationApi
import io.github.mudrichenkoevgeny.kmp.feature.user.auth.UserAuthServices
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
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.unlock.SendUnlockEmailConfirmationUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.unlock.SendUnlockPhoneConfirmationUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.unlock.UnlockByEmailUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.unlock.UnlockByExternalAuthProviderUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.unlock.UnlockByPhoneUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.settings.GetAvailableUserAuthProvidersUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.auth.google.DisabledGoogleAuthService
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.AddUserIdentifierEmailUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.AddUserIdentifierGoogleUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.AddUserIdentifierPhoneUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.DeleteUserIdentifierUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.EmailChangePasswordUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.GetUserIdentifierUseCase
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
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session.GetSessionUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session.GetSessionsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session.ReauthenticateSessionUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session.LogoutUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.RestoreUserUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.ScheduleUserDeletionUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.auth.settings.GetManagementAuthSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.auth.settings.RefreshManagementAuthSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.auth.settings.SaveRemoteAuthSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.auth.settings.ResetRemoteAuthSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.configuration.RefreshManagementUserConfigurationUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.globalsettings.GetManagementGlobalSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.globalsettings.RefreshManagementGlobalSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.globalsettings.SaveRemoteGlobalSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.globalsettings.ResetRemoteGlobalSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.security.settings.GetManagementSecuritySettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.security.settings.RefreshManagementSecuritySettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.security.settings.SaveRemoteSecuritySettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.security.settings.ResetRemoteSecuritySettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.GetUsersUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.GetUserUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.CreateUserUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.UpdateUserUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.DeleteUserUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session.ManagementGetSessionUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session.ManagementGetSessionsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.identifier.ManagementGetIdentifiersUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.identifier.ManagementGetIdentifierUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.identifier.ManagementDeleteIdentifierUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.identifier.ManagementDeleteIdentifierPasswordUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session.ManagementDeleteAllUserSessionsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session.ManagementDeleteSessionUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.security.ManagementDisableTotpUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.model.apptype.AppType

/**
 * Internal dependency wiring for management use cases.
 */
internal class ManagementUserUseCaseModule(
    private val managementUserRepositoryModule: ManagementUserRepositoryModule,
    private val authStorage: AuthStorage,
    private val storageModule: UserStorageModule,
    private val authServices: UserAuthServices,
    private val openUserConfigurationApi: OpenUserConfigurationApi,
    private val openGlobalSettingsRepository: OpenGlobalSettingsRepository,
    private val openSecuritySettingsRepository: OpenSecuritySettingsRepository,
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

    /** Requests an account unlock code for an email. */
    val sendUnlockEmailConfirmationUseCase by lazy {
        SendUnlockEmailConfirmationUseCase(
            unlockRepository = managementUserRepositoryModule.selfManagementUnlockRepository
        )
    }

    /** Unlocks an account using an email code. */
    val unlockByEmailUseCase by lazy {
        UnlockByEmailUseCase(
            unlockRepository = managementUserRepositoryModule.selfManagementUnlockRepository
        )
    }

    /** Requests an account unlock code for a phone number. */
    val sendUnlockPhoneConfirmationUseCase by lazy {
        SendUnlockPhoneConfirmationUseCase(
            unlockRepository = managementUserRepositoryModule.selfManagementUnlockRepository
        )
    }

    /** Unlocks an account using a phone code. */
    val unlockByPhoneUseCase by lazy {
        UnlockByPhoneUseCase(
            unlockRepository = managementUserRepositoryModule.selfManagementUnlockRepository
        )
    }

    /** Unlocks an account using an external auth provider token. */
    val unlockByExternalAuthProviderUseCase by lazy {
        UnlockByExternalAuthProviderUseCase(
            unlockRepository = managementUserRepositoryModule.selfManagementUnlockRepository
        )
    }

    /** Returns auth providers allowed for management context. */
    val getAvailableUserAuthProvidersUseCase by lazy {
        GetAvailableUserAuthProvidersUseCase(
            appType = AppType.MANAGEMENT,
            openAuthSettingsRepository = null
        )
    }

    /** Refreshes full configuration for the management user. */
    val refreshUserConfigurationUseCase by lazy {
        RefreshManagementUserConfigurationUseCase(
            openUserConfigurationApi = openUserConfigurationApi,
            openGlobalSettingsRepository = openGlobalSettingsRepository,
            openSecuritySettingsRepository = openSecuritySettingsRepository
        )
    }

    /** Signs out the manager. */
    val logoutUseCase by lazy {
        LogoutUseCase(
            sessionRepository = managementUserRepositoryModule.selfManagementSessionRepository,
            userRepository = managementUserRepositoryModule.selfManagementUserRepository
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

    /** Returns specific session details. */
    val getSessionUseCase by lazy {
        GetSessionUseCase(
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

    /** Re-authenticates session via TOTP to update its trust level. */
    val reauthenticateSessionUseCase by lazy {
        ReauthenticateSessionUseCase(
            sessionRepository = managementUserRepositoryModule.selfManagementSessionRepository
        )
    }

    /** Returns account identifiers. */
    val getUserIdentifiersUseCase by lazy {
        GetUserIdentifiersUseCase(
            identifierRepository = managementUserRepositoryModule.selfManagementIdentifierRepository
        )
    }

    /** Returns identifier details. */
    val getUserIdentifierUseCase by lazy {
        GetUserIdentifierUseCase(
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

    /** Associates a new Google identifier with the current account. */
    val addUserIdentifierGoogleUseCase by lazy {
        AddUserIdentifierGoogleUseCase(
            authService = authServices.googleAuth ?: DisabledGoogleAuthService(),
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

    /** Resets remote auth settings. */
    val resetRemoteAuthSettingsUseCase by lazy {
        ResetRemoteAuthSettingsUseCase(
            managementAuthSettingsRepository = managementUserRepositoryModule.managementAuthSettingsRepository
        )
    }

    /** Resets remote global settings. */
    val resetRemoteGlobalSettingsUseCase by lazy {
        ResetRemoteGlobalSettingsUseCase(
            managementGlobalSettingsRepository = managementUserRepositoryModule.managementGlobalSettingsRepository
        )
    }

    /** Resets remote security settings. */
    val resetRemoteSecuritySettingsUseCase by lazy {
        ResetRemoteSecuritySettingsUseCase(
            managementSecuritySettingsRepository = managementUserRepositoryModule.managementSecuritySettingsRepository
        )
    }

    /** Returns paginated list of users. */
    val getUsersUseCase by lazy {
        GetUsersUseCase(
            managementUserRepository = managementUserRepositoryModule.managementUserRepository
        )
    }

    /** Retrieves specific user details. */
    val getUserUseCase by lazy {
        GetUserUseCase(
            managementUserRepository = managementUserRepositoryModule.managementUserRepository
        )
    }

    /** Creates a new user account. */
    val createUserUseCase by lazy {
        CreateUserUseCase(
            managementUserRepository = managementUserRepositoryModule.managementUserRepository
        )
    }

    /** Updates user details. */
    val updateUserUseCase by lazy {
        UpdateUserUseCase(
            managementUserRepository = managementUserRepositoryModule.managementUserRepository
        )
    }

    /** Deletes user account. */
    val deleteUserUseCase by lazy {
        DeleteUserUseCase(
            managementUserRepository = managementUserRepositoryModule.managementUserRepository
        )
    }

    /** Retrieves user sessions administratively. */
    val managementGetSessionsUseCase by lazy {
        ManagementGetSessionsUseCase(
            managementSessionRepository = managementUserRepositoryModule.managementSessionRepository
        )
    }

    /** Retrieves specific user session details administratively. */
    val managementGetSessionUseCase by lazy {
        ManagementGetSessionUseCase(
            managementSessionRepository = managementUserRepositoryModule.managementSessionRepository
        )
    }

    /** Retrieves user identifier administratively. */
    val managementGetIdentifierUseCase by lazy {
        ManagementGetIdentifierUseCase(
            managementIdentifierRepository = managementUserRepositoryModule.managementIdentifierRepository
        )
    }

    /** Retrieves user identifiers administratively. */
    val managementGetIdentifiersUseCase by lazy {
        ManagementGetIdentifiersUseCase(
            managementIdentifierRepository = managementUserRepositoryModule.managementIdentifierRepository
        )
    }

    /** Removes user identifier administratively. */
    val managementDeleteIdentifierUseCase by lazy {
        ManagementDeleteIdentifierUseCase(
            managementIdentifierRepository = managementUserRepositoryModule.managementIdentifierRepository
        )
    }

    /** Removes user identifier password administratively. */
    val managementDeleteIdentifierPasswordUseCase by lazy {
        ManagementDeleteIdentifierPasswordUseCase(
            managementIdentifierRepository = managementUserRepositoryModule.managementIdentifierRepository
        )
    }

    /** Disables TOTP for a specific user administratively. */
    val managementDisableTotpUseCase by lazy {
        ManagementDisableTotpUseCase(
            managementUserSecurityRepository = managementUserRepositoryModule.managementUserSecurityRepository
        )
    }

    /** Revokes all active sessions for a specific user administratively. */
    val managementDeleteAllUserSessionsUseCase by lazy {
        ManagementDeleteAllUserSessionsUseCase(
            managementSessionRepository = managementUserRepositoryModule.managementSessionRepository
        )
    }

    /** Revokes a specific session for a specific user administratively. */
    val managementDeleteSessionUseCase by lazy {
        ManagementDeleteSessionUseCase(
            managementSessionRepository = managementUserRepositoryModule.managementSessionRepository
        )
    }
}
