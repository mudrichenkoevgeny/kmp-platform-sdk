package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.login

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.onSuccess
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.login.LoginRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth.AuthStorage
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.user.UserStorage
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.data.AuthData

/**
 * Completes the MFA flow using a time-based one-time password (TOTP) and, on success, persists
 * session tokens and the current user snapshot.
 *
 * @param loginRepository Remote login API.
 * @param authStorage Encrypted token storage updated after a successful login.
 * @param userStorage User snapshot storage updated after a successful login.
 */
open class LoginByTotpUseCase(
    private val loginRepository: LoginRepository,
    private val authStorage: AuthStorage,
    private val userStorage: UserStorage
) {
    /**
     * @param mfaToken Opaque intermediate token from the initial authentication step.
     * @param code Time-based verification code.
     * @return [AuthData] after a successful login and local persistence, or an error result without
     * touching storage when login fails.
     */
    open suspend fun execute(mfaToken: String, code: String): AppResult<AuthData> {
        return loginRepository.loginByTotp(mfaToken, code)
            .onSuccess { authData ->
                authStorage.updateTokens(
                    accessToken = authData.sessionToken.accessToken,
                    refreshToken = authData.sessionToken.refreshToken,
                    expiresAt = authData.sessionToken.expiresAt
                )
                userStorage.updateCurrentUser(authData.userDetails)
            }
    }
}
