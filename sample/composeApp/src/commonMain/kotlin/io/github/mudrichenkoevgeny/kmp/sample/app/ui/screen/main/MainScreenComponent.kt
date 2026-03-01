package io.github.mudrichenkoevgeny.kmp.sample.app.ui.screen.main

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.root.LoginRootComponent
import io.github.mudrichenkoevgeny.kmp.sample.app.ui.screen.home.HomeScreenComponent
import io.github.mudrichenkoevgeny.kmp.sample.app.ui.screen.profile.ProfileScreenComponent
import kotlinx.serialization.Serializable

interface MainScreenComponent {
    val stack: Value<ChildStack<Config, Child>>

    val loginDialogSlot: Value<ChildSlot<DialogConfig, LoginRootComponent>>

    fun onTabClick(config: Config)
    fun onShowLogin()
    fun onDismissLogin()

    sealed class Child {
        class HomeChild(val component: HomeScreenComponent) : Child()
        class ProfileChild(val component: ProfileScreenComponent) : Child()
    }

    sealed class Config {
        @Serializable object Home : Config()
        @Serializable object Profile : Config()
    }

    @Serializable
    sealed class DialogConfig {
        @Serializable object Login : DialogConfig()
    }
}