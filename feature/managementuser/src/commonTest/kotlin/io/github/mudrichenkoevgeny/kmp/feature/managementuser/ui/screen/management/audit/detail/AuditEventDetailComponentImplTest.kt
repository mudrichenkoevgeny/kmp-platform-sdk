package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.audit.detail

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.runComponentTest
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.audit.domain.model.event.createTestAuditEventPrivate
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.audit.repository.ManagementAuditRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.audit.GetAuditEventUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.user.UserRepositoryMock
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.actor.AuditActorType
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.domain.model.event.AuditEventId
import io.github.mudrichenkoevgeny.shared.foundation.core.audit.navigation.screen.management.audit.detail.ManagementAuditEventDetailParams
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

    private val userRepository = UserRepositoryMock()

    @Test
    fun init_loadsEventSuccessfully() = runComponentTest {
        val event = createTestAuditEventPrivate()
        val repository = ManagementAuditRepositoryMock(getAuditEventResult = AppResult.Success(event))
        val useCase = GetAuditEventUseCase(repository)

        val lifecycle = LifecycleRegistry()
        val componentContext = DefaultComponentContext(lifecycle)
        lifecycle.resume()

        val component = AuditEventDetailComponentImpl(
            componentContext = componentContext,
            params = ManagementAuditEventDetailParams(eventId = event.id.asHexDashString()),
            userRepository = userRepository,
            getAuditEventUseCase = useCase,
            onNavigateToUserDetail = {},
            onNavigateToSessionDetail = {},
            onBack = {}
        )

        advanceTimeBy(100.milliseconds)

        val state = assertIs<AuditEventDetailScreenState.Content>(component.state.value)
        assertEquals(event, state.event)
    }

    @Test
    fun init_emitsErrorState_whenUseCaseFails() = runComponentTest {
        val error = CommonError.Unknown()
        val repository = ManagementAuditRepositoryMock(getAuditEventResult = AppResult.Error(error))
        val useCase = GetAuditEventUseCase(repository)

        val lifecycle = LifecycleRegistry()
        val componentContext = DefaultComponentContext(lifecycle)
        lifecycle.resume()

        val eventId = AuditEventId.generate()
        val component = AuditEventDetailComponentImpl(
            componentContext = componentContext,
            params = ManagementAuditEventDetailParams(eventId = eventId.asHexDashString()),
            userRepository = userRepository,
            getAuditEventUseCase = useCase,
            onNavigateToUserDetail = {},
            onNavigateToSessionDetail = {},
            onBack = {}
        )

        advanceTimeBy(100.milliseconds)

        val state = assertIs<AuditEventDetailScreenState.Error>(component.state.value)
        assertEquals(error, state.error)
    }

    @Test
    fun onSubjectClick_whenSubjectIsUser_invokesNavigateToUserDetail() = runComponentTest {
        val userId = UserId.generate()
        val event = createTestAuditEventPrivate(
            actorType = AuditActorType.USER,
            actorId = userId.value.toHexDashString()
        )
        val repository = ManagementAuditRepositoryMock(getAuditEventResult = AppResult.Success(event))
        val useCase = GetAuditEventUseCase(repository)

        var navigatedUserId: UserId? = null
        val lifecycle = LifecycleRegistry()
        val componentContext = DefaultComponentContext(lifecycle)
        lifecycle.resume()

        val component = AuditEventDetailComponentImpl(
            componentContext = componentContext,
            params = ManagementAuditEventDetailParams(eventId = event.id.asHexDashString()),
            userRepository = userRepository,
            getAuditEventUseCase = useCase,
            onNavigateToUserDetail = { navigatedUserId = it },
            onNavigateToSessionDetail = {},
            onBack = {}
        )

        advanceTimeBy(100.milliseconds)

        component.onSubjectClick()

        assertEquals(userId, navigatedUserId)
    }

    @Test
    fun onResourceClick_whenResourceIsUser_invokesNavigateToUserDetail() = runComponentTest {
        val userId = UserId.generate()
        val event = createTestAuditEventPrivate(
            resource = UserAuditResourceType.USER,
            resourceId = userId.value.toHexDashString()
        )
        val repository = ManagementAuditRepositoryMock(getAuditEventResult = AppResult.Success(event))
        val useCase = GetAuditEventUseCase(repository)

        var navigatedUserId: UserId? = null
        val lifecycle = LifecycleRegistry()
        val componentContext = DefaultComponentContext(lifecycle)
        lifecycle.resume()

        val component = AuditEventDetailComponentImpl(
            componentContext = componentContext,
            params = ManagementAuditEventDetailParams(eventId = event.id.asHexDashString()),
            userRepository = userRepository,
            getAuditEventUseCase = useCase,
            onNavigateToUserDetail = { navigatedUserId = it },
            onNavigateToSessionDetail = {},
            onBack = {}
        )

        advanceTimeBy(100.milliseconds)

        component.onResourceClick()

        assertEquals(userId, navigatedUserId)
    }

    @Test
    fun onResourceClick_whenResourceIsSession_invokesNavigateToSessionDetail() = runComponentTest {
        val sessionId = UserSessionId.generate()
        val event = createTestAuditEventPrivate(
            resource = UserAuditResourceType.SESSION,
            resourceId = sessionId.value.toHexDashString()
        )
        val repository = ManagementAuditRepositoryMock(getAuditEventResult = AppResult.Success(event))
        val useCase = GetAuditEventUseCase(repository)

        var navigatedSessionId: UserSessionId? = null
        val lifecycle = LifecycleRegistry()
        val componentContext = DefaultComponentContext(lifecycle)
        lifecycle.resume()

        val component = AuditEventDetailComponentImpl(
            componentContext = componentContext,
            params = ManagementAuditEventDetailParams(eventId = event.id.asHexDashString()),
            userRepository = userRepository,
            getAuditEventUseCase = useCase,
            onNavigateToUserDetail = {},
            onNavigateToSessionDetail = { navigatedSessionId = it },
            onBack = {}
        )

        advanceTimeBy(100.milliseconds)

        component.onResourceClick()

        assertEquals(sessionId, navigatedSessionId)
    }

    @Test
    fun onBackClick_invokesOnBack() = runComponentTest {
        val repository = ManagementAuditRepositoryMock()
        val useCase = GetAuditEventUseCase(repository)

        var onBackCalled = false
        val lifecycle = LifecycleRegistry()
        val componentContext = DefaultComponentContext(lifecycle)
        lifecycle.resume()

        val component = AuditEventDetailComponentImpl(
            componentContext = componentContext,
            params = ManagementAuditEventDetailParams(eventId = AuditEventId.generate().asHexDashString()),
            userRepository = userRepository,
            getAuditEventUseCase = useCase,
            onNavigateToUserDetail = {},
            onNavigateToSessionDetail = {},
            onBack = { onBackCalled = true }
        )

        component.onBackClick()

        assertEquals(true, onBackCalled)
    }
}
