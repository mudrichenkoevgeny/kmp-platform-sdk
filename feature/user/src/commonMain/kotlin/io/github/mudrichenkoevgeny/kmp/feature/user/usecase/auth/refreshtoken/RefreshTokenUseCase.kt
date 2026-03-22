package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.auth.refreshtoken

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.onSuccess
import io.github.mudrichenkoevgeny.kmp.feature.user.error.model.UserError
import io.github.mudrichenkoevgeny.kmp.feature.user.model.token.SessionToken
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.refreshtoken.RefreshTokenRepository
import io.github.mudrichenkoevgeny.kmp.feature.user.storage.auth.AuthStorage

/**
 * Refreshes the session using the stored refresh token; updates [AuthStorage] when the backend
 * returns a new token pair.
 *
 * @param refreshTokenRepository Remote token refresh API.
 * @param authStorage Source of the current refresh token and destination for updated credentials.
 */
class RefreshTokenUseCase(
    private val refreshTokenRepository: RefreshTokenRepository,
    private val authStorage: AuthStorage
) {
    /**
     * @return Fresh [SessionToken] and updated storage on success; [AppResult.Error] with
     * [UserError.InvalidRefreshToken] when no refresh token is cached, or an error from refresh when
     * the backend rejects the token.
     */
    suspend fun execute(): AppResult<SessionToken> {
        val currentRefreshToken = authStorage.getRefreshToken()?.value
            ?: return AppResult.Error(UserError.InvalidRefreshToken())

        return refreshTokenRepository.refreshToken(currentRefreshToken)
            .onSuccess { sessionToken ->
                authStorage.updateTokens(
                    accessToken = sessionToken.accessToken,
                    refreshToken = sessionToken.refreshToken,
                    expiresAt = sessionToken.expiresAt
                )
            }
    }
}