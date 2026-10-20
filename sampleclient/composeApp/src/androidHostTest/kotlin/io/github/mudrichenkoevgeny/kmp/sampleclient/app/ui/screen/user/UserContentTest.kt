package io.github.mudrichenkoevgeny.kmp.sampleclient.app.ui.screen.user

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
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.domain.model.user.createTestUserPrivate
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.user.main.MainUserComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.main.MainUserScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.main.MainUserScreenState
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.main.MainUserTestTags
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test
import kotlin.test.assertEquals

@InternalApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK], application = Application::class)
class UserContentTest {

    @Test
    fun loading_showsIndeterminateProgress() = runComposeUiTest {
        val component = MainUserComponentMock(MainUserScreenState.Loading)
        setContent {
            ComponentTestHarness {
                MainUserScreen(component)
            }
        }
        mainClock.autoAdvance = false
        mainClock.advanceTimeBy(FullscreenLoadingConfig.DELAY_MILLIS + LOADING_EXTRA_DELAY_MS)
        onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate)).assertExists()
    }

    @Test
    fun unauthorized_showsMessageAndLogin_invokesCallback() = runComposeUiTest {
        val component = MainUserComponentMock(MainUserScreenState.Unauthorized())
        setContent {
            ComponentTestHarness {
                MainUserScreen(component)
            }
        }
        onNodeWithTag(MainUserTestTags.LOGIN_BUTTON).assertIsDisplayed().performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.loginCalls)
    }

    @Test
    fun content_showsAuthorizedUserContent() = runComposeUiTest {
        val component = MainUserComponentMock(
            MainUserScreenState.Content(user = createTestUserPrivate())
        )
        setContent {
            ComponentTestHarness {
                MainUserScreen(component)
            }
        }
        onNodeWithTag(MainUserTestTags.USER_ID_TEXT).assertIsDisplayed()
        onNodeWithTag(MainUserTestTags.LOGOUT_BUTTON).performScrollTo().assertIsDisplayed()
    }

    @Test
    fun error_showsGlobalErrorMessage() = runComposeUiTest {
        val component = MainUserComponentMock(
            MainUserScreenState.Error(CommonError.Unknown())
        )
        setContent {
            ComponentTestHarness {
                MainUserScreen(component)
            }
        }
        onNodeWithTag(MainUserTestTags.GLOBAL_ERROR_TEXT).assertIsDisplayed()
    }

    private companion object {
        const val LOADING_EXTRA_DELAY_MS = 50L
        const val EXPECTED_SINGLE_CALLBACK = 1
    }
}
