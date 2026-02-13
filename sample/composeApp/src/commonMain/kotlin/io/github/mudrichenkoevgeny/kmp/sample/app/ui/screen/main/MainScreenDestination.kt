package io.github.mudrichenkoevgeny.kmp.sample.app.ui.screen.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import io.github.mudrichenkoevgeny.kmp.sample.app.Res
import io.github.mudrichenkoevgeny.kmp.sample.app.nav_home
import io.github.mudrichenkoevgeny.kmp.sample.app.nav_profile
import org.jetbrains.compose.resources.StringResource

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
        val allDestinations = listOf(Home, Profile)

        fun fromConfig(config: MainScreenComponent.Config): MainScreenDestination =
            when (config) {
                is MainScreenComponent.Config.Home -> Home
                is MainScreenComponent.Config.Profile -> Profile
            }
    }
}