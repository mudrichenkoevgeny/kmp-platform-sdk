package io.github.mudrichenkoevgeny.kmp.feature.user.mock.network.model.auth.settings

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.auth.settings.AvailableAuthProvidersPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.auth.settings.PublicAuthSettingsPayload

@InternalApi
fun publicAuthSettingsPayloadMock(): PublicAuthSettingsPayload = PublicAuthSettingsPayload(
    availableAuthProviders = AvailableAuthProvidersPayload(
        primary = listOf(UserAuthProvider.EMAIL.serialName),
        secondary = emptyList()
    ),
    maxTotalIdentifiers = 5,
    maxEmailIdentifiers = 2,
    maxPhoneIdentifiers = 2,
    maxIdentifiersPerExternalProvider = 2
)