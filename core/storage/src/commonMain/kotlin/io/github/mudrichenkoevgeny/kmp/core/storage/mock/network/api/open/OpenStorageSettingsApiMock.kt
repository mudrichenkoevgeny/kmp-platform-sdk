package io.github.mudrichenkoevgeny.kmp.core.storage.mock.network.api.open

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.storage.network.api.open.OpenStorageSettingsApi
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.model.storagesettings.OpenStorageSettingsPayload
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.storage.network.model.storagesettings.createTestOpenStorageSettingsPayload

/**
 * Mock implementation of [OpenStorageSettingsApi] for testing.
 */
@InternalApi
public class OpenStorageSettingsApiMock(
    public var result: AppResult<OpenStorageSettingsPayload> = AppResult.Success(createTestOpenStorageSettingsPayload())
) : OpenStorageSettingsApi {
    public var getStorageSettingsCallCount: Int = 0

    public override suspend fun getStorageSettings(): AppResult<OpenStorageSettingsPayload> {
        getStorageSettingsCallCount++
        return result
    }
}
