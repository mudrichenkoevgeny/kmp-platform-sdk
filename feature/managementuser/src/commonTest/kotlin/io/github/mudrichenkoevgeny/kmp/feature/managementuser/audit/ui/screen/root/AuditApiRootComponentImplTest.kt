package io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit.ui.screen.root

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.domain.model.listing.pagedResultMock
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.runComponentTest
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit.mock.domain.model.event.auditEventMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit.mock.repository.ManagementAuditRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit.ui.screen.detail.AuditEventDetailComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit.ui.screen.events.AuditEventsComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit.usecase.GetAuditEventUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit.usecase.GetAuditEventsUseCase
import kotlinx.coroutines.test.advanceTimeBy
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.time.Duration.Companion.milliseconds

@InternalApi
class AuditApiRootComponentImplTest {

    @Test
    fun initialStack_startsAtMain() = runComponentTest {
        val event = auditEventMock()
        val repository = ManagementAuditRepositoryMock(
            getAuditEventsResult = AppResult.Success(pagedResultMock(listOf(event))),
            getAuditEventResult = AppResult.Success(event)
        )
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
        val event = auditEventMock()
        val repository = ManagementAuditRepositoryMock(
            getAuditEventsResult = AppResult.Success(pagedResultMock(listOf(event))),
            getAuditEventResult = AppResult.Success(event)
        )
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
