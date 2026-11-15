package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.otp

import com.arkivanov.decompose.value.Value

/** Component for entering an OTP confirmation code to complete account unlock. */
public interface UnlockOtpComponent {

    /** Current screen state. */
    public val state: Value<UnlockOtpScreenState>

    /** Updates the typed OTP confirmation code. */
    public fun onCodeChanged(code: String)

    /** Submits the OTP code to unlock the account. */
    public fun onUnlockClick()

    /** Resends a new OTP confirmation code. */
    public fun onResendCodeClick()

    /** Navigates back. */
    public fun onBackClick()
}
