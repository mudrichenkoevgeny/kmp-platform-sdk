package io.github.mudrichenkoevgeny.kmp.core.common.ui.component.input

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
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
import kotlin.test.assertTrue

@InternalApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK])
class CorePasswordTextFieldTest {

    @Test
    fun displaysPasswordFieldAndTogglesVisibility() = runComposeUiTest {
        var password = ""
        var isVisible = false
        var toggled = false

        setContent {
            ComponentTestHarness {
                CorePasswordTextField(
                    value = password,
                    onValueChange = { password = it },
                    isPasswordVisible = isVisible,
                    onTogglePasswordVisibility = {
                        isVisible = !isVisible
                        toggled = true
                    },
                    modifier = Modifier.testTag(TEST_TAG),
                    toggleModifier = Modifier.testTag(TOGGLE_TAG)
                )
            }
        }

        onNodeWithTag(TEST_TAG).assertIsDisplayed().performTextInput("secret123")
        assertEquals("secret123", password)

        onNodeWithTag(TOGGLE_TAG).assertIsDisplayed().performClick()
        assertTrue(toggled)
    }

    private companion object {
        const val TEST_TAG = "CorePasswordTextField"
        const val TOGGLE_TAG = "CorePasswordTextField_Toggle"
    }
}
