package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.otp.UnlockOtpComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.selection.UnlockMethodSelectionComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.target.UnlockTargetInputComponent
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.route.client.ClientAuthNavigation

/** Root Decompose interface for account unlock flow navigation stack. */
public interface UnlockRootComponent {
    public val stack: Value<ChildStack<ClientAuthNavigation, Child>>
    public fun onDismiss(): Unit

    public sealed interface Child {
        public class MethodSelection(public val component: UnlockMethodSelectionComponent) : Child
        public class TargetInput(public val component: UnlockTargetInputComponent) : Child
        public class OtpInput(public val component: UnlockOtpComponent) : Child
        public object Success : Child
    }
}
