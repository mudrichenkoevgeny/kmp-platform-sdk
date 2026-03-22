package io.github.mudrichenkoevgeny.kmp.feature.user.usecase.configuration

import io.github.mudrichenkoevgeny.kmp.feature.user.repository.auth.wireAuthSettingsResponse
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.response.settings.PasswordPolicyResponse
import io.github.mudrichenkoevgeny.shared.foundation.core.security.network.response.settings.SecuritySettingsResponse
import io.github.mudrichenkoevgeny.shared.foundation.core.settings.network.response.GlobalSettingsResponse
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.network.response.configuration.UserConfigurationResponse

private const val WIRE_PRIVACY_POLICY_URL = "https://privacy"
private const val WIRE_TERMS_OF_SERVICE_URL = "https://terms"
private const val WIRE_CONTACT_SUPPORT_EMAIL = "support@example.com"
private const val WIRE_PASSWORD_MIN_LENGTH = 8

internal fun wireUserConfigurationResponse(): UserConfigurationResponse = UserConfigurationResponse(
    globalSettings = GlobalSettingsResponse(
        privacyPolicyUrl = WIRE_PRIVACY_POLICY_URL,
        termsOfServiceUrl = WIRE_TERMS_OF_SERVICE_URL,
        contactSupportEmail = WIRE_CONTACT_SUPPORT_EMAIL
    ),
    securitySettings = SecuritySettingsResponse(
        passwordPolicy = PasswordPolicyResponse(
            minLength = WIRE_PASSWORD_MIN_LENGTH,
            requireLetter = true,
            requireUpperCase = false,
            requireLowerCase = false,
            requireDigit = false,
            requireSpecialChar = false,
            commonPasswords = emptySet()
        )
    ),
    authSettings = wireAuthSettingsResponse()
)
