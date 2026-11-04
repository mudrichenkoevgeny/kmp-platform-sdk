package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.session.globallist

import android.app.Application
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.PaginationState
import io.github.mudrichenkoevgeny.kmp.core.common.ui.component.loading.FullscreenLoadingConfig
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ComponentTestHarness
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.user.session.SessionListComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.session.list.SessionListScreen
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.session.list.SessionListScreenState
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.session.list.SessionListTestTags
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.domain.model.session.createTestUserSessionSummary
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test
import kotlin.test.assertEquals

@InternalApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK], application = Application::class)
class GlobalSessionListScreenTest {

    @Test
    fun loading_showsIndeterminateProgressAfterDefaultDelay() = runComposeUiTest {
        val component = SessionListComponentMock(initialState = SessionListScreenState.Loading, includeFilterPanel = true)
        setContent {
            ComponentTestHarness {
                SessionListScreen(component)
            }
        }
        mainClock.autoAdvance = false
        mainClock.advanceTimeBy(FullscreenLoadingConfig.DELAY_MILLIS + LOADING_EXTRA_DELAY_MS)
        onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate)).assertExists()
    }

    @Test
    fun content_displaysElements() = runComposeUiTest {
        val session = createTestUserSessionSummary()
        val component = SessionListComponentMock(
            initialState = SessionListScreenState.Content(
                paging = PaginationState(
                    items = listOf(session),
                    isInitialLoading = false
                )
            ),
            includeFilterPanel = true
        )
        setContent {
            ComponentTestHarness {
                SessionListScreen(component)
            }
        }
        onNodeWithTag(SessionListTestTags.TITLE).assertIsDisplayed()
        onNodeWithTag(SessionListTestTags.BACK_BUTTON).assertIsDisplayed()
        onNodeWithTag(SessionListTestTags.FILTER_BUTTON).assertIsDisplayed()
        onNodeWithTag(SessionListTestTags.REFRESH_BUTTON).assertIsDisplayed()
        onNodeWithTag(SessionListTestTags.SESSION_LIST).assertIsDisplayed()
    }

    @Test
    fun content_clickBack_invokesCallback() = runComposeUiTest {
        val component = SessionListComponentMock(
            initialState = SessionListScreenState.Content(paging = PaginationState(emptyList(), isInitialLoading = false)),
            includeFilterPanel = true
        )
        setContent {
            ComponentTestHarness {
                SessionListScreen(component)
            }
        }
        onNodeWithTag(SessionListTestTags.BACK_BUTTON).performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.backCalls)
    }

    @Test
    fun content_clickRefresh_invokesCallback() = runComposeUiTest {
        val component = SessionListComponentMock(
            initialState = SessionListScreenState.Content(paging = PaginationState(emptyList(), isInitialLoading = false)),
            includeFilterPanel = true
        )
        setContent {
            ComponentTestHarness {
                SessionListScreen(component)
            }
        }
        onNodeWithTag(SessionListTestTags.REFRESH_BUTTON).performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.refreshCalls)
    }

    @Test
    fun content_clickFilter_invokesCallback() = runComposeUiTest {
        val component = SessionListComponentMock(
            initialState = SessionListScreenState.Content(paging = PaginationState(emptyList(), isInitialLoading = false)),
            includeFilterPanel = true
        )
        setContent {
            ComponentTestHarness {
                SessionListScreen(component)
            }
        }
        onNodeWithTag(SessionListTestTags.FILTER_BUTTON).performClick()
        assertEquals(0, component.backCalls)
    }

    @Test
    fun error_displaysErrorText() = runComposeUiTest {
        val component = SessionListComponentMock(
            initialState = SessionListScreenState.Error(CommonError.Unknown()),
            includeFilterPanel = true
        )
        setContent {
            ComponentTestHarness {
                SessionListScreen(component)
            }
        }
        onNodeWithTag(SessionListTestTags.GLOBAL_ERROR_TEXT).assertIsDisplayed()
    }

    private companion object {
        const val LOADING_EXTRA_DELAY_MS = 50L
        const val EXPECTED_SINGLE_CALLBACK = 1
    }
}
