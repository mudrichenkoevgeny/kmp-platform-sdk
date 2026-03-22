package io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.refreshtoken

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.model.token.SessionToken

/**
 * Obtains a new session token pair using a refresh token from the auth API.
 */
interface RefreshTokenRepository {
    /**
     * Exchanges [refreshToken] for fresh access and refresh tokens.
     *
     * @param refreshToken Opaque refresh credential issued by the backend.
     * @return [SessionToken] on success, or an error [AppResult] when refresh is rejected or the
     * request fails.
     */
    suspend fun refreshToken(refreshToken: String): AppResult<SessionToken>
}