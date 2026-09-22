package io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.totp

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
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.user.security.DisableTotpUseCaseMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.user.security.EnableTotpUseCaseMock
import io.github.mudrichenkoevgeny.kmp.feature.user.mock.usecase.user.security.SetupTotpUseCaseMock
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.totp.main.TotpMainComponentImpl
import io.github.mudrichenkoevgeny.kmp.feature.user.ui.screen.profile.totp.main.TotpMainScreenState
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.totprecoverycodes.TotpRecoveryCodes
import io.github.mudrichenkoevgeny.shared.foundation.core.security.domain.model.totpsetup.TotpSetup
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.milliseconds

@InternalApi
class TotpMainComponentImplTest {

    @Test
    fun init_whenUserNull_emitsError() = runComponentTest {
        val userRepository = UserRepositoryMock()
        val context = createTotpMainComponentTestContext(userRepository = userRepository)
        try {
            userRepository.emit(null)
            runCurrent()
            val state = assertIs<TotpMainScreenState.Error>(context.component.state.value)
            assertIs<CommonError.Unknown>(state.error)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun init_whenTotpDisabled_emitsDisabled() = runComponentTest {
        val user = userDetailsMock(isTotpEnabled = false)
        val userRepository = UserRepositoryMock()
        val context = createTotpMainComponentTestContext(userRepository = userRepository)
        try {
            userRepository.emit(user)
            runCurrent()
            assertIs<TotpMainScreenState.Disabled>(context.component.state.value)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun init_whenTotpEnabled_emitsEnabled() = runComponentTest {
        val user = userDetailsMock(isTotpEnabled = true)
        val userRepository = UserRepositoryMock()
        val context = createTotpMainComponentTestContext(
            userRepository = userRepository
        )
        try {
            userRepository.emit(user)
            runCurrent()
            assertIs<TotpMainScreenState.Enabled>(context.component.state.value)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onSetupClick_success_movesToSetupInProgress() = runComponentTest {
        val user = userDetailsMock(isTotpEnabled = false)
        val userRepository = UserRepositoryMock()
        val totpSetup = TotpSetup(secretKey = SECRET_KEY, otpAuthUrl = OTP_AUTH_URL, mfaToken = MFA_TOKEN)
        val setupTotpUseCase = SetupTotpUseCaseMock().apply {
            resultProvider = { AppResult.Success(totpSetup) }
        }
        val context = createTotpMainComponentTestContext(
            userRepository = userRepository,
            setupTotpUseCase = setupTotpUseCase
        )
        try {
            userRepository.emit(user)
            runCurrent()
            context.component.onSetupClick()
            advanceTimeBy(100.milliseconds)

            val state = assertIs<TotpMainScreenState.SetupInProgress>(context.component.state.value)
            assertEquals(totpSetup, state.setup)
            assertEquals(ONE_CALL, setupTotpUseCase.executeCalls)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onSetupClick_error_keepsDisabledWithActionError() = runComponentTest {
        val user = userDetailsMock(isTotpEnabled = false)
        val userRepository = UserRepositoryMock()
        val setupTotpUseCase = SetupTotpUseCaseMock().apply {
            resultProvider = { AppResult.Error(CommonError.Unknown()) }
        }
        val context = createTotpMainComponentTestContext(
            userRepository = userRepository,
            setupTotpUseCase = setupTotpUseCase
        )
        try {
            userRepository.emit(user)
            runCurrent()
            context.component.onSetupClick()
            advanceTimeBy(100.milliseconds)

            val state = assertIs<TotpMainScreenState.Disabled>(context.component.state.value)
            assertFalse(state.actionLoading)
            assertIs<CommonError.Unknown>(state.actionError)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onCodeChanged_updatesCodeAndClearsActionError() = runComponentTest {
        val user = userDetailsMock(isTotpEnabled = false)
        val userRepository = UserRepositoryMock()
        val totpSetup = TotpSetup(secretKey = SECRET_KEY, otpAuthUrl = OTP_AUTH_URL, mfaToken = MFA_TOKEN)
        val setupTotpUseCase = SetupTotpUseCaseMock().apply {
            resultProvider = { AppResult.Success(totpSetup) }
        }
        val context = createTotpMainComponentTestContext(
            userRepository = userRepository,
            setupTotpUseCase = setupTotpUseCase
        )
        try {
            userRepository.emit(user)
            runCurrent()
            context.component.onSetupClick()
            advanceTimeBy(100.milliseconds)

            context.component.onCodeChanged(CONFIRMATION_CODE)
            val state = assertIs<TotpMainScreenState.SetupInProgress>(context.component.state.value)
            assertEquals(CONFIRMATION_CODE, state.code)
            assertNull(state.actionError)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onConfirmSetupClick_success_movesToEnabledAndNavigatesToRecoveryCodes() = runComponentTest {
        val user = userDetailsMock(isTotpEnabled = false)
        val userRepository = UserRepositoryMock()
        val totpSetup = TotpSetup(secretKey = SECRET_KEY, otpAuthUrl = OTP_AUTH_URL, mfaToken = MFA_TOKEN)
        val recoveryCodes = TotpRecoveryCodes(codes = listOf(CODE_ONE, CODE_TWO))
        val setupTotpUseCase = SetupTotpUseCaseMock().apply {
            resultProvider = { AppResult.Success(totpSetup) }
        }
        val enableTotpUseCase = EnableTotpUseCaseMock().apply {
            resultProvider = { _, _ -> AppResult.Success(recoveryCodes) }
        }
        val context = createTotpMainComponentTestContext(
            userRepository = userRepository,
            setupTotpUseCase = setupTotpUseCase,
            enableTotpUseCase = enableTotpUseCase
        )
        try {
            userRepository.emit(user)
            runCurrent()
            context.component.onSetupClick()
            advanceTimeBy(100.milliseconds)

            context.component.onCodeChanged(CONFIRMATION_CODE)
            context.component.onConfirmSetupClick()
            advanceTimeBy(100.milliseconds)

            assertIs<TotpMainScreenState.Enabled>(context.component.state.value)
            assertEquals(ONE_CALL, enableTotpUseCase.executeCalls)
            assertEquals(ONE_CALL, context.onNavigateToRecoveryCodesCalls)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onDisableClick_showsConfirmationDialog() = runComponentTest {
        val user = userDetailsMock(isTotpEnabled = true)
        val userRepository = UserRepositoryMock()
        val context = createTotpMainComponentTestContext(
            userRepository = userRepository
        )
        try {
            userRepository.emit(user)
            runCurrent()

            context.component.onDisableClick()
            runCurrent()

            val state = assertIs<TotpMainScreenState.Enabled>(context.component.state.value)
            assertTrue(state.showDisableConfirmation)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onConfirmDisable_success_movesToDisabledAndRefreshesUser() = runComponentTest {
        val user = userDetailsMock(isTotpEnabled = true)
        val userRepository = UserRepositoryMock()
        val disableTotpUseCase = DisableTotpUseCaseMock().apply {
            resultProvider = { AppResult.Success(Unit) }
        }
        val context = createTotpMainComponentTestContext(
            userRepository = userRepository,
            disableTotpUseCase = disableTotpUseCase
        )
        try {
            userRepository.emit(user)
            runCurrent()

            context.component.onDisableClick()
            context.component.onConfirmDisable()
            advanceTimeBy(100.milliseconds)

            assertIs<TotpMainScreenState.Disabled>(context.component.state.value)
            assertEquals(ONE_CALL, disableTotpUseCase.executeCalls)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onRecoveryCodesClick_navigatesToRecoveryCodes() = runComponentTest {
        val user = userDetailsMock(isTotpEnabled = true)
        val userRepository = UserRepositoryMock()
        val context = createTotpMainComponentTestContext(
            userRepository = userRepository
        )
        try {
            userRepository.emit(user)
            runCurrent()

            context.component.onRecoveryCodesClick()
            assertEquals(ONE_CALL, context.onNavigateToRecoveryCodesCalls)
        } finally {
            context.destroy()
        }
    }

    @Test
    fun onBackClick_invokesOnBack() = runComponentTest {
        val context = createTotpMainComponentTestContext()
        try {
            context.component.onBackClick()
            assertEquals(ONE_CALL, context.onBackCalls)
        } finally {
            context.destroy()
        }
    }

    private fun createTotpMainComponentTestContext(
        userRepository: UserRepositoryMock = UserRepositoryMock(),
        setupTotpUseCase: SetupTotpUseCaseMock = SetupTotpUseCaseMock(),
        enableTotpUseCase: EnableTotpUseCaseMock = EnableTotpUseCaseMock(),
        disableTotpUseCase: DisableTotpUseCaseMock = DisableTotpUseCaseMock()
    ): TotpMainComponentTestContext {
        val lifecycle = LifecycleRegistry()
        lifecycle.resume()

        val context = TotpMainComponentTestContext(
            lifecycle = lifecycle
        )

        context.component = TotpMainComponentImpl(
            componentContext = DefaultComponentContext(lifecycle),
            userRepository = userRepository,
            setupTotpUseCase = setupTotpUseCase,
            enableTotpUseCase = enableTotpUseCase,
            disableTotpUseCase = disableTotpUseCase,
            onNavigateToRecoveryCodes = { context.onNavigateToRecoveryCodesCalls++ },
            onBack = { context.onBackCalls++ }
        )

        return context
    }

    private class TotpMainComponentTestContext(
        val lifecycle: LifecycleRegistry
    ) {
        lateinit var component: TotpMainComponentImpl
        var onNavigateToRecoveryCodesCalls: Int = 0
        var onBackCalls: Int = 0

        fun destroy() = lifecycle.destroy()
    }

    private companion object {
        const val ONE_CALL = 1
        const val CONFIRMATION_CODE = "123456"
        const val SECRET_KEY = "SECRET"
        const val OTP_AUTH_URL = "otpauth://totp/..."
        const val MFA_TOKEN = "mfa-token"
        const val CODE_ONE = "1111-2222"
        const val CODE_TWO = "3333-4444"
    }
}
