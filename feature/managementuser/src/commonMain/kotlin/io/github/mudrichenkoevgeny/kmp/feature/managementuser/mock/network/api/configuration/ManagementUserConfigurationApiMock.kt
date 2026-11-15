package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.network.api.configuration

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.configuration.ManagementUserConfigurationApi
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.configuration.ManagementUserConfigurationPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.configuration.OpenUserConfigurationPayload
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.network.model.configuration.createTestManagementUserConfigurationPayload
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.network.model.configuration.createTestOpenUserConfigurationPayload

/**
 * Mock implementation of [ManagementUserConfigurationApi] for testing.
 */
@InternalApi
public class ManagementUserConfigurationApiMock(
    public var getManagementUserConfigurationResult: AppResult<ManagementUserConfigurationPayload> = AppResult.Success(
        createTestManagementUserConfigurationPayload()
    ),
    public var getOpenUserConfigurationResult: AppResult<OpenUserConfigurationPayload> = AppResult.Success(
        createTestOpenUserConfigurationPayload()
    )
) : ManagementUserConfigurationApi {

    public override suspend fun getManagementUserConfiguration(): AppResult<ManagementUserConfigurationPayload> =
        getManagementUserConfigurationResult

    public override suspend fun getOpenUserConfiguration(): AppResult<OpenUserConfigurationPayload> =
        getOpenUserConfigurationResult
}
