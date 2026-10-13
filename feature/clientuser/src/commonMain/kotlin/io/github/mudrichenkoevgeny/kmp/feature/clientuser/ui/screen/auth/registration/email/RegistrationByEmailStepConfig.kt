package io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.registration.email

import kotlinx.serialization.Serializable

/**
 * Serializable configurations driving the Decompose navigation stack for the registration by email wizard.
 */
@Serializable
sealed interface RegistrationByEmailStepConfig {
    /** Step 1: entering email address. */
    @Serializable
    data class EmailInput(
        val initialEmail: String = ""
    ) : RegistrationByEmailStepConfig

    /** Step 2: entering confirmation OTP and password. */
    @Serializable
    data class ConfirmInput(
        val email: String,
        val resendTimerSeconds: Int = 0
    ) : RegistrationByEmailStepConfig
}
