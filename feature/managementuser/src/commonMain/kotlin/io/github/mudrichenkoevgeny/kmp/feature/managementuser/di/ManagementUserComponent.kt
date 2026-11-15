package io.github.mudrichenkoevgeny.kmp.feature.managementuser.di

import com.arkivanov.decompose.ComponentContext
import io.github.mudrichenkoevgeny.kmp.core.common.di.CommonComponent
import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.messagehandler.WebSocketMessageHandler
import io.github.mudrichenkoevgeny.kmp.core.security.di.SecurityComponent
import io.github.mudrichenkoevgeny.kmp.core.settings.di.SettingsComponent
import io.github.mudrichenkoevgeny.kmp.core.storage.di.ManagementStorageDependencies
import io.github.mudrichenkoevgeny.kmp.core.storage.di.StorageComponent
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.management.GetManagementStorageSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.management.ManagementDeleteFileUseCase
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.management.ManagementDeleteFilesBatchUseCase
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.management.ManagementGetFileDetailUseCase
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.management.ManagementGetFilesUseCase
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.management.ObserveManagementStorageSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.management.RefreshManagementStorageSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.management.ResetRemoteManagementStorageSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.core.storage.usecase.management.UpdateManagementStorageSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.di.audit.AuditApiComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.auth.settings.ManagementAuthSettingsRepository
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.globalsettings.ManagementGlobalSettingsRepository
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.security.settings.ManagementSecuritySettingsRepository
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.auth.login.root.ManagementLoginRootComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.auth.login.root.ManagementLoginRootComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.root.ManagementRootComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.root.ManagementRootComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.audit.GetAuditEventUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.audit.GetAuditEventsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.auth.settings.GetManagementAuthSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.auth.settings.RefreshManagementAuthSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.auth.settings.ResetRemoteAuthSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.auth.settings.SaveRemoteAuthSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.configuration.RefreshManagementUserConfigurationUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.globalsettings.GetManagementGlobalSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.globalsettings.RefreshManagementGlobalSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.globalsettings.ResetRemoteGlobalSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.globalsettings.SaveRemoteGlobalSettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.identifier.ManagementDeleteIdentifierPasswordUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.identifier.ManagementGetIdentifierUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.identifier.ManagementGetIdentifiersUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.identifier.ManagementGetUserIdentifiersUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.security.settings.GetManagementSecuritySettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.security.settings.RefreshManagementSecuritySettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.security.settings.ResetRemoteSecuritySettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.security.settings.SaveRemoteSecuritySettingsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session.ManagementDeleteAllUserSessionsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session.ManagementDeleteSessionUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session.ManagementGetSessionUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session.ManagementGetSessionsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session.ManagementGetUserSessionsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.CreateUserUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.DeleteUserUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.GetUserUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.GetUsersUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.UpdateUserUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.user.security.ManagementDisableTotpUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.auth.UserAuthServices
import io.github.mudrichenkoevgeny.kmp.feature.user.di.UserStorageModule
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.login.LoginRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.resetpassword.ResetPasswordRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.unlock.UnlockRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.user.UserRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth.AuthStorage
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.root.UserRootComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.root.UserRootComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.login.LoginByEmailUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.login.LoginByTotpRecoveryCodeUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.login.LoginByTotpUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.refreshtoken.RefreshTokenUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.resetpassword.ResetEmailPasswordUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.resetpassword.SendResetPasswordConfirmationToEmailUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.settings.GetAvailableUserAuthProvidersUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.unlock.SendUnlockEmailConfirmationUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.unlock.SendUnlockPhoneConfirmationUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.unlock.UnlockByEmailUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.unlock.UnlockByExternalAuthProviderUseCase
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
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.action.CompositeAuditActionTypeParser
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.metadata.CommonAuditMetadataKey
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.metadata.CompositeAuditMetadataKeyParser
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.resource.CompositeAuditResourceTypeParser
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.apptype.AppType
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.audit.action.SecurityAuditActionType
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.audit.resource.SecurityAuditResourceType
import io.github.mudrichenkoevgeny.shared.foundation.core.security.passwordpolicy.validator.PasswordPolicyValidator
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.domain.audit.action.SettingsAuditActionType
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.domain.audit.resource.SettingsAuditResourceType
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.audit.action.UserAuditActionType
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.audit.metadata.UserAuditMetadataKey
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.audit.resource.UserAuditResourceType
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
 * @param storageComponent Storage repositories and use cases from `core:storage`.
 * @param authStorage Token and auth-settings persistence implementing [AuthStorage].
 * @param authServices Optional platform services (e.g. Google Sign-In); may provide a null Google delegate.
 * @param parentScope Optional coroutine scope for repository-driven work; if null, an internal supervisor scope is used.
 */
