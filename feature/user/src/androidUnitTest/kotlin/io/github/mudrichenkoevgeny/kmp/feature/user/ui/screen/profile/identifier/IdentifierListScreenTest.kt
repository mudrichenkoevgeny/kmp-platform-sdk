package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier

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
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.identifier.userIdentifierMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.profile.identifier.IdentifierListComponentMock
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test
import kotlin.test.assertEquals

@InternalApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK], application = Application::class)
class IdentifierListScreenTest {

    @Test
    fun displaysIdentifiers_andInvokesActions() = runComposeUiTest {
        val identifier = userIdentifierMock()
        val component = IdentifierListComponentMock(
            initialState = IdentifierListScreenState.Content(
                paging = PaginationState(items = listOf(identifier))
            )
        )

        setContent {
            ComponentTestHarness {
                IdentifierListScreen(component)
            }
        }

        onNodeWithTag(IdentifierListTestTags.TITLE).assertIsDisplayed()
        onNodeWithTag(IdentifierListTestTags.BACK_BUTTON).assertIsDisplayed()
        onNodeWithTag(IdentifierListTestTags.REFRESH_BUTTON).assertIsDisplayed()
        onNodeWithTag(IdentifierListTestTags.IDENTIFIER_LIST).assertIsDisplayed()
        
        onNodeWithTag(IdentifierListTestTags.IDENTIFIER_ITEM_PREFIX + identifier.id.value).assertIsDisplayed()
    }

    @Test
    fun error_showsGlobalErrorTextNode() = runComposeUiTest {
        val component = IdentifierListComponentMock(
            initialState = IdentifierListScreenState.Error(error = CommonError.Unknown())
        )
        setContent {
            ComponentTestHarness {
                IdentifierListScreen(component)
            }
        }
        onNodeWithTag(IdentifierListTestTags.GLOBAL_ERROR_TEXT).assertIsDisplayed()
    }

    @Test
    fun backButton_invokesOnBackClick() = runComposeUiTest {
        val component = IdentifierListComponentMock(
            initialState = IdentifierListScreenState.Content(paging = PaginationState())
        )
        setContent {
            ComponentTestHarness {
                IdentifierListScreen(component)
            }
        }
        onNodeWithTag(IdentifierListTestTags.BACK_BUTTON).performClick()
        assertEquals(1, component.backCalls)
    }

    @Test
    fun refreshButton_invokesOnRefresh() = runComposeUiTest {
        val component = IdentifierListComponentMock(
            initialState = IdentifierListScreenState.Content(paging = PaginationState())
        )
        setContent {
            ComponentTestHarness {
                IdentifierListScreen(component)
            }
        }
        onNodeWithTag(IdentifierListTestTags.REFRESH_BUTTON).performClick()
        assertEquals(1, component.refreshCalls)
    }
}
