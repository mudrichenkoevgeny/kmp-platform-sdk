package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.target

import com.arkivanov.decompose.value.Value

/** Component for entering email or phone number to receive an account unlock OTP. */
interface UnlockTargetInputComponent {

    /** Current screen state. */
    val state: Value<UnlockTargetInputScreenState>

    /** Updates the typed email or phone input. */
    fun onInputChanged(value: String)

    /** Triggers sending the unlock confirmation code to the typed email or phone. */
    fun onSendCodeClick()

    /** Navigates back to method selection. */
    fun onBackClick()
}
