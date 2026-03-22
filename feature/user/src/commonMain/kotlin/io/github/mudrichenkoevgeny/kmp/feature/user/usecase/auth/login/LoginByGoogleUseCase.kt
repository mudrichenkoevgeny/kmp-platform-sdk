package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.login

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.flatMap
import io.github.mudrichenkoevgeny.kmp.core.common.result.onSuccess
import io.github.mudrichenkoevgeny.kmp.feature.user.auth.google.GoogleAuthService
import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.AuthData
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.login.LoginRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth.AuthStorage
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.user.UserStorage
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.UserAuthProvider

/**
 * Google Sign-In flow: obtains a provider token, exchanges it for session material via
 * [LoginRepository], then persists tokens and user snapshot on success.
 *
 * @param authService Platform Google auth helper that returns a credential string for the backend.
 * @param loginRepository Remote login API for external providers.
 * @param authStorage Encrypted token storage updated after a successful login.
 * @param userStorage User snapshot storage updated after a successful login.
 */
class LoginByGoogleUseCase(
    private val authService: GoogleAuthService,
    private val loginRepository: LoginRepository,
    private val authStorage: AuthStorage,
    private val userStorage: UserStorage
) {
    /**
     * @return [AuthData] after successful Google sign-in, backend login, and local persistence; or
     * an error result from the auth service, login API, or validation—storage is not updated on failure.
     */
    suspend fun execute(): AppResult<AuthData> {
        return authService.signIn()
            .flatMap { authToken ->
                loginRepository.loginByExternalAuthProvider(UserAuthProvider.GOOGLE, authToken)
            }
            .onSuccess { authData ->
                authStorage.updateTokens(
                    accessToken = authData.sessionToken.accessToken,
                    refreshToken = authData.sessionToken.refreshToken,
                    expiresAt = authData.sessionToken.expiresAt
                )
                userStorage.updateCurrentUser(authData.currentUser)
            }
    }
}