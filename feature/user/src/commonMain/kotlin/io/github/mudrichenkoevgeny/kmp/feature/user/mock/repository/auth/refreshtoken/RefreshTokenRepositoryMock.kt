package io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.auth.refreshtoken

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.refreshtoken.RefreshTokenRepository
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.token.SessionToken

@InternalApi
class RefreshTokenRepositoryMock : RefreshTokenRepository {

    var refreshTokenResultProvider: (String) -> AppResult<SessionToken> = {
        AppResult.Error(
            CommonError.ContractViolation(
                throwable = IllegalStateException("RefreshTokenRepositoryMock: result not provided")
            )
        )
    }

    var lastRefreshToken: String? = null

    override suspend fun refreshToken(refreshToken: String): AppResult<SessionToken> {
        lastRefreshToken = refreshToken
        return refreshTokenResultProvider(refreshToken)
    }
}
