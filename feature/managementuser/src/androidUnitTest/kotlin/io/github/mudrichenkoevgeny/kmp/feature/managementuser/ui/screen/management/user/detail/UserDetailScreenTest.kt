package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.detail

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
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.user.detail.UserDetailComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.user.userDetailsMock
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test
import kotlin.test.assertEquals

@InternalApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK], application = Application::class)
class UserDetailScreenTest {

    @Test
    fun loading_showsIndeterminateProgressAfterDefaultDelay() = runComposeUiTest {
        val component = UserDetailComponentMock(UserDetailScreenState.Loading)
        setContent {
            ComponentTestHarness {
                UserDetailScreen(component)
            }
        }
        mainClock.autoAdvance = false
        mainClock.advanceTimeBy(FullscreenLoadingConfig.DELAY_MILLIS + LOADING_EXTRA_DELAY_MS)
        onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate)).assertExists()
    }

    @Test
    fun content_displaysElements() = runComposeUiTest {
        val user = userDetailsMock()
        val component = UserDetailComponentMock(
            UserDetailScreenState.Content(
                user = user,
                authorityLevelInput = "0",
                accountStatusInput = "ACTIVE",
            ),
        )
        setContent {
            ComponentTestHarness {
                UserDetailScreen(component)
            }
        }
        onNodeWithTag(UserDetailTestTags.TITLE).assertIsDisplayed()
        onNodeWithTag(UserDetailTestTags.BACK_BUTTON).assertIsDisplayed()
        onNodeWithTag(UserDetailTestTags.SESSIONS_BUTTON).performScrollTo().assertIsDisplayed()
        onNodeWithTag(UserDetailTestTags.IDENTIFIERS_BUTTON).performScrollTo().assertIsDisplayed()
        onNodeWithTag(UserDetailTestTags.ACCOUNT_STATUS_INPUT).performScrollTo().assertIsDisplayed()
        onNodeWithTag(UserDetailTestTags.AUTHORITY_LEVEL_INPUT).performScrollTo().assertIsDisplayed()
        onNodeWithTag(UserDetailTestTags.UPDATE_BUTTON).performScrollTo().assertIsDisplayed()
        onNodeWithTag(UserDetailTestTags.DELETE_BUTTON).performScrollTo().assertIsDisplayed()
    }

    @Test
    fun content_clickUpdate_invokesCallback() = runComposeUiTest {
        val user = userDetailsMock()
        val component = UserDetailComponentMock(
            UserDetailScreenState.Content(
                user = user,
                authorityLevelInput = "0",
                accountStatusInput = "ACTIVE",
            ),
        )
        setContent {
            ComponentTestHarness {
                UserDetailScreen(component)
            }
        }
        onNodeWithTag(UserDetailTestTags.UPDATE_BUTTON).performScrollTo().performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.updateCalls)
    }

    @Test
    fun content_clickDelete_invokesCallback() = runComposeUiTest {
        val user = userDetailsMock()
        val component = UserDetailComponentMock(
            UserDetailScreenState.Content(
                user = user,
                authorityLevelInput = "0",
                accountStatusInput = "ACTIVE",
            ),
        )
        setContent {
            ComponentTestHarness {
                UserDetailScreen(component)
            }
        }
        onNodeWithTag(UserDetailTestTags.DELETE_BUTTON).performScrollTo().performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.deleteCalls)
    }

    @Test
    fun content_clickSessions_invokesCallback() = runComposeUiTest {
        val user = userDetailsMock()
        val component = UserDetailComponentMock(
            UserDetailScreenState.Content(
                user = user,
                authorityLevelInput = "0",
                accountStatusInput = "ACTIVE",
            ),
        )
        setContent {
            ComponentTestHarness {
                UserDetailScreen(component)
            }
        }
        onNodeWithTag(UserDetailTestTags.SESSIONS_BUTTON).performScrollTo().performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.sessionsCalls)
    }

    @Test
    fun content_clickIdentifiers_invokesCallback() = runComposeUiTest {
        val user = userDetailsMock()
        val component = UserDetailComponentMock(
            UserDetailScreenState.Content(
                user = user,
                authorityLevelInput = "0",
                accountStatusInput = "ACTIVE",
            ),
        )
        setContent {
            ComponentTestHarness {
                UserDetailScreen(component)
            }
        }
        onNodeWithTag(UserDetailTestTags.IDENTIFIERS_BUTTON).performScrollTo().performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.identifiersCalls)
    }

    @Test
    fun error_displaysErrorNode() = runComposeUiTest {
        val component = UserDetailComponentMock(UserDetailScreenState.Error(CommonError.Unknown()))
        setContent {
            ComponentTestHarness {
                UserDetailScreen(component)
            }
        }
        onNodeWithTag(UserDetailTestTags.GLOBAL_ERROR).assertIsDisplayed()
    }

    private companion object {
        const val LOADING_EXTRA_DELAY_MS = 50L
        const val EXPECTED_SINGLE_CALLBACK = 1
    }
}
