package io.github.mudrichenkoevgeny.kmp.samplemanagement.app.navigation.deeplink

import io.github.mudrichenkoevgeny.kmp.samplemanagement.app.ui.screen.main.MainScreenComponent
import io.github.mudrichenkoevgeny.shared.foundation.core.common.navigation.route.NavigationRoute
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.route.client.ClientUserNavigation

/**
 * Top-level application deep link destinations supported by the management sample portal.
 */
sealed interface ManagementAppDeepLinkRoute {
    /** Target navigation tab configuration inside [MainScreenComponent]. */
    val tab: MainScreenComponent.Config

    /** Whether navigating to this deep link route requires an authenticated user session. */
    val requiresAuth: Boolean

    /**
     * Administrative management portal deep link route.
     *
     * @param destination Specific management navigation route.
     * @param tab Target main screen tab (defaults to [MainScreenComponent.Config.Management]).
     * @param requiresAuth Authenticated entry required for administrative routes (defaults to `true`).
     */
    data class Management(
        val destination: NavigationRoute,
        override val tab: MainScreenComponent.Config = MainScreenComponent.Config.Management,
        override val requiresAuth: Boolean = true
    ) : ManagementAppDeepLinkRoute

    /**
     * Authenticated staff user main and settings deep link route.
     *
     * @param destination Specific client user navigation route.
     * @param tab Target main screen tab (defaults to [MainScreenComponent.Config.User]).
     * @param requiresAuth Authenticated entry required for user routes (defaults to `true`).
     */
    data class User(
        val destination: ClientUserNavigation,
        override val tab: MainScreenComponent.Config = MainScreenComponent.Config.User,
        override val requiresAuth: Boolean = true
    ) : ManagementAppDeepLinkRoute
}
