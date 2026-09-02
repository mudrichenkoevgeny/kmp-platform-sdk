package io.github.mudrichenkoevgeny.kmp.sampleclient.app.ui.screen.home

import android.app.Application
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.runComposeUiTest
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK], application = Application::class)
class HomeScreenTest {

    @Test
    fun displaysPlaceholderTitle() = runComposeUiTest {
        setContent {
            MaterialTheme {
                HomeScreen(screenComponent = object : HomeScreenComponent {})
            }
        }
        onNodeWithTag(HomeScreenTestTags.TITLE).assertIsDisplayed()
    }
}