package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.selection

import com.arkivanov.decompose.value.Value

/** Component for choosing an unlock method (email, phone, or external provider). */
interface UnlockMethodSelectionComponent {

    /** Current screen state. */
    val state: Value<UnlockMethodSelectionScreenState>

    /** Navigates to email input for email unlock. */
    fun onSelectEmailUnlock()

    /** Navigates to phone input for phone unlock. */
    fun onSelectPhoneUnlock()

    /** Triggers unlock via Google OAuth token. */
    fun onSelectGoogleUnlock()

    /** Triggers unlock via Apple OAuth token. */
    fun onSelectAppleUnlock()

    /** Navigates back. */
    fun onBackClick()
}
