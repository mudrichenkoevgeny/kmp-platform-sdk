package io.github.mudrichenkoevgeny.kmp.feature.auditapi.ui.screen.events

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
import io.github.mudrichenkoevgeny.kmp.feature.auditapi.mock.ui.screen.events.AuditEventsComponentMock
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
import kotlin.test.assertEquals
import kotlin.time.Instant

@InternalApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [ROBOLECTRIC_SDK], application = Application::class)
class AuditEventsScreenTest {

    @Test
    fun loading_showsIndeterminateProgressAfterDefaultDelay() = runComposeUiTest {
        val component = AuditEventsComponentMock(AuditEventsScreenState.Loading)
        setContent {
            ComponentTestHarness {
                AuditEventsScreen(component)
            }
        }
        mainClock.autoAdvance = false
        mainClock.advanceTimeBy(FullscreenLoadingConfig.DELAY_MILLIS + LOADING_EXTRA_DELAY_MS)
        onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate)).assertExists()
    }

    @Test
    fun content_displaysElements() = runComposeUiTest {
        val event = AuditEvent(
            id = AuditEventId.generate(),
            actorType = AuditActorType.USER,
            action = UserAuditActionType.MANAGEMENT_UPDATE_USER,
            resource = UserAuditResourceType.USER,
            status = AuditStatus.SUCCESS,
            createdAt = Instant.fromEpochMilliseconds(0),
        )
        val component = AuditEventsComponentMock(
            AuditEventsScreenState.Content(
                paging = PaginationState(
                    items = listOf(event),
                    isInitialLoading = false,
                ),
            ),
        )
        setContent {
            ComponentTestHarness {
                AuditEventsScreen(component)
            }
        }
        onNodeWithTag(AuditEventsTestTags.TITLE).assertIsDisplayed()
        onNodeWithTag(AuditEventsTestTags.BACK_BUTTON).assertIsDisplayed()
        onNodeWithTag(AuditEventsTestTags.REFRESH_BUTTON).assertIsDisplayed()
        onNodeWithTag(AuditEventsTestTags.EVENT_LIST).assertIsDisplayed()
    }

    @Test
    fun content_clickBack_invokesCallback() = runComposeUiTest {
        val component = AuditEventsComponentMock(AuditEventsScreenState.Content(paging = PaginationState(emptyList(), isInitialLoading = false)))
        setContent {
            ComponentTestHarness {
                AuditEventsScreen(component)
            }
        }
        onNodeWithTag(AuditEventsTestTags.BACK_BUTTON).performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.backCalls)
    }

    @Test
    fun content_clickRefresh_invokesCallback() = runComposeUiTest {
        val component = AuditEventsComponentMock(AuditEventsScreenState.Content(paging = PaginationState(emptyList(), isInitialLoading = false)))
        setContent {
            ComponentTestHarness {
                AuditEventsScreen(component)
            }
        }
        onNodeWithTag(AuditEventsTestTags.REFRESH_BUTTON).performClick()
        assertEquals(EXPECTED_SINGLE_CALLBACK, component.refreshCalls)
    }

    @Test
    fun error_displaysErrorText() = runComposeUiTest {
        val component = AuditEventsComponentMock(AuditEventsScreenState.Error(CommonError.Unknown()))
        setContent {
            ComponentTestHarness {
                AuditEventsScreen(component)
            }
        }
        onNodeWithTag(AuditEventsTestTags.GLOBAL_ERROR_TEXT).assertIsDisplayed()
    }

    private companion object {
        const val LOADING_EXTRA_DELAY_MS = 50L
        const val EXPECTED_SINGLE_CALLBACK = 1
    }
}
