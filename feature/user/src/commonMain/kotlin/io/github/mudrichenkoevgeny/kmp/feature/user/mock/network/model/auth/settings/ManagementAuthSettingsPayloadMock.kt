package io.github.mudrichenkoevgeny.kmp.feature.user.mock.network.model.auth.settings

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.network.model.emailrestriction.emailRestrictionPolicyPayloadMock
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.auth.settings.ManagementAuthSettingsPayload
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.model.emailrestriction.EmailRestrictionPolicyPayload

@InternalApi
fun managementAuthSettingsPayloadMock(
    maxTotalIdentifiers: Int = 10,
    maxEmailIdentifiers: Int = 5,
    maxPhoneIdentifiers: Int = 5,
    maxIdentifiersPerExternalProvider: Int = 2,
    maxActiveSessionsForOpenUser: Int = 3,
    maxActiveSessionsForManagementUser: Int = 5,
    accessTokenExpirationSeconds: Int = 3600,
    refreshTokenExpirationSeconds: Int = 86400,
    accountDeletionGracePeriodSeconds: Int = 604800,
    accountDeletionCheckIntervalSeconds: Int = 86400,
    isRegistrationEnabled: Boolean = true,
    openEmailRestrictionPolicy: EmailRestrictionPolicyPayload = emailRestrictionPolicyPayloadMock(),
    managementEmailRestrictionPolicy: EmailRestrictionPolicyPayload = emailRestrictionPolicyPayloadMock()
): ManagementAuthSettingsPayload = ManagementAuthSettingsPayload(
    availableAuthProviders = availableAuthProvidersPayloadMock(),
    maxTotalIdentifiers = maxTotalIdentifiers,
    maxEmailIdentifiers = maxEmailIdentifiers,
    maxPhoneIdentifiers = maxPhoneIdentifiers,
    maxIdentifiersPerExternalProvider = maxIdentifiersPerExternalProvider,
    maxActiveSessionsForOpenUser = maxActiveSessionsForOpenUser,
    maxActiveSessionsForManagementUser = maxActiveSessionsForManagementUser,
    accessTokenExpirationSeconds = accessTokenExpirationSeconds,
    refreshTokenExpirationSeconds = refreshTokenExpirationSeconds,
    accountDeletionGracePeriodSeconds = accountDeletionGracePeriodSeconds,
    accountDeletionCheckIntervalSeconds = accountDeletionCheckIntervalSeconds,
    isRegistrationEnabled = isRegistrationEnabled,
    openEmailRestrictionPolicy = openEmailRestrictionPolicy,
    managementEmailRestrictionPolicy = managementEmailRestrictionPolicy
)
