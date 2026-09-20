package io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit.ui.screen.detail

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.runComponentTest
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit.mock.domain.model.event.auditEventMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit.mock.repository.ManagementAuditRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.audit.usecase.GetAuditEventUseCase
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.event.AuditEventId
import kotlinx.coroutines.test.advanceTimeBy
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.time.Duration.Companion.milliseconds

@InternalApi
class AuditEventDetailComponentImplTest {

    @Test
    fun init_loadsEventSuccessfully() = runComponentTest {
        val event = auditEventMock()
        val repository = ManagementAuditRepositoryMock(getAuditEventResult = AppResult.Success(event))
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
        val repository = ManagementAuditRepositoryMock(getAuditEventResult = AppResult.Error(CommonError.Unknown()))
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
        val repository = ManagementAuditRepositoryMock(getAuditEventResult = AppResult.Error(CommonError.Unknown()))
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
