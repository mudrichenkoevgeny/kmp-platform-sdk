package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.network.api.auth.settings

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.auth.settings.ManagementAuthSettingsApi
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.auth.settings.ManagementAuthSettingsPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.auth.settings.OpenAuthSettingsPayload
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.network.model.auth.settings.createTestOpenAuthSettingsPayload

@InternalApi
class ManagementAuthSettingsApiMock : ManagementAuthSettingsApi {

    var lastRequest: ManagementAuthSettingsPayload? = null
    var getCallCount = 0
    var getOpenCallCount = 0
    var updateCallCount = 0
    var resetCallCount = 0

    var getResultProvider: () -> AppResult<ManagementAuthSettingsPayload> = {
        AppResult.Error(CommonError.Unknown(isRetryable = false))
    }

    var getOpenResultProvider: () -> AppResult<OpenAuthSettingsPayload> = {
        AppResult.Success(createTestOpenAuthSettingsPayload())
    }

    var updateResultProvider: () -> AppResult<ManagementAuthSettingsPayload> = {
        getResultProvider()
    }

    var resetResultProvider: () -> AppResult<ManagementAuthSettingsPayload> = {
        getResultProvider()
    }

    override suspend fun getManagementAuthSettings(): AppResult<ManagementAuthSettingsPayload> {
        getCallCount++
        return getResultProvider()
    }

    override suspend fun getOpenAuthSettings(): AppResult<OpenAuthSettingsPayload> {
        getOpenCallCount++
        return getOpenResultProvider()
    }

    override suspend fun updateManagementAuthSettings(request: ManagementAuthSettingsPayload): AppResult<ManagementAuthSettingsPayload> {
        updateCallCount++
        lastRequest = request
        return updateResultProvider()
    }

    override suspend fun resetManagementAuthSettings(): AppResult<ManagementAuthSettingsPayload> {
        resetCallCount++
        return resetResultProvider()
    }
}
