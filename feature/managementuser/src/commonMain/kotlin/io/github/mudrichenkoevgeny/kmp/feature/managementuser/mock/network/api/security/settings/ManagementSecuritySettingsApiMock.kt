package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.network.api.security.settings

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.security.mock.network.model.securitysettings.managementSecuritySettingsPayloadMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.security.settings.ManagementSecuritySettingsApi
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.securitysettings.ManagementSecuritySettingsPayload

@InternalApi
class ManagementSecuritySettingsApiMock(
    var getResult: AppResult<ManagementSecuritySettingsPayload> = AppResult.Success(managementSecuritySettingsPayloadMock()),
    var updateResult: AppResult<Unit> = AppResult.Success(Unit)
) : ManagementSecuritySettingsApi {

    override suspend fun getManagementSecuritySettings(): AppResult<ManagementSecuritySettingsPayload> = getResult

    override suspend fun updateManagementSecuritySettings(request: ManagementSecuritySettingsPayload): AppResult<Unit> = updateResult
}
