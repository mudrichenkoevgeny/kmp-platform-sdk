package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.otp

import com.arkivanov.decompose.value.Value

/** Component for entering an OTP confirmation code to complete account unlock. */
interface UnlockOtpComponent {

    /** Current screen state. */
    val state: Value<UnlockOtpScreenState>

    /** Updates the typed OTP confirmation code. */
    fun onCodeChanged(code: String)

    /** Submits the OTP code to unlock the account. */
    fun onUnlockClick()

    /** Resends a new OTP confirmation code. */
    fun onResendCodeClick()

    /** Navigates back. */
    fun onBackClick()
}
