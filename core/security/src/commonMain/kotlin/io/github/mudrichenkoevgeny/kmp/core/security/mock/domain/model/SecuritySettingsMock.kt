package io.github.mudrichenkoevgeny.kmp.core.security.mock.domain.model

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
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
    recentAuthenticationValiditySeconds: Int = 300,
    recentAuthenticationValiditySecondsForManagement: Int = 60,
    passwordPolicy: ManagementPasswordPolicy = managementPasswordPolicyMock(),
    otpConfirmation: OtpConfirmation = otpConfirmationMock(),
    mfaTokenExpirationSeconds: Int = 180,
    maxRequestsPerPeriod: Int = 100,
    rateLimitPeriodSeconds: Int = 60
) = ManagementSecuritySettings(
    recentAuthenticationValiditySeconds = recentAuthenticationValiditySeconds,
    recentAuthenticationValiditySecondsForManagement = recentAuthenticationValiditySecondsForManagement,
    passwordPolicy = passwordPolicy,
    otpConfirmation = otpConfirmation,
    mfaTokenExpirationSeconds = mfaTokenExpirationSeconds,
    maxRequestsPerPeriod = maxRequestsPerPeriod,
    rateLimitPeriodSeconds = rateLimitPeriodSeconds
)
