package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.identifier.list

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
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.profile.identifier.SelfIdentifierListComponentMock
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.settings.AvailableAuthProviders
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test
import kotlin.test.assertEquals

@InternalApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK], application = Application::class)
class SelfIdentifierListScreenTest {

    @Test
    fun displaysIdentifiers_andInvokesActions() = runComposeUiTest {
        val identifier = userIdentifierMock()
        val component = SelfIdentifierListComponentMock(
            initialState = SelfIdentifierListScreenState.Content(
                paging = PaginationState(items = listOf(identifier))
            )
        )

        setContent {
            ComponentTestHarness {
                SelfIdentifierListScreen(component)
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
        val component = SelfIdentifierListComponentMock(
            initialState = SelfIdentifierListScreenState.Error(error = CommonError.Unknown())
        )
        setContent {
            ComponentTestHarness {
                SelfIdentifierListScreen(component)
            }
        }
        onNodeWithTag(IdentifierListTestTags.GLOBAL_ERROR_TEXT).assertIsDisplayed()
    }

    @Test
    fun backButton_invokesOnBackClick() = runComposeUiTest {
        val component = SelfIdentifierListComponentMock(
            initialState = SelfIdentifierListScreenState.Content(paging = PaginationState())
        )
        setContent {
            ComponentTestHarness {
                SelfIdentifierListScreen(component)
            }
        }
        onNodeWithTag(IdentifierListTestTags.BACK_BUTTON).performClick()
        assertEquals(1, component.backCalls)
    }

    @Test
    fun refreshButton_invokesOnRefresh() = runComposeUiTest {
        val component = SelfIdentifierListComponentMock(
            initialState = SelfIdentifierListScreenState.Content(paging = PaginationState())
        )
        setContent {
            ComponentTestHarness {
                SelfIdentifierListScreen(component)
            }
        }
        onNodeWithTag(IdentifierListTestTags.REFRESH_BUTTON).performClick()
        assertEquals(1, component.refreshCalls)
    }

    @Test
    fun addIdentifierDialog_isDisplayedWhenStateIsOpen() = runComposeUiTest {
        val component = SelfIdentifierListComponentMock(
            initialState = SelfIdentifierListScreenState.Content(
                paging = PaginationState(),
                addIdentifierDialogState = AddIdentifierDialogState.ProviderSelection,
                isAddIdentifierSupported = true,
                availableAuthProviders = AvailableAuthProviders(
                    primary = listOf(UserAuthProvider.EMAIL),
                    secondary = emptyList()
                )
            )
        )
        setContent {
            ComponentTestHarness {
                SelfIdentifierListScreen(component)
            }
        }
        onNodeWithTag(IdentifierListTestTags.ADD_IDENTIFIER_DIALOG_TITLE).assertIsDisplayed()
    }

    @Test
    fun addIdentifierButton_displayed_whenSupported() = runComposeUiTest {
        val supportedComponent = SelfIdentifierListComponentMock(
            initialState = SelfIdentifierListScreenState.Content(
                paging = PaginationState(),
                isAddIdentifierSupported = true
            )
        )
        setContent {
            ComponentTestHarness {
                SelfIdentifierListScreen(supportedComponent)
            }
        }
        onNodeWithTag(IdentifierListTestTags.ADD_IDENTIFIER_BUTTON).assertIsDisplayed()
    }

    @Test
    fun addIdentifierButton_hidden_whenNotSupported() = runComposeUiTest {
        val unsupportedComponent = SelfIdentifierListComponentMock(
            initialState = SelfIdentifierListScreenState.Content(
                paging = PaginationState(),
                isAddIdentifierSupported = false
            )
        )
        setContent {
            ComponentTestHarness {
                SelfIdentifierListScreen(unsupportedComponent)
            }
        }
        onNodeWithTag(IdentifierListTestTags.ADD_IDENTIFIER_BUTTON).assertDoesNotExist()
    }
}
