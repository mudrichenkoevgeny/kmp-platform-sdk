package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.audit.detail

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.runComponentTest
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.audit.domain.model.event.auditEventMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.audit.repository.ManagementAuditRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.audit.GetAuditEventUseCase
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.actor.AuditActorType
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.event.AuditEventId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.audit.resource.UserAuditResourceType
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
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
            onBack = {}
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
            onBack = {}
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
            onBack = { backClicked = true }
        )

        component.onBackClick()
        assertEquals(true, backClicked)
    }

    @Test
    fun onResourceClick_navigatesToSessionDetail_whenResourceIsSession() = runComponentTest {
        val targetSessionId = UserSessionId.generate()
        val event = auditEventMock(
            resource = UserAuditResourceType.SESSION,
            resourceId = targetSessionId.asHexDashString()
        )
        val repository = ManagementAuditRepositoryMock(getAuditEventResult = AppResult.Success(event))
        val useCase = GetAuditEventUseCase(repository)

        val lifecycle = LifecycleRegistry()
        val componentContext = DefaultComponentContext(lifecycle)
        lifecycle.resume()

        var navigatedSessionId: UserSessionId? = null
        val component = AuditEventDetailComponentImpl(
            componentContext = componentContext,
            eventId = event.id,
            getAuditEventUseCase = useCase,
            onNavigateToSessionDetail = { navigatedSessionId = it },
            onBack = {}
        )

        advanceTimeBy(100.milliseconds)
        component.onResourceClick()
        assertEquals(targetSessionId, navigatedSessionId)
    }

    @Test
    fun onSubjectClick_navigatesToUserDetail_whenActorIsOtherUser() = runComponentTest {
        val actorUserId = UserId.generate()
        val currentUserId = UserId.generate()
        val event = auditEventMock(
            actorId = actorUserId.asHexDashString(),
            actorType = AuditActorType.USER
        )
        val repository = ManagementAuditRepositoryMock(getAuditEventResult = AppResult.Success(event))
        val useCase = GetAuditEventUseCase(repository)

        val lifecycle = LifecycleRegistry()
        val componentContext = DefaultComponentContext(lifecycle)
        lifecycle.resume()

        var navigatedUserId: UserId? = null
        val component = AuditEventDetailComponentImpl(
            componentContext = componentContext,
            eventId = event.id,
            getAuditEventUseCase = useCase,
            currentUserId = currentUserId,
            onNavigateToUserDetail = { navigatedUserId = it },
            onBack = {}
        )

        advanceTimeBy(100.milliseconds)
        component.onSubjectClick()
        assertEquals(actorUserId, navigatedUserId)
    }

    @Test
    fun onSubjectClick_navigatesToProfile_whenActorIsSelfUser() = runComponentTest {
        val selfUserId = UserId.generate()
        val event = auditEventMock(
            actorId = selfUserId.asHexDashString(),
            actorType = AuditActorType.USER
        )
        val repository = ManagementAuditRepositoryMock(getAuditEventResult = AppResult.Success(event))
        val useCase = GetAuditEventUseCase(repository)

        val lifecycle = LifecycleRegistry()
        val componentContext = DefaultComponentContext(lifecycle)
        lifecycle.resume()

        var profileNavigated = false
        val component = AuditEventDetailComponentImpl(
            componentContext = componentContext,
            eventId = event.id,
            getAuditEventUseCase = useCase,
            currentUserId = selfUserId,
            onNavigateToProfile = { profileNavigated = true },
            onBack = {}
        )

        advanceTimeBy(100.milliseconds)
        component.onSubjectClick()
        assertEquals(true, profileNavigated)
    }
}