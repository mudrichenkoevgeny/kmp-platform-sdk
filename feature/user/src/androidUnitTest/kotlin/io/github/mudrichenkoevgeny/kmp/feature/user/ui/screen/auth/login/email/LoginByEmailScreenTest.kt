package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.email

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
class LoginByEmailScreenTest {

    @Test
    fun loading_showsIndeterminateProgressAfterDefaultDelay() = runComposeUiTest {
        val component = FakeLoginByEmailComponent(LoginByEmailScreenState.Loading)
        setContent { LoginByEmailScreenHarness(component) }
        mainClock.autoAdvance = false
        mainClock.advanceTimeBy(FullscreenLoadingConfig.DELAY_MILLIS + LOADING_EXTRA_DELAY_MS)
        onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate)).assertExists()
    }

    @Test
    fun content_displaysTitleFieldsAndActions() = runComposeUiTest {
        val component = FakeLoginByEmailComponent(
            LoginByEmailScreenState.Content(
                email = LOGIN_EMAIL,
                isEmailValid = true,
                password = LOGIN_PASSWORD,
                isPasswordValid = true
            )
        )
        setContent { LoginByEmailScreenHarness(component) }
        onNodeWithText(LOGIN_BY_EMAIL_TITLE).assertIsDisplayed()
        onNodeWithText(EMAIL_LABEL).assertIsDisplayed()
        onNodeWithText(PASSWORD_LABEL).assertIsDisplayed()
        onNodeWithText(FORGOT_PASSWORD).assertIsDisplayed()
        onNodeWithText(LOGIN_BUTTON).assertIsDisplayed()
        onNodeWithText(NO_ACCOUNT_REGISTER).assertIsDisplayed()
    }

    @Test
    fun content_clickLogin_invokesCallback() = runComposeUiTest {
        val component = FakeLoginByEmailComponent(
            LoginByEmailScreenState.Content(
                email = LOGIN_EMAIL,
                isEmailValid = true,
                password = LOGIN_PASSWORD,
                isPasswordValid = true
            )
        )
        setContent { LoginByEmailScreenHarness(component) }
        onNodeWithText(LOGIN_BUTTON).performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.loginClicks)
    }

    @Test
    fun content_clickForgotPassword_invokesCallback() = runComposeUiTest {
        val component = FakeLoginByEmailComponent(LoginByEmailScreenState.Content())
        setContent { LoginByEmailScreenHarness(component) }
        onNodeWithText(FORGOT_PASSWORD).performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.forgotPasswordClicks)
    }

    @Test
    fun content_clickRegister_invokesCallback() = runComposeUiTest {
        val component = FakeLoginByEmailComponent(LoginByEmailScreenState.Content())
        setContent { LoginByEmailScreenHarness(component) }
        onNodeWithText(NO_ACCOUNT_REGISTER).performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.registrationClicks)
    }

    @Test
    fun content_inlineError_showsLocalizedMessage() = runComposeUiTest {
        val component = FakeLoginByEmailComponent(
            LoginByEmailScreenState.Content(
                email = EMAIL_INLINE_ERROR,
                actionError = CommonError.Unknown()
            )
        )
        setContent { LoginByEmailScreenHarness(component) }
        onNodeWithText(MOCK_ERROR_MESSAGE).assertIsDisplayed()
    }

    private companion object {
        const val LOADING_EXTRA_DELAY_MS = 50L
        const val EXPECTED_SINGLE_CALLBACK = 1

        const val LOGIN_EMAIL = "a@b.com"
        const val LOGIN_PASSWORD = "secret"
        const val EMAIL_INLINE_ERROR = "x@y.com"

        const val LOGIN_BY_EMAIL_TITLE = "Login by Email"
        const val EMAIL_LABEL = "Email"
        const val PASSWORD_LABEL = "Password"
        const val FORGOT_PASSWORD = "Forgot password?"
        const val LOGIN_BUTTON = "Login"
        const val NO_ACCOUNT_REGISTER = "Don't have an account? Register"
        const val MOCK_ERROR_MESSAGE = "Unknown Error"
    }
}

@Composable
private fun LoginByEmailScreenHarness(component: LoginByEmailComponent) {
    MaterialTheme {
        CompositionLocalProvider(LocalErrorParser provides MockAppErrorParser) {
            LoginByEmailScreen(component)
        }
    }
}

private class FakeLoginByEmailComponent(
    initial: LoginByEmailScreenState
) : LoginByEmailComponent {

    private val mutableState = MutableValue(initial)
    override val state: Value<LoginByEmailScreenState> get() = mutableState

    var loginClicks: Int = 0
        private set
    var forgotPasswordClicks: Int = 0
        private set
    var registrationClicks: Int = 0
        private set

    override fun onEmailChanged(email: String) {}
    override fun onPasswordChanged(password: String) {}
    override fun onTogglePasswordVisibility() {}
    override fun onLoginClick() {
        loginClicks++
    }

    override fun onForgotPasswordClick() {
        forgotPasswordClicks++
    }

    override fun onRegistrationClick() {
        registrationClicks++
    }

    override fun onBackClick() {}
}
