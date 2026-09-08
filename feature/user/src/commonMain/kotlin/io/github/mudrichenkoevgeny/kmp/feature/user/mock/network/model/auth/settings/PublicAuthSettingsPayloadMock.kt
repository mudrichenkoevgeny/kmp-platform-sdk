package io.github.mudrichenkoevgeny.kmp.feature.user.mock.network.model.auth.settings

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.auth.settings.OpenAuthSettingsPayload

@InternalApi
fun openAuthSettingsPayloadMock(
    isRegistrationEnabled: Boolean = true
): OpenAuthSettingsPayload = OpenAuthSettingsPayload(
    availableAuthProviders = availableAuthProvidersPayloadMock(),
    maxTotalIdentifiers = 5,
    maxEmailIdentifiers = 2,
    maxPhoneIdentifiers = 2,
    maxIdentifiersPerExternalProvider = 2,
    isRegistrationEnabled = isRegistrationEnabled
)
