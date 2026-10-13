package io.github.mudrichenkoevgeny.kmp.samplemanagement.app.navigation.deeplink

import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.ManagementDestination
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.ProfileDestination
import io.github.mudrichenkoevgeny.kmp.samplemanagement.app.ui.screen.main.MainScreenComponent

/**
 * Represents a parsed deep link route that can be executed by the root component.
 */
sealed interface ManagementAppDeepLinkRoute {
    /** Whether the user must be authenticated to access this route. */
    val requiresAuth: Boolean

    /** Top-level tab configuration required to display this route. */
    val tab: MainScreenComponent.Config

    /**
     * Route intended for the Management feature.
     *
     * @param destination The destination in the Management component stack.
     * @param tab The target main tab to select.
     * @param requiresAuth True if authentication is required.
     */
    data class Management(
        val destination: ManagementDestination,
        override val tab: MainScreenComponent.Config = MainScreenComponent.Config.Settings,
        override val requiresAuth: Boolean = true
    ) : ManagementAppDeepLinkRoute

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
    ) : ManagementAppDeepLinkRoute
}
