package io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.registration.email

import kotlinx.serialization.Serializable

/**
 * Serializable configurations driving the Decompose navigation stack for the registration by email wizard.
 */
@Serializable
public sealed interface RegistrationByEmailStepConfig {
    /** Step 1: entering email address. */
    @Serializable
    public data class EmailInput(
        public val initialEmail: String = ""
    ) : RegistrationByEmailStepConfig

    /** Step 2: entering confirmation OTP and password. */
    @Serializable
    public data class ConfirmInput(
        public val email: String,
        public val resendTimerSeconds: Int = 0
    ) : RegistrationByEmailStepConfig
}
