package io.github.mudrichenkoevgeny.kmp.feature.user.mock.network.model.token

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.token.SessionTokenPayload

@InternalApi
fun sessionTokenPayloadMock(
    accessToken: String = "access-token",
    refreshToken: String = "refresh-token",
    expiresAt: Long = 99L,
    tokenType: String = "Bearer"
): SessionTokenPayload = SessionTokenPayload(
    accessToken = accessToken,
    refreshToken = refreshToken,
    expiresAt = expiresAt,
    tokenType = tokenType
)