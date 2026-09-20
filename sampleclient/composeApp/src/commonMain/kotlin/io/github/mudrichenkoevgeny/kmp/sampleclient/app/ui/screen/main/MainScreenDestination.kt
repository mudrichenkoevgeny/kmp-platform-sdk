package io.github.mudrichenkoevgeny.kmp.sampleclient.app.ui.screen.main

import io.github.mudrichenkoevgeny.kmp.core.common.Res as CommonRes
import io.github.mudrichenkoevgeny.kmp.core.common.*
import io.github.mudrichenkoevgeny.kmp.sampleclient.app.Res
import io.github.mudrichenkoevgeny.kmp.sampleclient.app.nav_home
import io.github.mudrichenkoevgeny.kmp.sampleclient.app.nav_profile
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

    companion object {
        /**
         * Tabs shown in mobile bottom navigation (order matches display).
         */
        val allDestinations = listOf(Home, Profile)

        /**
         * @param config Stack configuration for the active child.
         * @return Matching [MainScreenDestination] for labels and selection state.
         */
        fun fromConfig(config: MainScreenComponent.Config): MainScreenDestination =
            when (config) {
                is MainScreenComponent.Config.Home -> Home
                is MainScreenComponent.Config.Profile -> Profile
            }
    }
}