public class ManagementUserComponent(
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
            listOf(
                UserAuditActionType::fromValueOrNull,
                SecurityAuditActionType::fromValueOrNull,
                SettingsAuditActionType::fromValueOrNull
            )
        ),
        compositeAuditResourceTypeParser = CompositeAuditResourceTypeParser(
            listOf(
                UserAuditResourceType::fromValueOrNull,
                SecurityAuditResourceType::fromValueOrNull,
                SettingsAuditResourceType::fromValueOrNull
            )
        ),
        compositeAuditMetadataKeyParser = CompositeAuditMetadataKeyParser(
            listOf(
                CommonAuditMetadataKey::fromValueOrNull,
                UserAuditMetadataKey::fromValueOrNull
            )
        )
    )

    /** Repository for self-management login (email/totp). */
    public val loginRepository: LoginRepository
        get() = repositoryModule.selfManagementLoginRepository

    /** Repository for self-management account unlock operations. */
    public val unlockRepository: UnlockRepository
        get() = repositoryModule.selfManagementUnlockRepository

    /** Repository for current management user data. */
    public val userRepository: UserRepository
        get() = repositoryModule.selfManagementUserRepository

    /** Repository for password recovery in management context. */
    public val passwordRepository: ResetPasswordRepository
        get() = repositoryModule.selfManagementResetPasswordRepository

    /** Repository for management-specific authentication settings. */
    public val authSettingsRepository: ManagementAuthSettingsRepository
        get() = repositoryModule.managementAuthSettingsRepository

    /** Validator for password rules from core security. */
    public val passwordPolicyValidator: PasswordPolicyValidator
        get() = securityComponent.passwordPolicyValidator

    private val useCaseModule = ManagementUserUseCaseModule(
        managementUserRepositoryModule = repositoryModule,
        authStorage = authStorage,
        storageModule = storageModule,
        authServices = authServices,
        managementUserConfigurationApi = networkModule.managementUserConfigurationApi,
        openGlobalSettingsRepository = settingsComponent.globalSettingsRepository,
        openSecuritySettingsRepository = securityComponent.securitySettingsRepository,
        openStorageSettingsRepository = storageComponent.openStorageSettingsRepository,
        openAuthSettingsRepository = repositoryModule.openAuthSettingsRepository,
        managementAuthSettingsRepository = repositoryModule.managementAuthSettingsRepository,
        managementGlobalSettingsRepository = repositoryModule.managementGlobalSettingsRepository,
        managementSecuritySettingsRepository = repositoryModule.managementSecuritySettingsRepository
    )

    /** Refreshes the management session. */
    public val refreshTokenUseCase: RefreshTokenUseCase
        get() = useCaseModule.refreshTokenUseCase

    /** Signs in as manager via email. */
    public val loginByEmailUseCase: LoginByEmailUseCase
        get() = useCaseModule.loginByEmailUseCase

    /** Signs in as manager via TOTP. */
    public val loginByTotpUseCase: LoginByTotpUseCase
        get() = useCaseModule.loginByTotpUseCase

    /** Signs in as manager via recovery code. */
    public val loginByTotpRecoveryCodeUseCase: LoginByTotpRecoveryCodeUseCase
        get() = useCaseModule.loginByTotpRecoveryCodeUseCase

    /** Forces refresh of auth settings. */
    public val refreshManagementAuthSettingsUseCase: RefreshManagementAuthSettingsUseCase
        get() = useCaseModule.refreshManagementAuthSettingsUseCase

    /** Forces refresh of global settings. */
    public val refreshManagementGlobalSettingsUseCase: RefreshManagementGlobalSettingsUseCase
        get() = useCaseModule.refreshManagementGlobalSettingsUseCase

    /** Forces refresh of security settings. */
    public val refreshManagementSecuritySettingsUseCase: RefreshManagementSecuritySettingsUseCase
        get() = useCaseModule.refreshManagementSecuritySettingsUseCase

    /** Resets management password. */
    public val resetEmailPasswordUseCase: ResetEmailPasswordUseCase
        get() = useCaseModule.resetEmailPasswordUseCase

    /** Requests password reset code. */
    public val sendResetPasswordConfirmationToEmailUseCase: SendResetPasswordConfirmationToEmailUseCase
        get() = useCaseModule.sendResetPasswordConfirmationToEmailUseCase

    /** Returns auth providers for management app. */
    public val getAvailableUserAuthProvidersUseCase: GetAvailableUserAuthProvidersUseCase
        get() = useCaseModule.getAvailableUserAuthProvidersUseCase

    /** Refreshes full configuration for the management user. */
    public val refreshUserConfigurationUseCase: RefreshManagementUserConfigurationUseCase
        get() = useCaseModule.refreshUserConfigurationUseCase

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

    /** Signs out the manager. */
    public val logoutUseCase: LogoutUseCase
        get() = useCaseModule.logoutUseCase

    /** Schedules the manager account for deletion. */
    public val scheduleUserDeletionUseCase: ScheduleUserDeletionUseCase
        get() = useCaseModule.scheduleUserDeletionUseCase

    /** Restores an account scheduled for deletion. */
    public val restoreUserUseCase: RestoreUserUseCase
        get() = useCaseModule.restoreUserUseCase

    /** Initiates TOTP setup for the manager. */
    public val setupTotpUseCase: SetupTotpUseCase
        get() = useCaseModule.setupTotpUseCase

    /** Enables TOTP for the manager. */
    public val enableTotpUseCase: EnableTotpUseCase
        get() = useCaseModule.enableTotpUseCase

    /** Disables TOTP for the manager. */
    public val disableTotpUseCase: DisableTotpUseCase
        get() = useCaseModule.disableTotpUseCase

    /** Returns active MFA recovery codes for the manager. */
    public val getRecoveryCodesUseCase: GetRecoveryCodesUseCase
        get() = useCaseModule.getRecoveryCodesUseCase

    /** Generates new MFA recovery codes for the manager. */
    public val regenerateRecoveryCodesUseCase: RegenerateRecoveryCodesUseCase
        get() = useCaseModule.regenerateRecoveryCodesUseCase

    /** Returns active sessions for the manager. */
    public val getSessionsUseCase: GetSessionsUseCase
        get() = useCaseModule.getSessionsUseCase

    /** Returns specific session details for the manager. */
    public val getSessionUseCase: GetSessionUseCase
        get() = useCaseModule.getSessionUseCase

    /** Revokes specific manager session. */
    public val deleteSessionUseCase: DeleteSessionUseCase
        get() = useCaseModule.deleteSessionUseCase

    /** Removes a user identifier password administratively. */
    public val deleteIdentifierPasswordUseCase: ManagementDeleteIdentifierPasswordUseCase
        get() = useCaseModule.managementDeleteIdentifierPasswordUseCase

    /** Revokes all other manager sessions. */
    public val deleteAllOtherSessionsUseCase: DeleteAllOtherSessionsUseCase
        get() = useCaseModule.deleteAllOtherSessionsUseCase

    /** Re-authenticates session via TOTP to update its trust level. */
    public val reauthenticateSessionUseCase: ReauthenticateSessionUseCase
        get() = useCaseModule.reauthenticateSessionUseCase

    /** Returns account identifiers for the manager. */
    public val getUserIdentifiersUseCase: GetUserIdentifiersUseCase
        get() = useCaseModule.getUserIdentifiersUseCase

    /** Returns identifier details for the manager. */
    public val getUserIdentifierUseCase: GetUserIdentifierUseCase
        get() = useCaseModule.getUserIdentifierUseCase

    /** Removes a manager identifier. */
    public val deleteUserIdentifierUseCase: DeleteUserIdentifierUseCase
        get() = useCaseModule.deleteUserIdentifierUseCase

    /** Sends email confirmation for linking (manager). */
    public val sendAddEmailIdentifierConfirmationUseCase: SendAddEmailIdentifierConfirmationUseCase
        get() = useCaseModule.sendAddEmailIdentifierConfirmationUseCase

    /** Links new email (manager). */
    public val addUserIdentifierEmailUseCase: AddUserIdentifierEmailUseCase
        get() = useCaseModule.addUserIdentifierEmailUseCase

    /** Sends phone confirmation for linking (manager). */
    public val sendAddPhoneIdentifierConfirmationUseCase: SendAddPhoneIdentifierConfirmationUseCase
        get() = useCaseModule.sendAddPhoneIdentifierConfirmationUseCase

    /** Links new phone (manager). */
    public val addUserIdentifierPhoneUseCase: AddUserIdentifierPhoneUseCase
        get() = useCaseModule.addUserIdentifierPhoneUseCase

    /** Updates account password. */
    public val emailChangePasswordUseCase: EmailChangePasswordUseCase
        get() = useCaseModule.emailChangePasswordUseCase

    /** Returns paginated list of users. */
    public val getUsersUseCase: GetUsersUseCase
        get() = useCaseModule.getUsersUseCase

    /** Retrieves specific user details. */
    public val getUserUseCase: GetUserUseCase
        get() = useCaseModule.getUserUseCase

    /** Creates a new user account. */
    public val createUserUseCase: CreateUserUseCase
        get() = useCaseModule.createUserUseCase

    /** Updates user details. */
    public val updateUserUseCase: UpdateUserUseCase
        get() = useCaseModule.updateUserUseCase

    /** Deletes user account. */
    public val deleteUserUseCase: DeleteUserUseCase
        get() = useCaseModule.deleteUserUseCase

    /** Retrieves user sessions administratively. */
    public val managementGetSessionsUseCase: ManagementGetSessionsUseCase
        get() = useCaseModule.managementGetSessionsUseCase

    /** Retrieves active user sessions for a specific user administratively. */
    public val managementGetUserSessionsUseCase: ManagementGetUserSessionsUseCase
        get() = useCaseModule.managementGetUserSessionsUseCase

    /** Retrieves specific user session details administratively. */
    public val managementGetSessionUseCase: ManagementGetSessionUseCase
        get() = useCaseModule.managementGetSessionUseCase

    /** Retrieves user identifiers administratively. */
    public val managementGetIdentifiersUseCase: ManagementGetIdentifiersUseCase
        get() = useCaseModule.managementGetIdentifiersUseCase

    /** Retrieves user identifiers for a specific user administratively. */
    public val managementGetUserIdentifiersUseCase: ManagementGetUserIdentifiersUseCase
        get() = useCaseModule.managementGetUserIdentifiersUseCase

    /** Retrieves specific user identifier details administratively. */
    public val managementGetIdentifierUseCase: ManagementGetIdentifierUseCase
        get() = useCaseModule.managementGetIdentifierUseCase

    /** Disables TOTP for a specific user administratively. */
    public val managementDisableTotpUseCase: ManagementDisableTotpUseCase
        get() = useCaseModule.managementDisableTotpUseCase

    /** Revokes all sessions for a specific user administratively. */
    public val managementDeleteAllUserSessionsUseCase: ManagementDeleteAllUserSessionsUseCase
        get() = useCaseModule.managementDeleteAllUserSessionsUseCase

    /** Revokes a specific session for a user administratively. */
    public val managementDeleteSessionUseCase: ManagementDeleteSessionUseCase
        get() = useCaseModule.managementDeleteSessionUseCase

    /** Returns paginated audit events. */
    public val getAuditEventsUseCase: GetAuditEventsUseCase
        get() = auditApiComponent.getAuditEventsUseCase

    /** Retrieves specific audit event by ID. */
    public val getAuditEventUseCase: GetAuditEventUseCase
        get() = auditApiComponent.getAuditEventUseCase

    private val userWebSocketModule = ManagementUserWebSocketModule(
        userStorage = storageModule.userStorage,
        userRepository = repositoryModule.selfManagementUserRepository,
        authStorage = authStorage,
        refreshTokenUseCase = refreshTokenUseCase,
        scope = componentScope
    )

    /** WebSocket handler for management push events. */
    public val userWebSocketMessageHandler: WebSocketMessageHandler
        get() = userWebSocketModule.userWebSocketMessageHandler

    /** Repository for management-specific global settings. */
    public val globalSettingsRepository: ManagementGlobalSettingsRepository
        get() = repositoryModule.managementGlobalSettingsRepository

    /** Repository for management-specific security settings. */
    public val securitySettingsRepository: ManagementSecuritySettingsRepository
        get() = repositoryModule.managementSecuritySettingsRepository

    /** Returns management auth settings. */
    public val getManagementAuthSettingsUseCase: GetManagementAuthSettingsUseCase
        get() = useCaseModule.getManagementAuthSettingsUseCase

    /** Saves remote auth settings. */
    public val saveRemoteAuthSettingsUseCase: SaveRemoteAuthSettingsUseCase
        get() = useCaseModule.saveRemoteAuthSettingsUseCase

    /** Resets remote auth settings. */
    public val resetRemoteAuthSettingsUseCase: ResetRemoteAuthSettingsUseCase
        get() = useCaseModule.resetRemoteAuthSettingsUseCase

    /** Returns management global settings. */
    public val getManagementGlobalSettingsUseCase: GetManagementGlobalSettingsUseCase
        get() = useCaseModule.getManagementGlobalSettingsUseCase

    /** Saves remote global settings. */
    public val saveRemoteGlobalSettingsUseCase: SaveRemoteGlobalSettingsUseCase
        get() = useCaseModule.saveRemoteGlobalSettingsUseCase

    /** Resets remote global settings. */
    public val resetRemoteGlobalSettingsUseCase: ResetRemoteGlobalSettingsUseCase
        get() = useCaseModule.resetRemoteGlobalSettingsUseCase

    /** Returns management security settings. */
    public val getManagementSecuritySettingsUseCase: GetManagementSecuritySettingsUseCase
        get() = useCaseModule.getManagementSecuritySettingsUseCase

    /** Saves remote security settings. */
    public val saveRemoteSecuritySettingsUseCase: SaveRemoteSecuritySettingsUseCase
        get() = useCaseModule.saveRemoteSecuritySettingsUseCase

    /** Resets remote security settings. */
    public val resetRemoteSecuritySettingsUseCase: ResetRemoteSecuritySettingsUseCase
        get() = useCaseModule.resetRemoteSecuritySettingsUseCase

    /** Administrative storage file listing. */
    public val managementGetFilesUseCase: ManagementGetFilesUseCase
        get() = storageComponent.managementGetFilesUseCase

    /** Administrative storage file detail. */
    public val managementGetFileDetailUseCase: ManagementGetFileDetailUseCase
        get() = storageComponent.managementGetFileDetailUseCase

    /** Administrative storage file deletion. */
    public val managementDeleteFileUseCase: ManagementDeleteFileUseCase
        get() = storageComponent.managementDeleteFileUseCase

    /** Administrative storage file batch deletion. */
    public val managementDeleteFilesBatchUseCase: ManagementDeleteFilesBatchUseCase
        get() = storageComponent.managementDeleteFilesBatchUseCase

    /** Bundles management storage dependencies. */
    public val managementStorageDependencies: ManagementStorageDependencies
        get() = storageComponent.managementStorageDependencies

    /** Returns management storage settings. */
    public val getManagementStorageSettingsUseCase: GetManagementStorageSettingsUseCase
        get() = storageComponent.getManagementStorageSettingsUseCase

    /** Refreshes management storage settings. */
    public val refreshManagementStorageSettingsUseCase: RefreshManagementStorageSettingsUseCase
        get() = storageComponent.refreshManagementStorageSettingsUseCase

    /** Updates remote management storage settings. */
    public val updateManagementStorageSettingsUseCase: UpdateManagementStorageSettingsUseCase
        get() = storageComponent.updateManagementStorageSettingsUseCase

    /** Resets remote management storage settings. */
    public val resetRemoteManagementStorageSettingsUseCase: ResetRemoteManagementStorageSettingsUseCase
        get() = storageComponent.resetRemoteManagementStorageSettingsUseCase

    /** Observes management storage settings flow. */
    public val observeManagementStorageSettingsUseCase: ObserveManagementStorageSettingsUseCase
        get() = storageComponent.observeManagementStorageSettingsUseCase

    /**
     * Creates the root Decompose component for the management user flow.
     *
     * @param componentContext Decompose context for the new component.
     * @param onNavigateToLogin Invoked when the manager needs to sign in.
     * @return A new instance of [UserRootComponent].
     */
    public fun createUserComponent(
        componentContext: ComponentContext,
        onNavigateToSelfStorageFiles: (() -> Unit)? = null,
        onNavigateToLogin: () -> Unit
    ): UserRootComponent = UserRootComponentImpl(
        componentContext = componentContext,
        appType = AppType.MANAGEMENT,
        userRepository = userRepository,
        authStorage = authStorage,
        identifierRepository = repositoryModule.selfManagementIdentifierRepository,
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
        onNavigateToClientStorageFiles = onNavigateToSelfStorageFiles,
        onNavigateToLogin = onNavigateToLogin
    )

    /**
     * Creates the root Decompose component for management login flow.
     *
     * @param componentContext Decompose context.
     * @param onFinished Invoked on successful login.
     */
    public fun createLoginRootDialogComponent(
        componentContext: ComponentContext,
        onFinished: () -> Unit
    ): ManagementLoginRootComponent = ManagementLoginRootComponentImpl(
        componentContext = componentContext,
        settingsComponent = settingsComponent,
        securityComponent = securityComponent,
        managementUserComponent = this,
        onFinished = onFinished
    )

    /**
     * Creates the root Decompose component for management flow.
     *
     * @param componentContext Decompose context for the component.
     * @param onNavigateToUserMain Callback to navigate to the current user's main screen.
     * @return A new instance of [ManagementRootComponent].
     */
    public fun createManagementComponent(
        componentContext: ComponentContext,
        onNavigateToUserMain: () -> Unit = {}
    ): ManagementRootComponent = ManagementRootComponentImpl(
        componentContext = componentContext,
        userRepository = userRepository,
        authStorage = authStorage,
        deleteSessionUseCase = deleteSessionUseCase,
        deleteUserIdentifierUseCase = deleteUserIdentifierUseCase,
        getManagementAuthSettingsUseCase = getManagementAuthSettingsUseCase,
        saveRemoteAuthSettingsUseCase = saveRemoteAuthSettingsUseCase,
        resetRemoteAuthSettingsUseCase = resetRemoteAuthSettingsUseCase,
        getManagementGlobalSettingsUseCase = getManagementGlobalSettingsUseCase,
        saveRemoteGlobalSettingsUseCase = saveRemoteGlobalSettingsUseCase,
        resetRemoteGlobalSettingsUseCase = resetRemoteGlobalSettingsUseCase,
        getManagementSecuritySettingsUseCase = getManagementSecuritySettingsUseCase,
        saveRemoteSecuritySettingsUseCase = saveRemoteSecuritySettingsUseCase,
        resetRemoteSecuritySettingsUseCase = resetRemoteSecuritySettingsUseCase,
        getManagementStorageSettingsUseCase = getManagementStorageSettingsUseCase,
        updateManagementStorageSettingsUseCase = updateManagementStorageSettingsUseCase,
        resetRemoteManagementStorageSettingsUseCase = resetRemoteManagementStorageSettingsUseCase,
        getUsersUseCase = getUsersUseCase,
        getUserUseCase = getUserUseCase,
        createUserUseCase = createUserUseCase,
        updateUserUseCase = updateUserUseCase,
        deleteUserUseCase = deleteUserUseCase,
        managementGetSessionsUseCase = managementGetSessionsUseCase,
        managementGetSessionUseCase = managementGetSessionUseCase,
        managementGetUserSessionsUseCase = managementGetUserSessionsUseCase,
        managementGetIdentifiersUseCase = managementGetIdentifiersUseCase,
        managementGetIdentifierUseCase = managementGetIdentifierUseCase,
        managementGetUserIdentifiersUseCase = managementGetUserIdentifiersUseCase,
        managementDisableTotpUseCase = managementDisableTotpUseCase,
        managementDeleteSessionUseCase = managementDeleteSessionUseCase,
        managementDeleteAllUserSessionsUseCase = managementDeleteAllUserSessionsUseCase,
        managementDeleteIdentifierUseCase = useCaseModule.managementDeleteIdentifierUseCase,
        deleteIdentifierPasswordUseCase = useCaseModule.managementDeleteIdentifierPasswordUseCase,
        getAuditEventsUseCase = getAuditEventsUseCase,
        getAuditEventUseCase = getAuditEventUseCase,
        storageDependencies = managementStorageDependencies,
        onNavigateToUserMain = onNavigateToUserMain
    )
}
