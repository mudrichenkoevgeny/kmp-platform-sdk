package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.network.api.storage.settings

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.storage.settings.ManagementStorageSettingsApi
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.model.storagesettings.ManagementStorageSettingsPayload
import io.github.mudrichenkoevgeny.shared.foundation.core.storage.network.model.storagesettings.OpenStorageSettingsPayload
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.storage.network.model.storagesettings.createTestManagementStorageSettingsPayload
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.storage.network.model.storagesettings.createTestOpenStorageSettingsPayload

@InternalApi
class ManagementStorageSettingsApiMock(
    var getResult: AppResult<ManagementStorageSettingsPayload> = AppResult.Success(createTestManagementStorageSettingsPayload()),
    var getOpenResult: AppResult<OpenStorageSettingsPayload> = AppResult.Success(createTestOpenStorageSettingsPayload()),
    var updateResult: AppResult<ManagementStorageSettingsPayload> = AppResult.Success(createTestManagementStorageSettingsPayload()),
    var resetResult: AppResult<ManagementStorageSettingsPayload> = AppResult.Success(createTestManagementStorageSettingsPayload())
) : ManagementStorageSettingsApi {

    override suspend fun getManagementStorageSettings(): AppResult<ManagementStorageSettingsPayload> = getResult

    override suspend fun getOpenStorageSettings(): AppResult<OpenStorageSettingsPayload> = getOpenResult

    override suspend fun updateManagementStorageSettings(request: ManagementStorageSettingsPayload): AppResult<ManagementStorageSettingsPayload> = updateResult

    override suspend fun resetManagementStorageSettings(): AppResult<ManagementStorageSettingsPayload> = resetResult
}
