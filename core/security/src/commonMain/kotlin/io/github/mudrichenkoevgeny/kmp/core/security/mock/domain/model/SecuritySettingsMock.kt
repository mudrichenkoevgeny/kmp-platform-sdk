package io.github.mudrichenkoevgeny.kmp.core.security.mock.domain.model

import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.otpconfirmation.OtpConfirmation
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.passwordpolicy.PasswordPolicy
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.securitysettings.SecuritySettings

@InternalApi
fun securitySettingsMock(
    recentAuthenticationValiditySeconds: Int = 300,
    recentAuthenticationValiditySecondsForManagement: Int = 60,
    passwordPolicy: PasswordPolicy = passwordPolicyMock(),
    otpConfirmation: OtpConfirmation =otpConfirmationMock(),
    mfaTokenExpirationSeconds: Int = 180
) = SecuritySettings(
    recentAuthenticationValiditySeconds = recentAuthenticationValiditySeconds,
    recentAuthenticationValiditySecondsForManagement = recentAuthenticationValiditySecondsForManagement,
    passwordPolicy = passwordPolicy,
    otpConfirmation = otpConfirmation,
    mfaTokenExpirationSeconds = mfaTokenExpirationSeconds
)