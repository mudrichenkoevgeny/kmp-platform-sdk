package io.github.mudrichenkoevgeny.kmp.feature.managementuser.di

import com.arkivanov.decompose.ComponentContext
import io.github.mudrichenkoevgeny.kmp.core.common.di.CommonComponent
import io.github.mudrichenkoevgeny.kmp.core.security.di.SecurityComponent
import io.github.mudrichenkoevgeny.kmp.core.settings.di.SettingsComponent
import io.github.mudrichenkoevgeny.kmp.feature.auditapi.di.AuditApiComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.auth.UserAuthServices
import io.github.mudrichenkoevgeny.kmp.feature.user.di.UserStorageModule
import io.github.mudrichenkoevgeny.kmp.feature.user.model.apptype.AppType
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth.AuthStorage
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.auth.login.root.ManagementLoginRootComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.auth.login.root.ManagementLoginRootComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.UsersManagementRootComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.UsersManagementRootComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings.ManagementSettingsRootComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings.ManagementSettingsRootComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.ProfileRootComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.ProfileRootComponentImpl
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.action.CompositeAuditActionTypeParser
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.metadata.CommonAuditMetadataKey
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.metadata.CompositeAuditMetadataKeyParser
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.resource.CompositeAuditResourceTypeParser
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.audit.action.UserAuditActionType
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.audit.metadata.UserAuditMetadataKey
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.audit.resource.UserAuditResourceType
import io.github.mudrichenkoevgeny.shared.foundation.feature.securityapi.domain.audit.action.SecurityAuditActionType
import io.github.mudrichenkoevgeny.shared.foundation.feature.securityapi.domain.audit.resource.SecurityAuditResourceType
import io.github.mudrichenkoevgeny.shared.foundation.feature.settingsapi.domain.audit.action.SettingsAuditActionType
import io.github.mudrichenkoevgeny.shared.foundation.feature.settingsapi.domain.audit.resource.SettingsAuditResourceType
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
 * login dialog Decompose tree ([ManagementLoginRootComponent]).
 *
 * @param commonComponent Shared infrastructure from `core:common`.
 * @param settingsComponent Global settings repository access from `core:settings`.
 * @param securityComponent Security settings and validators from `core:security`.
 * @param authStorage Token and auth-settings persistence implementing [AuthStorage].
 * @param authServices Optional platform services (e.g. Google Sign-In); may provide a null Google delegate.
 * @param parentScope Optional coroutine scope for repository-driven work; if null, an internal supervisor scope is used.
 */
