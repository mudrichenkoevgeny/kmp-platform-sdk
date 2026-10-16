package io.github.mudrichenkoevgeny.kmp.sampleclient.app.ui.screen.main

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.login.root.ClientLoginRootComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.root.UserRootComponent
import io.github.mudrichenkoevgeny.kmp.sampleclient.app.navigation.deeplink.ClientAppDeepLinkRoute
import kotlinx.serialization.Serializable

/**
 * Decompose root for the sample: user stack and a dialog slot for the login flow.
 */
interface MainScreenComponent {
    /**
     * Navigation stack for primary destinations.
     */
    val stack: Value<ChildStack<Config, Child>>

    /**
     * Navigator for the main stack.
     */
    val stackNavigation: StackNavigation<Config>

    /**
     * Optional overlay slot hosting [ClientLoginRootComponent] when login is requested from user flow.
     */
    val loginDialogSlot: Value<ChildSlot<DialogConfig, ClientLoginRootComponent>>

    /**
     * Handles an incoming parsed deep link.
     */
    fun handleDeepLink(route: ClientAppDeepLinkRoute)

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
        class UserChild(val component: UserRootComponent) : Child()
    }

    /**
     * Serializable tab keys for the main stack.
     */
    @Serializable
    sealed class Config {
        @Serializable object User : Config()
    }

    /**
     * Serializable keys for dialog-slot children.
     */
    @Serializable
    sealed class DialogConfig {
        @Serializable data class Login(
            val isDismissible: Boolean = true
        ) : DialogConfig()
    }
}
