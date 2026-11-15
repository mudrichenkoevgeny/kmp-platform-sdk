package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.network.api.auth.settings

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.auth.settings.ManagementAuthSettingsApi
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.auth.settings.ManagementAuthSettingsPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.auth.settings.OpenAuthSettingsPayload
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.network.model.auth.settings.createTestOpenAuthSettingsPayload

/**
 * Mock implementation of [ManagementAuthSettingsApi] for testing.
 */
@InternalApi
public class ManagementAuthSettingsApiMock : ManagementAuthSettingsApi {

    public var lastRequest: ManagementAuthSettingsPayload? = null
    public var getCallCount: Int = 0
    public var getOpenCallCount: Int = 0
    public var updateCallCount: Int = 0
    public var resetCallCount: Int = 0

    public var getResultProvider: () -> AppResult<ManagementAuthSettingsPayload> = {
        AppResult.Error(CommonError.Unknown(isRetryable = false))
    }

    public var getOpenResultProvider: () -> AppResult<OpenAuthSettingsPayload> = {
        AppResult.Success(createTestOpenAuthSettingsPayload())
    }

    public var updateResultProvider: () -> AppResult<ManagementAuthSettingsPayload> = {
        getResultProvider()
    }

    public var resetResultProvider: () -> AppResult<ManagementAuthSettingsPayload> = {
        getResultProvider()
    }

    public override suspend fun getManagementAuthSettings(): AppResult<ManagementAuthSettingsPayload> {
        getCallCount++
        return getResultProvider()
    }

    public override suspend fun getOpenAuthSettings(): AppResult<OpenAuthSettingsPayload> {
        getOpenCallCount++
        return getOpenResultProvider()
    }

    public override suspend fun updateManagementAuthSettings(request: ManagementAuthSettingsPayload): AppResult<ManagementAuthSettingsPayload> {
        updateCallCount++
        lastRequest = request
        return updateResultProvider()
    }

    public override suspend fun resetManagementAuthSettings(): AppResult<ManagementAuthSettingsPayload> {
        resetCallCount++
        return resetResultProvider()
    }
}
