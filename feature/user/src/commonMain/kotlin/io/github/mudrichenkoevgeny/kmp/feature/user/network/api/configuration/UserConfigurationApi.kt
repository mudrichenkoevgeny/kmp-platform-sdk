package io.github.mudrichenkoevgeny.kmp.feature.user.network.api.configuration

import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.response.configuration.UserConfigurationResponse

interface UserConfigurationApi {
    suspend fun getUserConfiguration(): AppResult<UserConfigurationResponse>
}