package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.selection

import com.arkivanov.decompose.value.Value

/** Component for choosing an unlock method (email, phone, or external provider). */
public interface UnlockMethodSelectionComponent {

    /** Current screen state. */
    public val state: Value<UnlockMethodSelectionScreenState>

    /** Navigates to email input for email unlock. */
    public fun onSelectEmailUnlock()

    /** Navigates to phone input for phone unlock. */
    public fun onSelectPhoneUnlock()

    /** Triggers unlock via Google OAuth token. */
    public fun onSelectGoogleUnlock()

    /** Triggers unlock via Apple OAuth token. */
    public fun onSelectAppleUnlock()

    /** Navigates back. */
    public fun onBackClick()
}
