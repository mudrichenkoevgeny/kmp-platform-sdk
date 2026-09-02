package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.totp

import com.arkivanov.decompose.value.Value

/**
 * Manages the TOTP lifecycle: setup, enabling with code, viewing recovery codes, and disabling.
 */
interface TotpSettingsComponent {
    /** Reactive UI state. */
    val state: Value<TotpSettingsScreenState>

    /** Starts the TOTP setup flow. */
    fun onSetupClick()

    /** Updates the confirmation code during setup. */
    fun onCodeChanged(code: String)

    /** Finalizes setup using the verification code. */
    fun onConfirmSetupClick()

    /** Shows disable TOTP confirmation. */
    fun onDisableClick()

    /** Executes TOTP disabling after confirmation. */
    fun onConfirmDisable()

    /** Shows recovery code regeneration confirmation. */
    fun onRegenerateRecoveryCodesClick()

    /** Executes regeneration after confirmation. */
    fun onConfirmRegenerateRecoveryCodes()

    /** Hides any active confirmation dialogs. */
    fun onDismissDialogs()

    /** Navigates back. */
    fun onBackClick()
}
