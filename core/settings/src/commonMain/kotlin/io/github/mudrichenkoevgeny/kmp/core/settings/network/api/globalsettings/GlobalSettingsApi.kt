package io.github.mudrichenkoevgeny.kmp.core.settings.network.api.globalsettings

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.network.response.GlobalSettingsResponse

interface GlobalSettingsApi {
    suspend fun getGlobalSettings(): AppResult<GlobalSettingsResponse>
}