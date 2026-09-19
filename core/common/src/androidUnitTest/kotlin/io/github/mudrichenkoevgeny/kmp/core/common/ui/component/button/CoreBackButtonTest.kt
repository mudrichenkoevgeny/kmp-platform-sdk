package io.github.mudrichenkoevgeny.kmp.core.common.ui.component.button

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ComponentTestHarness
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test
import kotlin.test.assertTrue

@InternalApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK])
class CoreBackButtonTest {

    @Test
    fun triggersOnClickWhenClicked() = runComposeUiTest {
        var clicked = false

        setContent {
            ComponentTestHarness {
                CoreBackButton(
                    onClick = { clicked = true },
                    modifier = Modifier.testTag(TEST_TAG)
                )
            }
        }

        onNodeWithTag(TEST_TAG).assertIsDisplayed().assertHasClickAction().performClick()
        assertTrue(clicked)
    }

    @Test
    fun disabledState_doesNotTriggerOnClick() = runComposeUiTest {
        var clicked = false

        setContent {
            ComponentTestHarness {
                CoreBackButton(
                    onClick = { clicked = true },
                    enabled = false,
                    modifier = Modifier.testTag(TEST_TAG)
                )
            }
        }

        onNodeWithTag(TEST_TAG).assertIsNotEnabled().performClick()
        assertTrue(!clicked)
    }

    private companion object {
        const val TEST_TAG = "CoreBackButton"
    }
}
