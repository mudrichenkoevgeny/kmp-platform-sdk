package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.session.list

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.resume
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.listing.ListingConstants
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.runComponentTest
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.session.DeleteAllOtherSessionsUseCaseMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.session.DeleteSessionUseCaseMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.session.GetSessionsUseCaseMock
import io.github.mudrichenkoevgeny.shared.foundation.core.common.domain.model.listing.PagedResult
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId
import io.github.mudrichenkoevgeny.shared.foundation.testing.core.common.domain.model.listing.createTestPagedResult
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.domain.model.session.createTestUserSessionSummary
import io.github.mudrichenkoevgeny.shared.foundation.testing.feature.user.domain.model.session.createTestUserSessionSummaryList
import kotlinx.coroutines.test.advanceUntilIdle
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.time.Duration.Companion.milliseconds

@InternalApi
class SelfSessionListComponentImplTest {

    @Test
    fun init_loadsSessionsSuccessfully() = runComponentTest {
        val sessions = createTestUserSessionSummaryList()
        val getSessionsUseCase = GetSessionsUseCaseMock().apply {
            resultProvider = { _, _ ->
                AppResult.Success(createTestPagedResult(items = sessions))
            }
        }
        val context = createSessionListComponentTestContext(getSessionsUseCase = getSessionsUseCase)
        try {
            advanceUntilIdle()
            val state = assertIs<SelfSessionListScreenState.Content>(context.component.state.value)
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
            advanceUntilIdle()
            val state = assertIs<SelfSessionListScreenState.Error>(context.component.state.value)
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
            advanceUntilIdle()
            val initialCalls = getSessionsUseCase.executeCalls

            context.component.onRefresh()
            advanceUntilIdle()

            assertEquals(initialCalls + 1, getSessionsUseCase.executeCalls)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onRevokeSessionClick_success_reloadsSessions() = runComponentTest {
        val sessions = createTestUserSessionSummaryList()
        val sessionId = sessions.first().id
        val getSessionsUseCase = GetSessionsUseCaseMock().apply {
            resultProvider = { _, _ ->
                AppResult.Success(createTestPagedResult(items = sessions))
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
            advanceUntilIdle()
            val initialLoadCalls = getSessionsUseCase.executeCalls

            context.component.onRevokeSessionClick(sessionId)
            advanceUntilIdle()

            assertEquals(1, deleteSessionUseCase.executeCalls)
            assertEquals(initialLoadCalls + 1, getSessionsUseCase.executeCalls)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onRevokeSessionClick_error_keepsContentWithActionError() = runComponentTest {
        val sessions = createTestUserSessionSummaryList()
        val sessionId = sessions.first().id
        val getSessionsUseCase = GetSessionsUseCaseMock().apply {
            resultProvider = { _, _ ->
                AppResult.Success(createTestPagedResult(items = sessions))
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
            advanceUntilIdle()

            context.component.onRevokeSessionClick(sessionId)
            advanceUntilIdle()

            val state = assertIs<SelfSessionListScreenState.Content>(context.component.state.value)
            assertFalse(state.actionLoading)
            assertIs<CommonError.Unknown>(state.actionError)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onRevokeAllOtherSessionsClick_success_reloadsSessions() = runComponentTest {
        val sessions = createTestUserSessionSummaryList()
        val getSessionsUseCase = GetSessionsUseCaseMock().apply {
            resultProvider = { _, _ ->
                AppResult.Success(createTestPagedResult(items = sessions))
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
            advanceUntilIdle()
            val initialLoadCalls = getSessionsUseCase.executeCalls

            context.component.onRevokeAllOtherSessionsClick()
            advanceUntilIdle()

            assertEquals(1, deleteAllOtherSessionsUseCase.executeCalls)
            assertEquals(initialLoadCalls + 1, getSessionsUseCase.executeCalls)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onLoadNextPage_success_appendsSessions() = runComponentTest {
        val initialSessions = listOf(createTestUserSessionSummary(id = UserSessionId.generate()))
        val nextSessions = listOf(createTestUserSessionSummary(id = UserSessionId.generate()))
        val getSessionsUseCase = GetSessionsUseCaseMock().apply {
            resultProvider = { page, _ ->
                val items = if (page == ListingConstants.INITIAL_PAGE_NUMBER) initialSessions else nextSessions
                AppResult.Success(
                    createTestPagedResult(
                        items = items,
                        totalCount = 2,
                        pageNumber = page ?: 1,
                        pageSize = 1,
                        totalPages = 2
                    )
                )
            }
        }
        val context = createSessionListComponentTestContext(getSessionsUseCase = getSessionsUseCase)
        try {
            advanceUntilIdle()
            val state1 = assertIs<SelfSessionListScreenState.Content>(context.component.state.value)
            assertEquals(initialSessions, state1.paging.items)

            context.component.onLoadNextPage()
            advanceUntilIdle()

            val state2 = assertIs<SelfSessionListScreenState.Content>(context.component.state.value)
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

        context.component = SelfSessionListComponentImpl(
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
        lateinit var component: SelfSessionListComponentImpl
        var onBackCalls: Int = 0

        fun destroy() = lifecycle.destroy()
    }
}
