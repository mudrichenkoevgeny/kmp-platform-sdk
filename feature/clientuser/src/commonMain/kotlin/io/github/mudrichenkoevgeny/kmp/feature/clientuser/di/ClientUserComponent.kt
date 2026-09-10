package io.github.mudrichenkoevgeny.kmp.feature.clientuser.di

import com.arkivanov.decompose.ComponentContext
import io.github.mudrichenkoevgeny.kmp.core.common.di.CommonComponent
import io.github.mudrichenkoevgeny.kmp.core.security.di.SecurityComponent
import io.github.mudrichenkoevgeny.kmp.core.settings.di.SettingsComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.auth.UserAuthServices
import io.github.mudrichenkoevgeny.kmp.feature.user.di.UserStorageModule
import io.github.mudrichenkoevgeny.kmp.feature.user.model.apptype.AppType
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth.AuthStorage
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.login.root.ClientLoginRootComponent
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.login.root.ClientLoginRootComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.ProfileRootComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.ProfileRootComponentImpl
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
 * @param authStorage Token and auth-settings persistence implementing [AuthStorage].
 * @param authServices Optional platform services (e.g. Google Sign-In); may provide a null Google delegate.
 * @param parentScope Optional coroutine scope for repository-driven work; if null, an internal supervisor scope is used.
 */
