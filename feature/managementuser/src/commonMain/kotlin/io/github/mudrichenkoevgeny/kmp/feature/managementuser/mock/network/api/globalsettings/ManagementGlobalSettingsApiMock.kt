package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.network.api.globalsettings

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.globalsettings.ManagementGlobalSettingsApi
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.network.model.globalsettings.ManagementGlobalSettingsPayload
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.network.model.globalsettings.OpenGlobalSettingsPayload
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.settings.network.model.globalsettings.createTestManagementGlobalSettingsPayload
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.settings.network.model.globalsettings.createTestOpenGlobalSettingsPayload

@InternalApi
class ManagementGlobalSettingsApiMock(
    var getResult: AppResult<ManagementGlobalSettingsPayload> = AppResult.Success(createTestManagementGlobalSettingsPayload()),
    var getOpenResult: AppResult<OpenGlobalSettingsPayload> = AppResult.Success(createTestOpenGlobalSettingsPayload()),
    var updateResult: AppResult<ManagementGlobalSettingsPayload> = AppResult.Success(createTestManagementGlobalSettingsPayload()),
    var resetResult: AppResult<ManagementGlobalSettingsPayload> = AppResult.Success(createTestManagementGlobalSettingsPayload())
) : ManagementGlobalSettingsApi {

    override suspend fun getManagementGlobalSettings(): AppResult<ManagementGlobalSettingsPayload> = getResult

    override suspend fun getOpenGlobalSettings(): AppResult<OpenGlobalSettingsPayload> = getOpenResult

    override suspend fun updateManagementGlobalSettings(request: ManagementGlobalSettingsPayload): AppResult<ManagementGlobalSettingsPayload> = updateResult

    override suspend fun resetManagementGlobalSettings(): AppResult<ManagementGlobalSettingsPayload> = resetResult
}
