package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.repository.auth.refreshtoken

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.refreshtoken.RefreshTokenRepository
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.token.SessionToken

/**
 * Mock implementation of [RefreshTokenRepository] for testing.
 */
@InternalApi
public class SelfManagementRefreshTokenRepositoryMock : RefreshTokenRepository {

    public var resultProvider: (String) -> AppResult<SessionToken> = {
        AppResult.Error(
            CommonError.ContractViolation(
                throwable = IllegalStateException("RefreshTokenRepositoryMock: result not provided")
            )
        )
    }

    public override suspend fun refreshToken(refreshToken: String): AppResult<SessionToken> =
        resultProvider(refreshToken)
}
