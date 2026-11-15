package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.totp.main

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.value.Value

/**
 * Manages the TOTP lifecycle: setup, enabling with code, viewing recovery codes, and disabling.
 */
public interface TotpMainComponent {
    /**
     * Decompose navigation stack driving the TOTP steps.
     */
    public val stack: Value<ChildStack<TotpStepConfig, Child>>

    /**
     * Stack navigator for TOTP steps.
     */
    public val stackNavigation: StackNavigation<TotpStepConfig>

    /**
     * Modal dialog slot (e.g., disable TOTP confirmation).
     */
    public val dialogSlot: Value<ChildSlot<TotpDialogConfig, DialogChild>>

    /**
     * Dialog slot navigator.
     */
    public val dialogSlotNavigation: SlotNavigation<TotpDialogConfig>

    /** Reactive UI state. */
    public val state: Value<TotpMainScreenState>

    /** Child instances corresponding to each wizard step. */
    public sealed interface Child {
        public data object Status : Child
        public data class Setup(
            val mfaToken: String,
            val otpAuthUrl: String,
            val secretKey: String
        ) : Child
    }

    /** Dialog child instances. */
    public sealed interface DialogChild {
        public data object ConfirmDisable : DialogChild
    }

    /** Starts the TOTP setup flow. */
    public fun onSetupClick()

    /** Updates the confirmation code during setup. */
    public fun onCodeChanged(code: String)

    /** Finalizes setup using the verification code. */
    public fun onConfirmSetupClick()

    /** Opens recovery codes management screen. */
    public fun onRecoveryCodesClick()

    /** Shows disable TOTP confirmation. */
    public fun onDisableClick()

    /** Executes TOTP disabling after confirmation. */
    public fun onConfirmDisable()

    /** Hides any active confirmation dialogs. */
    public fun onDismissDialogs()

    /** Navigates back. */
    public fun onBackClick()
}
