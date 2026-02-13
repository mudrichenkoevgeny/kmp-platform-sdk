package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.login.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.login.LoginDestination
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.login.welcome.LoginWelcomeComponentImpl

class LoginRootComponentImpl(
    componentContext: ComponentContext,
    private val onFinished: () -> Unit
) : LoginRootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<LoginDestination>()

    override val stack: Value<ChildStack<LoginDestination, LoginRootComponent.Child>> =
        childStack(
            source = navigation,
            serializer = LoginDestination.serializer(),
            initialConfiguration = LoginDestination.Welcome,
            handleBackButton = true,
            childFactory = ::createChild
        )

    private fun createChild(
        config: LoginDestination,
        context: ComponentContext
    ): LoginRootComponent.Child = when (config) {
        is LoginDestination.Welcome -> LoginRootComponent.Child.Welcome(
            LoginWelcomeComponentImpl(
                componentContext = context,
//                onNavigateTo = { dest -> navigation.push(dest) }
            )
        )
//        else -> {
//
//        }
    }

    override fun onDismiss() = onFinished()
}