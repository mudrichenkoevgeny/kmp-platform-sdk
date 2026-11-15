package io.github.mudrichenkoevgeny.kmp.core.settings.mock.network.globalsettings

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.settings.network.globalsettings.OpenGlobalSettingsApi
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.network.model.globalsettings.OpenGlobalSettingsPayload
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.settings.network.model.globalsettings.createTestOpenGlobalSettingsPayload

/**
 * Mock implementation of [OpenGlobalSettingsApi] for testing purposes.
 */
@InternalApi
public class OpenGlobalSettingsApiMock(
    public var result: AppResult<OpenGlobalSettingsPayload> = AppResult.Success(createTestOpenGlobalSettingsPayload())
) : OpenGlobalSettingsApi {
    public var getGlobalSettingsCallCount: Int = 0

    public override suspend fun getOpenGlobalSettings(): AppResult<OpenGlobalSettingsPayload> {
        getGlobalSettingsCallCount++
        return result
    }
}
