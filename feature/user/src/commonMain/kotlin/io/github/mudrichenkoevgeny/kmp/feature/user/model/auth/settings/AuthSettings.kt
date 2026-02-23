package io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.settings

import kotlinx.serialization.Serializable

@Serializable
data class AuthSettings(
    val availableAuthProviders: AvailableAuthProviders
)