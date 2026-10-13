package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.totp.main

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.value.Value

/**
 * Manages the TOTP lifecycle: setup, enabling with code, viewing recovery codes, and disabling.
 */
interface TotpMainComponent {
    /**
     * Decompose navigation stack driving the TOTP steps.
     */
    val stack: Value<ChildStack<TotpStepConfig, Child>>

    /**
     * Stack navigator for TOTP steps.
     */
    val stackNavigation: StackNavigation<TotpStepConfig>

    /**
     * Modal dialog slot (e.g., disable TOTP confirmation).
     */
    val dialogSlot: Value<ChildSlot<TotpDialogConfig, DialogChild>>

    /**
     * Dialog slot navigator.
     */
    val dialogSlotNavigation: SlotNavigation<TotpDialogConfig>

    /** Reactive UI state. */
    val state: Value<TotpMainScreenState>

    /** Child instances corresponding to each wizard step. */
    sealed interface Child {
        data object Status : Child
        data class Setup(val mfaToken: String, val otpAuthUrl: String, val secretKey: String) : Child
    }

    /** Dialog child instances. */
    sealed interface DialogChild {
        data object ConfirmDisable : DialogChild
    }

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
