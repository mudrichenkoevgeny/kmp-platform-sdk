package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.totp.main.dialog

/**
 * Child instances created for TOTP dialog configurations.
 */
public sealed interface TotpDialogChild {
    /** Confirmation dialog instance for disabling TOTP. */
    public data object ConfirmDisable : TotpDialogChild
}
