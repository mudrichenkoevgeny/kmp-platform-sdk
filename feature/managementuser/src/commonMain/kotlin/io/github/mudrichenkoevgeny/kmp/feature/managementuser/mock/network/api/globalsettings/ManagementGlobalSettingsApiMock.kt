package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.network.api.globalsettings

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.globalsettings.ManagementGlobalSettingsApi
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.network.model.globalsettings.ManagementGlobalSettingsPayload
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.network.model.globalsettings.OpenGlobalSettingsPayload
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.settings.network.model.globalsettings.createTestManagementGlobalSettingsPayload
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.settings.network.model.globalsettings.createTestOpenGlobalSettingsPayload

/**
 * Mock implementation of [ManagementGlobalSettingsApi] for testing.
 */
@InternalApi
public class ManagementGlobalSettingsApiMock(
    public var getResult: AppResult<ManagementGlobalSettingsPayload> = AppResult.Success(
        createTestManagementGlobalSettingsPayload()
    ),
    public var getOpenResult: AppResult<OpenGlobalSettingsPayload> = AppResult.Success(
        createTestOpenGlobalSettingsPayload()
    ),
    public var updateResult: AppResult<ManagementGlobalSettingsPayload> = AppResult.Success(
        createTestManagementGlobalSettingsPayload()
    ),
    public var resetResult: AppResult<ManagementGlobalSettingsPayload> = AppResult.Success(
        createTestManagementGlobalSettingsPayload()
    )
) : ManagementGlobalSettingsApi {

    public override suspend fun getManagementGlobalSettings(): AppResult<ManagementGlobalSettingsPayload> = getResult

    public override suspend fun getOpenGlobalSettings(): AppResult<OpenGlobalSettingsPayload> = getOpenResult

    public override suspend fun updateManagementGlobalSettings(
        request: ManagementGlobalSettingsPayload
    ): AppResult<ManagementGlobalSettingsPayload> = updateResult

    public override suspend fun resetManagementGlobalSettings(): AppResult<ManagementGlobalSettingsPayload> =
        resetResult
}
