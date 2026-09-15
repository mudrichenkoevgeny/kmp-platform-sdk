package io.github.mudrichenkoevgeny.kmp.core.security.mock.domain.model

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.security.mock.domain.model.accountlockout.accountLockoutPolicyMock
import io.github.mudrichenkoevgeny.kmp.core.security.mock.domain.model.iprestriction.ipRestrictionPolicyMock
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.accountlockout.AccountLockoutPolicy
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.iprestriction.IpRestrictionPolicy
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.otpconfirmation.OtpConfirmation
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.passwordpolicy.ManagementPasswordPolicy
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.passwordpolicy.OpenPasswordPolicy
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.securitysettings.ManagementSecuritySettings
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.securitysettings.OpenSecuritySettings

@InternalApi
fun openSecuritySettingsMock(
    passwordPolicy: OpenPasswordPolicy = openPasswordPolicyMock(),
    otpConfirmation: OtpConfirmation = otpConfirmationMock()
) = OpenSecuritySettings(
    passwordPolicy = passwordPolicy,
    otpConfirmation = otpConfirmation
)

@InternalApi
fun managementSecuritySettingsMock(
    recentAuthenticationValiditySecondsForOpenUser: Int = 300,
    recentAuthenticationValiditySecondsForManagementUser: Int = 60,
    passwordPolicy: ManagementPasswordPolicy = managementPasswordPolicyMock(),
    otpConfirmation: OtpConfirmation = otpConfirmationMock(),
    accountLockoutPolicy: AccountLockoutPolicy = accountLockoutPolicyMock(),
    accountLockoutCheckIntervalSeconds: Int = 60,
    openIpRestrictionPolicy: IpRestrictionPolicy = ipRestrictionPolicyMock(),
    managementIpRestrictionPolicy: IpRestrictionPolicy = ipRestrictionPolicyMock(),
    mfaTokenExpirationSeconds: Int = 180,
    maxRequestsPerPeriod: Int = 100,
    rateLimitPeriodSeconds: Int = 60,
    refreshTokenRotationGracePeriodSeconds: Int = 30
) = ManagementSecuritySettings(
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
