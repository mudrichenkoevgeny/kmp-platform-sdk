package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.totp

import com.arkivanov.decompose.value.Value

/**
 * MFA login: handles TOTP and recovery code entry, validation, and submission.
 */
public interface LoginByTotpComponent {
    /**
     * Current UI state for [LoginByTotpScreen].
     */
    public val state: Value<LoginByTotpScreenState>

    /**
     * Updates the verification code text.
     *
     * @param code new code value.
     */
    public fun onCodeChanged(code: String)

    /**
     * Toggles between TOTP and Recovery Code entry modes.
     */
    public fun onToggleModeClick()

    /**
     * Executes the login use case based on the current mode.
     */
    public fun onSubmitClick()

    /**
     * Navigates back to the previous screen (usually password login).
     */
    public fun onBackClick()
}
