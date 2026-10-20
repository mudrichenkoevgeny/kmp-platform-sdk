package io.github.mudrichenkoevgeny.kmp.core.common.ui.component.input

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.runComposeUiTest
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ComponentTestHarness
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test
import kotlin.test.assertEquals

@InternalApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK])
class CoreOutlinedTextFieldTest {

    @Test
    fun displaysFieldAndHandlesInput() = runComposeUiTest {
        var text = ""

        setContent {
            ComponentTestHarness {
                CoreOutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier.testTag(TEST_TAG)
                )
            }
        }

        onNodeWithTag(TEST_TAG).assertIsDisplayed().performTextInput("Hello")
        assertEquals("Hello", text)
    }

    private companion object {
        const val TEST_TAG = "CoreOutlinedTextField"
    }
}
