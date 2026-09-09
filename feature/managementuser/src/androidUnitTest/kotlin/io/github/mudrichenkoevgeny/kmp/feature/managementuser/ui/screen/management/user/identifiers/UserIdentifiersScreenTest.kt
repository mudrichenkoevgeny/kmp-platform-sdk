package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.identifiers

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
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.user.identifiers.UserIdentifiersComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.identifier.userIdentifierMock
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test
import kotlin.test.assertEquals

@InternalApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK], application = Application::class)
class UserIdentifiersScreenTest {

    @Test
    fun loading_showsIndeterminateProgressAfterDefaultDelay() = runComposeUiTest {
        val component = UserIdentifiersComponentMock(UserIdentifiersScreenState.Loading)
        setContent {
            ComponentTestHarness {
                UserIdentifiersScreen(component)
            }
        }
        mainClock.autoAdvance = false
        mainClock.advanceTimeBy(FullscreenLoadingConfig.DELAY_MILLIS + LOADING_EXTRA_DELAY_MS)
        onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate)).assertExists()
    }

    @Test
    fun content_displaysElements() = runComposeUiTest {
        val identifier = userIdentifierMock()
        val component = UserIdentifiersComponentMock(
            UserIdentifiersScreenState.Content(
                paging = PaginationState(
                    items = listOf(identifier),
                    isInitialLoading = false,
                ),
            )
        )
        setContent {
            ComponentTestHarness {
                UserIdentifiersScreen(component)
            }
        }
        onNodeWithTag(UserIdentifiersTestTags.TITLE).assertIsDisplayed()
        onNodeWithTag(UserIdentifiersTestTags.BACK_BUTTON).assertIsDisplayed()
        onNodeWithTag(UserIdentifiersTestTags.REFRESH_BUTTON).assertIsDisplayed()
        onNodeWithTag(UserIdentifiersTestTags.IDENTIFIER_LIST).assertIsDisplayed()
    }

    @Test
    fun content_clickBack_invokesCallback() = runComposeUiTest {
        val component = UserIdentifiersComponentMock(UserIdentifiersScreenState.Content(paging = PaginationState(emptyList(), isInitialLoading = false)))
        setContent {
            ComponentTestHarness {
                UserIdentifiersScreen(component)
            }
        }
        onNodeWithTag(UserIdentifiersTestTags.BACK_BUTTON).performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.backCalls)
    }

    @Test
    fun content_clickRefresh_invokesCallback() = runComposeUiTest {
        val component = UserIdentifiersComponentMock(UserIdentifiersScreenState.Content(paging = PaginationState(emptyList(), isInitialLoading = false)))
        setContent {
            ComponentTestHarness {
                UserIdentifiersScreen(component)
            }
        }
        onNodeWithTag(UserIdentifiersTestTags.REFRESH_BUTTON).performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.refreshCalls)
    }

    @Test
    fun error_displaysErrorText() = runComposeUiTest {
        val component = UserIdentifiersComponentMock(UserIdentifiersScreenState.Error(CommonError.Unknown()))
        setContent {
            ComponentTestHarness {
                UserIdentifiersScreen(component)
            }
        }
        onNodeWithTag(UserIdentifiersTestTags.GLOBAL_ERROR_TEXT).assertIsDisplayed()
    }

    private companion object {
        const val LOADING_EXTRA_DELAY_MS = 50L
        const val EXPECTED_SINGLE_CALLBACK = 1
    }
}
