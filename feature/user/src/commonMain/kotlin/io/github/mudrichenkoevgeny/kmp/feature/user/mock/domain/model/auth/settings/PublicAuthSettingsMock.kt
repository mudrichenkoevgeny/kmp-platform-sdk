package io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.auth.settings

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.settings.AvailableAuthProviders
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.settings.PublicAuthSettings
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider

@InternalApi
fun publicAuthSettingsMock() = PublicAuthSettings(
    availableAuthProviders = AvailableAuthProviders(
        primary = listOf(UserAuthProvider.EMAIL),
        secondary = emptyList()
    ),
    maxTotalIdentifiers = 5,
    maxEmailIdentifiers = 2,
    maxPhoneIdentifiers = 2,
    maxIdentifiersPerExternalProvider = 2
)