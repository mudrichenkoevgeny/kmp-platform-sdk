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
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.sessions.GlobalSessionListComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.session.userSessionMock
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
        val component = GlobalSessionListComponentMock(GlobalSessionListScreenState.Loading)
        setContent {
            ComponentTestHarness {
                GlobalSessionListScreen(component)
            }
        }
        mainClock.autoAdvance = false
        mainClock.advanceTimeBy(FullscreenLoadingConfig.DELAY_MILLIS + LOADING_EXTRA_DELAY_MS)
        onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate)).assertExists()
    }

    @Test
    fun content_displaysElements() = runComposeUiTest {
        val session = userSessionMock()
        val component = GlobalSessionListComponentMock(
            GlobalSessionListScreenState.Content(
                paging = PaginationState(
                    items = listOf(session),
                    isInitialLoading = false,
                ),
            )
        )
        setContent {
            ComponentTestHarness {
                GlobalSessionListScreen(component)
            }
        }
        onNodeWithTag(GlobalSessionListTestTags.TITLE).assertIsDisplayed()
        onNodeWithTag(GlobalSessionListTestTags.BACK_BUTTON).assertIsDisplayed()
        onNodeWithTag(GlobalSessionListTestTags.FILTER_BUTTON).assertIsDisplayed()
        onNodeWithTag(GlobalSessionListTestTags.REFRESH_BUTTON).assertIsDisplayed()
        onNodeWithTag(GlobalSessionListTestTags.SESSION_LIST).assertIsDisplayed()
    }

    @Test
    fun content_clickBack_invokesCallback() = runComposeUiTest {
        val component = GlobalSessionListComponentMock(GlobalSessionListScreenState.Content(paging = PaginationState(emptyList(), isInitialLoading = false)))
        setContent {
            ComponentTestHarness {
                GlobalSessionListScreen(component)
            }
        }
        onNodeWithTag(GlobalSessionListTestTags.BACK_BUTTON).performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.backCalls)
    }

    @Test
    fun content_clickRefresh_invokesCallback() = runComposeUiTest {
        val component = GlobalSessionListComponentMock(GlobalSessionListScreenState.Content(paging = PaginationState(emptyList(), isInitialLoading = false)))
        setContent {
            ComponentTestHarness {
                GlobalSessionListScreen(component)
            }
        }
        onNodeWithTag(GlobalSessionListTestTags.REFRESH_BUTTON).performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.refreshCalls)
    }
    
    @Test
    fun content_clickFilter_invokesCallback() = runComposeUiTest {
        val component = GlobalSessionListComponentMock(GlobalSessionListScreenState.Content(paging = PaginationState(emptyList(), isInitialLoading = false)))
        setContent {
            ComponentTestHarness {
                GlobalSessionListScreen(component)
            }
        }
        onNodeWithTag(GlobalSessionListTestTags.FILTER_BUTTON).performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.toggleFilterPanelCalls)
    }

    @Test
    fun error_displaysErrorText() = runComposeUiTest {
        val component = GlobalSessionListComponentMock(GlobalSessionListScreenState.Error(CommonError.Unknown()))
        setContent {
            ComponentTestHarness {
                GlobalSessionListScreen(component)
            }
        }
        onNodeWithTag(GlobalSessionListTestTags.GLOBAL_ERROR_TEXT).assertIsDisplayed()
    }

    private companion object {
        const val LOADING_EXTRA_DELAY_MS = 50L
        const val EXPECTED_SINGLE_CALLBACK = 1
    }
}
