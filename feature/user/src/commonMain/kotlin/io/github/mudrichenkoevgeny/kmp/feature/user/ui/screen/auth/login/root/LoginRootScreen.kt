package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.root

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalCommonComponent
import io.github.mudrichenkoevgeny.kmp.core.common.ui.theme.Dimens
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.email.LoginByEmailScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.phone.LoginByPhoneScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.welcome.LoginWelcomeScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.password.ResetEmailPasswordScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.registration.email.RegistrationByEmailScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginRootScreen(component: LoginRootComponent) {
    val commonComponent = LocalCommonComponent.current

    if (commonComponent.platformRepository.getDeviceInfo().isMobileClient()) {
        ModalBottomSheet(onDismissRequest = component::onDismiss) {
            LoginDialogContainer(component)
        }
    } else {
        Dialog(onDismissRequest = component::onDismiss) {
            LoginDialogContainer(component)
        }
    }
}

@Composable
private fun LoginDialogContainer(component: LoginRootComponent) {
    Surface(
        modifier = Modifier.fillMaxWidth().wrapContentHeight(),
        shape = RoundedCornerShape(Dimens.roundedCornerShape),
        color = MaterialTheme.colorScheme.surface
    ) {
        Children(
            stack = component.stack,
            animation = stackAnimation(slide())
        ) { child ->
            when (val instance = child.instance) {
                is LoginRootComponent.Child.Welcome -> LoginWelcomeScreen(instance.component)
                is LoginRootComponent.Child.LoginByEmail -> LoginByEmailScreen(instance.component)
                is LoginRootComponent.Child.LoginByPhone -> LoginByPhoneScreen(instance.component)
                is LoginRootComponent.Child.RegistrationByEmail -> RegistrationByEmailScreen(instance.component)
                is LoginRootComponent.Child.ResetEmailPassword -> ResetEmailPasswordScreen(instance.component)
            }
        }
    }
}