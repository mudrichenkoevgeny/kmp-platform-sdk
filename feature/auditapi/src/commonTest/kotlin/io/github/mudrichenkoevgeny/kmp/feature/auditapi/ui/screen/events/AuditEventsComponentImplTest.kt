package io.github.mudrichenkoevgeny.kmp.feature.auditapi.ui.screen.events

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.domain.model.listing.pagedResultMock
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.runComponentTest
import io.github.mudrichenkoevgeny.kmp.feature.auditapi.repository.ManagementAuditRepository
import io.github.mudrichenkoevgeny.kmp.feature.auditapi.usecase.GetAuditEventsUseCase
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.actor.AuditActorType
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.event.AuditEvent
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.event.AuditEventId
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.listing.AuditSortValues
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.status.AuditStatus
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.SortOrder
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.audit.action.UserAuditActionType
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.audit.resource.UserAuditResourceType
import kotlinx.coroutines.test.advanceTimeBy
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Instant

@InternalApi
class AuditEventsComponentImplTest {

    @Test
    fun init_loadsEventsSuccessfully() = runComponentTest {
        val event = AuditEvent(
            id = AuditEventId.generate(),
            actorType = AuditActorType.USER,
            action = UserAuditActionType.MANAGEMENT_UPDATE_USER,
            resource = UserAuditResourceType.USER,
            status = AuditStatus.SUCCESS,
            createdAt = Instant.fromEpochMilliseconds(0),
        )
        val pagedResult = pagedResultMock(listOf(event))
        val repository = object : ManagementAuditRepository {
            override suspend fun getAuditEvents(
                pageNumber: Int?,
                pageSize: Int?,
                sortBy: AuditSortValues.AuditEventSortBy?,
                sortOrder: SortOrder?
            ) = AppResult.Success(pagedResult)

            override suspend fun getAuditEvent(eventId: String) = AppResult.Success(event)
        }
        val useCase = GetAuditEventsUseCase(repository)

        val lifecycle = LifecycleRegistry()
        val componentContext = DefaultComponentContext(lifecycle)
        lifecycle.resume()

        val component = AuditEventsComponentImpl(
            componentContext = componentContext,
            getAuditEventsUseCase = useCase,
            onNavigateToEventDetail = {},
            onBack = {},
        )

        advanceTimeBy(100.milliseconds)
        val state = assertIs<AuditEventsScreenState.Content>(component.state.value)
        assertEquals(listOf(event), state.paging.items)
    }
}
