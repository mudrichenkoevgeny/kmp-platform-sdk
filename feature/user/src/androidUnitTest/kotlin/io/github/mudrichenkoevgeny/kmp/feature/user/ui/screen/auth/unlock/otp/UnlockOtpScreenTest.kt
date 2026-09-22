package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.unlock.otp

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextReplacement
import androidx.compose.ui.test.runComposeUiTest
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ComponentTestHarness
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.auth.unlock.otp.UnlockOtpComponentMock
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
class UnlockOtpScreenTest {

    @Test
    fun rendersTargetAndBindsCodeInput() = runComposeUiTest {
        val initialState = UnlockOtpScreenState(
            method = UnlockMethod.EMAIL,
            target = "user@example.com",
            codeInput = ""
        )
        val component = UnlockOtpComponentMock(initialState = initialState)

        setContent {
            ComponentTestHarness {
                UnlockOtpScreen(component)
            }
        }

        onNodeWithText("user@example.com").assertIsDisplayed()
        onNodeWithTag(UnlockOtpTestTags.UNLOCK_BUTTON).assertIsNotEnabled()

        onNodeWithTag(UnlockOtpTestTags.CODE_INPUT).performTextReplacement("123456")
        assertEquals("123456", component.state.value.codeInput)
        onNodeWithTag(UnlockOtpTestTags.UNLOCK_BUTTON).assertIsEnabled()
    }

    @Test
    fun unlockButton_triggersUnlockClick() = runComposeUiTest {
        var unlockClicked = false
        val initialState = UnlockOtpScreenState(
            method = UnlockMethod.EMAIL,
            target = "user@example.com",
            codeInput = "123456"
        )
        val component = object : UnlockOtpComponentMock(initialState = initialState) {
            override fun onUnlockClick() {
                unlockClicked = true
            }
        }

        setContent {
            ComponentTestHarness {
                UnlockOtpScreen(component)
            }
        }

        onNodeWithTag(UnlockOtpTestTags.UNLOCK_BUTTON).performClick()
        assertTrue(unlockClicked)
    }

    @Test
    fun resendButton_showsTimerWhenPositive_showsButtonWhenZero() = runComposeUiTest {
        var resendClicked = false
        val initialState = UnlockOtpScreenState(
            method = UnlockMethod.EMAIL,
            target = "user@example.com",
            remainingDelaySeconds = 30
        )
        val component = object : UnlockOtpComponentMock(initialState = initialState) {
            override fun onResendCodeClick() {
                resendClicked = true
            }
        }

        setContent {
            ComponentTestHarness {
                UnlockOtpScreen(component)
            }
        }

        onNodeWithTag(UnlockOtpTestTags.RESEND_TIMER_TEXT).assertIsDisplayed()

        component.updateState(initialState.copy(remainingDelaySeconds = 0))

        onNodeWithTag(UnlockOtpTestTags.RESEND_BUTTON).assertIsEnabled()
        onNodeWithTag(UnlockOtpTestTags.RESEND_BUTTON).performClick()
        assertTrue(resendClicked)
    }
}
