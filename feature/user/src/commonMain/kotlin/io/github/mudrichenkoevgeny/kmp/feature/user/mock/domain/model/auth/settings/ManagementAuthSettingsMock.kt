package io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.auth.settings

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.emailrestriction.emailRestrictionPolicyMock
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.settings.ManagementAuthSettings
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.emailrestriction.EmailRestrictionPolicy

@InternalApi
fun managementAuthSettingsMock(
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
    openEmailRestrictionPolicy: EmailRestrictionPolicy = emailRestrictionPolicyMock(),
    managementEmailRestrictionPolicy: EmailRestrictionPolicy = emailRestrictionPolicyMock()
): ManagementAuthSettings = ManagementAuthSettings(
    availableAuthProviders = availableAuthProvidersMock(),
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
