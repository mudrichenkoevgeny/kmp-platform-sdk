package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.email

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
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.auth.login.email.LoginByEmailComponentMock
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test
import kotlin.test.assertEquals

@InternalApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK], application = Application::class)
class LoginByEmailScreenTest {

    @Test
    fun loading_showsIndeterminateProgressAfterDefaultDelay() = runComposeUiTest {
        val component = LoginByEmailComponentMock(LoginByEmailScreenState.Loading)
        setContent {
            ComponentTestHarness {
                LoginByEmailScreen(component)
            }
        }
        mainClock.autoAdvance = false
        mainClock.advanceTimeBy(FullscreenLoadingConfig.DELAY_MILLIS + LOADING_EXTRA_DELAY_MS)
        onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate)).assertExists()
    }

    @Test
    fun content_displaysElements() = runComposeUiTest {
        val component = LoginByEmailComponentMock(
            LoginByEmailScreenState.Content(
                email = LOGIN_EMAIL,
                isEmailValid = true,
                password = LOGIN_PASSWORD,
                isPasswordValid = true,
                isRegistrationAvailable = true
            )
        )
        setContent {
            ComponentTestHarness {
                LoginByEmailScreen(component)
            }
        }
        onNodeWithTag(LoginByEmailTestTags.BACK_BUTTON).assertIsDisplayed()
        onNodeWithTag(LoginByEmailTestTags.TITLE).assertIsDisplayed()
        onNodeWithTag(LoginByEmailTestTags.EMAIL_INPUT).assertIsDisplayed().assertTextContains(LOGIN_EMAIL)
        onNodeWithTag(LoginByEmailTestTags.PASSWORD_INPUT).assertIsDisplayed().assertTextContains(LOGIN_PASSWORD)
        onNodeWithTag(LoginByEmailTestTags.TOGGLE_PASSWORD_VISIBILITY_BUTTON).assertIsDisplayed()
        onNodeWithTag(LoginByEmailTestTags.FORGOT_PASSWORD_BUTTON).assertIsDisplayed()
        onNodeWithTag(LoginByEmailTestTags.LOGIN_BUTTON).assertIsDisplayed()
        onNodeWithTag(LoginByEmailTestTags.REGISTRATION_BUTTON).assertIsDisplayed()
    }

    @Test
    fun content_clickLogin_invokesCallback() = runComposeUiTest {
        val component = LoginByEmailComponentMock(
            LoginByEmailScreenState.Content(
                email = LOGIN_EMAIL,
                isEmailValid = true,
                password = LOGIN_PASSWORD,
                isPasswordValid = true
            )
        )
        setContent {
            ComponentTestHarness {
                LoginByEmailScreen(component)
            }
        }
        onNodeWithTag(LoginByEmailTestTags.LOGIN_BUTTON).performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.loginCalls)
    }

    @Test
    fun content_clickForgotPassword_invokesCallback() = runComposeUiTest {
        val component = LoginByEmailComponentMock(LoginByEmailScreenState.Content())
        setContent {
            ComponentTestHarness {
                LoginByEmailScreen(component)
            }
        }
        onNodeWithTag(LoginByEmailTestTags.FORGOT_PASSWORD_BUTTON).performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.forgotPasswordCalls)
    }

    @Test
    fun content_clickRegister_invokesCallback() = runComposeUiTest {
        val component = LoginByEmailComponentMock(
            LoginByEmailScreenState.Content(isRegistrationAvailable = true)
        )
        setContent {
            ComponentTestHarness {
                LoginByEmailScreen(component)
            }
        }
        onNodeWithTag(LoginByEmailTestTags.REGISTRATION_BUTTON).performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.registrationCalls)
    }

    @Test
    fun content_clickTogglePasswordVisibility_invokesCallback() = runComposeUiTest {
        val component = LoginByEmailComponentMock(LoginByEmailScreenState.Content())
        setContent {
            ComponentTestHarness {
                LoginByEmailScreen(component)
            }
        }
        onNodeWithTag(LoginByEmailTestTags.TOGGLE_PASSWORD_VISIBILITY_BUTTON).performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.togglePasswordVisibilityCalls)
    }

    @Test
    fun content_clickBack_invokesCallback() = runComposeUiTest {
        val component = LoginByEmailComponentMock(LoginByEmailScreenState.Content())
        setContent {
            ComponentTestHarness {
                LoginByEmailScreen(component)
            }
        }
        onNodeWithTag(LoginByEmailTestTags.BACK_BUTTON).performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.backCalls)
    }

    @Test
    fun content_inlineError_showsErrorTextNode() = runComposeUiTest {
        val component = LoginByEmailComponentMock(
            LoginByEmailScreenState.Content(
                email = EMAIL_INLINE_ERROR,
                actionError = CommonError.Unknown()
            )
        )
        setContent {
            ComponentTestHarness {
                LoginByEmailScreen(component)
            }
        }
        onNodeWithTag(LoginByEmailTestTags.ERROR_TEXT).assertIsDisplayed()
    }

    private companion object {
        const val LOADING_EXTRA_DELAY_MS = 50L
        const val EXPECTED_SINGLE_CALLBACK = 1

        const val LOGIN_EMAIL = "a@b.com"
        const val LOGIN_PASSWORD = "secret"
        const val EMAIL_INLINE_ERROR = "x@y.com"
    }
}