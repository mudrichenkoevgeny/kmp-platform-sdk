package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.totp.recovery

import com.arkivanov.decompose.value.Value

/** Component for viewing and regenerating TOTP recovery codes. */
public interface TotpRecoveryCodesComponent {
    /** Reactive UI state. */
    public val state: Value<TotpRecoveryCodesScreenState>

    /** Shows regeneration confirmation dialog. */
    public fun onRegenerateClick()

    /** Executes regeneration after confirmation. */
    public fun onConfirmRegenerate()

    /** Hides confirmation dialogs. */
    public fun onDismissDialogs()

    /** Navigates back. */
    public fun onBackClick()
}
