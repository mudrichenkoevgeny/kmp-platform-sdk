package io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit.ui.screen.root

import android.app.Application
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.runComposeUiTest
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.PaginationState
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ComponentTestHarness
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit.mock.ui.screen.detail.AuditEventDetailComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit.mock.ui.screen.events.AuditEventsComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit.mock.ui.screen.root.AuditApiRootComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit.ui.screen.detail.AuditEventDetailScreenState
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit.ui.screen.detail.AuditEventDetailTestTags
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit.ui.screen.events.AuditEventsScreenState
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit.ui.screen.events.AuditEventsTestTags
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit.mock.domain.model.event.auditEventMock
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test


@InternalApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK], application = Application::class)
class AuditApiRootScreenTest {

    @Test
    fun displaysMainChild_whenActiveConfigurationIsMain() = runComposeUiTest {
        val eventsComponent = AuditEventsComponentMock(
            initialState = AuditEventsScreenState.Content(paging = PaginationState(emptyList(), isInitialLoading = false)),
        )
        val rootComponent = AuditApiRootComponentMock(
            initialChild = AuditApiRootComponent.Child.Main(eventsComponent),
            initialConfiguration = AuditApiDestination.Main,
        )

        setContent {
            ComponentTestHarness {
                AuditApiRootScreen(rootComponent)
            }
        }

        onNodeWithTag(AuditEventsTestTags.TITLE).assertIsDisplayed()
    }

    @Test
    fun displaysDetailChild_whenActiveConfigurationIsDetail() = runComposeUiTest {
        val event = auditEventMock()
        val detailComponent = AuditEventDetailComponentMock(
            initialState = AuditEventDetailScreenState.Content(event = event),
        )
        val rootComponent = AuditApiRootComponentMock(
            initialChild = AuditApiRootComponent.Child.Detail(detailComponent),
            initialConfiguration = AuditApiDestination.Detail(event.id.value.toString()),
        )

        setContent {
            ComponentTestHarness {
                AuditApiRootScreen(rootComponent)
            }
        }

        onNodeWithTag(AuditEventDetailTestTags.TITLE).assertIsDisplayed()
    }
}
