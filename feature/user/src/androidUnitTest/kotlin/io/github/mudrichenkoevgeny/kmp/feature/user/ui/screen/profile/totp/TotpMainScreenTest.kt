package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.totp

import android.app.Application
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.runComposeUiTest
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenLoadingConfig
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ComponentTestHarness
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.profile.totp.TotpMainComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.totp.main.TotpMainScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.totp.main.TotpMainScreenState
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.totp.main.TotpMainTestTags
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.totpsetup.TotpSetup
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test
import kotlin.test.assertEquals

@InternalApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK], application = Application::class)
class TotpMainScreenTest {

    @Test
    fun loading_showsIndeterminateProgressAfterDefaultDelay() = runComposeUiTest {
        val component = TotpMainComponentMock(TotpMainScreenState.Loading)
        setContent {
            ComponentTestHarness {
                TotpMainScreen(component)
            }
        }
        mainClock.autoAdvance = false
        mainClock.advanceTimeBy(FullscreenLoadingConfig.DELAY_MILLIS + LOADING_EXTRA_DELAY_MS)
        onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate)).assertExists()
    }

    @Test
    fun disabled_displaysElementsAndInvokesSetup() = runComposeUiTest {
        val component = TotpMainComponentMock(TotpMainScreenState.Disabled())
        setContent {
            ComponentTestHarness {
                TotpMainScreen(component)
            }
        }
        onNodeWithTag(TotpMainTestTags.BACK_BUTTON).assertIsDisplayed()
        onNodeWithTag(TotpMainTestTags.TITLE).assertIsDisplayed()
        onNodeWithTag(TotpMainTestTags.DISABLED_DESC_TEXT).assertIsDisplayed()
        onNodeWithTag(TotpMainTestTags.SETUP_TOTP_BUTTON).assertIsDisplayed().performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.setupCalls)
    }

    @Test
    fun disabled_showsActionError() = runComposeUiTest {
        val component = TotpMainComponentMock(
            TotpMainScreenState.Disabled(actionError = CommonError.Unknown())
        )
        setContent {
            ComponentTestHarness {
                TotpMainScreen(component)
            }
        }
        onNodeWithTag(TotpMainTestTags.DISABLED_ACTION_ERROR_TEXT).assertIsDisplayed()
    }

    @Test
    fun setupInProgress_displaysElementsAndInvokesConfirm() = runComposeUiTest {
        val setup = TotpSetup(secretKey = SECRET_KEY, otpAuthUrl = OTP_AUTH_URL, mfaToken = MFA_TOKEN)
        val component = TotpMainComponentMock(
            TotpMainScreenState.SetupInProgress(setup = setup, code = CODE_SIX_DIGITS)
        )
        setContent {
            ComponentTestHarness {
                TotpMainScreen(component)
            }
        }
        onNodeWithTag(TotpMainTestTags.BACK_BUTTON).assertIsDisplayed()
        onNodeWithTag(TotpMainTestTags.TITLE).assertIsDisplayed()
        onNodeWithTag(TotpMainTestTags.STEP1_TITLE).assertIsDisplayed()
        onNodeWithTag(TotpMainTestTags.STEP1_DESC).assertIsDisplayed()
        onNodeWithTag(TotpMainTestTags.QR_CODE_BOX).assertIsDisplayed()
        onNodeWithTag(TotpMainTestTags.MANUAL_KEY_LABEL).performScrollTo().assertIsDisplayed()
        onNodeWithTag(TotpMainTestTags.SECRET_KEY_TEXT).performScrollTo().assertIsDisplayed().assertTextContains(SECRET_KEY)
        onNodeWithTag(TotpMainTestTags.COPY_SECRET_KEY_BUTTON).performScrollTo().assertIsDisplayed()
        onNodeWithTag(TotpMainTestTags.STEP2_TITLE).performScrollTo().assertIsDisplayed()
        onNodeWithTag(TotpMainTestTags.STEP2_DESC).performScrollTo().assertIsDisplayed()
        onNodeWithTag(TotpMainTestTags.CODE_INPUT).performScrollTo().assertIsDisplayed().assertTextContains(CODE_SIX_DIGITS)
        onNodeWithTag(TotpMainTestTags.CONFIRM_SETUP_BUTTON).performScrollTo().assertIsDisplayed().performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.confirmSetupCalls)
    }

    @Test
    fun setupInProgress_showsActionError() = runComposeUiTest {
        val setup = TotpSetup(secretKey = SECRET_KEY, otpAuthUrl = OTP_AUTH_URL, mfaToken = MFA_TOKEN)
        val component = TotpMainComponentMock(
            TotpMainScreenState.SetupInProgress(setup = setup, actionError = CommonError.Unknown())
        )
        setContent {
            ComponentTestHarness {
                TotpMainScreen(component)
            }
        }
        onNodeWithTag(TotpMainTestTags.SETUP_ACTION_ERROR_TEXT).performScrollTo().assertIsDisplayed()
    }

    @Test
    fun enabled_displaysElementsAndInvokesActions() = runComposeUiTest {
        val component = TotpMainComponentMock(
            TotpMainScreenState.Enabled()
        )
        setContent {
            ComponentTestHarness {
                TotpMainScreen(component)
            }
        }
        onNodeWithTag(TotpMainTestTags.BACK_BUTTON).assertIsDisplayed()
        onNodeWithTag(TotpMainTestTags.TITLE).assertIsDisplayed()
        onNodeWithTag(TotpMainTestTags.ENABLED_TITLE).assertIsDisplayed()
        onNodeWithTag(TotpMainTestTags.ENABLED_DESC).performScrollTo().assertIsDisplayed()

        onNodeWithTag(TotpMainTestTags.RECOVERY_CODES_BUTTON).performScrollTo().assertIsDisplayed().performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.recoveryCodesCalls)

        onNodeWithTag(TotpMainTestTags.DISABLE_TOTP_BUTTON).performScrollTo().assertIsDisplayed().performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.disableCalls)
    }

    @Test
    fun enabled_showsActionError() = runComposeUiTest {
        val component = TotpMainComponentMock(
            TotpMainScreenState.Enabled(actionError = CommonError.Unknown())
        )
        setContent {
            ComponentTestHarness {
                TotpMainScreen(component)
            }
        }
        onNodeWithTag(TotpMainTestTags.ENABLED_ACTION_ERROR_TEXT).performScrollTo().assertIsDisplayed()
    }

    @Test
    fun error_showsGlobalErrorTextNode() = runComposeUiTest {
        val component = TotpMainComponentMock(
            TotpMainScreenState.Error(error = CommonError.Unknown())
        )
        setContent {
            ComponentTestHarness {
                TotpMainScreen(component)
            }
        }
        onNodeWithTag(TotpMainTestTags.GLOBAL_ERROR_TEXT).assertIsDisplayed()
    }

    @Test
    fun backButton_invokesOnBackClick() = runComposeUiTest {
        val component = TotpMainComponentMock(TotpMainScreenState.Disabled())
        setContent {
            ComponentTestHarness {
                TotpMainScreen(component)
            }
        }
        onNodeWithTag(TotpMainTestTags.BACK_BUTTON).performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.backCalls)
    }

    private companion object {
        const val LOADING_EXTRA_DELAY_MS = 50L
        const val EXPECTED_SINGLE_CALLBACK = 1

        const val SECRET_KEY = "SECRET"
        const val OTP_AUTH_URL = "otpauth://totp/..."
        const val MFA_TOKEN = "mfa-token"
        const val CODE_SIX_DIGITS = "123456"
    }
}
