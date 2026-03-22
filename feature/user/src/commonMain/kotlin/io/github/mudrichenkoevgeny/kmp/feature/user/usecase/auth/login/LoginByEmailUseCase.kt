package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.login

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.onSuccess
import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.AuthData
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.login.LoginRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth.AuthStorage
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.user.UserStorage

/**
 * Signs in with email credentials and, on success, persists session tokens and the current user
 * snapshot for the app.
 *
 * @param loginRepository Remote login API.
 * @param authStorage Encrypted token storage updated after a successful login.
 * @param userStorage User snapshot storage updated after a successful login.
 */
class LoginByEmailUseCase(
    private val loginRepository: LoginRepository,
    private val authStorage: AuthStorage,
    private val userStorage: UserStorage
) {
    /**
     * @param email Account email.
     * @param password Account password.
     * @return [AuthData] after a successful login and local persistence, or an error result without
     * touching storage when login fails.
     */
    suspend fun execute(email: String, password: String): AppResult<AuthData> {
        return loginRepository.loginByEmail(email, password)
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