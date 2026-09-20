package io.github.mudrichenkoevgeny.kmp.samplemanagement.app.ui.screen.main

import io.github.mudrichenkoevgeny.kmp.core.common.Res as CommonRes
import io.github.mudrichenkoevgeny.kmp.core.common.*
import io.github.mudrichenkoevgeny.kmp.samplemanagement.app.Res
import io.github.mudrichenkoevgeny.kmp.samplemanagement.app.nav_home
import io.github.mudrichenkoevgeny.kmp.samplemanagement.app.nav_profile
import io.github.mudrichenkoevgeny.kmp.samplemanagement.app.nav_settings
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

/**
 * UI-facing tab model: ties [MainScreenComponent.Config] to localized titles and toolbar icons.
 */
sealed interface MainScreenDestination {
    val config: MainScreenComponent.Config
    val title: StringResource
    val iconRes: DrawableResource

    data object Home : MainScreenDestination {
        override val config = MainScreenComponent.Config.Home
        override val title = Res.string.nav_home
        override val iconRes = CommonRes.drawable.ic_home
    }

    data object Profile : MainScreenDestination {
        override val config = MainScreenComponent.Config.Profile
        override val title = Res.string.nav_profile
        override val iconRes = CommonRes.drawable.ic_profile
    }

    data object Settings : MainScreenDestination {
        override val config = MainScreenComponent.Config.Settings
        override val title = Res.string.nav_settings
        override val iconRes = CommonRes.drawable.ic_settings
    }

    companion object {
        /**
         * Tabs shown in mobile bottom navigation (order matches display).
         */
        val allDestinations = listOf(Home, Profile, Settings)

        /**
         * @param isAuthorized Whether the user is authenticated.
         * @return List of destinations available to the current user state.
         */
        fun getDestinations(isAuthorized: Boolean): List<MainScreenDestination> =
            if (isAuthorized) {
                listOf(Home, Profile, Settings)
            } else {
                listOf(Home, Profile)
            }

        /**
         * @param config Stack configuration for the active child.
         * @return Matching [MainScreenDestination] for labels and selection state.
         */
        fun fromConfig(config: MainScreenComponent.Config): MainScreenDestination =
            when (config) {
                is MainScreenComponent.Config.Home -> Home
                is MainScreenComponent.Config.Profile -> Profile
                is MainScreenComponent.Config.Settings -> Settings
            }
    }
}
