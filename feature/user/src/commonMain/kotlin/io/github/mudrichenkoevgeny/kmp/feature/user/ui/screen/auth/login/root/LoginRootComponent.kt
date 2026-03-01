package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.LoginDestination
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.email.LoginByEmailComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.phone.LoginByPhoneComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.welcome.LoginWelcomeComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.password.ResetEmailPasswordComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.registration.email.RegistrationByEmailComponent

interface LoginRootComponent {
    val stack: Value<ChildStack<LoginDestination, Child>>

    fun onDismiss()

    sealed interface Child {
        class Welcome(val component: LoginWelcomeComponent) : Child
        class LoginByEmail(val component: LoginByEmailComponent) : Child
        class LoginByPhone(val component: LoginByPhoneComponent) : Child
        class RegistrationByEmail(val component: RegistrationByEmailComponent) : Child
        class ResetEmailPassword(val component: ResetEmailPasswordComponent) : Child
    }
}