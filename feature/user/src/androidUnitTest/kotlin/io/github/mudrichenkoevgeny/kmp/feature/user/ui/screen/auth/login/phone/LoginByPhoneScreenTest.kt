package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.phone

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
class LoginByPhoneScreenTest {

    @Test
    fun loading_showsIndeterminateProgressAfterDefaultDelay() = runComposeUiTest {
        val component = FakeLoginByPhoneComponent(LoginByPhoneScreenState.Loading)
        setContent { LoginByPhoneScreenHarness(component) }
        mainClock.autoAdvance = false
        mainClock.advanceTimeBy(FullscreenLoadingConfig.DELAY_MILLIS + LOADING_EXTRA_DELAY_MS)
        onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate)).assertExists()
    }

    @Test
    fun phoneInput_displaysTitlePhoneLabelAndSendCode() = runComposeUiTest {
        val component = FakeLoginByPhoneComponent(
            LoginByPhoneScreenState.PhoneInput(phoneNumber = PHONE_NUMBER_FULL, isPhoneNumberValid = true)
        )
        setContent { LoginByPhoneScreenHarness(component) }
        onNodeWithText(ENTER_PHONE_TITLE).assertIsDisplayed()
        onNodeWithText(PHONE_NUMBER_LABEL).assertIsDisplayed()
        onNodeWithText(SEND_CODE).assertIsDisplayed()
    }

    @Test
    fun phoneInput_inlineError_showsLocalizedMessage() = runComposeUiTest {
        val component = FakeLoginByPhoneComponent(
            LoginByPhoneScreenState.PhoneInput(
                phoneNumber = PHONE_NUMBER_FULL,
                isPhoneNumberValid = true,
                actionError = CommonError.Unknown()
            )
        )
        setContent { LoginByPhoneScreenHarness(component) }
        onNodeWithText(MOCK_ERROR_MESSAGE).assertIsDisplayed()
    }

    @Test
    fun phoneInput_clickSendCode_invokesCallback() = runComposeUiTest {
        val component = FakeLoginByPhoneComponent(
            LoginByPhoneScreenState.PhoneInput(phoneNumber = PHONE_NUMBER_FULL, isPhoneNumberValid = true)
        )
        setContent { LoginByPhoneScreenHarness(component) }
        onNodeWithText(SEND_CODE).performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.sendCodeClicks)
    }

    @Test
    fun codeInput_displaysTitlesAndCodeSentLine() = runComposeUiTest {
        val phone = PHONE_NUMBER_E164
        val component = FakeLoginByPhoneComponent(
            LoginByPhoneScreenState.CodeInput(phoneNumber = phone, code = CODE_TWO_DIGITS)
        )
        setContent { LoginByPhoneScreenHarness(component) }
        onNodeWithText(ENTER_CODE_TITLE).assertIsDisplayed()
        onNodeWithText(CODE_SENT_PREFIX, substring = true).assertIsDisplayed()
        onNodeWithText(phone, substring = true).assertIsDisplayed()
        onNodeWithText(CONFIRMATION_CODE_LABEL).assertIsDisplayed()
    }

    @Test
    fun codeInput_whenTimerPositive_showsResendCountdown() = runComposeUiTest {
        val component = FakeLoginByPhoneComponent(
            LoginByPhoneScreenState.CodeInput(
                phoneNumber = PHONE_NUMBER_SHORT,
                code = "",
                resendTimerSeconds = RESEND_TIMER_SECONDS_UI
            )
        )
        setContent { LoginByPhoneScreenHarness(component) }
        onNodeWithText(RESEND_TIMER_SECONDS_TEXT, substring = true).assertIsDisplayed()
    }

    @Test
    fun codeInput_whenCanResend_showsResendButton() = runComposeUiTest {
        val component = FakeLoginByPhoneComponent(
            LoginByPhoneScreenState.CodeInput(
                phoneNumber = PHONE_NUMBER_SHORT,
                code = "",
                resendTimerSeconds = RESEND_TIMER_EXPIRED_SECONDS
            )
        )
        setContent { LoginByPhoneScreenHarness(component) }
        onNodeWithText(RESEND_CODE).assertIsDisplayed()
    }

    @Test
    fun codeInput_clickChangePhone_invokesCallback() = runComposeUiTest {
        val component = FakeLoginByPhoneComponent(
            LoginByPhoneScreenState.CodeInput(
                phoneNumber = PHONE_NUMBER_SHORT,
                code = CODE_SIX_DIGITS,
                resendTimerSeconds = RESEND_TIMER_EXPIRED_SECONDS
            )
        )
        setContent { LoginByPhoneScreenHarness(component) }
        onNodeWithText(CHANGE_PHONE).performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.resetPhoneClicks)
    }

    private companion object {
        const val LOADING_EXTRA_DELAY_MS = 50L
        const val EXPECTED_SINGLE_CALLBACK = 1

        const val PHONE_NUMBER_FULL = "0123456789"
        const val PHONE_NUMBER_SHORT = "012"
        const val PHONE_NUMBER_E164 = "+10005550001"
        const val CODE_TWO_DIGITS = "12"
        const val CODE_SIX_DIGITS = "123456"
        const val RESEND_TIMER_SECONDS_UI = 33
        const val RESEND_TIMER_SECONDS_TEXT = "33"
        const val RESEND_TIMER_EXPIRED_SECONDS = 0

        const val ENTER_PHONE_TITLE = "Enter phone number"
        const val ENTER_CODE_TITLE = "Enter code"
        const val PHONE_NUMBER_LABEL = "Phone number"
        const val CONFIRMATION_CODE_LABEL = "Confirmation code"
        const val SEND_CODE = "Send code"
        const val RESEND_CODE = "Resend code"
        const val CHANGE_PHONE = "Change phone number"
        const val CODE_SENT_PREFIX = "The code has been sent to"
        const val MOCK_ERROR_MESSAGE = "Unknown Error"
    }
}

@Composable
private fun LoginByPhoneScreenHarness(component: LoginByPhoneComponent) {
    MaterialTheme {
        CompositionLocalProvider(LocalErrorParser provides MockAppErrorParser) {
            LoginByPhoneScreen(component)
        }
    }
}

private class FakeLoginByPhoneComponent(
    initial: LoginByPhoneScreenState
) : LoginByPhoneComponent {

    private val mutableState = MutableValue(initial)
    override val state: Value<LoginByPhoneScreenState> get() = mutableState

    var sendCodeClicks: Int = 0
        private set
    var resetPhoneClicks: Int = 0
        private set

    override fun onPhoneChanged(phone: String) {}
    override fun onCodeChanged(code: String) {}
    override fun onSendCodeClick() {
        sendCodeClicks++
    }

    override fun onResetPhoneClick() {
        resetPhoneClicks++
    }

    override fun onConfirmCodeClick() {}
    override fun onBackClick() {}
}
