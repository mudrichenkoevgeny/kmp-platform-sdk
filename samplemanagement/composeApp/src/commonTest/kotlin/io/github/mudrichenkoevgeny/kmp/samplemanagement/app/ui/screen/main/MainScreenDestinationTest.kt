package io.github.mudrichenkoevgeny.kmp.samplemanagement.app.ui.screen.main

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class MainScreenDestinationTest {

    @Test
    fun `fromConfig maps home, profile, and settings`() {
        assertSame(
            MainScreenDestination.Home,
            MainScreenDestination.fromConfig(MainScreenComponent.Config.Home)
        )
        assertSame(
            MainScreenDestination.Profile,
            MainScreenDestination.fromConfig(MainScreenComponent.Config.Profile)
        )
        assertSame(
            MainScreenDestination.Settings,
            MainScreenDestination.fromConfig(MainScreenComponent.Config.Settings)
        )
    }

    @Test
    fun `allDestinations contains home, profile, and settings in order`() {
        assertEquals(
            listOf(MainScreenDestination.Home, MainScreenDestination.Profile, MainScreenDestination.Settings),
            MainScreenDestination.allDestinations
        )
    }
}
