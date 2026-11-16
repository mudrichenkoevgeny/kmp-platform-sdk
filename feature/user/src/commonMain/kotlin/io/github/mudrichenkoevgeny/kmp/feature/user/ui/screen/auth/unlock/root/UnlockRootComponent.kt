package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.otp.UnlockOtpComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.selection.UnlockMethodSelectionComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.target.UnlockTargetInputComponent
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.route.client.ClientAuthNavigation

/** Root Decompose interface for account unlock flow navigation stack. */
public interface UnlockRootComponent {
    /** Navigation stack driving the unlock steps. */
    public val stack: Value<ChildStack<ClientAuthNavigation, Child>>

    /** Dismisses the unlock flow. */
    public fun onDismiss(): Unit

    /**
     * Child instances for the unlock stack.
     */
    public sealed interface Child {
        /** Method selection step. */
        public class MethodSelection(public val component: UnlockMethodSelectionComponent) : Child

        /** Target input step. */
        public class TargetInput(public val component: UnlockTargetInputComponent) : Child

        /** OTP verification step. */
        public class OtpInput(public val component: UnlockOtpComponent) : Child

        /** Success step. */
        public object Success : Child
    }
}
