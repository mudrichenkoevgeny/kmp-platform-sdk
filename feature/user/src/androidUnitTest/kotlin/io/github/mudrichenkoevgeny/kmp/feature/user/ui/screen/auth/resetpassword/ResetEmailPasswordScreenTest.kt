package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.resetpassword

import android.app.Application
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenLoadingConfig
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ComponentTestHarness
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.auth.resetpassword.ResetEmailPasswordComponentMock
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test
import kotlin.test.assertEquals

@InternalApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK], application = Application::class)
class ResetEmailPasswordScreenTest {

    @Test
    fun loading_showsIndeterminateProgressAfterDefaultDelay() = runComposeUiTest {
        val component = ResetEmailPasswordComponentMock(ResetEmailPasswordScreenState.Loading)
        setContent {
            ComponentTestHarness {
                ResetEmailPasswordScreen(component)
            }
        }
        mainClock.autoAdvance = false
        mainClock.advanceTimeBy(FullscreenLoadingConfig.DELAY_MILLIS + LOADING_EXTRA_DELAY_MS)
        onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate)).assertExists()
    }

    @Test
    fun emailInput_displaysTitleEmailAndSendCode() = runComposeUiTest {
        val component = ResetEmailPasswordComponentMock(
            ResetEmailPasswordScreenState.EmailInput(email = EMAIL_SEND_STEP, isEmailValid = true)
        )
        setContent {
            ComponentTestHarness {
                ResetEmailPasswordScreen(component)
            }
        }
        onNodeWithTag(ResetEmailPasswordTestTags.EMAIL_STEP_BACK_BUTTON).assertIsDisplayed()
        onNodeWithTag(ResetEmailPasswordTestTags.EMAIL_STEP_TITLE).assertIsDisplayed()
        onNodeWithTag(ResetEmailPasswordTestTags.EMAIL_INPUT).assertIsDisplayed().assertTextContains(EMAIL_SEND_STEP)
        onNodeWithTag(ResetEmailPasswordTestTags.SEND_CODE_BUTTON).assertIsDisplayed()
    }

    @Test
    fun emailInput_inlineError_showsErrorTextNode() = runComposeUiTest {
        val component = ResetEmailPasswordComponentMock(
            ResetEmailPasswordScreenState.EmailInput(
                email = EMAIL_SEND_STEP,
                isEmailValid = true,
                actionError = CommonError.Unknown()
            )
        )
        setContent {
            ComponentTestHarness {
                ResetEmailPasswordScreen(component)
            }
        }
        onNodeWithTag(ResetEmailPasswordTestTags.EMAIL_STEP_ERROR_TEXT).assertIsDisplayed()
    }

    @Test
    fun emailInput_clickSendCode_invokesCallback() = runComposeUiTest {
        val component = ResetEmailPasswordComponentMock(
            ResetEmailPasswordScreenState.EmailInput(email = EMAIL_SEND_STEP, isEmailValid = true)
        )
        setContent {
            ComponentTestHarness {
                ResetEmailPasswordScreen(component)
            }
        }
        onNodeWithTag(ResetEmailPasswordTestTags.SEND_CODE_BUTTON).performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.sendCodeCalls)
    }

    @Test
    fun resetInput_displaysCodeSentLineAndFields() = runComposeUiTest {
        val email = EMAIL_CODE_SENT_LINE
        val component = ResetEmailPasswordComponentMock(
            ResetEmailPasswordScreenState.ResetInput(
                email = email,
                code = CODE_THREE_DIGITS,
                newPassword = NEW_PASSWORD_INVALID,
                isPasswordValid = false,
                resendTimerSeconds = RESEND_TIMER_RESET_INPUT_SECONDS
            )
        )
        setContent {
            ComponentTestHarness {
                ResetEmailPasswordScreen(component)
            }
        }
        onNodeWithTag(ResetEmailPasswordTestTags.RESET_STEP_BACK_BUTTON).assertIsDisplayed()
        onNodeWithTag(ResetEmailPasswordTestTags.RESET_STEP_TITLE).assertIsDisplayed()
        onNodeWithTag(ResetEmailPasswordTestTags.CODE_SENT_INFO_TEXT).assertIsDisplayed().assertTextContains(email, substring = true)
        onNodeWithTag(ResetEmailPasswordTestTags.CODE_INPUT).assertIsDisplayed().assertTextContains(CODE_THREE_DIGITS)
        onNodeWithTag(ResetEmailPasswordTestTags.NEW_PASSWORD_INPUT).assertIsDisplayed().assertTextContains(NEW_PASSWORD_INVALID)
        onNodeWithTag(ResetEmailPasswordTestTags.CONFIRM_BUTTON).assertIsDisplayed()
        onNodeWithTag(ResetEmailPasswordTestTags.CHANGE_EMAIL_BUTTON).assertIsDisplayed()
    }

    @Test
    fun resetInput_whenTimerPositive_showsCountdown() = runComposeUiTest {
        val component = ResetEmailPasswordComponentMock(
            ResetEmailPasswordScreenState.ResetInput(
                email = EMAIL_RESET_FLOW,
                resendTimerSeconds = RESEND_TIMER_COUNTDOWN_SECONDS
            )
        )
        setContent {
            ComponentTestHarness {
                ResetEmailPasswordScreen(component)
            }
        }
        onNodeWithTag(ResetEmailPasswordTestTags.RESEND_TIMER_TEXT).assertIsDisplayed()
    }

    @Test
    fun resetInput_whenCanResend_showsResendButton() = runComposeUiTest {
        val component = ResetEmailPasswordComponentMock(
            ResetEmailPasswordScreenState.ResetInput(
                email = EMAIL_RESET_FLOW,
                resendTimerSeconds = RESEND_TIMER_EXPIRED_SECONDS
            )
        )
        setContent {
            ComponentTestHarness {
                ResetEmailPasswordScreen(component)
            }
        }
        onNodeWithTag(ResetEmailPasswordTestTags.RESEND_CODE_BUTTON).assertIsDisplayed()
    }

    @Test
    fun resetInput_clickChangeEmail_invokesCallback() = runComposeUiTest {
        val component = ResetEmailPasswordComponentMock(
            ResetEmailPasswordScreenState.ResetInput(
                email = EMAIL_RESET_FLOW,
                code = CODE_SIX_DIGITS,
                newPassword = NEW_PASSWORD_VALID,
                isPasswordValid = true,
                resendTimerSeconds = RESEND_TIMER_COUNTDOWN_SECONDS
            )
        )
        setContent {
            ComponentTestHarness {
                ResetEmailPasswordScreen(component)
            }
        }
        onNodeWithTag(ResetEmailPasswordTestTags.CHANGE_EMAIL_BUTTON).performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.resetEmailCalls)
    }

    private companion object {
        const val LOADING_EXTRA_DELAY_MS = 50L
        const val EXPECTED_SINGLE_CALLBACK = 1

        const val EMAIL_SEND_STEP = "u@example.com"
        const val EMAIL_CODE_SENT_LINE = "user@test.com"
        const val EMAIL_RESET_FLOW = "a@b.com"

        const val CODE_THREE_DIGITS = "123"
        const val CODE_SIX_DIGITS = "123456"
        const val NEW_PASSWORD_INVALID = "x"
        const val NEW_PASSWORD_VALID = "longenough"

        const val RESEND_TIMER_RESET_INPUT_SECONDS = 20
        const val RESEND_TIMER_COUNTDOWN_SECONDS = 7
        const val RESEND_TIMER_EXPIRED_SECONDS = 0
    }
}