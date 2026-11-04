package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.session.userlist

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.runComponentTest
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.repository.session.ManagementSessionRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.session.list.datasource.ManagementSessionListDataSource
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session.ManagementDeleteAllUserSessionsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session.ManagementDeleteSessionUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session.ManagementGetSessionsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session.ManagementGetUserSessionsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.session.list.SessionListComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.session.list.SessionListScreenState
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionSummary
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.common.domain.model.listing.createTestPagedResult
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.domain.model.session.createTestUserSessionSummary
import kotlinx.coroutines.test.advanceUntilIdle
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class UserSessionListComponentImplTest {

    @Test
    fun init_loadsSessionsSuccessfully() = runComponentTest {
        val session = createTestUserSessionSummary()
        val context = createTestContext(sessions = listOf(session))

        advanceUntilIdle()
        val state = assertIs<SessionListScreenState.Content>(context.component.state.value)
        assertEquals(listOf(session), state.paging.items)
    }

    @Test
    fun init_emitsErrorState_whenUseCaseFails() = runComponentTest {
        val repository = ManagementSessionRepositoryMock()
        val error = CommonError.Unknown()
        repository.getUserSessionsResultProvider = { AppResult.Error(error) }
        val context = createTestContext(repository = repository)

        advanceUntilIdle()
        val state = assertIs<SessionListScreenState.Error>(context.component.state.value)
        assertEquals(error, state.error)
    }

    @Test
    fun onRefresh_reloadsSessions() = runComponentTest {
        val context = createTestContext()
        advanceUntilIdle()

        context.component.onRefresh()

        advanceUntilIdle()
        assertIs<SessionListScreenState.Content>(context.component.state.value)
    }

    @Test
    fun onBackClick_invokesCallback() = runComponentTest {
        val context = createTestContext()
        context.component.onBackClick()
        assertEquals(1, context.onBackCalls)
    }

    private fun createTestContext(
        userId: UserId = UserId.generate(),
        sessions: List<UserSessionSummary>? = null,
        repository: ManagementSessionRepositoryMock = ManagementSessionRepositoryMock()
    ): TestContext {
        if (sessions != null) {
            repository.getUserSessionsResultProvider = { AppResult.Success(createTestPagedResult(sessions)) }
        }

        val lifecycle = LifecycleRegistry()
        lifecycle.resume()

        val getGlobalUseCase = ManagementGetSessionsUseCase(repository)
        val getUserUseCase = ManagementGetUserSessionsUseCase(repository)
        val deleteSessionUseCase = ManagementDeleteSessionUseCase(repository)
        val deleteAllSessionsUseCase = ManagementDeleteAllUserSessionsUseCase(repository)
        val context = TestContext()

        context.component = SessionListComponentImpl(
            componentContext = DefaultComponentContext(lifecycle),
            dataSource = ManagementSessionListDataSource(
                managementGetSessionsUseCase = getGlobalUseCase,
                managementGetUserSessionsUseCase = getUserUseCase,
                managementDeleteSessionUseCase = deleteSessionUseCase,
                managementDeleteAllUserSessionsUseCase = deleteAllSessionsUseCase
            ),
            initialUserId = userId.asHexDashString(),
            includeFilterPanel = false,
            onBack = { context.onBackCalls++ }
        )

        return context
    }

    private class TestContext {
        lateinit var component: SessionListComponentImpl
        var onBackCalls = 0
    }
}
