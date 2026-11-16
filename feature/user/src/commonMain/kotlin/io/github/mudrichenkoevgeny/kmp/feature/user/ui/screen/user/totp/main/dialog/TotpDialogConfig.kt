package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.totp.main.dialog

import kotlinx.serialization.Serializable

/**
 * Serializable configurations driving the Decompose child slot for TOTP modal dialogs.
 */
@Serializable
public sealed interface TotpDialogConfig {
    /** Modal dialog confirming TOTP disabling for the account. */
    @Serializable
    public data object ConfirmDisable : TotpDialogConfig
}
