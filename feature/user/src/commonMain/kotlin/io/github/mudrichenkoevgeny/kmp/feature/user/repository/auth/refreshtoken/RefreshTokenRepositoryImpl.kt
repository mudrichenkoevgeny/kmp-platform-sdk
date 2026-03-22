package io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.refreshtoken

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.mapSuccess
import io.github.mudrichenkoevgeny.kmp.feature.user.mapper.token.toSessionToken
import io.github.mudrichenkoevgeny.kmp.feature.user.model.token.SessionToken
import io.github.mudrichenkoevgeny.kmp.feature.user.network.api.auth.refreshtoken.RefreshTokenApi
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.refreshtoken.RefreshTokenRequest

/**
 * Implements [RefreshTokenRepository] by delegating to [RefreshTokenApi].
 *
 * @param refreshTokenApi HTTP endpoint for token refresh.
 */
class RefreshTokenRepositoryImpl(
    private val refreshTokenApi: RefreshTokenApi
) : RefreshTokenRepository {

    override suspend fun refreshToken(refreshToken: String): AppResult<SessionToken> {
        return refreshTokenApi.refreshToken(RefreshTokenRequest(refreshToken))
            .mapSuccess { sessionTokenResponse ->
                sessionTokenResponse.toSessionToken()
            }
    }
}