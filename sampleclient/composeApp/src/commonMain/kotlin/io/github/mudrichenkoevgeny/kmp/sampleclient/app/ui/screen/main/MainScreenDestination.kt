package io.github.mudrichenkoevgeny.kmp.sampleclient.app.ui.screen.main

import androidx.compose.runtime.Composable
import io.github.mudrichenkoevgeny.kmp.core.common.ic_user
import io.github.mudrichenkoevgeny.kmp.feature.user.localization.LocalUserStrings
import org.jetbrains.compose.resources.DrawableResource
import io.github.mudrichenkoevgeny.kmp.core.common.Res as CommonRes

/**
 * UI-facing tab model: ties [MainScreenComponent.Config] to localized titles and toolbar icons.
 */
sealed interface MainScreenDestination {
    val config: MainScreenComponent.Config
    @Composable fun title(): String
    val iconRes: DrawableResource

    data object User : MainScreenDestination {
        override val config = MainScreenComponent.Config.User
        @Composable override fun title(): String = LocalUserStrings.current.ui.user
        override val iconRes = CommonRes.drawable.ic_user
    }

    companion object {
        /**
         * Tabs shown in mobile bottom navigation (order matches display).
         */
        val allDestinations = listOf(User)

        /**
         * @param config Stack configuration for the active child.
         * @return Matching [MainScreenDestination] for labels and selection state.
         */
        fun fromConfig(config: MainScreenComponent.Config): MainScreenDestination =
            when (config) {
                is MainScreenComponent.Config.User -> User
            }
    }
}
