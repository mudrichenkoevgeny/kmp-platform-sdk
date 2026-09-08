package io.github.mudrichenkoevgeny.kmp.samplemanagement.app.ui.screen.main

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.auth.login.root.ManagementLoginRootComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.settings.ManagementSettingsRootComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.ProfileRootComponent
import io.github.mudrichenkoevgeny.kmp.samplemanagement.app.ui.screen.home.HomeScreenComponent
import kotlinx.serialization.Serializable

/**
 * Decompose root for the sample: tab stack (home, profile, settings) and a dialog slot for the login flow.
 */
interface MainScreenComponent {
    /**
     * Navigation stack for primary destinations.
     */
    val stack: Value<ChildStack<Config, Child>>

    /**
     * Optional overlay slot hosting [ManagementLoginRootComponent] when login is requested from profile.
     */
    val loginDialogSlot: Value<ChildSlot<DialogConfig, ManagementLoginRootComponent>>

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
        class ProfileChild(val component: ProfileRootComponent) : Child()
        class SettingsChild(val component: ManagementSettingsRootComponent) : Child()
    }

    /**
     * Serializable tab keys for the main stack.
     */
    sealed class Config {
        @Serializable object Home : Config()
        @Serializable object Profile : Config()
        @Serializable object Settings : Config()
    }

    /**
     * Serializable keys for dialog-slot children.
     */
    @Serializable
    sealed class DialogConfig {
        @Serializable object Login : DialogConfig()
    }
}