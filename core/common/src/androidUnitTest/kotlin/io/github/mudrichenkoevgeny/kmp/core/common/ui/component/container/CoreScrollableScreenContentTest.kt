package io.github.mudrichenkoevgeny.kmp.core.common.ui.component.container

import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.runComposeUiTest
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.localization.LocalCommonStrings
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ComponentTestHarness
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test

@InternalApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK])
class CoreScrollableScreenContentTest {

    @Test
    fun displaysContentInsideContainer() = runComposeUiTest {
        setContent {
            ComponentTestHarness {
                CoreScrollableScreenContent(
                    modifier = Modifier.testTag(TEST_TAG)
                ) {
                    Text(LocalCommonStrings.current.ui.action)
                }
            }
        }

        onNodeWithTag(TEST_TAG).assertIsDisplayed()
    }

    private companion object {
        const val TEST_TAG = "CoreScrollableScreenContent"
    }
}
