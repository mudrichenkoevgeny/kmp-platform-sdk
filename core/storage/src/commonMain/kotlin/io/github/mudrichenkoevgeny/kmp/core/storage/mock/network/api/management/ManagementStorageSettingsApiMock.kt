package io.github.mudrichenkoevgeny.kmp.core.storage.mock.network.api.management

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.storage.network.api.management.ManagementStorageSettingsApi
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.model.storagesettings.ManagementStorageSettingsPayload
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.model.storagesettings.OpenStorageSettingsPayload
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.storage.network.model.storagesettings.createTestManagementStorageSettingsPayload
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.storage.network.model.storagesettings.createTestOpenStorageSettingsPayload

/**
 * Mock implementation of [ManagementStorageSettingsApi] for testing.
 */
@InternalApi
public class ManagementStorageSettingsApiMock(
    public var getResult: AppResult<ManagementStorageSettingsPayload> = AppResult.Success(
        createTestManagementStorageSettingsPayload()
    ),
    public var getOpenResult: AppResult<OpenStorageSettingsPayload> = AppResult.Success(
        createTestOpenStorageSettingsPayload()
    ),
    public var updateResult: AppResult<ManagementStorageSettingsPayload> = AppResult.Success(
        createTestManagementStorageSettingsPayload()
    ),
    public var resetResult: AppResult<ManagementStorageSettingsPayload> = AppResult.Success(
        createTestManagementStorageSettingsPayload()
    )
) : ManagementStorageSettingsApi {

    public override suspend fun getManagementStorageSettings(): AppResult<ManagementStorageSettingsPayload> = getResult
    public override suspend fun getOpenStorageSettings(): AppResult<OpenStorageSettingsPayload> = getOpenResult
    public override suspend fun updateManagementStorageSettings(
        request: ManagementStorageSettingsPayload
    ): AppResult<ManagementStorageSettingsPayload> = updateResult

    public override suspend fun resetManagementStorageSettings(): AppResult<ManagementStorageSettingsPayload> =
        resetResult
}
