package io.github.mudrichenkoevgeny.kmp.feature.clientuser.repository.auth.refreshtoken

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.result.mapSuccess
import io.github.mudrichenkoevgeny.kmp.feature.user.network.api.auth.refreshtoken.RefreshTokenApi
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.refreshtoken.RefreshTokenRepository
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.token.SessionToken
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.mapper.token.toSessionToken
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.token.RefreshTokenPayload

/**
 * Implements [RefreshTokenRepository] by delegating to [RefreshTokenApi].
 *
 * @param refreshTokenApi HTTP endpoint for token refresh.
 */
class OpenRefreshTokenRepositoryImpl(
    private val refreshTokenApi: RefreshTokenApi
) : RefreshTokenRepository {

    override suspend fun refreshToken(refreshToken: String): AppResult<SessionToken> {
        return refreshTokenApi.refreshToken(RefreshTokenPayload(refreshToken))
            .mapSuccess { sessionTokenResponse ->
                sessionTokenResponse.toSessionToken()
            }
    }
}