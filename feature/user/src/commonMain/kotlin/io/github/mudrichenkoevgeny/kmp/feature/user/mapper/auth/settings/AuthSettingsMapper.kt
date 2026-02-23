package io.github.mudrichenkoevgeny.kmp.feature.user.mapper.auth.settings

import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.settings.AuthSettings
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.response.auth.settings.AuthSettingsResponse

fun AuthSettingsResponse.toAuthSettings() = AuthSettings(
    availableAuthProviders = this.availableAuthProviders.toAvailableAuthProviders()
)