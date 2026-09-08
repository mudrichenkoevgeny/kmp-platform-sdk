package io.github.mudrichenkoevgeny.kmp.feature.managementuser.di

import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.service.WebSocketService
import io.github.mudrichenkoevgeny.kmp.core.common.storage.EncryptedSettings
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.auth.login.SelfManagementLoginRepositoryImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.auth.refreshtoken.SelfManagementRefreshTokenRepositoryImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.auth.resetpassword.SelfManagementResetPasswordRepositoryImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.auth.settings.ManagementAuthSettingsRepository
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.auth.settings.ManagementAuthSettingsRepositoryImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.globalsettings.ManagementGlobalSettingsRepository
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.globalsettings.ManagementGlobalSettingsRepositoryImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.identifier.ManagementIdentifierRepository
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.identifier.ManagementIdentifierRepositoryImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.identifier.SelfManagementIdentifierRepositoryImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.security.settings.ManagementSecuritySettingsRepository
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.security.settings.ManagementSecuritySettingsRepositoryImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.session.ManagementSessionRepository
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.session.ManagementSessionRepositoryImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.session.SelfManagementSessionRepositoryImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.user.ManagementUserRepository
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.user.ManagementUserRepositoryImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.user.SelfManagementUserRepositoryImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.user.security.ManagementUserSecurityRepository
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.user.security.ManagementUserSecurityRepositoryImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.user.security.SelfManagementUserSecurityRepositoryImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.storage.auth.settings.EncryptedManagementAuthSettingsStorage
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.storage.auth.settings.ManagementAuthSettingsStorage
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.storage.globalsettings.EncryptedManagementGlobalSettingsStorage
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.storage.globalsettings.ManagementGlobalSettingsStorage
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.storage.security.settings.EncryptedManagementSecuritySettingsStorage
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.storage.security.settings.ManagementSecuritySettingsStorage
import io.github.mudrichenkoevgeny.kmp.feature.user.di.UserStorageModule
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.login.LoginRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.refreshtoken.RefreshTokenRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.resetpassword.ResetPasswordRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.confirmation.ConfirmationRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.confirmation.ConfirmationRepositoryImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.identifier.IdentifierRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.session.SessionRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.user.UserRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.user.security.UserSecurityRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth.AuthStorage
import kotlinx.coroutines.CoroutineScope
import kotlin.time.Clock

/**
 * Coordinates user feature repositories: auth (login, registration, tokens, password, auth settings),
 * confirmation timing, and user profile loading against user storage and network APIs.
 *
 * @param networkModule Lazy Ktor API accessors.
 * @param authStorage Token persistence.
 * @param encryptedSettings Encrypted settings.
 * @param storageModule User profile storage.
 * @param webSocketService Used by auth-settings repository for reactive updates.
 * @param repositoryScope Coroutine scope for long-lived repository jobs.
 */
