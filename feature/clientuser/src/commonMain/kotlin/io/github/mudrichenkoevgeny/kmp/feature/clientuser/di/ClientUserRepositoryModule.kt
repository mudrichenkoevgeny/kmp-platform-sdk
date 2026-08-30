package io.github.mudrichenkoevgeny.kmp.feature.clientuser.di

import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.service.WebSocketService
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.login.LoginRepository
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.repository.auth.login.OpenLoginRepositoryImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.resetpassword.ResetPasswordRepository
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.repository.auth.resetpassword.OpenResetPasswordRepositoryImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.refreshtoken.RefreshTokenRepository
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.repository.auth.refreshtoken.OpenRefreshTokenRepositoryImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.registration.RegistrationRepository
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.repository.auth.registration.OpenRegistrationRepositoryImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.settings.OpenAuthSettingsRepository
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.repository.auth.settings.OpenAuthSettingsRepositoryImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.confirmation.ConfirmationRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.confirmation.ConfirmationRepositoryImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.identifier.IdentifierRepository
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.repository.identifier.OpenIdentifierRepositoryImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.session.SessionRepository
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.repository.session.OpenSessionRepositoryImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.user.UserRepository
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.repository.user.OpenUserRepositoryImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.user.security.UserSecurityRepository
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.repository.user.security.OpenUserSecurityRepositoryImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.di.UserStorageModule
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
internal class ClientUserRepositoryModule(
    private val networkModule: ClientUserNetworkModule,
    private val authStorage: AuthStorage,
    private val storageModule: UserStorageModule,
    private val webSocketService: WebSocketService,
    repositoryScope: CoroutineScope
) {
    // Confirmation
    val confirmationRepository: ConfirmationRepository by lazy {
        ConfirmationRepositoryImpl(Clock.System)
    }

    // Auth
    val loginRepository: LoginRepository by lazy {
        OpenLoginRepositoryImpl(networkModule.loginApi, confirmationRepository)
    }
    val registrationRepository: RegistrationRepository by lazy {
        OpenRegistrationRepositoryImpl(networkModule.registrationApi, confirmationRepository)
    }
    val refreshTokenRepository: RefreshTokenRepository by lazy {
        OpenRefreshTokenRepositoryImpl(networkModule.refreshTokenApi)
    }
    val resetPasswordRepository: ResetPasswordRepository by lazy {
        OpenResetPasswordRepositoryImpl(networkModule.resetPasswordApi, confirmationRepository)
    }
    val openAuthSettingsRepository: OpenAuthSettingsRepository by lazy {
        OpenAuthSettingsRepositoryImpl(
            openAuthSettingsApi = networkModule.authSettingsApi,
            authStorage = authStorage,
            webSocketService = webSocketService,
            repositoryScope = repositoryScope
        )
    }

    // Identifier
    val identifierRepository: IdentifierRepository by lazy {
        OpenIdentifierRepositoryImpl(
            openIdentifiersApi = networkModule.identifiersApi,
            confirmationRepository = confirmationRepository,
            userStorage = storageModule.userStorage
        )
    }

    // Session
    val sessionRepository: SessionRepository by lazy {
        OpenSessionRepositoryImpl(
            sessionApi = networkModule.sessionApi,
            userStorage = storageModule.userStorage
        )
    }

    // User
    val userRepository: UserRepository by lazy {
        OpenUserRepositoryImpl(
            userStorage = storageModule.userStorage,
            authStorage = authStorage,
            openUserApi = networkModule.userApi,
            webSocketService = webSocketService,
            repositoryScope = repositoryScope
        )
    }

    val userSecurityRepository: UserSecurityRepository by lazy {
        OpenUserSecurityRepositoryImpl(
            userSecurityApi = networkModule.userSecurityApi,
            userStorage = storageModule.userStorage
        )
    }
}
