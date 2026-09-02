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
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.profile.totp.TotpSettingsComponentMock
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.totprecoverycodes.TotpRecoveryCodes
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.totpsetup.TotpSetup
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test
import kotlin.test.assertEquals

@InternalApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK], application = Application::class)
class TotpSettingsScreenTest {

    @Test
    fun loading_showsIndeterminateProgressAfterDefaultDelay() = runComposeUiTest {
        val component = TotpSettingsComponentMock(TotpSettingsScreenState.Loading)
        setContent {
            ComponentTestHarness {
                TotpSettingsScreen(component)
            }
        }
        mainClock.autoAdvance = false
        mainClock.advanceTimeBy(FullscreenLoadingConfig.DELAY_MILLIS + LOADING_EXTRA_DELAY_MS)
        onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate)).assertExists()
    }

    @Test
    fun disabled_displaysElementsAndInvokesSetup() = runComposeUiTest {
        val component = TotpSettingsComponentMock(TotpSettingsScreenState.Disabled())
        setContent {
            ComponentTestHarness {
                TotpSettingsScreen(component)
            }
        }
        onNodeWithTag(TotpSettingsTestTags.BACK_BUTTON).assertIsDisplayed()
        onNodeWithTag(TotpSettingsTestTags.TITLE).assertIsDisplayed()
        onNodeWithTag(TotpSettingsTestTags.DISABLED_DESC_TEXT).assertIsDisplayed()
        onNodeWithTag(TotpSettingsTestTags.SETUP_TOTP_BUTTON).assertIsDisplayed().performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.setupCalls)
    }

    @Test
    fun disabled_showsActionError() = runComposeUiTest {
        val component = TotpSettingsComponentMock(
            TotpSettingsScreenState.Disabled(actionError = CommonError.Unknown())
        )
        setContent {
            ComponentTestHarness {
                TotpSettingsScreen(component)
            }
        }
        onNodeWithTag(TotpSettingsTestTags.DISABLED_ACTION_ERROR_TEXT).assertIsDisplayed()
    }

    @Test
    fun setupInProgress_displaysElementsAndInvokesConfirm() = runComposeUiTest {
        val setup = TotpSetup(secretKey = SECRET_KEY, otpAuthUrl = OTP_AUTH_URL, mfaToken = MFA_TOKEN)
        val component = TotpSettingsComponentMock(
            TotpSettingsScreenState.SetupInProgress(setup = setup, code = CODE_SIX_DIGITS)
        )
        setContent {
            ComponentTestHarness {
                TotpSettingsScreen(component)
            }
        }
        onNodeWithTag(TotpSettingsTestTags.BACK_BUTTON).assertIsDisplayed()
        onNodeWithTag(TotpSettingsTestTags.TITLE).assertIsDisplayed()
        onNodeWithTag(TotpSettingsTestTags.STEP1_TITLE).assertIsDisplayed()
        onNodeWithTag(TotpSettingsTestTags.STEP1_DESC).assertIsDisplayed()
        onNodeWithTag(TotpSettingsTestTags.QR_CODE_BOX).assertIsDisplayed()
        onNodeWithTag(TotpSettingsTestTags.MANUAL_KEY_LABEL).performScrollTo().assertIsDisplayed()
        onNodeWithTag(TotpSettingsTestTags.SECRET_KEY_TEXT).performScrollTo().assertIsDisplayed().assertTextContains(SECRET_KEY)
        onNodeWithTag(TotpSettingsTestTags.COPY_SECRET_KEY_BUTTON).performScrollTo().assertIsDisplayed()
        onNodeWithTag(TotpSettingsTestTags.STEP2_TITLE).performScrollTo().assertIsDisplayed()
        onNodeWithTag(TotpSettingsTestTags.STEP2_DESC).performScrollTo().assertIsDisplayed()
        onNodeWithTag(TotpSettingsTestTags.CODE_INPUT).performScrollTo().assertIsDisplayed().assertTextContains(CODE_SIX_DIGITS)
        onNodeWithTag(TotpSettingsTestTags.CONFIRM_SETUP_BUTTON).performScrollTo().assertIsDisplayed().performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.confirmSetupCalls)
    }

    @Test
    fun setupInProgress_showsActionError() = runComposeUiTest {
        val setup = TotpSetup(secretKey = SECRET_KEY, otpAuthUrl = OTP_AUTH_URL, mfaToken = MFA_TOKEN)
        val component = TotpSettingsComponentMock(
            TotpSettingsScreenState.SetupInProgress(setup = setup, actionError = CommonError.Unknown())
        )
        setContent {
            ComponentTestHarness {
                TotpSettingsScreen(component)
            }
        }
        onNodeWithTag(TotpSettingsTestTags.SETUP_ACTION_ERROR_TEXT).performScrollTo().assertIsDisplayed()
    }

    @Test
    fun enabled_displaysElementsAndInvokesActions() = runComposeUiTest {
        val recoveryCodes = TotpRecoveryCodes(codes = listOf(CODE_ONE, CODE_TWO))
        val component = TotpSettingsComponentMock(
            TotpSettingsScreenState.Enabled(recoveryCodes = recoveryCodes)
        )
        setContent {
            ComponentTestHarness {
                TotpSettingsScreen(component)
            }
        }
        onNodeWithTag(TotpSettingsTestTags.BACK_BUTTON).assertIsDisplayed()
        onNodeWithTag(TotpSettingsTestTags.TITLE).assertIsDisplayed()
        onNodeWithTag(TotpSettingsTestTags.ENABLED_TITLE).assertIsDisplayed()
        onNodeWithTag(TotpSettingsTestTags.RECOVERY_CODES_TITLE).performScrollTo().assertIsDisplayed()
        onNodeWithTag(TotpSettingsTestTags.RECOVERY_CODES_DESC).performScrollTo().assertIsDisplayed()
        onNodeWithTag(TotpSettingsTestTags.RECOVERY_CODES_CONTAINER).performScrollTo().assertIsDisplayed()
        onNodeWithTag(TotpSettingsTestTags.COPY_ALL_RECOVERY_CODES_BUTTON).performScrollTo().assertIsDisplayed()

        onNodeWithTag(TotpSettingsTestTags.REGENERATE_RECOVERY_CODES_BUTTON).performScrollTo().assertIsDisplayed().performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.regenerateRecoveryCodesCalls)

        onNodeWithTag(TotpSettingsTestTags.DISABLE_TOTP_BUTTON).performScrollTo().assertIsDisplayed().performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.disableCalls)
    }

    @Test
    fun enabled_showsActionError() = runComposeUiTest {
        val recoveryCodes = TotpRecoveryCodes(codes = listOf(CODE_ONE, CODE_TWO))
        val component = TotpSettingsComponentMock(
            TotpSettingsScreenState.Enabled(recoveryCodes = recoveryCodes, actionError = CommonError.Unknown())
        )
        setContent {
            ComponentTestHarness {
                TotpSettingsScreen(component)
            }
        }
        onNodeWithTag(TotpSettingsTestTags.ENABLED_ACTION_ERROR_TEXT).performScrollTo().assertIsDisplayed()
    }

    @Test
    fun error_showsGlobalErrorTextNode() = runComposeUiTest {
        val component = TotpSettingsComponentMock(
            TotpSettingsScreenState.Error(error = CommonError.Unknown())
        )
        setContent {
            ComponentTestHarness {
                TotpSettingsScreen(component)
            }
        }
        onNodeWithTag(TotpSettingsTestTags.GLOBAL_ERROR_TEXT).assertIsDisplayed()
    }

    @Test
    fun backButton_invokesOnBackClick() = runComposeUiTest {
        val component = TotpSettingsComponentMock(TotpSettingsScreenState.Disabled())
        setContent {
            ComponentTestHarness {
                TotpSettingsScreen(component)
            }
        }
        onNodeWithTag(TotpSettingsTestTags.BACK_BUTTON).performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.backCalls)
    }

    private companion object {
        const val LOADING_EXTRA_DELAY_MS = 50L
        const val EXPECTED_SINGLE_CALLBACK = 1

        const val SECRET_KEY = "SECRET"
        const val OTP_AUTH_URL = "otpauth://totp/..."
        const val MFA_TOKEN = "mfa-token"
        const val CODE_SIX_DIGITS = "123456"
        const val CODE_ONE = "1111-2222"
        const val CODE_TWO = "3333-4444"
    }
}