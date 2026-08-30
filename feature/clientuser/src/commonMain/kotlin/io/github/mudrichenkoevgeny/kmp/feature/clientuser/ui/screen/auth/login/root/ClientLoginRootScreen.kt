package io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.login.root

import androidx.compose.runtime.Composable
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.email.LoginByEmailScreen
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.login.phone.LoginByPhoneScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.root.LoginRootContainer
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.welcome.LoginWelcomeScreen
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.registration.email.RegistrationByEmailScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.resetpassword.ResetEmailPasswordScreen

@Composable
fun ClientLoginRootScreen(component: ClientLoginRootComponent) {
    LoginRootContainer(
        stack = component.stack,
        onDismiss = component::onDismiss
    ) { instance ->
        when (instance) {
            is ClientLoginRootComponent.Child.Welcome -> LoginWelcomeScreen(instance.component)
            is ClientLoginRootComponent.Child.LoginByEmail -> LoginByEmailScreen(instance.component)
            is ClientLoginRootComponent.Child.LoginByPhone -> LoginByPhoneScreen(instance.component)
            is ClientLoginRootComponent.Child.RegistrationByEmail -> RegistrationByEmailScreen(instance.component)
            is ClientLoginRootComponent.Child.ResetEmailPassword -> ResetEmailPasswordScreen(instance.component)
        }
    }
}