package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.audit.list

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.domain.model.listing.pagedResultMock
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.runComponentTest
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.audit.domain.model.event.auditEventMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.audit.repository.ManagementAuditRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.audit.GetAuditEventsUseCase
import kotlinx.coroutines.test.advanceTimeBy
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.time.Duration.Companion.milliseconds

@InternalApi
class AuditEventListComponentImplTest {

    @Test
    fun init_loadsEventsSuccessfully() = runComponentTest {
        val event = auditEventMock()
        val pagedResult = pagedResultMock(listOf(event))
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

        advanceTimeBy(100.milliseconds)
        val state = assertIs<AuditEventListScreenState.Content>(component.state.value)
        assertEquals(listOf(event), state.paging.items)
    }
}