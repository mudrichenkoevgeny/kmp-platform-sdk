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
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.login.LoginByTotpRecoveryCodeUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.login.LoginByTotpUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.login.LoginByGoogleUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.resetpassword.ResetEmailPasswordUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.resetpassword.SendResetPasswordConfirmationToEmailUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.refreshtoken.RefreshTokenUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.login.LoginByPhoneUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.login.SendLoginConfirmationToPhoneUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.registration.RegistrationByEmailUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.registration.SendRegistrationConfirmationToEmailUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.settings.GetAuthSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.settings.GetAvailableUserAuthProvidersUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.settings.ObserveAuthSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.AddUserIdentifierEmailUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.AddUserIdentifierExternalAuthProviderUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.AddUserIdentifierPhoneUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.DeleteUserIdentifierUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.EmailChangePasswordUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.GetUserIdentifierUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.GetUserIdentifiersUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.SendAddEmailIdentifierConfirmationUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.SendAddPhoneIdentifierConfirmationUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session.DeleteAllOtherSessionsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session.DeleteSessionUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session.GetSessionUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session.GetSessionsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session.LogoutUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.session.ReauthenticateSessionUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.security.DisableTotpUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.security.EnableTotpUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.security.GetRecoveryCodesUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.security.RegenerateRecoveryCodesUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.security.SetupTotpUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.RestoreUserUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.ScheduleUserDeletionUseCase
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
    /** Refreshes current session tokens. */
    val refreshTokenUseCase by lazy {
        RefreshTokenUseCase(
            refreshTokenRepository = clientUserRepositoryModule.refreshTokenRepository,
            authStorage = authStorage
        )
    }

    /** Signs in with email. */
    val loginByEmailUseCase by lazy {
        LoginByEmailUseCase(
            loginRepository = clientUserRepositoryModule.loginRepository,
            authStorage = authStorage,
            userStorage = storageModule.userStorage
        )
    }

    /** Signs in with phone OTP. */
    val loginByPhoneUseCase by lazy {
        LoginByPhoneUseCase(
            loginRepository = clientUserRepositoryModule.loginRepository,
            authStorage = authStorage,
            userStorage = storageModule.userStorage
        )
    }

    /** Requests a login OTP for a phone number. */
    val sendLoginConfirmationToPhoneUseCase by lazy {
        SendLoginConfirmationToPhoneUseCase(
            loginRepository = clientUserRepositoryModule.loginRepository
        )
    }

    /** Signs in with Google credentials. */
    val loginByGoogleUseCase by lazy {
        LoginByGoogleUseCase(
            authService = authServices.googleAuth ?: DisabledGoogleAuthService(),
            loginRepository = clientUserRepositoryModule.loginRepository,
            authStorage = authStorage,
            userStorage = storageModule.userStorage
        )
    }

    /** Completes MFA flow via TOTP. */
    val loginByTotpUseCase by lazy {
        LoginByTotpUseCase(
            loginRepository = clientUserRepositoryModule.loginRepository,
            authStorage = authStorage,
            userStorage = storageModule.userStorage
        )
    }

    /** Completes MFA flow via recovery code. */
    val loginByTotpRecoveryCodeUseCase by lazy {
        LoginByTotpRecoveryCodeUseCase(
            loginRepository = clientUserRepositoryModule.loginRepository,
            authStorage = authStorage,
            userStorage = storageModule.userStorage
        )
    }

    /** Registers a new account with email. */
    val registrationByEmailUseCase by lazy {
        RegistrationByEmailUseCase(
            registrationRepository = clientUserRepositoryModule.registrationRepository,
            authStorage = authStorage,
            userStorage = storageModule.userStorage
        )
    }

    /** Requests a registration code for an email. */
    val sendRegistrationConfirmationToEmailUseCase by lazy {
        SendRegistrationConfirmationToEmailUseCase(
            registrationRepository = clientUserRepositoryModule.registrationRepository
        )
    }

    /** Forces a refresh of allowed auth providers. */
    val refreshAuthSettingsUseCase by lazy {
        RefreshAuthSettingsUseCase(
            openAuthSettingsRepository = openAuthSettingsRepository
        )
    }

    /** Returns auth providers allowed for this client app. */
    val getAvailableUserAuthProvidersUseCase by lazy {
        GetAvailableUserAuthProvidersUseCase(
            appType = AppType.CLIENT,
            openAuthSettingsRepository = openAuthSettingsRepository
        )
    }

    /** Returns current public auth settings. */
    val getAuthSettingsUseCase by lazy {
        GetAuthSettingsUseCase(
            openAuthSettingsRepository = openAuthSettingsRepository
        )
    }

    /** Observes public auth settings updates. */
    val observeAuthSettingsUseCase by lazy {
        ObserveAuthSettingsUseCase(
            openAuthSettingsRepository = openAuthSettingsRepository
        )
    }

    /** Resets password using an email code. */
    val resetEmailPasswordUseCase by lazy {
        ResetEmailPasswordUseCase(
            resetPasswordRepository = clientUserRepositoryModule.resetPasswordRepository
        )
    }

    /** Requests a password-reset code for an email. */
    val sendResetPasswordConfirmationToEmailUseCase by lazy {
        SendResetPasswordConfirmationToEmailUseCase(
            resetPasswordRepository = clientUserRepositoryModule.resetPasswordRepository
        )
    }

    /** Full state refresh for the authenticated user. */
    val refreshUserConfigurationUseCase by lazy {
        RefreshClientUserConfigurationUseCase(
            userConfigurationApi = userConfigurationApi,
            globalSettingsRepository = globalSettingsRepository,
            securitySettingsRepository = securitySettingsRepository,
            openAuthSettingsRepository = openAuthSettingsRepository
        )
    }

    /** Schedules current account for deletion. */
    val scheduleUserDeletionUseCase by lazy {
        ScheduleUserDeletionUseCase(
            userRepository = clientUserRepositoryModule.userRepository
        )
    }

    /** Restores an account scheduled for deletion. */
    val restoreUserUseCase by lazy {
        RestoreUserUseCase(
            userRepository = clientUserRepositoryModule.userRepository
        )
    }

    // Security
    /** Initiates TOTP setup. */
    val setupTotpUseCase by lazy {
        SetupTotpUseCase(
            userSecurityRepository = clientUserRepositoryModule.userSecurityRepository
        )
    }

    /** Enables TOTP with a code. */
    val enableTotpUseCase by lazy {
        EnableTotpUseCase(
            userSecurityRepository = clientUserRepositoryModule.userSecurityRepository
        )
    }

    /** Disables TOTP. */
    val disableTotpUseCase by lazy {
        DisableTotpUseCase(
            userSecurityRepository = clientUserRepositoryModule.userSecurityRepository
        )
    }

    /** Retrieves active recovery codes. */
    val getRecoveryCodesUseCase by lazy {
        GetRecoveryCodesUseCase(
            userSecurityRepository = clientUserRepositoryModule.userSecurityRepository
        )
    }

    /** Regenerates recovery codes. */
    val regenerateRecoveryCodesUseCase by lazy {
        RegenerateRecoveryCodesUseCase(
            userSecurityRepository = clientUserRepositoryModule.userSecurityRepository
        )
    }

    // Sessions
    /** Returns active sessions. */
    val getSessionsUseCase by lazy {
        GetSessionsUseCase(
            sessionRepository = clientUserRepositoryModule.sessionRepository
        )
    }

    /** Returns specific session details. */
    val getSessionUseCase by lazy {
        GetSessionUseCase(
            sessionRepository = clientUserRepositoryModule.sessionRepository
        )
    }

    /** Signs out the user. */
    val logoutUseCase by lazy {
        LogoutUseCase(
            sessionRepository = clientUserRepositoryModule.sessionRepository,
            authStorage = authStorage,
            userStorage = storageModule.userStorage
        )
    }

    /** Revokes specific session. */
    val deleteSessionUseCase by lazy {
        DeleteSessionUseCase(
            sessionRepository = clientUserRepositoryModule.sessionRepository
        )
    }

    /** Revokes all other sessions. */
    val deleteAllOtherSessionsUseCase by lazy {
        DeleteAllOtherSessionsUseCase(
            sessionRepository = clientUserRepositoryModule.sessionRepository
        )
    }

    /** Upgrades session trust level. */
    val reauthenticateSessionUseCase by lazy {
        ReauthenticateSessionUseCase(
            sessionRepository = clientUserRepositoryModule.sessionRepository
        )
    }

    // Identifiers
    /** Returns specific identifier. */
    val getUserIdentifierUseCase by lazy {
        GetUserIdentifierUseCase(
            identifierRepository = clientUserRepositoryModule.identifierRepository
        )
    }

    /** Returns paged identifiers. */
    val getUserIdentifiersUseCase by lazy {
        GetUserIdentifiersUseCase(
            identifierRepository = clientUserRepositoryModule.identifierRepository
        )
    }

    /** Removes identifier. */
    val deleteUserIdentifierUseCase by lazy {
        DeleteUserIdentifierUseCase(
            identifierRepository = clientUserRepositoryModule.identifierRepository
        )
    }

    /** Links new email. */
    val addUserIdentifierEmailUseCase by lazy {
        AddUserIdentifierEmailUseCase(
            identifierRepository = clientUserRepositoryModule.identifierRepository
        )
    }

    /** Links new phone. */
    val addUserIdentifierPhoneUseCase by lazy {
        AddUserIdentifierPhoneUseCase(
            identifierRepository = clientUserRepositoryModule.identifierRepository
        )
    }

    /** Links external provider. */
    val addUserIdentifierExternalAuthProviderUseCase by lazy {
        AddUserIdentifierExternalAuthProviderUseCase(
            identifierRepository = clientUserRepositoryModule.identifierRepository
        )
    }

    /** Sends email confirmation for linking. */
    val sendAddEmailIdentifierConfirmationUseCase by lazy {
        SendAddEmailIdentifierConfirmationUseCase(
            identifierRepository = clientUserRepositoryModule.identifierRepository
        )
    }

    /** Sends phone confirmation for linking. */
    val sendAddPhoneIdentifierConfirmationUseCase by lazy {
        SendAddPhoneIdentifierConfirmationUseCase(
            identifierRepository = clientUserRepositoryModule.identifierRepository
        )
    }

    /** Updates password via email. */
    val emailChangePasswordUseCase by lazy {
        EmailChangePasswordUseCase(
            identifierRepository = clientUserRepositoryModule.identifierRepository
        )
    }
}
