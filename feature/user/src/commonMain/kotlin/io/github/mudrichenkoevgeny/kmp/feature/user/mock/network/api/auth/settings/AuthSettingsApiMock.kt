package io.github.mudrichenkoevgeny.kmp.feature.user.mock.network.api.auth.settings

import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.feature.user.network.api.auth.settings.AuthSettingsApi
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.auth.settings.PublicAuthSettingsPayload

@InternalApi
class AuthSettingsApiMock : AuthSettingsApi {
    var result: AppResult<PublicAuthSettingsPayload> = AppResult.Error(CommonError.Unknown(isRetryable = false))
    var callCount = 0

    override suspend fun getAuthSettings(): AppResult<PublicAuthSettingsPayload> {
        callCount++
        return result
    }
}