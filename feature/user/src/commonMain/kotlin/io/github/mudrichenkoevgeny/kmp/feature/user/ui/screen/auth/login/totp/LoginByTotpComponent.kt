package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.totp

import com.arkivanov.decompose.value.Value

/**
 * MFA login: handles TOTP and recovery code entry, validation, and submission.
 */
interface LoginByTotpComponent {
    /**
     * Current UI state for [LoginByTotpScreen].
     */
    val state: Value<LoginByTotpScreenState>

    /**
     * Updates the verification code text.
     *
     * @param code new code value.
     */
    fun onCodeChanged(code: String)

    /**
     * Toggles between TOTP and Recovery Code entry modes.
     */
    fun onToggleModeClick()

    /**
     * Executes the login use case based on the current mode.
     */
    fun onSubmitClick()

    /**
     * Navigates back to the previous screen (usually password login).
     */
    fun onBackClick()
}
