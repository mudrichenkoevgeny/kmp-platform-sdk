package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.totp.main

import com.arkivanov.decompose.value.Value

/**
 * Manages the TOTP lifecycle: setup, enabling with code, viewing recovery codes, and disabling.
 */
interface TotpMainComponent {
    /** Reactive UI state. */
    val state: Value<TotpMainScreenState>

    /** Starts the TOTP setup flow. */
    fun onSetupClick()

    /** Updates the confirmation code during setup. */
    fun onCodeChanged(code: String)

    /** Finalizes setup using the verification code. */
    fun onConfirmSetupClick()

    /** Opens recovery codes management screen. */
    fun onRecoveryCodesClick()

    /** Shows disable TOTP confirmation. */
    fun onDisableClick()

    /** Executes TOTP disabling after confirmation. */
    fun onConfirmDisable()

    /** Hides any active confirmation dialogs. */
    fun onDismissDialogs()

    /** Navigates back. */
    fun onBackClick()
}
