package io.github.mudrichenkoevgeny.kmp.feature.user.network.api.auth.refreshtoken

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.token.RefreshTokenPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.token.SessionTokenPayload

/** Exchange refresh token for a new session token pair. */
interface RefreshTokenApi {
    /**
     * Obtains a new access (and refresh) token pair from the current refresh token.
     *
     * @param request Refresh token wrapper from the shared contract.
     * @return New session token material and expiry, or a mapped failure.
     */
    suspend fun refreshToken(
        request: RefreshTokenPayload
    ): AppResult<SessionTokenPayload>
}