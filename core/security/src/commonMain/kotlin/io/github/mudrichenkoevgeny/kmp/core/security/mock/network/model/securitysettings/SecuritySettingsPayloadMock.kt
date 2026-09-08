package io.github.mudrichenkoevgeny.kmp.core.security.mock.network.model.securitysettings

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.security.mock.network.model.otpconfirmation.otpConfirmationPayloadMock
import io.github.mudrichenkoevgeny.kmp.core.security.mock.network.model.passwordpolicy.managementPasswordPolicyPayloadMock
import io.github.mudrichenkoevgeny.kmp.core.security.mock.network.model.passwordpolicy.openPasswordPolicyPayloadMock
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
    recentAuthenticationValiditySeconds: Int = 300,
    recentAuthenticationValiditySecondsForManagement: Int = 60,
    passwordPolicy: ManagementPasswordPolicyPayload = managementPasswordPolicyPayloadMock(),
    otpConfirmation: OtpConfirmationPayload = otpConfirmationPayloadMock(),
    mfaTokenExpirationSeconds: Int = 180,
    maxRequestsPerPeriod: Int = 100,
    rateLimitPeriodSeconds: Int = 60
) = ManagementSecuritySettingsPayload(
    recentAuthenticationValiditySeconds = recentAuthenticationValiditySeconds,
    recentAuthenticationValiditySecondsForManagement = recentAuthenticationValiditySecondsForManagement,
    passwordPolicy = passwordPolicy,
    otpConfirmation = otpConfirmation,
    mfaTokenExpirationSeconds = mfaTokenExpirationSeconds,
    maxRequestsPerPeriod = maxRequestsPerPeriod,
    rateLimitPeriodSeconds = rateLimitPeriodSeconds
)
