package io.github.mudrichenkoevgeny.kmp.sample.app.ui.screen.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import io.github.mudrichenkoevgeny.kmp.sample.app.Res
import io.github.mudrichenkoevgeny.kmp.sample.app.nav_home
import io.github.mudrichenkoevgeny.kmp.sample.app.nav_profile
import org.jetbrains.compose.resources.StringResource

/**
 * UI-facing tab model: ties [MainScreenComponent.Config] to localized titles and toolbar icons.
 */
sealed interface MainScreenDestination {
    val config: MainScreenComponent.Config
    val title: StringResource
    val icon: ImageVector

    data object Home : MainScreenDestination {
        override val config = MainScreenComponent.Config.Home
        override val title = Res.string.nav_home
        override val icon = Icons.Default.Home
    }

    data object Profile : MainScreenDestination {
        override val config = MainScreenComponent.Config.Profile
        override val title = Res.string.nav_profile
        override val icon = Icons.Default.Person
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