package io.github.mudrichenkoevgeny.kmp.samplemanagement.app.ui.screen.profile

import android.app.Application
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenLoadingConfig
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ComponentTestHarness
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.user.userDetailsMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.profile.main.MainProfileComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.main.MainProfileScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.main.MainProfileScreenState
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.main.MainProfileTestTags
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test
import kotlin.test.assertEquals

@InternalApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK], application = Application::class)
class ProfileContentTest {

    @Test
    fun loading_showsIndeterminateProgress() = runComposeUiTest {
        val component = MainProfileComponentMock(MainProfileScreenState.Loading)
        setContent {
            ComponentTestHarness {
                MainProfileScreen(component)
            }
        }
        mainClock.autoAdvance = false
        mainClock.advanceTimeBy(FullscreenLoadingConfig.DELAY_MILLIS + LOADING_EXTRA_DELAY_MS)
        onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate)).assertExists()
    }

    @Test
    fun unauthorized_showsMessageAndLogin_invokesCallback() = runComposeUiTest {
        val component = MainProfileComponentMock(MainProfileScreenState.Unauthorized)
        setContent {
            ComponentTestHarness {
                MainProfileScreen(component)
            }
        }
        onNodeWithTag(MainProfileTestTags.UNAUTHORIZED_TEXT).assertIsDisplayed()
        onNodeWithTag(MainProfileTestTags.LOGIN_BUTTON).assertIsDisplayed().performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.loginCalls)
    }

    @Test
    fun content_showsAuthorizedUserContent() = runComposeUiTest {
        val component = MainProfileComponentMock(
            MainProfileScreenState.Content(user = userDetailsMock())
        )
        setContent {
            ComponentTestHarness {
                MainProfileScreen(component)
            }
        }
        onNodeWithTag(MainProfileTestTags.USER_ID_TEXT).assertIsDisplayed()
        onNodeWithTag(MainProfileTestTags.LOGOUT_BUTTON).assertIsDisplayed()
    }

    @Test
    fun error_showsGlobalErrorMessage() = runComposeUiTest {
        val component = MainProfileComponentMock(
            MainProfileScreenState.Error(CommonError.Unknown())
        )
        setContent {
            ComponentTestHarness {
                MainProfileScreen(component)
            }
        }
        onNodeWithTag(MainProfileTestTags.GLOBAL_ERROR_TEXT).assertIsDisplayed()
    }

    private companion object {
        const val LOADING_EXTRA_DELAY_MS = 50L
        const val EXPECTED_SINGLE_CALLBACK = 1
    }
}
