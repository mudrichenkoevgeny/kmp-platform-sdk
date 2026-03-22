package io.github.mudrichenkoevgeny.kmp.sample.app.ui.screen.home

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK])
class HomeScreenTest {

    @Test
    fun displaysPlaceholderTitle() = runComposeUiTest {
        setContent {
            MaterialTheme {
                HomeScreen(screenComponent = object : HomeScreenComponent {})
            }
        }
        onNodeWithText(HOME_TITLE).assertIsDisplayed()
    }

    private companion object {
        const val HOME_TITLE = "Home Screen"
    }
}
