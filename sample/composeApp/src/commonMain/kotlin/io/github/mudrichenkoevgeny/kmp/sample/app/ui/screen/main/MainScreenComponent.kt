package io.github.mudrichenkoevgeny.kmp.sample.app.ui.screen.main

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.root.LoginRootComponent
import io.github.mudrichenkoevgeny.kmp.sample.app.ui.screen.home.HomeScreenComponent
import io.github.mudrichenkoevgeny.kmp.sample.app.ui.screen.profile.ProfileScreenComponent
import kotlinx.serialization.Serializable

/**
 * Decompose root for the sample: tab stack (home and profile) and a dialog slot for the login flow.
 */
interface MainScreenComponent {
    /**
     * Navigation stack for primary destinations.
     */
    val stack: Value<ChildStack<Config, Child>>

    /**
     * Optional overlay slot hosting [LoginRootComponent] when login is requested from profile.
     */
    val loginDialogSlot: Value<ChildSlot<DialogConfig, LoginRootComponent>>

    /**
     * @param config Tab configuration to bring to the front of the stack.
     */
    fun onTabClick(config: Config)

    /**
     * Opens the login dialog slot.
     */
    fun onShowLogin()

    /**
     * Dismisses the login dialog slot.
     */
    fun onDismissLogin()

    /**
     * Child instances created for each tab configuration.
     */
    sealed class Child {
        class HomeChild(val component: HomeScreenComponent) : Child()
        class ProfileChild(val component: ProfileScreenComponent) : Child()
    }

    /**
     * Serializable tab keys for the main stack.
     */
    sealed class Config {
        @Serializable object Home : Config()
        @Serializable object Profile : Config()
    }

    /**
     * Serializable keys for dialog-slot children.
     */
    @Serializable
    sealed class DialogConfig {
        @Serializable object Login : DialogConfig()
    }
}