package io.github.mudrichenkoevgeny.kmp.sample.app.ui.screen.main

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class MainScreenDestinationTest {

    @Test
    fun `fromConfig maps home and profile`() {
        assertSame(
            MainScreenDestination.Home,
            MainScreenDestination.fromConfig(MainScreenComponent.Config.Home)
        )
        assertSame(
            MainScreenDestination.Profile,
            MainScreenDestination.fromConfig(MainScreenComponent.Config.Profile)
        )
    }

    @Test
    fun `allDestinations contains home and profile in order`() {
        assertEquals(
            listOf(MainScreenDestination.Home, MainScreenDestination.Profile),
            MainScreenDestination.allDestinations
        )
    }
}
