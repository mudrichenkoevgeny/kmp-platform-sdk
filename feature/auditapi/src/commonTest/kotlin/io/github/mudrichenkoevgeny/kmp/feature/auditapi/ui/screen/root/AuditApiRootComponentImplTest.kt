package io.github.mudrichenkoevgeny.kmp.feature.auditapi.ui.screen.root

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.domain.model.listing.pagedResultMock
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.runComponentTest
import io.github.mudrichenkoevgeny.kmp.feature.auditapi.repository.ManagementAuditRepository
import io.github.mudrichenkoevgeny.kmp.feature.auditapi.ui.screen.detail.AuditEventDetailComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.auditapi.ui.screen.events.AuditEventsComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.auditapi.usecase.GetAuditEventUseCase
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
class AuditApiRootComponentImplTest {

    @Test
    fun initialStack_startsAtMain() = runComponentTest {
        val event = AuditEvent(
            id = AuditEventId.generate(),
            actorType = AuditActorType.USER,
            action = UserAuditActionType.MANAGEMENT_UPDATE_USER,
            resource = UserAuditResourceType.USER,
            status = AuditStatus.SUCCESS,
            createdAt = Instant.fromEpochMilliseconds(0),
        )
        val repository = object : ManagementAuditRepository {
            override suspend fun getAuditEvents(
                pageNumber: Int?,
                pageSize: Int?,
                sortBy: AuditSortValues.AuditEventSortBy?,
                sortOrder: SortOrder?
            ) = AppResult.Success(pagedResultMock(listOf(event)))

            override suspend fun getAuditEvent(eventId: String) = AppResult.Success(event)
        }
        val getAuditEventsUseCase = GetAuditEventsUseCase(repository)
        val getAuditEventUseCase = GetAuditEventUseCase(repository)

        val lifecycle = LifecycleRegistry()
        val componentContext = DefaultComponentContext(lifecycle)
        lifecycle.resume()

        var backClicked = false
        val rootComponent = AuditApiRootComponentImpl(
            componentContext = componentContext,
            getAuditEventsUseCase = getAuditEventsUseCase,
            getAuditEventUseCase = getAuditEventUseCase,
            onBack = { backClicked = true },
        )

        advanceTimeBy(100.milliseconds)
        val currentChild = rootComponent.stack.value.active.instance
        assertIs<AuditApiRootComponent.Child.Main>(currentChild)
        assertEquals(AuditApiDestination.Main, rootComponent.stack.value.active.configuration)

        rootComponent.onBackClick()
        assertEquals(true, backClicked)
    }

    @Test
    fun navigateToDetail_pushesDetailScreenAndPopsBack() = runComponentTest {
        val event = AuditEvent(
            id = AuditEventId.generate(),
            actorType = AuditActorType.USER,
            action = UserAuditActionType.MANAGEMENT_UPDATE_USER,
            resource = UserAuditResourceType.USER,
            status = AuditStatus.SUCCESS,
            createdAt = Instant.fromEpochMilliseconds(0),
        )
        val repository = object : ManagementAuditRepository {
            override suspend fun getAuditEvents(
                pageNumber: Int?,
                pageSize: Int?,
                sortBy: AuditSortValues.AuditEventSortBy?,
                sortOrder: SortOrder?
            ) = AppResult.Success(pagedResultMock(listOf(event)))

            override suspend fun getAuditEvent(eventId: String) = AppResult.Success(event)
        }
        val getAuditEventsUseCase = GetAuditEventsUseCase(repository)
        val getAuditEventUseCase = GetAuditEventUseCase(repository)

        val lifecycle = LifecycleRegistry()
        val componentContext = DefaultComponentContext(lifecycle)
        lifecycle.resume()

        val rootComponent = AuditApiRootComponentImpl(
            componentContext = componentContext,
            getAuditEventsUseCase = getAuditEventsUseCase,
            getAuditEventUseCase = getAuditEventUseCase,
            onBack = {},
        )

        advanceTimeBy(100.milliseconds)
        val mainChild = assertIs<AuditApiRootComponent.Child.Main>(rootComponent.stack.value.active.instance)
        val eventsComponent = assertIs<AuditEventsComponentImpl>(mainChild.component)

        eventsComponent.onEventClick(event.id)

        val detailChild = assertIs<AuditApiRootComponent.Child.Detail>(rootComponent.stack.value.active.instance)
        assertEquals(AuditApiDestination.Detail(event.id.value.toString()), rootComponent.stack.value.active.configuration)

        val detailComponent = assertIs<AuditEventDetailComponentImpl>(detailChild.component)
        detailComponent.onBackClick()

        assertIs<AuditApiRootComponent.Child.Main>(rootComponent.stack.value.active.instance)
        assertEquals(AuditApiDestination.Main, rootComponent.stack.value.active.configuration)
    }
}
