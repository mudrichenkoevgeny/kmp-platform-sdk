package io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.settings

import kotlinx.serialization.Serializable

/** Cached auth policy from the backend: which sign-in methods are enabled and how they are grouped. */
@Serializable
data class AuthSettings(
    val availableAuthProviders: AvailableAuthProviders
)