package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.totp.recovery

import android.app.Application
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.assertIsDisplayed
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
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.profile.totp.recovery.TotpRecoveryCodesComponentMock
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.totprecoverycodes.TotpRecoveryCodes
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test
import kotlin.test.assertEquals

@InternalApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK], application = Application::class)
class TotpRecoveryCodesScreenTest {

    @Test
    fun loading_showsIndeterminateProgressAfterDefaultDelay() = runComposeUiTest {
        val component = TotpRecoveryCodesComponentMock(TotpRecoveryCodesScreenState.Loading)
        setContent {
            ComponentTestHarness {
                TotpRecoveryCodesScreen(component)
            }
        }
        mainClock.autoAdvance = false
        mainClock.advanceTimeBy(FullscreenLoadingConfig.DELAY_MILLIS + LOADING_EXTRA_DELAY_MS)
        onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate)).assertExists()
    }

    @Test
    fun content_displaysCodesAndInvokesRegenerate() = runComposeUiTest {
        val recoveryCodes = TotpRecoveryCodes(codes = listOf(CODE_ONE, CODE_TWO))
        var regenerateClicked = false
        val component = object : TotpRecoveryCodesComponentMock(
            initialState = TotpRecoveryCodesScreenState.Content(recoveryCodes = recoveryCodes)
        ) {
            override fun onRegenerateClick() {
                regenerateClicked = true
            }
        }

        setContent {
            ComponentTestHarness {
                TotpRecoveryCodesScreen(component)
            }
        }

        onNodeWithTag(TotpRecoveryCodesTestTags.BACK_BUTTON).assertIsDisplayed()
        onNodeWithTag(TotpRecoveryCodesTestTags.TITLE).assertIsDisplayed()
        onNodeWithTag(TotpRecoveryCodesTestTags.RECOVERY_CODES_TITLE).assertIsDisplayed()
        onNodeWithTag(TotpRecoveryCodesTestTags.RECOVERY_CODES_DESC).assertIsDisplayed()
        onNodeWithTag(TotpRecoveryCodesTestTags.RECOVERY_CODES_CONTAINER).assertIsDisplayed()
        onNodeWithTag(TotpRecoveryCodesTestTags.COPY_ALL_RECOVERY_CODES_BUTTON).assertIsDisplayed()

        onNodeWithTag(TotpRecoveryCodesTestTags.REGENERATE_RECOVERY_CODES_BUTTON).performScrollTo().assertIsDisplayed().performClick()
        assertEquals(true, regenerateClicked)
    }

    @Test
    fun content_showsActionError() = runComposeUiTest {
        val recoveryCodes = TotpRecoveryCodes(codes = listOf(CODE_ONE, CODE_TWO))
        val component = TotpRecoveryCodesComponentMock(
            TotpRecoveryCodesScreenState.Content(recoveryCodes = recoveryCodes, actionError = CommonError.Unknown())
        )
        setContent {
            ComponentTestHarness {
                TotpRecoveryCodesScreen(component)
            }
        }
        onNodeWithTag(TotpRecoveryCodesTestTags.ACTION_ERROR_TEXT).performScrollTo().assertIsDisplayed()
    }

    @Test
    fun error_showsGlobalErrorTextNode() = runComposeUiTest {
        val component = TotpRecoveryCodesComponentMock(
            TotpRecoveryCodesScreenState.Error(error = CommonError.Unknown())
        )
        setContent {
            ComponentTestHarness {
                TotpRecoveryCodesScreen(component)
            }
        }
        onNodeWithTag(TotpRecoveryCodesTestTags.GLOBAL_ERROR_TEXT).assertIsDisplayed()
    }

    @Test
    fun backButton_invokesOnBackClick() = runComposeUiTest {
        var backClicked = false
        val component = object : TotpRecoveryCodesComponentMock() {
            override fun onBackClick() {
                backClicked = true
            }
        }
        setContent {
            ComponentTestHarness {
                TotpRecoveryCodesScreen(component)
            }
        }
        onNodeWithTag(TotpRecoveryCodesTestTags.BACK_BUTTON).performClick()
        assertEquals(true, backClicked)
    }

    private companion object {
        const val LOADING_EXTRA_DELAY_MS = 50L
        const val CODE_ONE = "1111-2222"
        const val CODE_TWO = "3333-4444"
    }
}
