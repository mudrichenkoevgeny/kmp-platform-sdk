package io.github.mudrichenkoevgeny.kmp.feature.auditapi.ui.screen.root

import android.app.Application
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.runComposeUiTest
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.PaginationState
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ComponentTestHarness
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.ROBOLECTRIC_SDK
import io.github.mudrichenkoevgeny.kmp.feature.auditapi.mock.ui.screen.detail.AuditEventDetailComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.auditapi.mock.ui.screen.events.AuditEventsComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.auditapi.mock.ui.screen.root.AuditApiRootComponentMock
import io.github.mudrichenkoevgeny.kmp.feature.auditapi.ui.screen.detail.AuditEventDetailScreenState
import io.github.mudrichenkoevgeny.kmp.feature.auditapi.ui.screen.detail.AuditEventDetailTestTags
import io.github.mudrichenkoevgeny.kmp.feature.auditapi.ui.screen.events.AuditEventsScreenState
import io.github.mudrichenkoevgeny.kmp.feature.auditapi.ui.screen.events.AuditEventsTestTags
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.actor.AuditActorType
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.event.AuditEvent
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.event.AuditEventId
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.status.AuditStatus
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.audit.action.UserAuditActionType
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.audit.resource.UserAuditResourceType
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.Test
import kotlin.time.Instant

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
        val event = AuditEvent(
            id = AuditEventId.generate(),
            actorType = AuditActorType.USER,
            action = UserAuditActionType.MANAGEMENT_UPDATE_USER,
            resource = UserAuditResourceType.USER,
            status = AuditStatus.SUCCESS,
            createdAt = Instant.fromEpochMilliseconds(0),
        )
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
