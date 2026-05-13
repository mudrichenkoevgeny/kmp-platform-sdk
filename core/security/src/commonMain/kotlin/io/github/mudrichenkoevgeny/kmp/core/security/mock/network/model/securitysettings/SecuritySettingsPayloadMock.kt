package io.github.mudrichenkoevgeny.kmp.core.security.mock.network.model.securitysettings

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.security.mock.network.model.otpconfirmation.otpConfirmationPayloadMock
import io.github.mudrichenkoevgeny.kmp.core.security.mock.network.model.passwordpolicy.passwordPolicyPayloadMock
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.otpconfirmation.OtpConfirmationPayload
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.passwordpolicy.PasswordPolicyPayload
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.model.securitysettings.SecuritySettingsPayload

@InternalApi
fun securitySettingsPayloadMock(
    recentAuthenticationValiditySeconds: Int = 600,
    recentAuthenticationValiditySecondsForManagement: Int = 300,
    passwordPolicy: PasswordPolicyPayload = passwordPolicyPayloadMock(),
    otpConfirmation: OtpConfirmationPayload = otpConfirmationPayloadMock(),
    mfaTokenExpirationSeconds: Int = 600
) = SecuritySettingsPayload(
    recentAuthenticationValiditySeconds = recentAuthenticationValiditySeconds,
    recentAuthenticationValiditySecondsForManagement = recentAuthenticationValiditySecondsForManagement,
    passwordPolicy = passwordPolicy,
    otpConfirmation = otpConfirmation,
    mfaTokenExpirationSeconds = mfaTokenExpirationSeconds
)