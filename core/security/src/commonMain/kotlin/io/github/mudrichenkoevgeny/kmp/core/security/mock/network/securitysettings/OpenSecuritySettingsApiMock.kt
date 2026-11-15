package io.github.mudrichenkoevgeny.kmp.core.security.mock.network.securitysettings

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.security.network.securitysettings.OpenSecuritySettingsApi
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.securitysettings.OpenSecuritySettingsPayload
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.security.network.model.securitysettings.createTestOpenSecuritySettingsPayload

/**
 * Mock implementation of [OpenSecuritySettingsApi] for testing purposes.
 */
@InternalApi
public class OpenSecuritySettingsApiMock(
    public var result: AppResult<OpenSecuritySettingsPayload> = AppResult.Success(
        createTestOpenSecuritySettingsPayload()
    )
) : OpenSecuritySettingsApi {
    public var getSecuritySettingsCallCount: Int = 0

    public override suspend fun getSecuritySettings(): AppResult<OpenSecuritySettingsPayload> {
        getSecuritySettingsCallCount++
        return result
    }
}
