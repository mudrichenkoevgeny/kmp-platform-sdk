package io.github.mudrichenkoevgeny.kmp.sampleclient.app.navigation.deeplink

import io.github.mudrichenkoevgeny.kmp.sampleclient.app.ui.screen.main.MainScreenComponent
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.route.client.ClientAuthNavigation
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.route.client.ClientUserNavigation

/**
 * Top-level application deep link destinations supported by the client sample app.
 */
sealed interface ClientAppDeepLinkRoute {
    /** Target navigation tab configuration inside [MainScreenComponent]. */
    val tab: MainScreenComponent.Config

    /** Whether navigating to this deep link route requires an authenticated user session. */
    val requiresAuth: Boolean

    /**
     * Authentication flow deep link route (e.g. login, registration, password reset, unlock).
     *
     * @param destination Specific client authentication navigation route.
     * @param tab Target main screen tab (defaults to [MainScreenComponent.Config.User]).
     * @param requiresAuth Unauthenticated entry allowed for login routes (defaults to `false`).
     */
    data class Login(
        val destination: ClientAuthNavigation,
        override val tab: MainScreenComponent.Config = MainScreenComponent.Config.User,
        override val requiresAuth: Boolean = false
    ) : ClientAppDeepLinkRoute

    /**
     * Authenticated user main and settings deep link route.
     *
     * @param destination Specific client user navigation route.
     * @param tab Target main screen tab (defaults to [MainScreenComponent.Config.User]).
     * @param requiresAuth Authenticated entry required for user routes (defaults to `true`).
     */
    data class User(
        val destination: ClientUserNavigation,
        override val tab: MainScreenComponent.Config = MainScreenComponent.Config.User,
        override val requiresAuth: Boolean = true
    ) : ClientAppDeepLinkRoute
}
