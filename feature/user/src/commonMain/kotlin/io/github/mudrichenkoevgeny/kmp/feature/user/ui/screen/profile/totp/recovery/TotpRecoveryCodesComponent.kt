package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.totp.recovery

import com.arkivanov.decompose.value.Value

/** Component for viewing and regenerating TOTP recovery codes. */
interface TotpRecoveryCodesComponent {
    /** Reactive UI state. */
    val state: Value<TotpRecoveryCodesScreenState>

    /** Shows regeneration confirmation dialog. */
    fun onRegenerateClick()

    /** Executes regeneration after confirmation. */
    fun onConfirmRegenerate()

    /** Hides confirmation dialogs. */
    fun onDismissDialogs()

    /** Navigates back. */
    fun onBackClick()
}
