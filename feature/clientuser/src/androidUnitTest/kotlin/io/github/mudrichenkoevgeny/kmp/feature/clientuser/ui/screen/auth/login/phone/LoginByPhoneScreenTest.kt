package io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.login.phone

import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenLoadingConfig
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ComponentTestHarness
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.mock.ui.screen.auth.login.phone.LoginByPhoneComponentMock
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test
import kotlin.test.assertEquals

@InternalApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK])
class LoginByPhoneScreenTest {

    @Test
    fun loading_showsIndeterminateProgressAfterDefaultDelay() = runComposeUiTest {
        val component = LoginByPhoneComponentMock(
            LoginByPhoneScreenState.PhoneInput(actionLoading = true)
        )
        setContent {
            ComponentTestHarness {
                LoginByPhoneScreen(component)
            }
        }
        mainClock.autoAdvance = false
        mainClock.advanceTimeBy(FullscreenLoadingConfig.DELAY_MILLIS + LOADING_EXTRA_DELAY_MS)
        onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate)).assertExists()
    }

    @Test
    fun phoneInput_displaysTitlePhoneLabelAndSendCode() = runComposeUiTest {
        val component = LoginByPhoneComponentMock(
            LoginByPhoneScreenState.PhoneInput(phoneNumber = PHONE_NUMBER_FULL, isPhoneNumberValid = true)
        )
        setContent {
            ComponentTestHarness {
                LoginByPhoneScreen(component)
            }
        }
        onNodeWithText(ENTER_PHONE_TITLE).assertIsDisplayed()
        onNodeWithText(PHONE_NUMBER_LABEL).assertIsDisplayed()
        onNodeWithText(SEND_CODE).assertIsDisplayed()
    }

    @Test
    fun phoneInput_inlineError_showsLocalizedMessage() = runComposeUiTest {
        val component = LoginByPhoneComponentMock(
            LoginByPhoneScreenState.PhoneInput(
                phoneNumber = PHONE_NUMBER_FULL,
                isPhoneNumberValid = true,
                actionError = CommonError.Unknown()
            )
        )
        setContent {
            ComponentTestHarness {
                LoginByPhoneScreen(component)
            }
        }
        onNodeWithText(MOCK_ERROR_MESSAGE).assertIsDisplayed()
    }

    @Test
    fun phoneInput_clickSendCode_invokesCallback() = runComposeUiTest {
        val component = LoginByPhoneComponentMock(
            LoginByPhoneScreenState.PhoneInput(phoneNumber = PHONE_NUMBER_FULL, isPhoneNumberValid = true)
        )
        setContent {
            ComponentTestHarness {
                LoginByPhoneScreen(component)
            }
        }
        onNodeWithText(SEND_CODE).performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.sendCodeCalls)
    }

    @Test
    fun codeInput_displaysTitlesAndCodeSentLine() = runComposeUiTest {
        val phone = PHONE_NUMBER_E164
        val component = LoginByPhoneComponentMock(
            LoginByPhoneScreenState.CodeInput(phoneNumber = phone, code = CODE_TWO_DIGITS)
        )
        setContent {
            ComponentTestHarness {
                LoginByPhoneScreen(component)
            }
        }
        onNodeWithText(ENTER_CODE_TITLE).assertIsDisplayed()
        onNodeWithText(CODE_SENT_PREFIX, substring = true).assertIsDisplayed()
        onNodeWithText(phone, substring = true).assertIsDisplayed()
        onNodeWithText(CONFIRMATION_CODE_LABEL).assertIsDisplayed()
    }

    @Test
    fun codeInput_whenTimerPositive_showsResendCountdown() = runComposeUiTest {
        val component = LoginByPhoneComponentMock(
            LoginByPhoneScreenState.CodeInput(
                phoneNumber = PHONE_NUMBER_SHORT,
                code = "",
                resendTimerSeconds = RESEND_TIMER_SECONDS_UI
            )
        )
        setContent {
            ComponentTestHarness {
                LoginByPhoneScreen(component)
            }
        }
        onNodeWithText(RESEND_TIMER_SECONDS_TEXT, substring = true).assertIsDisplayed()
    }

    @Test
    fun codeInput_whenCanResend_showsResendButton() = runComposeUiTest {
        val component = LoginByPhoneComponentMock(
            LoginByPhoneScreenState.CodeInput(
                phoneNumber = PHONE_NUMBER_SHORT,
                code = "",
                resendTimerSeconds = RESEND_TIMER_EXPIRED_SECONDS
            )
        )
        setContent {
            ComponentTestHarness {
                LoginByPhoneScreen(component)
            }
        }
        onNodeWithText(RESEND_CODE).assertIsDisplayed()
    }

    @Test
    fun codeInput_clickChangePhone_invokesCallback() = runComposeUiTest {
        val component = LoginByPhoneComponentMock(
            LoginByPhoneScreenState.CodeInput(
                phoneNumber = PHONE_NUMBER_SHORT,
                code = CODE_SIX_DIGITS,
                resendTimerSeconds = RESEND_TIMER_EXPIRED_SECONDS
            )
        )
        setContent {
            ComponentTestHarness {
                LoginByPhoneScreen(component)
            }
        }
        onNodeWithText(CHANGE_PHONE).performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.resetPhoneCalls)
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
