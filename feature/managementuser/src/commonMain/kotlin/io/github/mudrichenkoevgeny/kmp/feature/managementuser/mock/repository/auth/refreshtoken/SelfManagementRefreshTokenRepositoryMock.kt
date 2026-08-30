package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.repository.auth.refreshtoken

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.refreshtoken.RefreshTokenRepository
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.token.SessionToken

@InternalApi
class SelfManagementRefreshTokenRepositoryMock : RefreshTokenRepository {

    var resultProvider: (String) -> AppResult<SessionToken> = {
        AppResult.Error(
            CommonError.ContractViolation(
                throwable = IllegalStateException("RefreshTokenRepositoryMock: result not provided")
            )
        )
    }

    override suspend fun refreshToken(refreshToken: String): AppResult<SessionToken> =
        resultProvider(refreshToken)
}