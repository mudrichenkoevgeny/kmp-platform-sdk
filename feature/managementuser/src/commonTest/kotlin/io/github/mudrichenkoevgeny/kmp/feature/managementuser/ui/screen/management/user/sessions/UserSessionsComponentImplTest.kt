package io.github.mudrichenkoevgeny.kmp.feature.managementuser.ui.screen.management.user.sessions

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.mock.domain.model.listing.pagedResultMock
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.runComponentTest
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.mock.repository.session.ManagementSessionRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.managementuser.usecase.session.ManagementGetSessionsUseCase
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.session.userSessionMock
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSession
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import kotlinx.coroutines.test.advanceUntilIdle
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@InternalApi
class UserSessionsComponentImplTest {

    @Test
    fun init_loadsSessionsSuccessfully() = runComponentTest {
        val session = userSessionMock()
        val context = createTestContext(sessions = listOf(session))

        advanceUntilIdle()
        val state = assertIs<UserSessionsScreenState.Content>(context.component.state.value)
        assertEquals(listOf(session), state.paging.items)
    }

    @Test
    fun init_emitsErrorState_whenUseCaseFails() = runComponentTest {
        val repository = ManagementSessionRepositoryMock()
        val error = CommonError.Unknown()
        repository.getSessionsResultProvider = { AppResult.Error(error) }
        val context = createTestContext(repository = repository)

        advanceUntilIdle()
        val state = assertIs<UserSessionsScreenState.Error>(context.component.state.value)
        assertEquals(error, state.error)
    }

    @Test
    fun onRefresh_reloadsSessions() = runComponentTest {
        val context = createTestContext()
        advanceUntilIdle()

        context.component.onRefresh()

        advanceUntilIdle()
        assertIs<UserSessionsScreenState.Content>(context.component.state.value)
    }

    @Test
    fun onBackClick_invokesCallback() = runComponentTest {
        val context = createTestContext()
        context.component.onBackClick()
        assertEquals(1, context.onBackCalls)
    }

    private fun createTestContext(
        userId: UserId = UserId.generate(),
        sessions: List<UserSession>? = null,
        repository: ManagementSessionRepositoryMock = ManagementSessionRepositoryMock(),
    ): TestContext {
        if (sessions != null) {
            repository.getSessionsResultProvider = { AppResult.Success(pagedResultMock(sessions)) }
        }

        val lifecycle = LifecycleRegistry()
        lifecycle.resume()

        val useCase = ManagementGetSessionsUseCase(repository)
        val context = TestContext()

        context.component = UserSessionsComponentImpl(
            componentContext = DefaultComponentContext(lifecycle),
            userId = userId,
            managementGetSessionsUseCase = useCase,
            onBack = { context.onBackCalls++ },
        )

        return context
    }

    private class TestContext {
        lateinit var component: UserSessionsComponentImpl
        var onBackCalls = 0
    }
}
