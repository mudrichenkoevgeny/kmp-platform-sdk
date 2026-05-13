package io.github.mudrichenkoevgeny.kmp.core.security.mock.network.securitysettings

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.security.network.securitysettings.SecuritySettingsApi
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.securitysettings.SecuritySettingsPayload

@InternalApi
class SecuritySettingsApiMock : SecuritySettingsApi {
    var result: AppResult<SecuritySettingsPayload> = AppResult.Error(CommonError.Unknown())
    var callCount = 0

    override suspend fun getSecuritySettings(): AppResult<SecuritySettingsPayload> {
        callCount++
        return result
    }
}