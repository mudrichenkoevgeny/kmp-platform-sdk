package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.auth.login.root

import androidx.compose.runtime.Composable
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.email.LoginByEmailScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.pendingdeletion.PendingDeletionScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.root.LoginRootContainer
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.totp.LoginByTotpScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.welcome.LoginWelcomeScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.resetpassword.ResetEmailPasswordScreen

@Composable
fun ManagementLoginRootScreen(component: ManagementLoginRootComponent) {
    LoginRootContainer(
        stack = component.stack,
        onDismiss = component::onDismiss
    ) { instance ->
        when (instance) {
            is ManagementLoginRootComponent.Child.Welcome -> LoginWelcomeScreen(instance.component)
            is ManagementLoginRootComponent.Child.LoginByEmail -> LoginByEmailScreen(instance.component)
            is ManagementLoginRootComponent.Child.ResetEmailPassword -> ResetEmailPasswordScreen(instance.component)
            is ManagementLoginRootComponent.Child.LoginByTotp -> LoginByTotpScreen(instance.component)
            is ManagementLoginRootComponent.Child.PendingDeletion -> PendingDeletionScreen(instance.component)
        }
    }
}