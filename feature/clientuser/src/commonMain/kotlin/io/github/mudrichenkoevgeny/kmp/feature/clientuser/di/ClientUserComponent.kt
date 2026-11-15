package io.github.mudrichenkoevgeny.kmp.feature.clientuser.di

import com.arkivanov.decompose.ComponentContext
import io.github.mudrichenkoevgeny.kmp.core.common.di.CommonComponent
import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.messagehandler.WebSocketMessageHandler
import io.github.mudrichenkoevgeny.kmp.core.security.di.SecurityComponent
import io.github.mudrichenkoevgeny.kmp.core.settings.di.SettingsComponent
import io.github.mudrichenkoevgeny.kmp.core.storage.di.StorageComponent
import io.github.mudrichenkoevgeny.kmp.core.storage.repository.base.StorageRepository
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.login.root.ClientLoginRootComponent
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.login.root.ClientLoginRootComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.usecase.configuration.RefreshClientUserConfigurationUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.auth.UserAuthServices
import io.github.mudrichenkoevgeny.kmp.feature.user.di.UserStorageModule
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.login.LoginRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.registration.RegistrationRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.resetpassword.ResetPasswordRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.settings.OpenAuthSettingsRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.unlock.UnlockRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.user.UserRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth.AuthStorage
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.user.UserStorage
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.root.UserRootComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.root.UserRootComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.login.LoginByEmailUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.login.LoginByGoogleUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.login.LoginByPhoneUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.login.LoginByTotpRecoveryCodeUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.login.LoginByTotpUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.login.SendLoginConfirmationToPhoneUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.refreshtoken.RefreshTokenUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.registration.RegistrationByEmailUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.registration.SendRegistrationConfirmationToEmailUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.resetpassword.ResetEmailPasswordUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.resetpassword.SendResetPasswordConfirmationToEmailUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.settings.GetAuthSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.settings.GetAvailableUserAuthProvidersUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.settings.ObserveAuthSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.settings.RefreshOpenAuthSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.unlock.SendUnlockEmailConfirmationUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.unlock.SendUnlockPhoneConfirmationUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.unlock.UnlockByEmailUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.unlock.UnlockByExternalAuthProviderUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.unlock.UnlockByGoogleUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.unlock.UnlockByPhoneUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.identifier.AddUserIdentifierEmailUseCase
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
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.RestoreUserUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.ScheduleUserDeletionUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.security.DisableTotpUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.security.EnableTotpUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.security.GetRecoveryCodesUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.security.RegenerateRecoveryCodesUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.user.security.SetupTotpUseCase
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.apptype.AppType
import io.github.mudrichenkoevgeny.shared.foundation.core.security.passwordpolicy.validator.PasswordPolicyValidator
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
 * @param storageComponent Storage repositories and use cases from `core:storage`.
 * @param authStorage Token and auth-settings persistence implementing [AuthStorage].
 * @param authServices Optional platform services (e.g. Google Sign-In); may provide a null Google delegate.
 * @param parentScope Optional coroutine scope for repository-driven work; if null, an internal supervisor scope is used.
 */
