package io.github.mudrichenkoevgeny.kmp.feature.user.api.auth.refreshtoken

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.request.auth.refreshtoken.RefreshTokenRequest
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.response.token.SessionTokenResponse

interface RefreshTokenApi {
    suspend fun refreshToken(
        request: RefreshTokenRequest
    ): AppResult<SessionTokenResponse>
}