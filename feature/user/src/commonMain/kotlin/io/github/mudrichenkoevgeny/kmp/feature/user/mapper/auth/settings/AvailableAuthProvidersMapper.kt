package io.github.mudrichenkoevgeny.kmp.feature.user.mapper.auth.settings

import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.settings.AvailableAuthProviders
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.response.auth.settings.AvailableAuthProvidersResponse

/**
 * Maps wire provider strings into [AvailableAuthProviders].
 *
 * Entries that [UserAuthProvider.fromValue] cannot parse are **dropped** (silent filter), preserving relative order for the rest.
 */
fun AvailableAuthProvidersResponse.toAvailableAuthProviders() = AvailableAuthProviders(
    primary = this.primary.mapNotNull { UserAuthProvider.fromValue(it) },
    secondary = this.secondary.mapNotNull { UserAuthProvider.fromValue(it) }
)