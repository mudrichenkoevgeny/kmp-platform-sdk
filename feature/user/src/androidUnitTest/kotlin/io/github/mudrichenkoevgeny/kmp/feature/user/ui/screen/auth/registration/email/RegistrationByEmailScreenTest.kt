package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.registration.email

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
class RegistrationByEmailScreenTest {

    @Test
    fun loading_showsIndeterminateProgressAfterDefaultDelay() = runComposeUiTest {
        val component = FakeRegistrationByEmailComponent(RegistrationByEmailScreenState.Loading)
        setContent { RegistrationByEmailScreenHarness(component) }
        mainClock.autoAdvance = false
        mainClock.advanceTimeBy(FullscreenLoadingConfig.DELAY_MILLIS + LOADING_EXTRA_DELAY_MS)
        onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate)).assertExists()
    }

    @Test
    fun emailInput_displaysTitleEmailAndSendCode() = runComposeUiTest {
        val component = FakeRegistrationByEmailComponent(
            RegistrationByEmailScreenState.EmailInput(email = EMAIL_SEND_STEP, isEmailValid = true)
        )
        setContent { RegistrationByEmailScreenHarness(component) }
        onNodeWithText(REGISTRATION_TITLE).assertIsDisplayed()
        onNodeWithText(EMAIL_LABEL).assertIsDisplayed()
        onNodeWithText(SEND_CODE).assertIsDisplayed()
    }

    @Test
    fun emailInput_inlineError_showsLocalizedMessage() = runComposeUiTest {
        val component = FakeRegistrationByEmailComponent(
            RegistrationByEmailScreenState.EmailInput(
                email = EMAIL_SEND_STEP,
                isEmailValid = true,
                actionError = CommonError.Unknown()
            )
        )
        setContent { RegistrationByEmailScreenHarness(component) }
        onNodeWithText(MOCK_ERROR_MESSAGE).assertIsDisplayed()
    }

    @Test
    fun emailInput_clickSendCode_invokesCallback() = runComposeUiTest {
        val component = FakeRegistrationByEmailComponent(
            RegistrationByEmailScreenState.EmailInput(email = EMAIL_SEND_STEP, isEmailValid = true)
        )
        setContent { RegistrationByEmailScreenHarness(component) }
        onNodeWithText(SEND_CODE).performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.sendCodeClicks)
    }

    @Test
    fun registrationInput_displaysCodeSentLinePasswordRegister() = runComposeUiTest {
        val email = EMAIL_CODE_SENT_LINE
        val component = FakeRegistrationByEmailComponent(
            RegistrationByEmailScreenState.RegistrationInput(
                email = email,
                code = CODE_THREE_DIGITS,
                password = REGISTRATION_PASSWORD_INVALID,
                isPasswordValid = false,
                resendTimerSeconds = RESEND_TIMER_REGISTRATION_INPUT_SECONDS
            )
        )
        setContent { RegistrationByEmailScreenHarness(component) }
        onNodeWithText(ENTER_CODE_TITLE).assertIsDisplayed()
        onNodeWithText(CODE_SENT_PREFIX, substring = true).assertIsDisplayed()
        onNodeWithText(email, substring = true).assertIsDisplayed()
        onNodeWithText(CONFIRMATION_CODE_LABEL).assertIsDisplayed()
        onNodeWithText(PASSWORD_LABEL).assertIsDisplayed()
        onNodeWithText(REGISTER_BUTTON).assertIsDisplayed()
        onNodeWithText(CHANGE_EMAIL).assertIsDisplayed()
    }

    @Test
    fun registrationInput_whenTimerPositive_showsCountdown() = runComposeUiTest {
        val component = FakeRegistrationByEmailComponent(
            RegistrationByEmailScreenState.RegistrationInput(
                email = EMAIL_REGISTRATION_FLOW,
                resendTimerSeconds = RESEND_TIMER_COUNTDOWN_SECONDS
            )
        )
        setContent { RegistrationByEmailScreenHarness(component) }
        onNodeWithText(RESEND_TIMER_COUNTDOWN_TEXT, substring = true).assertIsDisplayed()
    }

    @Test
    fun registrationInput_whenCanResend_showsResendButton() = runComposeUiTest {
        val component = FakeRegistrationByEmailComponent(
            RegistrationByEmailScreenState.RegistrationInput(
                email = EMAIL_REGISTRATION_FLOW,
                resendTimerSeconds = RESEND_TIMER_EXPIRED_SECONDS
            )
        )
        setContent { RegistrationByEmailScreenHarness(component) }
        onNodeWithText(RESEND_CODE).assertIsDisplayed()
    }

    @Test
    fun registrationInput_clickRegister_invokesCallback() = runComposeUiTest {
        val component = FakeRegistrationByEmailComponent(
            RegistrationByEmailScreenState.RegistrationInput(
                email = EMAIL_REGISTRATION_FLOW,
                code = CODE_SIX_DIGITS,
                password = REGISTRATION_PASSWORD_VALID,
                isPasswordValid = true
            )
        )
        setContent { RegistrationByEmailScreenHarness(component) }
        onNodeWithText(REGISTER_BUTTON).performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.registerClicks)
    }

    private companion object {
        const val LOADING_EXTRA_DELAY_MS = 50L
        const val EXPECTED_SINGLE_CALLBACK = 1

        const val EMAIL_SEND_STEP = "n@example.com"
        const val EMAIL_CODE_SENT_LINE = "new@user.com"
        const val EMAIL_REGISTRATION_FLOW = "a@b.com"

        const val CODE_THREE_DIGITS = "123"
        const val CODE_SIX_DIGITS = "123456"
        const val REGISTRATION_PASSWORD_INVALID = "x"
        const val REGISTRATION_PASSWORD_VALID = "validpass1"

        const val RESEND_TIMER_REGISTRATION_INPUT_SECONDS = 15
        const val RESEND_TIMER_COUNTDOWN_SECONDS = 44
        const val RESEND_TIMER_COUNTDOWN_TEXT = "44"
        const val RESEND_TIMER_EXPIRED_SECONDS = 0

        const val REGISTRATION_TITLE = "Registration by Email"
        const val ENTER_CODE_TITLE = "Enter code"
        const val EMAIL_LABEL = "Email"
        const val CONFIRMATION_CODE_LABEL = "Confirmation code"
        const val PASSWORD_LABEL = "Password"
        const val SEND_CODE = "Send code"
        const val RESEND_CODE = "Resend code"
        const val REGISTER_BUTTON = "Register"
        const val CHANGE_EMAIL = "Change email"
        const val CODE_SENT_PREFIX = "The code has been sent to"
        const val MOCK_ERROR_MESSAGE = "Unknown Error"
    }
}

@Composable
private fun RegistrationByEmailScreenHarness(component: RegistrationByEmailComponent) {
    MaterialTheme {
        CompositionLocalProvider(LocalErrorParser provides MockAppErrorParser) {
            RegistrationByEmailScreen(component)
        }
    }
}

private class FakeRegistrationByEmailComponent(
    initial: RegistrationByEmailScreenState
) : RegistrationByEmailComponent {

    private val mutableState = MutableValue(initial)
    override val state: Value<RegistrationByEmailScreenState> get() = mutableState

    var sendCodeClicks: Int = 0
        private set
    var registerClicks: Int = 0
        private set

    override fun onEmailChanged(email: String) {}
    override fun onSendCodeClick() {
        sendCodeClicks++
    }

    override fun onCodeChanged(code: String) {}
    override fun onPasswordChanged(password: String) {}
    override fun onTogglePasswordVisibility() {}
    override fun onRegisterClick() {
        registerClicks++
    }

    override fun onBackClick() {}
}
