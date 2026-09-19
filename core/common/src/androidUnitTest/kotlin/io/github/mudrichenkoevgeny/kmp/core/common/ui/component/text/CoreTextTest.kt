package io.github.mudrichenkoevgeny.kmp.core.common.ui.component.text

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.runComposeUiTest
import io.github.mudrichenkoevgeny.kmp.core.common.Res
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ComponentTestHarness
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.kmp.core.common.ui_common_message
import org.jetbrains.compose.resources.stringResource
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test

@InternalApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK])
class CoreTextTest {

    @Test
    fun displaysBodyTextCorrectly() = runComposeUiTest {
        setContent {
            ComponentTestHarness {
                CoreBodyText(
                    text = stringResource(Res.string.ui_common_message),
                    modifier = Modifier.testTag(TEST_TAG)
                )
            }
        }

        onNodeWithTag(TEST_TAG).assertIsDisplayed()
    }

    @Test
    fun displaysSmallTextCorrectly() = runComposeUiTest {
        setContent {
            ComponentTestHarness {
                CoreSmallText(
                    text = stringResource(Res.string.ui_common_message),
                    modifier = Modifier.testTag(TEST_TAG)
                )
            }
        }

        onNodeWithTag(TEST_TAG).assertIsDisplayed()
    }

    @Test
    fun displaysTitleTextCorrectly() = runComposeUiTest {
        setContent {
            ComponentTestHarness {
                CoreTitleText(
                    text = stringResource(Res.string.ui_common_message),
                    modifier = Modifier.testTag(TEST_TAG)
                )
            }
        }

        onNodeWithTag(TEST_TAG).assertIsDisplayed()
    }

    private companion object {
        const val TEST_TAG = "CoreText"
    }
}
