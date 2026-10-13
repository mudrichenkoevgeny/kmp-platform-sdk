package io.github.mudrichenkoevgeny.kmp.sampleclient.app.navigation.deeplink

import io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.login.ClientLoginDestination
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.ProfileDestination
import io.github.mudrichenkoevgeny.kmp.sampleclient.app.ui.screen.main.MainScreenComponent

/**
 * Represents a parsed deep link route that can be executed by the client root component.
 */
sealed interface ClientAppDeepLinkRoute {
    /** Whether the user must be authenticated to access this route. */
    val requiresAuth: Boolean

    /** Top-level tab configuration required to display this route. */
    val tab: MainScreenComponent.Config

    /**
     * Route intended for the Login/Auth flow.
     *
     * @param destination The destination in the Login component stack.
     * @param tab The target main tab to select.
     * @param requiresAuth True if authentication is required.
     */
    data class Login(
        val destination: ClientLoginDestination,
        override val tab: MainScreenComponent.Config = MainScreenComponent.Config.Home,
        override val requiresAuth: Boolean = false
    ) : ClientAppDeepLinkRoute

    /**
     * Route intended for the Profile feature.
     *
     * @param destination The destination in the Profile component stack.
     * @param tab The target main tab to select.
     * @param requiresAuth True if authentication is required.
     */
    data class Profile(
        val destination: ProfileDestination,
        override val tab: MainScreenComponent.Config = MainScreenComponent.Config.Profile,
        override val requiresAuth: Boolean = destination !is ProfileDestination.Main
    ) : ClientAppDeepLinkRoute
}
