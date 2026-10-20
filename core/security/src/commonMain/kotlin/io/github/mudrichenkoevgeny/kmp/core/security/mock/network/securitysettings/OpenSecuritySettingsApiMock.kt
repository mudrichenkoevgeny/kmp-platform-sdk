package io.github.mudrichenkoevgeny.kmp.core.security.mock.network.securitysettings

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.security.network.securitysettings.OpenSecuritySettingsApi
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.securitysettings.OpenSecuritySettingsPayload
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.security.network.model.securitysettings.createTestOpenSecuritySettingsPayload

@InternalApi
class OpenSecuritySettingsApiMock(
    var result: AppResult<OpenSecuritySettingsPayload> = AppResult.Success(createTestOpenSecuritySettingsPayload())
) : OpenSecuritySettingsApi {
    var getSecuritySettingsCallCount = 0

    override suspend fun getSecuritySettings(): AppResult<OpenSecuritySettingsPayload> {
        getSecuritySettingsCallCount++
        return result
    }
}
