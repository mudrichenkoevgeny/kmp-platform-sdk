package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.audit.list

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.runComponentTest
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.common.domain.model.listing.createTestPagedResult
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.audit.repository.ManagementAuditRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.audit.GetAuditEventsUseCase
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.audit.domain.model.event.createTestAuditEventSummaryList
import kotlinx.coroutines.test.advanceUntilIdle
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class AuditEventListComponentImplTest {

    @Test
    fun init_loadsEventsSuccessfully() = runComponentTest {
        val events = createTestAuditEventSummaryList()
        val pagedResult = createTestPagedResult(events)
        val repository = ManagementAuditRepositoryMock(getAuditEventsResult = AppResult.Success(pagedResult))
        val useCase = GetAuditEventsUseCase(repository)

        val lifecycle = LifecycleRegistry()
        val componentContext = DefaultComponentContext(lifecycle)
        lifecycle.resume()

        val component = AuditEventListComponentImpl(
            componentContext = componentContext,
            getAuditEventsUseCase = useCase,
            onNavigateToEventDetail = {},
            onBack = {}
        )

        advanceUntilIdle()
        val state = assertIs<AuditEventListScreenState.Content>(component.state.value)
        assertEquals(events, state.paging.items)
    }
}