class ManagementUserComponent(
    val commonComponent: CommonComponent,
    val settingsComponent: SettingsComponent,
    val securityComponent: SecurityComponent,
    val authStorage: AuthStorage,
    val authServices: UserAuthServices,
    parentScope: CoroutineScope? = null
) {
    private val componentScope = parentScope ?: CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val storageModule = UserStorageModule(commonComponent.encryptedSettings)
    private val networkModule = ManagementUserNetworkModule(httpClient = commonComponent.httpClient)
    private val repositoryModule = ManagementUserRepositoryModule(
        networkModule = networkModule,
        authStorage = authStorage,
        encryptedSettings = commonComponent.encryptedSettings,
        storageModule = storageModule,
        webSocketService = commonComponent.webSocketService,
        repositoryScope = componentScope
    )

    private val auditApiComponent = AuditApiComponent(
        httpClient = commonComponent.httpClient,
        compositeAuditActionTypeParser = CompositeAuditActionTypeParser(
            setOf(
                UserAuditActionType.entries.first(),
                SecurityAuditActionType.entries.first(),
                SettingsAuditActionType.entries.first()
            )
        ),
        compositeAuditResourceTypeParser = CompositeAuditResourceTypeParser(
            setOf(
                UserAuditResourceType.entries.first(),
                SecurityAuditResourceType.entries.first(),
                SettingsAuditResourceType.entries.first()
            )
        ),
        compositeAuditMetadataKeyParser = CompositeAuditMetadataKeyParser(
            setOf(
                CommonAuditMetadataKey.entries.first(),
                UserAuditMetadataKey.entries.first()
            )
        )
    )

    /** Repository for self-management login (email/totp). */
    val loginRepository get() = repositoryModule.selfManagementLoginRepository

    /** Repository for current management user profile. */
    val userRepository get() = repositoryModule.selfManagementUserRepository

    /** Repository for password recovery in management context. */
    val passwordRepository get() = repositoryModule.selfManagementResetPasswordRepository

    /** Repository for management-specific authentication settings. */
    val authSettingsRepository get() = repositoryModule.managementAuthSettingsRepository

    /** Validator for password rules from core security. */
    val passwordPolicyValidator get() = securityComponent.passwordPolicyValidator

    private val useCaseModule = ManagementUserUseCaseModule(
        managementUserRepositoryModule = repositoryModule,
        authStorage = authStorage,
        storageModule = storageModule,
        authServices = authServices,
        managementUserConfigurationApi = networkModule.userConfigurationApi,
        managementAuthSettingsRepository = repositoryModule.managementAuthSettingsRepository,
        managementGlobalSettingsRepository = repositoryModule.managementGlobalSettingsRepository,
        managementSecuritySettingsRepository = repositoryModule.managementSecuritySettingsRepository
    )

    /** Refreshes the management session. */
    val refreshTokenUseCase get() = useCaseModule.refreshTokenUseCase

    /** Signs in as manager via email. */
    val loginByEmailUseCase get() = useCaseModule.loginByEmailUseCase

    /** Signs in as manager via TOTP. */
    val loginByTotpUseCase get() = useCaseModule.loginByTotpUseCase

    /** Signs in as manager via recovery code. */
    val loginByTotpRecoveryCodeUseCase get() = useCaseModule.loginByTotpRecoveryCodeUseCase

    /** Forces refresh of auth settings. */
    val refreshManagementAuthSettingsUseCase get() = useCaseModule.refreshManagementAuthSettingsUseCase

    /** Forces refresh of global settings. */
    val refreshManagementGlobalSettingsUseCase get() = useCaseModule.refreshManagementGlobalSettingsUseCase

    /** Forces refresh of security settings. */
    val refreshManagementSecuritySettingsUseCase get() = useCaseModule.refreshManagementSecuritySettingsUseCase

    /** Resets management password. */
    val resetEmailPasswordUseCase get() = useCaseModule.resetEmailPasswordUseCase

    /** Requests password reset code. */
    val sendResetPasswordConfirmationToEmailUseCase get() = useCaseModule.sendResetPasswordConfirmationToEmailUseCase

    /** Returns auth providers for management app. */
    val getAvailableUserAuthProvidersUseCase get() = useCaseModule.getAvailableUserAuthProvidersUseCase

    /** Refreshes full configuration for the management user. */
    val refreshUserConfigurationUseCase get() = useCaseModule.refreshUserConfigurationUseCase

    /** Signs out the manager. */
    val logoutUseCase get() = useCaseModule.logoutUseCase

    /** Schedules the manager account for deletion. */
    val scheduleUserDeletionUseCase get() = useCaseModule.scheduleUserDeletionUseCase

    /** Restores an account scheduled for deletion. */
    val restoreUserUseCase get() = useCaseModule.restoreUserUseCase

    /** Initiates TOTP setup for the manager. */
    val setupTotpUseCase get() = useCaseModule.setupTotpUseCase

    /** Enables TOTP for the manager. */
    val enableTotpUseCase get() = useCaseModule.enableTotpUseCase

    /** Disables TOTP for the manager. */
    val disableTotpUseCase get() = useCaseModule.disableTotpUseCase

    /** Returns active MFA recovery codes for the manager. */
    val getRecoveryCodesUseCase get() = useCaseModule.getRecoveryCodesUseCase

    /** Generates new MFA recovery codes for the manager. */
    val regenerateRecoveryCodesUseCase get() = useCaseModule.regenerateRecoveryCodesUseCase

    /** Returns active sessions for the manager. */
    val getSessionsUseCase get() = useCaseModule.getSessionsUseCase

    /** Revokes specific manager session. */
    val deleteSessionUseCase get() = useCaseModule.deleteSessionUseCase

    /** Revokes all other manager sessions. */
    val deleteAllOtherSessionsUseCase get() = useCaseModule.deleteAllOtherSessionsUseCase

    /** Returns account identifiers for the manager. */
    val getUserIdentifiersUseCase get() = useCaseModule.getUserIdentifiersUseCase

    /** Removes a manager identifier. */
    val deleteUserIdentifierUseCase get() = useCaseModule.deleteUserIdentifierUseCase

    /** Sends email confirmation for linking (manager). */
    val sendAddEmailIdentifierConfirmationUseCase get() = useCaseModule.sendAddEmailIdentifierConfirmationUseCase

    /** Links new email (manager). */
    val addUserIdentifierEmailUseCase get() = useCaseModule.addUserIdentifierEmailUseCase

    /** Sends phone confirmation for linking (manager). */
    val sendAddPhoneIdentifierConfirmationUseCase get() = useCaseModule.sendAddPhoneIdentifierConfirmationUseCase

    /** Links new phone (manager). */
    val addUserIdentifierPhoneUseCase get() = useCaseModule.addUserIdentifierPhoneUseCase

    /** Updates account password. */
    val emailChangePasswordUseCase get() = useCaseModule.emailChangePasswordUseCase

    /** Returns paginated list of users. */
    val getUsersUseCase get() = useCaseModule.getUsersUseCase

    /** Retrieves specific user details. */
    val getUserUseCase get() = useCaseModule.getUserUseCase

    /** Creates a new user account. */
    val createUserUseCase get() = useCaseModule.createUserUseCase

    /** Updates user details. */
    val updateUserUseCase get() = useCaseModule.updateUserUseCase

    /** Deletes user account. */
    val deleteUserUseCase get() = useCaseModule.deleteUserUseCase

    /** Retrieves user sessions administratively. */
    val managementGetSessionsUseCase get() = useCaseModule.managementGetSessionsUseCase

    /** Retrieves user identifiers administratively. */
    val managementGetIdentifiersUseCase get() = useCaseModule.managementGetIdentifiersUseCase

    /** Returns paginated audit events. */
    val getAuditEventsUseCase get() = auditApiComponent.getAuditEventsUseCase

    /** Retrieves specific audit event by ID. */
    val getAuditEventUseCase get() = auditApiComponent.getAuditEventUseCase

    /**
     * Creates the root Decompose component for the management profile flow.
     *
     * @param componentContext Decompose context for the new component.
     * @param onNavigateToLogin Invoked when the manager needs to sign in.
     * @return A new instance of [ProfileRootComponent].
     */
    fun createProfileComponent(
        componentContext: ComponentContext,
        onNavigateToLogin: () -> Unit
    ): ProfileRootComponent = ProfileRootComponentImpl(
        componentContext = componentContext,
        appType = AppType.MANAGEMENT,
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

    private val userWebSocketModule = ManagementUserWebSocketModule(
        userStorage = storageModule.userStorage,
        userRepository = repositoryModule.selfManagementUserRepository,
        refreshTokenUseCase = refreshTokenUseCase,
        scope = componentScope
    )

    /** WebSocket handler for management push events. */
    val userWebSocketMessageHandler get() = userWebSocketModule.userWebSocketMessageHandler

    /**
     * Creates the root Decompose component for management login flow.
     *
     * @param componentContext Decompose context.
     * @param onFinished Invoked on successful login.
     */
    fun createLoginRootDialogComponent(
        componentContext: ComponentContext,
        onFinished: () -> Unit
    ): ManagementLoginRootComponent = ManagementLoginRootComponentImpl(
        componentContext = componentContext,
        settingsComponent = settingsComponent,
        securityComponent = securityComponent,
        managementUserComponent = this,
        onFinished = onFinished
    )

    /** Repository for management-specific global settings. */
    val globalSettingsRepository get() = repositoryModule.managementGlobalSettingsRepository

    /** Repository for management-specific security settings. */
    val securitySettingsRepository get() = repositoryModule.managementSecuritySettingsRepository

    /** Returns management auth settings. */
    val getManagementAuthSettingsUseCase get() = useCaseModule.getManagementAuthSettingsUseCase

    /** Saves remote auth settings. */
    val saveRemoteAuthSettingsUseCase get() = useCaseModule.saveRemoteAuthSettingsUseCase

    /** Returns management global settings. `*/
    val getManagementGlobalSettingsUseCase get() = useCaseModule.getManagementGlobalSettingsUseCase

    /** Saves remote global settings. */
    val saveRemoteGlobalSettingsUseCase get() = useCaseModule.saveRemoteGlobalSettingsUseCase

    /** Returns management security settings. */
    val getManagementSecuritySettingsUseCase get() = useCaseModule.getManagementSecuritySettingsUseCase

    /** Saves remote security settings. */
    val saveRemoteSecuritySettingsUseCase get() = useCaseModule.saveRemoteSecuritySettingsUseCase

    /**
     * Creates the root Decompose component for users management flow.
     *
     * @param componentContext Decompose context.
     * @param onBack Navigation back callback.
     * @return A new instance of [UsersManagementRootComponent].
     */
    fun createUsersManagementComponent(
        componentContext: ComponentContext,
        onBack: () -> Unit
    ): UsersManagementRootComponent = UsersManagementRootComponentImpl(
        componentContext = componentContext,
        getUsersUseCase = getUsersUseCase,
        getUserUseCase = getUserUseCase,
        createUserUseCase = createUserUseCase,
        updateUserUseCase = updateUserUseCase,
        deleteUserUseCase = deleteUserUseCase,
        managementGetSessionsUseCase = managementGetSessionsUseCase,
        managementGetIdentifiersUseCase = managementGetIdentifiersUseCase,
        onBack = onBack
    )

    /**
     * Creates the root Decompose component for management settings flow (Auth, Global, Security, and Users settings).
     *
     * @param componentContext Decompose context for the component.
     * @return A new instance of [ManagementSettingsRootComponent].
     */
    fun createManagementSettingsComponent(
        componentContext: ComponentContext
    ): ManagementSettingsRootComponent =
        ManagementSettingsRootComponentImpl(
            componentContext = componentContext,
            getManagementAuthSettingsUseCase = getManagementAuthSettingsUseCase,
            saveRemoteAuthSettingsUseCase = saveRemoteAuthSettingsUseCase,
            getManagementGlobalSettingsUseCase = getManagementGlobalSettingsUseCase,
            saveRemoteGlobalSettingsUseCase = saveRemoteGlobalSettingsUseCase,
            getManagementSecuritySettingsUseCase = getManagementSecuritySettingsUseCase,
            saveRemoteSecuritySettingsUseCase = saveRemoteSecuritySettingsUseCase,
            getUsersUseCase = getUsersUseCase,
            getUserUseCase = getUserUseCase,
            createUserUseCase = createUserUseCase,
            updateUserUseCase = updateUserUseCase,
            deleteUserUseCase = deleteUserUseCase,
            managementGetSessionsUseCase = managementGetSessionsUseCase,
            managementGetIdentifiersUseCase = managementGetIdentifiersUseCase,
            getAuditEventsUseCase = getAuditEventsUseCase,
            getAuditEventUseCase = getAuditEventUseCase
        )
}
