package io.github.mudrichenkoevgeny.kmp.feature.auditapi.ui.screen.detail

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.runComponentTest
import io.github.mudrichenkoevgeny.kmp.feature.auditapi.repository.ManagementAuditRepository
import io.github.mudrichenkoevgeny.kmp.feature.auditapi.usecase.GetAuditEventUseCase
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
class AuditEventDetailComponentImplTest {

    @Test
    fun init_loadsEventSuccessfully() = runComponentTest {
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
            ) = throw NotImplementedError()

            override suspend fun getAuditEvent(eventId: String) = AppResult.Success(event)
        }
        val useCase = GetAuditEventUseCase(repository)

        val lifecycle = LifecycleRegistry()
        val componentContext = DefaultComponentContext(lifecycle)
        lifecycle.resume()

        val component = AuditEventDetailComponentImpl(
            componentContext = componentContext,
            eventId = event.id,
            getAuditEventUseCase = useCase,
            onBack = {},
        )

        advanceTimeBy(100.milliseconds)
        val state = assertIs<AuditEventDetailScreenState.Content>(component.state.value)
        assertEquals(event, state.event)
    }

    @Test
    fun init_emitsErrorState_whenUseCaseFails() = runComponentTest {
        val repository = object : ManagementAuditRepository {
            override suspend fun getAuditEvents(
                pageNumber: Int?,
                pageSize: Int?,
                sortBy: AuditSortValues.AuditEventSortBy?,
                sortOrder: SortOrder?
            ) = throw NotImplementedError()

            override suspend fun getAuditEvent(eventId: String) = AppResult.Error(CommonError.Unknown())
        }
        val useCase = GetAuditEventUseCase(repository)

        val lifecycle = LifecycleRegistry()
        val componentContext = DefaultComponentContext(lifecycle)
        lifecycle.resume()

        val component = AuditEventDetailComponentImpl(
            componentContext = componentContext,
            eventId = AuditEventId.generate(),
            getAuditEventUseCase = useCase,
            onBack = {},
        )

        advanceTimeBy(100.milliseconds)
        assertIs<AuditEventDetailScreenState.Error>(component.state.value)
    }

    @Test
    fun onBackClick_invokesOnBackCallback() = runComponentTest {
        val repository = object : ManagementAuditRepository {
            override suspend fun getAuditEvents(
                pageNumber: Int?,
                pageSize: Int?,
                sortBy: AuditSortValues.AuditEventSortBy?,
                sortOrder: SortOrder?
            ) = throw NotImplementedError()

            override suspend fun getAuditEvent(eventId: String) = AppResult.Error(CommonError.Unknown())
        }
        val useCase = GetAuditEventUseCase(repository)

        val lifecycle = LifecycleRegistry()
        val componentContext = DefaultComponentContext(lifecycle)
        lifecycle.resume()

        var backClicked = false
        val component = AuditEventDetailComponentImpl(
            componentContext = componentContext,
            eventId = AuditEventId.generate(),
            getAuditEventUseCase = useCase,
            onBack = { backClicked = true },
        )

        component.onBackClick()
        assertEquals(true, backClicked)
    }
}
