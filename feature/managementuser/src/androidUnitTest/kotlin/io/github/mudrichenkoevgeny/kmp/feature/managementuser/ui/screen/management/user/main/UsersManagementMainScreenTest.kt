package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.main

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
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.user.main.UsersManagementMainComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.user.userDetailsMock
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test
import kotlin.test.assertEquals

@InternalApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK], application = Application::class)
class UsersManagementMainScreenTest {

    @Test
    fun loading_showsIndeterminateProgressAfterDefaultDelay() = runComposeUiTest {
        val component = UsersManagementMainComponentMock(UsersManagementMainScreenState.Loading)
        setContent {
            ComponentTestHarness {
                UsersManagementMainScreen(component)
            }
        }
        mainClock.autoAdvance = false
        mainClock.advanceTimeBy(FullscreenLoadingConfig.DELAY_MILLIS + LOADING_EXTRA_DELAY_MS)
        onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate)).assertExists()
    }

    @Test
    fun content_displaysElements() = runComposeUiTest {
        val user = userDetailsMock()
        val component = UsersManagementMainComponentMock(
            UsersManagementMainScreenState.Content(
                paging = PaginationState(
                    items = listOf(user),
                    isInitialLoading = false,
                ),
            )
        )
        setContent {
            ComponentTestHarness {
                UsersManagementMainScreen(component)
            }
        }
        onNodeWithTag(UsersManagementMainTestTags.TITLE).assertIsDisplayed()
        onNodeWithTag(UsersManagementMainTestTags.BACK_BUTTON).assertIsDisplayed()
        onNodeWithTag(UsersManagementMainTestTags.REFRESH_BUTTON).assertIsDisplayed()
        onNodeWithTag(UsersManagementMainTestTags.CREATE_USER_FAB).assertIsDisplayed()
        onNodeWithTag(UsersManagementMainTestTags.USER_LIST).assertIsDisplayed()
    }

    @Test
    fun content_clickBack_invokesCallback() = runComposeUiTest {
        val component = UsersManagementMainComponentMock(UsersManagementMainScreenState.Content(paging = PaginationState(emptyList(), isInitialLoading = false)))
        setContent {
            ComponentTestHarness {
                UsersManagementMainScreen(component)
            }
        }
        onNodeWithTag(UsersManagementMainTestTags.BACK_BUTTON).performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.backCalls)
    }

    @Test
    fun content_clickRefresh_invokesCallback() = runComposeUiTest {
        val component = UsersManagementMainComponentMock(UsersManagementMainScreenState.Content(paging = PaginationState(emptyList(), isInitialLoading = false)))
        setContent {
            ComponentTestHarness {
                UsersManagementMainScreen(component)
            }
        }
        onNodeWithTag(UsersManagementMainTestTags.REFRESH_BUTTON).performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.refreshCalls)
    }

    @Test
    fun content_clickCreateUser_invokesCallback() = runComposeUiTest {
        val component = UsersManagementMainComponentMock(UsersManagementMainScreenState.Content(paging = PaginationState(emptyList(), isInitialLoading = false)))
        setContent {
            ComponentTestHarness {
                UsersManagementMainScreen(component)
            }
        }
        onNodeWithTag(UsersManagementMainTestTags.CREATE_USER_FAB).performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.createUserCalls)
    }

    @Test
    fun error_displaysErrorText() = runComposeUiTest {
        val component = UsersManagementMainComponentMock(UsersManagementMainScreenState.Error(CommonError.Unknown()))
        setContent {
            ComponentTestHarness {
                UsersManagementMainScreen(component)
            }
        }
        onNodeWithTag(UsersManagementMainTestTags.GLOBAL_ERROR_TEXT).assertIsDisplayed()
    }

    private companion object {
        const val LOADING_EXTRA_DELAY_MS = 50L
        const val EXPECTED_SINGLE_CALLBACK = 1
    }
}
