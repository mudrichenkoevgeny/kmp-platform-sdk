package io.github.mudrichenkoevgeny.kmp.core.security.mock.network.model.securitysettings

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.security.mock.network.model.accountlockout.accountLockoutPolicyPayloadMock
import io.github.mudrichenkoevgeny.kmp.core.security.mock.network.model.iprestriction.ipRestrictionPolicyPayloadMock
import io.github.mudrichenkoevgeny.kmp.core.security.mock.network.model.otpconfirmation.otpConfirmationPayloadMock
import io.github.mudrichenkoevgeny.kmp.core.security.mock.network.model.passwordpolicy.managementPasswordPolicyPayloadMock
import io.github.mudrichenkoevgeny.kmp.core.security.mock.network.model.passwordpolicy.openPasswordPolicyPayloadMock
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.accountlockout.AccountLockoutPolicyPayload
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.iprestriction.IpRestrictionPolicyPayload
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.otpconfirmation.OtpConfirmationPayload
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.passwordpolicy.ManagementPasswordPolicyPayload
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.passwordpolicy.OpenPasswordPolicyPayload
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.securitysettings.ManagementSecuritySettingsPayload
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.securitysettings.OpenSecuritySettingsPayload

@InternalApi
fun openSecuritySettingsPayloadMock(
    passwordPolicy: OpenPasswordPolicyPayload = openPasswordPolicyPayloadMock(),
    otpConfirmation: OtpConfirmationPayload = otpConfirmationPayloadMock()
) = OpenSecuritySettingsPayload(
    passwordPolicy = passwordPolicy,
    otpConfirmation = otpConfirmation
)

@InternalApi
fun managementSecuritySettingsPayloadMock(
    recentAuthenticationValiditySecondsForOpenUser: Int = 300,
    recentAuthenticationValiditySecondsForManagementUser: Int = 60,
    passwordPolicy: ManagementPasswordPolicyPayload = managementPasswordPolicyPayloadMock(),
    otpConfirmation: OtpConfirmationPayload = otpConfirmationPayloadMock(),
    accountLockoutPolicy: AccountLockoutPolicyPayload = accountLockoutPolicyPayloadMock(),
    accountLockoutCheckIntervalSeconds: Int = 60,
    openIpRestrictionPolicy: IpRestrictionPolicyPayload = ipRestrictionPolicyPayloadMock(),
    managementIpRestrictionPolicy: IpRestrictionPolicyPayload = ipRestrictionPolicyPayloadMock(),
    mfaTokenExpirationSeconds: Int = 180,
    maxRequestsPerPeriod: Int = 100,
    rateLimitPeriodSeconds: Int = 60,
    refreshTokenRotationGracePeriodSeconds: Int = 30
) = ManagementSecuritySettingsPayload(
    recentAuthenticationValiditySecondsForOpenUser = recentAuthenticationValiditySecondsForOpenUser,
    recentAuthenticationValiditySecondsForManagementUser = recentAuthenticationValiditySecondsForManagementUser,
    passwordPolicy = passwordPolicy,
    otpConfirmation = otpConfirmation,
    accountLockoutPolicy = accountLockoutPolicy,
    accountLockoutCheckIntervalSeconds = accountLockoutCheckIntervalSeconds,
    openIpRestrictionPolicy = openIpRestrictionPolicy,
    managementIpRestrictionPolicy = managementIpRestrictionPolicy,
    mfaTokenExpirationSeconds = mfaTokenExpirationSeconds,
    maxRequestsPerPeriod = maxRequestsPerPeriod,
    rateLimitPeriodSeconds = rateLimitPeriodSeconds,
    refreshTokenRotationGracePeriodSeconds = refreshTokenRotationGracePeriodSeconds
)
