package io.github.mudrichenkoevgeny.kmp.sample.app.ui.screen.main

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import com.arkivanov.decompose.Child
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.kmp.sample.app.ui.screen.home.HomeScreenComponent
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK])
class MainContentTest {

    @Test
    fun mobile_showsBottomNavLabelsAndHomeBody() = runComposeUiTest {
        setContent {
            MaterialTheme {
                MainContent(
                    isMobile = true,
                    screenStack = homeOnlyStack(),
                    currentDestination = MainScreenDestination.Home,
                    destinations = MainScreenDestination.allDestinations,
                    onDestinationChange = {}
                )
            }
        }
        onNodeWithText(NAV_HOME).assertIsDisplayed()
        onNodeWithText(NAV_PROFILE).assertIsDisplayed()
        onNodeWithText(HOME_BODY).assertIsDisplayed()
    }

    @Test
    fun web_showsHomeNavLabelAndHomeBody() = runComposeUiTest {
        setContent {
            MaterialTheme {
                MainContent(
                    isMobile = false,
                    screenStack = homeOnlyStack(),
                    currentDestination = MainScreenDestination.Home,
                    destinations = MainScreenDestination.allDestinations,
                    onDestinationChange = {}
                )
            }
        }
        onNodeWithText(NAV_HOME).assertIsDisplayed()
        onNodeWithText(HOME_BODY).assertIsDisplayed()
    }

    @Test
    fun mobile_clickProfileTab_notifiesDestination() = runComposeUiTest {
        val clicks = mutableListOf<MainScreenDestination>()
        setContent {
            MaterialTheme {
                MainContent(
                    isMobile = true,
                    screenStack = homeOnlyStack(),
                    currentDestination = MainScreenDestination.Home,
                    destinations = MainScreenDestination.allDestinations,
                    onDestinationChange = { clicks.add(it) }
                )
            }
        }
        onNodeWithText(NAV_PROFILE).performClick()
        assertEquals(MainScreenDestination.Profile, clicks.single())
    }

    private companion object {
        const val NAV_HOME = "Home"
        const val NAV_PROFILE = "Profile"
        const val HOME_BODY = "Home Screen"
    }
}

private fun homeOnlyStack(): Value<ChildStack<MainScreenComponent.Config, MainScreenComponent.Child>> =
    MutableValue(
        ChildStack(
            active = Child.Created(
                configuration = MainScreenComponent.Config.Home,
                instance = MainScreenComponent.Child.HomeChild(object : HomeScreenComponent {})
            ),
            backStack = emptyList()
        )
    )
