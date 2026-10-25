package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.network.api.configuration

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.configuration.ManagementUserConfigurationApi
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.configuration.ManagementUserConfigurationPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.configuration.OpenUserConfigurationPayload
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.network.model.configuration.createTestManagementUserConfigurationPayload
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.network.model.configuration.createTestOpenUserConfigurationPayload

@InternalApi
class ManagementUserConfigurationApiMock(
    var getManagementUserConfigurationResult: AppResult<ManagementUserConfigurationPayload> = AppResult.Success(
        createTestManagementUserConfigurationPayload()
    ),
    var getOpenUserConfigurationResult: AppResult<OpenUserConfigurationPayload> = AppResult.Success(
        createTestOpenUserConfigurationPayload()
    )
) : ManagementUserConfigurationApi {

    override suspend fun getManagementUserConfiguration(): AppResult<ManagementUserConfigurationPayload> =
        getManagementUserConfigurationResult

    override suspend fun getOpenUserConfiguration(): AppResult<OpenUserConfigurationPayload> =
        getOpenUserConfigurationResult
}
