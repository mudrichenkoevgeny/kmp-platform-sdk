package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.network.api.security.settings

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.security.settings.ManagementSecuritySettingsApi
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.securitysettings.ManagementSecuritySettingsPayload
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.security.network.model.securitysettings.createTestManagementSecuritySettingsPayload

@InternalApi
class ManagementSecuritySettingsApiMock(
    var getResult: AppResult<ManagementSecuritySettingsPayload> = AppResult.Success(createTestManagementSecuritySettingsPayload()),
    var updateResult: AppResult<Unit> = AppResult.Success(Unit),
    var resetResult: AppResult<ManagementSecuritySettingsPayload> = AppResult.Success(createTestManagementSecuritySettingsPayload())
) : ManagementSecuritySettingsApi {

    override suspend fun getManagementSecuritySettings(): AppResult<ManagementSecuritySettingsPayload> = getResult

    override suspend fun updateManagementSecuritySettings(request: ManagementSecuritySettingsPayload): AppResult<Unit> = updateResult

    override suspend fun resetManagementSecuritySettings(): AppResult<ManagementSecuritySettingsPayload> = resetResult
}
