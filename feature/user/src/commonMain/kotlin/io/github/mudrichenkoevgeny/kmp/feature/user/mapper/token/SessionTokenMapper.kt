package io.github.mudrichenkoevgeny.kmp.feature.user.mapper.token

import io.github.mudrichenkoevgeny.kmp.feature.user.model.token.AccessToken
import io.github.mudrichenkoevgeny.kmp.feature.user.model.token.RefreshToken
import io.github.mudrichenkoevgeny.kmp.feature.user.model.token.SessionToken
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.response.token.SessionTokenResponse

/**
 * Maps token payload from the auth API into a typed [SessionToken] (wrapped access/refresh tokens plus metadata).
 */
fun SessionTokenResponse.toSessionToken(): SessionToken = SessionToken(
    accessToken = AccessToken(accessToken),
    refreshToken = RefreshToken(refreshToken),
    expiresAt = expiresAt,
    tokenType = tokenType
)