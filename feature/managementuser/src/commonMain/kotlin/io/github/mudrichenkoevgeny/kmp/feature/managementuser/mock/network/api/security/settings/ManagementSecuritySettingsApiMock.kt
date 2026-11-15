package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.network.api.security.settings

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.security.settings.ManagementSecuritySettingsApi
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.securitysettings.ManagementSecuritySettingsPayload
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.securitysettings.OpenSecuritySettingsPayload
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.security.network.model.securitysettings.createTestManagementSecuritySettingsPayload
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.security.network.model.securitysettings.createTestOpenSecuritySettingsPayload

/**
 * Mock implementation of [ManagementSecuritySettingsApi] for testing.
 */
@InternalApi
public class ManagementSecuritySettingsApiMock(
    public var getResult: AppResult<ManagementSecuritySettingsPayload> = AppResult.Success(
        createTestManagementSecuritySettingsPayload()
    ),
    public var getOpenResult: AppResult<OpenSecuritySettingsPayload> = AppResult.Success(
        createTestOpenSecuritySettingsPayload()
    ),
    public var updateResult: AppResult<ManagementSecuritySettingsPayload> = AppResult.Success(
        createTestManagementSecuritySettingsPayload()
    ),
    public var resetResult: AppResult<ManagementSecuritySettingsPayload> = AppResult.Success(
        createTestManagementSecuritySettingsPayload()
    )
) : ManagementSecuritySettingsApi {

    public override suspend fun getManagementSecuritySettings(): AppResult<ManagementSecuritySettingsPayload> =
        getResult

    public override suspend fun getOpenSecuritySettings(): AppResult<OpenSecuritySettingsPayload> = getOpenResult

    public override suspend fun updateManagementSecuritySettings(
        request: ManagementSecuritySettingsPayload
    ): AppResult<ManagementSecuritySettingsPayload> =
        updateResult

    public override suspend fun resetManagementSecuritySettings(): AppResult<ManagementSecuritySettingsPayload> =
        resetResult
}
