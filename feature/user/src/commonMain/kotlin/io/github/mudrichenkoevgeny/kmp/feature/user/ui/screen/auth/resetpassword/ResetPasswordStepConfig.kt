package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.resetpassword

import kotlinx.serialization.Serializable

/**
 * Serializable configurations driving the Decompose navigation stack for the password reset wizard.
 */
@Serializable
sealed interface ResetPasswordStepConfig {
    /**
     * Initial step: entering the email address.
     *
     * @param initialEmail Pre-filled email string if returning from a subsequent step.
     */
    @Serializable
    data class EmailInput(
        val initialEmail: String = ""
    ) : ResetPasswordStepConfig

    /**
     * Confirmation step: entering the OTP code and new password.
     *
     * @param email Validated target email address.
     * @param resendTimerSeconds Cooldown timer for resending the verification code.
     */
    @Serializable
    data class ConfirmInput(
        val email: String,
        val resendTimerSeconds: Int = 0
    ) : ResetPasswordStepConfig
}
