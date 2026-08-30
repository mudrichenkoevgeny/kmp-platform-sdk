package io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.auth.settings

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.settings.PublicAuthSettings

@InternalApi
fun publicAuthSettingsMock() = PublicAuthSettings(
    availableAuthProviders = availableAuthProvidersMock(),
    maxTotalIdentifiers = 5,
    maxEmailIdentifiers = 2,
    maxPhoneIdentifiers = 2,
    maxIdentifiersPerExternalProvider = 2
)