internal class ManagementUserRepositoryModule(
    private val networkModule: ManagementUserNetworkModule,
    private val authStorage: AuthStorage,
    private val encryptedSettings: EncryptedSettings,
    private val storageModule: UserStorageModule,
    private val webSocketService: WebSocketService,
    repositoryScope: CoroutineScope
) {
    // Confirmation
    /** Throttling for management-specific confirmation codes. */
    val confirmationRepository: ConfirmationRepository by lazy {
        ConfirmationRepositoryImpl(Clock.System)
    }

    // Auth
    /** Repository for self-management login. */
    val selfManagementLoginRepository: LoginRepository by lazy {
        SelfManagementLoginRepositoryImpl(networkModule.loginApi)
    }
    /** Repository for management session refresh. */
    val selfManagementRefreshTokenRepository: RefreshTokenRepository by lazy {
        SelfManagementRefreshTokenRepositoryImpl(networkModule.refreshTokenApi)
    }
    /** Repository for self-management password reset. */
    val selfManagementResetPasswordRepository: ResetPasswordRepository by lazy {
        SelfManagementResetPasswordRepositoryImpl(
            resetPasswordApi = networkModule.resetPasswordApi,
            confirmationRepository = confirmationRepository
        )
    }
    /** Storage for management auth settings. */
    val managementAuthSettingsStorage: ManagementAuthSettingsStorage by lazy {
        EncryptedManagementAuthSettingsStorage(encryptedSettings)
    }
    /** Repository for administrative auth settings. */
    val managementAuthSettingsRepository: ManagementAuthSettingsRepository by lazy {
        ManagementAuthSettingsRepositoryImpl(
            managementAuthSettingsApi = networkModule.authSettingsApi,
            managementAuthSettingsStorage = managementAuthSettingsStorage,
            webSocketService = webSocketService,
            repositoryScope = repositoryScope
        )
    }

    // Identifier
    /** Repository for managing current manager identifiers. */
    val selfManagementIdentifierRepository: IdentifierRepository by lazy {
        SelfManagementIdentifierRepositoryImpl(
            selfManagementIdentifiersApi = networkModule.identifiersApi
        )
    }
    /** Administrative repository for any user identifiers. */
    val managementIdentifierRepository: ManagementIdentifierRepository by lazy {
        ManagementIdentifierRepositoryImpl(
            managementIdentifierApi = networkModule.managementIdentifiersApi
        )
    }

    // Session
    /** Repository for current manager sessions. */
    val selfManagementSessionRepository: SessionRepository by lazy {
        SelfManagementSessionRepositoryImpl(
            selfManagementSessionApi = networkModule.sessionApi,
            userStorage = storageModule.userStorage
        )
    }
    /** Administrative repository for any user sessions. */
    val managementSessionRepository: ManagementSessionRepository by lazy {
        ManagementSessionRepositoryImpl(
            managementSessionApi = networkModule.managementSessionApi
        )
    }

    // User
    /** Repository for current manager profile. */
    val selfManagementUserRepository: UserRepository by lazy {
        SelfManagementUserRepositoryImpl(
            userStorage = storageModule.userStorage,
            authStorage = authStorage,
            selfManagementUserApi = networkModule.userApi,
            webSocketService = webSocketService,
            repositoryScope = repositoryScope
        )
    }
    /** Administrative repository for any user profile data. */
    val managementUserRepository: ManagementUserRepository by lazy {
        ManagementUserRepositoryImpl(
            managementUserApi = networkModule.managementUserApi
        )
    }
    /** Repository for current manager security (TOTP). */
    val selfManagementUserSecurityRepository: UserSecurityRepository by lazy {
        SelfManagementUserSecurityRepositoryImpl(
            userSecurityApi = networkModule.userSecurityApi,
            userStorage = storageModule.userStorage
        )
    }
    /** Administrative repository for any user security management. */
    val managementUserSecurityRepository: ManagementUserSecurityRepository by lazy {
        ManagementUserSecurityRepositoryImpl(
            managementUserSecurityApi = networkModule.managementUserSecurityApi
        )
    }

    // Global and Security Settings Repositories
    /** Storage for management global settings. */
    val managementGlobalSettingsStorage: ManagementGlobalSettingsStorage by lazy {
        EncryptedManagementGlobalSettingsStorage(encryptedSettings)
    }
    /** Administrative repository for global settings. */
    val managementGlobalSettingsRepository: ManagementGlobalSettingsRepository by lazy {
        ManagementGlobalSettingsRepositoryImpl(
            managementGlobalSettingsApi = networkModule.globalSettingsApi,
            managementGlobalSettingsStorage = managementGlobalSettingsStorage,
            webSocketService = webSocketService,
            repositoryScope = repositoryScope
        )
    }

    /** Storage for management security settings. */
    val managementSecuritySettingsStorage: ManagementSecuritySettingsStorage by lazy {
        EncryptedManagementSecuritySettingsStorage(encryptedSettings)
    }
    /** Administrative repository for security settings. */
    val managementSecuritySettingsRepository: ManagementSecuritySettingsRepository by lazy {
        ManagementSecuritySettingsRepositoryImpl(
            managementSecuritySettingsApi = networkModule.securitySettingsApi,
            managementSecuritySettingsStorage = managementSecuritySettingsStorage,
            webSocketService = webSocketService,
            repositoryScope = repositoryScope
        )
    }
}
