package io.github.mudrichenkoevgeny.kmp.samplemanagement.app.ui.screen.main

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class MainScreenDestinationTest {

    @Test
    fun `fromConfig maps user and management`() {
        assertSame(
            MainScreenDestination.User,
            MainScreenDestination.fromConfig(MainScreenComponent.Config.User)
        )
        assertSame(
            MainScreenDestination.Management,
            MainScreenDestination.fromConfig(MainScreenComponent.Config.Management)
        )
    }

    @Test
    fun `allDestinations contains management and user in order`() {
        assertEquals(
            listOf(MainScreenDestination.Management, MainScreenDestination.User),
            MainScreenDestination.allDestinations
        )
    }
}
