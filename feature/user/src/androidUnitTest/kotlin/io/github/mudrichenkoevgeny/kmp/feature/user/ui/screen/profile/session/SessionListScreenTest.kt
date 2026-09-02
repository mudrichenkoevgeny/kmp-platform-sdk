package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.session

import android.app.Application
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.PaginationState
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ComponentTestHarness
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.session.userSessionMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.profile.session.SessionListComponentMock
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test
import kotlin.test.assertEquals

@InternalApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK], application = Application::class)
class SessionListScreenTest {

    @Test
    fun displaysSessions_andInvokesActions() = runComposeUiTest {
        val session = userSessionMock()
        val component = SessionListComponentMock(
            initialState = SessionListScreenState.Content(
                paging = PaginationState(items = listOf(session))
            )
        )

        setContent {
            ComponentTestHarness {
                SessionListScreen(component)
            }
        }

        onNodeWithTag(SessionListTestTags.TITLE).assertIsDisplayed()
        onNodeWithTag(SessionListTestTags.BACK_BUTTON).assertIsDisplayed()
        onNodeWithTag(SessionListTestTags.REFRESH_BUTTON).assertIsDisplayed()
        onNodeWithTag(SessionListTestTags.SESSION_LIST).assertIsDisplayed()
        
        onNodeWithTag(SessionListTestTags.SESSION_ITEM_PREFIX + session.id.value).assertIsDisplayed()
    }

    @Test
    fun error_showsGlobalErrorTextNode() = runComposeUiTest {
        val component = SessionListComponentMock(
            initialState = SessionListScreenState.Error(error = CommonError.Unknown())
        )
        setContent {
            ComponentTestHarness {
                SessionListScreen(component)
            }
        }
        onNodeWithTag(SessionListTestTags.GLOBAL_ERROR_TEXT).assertIsDisplayed()
    }

    @Test
    fun backButton_invokesOnBackClick() = runComposeUiTest {
        val component = SessionListComponentMock(
            initialState = SessionListScreenState.Content(paging = PaginationState())
        )
        setContent {
            ComponentTestHarness {
                SessionListScreen(component)
            }
        }
        onNodeWithTag(SessionListTestTags.BACK_BUTTON).performClick()
        assertEquals(1, component.backCalls)
    }

    @Test
    fun refreshButton_invokesOnRefresh() = runComposeUiTest {
        val component = SessionListComponentMock(
            initialState = SessionListScreenState.Content(paging = PaginationState())
        )
        setContent {
            ComponentTestHarness {
                SessionListScreen(component)
            }
        }
        onNodeWithTag(SessionListTestTags.REFRESH_BUTTON).performClick()
        assertEquals(1, component.refreshCalls)
    }
}
