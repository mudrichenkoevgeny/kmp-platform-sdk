package io.github.mudrichenkoevgeny.kmp.feature.managementuser.di

import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.service.WebSocketService
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.auth.login.SelfManagementLoginRepositoryImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.auth.resetpassword.SelfManagementResetPasswordRepositoryImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.auth.refreshtoken.SelfManagementRefreshTokenRepositoryImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.auth.settings.ManagementAuthSettingsRepository
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.auth.settings.ManagementAuthSettingsRepositoryImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.identifier.ManagementIdentifierRepository
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.identifier.ManagementIdentifierRepositoryImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.confirmation.ConfirmationRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.confirmation.ConfirmationRepositoryImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.identifier.SelfManagementIdentifierRepositoryImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.session.ManagementSessionRepository
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.session.ManagementSessionRepositoryImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.session.SelfManagementSessionRepositoryImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.user.ManagementUserRepository
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.user.ManagementUserRepositoryImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.user.SelfManagementUserRepositoryImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.user.security.ManagementUserSecurityRepository
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.user.security.ManagementUserSecurityRepositoryImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.user.security.SelfManagementUserSecurityRepositoryImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.di.UserStorageModule
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.login.LoginRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.refreshtoken.RefreshTokenRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.resetpassword.ResetPasswordRepository
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
 * @param authStorage Token and auth-settings persistence.
 * @param storageModule User profile storage.
 * @param webSocketService Used by auth-settings repository for reactive updates.
 * @param repositoryScope Coroutine scope for long-lived repository jobs.
 */
internal class ManagementUserRepositoryModule(
    private val networkModule: ManagementUserNetworkModule,
    private val authStorage: AuthStorage,
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
    /** Repository for administrative auth settings. */
    val managementAuthSettingsRepository: ManagementAuthSettingsRepository by lazy {
        ManagementAuthSettingsRepositoryImpl(
            managementAuthSettingsApi = networkModule.authSettingsApi,
            authStorage = authStorage,
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
}