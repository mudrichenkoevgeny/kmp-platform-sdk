package io.github.mudrichenkoevgeny.kmp.sampleclient.app.ui.screen.main

import android.app.Application
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import com.arkivanov.decompose.Child
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.kmp.feature.user.localization.LocalUserStrings
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.user.UserRootComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.user.main.MainUserComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.root.UserRootComponent
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test

@InternalApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK], application = Application::class)
class MainContentTest {

    @Test
    fun mobile_showsBottomNavLabels() = runComposeUiTest {
        var userText = ""
        setContent {
            userText = LocalUserStrings.current.ui.user
            MaterialTheme {
                MainContent(
                    isMobile = true,
                    screenStack = userOnlyStack(),
                    currentDestination = MainScreenDestination.User,
                    destinations = MainScreenDestination.allDestinations,
                    onDestinationChange = {}
                )
            }
        }
        onNodeWithText(userText).assertIsDisplayed()
    }
}

@InternalApi
private fun userOnlyStack(): Value<ChildStack<MainScreenComponent.Config, MainScreenComponent.Child>> =
    MutableValue(
        ChildStack(
            active = Child.Created(
                configuration = MainScreenComponent.Config.User,
                instance = MainScreenComponent.Child.UserChild(
                    UserRootComponentMock(
                        child = UserRootComponent.Child.Main(MainUserComponentMock())
                    )
                )
            ),
            backStack = emptyList()
        )
    )
