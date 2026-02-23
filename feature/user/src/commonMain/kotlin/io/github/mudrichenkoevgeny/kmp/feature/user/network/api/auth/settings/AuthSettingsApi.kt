package io.github.mudrichenkoevgeny.kmp.feature.user.network.api.auth.settings

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.response.auth.settings.AuthSettingsResponse

interface AuthSettingsApi {
    suspend fun getAuthSettings(): AppResult<AuthSettingsResponse>
}