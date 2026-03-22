package io.github.mudrichenkoevgeny.kmp.feature.user.mapper.auth.settings

import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.settings.AuthSettings
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.response.auth.settings.AuthSettingsResponse

/**
 * Maps remote auth settings into [AuthSettings], including provider lists via `toAvailableAuthProviders`.
 */
fun AuthSettingsResponse.toAuthSettings() = AuthSettings(
    availableAuthProviders = this.availableAuthProviders.toAvailableAuthProviders()
)