package io.github.mudrichenkoevgeny.kmp.feature.user.mapper.auth.settings

import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.settings.AvailableAuthProviders
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.response.auth.settings.AvailableAuthProvidersResponse

fun AvailableAuthProvidersResponse.toAvailableAuthProviders() = AvailableAuthProviders(
    primary = this.primary.mapNotNull { UserAuthProvider.fromValue(it) },
    secondary = this.secondary.mapNotNull { UserAuthProvider.fromValue(it) }
)