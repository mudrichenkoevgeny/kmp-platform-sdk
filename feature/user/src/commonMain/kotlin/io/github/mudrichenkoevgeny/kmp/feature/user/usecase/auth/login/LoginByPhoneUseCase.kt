package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.login

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.onSuccess
import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.AuthData
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.login.LoginRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth.AuthStorage
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.user.UserStorage

/**
 * Completes phone-based login and, on success, persists session tokens and the current user snapshot.
 *
 * @param loginRepository Remote login API.
 * @param authStorage Encrypted token storage updated after a successful login.
 * @param userStorage User snapshot storage updated after a successful login.
 */
class LoginByPhoneUseCase(
    private val loginRepository: LoginRepository,
    private val authStorage: AuthStorage,
    private val userStorage: UserStorage
) {
    /**
     * @param phoneNumber Phone number in the format expected by the backend.
     * @param confirmationCode One-time code delivered to the phone.
     * @return [AuthData] after a successful login and local persistence, or an error result without
     * touching storage when login fails.
     */
    suspend fun execute(phoneNumber: String, confirmationCode: String): AppResult<AuthData> {
        return loginRepository.loginByPhone(phoneNumber, confirmationCode)
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