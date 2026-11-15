package io.github.mudrichenkoevgeny.kmp.feature.user.mock.network.api.auth.refreshtoken

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.network.api.auth.refreshtoken.RefreshTokenApi
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.token.RefreshTokenPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.token.SessionTokenPayload

/**
 * Mock implementation of [RefreshTokenApi] for testing.
 */
@InternalApi
public class RefreshTokenApiMock : RefreshTokenApi {
    public var refreshTokenResult: AppResult<SessionTokenPayload> = AppResult.Error(CommonError.Unknown())
    public var lastRefreshTokenRequest: RefreshTokenPayload? = null

    public override suspend fun refreshToken(request: RefreshTokenPayload): AppResult<SessionTokenPayload> {
        lastRefreshTokenRequest = request
        return refreshTokenResult
    }
}
