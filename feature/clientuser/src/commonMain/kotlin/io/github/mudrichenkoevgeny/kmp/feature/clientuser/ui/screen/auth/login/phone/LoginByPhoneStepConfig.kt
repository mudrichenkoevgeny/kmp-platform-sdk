package io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.login.phone

import kotlinx.serialization.Serializable

/**
 * Serializable configurations driving the Decompose navigation stack for the phone login wizard.
 */
@Serializable
sealed interface LoginByPhoneStepConfig {
    /** Step 1: entering phone number. */
    @Serializable
    data class PhoneInput(
        val initialPhone: String = ""
    ) : LoginByPhoneStepConfig

    /** Step 2: entering confirmation OTP code. */
    @Serializable
    data class CodeInput(
        val phone: String,
        val resendTimerSeconds: Int = 0
    ) : LoginByPhoneStepConfig
}
