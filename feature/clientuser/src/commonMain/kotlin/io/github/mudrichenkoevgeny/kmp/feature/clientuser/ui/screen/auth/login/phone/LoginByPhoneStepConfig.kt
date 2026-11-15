package io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.login.phone

import kotlinx.serialization.Serializable

/**
 * Serializable configurations driving the Decompose navigation stack for the phone login wizard.
 */
@Serializable
public sealed interface LoginByPhoneStepConfig {
    /** Step 1: entering phone number. */
    @Serializable
    public data class PhoneInput(
        public val initialPhone: String = ""
    ) : LoginByPhoneStepConfig

    /** Step 2: entering confirmation OTP code. */
    @Serializable
    public data class CodeInput(
        public val phone: String,
        public val resendTimerSeconds: Int = 0
    ) : LoginByPhoneStepConfig
}
