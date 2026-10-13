package io.github.mudrichenkoevgeny.kmp.samplemanagement.app.ui.screen.main

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.auth.login.ManagementLoginDestination
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.auth.login.root.ManagementLoginRootComponent
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.root.ProfileRootComponent
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.root.ManagementRootComponent
import io.github.mudrichenkoevgeny.kmp.samplemanagement.app.navigation.deeplink.ManagementAppDeepLinkRoute
import io.github.mudrichenkoevgeny.kmp.samplemanagement.app.ui.screen.home.HomeScreenComponent
import kotlinx.serialization.Serializable

/**
 * Decompose root for the management sample: tab stack (home, profile, settings) and a dialog slot for login flow.
 */
interface MainScreenComponent {

    /** Observable indication of active user authorization state. */
    val isAuthorized: Value<Boolean>

    /** Navigation stack for primary destinations. */
    val stack: Value<ChildStack<Config, Child>>

    /** Navigator for the main stack. */
    val stackNavigation: StackNavigation<Config>

    /** Optional overlay slot hosting [ManagementLoginRootComponent] when login is requested. */
    val loginDialogSlot: Value<ChildSlot<DialogConfig, ManagementLoginRootComponent>>

    /** Handles an incoming parsed deep link. */
    fun handleDeepLink(route: ManagementAppDeepLinkRoute)

    /**
     * Brings the specified tab to the front of the stack.
     *
     * @param config Tab configuration to select.
     */
    fun onTabClick(config: Config)

    /** Opens the login dialog slot. */
    fun onShowLogin()

    /** Dismisses the login dialog slot. */
    fun onDismissLogin()

    /** Child instances created for each tab configuration. */
    sealed class Child {
        class HomeChild(val component: HomeScreenComponent) : Child()
        class ProfileChild(val component: ProfileRootComponent) : Child()
        class SettingsChild(val component: ManagementRootComponent) : Child()
    }

    /** Serializable tab keys for the main stack. */
    @Serializable
    sealed class Config {
        @Serializable object Home : Config()
        @Serializable object Profile : Config()
        @Serializable object Settings : Config()
    }

    /** Serializable keys for dialog-slot children. */
    @Serializable
    sealed class DialogConfig {
        @Serializable data class Login(
            val initialDestination: ManagementLoginDestination = ManagementLoginDestination.Welcome,
            val isDismissible: Boolean = true
        ) : DialogConfig()
    }
}
