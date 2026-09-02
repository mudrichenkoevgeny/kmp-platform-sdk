package io.github.mudrichenkoevgeny.kmp.feature.clientuser.ui.screen.auth.registration.email

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
import io.github.mudrichenkoevgeny.kmp.feature.clientuser.mock.ui.screen.auth.registration.email.RegistrationByEmailComponentMock
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test
import kotlin.test.assertEquals

@InternalApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK], application = Application::class)
class RegistrationByEmailScreenTest {

    @Test
    fun loading_showsIndeterminateProgressAfterDefaultDelay() = runComposeUiTest {
        val component = RegistrationByEmailComponentMock(
            RegistrationByEmailScreenState.EmailInput(actionLoading = true)
        )
        setContent {
            ComponentTestHarness {
                RegistrationByEmailScreen(component)
            }
        }
        mainClock.autoAdvance = false
        mainClock.advanceTimeBy(FullscreenLoadingConfig.DELAY_MILLIS + LOADING_EXTRA_DELAY_MS)
        onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate)).assertExists()
    }

    @Test
    fun emailInput_displaysTitleEmailAndSendCode() = runComposeUiTest {
        val component = RegistrationByEmailComponentMock(
            RegistrationByEmailScreenState.EmailInput(email = EMAIL_SEND_STEP, isEmailValid = true)
        )
        setContent {
            ComponentTestHarness {
                RegistrationByEmailScreen(component)
            }
        }
        onNodeWithTag(RegistrationByEmailTestTags.BACK_BUTTON).assertIsDisplayed()
        onNodeWithTag(RegistrationByEmailTestTags.TITLE).assertIsDisplayed()
        onNodeWithTag(RegistrationByEmailTestTags.EMAIL_INPUT).assertIsDisplayed().assertTextContains(EMAIL_SEND_STEP)
        onNodeWithTag(RegistrationByEmailTestTags.SEND_CODE_BUTTON).assertIsDisplayed()
    }

    @Test
    fun emailInput_inlineError_showsErrorTextNode() = runComposeUiTest {
        val component = RegistrationByEmailComponentMock(
            RegistrationByEmailScreenState.EmailInput(
                email = EMAIL_SEND_STEP,
                isEmailValid = true,
                actionError = CommonError.Unknown()
            )
        )
        setContent {
            ComponentTestHarness {
                RegistrationByEmailScreen(component)
            }
        }
        onNodeWithTag(RegistrationByEmailTestTags.EMAIL_STEP_ERROR_TEXT).assertIsDisplayed()
    }

    @Test
    fun emailInput_clickSendCode_invokesCallback() = runComposeUiTest {
        val component = RegistrationByEmailComponentMock(
            RegistrationByEmailScreenState.EmailInput(email = EMAIL_SEND_STEP, isEmailValid = true)
        )
        setContent {
            ComponentTestHarness {
                RegistrationByEmailScreen(component)
            }
        }
        onNodeWithTag(RegistrationByEmailTestTags.SEND_CODE_BUTTON).performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.sendCodeCalls)
    }

    @Test
    fun registrationInput_displaysCodeSentLinePasswordRegister() = runComposeUiTest {
        val email = EMAIL_CODE_SENT_LINE
        val component = RegistrationByEmailComponentMock(
            RegistrationByEmailScreenState.RegistrationInput(
                email = email,
                code = CODE_THREE_DIGITS,
                password = REGISTRATION_PASSWORD_INVALID,
                isPasswordValid = false,
                resendTimerSeconds = RESEND_TIMER_REGISTRATION_INPUT_SECONDS
            )
        )
        setContent {
            ComponentTestHarness {
                RegistrationByEmailScreen(component)
            }
        }
        onNodeWithTag(RegistrationByEmailTestTags.BACK_BUTTON).assertIsDisplayed()
        onNodeWithTag(RegistrationByEmailTestTags.TITLE).assertIsDisplayed()
        onNodeWithTag(RegistrationByEmailTestTags.CODE_STEP_TITLE).assertIsDisplayed()
        onNodeWithTag(RegistrationByEmailTestTags.CODE_SENT_INFO_TEXT).assertIsDisplayed().assertTextContains(email)
        onNodeWithTag(RegistrationByEmailTestTags.CODE_INPUT).assertIsDisplayed().assertTextContains(CODE_THREE_DIGITS)
        onNodeWithTag(RegistrationByEmailTestTags.PASSWORD_INPUT).assertIsDisplayed()
        onNodeWithTag(RegistrationByEmailTestTags.TOGGLE_PASSWORD_VISIBILITY_BUTTON).assertIsDisplayed()
        onNodeWithTag(RegistrationByEmailTestTags.REGISTER_BUTTON).assertIsDisplayed()
    }

    @Test
    fun registrationInput_whenTimerPositive_showsCountdown() = runComposeUiTest {
        val component = RegistrationByEmailComponentMock(
            RegistrationByEmailScreenState.RegistrationInput(
                email = EMAIL_REGISTRATION_FLOW,
                resendTimerSeconds = RESEND_TIMER_COUNTDOWN_SECONDS
            )
        )
        setContent {
            ComponentTestHarness {
                RegistrationByEmailScreen(component)
            }
        }
        onNodeWithTag(RegistrationByEmailTestTags.RESEND_TIMER_TEXT).assertIsDisplayed()
    }

    @Test
    fun registrationInput_whenCanResend_showsResendButton() = runComposeUiTest {
        val component = RegistrationByEmailComponentMock(
            RegistrationByEmailScreenState.RegistrationInput(
                email = EMAIL_REGISTRATION_FLOW,
                resendTimerSeconds = RESEND_TIMER_EXPIRED_SECONDS
            )
        )
        setContent {
            ComponentTestHarness {
                RegistrationByEmailScreen(component)
            }
        }
        onNodeWithTag(RegistrationByEmailTestTags.RESEND_CODE_BUTTON).assertIsDisplayed()
    }

    @Test
    fun registrationInput_clickRegister_invokesCallback() = runComposeUiTest {
        val component = RegistrationByEmailComponentMock(
            RegistrationByEmailScreenState.RegistrationInput(
                email = EMAIL_REGISTRATION_FLOW,
                code = CODE_SIX_DIGITS,
                password = REGISTRATION_PASSWORD_VALID,
                isPasswordValid = true
            )
        )
        setContent {
            ComponentTestHarness {
                RegistrationByEmailScreen(component)
            }
        }
        onNodeWithTag(RegistrationByEmailTestTags.REGISTER_BUTTON).performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.registerCalls)
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
        const val RESEND_TIMER_EXPIRED_SECONDS = 0
    }
}