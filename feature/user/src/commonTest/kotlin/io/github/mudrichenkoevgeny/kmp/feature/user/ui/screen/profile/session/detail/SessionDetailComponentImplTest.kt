package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.session.detail

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.resume
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.runComponentTest
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.session.userSessionMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.session.DeleteSessionUseCaseMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.session.GetSessionUseCaseMock
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSession
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId
import kotlinx.coroutines.test.advanceTimeBy
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.milliseconds

@InternalApi
class SessionDetailComponentImplTest {

    @Test
    fun init_withProvidedSession_setsContentStateImmediately() = runComponentTest {
        val mockSession = userSessionMock()
        val context = createSessionDetailComponentTestContext(
            session = mockSession,
            isCurrentSession = true
        )
        try {
            advanceTimeBy(100.milliseconds)
            val state = assertIs<SessionDetailScreenState.Content>(context.component.state.value)
            assertEquals(mockSession, state.session)
            assertTrue(state.isCurrentSession)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun init_withSessionId_loadsSessionSuccessfully() = runComponentTest {
        val mockSession = userSessionMock()
        val getSessionUseCase = GetSessionUseCaseMock().apply {
            resultProvider = { AppResult.Success(mockSession) }
        }
        val context = createSessionDetailComponentTestContext(
            session = null,
            sessionId = mockSession.id,
            getSessionUseCase = getSessionUseCase
        )
        try {
            advanceTimeBy(100.milliseconds)
            val state = assertIs<SessionDetailScreenState.Content>(context.component.state.value)
            assertEquals(mockSession, state.session)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun init_withSessionId_whenLoadFails_emitsError() = runComponentTest {
        val getSessionUseCase = GetSessionUseCaseMock().apply {
            resultProvider = { AppResult.Error(CommonError.Unknown()) }
        }
        val context = createSessionDetailComponentTestContext(
            session = null,
            sessionId = UserSessionId.generate(),
            getSessionUseCase = getSessionUseCase
        )
        try {
            advanceTimeBy(100.milliseconds)
            val state = assertIs<SessionDetailScreenState.Error>(context.component.state.value)
            assertIs<CommonError.Unknown>(state.error)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onRevokeSessionClick_success_callsOnBack() = runComponentTest {
        var backCalled = false
        val deleteSessionUseCase = DeleteSessionUseCaseMock().apply {
            resultProvider = { AppResult.Success(Unit) }
        }
        val context = createSessionDetailComponentTestContext(
            session = userSessionMock(),
            deleteSessionUseCase = deleteSessionUseCase,
            onBack = { backCalled = true }
        )
        try {
            advanceTimeBy(100.milliseconds)
            context.component.onRevokeSessionClick()
            advanceTimeBy(100.milliseconds)
            assertTrue(backCalled)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onRevokeSessionClick_error_setsActionError() = runComponentTest {
        val deleteSessionUseCase = DeleteSessionUseCaseMock().apply {
            resultProvider = { AppResult.Error(CommonError.Unknown()) }
        }
        val context = createSessionDetailComponentTestContext(
            session = userSessionMock(),
            deleteSessionUseCase = deleteSessionUseCase
        )
        try {
            advanceTimeBy(100.milliseconds)
            context.component.onRevokeSessionClick()
            advanceTimeBy(100.milliseconds)
            val state = assertIs<SessionDetailScreenState.Content>(context.component.state.value)
            assertIs<CommonError.Unknown>(state.actionError)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onBackClick_callsOnBack() = runComponentTest {
        var backCalled = false
        val context = createSessionDetailComponentTestContext(
            session = userSessionMock(),
            onBack = { backCalled = true }
        )
        try {
            advanceTimeBy(100.milliseconds)
            context.component.onBackClick()
            assertTrue(backCalled)
        } finally {
            context.destroy()
        }
    }
}

private class SessionDetailComponentTestContext(
    val component: SessionDetailComponentImpl,
    val lifecycleRegistry: LifecycleRegistry
) {
    fun destroy() {
        lifecycleRegistry.destroy()
    }
}

@InternalApi
private fun createSessionDetailComponentTestContext(
    session: UserSession? = userSessionMock(),
    sessionId: UserSessionId? = session?.id,
    getSessionUseCase: GetSessionUseCaseMock? = null,
    deleteSessionUseCase: DeleteSessionUseCaseMock? = null,
    isCurrentSession: Boolean = false,
    onBack: () -> Unit = {}
): SessionDetailComponentTestContext {
    val lifecycleRegistry = LifecycleRegistry()
    lifecycleRegistry.resume()
    val componentContext = DefaultComponentContext(lifecycleRegistry)
    val component = SessionDetailComponentImpl(
        componentContext = componentContext,
        session = session,
        sessionId = sessionId,
        getSessionUseCase = getSessionUseCase,
        deleteSessionUseCase = deleteSessionUseCase,
        isCurrentSession = isCurrentSession,
        onBack = onBack
    )
    return SessionDetailComponentTestContext(component, lifecycleRegistry)
}
