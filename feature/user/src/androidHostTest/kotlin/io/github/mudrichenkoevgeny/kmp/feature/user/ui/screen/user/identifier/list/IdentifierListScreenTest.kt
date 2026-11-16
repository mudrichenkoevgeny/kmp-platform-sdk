package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.identifier.list

import android.app.Application
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.v2.runComposeUiTest
import com.arkivanov.decompose.Child
import com.arkivanov.decompose.router.slot.ChildSlot
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.PaginationState
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ComponentTestHarness
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.ui.screen.user.identifier.IdentifierListComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.identifier.list.dialog.AddIdentifierDialogChild
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.identifier.list.dialog.AddIdentifierDialogConfig
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.auth.settings.AvailableAuthProviders
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.authprovider.UserAuthProvider
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.domain.model.identifier.createTestUserIdentifierSummary
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
        val identifier = createTestUserIdentifierSummary()
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

    @Test
    fun addIdentifierDialog_isDisplayedWhenStateIsOpen() = runComposeUiTest {
        val config = AddIdentifierDialogConfig.ProviderSelection
        val slot = ChildSlot<AddIdentifierDialogConfig, AddIdentifierDialogChild>(
            child = Child.Created(
                configuration = config,
                instance = AddIdentifierDialogChild.ProviderSelection
            )
        )
        val component = IdentifierListComponentMock(
            initialState = IdentifierListScreenState.Content(
                paging = PaginationState(),
                isAddIdentifierSupported = true,
                availableAuthProviders = AvailableAuthProviders(
                    primary = listOf(UserAuthProvider.EMAIL),
                    secondary = emptyList()
                )
            ),
            initialDialogSlot = slot
        )
        setContent {
            ComponentTestHarness {
                IdentifierListScreen(component)
            }
        }
        onNodeWithTag(IdentifierListTestTags.ADD_IDENTIFIER_DIALOG_TITLE).assertIsDisplayed()
    }

    @Test
    fun addIdentifierButton_displayed_whenSupported() = runComposeUiTest {
        val supportedComponent = IdentifierListComponentMock(
            initialState = IdentifierListScreenState.Content(
                paging = PaginationState(),
                isAddIdentifierSupported = true
            )
        )
        setContent {
            ComponentTestHarness {
                IdentifierListScreen(supportedComponent)
            }
        }
        onNodeWithTag(IdentifierListTestTags.ADD_IDENTIFIER_BUTTON).assertIsDisplayed()
    }

    @Test
    fun addIdentifierButton_hidden_whenNotSupported() = runComposeUiTest {
        val unsupportedComponent = IdentifierListComponentMock(
            initialState = IdentifierListScreenState.Content(
                paging = PaginationState(),
                isAddIdentifierSupported = false
            )
        )
        setContent {
            ComponentTestHarness {
                IdentifierListScreen(unsupportedComponent)
            }
        }
        onNodeWithTag(IdentifierListTestTags.ADD_IDENTIFIER_BUTTON).assertDoesNotExist()
    }
}
