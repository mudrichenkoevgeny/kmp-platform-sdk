package io.github.mudrichenkoevgeny.kmp.core.settings.mock.network.globalsettings

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.settings.mock.network.model.globalsettings.openGlobalSettingsPayloadMock
import io.github.mudrichenkoevgeny.kmp.core.settings.network.globalsettings.OpenGlobalSettingsApi
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.network.model.globalsettings.OpenGlobalSettingsPayload

@InternalApi
class OpenGlobalSettingsApiMock(
    var result: AppResult<OpenGlobalSettingsPayload> = AppResult.Success(openGlobalSettingsPayloadMock())
) : OpenGlobalSettingsApi {
    var getGlobalSettingsCallCount = 0

    override suspend fun getOpenGlobalSettings(): AppResult<OpenGlobalSettingsPayload> {
        getGlobalSettingsCallCount++
        return result
    }
}
