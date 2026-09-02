package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.main

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.resume
import io.github.mudrichenkoevgeny.kmp.core.common.error.model.CommonError
import io.github.mudrichenkoevgeny.kmp.core.common.infrastructure.InternalApi
import io.github.mudrichenkoevgeny.kmp.core.common.result.AppResult
import io.github.mudrichenkoevgeny.kmp.core.common.ui.test.runComponentTest
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.domain.model.user.userDetailsMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.repository.user.UserRepositoryMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.session.LogoutUseCaseMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.user.RestoreUserUseCaseMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.user.ScheduleUserDeletionUseCaseMock
import io.github.mudrichenkoevgeny.kmp.feature.user.model.apptype.AppType
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.milliseconds

@InternalApi
class MainProfileComponentImplTest {

    @Test
    fun state_whenUserIsNull_emitsUnauthorized() = runComponentTest {
        val userRepository = UserRepositoryMock()
        val context = createMainProfileComponentTestContext(userRepository = userRepository)
        try {
            userRepository.emit(null)
            runCurrent()
            val state = assertIs<MainProfileScreenState.Unauthorized>(context.component.state.value)
            assertEquals(MainProfileScreenState.Unauthorized, state)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun state_whenUserPresentAndClientApp_emitsContentWithDeletionAvailable() = runComponentTest {
        val userDetails = userDetailsMock()
        val userRepository = UserRepositoryMock()
        val context = createMainProfileComponentTestContext(
            userRepository = userRepository,
            appType = AppType.CLIENT
        )
        try {
            userRepository.emit(userDetails)
            runCurrent()
            val state = assertIs<MainProfileScreenState.Content>(context.component.state.value)
            assertEquals(userDetails, state.user)
            assertTrue(state.isAccountDeletionAvailable)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun state_whenUserPresentAndManagementApp_emitsContentWithDeletionDisabled() = runComponentTest {
        val userDetails = userDetailsMock()
        val userRepository = UserRepositoryMock()
        val context = createMainProfileComponentTestContext(
            userRepository = userRepository,
            appType = AppType.MANAGEMENT
        )
        try {
            userRepository.emit(userDetails)
            runCurrent()
            val state = assertIs<MainProfileScreenState.Content>(context.component.state.value)
            assertEquals(userDetails, state.user)
            assertFalse(state.isAccountDeletionAvailable)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun state_whenUserFlowThrows_emitsError() = runComponentTest {
        val userRepository = UserRepositoryMock().apply {
            currentUserProvider = { flow { throw RuntimeException(RUNTIME_ERROR_MESSAGE) } }
        }
        val context = createMainProfileComponentTestContext(userRepository = userRepository)
        try {
            runCurrent()
            val state = assertIs<MainProfileScreenState.Error>(context.component.state.value)
            assertIs<CommonError.Unknown>(state.error)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onLoginClick_invokesNavigateToLogin() = runComponentTest {
        val context = createMainProfileComponentTestContext()
        try {
            context.component.onLoginClick()
            assertEquals(ONE_CALL, context.onNavigateToLoginCalls)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onTotpSettingsClick_invokesNavigateToTotp() = runComponentTest {
        val context = createMainProfileComponentTestContext()
        try {
            context.component.onTotpSettingsClick()
            assertEquals(ONE_CALL, context.onNavigateToTotpCalls)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onSessionsClick_invokesNavigateToSessions() = runComponentTest {
        val context = createMainProfileComponentTestContext()
        try {
            context.component.onSessionsClick()
            assertEquals(ONE_CALL, context.onNavigateToSessionsCalls)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onIdentifiersClick_invokesNavigateToIdentifiers() = runComponentTest {
        val context = createMainProfileComponentTestContext()
        try {
            context.component.onIdentifiersClick()
            assertEquals(ONE_CALL, context.onNavigateToIdentifiersCalls)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onLogoutClick_showsLogoutConfirmationDialog() = runComponentTest {
        val context = createMainProfileComponentTestContext()
        try {
            context.userRepository.emit(userDetailsMock())
            runCurrent()

            context.component.onLogoutClick()
            runCurrent()

            val state = assertIs<MainProfileScreenState.Content>(context.component.state.value)
            assertTrue(state.showLogoutConfirmation)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onConfirmLogout_executesLogoutUseCase() = runComponentTest {
        val logoutUseCase = LogoutUseCaseMock().apply {
            resultProvider = { AppResult.Success(Unit) }
        }
        val context = createMainProfileComponentTestContext(logoutUseCase = logoutUseCase)
        try {
            context.userRepository.emit(userDetailsMock())
            runCurrent()

            context.component.onConfirmLogout()
            advanceTimeBy(100.milliseconds)

            assertEquals(ONE_CALL, logoutUseCase.executeCalls)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onDeleteAccountClick_showsConfirmationDialog() = runComponentTest {
        val context = createMainProfileComponentTestContext()
        try {
            context.userRepository.emit(userDetailsMock())
            runCurrent()

            context.component.onDeleteAccountClick()
            runCurrent()

            val state = assertIs<MainProfileScreenState.Content>(context.component.state.value)
            assertTrue(state.showDeleteConfirmation)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onConfirmDeleteAccount_executesScheduleUserDeletionUseCase() = runComponentTest {
        val userDetails = userDetailsMock()
        val scheduleUserDeletionUseCase = ScheduleUserDeletionUseCaseMock().apply {
            resultProvider = { AppResult.Success(userDetails) }
        }
        val context = createMainProfileComponentTestContext(scheduleUserDeletionUseCase = scheduleUserDeletionUseCase)
        try {
            context.userRepository.emit(userDetails)
            runCurrent()

            context.component.onConfirmDeleteAccount()
            advanceTimeBy(100.milliseconds)

            assertEquals(ONE_CALL, scheduleUserDeletionUseCase.executeCalls)
            val state = assertIs<MainProfileScreenState.Content>(context.component.state.value)
            assertFalse(state.showDeleteConfirmation)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onRestoreAccountClick_executesRestoreUserUseCase() = runComponentTest {
        val userDetails = userDetailsMock()
        val restoreUserUseCase = RestoreUserUseCaseMock().apply {
            resultProvider = { AppResult.Success(userDetails) }
        }
        val context = createMainProfileComponentTestContext(restoreUserUseCase = restoreUserUseCase)
        try {
            context.userRepository.emit(userDetails)
            runCurrent()

            context.component.onRestoreAccountClick()
            advanceTimeBy(100.milliseconds)

            assertEquals(ONE_CALL, restoreUserUseCase.executeCalls)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onDismissDialog_hidesConfirmationDialog() = runComponentTest {
        val context = createMainProfileComponentTestContext()
        try {
            context.userRepository.emit(userDetailsMock())
            runCurrent()

            context.component.onDeleteAccountClick()
            runCurrent()

            context.component.onDismissDialog()
            runCurrent()

            val state = assertIs<MainProfileScreenState.Content>(context.component.state.value)
            assertFalse(state.showDeleteConfirmation)
        } finally {
            context.destroy()
        }
    }

    private fun createMainProfileComponentTestContext(
        appType: AppType = AppType.CLIENT,
        userRepository: UserRepositoryMock = UserRepositoryMock(),
        logoutUseCase: LogoutUseCaseMock = LogoutUseCaseMock(),
        scheduleUserDeletionUseCase: ScheduleUserDeletionUseCaseMock = ScheduleUserDeletionUseCaseMock(),
        restoreUserUseCase: RestoreUserUseCaseMock = RestoreUserUseCaseMock()
    ): MainProfileComponentTestContext {
        val lifecycle = LifecycleRegistry()
        lifecycle.resume()

        val context = MainProfileComponentTestContext(
            lifecycle = lifecycle,
            userRepository = userRepository
        )

        context.component = MainProfileComponentImpl(
            componentContext = DefaultComponentContext(lifecycle),
            appType = appType,
            userRepository = userRepository,
            logoutUseCase = logoutUseCase,
            scheduleUserDeletionUseCase = scheduleUserDeletionUseCase,
            restoreUserUseCase = restoreUserUseCase,
            onNavigateToLogin = { context.onNavigateToLoginCalls++ },
            onNavigateToTotp = { context.onNavigateToTotpCalls++ },
            onNavigateToSessions = { context.onNavigateToSessionsCalls++ },
            onNavigateToIdentifiers = { context.onNavigateToIdentifiersCalls++ }
        )

        return context
    }

    private class MainProfileComponentTestContext(
        val lifecycle: LifecycleRegistry,
        val userRepository: UserRepositoryMock
    ) {
        lateinit var component: MainProfileComponentImpl
        var onNavigateToLoginCalls: Int = 0
        var onNavigateToTotpCalls: Int = 0
        var onNavigateToSessionsCalls: Int = 0
        var onNavigateToIdentifiersCalls: Int = 0

        fun destroy() = lifecycle.destroy()
    }

    private companion object {
        const val ONE_CALL = 1
        const val RUNTIME_ERROR_MESSAGE = "Flow error"
    }
}