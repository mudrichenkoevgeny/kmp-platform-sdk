package io.github.mudrichenkoevgeny.kmp.core.storage.mock.network.storagesettings

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.storage.network.api.storagesettings.OpenStorageSettingsApi
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.model.storagesettings.OpenStorageSettingsPayload
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.storage.network.model.storagesettings.createTestOpenStorageSettingsPayload

@InternalApi
class OpenStorageSettingsApiMock(
    var result: AppResult<OpenStorageSettingsPayload> = AppResult.Success(createTestOpenStorageSettingsPayload())
) : OpenStorageSettingsApi {
    var getStorageSettingsCallCount = 0

    override suspend fun getStorageSettings(): AppResult<OpenStorageSettingsPayload> {
        getStorageSettingsCallCount++
        return result
    }
}
