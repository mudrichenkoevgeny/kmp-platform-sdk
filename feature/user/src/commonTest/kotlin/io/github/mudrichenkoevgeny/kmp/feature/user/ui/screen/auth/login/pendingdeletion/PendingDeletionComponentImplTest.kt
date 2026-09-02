package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.auth.login.pendingdeletion

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.resume
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.runComponentTest
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.user.userDetailsMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.session.LogoutUseCaseMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.user.RestoreUserUseCaseMock
import kotlinx.coroutines.test.advanceTimeBy
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.time.Duration.Companion.milliseconds

@InternalApi
class PendingDeletionComponentImplTest {

    @Test
    fun onRestoreAccountClick_success_callsOnRestoreSuccess() = runComponentTest {
        val userDetails = userDetailsMock()
        val restoreUserUseCase = RestoreUserUseCaseMock().apply {
            resultProvider = { AppResult.Success(userDetails) }
        }
        val context = createPendingDeletionComponentTestContext(restoreUserUseCase = restoreUserUseCase)
        try {
            context.component.onRestoreAccountClick()
            advanceTimeBy(100.milliseconds)

            assertEquals(ONE_CALL, restoreUserUseCase.executeCalls)
            assertEquals(ONE_CALL, context.onRestoreSuccessCalls)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onRestoreAccountClick_error_setsActionError() = runComponentTest {
        val restoreUserUseCase = RestoreUserUseCaseMock().apply {
            resultProvider = { AppResult.Error(CommonError.Unknown()) }
        }
        val context = createPendingDeletionComponentTestContext(restoreUserUseCase = restoreUserUseCase)
        try {
            context.component.onRestoreAccountClick()
            advanceTimeBy(100.milliseconds)

            val state = context.component.state.value
            assertFalse(state.actionLoading)
            assertIs<CommonError.Unknown>(state.actionError)
            assertEquals(ZERO_CALLS, context.onRestoreSuccessCalls)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onSignOutClick_executesLogout_andCallsOnSignOut() = runComponentTest {
        val logoutUseCase = LogoutUseCaseMock().apply {
            resultProvider = { AppResult.Success(Unit) }
        }
        val context = createPendingDeletionComponentTestContext(logoutUseCase = logoutUseCase)
        try {
            context.component.onSignOutClick()
            advanceTimeBy(100.milliseconds)

            assertEquals(ONE_CALL, logoutUseCase.executeCalls)
            assertEquals(ONE_CALL, context.onSignOutCalls)
        } finally {
            context.destroy()
        }
    }

    private fun createPendingDeletionComponentTestContext(
        restoreUserUseCase: RestoreUserUseCaseMock = RestoreUserUseCaseMock(),
        logoutUseCase: LogoutUseCaseMock = LogoutUseCaseMock()
    ): PendingDeletionComponentTestContext {
        val lifecycle = LifecycleRegistry()
        lifecycle.resume()

        val context = PendingDeletionComponentTestContext(lifecycle)

        context.component = PendingDeletionComponentImpl(
            componentContext = DefaultComponentContext(lifecycle),
            restoreUserUseCase = restoreUserUseCase,
            logoutUseCase = logoutUseCase,
            onRestoreSuccess = { context.onRestoreSuccessCalls++ },
            onSignOut = { context.onSignOutCalls++ }
        )

        return context
    }

    private class PendingDeletionComponentTestContext(
        val lifecycle: LifecycleRegistry
    ) {
        lateinit var component: PendingDeletionComponentImpl
        var onRestoreSuccessCalls: Int = 0
        var onSignOutCalls: Int = 0

        fun destroy() = lifecycle.destroy()
    }

    private companion object {
        const val ZERO_CALLS = 0
        const val ONE_CALL = 1
    }
}
