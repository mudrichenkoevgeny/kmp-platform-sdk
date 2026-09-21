package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.target

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextReplacement
import androidx.compose.ui.test.runComposeUiTest
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ComponentTestHarness
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.auth.unlock.target.UnlockTargetInputComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.user.model.auth.UnlockMethod
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@InternalApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK])
class UnlockTargetInputScreenTest {

    @Test
    fun emailInput_bindsInputAndTriggersCallback() = runComposeUiTest {
        var sendCodeClicked = false
        val initialState = UnlockTargetInputScreenState(
            method = UnlockMethod.EMAIL,
            input = ""
        )
        val component = object : UnlockTargetInputComponentMock(initialState = initialState) {
            override fun onSendCodeClick() {
                sendCodeClicked = true
            }
        }

        setContent {
            ComponentTestHarness {
                UnlockTargetInputScreen(component)
            }
        }

        onNodeWithTag(UnlockTargetInputTestTags.TITLE).assertIsDisplayed()
        onNodeWithTag(UnlockTargetInputTestTags.DESC_TEXT).assertIsDisplayed()
        onNodeWithTag(UnlockTargetInputTestTags.INPUT).assertIsDisplayed()
        onNodeWithTag(UnlockTargetInputTestTags.SUBMIT_BUTTON).assertIsNotEnabled()

        onNodeWithTag(UnlockTargetInputTestTags.INPUT).performTextReplacement("user@example.com")
        assertEquals("user@example.com", component.state.value.input)
        onNodeWithTag(UnlockTargetInputTestTags.SUBMIT_BUTTON).assertIsEnabled()

        onNodeWithTag(UnlockTargetInputTestTags.SUBMIT_BUTTON).performClick()
        assertTrue(sendCodeClicked)
    }

    @Test
    fun phoneInput_bindsInputAndTriggersCallback() = runComposeUiTest {
        var sendCodeClicked = false
        val initialState = UnlockTargetInputScreenState(
            method = UnlockMethod.PHONE,
            input = ""
        )
        val component = object : UnlockTargetInputComponentMock(initialState = initialState) {
            override fun onSendCodeClick() {
                sendCodeClicked = true
            }
        }

        setContent {
            ComponentTestHarness {
                UnlockTargetInputScreen(component)
            }
        }

        onNodeWithTag(UnlockTargetInputTestTags.TITLE).assertIsDisplayed()
        onNodeWithTag(UnlockTargetInputTestTags.DESC_TEXT).assertIsDisplayed()
        onNodeWithTag(UnlockTargetInputTestTags.INPUT).assertIsDisplayed()
        onNodeWithTag(UnlockTargetInputTestTags.SUBMIT_BUTTON).assertIsNotEnabled()

        onNodeWithTag(UnlockTargetInputTestTags.INPUT).performTextReplacement("79991234567")
        assertEquals("79991234567", component.state.value.input)
        onNodeWithTag(UnlockTargetInputTestTags.SUBMIT_BUTTON).assertIsEnabled()

        onNodeWithTag(UnlockTargetInputTestTags.SUBMIT_BUTTON).performClick()
        assertTrue(sendCodeClicked)
    }
}
