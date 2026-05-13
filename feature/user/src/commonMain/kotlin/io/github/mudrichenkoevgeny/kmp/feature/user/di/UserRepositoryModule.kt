package io.github.mudrichenkoevgeny.kmp.feature.user.di

import io.github.mudrichenkoevgeny.kmp.core.common.network.websocket.service.WebSocketService
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.login.LoginRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.login.LoginRepositoryImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.resetpassword.ResetPasswordRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.resetpassword.ResetPasswordRepositoryImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.refreshtoken.RefreshTokenRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.refreshtoken.RefreshTokenRepositoryImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.registration.RegistrationRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.registration.RegistrationRepositoryImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.settings.AuthSettingsRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.settings.AuthSettingsRepositoryImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.confirmation.ConfirmationRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.confirmation.ConfirmationRepositoryImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.identifier.IdentifierRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.identifier.IdentifierRepositoryImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.session.SessionRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.session.SessionRepositoryImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.user.UserRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.user.UserRepositoryImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.user.security.UserSecurityRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.user.security.UserSecurityRepositoryImpl
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
internal class UserRepositoryModule(
    private val networkModule: UserNetworkModule,
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
        LoginRepositoryImpl(networkModule.loginApi, confirmationRepository)
    }
    val registrationRepository: RegistrationRepository by lazy {
        RegistrationRepositoryImpl(networkModule.registrationApi, confirmationRepository)
    }
    val refreshTokenRepository: RefreshTokenRepository by lazy {
        RefreshTokenRepositoryImpl(networkModule.refreshTokenApi)
    }
    val resetPasswordRepository: ResetPasswordRepository by lazy {
        ResetPasswordRepositoryImpl(networkModule.resetPasswordApi, confirmationRepository)
    }
    val authSettingsRepository: AuthSettingsRepository by lazy {
        AuthSettingsRepositoryImpl(
            authSettingsApi = networkModule.authSettingsApi,
            authStorage = authStorage,
            webSocketService = webSocketService,
            repositoryScope = repositoryScope
        )
    }

    // Identifier
    val identifierRepository: IdentifierRepository by lazy {
        IdentifierRepositoryImpl(
            identifiersApi = networkModule.identifiersApi,
            confirmationRepository = confirmationRepository,
            userStorage = storageModule.userStorage
        )
    }

    // Session
    val sessionRepository: SessionRepository by lazy {
        SessionRepositoryImpl(
            sessionApi = networkModule.sessionApi,
            userStorage = storageModule.userStorage
        )
    }

    // User
    val userRepository: UserRepository by lazy {
        UserRepositoryImpl(
            userStorage = storageModule.userStorage,
            authStorage = authStorage,
            userApi = networkModule.userApi,
            webSocketService = webSocketService,
            repositoryScope = repositoryScope
        )
    }

    val userSecurityRepository: UserSecurityRepository by lazy {
        UserSecurityRepositoryImpl(
            userSecurityApi = networkModule.userSecurityApi,
            userStorage = storageModule.userStorage
        )
    }
}