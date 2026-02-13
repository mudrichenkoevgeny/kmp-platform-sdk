package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.login.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.login.LoginDestination
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.login.welcome.LoginWelcomeComponent

interface LoginRootComponent {
    val stack: Value<ChildStack<LoginDestination, Child>>

    fun onDismiss()

    sealed interface Child {
        class Welcome(val component: LoginWelcomeComponent) : Child
    }
}