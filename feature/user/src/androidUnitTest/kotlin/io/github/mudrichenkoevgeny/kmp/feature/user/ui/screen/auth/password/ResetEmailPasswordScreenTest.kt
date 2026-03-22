package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.password

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import io.github.mudrichenkoevgeny.kmp.core.common.di.LocalErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.mock.error.parser.MockAppErrorParser
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenLoadingConfig
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK])
class ResetEmailPasswordScreenTest {

    @Test
    fun loading_showsIndeterminateProgressAfterDefaultDelay() = runComposeUiTest {
        val component = FakeResetEmailPasswordComponent(ResetEmailPasswordScreenState.Loading)
        setContent { ResetEmailPasswordScreenHarness(component) }
        mainClock.autoAdvance = false
        mainClock.advanceTimeBy(FullscreenLoadingConfig.DELAY_MILLIS + LOADING_EXTRA_DELAY_MS)
        onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate)).assertExists()
    }

    @Test
    fun emailInput_displaysTitleEmailAndSendCode() = runComposeUiTest {
        val component = FakeResetEmailPasswordComponent(
            ResetEmailPasswordScreenState.EmailInput(email = EMAIL_SEND_STEP, isEmailValid = true)
        )
        setContent { ResetEmailPasswordScreenHarness(component) }
        onNodeWithText(RESET_PASSWORD_TITLE).assertIsDisplayed()
        onNodeWithText(EMAIL_LABEL).assertIsDisplayed()
        onNodeWithText(SEND_CODE).assertIsDisplayed()
    }

    @Test
    fun emailInput_inlineError_showsLocalizedMessage() = runComposeUiTest {
        val component = FakeResetEmailPasswordComponent(
            ResetEmailPasswordScreenState.EmailInput(
                email = EMAIL_SEND_STEP,
                isEmailValid = true,
                actionError = CommonError.Unknown()
            )
        )
        setContent { ResetEmailPasswordScreenHarness(component) }
        onNodeWithText(MOCK_ERROR_MESSAGE).assertIsDisplayed()
    }

    @Test
    fun emailInput_clickSendCode_invokesCallback() = runComposeUiTest {
        val component = FakeResetEmailPasswordComponent(
            ResetEmailPasswordScreenState.EmailInput(email = EMAIL_SEND_STEP, isEmailValid = true)
        )
        setContent { ResetEmailPasswordScreenHarness(component) }
        onNodeWithText(SEND_CODE).performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.sendCodeClicks)
    }

    @Test
    fun resetInput_displaysCodeSentLineAndFields() = runComposeUiTest {
        val email = EMAIL_CODE_SENT_LINE
        val component = FakeResetEmailPasswordComponent(
            ResetEmailPasswordScreenState.ResetInput(
                email = email,
                code = CODE_THREE_DIGITS,
                newPassword = NEW_PASSWORD_INVALID,
                isPasswordValid = false,
                resendTimerSeconds = RESEND_TIMER_RESET_INPUT_SECONDS
            )
        )
        setContent { ResetEmailPasswordScreenHarness(component) }
        onNodeWithText(ENTER_CODE_TITLE).assertIsDisplayed()
        onNodeWithText(CODE_SENT_PREFIX, substring = true).assertIsDisplayed()
        onNodeWithText(email, substring = true).assertIsDisplayed()
        onNodeWithText(CONFIRMATION_CODE_LABEL).assertIsDisplayed()
        onNodeWithText(NEW_PASSWORD_LABEL).assertIsDisplayed()
        onNodeWithText(CONFIRM_BUTTON).assertIsDisplayed()
        onNodeWithText(CHANGE_EMAIL).assertIsDisplayed()
    }

    @Test
    fun resetInput_whenTimerPositive_showsCountdown() = runComposeUiTest {
        val component = FakeResetEmailPasswordComponent(
            ResetEmailPasswordScreenState.ResetInput(
                email = EMAIL_RESET_FLOW,
                resendTimerSeconds = RESEND_TIMER_COUNTDOWN_SECONDS
            )
        )
        setContent { ResetEmailPasswordScreenHarness(component) }
        onNodeWithText(RESEND_TIMER_COUNTDOWN_TEXT, substring = true).assertIsDisplayed()
    }

    @Test
    fun resetInput_whenCanResend_showsResendButton() = runComposeUiTest {
        val component = FakeResetEmailPasswordComponent(
            ResetEmailPasswordScreenState.ResetInput(
                email = EMAIL_RESET_FLOW,
                resendTimerSeconds = RESEND_TIMER_EXPIRED_SECONDS
            )
        )
        setContent { ResetEmailPasswordScreenHarness(component) }
        onNodeWithText(RESEND_CODE).assertIsDisplayed()
    }

    @Test
    fun resetInput_clickChangeEmail_invokesCallback() = runComposeUiTest {
        val component = FakeResetEmailPasswordComponent(
            ResetEmailPasswordScreenState.ResetInput(
                email = EMAIL_RESET_FLOW,
                code = CODE_SIX_DIGITS,
                newPassword = NEW_PASSWORD_VALID,
                isPasswordValid = true,
                resendTimerSeconds = RESEND_TIMER_COUNTDOWN_SECONDS
            )
        )
        setContent { ResetEmailPasswordScreenHarness(component) }
        onNodeWithText(CHANGE_EMAIL).performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.resetEmailClicks)
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
        const val RESEND_TIMER_COUNTDOWN_TEXT = "7"
        const val RESEND_TIMER_EXPIRED_SECONDS = 0

        const val RESET_PASSWORD_TITLE = "Reset password"
        const val ENTER_CODE_TITLE = "Enter code"
        const val EMAIL_LABEL = "Email"
        const val CONFIRMATION_CODE_LABEL = "Confirmation code"
        const val NEW_PASSWORD_LABEL = "New password"
        const val SEND_CODE = "Send code"
        const val RESEND_CODE = "Resend code"
        const val CONFIRM_BUTTON = "Confirm"
        const val CHANGE_EMAIL = "Change email"
        const val CODE_SENT_PREFIX = "The code has been sent to"
        const val MOCK_ERROR_MESSAGE = "Unknown Error"
    }
}

@Composable
private fun ResetEmailPasswordScreenHarness(component: ResetEmailPasswordComponent) {
    MaterialTheme {
        CompositionLocalProvider(LocalErrorParser provides MockAppErrorParser) {
            ResetEmailPasswordScreen(component)
        }
    }
}

private class FakeResetEmailPasswordComponent(
    initial: ResetEmailPasswordScreenState
) : ResetEmailPasswordComponent {

    private val mutableState = MutableValue(initial)
    override val state: Value<ResetEmailPasswordScreenState> get() = mutableState

    var sendCodeClicks: Int = 0
        private set
    var resetEmailClicks: Int = 0
        private set

    override fun onEmailChanged(email: String) {}
    override fun onCodeChanged(code: String) {}
    override fun onPasswordChanged(password: String) {}
    override fun onSendCodeClick() {
        sendCodeClicks++
    }

    override fun onResetEmailClick() {
        resetEmailClicks++
    }

    override fun onConfirmResetClick() {}
    override fun onBackClick() {}
}
