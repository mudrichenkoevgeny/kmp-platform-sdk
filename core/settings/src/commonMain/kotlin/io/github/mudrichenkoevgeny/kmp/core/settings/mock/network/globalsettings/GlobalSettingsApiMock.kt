package io.github.mudrichenkoevgeny.kmp.core.settings.mock.network.globalsettings

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.settings.network.globalsettings.GlobalSettingsApi
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.network.model.globalsettings.GlobalSettingsPayload

@InternalApi
class GlobalSettingsApiMock : GlobalSettingsApi {
    var result: AppResult<GlobalSettingsPayload> = AppResult.Error(CommonError.Unknown())
    var callCount = 0

    override suspend fun getGlobalSettings(): AppResult<GlobalSettingsPayload> {
        callCount++
        return result
    }
}