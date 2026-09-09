package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user

import android.app.Application
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.runComposeUiTest
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.PaginationState
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ComponentTestHarness
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.user.UsersManagementRootComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.user.create.CreateUserComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.user.detail.UserDetailComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.user.identifiers.UserIdentifiersComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.user.main.UsersManagementMainComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.ui.screen.management.user.sessions.UserSessionsComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.create.CreateUserTestTags
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.detail.UserDetailScreenState
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.detail.UserDetailTestTags
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.identifiers.UserIdentifiersScreenState
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.identifiers.UserIdentifiersTestTags
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.main.UsersManagementMainScreenState
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.main.UsersManagementMainTestTags
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.sessions.UserSessionsScreenState
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.sessions.UserSessionsTestTags
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.user.userDetailsMock
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test

@InternalApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK], application = Application::class)
class UsersManagementRootScreenTest {

    @Test
    fun displaysMainChild_whenActiveConfigurationIsMain() = runComposeUiTest {
        val mainComponent = UsersManagementMainComponentMock(
            initialState = UsersManagementMainScreenState.Content(paging = PaginationState(emptyList(), isInitialLoading = false)),
        )
        val rootComponent = UsersManagementRootComponentMock(
            initialChild = UsersManagementRootComponent.Child.Main(mainComponent),
            initialConfiguration = UsersManagementDestination.Main,
        )

        setContent {
            ComponentTestHarness {
                UsersManagementRootScreen(rootComponent)
            }
        }

        onNodeWithTag(UsersManagementMainTestTags.TITLE).assertIsDisplayed()
    }

    @Test
    fun displaysDetailChild_whenActiveConfigurationIsDetail() = runComposeUiTest {
        val user = userDetailsMock()
        val detailComponent = UserDetailComponentMock(
            initialState = UserDetailScreenState.Content(user = user, authorityLevelInput = "0", accountStatusInput = "ACTIVE"),
        )
        val rootComponent = UsersManagementRootComponentMock(
            initialChild = UsersManagementRootComponent.Child.Detail(detailComponent),
            initialConfiguration = UsersManagementDestination.Detail(user.id.value.toString()),
        )

        setContent {
            ComponentTestHarness {
                UsersManagementRootScreen(rootComponent)
            }
        }

        onNodeWithTag(UserDetailTestTags.TITLE).assertIsDisplayed()
    }

    @Test
    fun displaysCreateChild_whenActiveConfigurationIsCreate() = runComposeUiTest {
        val createComponent = CreateUserComponentMock()
        val rootComponent = UsersManagementRootComponentMock(
            initialChild = UsersManagementRootComponent.Child.Create(createComponent),
            initialConfiguration = UsersManagementDestination.Create,
        )

        setContent {
            ComponentTestHarness {
                UsersManagementRootScreen(rootComponent)
            }
        }

        onNodeWithTag(CreateUserTestTags.TITLE).assertIsDisplayed()
    }

    @Test
    fun displaysSessionsChild_whenActiveConfigurationIsSessions() = runComposeUiTest {
        val userId = UserId.generate()
        val sessionsComponent = UserSessionsComponentMock(
            initialState = UserSessionsScreenState.Content(paging = PaginationState(emptyList(), isInitialLoading = false)),
        )
        val rootComponent = UsersManagementRootComponentMock(
            initialChild = UsersManagementRootComponent.Child.Sessions(sessionsComponent),
            initialConfiguration = UsersManagementDestination.Sessions(userId.value.toString()),
        )

        setContent {
            ComponentTestHarness {
                UsersManagementRootScreen(rootComponent)
            }
        }

        onNodeWithTag(UserSessionsTestTags.TITLE).assertIsDisplayed()
    }

    @Test
    fun displaysIdentifiersChild_whenActiveConfigurationIsIdentifiers() = runComposeUiTest {
        val userId = UserId.generate()
        val identifiersComponent = UserIdentifiersComponentMock(
            initialState = UserIdentifiersScreenState.Content(paging = PaginationState(emptyList(), isInitialLoading = false)),
        )
        val rootComponent = UsersManagementRootComponentMock(
            initialChild = UsersManagementRootComponent.Child.Identifiers(identifiersComponent),
            initialConfiguration = UsersManagementDestination.Identifiers(userId.value.toString()),
        )

        setContent {
            ComponentTestHarness {
                UsersManagementRootScreen(rootComponent)
            }
        }

        onNodeWithTag(UserIdentifiersTestTags.TITLE).assertIsDisplayed()
    }
}
