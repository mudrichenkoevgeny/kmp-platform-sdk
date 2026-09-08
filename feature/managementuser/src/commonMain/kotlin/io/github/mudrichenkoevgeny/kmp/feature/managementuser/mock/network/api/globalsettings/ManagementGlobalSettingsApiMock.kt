package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.network.api.globalsettings

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.settings.mock.network.model.globalsettings.managementGlobalSettingsPayloadMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.globalsettings.ManagementGlobalSettingsApi
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.network.model.globalsettings.ManagementGlobalSettingsPayload

@InternalApi
class ManagementGlobalSettingsApiMock(
    var getResult: AppResult<ManagementGlobalSettingsPayload> = AppResult.Success(managementGlobalSettingsPayloadMock()),
    var updateResult: AppResult<Unit> = AppResult.Success(Unit)
) : ManagementGlobalSettingsApi {

    override suspend fun getManagementGlobalSettings(): AppResult<ManagementGlobalSettingsPayload> = getResult

    override suspend fun updateManagementGlobalSettings(request: ManagementGlobalSettingsPayload): AppResult<Unit> = updateResult
}
