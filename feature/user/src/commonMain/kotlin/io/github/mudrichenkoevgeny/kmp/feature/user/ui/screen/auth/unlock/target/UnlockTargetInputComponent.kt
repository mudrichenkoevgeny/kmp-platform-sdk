package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.target

import com.arkivanov.decompose.value.Value

/** Component for entering email or phone number to receive an account unlock OTP. */
public interface UnlockTargetInputComponent {

    /** Current screen state. */
    public val state: Value<UnlockTargetInputScreenState>

    /** Updates the typed email or phone input. */
    public fun onInputChanged(value: String)

    /** Triggers sending the unlock confirmation code to the typed email or phone. */
    public fun onSendCodeClick()

    /** Navigates back to method selection. */
    public fun onBackClick()
}
