package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.UnlockDestination
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.otp.UnlockOtpComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.selection.UnlockMethodSelectionComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.target.UnlockTargetInputComponent

/** Root Decompose interface for account unlock flow navigation stack. */
interface UnlockRootComponent {
    val stack: Value<ChildStack<UnlockDestination, Child>>
    fun onDismiss()

    sealed interface Child {
        class MethodSelection(val component: UnlockMethodSelectionComponent) : Child
        class TargetInput(val component: UnlockTargetInputComponent) : Child
        class OtpInput(val component: UnlockOtpComponent) : Child
        object Success : Child
    }
}