class ClientUserComponent(
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

    /**
     * DataStore-backed storage for user profile and session metadata.
     */
    val userStorage get() = storageModule.userStorage

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
    val loginRepository get() = clientUserRepositoryModule.loginRepository

    /** Repository for new user registration. */
    val registrationRepository get() = clientUserRepositoryModule.registrationRepository

    /** Repository for current user profile data and real-time updates. */
    val userRepository get() = clientUserRepositoryModule.userRepository

    /** Repository for password recovery and change operations. */
    val passwordRepository get() = clientUserRepositoryModule.resetPasswordRepository

    /** Repository for fetching available authentication providers and policies. */
    val authSettingsRepository get() = clientUserRepositoryModule.openAuthSettingsRepository

    /** Foundation-level validator for password strength rules. */
    val passwordPolicyValidator get() = securityComponent.passwordPolicyValidator

    private val useCaseModule = ClientUserUseCaseModule(
        clientUserRepositoryModule = clientUserRepositoryModule,
        authStorage = authStorage,
        storageModule = storageModule,
        authServices = authServices,
        openUserConfigurationApi = networkModule.userConfigurationApi,
        openAuthSettingsRepository = clientUserRepositoryModule.openAuthSettingsRepository,
        openGlobalSettingsRepository = settingsComponent.globalSettingsRepository,
        openSecuritySettingsRepository = securityComponent.securitySettingsRepository
    )

    /** Refreshes the session using the stored refresh token. */
    val refreshTokenUseCase get() = useCaseModule.refreshTokenUseCase

    /** Signs in using email and password. */
    val loginByEmailUseCase get() = useCaseModule.loginByEmailUseCase

    /** Signs in using a phone number and OTP code. */
    val loginByPhoneUseCase get() = useCaseModule.loginByPhoneUseCase

    /** Signs in using TOTP MFA code. */
    val loginByTotpUseCase get() = useCaseModule.loginByTotpUseCase

    /** Signs in using MFA recovery code. */
    val loginByTotpRecoveryCodeUseCase get() = useCaseModule.loginByTotpRecoveryCodeUseCase

    /** Sends a login verification code to the specified phone number. */
    val sendLoginConfirmationToPhoneUseCase get() = useCaseModule.sendLoginConfirmationToPhoneUseCase

    /** Performs Google Sign-In using platform credentials. */
    val loginByGoogleUseCase get() = useCaseModule.loginByGoogleUseCase

    /** Refreshes the available auth providers from the network. */
    val refreshOpenAuthSettingsUseCase get() = useCaseModule.refreshOpenAuthSettingsUseCase

    /** Sends a registration verification code to the specified email. */
    val sendRegistrationConfirmationToEmailUseCase get() = useCaseModule.sendRegistrationConfirmationToEmailUseCase

    /** Completes the registration flow with an email and code. */
    val registrationByEmailUseCase get() = useCaseModule.registrationByEmailUseCase

    /** Returns a filtered list of auth providers enabled for this deployment. */
    val getAvailableUserAuthProvidersUseCase get() = useCaseModule.getAvailableUserAuthProvidersUseCase

    /** Resets the user's password using an email-delivered code. */
    val resetEmailPasswordUseCase get() = useCaseModule.resetEmailPasswordUseCase

    /** Sends a password-reset verification code to the specified email. */
    val sendResetPasswordConfirmationToEmailUseCase get() = useCaseModule.sendResetPasswordConfirmationToEmailUseCase

    /** Refreshes all user-related configurations (global, security, auth). */
    val refreshUserConfigurationUseCase get() = useCaseModule.refreshUserConfigurationUseCase

    /** Signs out the current user and clears local session data. */
    val logoutUseCase get() = useCaseModule.logoutUseCase

    /** Schedules the current account for permanent deletion. */
    val scheduleUserDeletionUseCase get() = useCaseModule.scheduleUserDeletionUseCase

    /** Restores an account scheduled for deletion. */
    val restoreUserUseCase get() = useCaseModule.restoreUserUseCase

    /** Initiates TOTP setup. */
    val setupTotpUseCase get() = useCaseModule.setupTotpUseCase

    /** Enables TOTP with a verification code. */
    val enableTotpUseCase get() = useCaseModule.enableTotpUseCase

    /** Disables TOTP. */
    val disableTotpUseCase get() = useCaseModule.disableTotpUseCase

    /** Returns active MFA recovery codes. */
    val getRecoveryCodesUseCase get() = useCaseModule.getRecoveryCodesUseCase

    /** Generates new MFA recovery codes. */
    val regenerateRecoveryCodesUseCase get() = useCaseModule.regenerateRecoveryCodesUseCase

    /** Returns active sessions. */
    val getSessionsUseCase get() = useCaseModule.getSessionsUseCase

    /** Revokes specific session. */
    val deleteSessionUseCase get() = useCaseModule.deleteSessionUseCase

    /** Revokes all other sessions. */
    val deleteAllOtherSessionsUseCase get() = useCaseModule.deleteAllOtherSessionsUseCase

    /** Returns account identifiers. */
    val getUserIdentifiersUseCase get() = useCaseModule.getUserIdentifiersUseCase

    /** Removes an identifier. */
    val deleteUserIdentifierUseCase get() = useCaseModule.deleteUserIdentifierUseCase

    /** Sends email confirmation for linking. */
    val sendAddEmailIdentifierConfirmationUseCase get() = useCaseModule.sendAddEmailIdentifierConfirmationUseCase

    /** Links new email. */
    val addUserIdentifierEmailUseCase get() = useCaseModule.addUserIdentifierEmailUseCase

    /** Sends phone confirmation for linking. */
    val sendAddPhoneIdentifierConfirmationUseCase get() = useCaseModule.sendAddPhoneIdentifierConfirmationUseCase

    /** Links new phone. */
    val addUserIdentifierPhoneUseCase get() = useCaseModule.addUserIdentifierPhoneUseCase

    /** Updates account password. */
    val emailChangePasswordUseCase get() = useCaseModule.emailChangePasswordUseCase

    /**
     * Creates the root Decompose component for the profile management flow.
     *
     * @param componentContext Decompose context for the new component.
     * @param onNavigateToLogin Invoked when the user needs to sign in from the profile.
     * @return A new instance of [ProfileRootComponent].
     */
    fun createProfileComponent(
        componentContext: ComponentContext,
        onNavigateToLogin: () -> Unit
    ): ProfileRootComponent = ProfileRootComponentImpl(
        componentContext = componentContext,
        appType = AppType.CLIENT,
        userRepository = userRepository,
        logoutUseCase = logoutUseCase,
        scheduleUserDeletionUseCase = scheduleUserDeletionUseCase,
        restoreUserUseCase = restoreUserUseCase,
        setupTotpUseCase = setupTotpUseCase,
        enableTotpUseCase = enableTotpUseCase,
        disableTotpUseCase = disableTotpUseCase,
        getRecoveryCodesUseCase = getRecoveryCodesUseCase,
        regenerateRecoveryCodesUseCase = regenerateRecoveryCodesUseCase,
        getSessionsUseCase = getSessionsUseCase,
        deleteSessionUseCase = deleteSessionUseCase,
        deleteAllOtherSessionsUseCase = deleteAllOtherSessionsUseCase,
        getUserIdentifiersUseCase = getUserIdentifiersUseCase,
        deleteUserIdentifierUseCase = deleteUserIdentifierUseCase,
        sendAddEmailIdentifierConfirmationUseCase = sendAddEmailIdentifierConfirmationUseCase,
        addUserIdentifierEmailUseCase = addUserIdentifierEmailUseCase,
        sendAddPhoneIdentifierConfirmationUseCase = sendAddPhoneIdentifierConfirmationUseCase,
        addUserIdentifierPhoneUseCase = addUserIdentifierPhoneUseCase,
        emailChangePasswordUseCase = emailChangePasswordUseCase,
        onNavigateToLogin = onNavigateToLogin
    )

    private val userWebSocketModule = ClientUserWebSocketModule(
        userStorage = storageModule.userStorage,
        userRepository = userRepository,
        refreshTokenUseCase = refreshTokenUseCase,
        scope = componentScope
    )

    /** WebSocket handler for identity-related push events. */
    val userWebSocketMessageHandler get() = userWebSocketModule.userWebSocketMessageHandler

    /**
     * Creates the root Decompose component for the login and registration flow.
     *
     * @param componentContext Decompose context for the new component.
     * @param onFinished Invoked when the authentication flow is successfully completed.
     * @return A new instance of [ClientLoginRootComponent].
     */
    fun createLoginRootDialogComponent(
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
