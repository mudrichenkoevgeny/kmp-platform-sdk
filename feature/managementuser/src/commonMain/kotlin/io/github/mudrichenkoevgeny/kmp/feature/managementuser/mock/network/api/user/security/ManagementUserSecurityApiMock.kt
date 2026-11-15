package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.network.api.user.security

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.user.security.ManagementUserSecurityApi
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId

/**
 * Mock implementation of [ManagementUserSecurityApi] for testing.
 */
@InternalApi
public class ManagementUserSecurityApiMock : ManagementUserSecurityApi {
    public var disableTotpResult: AppResult<Unit> = AppResult.Error(CommonError.Unknown())

    public override suspend fun disableTotp(userId: UserId): AppResult<Unit> = disableTotpResult
}
