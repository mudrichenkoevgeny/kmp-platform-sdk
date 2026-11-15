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
        val mfaToken: String,
        val otpAuthUrl: String,
        val secretKey: String
    ) : TotpStepConfig
}

/**
 * Serializable configurations driving the Decompose child slot for TOTP modal dialogs.
 */
@Serializable
public sealed interface TotpDialogConfig {
    /** Modal dialog confirming TOTP disabling for the account. */
    @Serializable
    public data object ConfirmDisable : TotpDialogConfig
}
