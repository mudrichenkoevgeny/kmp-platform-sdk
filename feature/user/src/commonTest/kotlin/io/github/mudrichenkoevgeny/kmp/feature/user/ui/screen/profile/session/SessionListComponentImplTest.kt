package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.session

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.resume
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.ListingConstants
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.runComponentTest
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.session.userSessionMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.session.DeleteAllOtherSessionsUseCaseMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.session.DeleteSessionUseCaseMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.session.GetSessionsUseCaseMock
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId
import kotlinx.coroutines.test.advanceTimeBy
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.time.Duration.Companion.milliseconds

@InternalApi
class SessionListComponentImplTest {

    @Test
    fun init_loadsSessionsSuccessfully() = runComponentTest {
        val sessions = listOf(userSessionMock())
        val getSessionsUseCase = GetSessionsUseCaseMock().apply {
            resultProvider = { _, _ ->
                AppResult.Success(
                    PagedResult(
                        items = sessions,
                        totalCount = sessions.size.toLong(),
                        pageNumber = 1,
                        pageSize = 10,
                        totalPages = 1
                    )
                )
            }
        }
        val context = createSessionListComponentTestContext(getSessionsUseCase = getSessionsUseCase)
        try {
            advanceTimeBy(100.milliseconds)
            val state = assertIs<SessionListScreenState.Content>(context.component.state.value)
            assertEquals(sessions, state.paging.items)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun init_whenLoadFails_emitsError() = runComponentTest {
        val getSessionsUseCase = GetSessionsUseCaseMock().apply {
            resultProvider = { _, _ -> AppResult.Error(CommonError.Unknown()) }
        }
        val context = createSessionListComponentTestContext(getSessionsUseCase = getSessionsUseCase)
        try {
            advanceTimeBy(100.milliseconds)
            val state = assertIs<SessionListScreenState.Error>(context.component.state.value)
            assertIs<CommonError.Unknown>(state.error)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onRefresh_reloadsSessions() = runComponentTest {
        val getSessionsUseCase = GetSessionsUseCaseMock()
        val context = createSessionListComponentTestContext(getSessionsUseCase = getSessionsUseCase)
        try {
            advanceTimeBy(100.milliseconds)
            val initialCalls = getSessionsUseCase.executeCalls

            context.component.onRefresh()
            advanceTimeBy(100.milliseconds)

            assertEquals(initialCalls + 1, getSessionsUseCase.executeCalls)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onRevokeSessionClick_success_reloadsSessions() = runComponentTest {
        val sessions = listOf(userSessionMock())
        val sessionId = sessions.first().id
        val getSessionsUseCase = GetSessionsUseCaseMock().apply {
            resultProvider = { _, _ ->
                AppResult.Success(
                    PagedResult(
                        items = sessions,
                        totalCount = sessions.size.toLong(),
                        pageNumber = 1,
                        pageSize = 10,
                        totalPages = 1
                    )
                )
            }
        }
        val deleteSessionUseCase = DeleteSessionUseCaseMock().apply {
            resultProvider = { AppResult.Success(Unit) }
        }
        val context = createSessionListComponentTestContext(
            getSessionsUseCase = getSessionsUseCase,
            deleteSessionUseCase = deleteSessionUseCase
        )
        try {
            advanceTimeBy(100.milliseconds)
            val initialLoadCalls = getSessionsUseCase.executeCalls

            context.component.onRevokeSessionClick(sessionId)
            advanceTimeBy(100.milliseconds)

            assertEquals(1, deleteSessionUseCase.executeCalls)
            assertEquals(initialLoadCalls + 1, getSessionsUseCase.executeCalls)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onRevokeSessionClick_error_keepsContentWithActionError() = runComponentTest {
        val sessions = listOf(userSessionMock())
        val sessionId = sessions.first().id
        val getSessionsUseCase = GetSessionsUseCaseMock().apply {
            resultProvider = { _, _ ->
                AppResult.Success(
                    PagedResult(
                        items = sessions,
                        totalCount = sessions.size.toLong(),
                        pageNumber = 1,
                        pageSize = 10,
                        totalPages = 1
                    )
                )
            }
        }
        val deleteSessionUseCase = DeleteSessionUseCaseMock().apply {
            resultProvider = { AppResult.Error(CommonError.Unknown()) }
        }
        val context = createSessionListComponentTestContext(
            getSessionsUseCase = getSessionsUseCase,
            deleteSessionUseCase = deleteSessionUseCase
        )
        try {
            advanceTimeBy(100.milliseconds)

            context.component.onRevokeSessionClick(sessionId)
            advanceTimeBy(100.milliseconds)

            val state = assertIs<SessionListScreenState.Content>(context.component.state.value)
            assertFalse(state.actionLoading)
            assertIs<CommonError.Unknown>(state.actionError)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onRevokeAllOtherSessionsClick_success_reloadsSessions() = runComponentTest {
        val sessions = listOf(userSessionMock(), userSessionMock())
        val getSessionsUseCase = GetSessionsUseCaseMock().apply {
            resultProvider = { _, _ ->
                AppResult.Success(
                    PagedResult(
                        items = sessions,
                        totalCount = sessions.size.toLong(),
                        pageNumber = 1,
                        pageSize = 10,
                        totalPages = 1
                    )
                )
            }
        }
        val deleteAllOtherSessionsUseCase = DeleteAllOtherSessionsUseCaseMock().apply {
            resultProvider = { AppResult.Success(Unit) }
        }
        val context = createSessionListComponentTestContext(
            getSessionsUseCase = getSessionsUseCase,
            deleteAllOtherSessionsUseCase = deleteAllOtherSessionsUseCase
        )
        try {
            advanceTimeBy(100.milliseconds)
            val initialLoadCalls = getSessionsUseCase.executeCalls

            context.component.onRevokeAllOtherSessionsClick()
            advanceTimeBy(100.milliseconds)

            assertEquals(1, deleteAllOtherSessionsUseCase.executeCalls)
            assertEquals(initialLoadCalls + 1, getSessionsUseCase.executeCalls)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onLoadNextPage_success_appendsSessions() = runComponentTest {
        val initialSessions = listOf(userSessionMock())
        val nextSessions = listOf(userSessionMock())
        val getSessionsUseCase = GetSessionsUseCaseMock().apply {
            resultProvider = { page, _ ->
                val items = if (page == ListingConstants.INITIAL_PAGE_NUMBER) initialSessions else nextSessions
                AppResult.Success(
                    PagedResult(
                        items = items,
                        totalCount = 2,
                        pageNumber = page,
                        pageSize = 1,
                        totalPages = 2
                    )
                )
            }
        }
        val context = createSessionListComponentTestContext(getSessionsUseCase = getSessionsUseCase)
        try {
            advanceTimeBy(100.milliseconds)
            val state1 = assertIs<SessionListScreenState.Content>(context.component.state.value)
            assertEquals(initialSessions, state1.paging.items)

            context.component.onLoadNextPage()
            advanceTimeBy(100.milliseconds)

            val state2 = assertIs<SessionListScreenState.Content>(context.component.state.value)
            assertEquals(initialSessions + nextSessions, state2.paging.items)
            assertEquals(2, getSessionsUseCase.executeCalls)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onBackClick_invokesOnBack() = runComponentTest {
        val context = createSessionListComponentTestContext()
        try {
            context.component.onBackClick()
            assertEquals(1, context.onBackCalls)
        } finally {
            context.destroy()
        }
    }

    private fun createSessionListComponentTestContext(
        getSessionsUseCase: GetSessionsUseCaseMock = GetSessionsUseCaseMock(),
        deleteSessionUseCase: DeleteSessionUseCaseMock = DeleteSessionUseCaseMock(),
        deleteAllOtherSessionsUseCase: DeleteAllOtherSessionsUseCaseMock = DeleteAllOtherSessionsUseCaseMock()
    ): SessionListComponentTestContext {
        val lifecycle = LifecycleRegistry()
        lifecycle.resume()

        val context = SessionListComponentTestContext(lifecycle)

        context.component = SessionListComponentImpl(
            componentContext = DefaultComponentContext(lifecycle),
            getSessionsUseCase = getSessionsUseCase,
            deleteSessionUseCase = deleteSessionUseCase,
            deleteAllOtherSessionsUseCase = deleteAllOtherSessionsUseCase,
            onBack = { context.onBackCalls++ }
        )

        return context
    }

    private class SessionListComponentTestContext(
        val lifecycle: LifecycleRegistry
    ) {
        lateinit var component: SessionListComponentImpl
        var onBackCalls: Int = 0

        fun destroy() = lifecycle.destroy()
    }
}
