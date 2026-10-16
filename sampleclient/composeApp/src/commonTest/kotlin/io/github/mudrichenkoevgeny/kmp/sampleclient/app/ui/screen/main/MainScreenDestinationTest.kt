package io.github.mudrichenkoevgeny.kmp.sampleclient.app.ui.screen.main

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class MainScreenDestinationTest {

    @Test
    fun `fromConfig maps user`() {
        assertSame(
            MainScreenDestination.User,
            MainScreenDestination.fromConfig(MainScreenComponent.Config.User)
        )
    }

    @Test
    fun `allDestinations contains user in order`() {
        assertEquals(
            listOf(MainScreenDestination.User),
            MainScreenDestination.allDestinations
        )
    }
}
