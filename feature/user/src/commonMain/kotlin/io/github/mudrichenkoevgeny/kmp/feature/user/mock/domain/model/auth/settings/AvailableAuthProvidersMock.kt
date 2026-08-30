package io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.auth.settings

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.settings.AvailableAuthProviders
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider

@InternalApi
fun availableAuthProvidersMock(
    primary: List<UserAuthProvider> = listOf(UserAuthProvider.EMAIL),
    secondary: List<UserAuthProvider> = listOf(UserAuthProvider.GOOGLE)
): AvailableAuthProviders = AvailableAuthProviders(
    primary = primary,
    secondary = secondary
)