package io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.network.api.auth.settings

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.network.api.auth.settings.ManagementAuthSettingsApi
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.auth.settings.ManagementAuthSettingsPayload

@InternalApi
class ManagementAuthSettingsApiMock : ManagementAuthSettingsApi {

    var lastRequest: ManagementAuthSettingsPayload? = null
    var getCallCount = 0
    var updateCallCount = 0

    var getResultProvider: () -> AppResult<ManagementAuthSettingsPayload> = {
        AppResult.Error(CommonError.Unknown(isRetryable = false))
    }

    var updateResultProvider: () -> AppResult<Unit> = {
        AppResult.Success(Unit)
    }

    override suspend fun getManagementAuthSettings(): AppResult<ManagementAuthSettingsPayload> {
        getCallCount++
        return getResultProvider()
    }

    override suspend fun updateManagementAuthSettings(request: ManagementAuthSettingsPayload): AppResult<Unit> {
        updateCallCount++
        lastRequest = request
        return updateResultProvider()
    }
}