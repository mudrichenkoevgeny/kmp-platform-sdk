package io.github.mudrichenkoevgeny.kmp.feature.managementuser.di

import io.github.mudrichenkoevgeny.kmp.core.security.repository.SecuritySettingsRepository
import io.github.mudrichenkoevgeny.kmp.core.settings.repository.GlobalSettingsRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.auth.UserAuthServices
import io.github.mudrichenkoevgeny.kmp.feature.user.auth.google.DisabledGoogleAuthService
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.configuration.ManagementUserConfigurationApi
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.auth.settings.ManagementAuthSettingsRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth.AuthStorage
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.login.LoginByEmailUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.login.LoginByTotpRecoveryCodeUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.login.LoginByTotpUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.resetpassword.ResetEmailPasswordUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.resetpassword.SendResetPasswordConfirmationToEmailUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.refreshtoken.RefreshTokenUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.settings.GetAvailableUserAuthProvidersUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.ScheduleUserDeletionUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.security.DisableTotpUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.security.EnableTotpUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.security.GetRecoveryCodesUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.security.RegenerateRecoveryCodesUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.security.SetupTotpUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session.DeleteAllOtherSessionsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session.DeleteSessionUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session.GetSessionUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session.GetSessionsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session.LogoutUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session.ReauthenticateSessionUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.AddUserIdentifierEmailUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.AddUserIdentifierExternalAuthProviderUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.AddUserIdentifierPhoneUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.DeleteUserIdentifierUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.EmailChangePasswordUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.GetUserIdentifierUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.GetUserIdentifiersUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.SendAddEmailIdentifierConfirmationUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.SendAddPhoneIdentifierConfirmationUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.auth.settings.GetManagementAuthSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.auth.settings.ObserveManagementAuthSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.auth.settings.RefreshAuthSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.auth.settings.RefreshManagementAuthSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.auth.settings.SaveRemoteAuthSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.configuration.RefreshManagementUserConfigurationUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.CreateUserUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.DeleteUserUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.GetUserUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.GetUsersUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.UpdateUserUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.security.ManagementDisableTotpUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session.ManagementDeleteAllUserSessionsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session.ManagementDeleteSessionUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session.ManagementGetSessionUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session.ManagementGetSessionsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.identifier.ManagementDeleteIdentifierUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.identifier.ManagementGetIdentifierUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.identifier.ManagementGetIdentifiersUseCase
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
    /** Refreshes the management session. */
    val refreshTokenUseCase by lazy {
        RefreshTokenUseCase(
            refreshTokenRepository = managementUserRepositoryModule.selfManagementRefreshTokenRepository,
            authStorage = authStorage
        )
    }

    /** Signs in via email. */
    val loginByEmailUseCase by lazy {
        LoginByEmailUseCase(
            loginRepository = managementUserRepositoryModule.selfManagementLoginRepository,
            authStorage = authStorage,
            userStorage = storageModule.userStorage
        )
    }

    /** Completes MFA flow via TOTP. */
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
    val refreshAuthSettingsUseCase by lazy {
        RefreshAuthSettingsUseCase(
            managementAuthSettingsRepository = managementUserRepositoryModule.managementAuthSettingsRepository
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

    /** Returns available auth providers for management. */
    val getAvailableUserAuthProvidersUseCase by lazy {
        GetAvailableUserAuthProvidersUseCase(
            appType = AppType.MANAGEMENT
        )
    }

    /** Refreshes configuration for the manager. */
    val refreshUserConfigurationUseCase by lazy {
        RefreshManagementUserConfigurationUseCase(
            userConfigurationApi = managementUserConfigurationApi,
            globalSettingsRepository = globalSettingsRepository,
            securitySettingsRepository = securitySettingsRepository,
            managementAuthSettingsRepository = managementAuthSettingsRepository
        )
    }

    /** Schedules manager account for deletion. (Note: might be unsupported by repo but we wire it). */
    val scheduleUserDeletionUseCase by lazy {
        ScheduleUserDeletionUseCase(
            userRepository = managementUserRepositoryModule.selfManagementUserRepository
        )
    }

    // Security
    /** Initiates TOTP setup for the manager. */
    val setupTotpUseCase by lazy {
        SetupTotpUseCase(
            userSecurityRepository = managementUserRepositoryModule.selfManagementUserSecurityRepository
        )
    }

    /** Enables TOTP for the manager. */
    val enableTotpUseCase by lazy {
        EnableTotpUseCase(
            userSecurityRepository = managementUserRepositoryModule.selfManagementUserSecurityRepository
        )
    }

    /** Disables TOTP for the manager. */
    val disableTotpUseCase by lazy {
        DisableTotpUseCase(
            userSecurityRepository = managementUserRepositoryModule.selfManagementUserSecurityRepository
        )
    }

    /** Retrieves manager's recovery codes. */
    val getRecoveryCodesUseCase by lazy {
        GetRecoveryCodesUseCase(
            userSecurityRepository = managementUserRepositoryModule.selfManagementUserSecurityRepository
        )
    }

    /** Regenerates manager's recovery codes. */
    val regenerateRecoveryCodesUseCase by lazy {
        RegenerateRecoveryCodesUseCase(
            userSecurityRepository = managementUserRepositoryModule.selfManagementUserSecurityRepository
        )
    }

    // Sessions (Self)
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

    /** Signs out the manager. */
    val logoutUseCase by lazy {
        LogoutUseCase(
            sessionRepository = managementUserRepositoryModule.selfManagementSessionRepository,
            authStorage = authStorage,
            userStorage = storageModule.userStorage
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

    /** Upgrades session trust level. */
    val reauthenticateSessionUseCase by lazy {
        ReauthenticateSessionUseCase(
            sessionRepository = managementUserRepositoryModule.selfManagementSessionRepository
        )
    }

    // Identifiers (Self)
    /** Returns specific identifier. */
    val getUserIdentifierUseCase by lazy {
        GetUserIdentifierUseCase(
            identifierRepository = managementUserRepositoryModule.selfManagementIdentifierRepository
        )
    }

    /** Returns paged identifiers. */
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

    /** Links new email. */
    val addUserIdentifierEmailUseCase by lazy {
        AddUserIdentifierEmailUseCase(
            identifierRepository = managementUserRepositoryModule.selfManagementIdentifierRepository
        )
    }

    /** Links new phone. */
    val addUserIdentifierPhoneUseCase by lazy {
        AddUserIdentifierPhoneUseCase(
            identifierRepository = managementUserRepositoryModule.selfManagementIdentifierRepository
        )
    }

    /** Links external provider. */
    val addUserIdentifierExternalAuthProviderUseCase by lazy {
        AddUserIdentifierExternalAuthProviderUseCase(
            identifierRepository = managementUserRepositoryModule.selfManagementIdentifierRepository
        )
    }

    /** Sends email confirmation for linking. */
    val sendAddEmailIdentifierConfirmationUseCase by lazy {
        SendAddEmailIdentifierConfirmationUseCase(
            identifierRepository = managementUserRepositoryModule.selfManagementIdentifierRepository
        )
    }

    /** Sends phone confirmation for linking. */
    val sendAddPhoneIdentifierConfirmationUseCase by lazy {
        SendAddPhoneIdentifierConfirmationUseCase(
            identifierRepository = managementUserRepositoryModule.selfManagementIdentifierRepository
        )
    }

    /** Updates password via email. */
    val emailChangePasswordUseCase by lazy {
        EmailChangePasswordUseCase(
            identifierRepository = managementUserRepositoryModule.selfManagementIdentifierRepository
        )
    }

    // Management User
    /** Creates a new user account. */
    val createUserUseCase by lazy {
        CreateUserUseCase(
            managementUserRepository = managementUserRepositoryModule.managementUserRepository
        )
    }

    /** Returns paginated users. */
    val getUsersUseCase by lazy {
        GetUsersUseCase(
            managementUserRepository = managementUserRepositoryModule.managementUserRepository
        )
    }

    /** Returns user details. */
    val getUserUseCase by lazy {
        GetUserUseCase(
            managementUserRepository = managementUserRepositoryModule.managementUserRepository
        )
    }

    /** Updates user account. */
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

    // Management Security
    /** Administratively disables TOTP for a user. */
    val managementDisableTotpUseCase by lazy {
        ManagementDisableTotpUseCase(
            managementUserSecurityRepository = managementUserRepositoryModule.managementUserSecurityRepository
        )
    }

    // Management Sessions
    /** Returns any user sessions. */
    val managementGetSessionsUseCase by lazy {
        ManagementGetSessionsUseCase(
            managementSessionRepository = managementUserRepositoryModule.managementSessionRepository
        )
    }

    /** Returns details of any session. */
    val managementGetSessionUseCase by lazy {
        ManagementGetSessionUseCase(
            managementSessionRepository = managementUserRepositoryModule.managementSessionRepository
        )
    }

    /** Revokes any session. */
    val managementDeleteSessionUseCase by lazy {
        ManagementDeleteSessionUseCase(
            managementSessionRepository = managementUserRepositoryModule.managementSessionRepository
        )
    }

    /** Revokes all sessions for a user. */
    val managementDeleteAllUserSessionsUseCase by lazy {
        ManagementDeleteAllUserSessionsUseCase(
            managementSessionRepository = managementUserRepositoryModule.managementSessionRepository
        )
    }

    // Management Identifiers
    /** Returns any user identifiers. */
    val managementGetIdentifiersUseCase by lazy {
        ManagementGetIdentifiersUseCase(
            managementIdentifierRepository = managementUserRepositoryModule.managementIdentifierRepository
        )
    }

    /** Returns details of any identifier. */
    val managementGetIdentifierUseCase by lazy {
        ManagementGetIdentifierUseCase(
            managementIdentifierRepository = managementUserRepositoryModule.managementIdentifierRepository
        )
    }

    /** Removes any identifier. */
    val managementDeleteIdentifierUseCase by lazy {
        ManagementDeleteIdentifierUseCase(
            managementIdentifierRepository = managementUserRepositoryModule.managementIdentifierRepository
        )
    }
}
