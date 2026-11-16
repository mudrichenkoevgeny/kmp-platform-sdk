package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.totp.main

import kotlinx.serialization.Serializable

/**
 * Serializable configurations driving the Decompose navigation stack for the TOTP setup and status wizard.
 */
@Serializable
public sealed interface TotpStepConfig {
    /**
     * Shows current TOTP status (Enabled summary or Disabled summary with "Set up 2FA" action).
     */
    @Serializable
    public data object Status : TotpStepConfig

    /**
     * Active setup step: displaying QR code, secret key, and code verification field.
     *
     * @param mfaToken MFA token required to verify setup.
     * @param otpAuthUrl QR code image URL or otpauth URI string.
     * @param secretKey Base32 encoded TOTP secret key for manual entry.
     */
    @Serializable
    public data class Setup(
        public val mfaToken: String,
        public val otpAuthUrl: String,
        public val secretKey: String
    ) : TotpStepConfig
}
