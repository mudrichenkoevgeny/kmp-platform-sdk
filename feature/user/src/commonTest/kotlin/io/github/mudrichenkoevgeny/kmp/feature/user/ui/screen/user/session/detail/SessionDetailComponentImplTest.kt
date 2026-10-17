package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.user.session.detail

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.resume
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.runComponentTest
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.session.userSessionPrivateMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.session.DeleteSessionUseCaseMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.session.GetSessionUseCaseMock
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.session.UserSessionPrivate
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.domain.model.user.UserId
import io.github.mudrichenkoevgeny.shared.foundation.feature.user.navigation.screen.base.user.session.detail.SessionDetailParams
import kotlinx.coroutines.test.advanceTimeBy
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.time.Duration.Companion.milliseconds

@InternalApi
class SessionDetailComponentImplTest {

    @Test
    fun init_withProvidedSession_setsContentStateImmediately() = runComponentTest {
        val mockSession = userSessionPrivateMock()
        val context = createSessionDetailComponentTestContext(
            session = mockSession
        )
        try {
            advanceTimeBy(100.milliseconds)
            val state = context.component.state.value
            assertIs<SessionDetailScreenState.Content>(state)
            assertEquals(mockSession, state.session)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun init_withSessionId_fetchesFromUseCase() = runComponentTest {
        val mockSession = userSessionPrivateMock()
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
            val state = context.component.state.value
            assertIs<SessionDetailScreenState.Content>(state)
            assertEquals(mockSession, state.session)
            assertEquals(ONE_CALL, getSessionUseCase.executeCalls)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun init_withSessionIdAndUseCaseError_setsErrorState() = runComponentTest {
        val sessionId = UserSessionId.generate()
        val getSessionUseCase = GetSessionUseCaseMock().apply {
            resultProvider = { AppResult.Error(CommonError.Unknown()) }
        }

        val context = createSessionDetailComponentTestContext(
            session = null,
            sessionId = sessionId,
            getSessionUseCase = getSessionUseCase
        )
        try {
            advanceTimeBy(100.milliseconds)
            val state = context.component.state.value
            assertIs<SessionDetailScreenState.Error>(state)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onRevokeSessionClick_executesUseCase_andTriggersOnBack() = runComponentTest {
        var onBackCalls = 0
        val mockSession = userSessionPrivateMock()
        val deleteSessionUseCase = DeleteSessionUseCaseMock().apply {
            resultProvider = { AppResult.Success(Unit) }
        }

        val context = createSessionDetailComponentTestContext(
            session = mockSession,
            deleteSessionUseCase = deleteSessionUseCase,
            onBack = { onBackCalls++ }
        )
        try {
            advanceTimeBy(100.milliseconds)
            context.component.onRevokeSessionClick()
            advanceTimeBy(100.milliseconds)

            assertEquals(ONE_CALL, deleteSessionUseCase.executeCalls)
            assertEquals(ONE_CALL, onBackCalls)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onUserClick_triggersNavigateToUserDetail() = runComponentTest {
        var navigatedUserId: UserId? = null
        val mockSession = userSessionPrivateMock()

        val context = createSessionDetailComponentTestContext(
            session = mockSession,
            onNavigateToUserDetail = { navigatedUserId = it }
        )
        try {
            advanceTimeBy(100.milliseconds)
            context.component.onUserClick()
            assertEquals(mockSession.userId, navigatedUserId)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onBackClick_triggersOnBack() = runComponentTest {
        var onBackCalls = 0
        val context = createSessionDetailComponentTestContext(
            onBack = { onBackCalls++ }
        )
        try {
            advanceTimeBy(100.milliseconds)
            context.component.onBackClick()
            assertEquals(ONE_CALL, onBackCalls)
        } finally {
            context.destroy()
        }
    }
}

@InternalApi
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
    session: UserSessionPrivate? = userSessionPrivateMock(),
    sessionId: UserSessionId? = session?.id,
    getSessionUseCase: GetSessionUseCaseMock? = null,
    deleteSessionUseCase: DeleteSessionUseCaseMock? = null,
    isCurrentSession: Boolean = false,
    onNavigateToUserDetail: ((UserId) -> Unit)? = null,
    onBack: () -> Unit = {}
): SessionDetailComponentTestContext {
    val lifecycleRegistry = LifecycleRegistry()
    lifecycleRegistry.resume()
    val componentContext = DefaultComponentContext(lifecycleRegistry)
    val targetSessionId = (sessionId ?: session?.id)?.asHexDashString() ?: ""
    val component = SessionDetailComponentImpl(
        componentContext = componentContext,
        params = SessionDetailParams(sessionId = targetSessionId),
        getSessionUseCase = getSessionUseCase ?: session?.let { loaded -> GetSessionUseCaseMock().apply { resultProvider = { AppResult.Success(loaded) } } },
        deleteSessionUseCase = deleteSessionUseCase,
        isCurrentSession = isCurrentSession,
        onNavigateToUserDetail = onNavigateToUserDetail,
        onBack = onBack
    )
    return SessionDetailComponentTestContext(component, lifecycleRegistry)
}

private const val ONE_CALL = 1