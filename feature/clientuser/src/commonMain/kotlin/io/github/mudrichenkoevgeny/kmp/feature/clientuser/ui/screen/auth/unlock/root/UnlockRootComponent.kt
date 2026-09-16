package io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.unlock.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.unlock.UnlockDestination
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.otp.UnlockOtpComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.selection.UnlockMethodSelectionComponent

/**
 * Root component for managing the account unlock stack navigation flow.
 */
interface UnlockRootComponent {

    /** Current navigation stack. */
    val stack: Value<ChildStack<UnlockDestination, Child>>

    /** Dismisses or closes the unlock flow surface. */
    fun onDismiss()

    /** Active child step in the unlock flow. */
    sealed interface Child {
        /** Channel method selection step. */
        class MethodSelection(val component: UnlockMethodSelectionComponent) : Child

        /** OTP confirmation code entry step. */
        class OtpInput(val component: UnlockOtpComponent) : Child

        /** Success state screen step. */
        object Success : Child
    }
}
