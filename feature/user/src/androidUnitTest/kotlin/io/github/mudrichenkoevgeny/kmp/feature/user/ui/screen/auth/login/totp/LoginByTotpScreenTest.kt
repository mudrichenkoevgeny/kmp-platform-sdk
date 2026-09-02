package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.totp

import android.app.Application
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenLoadingConfig
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ComponentTestHarness
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.auth.login.totp.LoginByTotpComponentMock
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test
import kotlin.test.assertEquals

@InternalApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK], application = Application::class)
class LoginByTotpScreenTest {

    @Test
    fun loading_showsIndeterminateProgressAfterDefaultDelay() = runComposeUiTest {
        val component = LoginByTotpComponentMock(LoginByTotpScreenState.Loading)
        setContent {
            ComponentTestHarness {
                LoginByTotpScreen(component)
            }
        }
        mainClock.autoAdvance = false
        mainClock.advanceTimeBy(FullscreenLoadingConfig.DELAY_MILLIS + LOADING_EXTRA_DELAY_MS)
        onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate)).assertExists()
    }

    @Test
    fun content_displaysElements() = runComposeUiTest {
        val component = LoginByTotpComponentMock(
            LoginByTotpScreenState.Content(
                mfaToken = MFA_TOKEN,
                code = TOTP_CODE,
                mode = LoginByTotpScreenState.Mode.TOTP
            )
        )
        setContent {
            ComponentTestHarness {
                LoginByTotpScreen(component)
            }
        }
        onNodeWithTag(LoginByTotpTestTags.BACK_BUTTON).assertIsDisplayed()
        onNodeWithTag(LoginByTotpTestTags.TITLE).assertIsDisplayed()
        onNodeWithTag(LoginByTotpTestTags.CODE_INPUT).assertIsDisplayed().assertTextContains(TOTP_CODE)
        onNodeWithTag(LoginByTotpTestTags.SUBMIT_BUTTON).assertIsDisplayed()
        onNodeWithTag(LoginByTotpTestTags.TOGGLE_MODE_BUTTON).assertIsDisplayed()
    }

    @Test
    fun content_clickSubmit_invokesCallback() = runComposeUiTest {
        val component = LoginByTotpComponentMock(
            LoginByTotpScreenState.Content(
                mfaToken = MFA_TOKEN,
                code = TOTP_CODE,
                mode = LoginByTotpScreenState.Mode.TOTP
            )
        )
        setContent {
            ComponentTestHarness {
                LoginByTotpScreen(component)
            }
        }
        onNodeWithTag(LoginByTotpTestTags.SUBMIT_BUTTON).performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.submitCalls)
    }

    @Test
    fun content_clickToggleMode_invokesCallback() = runComposeUiTest {
        val component = LoginByTotpComponentMock(
            LoginByTotpScreenState.Content(
                mfaToken = MFA_TOKEN,
                mode = LoginByTotpScreenState.Mode.TOTP
            )
        )
        setContent {
            ComponentTestHarness {
                LoginByTotpScreen(component)
            }
        }
        onNodeWithTag(LoginByTotpTestTags.TOGGLE_MODE_BUTTON).performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.toggleModeCalls)
    }

    @Test
    fun content_clickBack_invokesCallback() = runComposeUiTest {
        val component = LoginByTotpComponentMock(
            LoginByTotpScreenState.Content(mfaToken = MFA_TOKEN)
        )
        setContent {
            ComponentTestHarness {
                LoginByTotpScreen(component)
            }
        }
        onNodeWithTag(LoginByTotpTestTags.BACK_BUTTON).performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.backCalls)
    }

    @Test
    fun content_inlineError_showsErrorTextNode() = runComposeUiTest {
        val component = LoginByTotpComponentMock(
            LoginByTotpScreenState.Content(
                mfaToken = MFA_TOKEN,
                actionError = CommonError.Unknown()
            )
        )
        setContent {
            ComponentTestHarness {
                LoginByTotpScreen(component)
            }
        }
        onNodeWithTag(LoginByTotpTestTags.ERROR_TEXT).assertIsDisplayed()
    }

    private companion object {
        const val LOADING_EXTRA_DELAY_MS = 50L
        const val EXPECTED_SINGLE_CALLBACK = 1

        const val MFA_TOKEN = "test-mfa-token"
        const val TOTP_CODE = "123456"
    }
}