public class ClientUserComponent(
    public val commonComponent: CommonComponent,
    public val settingsComponent: SettingsComponent,
    public val securityComponent: SecurityComponent,
    public val storageComponent: StorageComponent,
    public val authStorage: AuthStorage,
    public val authServices: UserAuthServices,
    parentScope: CoroutineScope? = null
) {

    private val componentScope = parentScope
        ?: CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val storageModule = UserStorageModule(commonComponent.encryptedSettings)

    /**
     * DataStore-backed storage for user and session metadata.
     */
    public val userStorage: UserStorage get() = storageModule.userStorage

    private val networkModule = ClientUserNetworkModule(
        httpClient = commonComponent.httpClient
    )

    private val clientUserRepositoryModule = ClientUserRepositoryModule(
        networkModule,
        authStorage,
        commonComponent.encryptedSettings,
        storageModule,
        commonComponent.webSocketService,
        componentScope
    )

    /** Repository for email, phone and external logins. */
    public val loginRepository: LoginRepository get() = clientUserRepositoryModule.loginRepository

    /** Repository for new user registration. */
    public val registrationRepository: RegistrationRepository get() = clientUserRepositoryModule.registrationRepository

    /** Repository for current user data and real-time updates. */
    public val userRepository: UserRepository
        get() = clientUserRepositoryModule.userRepository

    /** Repository for password recovery and change operations. */
    public val passwordRepository: ResetPasswordRepository
        get() = clientUserRepositoryModule.resetPasswordRepository

    /** Repository for self-service account unlocking. */
    public val unlockRepository: UnlockRepository
        get() = clientUserRepositoryModule.unlockRepository

    /** Repository for fetching available authentication providers and policies. */
    public val authSettingsRepository: OpenAuthSettingsRepository
        get() = clientUserRepositoryModule.openAuthSettingsRepository

    /** Foundation-level validator for password strength rules. */
    public val passwordPolicyValidator: PasswordPolicyValidator
        get() = securityComponent.passwordPolicyValidator

    private val useCaseModule = ClientUserUseCaseModule(
        clientUserRepositoryModule = clientUserRepositoryModule,
        authStorage = authStorage,
        storageModule = storageModule,
        authServices = authServices,
        openUserConfigurationApi = networkModule.userConfigurationApi,
        openAuthSettingsRepository = clientUserRepositoryModule.openAuthSettingsRepository,
        openGlobalSettingsRepository = settingsComponent.globalSettingsRepository,
        openSecuritySettingsRepository = securityComponent.securitySettingsRepository,
        openStorageSettingsRepository = storageComponent.openStorageSettingsRepository
    )

    /** Refreshes the session using the stored refresh token. */
    public val refreshTokenUseCase: RefreshTokenUseCase
        get() = useCaseModule.refreshTokenUseCase

    /** Signs in using email and password. */
    public val loginByEmailUseCase: LoginByEmailUseCase
        get() = useCaseModule.loginByEmailUseCase

    /** Signs in using a phone number and OTP code. */
    public val loginByPhoneUseCase: LoginByPhoneUseCase
        get() = useCaseModule.loginByPhoneUseCase

    /** Signs in using TOTP MFA code. */
    public val loginByTotpUseCase: LoginByTotpUseCase
        get() = useCaseModule.loginByTotpUseCase

    /** Signs in using MFA recovery code. */
    public val loginByTotpRecoveryCodeUseCase: LoginByTotpRecoveryCodeUseCase
        get() = useCaseModule.loginByTotpRecoveryCodeUseCase

    /** Sends a login verification code to the specified phone number. */
    public val sendLoginConfirmationToPhoneUseCase: SendLoginConfirmationToPhoneUseCase
        get() = useCaseModule.sendLoginConfirmationToPhoneUseCase

    /** Performs Google Sign-In using platform credentials. */
    public val loginByGoogleUseCase: LoginByGoogleUseCase
        get() = useCaseModule.loginByGoogleUseCase

    /** Refreshes the available auth providers from the network. */
    public val refreshOpenAuthSettingsUseCase: RefreshOpenAuthSettingsUseCase
        get() = useCaseModule.refreshOpenAuthSettingsUseCase

    /** Sends a registration verification code to the specified email. */
    public val sendRegistrationConfirmationToEmailUseCase: SendRegistrationConfirmationToEmailUseCase
        get() = useCaseModule.sendRegistrationConfirmationToEmailUseCase

    /** Completes the registration flow with an email and code. */
    public val registrationByEmailUseCase: RegistrationByEmailUseCase
        get() = useCaseModule.registrationByEmailUseCase

    /** Returns a filtered list of auth providers enabled for this deployment. */
    public val getAvailableUserAuthProvidersUseCase: GetAvailableUserAuthProvidersUseCase
        get() = useCaseModule.getAvailableUserAuthProvidersUseCase

    /** Resets the user's password using an email-delivered code. */
    public val resetEmailPasswordUseCase: ResetEmailPasswordUseCase
        get() = useCaseModule.resetEmailPasswordUseCase

    /** Sends a password-reset verification code to the specified email. */
    public val sendResetPasswordConfirmationToEmailUseCase: SendResetPasswordConfirmationToEmailUseCase
        get() = useCaseModule.sendResetPasswordConfirmationToEmailUseCase

    /** Sends an account unlock code to the specified email. */
    public val sendUnlockEmailConfirmationUseCase: SendUnlockEmailConfirmationUseCase
        get() = useCaseModule.sendUnlockEmailConfirmationUseCase

    /** Unlocks an account using an email code. */
    public val unlockByEmailUseCase: UnlockByEmailUseCase
        get() = useCaseModule.unlockByEmailUseCase

    /** Sends an account unlock code to the specified phone. */
    public val sendUnlockPhoneConfirmationUseCase: SendUnlockPhoneConfirmationUseCase
        get() = useCaseModule.sendUnlockPhoneConfirmationUseCase

    /** Unlocks an account using a phone code. */
    public val unlockByPhoneUseCase: UnlockByPhoneUseCase
        get() = useCaseModule.unlockByPhoneUseCase

    /** Unlocks an account using an external provider. */
    public val unlockByExternalAuthProviderUseCase: UnlockByExternalAuthProviderUseCase
        get() = useCaseModule.unlockByExternalAuthProviderUseCase

    /** Unlocks an account using Google. */
    public val unlockByGoogleUseCase: UnlockByGoogleUseCase
        get() = useCaseModule.unlockByGoogleUseCase

    /** Refreshes all user-related configurations (global, security, auth). */
    public val refreshUserConfigurationUseCase: RefreshClientUserConfigurationUseCase
        get() = useCaseModule.refreshUserConfigurationUseCase

    /** Signs out the current user and clears local session data. */
    public val logoutUseCase: LogoutUseCase
        get() = useCaseModule.logoutUseCase

    /** Schedules the current account for permanent deletion. */
    public val scheduleUserDeletionUseCase: ScheduleUserDeletionUseCase
        get() = useCaseModule.scheduleUserDeletionUseCase

    /** Restores an account scheduled for deletion. */
    public val restoreUserUseCase: RestoreUserUseCase
        get() = useCaseModule.restoreUserUseCase

    /** Initiates TOTP setup. */
    public val setupTotpUseCase: SetupTotpUseCase
        get() = useCaseModule.setupTotpUseCase

    /** Enables TOTP with a verification code. */
    public val enableTotpUseCase: EnableTotpUseCase
        get() = useCaseModule.enableTotpUseCase

    /** Disables TOTP. */
    public val disableTotpUseCase: DisableTotpUseCase
        get() = useCaseModule.disableTotpUseCase

    /** Returns active MFA recovery codes. */
    public val getRecoveryCodesUseCase: GetRecoveryCodesUseCase
        get() = useCaseModule.getRecoveryCodesUseCase

    /** Generates new MFA recovery codes. */
    public val regenerateRecoveryCodesUseCase: RegenerateRecoveryCodesUseCase
        get() = useCaseModule.regenerateRecoveryCodesUseCase

    /** Returns active sessions. */
    public val getSessionsUseCase: GetSessionsUseCase
        get() = useCaseModule.getSessionsUseCase

    /** Returns specific session details. */
    public val getSessionUseCase: GetSessionUseCase
        get() = useCaseModule.getSessionUseCase

    /** Revokes specific session. */
    public val deleteSessionUseCase: DeleteSessionUseCase
        get() = useCaseModule.deleteSessionUseCase

    /** Revokes all other sessions. */
    public val deleteAllOtherSessionsUseCase: DeleteAllOtherSessionsUseCase
        get() = useCaseModule.deleteAllOtherSessionsUseCase

    /** Re-authenticates session via TOTP to update its trust level. */
    public val reauthenticateSessionUseCase: ReauthenticateSessionUseCase
        get() = useCaseModule.reauthenticateSessionUseCase

    /** Returns account identifiers. */
    public val getUserIdentifiersUseCase: GetUserIdentifiersUseCase
        get() = useCaseModule.getUserIdentifiersUseCase

    /** Returns identifier details. */
    public val getUserIdentifierUseCase: GetUserIdentifierUseCase
        get() = useCaseModule.getUserIdentifierUseCase

    /** Removes an identifier. */
    public val deleteUserIdentifierUseCase: DeleteUserIdentifierUseCase
        get() = useCaseModule.deleteUserIdentifierUseCase

    /** Sends email confirmation for linking. */
    public val sendAddEmailIdentifierConfirmationUseCase: SendAddEmailIdentifierConfirmationUseCase
        get() = useCaseModule.sendAddEmailIdentifierConfirmationUseCase

    /** Links new email. */
    public val addUserIdentifierEmailUseCase: AddUserIdentifierEmailUseCase
        get() = useCaseModule.addUserIdentifierEmailUseCase

    /** Sends phone confirmation for linking. */
    public val sendAddPhoneIdentifierConfirmationUseCase: SendAddPhoneIdentifierConfirmationUseCase
        get() = useCaseModule.sendAddPhoneIdentifierConfirmationUseCase

    /** Links new phone. */
    public val addUserIdentifierPhoneUseCase: AddUserIdentifierPhoneUseCase
        get() = useCaseModule.addUserIdentifierPhoneUseCase

    /** Updates account password. */
    public val emailChangePasswordUseCase: EmailChangePasswordUseCase
        get() = useCaseModule.emailChangePasswordUseCase

    /** Returns current public auth settings. */
    public val getAuthSettingsUseCase: GetAuthSettingsUseCase
        get() = useCaseModule.getAuthSettingsUseCase

    /** Observes public auth settings updates. */
    public val observeAuthSettingsUseCase: ObserveAuthSettingsUseCase
        get() = useCaseModule.observeAuthSettingsUseCase

    /** Repository for open storage file operations. */
    public val openStorageRepository: StorageRepository
        get() = storageComponent.openStorageRepository

    private val userWebSocketModule = ClientUserWebSocketModule(
        userStorage = storageModule.userStorage,
        userRepository = userRepository,
        authStorage = authStorage,
        refreshTokenUseCase = refreshTokenUseCase,
        scope = componentScope
    )

    /** WebSocket handler for identity-related push events. */
    public val userWebSocketMessageHandler: WebSocketMessageHandler
        get() = userWebSocketModule.userWebSocketMessageHandler

    /**
     * Creates the root Decompose component for the user management flow.
     *
     * @param componentContext Decompose context for the new component.
     * @param onNavigateToLogin Invoked when the user needs to sign in.
     * @param onNavigateToClientStorageFiles Optional callback to navigate to self-service storage files screen.
     * @return A new instance of [UserRootComponent].
     */
    public fun createUserComponent(
        componentContext: ComponentContext,
        onNavigateToLogin: () -> Unit,
        onNavigateToClientStorageFiles: (() -> Unit)? = null
    ): UserRootComponent = UserRootComponentImpl(
        componentContext = componentContext,
        appType = AppType.CLIENT,
        userRepository = userRepository,
        authStorage = authStorage,
        identifierRepository = clientUserRepositoryModule.identifierRepository,
        logoutUseCase = logoutUseCase,
        scheduleUserDeletionUseCase = scheduleUserDeletionUseCase,
        getAvailableUserAuthProvidersUseCase = getAvailableUserAuthProvidersUseCase,
        getUserIdentifiersUseCase = getUserIdentifiersUseCase,
        sendAddEmailIdentifierConfirmationUseCase = sendAddEmailIdentifierConfirmationUseCase,
        addUserIdentifierEmailUseCase = addUserIdentifierEmailUseCase,
        sendAddPhoneIdentifierConfirmationUseCase = sendAddPhoneIdentifierConfirmationUseCase,
        addUserIdentifierPhoneUseCase = addUserIdentifierPhoneUseCase,
        addUserIdentifierGoogleUseCase = useCaseModule.addUserIdentifierGoogleUseCase,
        selfUpdateUserUseCase = useCaseModule.selfUpdateUserUseCase,
        validateNicknameUseCase = useCaseModule.validateNicknameUseCase,
        validateDisplayNameUseCase = useCaseModule.validateDisplayNameUseCase,
        getSessionsUseCase = getSessionsUseCase,
        deleteSessionUseCase = deleteSessionUseCase,
        deleteAllOtherSessionsUseCase = deleteAllOtherSessionsUseCase,
        setupTotpUseCase = setupTotpUseCase,
        enableTotpUseCase = enableTotpUseCase,
        disableTotpUseCase = disableTotpUseCase,
        getRecoveryCodesUseCase = getRecoveryCodesUseCase,
        regenerateRecoveryCodesUseCase = regenerateRecoveryCodesUseCase,
        getSessionUseCase = getSessionUseCase,
        getUserIdentifierUseCase = getUserIdentifierUseCase,
        deleteUserIdentifierUseCase = deleteUserIdentifierUseCase,
        emailChangePasswordUseCase = emailChangePasswordUseCase,
        openStorageDependencies = storageComponent.createStorageDependencies(storageComponent.openStorageRepository),
        onNavigateToClientStorageFiles = onNavigateToClientStorageFiles,
        onNavigateToLogin = onNavigateToLogin
    )

    /**
     * Creates the root Decompose component for the login and registration flow.
     *
     * @param componentContext Decompose context for the new component.
     * @param onFinished Invoked when the authentication flow is successfully completed.
     * @return A new instance of [ClientLoginRootComponent].
     */
    public fun createLoginRootDialogComponent(
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
