package io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.login.phone

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
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.mock.ui.screen.auth.login.phone.LoginByPhoneComponentMock
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test
import kotlin.test.assertEquals

@InternalApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK], application = Application::class)
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
        onNodeWithTag(LoginByPhoneTestTags.BACK_BUTTON).assertIsDisplayed()
        onNodeWithTag(LoginByPhoneTestTags.PHONE_STEP_TITLE).assertIsDisplayed()
        onNodeWithTag(LoginByPhoneTestTags.PHONE_INPUT).assertIsDisplayed().assertTextContains(PHONE_NUMBER_FULL)
        onNodeWithTag(LoginByPhoneTestTags.SEND_CODE_BUTTON).assertIsDisplayed()
    }

    @Test
    fun phoneInput_inlineError_showsErrorTextNode() = runComposeUiTest {
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
        onNodeWithTag(LoginByPhoneTestTags.PHONE_STEP_ERROR_TEXT).assertIsDisplayed()
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
        onNodeWithTag(LoginByPhoneTestTags.SEND_CODE_BUTTON).performClick()
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
        onNodeWithTag(LoginByPhoneTestTags.BACK_BUTTON).assertIsDisplayed()
        onNodeWithTag(LoginByPhoneTestTags.CODE_STEP_TITLE).assertIsDisplayed()
        onNodeWithTag(LoginByPhoneTestTags.CODE_SENT_INFO_TEXT).assertIsDisplayed().assertTextContains(phone)
        onNodeWithTag(LoginByPhoneTestTags.CODE_INPUT).assertIsDisplayed().assertTextContains(CODE_TWO_DIGITS)
        onNodeWithTag(LoginByPhoneTestTags.CONFIRM_BUTTON).assertIsDisplayed()
        onNodeWithTag(LoginByPhoneTestTags.CHANGE_PHONE_BUTTON).assertIsDisplayed()
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
        onNodeWithTag(LoginByPhoneTestTags.RESEND_TIMER_TEXT).assertIsDisplayed()
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
        onNodeWithTag(LoginByPhoneTestTags.RESEND_CODE_BUTTON).assertIsDisplayed()
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
        onNodeWithTag(LoginByPhoneTestTags.CHANGE_PHONE_BUTTON).performClick()
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
        const val RESEND_TIMER_EXPIRED_SECONDS = 0
    }
}