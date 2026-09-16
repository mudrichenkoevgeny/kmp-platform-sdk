package io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.unlock.root

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.otp.UnlockOtpScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.selection.UnlockMethodSelectionScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.success.UnlockSuccessScreen

@Composable
fun UnlockRootScreen(component: UnlockRootComponent) {
    Children(stack = component.stack) { child ->
        when (val instance = child.instance) {
            is UnlockRootComponent.Child.MethodSelection -> UnlockMethodSelectionScreen(instance.component)
            is UnlockRootComponent.Child.OtpInput -> UnlockOtpScreen(instance.component)
            is UnlockRootComponent.Child.Success -> UnlockSuccessScreen(onFinished = component::onDismiss)
        }
    }
}
