package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.repository.user.security

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.repository.user.security.ManagementUserSecurityRepository
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId

/**
 * Mock implementation of [ManagementUserSecurityRepository] for testing.
 */
@InternalApi
public open class ManagementUserSecurityRepositoryMock : ManagementUserSecurityRepository {

    public var disableTotpResultProvider: (UserId) -> AppResult<Unit> = { AppResult.Success(Unit) }

    public var lastUserId: UserId? = null

    public override suspend fun disableTotp(userId: UserId): AppResult<Unit> {
        lastUserId = userId
        return disableTotpResultProvider(userId)
    }
}
