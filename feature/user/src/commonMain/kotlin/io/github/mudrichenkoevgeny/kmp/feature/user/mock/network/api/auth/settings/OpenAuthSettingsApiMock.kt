package io.github.mudrichenkoevgeny.kmp.feature.user.mock.network.api.auth.settings

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.network.api.auth.settings.OpenAuthSettingsApi
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.auth.settings.OpenAuthSettingsPayload

/**
 * Mock implementation of [OpenAuthSettingsApi] for testing.
 */
@InternalApi
public class OpenAuthSettingsApiMock : OpenAuthSettingsApi {
    public var result: AppResult<OpenAuthSettingsPayload> = AppResult.Error(CommonError.Unknown(isRetryable = false))
    public var callCount: Int = 0

    public override suspend fun getAuthSettings(): AppResult<OpenAuthSettingsPayload> {
        callCount++
        return result
    }
}
