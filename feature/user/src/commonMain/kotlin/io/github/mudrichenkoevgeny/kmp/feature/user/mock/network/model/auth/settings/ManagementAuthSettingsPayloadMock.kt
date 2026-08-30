package io.github.mudrichenkoevgeny.kmp.feature.user.mock.network.model.auth.settings

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.auth.settings.ManagementAuthSettingsPayload

@InternalApi
fun managementAuthSettingsPayloadMock(
    maxTotalIdentifiers: Int = 10,
    maxEmailIdentifiers: Int = 5,
    maxPhoneIdentifiers: Int = 5,
    maxIdentifiersPerExternalProvider: Int = 2,
    maxActiveSessions: Int = 3,
    accessTokenExpirationSeconds: Int = 3600,
    refreshTokenExpirationSeconds: Int = 86400,
    accountDeletionDelaySeconds: Int = 604800
): ManagementAuthSettingsPayload = ManagementAuthSettingsPayload(
    availableAuthProviders = availableAuthProvidersPayloadMock(),
    maxTotalIdentifiers = maxTotalIdentifiers,
    maxEmailIdentifiers = maxEmailIdentifiers,
    maxPhoneIdentifiers = maxPhoneIdentifiers,
    maxIdentifiersPerExternalProvider = maxIdentifiersPerExternalProvider,
    maxActiveSessions = maxActiveSessions,
    accessTokenExpirationSeconds = accessTokenExpirationSeconds,
    refreshTokenExpirationSeconds = refreshTokenExpirationSeconds,
    accountDeletionDelaySeconds = accountDeletionDelaySeconds
)