package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.selection

import com.arkivanov.decompose.value.Value

/** Component for choosing an unlock method (email, phone, or external provider). */
interface UnlockMethodSelectionComponent {

    /** Current screen state. */
    val state: Value<UnlockMethodSelectionScreenState>

    /** Updates the typed email address. */
    fun onEmailInputChanged(email: String)

    /** Updates the typed phone number. */
    fun onPhoneInputChanged(phone: String)

    /** Triggers sending email unlock OTP and navigating to OTP input. */
    fun onSelectEmailUnlock()

    /** Triggers sending phone unlock OTP and navigating to OTP input. */
    fun onSelectPhoneUnlock()

    /** Triggers unlock via Google OAuth token. */
    fun onSelectGoogleUnlock()

    /** Triggers unlock via Apple OAuth token. */
    fun onSelectAppleUnlock()

    /** Navigates back. */
    fun onBackClick()
}
